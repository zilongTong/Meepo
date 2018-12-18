package com.hunteron.common4s.analyzer.synonym;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class WordNode {

	private Character _selfChar;
	
	private boolean _allowEnd = false;
	
	private Map<Character, WordNode> _subNodes = new HashMap<Character, WordNode>(16, 0.95f);
	
	private String word;
	
	private String synonym [] ;
	private double weight = 1;
	private int type=0;
	//该分词对应的allowKey
	private Set<String> allowKey = new HashSet<String>();
	
	public void addKey(String k) {
		allowKey.add(k);
	}
	
	/**
	 * 只有当k不是“0”，而且allowKey不含k，返回为false
	 * 即，指定了filterKey，而且分词也指定了适合的allowKey
	 * @param k
	 * @return
	 */
	public boolean isAllowKey(String k) {
		//设置-1，表示无效关键字
		if(allowKey.contains("-1")) {
			return false;
		}
		
		if(StringUtils.isBlank(k) || k.equalsIgnoreCase("0")) {
			return true;
		}
		
		//设置了0或者没设置，都表示不限制
		if(allowKey.contains("0") || allowKey.size() == 0) {
			return true;
		}
		
		return allowKey.contains(k.toLowerCase());
	}
	
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public WordNode() {
	}
	public WordNode(Character ch) {
		this._selfChar = ch;
	}

	public Character get_selfChar() {
		return _selfChar;
	}
	public void set_selfChar(Character char1) {
		_selfChar = char1;
	}
	public Map<Character, WordNode> get_subNodes() {
		return _subNodes;
	}
	public void set_subNodes(Map<Character, WordNode> nodes) {
		_subNodes = nodes;
	}
	public WordNode push(Character ch) {
		
		if(_subNodes.get(ch) == null) {
			_subNodes.put(ch, new WordNode(ch));
		}
		return _subNodes.get(ch);
	}
	
	public WordNode get(Character ch) {
		return _subNodes.get(ch);
	}
	public boolean is_allowEnd() {
		return _allowEnd;
	}
	public void set_allowEnd(boolean end) {
		_allowEnd = end;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String[] getSynonym() {
		return synonym;
	}

	public void setSynonym(String synonym[]) {
		this.synonym = synonym;
	}
}
