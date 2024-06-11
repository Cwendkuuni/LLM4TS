package com.google.common.base;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

public class JoinerTest {

    Joiner joiner;
    ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        joiner = Joiner.on(",");
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    public void testAppendToIterable() throws IOException {
        List<String> list = Arrays.asList("apple", "banana", "cherry");
        joiner.appendTo(outputStream, list);
        assertEquals("apple,banana,cherry", outputStream.toString());
    }

    @Test
    public void testAppendToObjectArray() throws IOException {
        Object[] objects = new Object[]{"apple", "banana", "cherry"};
        joiner.appendTo(outputStream, objects);
        assertEquals("apple,banana,cherry", outputStream.toString());
    }

    @Test
    public void testAppendToNullObjectFirst() throws IOException {
        joiner.appendTo(outputStream, null, "apple", "banana");
        assertNull(outputStream.toString());
    }

    @Test
    public void testAppendToNullObjectSecond() throws IOException {
        joiner.appendTo(outputStream, "apple", null, "banana");
        assertNull(outputStream.toString());
    }

    @Test
    public void testAppendToNullObjectRest() throws IOException {
        joiner.appendTo(outputStream, "apple", "banana", null);
        assertNull(outputStream.toString());
    }

    @Test
    public void testJoinIterable() {
        List<String> list = Arrays.asList("apple", "banana", "cherry");
        assertEquals("apple,banana,cherry", joiner.join(list));
    }

    @Test
    public void testJoinObjectArray() {
        Object[] objects = new Object[]{"apple", "banana", "cherry"};
        assertEquals("apple,banana,cherry", joiner.join(objects));
    }

    @Test
    public void testJoinNullObjectFirst() {
        assertEquals("", joiner.join(null, "apple", "banana"));
    }

    @Test
    public void testJoinNullObjectSecond() {
        assertEquals("apple", joiner.join("apple", null, "banana"));
    }

    @Test
    public void testJoinNullObjectRest() {
        assertEquals("apple,banana", joiner.join("apple", "banana", null));
    }

    @Test
    public void testUseForNull() throws IOException {
        String nullText = "null";
        Joiner joinerWithNullText = joiner.useForNull(nullText);
        List<String> list = Arrays.asList(null, "apple", "banana");
        joinerWithNullText.appendTo(outputStream, list);
        assertEquals("null,apple,banana", outputStream.toString());
    }

    @Test
    public void testSkipNulls() throws IOException {
        Joiner joiner = Joiner.on(",");
        Joiner joinerSkippingNulls = joiner.skipNulls();
        List<Object> list = Arrays.asList(null, "apple", null, "banana");
        joinerSkippingNulls.appendTo(outputStream, list);
        assertEquals("apple,banana", outputStream.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUseForNullAndSkipNulls() throws IOException {
        Joiner joiner = Joiner.on(",");
        joiner.useForNull("null").skipNulls();
    }

    @Test
    public void testWithKeyValueSeparator() {
        String keyValueSeparator = "=";
        Joiner joiner = Joiner.on(":");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(keyValueSeparator);
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", null);
        assertEquals("key1:value1,key2:value2", mapJoiner.join(map));
    }
}