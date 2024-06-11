package com.example.splitter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.LinkedHashMap;

public class SplitterTest {

    @Test
    public void testSplitWithCharSeparator() {
        Splitter splitter = Splitter.on('-');
        List<String> result = new ArrayList<>(Arrays.asList("one-two-three", "four-five-six").split(splitter));
        List<String> expected = Arrays.asList("one", "two", "three", "four", "five", "six");
        assertEquals(expected, result);
    }

    @Test
    public void testSplitWithStringSeparator() {
        Splitter splitter = Splitter.on(":");
        List<String> result = new ArrayList<>(Arrays.asList("one:two:three", "four:five:six").split(splitter));
        List<String> expected = Arrays.asList("one", "two", "three", "four", "five", "six");
        assertEquals(expected, result);
    }

    @Test
    public void testSplitWithPatternSeparator() {
        Splitter splitter = Splitter.onPattern("\\s+");
        List<String> result = new ArrayList<>(Arrays.asList("one two three", "four five six").split(splitter));
        List<String> expected = Arrays.asList("one", "two", "three", "four", "five", "six");
        assertEquals(expected, result);
    }

    @Test
    public void testSplitWithFixedLengthSeparator() {
        Splitter splitter = Splitter.fixedLength(3);
        List<String> result = new ArrayList<>(Arrays.asList("onetwothree", "fourfivesix").split(splitter));
        List<String> expected = Arrays.asList("one", "two", "three", "four", "five", "six");
        assertEquals(expected, result);
    }

    @Test
    public void testSplitWithEmptySeparator() {
        Splitter splitter = Splitter.on(" ");
        List<String> result = new ArrayList<>(Arrays.asList("one two three", "four five six").split(splitter));
        List<String> expected = Arrays.asList("one", "two", "three", "four", "five", "six");
        assertEquals(expected, result);
    }

    @Test
    public void testSplitWithTrimmedResults() {
        Splitter splitter = Splitter.on('-').trimResults();
        List<String> result = new ArrayList<>(Arrays.asList(" one-two-three ", " four-five-six ").split(splitter));
        List<String> expected = Arrays.asList("one", "two", "three", "four", "five", "six");
        assertEquals(expected, result);
    }

    @Test
    public void testSplitWithOmittedEmptyStrings() {
        Splitter splitter = Splitter.on(':').omitEmptyStrings();
        List<String> result = new ArrayList<>(Arrays.asList("one: :three", "four:five:six: ").split(splitter));
        List<String> expected = Arrays.asList("one", "three", "four", "five", "six");
        assertEquals(expected, result);
    }

    @Test
    public void testSplitWithLimit() {
        Splitter splitter = Splitter.on(':').limit(3);
        List<String> result = new ArrayList<>(Arrays.asList("one:two:three:four:five:six".split(splitter)));
        List<String> expected = Arrays.asList("one", "two", "three");
        assertEquals(expected, result);
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on('-');
        List<String> result = splitter.splitToList("one-two-three-four-five-six");
        List<String> expected = Arrays.asList("one", "two", "three", "four", "five", "six");
        assertEquals(expected, result);
    }

    @Test
    public void testMapSplitter() {
        Splitter keySplitter = Splitter.on(":");
        List<String> keys = Arrays.asList("key1:value1:key2:value2");
        Map<String, String> map = Splitter.newMapSplitter(keySplitter).split(Joiner.on(":").join(keys));
        assertEquals(4, map.size());
        assertEquals("key1", map.keySet().iterator().next());
        assertEquals("value1", map.get("key1"));
        assertEquals("key2", map.keySet().iterator().next());
        assertEquals("value2", map.get("key2"));
    }
}