package com.hunteron.common4s.analyzer.ho.dic;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Smile.Wu
 * @version 2015-9-15
 * <pre>
 * 词典类，画像的分词单元
 * 
 * dicType:word:name
 * 
 * skillMark
 * 	2:精通:master
 *  2:熟练:familiar
 *  2:熟悉:familiar
 * 
 * skillWord
 *  1:java:java
 *  1:php
 *  1:lucene
 * 
 * tagWord
 *  0:领导力
 *  0:执行力
 *  0:大数据
 *  </pre>
 */
public class HODicTerm {
	//关键字
	private String word;
	//词典关键字类型
	private int dicType;
	//名称；skillMark：标志的别称；skillWord：技能词的同义词；tagWord：标签词的同义词；默认取值word
	private String name;
	//领域类型：技术领域
	private int fieldType;

	public HODicTerm(String word, int dicType) {
		super();
		if(StringUtils.isBlank(word)) {
			throw new RuntimeException("argument of HODicTerm constructor [word] is null");
		}
		this.word = word.toLowerCase().trim();
		this.dicType = dicType;
		this.name = word;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getDicType() {
		return dicType;
	}

	public void setDicType(int dicType) {
		this.dicType = dicType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getFieldType() {
		return fieldType;
	}

	public void setFieldType(int fieldType) {
		this.fieldType = fieldType;
	}
}
