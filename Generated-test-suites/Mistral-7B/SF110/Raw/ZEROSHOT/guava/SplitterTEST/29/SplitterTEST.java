package com.example.splittertest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on('-');
        CharSequence input = "test-this-out-123-test";
        List<String> expectedOutput = Arrays.asList("test", "this", "out", "123", "test");

        List<String> actualOutput = new ArrayList<String>();
        for (String string : splitter.split(input)) {
            actualOutput.add(string);
        }

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on("-");
        CharSequence input = "test-this-out-123-test";
        List<String> expectedOutput = Arrays.asList("test", "this", "out", "123", "test");

        List<String> actualOutput = new ArrayList<String>();
        for (String string : splitter.split(input)) {
            actualOutput.add(string);
        }

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testOnPatternSeparator() {
        String separatorPattern = "[.-]";
        Splitter splitter = Splitter.onPattern(separatorPattern);
        CharSequence input = "test-this-out-123-test";
        List<String> expectedOutput = Arrays.asList("test", "this", "out", "123", "test");

        List<String> actualOutput = new ArrayList<String>();
        for (String string : splitter.split(input)) {
            actualOutput.add(string);
        }

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFixedLengthSplitter() {
        int length = 3;
        Splitter splitter = Splitter.fixedLength(length);
        CharSequence input = "thisisatestthisisatestthisisatest";
        List<String> expectedOutput = Arrays.asList("this", "is", "a", "test", "this", "is", "a", "test", "this", "is", "a", "test");

        List<String> actualOutput = new ArrayList<String>();
        for (String string : splitter.split(input)) {
            actualOutput.add(string);
        }

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on('-');
        CharSequence input = "test-this-out-123-test";
        List<String> expectedOutput = Arrays.asList("test", "this", "out", "123", "test");

        List<String> actualOutput = splitter.splitToList(input);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMapSplitter() {
        String keyValueSeparator = ":";
        Splitter keySplitter = Splitter.on("-");
        Splitter valueSplitter = Splitter.on(" ");

        Splitter mapSplitter = Splitter.withKeyValueSeparator(keyValueSeparator).limit(2);
        CharSequence input = "key1:value1-key2:value2-key3:value3";
        Map<String, String> expectedOutput = new LinkedHashMap<String, String>() {{
            put("key1", "value1");
            put("key2", "value2");
        }};

        Map<String, String> actualOutput = mapSplitter.split(input);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptySeparatorPattern() {
        Splitter splitter = Splitter.onPattern("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSeparatorPattern() {
        Splitter splitter = Splitter.onPattern(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSequence() {
        Splitter splitter = Splitter.on('-');
        splitter.split(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testEmptySequence() {
        Splitter splitter = Splitter.on('-');
        List<String> output = new ArrayList<String>();
        for (String string : splitter.split(Character.toString(''))) {
            output.add(string);
        }

        assertEquals(Collections.emptyList(), output);
    }
}