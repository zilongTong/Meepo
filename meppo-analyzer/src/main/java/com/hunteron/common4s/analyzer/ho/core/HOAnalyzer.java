package com.hunteron.common4s.analyzer.ho.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hunteron.common4s.analyzer.ho.core.skillprocess.DefaultHOSkillMarkProcessor;
import com.hunteron.common4s.analyzer.ho.core.skillprocess.IHOSkillMarkPicker;
import com.hunteron.common4s.analyzer.ho.core.skillprocess.mid.HOSkillMarkResult;
import com.hunteron.common4s.analyzer.ho.dic.HODicTerm;
import com.hunteron.common4s.analyzer.ho.dic.TermType;
import com.hunteron.common4s.analyzer.ho.rs.HOResult;
import com.hunteron.common4s.analyzer.ho.rs.HOSkillMark;
import com.hunteron.common4s.analyzer.ho.rs.HOTermDetailResult;
import com.hunteron.common4s.analyzer.ho.similarity.DefaultSimilarity;
import com.hunteron.common4s.analyzer.ho.similarity.IHOSimilarity;
import com.hunteron.common4s.analyzer.ho.util.HORecoAnalyzerUtils;



/**
 *
 * @author Smile.Wu
 * @version 2015-9-15
 */
public class HOAnalyzer {
	//标签字典树
	private HONode tag_word_root = null;
	//技能标志字典树
	private HONode skill_mark_root = null;
	//技能分词字典树
	private HONode skill_word_root = null;
	
	public HOAnalyzer() {
		tag_word_root = new HONode(null);
		skill_word_root = new HONode(null);
		skill_mark_root = new HONode(null);
		similarity = new DefaultSimilarity();
	}
	
	public double matchPercent(HOResult result1, HOResult result2) {
		return similarity.similarity(result1, result2);
	}
	public void setVerbose(boolean verbose) {
		this.similarity.setVerbose(verbose);
	}
	/**
	 * 加载词典
	 * @param term
	 */
	public void loadTerm(HODicTerm term) {
		switch (term.getDicType()) {
		case TermType.TAG_WORD :
			loadTagWordTerm(term);
			break;
		case TermType.SKILL_MARK :
			loadSkillMarkTerm(term);
			break;
		case TermType.SKILL_WORD :
			loadSkillWordTerm(term);
			break;
		case TermType.STOP_WORD : 
			loadNodeStopChar(term);
			break;
		}
	}
	
	public void loadNodeStopChar(HODicTerm term) {
		String word = term.getWord();
		HONode temp = null;
		temp = skill_word_root;
		for(int i = 0; i < word.length(); i ++) {
			Character ch = word.charAt(i);
			//添加词库单字
			temp = temp.push(ch);
		}
		String stopString = term.getName();
		for(int i = 0, len = stopString.length(); i < len; i ++) {
			temp.addStopChar(stopString.charAt(i));
		}
	}
	
	public void loadSkillMarkTerm(HODicTerm term) {
		loadTerm(skill_mark_root, term);
	}
	public void loadSkillWordTerm(HODicTerm term) {
		loadTerm(skill_word_root, term);
	}
	public void loadTagWordTerm(HODicTerm term) {
		loadTerm(tag_word_root, term);
	}
	private void loadTerm(HONode root, HODicTerm term) {
		if(HORecoAnalyzerUtils.isNull(term)) {
			return;
		}
		String word = term.getWord();
		HONode temp = null;
		temp = root;
		for(int i = 0; i < word.length(); i ++) {
			Character ch = word.charAt(i);
			//添加词库单字
			temp = temp.push(ch);
		}
		temp.setEnd(true);
		
		//节点对应的分词结果，可以是原词，也可以是同义词
		temp.setWord(term.getName());
		temp.setDicType(term.getDicType());
		temp.setFieldType(term.getFieldType());
	}
	
	//技能标志关键字对应文本的提取器
	private IHOSkillMarkPicker markPicker = new DefaultHOSkillMarkProcessor();
	
