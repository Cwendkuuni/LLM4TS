package com.google.common.base;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.StringWriter;
import java.util.*;

import org.junit.Test;

public class JoinerTest {

    @Test
    public void testJoinerOnString() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
        assertEquals("apple,banana,cherry", result);
    }

    @Test
    public void testJoinerOnChar() {
        Joiner joiner = Joiner.on('-');
        String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
        assertEquals("apple-banana-cherry", result);
    }

    @Test
    public void testAppendToIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter appendable = new StringWriter();
        joiner.appendTo(appendable, Arrays.asList("apple", "banana", "cherry"));
        assertEquals("apple,banana,cherry", appendable.toString());
    }

    @Test
    public void testAppendToObjectArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter appendable = new StringWriter();
        joiner.appendTo(appendable, new Object[]{"apple", "banana", "cherry"});
        assertEquals("apple,banana,cherry", appendable.toString());
    }

    @Test
    public void testAppendToNull() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter appendable = new StringWriter();
        joiner.appendTo(appendable, null);
        assertNull(appendable.toString());
    }

    @Test
    public void testAppendToIterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter appendable = new StringWriter();
        Iterator<String> iterator = Arrays.asList("apple", "banana", "cherry").iterator();
        joiner.appendTo(appendable, iterator);
        assertEquals("apple,banana,cherry", appendable.toString());
    }

    @Test
    public void testJoin() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
        assertEquals("apple,banana,cherry", result);
    }

    @Test
    public void testJoinObjectArray() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(new Object[]{"apple", "banana", "cherry"});
        assertEquals("apple,banana,cherry", result);
    }

    @Test
    public void testJoinNull() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(null);
        assertNull(result);
    }

    @Test
    public void testJoinIterator() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(Arrays.asList("apple", "banana", "cherry").iterator());
        assertEquals("apple,banana,cherry", result);
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",");
        Joiner joinerWithNull = joiner.useForNull("NA");
        String result = joinerWithNull.join(Arrays.asList("apple", null, "cherry"));
        assertEquals("apple,NA,cherry", result);
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",");
        Joiner joinerWithNull = joiner.skipNulls();
        String result = joinerWithNull.join(Arrays.asList("apple", null, "cherry"));
        assertEquals("apple,cherry", result);
    }

    @Test
    public void testMapJoiner() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        String result = mapJoiner.join(map.entrySet());
        assertEquals("key1:value1,key2:value2,key3:value3", result);
    }
}