package org.analyzer.meppo.core;

import java.util.ArrayList;
import java.util.List;

import org.analyzer.meppo.dic.Hit;
import org.analyzer.meppo.dic.ResultHit;


public class ZeusAnalyzerContext {

	//待分词
	private char[] segmentBuffer;
	private int size;
	//游标
	private int cursor;
	
	private String keyPrev;
	
	private List<ResultHit> results = new ArrayList<>(1);
	
	public ZeusAnalyzerContext(String line) {
		this.cursor = 0;
		segmentBuffer = line.toLowerCase().toCharArray();
		this.size = segmentBuffer.length;
	}

	public ZeusAnalyzerContext() {
		
	}

	public String getKeyPrev() {
        return keyPrev;
    }

    public void setKeyPrev(String keyPrev) {
        this.keyPrev = keyPrev;
    }

    public void addResult(Hit hit) {
		results.add(new ResultHit(hit));
	}
	public List<ResultHit> getResults() {
		return results;
	}
	public void setResults(List<ResultHit> results) {
		this.results = results;
	}
	public char[] getSegmentBuffer() {
		return segmentBuffer;
	}
	public int getCursor() {
		return cursor;
	}
	
	public void clear() {
		this.results.clear();
	}
	
	public boolean moveNext() {
		if(cursor < size - 1) {
			cursor ++;
			return true;
		}
		return false;
	}
	
	public boolean hasBuffer() {
		return cursor < size - 1;
	}
    
	/*
	 * 填充缓冲
	 */
    public void fillBuffer() {
        throw new UnsupportedOperationException("not support [fillBuffer]");
    }
}
