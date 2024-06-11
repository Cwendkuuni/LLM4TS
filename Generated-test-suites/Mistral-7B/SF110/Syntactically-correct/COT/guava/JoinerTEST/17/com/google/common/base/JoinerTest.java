package com.example.junit; // replace with your test package name if needed

import static org.junit.Assert.*;
import com.google.common.base.Joiner;
import com.google.common.base.MapJoiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JoinerTest {

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testJoinerConstruction() {
        Joiner joiner = Joiner.on(",");
        assertEquals(",", joiner.separator());
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testAppendToIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = new StringWriter();
        joiner.appendTo(stringWriter, Arrays.asList("apple", "banana", "cherry"));
        assertEquals("apple,banana,cherry", stringWriter.toString());
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testAppendToObjectArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = new StringWriter();
        joiner.appendTo(stringWriter, new Object[]{"apple", "banana", "cherry"});
        assertEquals("apple,banana,cherry", stringWriter.toString());
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testAppendToIterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = new StringWriter();
        Iterator<String> iterator = Arrays.asList("apple", "banana", "cherry").iterator();
        joiner.appendTo(stringWriter, iterator);
        assertEquals("apple,banana,cherry", stringWriter.toString());
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(",");
        assertEquals("apple,banana,cherry", joiner.join(Arrays.asList("apple", "banana", "cherry")));
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testJoinObjectArray() {
        Joiner joiner = Joiner.on(",");
        assertEquals("apple,banana,cherry", joiner.join(new Object[]{"apple", "banana", "cherry"}));
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testJoinIterator() {
        Joiner joiner = Joiner.on(",");
        assertEquals("apple,banana,cherry", joiner.join(Arrays.asList("apple", "banana", "cherry").iterator()));
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",");
        Joiner joinerWithNullText = joiner.useForNull("null");
        assertEquals("null", joinerWithNullText.toString(null));
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSkipNulls() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = new StringWriter();
        joiner.appendTo(stringWriter, Arrays.asList("apple", "null", "banana"));
        assertEquals("apple,banana", stringWriter.toString());
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testMapJoinerAppendToMapEntrySet() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = new StringWriter();
        Map<String, String> map = Maps.newHashMap();
        map.put("key1", "value1");
        map.put("key2", "value2");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        mapJoiner.appendTo(stringWriter, map.entrySet());
        assertEquals("key1:value1,key2:value2", stringWriter.toString());
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testMapJoinerJoinMapEntrySet() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.withKeyValueSeparator(":").join(Maps.newHashMap().entrySet());
        assertEquals("key1:value1,key2:value2", result);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testUseForNullAndSkipNulls() {
        Joiner joiner = Joiner.on(",");
        Joiner joinerWithNullText = joiner.useForNull("null");
        joinerWithNullText.skipNulls();
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsAndWithKeyValueSeparator() {
        Joiner joiner = Joiner.on(",");
        Joiner joinerWithKeyValueSeparator = joiner.withKeyValueSeparator(":");
        joinerWithKeyValueSeparator.skipNulls();
    }
}