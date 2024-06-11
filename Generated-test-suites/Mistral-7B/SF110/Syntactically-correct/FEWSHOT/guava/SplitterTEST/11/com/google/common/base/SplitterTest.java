package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class SplitterTest {

    @Test
    public void testSplitterOnCharacter() {
        Splitter splitter = Splitter.on('-');
        String testString = "test-test-test-test";
        List<String> expectedList = Arrays.asList("test", "test", "test", "test");
        List<String> actualList = splitter.split(testString);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testSplitterOnPattern() {
        String separatorPattern = "[,-]";
        Splitter splitter = Splitter.onPattern(separatorPattern);
        String testString = "test-,test-test,-test";
        List<String> expectedList = Arrays.asList("test", "", "test", "test");
        List<String> actualList = splitter.split(testString);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testSplitterOnString() {
        Splitter splitter = Splitter.on("-");
        String testString = "test-test-test-test";
        List<String> expectedList = Arrays.asList("test", "test", "test", "test");
        List<String> actualList = splitter.split(testString);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testSplitterOnRegex() {
        String separatorPattern = "[,-]";
        Splitter splitter = Splitter.onPattern(Pattern.compile(separatorPattern));
        String testString = "test-,test-test,-test";
        List<String> expectedList = Arrays.asList("test", "", "test", "test");
        List<String> actualList = splitter.split(testString);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testSplitterOnFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        String testString = "test1test2test3test4";
        List<String> expectedList = Arrays.asList("test1", "test2", "test3", "test4");
        List<String> actualList = splitter.split(testString);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testSplitterWithKeyValueSeparator() {
        String separator = "-";
        Splitter keyValueSplitter = Splitter.on(separator);
        Splitter outerSplitter = Splitter.on(separator);
        Map<String, String> expectedMap = new LinkedHashMap<>();
        expectedMap.put("key1", "value1");
        expectedMap.put("key2", "value2");
        expectedMap.put("key3", "value3");

        String testString = "key1-value1-key2-value2-key3-value3";
        Map<String, String> actualMap = outerSplitter.withKeyValueSeparator(keyValueSplitter).split(testString);
        assertEquals(expectedMap, actualMap);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSplitterWithNullSequence() {
        Splitter splitter = Splitter.on('-');
        splitter.split(null);
    }

    @Test
    public void testSplitterWithEmptySequence() {
        Splitter splitter = Splitter.on('-');
        List<String> actualList = splitter.split(Character.toString(''));
        List<String> expectedList = Arrays.asList();
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testSplitterWithLimit() {
        Splitter splitter = Splitter.on('-').limit(2);
        String testString = "test1-test2-test3-test4-test5";
        List<String> expectedList = Arrays.asList("test1-test2", "test3-test4");
        List<String> actualList = splitter.split(testString);
        assertEquals(expectedList, actualList);
    }
}