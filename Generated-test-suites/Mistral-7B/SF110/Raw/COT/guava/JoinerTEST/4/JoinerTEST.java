package com.example.junit; // replace with your test package name if needed

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.base.MapJoiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class JoinerTest {

    @Test
    public void testJoinerConstructor() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
    }

    @Test
    public void testAppendToWithIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        joiner.appendTo(outputStream, Lists.newArrayList("apple", "banana", "cherry"));
        String expected = "apple,banana,cherry";
        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testAppendToWithIterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Iterator<String> iterator = Arrays.asList("apple", "banana", "cherry").iterator();
        joiner.appendTo(outputStream, iterator);
        String expected = "apple,banana,cherry";
        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testAppendToWithObjectArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        joiner.appendTo(outputStream, new Object[] {"apple", "banana", "cherry"});
        String expected = "apple,banana,cherry";
        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testAppendToWithSingleObject() throws IOException {
        Joiner joiner = Joiner.on(",");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        joiner.appendTo(outputStream, "apple");
        String expected = "apple";
        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testAppendToWithNullObject() throws IOException {
        Joiner joiner = Joiner.on(",");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        joiner.appendTo(outputStream, null);
        String expected = "";
        assertEquals(expected, outputStream.toString());
    }

    @Test(expected = IOException.class)
    public void testAppendToWithNullAppendable() throws IOException {
        Joiner joiner = Joiner.on(",");
        joiner.appendTo(null, Arrays.asList("apple", "banana", "cherry"));
    }

    @Test
    public void testJoinWithIterable() {
        Joiner joiner = Joiner.on(",");
        String expected = "apple,banana,cherry";
        assertEquals(expected, joiner.join(Arrays.asList("apple", "banana", "cherry")));
    }

    @Test
    public void testJoinWithIterator() {
        Joiner joiner = Joiner.on(",");
        Iterator<String> iterator = Arrays.asList("apple", "banana", "cherry").iterator();
        String expected = "apple,banana,cherry";
        assertEquals(expected, joiner.join(iterator));
    }

    @Test
    public void testJoinWithObjectArray() {
        Joiner joiner = Joiner.on(",");
        String expected = "apple,banana,cherry";
        assertEquals(expected, joiner.join(new Object[] {"apple", "banana", "cherry"}));
    }

    @Test
    public void testJoinWithSingleObject() {
        Joiner joiner = Joiner.on(",");
        String expected = "apple";
        assertEquals(expected, joiner.join("apple"));
    }

    @Test
    public void testJoinWithNullObject() {
        Joiner joiner = Joiner.on(",");
        String expected = "";
        assertEquals(expected, joiner.join(null));
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",");
        Joiner nullTextJoiner = joiner.useForNull("NULL");
        assertEquals("NULL", nullTextJoiner.toString(null));
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",");
        Joiner skipNullsJoiner = joiner.skipNulls();
        List<Object> list = Lists.newArrayList("apple", null, "banana", null, "cherry");
        String expected = "apple,banana";
        assertEquals(expected, skipNullsJoiner.join(list.iterator()));
    }

    @Test
    public void testMapJoinerAppendToWithMapEntryIterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        Map<String, String> map = Maps.newHashMap();
        map.put("key1", "value1");
        map.put("key2", null);
        map.put("key3", "value3");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mapJoiner.appendTo(outputStream, map.entrySet());
        String expected = "key1:value1,key3:value3";
        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testMapJoinerJoinWithMapEntryIterator() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        Map<String, String> map = Maps.newHashMap();
        map.put("key1", "value1");
        map.put("key2", null);
        map.put("key3", "value3");
        String expected = "key1:value1,key3:value3";
        assertEquals(expected, mapJoiner.join(map.entrySet()));
    }

    @Test
    public void testMapJoinerAppendToWithMapEntryList() throws IOException {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        Map<String, String> map = Maps.newHashMap();
        map.put("key1", "value1");
        map.put("key2", null);
        map.put("key3", "value3");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mapJoiner.appendTo(outputStream, map.entrySet());
        String expected = "key1:value1,key3:value3";
        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testMapJoinerJoinWithMapEntryList() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        Map<String, String> map = Maps.newHashMap();
        map.put("key1", "value1");
        map.put("key2", null);
        map.put("key3", "value3");
        String expected = "key1:value1,key3:value3";
        assertEquals(expected, mapJoiner.join(map.entrySet()));
    }

    @Test(expected = IOException.class)
    public void testMapJoinerAppendToWithNullIterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        mapJoiner.appendTo(null, Maps.newHashMap().entrySet().iterator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapJoinerJoinWithNullMap() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        mapJoiner.join(null);
    }

    @Test
    public void testNullTextForMapJoiner() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        Joiner nullTextJoiner = mapJoiner.useForNull("NULL");
        Map<String, String> map = Maps.newHashMap();
        map.put("key1", "value1");
        map.put("key2", null);
        map.put("key3", "value3");
        StringBuilder builder = new StringBuilder();
        nullTextJoiner.appendTo(builder, map.entrySet());
        String expected = "key1:value1,NULL:null,key3:value3";
        assertEquals(expected, builder.toString());
    }
}