package com.example.junit4;

import static org.junit.Assert.*;
import static com.google.common.base.Splitter.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = on('-');
        CharSequence sequence = "a-b-c-d-e";
        List<String> expected = Arrays.asList("a", "b", "c", "d", "e");
        List<String> result = splitter.split(sequence);
        assertEquals(expected, result);
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = on(":");
        CharSequence sequence = "a:b:c:d:e";
        List<String> expected = Arrays.asList("a", "b", "c", "d", "e");
        List<String> result = splitter.split(sequence);
        assertEquals(expected, result);
    }

    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = onPattern("\\s+");
        CharSequence sequence = "a b c   d e   f g h";
        List<String> expected = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h");
        List<String> result = splitter.split(sequence);
        assertEquals(expected, result);
    }

    @Test
    public void testFixedLengthSplitter() {
        Splitter splitter = fixedLength(2);
        CharSequence sequence = "12345678";
        List<String> expected = Arrays.asList("12", "34", "56", "78");
        List<String> result = splitter.split(sequence);
        assertEquals(expected, result);
    }

    @Test
    public void testOnEmptyString() {
        Splitter splitter = on('-');
        CharSequence sequence = "";
        List<String> expected = Collections.emptyList();
        List<String> result = splitter.split(sequence);
        assertEquals(expected, result);
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = on(' ').trimResults();
        CharSequence sequence = " a b c d e ";
        List<String> expected = Arrays.asList("a", "b", "c", "d", "e");
        List<String> result = splitter.split(sequence);
        assertEquals(expected, result);
    }

    @Test
    public void testTrimResultsWithCharMatcher() {
        Splitter splitter = on("\\s").trimResults(CharMatcher.WHITESPACE);
        CharSequence sequence = " a b c d e ";
        List<String> expected = Arrays.asList("a", "b", "c", "d", "e");
        List<String> result = splitter.split(sequence);
        assertEquals(expected, result);
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = on(':').omitEmptyStrings();
        CharSequence sequence = "a::b::c::d::e::";
        List<String> expected = Arrays.asList("a", "c", "d", "e");
        List<String> result = splitter.split(sequence);
        assertEquals(expected, result);
    }

    @Test
    public void testLimit() {
        Splitter splitter = on(':').limit(3);
        CharSequence sequence = "a::b::c::d::e::f::g::h::i::j::";
        List<String> expected = Arrays.asList("a", "b", "c");
        List<String> result = splitter.split(sequence);
        assertEquals(expected, result);
    }

    @Test
    public void testWithKeyValueSeparator() {
        Splitter keyValueSplitter = on(':');
        MapSplitter mapSplitter = withKeyValueSeparator(keyValueSplitter);
        CharSequence sequence = "name:John:age:30:city:New York";
        Map<String, String> expected = new LinkedHashMap<String, String>() {{
            put("name", "John");
            put("age", "30");
            put("city", "New York");
        }};
        Map<String, String> result = mapSplitter.split(sequence);
        assertEquals(expected, result);
    }
}