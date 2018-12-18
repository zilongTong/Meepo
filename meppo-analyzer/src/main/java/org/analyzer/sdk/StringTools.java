package org.analyzer.sdk;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;


public class StringTools {
    public static boolean isNotBlank(Object s) {
        return s != null && isNotBlank(s.toString());
    }

    public static boolean isNotBlank(String s) {
        return s != null && s.trim().length() > 0;
    }

    public static boolean isBlank(String s) {
        return !isNotBlank(s);
    }

    public static boolean isBlank(Object s) {
        return s == null || !isNotBlank(s.toString());
    }

    public static int indexInListWithASC(List<Integer> list, Integer obj) {
        if (list == null || list.size() < 1) {
            return -1;
        }
        int rs = -1;
        Collections.sort(list, new Comparator<Integer>() {

            @Override
            public int compare(Integer o1, Integer o2) {
                if (o1 > o2) {
                    return 1;
                }
                if (o1 < o2) {
                    return -1;
                }
                return 0;
            }
        });
        for (Integer o : list) {
            rs++;
            if (o.intValue() == obj.intValue()) {
                return rs;
            }
        }

        return rs;
    }

    public static <T> List<T> randomList(List<T> list, int count) {
        try {
            if (list != null && list.size() > count) {
                List<T> rs = new ArrayList<T>();
                int size = list.size();
                Random r = new Random(System.currentTimeMillis());
                int[] arr = new int[count];
                int start = r.nextInt(size);
                for (int i = 0; i < count; i++) {
                    if (start < size) {
                        arr[i] = start;
                    } else {
                        start = 0;
                        arr[i] = start;
                    }
                    start++;
                }
                for (int index : arr) {
                    rs.add(list.get(index));
                }
                return rs;
            }
            return list;
        } catch (Exception e) {

        }
        return list;
    }

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
        System.out.println(randomList(list, 2));

        System.out.println(indexInListWithASC(list, 2));

        System.out.println(getInt("2001.1", 0));

