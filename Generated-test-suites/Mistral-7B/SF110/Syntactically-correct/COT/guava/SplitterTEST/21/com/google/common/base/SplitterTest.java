package com.example.junit; // replace with your package name

import static org.junit.Assert.*;

import com.google.common.base.Splitter;
import com.google.common.base.Joiner;
import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.collect.LinkedHashMap;

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
        result.addAll(Arrays.asList("test-1", "test-2", "test-3"));
        result.addAll(splitter.split("test-1-test-2-test-3"));
        assertEquals(result, splitter.split("test-1-test-2-test-3"));
    }

    @org.junit.Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.is('-'));
        List<String> result = new ArrayList<>();
        result.addAll(Arrays.asList("test-1", "test-2", "test-3"));
        result.addAll(splitter.split("test-1-test-2-test-3"));
        assertEquals(result, splitter.split("test-1-test-2-test-3"));
    }

    @org.junit.Test
    public void testOnString() {
        Splitter splitter = Splitter.on(":");
        List<String> result = new ArrayList<>();
        result.addAll(Arrays.asList("key:value", "key2:value2", "key3:value3"));
        result.addAll(splitter.split("key:value:key2:value2:key3:value3"));
        assertEquals(result, splitter.split("key:value:key2:value2:key3:value3"));
    }

    @org.junit.Test
    public void testOnPattern() {
        Pattern pattern = Pattern.compile(":");
        Splitter splitter = Splitter.on(pattern);
        List<String> result = new ArrayList<>();
        result.addAll(Arrays.asList("key:value", "key2:value2", "key3:value3"));
        result.addAll(splitter.split("key:value:key2:value2:key3:value3"));
        assertEquals(result, splitter.split("key:value:key2:value2:key3:value3"));
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testOnNullPattern() {
        Splitter splitter = Splitter.on(null);
    }

    @org.junit.Test
    public void testOnEmptyPattern() {
        Pattern pattern = Pattern.compile("");
        Splitter splitter = Splitter.on(pattern);
        List<String> result = new ArrayList<>();
        result.addAll(Arrays.asList(""));
        result.addAll(splitter.split(""));
        assertEquals(result, splitter.split(""));
    }

    @org.junit.Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        List<String> result = new ArrayList<>();
        result.addAll(Arrays.asList("abc", "def", "ghi"));
        result.addAll(splitter.split("abcdefghi"));
        assertEquals(result, splitter.split("abcdefghi"));
    }

    @org.junit.Test
    public void testLimit() {
        Splitter splitter = Splitter.on(':').limit(2);
        List<String> result = new ArrayList<>();
        result.addAll(Arrays.asList("key:value", "key2:value2"));
        result.addAll(splitter.split("key:value:key2:value2:key3:value3"));
        assertEquals(result, Arrays.asList("key:value", "key2:value2"));
    }

    @org.junit.Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(':').omitEmptyStrings();
        List<String> result = new ArrayList<>();
        result.addAll(Arrays.asList("key:value", ""));
        result.addAll(splitter.split("key:value: :"));
        assertEquals(Arrays.asList("key:value"), result);
    }

    @org.junit.Test
    public void testWithKeyValueSeparator() {
        Splitter keyValueSplitter = Splitter.on("=");
        Map<String, String> map = new LinkedHashMap<>();
        map.putAll(Splitter.MapSplitter.withKeyValueSeparator(keyValueSplitter).split("key1=value1&key2=value2&key3=value3"));
        assertEquals(map, Arrays.asList(Arrays.asList("key1", "value1"), Arrays.asList("key2", "value2"), Arrays.asList("key3", "value3")).stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testDuplicateKey() {
        Map<String, String> map = Splitter.MapSplitter.withKeyValueSeparator(Splitter.on("=")).split("key1=value1&key1=value2");
    }
}