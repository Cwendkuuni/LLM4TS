package com.example.junit;

import com.google.common.base.Splitter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class SplitterTest {

    private Splitter stringSeparator;
    private Splitter charSeparator;
    private Splitter regexSeparator;
    private Splitter fixedLengthSeparator;
    private Splitter patternSeparator;
    private Splitter omittedEmptyStrings;
    private Splitter limitedSeparator;
    private Splitter trimmedResults;
    private Splitter omitEmptyStringsAndTrimmedResults;

    @Before
    public void setup() {
        stringSeparator = Splitter.on(":");
        charSeparator = Splitter.on(';');
        regexSeparator = Splitter.onPattern("\\s+");
        fixedLengthSeparator = Splitter.fixedLength(2);
        patternSeparator = Splitter.onPattern("\\|");
        omittedEmptyStrings = stringSeparator.omitEmptyStrings();
        limitedSeparator = stringSeparator.limit(3);
        trimmedResults = stringSeparator.trimResults();
        omitEmptyStringsAndTrimmedResults = omittedEmptyStrings.trimResults();
    }

    @Test
    public void testStringSeparator() {
        List<String> list = stringSeparator.split("key1:value1:key2:value2");
        assertEquals(Arrays.asList("key1", "value1", "key2", "value2"), list);
    }

    @Test
    public void testCharSeparator() {
        List<String> list = charSeparator.split("a;b;c;d");
        assertEquals(Arrays.asList("a", ";", "b", ";", "c", ";", "d"), list);
    }

    @Test
    public void testRegexSeparator() {
        List<String> list = regexSeparator.split("one two three four five");
        assertEquals(Arrays.asList("one", "two", "three", "four", "five"), list);
    }

    @Test
    public void testFixedLengthSeparator() {
        List<String> list = fixedLengthSeparator.split("abcdefghijk");
        assertEquals(Arrays.asList("abc", "def", "ghi", "jki"), list);
    }

    @Test
    public void testPatternSeparator() {
        List<String> list = patternSeparator.split("foo|bar|baz|qux");
        assertEquals(Arrays.asList("foo", "bar", "baz", "qux"), list);
    }

    @Test
    public void testOmitEmptyStrings() {
        List<String> list = omittedEmptyStrings.split("key1:value1::key2:value2:");
        assertEquals(Arrays.asList("key1:value1", "key2:value2"), list);
    }

    @Test
    public void testLimitSeparator() {
        List<String> list = limitedSeparator.split("key1:value1:key2:value2:key3:value3:key4:value4:key5:value5:key6:value6:");
        assertEquals(Arrays.asList("key1:value1", "key2:value2", "key3:value3"), list);
    }

    @Test
    public void testTrimmedResults() {
        List<String> list = trimmedResults.split(" key1:value1  ");
        assertEquals(Arrays.asList("key1:value1"), list);
    }

    @Test
    public void testOmitEmptyStringsAndTrimmedResults() {
        List<String> list = omitEmptyStringsAndTrimmedResults.split("key1:value1:         :key2:value2:");
        assertEquals(Arrays.asList("key1:value1", "key2:value2"), list);
    }
}