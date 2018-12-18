package org.analyzer.dog.core.skillprocess;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.analyzer.dog.rs.HOSkillMark;
import org.analyzer.dog.util.HORecoAnalyzerUtils;
import org.apache.commons.lang.StringUtils;

import org.analyzer.dog.core.skillprocess.mid.HOSkillMarkResult;
import org.analyzer.dog.core.skillprocess.sub.PriorityHandler;
import org.analyzer.dog.core.skillprocess.sub.SkillMarkHandler;


/**
 *
 * @author Leo
 * @version 2015-9-16
 */
public class DefaultHOSkillMarkProcessor implements IHOSkillMarkPicker {
	
	//默认标志关键字文本处理
	private SkillMarkHandler handler = new SkillMarkHandler();
	//“优先”的文本处理
	private PriorityHandler priorityHandler = new PriorityHandler();
	
	@Override
	public HOSkillMarkResult caculate(List<HOSkillMark> marks, String text) {
		HOSkillMarkResult result = new HOSkillMarkResult();
		
		int textLength = text.length(), prevIndex = 0, selfIndex = 0, nextIndex = 0;
		for(int i = 0, len = marks.size(); i < len; i ++) {
			HOSkillMark currentMark = marks.get(i);
			selfIndex = currentMark.getIndex();
			
			if(i + 1 < len) {
				HOSkillMark nextMark = marks.get(i + 1);
				nextIndex = nextMark.getIndex();
			} else {
				nextIndex = textLength;
			}
			
			String name = currentMark.getName();
			String skillString = null;
			
			//文本在左
			if(name.equalsIgnoreCase(HORecoAnalyzerUtils.PRIORITY)) {
				skillString = priorityHandler.handlerString(text, prevIndex, selfIndex, nextIndex, textLength);
			} else {
				//文本在右
				skillString = handler.handlerString(text, prevIndex, selfIndex, nextIndex, textLength);
			}
			if(StringUtils.isNotBlank(skillString)) {
				result.addSubText(name, skillString);
			}
			prevIndex = selfIndex;
		}
		

		result.addSubText(HORecoAnalyzerUtils.MUST, getRegexString(text, regex));
		
		return result;
	}
	
	public static String getRegexString(String line, Pattern p) {
		try {
			Matcher ma = p.matcher(line.replaceAll("\\s+", ""));
			while(ma.matches()) {
				return ma.group(1);
			}
			return ma.group(1);
		} catch (Exception e) {
			return "";
		}
	}
	static Pattern regex = Pattern.compile("[\\S|\\s|\n]*?年([\\S|\\s]{1,50})经验[\\S|\\s|\n]*\\S*");
}
