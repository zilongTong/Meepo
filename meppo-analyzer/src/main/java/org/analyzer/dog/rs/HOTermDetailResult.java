package org.analyzer.dog.rs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


/**
 *
 * @author Smile.Wu
 * @version 2015-9-16
 * 初步分词结果，记录关键字和次数；记录关键字位置信息
 */
public class HOTermDetailResult implements Serializable {

	private static final long serialVersionUID = 6407991421615679L;
	//关键字和次数
	private Map<String, Integer> wordCountMap = null;
	//技能型关键字标志信息
	private List<HOSkillMark> marks = null;
	
	private List<HOTermCount> termCountList = null;
	
	private int totalCount = 0;
	
	public HOTermDetailResult() {
		wordCountMap = new LinkedHashMap<String, Integer>();
		marks = new ArrayList<HOSkillMark>();
	}
	public void addWord(String word, int index) {
		if(StringUtils.isNotBlank(word)) {
			Integer oldCount = wordCountMap.get(word);
			if(oldCount == null) {
				oldCount = 0;
			}
			wordCountMap.put(word, oldCount + 1);
		}
		
		marks.add(new HOSkillMark(word, index));
	}
	public Map<String, Integer> getWordCountMap() {
		return wordCountMap;
	}
	/**
	 * 转换为结果片段
	 * @param segName
	 * @return
	 */
	public HOResultSegment convertToSeg(String segName) {
		return convertToSeg(segName, 15);
	}

	public HOResultSegment convertToSeg(String segName, int maxNum) {
		HOResultSegment seg = new HOResultSegment(segName, "");

		termCountList = new ArrayList<HOTermCount>();
		for(Map.Entry<String, Integer> en : wordCountMap.entrySet()) {
			termCountList.add(new HOTermCount(en.getKey(), en.getValue()));
			totalCount +=  en.getValue();
		}

		Collections.sort(termCountList, new Comparator<HOTermCount>() {

			@Override
			public int compare(HOTermCount o1, HOTermCount o2) {
				if(o1.getCount() == o2.getCount()) {
					return 0;
				}
				if(o1.getCount() > o2.getCount()) {
					return -1;
				}
				return 1;
			}
		});
		
		int count = 0;
		for(HOTermCount term : termCountList) {
			count ++;
			seg.addWord(term.getWord());
			if(count >= maxNum) {
				break;
			}
		}
		
		return seg;
	}
	
	public boolean hasData() {
		return wordCountMap.size() > 0;
	}
	public List<HOSkillMark> getMarks() {
		return marks;
	}
	public List<HOTermCount> getTermCountList() {
		return termCountList;
	}
	public int getTotalCount() {
		return totalCount;
	}
}
