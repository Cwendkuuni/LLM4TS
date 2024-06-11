package com.example.junit;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
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
        List<String> result = splitter.split("test-string");
        assertEquals(Arrays.asList("test", "string"), result);
    }

    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.is('-'));
        List<String> result = splitter.split("test-string");
        assertEquals(Arrays.asList("test", "string"), result);
    }

    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on("-");
        List<String> result = splitter.split("test-string");
        assertEquals(Arrays.asList("test", "string"), result);
    }

    @Test
    public void testOnPattern() {
        Pattern pattern = Pattern.compile("-");
        Splitter splitter = Splitter.on(pattern);
        List<String> result = splitter.split("test-string");
        assertEquals(Arrays.asList("test", "string"), result);
    }

    @Test
    public void testOnEmptyString() {
        Splitter splitter = Splitter.on("");
        List<String> result = splitter.split("test");
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testOnNull() {
        Splitter splitter = Splitter.on("");
        List<String> result = splitter.split(null);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on('-').limit(2);
        List<String> result = Arrays.asList("test1", "test2-string");
        List<String> expected = Arrays.asList("test1", "test2");
        List<String> split = splitter.split(Joiner.on('-').join(result));
        assertEquals(expected, split);
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on('-').omitEmptyStrings();
        List<String> result = Arrays.asList("test1-", "test2", "test3-");
        List<String> split = splitter.split(Joiner.on('-').join(result));
        List<String> expected = Arrays.asList("test2");
        assertEquals(expected, split);
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        List<String> result = Arrays.asList("test123", "test456");
        List<String> split = splitter.split(Joiner.on("").join(result));
        List<String> expected = Arrays.asList("12", "3", "45", "6");
        assertEquals(expected, split);
    }

    @Test
    public void testMapSplitter() {
        Splitter keySplitter = Splitter.on(":");
        Splitter valueSplitter = Splitter.on("=");
        MapSplitter mapSplitter = Splitter.on(":").withKeyValueSeparator(Splitter.on("="));
        List<String> input = Arrays.asList("key1:value1=text1,key2:value2=text2,key3:value3=text3");
        List<String> expected = Arrays.asList(Arrays.asList("key1", "value1"), Arrays.asList("key2", "value2"), Arrays.asList("key3", "value3"));
        List<List<String>> actual = mapSplitter.split(input);
        assertEquals(expected, actual);
    }
}