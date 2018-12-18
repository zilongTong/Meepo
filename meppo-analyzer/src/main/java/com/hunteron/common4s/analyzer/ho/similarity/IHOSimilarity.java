package com.hunteron.common4s.analyzer.ho.similarity;

import java.util.List;

import com.hunteron.common4s.analyzer.ho.rs.HOResult;
import com.hunteron.common4s.analyzer.ho.rs.HOResultSegment;



/**
 *
 * @author Smile.Wu
 * @version 2015-9-17
 */
public interface IHOSimilarity {
	/**
	 * 匹配度，相似度计算
	 * @param talent
	 * @param position
	 * @return
	 */
	public double similarity(HOResult talent, HOResult position);
	
	/**
	 * 所有技能型关键字结果匹配计算
	 * @param talentSeg
	 * @param positionSeg
	 * @return
	 */
	public double allSkillSimilarity(HOResultSegment talentSeg, HOResultSegment positionSeg);
	
	/**
	 * 标签型关键字匹配计算，加分项
	 * @param talentSeg
	 * @param positionSeg
	 * @return
	 */
	public double tagWordSimilarity(HOResultSegment talentSeg, HOResultSegment positionSeg);
	
	/**
	 * 标志型技能关键字的特殊计算
	 * @param talent
	 * @param position
	 * @return
	 */
	public double markSkillSimilarity(HOResult talent, HOResult position);
	
	public double masterSkillSimilarity(List<String> talentSKills, List<String> positionSkills);
	
	public void setVerbose(boolean verbose);
}
