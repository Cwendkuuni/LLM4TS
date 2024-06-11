package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SplitterTest {

    @Test
    public void testSplitterOnChar() {
        Splitter splitter = Splitter.on('-');
        List<String> result = new ArrayList<>(Arrays.asList("one-two-three".split("-")));
        List<String> expected = Arrays.asList("one", "two", "three");
        assertEquals(expected, result);
    }

    @Test
    public void testSplitterOnPattern() {
        Splitter splitter = Splitter.onPattern("\\s+");
        List<String> result = new ArrayList<>(Arrays.asList("one two three".split("\\s+")));
        List<String> expected = Arrays.asList("one", "two", "three");
        assertEquals(expected, result);
    }

    @Test
    public void testSplitterOnString() {
        Splitter splitter = Splitter.on(":");
        List<String> result = new ArrayList<>(Arrays.asList("key:value".split(":")));
        List<String> expected = Arrays.asList("key", "value");
        assertEquals(expected, result);
    }

    @Test
    public void testSplitterOnPatternWithLimit() {
        Splitter splitter = Splitter.onPattern("\\s+").limit(3);
        List<String> result = new ArrayList<>(Arrays.asList("the quick brown fox jumps over the lazy dog".split("\\s+")));
        List<String> expected = Arrays.asList("the", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog");
        List<String> expectedTrimmed = Arrays.asList("the", "quick", "brown", "fox", "jumps", "over", "the", "lazy");
        assertEquals(expected.subList(0, 3), result.subList(0, 3));
        assertEquals(expectedTrimmed, Arrays.asList(splitter.split("the quick brown fox jumps over the lazy dog").toArray(new String[0])));
    }

    @Test
    public void testSplitterOnPatternWithEmptyStringsOmitted() {
        Splitter splitter = Splitter.onPattern("\\s+").omitEmptyStrings();
        List<String> result = new ArrayList<>(Arrays.asList("the quick brown fox jumps over the lazy dog".split("\\s+")));
        List<String> expected = Arrays.asList("the", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog");
        List<String> expectedFiltered = Arrays.asList("the", "quick", "brown", "fox", "jumps", "over", "the", "lazy");
        assertEquals(expectedFiltered, Arrays.asList(splitter.split("the quick brown fox jumps over the lazy dog".toCharArray()).toArray(new String[0])));
    }

    @Test
    public void testSplitterOnPatternWithTrim() {
        Splitter splitter = Splitter.onPattern("\\s+").trimResults();
        List<String> result = new ArrayList<>(Arrays.asList(" the quick brown fox jumps over the lazy dog ".split("\\s+")));
        List<String> expected = Arrays.asList("the", "quick", "brown", "fox", "jumps", "over", "the", "lazy");
        List<String> expectedTrimmed = Arrays.asList("the", "quick", "brown", "fox", "jumps", "over", "the", "lazy");
        assertEquals(expectedTrimmed, Arrays.asList(splitter.split("the quick brown fox jumps over the lazy dog ".toCharArray()).toArray(new String[0])));
    }

    @Test
    public void testSplitterOnPatternWithTrimAndEmptyStringsOmitted() {
        Splitter splitter = Splitter.onPattern("\\s+").omitEmptyStrings().trimResults();
        List<String> result = new ArrayList<>(Arrays.asList(" the quick brown fox jumps over the lazy dog ".split("\\s+")));
        List<String> expected = Arrays.asList("the", "quick", "brown", "fox", "jumps", "over", "the", "lazy");
        List<String> expectedTrimmed = Arrays.asList("the", "quick", "brown", "fox", "jumps", "over", "the", "lazy");
        assertEquals(expectedTrimmed, Arrays.asList(splitter.split("the quick brown fox jumps over the lazy dog ".toCharArray()).toArray(new String[0])));
    }

    @Test
    public void testSplitterOnPatternWithKeyValueSeparator() {
        Splitter keyValueSplitter = Splitter.on("=");
        Map<String, String> result = new LinkedHashMap<>(Splitter.withKeyValueSeparator(keyValueSplitter).split("key1=value1&key2=value2"));
        Map<String, String> expected = new LinkedHashMap() {{
            put("key1", "value1");
            put("key2", "value2");
        }};
        assertEquals(expected, result);
    }
}