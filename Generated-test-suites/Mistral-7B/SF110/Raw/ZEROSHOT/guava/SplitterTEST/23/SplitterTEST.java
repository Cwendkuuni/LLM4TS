package com.example.junit4;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
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
        List<String> result = new ArrayList<>();
        result.addAll(Arrays.asList("a-b-c-d".split("-", 0), "a-b-c-d".split("-")));
        assertEquals(result, splitter.split("a-b-c-d"));
    }

    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.is('-'));
        List<String> result = new ArrayList<>();
        result.addAll(Arrays.asList("a--b--c--d".split(CharMatcher.is('-'), 0), "a--b--c--d".split(CharMatcher.is('-'))));
        assertEquals(result, splitter.split("a--b--c--d"));
    }

    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on("-");
        List<String> result = new ArrayList<>();
        result.addAll(Arrays.asList("a-b-c-d".split("-", 0), "a-b-c-d".split("-")));
        assertEquals(result, splitter.split("a-b-c-d"));
    }

    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.onPattern("[\\s]+");
        List<String> result = new ArrayList<>();
        result.addAll(Arrays.asList("foo bar baz".split(Pattern.compile("[\\s]+"), 0), "foo bar baz".split(Pattern.compile("[\\s]+"))));
        assertEquals(result, splitter.split("foo bar baz"));
    }

    @Test
    public void testOnPatternWithLimit() {
        Splitter splitter = Splitter.onPattern("[\\s]+").limit(2);
        List<String> result = new ArrayList<>();
        result.addAll(Arrays.asList("foo bar baz quux".split(Pattern.compile("[\\s]+"), 0), "foo bar baz quux".split(Pattern.compile("[\\s]+"))));
        assertEquals(Arrays.asList("foo", "bar baz"), splitter.split("foo bar baz quux"));
    }

    @Test
    public void testOnFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        List<String> result = new ArrayList<>();
        result.addAll(Arrays.asList("12345678901234567890123456".split("", 3), "12345678901234567890123456".split("", 3)));
        assertEquals(result, splitter.split("12345678901234567890123456"));
    }

    @Test
    public void testTrimming() {
        Splitter splitter = Splitter.on(" ").trimResults();
        List<String> result = new ArrayList<>();
        result.addAll(Arrays.asList("foo bar baz".split("\\s+"), "foo bar baz".split("\\s+")));
        List<String> expected = Arrays.asList("foo", "bar", "baz");
        assertEquals(expected, result);
    }

    @Test
    public void testOmittingEmptyStrings() {
        Splitter splitter = Splitter.on(" ").omitEmptyStrings();
        List<String> result = new ArrayList<>();
        result.addAll(Arrays.asList("foo bar baz".split("\\s+"), "foo bar baz".split("\\s+")));
        List<String> expected = Arrays.asList("bar", "baz");
        assertEquals(expected, result);
    }

    @Test
    public void testWithKeyValueSeparator() {
        Splitter splitterKeyValue = Splitter.on(":");
        Splitter splitterKey = Splitter.on(" ");
        Map<String, String> result = splitterKeyValue.withKeyValueSeparator(splitterKey).split("foo: bar 1: baz 2: quux");
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("foo", "bar");
        expected.put("1", "baz");
        expected.put("2", "quux");
        assertEquals(expected, result);
    }
}