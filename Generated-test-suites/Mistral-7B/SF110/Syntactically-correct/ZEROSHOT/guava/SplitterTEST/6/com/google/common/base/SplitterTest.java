package com.example.junit;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

public class SplitterTest {

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on('-');
        List<String> result = splitter.split("test-string");
        assertEquals(Arrays.asList("test", "string"), result);
    }

    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.onPattern("\\s+");
        List<String> result = splitter.split("test  string ");
        assertEquals(Arrays.asList("test", "string"), result);
    }

    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on("-");
        List<String> result = splitter.split("test-string");
        assertEquals(Arrays.asList("test", "string"), result);
    }

    @Test
    public void testOnRegex() {
        Splitter splitter = Splitter.on("\\s+");
        List<String> result = splitter.split("Test String   ");
        assertEquals(Arrays.asList("Test", "String"), result);
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(" ").limit(3);
        List<String> result = Arrays.asList("test one two three".split(splitter));
        assertEquals(Arrays.asList("test", "one", "two"), result);
    }

    @Test
    public void testLimitZero() {
        Splitter splitter = Splitter.on(" ").limit(0);
        List<String> result = Arrays.asList("test string".split(splitter));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testTrim() {
        Splitter splitter = Splitter.on(" ").limit(3).trimResults();
        List<String> result = Arrays.asList(" test one two three".split(splitter));
        assertEquals(Arrays.asList("one", "two"), result);
    }

    @Test
    public void testTrimWhitespace() {
        Splitter splitter = Splitter.on(" ").limit(3).trimResults(CharMatcher.WHITESPACE);
        List<String> result = Arrays.asList(" test one two three".split(splitter));
        assertEquals(Arrays.asList("one", "two"), result);
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(" ").limit(3).omitEmptyStrings();
        List<String> result = Arrays.asList("test  one two three".split(splitter));
        assertEquals(Arrays.asList("one", "two"), result);
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        List<String> result = Arrays.asList("1234567890".split(splitter));
        assertEquals(Arrays.asList("123", "456", "578", "90"), result);
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(" ").limit(3);
        List<String> result = splitter.splitToList("test one two three");
        assertEquals(Arrays.asList("test", "one", "two"), result);
    }

    @Test
    public void testMapSplitter() {
        Multimap<String, String> map = Splitter.on(":").withKeyValueSeparator(Splitter.on("=")).split(
                "test:key1=value1:key2=value2");
        assertEquals(2, map.size());
        assertEquals(Optional.of("key1"), map.get("test"));
        assertEquals(Collections.unmodifiableList(Arrays.asList("value1")), map.get("key1"));
        assertEquals(Optional.of("key2"), map.get("test2"));
        assertEquals(Collections.unmodifiableList(Arrays.asList("value2")), map.get("key2"));
    }
}