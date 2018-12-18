package com.hunteron.common4s.analyzer.zeus.core;

import java.util.ArrayList;
import java.util.List;

import com.hunteron.common4s.analyzer.zeus.ZeusUtils;
import com.hunteron.common4s.analyzer.zeus.dic.Hit;


public class TitleSegmenter {
	
	//存放历史匹配结果
	private List<Hit> hitHistory = new ArrayList<>(1);
	
	public void analyzer(ZeusAnalyzerContext context) {
		
		if(!this.hitHistory.isEmpty()) { //历史
			
			Hit[] historyArray = this.hitHistory.toArray(new Hit[this.hitHistory.size()]);
			for(Hit hit : historyArray) {
				int cursor = context.getCursor();
				
				//记录当前分词的信息
				int segMaxSpan = hit.getSegment().getRistrictMaxSpan();
				int segMinSpan = hit.getSegment().getRistrictMinSpan();
				int segEnd = hit.getEnd();
				//目前的hit
				Hit buff = hit.copy();
				
				//重新匹配的hit
				hit = ZeusDictionary.getInstance().matchWithHit(context.getSegmentBuffer(), cursor, hit);

				//当前hit的数据
				int thisEnd = hit.getEnd();
				
				//两个hit的距离
				int span = thisEnd - segEnd - 1;
				
				//如果hit后移，产生【span：间隔，相邻的两个字符，span为0】，表示hit发生变化
				if(span >= 0) {

					//如果当前的hit发生了变化，而且此hit之前是具有约束条件，则需要重新将之前的hit加入历史处理列表
					if(buff.getSegment().getRistrictMaxSpan() > 0) {
						hitHistory.add(buff);
					}
					
					if(hit.canBeWord(context.getKeyPrev()) &&  canBeWord(context.getSegmentBuffer(), hit.getSegment().getEndWord(), cursor)) {
						context.addResult(hit);
					} 
					
					if(segMaxSpan > 0 && span > segMaxSpan) {
						hitHistory.remove(hit);
					}
					if(segMinSpan > 0 && span < segMinSpan) {
						hitHistory.remove(hit);
					}
				} else {
					
					//hit没有后移，而且没有约束条件，则删除当前的hit
					if(segMaxSpan < 0 || (cursor - thisEnd >= segMaxSpan)) {
						hitHistory.remove(hit);
					}
				}
			}
		}
		
		
		//分词时，针对英文成词需要在进行处理
		int cursor = context.getCursor();
		Hit searchHit = ZeusDictionary.getInstance().matchWithMainDic(context.getSegmentBuffer(), cursor);
		
		if(searchHit.getSegment() != null) {
			
			if(searchHit.canBeWord(context.getKeyPrev()) && canBeWord(context.getSegmentBuffer(), searchHit.getSegment().getEndWord(), cursor)) {
			    //添加结果
				context.addResult(searchHit);
			}
			//可能匹配的，添加匹配记录
			hitHistory.add(searchHit);
		}
	}
	
	private boolean canBeWord(char[] buff, String word, int currentCursor) {
		int size = buff.length;
		
		int prevCursor = currentCursor - word.length();
		int nextCursor = currentCursor + 1;
		Character prev = null ,next = null;
		
		if(prevCursor >= 0) {
			prev = buff[prevCursor];
		}
		if(nextCursor < size) {
			next = buff[nextCursor];
		}
	
		return ZeusUtils.canBeFinalWord(prev, word, next);
	}
	
	public void reset() {
		hitHistory = new ArrayList<>(0);
	}
}
