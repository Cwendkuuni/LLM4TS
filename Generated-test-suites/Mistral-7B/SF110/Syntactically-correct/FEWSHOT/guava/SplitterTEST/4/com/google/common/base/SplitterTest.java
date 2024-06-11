package com.google.common.base;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on('-');
        List<String> expected = Arrays.asList("one", "two", "three");
        List<String> result = splitter.split("one-two-three");
        assertEquals(expected, result);
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on(":");
        List<String> expected = Arrays.asList("one:two:three:four", "", "five:six:seven:eight");
        List<String> result = splitter.split("one:two:three:four::five:six:seven:eight");
        assertEquals(expected, result);
    }

    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.onPattern("\\s+");
        List<String> expected = Arrays.asList("one", "two", "three", "four", "five", "six", "seven", "eight");
        List<String> result = splitter.split("one two three four five six seven eight");
        assertEquals(expected, result);
    }

    @Test
    public void testOnFixedLengthSeparator() {
        Splitter splitter = Splitter.fixedLength(3);
        List<String> expected = Arrays.asList("one", "two", "three", "four:five:six", "seven:eight:nine");
        List<String> result = splitter.split("onetwothree:four:five:six:seven:eight:nine");
        assertEquals(expected, result);
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(':').omitEmptyStrings();
        List<String> expected = Arrays.asList("one:two", "three:four:five");
        List<String> result = splitter.split("one::two::three:four:five");
        assertEquals(expected, result);
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(':').limit(3);
        List<String> expected = Arrays.asList("one:two", "three:four:five", "six:seven:eight:nine:ten:eleven:twelve:thirteen:fourteen:fifteen:sixteen: seventeen:eighteen:nineteen");
        List<String> result = splitter.split("one:two:three:four:five:six:seven:eight:nine:ten:eleven:twelve:thirteen:fourteen:fifteen:sixteen:seventeen:eighteen:nineteen");
        assertEquals(expected, result);
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(':').trimResults();
        List<String> expected = Arrays.asList("one   :two  ", "three :four :five");
        List<String> result = splitter.split("one   :two  :three :four :five");
        assertEquals(expected, result);
    }

    @Test
    public void testTrimResultsWithCustomTrimmer() {
        Splitter splitter = Splitter.on(':').trimResults(Pattern.compile("\\s+"));
        List<String> expected = Arrays.asList("one :two :three :four :five", "", "six :seven :eight :nine :ten :eleven :twelve :thirteen :fourteen :fifteen :sixteen : seventeen :eighteen :nineteen");
        List<String> result = splitter.split("one    :two    :three    :four    :five    :six    :seven    :eight    :nine    :ten    :eleven    :twelve    :thirteen    :fourteen    :fifteen    :sixteen    : seventeen    :eighteen    :nineteen");
        assertEquals(expected, result);
    }

    @Test
    public void testMapSplitter() {
        Splitter keySplitter = Splitter.on(':');
        Splitter valueSplitter = Splitter.on('-');
        MapSplitter mapSplitter = Splitter.on(':').withKeyValueSeparator(keySplitter).withKeyValueSeparator(valueSplitter);
        List<Map.Entry<String, String>> expected = Arrays.asList(Map.entry("one-one", "two"), Map.entry("three-three", "four-one"), Map.entry("five-three", "six-two"), Map.entry("seven-five", "eight"));
        List<Map.Entry<String, String>> result = mapSplitter.split("one-one:two:three-three:four-one:five-three:six-two:seven-five:eight");
        assertEquals(expected, result);
    }
}