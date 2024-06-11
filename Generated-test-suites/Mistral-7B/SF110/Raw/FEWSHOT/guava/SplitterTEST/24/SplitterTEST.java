package com.google.common.base;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

public class SplitterTest {

    private Splitter stringSplitter;
    private Splitter charSplitter;
    private Splitter patternSplitter;
    private Splitter fixedLengthSplitter;

    @Before
    public void setup() {
        stringSplitter = Splitter.on(' ');
        charSplitter = Splitter.on('-');
        patternSplitter = Splitter.onPattern("\\s+");
        fixedLengthSplitter = Splitter.fixedLength(3);
    }

    @Test
    public void testNullSequence() {
        assertNull(stringSplitter.split(null));
        assertNull(charSplitter.split(null));
        assertNull(patternSplitter.split(null));
        assertNull(fixedLengthSplitter.split(null));
    }

    @Test
    public void testEmptySequence() {
        assertEquals(Collections.emptyList(), stringSplitter.split(""));
        assertEquals(Collections.singletonList(""), charSplitter.split(""));
        assertEquals(Collections.emptyList(), patternSplitter.split(""));
        assertEquals(Collections.emptyList(), fixedLengthSplitter.split(""));
    }

    @Test
    public void testSingleCharacterSequence() {
        assertEquals(Collections.singletonList(""), stringSplitter.split(""));
        assertEquals(Collections.singletonList("--"), charSplitter.split("--"));
        assertEquals(Collections.singletonList(""), patternSplitter.split(""));
        assertEquals(Collections.singletonList(""), fixedLengthSplitter.split(""));

        assertEquals(Collections.singletonList("a"), stringSplitter.split("a"));
        assertEquals(Collections.singletonList("a"), charSplitter.split("--a"));
        assertEquals(Collections.singletonList(""), patternSplitter.split(" "));
        assertEquals(Collections.singletonList("aaa"), fixedLengthSplitter.split("aaa"));

        assertEquals(Arrays.asList("", "a"), stringSplitter.split("a"));
        assertEquals(Arrays.asList("--", "a"), charSplitter.split("--a"));
        assertEquals(Arrays.asList("", ""), patternSplitter.split(" "));
        assertEquals(Arrays.asList("aaa", ""), fixedLengthSplitter.split("aaa"));
    }

    @Test
    public void testSimpleSequence() {
        assertEquals(Arrays.asList("foo", "bar"), stringSplitter.split("foo bar"));
        assertEquals(Arrays.asList("f", "oo", " ", "b", "ar"), charSplitter.split("foo bar"));
        assertEquals(Arrays.asList("foo", "bar"), patternSplitter.split("foo bar"));
        assertEquals(Arrays.asList("foo", "bar"), fixedLengthSplitter.split("foo bar"));

        assertEquals(Arrays.asList("foo", "bar", "baz"), stringSplitter.split("foo bar baz"));
        assertEquals(Arrays.asList("f", "oo", " ", "b", "ar", " ", "b", "az"), charSplitter.split("foo bar baz"));
        assertEquals(Arrays.asList("foo", "bar", "baz"), patternSplitter.split("foo bar baz"));
        assertEquals(Arrays.asList("foo", "bar", "baz"), fixedLengthSplitter.split("foo bar baz"));
    }

    @Test
    public void testComplexSequence() {
        assertEquals(Arrays.asList("foo", "bar", "baz", "qux"), stringSplitter.split("foo bar baz qux"));
        assertEquals(Arrays.asList("f", "oo", " ", "b", "ar", " ", "b", "az", " ", "q", "u", "x"), charSplitter.split("foo bar baz qux"));
        assertEquals(Arrays.asList("foo", "bar", "baz", "qux"), patternSplitter.split("foo bar baz qux"));
        assertEquals(Arrays.asList("foo", "bar", "baz", "qux"), fixedLengthSplitter.split("foo bar baz qux"));

        assertEquals(Arrays.asList("foo", "bar", "baz", "qux", "quux"), stringSplitter.split("foo bar baz qux quux"));
        assertEquals(Arrays.asList("f", "oo", " ", "b", "ar", " ", "b", "az", " ", "q", "u", "x", " ", "q", "u", "u", "x"), charSplitter.split("foo bar baz qux quux"));
        assertEquals(Arrays.asList("foo", "bar", "baz", "qux", "quux"), patternSplitter.split("foo bar baz qux quux"));
        assertEquals(Arrays.asList("foo", "bar", "baz", "qux", "quux"), fixedLengthSplitter.split("foo bar baz qux quux"));
    }

