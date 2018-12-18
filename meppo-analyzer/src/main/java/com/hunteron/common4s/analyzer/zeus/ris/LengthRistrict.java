package com.hunteron.common4s.analyzer.zeus.ris;

import com.hunteron.common4s.analyzer.zeus.ZeusUtils;


public class LengthRistrict implements IRistrict {

	private int minSpan = -1;
	
	private int maxSpan = -1;
	
	public LengthRistrict(String regex) {
		if(ZeusUtils.isNotBlank(regex)) {
			String[] splitValues = regex.split(",");
			int len = splitValues.length;
			if(len ==  0) {
				return;
			}
			if(len == 1) {
				maxSpan = ZeusUtils.getInt(splitValues[0], -1);
			} else {
				int left = ZeusUtils.getInt(splitValues[0], -1);
				int right = ZeusUtils.getInt(splitValues[1], -1);
				
				minSpan = Math.min(left, right);
				maxSpan = Math.max(left, right);
			}
		}
	}
	
	@Override
	public boolean accept() {
		return false;
	}

	public int getMinSpan() {
		return minSpan;
	}

	public void setMinSpan(int minSpan) {
		this.minSpan = minSpan;
	}

	public int getMaxSpan() {
		return maxSpan;
	}

	public void setMaxSpan(int maxSpan) {
		this.maxSpan = maxSpan;
	}
	
	@Override
	public String toString() {
		return "[" + minSpan + "-" + maxSpan + "]";
	}
}
