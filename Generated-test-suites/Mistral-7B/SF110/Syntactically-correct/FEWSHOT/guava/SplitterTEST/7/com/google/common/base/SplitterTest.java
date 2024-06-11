package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;

public class SplitterTest {

    @Test
    public void testSplitterOnChar() {
        Splitter splitter = Splitter.on('-');
        List<String> result = splitter.split("a-b-c-d");
        assertEquals(Arrays.asList("a", "b", "c", "d"), result);
    }

    @Test
    public void testSplitterOnString() {
        Splitter splitter = Splitter.on("--");;
        List<String> result = splitter.split("a--b--c--d");
        assertEquals(Arrays.asList("a", "b", "c", "d"), result);
    }

    @Test
    public void testSplitterOnPattern() {
        Splitter splitter = Splitter.onPattern("\\s+");
        List<String> result = splitter.split("the quick brown fox jumps over the lazy dog");
        assertEquals(Arrays.asList("the", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog"), result);
    }

    @Test
    public void testSplitterOnFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        List<String> result = new ArrayList<>(splitter.split("1234567890"));
        assertEquals(Arrays.asList("12", "34", "56", "78", "90"), result);
    }

    @Test
    public void testSplitterOmitEmptyStrings() {
        Splitter splitter = Splitter.on('-').omitEmptyStrings();
        List<String> result = splitter.split("a-b- -c-d");
        assertEquals(Arrays.asList("a", "b", "c", "d"), result);
    }

    @Test
    public void testSplitterLimit() {
        Splitter splitter = Splitter.on('-').limit(3);
        List<String> result = new ArrayList<>(splitter.split("a-b-c-d-e-f-g-h-i-j-k-l"));
        assertEquals(Arrays.asList("a", "b", "c"), result);
    }

    @Test
    public void testMapSplitter() {
        Splitter keySplitter = Splitter.on(":");
        Splitter valueSplitter = Splitter.on("=");
        MapSplitter mapSplitter = new Splitter(":").withKeyValueSeparator(keySplitter).withKeyValueSeparator(valueSplitter);
        Map<String, String> result = mapSplitter.split("key1:value1=value2:value3:key2:value4=value5");
        assertEquals(Arrays.asList("key1", "value1", "value2", "key2", "value4"), result.keySet().stream().map(String::toString).toList());
        assertEquals(Arrays.asList("value1", "value2", "value5"), result.values().stream().map(String::toString).toList());
    }
}