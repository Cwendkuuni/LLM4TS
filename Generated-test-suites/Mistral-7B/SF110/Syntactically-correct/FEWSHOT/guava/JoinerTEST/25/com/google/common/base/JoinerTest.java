package com.google.common.base;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.StringWriter;
import java.util.*;

import org.junit.Test;

public class JoinerTest {

    private static final Joiner JOINER = Joiner.on(",");

    @Test
    public void testJoinIterable() {
        List<String> list = Arrays.asList("one", "two", "three");
        StringBuilder expected = new StringBuilder();
        expected.append("one").append(JOINER.separator).append("two").append(JOINER.separator).append("three");
        StringBuilder result = new StringBuilder();
        JOINER.appendTo(result, list.iterator());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testJoinArray() {
        Object[] array = new Object[] {"one", "two", "three"};
        StringBuilder expected = new StringBuilder();
        expected.append("one").append(JOINER.separator).append("two").append(JOINER.separator).append("three");
        StringBuilder result = new StringBuilder();
        JOINER.appendTo(result, array);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testJoinNullAndObjects() {
        Object[] array = new Object[] {null, "one", "two", null};
        StringBuilder expected = new StringBuilder();
        expected.append((Joiner.this.useForNull(null).toString(null)));
        expected.append(JOINER.separator);
        expected.append("one");
        expected.append(JOINER.separator);
        expected.append("two");
        expected.append(JOINER.separator);
        expected.append((Joiner.this.useForNull(null).toString(null)));
        StringBuilder result = new StringBuilder();
        Joiner joiner = Joiner.on(",");
        JOINER.appendTo(result, array);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testJoinIterator() {
        Iterator<String> iterator = Arrays.asList("one", "two", "three").iterator();
        StringBuilder expected = new StringBuilder();
        expected.append("one").append(JOINER.separator).append("two").append(JOINER.separator).append("three");
        StringBuilder result = new StringBuilder();
        JOINER.appendTo(result, iterator);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testJoinMapEntrySet() {
        Map<String, String> map = new HashMap<>();
        map.put("one", "oneValue");
        map.put("two", "twoValue");
        map.put("three", "threeValue");
        StringWriter expected = new StringWriter();
        expected.append("one").append(JOINER.keyValueSeparator).append("oneValue");
        expected.append(JOINER.separator);
        expected.append("two").append(JOINER.keyValueSeparator).append("twoValue");
        expected.append(JOINER.separator);
        expected.append("three").append(JOINER.keyValueSeparator).append("threeValue");
        StringWriter result = new StringWriter();
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
        mapJoiner.appendTo(result, map.entrySet());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testJoinMapEntryIterator() {
        Map<String, String> map = new HashMap<>();
        map.put("one", "oneValue");
        map.put("two", "twoValue");
        map.put("three", "threeValue");
        StringBuilder expected = new StringBuilder();
        expected.append("one=").append("oneValue");
        expected.append(JOINER.separator);
        expected.append("two=").append("twoValue");
        expected.append(JOINER.separator);
        expected.append("three=").append("threeValue");
        StringBuilder result = new StringBuilder();
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
        mapJoiner.appendTo(result, map.entrySet().iterator());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testJoinMapEntryArray() {
        Map<String, String> map = new HashMap<>();
        map.put("one", "oneValue");
        map.put("two", "twoValue");
        map.put("three", "threeValue");
        Object[] array = map.entrySet().toArray();
        StringBuilder expected = new StringBuilder();
        expected.append("one=").append("oneValue");
        expected.append(JOINER.separator);
        expected.append("two=").append("twoValue");
        expected.append(JOINER.separator);
        expected.append("three=").append("threeValue");
        StringBuilder result = new StringBuilder();
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
        mapJoiner.appendTo(result, (Iterable<? extends Map.Entry<?, ?>>) array);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testJoinMapEntryNullKey() {
        Map<String, String> map = new HashMap<>();
        map.put(null, "oneValue");
        map.put("two", "twoValue");
        map.put("three", "threeValue");
        Object[] array = map.entrySet().toArray();
        StringBuilder expected = new StringBuilder();
        expected.append((Joiner.this.useForNull(null).toString(null)));
        expected.append(JOINER.keyValueSeparator);
        expected.append("oneValue");
        expected.append(JOINER.separator);
        expected.append("two");
        expected.append(JOINER.keyValueSeparator);
        expected.append("twoValue");
        expected.append(JOINER.separator);
        expected.append("three");
        expected.append(JOINER.keyValueSeparator);
        expected.append("threeValue");
        StringBuilder result = new StringBuilder();
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=").useForNull("null");
        mapJoiner.appendTo(result, (Iterable<? extends Map.Entry<?, ?>>) array);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testJoinMapEntryNullValue() {
        Map<String, String> map = new HashMap<>();
        map.put("one", null);
        map.put("two", "twoValue");
        map.put("three", "threeValue");
        Object[] array = map.entrySet().toArray();
        StringBuilder expected = new StringBuilder();
        expected.append("one").append(JOINER.separator);
        expected.append((Joiner.this.useForNull(null).toString(null)));
        expected.append(JOINER.separator);
        expected.append("two");
        expected.append(JOINER.keyValueSeparator);
        expected.append("twoValue");
        expected.append(JOINER.separator);
        expected.append("three");
        expected.append(JOINER.keyValueSeparator);
        expected.append("threeValue");
        StringBuilder result = new StringBuilder();
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=").useForNull("null");
        mapJoiner.appendTo(result, (Iterable<? extends Map.Entry<?, ?>>) array);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testJoinMapEntryNullBoth() {
        Map<String, String> map = new HashMap<>();
        map.put(null, null);
        map.put("two", "twoValue");
        map.put("three", "threeValue");
        Object[] array = map.entrySet().toArray();
        StringBuilder expected = new StringBuilder();
        expected.append((Joiner.this.useForNull(null).toString(null)));
        expected.append(JOINER.keyValueSeparator);
        expected.append((Joiner.this.useForNull(null).toString(null)));
        expected.append(JOINER.separator);
        expected.append("two");
        expected.append(JOINER.keyValueSeparator);
        expected.append("twoValue");
        expected.append(JOINER.separator);
        expected.append("three");
        expected.append(JOINER.keyValueSeparator);
        expected.append("threeValue");
        StringBuilder result = new StringBuilder();
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=").useForNull("null");
        mapJoiner.appendTo(result, (Iterable<? extends Map.Entry<?, ?>>) array);
        assertEquals(expected.toString(), result.toString());
    }
}