	private IHOSimilarity similarity = null;

	
	/**
	 * 分析
	 * @param text
	 * @return
	 */
	public HOResult analyzePositionText(String text) {
		return analyzeText(text, markPicker, null);
	}
	public HOResult analyzePositionText(String text, String mustString) {
		return analyzeText(text, markPicker, mustString);
	}
	public HOResult analyzePositionText(String text, IHOSkillMarkPicker picker) {
		return analyzeText(text, picker, null);
	}
	public HOTermDetailResult analyzeMustString(String text) {
		return analyzeTextWithRootNode(skill_word_root, text);
	}
	public HOResult analyzeTalentText(String text) {
		HOResult result = analyzeTalentText(text, markPicker);
		return result;
	}
	public HOResult analyzeTalentText(String text, IHOSkillMarkPicker picker) {
		
		HOResult result = analyzeText(text, picker, null);

		List<HOSkillMark> markList = createSkillMarkList(text);
		String selfCondition = "";
		int textLength = text.length();
		for(HOSkillMark mark : markList) {
			if(mark.getName().equalsIgnoreCase(HORecoAnalyzerUtils.SELF_CONDITION)) {
				selfCondition = text.substring(mark.getIndex(), Math.min(textLength, mark.getIndex() + 300));
			}
		}
		
		if(StringUtils.isNotBlank(selfCondition)) {
			HOTermDetailResult hoTermDetailResult = analyzeTextWithRootNode(skill_word_root, selfCondition);
			result.smartResetJustForTalent(hoTermDetailResult.getMarks());
		} else {
			result.smartResetJustForTalent();
		}
		
		return result;
	}
	/**
	 * 指定提取器的分析
	 * @param text
	 * @return
	 */
	public HOResult analyzeText(String text, IHOSkillMarkPicker picker, String mustString) {
		HOResult hoResult = new HOResult();
		
		if(StringUtils.isBlank(text)) {
			return hoResult;
		}
		
		/*
		 * 标签型分词
		 * 获得标签型分词的分词结果，数量
		 */
		HOTermDetailResult tagWordMapper = analyzeTextWithRootNode(tag_word_root, text);
		//标签型分词结果不再处理，直接转换为结果段
		hoResult.setTagWordSegment(tagWordMapper.convertToSeg(HORecoAnalyzerUtils.TAG_WORD));
		
		//先进行一次全文的技能分词
		HOTermDetailResult hoTermDetailResult = analyzeTextWithRootNode(skill_word_root, text);
		hoResult.setAllSkillWordSegment(hoTermDetailResult.convertToSeg(HORecoAnalyzerUtils.ALL_SKILL, 50));
		hoResult.setAllSkillWordResult(hoTermDetailResult);
		
		//文本分段，按照技能标志关键字分段
		List<HOSkillMark> markList = createSkillMarkList(text);
		
		//根据技能标志信息，进行文本切割
		HOSkillMarkResult markResult = picker.caculate(markList, text);
		if(mustString != null) {
			markResult.addSubText(HORecoAnalyzerUtils.MUST, mustString);
		}
		
		//分段后分别分词
		//组合结果
		if(markResult != null) {
			for(Map.Entry<String, String> en : markResult.getSkillMap().entrySet()) {
				String skillMarkName = en.getKey();
				String subText = en.getValue();
				//切割后的文本进行技能分词
				HOTermDetailResult skillTermMapper = analyzeTextWithRootNode(skill_word_root, subText);
				if(skillTermMapper.hasData()) {
					for(String skillWord : skillTermMapper.getWordCountMap().keySet()) {
						hoResult.addSkillWord(skillMarkName, skillWord);
					}
				}
			}
		}
		
		return hoResult;
	}
	
	/*
	 * 用技能标志分词字典树，来生成技能标志已经对应文本
	 */
	private List<HOSkillMark> createSkillMarkList(String text) {
		HOTermDetailResult mapper = analyzeTextWithRootNode(skill_mark_root, text);
		if(mapper.hasData()) {
			return mapper.getMarks();
		}
		return new ArrayList<HOSkillMark>(0);
	}
	
