package com.example.junit4;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.collect.LinkedHashMap;

@GwtIncompatible
public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on('-');
        List<String> result = new ArrayList<>(Arrays.asList("one-two-three".split("-")));
        List<String> expected = Arrays.asList("one", "two", "three");
        assertEquals(expected, result);
    }

    @Test
    public void testOnCharMatcherSeparator() {
        CharMatcher separatorMatcher = CharMatcher.is('-');
        Splitter splitter = Splitter.on(separatorMatcher);
        List<String> result = new ArrayList<>(Arrays.asList("one--two--three".split(separatorMatcher)));
        List<String> expected = Arrays.asList("one", "two", "three");
        assertEquals(expected, result);
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on(":");
        List<String> result = new ArrayList<>(Arrays.asList("one:two:three".split(":")));
        List<String> expected = Arrays.asList("one", "two", "three");
        assertEquals(expected, result);
    }

    @Test
    public void testOnPatternSeparator() {
        Pattern separatorPattern = Pattern.compile(":");
        Splitter splitter = Splitter.on(separatorPattern);
        List<String> result = new ArrayList<>(Arrays.asList("one:two:three".split(separatorPattern.pattern())));
        List<String> expected = Arrays.asList("one", "two", "three");
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testOnNullSeparator() {
        Splitter.on(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnEmptyPatternSeparator() {
        Splitter.onPattern("");
    }

    @Test
    public void testFixedLengthSplitter() {
        Splitter splitter = Splitter.fixedLength(3);
        List<String> result = new ArrayList<>(Arrays.asList("123456789012345678901234567890".split(splitter)));
        List<String> expected = Arrays.asList("123", "456", "789", "012", "345", "678", "901", "234", "567", "890");
        assertEquals(expected, result);
    }

    @Test
    public void testLimitSplitter() {
        Splitter splitter = Splitter.on('-').limit(3);
        List<String> result = new ArrayList<>(Arrays.asList("one--two--three--four--five--six--seven--eight--nine--ten--eleven--twelve--thirteen--fourteen--fifteen--sixteen--seventeen--eighteen--nineteen--twenty--twenty-one--twenty-two--twenty-three--twenty-four--twenty-five--twenty-six--twenty-seven--twenty-eight--twenty-nine--thirty".split("-", 3)));
        List<String> expected = Arrays.asList("one--two--three", "four--five--six", "seven--eight--nine");
        assertEquals(expected, result);
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on('-').omitEmptyStrings();
        List<String> result = new ArrayList<>(Arrays.asList("one--two--three--".split("-")));
        List<String> expected = Arrays.asList("one--two--three");
        assertEquals(expected, result);
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(":").trimResults();
        List<String> result = new ArrayList<>(Arrays.asList(" :one:two:three: ".split(":")));
        List<String> expected = Arrays.asList("one", "two", "three");
        assertEquals(expected, result);
    }

    @Test
    public void testTrimResultsWithCustomTrimmer() {
        Splitter splitter = Splitter.on(":").trimResults(CharMatcher.WHITESPACE);
        List<String> result = new ArrayList<>(Arrays.asList(" :one:two:three: ".split(":")));
        List<String> expected = Arrays.asList("one", "two", "three");
        assertEquals(expected, result);
    }

    @Test
    public void testMapSplitter() {
        Splitter outerSplitter = Splitter.on(":");
        Splitter entrySplitter = Splitter.on("-");
        MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator(entrySplitter);
        List<String[]> input = Arrays.asList("key1:value1-key2:value2-key3:value3-key4:value4".split(":"));
        List<Map.Entry<String, String>> expected = Arrays.asList(
                Map.entry("key1", "value1"),
                Map.entry("key2", "value2"),
                Map.entry("key3", "value3"),
                Map.entry("key4", "value4")
        );
        List<Map.Entry<String, String>> result = mapSplitter.split(input.get(0));
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEntry() {
        Splitter outerSplitter = Splitter.on(":");
        Splitter entrySplitter = Splitter.on("-");
        MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator(entrySplitter);
        List<String[]> input = Arrays.asList("key1:value1--key2:value2-invalidEntry".split(":"));
        mapSplitter.split(input.get(0));
    }
}