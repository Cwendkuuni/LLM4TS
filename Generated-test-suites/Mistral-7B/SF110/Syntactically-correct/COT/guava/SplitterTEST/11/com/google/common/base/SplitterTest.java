package com.example.junit4;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.MapSplitter;
import com.google.common.base.Splitter;

public class SplitterTest {

    private CharSequence charSequence;
    private Splitter splitter;
    private Splitter patternSplitter;
    private Splitter fixedLengthSplitter;

    @Before
    public void setUp() {
        charSequence = "the quick brown fox jumps over the lazy dog";
        splitter = Splitter.on(" ").trimResults();
        patternSplitter = Splitter.onPattern("[ ]+");
        fixedLengthSplitter = Splitter.fixedLength(2);
    }

    @Test
    public void testOnChar() {
        List<String> result = new ArrayList<>(Arrays.asList(splitter.split(charSequence)));
        List<String> expected = Arrays.asList("the", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog");
        assertEquals(expected, result);
    }

    @Test
    public void testOnCharMatcher() {
        List<String> result = new ArrayList<>(Arrays.asList(splitter.split(charSequence)));
        List<String> expected = Arrays.asList("the", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog");
        assertEquals(expected, result);
    }

    @Test
    public void testOnPattern() {
        List<String> result = new ArrayList<>(Arrays.asList(patternSplitter.split(charSequence)));
        List<String> expected = Arrays.asList("the", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog");
        assertEquals(expected, result);
    }

    @Test
    public void testOnPatternWithEmptyString() {
        List<String> result = new ArrayList<>(Arrays.asList(patternSplitter.split(CharSequences.emptyString())));
        List<String> expected = Collections.emptyList();
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnPatternWithEmptyPattern() {
        splitter.onPattern("");
    }

    @Test
    public void testOnPatternWithInvalidPattern() {
        List<String> result = new ArrayList<>(Arrays.asList(patternSplitter.split(CharSequences.of("abc", "[ab]c"))));
        List<String> expected = Arrays.asList("abc", "[ab]c");
        assertEquals(expected, result);
    }

    @Test
    public void testFixedLength() {
        List<String> result = new ArrayList<>(Arrays.asList(fixedLengthSplitter.split(charSequence)));
        List<String> expected = Arrays.asList("th", "he", "qu", "ck", "br", "ow", "n", "fox", "jump", "so", "ver", "the", "laz", "y", "d");
        assertEquals(expected, result);
    }

    @Test
    public void testOmitEmptyStrings() {
        List<String> result = new ArrayList<>(Arrays.asList(splitter.omitEmptyStrings().split(charSequence)));
        List<String> expected = Arrays.asList("quick", "brown", "fox", "jumps", "lazy", "dog");
        assertEquals(expected, result);
    }

    @Test
    public void testLimit() {
        List<String> result = new ArrayList<>(Arrays.asList(splitter.limit(3).split(charSequence)));
        List<String> expected = Arrays.asList("the", "quick", "brown");
        assertEquals(expected, result);
    }

    @Test
    public void testTrimResults() {
        List<String> result = new ArrayList<>(Arrays.asList(splitter.trimResults().split(charSequence)));
        List<String> expected = Arrays.asList("the", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog");
        List<String> trimmedResult = new ArrayList<>(Arrays.asList(splitter.trimResults(CharMatcher.WHITESPACE).split(charSequence)));
        List<String> trimmedExpected = Arrays.asList("the", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog");
        assertEquals(expected, result);
        assertEquals(trimmedExpected, trimmedResult);
    }

    @Test
    public void testSplitToList() {
        List<String> result = splitter.splitToList(charSequence);
        List<String> expected = Arrays.asList("the", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog");
        assertEquals(expected, result);
    }

    @Test
    public void testMapSplitter() {
        Map<String, String> result = new LinkedHashMap<>();
        result.putAll(MapSplitter.on(":").withKeyValueSeparator(Splitter.on("=")).split(CharSequences.of("key1:value1=one,key2:value2=two,key3:value3=three")));
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2=two");
        expected.put("key3", "value3=three");
        assertEquals(expected, result);
    }
}