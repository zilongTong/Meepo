package org.analyzer.sdk;



/**
 * @author Leo
 * @date 2016年4月2日  下午1:19:46
 */
public class StringFormat {
	public static Integer getInt(String v, int dft) {
		if (isNumeric(v)) {
			return (int) Double.parseDouble(v);
		}
		return dft;
	}

	public static Integer getInt(Object o, int dft) {
		if (o == null) {
			return dft;
		}
		String v = o.toString();
		if (isNumeric(v)) {
			return (int) Double.parseDouble(v);
		}
		return dft;
	}

	public static Float getFloat(String v, float dft) {
		if (isNumeric(v)) {
			return Float.parseFloat(v);
		}
		return dft;
	}

	public static Float getFloat(Object o, float dft) {
		if (o == null) {
			return dft;
		}
		String v = o.toString();
		if (isNumeric(v)) {
			return Float.parseFloat(v);
		}
		return dft;
	}

	public static Double getDbl(String v, Double dft) {
		try {
			return Double.parseDouble(v);
		} catch (Exception e) {

		}
		return dft;
	}

	public static Double getDbl(Object o, Double dft) {
		try {
			if (o == null) {
				return dft;
			}
			String v = o.toString();
			return Double.parseDouble(v);
		} catch (Exception e) {

		}
		return dft;
	}
	
	public static Long getLong(String v, long dft) {
		if (isNumber(v)) {
			return Long.parseLong(v);
		}
		return dft;
	}

	public static Long getLong(Object v, long dft) {
		if (v == null) {
			return dft;
		}
		return getLong(v.toString(), dft);
	}

	public static Boolean getBoolean(String v, boolean dft) {
		if (StringTools.isNotBlank(v)) {
			if (v.equalsIgnoreCase("true")) {
				return true;
			}
			if (getInt(v, -1) > 0) {
				return true;
			}
		}
		return dft;
	}

	public static String getString(String s, String dft) {
		if (StringTools.isNotBlank(s)) {
			return s;
		}
		return dft;
	}

	public static Integer getIntFromBoolean(String v, int trueInt, int falseInt) {
		if (getBoolean(v, false)) {
			return trueInt;
		}
		return falseInt;
	}

	public static boolean isNumeric(String s) {
		if (!StringTools.isNotBlank(s))
			return false;
		return StaticValue.Regex.NUMERIC.matcher(s).matches();
	}

	public static boolean isNumber(String s) {
		if (!StringTools.isNotBlank(s))
			return false;
		return StaticValue.Regex.NUMBER.matcher(s).matches();
	}
}
