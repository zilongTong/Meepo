package org.analyzer.meppo.dic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.analyzer.meppo.ris.IRistrict;
import org.apache.commons.lang.StringUtils;

import org.analyzer.meppo.AnalyzerUtils;

/**
 * 词典内容
 * 
 * @author wjyuian
 *
 */
public class DicSegment {

    private final static int MAX_CHILDREN_ARRAY_LENGTH = 5;

    private Character current;

    /*
     * 当前字符与【前驱】字符之间的约束条件， 也就是说，当前的约束条件应该是属于上一个字符串，但是由于处理方式问题，保存在后一个字符数较为合理
     * 分词时，DicSegment的子父关系，决定了“子可以确定父，父不能确定子”，因此，约束关系存放在子节点，可以唯一确定子父两个分词
     */
    private IRistrict ristrict = null;

    // 当前字符的【后继】约束条件的最大跨度，如果是-1，表示当前字符后面没有约束条件
    private int ristrictMaxSpan = -1;
    private int ristrictMinSpan = -1;

    private DicSegment[] childrenArray;

    private Map<Character, DicSegment> childrenMap;

    private int subSize = 0;
    // 成词
    private boolean canBeEnd;

    private String endWord;

    private String matchKey = "";

    private List<String> keys = new ArrayList<String>();

    private String value;

    public boolean hasKeyStartWith(String keyStart) {
        if (StringUtils.isBlank(keyStart)) {
            return true;
        }
        matchKey = "";
        for (String tempKey : keys) {
            if (tempKey != null && tempKey.startsWith(keyStart)) {
                matchKey = tempKey;
                return true;
            }
        }
        return false;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public void addKey(String key) {
        this.keys.add(key);
        this.matchKey = key;
    }

    public String getMatchKey() {
        return matchKey;
    }

    public void setMatchKey(String matchKey) {
        this.matchKey = matchKey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if (StringUtils.isBlank(this.value)) {
            this.value = value;
        }
    }

    public DicSegment(Character ch) {
        this.current = ch;
        init();
    }

    public DicSegment(Character ch, IRistrict ristrict) {
        this.current = ch;
        this.ristrict = ristrict;
        init();
    }

    public void init() {
        childrenArray = new DicSegment[MAX_CHILDREN_ARRAY_LENGTH];
        childrenMap = null;
    }

    public String getEndWord() {
        return endWord;
    }

    public void setEndWord(String endWord) {
        this.endWord = endWord;
    }

    public boolean isCanBeEnd() {
        return canBeEnd;
    }

    public void setCanBeEnd() {
        this.canBeEnd = true;
    }

    /**
     * 添加子节点
     * 
     * @param subSeg
     */
    public DicSegment addChildrenDicSegment(DicSegment subSeg) {
        if (subSize < MAX_CHILDREN_ARRAY_LENGTH) {
            DicSegment[] array = childrenArray;
            DicSegment old = searchSegmentArray(array, subSeg.current);
            if (old != null) {
                return old;
            }
            childrenArray[subSize] = subSeg;
            subSize++;
        } else {
            if (childrenArray != null) {
                childrenMap = new HashMap<>(10);
                for (DicSegment seg : childrenArray) {
                    childrenMap.put(seg.current, seg);
                }
                childrenArray = null;
            }
            DicSegment old = childrenMap.get(subSeg.current);
            if (old != null) {
                return old;
            }
            childrenMap.put(subSeg.current, subSeg);
        }
        return subSeg;
    }

    // 记录最大的跨越值
    public void setNextRistrictMaxSpan(IRistrict ristrict) {
        int min = ristrict.getMinSpan();
        int max = ristrict.getMaxSpan();
        if (max > ristrictMaxSpan) {
            ristrictMaxSpan = max;
        }
        if (ristrictMinSpan < 0 || min < ristrictMinSpan) {
            ristrictMinSpan = min;
        }
    }

    /**
     * 从指定的hit里面进行match
     * 
     * @param chars
     * @param begin
     * @param length
     * @param hit
     * @return
     */
    public Hit matchArray(char[] chars, int begin, int length, Hit hit) {
        if (hit == null) {
            hit = new Hit();
            hit.setBegin(begin);
        }

        Character searchChar = chars[begin];
        DicSegment ds = null;

        DicSegment[] charArrayTemp = this.childrenArray;
        if (charArrayTemp != null) {
            ds = searchSegmentArray(charArrayTemp, searchChar);
        } else {
            ds = this.childrenMap.get(searchChar);
        }

        if (ds != null) {
            hit.setSegment(ds);
            hit.setEnd(begin);
        }

        return hit;
    }

    public DicSegment searchSegmentArray(DicSegment[] charArrayTemp, Character target) {
        for (DicSegment search : charArrayTemp) {
            if (search != null && search.current.compareTo(target) == 0) {
                return search;
            }
        }
        return null;
    }

    public int getRistrictMaxSpan() {
        return ristrictMaxSpan;
    }

    public int getRistrictMinSpan() {
        return ristrictMinSpan;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        sb.append("seg[char=").append(current);

        if (childrenArray != null) {
            sb.append(", sub:");
            for (DicSegment seg : childrenArray) {
                if (seg != null) {
                    sb.append("").append(seg.toString());
                }
            }
        }
        if (childrenMap != null) {
            sb.append(", sub:");
            for (DicSegment seg : childrenMap.values()) {
                sb.append("").append(seg.toString());
            }
        }
        if (ristrict != null) {
            sb.append(", ristrict=").append(ristrict);
        }
        if (AnalyzerUtils.isNotBlank(endWord)) {
            sb.append(", endWord=").append(endWord);
        }
        sb.append("]");
        return sb.toString();
    }

}
