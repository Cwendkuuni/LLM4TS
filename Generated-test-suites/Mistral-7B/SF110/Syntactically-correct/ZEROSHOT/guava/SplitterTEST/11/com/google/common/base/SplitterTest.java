package com.example.junit;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class SplitterTest {

    @org.junit.Test
    public void testOnChar() {
        Splitter splitter = Splitter.on('-');
        CharSequence sequence = "apple-banana-orange";
        List<String> result = splitter.split(sequence);
        List<String> expected = Arrays.asList("apple", "banana", "orange");
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.is('-'));
        CharSequence sequence = "apple-banana-orange";
        List<String> result = splitter.split(sequence);
        List<String> expected = Arrays.asList("apple", "banana", "orange");
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testOnString() {
        Splitter splitter = Splitter.on("-");
        CharSequence sequence = "apple-banana-orange";
        List<String> result = splitter.split(sequence);
        List<String> expected = Arrays.asList("apple", "banana", "orange");
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testOnPattern() {
        Splitter splitter = Splitter.on(Pattern.compile("-"));
        CharSequence sequence = "apple-banana-orange";
        List<String> result = splitter.split(sequence);
        List<String> expected = Arrays.asList("apple", "banana", "orange");
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testOnPatternWithEmptyString() {
        Splitter splitter = Splitter.on(Pattern.compile("\\s+"));
        CharSequence sequence = "apple   banana     orange";
        List<String> result = splitter.split(sequence);
        List<String> expected = Arrays.asList("apple", "", "banana", "", "orange");
        if (splitter.omitEmptyStrings()) {
            expected.remove(1);
            expected.remove(3);
        }
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        CharSequence sequence = "1234567890";
        List<String> result = splitter.split(sequence);
        List<String> expected = Arrays.asList("123", "456", "789");
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testLimit() {
        Splitter splitter = Splitter.on('-').limit(3);
        CharSequence sequence = "apple-banana-orange-grape-kiwi";
        List<String> result = Arrays.asList(splitter.split(sequence));
        List<String> expected = Arrays.asList("apple-banana-orange");
        assertEquals(expected, result.get(0));
    }

    @org.junit.Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on('-').trimResults();
        CharSequence sequence = " apple-banana-orange ";
        List<String> result = splitter.split(sequence);
        List<String> expected = Arrays.asList("apple", "banana", "orange");
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testTrimResultsWithCharMatcher() {
        Splitter splitter = Splitter.on('-').trimResults(CharMatcher.whitespace());
        CharSequence sequence = " apple-banana-orange ";
        List<String> result = splitter.split(sequence);
        List<String> expected = Arrays.asList("apple", "banana", "orange");
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on('-');
        CharSequence sequence = "apple-banana-orange";
        List<String> result = splitter.splitToList(sequence);
        List<String> expected = Arrays.asList("apple", "banana", "orange");
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testWithKeyValueSeparator() {
        Splitter keyValueSplitter = Splitter.on(":");
        Multimap<String, String> map = Splitter.on("-").withKeyValueSeparator(keyValueSplitter).split(
                "key1:value1-key2:value2-key3:value3");
        List<String> keys = new ArrayList<String>(map.keySet());
        List<String> values = new ArrayList<String>(map.values());
        List<Object> expected = Arrays.asList(Arrays.asList("key1", "value1"), Arrays.asList("key2", "value2"), Arrays.asList("key3", "value3"));
        assertEquals(expected, map.entrySet());
    }
}