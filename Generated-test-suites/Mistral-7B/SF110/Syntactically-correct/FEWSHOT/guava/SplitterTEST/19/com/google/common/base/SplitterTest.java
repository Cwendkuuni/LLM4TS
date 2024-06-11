package com.google.common.base;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.Test;

public class SplitterTest {
    @Test
    public void testSplitterOnChar() {
        Splitter splitter = Splitter.on('-');
        String testString = "--foo---bar---baz---";
        List<String> expectedResult = Arrays.asList("foo", "bar", "baz");

        List<String> result = new ArrayList<>();
        for (String s : splitter.split(testString)) {
            result.add(s);
        }

        assertEquals(expectedResult, result);
    }

    @Test
    public void testSplitterOnString() {
        Splitter splitter = Splitter.on("/");
        String testString = "/foo/bar/baz/";
        List<String> expectedResult = Arrays.asList("foo", "bar", "baz");

        List<String> result = new ArrayList<>();
        for (String s : splitter.split(testString)) {
            result.add(s);
        }

        assertEquals(expectedResult, result);
    }

    @Test
    public void testSplitterOnPattern() {
        String separatorPattern = "\\s+";
        Splitter splitter = Splitter.onPattern(separatorPattern);
        String testString = "foo bar baz";
        List<String> expectedResult = Arrays.asList("foo", "bar", "baz");

        List<String> result = new ArrayList<>();
        for (String s : splitter.split(testString)) {
            result.add(s);
        }

        assertEquals(expectedResult, result);
    }

    @Test
    public void testSplitterFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        String testString = "abcdefghijklmnopqrstuvwxyz";
        List<String> expectedResult = Arrays.asList("abc", "def", "ghi");

        List<String> result = new ArrayList<>();
        for (String s : splitter.split(testString)) {
            result.add(s);
        }

        assertEquals(expectedResult, result);
    }

    @Test
    public void testSplitterOnEmptyString() {
        Splitter splitter = Splitter.on(" ");
        String testString = "foo bar baz ";
        List<String> expectedResult = Arrays.asList("foo", "bar", "baz");

        List<String> result = new ArrayList<>();
        for (String s : splitter.split(testString)) {
            result.add(s);
        }

        assertEquals(expectedResult, result);
    }

    @Test
    public void testSplitterOnEmptyStringWithTrim() {
        Splitter splitter = Splitter.on(" ").trimResults();
        String testString = "foo bar baz ";
        List<String> expectedResult = Arrays.asList("foo", "bar", "baz");

        List<String> result = new ArrayList<>();
        for (String s : splitter.split(testString)) {
            result.add(s);
        }

        assertEquals(expectedResult, result);
    }

    @Test
    public void testSplitterOnEmptyStringWithLimit() {
        Splitter splitter = Splitter.on(" ").limit(2);
        String testString = "foo bar baz ";
        List<String> expectedResult = Arrays.asList("foo", "bar");

        List<String> result = new ArrayList<>();
        for (String s : splitter.split(testString)) {
            result.add(s);
        }

        assertEquals(expectedResult, result);
    }

    @Test
    public void testSplitterWithKeyValueSeparator() {
        Splitter keyValueSplitter = Splitter.on(":");
        MapSplitter mapSplitter = Splitter.on("=").withKeyValueSeparator(keyValueSplitter);
        String testString = "foo:bar=1:baz:qux=3:quux=5";
        Map<String, Integer> expectedResult = new LinkedHashMap<>();
        expectedResult.put("foo", 1);
        expectedResult.put("baz", 3);
        expectedResult.put("quux", 5);

        Map<String, Integer> result = mapSplitter.split(testString);

        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSplitterWithInvalidKeyValueSeparator() {
        Splitter keyValueSplitter = Splitter.on(":");
        MapSplitter mapSplitter = Splitter.on(" ").withKeyValueSeparator(keyValueSplitter);
        String testString = "foo:bar=1:baz:qux=3:quux=5";

        mapSplitter.split(testString);
    }
}