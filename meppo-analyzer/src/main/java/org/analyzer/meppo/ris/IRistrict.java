package org.analyzer.meppo.ris;

/**
 * 分词的约束条件
 * 
 * 两个关键词分词之间的约束关系
 * 	e. java{0,5}开发	： “java” 和 “开发” 两个分词之间，可以跨越0到5个字符，这个约束关系是有“java”和“开发”公共作用生效的
 * @author wjyuian
 *
 */
public interface IRistrict {

	public boolean accept();

	public int getMinSpan();

	public int getMaxSpan();
}
