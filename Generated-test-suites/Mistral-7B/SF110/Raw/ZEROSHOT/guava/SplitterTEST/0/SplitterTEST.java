package com.example.junit4;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on('-');
        String input = "this-is-a-test-string-with-dashes";
        List<String> expected = Arrays.asList("this", "is", "a", "test", "string", "with", "dashes");
        List<String> result = new ArrayList<>();
        for (String s : splitter.split(input)) {
            result.add(s);
        }
        assertEquals(expected, result);
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on(":");
        String input = "key:value:another_key:another_value";
        List<String> expected = Arrays.asList("key", "value", "another_key", "another_value");
        List<String> result = new ArrayList<>();
        for (String s : splitter.split(input)) {
            result.add(s);
        }
        assertEquals(expected, result);
    }

    @Test
    public void testOnPatternSeparator() {
        Pattern pattern = Pattern.compile(":");
        Splitter splitter = Splitter.on(pattern);
        String input = "key:value:another_key:another_value";
        List<String> expected = Arrays.asList("key", "value", "another_key", "another_value");
        List<String> result = new ArrayList<>();
        for (String s : splitter.split(input)) {
            result.add(s);
        }
        assertEquals(expected, result);
    }

    @Test
    public void testOnRegularExpressionSeparator() {
        String separatorPattern = "\\:";
        Splitter splitter = Splitter.onPattern(separatorPattern);
        String input = "key:value:another_key:another_value";
        List<String> expected = Arrays.asList("key", "value", "another_key", "another_value");
        List<String> result = new ArrayList<>();
        for (String s : splitter.split(input)) {
            result.add(s);
        }
        assertEquals(expected, result);
    }

    @Test
    public void testFixedLengthSeparator() {
        Splitter splitter = Splitter.fixedLength(3);
        String input = "1234567890";
        List<String> expected = Arrays.asList("123", "456", "789", "0");
        List<String> result = new ArrayList<>();
        for (String s : splitter.split(input)) {
            result.add(s);
        }
        assertEquals(expected, result);
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(":").omitEmptyStrings();
        String input = "key:::value:another_key:";
        List<String> expected = Arrays.asList("key", "value", "another_key");
        List<String> result = new ArrayList<>();
        for (String s : splitter.split(input)) {
            result.add(s);
        }
        assertEquals(expected, result);
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(":").limit(3);
        String input = "key1:value1:key2:value2:key3:value3:key4:value4:key5:value5";
        List<String> expected = Arrays.asList("key1", "value1", "key2:value2:key3");
        List<String> result = new ArrayList<>();
        for (String s : splitter.split(input)) {
            result.add(s);
        }
        assertEquals(expected, result);
    }

    @Test
    public void testTrimResults() {
        CharMatcher trimmer = CharMatcher.whitespace();
        Splitter splitter = Splitter.on(":").trimResults(trimmer);
        String input = " key1 : value1  : key2 : value2 ";
        List<String> expected = Arrays.asList("key1", "value1", "key2", "value2");
        List<String> result = new ArrayList<>();
        for (String s : splitter.split(input)) {
            result.add(s);
        }
        assertEquals(expected, result);
    }

    @Test
    public void testMapSplitter() {
        Splitter keySplitter = Splitter.on(":");
        Splitter valueSplitter = Splitter.on("=");
        MapSplitter mapSplitter = new Splitter(keySplitter, valueSplitter);
        String input = "key1=value1:key2=value2:key3=value3";
        Map<String, String> expected = new HashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        expected.put("key3", "value3");
        Map<String, String> result = mapSplitter.split(input);
        assertEquals(expected, result);
    }
}