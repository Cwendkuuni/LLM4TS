package org.apache.commons.cli; // assuming the Joiner class is in the package com.google.common.base

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class JoinerTest {

    private Joiner joiner;

    @Before
    public void setup() {
        joiner = Joiner.on(":");
    }

    @Test
    public void testOnString() {
        Joiner joiner = Joiner.on(":");
        assertEquals("a:b:c", joiner.join(Arrays.asList("a", "b", "c")));
        assertEquals("a:b:c:d:e", joiner.join(Arrays.asList("a", "b", "c", "d", "e")));
    }

    @Test
    public void testOnChar() {
        Joiner joiner = Joiner.on(';');
        assertEquals("a;b;c", joiner.join(Arrays.asList("a", "b", "c")));
        assertEquals("a;b;c;d;e", joiner.join(Arrays.asList("a", "b", "c", "d", "e")));
    }

    @Test
    public void testAppendToIterable() throws IOException {
        StringWriter writer = new StringWriter();
        joiner.appendTo(writer, Arrays.asList("a", "b", "c"));
        assertEquals("a:b:c", writer.toString());
    }

    @Test
    public void testAppendToObjectArray() throws IOException {
        StringWriter writer = new StringWriter();
        joiner.appendTo(writer, new Object[]{"a", "b", "c"});
        assertEquals("a:b:c", writer.toString());
    }

    @Test
    public void testAppendToIterableAndNull() throws IOException {
        StringWriter writer = new StringWriter();
        joiner.appendTo(writer, Arrays.asList("a", null, "c"));
        assertEquals("a:" + Joiner.NULL_REPLACEMENT + ":c", writer.toString());
    }

    @Test
    public void testAppendToObjectAndNull() throws IOException {
        StringWriter writer = new StringWriter();
        joiner.appendTo(writer, "a", null, "c");
        assertEquals("a:" + Joiner.NULL_REPLACEMENT + ":c", writer.toString());
    }

    @Test
    public void testAppendToObjectArrayAndNull() throws IOException {
        StringWriter writer = new StringWriter();
        joiner.appendTo(writer, new Object[]{"a", null, "c"});
        assertEquals("a:" + Joiner.NULL_REPLACEMENT + ":c", writer.toString());
    }

    @Test
    public void testAppendToIterableAndNullAndEmpty() throws IOException {
        StringWriter writer = new StringWriter();
        joiner.appendTo(writer, Arrays.asList(null, null, null, ""));
        assertEquals(Joiner.NULL_REPLACEMENT + Joiner.NULL_REPLACEMENT + Joiner.NULL_REPLACEMENT + Joiner.NULL_REPLACEMENT, writer.toString());
    }

    @Test
    public void testAppendToStringBuilder() {
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("a", "b", "c"));
        assertEquals("a:b:c", builder.toString());
    }

    @Test
    public void testAppendToStringBuilderAndNull() {
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("a", null, "c"));
        assertEquals("a:" + Joiner.NULL_REPLACEMENT + ":c", builder.toString());
    }

    @Test
    public void testJoinIterable() {
        assertEquals("a:b:c", joiner.join(Arrays.asList("a", "b", "c")));
        assertEquals("", joiner.join(Collections.emptyList()));
    }

    @Test
    public void testJoinObjectArray() {
        assertEquals("a:b:c", joiner.join(new Object[]{"a", "b", "c"}));
        assertEquals("", joiner.join(new Object[0]));
    }

    @Test
    public void testJoinObjectAndNull() {
        assertEquals("a:" + Joiner.NULL_REPLACEMENT + ":c", joiner.join("a", null, "c"));
        assertEquals("", joiner.join(null, null, null));
    }

    @Test
    public void testJoinObjectArrayAndNull() {
        assertEquals("a:" + Joiner.NULL_REPLACEMENT + ":c", joiner.join(new Object[]{"a", null, "c"}));
    }

    @Test
    public void testJoinIterableAndNull() {
        assertEquals("", joiner.join(Arrays.asList(null)));
        assertEquals("", joiner.join(Arrays.asList(null, null)));
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(":").useForNull("N/A");
        assertEquals("a:N/A:b:N/A:c", joiner.join(Arrays.asList("a", null, "b", null, "c")));
    }

    @Test
    public void testUseForNullAndSkipNulls() {
        Joiner joiner = Joiner.on(":").skipNulls().useForNull("N/A");
        assertEquals("a:b:c", joiner.join(Arrays.asList("a", null, "b", null, "c")));
    }

    @Test
    public void testAppendToMapEntrySet() throws IOException {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        StringWriter writer = new StringWriter();
        joiner.appendTo(writer, map.entrySet());
        assertEquals("one:1:two:2:three:3", writer.toString());
    }

    @Test
    public void testJoinMapEntrySet() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        assertEquals("one:1:two:2:three:3", joiner.join(map.entrySet()));
    }

    @Test
    public void testUseForNullWithMapEntrySet() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", null);
        map.put("three", 3);

        assertEquals("one:1:three:3", joiner.useForNull("N/A").join(map.entrySet()));
    }

    @Test
    public void testSkipNullsWithMapEntrySet() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", null);
        map.put("three", 3);

        assertEquals("one:1:three:3", joiner.skipNulls().join(map.entrySet()));
    }

    @Test
    public void testWithKeyValueSeparator() {
        Joiner joiner = Joiner.on(":").withKeyValueSeparator("=");

        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        String expected = "one=1:two=2:three=3";

        assertEquals(expected, joiner.join(map.entrySet()));
    }
}