package org.analyzer.dog.rs;

import java.io.Serializable;

/**
 *
 * @author Leo
 * @version 2015-9-17
 * 关键字的分词结果
 */
public class HOTermCount implements Serializable {

	private static final long serialVersionUID = -1659927459306112885L;
	//关键字
	private String word;
	//次数
	private int count;
	
	public HOTermCount() {
		
	}
	
	public HOTermCount(String word, int count) {
		super();
		this.word = word;
		this.count = count;
	}

	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}
