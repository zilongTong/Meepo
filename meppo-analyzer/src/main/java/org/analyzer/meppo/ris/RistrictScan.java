package org.analyzer.meppo.ris;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import org.analyzer.meppo.AnalyzerUtils;


public class RistrictScan {
	private static final int leftBrace = '{';
	private static final int rightBrace = '}';

	private String word = null;
	
	private int ristrictNextIndex = -1;
	
	private IRistrict ristrict = null;
	
	private String key = null;
	
	public RistrictScan(String line) {
		word = line;
	}
	
	public static List<RistrictScan> create(String line) {
		List<RistrictScan> rss = new ArrayList<>(0);
		if(AnalyzerUtils.isNotBlank(line)) {
		    String key = null;
		    int keyIndex = line.indexOf("::");
		    if(keyIndex > 0) {
		        key = line.substring(0, keyIndex);
		        line = line.substring(keyIndex + 2);
		        
		    }
		    
			int leftBraceIndex = line.indexOf(leftBrace);
			int rightBraceIndex = line.indexOf(rightBrace);
			int length = line.length();
			
			if(leftBraceIndex < 0 || rightBraceIndex < 0) {
				//不存在{}，或者}是最后字符，}小于{+2
				if(line.indexOf('=') > 0) {	//普通的拼接
					List<String> words = AnalyzerUtils.createFromLine(line, "=");
					rss = new ArrayList<>(words.size());
					RistrictScan temp = null;
					for(String word : words) {
						temp = new RistrictScan(word);
						temp.setKey(key);
						rss.add(temp);
					}
					return rss;
				} else {
				    String[] words = line.split(",");
				    for(String word : words) {
				        if(StringUtils.isNotBlank(word)) {
			                //普通词库
			                RistrictScan rs = new RistrictScan(word);
			                rs.setKey(key);
			                rss.add(rs);
				        }
				    }
				}
				
				return rss;
			} else {
				if(rightBraceIndex - leftBraceIndex < 2 || rightBraceIndex == length || rightBraceIndex == 0) {
					return rss;
				}
				
				String regex = line.substring(leftBraceIndex + 1, rightBraceIndex);
				LengthRistrict lengthRistrict = new LengthRistrict(regex);
				
				//带约束条件的拼接
				if(lengthRistrict.getMaxSpan() > 0) {//有效的约束条件
					String prev = line.substring(0, leftBraceIndex);
					
					String after = line.substring(rightBraceIndex + 1);
					
					String[] prevs = prev.split(",");
					String[] afters = after.split(",");
					rss = new ArrayList<>(prevs.length * afters.length);
					RistrictScan temp = null;
					for(String pv : prevs) {
						for(String aft : afters) {
							String word = pv + aft;
							temp = new RistrictScan(word);
							temp.ristrict = lengthRistrict;
							temp.ristrictNextIndex = pv.length();
	                        temp.setKey(key);
							rss.add(temp);
						}
					}
					return rss;
				}
			}
	        RistrictScan rs = new RistrictScan(line);
	        rs.setKey(key);
	        rss.add(rs);
		}
		return rss;
	}
	
	public boolean isValid() {
		return AnalyzerUtils.isNotBlank(word);
	}

	public String getWord() {
		return word;
	}

	public static int getLeftbrace() {
		return leftBrace;
	}

	public static int getRightbrace() {
		return rightBrace;
	}

	public int getRistrictNextIndex() {
		return ristrictNextIndex;
	}

	public IRistrict getRistrict() {
		return ristrict;
	}

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}
