package com.example.junit.test.joiner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.base.MapJoiner;
import com.google.common.base.Preconditions;

public class JoinerTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testJoinerOnString() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
        assertEquals("apple,banana,cherry", result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testJoinerOnChar() {
        Joiner joiner = Joiner.on('-');
        String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
        assertEquals("apple-banana-cherry", result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAppendToWithIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        joiner.appendTo(outputStream, Arrays.asList("apple", "banana", "cherry"));
        String result = outputStream.toString();
        assertEquals("apple,banana,cherry", result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAppendToWithObjectArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        joiner.appendTo(outputStream, new Object[] {"apple", "banana", "cherry"});
        String result = outputStream.toString();
        assertEquals("apple,banana,cherry", result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAppendToWithNullableObject() throws IOException {
        Joiner joiner = Joiner.on(",");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        joiner.appendTo(outputStream, new Object[] {"apple", null, "cherry"});
        String result = outputStream.toString();
        assertEquals("apple,null,cherry", result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAppendToWithIterableEmpty() throws IOException {
        Joiner joiner = Joiner.on(",");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        joiner.appendTo(outputStream, Collections.emptyList());
        String result = outputStream.toString();
        assertEquals("", result);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = IllegalArgumentException.class)
    public void testAppendToWithNullAppendable() throws IOException {
        Joiner joiner = Joiner.on(",");
        joiner.appendTo(null, Arrays.asList("apple", "banana", "cherry"));
    }

    @Test
    public void testJoinWithNullIterable() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(null);
        assertEquals("", result);
    }

    @Test
    public void testJoinWithIterableEmpty() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(Collections.emptyList());
        assertEquals("", result);
    }

    @Test
    public void testJoinWithSingleObject() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(Arrays.asList("apple"));
        assertEquals("apple", result);
    }

    @Test
    public void testJoinWithIterableMultipleObjects() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
        assertEquals("apple,banana,cherry", result);
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",");
        Joiner nullJoiner = joiner.useForNull("N/A");
        String result = nullJoiner.join(Arrays.asList("apple", null, "cherry"));
        assertEquals("apple,N/A,cherry", result);
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",");
        Joiner skipNullsJoiner = joiner.skipNulls();
        String result = skipNullsJoiner.join(Arrays.asList(null, "apple", null, "cherry"));
        assertEquals("apple", result);
    }

    @Test
    public void testMapJoinerJoinWithMapEmpty() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        Map<String, String> map = new HashMap<>();
        String result = mapJoiner.join(map);
        assertEquals("", result);
    }

    @Test
    public void testMapJoinerJoinWithMapSingleKeyValue() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        String result = mapJoiner.join(map);
        assertEquals("key1:value1", result);
    }

    @Test
    public void testMapJoinerJoinWithMapMultipleKeyValue() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        String result = mapJoiner.join(map);
        assertEquals("key1:value1,key2:value2,key3:value3", result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMapJoinerUseForNull() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        Joiner nullJoiner = mapJoiner.useForNull("N/A");
        Map<String, String> map = new HashMap<>();
        nullJoiner.join(map);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMapJoinerSkipNulls() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        Joiner skipNullsJoiner = mapJoiner.skipNulls();
        Map<String, String> map = new HashMap<>();
        map.put(null, "value");
        skipNullsJoiner.join(map);
    }
}