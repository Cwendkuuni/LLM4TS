package com.example.junit;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SplitterTest {

    private Splitter stringSplitter;
    private Splitter charSplitter;
    private Splitter patternSplitter;
    private Splitter fixedLengthSplitter;
    private Splitter patternAndTrimSplitter;
    private Splitter omitEmptyStringsSplitter;
    private Splitter limitSplitter;
    private Splitter omitEmptyStringsAndLimitSplitter;
    private Splitter trimResultsSplitter;
    private Splitter trimResultsAndLimitSplitter;

    @Before
    public void setUp() {
        stringSplitter = Splitter.on(" ");
        charSplitter = Splitter.on(' ');
        patternSplitter = Splitter.onPattern("[\\s,]");
        fixedLengthSplitter = Splitter.fixedLength(3);
        patternAndTrimSplitter = Splitter.onPattern("\\s+").trimResults();
        omitEmptyStringsSplitter = stringSplitter.omitEmptyStrings();
        limitSplitter = stringSplitter.limit(3);
        omitEmptyStringsAndLimitSplitter = omitEmptyStringsSplitter.limit(2);
        trimResultsSplitter = stringSplitter.trimResults();
        trimResultsAndLimitSplitter = trimResultsSplitter.limit(2);
    }

    @Test
    public void testOn() {
        checkArgument(stringSplitter != null);
        checkArgument(charSplitter != null);
        checkArgument(patternSplitter != null);
    }

    @Test
    public void testOnString() {
        testSplit("", stringSplitter, Arrays.asList());
        testSplit("", charSplitter, Arrays.asList());
        testSplit("", patternSplitter, Arrays.asList());
        testSplit("", fixedLengthSplitter, Arrays.asList("", "", ""));

        testSplit("", patternAndTrimSplitter, Arrays.asList());
        testSplit("", patternAndTrimSplitter, Arrays.asList());
        testSplit("", patternAndTrimSplitter, Arrays.asList());

        testSplit("", omitEmptyStringsSplitter, Arrays.asList());
        testSplit("", omitEmptyStringsSplitter, Arrays.asList());
        testSplit("", omitEmptyStringsSplitter, Arrays.asList());

        testSplit("", limitSplitter, Arrays.asList("", "", ""));
        testSplit("", limitSplitter, Arrays.asList("", "", "", "", ""));

        testSplit("", trimResultsSplitter, Arrays.asList("", "", ""));
        testSplit("", trimResultsAndLimitSplitter, Arrays.asList("", "", ""));
        testSplit("", trimResultsAndLimitSplitter, Arrays.asList("", "", "", "", ""));

        testSplit("abc def", stringSplitter, Arrays.asList("abc", "def"));
        testSplit("abc def", charSplitter, Arrays.asList("abc", "def"));
        testSplit("abc def", patternSplitter, Arrays.asList("abc", "", "def"));
        testSplit("abc def", fixedLengthSplitter, Arrays.asList("abc", "def", ""));

        testSplit(" abc def ", patternAndTrimSplitter, Arrays.asList("abc", "def"));
        testSplit("abc   def", patternAndTrimSplitter, Arrays.asList("abc", "def"));
        testSplit(" abc def ", patternAndTrimSplitter, Arrays.asList("abc", "def"));

        testSplit("abc def", omitEmptyStringsSplitter, Arrays.asList("abc", "def"));
        testSplit("abc def ghi", omitEmptyStringsSplitter, Arrays.asList("abc", "def"));
        testSplit("abc def ghi", omitEmptyStringsSplitter, Arrays.asList("abc", "def"));

        testSplit("abc def", limitSplitter, Arrays.asList("abc", "def"));
        testSplit("abc def ghi", limitSplitter, Arrays.asList("abc", "def", "ghi"));
        testSplit("abc def ghi jkl", limitSplitter, Arrays.asList("abc", "def", "ghi"));

        testSplit("abc def", trimResultsSplitter, Arrays.asList("abc", "def"));
        testSplit("abc def ghi", trimResultsSplitter, Arrays.asList("abc", "def"));
        testSplit("abc def ghi jkl", trimResultsSplitter, Arrays.asList("abc", "def"));

        testSplit("abc def", trimResultsAndLimitSplitter, Arrays.asList("abc", "def"));
        testSplit("abc def ghi", trimResultsAndLimitSplitter, Arrays.asList("abc", "def"));
        testSplit("abc def ghi jkl", trimResultsAndLimitSplitter, Arrays.asList("abc", "def"));
    }

    @Test
    public void testOnPattern() {
        testSplit("", patternSplitter, Arrays.asList());
        testSplit("", patternSplitter, Arrays.asList());
        testSplit("", patternSplitter, Arrays.asList());

        testSplit("", patternAndTrimSplitter, Arrays.asList());
        testSplit("", patternAndTrimSplitter, Arrays.asList());
        testSplit("", patternAndTrimSplitter, Arrays.asList());

        testSplit("abc def ghi", patternSplitter, Arrays.asList("abc", "", "def", "", "ghi"));
        testSplit("abc def ghi", patternAndTrimSplitter, Arrays.asList("abc", "def", "ghi"));
        testSplit("abc def ghi jkl", patternSplitter, Arrays.asList("abc", "", "def", "", "ghi", "", "jkl"));
        testSplit("abc def ghi jkl", patternAndTrimSplitter, Arrays.asList("abc", "def", "ghi", "jkl"));

        testSplit("abc def ghi", patternSplitter, Arrays.asList("abc", "def", "ghi"));
        testSplit("abc def ghi jkl", patternSplitter, Arrays.asList("abc", "def", "ghi", "jkl"));

        testSplit("abc def ghi jkl mno", patternSplitter, Arrays.asList("abc", "def", "ghi", "jkl", "mno"));
        testSplit("abc def ghi jkl mno pqr", patternSplitter, Arrays.asList("abc", "def", "ghi", "jkl", "mno", "pqr"));
    }

    @Test
    public void testFixedLength() {
        testSplit("", fixedLengthSplitter, Arrays.asList("", "", ""));
        testSplit("", fixedLengthSplitter, Arrays.asList(""));
        testSplit("", fixedLengthSplitter, Arrays.asList(""));

        testSplit("abc def ghi", fixedLengthSplitter, Arrays.asList("abc", "def", "ghi"));
        testSplit("abc def ghi jkl", fixedLengthSplitter, Arrays.asList("abc", "def", "ghi", "jkl"));
        testSplit("abc def ghi jkl mno pqr", fixedLengthSplitter, Arrays.asList("abc", "def", "ghi", "jkl", "mno", "pqr"));
    }

    @Test
    public void testMapSplitter() {
        Map<String, String> map = splitStringToMap("", patternAndTrimSplitter);
        assertEquals(0, map.size());

        map = splitStringToMap("key1=value1 key2=value2 key3=value3", patternAndTrimSplitter);
        assertEquals(3, map.size());
        assertEquals("key1", map.keySet().iterator().next());
        assertEquals("value1", map.get("key1"));

        map = splitStringToMap("key1=value1 key2=value2 key3=value3 key4=value4", patternAndTrimSplitter);
        assertEquals(4, map.size());
        assertTrue(map.containsKey("key1"));
        assertTrue(map.containsKey("key2"));
        assertTrue(map.containsKey("key3"));
        assertTrue(map.containsKey("key4"));

        map = splitStringToMap("key1=value1 key2=value2 key3=value3 key4=value4 key5=value5 key6=value6", patternAndTrimSplitter);
        assertEquals(6, map.size());
        assertTrue(map.containsKey("key1"));
        assertTrue(map.containsKey("key2"));
        assertTrue(map.containsKey("key3"));
        assertTrue(map.containsKey("key4"));
        assertTrue(map.containsKey("key5"));
        assertTrue(map.containsKey("key6"));

        map = splitStringToMap("key1=value1 key2=value2 key3=value3 key4=value4 key5=value5 key6=value6 key7=value7", patternAndTrimSplitter);
        assertEquals(7, map.size());
        assertTrue(map.containsKey("key1"));
        assertTrue(map.containsKey("key2"));
        assertTrue(map.containsKey("key3"));
        assertTrue(map.containsKey("key4"));
        assertTrue(map.containsKey("key5"));
        assertTrue(map.containsKey("key6"));
        assertTrue(map.containsKey("key7"));
    }

    private void testSplit(String input, Splitter splitter, List<String> expectedOutput) {
        List<String> output = new ArrayList<>();
        for (String s : splitter.split(input)) {
            output.add(s);
        }
        assertEquals(expectedOutput, output);
    }

    private Map<String, String> splitStringToMap(String input, Splitter splitter) {
        Map<String, String> map = splitter.withKeyValueSeparator("=").split(input);
        return (Map<String, String>) map;
    }
}