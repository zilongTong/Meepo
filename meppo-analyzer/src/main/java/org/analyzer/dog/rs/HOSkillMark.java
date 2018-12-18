package org.analyzer.dog.rs;

import java.io.Serializable;

/**
 *
 * @author Smile.Wu
 * @version 2015-9-16
 * 技能标志关键字的位置信息
 */
public class HOSkillMark implements Serializable {

	private static final long serialVersionUID = -7238233953229739688L;

	private String name;
	
	private int index;
	
	public HOSkillMark(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return "HOSkillMark [name=" + name + ", index=" + index + "]";
	}
}
