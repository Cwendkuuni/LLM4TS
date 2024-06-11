package com.example.junit4;

import static org.junit.Assert.*;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import org.junit.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on('-');
        List<String> result = splitter.split("test-one-two-three");
        assertEquals(3, result.size());
        assertEquals("test", result.get(0));
        assertEquals("one", result.get(1));
        assertEquals("two", result.get(2));
        assertEquals("three", result.get(2));
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on(":");
        List<String> result = splitter.split("test:one:two:three");
        assertEquals(4, result.size());
        assertEquals("test", result.get(0));
        assertEquals("one", result.get(1));
        assertEquals("two", result.get(2));
        assertEquals("three", result.get(3));
    }

    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.on(Pattern.compile(":"));
        List<String> result = splitter.split("test:one:two:three");
        assertEquals(4, result.size());
        assertEquals("test", result.get(0));
        assertEquals("one", result.get(1));
        assertEquals("two", result.get(2));
        assertEquals("three", result.get(3));
    }

    @Test
    public void testOnFixedLengthSeparator() {
        Splitter splitter = Splitter.fixedLength(2);
        List<String> result = splitter.split("test1234test5678");
        assertEquals(5, result.size());
        assertEquals("test", result.get(0));
        assertEquals("12", result.get(1));
        assertEquals("34", result.get(2));
        assertEquals("test", result.get(3));
        assertEquals("56", result.get(4));
    }

    @Test(expected = NullPointerException.class)
    public void testNullSequence() {
        Splitter splitter = Splitter.on('-');
        splitter.split(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptySequence() {
        Splitter splitter = Splitter.on('-');
        List<String> result = splitter.split(Character.toString(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptySeparator() {
        Splitter splitter = Splitter.on("");
        List<String> result = splitter.split("test:one:two:three");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatternThatMatchesEmptyString() {
        Splitter splitter = Splitter.onPattern(":|");
        List<String> result = splitter.split("test:one:two:");
    }

    @Test
    public void testMapSplitter() {
        Splitter keySplitter = Splitter.on(":");
        Splitter valueSplitter = Splitter.on("=");
        MapSplitter mapSplitter = new Splitter.MapSplitter(keySplitter, valueSplitter);
        List<Map.Entry<String, String>> entries = mapSplitter.split("key1=value1:key2=value2:key3=value3");
        assertEquals(3, entries.size());
        Map.Entry<String, String> entry1 = entries.get(0);
        assertEquals("key1", entry1.getKey());
        assertEquals("value1", entry1.getValue());
        Map.Entry<String, String> entry2 = entries.get(1);
        assertEquals("key2", entry2.getKey());
        assertEquals("value2", entry2.getValue());
        Map.Entry<String, String> entry3 = entries.get(2);
        assertEquals("key3", entry3.getKey());
        assertEquals("value3", entry3.getValue());
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on('-').omitEmptyStrings();
        List<String> result = splitter.split("test-one- -two-three");
        assertEquals(3, result.size());
        assertEquals("one", result.get(1));
        assertEquals("two", result.get(2));
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on('-').limit(2);
        List<String> result = splitter.split("test-one-two-three-four-five");
        assertEquals(3, result.size());
        assertEquals("test-one", result.get(0));
        assertEquals("two-three", result.get(1));
    }
}