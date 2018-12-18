package com.hunteron.common4s.analyzer.ho.core.skillprocess.sub;
/**
 *
 * @author Smile.Wu
 * @version 2015-9-17
 * “优先”对应文本的截取方式
 */
public class PriorityHandler extends SkillMarkHandler {
	public PriorityHandler() {
		setLeft(true);
		addStopChar('有');
	}
}
