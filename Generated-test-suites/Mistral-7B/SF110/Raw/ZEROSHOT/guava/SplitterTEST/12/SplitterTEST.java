package com.example.junit;

import static org.junit.Assert.*;

import com.google.common.base.Splitter;
import com.google.common.base.Joiner;
import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.collect.LinkedHashMap;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class SplitterTest {

    @org.junit.Test
    public void testOnChar() {
        Splitter splitter = Splitter.on('-');
        List<String> result = splitter.split("apple-banana-orange");
        assertEquals(Arrays.asList("apple", "banana", "orange"), result);
    }

    @org.junit.Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.is('-'));
        List<String> result = splitter.split("apple-banana-orange");
        assertEquals(Arrays.asList("apple", "banana", "orange"), result);
    }

    @org.junit.Test
    public void testOnString() {
        Splitter splitter = Splitter.on("-");
        List<String> result = splitter.split("apple-banana-orange");
        assertEquals(Arrays.asList("apple", "banana", "orange"), result);
    }

    @org.junit.Test
    public void testOnPattern() {
        Pattern pattern = Pattern.compile("-");
        Splitter splitter = Splitter.on(pattern);
        List<String> result = splitter.split("apple-banana-orange");
        assertEquals(Arrays.asList("apple", "banana", "orange"), result);
    }

    @org.junit.Test
    public void testOnPatternWithEmptyString() {
        Pattern pattern = Pattern.compile("\\s+");
        Splitter splitter = Splitter.on(pattern);
        List<String> result = splitter.split("hello world");
        assertEquals(Arrays.asList("hello", "world"), result);
    }

    @org.junit.Test
    public void testOnPatternWithLimit() {
        Pattern pattern = Pattern.compile("\\s+");
        Splitter splitter = Splitter.on(pattern).limit(2);
        List<String> result = splitter.split("hello world this is a test");
        assertEquals(Arrays.asList("hello", "world", "this"), result);
    }

    @org.junit.Test
    public void testOnPatternWithEmptyStringsOmit() {
        Pattern pattern = Pattern.compile("\\s+");
        Splitter splitter = Splitter.on(pattern).omitEmptyStrings();
        List<String> result = splitter.split("hello world   ");
        assertEquals(Arrays.asList("hello", "world"), result);
    }

    @org.junit.Test
    public void testOnPatternWithKeyValueSeparator() {
        Pattern pattern = Pattern.compile(":");
        Splitter keyValueSplitter = Splitter.on(pattern);
        Map<String, String> map = Splitter.on('-').withKeyValueSeparator(keyValueSplitter).split("apple:1,banana:2,orange:3");
        assertEquals(Arrays.asList("apple", "1"), map.keySet().toList());
        assertEquals(Arrays.asList("1", "banana", "2"), map.values().toList());
    }

    @org.junit.Test
    public void testOnPatternWithInvalidEntry() {
        Pattern pattern = Pattern.compile(":");
        Splitter keyValueSplitter = Splitter.on(pattern);
        try {
            Map<String, String> map = Splitter.on('-').withKeyValueSeparator(keyValueSplitter).split("apple:1,banana:2,orange");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Chunk [orange] is not a valid entry"));
        }
    }

    @org.junit.Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        List<String> result = splitter.split("123456789");
        assertEquals(Arrays.asList("123", "456", "789"), result);
    }

    @org.junit.Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on('-').trimResults();
        List<String> result = splitter.split("apple-banana-orange");
        assertEquals(Arrays.asList("apple", "banana", "orange"), result);
        assertEquals(Arrays.asList("apple", "banana", "orange"), Joiner.on(" ").join(result));
    }

    @org.junit.Test
    public void testTrimResultsWithCustomTrimmer() {
        Splitter splitter = Splitter.on('-').trimResults(CharMatcher.is(' '));
        List<String> result = splitter.split(" apple-banana orange ");
        assertEquals(Arrays.asList("apple", "banana", "orange"), result);
        assertEquals(Arrays.asList("apple", "banana", "orange"), Joiner.on(" ").join(result));
    }
}