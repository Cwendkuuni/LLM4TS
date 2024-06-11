package com.example.junit4;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.base.MapSplitter;
import com.google.common.base.Splitter;

public class SplitterTest {

    private Splitter separatorMatcherSplitter;
    private Splitter separatorStringSplitter;
    private Splitter separatorPatternSplitter;
    private Splitter fixedLengthSplitter;
    private Splitter omitEmptyStringsSplitter;
    private Splitter limitSplitter;
    private Splitter trimResultsSplitter;
    private Splitter trimResultsWithSeparatorSplitter;

    @Before
    public void setUp() {
        separatorMatcherSplitter = Splitter.on(CharMatcher.is('-'));
        separatorStringSplitter = Splitter.on("--");
        separatorPatternSplitter = Splitter.onPattern("--");
        fixedLengthSplitter = Splitter.fixedLength(3);
        omitEmptyStringsSplitter = Splitter.on("--").omitEmptyStrings();
        limitSplitter = Splitter.on("--").limit(2);
        trimResultsSplitter = Splitter.on("--").trimResults();
        trimResultsWithSeparatorSplitter = Splitter.onPattern("--").trimResults();
    }

    @Test
    public void testOnCharMatcher() {
        List<String> result = new ArrayList<>(Arrays.asList("abc", "def", "ghi"));
        assertEquals(result, separatorMatcherSplitter.split("abc-def-ghi"));
    }

    @Test
    public void testOnCharSequence() {
        List<String> result = new ArrayList<>(Arrays.asList("abc", "def", "ghi"));
        assertEquals(result, separatorStringSplitter.split("abc--def--ghi"));
    }

    @Test
    public void testOnPattern() {
        List<String> result = new ArrayList<>(Arrays.asList("abc", "def", "ghi"));
        assertEquals(result, separatorPatternSplitter.split("abc--def--ghi"));
    }

    @Test
    public void testFixedLength() {
        List<String> result = new ArrayList<>(Arrays.asList("abc", "def", "ghi"));
        assertEquals(result, fixedLengthSplitter.split("abcdefghi"));
    }

    @Test
    public void testOmitEmptyStrings() {
        List<String> result = new ArrayList<>(Arrays.asList("abc", "def"));
        assertEquals(result, omitEmptyStringsSplitter.split("abc--def"));
    }

    @Test
    public void testLimit() {
        List<String> result = new ArrayList<>(Arrays.asList("abc", "def"));
        assertEquals(result, limitSplitter.split("abc--def--ghi--jkl"));
    }

    @Test
    public void testTrimResults() {
        List<String> result = new ArrayList<>(Arrays.asList("abc", "def", "ghi"));
        assertEquals(result, trimResultsSplitter.split("abc def ghi"));
    }

    @Test
    public void testTrimResultsWithSeparator() {
        List<String> result = new ArrayList<>(Arrays.asList("abc", "def", "ghi"));
        assertEquals(result, trimResultsWithSeparatorSplitter.split("abc--def--ghi"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullCharSequence() {
        try {
            separatorMatcherSplitter.split(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testEmptyCharSequence() {
        List<String> result = new ArrayList<>();
        assertEquals(result, separatorMatcherSplitter.split(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptySeparator() {
        try {
            Splitter separatorEmptySplitter = Splitter.on("");
            separatorEmptySplitter.split("abc");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEmptySeparatorPattern() {
        List<String> result = new ArrayList<>();
        assertEquals(result, separatorPatternSplitter.split("abc"));
    }

    @Test
    public void testEmptySeparatorMatcher() {
        List<String> result = new ArrayList<>();
        assertEquals(result, separatorMatcherSplitter.split("abc"));
    }

    @Test
    public void testInvalidPattern() {
        try {
            Splitter separatorInvalidPatternSplitter = Splitter.onPattern("[");
            separatorInvalidPatternSplitter.split("abc");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSplitWithTrailingSeparator() {
        List<String> result = new ArrayList<>(Arrays.asList("abc", "def", "ghi", ""));
        assertEquals(result, separatorMatcherSplitter.split("abc--def--ghi--"));
    }
}