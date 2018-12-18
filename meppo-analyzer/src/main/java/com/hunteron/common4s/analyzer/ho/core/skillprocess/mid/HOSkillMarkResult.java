package com.hunteron.common4s.analyzer.ho.core.skillprocess.mid;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Smile.Wu
 * @version 2015-9-16
 * 技能标志关键字以及文本信息
 */
public class HOSkillMarkResult {

	private Map<String, String> skillMap = null;
	
	public HOSkillMarkResult() {
		skillMap = new HashMap<String, String>();
	}

	public Map<String, String> getSkillMap() {
		return skillMap;
	}

	public void setSkillMap(Map<String, String> skillMap) {
		this.skillMap = skillMap;
	}
	
	//添加不同类型的文本
	public void addSubText(String name, String subText) {
		if(StringUtils.isBlank(name) || StringUtils.isBlank(subText)) {
			return;
		}
		String oldText = skillMap.get(name);
		if(oldText == null) {
			oldText = "";
			skillMap.put(name, subText);
		} else {
			skillMap.put(name, oldText + " ； " + subText);
		}
	}
}
