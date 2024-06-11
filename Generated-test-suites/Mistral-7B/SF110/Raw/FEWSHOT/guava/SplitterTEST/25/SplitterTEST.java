package com.google.common.base;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.Test;

public class SplitterTest {

    @Test
    public void testSplitterOnCharacter() {
        Splitter splitter = Splitter.on('-');
        List<String> result = splitter.split("a-b-c-d-e");
        assertEquals(Arrays.asList("a", "b", "c", "d", "e"), result);
    }

    @Test
    public void testSplitterOnPattern() {
        Pattern pattern = Pattern.compile("-");
        Splitter splitter = Splitter.on(pattern);
        List<String> result = splitter.split("a-b-c-d-e");
        assertEquals(Arrays.asList("a", "b", "c", "d", "e"), result);
    }

    @Test
    public void testSplitterOnString() {
        Splitter splitter = Splitter.on("-");
        List<String> result = splitter.split("a--b--c--d--e");
        assertEquals(Arrays.asList("a", "--b", "--c", "--d", "--e"), result);
    }

    @Test
    public void testSplitterOnEmptyString() {
        Splitter splitter = Splitter.on("");
        List<String> result = splitter.split("");
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testSplitterOnNull() {
        Splitter splitter = Splitter.on('-');
        try {
            splitter.split(null);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testSplitterOnFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        List<String> result = splitter.split("abcdefghijklmnopqrstuvwxyz");
        assertEquals(Arrays.asList("abc", "def", "ghi"), result);
    }

    @Test
    public void testSplitterOnEmptyStrings() {
        Splitter splitter = Splitter.on("-");
        List<String> result = splitter.split("---");
        assertEquals(Arrays.asList("", "", ""), result);
    }

    @Test
    public void testSplitterOmittingEmptyStrings() {
        Splitter splitter = Splitter.on("-").omitEmptyStrings();
        List<String> result = splitter.split("--");
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testSplitterTrimmingResults() {
        Splitter splitter = Splitter.on("-").trimResults();
        List<String> result = splitter.split("---a--b---c--d---");
        assertEquals(Arrays.asList("a", "b", "c", "d"), result);
    }

    @Test
    public void testSplitterTrimmingResultsWithCharMatcher() {
        Splitter splitter = Splitter.on("-").trimResults(CharMatcher.whitespace());
        List<String> result = splitter.split("--   a  --b--  c--  d--");
        assertEquals(Arrays.asList("a", "b", "c", "d"), result);
    }

    @Test
    public void testMapSplitter() {
        Splitter keySplitter = Splitter.on(':');
        Splitter valueSplitter = Splitter.on('_');
        MapSplitter mapSplitter = Splitter.on(':').withKeyValueSeparator(keySplitter, valueSplitter);
        String input = "key1:value1_key2:value2_key3:value3";
        Map<String, String> result = mapSplitter.split(input);
        assertEquals(new HashSet<>(Arrays.asList("key1", "key2", "key3")), new HashSet<>(result.keySet()));
        assertEquals(new HashSet<>(Arrays.asList("value1", "value2", "value3")), new HashSet<>(result.values()));
    }

    @Test
    public void testMapSplitterInvalidEntry() {
        Splitter keySplitter = Splitter.on(':');
        Splitter valueSplitter = Splitter.on('_');
        MapSplitter mapSplitter = Splitter.on(':').withKeyValueSeparator(keySplitter, valueSplitter);
        String input = "key1:value1_key2:value2:invalidEntry";
        try {
            mapSplitter.split(input);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Chunk [invalidEntry] is not a valid entry", e.getMessage());
        }
    }
}