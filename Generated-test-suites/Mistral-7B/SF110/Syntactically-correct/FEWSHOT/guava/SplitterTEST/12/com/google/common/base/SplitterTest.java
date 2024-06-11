package com.google.common.base;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.Test;

public class SplitterTest {

    @Test
    public void testNewInstance() {
        Splitter splitter = Splitter.on('-');
        assertEquals(Splitter.Strategy.class, splitter.strategy.getClass());
    }

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on('-');
        List<String> result = splitter.split("foo--bar--baz");
        assertEquals(Arrays.asList("foo", "", "bar", "", "baz"), result);
    }

    @Test
    public void testOnCharWithTrimming() {
        Splitter splitter = Splitter.on('-').trimResults();
        List<String> result = splitter.split("foo--bar--baz");
        assertEquals(Arrays.asList("foo", "", "bar", "", "baz"), result);
        assertTrue(Arrays.asList("", "", "", "", "").equals(splitter.split("-- foo --bar --baz --")));
    }

    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.onPattern("\\s+");
        List<String> result = splitter.split("foo bar baz");
        assertEquals(Arrays.asList("foo", "bar", "baz"), result);
    }

    @Test
    public void testOnEmptyString() {
        Splitter splitter = Splitter.on("");
        List<String> result = splitter.split("");
        assertFalse(result.isEmpty());
        assertEquals(Arrays.asList(""), result);
    }

    @Test
    public void testOnPatternWithLimit() {
        Splitter splitter = Splitter.onPattern("\\s+").limit(2);
        List<String> result = splitter.split("foo bar baz");
        assertEquals(Arrays.asList("foo", "bar"), result);
    }

    @Test
    public void testOnPatternWithOmittingEmptyStrings() {
        Splitter splitter = Splitter.onPattern("\\s+").omitEmptyStrings();
        List<String> result = splitter.split("foo bar baz");
        assertEquals(Arrays.asList("foo", "bar", "baz"), result);
        List<String> result2 = splitter.split("foo bar   baz");
        assertEquals(Arrays.asList("foo", "bar", "baz"), result2);
        List<String> result3 = splitter.split("foo");
        assertNull(result3);
    }

    @Test
    public void testOnPatternWithTrimming() {
        Splitter splitter = Splitter.onPattern("\\s+").trimResults();
        List<String> result = splitter.split("foo bar baz");
        assertEquals(Arrays.asList("foo", "bar", "baz"), result);
        List<String> result2 = splitter.split("foo bar   baz");
        assertEquals(Arrays.asList("foo", "bar", "baz"), result2);
        List<String> result3 = splitter.split("foo");
        assertNull(result3);
    }

    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on("--");
        List<String> result = splitter.split("foo--bar--baz");
        assertEquals(Arrays.asList("foo", "bar", "baz"), result);
    }

    @Test
    public void testOnStringWithLimit() {
        Splitter splitter = Splitter.on("--").limit(2);
        List<String> result = splitter.split("foo--bar--baz");
        assertEquals(Arrays.asList("foo", "bar"), result);
    }

    @Test
    public void testOnStringWithTrimming() {
        Splitter splitter = Splitter.on("--").trimResults();
        List<String> result = splitter.split("foo--bar--baz");
        assertEquals(Arrays.asList("foo", "bar", "baz"), result);
    }

    @Test
    public void testOnStringWithOmittingEmptyStrings() {
        Splitter splitter = Splitter.on("--").omitEmptyStrings();
        List<String> result = splitter.split("foo--bar--baz");
        assertEquals(Arrays.asList("foo", "bar", "baz"), result);
        List<String> result2 = splitter.split("foo--");
        assertEquals(Arrays.asList("foo"), result2);
        List<String> result3 = splitter.split("foo");
        assertNull(result3);
    }

    @Test
    public void testOnStringWithTrimmingAndOmittingEmptyStrings() {
        Splitter splitter = Splitter.on("--").trimResults().omitEmptyStrings();
        List<String> result = splitter.split("foo--bar--baz");
        assertEquals(Arrays.asList("foo", "bar", "baz"), result);
        List<String> result2 = splitter.split("foo--");
        assertEquals(Arrays.asList("foo"), result2);
        List<String> result3 = splitter.split("foo");
        assertNull(result3);
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        List<String> result = splitter.split("foo012bar034baz056");
        assertEquals(Arrays.asList("foo012", "bar034", "baz056"), result);
    }

    @Test
    public void testFixedLengthWithTrimming() {
        Splitter splitter = Splitter.fixedLength(3).trimResults();
        List<String> result = splitter.split("foo012bar034baz056");
        assertEquals(Arrays.asList("foo012", "bar034", "baz056"), result);
    }

    @Test
    public void testFixedLengthWithLimit() {
        Splitter splitter = Splitter.fixedLength(3).limit(2);
        List<String> result = splitter.split("foo012bar034baz056");
        assertEquals(Arrays.asList("foo012", "bar034"), result);
    }

    @Test
    public void testFixedLengthWithTrimmingAndLimit() {
        Splitter splitter = Splitter.fixedLength(3).trimResults().limit(2);
        List<String> result = splitter.split("foo012bar034baz056");
        assertEquals(Arrays.asList("foo012", "bar034"), result);
    }

    @Test
    public void testMapSplitter() {
        Splitter outerSplitter = Splitter.on(':');
        Splitter innerSplitter = Splitter.on('=');
        MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator(innerSplitter);
        String testString = "foo:bar=value1:bar=value2:baz:qux=value3";
        Map<String, String> result = mapSplitter.split(testString);
        assertEquals(Collections.unmodifiableMap(Arrays.asMap("foo", "bar=value1", "baz", "qux=value3")), result);
    }

    @Test
    public void testMapSplitterWithLimit() {
        Splitter outerSplitter = Splitter.on(':').limit(2);
        Splitter innerSplitter = Splitter.on('=');
        MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator(innerSplitter);
        String testString = "foo:bar=value1:bar=value2:baz:qux=value3";
        Map<String, String> result = mapSplitter.split(testString);
        assertEquals(Collections.unmodifiableMap(Arrays.asMap("foo", "bar=value1")), result);
    }

    @Test
    public void testMapSplitterWithTrimming() {
        Splitter outerSplitter = Splitter.on(':').trimResults();
        Splitter innerSplitter = Splitter.on('=').trimResults();
        MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator(innerSplitter);
        String testString = "foo:bar=value1:bar=value2:baz:qux=value3";
        Map<String, String> result = mapSplitter.split(testString);
        assertEquals(Collections.unmodifiableMap(Arrays.asMap("foo", "bar=value1")), result);
    }

    @Test
    public void testMapSplitterWithTrimmingAndLimit() {
        Splitter outerSplitter = Splitter.on(':').trimResults().limit(2);
        Splitter innerSplitter = Splitter.on('=').trimResults();
        MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator(innerSplitter);
        String testString = "foo:bar=value1:bar=value2:baz:qux=value3";
        Map<String, String> result = mapSplitter.split(testString);
        assertEquals(Collections.unmodifiableMap(Arrays.asMap("foo", "bar=value1")), result);
    }

    @Test
    public void testMapSplitterWithTrimmingAndOmitEmptyStrings() {
        Splitter outerSplitter = Splitter.on(':').trimResults().omitEmptyStrings();
        Splitter innerSplitter = Splitter.on('=').trimResults().omitEmptyStrings();
        MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator(innerSplitter);
        String testString = "foo:bar=value1:bar:baz:qux=";
        Map<String, String> result = mapSplitter.split(testString);
        assertEquals(Collections.unmodifiableMap(Arrays.asMap()), result);
    }

    @Test
    public void testMapSplitterWithTrimmingAndLimitAndOmitEmptyStrings() {
        Splitter outerSplitter = Splitter.on(':').trimResults().omitEmptyStrings().limit(2);
        Splitter innerSplitter = Splitter.on('=').trimResults().omitEmptyStrings();
        MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator(innerSplitter);
        String testString = "foo:bar=value1:bar:baz:qux=";
        Map<String, String> result = mapSplitter.split(testString);
        assertEquals(Collections.unmodifiableMap(Arrays.asMap()), result);
    }
}