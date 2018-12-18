package org.analyzer.dog.core.skillprocess;

import java.util.List;

import org.analyzer.dog.core.skillprocess.mid.HOSkillMarkResult;
import org.analyzer.dog.rs.HOSkillMark;



/**
 *
 * @author Leo
 * @version 2015-9-16
 */
public interface IHOSkillMarkPicker {

	public HOSkillMarkResult caculate(List<HOSkillMark> marks, String text);
}
