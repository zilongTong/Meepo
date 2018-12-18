package org.analyzer.sdk;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.regex.Pattern;

/** 
 * @author 吴健  (HQ01U8435)	Email : wujian@metersbonwe.com 
 * @version 创建时间：2012-5-23 下午1:32:27 
 */
public interface StaticValue {
	
	public interface Regex {
		public static final Pattern NUMBER = Pattern.compile("[\\d]+");
		public static final Pattern NO_NUMERIC = Pattern.compile("[^-|\\d|\\.|\\d]+");
		public static final Pattern NUMERIC = Pattern.compile("-?\\d+\\.?\\d*");
		public static final Pattern LETTERS = Pattern.compile("\\w+");
		public static final Pattern NUMBER_OR_LETTER = Pattern.compile("[\\d|\\w]+");
		public static final Pattern ID_CARD_LAST = Pattern.compile("(\\d{6})[a-zA-Z]?$");

		public final static Pattern CHINESE_WORD = Pattern.compile("[\u4e00-\u9fa5]+");
		public final static Pattern CONTAIN_CHINESE_WORD = Pattern.compile("\\S*[\u4e00-\u9fa5]+\\S*");

		public final static Pattern CHINESE_WORD_comma_blank_minus = Pattern.compile("[\u4e00-\u9fa5|\\,|\\s|\\-]+");
		public final static Pattern LETTERS_comma_blank_minus = Pattern.compile("[\\w|\\,|\\s|\\-]+");
		public final static Pattern CHINESE_WORD_letter_comma_blank_minus = Pattern.compile("[\u4e00-\u9fa5|\\w|\\,|\\s|\\-]+");

	}

	public static final String INPUT_KEYWORD = "[^a-zA-Z|\u4e00-\u9fa5|\\d|\\-|\\+|\\s|\\@|\\!|\\！|\\.]";
	public static final String DEFAULT_STATIC_KEYWORD = "hunter_on";
	public static final String AUTOKEY_FILTER = "[^a-zA-Z|\u4e00-\u9fa5|\\d|\\-|\\+|\\#]";
	/**
	 * 替换关键字中的空格
	 */
	public static final String special_split = "#%%#";
	
	public interface DateFormat {
		public static SimpleDateFormat FORMAT_FULL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		public static SimpleDateFormat FORMAT_SIMPLE_MINUS = new SimpleDateFormat("yyyy-MM-dd");
		public static SimpleDateFormat FORMAT_SIMPLE_LONG = new SimpleDateFormat("yyyyMMdd");
		public static SimpleDateFormat FORMAT_FULL_ON_DAY = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		public static SimpleDateFormat FORMAT_HHmmss = new SimpleDateFormat("HHmmss");
	}

	/**
	 * 排序
	 */
	public static IntegerSort _comparator = new IntegerSort();
}


class IntegerSort implements Comparator<Integer> {

	public int compare(Integer o1, Integer o2) {
		if (o1 == o2) {
			return 0;
		} else if (o1 > o2) {
			return 1;
		} else {
			return -1;
		}
	}
}
