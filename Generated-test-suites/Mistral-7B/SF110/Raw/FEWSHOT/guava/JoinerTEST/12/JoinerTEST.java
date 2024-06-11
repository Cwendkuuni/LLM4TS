package com.google.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class JoinerTest {

    @SuppressWarnings("unchecked")
    @Test
    public <T> void testAppendToIterable() throws IOException {
        Joiner joiner = Joiner.on(":");
        StringBuilder appendable = new StringBuilder();

        List<String> parts = Arrays.asList("one", "two", "three");
        joiner.appendTo(appendable, parts);
        assertEquals("one:two:three", appendable.toString());

        joiner = Joiner.on("|");
        appendable = new StringBuilder();
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        joiner.appendTo(appendable, map.entrySet());
        assertEquals("key1|value1:key2|value2", appendable.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public <T> void testAppendToObject() throws IOException {
        Joiner joiner = Joiner.on(":");
        StringWriter appendable = new StringWriter();

        Object[] parts = new Object[]{"one", "two", "three"};
        joiner.appendTo(appendable, parts);
        assertEquals("one:two:three", appendable.toString());

        joiner = Joiner.on("|");
        appendable = new StringWriter();
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        joiner.appendTo(appendable, map);
        assertEquals("key1|value1:key2|value2", appendable.toString());
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on("#");
        assertEquals("#", joiner.useForNull(null).toString(null));
        assertEquals("one#two#three", joiner.useForNull(null).appendTo(new StringBuilder(), Arrays.asList("one", "two", "three")).toString());
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",");
        StringBuilder appendable = new StringBuilder();
        Object[] parts = new Object[]{"one", null, "three"};
        joiner.appendTo(appendable, parts);
        assertEquals("one,three", appendable.toString());

        joiner = Joiner.on("|");
        StringWriter appendable2 = new StringWriter();
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put(null, null);
        map.put("key3", "value3");
        joiner.appendTo(appendable2, map.entrySet());
        assertEquals("key1=value1|key3=value3", appendable2.toString());
    }

    @Test
    public void testMapJoiner() {
        Joiner joiner = Joiner.on(":");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");

        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");

        StringBuilder appendable = new StringBuilder();
        mapJoiner.appendTo(appendable, map);
        assertEquals("key1=value1:key2=value2:key3=value3", appendable.toString());
    }

    @Test
    public void testIterableJoinerNullParts() {
        Joiner joiner = Joiner.on("#");

        Iterator<Object> parts = null;
        StringBuilder appendable = new StringBuilder();
        joiner.appendTo(appendable, parts);
        assertEquals("", appendable.toString());
    }

    @Test
    public void testIterableJoinerEmptyParts() {
        Joiner joiner = Joiner.on("#");

        Iterator<Object> parts = Collections.emptyIterator();
        StringBuilder appendable = new StringBuilder();
        joiner.appendTo(appendable, parts);
        assertEquals("", appendable.toString());
    }

    @Test
    public void testIterableJoinerSinglePart() {
        Joiner joiner = Joiner.on("#");

        Iterator<Object> parts = Arrays.asList("one").iterator();
        StringBuilder appendable = new StringBuilder();
        joiner.appendTo(appendable, parts);
        assertEquals("one", appendable.toString());
    }

    @Test
    public void testIterableJoinerMultipleParts() {
        Joiner joiner = Joiner.on("#");

        Iterator<Object> parts = Arrays.asList("one", "two", "three").iterator();
        StringBuilder appendable = new StringBuilder();
        joiner.appendTo(appendable, parts);
        assertEquals("one#two#three", appendable.toString());
    }

    @Test
    public void testObjectJoinerNullParts() {
        Joiner joiner = Joiner.on("#");

        Object[] parts = new Object[]{null};
        StringBuilder appendable = new StringBuilder();
        joiner.appendTo(appendable, parts);
        assertEquals("", appendable.toString());
    }

    @Test
    public void testObjectJoinerEmptyParts() {
        Joiner joiner = Joiner.on("#");

        Object[] parts = new Object[]{};
        StringBuilder appendable = new StringBuilder();
        joiner.appendTo(appendable, parts);
        assertEquals("", appendable.toString());
    }

    @Test
    public void testObjectJoinerSinglePart() {
        Joiner joiner = Joiner.on("#");

        Object[] parts = new Object[]{"one"};
        StringBuilder appendable = new StringBuilder();
        joiner.appendTo(appendable, parts);
        assertEquals("one", appendable.toString());
    }

    @Test
    public void testObjectJoinerMultipleParts() {
        Joiner joiner = Joiner.on("#");

        Object[] parts = new Object[]{"one", "two", "three"};
        StringBuilder appendable = new StringBuilder();
        joiner.appendTo(appendable, parts);
        assertEquals("one#two#three", appendable.toString());
    }

    @Test
    public void testToStringNullPart() {
        Joiner joiner = Joiner.on("#");

        Object part = null;
        assertEquals(null, joiner.toString(part));
    }

    @Test
    public void testToStringNonStringPart() {
        Joiner joiner = Joiner.on("#");

        Integer part = 1;
        assertEquals(part.toString(), joiner.toString(part));
    }

    @Test
    public void testToStringStringPart() {
        Joiner joiner = Joiner.on("#");

        String part = "one";
        assertEquals(part, joiner.toString(part));
    }
}