	/*
	 * 用指定的分词根节点，将对应的文本进行切词提取
	 */
	private HOTermDetailResult analyzeTextWithRootNode(HONode root, String text) {
		HOTermDetailResult resultMap = new HOTermDetailResult();
		
		if(text == null || text.length() < 2) {
			return resultMap;
		}

		text = text.replaceAll("\\s+", " ");
		text = text.toLowerCase();
		
		int mainIndex = 0;	//外层指针
		int termIndex = 0;	//内层指针，即分词指针
		int textLength = text.length();
		HONode currentTermNode = null;
		
		//保存最近一个可结尾的节点，保证最大分词
		HONode maxCanBeEndNode = null;
		for(; mainIndex < textLength;) {
			
			//当前字符
			Character currentChar = text.charAt(mainIndex);
			
			//是否存在与字典树中
			if((currentTermNode = root.get(currentChar)) == null) {
				 mainIndex ++;
				//无法成词
				continue;
			}
			int tempTermIndex = -1;

			if(currentTermNode.isEnd()) {
				tempTermIndex = mainIndex;
				maxCanBeEndNode = currentTermNode;
			}
			
			//有成词的机会
			termIndex = mainIndex;
			if(termIndex >= textLength) {//超出
				break;
			}
			
			while(termIndex < textLength - 1) {
				termIndex ++;
				currentChar = text.charAt(termIndex);
				//下一个字符节点
				HONode termNextCharNode = currentTermNode.get(currentChar);
				if(termNextCharNode == null) {
					break;
				}
				currentTermNode = termNextCharNode;

				if(currentTermNode.isEnd()) {
					tempTermIndex = termIndex;
					maxCanBeEndNode = currentTermNode;
				}
			}
			//词尾
			if(maxCanBeEndNode != null) {
				String canBeWord = maxCanBeEndNode.getWord();
				Character prev = null;
				if(mainIndex > 0) {
					prev = text.charAt(mainIndex-1);
				}
				//如果当前的关键字最后的字符是英文，则 currentChar 不能为英文，否则不成词
				currentChar = ' ';
				if(tempTermIndex >= 0 && tempTermIndex < textLength - 1) {
					currentChar = text.charAt(tempTermIndex + 1);
				}

				mainIndex ++;
				//针对非技能标志词，需要进行是否可成词判断
				if(maxCanBeEndNode.getDicType() == TermType.SKILL_MARK || (canBeFinalWord(prev, canBeWord, currentChar) && 
						!maxCanBeEndNode.isThisNodeStopThisChar(currentChar))) {
					resultMap.addWord(canBeWord, mainIndex);
					
					mainIndex = termIndex;

					if(mainIndex >= textLength - 1) {
						break;
					}
				}
				maxCanBeEndNode = null;
			} else {

				mainIndex ++;
			}
		}
		
		return resultMap;
	}
	
	//是否可成词判断
		private boolean canBeFinalWord(Character prev, String word, Character ch) {
			if(StringUtils.isBlank(word)) {
				return false;
			}
//			//如果当前可成词分词的最后一个字不是英文字母，则成词
//			if(!HOAnalyzerUtils.isLetter(word.charAt(word.length() - 1))) {
//				return true;
//			}
//			//当前是英文，则前面不能是英文或者数字
//			if(StringUtils.isAlpha(word) && (prev != null && (HOAnalyzerUtils.isLetter(prev) || StringUtils.isNumeric(prev+"")))) {
//				return false;
//			}
//			
////			//如果类似这种情况比较多，可以统一配置在词典中，加载的字典树节点中设置停用字符集
////			if(word.equalsIgnoreCase("http") && ch == ':') {
////				return false;
////			}
//			
//			//当前是英文，则后面不能是英文或者'-'
//			if(HOAnalyzerUtils.isLetter(ch) || ch == '-') {
//				return false;
//			}
			
			char wordFirstChar = word.charAt(0);
			if(HORecoAnalyzerUtils.isLetter(wordFirstChar) || HORecoAnalyzerUtils.isNumber(wordFirstChar)) {
				//如果关键字首字是数字，英文
				if(prev != null && (HORecoAnalyzerUtils.isLetter(prev) || HORecoAnalyzerUtils.isNumber(prev))) {
					return false;
				}
			}

			char wordEndChar = word.charAt(word.length() - 1);
			if(HORecoAnalyzerUtils.isLetter(wordEndChar) || HORecoAnalyzerUtils.isNumber(wordEndChar)) {
				//如果关键字首字是数字，英文
				if(ch != null && (HORecoAnalyzerUtils.isLetter(ch) || HORecoAnalyzerUtils.isNumber(ch))) {
					return false;
				}
			}
			
			return true;
		}

}
