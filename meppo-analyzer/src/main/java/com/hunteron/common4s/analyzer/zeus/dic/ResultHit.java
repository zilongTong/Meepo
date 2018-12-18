package com.hunteron.common4s.analyzer.zeus.dic;


/**
 * @author Smile.Wu
 * @date 2016年2月1日  上午11:50:24
 */
public class ResultHit {

	private String word;
	private int begin;
	private int end;
	private String key;
	private String value;
	public ResultHit(Hit h) {
		DicSegment seg = h.getSegment();
		if(seg != null) {
			word = seg.getEndWord();
			this.begin = h.getBegin();
			this.end = h.getEnd();
			this.key = seg.getMatchKey();
			this.value = seg.getValue();
		}
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
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
	public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    //合并
	public boolean merge(ResultHit hit) {
		if(this.begin <= hit.begin && this.end >= hit.end) {
			return true;
		}
		if(this.begin >= hit.begin && this.end <= hit.end) {
			this.word = hit.word;
			this.begin = hit.begin;
			this.end = hit.end;
			return true;
		}
		
		if(hit.begin >= this.end) {
			this.word = this.word + " " + hit.getWord();
			this.end = hit.getEnd();
			return true;
		}
		if(hit.getEnd() <= this.begin) {
			this.word = hit.getWord() + " " + this.word;
			this.begin = hit.begin;
			return true;
		}
		
		return false;
	}
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
