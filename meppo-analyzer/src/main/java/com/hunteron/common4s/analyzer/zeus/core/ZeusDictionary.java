package com.hunteron.common4s.analyzer.zeus.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.hunteron.common4s.analyzer.zeus.ZeusUtils;
import com.hunteron.common4s.analyzer.zeus.dic.DicSegment;
import com.hunteron.common4s.analyzer.zeus.dic.Hit;
import com.hunteron.common4s.analyzer.zeus.ris.RistrictScan;

/**
 * 带有同义词、可设置跨越值的分词器
 * 细分领域专业分词
 * @author wjyuian
 *
 */
public class ZeusDictionary {

	private DicSegment MAIN_DIC = null;
	
	private static ZeusDictionary INSTANCE = new ZeusDictionary();
	
	private static final String DIC_PATH = "/txt/zeus_dic.txt";
	
	public static void reloadDictionary() {
		INSTANCE = new ZeusDictionary();
	}
	
	public static ZeusDictionary getInstance() {
		if(INSTANCE == null) {
			synchronized (ZeusDictionary.class)	{
				if(INSTANCE == null) {
					INSTANCE = new ZeusDictionary();
				}
			}
		}
		return INSTANCE;
	}
	
	private ZeusDictionary() {
		DicSegment seg = new DicSegment(null);
		MAIN_DIC = seg;
	}
	
	public void initDictionary() {
		if(ZeusUtils.isBlank(DIC_PATH)) {
			return;
		}
		InputStream is = null;
        InputStreamReader isr = null;
		BufferedReader br = null;
		
		try {
			is = ZeusDictionary.class.getResourceAsStream(DIC_PATH);
	        isr = new InputStreamReader(is, "utf-8");
			br = new BufferedReader(isr, 512);
			String line = null;
			while((line = br.readLine()) != null) {
				loadWord(line);
			}
		} catch (IOException e) {
		    
		} finally {
			try {
				if(br != null) {
					br.close();
				}
				if(isr != null) {
					isr.close();
				}
				if(is != null) {
					is.close();
				}
			} catch (Exception e2) {
				
			}
		}
	}
	
	
	public void loadWord(String word) {
		if(word == null) {
			return;
		}
		if(word.indexOf('#') == 0 || word.length() < 1) {
			return;
		}
		
		int valueIndex = -1;
		String value = "";
		if((valueIndex = word.indexOf("::=")) > 0) {
		    //指定了关键字对应的映射值
		    value = word.substring(valueIndex + 3);
		    word = word.substring(0, valueIndex);
		}
		
		List<RistrictScan> scans = RistrictScan.create(word);
		if(scans == null || scans.size() < 1) {
			return;
		}
		for(RistrictScan scan : scans) {
			//有效的格式定义
			if(scan != null && scan.isValid()) {
				//解析之后的关键字，字符串转为小写
				String newWord = scan.getWord().trim();
				newWord = newWord.toLowerCase();
				int ristrictWordIndex = scan.getRistrictNextIndex();
				DicSegment current = MAIN_DIC;
				for(int i = 0, len = newWord.length(); i < len; i ++) {
					Character ch = newWord.charAt(i);
					DicSegment segment;
					if(i == ristrictWordIndex) {
						segment = new DicSegment(ch, scan.getRistrict());
						
						current.setNextRistrictMaxSpan(scan.getRistrict());
					} else {
						segment = new DicSegment(ch);
					}
					current = current.addChildrenDicSegment(segment);
				}
				current.setCanBeEnd();
				current.setEndWord(newWord);
				current.addKey(scan.getKey());
				current.setValue(value);
			}
		}
	}
	
	public Hit matchWithHit(char[] chars, int currentCursor, Hit hit) {
		DicSegment seg = hit.getSegment();
		
		return seg.matchArray(chars, currentCursor, 1, hit);
	}

	public Hit matchWithMainDic(char[] chars, int currentCursor) {
		
		return getRoot().matchArray(chars, currentCursor, 1, null);
	}
	
	public DicSegment getRoot() {
		return MAIN_DIC;
	}
}