    @Test
    public void testLimit() {
        assertEquals(Arrays.asList("foo", "bar"), stringSplitter.limit(1).split("foo bar"));
        assertEquals(Arrays.asList("foo"), stringSplitter.limit(1).split("foo bar"));

        assertEquals(Arrays.asList("f", "oo", " "), charSplitter.limit(2).split("foo bar"));
        assertEquals(Arrays.asList("f", "oo"), charSplitter.limit(2).split("foo bar"));
        assertEquals(Arrays.asList("f"), charSplitter.limit(2).split("foo bar"));

        assertEquals(Arrays.asList("foo", "bar"), patternSplitter.limit(1).split("foo bar"));
        assertEquals(Arrays.asList("foo"), patternSplitter.limit(1).split("foo bar"));

        assertEquals(Arrays.asList("foo", "bar"), fixedLengthSplitter.limit(2).split("foo bar"));
        assertEquals(Arrays.asList("foo"), fixedLengthSplitter.limit(2).split("foo bar"));
        assertEquals(Collections.emptyList(), fixedLengthSplitter.limit(0).split("foo bar"));
    }

    @Test
    public void testTrim() {
        assertEquals(Arrays.asList("foo", "bar"), stringSplitter.trimResults().split("foo bar"));
        assertEquals(Arrays.asList("foo", "bar"), stringSplitter.trimResults("\\s").split("foo bar"));

        assertEquals(Arrays.asList("foo", "bar"), charSplitter.trimResults().split("foo bar"));
        assertEquals(Arrays.asList("f", "oo", " ", "b", "ar"), charSplitter.trimResults().split("foo bar"));
        assertEquals(Arrays.asList("f", "oo", " ", "b", "ar"), charSplitter.trimResults(CharMatcher.WHITESPACE).split("foo bar"));

        assertEquals(Arrays.asList("foo", "bar"), patternSplitter.trimResults().split("foo bar"));
        assertEquals(Arrays.asList("foo", "bar"), patternSplitter.trimResults(Pattern.compile("\\s+")).split("foo bar"));

        assertEquals(Arrays.asList("foo", "bar"), fixedLengthSplitter.trimResults().split("foo bar"));
        assertEquals(Arrays.asList("foo", "bar"), fixedLengthSplitter.trimResults(CharMatcher.WHITESPACE).split("foo bar"));
    }

    @Test
    public void testOmitEmptyStrings() {
        assertEquals(Arrays.asList("1", "2", "3"), stringSplitter.omitEmptyStrings().split("1 2 3"));
        assertEquals(Arrays.asList("1", "2", "3"), stringSplitter.omitEmptyStrings().split("1 2 3 "));
        assertEquals(Arrays.asList("1"), stringSplitter.omitEmptyStrings().split("1 "));
        assertEquals(Collections.emptyList(), stringSplitter.omitEmptyStrings().split(""));

        assertEquals(Arrays.asList("1", "2", "3"), charSplitter.omitEmptyStrings().split("1 2 3"));
        assertEquals(Arrays.asList("1", "2", "3"), charSplitter.omitEmptyStrings().split("1 2 3 "));
        assertEquals(Collections.singletonList("1"), charSplitter.omitEmptyStrings().split("1 "));
        assertEquals(Collections.emptyList(), charSplitter.omitEmptyStrings().split(""));

        assertEquals(Arrays.asList("1", "2", "3"), patternSplitter.omitEmptyStrings().split("1 2 3"));
        assertEquals(Arrays.asList("1", "2", "3"), patternSplitter.omitEmptyStrings().split("1 2 3 "));
        assertEquals(Collections.emptyList(), patternSplitter.omitEmptyStrings().split(""));

        assertEquals(Arrays.asList("1", "2", "3"), fixedLengthSplitter.omitEmptyStrings().split("1 2 3"));
        assertEquals(Arrays.asList("1", "2", "3"), fixedLengthSplitter.omitEmptyStrings().split("1 2 3 "));
        assertEquals(Collections.emptyList(), fixedLengthSplitter.omitEmptyStrings().split(""));
    }

