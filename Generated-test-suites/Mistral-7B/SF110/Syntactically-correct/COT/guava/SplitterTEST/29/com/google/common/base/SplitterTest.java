package com.example.junit.tests;

import static org.junit.Assert.*;

import com.google.common.base.Splitter;
import com.google.common.collect.Joiner;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class SplitterTest {

    @org.junit.Test
    public void testOnChar() {
        Splitter splitter = Splitter.on('-');
        List<String> result = splitter.split("test-string");
        assertEquals(Arrays.asList("test", "string"), result);
    }

    @org.junit.Test
    public void testOnCharMatcher() {
        CharMatcher separatorMatcher = CharMatcher.is('-');
        Splitter splitter = Splitter.on(separatorMatcher);
        List<String> result = splitter.split("test-string");
        assertEquals(Arrays.asList("test", "string"), result);
    }

    @org.junit.Test
    public void testOnString() {
        Splitter splitter = Splitter.on("--");
        List<String> result = splitter.split("test--string");
        assertEquals(Arrays.asList("test", "--", "string"), result);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testOnNullPattern() {
        Splitter.on((Pattern) null);
    }

    @org.junit.Test
    public void testOnPattern() {
        Pattern separatorPattern = Pattern.compile("-");
        Splitter splitter = Splitter.on(separatorPattern);
        List<String> result = splitter.split("test-string");
        assertEquals(Arrays.asList("test", "string"), result);
    }

    @org.junit.Test
    public void testOnFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        List<String> result = splitter.split("123456");
        assertEquals(Arrays.asList("12", "34", "56"), result);
    }

    @org.junit.Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on('-');
        List<String> result = splitter.split("test--string");
        List<String> expected = Arrays.asList("test", "string");
        assertEquals(expected, result);
        result = splitter.split("test----string");
        expected = Arrays.asList("test", "string");
        assertTrue(result.equals(expected));
    }

    @org.junit.Test
    public void testLimit() {
        Splitter splitter = Splitter.on('-').limit(2);
        List<String> result = splitter.split("test--string--test");
        List<String> expected = Arrays.asList("test--", "test");
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testMapSplitter() {
        Splitter keySplitter = Splitter.on(":");
        Splitter valueSplitter = Splitter.on("=");
        MapSplitter mapSplitter = new Splitter.MapSplitter(keySplitter, valueSplitter);
        List<Map.Entry<String, String>> result = mapSplitter.split("key1:value1=key2:value2").entrySet();
        List<Map.Entry<String, String>> expected = Arrays.asList(new AbstractMap.SimpleEntry<>("key1", "value1"), new AbstractMap.SimpleEntry<>("key2", "value2"));
        assertEquals(expected, result);
    }
}