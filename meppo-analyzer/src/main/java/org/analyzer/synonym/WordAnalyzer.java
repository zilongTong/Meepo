package org.analyzer.synonym;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.analyzer.sdk.StringTools;
import org.apache.commons.lang.StringUtils;

import org.analyzer.meppo.AnalyzerUtils;







public class WordAnalyzer {

	//基础词库路径
	public static final String DICT_PATH = "/txt/synonym1.dic";
//	
	//public static final String DICT_PATH = "D:/city.txt";
	//词库树根节点
	private WordNode _root;
	
	private volatile boolean isInitDefault = false;
	public WordAnalyzer() {
		//System.out.println("..."+isInitDefault);
		_root = new WordNode();
//		initDictMap();
	}

	public WordAnalyzer(String[] kvs) {
		_root = new WordNode();
		for(String kv : kvs) {
			loadExternalWeightKey(kv);
		}
	}
	public void loadExternalWeightKey(String word) {
		if(StringUtils.isBlank(word)) {
			return;
		}
		word = word.toLowerCase();
		double weight = 1;
		if(word.indexOf(":") > 0) {
			String[] vals = word.split(":");
			word = vals[0];

			weight = StringTools.getDbl(vals[1], 1d);
		}
		WordNode temp = null;
		temp = _root;
		for(int i = 0; i < word.length(); i ++) {
			Character ch = word.charAt(i);
			//添加词库单字
			temp = temp.push(ch);
		}
		temp.set_allowEnd(true);
		temp.setWeight(weight);
	}
	
	
	public WordNode get_root() {
		return _root;
	}

	public void set_root(WordNode _root) {
		this._root = _root;
	}

	public synchronized void initDictMap() {
		try {
			if(!isInitDefault) {
				isInitDefault = true;
				initDictMap(WordAnalyzer.DICT_PATH);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 初始化词库树
	 * @throws IOException
	 */
	public void initDictMap(String dicPath) throws IOException {
		if(StringUtils.isBlank(dicPath)) {
			return;
		}
		InputStream is = null;
        InputStreamReader isr = null;
		BufferedReader br = null;
		
		try {
			is = WordAnalyzer.class.getResourceAsStream(dicPath);
	        isr = new InputStreamReader(is, "utf-8");
			br = new BufferedReader(isr, 512);
			String line = null;
			while((line = br.readLine()) != null) {
				loadExternal(line);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if(br != null) {
				br.close();
			}
			if(isr != null) {
				isr.close();
			}
			if(is != null) {
				is.close();
			}
		}
	}
	
	/**
	 * 加载外部分词；
	 * @param word	分词内容:allowKey1[,allowKey2,...,allowKeyn]		
	 * 例如		java:1001,1002		表示，只有1001和1002对应的文本需要关注java这个分词
	 */
	public void loadExternal(String word) {
		if(StringUtils.isBlank(word) || word.indexOf(",") < 0) {
			return;
		}
		word = word.toLowerCase();
		String [] w = word.split(",");
		WordNode temp = null;
		
		for(String s : w){
			temp = _root;
			for(int i = 0; i < s.length(); i ++) {
				Character ch = s.charAt(i);
				//添加词库单字
				temp = temp.push(ch);
			}
			temp.set_allowEnd(true);
			temp.setSynonym(w);
		}		
		
	}

    public List<String> analyzeSynonmy(String word){
    	if  (StringUtils.isBlank(word)){
    		return new ArrayList<>();
    	}
    	List<String []> rs = new ArrayList<>();
		word = word.toLowerCase();
		int index = 0,
				len = word.length();
		String pre = "";
		WordNode current = null;
		while(index < len){
			char ch = word.charAt(index);
			current = _root.get(ch);
			if  (current == null){
				pre += ch;
				index ++ ;
				continue;
			}
			int tmp = 0;
			String [] synonym = null;
			String temp = "";
			for(;current != null;){
				temp += current.get_selfChar();
				//可能的成词
				if  (current.is_allowEnd()){
					//首字符是字母或数字，且 前一个字符是数字或字母
					//或者  尾字符是字母或数字，且 后一个字符是数字或字母
					//则认为不成词
					Character prev = null;
					Character suff = null;
					if  (index - temp.length() >= 0){
						prev = word.charAt(index - temp.length());
					}
					if  (index + 1 < len){
						suff = word.charAt(index + 1);
					}
					if (AnalyzerUtils.canBeFinalWord(prev, temp, suff)){
						tmp = index;
						synonym = current.getSynonym();
					}
					
				}
				index ++;
				if  (index >= len){
					break;
				}
				ch = word.charAt(index);
				current = current.get(ch);
			}
			if  (synonym != null){
				index = tmp + 1;
				if  (pre.length() > 0){
					rs.add(new String []{pre});
					pre = "";
				}
				rs.add(synonym);
			}else{
				pre += temp.charAt(0);
				index = index -(temp.length() -1);
			}
			
		}
		if  (pre.length() > 0){
			rs.add(new String []{pre});
			pre = "";
		}
		int size = rs.size();
		String result [] = new String[size]; 
		list = new ArrayList<>();
		queen(rs, 0, size, result);
		return list;
    }

    
	private String show(String[] result) {
		String s = "";
		for(String ss : result){
			s += ss;
		}
		//System.out.println(s);
		return s;
	}
	private void queen(List<String []> rs, int i, int size, String result []){
		if (i >= size){
			list.add(show(result));
			return;
		}
		String []ss = rs.get(i);
		for(String s : ss){
			result[i] = s;
			queen(rs, ++i, size, result);
			i--;
		}
	}
	private List<String> list = null;
	
	public String [] getSynonmy(String word){
		if (StringUtils.isBlank(word)){
			return new String[]{};
		}
		WordNode current = _root;
		for(int i = 0; i < word.length(); i++){
			char c = word.charAt(i);
			current = current.get(c);
			if (current == null){
				return new String[]{word};
			}
		}
		if (current.is_allowEnd()){
			return current.getSynonym();
		}
		return new String[]{word};
	}
}
