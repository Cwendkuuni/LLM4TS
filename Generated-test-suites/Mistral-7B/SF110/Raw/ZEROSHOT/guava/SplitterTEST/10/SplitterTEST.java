package com.example.junit4;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on('-');
        List<String> result = new ArrayList<>();
        result.addAll(splitter.split("a-b-c-d-e"));
        assertEquals(Arrays.asList("a", "b", "c", "d", "e"), result);
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on(",");
        List<String> result = new ArrayList<>();
        result.addAll(splitter.split("apple,banana,cherry"));
        assertEquals(Arrays.asList("apple", "banana", "cherry"), result);
    }

    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.onPattern("\\s+");
        List<String> result = new ArrayList<>();
        result.addAll(Arrays.asList(splitter.split("   apple   banana   cherry   ")));
        assertEquals(Arrays.asList("apple", "banana", "cherry"), result);
    }

    @Test
    public void testOnFixedLengthSeparator() {
        Splitter splitter = Splitter.fixedLength(3);
        List<String> result = new ArrayList<>();
        result.addAll(splitter.split("abcdefghijklmnop"));
        assertEquals(Arrays.asList("abc", "def", "ghi", "jkl", "mno", "p"), result);
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",").omitEmptyStrings();
        List<String> result = new ArrayList<>();
        result.addAll(splitter.split("apple, ,banana, ,cherry"));
        assertEquals(Arrays.asList("apple", "banana", "cherry"), result);
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",").limit(3);
        List<String> result = new ArrayList<>();
        result.addAll(splitter.split("apple,banana,cherry,orange,grape"));
        assertEquals(Arrays.asList("apple", "banana", "cherry"), result);
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        List<String> result = new ArrayList<>();
        result.addAll(Arrays.asList(splitter.split(" apple   , banana   , cherry   ")));
        assertEquals(Arrays.asList("apple", "banana", "cherry"), result);
    }

    @Test
    public void testTrimResultsWithCustomTrimmer() {
        Splitter splitter = Splitter.on(",").trimResults(CharMatcher.whitespace());
        List<String> result = new ArrayList<>();
        result.addAll(Arrays.asList(splitter.split(" apple   , banana   , cherry   ")));
        assertEquals(Arrays.asList("apple", "banana", "cherry"), result);
    }

    @Test
    public void testMapSplitter() {
        Splitter keySplitter = Splitter.on(",");
        Splitter valueSplitter = Splitter.on(":");
        MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator(keySplitter, valueSplitter);
        List<Map.Entry<String, String>> result = new ArrayList<>();
        result.addAll(mapSplitter.split("key1:value1,key2:value2,key3:value3").entrySet());
        assertEquals(Arrays.asList(
                Map.entry("key1", "value1"),
                Map.entry("key2", "value2"),
                Map.entry("key3", "value3")),
            result);
    }
}