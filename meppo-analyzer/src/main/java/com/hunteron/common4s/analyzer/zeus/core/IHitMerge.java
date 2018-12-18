package com.hunteron.common4s.analyzer.zeus.core;

import java.util.List;

import com.hunteron.common4s.analyzer.zeus.dic.ResultHit;


/**
 * @author Smile.Wu
 * @date 2016年2月1日  下午6:04:08
 */
public interface IHitMerge {

	public List<ResultHit> merge(List<ResultHit> hits);
}
