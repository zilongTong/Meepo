package com.hunteron.common4s.analyzer.ho.core.skillprocess;

import java.util.List;

import com.hunteron.common4s.analyzer.ho.core.skillprocess.mid.HOSkillMarkResult;
import com.hunteron.common4s.analyzer.ho.rs.HOSkillMark;



/**
 *
 * @author Smile.Wu
 * @version 2015-9-16
 */
public interface IHOSkillMarkPicker {

	public HOSkillMarkResult caculate(List<HOSkillMark> marks, String text);
}
