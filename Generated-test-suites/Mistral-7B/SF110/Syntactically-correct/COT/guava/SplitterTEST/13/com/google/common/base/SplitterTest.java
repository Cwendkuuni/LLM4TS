package com.example.junit; // replace with your test package if needed

import static org.junit.Assert.*;
import com.google.common.base.Splitter;
import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Patterns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SplitterTest {

    @org.junit.Test
    public void testOnChar() {
        Splitter splitter = Splitter.on('-');
        List<String> result = new ArrayList<>();
        result.addAll(splitter.split("test-string"));
        assertEquals(Arrays.asList("test", "string"), result);
    }

    @org.junit.Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.is('-'));
        List<String> result = new ArrayList<>();
        result.addAll(splitter.split("test-string"));
        assertEquals(Arrays.asList("test", "string"), result);
    }

    @org.junit.Test
    public void testOnString() {
        Splitter splitter = Splitter.on("-");
        List<String> result = new ArrayList<>();
        result.addAll(splitter.split("test-string"));
        assertEquals(Arrays.asList("test", "string"), result);
    }

    @org.junit.Test
    public void testOnPattern() {
        Pattern pattern = Patterns.REGEX("-");
        Splitter splitter = Splitter.on(pattern);
        List<String> result = new ArrayList<>();
        result.addAll(splitter.split("test-string"));
        assertEquals(Arrays.asList("test", "string"), result);
    }

    @org.junit.Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        List<String> result = new ArrayList<>();
        result.addAll(splitter.split("12345678"));
        assertEquals(Arrays.asList("12", "34", "56", "78"), result);
    }

    @org.junit.Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on("-").omitEmptyStrings();
        List<String> result = new ArrayList<>();
        result.addAll(splitter.split("test--string"));
        assertEquals(Arrays.asList("test", "string"), result);
    }

    @org.junit.Test
    public void testLimit() {
        Splitter splitter = Splitter.on("-").limit(3);
        List<String> result = new ArrayList<>();
        result.addAll(splitter.split("test----string"));
        assertEquals(Arrays.asList("test", "----", "string"), result);
    }

    @org.junit.Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on("-").trimResults();
        List<String> result = new ArrayList<>();
        result.addAll(splitter.split("test--string"));
        assertEquals(Arrays.asList("test", "string"), result);
    }

    @org.junit.Test
    public void testTrimResultsWithCustomTrimmer() {
        Splitter splitter = Splitter.on("-").trimResults(CharMatcher.is(' '));
        List<String> result = new ArrayList<>();
        result.addAll(splitter.split(" test--string "));
        assertEquals(Arrays.asList("test", "string"), result);
    }

    @org.junit.Test
    public void testSplitToList() {
        List<String> result = Splitter.on("-").splitToList("test--string");
        assertEquals(Arrays.asList("test", "string"), result);
    }

    @org.junit.Test
    public void testMapSplitter() {
        List<Map.Entry<String, String>> expected = Arrays.asList(
                Map.entry("test", "string")
        );
        Map<String, String> map = Splitter.on("-").withKeyValueSeparator("=").split("test=string");
        assertEquals(expected, map.entrySet());
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testNullSequence() {
        Splitter splitter = Splitter.on("-");
        splitter.split(null);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testEmptySequence() {
        Splitter splitter = Splitter.on("-");
        splitter.split(Character.toString(''));
    }
}