package com.google.common.base;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

public class SplitterTest {

    private Splitter splitter;

    @Before
    public void setup() {
        splitter = Splitter.on(' ');
    }

    @Test
    public void testSplitterCreation() {
        assertNotNull(splitter);
    }

    @Test
    public void testEmptySequence() {
        List<String> result = splitter.split(Character.toString(0));
        assertEmpty(result);
    }

    @Test
    public void testSplitWithSingleSeparator() {
        List<String> result = splitter.split("hello world");
        assertEquals(Arrays.asList("hello", "world"), result);
    }

    @Test
    public void testSplitWithMultipleSeparators() {
        List<String> result = splitter.split("hello world:12345 hello world:67890");
        assertEquals(Arrays.asList("hello", "world:12345", "hello", "world:67890"), result);
    }

    @Test
    public void testSplitWithEmptyStrings() {
        List<String> result = splitter.split("hello , , world");
        assertEquals(Arrays.asList("hello", "", "", "world"), result);
    }

    @Test
    public void testSplitWithOmittedEmptyStrings() {
        List<String> result = splitter.omitEmptyStrings().split("hello , , world");
        assertEquals(Arrays.asList("hello", "world"), result);
    }

    @Test
    public void testSplitWithMaxLimit() {
        List<String> result = splitter.limit(1).split("repeat repeat repeat repeat repeat repeat");
        assertEquals(Arrays.asList("repeat"), result);
    }

    @Test
    public void testSplitWithTrimming() {
        List<String> result = splitter.trimResults().split("  hello world  ");
        assertEquals(Arrays.asList("hello", "world"), result);
    }

    @Test
    public void testSplitWithCustomCharMatcher() {
        List<String> result = Splitter.on(Pattern.compile("[\\s,]+")).split("hello , world,  123, 456");
        assertEquals(Arrays.asList("hello", "", "", "world", "", "", "123", "", "", "456"), result);
    }

    @Test
    public void testSplitWithCustomSeparatorPattern() {
        List<String> result = Splitter.onPattern("[\\s,]+").split("hello , world,  123, 456");
        assertEquals(Arrays.asList("hello", "", "", "world", "", "", "123", "", "", "456"), result);
    }

    @Test
    public void testSplitWithCustomSeparator() {
        List<String> result = splitter.on(',').split("hello,world,123,456");
        assertEquals(Arrays.asList("hello", "world", "123", "456"), result);
    }

    @Test
    public void testSplitWithCustomSeparatorAndOmittedEmptyStrings() {
        List<String> result = splitter.on(',').omitEmptyStrings().split("hello,world,,123,456");
        assertEquals(Arrays.asList("hello", "world", "123", "456"), result);
    }

    @Test
    public void testSplitWithCustomSeparatorAndLimit() {
        List<String> result = splitter.on(',').limit(2).split("hello,world,,123,456");
        assertEquals(Arrays.asList("hello", "world"), result);
    }

    @Test
    public void testSplitWithCustomSeparatorAndTrimming() {
        List<String> result = splitter.on(',').trimResults().split("hello,world,,123,456");
        assertEquals(Arrays.asList("hello", "world", "123", "456"), result);
    }

    @Test
    public void testMapSplitterWithSingleKeyValue() {
        Map<String, String> result = splitter.withKeyValueSeparator('=').split("key=value");
        assertEquals(Collections.singletonMap("key", "value"), result);
    }

    @Test
    public void testMapSplitterWithMultipleKeyValue() {
        Map<String, String> result = splitter.withKeyValueSeparator('=').split("key1=value1&key2=value2");
        assertEquals(Arrays.asList(Map.entry("key1", "value1"), Map.entry("key2", "value2")).iterator().next(),
                result.entrySet().iterator().next());
    }

    @Test
    public void testMapSplitterWithEmptyValues() {
        Map<String, String> result = splitter.withKeyValueSeparator('=').split("key1=&key2=");
        assertEquals(Collections.singletonMap("key1", ""), result);
    }

    @Test
    public void testMapSplitterWithMultipleKeyValueAndEmptyKeys() {
        Map<String, String> result = splitter.withKeyValueSeparator('=').split("key1=value1&key2=");
        assertEquals(Collections.singletonMap("key1", "value1"), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapSplitterWithInvalidEntry() {
        String invalidEntry = "invalidEntry";
        Map<String, String> result = splitter.withKeyValueSeparator('=').split(invalidEntry);
        fail("MapSplitter should throw IllegalArgumentException for invalid entries: " + invalidEntry);
    }
}