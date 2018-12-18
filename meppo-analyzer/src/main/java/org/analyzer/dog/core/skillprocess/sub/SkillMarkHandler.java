package org.analyzer.dog.core.skillprocess.sub;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Smile.Wu
 * @version 2015-9-17
 * 标志分词的对应文本处理
 */
public class SkillMarkHandler {

	//ture：在左边提取文本；false：在右边提取
	private boolean isLeft = false;
	//最大长度
	private int maxLength = 100;
	//终止字符
	private Set<Character> stopChars = null;
	
	private boolean isStopWithOtherIndex = true;
	
	public SkillMarkHandler() {
		stopChars = new HashSet<Character>();
		stopChars.add('!');
		stopChars.add('。');
		stopChars.add(';');
		stopChars.add('；');
		stopChars.add('！');
		stopChars.add('\n');
	}
	public boolean isLeft() {
		return isLeft;
	}
	public void setLeft(boolean isLeft) {
		this.isLeft = isLeft;
	}
	
	public boolean isStopWithOtherIndex() {
		return isStopWithOtherIndex;
	}
	public void setStopWithOtherIndex(boolean isStopWithOtherIndex) {
		this.isStopWithOtherIndex = isStopWithOtherIndex;
	}
	public int getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	public Set<Character> getStopChars() {
		return stopChars;
	}
	public void setStopChars(Set<Character> stopChars) {
		this.stopChars = stopChars;
	}
	public void addStopChar(Character c) {
		stopChars.add(c);
	}
	public String handlerString(String text, int prevIndex, int selfIndex, int nextIndex, int textLength) {
		if(isLeft) { //标志关键字左边是关注的技能型关键字
			
			int stopIndex = selfIndex - 1;
			int stopLength = Math.max(selfIndex - maxLength, 0);
			for(int index = stopIndex; index >= stopLength; index --) {
				Character currentChar = text.charAt(index);
				stopIndex = index;
				if(stopChars.contains(currentChar)) {
					break;
				}
			}
			String skillWordString = text.substring(stopIndex + 1, selfIndex);
			return skillWordString;
			
		} else {//右边
			int stopLength = Math.min(textLength, selfIndex + maxLength);
			if(isStopWithOtherIndex) {
				stopLength = Math.min(stopLength, nextIndex);
			}
			int stopIndex = selfIndex + 1;
			for(int index = stopIndex; index < stopLength; index ++) {
				Character currentChar = text.charAt(index);
				stopIndex = index;
				if(stopChars.contains(currentChar)) {
					break;
				}
			}
			String skillWordString = text.substring(selfIndex+1, Math.min(stopIndex+1, textLength));
			return skillWordString;
		}
	}
}
