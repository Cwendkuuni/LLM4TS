package com.google.common.base;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

public class SplitterTest {

    

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on('-');
        CharSequence sequence = "this-is-a-test-string-with-hyphens";
        List<String> result = new ArrayList<String>(Arrays.asList(splitter.split(sequence)));
        List<String> expected = Arrays.asList("this", "is", "a", "test", "string", "with", "hyphens");
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testOnNullSeparator() {
        Splitter.on(null);
    }

    @Test
    public void testOnEmptyStringSeparator() {
        Splitter splitter = Splitter.on("");
        CharSequence sequence = "test";
        List<String> result = new ArrayList<String>(Arrays.asList(splitter.split(sequence)));
        List<String> expected = Arrays.asList("test");
        assertEquals(expected, result);
    }

    @Test
    public void testOnPatternSeparator() {
        Pattern pattern = Pattern.compile("\\s+");
        Splitter splitter = Splitter.onPattern(pattern);
        CharSequence sequence = "this is a test string with spaces";
        List<String> result = new ArrayList<String>(Arrays.asList(splitter.split(sequence)));
        List<String> expected = Arrays.asList("this", "is", "a", "test", "string", "with", "spaces");
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testOnNullPatternSeparator() {
        Splitter.onPattern(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnEmptyPatternSeparator() {
        Splitter.onPattern("");
    }

    @Test
    public void testOnInvalidPatternSeparator() {
        Pattern pattern = Pattern.compile(".*");
        Splitter splitter = Splitter.onPattern(pattern);
        CharSequence sequence = "this-is-a-test-string-with-hyphens";
        List<String> result = new ArrayList<String>(Arrays.asList(splitter.split(sequence)));
        List<String> expected = Arrays.asList("this-is-a-test-string-with-hyphens");
        assertEquals(expected, result);
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on('-').omitEmptyStrings();
        CharSequence sequence = "this-is-a-test-string-with-hyphens";
        List<String> result = new ArrayList<String>(Arrays.asList(splitter.split(sequence)));
        List<String> expected = Arrays.asList("this", "is", "a", "test", "string", "with", "hyphens");
        List<String> expectedFiltered = Arrays.asList("this", "is", "a", "test", "string", "with");
        assertEquals(expectedFiltered, result);
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on('-').limit(3);
        CharSequence sequence = "this-is-a-test-string-with-hyphens";
        List<String> result = new ArrayList<String>(Arrays.asList(splitter.split(sequence)));
        List<String> expected = Arrays.asList("this", "is", "a");
        assertEquals(expected, result);
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        CharSequence sequence = "1234567890";
        List<String> result = new ArrayList<String>(Arrays.asList(splitter.split(sequence)));
        List<String> expected = Arrays.asList("12", "34", "56", "78", "90");
        assertEquals(expected, result);
    }

    @Test
    public void testWithKeyValueSeparator() {
        Splitter keyValueSplitter = Splitter.on(":");
        Splitter outerSplitter = Splitter.on("-");
        MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator(keyValueSplitter);
        CharSequence sequence = "key1:value1-key2:value2-key3:value3";
        Map<String, String> result = mapSplitter.split(sequence);
        Map<String, String> expected = new LinkedHashMap<String, String>() {{
            put("key1", "value1");
            put("key2", "value2");
            put("key3", "value3");
        }};
        assertEquals(expected, result);
    }

    @Test
    public void testWithInvalidKeyValueSeparator() {
        Splitter keyValueSplitter = Splitter.on(":");
        Splitter outerSplitter = Splitter.on("-");
        MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator(keyValueSplitter);
        CharSequence sequence = "key1:value1-key2:value2-key3:value3:anotherKey:anotherValue";
        Map<String, String> result = mapSplitter.split(sequence);
        assertTrue(result.isEmpty());
    }

    
}