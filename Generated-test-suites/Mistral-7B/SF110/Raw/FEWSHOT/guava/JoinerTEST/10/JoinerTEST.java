package com.google.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class JoinerTest {

    @Test
    public void testJoinerConstruction() {
        Joiner joiner = Joiner.on(",");
        assertEquals(",", joiner.separator());

        joiner = Joiner.on("-");
        assertEquals("-", joiner.separator());
    }

    @Test
    public void testAppendToIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = new StringWriter();
        joiner.appendTo(stringWriter, Arrays.asList("one", "two", "three"));
        assertEquals("one,two,three", stringWriter.toString());
    }

    @Test
    public void testAppendToIterableWithNulls() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = new StringWriter();
        joiner.appendTo(stringWriter, Arrays.asList("one", null, "two", null, "three"));
        assertEquals("one,null,two,null,three", stringWriter.toString());
    }

    @Test
    public void testAppendToObjectArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = new StringWriter();
        joiner.appendTo(stringWriter, new String[]{"one", "two", "three"});
        assertEquals("one,two,three", stringWriter.toString());
    }

    @Test
    public void testAppendToObjectArrayWithNulls() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = new StringWriter();
        joiner.appendTo(stringWriter, new String[]{"one", null, "two", null, "three"});
        assertEquals("one,null,two,null,three", stringWriter.toString());
    }

    @Test
    public void testAppendToIterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = new StringWriter();
        Iterator<String> iterator = Arrays.asList("one", "two", "three").iterator();
        joiner.appendTo(stringWriter, iterator);
        assertEquals("one,two,three", stringWriter.toString());
    }

    @Test
    public void testAppendToIteratorWithNulls() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = new StringWriter();
        Iterator<String> iterator = Arrays.asList("one", null, "two", null, "three").iterator();
        joiner.appendTo(stringWriter, iterator);
        assertEquals("one,null,two,null,three", stringWriter.toString());
    }

    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(",");
        assertEquals("one,two,three", joiner.join(Arrays.asList("one", "two", "three")));
    }

    @Test
    public void testJoinObjectArray() {
        Joiner joiner = Joiner.on(",");
        assertEquals("one,two,three", joiner.join(new String[]{"one", "two", "three"}));
    }

    @Test
    public void testJoinObjectArrayWithNulls() {
        Joiner joiner = Joiner.on(",");
        assertEquals("one,null,two,null,three", joiner.join(new String[]{"one", null, "two", null, "three"}));
    }

    @Test
    public void testJoinIterator() {
        Joiner joiner = Joiner.on(",");
        assertEquals("one,two,three", joiner.join(Arrays.asList("one", "two", "three").iterator()));
    }

    @Test
    public void testJoinNullIterable() {
        Joiner joiner = Joiner.on(",");
        assertEquals("", joiner.join(null));
    }

    @Test
    public void testJoinEmptyIterable() {
        Joiner joiner = Joiner.on(",");
        assertEquals("", joiner.join(Collections.emptyList()));
    }

    @Test
    public void testJoinEmptyObjectArray() {
        Joiner joiner = Joiner.on(",");
        assertEquals("", joiner.join(new String[0]));
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",");
        assertEquals("null", joiner.useForNull(null).toString(null));
        assertEquals("one,two,three", joiner.join(new String[]{"one", "two", "three"}));
        assertEquals("one,null,two,null,three", joiner.useForNull("null").join(new String[]{"one", null, "two", null, "three"}));
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",");
        assertEquals("one,two,three", joiner.join(Arrays.asList("one", null, "two", null, "three")));
        assertEquals("one,two", joiner.skipNulls().join(Arrays.asList("one", null, "two", null, "three")));
        assertEquals("", joiner.skipNulls().join(new String[0]));
    }

    @Test
    public void testMapJoinerConstruction() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
        assertEquals(",", mapJoiner.separator());
        assertEquals("=", mapJoiner.keyValueSeparator());
    }

    @Test
    public void testMapJoinerJoinMap() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        assertEquals("key1=value1,key2=value2,key3=value3", mapJoiner.join(map));
    }

    @Test
    public void testMapJoinerAppendToMapEntrySet() throws IOException {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
        StringWriter stringWriter = new StringWriter();
        mapJoiner.appendTo(stringWriter, new HashSet<>(Arrays.asList(
                Map.entry("key1", "value1"), Map.entry("key2", "value2"), Map.entry("key3", "value3")).iterator()));
        assertEquals("key1=value1,key2=value2,key3=value3", stringWriter.toString());
    }
}