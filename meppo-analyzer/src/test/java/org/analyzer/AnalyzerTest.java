package org.analyzer;

import java.util.List;

import org.analyzer.sdk.JunitConsoleOut;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

import org.analyzer.meppo.core.ZeusDictionary;
import org.analyzer.meppo.core.ZeusSegmenter;
import org.analyzer.meppo.dic.ResultHit;

public class AnalyzerTest extends JunitConsoleOut {

    @Before
    public void init() {
        ZeusDictionary.getInstance().initDictionary();
    }

    @Test
    public void test_zeus() {
        ZeusSegmenter seg = new ZeusSegmenter("工作1经验", "work_");
        
        List<ResultHit> hits = seg.analyze();
        
        if(CollectionUtils.isNotEmpty(hits)) {
            for(ResultHit h : hits) {
                console("{}[{}] : {}-{}", new Object[]{
                        h.getWord(), h.getKey(), h.getBegin(), h.getEnd()
                });
            }
        }
    }
}
