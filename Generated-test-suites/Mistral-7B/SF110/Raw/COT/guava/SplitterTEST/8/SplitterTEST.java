package com.example.junit;

import static org.junit.Assert.*;
import com.google.common.base.Splitter;
import com.google.common.base.Joiner;
import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.collect.LinkedHashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SplitterTest {

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSplitterConstruction() {
        Splitter splitter = Splitter.on('-');
        assertNotNull(splitter);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSplitterOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.is('-'));
        CharSequence sequence = "test-test-test";
        List<String> result = new ArrayList<>(Arrays.asList("test", "test", "test"));
        Iterable<String> actual = splitter.split(sequence);
        List<String> expected = new ArrayList<>(result);
        Iterator<String> iterator = Joiner.on(", ").split(Joiner.join(expected, "")).listIterator();

        assertEquals(expected.size(), actual.size());
        while (iterator.hasNext()) {
            assertEquals(iterator.next(), next(actual).iterator().next());
        }
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSplitterOnString() {
        Splitter splitter = Splitter.on("-");
        CharSequence sequence = "test-test-test";
        List<String> result = new ArrayList<>(Arrays.asList("test", "test", "test"));
        Iterable<String> actual = splitter.split(sequence);
        List<String> expected = new ArrayList<>(result);
        Iterator<String> iterator = Joiner.on(", ").split(Joiner.join(expected, "")).listIterator();

        assertEquals(expected.size(), actual.size());
        while (iterator.hasNext()) {
            assertEquals(iterator.next(), next(actual).iterator().next());
        }
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSplitterOnPattern() {
        Pattern separatorPattern = Pattern.compile("\\-");
        Splitter splitter = Splitter.on(separatorPattern);
        CharSequence sequence = "test-test-test";
        List<String> result = new ArrayList<>(Arrays.asList("test", "test", "test"));
        Iterable<String> actual = splitter.split(sequence);
        List<String> expected = new ArrayList<>(result);
        Iterator<String> iterator = Joiner.on(", ").split(Joiner.join(expected, "")).listIterator();

        assertEquals(expected.size(), actual.size());
        while (iterator.hasNext()) {
            assertEquals(iterator.next(), next(actual).iterator().next());
        }
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSplitterFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        CharSequence sequence = "test123test456test789";
        List<String> result = new ArrayList<>(Arrays.asList("test123", "test456", "test789"));
        Iterable<String> actual = splitter.split(sequence);
        List<String> expected = new ArrayList<>(result);
        Iterator<String> iterator = Joiner.on(", ").split(Joiner.join(expected, "")).listIterator();

        assertEquals(expected.size(), actual.size());
        while (iterator.hasNext()) {
            assertEquals(iterator.next(), next(actual).iterator().next());
        }
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSplitterOnEmptyString() {
        Splitter splitter = Splitter.on('-');
        CharSequence sequence = "";
        List<String> expected = Collections.emptyList();
        Iterable<String> actual = splitter.split(sequence);
        Iterator<String> iterator = expected.iterator();

        assertEquals(expected.size(), actual.size());
        while (iterator.hasNext()) {
            assertEquals(iterator.next(), next(actual).iterator().next());
        }
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSplitterOmitEmptyStrings() {
        Splitter splitter = Splitter.on('-').omitEmptyStrings();
        CharSequence sequence = "test--test-test";
        List<String> result = new ArrayList<>(Arrays.asList("test", "test"));
        Iterable<String> actual = splitter.split(sequence);
        List<String> expected = new ArrayList<>(result);
        Iterator<String> iterator = Joiner.on(", ").split(Joiner.join(expected, "")).listIterator();

        assertEquals(expected.size(), actual.size());
        while (iterator.hasNext()) {
            assertEquals(iterator.next(), next(actual).iterator().next());
        }
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSplitterLimit() {
        Splitter splitter = Splitter.on('-').limit(2);
        CharSequence sequence = "test123test456test789";
        List<String> result = new ArrayList<>(Arrays.asList("test123", "test456"));
        Iterable<String> actual = splitter.split(sequence);
        List<String> expected = new ArrayList<>(result);
        Iterator<String> iterator = Joiner.on(", ").split(Joiner.join(expected, "")).listIterator();

        assertEquals(expected.size(), actual.size());
        while (iterator.hasNext()) {
            assertEquals(iterator.next(), next(actual).iterator().next());
        }
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testMapSplitter() {
        Splitter keySplitter = Splitter.on(':');
        Splitter valueSplitter = Splitter.on('-');
        Map<String, String> result = new LinkedHashMap<>();
        result.put("key1", "value1-value2");
        result.put("key2:key3", "value3-value4-value5");
        Map<String, String> actual = new Splitter.MapSplitter(keySplitter, valueSplitter).split("key1:value1-value2,key2:key3:value3-value4-value5");

        assertEquals(result.size(), actual.size());
        for (Map.Entry<String, String> entry : result.entrySet()) {
            String[] keys = entry.getKey().split(":");
            String[] values = entry.getValue().split("-");
            assertEquals(keys.length, 2);
            assertEquals(keys[0], next(actual.entrySet().iterator().next()).getKey());
            assertEquals(values.length, 2);
            assertEquals(values[0], next(actual.entrySet().iterator().next()).getValue());
        }
    }

    private static <T> T next(Iterator<T> iterator) {
        return iterator.next();
    }

}