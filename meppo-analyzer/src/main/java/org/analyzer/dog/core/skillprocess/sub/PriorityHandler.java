package org.analyzer.dog.core.skillprocess.sub;
/**
 *
 * @author Leo
 * @version 2015-9-17
 * “优先”对应文本的截取方式
 */
public class PriorityHandler extends SkillMarkHandler {
	public PriorityHandler() {
		setLeft(true);
		addStopChar('有');
	}
}
