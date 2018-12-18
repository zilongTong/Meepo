package org.analyzer.dog.similarity;

import java.util.List;

import org.analyzer.dog.rs.HOResult;
import org.analyzer.dog.rs.HOResultSegment;
import org.apache.commons.collections.CollectionUtils;


/**
 *
 * @author Leo
 * @version 2015-9-17
 */
public class HOSimilarity implements IHOSimilarity {

	@Override
	public double similarity(HOResult talent, HOResult position) {
		return 0;
	}

	@Override
	public double allSkillSimilarity(HOResultSegment talentSeg,
                                     HOResultSegment positionSeg) {
		return stringListSimilarity(talentSeg.getWordList(), positionSeg.getWordList());
	}

	@Override
	public double tagWordSimilarity(HOResultSegment talentSeg,
			HOResultSegment positionSeg) {
		return stringListSimilarity(talentSeg.getWordList(), positionSeg.getWordList());
	}
	@Override
	public double masterSkillSimilarity(List<String> talentSKills,
			List<String> positionSkills) {
		int sameCount = stringContainNumber(talentSKills, positionSkills);
		if(sameCount > 0) {
			int positionMasters = positionSkills.size();
			
			double delt = 0.3 / (double)positionMasters;
			
			return 0.7 + delt * sameCount;
		}
		return 0;
	}

	@Override
	public double markSkillSimilarity(HOResult talent, HOResult position) {
		return 0;
	}
	//相似度计算
	public double stringListSimilarity(List<String> mainList, List<String> positionStringList) {
		double matchCount = stringContainNumber(mainList, positionStringList);
		if(matchCount <= 0) {
			return matchCount;
		}
		float mainListSize = mainList.size();
		float positionStringSize = positionStringList.size();
		
		return (matchCount / mainListSize) * (matchCount / positionStringSize);
	}
	//相似数量
	public int stringContainNumber(List<String> mainList, List<String> positionStringList) {
		if(CollectionUtils.isEmpty(positionStringList)) {
			return 0;
		}
		
		if(CollectionUtils.isEmpty(mainList)) {
			return 0;
		}
		int matchCount = 0;
		
		for(String word : positionStringList) {
			if(mainList.contains(word)) {
				matchCount ++;
			}
		}
		return matchCount;
	}
	/**
	 * 基于右边列表的相似度计算
	 * @param mainList
	 * @param positionStringList
	 * @return
	 */
	public double stringListSimilarityBasePosition(List<String> mainList, List<String> positionStringList) {
		double matchCount = stringContainNumber(mainList, positionStringList);
		if(matchCount <= 0) {
			return matchCount;
		}
		float positionStringSize = positionStringList.size();
		return matchCount / positionStringSize;
	}
	
	/**
	 * 根据master要求的数量，区分不同的最低匹配度阀值
	 * @param result
	 * @return
	 */
	protected double minMasterMatch(HOResult result) {
		int size = result.getSegmentStringListByName("master").size();
		if(size <= 5) {
			return 0.5;
		}
		return 0.8;
	}
	
	@Override
	public void setVerbose(boolean verbose) {	
	}
}
