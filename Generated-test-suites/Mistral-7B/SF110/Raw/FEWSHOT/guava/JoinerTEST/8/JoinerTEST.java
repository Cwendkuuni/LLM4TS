package com.google.common.base;

import static com.google.common.base.Joiner.Util.iterable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class JoinerTest {

    @Test
    public void testJoinStringSeparator() {
        Joiner joiner = Joiner.on(":");
        List<String> list = Arrays.asList("foo", "bar", "baz");
        String expected = "foo:bar:baz";
        assertEquals(expected, joiner.join(list));
    }

    @Test
    public void testJoinCharSeparator() {
        Joiner joiner = Joiner.on('-');
        List<String> list = Arrays.asList("foo", "bar", "baz");
        String expected = "foo-bar-baz";
        assertEquals(expected, joiner.join(list));
    }

    @Test
    public void testAppendToStringBuilder() throws IOException {
        Joiner joiner = Joiner.on(":");
        StringBuilder stringBuilder = new StringBuilder();
        List<String> list = Arrays.asList("foo", "bar", "baz");
        joiner.appendTo(stringBuilder, list);
        String expected = "foo:bar:baz";
        assertEquals(expected, stringBuilder.toString());
    }

    @Test
    public void testAppendToByteArrayOutputStream() throws IOException {
        Joiner joiner = Joiner.on(":");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        List<String> list = Arrays.asList("foo", "bar", "baz");
        joiner.appendTo(outputStream, list);
        String expected = "foo:bar:baz";
        assertEquals(expected, new String(outputStream.toByteArray()));
    }

    @Test
    public void testAppendToIterable() throws IOException {
        Joiner joiner = Joiner.on(":");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Iterable<String> iterable = Arrays.asList("foo", "bar", "baz");
        joiner.appendTo(outputStream, iterable);
        String expected = "foo:bar:baz";
        assertEquals(expected, new String(outputStream.toByteArray()));
    }

    @Test
    public void testAppendToObjectArray() throws IOException {
        Joiner joiner = Joiner.on(":");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Object[] array = {"foo", "bar", "baz"};
        joiner.appendTo(outputStream, array);
        String expected = "foo:bar:baz";
        assertEquals(expected, new String(outputStream.toByteArray()));
    }

    @Test
    public void testAppendToNullAndObjects() throws IOException {
        Joiner joiner = Joiner.on(":");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        joiner.appendTo(outputStream, (Object[]) null, "foo", "bar", null);
        String expected = ":foo:bar:";
        assertEquals(expected, new String(outputStream.toByteArray()));
    }

    @Test
    public void testAppendToMap() throws IOException {
        Joiner joiner = Joiner.on(":");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        joiner.appendTo(outputStream, map.entrySet());
        String expected = "key1:value1:key2:value2:key3:value3";
        assertEquals(expected, new String(outputStream.toByteArray()));
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(":");
        assertEquals("null", joiner.useForNull("null").join(Arrays.asList("foo", null, "bar")));
        assertEquals("foo:bar", joiner.useForNull(null).join(Arrays.asList("foo", null, "bar")));
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(":");
        assertEquals("foo:bar", joiner.skipNulls().join(Arrays.asList("foo", null, "bar")));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUseForNullAndSkipNulls() {
        Joiner joiner = Joiner.on(":");
        joiner.useForNull("null").skipNulls();
    }

    @Test
    public void testWithKeyValueSeparator() {
        Joiner joiner = Joiner.on(":");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        StringBuilder expected = new StringBuilder();
        expected.append("key1=value1:key2=value2:key3=value3");
        assertEquals(expected.toString(), mapJoiner.join(map.entrySet()));
    }

    @Test
    public void testToString() {
        Joiner joiner = Joiner.on(":");
        assertEquals(":", joiner.toString(null));
        assertEquals("foo", joiner.toString("foo"));
    }
}