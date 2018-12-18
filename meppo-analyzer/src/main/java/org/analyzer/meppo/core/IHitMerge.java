package org.analyzer.meppo.core;

import java.util.List;

import org.analyzer.meppo.dic.ResultHit;


/**
 * @author Leo
 * @date 2016年2月1日  下午6:04:08
 */
public interface IHitMerge {

	public List<ResultHit> merge(List<ResultHit> hits);
}
