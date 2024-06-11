package com.example.junit4;

import static org.junit.Assert.*;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.collect.LinkedHashMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SplitterTest {

    @org.junit.Test
    public void testOnChar() {
        Splitter splitter = Splitter.on('-').trimResults();
        String input = "apple-banana-orange";
        List<String> expected = Arrays.asList("apple", "banana", "orange");
        List<String> result = Lists.newArrayList(splitter.split(input));
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testOnCharEmptyString() {
        Splitter splitter = Splitter.on('-').trimResults();
        String input = "";
        List<String> expected = Collections.emptyList();
        List<String> result = Lists.newArrayList(splitter.split(input));
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testOnCharLimit() {
        Splitter splitter = Splitter.on('-').limit(2).trimResults();
        String input = "apple-banana-orange-grape-kiwi";
        List<String> expected = Arrays.asList("apple", "banana");
        List<String> result = Lists.newArrayList(splitter.split(input));
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testOnCharOmitEmptyStrings() {
        Splitter splitter = Splitter.on('-').omitEmptyStrings().trimResults();
        String input = "apple-banana-orange-";
        List<String> expected = Arrays.asList("apple", "banana", "orange");
        List<String> result = Lists.newArrayList(splitter.split(input));
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testOnCharWithKeyValueSeparator() {
        Splitter keySplitter = Splitter.on(':');
        Splitter valueSplitter = Splitter.on(' ');
        MapSplitter mapSplitter = Splitter.on('=').withKeyValueSeparator(keySplitter, valueSplitter);
        String input = "key1:value1 value2:value2 key3:value3";
        Map<String, String> expected = Maps.newLinkedHashMap(Arrays.asList(
                new AbstractMap.SimpleEntry<>("key1", "value1"),
                new AbstractMap.SimpleEntry<>("key2", "value2"),
                new AbstractMap.SimpleEntry<>("key3", "value3")
        ));
        Map<String, String> result = mapSplitter.split(input);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testOnPattern() {
        Splitter splitter = Splitter.onPattern("\\s+");
        String input = "apple banana orange";
        List<String> expected = Arrays.asList("apple", "banana", "orange");
        List<String> result = Lists.newArrayList(splitter.split(input));
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testOnPatternEmptyString() {
        Splitter splitter = Splitter.onPattern("\\s+");
        String input = "";
        List<String> expected = Collections.emptyList();
        List<String> result = Lists.newArrayList(splitter.split(input));
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testOnPatternLimit() {
        Splitter splitter = Splitter.onPattern("\\s+").limit(2);
        String input = "apple banana orange grape kiwi";
        List<String> expected = Arrays.asList("apple", "banana");
        List<String> result = Lists.newArrayList(splitter.split(input));
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testOnPatternOmitEmptyStrings() {
        Splitter splitter = Splitter.onPattern("\\s+").omitEmptyStrings();
        String input = "apple banana orange grape   ";
        List<String> expected = Arrays.asList("apple", "banana", "orange");
        List<String> result = Lists.newArrayList(splitter.split(input));
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testOnPatternWithKeyValueSeparator() {
        Pattern keyPattern = Pattern.compile("\\w+:");
        Pattern valuePattern = Pattern.compile("\\s+");
        MapSplitter mapSplitter = Splitter.onPattern("=").withKeyValueSeparator(keyPattern, valuePattern);
        String input = "key1:value1 value2:value2 key3:value3";
        Map<String, String> expected = Maps.newLinkedHashMap(Arrays.asList(
                new AbstractMap.SimpleEntry<>("key1", "value1"),
                new AbstractMap.SimpleEntry<>("key2", "value2"),
                new AbstractMap.SimpleEntry<>("key3", "value3")
        ));
        Map<String, String> result = mapSplitter.split(input);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        String input = "1234567890";
        List<String> expected = Arrays.asList("123", "456", "789");
        List<String> result = Lists.newArrayList(splitter.split(input));
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFixedLengthEmptyString() {
        Splitter splitter = Splitter.fixedLength(3);
        String input = "";
        List<String> expected = Collections.emptyList();
        List<String> result = Lists.newArrayList(splitter.split(input));
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFixedLengthLimit() {
        Splitter splitter = Splitter.fixedLength(3).limit(2);
        String input = "12345678901234567890";
        List<String> expected = Arrays.asList("123", "456");
        List<String> result = Lists.newArrayList(splitter.split(input));
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFixedLengthOmitEmptyStrings() {
        Splitter splitter = Splitter.fixedLength(3).omitEmptyStrings();
        String input = "123 456 789";
        List<String> expected = Arrays.asList("123", "456", "789");
        List<String> result = Lists.newArrayList(splitter.split(input));
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testToString() {
        Splitter splitter = Splitter.on('-');
        String input = "apple-banana-orange";
        Iterable<String> iterable = splitter.split(input);
        List<String> expected = Arrays.asList("apple", "banana", "orange");
        List<String> result = new ArrayList<>();
        for (String s : iterable) {
            result.add(s);
        }
        assertEquals(expected, result);
        String actual = iterable.toString();
        String expectedToString = Joiner.on(", ").join(expected);
        assertEquals(expectedToString, actual);
    }
}