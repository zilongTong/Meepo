package com.hunteron.common4s.analyzer.zeus;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZeusUtils {
	
	private static final Pattern pattern = Pattern.compile("[0-9]+"); 

	static Pattern regexLetter = Pattern.compile("[\\S|\\s|\n]*?([a-zA-Z|\\+|\\#|\\d|\\s]+)[\\S|\\s|\n]*\\S*");
	public static String getRegexString(String line, Pattern p) {
		if(isBlank(line)) {
			return "";
		}
		try {
			Matcher ma = p.matcher(line.replaceAll("\\s+", " "));
			while(ma.matches()) {
				return ma.group(1);
			}
			return ma.group(1);
		} catch (Exception e) {
			return "";
		}
	}
	public static boolean hasLetter(String line) {
		return isNotBlank(getRegexString(line, regexLetter));
	}
	
	public static boolean isNumber(String str) {
		Matcher isNum = pattern.matcher(str);
		if(!isNum.matches()){
			return false; 
		} 
		return true; 
	}
	
	public static boolean isBlank(String v) {
		return v == null || v.trim().length() < 1;
	}
	public static boolean isNotBlank(String v) {
		return !isBlank(v);
	}

	public static int getInt(Object v, int defaultValue) {
		if(v == null) {
			return defaultValue;
		}
		String vS = v.toString().trim();
		if(isNumber(vS)) {
			return Integer.parseInt(vS);
		}
		return defaultValue;
	}
	
	public static List<String> createFromLine(String line, String sep) {
		List<String> result = new ArrayList<>(5);
		String[] lines = line.split(sep);
		String prev = lines[0];
		
		String after = lines[1];
		
		String[] prevs = prev.split(",");
		String afters[] = after.split(",");
		for(String pv : prevs) {
			for(int i = 0, len = afters.length; i < len; i ++) {
				String newWord = pv + afters[i];
				result.add(newWord);
			}
		}
		return result;
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

    public static boolean isNumber(Character ch) {
    	int v = ch - 0;
    	return v >= 48 && v <= 57;
    }

	//是否可成词判断
	public static boolean canBeFinalWord(Character prev, String word, Character ch) {
		if(isBlank(word)) {
			return false;
		}
		
		char wordFirstChar = word.charAt(0);
		if(isLetter(wordFirstChar) || isNumber(wordFirstChar)) {
			//如果关键字首字是数字，英文
			if(prev != null && (isLetter(prev) || isNumber(prev))) {
				return false;
			}
		}

		char wordEndChar = word.charAt(word.length() - 1);
		if(isLetter(wordEndChar) || isNumber(wordEndChar)) {
			//如果关键字首字是数字，英文
			if(ch != null && (isLetter(ch) || isNumber(ch))) {
				return false;
			}
		}
		
		return true;
	}
	
	public static void main(String[] args) {
		System.out.println(isNumber("123"));
		System.out.println(hasLetter(" 软件工程师"));
	}
}
