package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class SplitterTest {

    @Test
    public void testSplitterConstruction() {
        Splitter splitter = Splitter.on(' ');
        assertEquals(CharMatcher.is(' '), splitter.trimmer());
        assertEquals(false, splitter.omitEmptyStrings());
        assertEquals(Strategy.class, splitter.strategy().getClass());
        assertEquals(Integer.MAX_VALUE, splitter.limit());

        Splitter splitterWithLimit = splitter.limit(10);
        assertEquals(Integer.MAX_VALUE, splitter.limit());
        assertEquals(10, splitterWithLimit.limit());
    }

    @Test
    public void testSplitterStrategy() {
        Splitter splitter = Splitter.on(' ');
        Strategy strategy = splitter.strategy();
        Iterator<String> iterator = strategy.iterator(splitter, "test");

        assertEquals("test", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSplitterOnChar() {
        Splitter splitter = Splitter.on(' ');
        Iterator<String> iterator = splitter.split("test test test").iterator();
        assertEquals("test", iterator.next());
        assertEquals("test", iterator.next());
        assertEquals("test", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSplitterOnPattern() {
        String separatorPattern = "\\s+";
        Splitter splitter = Splitter.onPattern(Pattern.compile(separatorPattern));
        Iterator<String> iterator = splitter.split("test test test").iterator();
        assertEquals("test", iterator.next());
        assertEquals("test", iterator.next());
        assertEquals("test", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSplitterOnString() {
        String separator = "\t";
        Splitter splitter = Splitter.on(separator);
        Iterator<String> iterator = splitter.split("test\ttest\ttest").iterator();
        assertEquals("test", iterator.next());
        assertEquals("test", iterator.next());
        assertEquals("test", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSplitterOnEmptyString() {
        Splitter splitter = Splitter.on(' ');
        Iterator<String> iterator = splitter.split("").iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSplitterOnNullString() {
        Splitter splitter = Splitter.on(' ');
        Iterator<String> iterator = splitter.split(null).iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSplitterWithLimit() {
        String text = "1234567890123456789012345678901234567890";
        Splitter splitter = Splitter.on(' ').limit(5);
        List<String> result = new ArrayList<>();
        for (String s : splitter.splitToList(text)) {
            result.add(s);
        }
        assertEquals(5, result.size());
        assertEquals("12345", result.get(0));
        assertEquals("67890", result.get(1));
        assertEquals("12345", result.get(2));
        assertEquals("67890", result.get(3));
        assertEquals("12345", result.get(4));
    }

    @Test
    public void testSplitterWithEmptyStringsOmitted() {
        String text = "test test test test ";
        Splitter splitter = Splitter.on(' ').omitEmptyStrings();
        List<String> result = new ArrayList<>();
        for (String s : splitter.splitToList(text)) {
            result.add(s);
        }
        assertEquals(3, result.size());
        assertEquals("test", result.get(0));
        assertEquals("test", result.get(1));
        assertEquals("test", result.get(2));
    }

    @Test
    public void testSplitterWithTrimming() {
        String text = " test test test ";
        Splitter splitter = Splitter.on(' ').trimResults();
        List<String> result = new ArrayList<>();
        for (String s : splitter.splitToList(text)) {
            result.add(s);
        }
        assertEquals(3, result.size());
        assertEquals("test", result.get(0));
        assertEquals("test", result.get(1));
        assertEquals("test", result.get(2));
    }

    @Test
    public void testSplitterWithCustomTrimmer() {
        String text = " test test test ";
        CharMatcher trimmer = CharMatcher.is('t');
        Splitter splitter = Splitter.on(' ').trimResults(trimmer);
        List<String> result = new ArrayList<>();
        for (String s : splitter.splitToList(text)) {
            result.add(s);
        }
        assertEquals(3, result.size());
        assertEquals("est", result.get(0));
        assertEquals("est", result.get(1));
        assertEquals("est", result.get(2));
    }

    @Test
    public void testMapSplitter() {
        String text = "key1=value1 key2=value2 key3=value3";
        Splitter outerSplitter = Splitter.on(' ');
        Splitter entrySplitter = Splitter.on('=').omitEmptyStrings();
        MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator(entrySplitter);
        Map<String, String> result = mapSplitter.split(text);
        assertEquals(3, result.size());
        assertEquals("key1", result.keySet().iterator().next());
        assertEquals("value1", result.get("key1"));
        assertEquals("key2", result.keySet().iterator().next());
        assertEquals("value2", result.get("key2"));
        assertEquals("key3", result.keySet().iterator().next());
        assertEquals("value3", result.get("key3"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullSequence() {
        Splitter splitter = Splitter.on(' ');
        splitter.split(null);
    }
}