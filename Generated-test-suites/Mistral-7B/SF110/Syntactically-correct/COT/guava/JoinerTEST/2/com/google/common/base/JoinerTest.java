package com.example.junit4; // replace with your package name

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.base.MapJoiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;

public class JoinerTest {

    private Joiner joiner;
    private MapJoiner mapJoiner;

    @Before
    public void setUp() {
        joiner = Joiner.on(",");
        mapJoiner = joiner.withKeyValueSeparator(":");
    }

    @Test
    public void testJoinerConstructionWithStringSeparator() throws IOException {
        Joiner joiner = Joiner.on(",");
        assertEquals(",", joiner.separator());
    }

    @Test
    public void testJoinerConstructionWithCharSeparator() throws IOException {
        Joiner joiner = Joiner.on('-');
        assertEquals("-", joiner.separator());
    }

    @Test
    public void testAppendToIterable() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Joiner joiner = Joiner.on(",");
        joiner.appendTo(outputStream, Arrays.asList("apple", "banana", "cherry"));
        assertEquals("apple,banana,cherry", outputStream.toString());
    }

    @Test
    public void testAppendToObjectArray() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Joiner joiner = Joiner.on(",");
        joiner.appendTo(outputStream, new Object[]{"apple", "banana", "cherry"});
        assertEquals("apple,banana,cherry", outputStream.toString());
    }

    @Test
    public void testAppendToIterator() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Joiner joiner = Joiner.on(",");
        Iterator<String> iterator = Arrays.asList("apple", "banana", "cherry").iterator();
        joiner.appendTo(outputStream, iterator);
        assertEquals("apple,banana,cherry", outputStream.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendToNullIterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        joiner.appendTo(mock(Appendable.class), (Iterator<Object>)mock(Iterator.class).stubReturnNull());
    }

    @Test(expected = IOException.class)
    public void testAppendToIOException() throws IOException {
        ByteArrayOutputStream outputStream = mock(ByteArrayOutputStream.class);
        doThrow(IOException.class).when(outputStream).append(anyString());
        Joiner joiner = Joiner.on(",");
        joiner.appendTo(outputStream, Arrays.asList("apple", "banana", "cherry").iterator());
    }

    @Test
    public void testJoinIterable() {
        String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
        assertEquals("apple,banana,cherry", result);
    }

    @Test
    public void testJoinObjectArray() {
        String result = joiner.join(new Object[]{"apple", "banana", "cherry"});
        assertEquals("apple,banana,cherry", result);
    }

    @Test
    public void testJoinIterator() {
        String result = joiner.join(Arrays.asList("apple", "banana", "cherry").iterator());
        assertEquals("apple,banana,cherry", result);
    }

    @Test
    public void testJoinEmptyIterable() {
        String result = joiner.join(Collections.emptyList());
        assertEquals("", result);
    }

    @Test
    public void testJoinNullObject() {
        String result = joiner.join(new Object[]{"apple", null, "cherry"});
        assertEquals("apple,null,cherry", result);
    }

    @Test
    public void testMapJoinerAppendToMapEntrySet() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        mapJoiner.appendTo(outputStream, map.entrySet());
        assertEquals("key1:" + joiner.separator() + "value1" + joiner.separator() + "key2:" + joiner.separator() + "value2", outputStream.toString());
    }

    @Test
    public void testMapJoinerJoinMapEntrySet() {
        String result = mapJoiner.join(map.entrySet());
        assertEquals("key1:" + joiner.separator() + "value1" + joiner.separator() + "key2:" + joiner.separator() + "value2", result);
    }

    @Test
    public void testMapJoinerUseForNull() {
        Joiner joiner = joiner.useForNull("NULL");
        assertEquals("NULL", joiner.toString(null));
    }

    @Test
    public void testMapJoinerSkipNulls() {
        Joiner joiner = joiner.skipNulls();
        Iterator<String> nullIterator = Arrays.asList("apple", null, "cherry").iterator();
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, nullIterator);
        assertEquals("apple,cherry", sb.toString());
    }
}