package org.analyzer.dog.rs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import org.analyzer.dog.util.HORecoAnalyzerUtils;


/**
 *
 * @author Smile.Wu
 * @version 2015-9-15
 * 
 * 文本分析结果
 */
public class HOResult implements Serializable {

	private static final long serialVersionUID = -713886585997484990L;

	//有标志的技能型分词结果
	private Map<String, HOResultSegment> skillMap = new HashMap<String, HOResultSegment>();
	
	//所有技能分词结果
	private HOResultSegment allSkillWordSegment = new HOResultSegment("", "");
	
	//标签型分词结果；返回结果中的标签，按照次数降序
	private HOResultSegment tagWordSegment = new HOResultSegment("", "");
	
	//所有技能分词结果，主要是当解析人才的技能时，需要按照关键字次数进行层次分割；保留了关键字的次数信息
	private HOTermDetailResult allSkillWordResult = new HOTermDetailResult();
	
	public HOResult() {
		
	}
	/**
	 * 从文本解析出来的关键字总数，数量太少，可以设定为不予计算
	 * @return
	 */
	public int resultWordCount() {
		int total = 0;
		total += allSkillWordSegment.getWordList().size();
		total += tagWordSegment.getWordList().size();
		return total;
	}
	
	public List<String> getSegmentStringListByName(String... names) {
		List<String> result = new ArrayList<String>();
		for(String name : names) {
			HOResultSegment seg = skillMap.get(name);
			if(seg != null) {
				result.addAll(seg.getWordList());
			}
		}
		return result;
	}
	
	/**
	 * 结果集中添加技能分词结果
	 * @param name
	 * @param word
	 */
	public void addSkillWord(String name, String word) {
		HOResultSegment seg = skillMap.get(name);
		if(seg != null) {
			seg.addWord(word);
		} else {
			skillMap.put(name, new HOResultSegment(name, word));
		}
	}
	/**
	 * 添加标签的分词结果
	 * @param word
	 */
	public void addTagWord(String word) {
		if(tagWordSegment == null) {
			tagWordSegment = new HOResultSegment("tagWord", word);
		} else {
			tagWordSegment.addWord(word);
		}
	}

	public Map<String, HOResultSegment> getSkillMap() {
		return skillMap;
	}

	public HOResultSegment getTagWordSegment() {
		return tagWordSegment;
	}

	public void setAllSkillWordResult(HOTermDetailResult allSkillWordResult) {
		this.allSkillWordResult = allSkillWordResult;
	}

	public void setTagWordSegment(HOResultSegment tagWordSegment) {
		this.tagWordSegment = tagWordSegment;
	}

	public HOResultSegment getAllSkillWordSegment() {
		return allSkillWordSegment;
	}

	public void setAllSkillWordSegment(HOResultSegment allSkillWordSegment) {
		this.allSkillWordSegment = allSkillWordSegment;
	}
	
	public HOTermDetailResult getAllSkillWordResult() {
		return allSkillWordResult;
	}
	
	
	public void smartResetJustForTalent(List<HOSkillMark> marks) {
		if(CollectionUtils.isEmpty(marks)) {
			smartResetJustForTalent();
		} else {
			List<String> skillWordList = new ArrayList<String>();
			for(HOSkillMark mark : marks) {
				String word = mark.getName();
				if(skillWordList.contains(word)) {
					continue;
				}
				skillWordList.add(word);
			}
			
			int size = skillWordList.size();
			int masterSize = (int) (size * 0.3);
			
			if(size > 3 && masterSize > 0) {
				resetSkillMap(HORecoAnalyzerUtils.MASTER, skillWordList.subList(0, masterSize));
				int familiar = (int) (size * 0.6);
				resetSkillMap(HORecoAnalyzerUtils.FAMILIAR, skillWordList.subList(masterSize, familiar));
				resetSkillMap(HORecoAnalyzerUtils.UNDERSTANDING, skillWordList.subList(familiar, size));
			} else {
				resetSkillMap(HORecoAnalyzerUtils.MASTER, skillWordList);
			}
		}
	}
	
	private void resetSkillMap(String name, List<String> words) {
		if(CollectionUtils.isEmpty(words)) {
			return;
		}
		HOResultSegment masterSeg = skillMap.get(name);
		if(masterSeg == null || !masterSeg.hasWord()) {
			skillMap.put(name, new HOResultSegment(name, words));
		} 
//		else {
//			for(String word : words) {
//				masterSeg.addWord(word);
//			}
//		}
	}
	
	/**
	 * 对于人才的简历，可以进行一个技能词分层
	 */
	public void smartResetJustForTalent() {
		List<HOTermCount> terms = allSkillWordResult.getTermCountList();
		float totalCount = allSkillWordResult.getTotalCount();
		
		float wordNum = terms.size();
		float averageCount = totalCount / wordNum;
		float familiarCount = (float) (averageCount * 0.8);
		
		List<String> masterList = new ArrayList<String>();
		List<String> familiarList = new ArrayList<String>();
		List<String> understandingList = new ArrayList<String>();
		for(HOTermCount term : terms) {
			int c = term.getCount();
			String word = term.getWord();
			if(c > averageCount * 2) {
				masterList.add(word);
			} else if(c > familiarCount) {
				familiarList.add(word);
			} else {
				understandingList.add(word);
			}
//			System.out.println(word + " : " + c);
		}
		
		mergeSegmentByName(HORecoAnalyzerUtils.MASTER, masterList);
		if(CollectionUtils.isEmpty(understandingList) && CollectionUtils.isNotEmpty(familiarList)) {
			int familiarSize = familiarList.size();
			int underSize = (int) (familiarSize * 0.4);
			understandingList = familiarList.subList(familiarSize - underSize, familiarSize);
			familiarList = familiarList.subList(0, familiarSize - underSize);
		}

		if(mergeSegmentByName(HORecoAnalyzerUtils.FAMILIAR, familiarList)) {
			mergeSegmentByName(HORecoAnalyzerUtils.UNDERSTANDING, understandingList);
		} else {
			mergeSegmentByName(HORecoAnalyzerUtils.UNDERSTANDING, familiarList);
		}
		

	}
	
	public boolean mergeSegmentByName(String name, List<String> newList) {
		HOResultSegment old = skillMap.get(name);
		if(old == null) {
			skillMap.put(name, new HOResultSegment(name, newList));
			return true;
		} 
		return false;
//		else {
//			for(String word : newList) {
//				old.addWord(word);
//			}
//		}
	}
}
