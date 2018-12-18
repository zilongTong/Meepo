package org.analyzer.dog.rs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;


/**
 *
 * @author Smile.Wu
 * @version 2015-9-15
 * 分词结果
 * name：当前分词属于技能型还是标签型；标签型的name就是tagWord；技能型的，name就是词典载入的时候指定的
 */
public class HOResultSegment implements Serializable {
	
	private static final long serialVersionUID = -3364138562290719034L;
	//分词结果名称；skillMark里面的级别；tagWord
	private String name;
	//分词关键字
	private List<String> wordList = new ArrayList<String>();
	
	public HOResultSegment(String name, String word) {
		this.name = name;
		addWord(word);
	}
	
	public HOResultSegment(String name, List<String> wordList) {
		this.name = name;
		this.wordList = wordList;
	}

	public boolean hasWord() {
		return CollectionUtils.isNotEmpty(wordList);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getWordList() {
		return wordList;
	}
	public void addWord(String word) {
		if(StringUtils.isNotBlank(word) && 
				!wordList.contains(word)) {
			wordList.add(word);
		}
	}
}
