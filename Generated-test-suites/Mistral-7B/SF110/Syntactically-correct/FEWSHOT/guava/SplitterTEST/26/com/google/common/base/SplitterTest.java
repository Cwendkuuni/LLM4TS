package com.google.common.base;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SplitterTest {

    @Test
    public void testSplitterOnChar() {
        Splitter splitter = Splitter.on('-');
        String input = "--foo--bar--baz--qux--";
        List<String> expected = Arrays.asList("foo", "bar", "baz", "qux");
        List<String> actual = new ArrayList<String>();
        for (String str : splitter.split(input)) {
            actual.add(str);
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testSplitterOnPattern() {
        String pattern = "[,-]";
        Splitter splitter = Splitter.onPattern(pattern);
        String input = "--foo--bar--baz--qux--";
        List<String> expected = Arrays.asList("foo", "bar", "baz", "qux");
        List<String> actual = new ArrayList<String>();
        for (String str : splitter.split(input)) {
            actual.add(str);
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testSplitterOnString() {
        Splitter splitter = Splitter.on("-");
        String input = "--foo--bar--baz--qux--";
        List<String> expected = Arrays.asList("foo", "bar", "baz", "qux");
        List<String> actual = new ArrayList<String>();
        for (String str : splitter.split(input)) {
            actual.add(str);
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testSplitterFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        String input = "abcdefghijklmnopqrstuvwxyz";
        List<String> expected = Arrays.asList("abc", "def", "ghi", "ijk", "lmn", "opq", "rst", "uvw", "xyz");
        List<String> actual = new ArrayList<String>();
        for (String str : splitter.split(input)) {
            actual.add(str);
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testSplitterOmitEmptyStrings() {
        Splitter splitter = Splitter.on("-").omitEmptyStrings();
        String input = "--foo---bar--baz---qux--";
        List<String> expected = Arrays.asList("foo", "bar", "baz");
        List<String> actual = new ArrayList<String>();
        for (String str : splitter.split(input)) {
            actual.add(str);
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testSplitterLimit() {
        Splitter splitter = Splitter.on("-").limit(5);
        String input = "-------------------------------";
        List<String> expected = Arrays.asList("------------------", "----------------");
        List<String> actual = new ArrayList<String>();
        for (String str : splitter.split(input)) {
            actual.add(str);
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testMapSplitter() {
        Splitter keySplitter = Splitter.on("=");
        Splitter valueSplitter = Splitter.on(":");
        Map<String, String> expected = new LinkedHashMap<String, String>() {{
            put("foo=bar:baz", "foo", "bar:baz");
            put("foo=bar:baz:qux", "foo", "bar:baz", "qux");
        }};
        Map<String, String> actual = Splitter.MapSplitter.withKeyValueSeparator(keySplitter, valueSplitter).split(
                "foo=bar:baz\nfoo=bar:baz:qux");
        assertEquals(expected, actual);
    }
}