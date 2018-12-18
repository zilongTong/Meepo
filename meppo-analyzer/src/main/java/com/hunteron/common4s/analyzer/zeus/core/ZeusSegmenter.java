package com.hunteron.common4s.analyzer.zeus.core;

import java.util.ArrayList;
import java.util.List;

import com.hunteron.common4s.analyzer.zeus.dic.ResultHit;


public class ZeusSegmenter {

	private ZeusAnalyzerContext context;
	
	private TitleSegmenter segmenter;
	
    public ZeusSegmenter(String line, String keyPrev) {
        init(line);
        context.setKeyPrev(keyPrev);
    }
	
	public ZeusSegmenter(String line) {
	    init(line);
	}
	
    private void init(String line) {
        segmenter = new TitleSegmenter();
        
        context = new ZeusAnalyzerContext(line);
	}
	
	public List<ResultHit> analyze() {
		return analyze(null);
	}

	public List<ResultHit> analyze(IHitMerge merge) {
		if(!context.hasBuffer()) {
			return new ArrayList<>(0);
		}
		
		List<ResultHit> rs = null;
		while((rs = context.getResults()) == null || rs.size() < 1) {
			if(!context.hasBuffer()) {
				return null;
			}
			do {
				segmenter.analyzer(context);
				
			} while(context.moveNext());
			
			/*
			 * 如果文本输入是Reader对象，则reset的作用就是，重置分词组件；然后context重新从Reader中填充字符数组，重新分词
			 */
			segmenter.reset();
		}
		
		if(merge != null) {
			return merge.merge(rs);
		}
		
		return rs;
	}
}
