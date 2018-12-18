package org.analyzer.dog.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Leo
 * @version 2015-9-15
 * 词典树节点
 */
public class HONode {
	//当前节点字符
	private Character self = null;
	//是否是分词词尾
	private boolean end = false;
	//节点的下一个节点
	private Map<Character, HONode> subNodes = new HashMap<Character, HONode>();
	/**
	 * 节点内容；
	 * skillMark取值name||word；
	 * skillWord取值name||word；
	 * tagWord取值name||word
	 */
	private String word;
	//关键字权重
	private int weight;
	//词典类型：skillMark；skillWord；tagWord
	private int dicType;
	//领域类型
	private int fieldType;
	
	private Set<Character> stopCharSet = new HashSet<Character>(0);
	
	public void addStopChar(Character ch) {
		stopCharSet.add(ch);
	}
	public boolean isThisNodeStopThisChar(Character ch) {
		if(stopCharSet.size() < 1) {
			return false;
		}
		return stopCharSet.contains(ch);
	}
	
	public HONode(Character ch) {
		this.self = ch;
	}
	public Character getSelf() {
		return self;
	}
	public void setSelf(Character self) {
		this.self = self;
	}
	public boolean isEnd() {
		return end;
	}
	public void setEnd(boolean end) {
		this.end = end;
	}
	public Map<Character, HONode> getSubNodes() {
		return subNodes;
	}
	public void setSubNodes(Map<Character, HONode> subNodes) {
		this.subNodes = subNodes;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public int getDicType() {
		return dicType;
	}
	public void setDicType(int dicType) {
		this.dicType = dicType;
	}
	public int getFieldType() {
		return fieldType;
	}
	public void setFieldType(int fieldType) {
		this.fieldType = fieldType;
	}
	
	public HONode push(Character ch) {
		HONode node = null;
		if((node = subNodes.get(ch)) == null) {
			node = new HONode(ch);
			subNodes.put(ch, node);
		}
		return node;
	}
	public HONode get(Character ch) {
		return subNodes.get(ch);
	}
}
