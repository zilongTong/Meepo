package com.hunteron.common4s.analyzer.ho.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.hunteron.common4s.analyzer.ho.dic.HODicTerm;
import com.hunteron.common4s.jdk.string.str.StringTools;


/**
 *
 * @author Smile.Wu
 * @version 2015-9-16
 */
public class HORecoAnalyzerUtils {

	public static final String MASTER = "master";
	public static final String MUST = "must";
	public static final String FAMILIAR = "familiar";
	public static final String UNDERSTANDING = "understanding";
	public static final String PRIORITY = "priority";
	public static final String TAG_WORD = "tagWord";
	public static final String ALL_SKILL = "allSkill";
	public static final String SELF_CONDITION = "selfCondition";

//	public static final String[] list ={MUST,MASTER,FAMILIAR,UNDERSTANDING};
	public static final String[] simpleList ={MUST,MASTER};
	public static boolean isNull(HODicTerm term) {
		return term == null || StringUtils.isBlank(term.getWord());
	}
	
	/**
	 * 
	 *  1:java:java
	 *  1:php
	 * @param line
	 * @return
	 */
	public static HODicTerm createTermFromLine(String line) {
		if(StringUtils.isBlank(line) || line.startsWith("#")) {
			return null;
		}
		String[] values = line.split(":");
		
		if(line.startsWith("-1") && values.length >= 2) {
			String word = values[1];
			int nextIndex = line.indexOf(":", 3);
			String other = line.substring(nextIndex + 1);
			HODicTerm term = new HODicTerm(word, -1);
			//停止符存放在name字段
			term.setName(other);
			return term;
		} else if(values.length >= 2) {
			int type = StringTools.getInt(values[0], -1);
			if(type < 0) {
				return null;
			}
			String word = values[1];
			HODicTerm term = new HODicTerm(word, type);
			if(values.length >= 3) {
				term.setName(values[2]);
			} else {
				term.setName(word);
			}
			return term;
		}

		return null;
	}
	public static boolean isNumber(Character ch) {
    	int v = ch - 0;
    	return v >= 48 && v <= 57;
    }

	//运营:真运营,营销,销售+客户,经理,总监,专员,主管
	public static List<HODicTerm> createTermFromLine(List<String> lines) {
		List<HODicTerm> rs = new ArrayList<>();
		
		for(String l : lines) {
			if(l.startsWith("100:")) {
				String temp = l.substring(4);
				String[] values = temp.split("\\+");
				if(values.length >= 2) {

					String[] left = values[0].split(",");
					String[] right = values[1].split(",");
					
					List<String> leftList = new ArrayList<>(left.length);
					for(String lf : left) {
						if(lf.indexOf(":") > 0) {
							String[] lfValues = lf.split(":");
							String name = lfValues[0];
							leftList.add(name);
							for(int i = 1, len = lfValues.length; i < len; i ++){
								leftList.add(name + ":" + lfValues[i]);
							}
						} else {
							leftList.add(lf);
						}
					}
					
					for(String lf : leftList) {
						for(String rf : right) {
							if(lf.indexOf(":") > 0) {
								String[] lfValues = lf.split(":");
								if(lfValues.length == 2) {
									HODicTerm term = new HODicTerm(lfValues[1] + rf, 1);
									term.setName(lfValues[0] + rf);
									rs.add(term);
								}
							} else {
								HODicTerm term = new HODicTerm(lf + rf, 1);
								rs.add(term);
							}
						}
					}
				}
			}
		}
		
		return rs;
	}
	/**
	 * 是否是英文字母
	 * @param ch
	 * @return
	 */
    public static boolean isLetter(Character ch) {
    	int v = ch - 0;
    	return (v >= 65 && v <= 90) || (v >= 97 && v <= 122);
    }
    public static void main(String[] args) {
    	List<String> lines = new ArrayList<>();
    	lines.add("100:运营:真运营,营销:传销,销售+客户,经理,总监,专员,主管");
    	
    	List<HODicTerm> terms = createTermFromLine(lines);
    	for(HODicTerm term : terms) {
    		System.out.println(term.getName() + " : " +term.getWord());
    	}
	}
}
