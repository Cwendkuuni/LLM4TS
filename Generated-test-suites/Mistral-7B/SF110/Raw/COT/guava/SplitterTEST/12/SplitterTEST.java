package com.google.common.base;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

public class SplitterTest {

    private Splitter charSeparatorSplitter;
    private Splitter stringSeparatorSplitter;
    private Splitter patternSeparatorSplitter;
    private Splitter fixedLengthSplitter;

    @Before
    public void setUp() {
        charSeparatorSplitter = Splitter.on('-');
        stringSeparatorSplitter = Splitter.on("--");
        patternSeparatorSplitter = Splitter.onPattern("--");
        fixedLengthSplitter = Splitter.fixedLength(2);
    }

    @Test
    public void testCharSeparatorSplitter() {
        List<String> result = new ArrayList<>(Arrays.asList("one", "two", "three"));
        CharSequence input = "one-two-three";

        List<String> expected = charSeparatorSplitter.split(input);
        assertEquals(result, expected);
    }

    @Test
    public void testStringSeparatorSplitter() {
        List<String> result = new ArrayList<>(Arrays.asList("one--two", "three"));
        CharSequence input = "one--two--three";

        List<String> expected = stringSeparatorSplitter.split(input);
        assertEquals(result, expected);
    }

    @Test
    public void testPatternSeparatorSplitter() {
        List<String> result = new ArrayList<>(Arrays.asList("one--two", "three"));
        CharSequence input = "one--two--three";

        Pattern pattern = Pattern.compile("--");
        List<String> expected = patternSeparatorSplitter.split(input);
        Matcher matcher = pattern.matcher(expected);
        assertTrue(matcher.matches());
        assertEquals(result, expected);
    }

    @Test
    public void testFixedLengthSplitter() {
        List<String> result = new ArrayList<>(Arrays.asList("ab", "cd"));
        CharSequence input = "abcd";

        List<String> expected = fixedLengthSplitter.split(input);
        assertEquals(result, expected);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInputCharSequence() {
        Splitter charSeparatorSplitter = Splitter.on('-');
        charSeparatorSplitter.split(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptySeparatorString() {
        Splitter stringSeparatorSplitter = Splitter.on("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyPattern() {
        Splitter patternSeparatorSplitter = Splitter.onPattern("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPattern() {
        Splitter patternSeparatorSplitter = Splitter.onPattern(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeLimit() {
        Splitter limitSplitter = Splitter.limit(-1);
        CharSequence input = "abcdef";
        List<String> result = limitSplitter.split(input);
        List<String> expected = Arrays.asList("abcdef");
        assertEquals(expected, result);
    }
}