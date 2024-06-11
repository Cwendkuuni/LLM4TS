package com.example.junit; // replace with your package name

import static org.junit.Assert.*;
import static com.google.common.base.CharMatcher.*;
import static com.google.common.base.Joiner.*;
import static com.google.common.base.Splitter.*;

import com.google.common.base.*;
import org.junit.*;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = on('-');
        CharSequence input = "one-two-three-four-five";
        Iterable<String> result = splitter.split(input);
        List<String> expectedResult = asList("one", "two", "three", "four", "five");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testOnCharMatcherSeparator() {
        Splitter splitter = on(anyOf('a', 'e'));
        CharSequence input = "apple apple banana bread";
        Iterable<String> result = splitter.split(input);
        List<String> expectedResult = asList("apple", "apple", "banana", "bread");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = on(",");
        CharSequence input = "apple,banana,orange";
        Iterable<String> result = splitter.split(input);
        List<String> expectedResult = asList("apple", "banana", "orange");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = onPattern("\\s+");
        CharSequence input = "apple orange apple banana";
        Iterable<String> result = splitter.split(input);
        List<String> expectedResult = asList("apple", "orange", "apple", "banana");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testOnFixedLengthSeparator() {
        Splitter splitter = fixedLength(3);
        CharSequence input = "abcdefghijkl";
        Iterable<String> result = splitter.split(input);
        List<String> expectedResult = asList("abc", "def", "ghi", "ijk", "l");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = on("-").omitEmptyStrings();
        CharSequence input = "one---two";
        Iterable<String> result = splitter.split(input);
        List<String> expectedResult = asList("one", "two");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testLimit() {
        Splitter splitter = on(",").limit(3);
        CharSequence input = "apple,banana,orange,grape,kiwi";
        Iterable<String> result = splitter.split(input);
        List<String> expectedResult = asList("apple", "banana", "orange");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = on("-").trimResults();
        CharSequence input = " one-two-three ";
        Iterable<String> result = splitter.split(input);
        List<String> expectedResult = asList("one", "two", "three");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testTrimResultsWithCharMatcher() {
        Splitter splitter = on("-").trimResults(WHITESPACE);
        CharSequence input = " one-two-three ";
        Iterable<String> result = splitter.split(input);
        List<String> expectedResult = asList("one", "two", "three");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testMapSplitter() {
        Splitter keySplitter = on(":");
        Splitter valueSplitter = on(",");
        MapSplitter mapSplitter = withKeyValueSeparator(keySplitter).withKeyValueSeparator(valueSplitter);
        CharSequence input = "key1:value1,value2,value3:key2:value4,value5:key3:value6";
        Map<String, List<String>> expectedResult = new LinkedHashMap<>();
        expectedResult.put("key1", asList("value1"));
        expectedResult.put("key2", asList("value4", "value5"));
        expectedResult.put("key3", asList("value6"));

        Map<String, List<String>> result = mapSplitter.split(input);
        assertEquals(expectedResult, result);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        Splitter splitter = on("-");
        splitter.split(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptySeparatorPattern() {
        Splitter splitter = onPattern("");
        CharSequence input = "test";
        try {
            splitter.split(input);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Expected IllegalArgumentException, but was: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyInput() {
        Splitter splitter = on("-");
        CharSequence input = "";
        try {
            splitter.split(input);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Expected IllegalArgumentException, but was: " + e.getMessage());
        }
    }
}