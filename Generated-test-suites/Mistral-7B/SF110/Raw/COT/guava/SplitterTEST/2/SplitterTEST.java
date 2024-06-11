package com.example.test;

import static org.junit.Assert.*;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterators;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on('-');
        List<String> result = new ArrayList<>(Arrays.asList("hello-world", "this-is-a-test"));
        Iterator<String> iterator = splitter.split("hello-world-this-is-a-test").iterator();
        List<String> actual = new ArrayList<>();
        while (iterator.hasNext()) {
            actual.add(iterator.next());
        }
        assertEquals(result, actual);
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on(":");
        List<String> result = new ArrayList<>(Arrays.asList("key1:value1", "key2:value2"));
        Iterator<String> iterator = splitter.split("key1:value1:key2:value2").iterator();
        List<String> actual = new ArrayList<>();
        while (iterator.hasNext()) {
            actual.add(iterator.next());
        }
        assertEquals(result, actual);
    }

    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.on(Pattern.compile("\\s+"));
        List<String> result = new ArrayList<>(Arrays.asList("first", "second", "third"));
        Iterator<String> iterator = splitter.split("first second third").iterator();
        List<String> actual = new ArrayList<>();
        while (iterator.hasNext()) {
            actual.add(iterator.next());
        }
        assertEquals(result, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        Splitter splitter = Splitter.on('-');
        splitter.split(null);
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(':').limit(2);
        List<String> result = new ArrayList<>(Arrays.asList("key1:value1", "key2:value2"));
        Iterator<String> iterator = splitter.split("key1:value1:key2:value2:key3:value3").iterator();
        List<String> actual = new ArrayList<>();
        while (iterator.hasNext()) {
            actual.add(iterator.next());
        }
        assertEquals(result, actual);
        assertEquals(2, actual.size());
    }

    @Test
    public void testTrimEmptyStrings() {
        Splitter splitter = Splitter.on(':').omitEmptyStrings();
        List<String> result = new ArrayList<>(Arrays.asList("key1:value1", "key2:value2"));
        Iterator<String> iterator = splitter.split("key1:value1:key2::value2").iterator();
        List<String> actual = new ArrayList<>();
        while (iterator.hasNext()) {
            actual.add(iterator.next());
        }
        assertEquals(result, actual);
        assertEquals(2, actual.size());
    }

    @Test
    public void testWithKeyValueSeparator() {
        Splitter keyValueSplitter = Splitter.on("=");
        MapSplitter mapSplitter = Splitter.on(":").withKeyValueSeparator(keyValueSplitter);
        List<Map.Entry<String, String>> expected = Arrays.asList(Map.entry("key1", "value1"), Map.entry("key2", "value2"));
        Map<String, String> result = mapSplitter.split("key1=value1&key2=value2");
        List<Map.Entry<String, String>> actual = new ArrayList<>(Iterators.asList(result.entrySet().iterator()));
        assertEquals(expected, actual);
    }
}