        System.out.println("13.1 年以上".replaceAll(StaticValue.Regex.NO_NUMERIC.pattern(), ""));
    }

    public static String delHTMLTag(String htmlStr) {
        if (isBlank(htmlStr)) {
            return "";
        }
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
        String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        return htmlStr.trim(); // 返回文本字符串
    }

    public static boolean isMatch(String s, Pattern p) {
        if (isBlank(s)) {
            return false;
        }
        return p.matcher(trimAllBlanks(s)).matches();
    }

    public static String trimAllBlanks(String s) {
        if (isBlank(s)) {
            return "";
        }
        return s.replaceAll("\\s", "");
    }

    /**
     * 判断字符串是否是数字或者字母
     * 
     * @param s
     * @return
     */
    public static boolean isNumberOrLetters(String s) {
        if (isBlank(s)) {
            return false;
        }
        return StaticValue.Regex.NUMBER_OR_LETTER.matcher(trimAllBlanks(s)).matches();
    }

    public static boolean isLetters(String s) {
        if (isBlank(s)) {
            return false;
        }
        return StaticValue.Regex.LETTERS.matcher(trimAllBlanks(s)).matches();
    }

    public static boolean isAllChineseWord(String s) {
        if (isBlank(s)) {
            return false;
        }
        return StaticValue.Regex.CHINESE_WORD.matcher(trimAllBlanks(s)).matches();
    }

    public static boolean isContainChineseWord(String s) {
        if (isBlank(s)) {
            return false;
        }
        return StaticValue.Regex.CONTAIN_CHINESE_WORD.matcher(trimAllBlanks(s)).matches();
    }

    /**
     * 汉字的code codePointAt
     * 
     * @param s
     * @return
     */
    public static String stringToCharCode(String s) {
        String rs = "";
        s = s.toLowerCase();
        if (isNumberOrLetters(s)) {
            return s;
        }
        List<Integer> codes = new ArrayList<Integer>();
        for (int i = 0, len = s.length(); i < len; i++) {
            int cv = s.codePointAt(i);
            if (cv == 32) {
                continue;
            }
            codes.add(cv);
        }
        Collections.sort(codes, StaticValue._comparator);
        for (Integer code : codes) {
            rs += String.valueOf(code) + "_";
        }
        return rs;
    }

    /**
     * 用于显示，多个空格合并成一个空格
     * 
     * @param word
     * @return
     */
    public static String withOneSpace(String word) {
        return replace(word, " ");
    }

    /**
     * 多个空格由一个自定义特殊字符代替，用于索引
     * 
     * @param word
     * @return
     */
    public static String withReplaceSpace(String word) {
        return replace(word, StaticValue.special_split);
    }

    /**
     * 将特殊字符用指定的字符串替换
     * 
     * @param w
     * @param sp
     * @return
     */
    public static String replace(String w, String sp) {
        try {
            String temp = toDBC(w);
            temp = temp.replaceAll(StaticValue.AUTOKEY_FILTER, "").toLowerCase();
            return temp.trim().replaceAll("\\s+", sp);
        } catch (Exception e) {
            return w;
        }
    }

    public static String clearAutoKeyWord(String w) {
        try {
            if (w == null)
                return null;
            String temp = toDBC(w);
            temp = temp.replaceAll(StaticValue.AUTOKEY_FILTER, " ");

            temp = temp.replaceAll("\\-", " ");

            return temp.replaceAll("[||\\(|\\)]?", "").trim().replaceAll("\\s+", " ");
        } catch (Exception e) {
            return w;
        }
    }

    /**
     * 初始输入关键字清理
     * 
     * @param w
     * @return
     */
    public static String cleanQueryWord(String w) {
        try {
            String temp = toDBC(w);
            temp = temp.replaceAll(StaticValue.INPUT_KEYWORD, " ").toLowerCase();
            // temp = temp.replaceAll("\\s*\\-+\\s*", " -");
            temp = temp.replaceAll("\\s+", " ");
            return temp.trim();
        } catch (Exception e) {
            return w;
        }
    }

    /**
     * 字符串是否重复 ，按空格分词
     * 
     * @param vals
     * @return
     */
    private static boolean isRepeatSpliteBySpace(String[] vals) {
        Set<String> set = new HashSet<String>(vals.length);

        for (String v : vals) {
            if (v.trim().length() > 0) {
                if (!set.contains(v)) {
                    set.add(v);
                } else {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 字符串是否单字重复
     * 
     * @param s
     * @return
     */
    private static boolean isRepeatSingleWord(String s) {
        Set<Character> set = new HashSet<Character>(s.length());
        int repeatCount = 0;
        for (int i = 0, len = s.length(); i < len; i++) {
            char ch = s.charAt(i);
            if (!set.contains(ch)) {
                set.add(ch);
            } else {
                repeatCount++;
            }
        }

        return (float) repeatCount / (float) s.length() >= 0.2;
    }

    /**
     * 对关键字进行简单的去重复 “女 女装 衬衫” ---> “女装 衬衫”
     * 
     * @param w
     * @return
     */
    public static String replaceSameWord(String w) {
        if (isBlank(w)) {
            return "";
        }
        try {
            String[] ws = w.split(" ");
            List<String> rs = Arrays.asList(ws);
            Collections.sort(rs, new Comparator<String>() {
                public int compare(String o1, String o2) {
                    return o2.length() - o1.length();
                }

            });
            StringBuilder sb = new StringBuilder("");
            for (String s : rs) {
                if (s != null && !s.isEmpty() && sb.indexOf(s) < 0) {
                    sb.append(s + " ");
                }
            }
            return sb.toString().trim();
        } catch (Exception e) {
            return w;
        }
    }

    /**
     * 关键字是否有一定的重复性
     * 
     * @param l
     * @return
     */
    public static boolean isRepeatMany(String l) {
        if (l == null) {
            return false;
        }
        String[] vals = l.split(" ");
        boolean rs = isRepeatSpliteBySpace(vals);
        if (rs) {
            return rs;
        }

        return isRepeatSingleWord(l);
    }

    /**
     * 全角转半角
     * 
     * @param input
     * @return
     */
    public static String toDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    public static long valueAsDefault(long value, long min, long max, long dft) {
        return value >= min && value <= max ? value : dft;
    }

    public static int valueAsDefault(int value, int min, int max, int dft) {
        return value >= min && value <= max ? value : dft;
    }

    private static final Pattern ID_SN_PATTERN = Pattern.compile("(\\d+?)\\s+", 0);
    private static final Pattern ID_SN_PATTERN_END = Pattern.compile("(\\d{1,})$", 0);

    public static List<String> pickString(String input) {
        List<String> rs = new ArrayList<String>(0);
        try {
            if (isBlank(input)) {
                return rs;
            }

            Matcher m = ID_SN_PATTERN_END.matcher(input);

            while (m.find()) {
                rs.add(m.group(1));
            }
            if (rs.size() < 1) {
                m = ID_SN_PATTERN.matcher(input);
                while (m.find()) {
                    rs.add(m.group(1));
                }
            }
            return rs;
        } catch (Exception e) {
            return rs;
        }
    }

    public static List<String> pickNumString(String input) {
        List<String> rs = new ArrayList<String>(0);
        try {
            if (isBlank(input)) {
                return rs;
            }
            String[] vals = input.split("\\s+");

            for (String s : vals) {
                if (StringFormat.isNumber(s)) {
                    rs.add(s);
                }
            }

            return rs;
        } catch (Exception e) {
            return rs;
        }
    }

    public static String[] stringToStringArray(String ids) {
        if (isBlank(ids)) {
            return new String[0];
        }
        String[] temp = ids.split(",");
        return temp;
    }

    public static List<String> stringToStringList(String ids) {
        if (isBlank(ids)) {
            return new ArrayList<String>(0);
        }
        String[] temp = ids.split(",");
        return Arrays.asList(temp);
    }

    public static List<Long> stringToLongList(String ids) {
        String[] temp = stringToStringArray(ids);
        List<Long> rs = new ArrayList<Long>();
        for (String s : temp) {
            long sL = StringFormat.getLong(s, -1001);
            if (sL != -1001) {
                rs.add(sL);
            }
        }
        return rs;
    }

    public static List<Integer> stringToIntegerList(String ids) {
        String[] temp = stringToStringArray(ids);
        List<Integer> rs = new ArrayList<Integer>();
        for (String s : temp) {
            int sL = StringFormat.getInt(s, -1001);
            if (sL != -1001) {
                rs.add(sL);
            }
        }
        return rs;
    }

    public static List<Integer> stringToIntegerList(String ids, String reg) {
        String[] temp = ids.split(reg);
        List<Integer> rs = new ArrayList<Integer>();
        for (String s : temp) {
            int sL = StringFormat.getInt(s, -1001);
            if (sL != -1001) {
                rs.add(sL);
            }
        }
        return rs;
    }

    public static List<Long> stringToLongList(List<String> ids) {
        if (ids == null || ids.size() < 1) {
            return new ArrayList<Long>(0);
        }
        List<Long> rs = new ArrayList<Long>();
        for (String s : ids) {
            long sL = StringFormat.getLong(s, -1001L);
            if (sL != -1001) {
                rs.add(sL);
            }
        }
        return rs;
    }

    public static List<Long> stringToLongList(String ids, String regex) {
        List<Long> rs = new ArrayList<Long>();
        if (StringUtils.isBlank(ids)) {
            return rs;
        }
        String[] ss = ids.split(regex);
        if (ss.length > 0) {
            for (String s : ss) {
                long sL = getLong(s, -1001L);
                if (sL != -1001) {
                    rs.add(sL);
                }
            }
        }
        return rs;
    }

    public static List<Integer> stringToIntList(String ids, String regex) {
        List<Integer> rs = new ArrayList<>();
        if (StringUtils.isBlank(ids)) {
            return rs;
        }
        String[] ss = ids.split(regex);
        if (ss.length > 0) {
            for (String s : ss) {
                int sL = getInt(s, -1001);
                if (sL != -1001) {
                    rs.add(sL);
                }
            }
        }
        return rs;
    }

    public static <T> String join(List<T> values, String sep) {
        StringBuilder sb = new StringBuilder("");

        if (values != null && values.size() > 0) {
            for (T o : values) {
                if (o != null) {
                    sb.append(o.toString()).append(sep);
                }
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - sep.length());
            }
        }

        return sb.toString();
    }

    public static <T> List<T> subList(List<T> list, int limit) {
        if (list == null || list.size() < 1) {
            return new ArrayList<T>();
        }
        int size = list.size();
        if (size > limit) {
            List<T> rs = new ArrayList<>(limit);
            int c = 1;
            for (T t : list) {
                rs.add(t);
                if (c++ >= limit) {
                    break;
                }
            }
            return rs;
        }
        return list;
    }

    // public static void main(String[] args) {
    // // long b = System.currentTimeMillis();
    //
    // //
    // // System.out.println(delHTMLTag("<li><a id=\"MyLinks1_HomeLink\"
    // // class=\"menu\" href=\"http://www.cnblogs.com/\">博客园</a></li>"));
    // //
    // // System.out.println(clearWord("健康管理中心总经理 (急)"));
    // //
    // // System.out.println(clearWord(withOneSpace("软件工程师-系统")));
    // //
    // // System.out.println();
    // // System.out.println("--"+pickNumString("888wang_tao@163.com"));
    // //
    // // System.out.println(isRepeatMany("上海"));
    //
    // // System.out.println(clearWord("C++#"));
    // // System.out.println(StringTools.cleanInputWord("java -lucene \\"));
    //
    // // String t = "、..-- 熟悉PHP和- MYSQL数据库开发； 2、熟悉开源产品并能对程序进行二次开发";
    // //
    // // System.out.println("－" +
    // // t.replaceAll("^[^(a-zA-Z|\u4e00-\u9fa5|\\d)|\\s]*", ""));
    //
    // long b = System.currentTimeMillis();
    // String word = "工程(师 java@126wujy 测试!9633 - php -java";
    // word = "cocos2d-x";
    // word = cleanQueryWord(word);
    // System.out.println(word);
    // System.out.println("cost : " + (System.currentTimeMillis() - b));
    //
    // System.out.println(pickString("123talk"));
    //
    // System.out.println(isContainChineseWord("node.js  "));
    // }

    public static final char[] ender = new char[] { '-', '—', '－', '–', '/', '(' };

    public static String cleanPositionTitle(String title) {

        for (char ch : ender) {
            int v = title.indexOf(ch);
            if (v > 1) {
                title = title.substring(0, v).trim();
            }
        }

        return title;
    }

    static Pattern regexLetter = Pattern.compile("[\\S|\\s|\n]*?([a-zA-Z|\\+|\\#|\\d|\\s]+)[\\S|\\s|\n]*\\S*");

    public static String getMainWordFromTitle(String line) {
        return getRegexString(line, regexLetter);
    }

    public static String getRegexString(String line, Pattern p) {
        if (isBlank(line)) {
            return "";
        }
        try {
            Matcher ma = p.matcher(line.replaceAll("\\s+", " "));
            while (ma.matches()) {
                return ma.group(1);
            }
            return ma.group(1);
        } catch (Exception e) {
            return "";
        }
    }

    public static String splitAndGet(String line, String sep, int index) {
        if (isBlank(line)) {
            return line;
        }
        String[] values = line.split(sep);
        if (values.length >= index) {
            for (int i = index, len = values.length; i < len; i++) {
                String v = values[i].trim();
                if (v.length() > 1) {
                    return v;
                }
            }
        }
        return line;
    }

    public static boolean isLettersOrSymbol(String s) {
        if (isBlank(s)) {
            return false;
        }
        return LETTERS_AND_SYMBOL.matcher(trimAllBlanks(s)).matches();
    }

    private final static Pattern LETTERS_AND_SYMBOL = Pattern.compile("[\\w+|\\d|\\.|\\+|\\#]+");

    public static final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    public final static String MD5(String s) {
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private final static Pattern HAN_ZI = Pattern.compile("[\u4e00-\u9fa5]+");
    private final static Pattern HAVE_HAN_ZI = Pattern.compile("\\S*[\u4e00-\u9fa5]+\\S*");
    public static final String DEFAULT_STATIC_KEYWORD = "hunter_on";

    /**
    * 排序
    */
    private static IntegerSort _comparator = new IntegerSort();
    /**
    * 替换关键字中的空格
    */
    public static final String special_split = "#%%#";

    public final static Pattern HAN_ZI_dou = Pattern.compile("[\u4e00-\u9fa5|\\,|\\s|\\-]+");
    public final static Pattern LETTERS_dou = Pattern.compile("[\\w|\\,|\\s|\\-]+");
    public final static Pattern HANZI_LETTERS_dou = Pattern.compile("[\u4e00-\u9fa5|\\w|\\,|\\s|\\-]+");

    public static boolean isAllHanzi(String s) {
        if (StringUtils.isBlank(s)) {
            return false;
        }
        return HAN_ZI.matcher(s.replaceAll("\\s", "")).matches();
    }

    public static boolean hasHanZi(String s) {
        if (StringUtils.isBlank(s)) {
            return false;
        }
        return HAVE_HAN_ZI.matcher(s.replaceAll("\\s", "")).matches();
    }

    /**
    * 汉字的code codePointAt
    * 
    * @param s
    * @return
    */
    public static String wordCode(String s) {
        String rs = "";
        s = s.toLowerCase();
        if (isNumberOrLetters(s)) {
            return s;
        }
        List<Integer> codes = new ArrayList<Integer>();
        for (int i = 0, len = s.length(); i < len; i++) {
            int cv = s.codePointAt(i);
            if (cv == 32) {
                continue;
            }
            codes.add(cv);
        }
        Collections.sort(codes, _comparator);
        for (Integer code : codes) {
            rs += String.valueOf(code) + "_";
        }
        return rs;
    }

    public static boolean isNotNull(String v) {
        return v != null && !v.trim().isEmpty() && StringUtils.isNotBlank(v);
    }

    public static boolean isNumeric(String s) {
        if (!isNotNull(s))
            return false;
        return StaticValue.Regex.NUMERIC.matcher(s).matches();
    }

    public static Integer getInt(String v, int dft) {
        if (isNumeric(v)) {
            return (int) Double.parseDouble(v);
        }
        return dft;
    }

    public static Integer getIntWithDelete(String v, int dft) {
        if (isBlank(v)) {
            return dft;
        }
        return getInt(v.replaceAll(StaticValue.Regex.NO_NUMERIC.pattern(), ""), dft);
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

    public static boolean isNumber(String s) {
        if (!isNotNull(s))
            return false;
        return StaticValue.Regex.NUMBER.matcher(s).matches();
    }

    public static Long getLong(String v, long dft) {
        if (isNumeric(v)) {
            return (long) Double.parseDouble(v);
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
        if (isNotNull(v)) {
            if (v.equalsIgnoreCase("true")) {
                return true;
            }
            if (getInt(v, -1) > 0) {
                return true;
            }
        }
        return dft;
    }

    public static Boolean getBoolean(Boolean v, boolean dft) {
        if (v == null) {
            return dft;
        }
        return v;
    }

    public static String getString(String s, String dft) {
        if (isNotNull(s)) {
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

    public static String join(Collection<Object> values, String sep) {
        StringBuilder sb = new StringBuilder("");

        if (values != null && values.size() > 0) {
            for (Object o : values) {
                sb.append(o.toString()).append(sep);
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - sep.length());
            }
        }

        return sb.toString();
    }

    public static String joinList(List<Long> values, String sep) {
        StringBuilder sb = new StringBuilder("");

        if (values != null && values.size() > 0) {
            for (Long o : values) {
                sb.append(o.toString()).append(sep);
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - sep.length());
            }
        }

        return sb.toString();
    }

}
