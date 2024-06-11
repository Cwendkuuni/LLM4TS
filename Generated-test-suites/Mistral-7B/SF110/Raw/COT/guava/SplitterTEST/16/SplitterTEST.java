package com.google.common.base; // Make sure to import all necessary packages for the tests

import org.junit.Test;
import static org.junit.Assert.*;

public class SplitterTest {

    

    // Test case for on(char) method
    @Test
    public void testSplitterOnChar() {
        String input = "one,two,three,four,five";
        Splitter splitter = Splitter.on(',').trimResults();
        List<String> result = splitter.splitToList(input);
        assertEquals(Arrays.asList("one", "two", "three", "four", "five"), result);
    }

    // Test case for on(String) method
    @Test
    public void testSplitterOnString() {
        String input = "one|two|three|four|five";
        Splitter splitter = Splitter.on("|");
        List<String> result = splitter.splitToList(input);
        assertEquals(Arrays.asList("one", "two", "three", "four", "five"), result);
    }

    // Test case for on(Pattern) method
    @Test
    public void testSplitterOnPattern() {
        String input = "one-two-three-four-five";
        Pattern pattern = Pattern.compile("-");
        Splitter splitter = Splitter.on(pattern);
        List<String> result = splitter.splitToList(input);
        assertEquals(Arrays.asList("one", "two", "three", "four", "five"), result);
    }

    // Test case for onPattern(String) method
    @Test
    public void testSplitterOnPatternString() {
        String input = "one-two-three-four-five";
        Splitter splitter = Splitter.onPattern("[-]");
        List<String> result = splitter.splitToList(input);
        assertEquals(Arrays.asList("one", "two", "three", "four", "five"), result);
    }

    // Test case for fixedLength(int) method
    @Test
    public void testSplitterFixedLength() {
        String input = "oneABCtwoABCthreeABCfourABCfive";
        Splitter splitter = Splitter.fixedLength(3);
        List<String> result = splitter.splitToList(input);
        assertEquals(Arrays.asList("oneABC", "ABC", "twoABC", "ABC", "threeABC", "ABC", "fourABC", "ABC", "five"), result);
    }

    // Test case for omitEmptyStrings() method
    @Test
    public void testSplitterOmitEmptyStrings() {
        String input = "one,,three,four,,five";
        Splitter splitter = new Splitter(Splitter.on(","));
        List<String> result = splitter.splitToList(input);
        assertEquals(Arrays.asList("one", "three", "four", "five"), result);
    }

    // Test case for limit(int) method
    @Test
    public void testSplitterLimit() {
        String input = "one,two,three,four,five,six,seven,eight,nine,ten";
        Splitter splitter = Splitter.on(",").limit(5);
        List<String> result = splitter.splitToList(input);
        assertEquals(Arrays.asList("one", "two", "three", "four", "five"), result);
    }

    // Test case for withKeyValueSeparator(String) method
    @Test
    public void testMapSplitterWithKeyValueSeparatorString() {
        String input = "name=John,age=30,city=New York";
        Splitter keySplitter = Splitter.on(",");
        Splitter valueSplitter = Splitter.on("=");
        MapSplitter mapSplitter = new Splitter(keySplitter).withKeyValueSeparator(valueSplitter);
        Map<String, String> result = mapSplitter.split(input);
        assertEquals(Arrays.asList(entries("name", "John"), entries("age", "30"), entries("city", "New York")), result.entrySet());
    }

    // Test case for withKeyValueSeparator(char) method
    @Test
    public void testMapSplitterWithKeyValueSeparatorChar() {
        String input = "name=John,age=30,city=New York";
        Splitter keySplitter = Splitter.on(",");
        Splitter valueSplitter = Splitter.on("=");
        MapSplitter mapSplitter = new Splitter(keySplitter).withKeyValueSeparator(valueSplitter);
        Map<String, String> result = mapSplitter.split(input);
        assertEquals(Arrays.asList(entries("name", "John"), entries("age", "30"), entries("city", "New York")), result.entrySet());
    }

    // Test case for withKeyValueSeparator(Splitter) method
    @Test
    public void testMapSplitterWithKeyValueSeparatorSplitter() {
        String input = "name=John,age=30,city=New York";
        Splitter keySplitter = Splitter.on(",");
        Splitter valueSplitter = Splitter.on("=");
        MapSplitter mapSplitter = new Splitter(keySplitter).withKeyValueSeparator(valueSplitter);
        Map<String, String> result = mapSplitter.split(input);
        assertEquals(Arrays.asList(entries("name", "John"), entries("age", "30"), entries("city", "New York")), result.entrySet());
    }

    private static Map.Entry<String, String> entries(String key, String value) {
        return new AbstractMap.SimpleImmutableEntry<>(key, value);
    }

    
}