package com.example.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

public class SplitterTest {

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on('-');
        Collection<String> result = splitter.split("test-string");
        List<String> expected = Arrays.asList("test", "string");
        assertEquals(expected, result);
    }

    @Test
    public void testOnCharMatcher() {
        CharMatcher charMatcher = CharMatcher.is('-');
        Splitter splitter = Splitter.on(charMatcher);
        Collection<String> result = splitter.split("test-string");
        List<String> expected = Arrays.asList("test", "string");
        assertEquals(expected, result);
    }

    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on("-");
        Collection<String> result = splitter.split("test-string");
        List<String> expected = Arrays.asList("test", "string");
        assertEquals(expected, result);
    }

    @Test
    public void testOnPattern() {
        Pattern pattern = Pattern.compile("-");
        Splitter splitter = Splitter.onPattern("-");
        Collection<String> result = splitter.split("test-string");
        List<String> expected = Arrays.asList("test", "string");
        assertEquals(expected, result);
    }

    @Test
    public void testOnFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Collection<String> result = splitter.split("test123string");
        List<String> expected = Arrays.asList("test", "12", "string");
        assertEquals(expected, result);
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on('-').omitEmptyStrings();
        Collection<String> result = splitter.split("test- -string");
        List<String> expected = Arrays.asList("test", "string");
        assertEquals(expected, result);
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on('-').trimResults();
        Collection<String> result = splitter.split(" test--string ");
        List<String> expected = Arrays.asList("test", "string");
        assertEquals(expected, result);
    }

    @Test
    public void testTrimResultsWithCustomTrimmer() {
        Splitter splitter = Splitter.on('-').trimResults(CharMatcher.WHITESPACE);
        Collection<String> result = splitter.split(" test--string ");
        List<String> expected = Arrays.asList("test", "string");
        assertEquals(expected, result);
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on('-').limit(2);
        Collection<String> result = splitter.split("test123456-string");
        List<String> expected = Arrays.asList("test", "12345");
        assertEquals(expected, result);
    }

    @Test
    public void testMapSplitter() {
        Splitter keySplitter = Splitter.on(':');
        Splitter valueSplitter = Splitter.on(' ');
        MapSplitter mapSplitter = Splitter.on(':').withKeyValueSeparator(keySplitter, valueSplitter);
        Collection<Map.Entry<String, String>> result = mapSplitter.split("key1:value1 value2:value2 key3:value3");
        List<Map.Entry<String, String>> expected = Arrays.asList(
                Map.entry("key1", "value1"),
                Map.entry("key2", "value2")
        );
        assertEquals(expected, result);
    }
}