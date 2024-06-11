package com.example.junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
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
        Splitter splitter = Splitter.on(' ');
        List<String> result = new ArrayList<String>();
        result.addAll(splitter.split("The quick brown fox jumps over the lazy dog"));
        assertEquals(Arrays.asList("The", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog"), result);
    }

    @Test
    public void testOnCharMatcher() {
        CharMatcher separatorMatcher = CharMatcher.is(':');
        Splitter splitter = Splitter.on(separatorMatcher);
        List<String> result = new ArrayList<String>();
        result.addAll(splitter.split("key1:value1:key2:value2"));
        assertEquals(Arrays.asList("key1", "value1", "key2", "value2"), result);
    }

    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(",");
        List<String> result = new ArrayList<String>();
        result.addAll(splitter.split("apple,banana,orange"));
        assertEquals(Arrays.asList("apple", "banana", "orange"), result);
    }

    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.onPattern("\\s+");
        List<String> result = new ArrayList<String>();
        result.addAll(splitter.split("The quick brown fox jumps over the lazy dog"));
        assertEquals(Arrays.asList("The", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog"), result);
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        List<String> result = new ArrayList<String>();
        result.addAll(splitter.split("1234567890"));
        assertEquals(Arrays.asList("123", "456", "789"), result);
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(" ").omitEmptyStrings();
        List<String> result = new ArrayList<String>();
        result.addAll(splitter.split(" The quick brown fox jumps over the lazy dog "));
        assertEquals(Arrays.asList("The", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog"), result);
    }

    @Test(expected = NullPointerException.class)
    public void testNullSequence() {
        Splitter splitter = Splitter.on(" ");
        splitter.split(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptySeparator() {
        Splitter splitter = Splitter.on("");
    }

    @Test
    public void testWithKeyValueSeparator() {
        Splitter keyValueSplitter = Splitter.on("=");
        Map<String, String> result = new LinkedHashMap<String, String>();
        result.putAll(Splitter.on("key1=value1&key2=value2").withKeyValueSeparator(keyValueSplitter).split("key1=value1&key2=value2"));
        assertEquals(Arrays.asList(new String[]{"key1", "value1", "key2", "value2"}), result.entrySet().stream().map(entry -> Arrays.asList(entry.getKey(), entry.getValue())).toList());
    }
}