package org.common4s.analyzer;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

import com.hunteron.common4s.analyzer.zeus.core.ZeusDictionary;
import com.hunteron.common4s.analyzer.zeus.core.ZeusSegmenter;
import com.hunteron.common4s.analyzer.zeus.dic.ResultHit;
import com.hunteron.common4s.jdk.JunitConsoleOut;

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
