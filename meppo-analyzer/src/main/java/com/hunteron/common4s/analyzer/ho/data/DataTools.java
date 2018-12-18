package com.hunteron.common4s.analyzer.ho.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.hunteron.common4s.analyzer.ho.dic.HODicTerm;
import com.hunteron.common4s.analyzer.ho.util.HORecoAnalyzerUtils;
import com.hunteron.common4s.jdk.resource.file.ResourceReader;


/**
 *
 * @author Smile.Wu
 * @version 2015-9-16
 */
public class DataTools {

	public static String positionJd() {
		return getFileText("/txt/ho_position_info.txt");
	}
	public static String talentCV() {
		return getFileText("/txt/ho_talent_info.txt");
	}
	
	public static String getFileText(String resourcePath) {
		ResourceReader reader = null;
		StringBuilder result = new StringBuilder("");
		try {
			reader = new ResourceReader(DataTools.class.getResourceAsStream(resourcePath), "utf-8");
			reader.load();
			String line = null;
			while((line = reader.readLine()) != null) {
				result.append(line).append("\n");
			}
			reader.close();
			
		} catch (Exception e) {
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					
				}
			}
		}
		return result.toString();
	}
	
	public static List<HODicTerm> readDicTermsFromFile(String resourcePath) {
		ResourceReader reader = null;
		List<HODicTerm> resultList = new ArrayList<HODicTerm>();
		try {
			reader = new ResourceReader(DataTools.class.getResourceAsStream(resourcePath), "utf-8");
			reader.load();
			String line = null;
			List<String> titleLines = new ArrayList<>(20);
			while((line = reader.readLine()) != null) {
				if(line.startsWith("100:") || line.startsWith("101:")) {
					titleLines.add(line);
					continue;
				}
				HODicTerm term = HORecoAnalyzerUtils.createTermFromLine(line);
				if(term != null) {
					resultList.add(term);
				}
			}
			
			List<HODicTerm> terms = HORecoAnalyzerUtils.createTermFromLine(titleLines);
			if(CollectionUtils.isNotEmpty(terms)) {
				for(HODicTerm term : terms) {
					resultList.add(term);
				}
			}
			
			reader.close();
			
		} catch (Exception e) {
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					
				}
			}
		}
		return resultList;
	}
}
