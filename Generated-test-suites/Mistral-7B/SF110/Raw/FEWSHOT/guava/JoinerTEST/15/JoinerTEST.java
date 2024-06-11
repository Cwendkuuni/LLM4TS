package com.google.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class JoinerTest {

    @SuppressWarnings("unchecked")
    @Test
    public <T> void testAppendToIterable() throws IOException {
        Joiner joiner = Joiner.on(":");
        StringBuilder sb = new StringBuilder();
        Iterable<String> iterable = Arrays.asList("1", "2", "3");
        joiner.appendTo(sb, iterable);
        assertEquals("1:2:3", sb.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public <T> void testAppendToObjectArray() throws IOException {
        Joiner joiner = Joiner.on(":");
        StringBuilder sb = new StringBuilder();
        Object[] array = new Object[]{"1", "2", "3"};
        joiner.appendTo(sb, array);
        assertEquals("1:2:3", sb.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public <T> void testAppendToNullFirst() throws IOException {
        Joiner joiner = Joiner.on(":");
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, null, "2", "3");
        assertNull(sb.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public <T> void testAppendToNullSecond() throws IOException {
        Joiner joiner = Joiner.on(":");
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, "1", null, "3");
        assertNull(sb.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public <T> void testAppendToNullableObjectArray() throws IOException {
        Joiner joiner = Joiner.on(":");
        StringBuilder sb = new StringBuilder();
        Object[] array = new Object[]{"1", null, "3"};
        joiner.appendTo(sb, array);
        assertEquals("1:3", sb.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public <T> void testJoinIterable() {
        Joiner joiner = Joiner.on(":");
        Iterable<String> iterable = Arrays.asList("1", "2", "3");
        assertEquals("1:2:3", joiner.join(iterable));
    }

    @SuppressWarnings("unchecked")
    @Test
    public <T> void testJoinObjectArray() {
        Joiner joiner = Joiner.on(":");
        Object[] array = new Object[]{"1", "2", "3"};
        assertEquals("1:2:3", joiner.join(array));
    }

    @Test
    public void testJoinNull() {
        Joiner joiner = Joiner.on(":");
        assertEquals("", joiner.join(null));
    }

    @SuppressWarnings("unchecked")
    @Test
    public <T> void testJoinNullableObjectArray() {
        Joiner joiner = Joiner.on(":");
        Object[] array = new Object[]{"1", null, "3"};
        assertEquals("1:3", joiner.join(array));
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(":");
        Joiner joinerWithNullText = joiner.useForNull("NULL");
        assertEquals("NULL", joinerWithNullText.toString(null));
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(":");
        Joiner joinerSkippingNulls = joiner.skipNulls();
        Iterable<Object> iterable = Arrays.asList(null, "1", null, "3");
        assertEquals("1:3", joinerSkippingNulls.join(iterable));
    }

    @Test
    public void testMapJoiner() throws IOException {
        Joiner joiner = Joiner.on(":");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", null);
        StringBuilder sb = new StringBuilder();
        mapJoiner.appendTo(sb, map.entrySet());
        assertEquals("key1=value1:key2=value2", sb.toString());
    }

    @Test
    public void testMapJoinerUseForNull() throws IOException {
        Joiner joiner = Joiner.on(":");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("=").useForNull("NULL");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", null);
        StringBuilder sb = new StringBuilder();
        mapJoiner.appendTo(sb, map.entrySet());
        assertEquals("key1=value1:key2=value2:NULL", sb.toString());
    }

    @Test
    public void testMapJoinerSkipNulls() throws IOException {
        Joiner joiner = Joiner.on(":");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("=").skipNulls();
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", null);
        map.put("key3", "value3");
        StringBuilder sb = new StringBuilder();
        mapJoiner.appendTo(sb, map.entrySet());
        assertEquals("key1=value1:key3=value3", sb.toString());
    }

}