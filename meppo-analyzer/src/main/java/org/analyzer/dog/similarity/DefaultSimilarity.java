package org.analyzer.dog.similarity;

import java.util.List;

import org.analyzer.dog.rs.HOResult;
import org.analyzer.dog.util.HORecoAnalyzerUtils;
import org.apache.commons.collections.CollectionUtils;


/**
 *
 * @author Leo
 * @version 2015-9-17
 */
public class DefaultSimilarity extends HOSimilarity {
	boolean verbose = false;
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	@Override
	public double similarity(HOResult talent, HOResult position) {
		double totalMatch = 0;
		if(verbose) {
			System.out.println("\ttalent word size : " + talent.resultWordCount());
			System.out.println("\tposition word size : " + position.resultWordCount());
		}
		
		double markSkillMatch = markSkillSimilarity(talent, position);
		if(verbose) {
			System.out.println("\tmarkSkillSimilarity : " + markSkillMatch);
		}
		totalMatch += markSkillMatch * 0.9;
		
		double tagWordMatch = 0;
		List<String> talentTagList = talent.getTagWordSegment().getWordList();
		double sameTagsNum = stringContainNumber(talentTagList, position.getTagWordSegment().getWordList());
		tagWordMatch = sameTagsNum * 0.02;
		
		if(verbose) {
			System.out.println("\ttagWordMatch : " + tagWordMatch);
		}
		totalMatch += tagWordMatch;

//		totalMatch = totalMatch > 1 ? 1 : totalMatch;
		
		return totalMatch;
	}

	@Override
	public double markSkillSimilarity(HOResult talent, HOResult position) {
		List<String> allSkillWordList = talent.getAllSkillWordSegment().getWordList();
		//must
		/*
		 * 必须满足的条件
		 * 	1、职位标题提到的技能型关键字
		 * 	2、句式描述：
		 * 			2年以上Android客户端开发经验；
		 * 			5年以上java开发经验,精通Java语言，了解JVM的工作原理
		 */
		List<String> positionMustList = position.getSegmentStringListByName(HORecoAnalyzerUtils.MUST);
		double mustMatch = stringListSimilarityBasePosition(talent.getSegmentStringListByName(
				HORecoAnalyzerUtils.MUST, 
				HORecoAnalyzerUtils.MASTER, 
				HORecoAnalyzerUtils.FAMILIAR), positionMustList);

		if(CollectionUtils.isNotEmpty(positionMustList) && mustMatch < 0.5) {
			return 0;
		}
		/*
		 * 标志技能关键字匹配得分
		 * 		精通
		 * 		熟练
		 * 		熟悉
			2:精通:master
			2:深刻理解:master
			2:熟练:familiar
			2:熟悉掌握:familiar
			2:具备:familiar
		 	2:熟悉:understanding
			2:了解:understanding
			2:掌握:understanding
			2:深入理解:understanding
			2:熟知:understanding
			2:优先:priority
		 */
		//精通
		List<String> talentMasterList = talent.getSegmentStringListByName(HORecoAnalyzerUtils.MASTER);
		List<String> positionMasterList = position.getSegmentStringListByName(HORecoAnalyzerUtils.MASTER);
		double masterMatch = masterSkillSimilarity(talentMasterList, positionMasterList);
		
		if(masterMatch < 0.1) {
			//补
			masterMatch += masterSkillSimilarity(talent.getSegmentStringListByName(HORecoAnalyzerUtils.FAMILIAR), position.getSegmentStringListByName(HORecoAnalyzerUtils.MASTER)) * 0.8;
		}
		
		
		//熟练
		double familiarMatch = stringListSimilarityBasePosition(
				talent.getSegmentStringListByName(HORecoAnalyzerUtils.MASTER, HORecoAnalyzerUtils.FAMILIAR), 
				position.getSegmentStringListByName(HORecoAnalyzerUtils.FAMILIAR));
		if(familiarMatch < 0.5) {
			familiarMatch += stringListSimilarityBasePosition(allSkillWordList, 
					position.getSegmentStringListByName(HORecoAnalyzerUtils.FAMILIAR)) * 0.5;
		}
		
		//熟悉
		double understandingMatch = stringListSimilarityBasePosition(
				talent.getSegmentStringListByName(HORecoAnalyzerUtils.MASTER, HORecoAnalyzerUtils.FAMILIAR, HORecoAnalyzerUtils.UNDERSTANDING), 
				position.getSegmentStringListByName(HORecoAnalyzerUtils.UNDERSTANDING));
		if(understandingMatch < 0.5){
			understandingMatch += stringListSimilarityBasePosition(
					allSkillWordList, position.getSegmentStringListByName(HORecoAnalyzerUtils.UNDERSTANDING)) * 0.5;
		}
		
		//“优先”的加分项
		double priorityMatch = stringListSimilarityBasePosition(
				talent.getSegmentStringListByName(HORecoAnalyzerUtils.MASTER, HORecoAnalyzerUtils.FAMILIAR, HORecoAnalyzerUtils.UNDERSTANDING, HORecoAnalyzerUtils.PRIORITY), 
				position.getSegmentStringListByName(HORecoAnalyzerUtils.PRIORITY));
		

		if(verbose) {
			System.out.println("\tmustMatch : " + mustMatch);
			System.out.println("\tmasterMatch : " + masterMatch);
			System.out.println("\tfamiliarMatch : " + familiarMatch);
			System.out.println("\tunderstandingMatch : " + understandingMatch);
			System.out.println("\tpriorityMatch : " + priorityMatch);
			
		}
		
		//精通 多选一，精通多选一匹配，和must匹配，则匹配度合格
		double sim = mustMatch * 0.5 + masterMatch * 0.3 + familiarMatch * 0.1 + understandingMatch * 0.1;
		return sim + priorityMatch * 0.2;
	}
}
