package com.example.junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on('-');
        List<String> result = new ArrayList<>();
        result.addAll(Arrays.asList("apple-banana-orange".split("-", 0), splitter.split("apple-banana-orange")));
        assertEquals(result, Arrays.asList("apple", "banana", "orange"));
    }

    @Test
    public void testOnCharMatcherSeparator() {
        Splitter splitter = Splitter.on(CharMatcher.is('-'));
        List<String> result = new ArrayList<>();
        result.addAll(Arrays.asList("apple--banana--orange".split(Pattern.quote("-"), 0), splitter.split("apple--banana--orange")));
        assertEquals(result, Arrays.asList("apple", "", "banana", "", "orange"));
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on("-");
        List<String> result = new ArrayList<>();
        result.addAll(Arrays.asList("apple:banana:orange".split(":", 0), splitter.split("apple:banana:orange")));
        assertEquals(result, Arrays.asList("apple", "banana", "orange"));
    }

    @Test
    public void testOnPatternSeparator() {
        Pattern pattern = Pattern.compile(":");
        Splitter splitter = Splitter.on(pattern);
        List<String> result = new ArrayList<>();
        result.addAll(Arrays.asList("apple:banana:orange".split(pattern), splitter.split("apple:banana:orange")));
        assertEquals(result, Arrays.asList("apple", "banana", "orange"));
    }

    @Test
    public void testFixedLengthSplitter() {
        Splitter splitter = Splitter.fixedLength(3);
        List<String> result = new ArrayList<>();
        result.addAll(Arrays.asList("1234567890123456789012345678901234567890".split("", 3), splitter.split("1234567890123456789012345678901234567890")));
        assertEquals(result, Arrays.asList("123", "456", "789", "012", "345", "678", "901", "234", "567", "890"));
    }

    @Test
    public void testOmmitEmptyStrings() {
        Splitter splitter = Splitter.on("-").omitEmptyStrings();
        List<String> result = new ArrayList<>();
        result.addAll(Arrays.asList("apple--banana---orange".split("-"), splitter.split("apple--banana---orange")));
        assertEquals(result, Arrays.asList("apple", "banana", "orange"));
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on("-").limit(2);
        List<String> result = new ArrayList<>();
        result.addAll(Arrays.asList("apple---banana---orange---grape---kiwi--pear---strawberry".split("-"), splitter.split("apple---banana---orange---grape---kiwi--pear---strawberry")));
        assertEquals(result, Arrays.asList("apple", "banana", "orange"));
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on("-").trimResults();
        List<String> result = new ArrayList<>();
        result.addAll(Arrays.asList(" apple--banana---orange ".split("-"), splitter.split(" apple--banana---orange ")));
        assertEquals(result, Arrays.asList("apple", "banana", "orange"));
    }

    @Test
    public void testTrimResultsWithCustomTrimmer() {
        Splitter splitter = Splitter.on("-").trimResults(CharMatcher.is(":"));
        List<String> result = new ArrayList<>();
        result.addAll(Arrays.asList(" apple:banana:orange ".split(":", 0), splitter.split(" apple:banana:orange ")));
        assertEquals(result, Arrays.asList("apple", "banana", "orange"));
    }

    @Test
    public void testIterable() {
        Splitter splitter = Splitter.on("-");
        Iterator<String> iterator = splitter.split("apple--banana--orange").iterator();
        List<String> result = new ArrayList<>();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        assertEquals(result, Arrays.asList("apple", "banana", "orange"));
    }

    @Test
    public void testList() {
        Splitter splitter = Splitter.on("-");
        List<String> result = splitter.splitToList("apple--banana--orange");
        assertEquals(result, Arrays.asList("apple", "banana", "orange"));
    }

    @Test
    public void testMapSplitter() {
        Splitter keySplitter = Splitter.on(":");
        Splitter valueSplitter = Splitter.on("-");
        MapSplitter mapSplitter = new Splitter(Splitter.on("-"), keySplitter);
        Map<String, String> result = mapSplitter.split("apple:apple-banana:orange:banana-grape:grape");
        assertEquals(result.size(), 3);
        assertEquals(result.get("apple"), "apple");
        assertEquals(result.get("banana"), "banana-grape");
        assertEquals(result.get("grape"), "grape");
    }
}