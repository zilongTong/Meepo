package com.hunteron.common4s.analyzer.zeus.dic;

import org.apache.commons.lang.StringUtils;

public class Hit {

	private DicSegment segment;
	
	//当前seg的开始位置
	private int begin;
	
	//当前seg的结束位置
	private int end;
	
	public boolean canBeWord(String keyPrev) {
	    if(StringUtils.isEmpty(keyPrev)) {
	        return this.segment.isCanBeEnd();
	    }
        return this.segment.isCanBeEnd() && this.segment.hasKeyStartWith(keyPrev);
    }

	public Hit() {
	}
	public Hit(DicSegment segment) {
		super();
		this.segment = segment;
	}

	public DicSegment getSegment() {
		return segment;
	}

	public void setSegment(DicSegment segment) {
		this.segment = segment;
	}

	public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}
	
	public Hit copy() {
		Hit hit = new Hit(this.segment);
		hit.begin = this.begin;
		hit.end = this.end;
		return hit;
	}
}