    @Test
    public void testCustomSeparator() {
        assertEquals(Arrays.asList("a", "b"), stringSplitter.on('/').split("a/b"));
        assertEquals(Arrays.asList("a", "b"), charSplitter.on('/').split("a/b"));
        assertEquals(Arrays.asList("a", "b"), patternSplitter.onPattern("/").split("a/b"));
        assertEquals(Arrays.asList("a", "b"), fixedLengthSplitter.on(3).split("aab"));

        assertEquals(Arrays.asList("a", "b"), stringSplitter.on('/').omitEmptyStrings().split("a/b"));
        assertEquals(Arrays.asList("a", "b"), charSplitter.on('/').omitEmptyStrings().split("a/b"));
        assertEquals(Arrays.asList("a", "b"), patternSplitter.onPattern("/").omitEmptyStrings().split("a/b"));
        assertEquals(Arrays.asList("a", "b"), fixedLengthSplitter.on(3).omitEmptyStrings().split("aab"));

        assertEquals(Arrays.asList("a=1", "b=2"), stringSplitter.onPattern("=[^=]+").split("a=1=b=2"));
        assertEquals(Arrays.asList("a=1", "b=2"), charSplitter.onPattern("=[^=]+").split("a=1=b=2"));
        assertEquals(Arrays.asList("a=1", "b=2"), patternSplitter.onPattern("=[^=]+").split("a=1=b=2"));
        assertEquals(Arrays.asList("a=1", "b=2"), fixedLengthSplitter.onPattern(Pattern.compile("[^=]+")).split("a=1=b=2"));

        assertEquals(Arrays.asList("1=a", "2=b"), stringSplitter.onPattern("=[^=]+").omitEmptyStrings().split("a=1=b=2"));
        assertEquals(Arrays.asList("1=a", "2=b"), charSplitter.onPattern("=[^=]+").omitEmptyStrings().split("a=1=b=2"));
        assertEquals(Arrays.asList("1=a", "2=b"), patternSplitter.onPattern("=[^=]+").omitEmptyStrings().split("a=1=b=2"));
        assertEquals(Arrays.asList("1=a", "2=b"), fixedLengthSplitter.onPattern(Pattern.compile("[^=]+")).omitEmptyStrings().split("a=1=b=2"));
    }

    @Test
    public void testMapSplitter() {
        Map<String, String> map = stringSplitter.withKeyValueSeparator(":").split("key1:value1:key2:value2");
        assertEquals(2, map.size());
        assertEquals("key1", map.get("key1"));
        assertEquals("value1", map.get("value1"));
        assertEquals("key2", map.get("key2"));
        assertEquals("value2", map.get("value2"));

        map = charSplitter.withKeyValueSeparator(':').split("key1:value1:key2:value2");
        assertEquals(2, map.size());
        assertEquals("key1", map.get("key1"));
        assertEquals("value1", map.get("value1"));
        assertEquals("key2", map.get("key2"));
        assertEquals("value2", map.get("value2"));

        map = patternSplitter.withKeyValueSeparator(Pattern.compile(":")).split("key1:value1:key2:value2");
        assertEquals(2, map.size());
        assertEquals("key1", map.get("key1"));
        assertEquals("value1", map.get("value1"));
        assertEquals("key2", map.get("key2"));
        assertEquals("value2", map.get("value2"));

        map = fixedLengthSplitter.withKeyValueSeparator(3).split("key1:value1:key2:value2");
        assertEquals(2, map.size());
        assertEquals("key1", map.get("key1"));
        assertEquals("value1", map.get("value1"));
        assertEquals("key2", map.get("key2"));
        assertEquals("value2", map.get("value2"));

        map = stringSplitter.withKeyValueSeparator(":").omitEmptyStrings().split("key1:value1:key2:value2");
        assertEquals(2, map.size());
        assertEquals("key1", map.get("key1"));
        assertEquals("value1", map.get("value1"));
        assertFalse(map.containsKey(""));
        assertFalse(map.containsValue(""));

        map = charSplitter.withKeyValueSeparator(':').omitEmptyStrings().split("key1:value1:key2:value2");
        assertEquals(2, map.size());
        assertEquals("key1", map.get("key1"));
        assertEquals("value1", map.get("value1"));
        assertFalse(map.containsKey((Object) ""));
        assertFalse(map.containsValue((Object) ""));

        map = patternSplitter.withKeyValueSeparator(Pattern.compile(":")).omitEmptyStrings().split("key1:value1:key2:value2");
        assertEquals(2, map.size());
        assertEquals("key1", map.get("key1"));
        assertEquals("value1", map.get("value1"));
        assertFalse(map.containsKey((Object) ""));
        assertFalse(map.containsValue((Object) ""));

        map = fixedLengthSplitter.withKeyValueSeparator(3).omitEmptyStrings().split("key1:value1:key2:value2");
        assertEquals(2, map.size());
        assertEquals("key1", map.get("key1"));
        assertEquals("value1", map.get("value1"));
        assertFalse(map.containsKey((Object) ""));
        assertFalse(map.containsValue((Object) ""));
    }
}