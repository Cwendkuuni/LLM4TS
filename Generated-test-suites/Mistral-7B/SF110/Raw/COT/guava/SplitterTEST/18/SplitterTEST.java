package com.google.common.base;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(JUnit4.class)
public class SplitterTest {

    // Basic test cases for each method
    
    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on('-');
        CharSequence input = "this-is-a-test";
        List<String> expectedResult = Arrays.asList("this", "is", "a", "test");
        List<String> result = new ArrayList<>(splitter.split(input));
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testOnCharMatcherSeparator() {
        Splitter splitter = Splitter.on(CharMatcher.is('-'));
        CharSequence input = "this-is-a-test";
        List<String> expectedResult = Arrays.asList("this", "is", "a", "test");
        List<String> result = new ArrayList<>(splitter.split(input));
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on("-");
        CharSequence input = "this-is-a-test";
        List<String> expectedResult = Arrays.asList("this", "is", "a", "test");
        List<String> result = new ArrayList<>(splitter.split(input));
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.on(Pattern.compile("[-]"));
        CharSequence input = "this-is-a-test";
        List<String> expectedResult = Arrays.asList("this", "is", "a", "test");
        List<String> result = new ArrayList<>(splitter.split(input));
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        CharSequence input = "12345678";
        List<String> expectedResult = Arrays.asList("12", "34", "56", "78");
        List<String> result = new ArrayList<>(splitter.split(input));
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on('-').omitEmptyStrings();
        CharSequence input = "this-is-a-test-";
        List<String> expectedResult = Arrays.asList("this", "is", "a", "test");
        List<String> result = new ArrayList<>(splitter.split(input));
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on('-').limit(3);
        CharSequence input = "this-is-a-test-this-is-another-test";
        List<String> expectedResult = Arrays.asList("this", "is", "a", "test");
        List<String> result = new ArrayList<>(splitter.split(input));
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on('-').trimResults();
        CharSequence input = "this-is-a-test-this-is-another-test";
        List<String> expectedResult = Arrays.asList("this", "is", "a", "test", "this", "is", "another", "test");
        List<String> result = new ArrayList<>(splitter.split(input));
        List<String> trimmedResult = new ArrayList<>(splitter.splitToList(input));
        Assert.assertEquals(expectedResult, result);
        Assert.assertEquals(trimmedResult.get(0), "this");
        Assert.assertEquals(trimmedResult.get(1), "is");
        Assert.assertEquals(trimmedResult.get(2), "a");
        Assert.assertEquals(trimmedResult.get(3), "test");
    }

    @Test
    public void testTrimResultsWithCharMatcher() {
        Splitter splitter = Splitter.on('-').trimResults(CharMatcher.WHITESPACE);
        CharSequence input = " this-is-a-test-this-is-another-test ";
        List<String> expectedResult = Arrays.asList("this", "is", "a", "test", "this", "is", "another", "test");
        List<String> result = new ArrayList<>(splitter.split(input));
        List<String> trimmedResult = new ArrayList<>(splitter.splitToList(input));
        Assert.assertEquals(expectedResult, result);
        Assert.assertEquals(trimmedResult.get(0), "this");
        Assert.assertEquals(trimmedResult.get(1), "is");
        Assert.assertEquals(trimmedResult.get(2), "a");
        Assert.assertEquals(trimmedResult.get(3), "test");
    }

    @Test
    public void testMapSplitter() {
        Splitter keySplitter = Splitter.on(':');
        Splitter valueSplitter = Splitter.on('-');
        MapSplitter mapSplitter = Splitter.on(':').withKeyValueSeparator(keySplitter, valueSplitter);
        CharSequence input = "key1:value1-key2:value2-key3:value3";
        Map<String, String> expectedResult = new LinkedHashMap<>();
        expectedResult.put("key1", "value1");
        expectedResult.put("key2", "value2");
        expectedResult.put("key3", "value3");
        Map<String, String> result = mapSplitter.split(input);
        Assert.assertEquals(expectedResult, result);
    }

    // Edge cases and exception handling test cases
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() {
        Splitter splitter = Splitter.on('-');
        splitter.split(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyStringInput() {
        Splitter splitter = Splitter.on('-');
        List<String> result = new ArrayList<>(splitter.split(""));
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyPatternInput() {
        Splitter splitter = Splitter.onPattern("");
        List<String> result = new ArrayList<>(splitter.split(""));
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPatternInput() {
        Splitter splitter = Splitter.onPattern(null);
        List<String> result = new ArrayList<>(splitter.split(""));
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullStrategyInput() {
        Splitter splitter = new Splitter(null);
        List<String> result = new ArrayList<>(splitter.split(""));
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInvalidSeparatorIndex() {
        Splitter splitter = Splitter.on('-');
        CharSequence input = "this--is-a-test";
        Iterator<String> iterator = splitter.splittingIterator(input);
        while (iterator.hasNext()) {
            iterator.next();
        }
        String nextEntry = iterator.next();
        Assert.fail("Expected IndexOutOfBoundsException: " + nextEntry);
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitterWithInvalidStrategy() {
        Splitter splitter = new Splitter(new Strategy() {
            @Override
            public Iterator<String> iterator(final Splitter p0, final CharSequence p1) {
                throw new IllegalStateException("Invalid strategy implementation");
            }
        });
        List<String> result = new ArrayList<>(splitter.split(""));
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test(expected = IllegalStateException.class)
    public void testMapSplitterWithInvalidKeyValueSplitter() {
        Splitter keySplitter = new Splitter(new Strategy() {
            @Override
            public Iterator<String> iterator(final Splitter p0, final CharSequence p1) {
                throw new IllegalStateException("Invalid key splitter implementation");
            }
        });
        Splitter valueSplitter = new Splitter(new Strategy() {
            @Override
            public Iterator<String> iterator(final Splitter p0, final CharSequence p1) {
                throw new IllegalStateException("Invalid value splitter implementation");
            }
        });
        MapSplitter mapSplitter = new MapSplitter(null, valueSplitter);
        CharSequence input = "key1:value1-key2:value2-key3:value3";
        Map<String, String> result = mapSplitter.split(input);
        Assert.assertNull(result);
    }

    @Test
    public void testMapSplitterWithEmptyKey() {
        Splitter keySplitter = Splitter.on(':');
        Splitter valueSplitter = Splitter.on('-');
        MapSplitter mapSplitter = Splitter.on(':').withKeyValueSeparator(keySplitter, valueSplitter);
        CharSequence input = "key1:value1-key2:value2-key3:";
        Map<String, String> expectedResult = new LinkedHashMap<>();
        expectedResult.put("key1", "value1");
        expectedResult.put("key2", "value2");
        Map<String, String> result = mapSplitter.split(input);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testMapSplitterWithEmptyValue() {
        Splitter keySplitter = Splitter.on(':');
        Splitter valueSplitter = Splitter.on('-');
        MapSplitter mapSplitter = Splitter.on(':').withKeyValueSeparator(keySplitter, valueSplitter);
        CharSequence input = "key1:value1-key2:value2-key3:";
        Map<String, String> expectedResult = new LinkedHashMap<>();
        expectedResult.put("key1", "value1");
        expectedResult.put("key2", "value2");
        expectedResult.put("key3", "");
        Map<String, String> result = mapSplitter.split(input);
        Assert.assertEquals(expectedResult, result);
    }
}