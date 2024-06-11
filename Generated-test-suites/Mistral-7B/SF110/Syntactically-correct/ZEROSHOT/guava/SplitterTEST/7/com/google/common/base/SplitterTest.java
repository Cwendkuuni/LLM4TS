package com.example.junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

public class SplitterTest {

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on('-');
        List<String> result = new ArrayList<String>();
        result.addAll(splitter.split("test-string"));
        assertEquals(new String[]{"test", "string"}, result.toArray(new String[0]));
    }

    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.is('-'));
        List<String> result = new ArrayList<String>();
        result.addAll(splitter.split("test-string"));
        assertEquals(new String[]{"test", "string"}, result.toArray(new String[0]));
    }

    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.on("\\-");
        List<String> result = new ArrayList<String>();
        result.addAll(splitter.split("test-string"));
        assertEquals(new String[]{"test", "string"}, result.toArray(new String[0]));
    }

    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on("-");
        List<String> result = new ArrayList<String>();
        result.addAll(splitter.split("test-string"));
        assertEquals(new String[]{"test", "string"}, result.toArray(new String[0]));
    }

    @Test
    public void testOnPatternWithEmptyString() {
        Splitter splitter = Splitter.onPattern("\\s+");
        List<String> result = new ArrayList<String>();
        result.addAll(splitter.split(" test string "));
        assertEquals(new String[]{"", "test", "string"}, result.toArray(new String[0]));
    }

    @Test
    public void testOmmitEmptyStrings() {
        Splitter splitter = Splitter.on('-').omitEmptyStrings();
        List<String> result = new ArrayList<String>();
        result.addAll(splitter.split("test--string"));
        assertEquals(new String[]{"test", "string"}, result.toArray(new String[0]));
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on('-').limit(2);
        List<String> result = new ArrayList<String>();
        result.addAll(splitter.split("test-----string"));
        assertEquals(new String[]{"test", "---"}, result.toArray(new String[0]));
    }

    @Test
    public void testTrim() {
        Splitter splitter = Splitter.on('-').trimResults();
        List<String> result = new ArrayList<String>();
        result.addAll(splitter.split(" test--string "));
        assertEquals(new String[]{"test", "string"}, result.toArray(new String[0]));
    }

    @Test
    public void testTrimWithCustomTrimmer() {
        Splitter splitter = Splitter.on('-').trimResults(CharMatcher.WHITESPACE);
        List<String> result = new ArrayList<String>();
        result.addAll(splitter.split(" test--string "));
        assertEquals(new String[]{"test", "string"}, result.toArray(new String[0]));
    }

    @Test
    public void testMapSplitter() {
        Splitter keySplitter = Splitter.on(':');
        Splitter valueSplitter = Splitter.on('=').omitEmptyStrings();
        Map<String, String> result = new LinkedHashMap<String, String>();
        result.putAll(new Splitter.MapSplitter(keySplitter, valueSplitter).split("key1:value1=value2"));
        assertEquals(new MapEntry[] { new Map.SimpleEntry<String, String>("key1", "value1"), new Map.SimpleEntry<String, String>("key2", "value2") }, result.entrySet().toArray(new Map.Entry[0][]));
    }
}