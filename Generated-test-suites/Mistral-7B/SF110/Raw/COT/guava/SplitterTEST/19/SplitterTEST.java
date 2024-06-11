package com.example.junit.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strategies;

public class SplitterTest {

    @Test
    public void testSplitterConstruction() {
        Splitter splitter = Splitter.on('-');
        assertNotNull(splitter);
    }

    @Test
    public void testSplitString() {
        Splitter splitter = Splitter.on('-');
        String input = "apple-banana-orange";
        List<String> expectedOutput = Arrays.asList("apple", "banana", "orange");
        List<String> output = new ArrayList<String>();
        for (String s : splitter.split(input)) {
            output.add(s);
        }
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testSplitEmptyString() {
        Splitter splitter = Splitter.on('-');
        List<String> output = new ArrayList<String>();
        for (String s : splitter.split("")) {
            output.add(s);
        }
        assertTrue(output.isEmpty());
    }

    @Test
    public void testSplitNullString() {
        Splitter splitter = Splitter.on('-');
        assertThrows(NullPointerException.class, () -> splitter.split(null));
    }

    @Test
    public void testSplitWithLimit() {
        Splitter splitter = Splitter.on('-').limit(2);
        String input = "apple-banana-orange-grape";
        List<String> expectedOutput = Arrays.asList("apple", "banana");
        List<String> output = new ArrayList<String>();
        for (String s : splitter.split(input)) {
            output.add(s);
        }
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testSplitWithOmmitEmptyStrings() {
        Splitter splitter = Splitter.on('-').omitEmptyStrings();
        String input = "apple-banana-orange-";
        List<String> expectedOutput = Arrays.asList("apple", "banana", "orange");
        List<String> output = new ArrayList<String>();
        for (String s : splitter.split(input)) {
            output.add(s);
        }
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testSplitWithTrimResults() {
        Splitter splitter = Splitter.on('-').trimResults();
        String input = "  apple-banana-orange  ";
        List<String> expectedOutput = Arrays.asList("apple", "banana", "orange");
        List<String> output = new ArrayList<String>();
        for (String s : splitter.split(input)) {
            output.add(s);
        }
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testSplitWithCustomSeparator() {
        Splitter splitter = Splitter.onPattern("\\|");
        String input = "apple|banana|orange";
        List<String> expectedOutput = Arrays.asList("apple", "banana", "orange");
        List<String> output = new ArrayList<String>();
        for (String s : splitter.split(input)) {
            output.add(s);
        }
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testSplitWithCustomSeparatorAndLimit() {
        Splitter splitter = Splitter.onPattern("\\|").limit(2);
        String input = "apple|banana|orange|grape";
        List<String> expectedOutput = Arrays.asList("apple", "banana");
        List<String> output = new ArrayList<String>();
        for (String s : splitter.split(input)) {
            output.add(s);
        }
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testSplitWithCustomSeparatorAndTrimResults() {
        Splitter splitter = Splitter.onPattern("\\|").trimResults();
        String input = "  apple|banana|orange  ";
        List<String> expectedOutput = Arrays.asList("apple", "banana", "orange");
        List<String> output = new ArrayList<String>();
        for (String s : splitter.split(input)) {
            output.add(s);
        }
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testSplitWithCustomSeparatorAndOmmitEmptyStrings() {
        Splitter splitter = Splitter.onPattern("\\|").omitEmptyStrings();
        String input = "apple||banana|orange|";
        List<String> expectedOutput = Arrays.asList("apple", "banana", "orange");
        List<String> output = new ArrayList<String>();
        for (String s : splitter.split(input)) {
            output.add(s);
        }
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testMapSplitter() {
        Splitter keySplitter = Splitter.on(":");
        Splitter valueSplitter = Splitter.on("=");
        Map<String, String> input = new LinkedHashMap<String, String>() {{
            put("key1:value1=", "value1");
            put("key2:value2=value2", "value2");
            put("key3:value3", "value3");
            put("key4:value4=", "");
            put("", "");
        }};
        Map<String, String> expectedOutput = new LinkedHashMap<String, String>() {{
            put("key1", "value1");
            put("key2", "value2");
            put("key3", "value3");
        }};
        Map<String, String> output = new LinkedHashMap<String, String>();
        for (Map.Entry<String, String> entry : new Splitter.MapSplitter(keySplitter, valueSplitter).split(input.toString())) {
            output.put(entry.getKey(), entry.getValue());
        }
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testMapSplitterNullInput() {
        Splitter keySplitter = Splitter.on(":");
        Splitter valueSplitter = Splitter.on("=");
        assertThrows(NullPointerException.class, () -> new Splitter.MapSplitter(keySplitter, valueSplitter).split(null));
    }

}