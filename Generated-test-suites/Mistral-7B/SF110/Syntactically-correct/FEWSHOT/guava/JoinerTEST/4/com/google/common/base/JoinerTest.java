package com.google.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class JoinerTest {

    // Test Joiner construction and basic functionality
    @Test
    public void testJoinerConstruction() {
        Joiner joiner = Joiner.on(",");
        assertEquals(",", joiner.separator);
    }

    @Test
    public void testJoinerAppendingToStringBuilder() {
        Joiner joiner = Joiner.on(",");
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, Arrays.asList("foo", "bar", "baz"));
        assertEquals("foo,bar,baz", sb.toString());
    }

    @Test
    public void testJoinerAppendingToAppendable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringJoiner sj = StringJoiner.on(",");
        StringWriter sw = new StringWriter(sj);
        joiner.appendTo(sw, Arrays.asList("foo", "bar", "baz"));
        assertEquals("foo,bar,baz", sw.toString());
    }

    // Test Joiner appendTo on Iterable and Iterator
    @Test
    public void testJoinerAppendToIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter sw = new StringWriter();
        joiner.appendTo(sw, Arrays.asList("foo", "bar", "baz"));
        assertEquals("foo,bar,baz", sw.toString());
    }

    @Test
    public void testJoinerAppendToIterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter sw = new StringWriter();
        Iterator<String> iter = Arrays.asList("foo", "bar", "baz").iterator();
        joiner.appendTo(sw, iter);
        assertEquals("foo,bar,baz", sw.toString());
    }

    // Test Joiner appendTo on Object[]
    @Test
    public void testJoinerAppendToObjectArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter sw = new StringWriter();
        joiner.appendTo(sw, new String[]{"foo", "bar", "baz"});
        assertEquals("foo,bar,baz", sw.toString());
    }

    // Test Joiner appendTo on null, single, and multiple elements
    @Test
    public void testJoinerAppendToNull() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter sw = new StringWriter();
        joiner.appendTo(sw, null);
        assertNull(sw.toString());
    }

    @Test
    public void testJoinerAppendToSingleElement() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter sw = new StringWriter();
        joiner.appendTo(sw, Arrays.asList("foo"));
        assertEquals("foo", sw.toString());
    }

    @Test
    public void testJoinerAppendToMultipleElements() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter sw = new StringWriter();
        joiner.appendTo(sw, Arrays.asList("foo", "bar", "baz"));
        assertEquals("foo,bar,baz", sw.toString());
    }

    // Test Joiner join on Iterable and Iterator
    @Test
    public void testJoinerJoinIterable() {
        Joiner joiner = Joiner.on(",");
        assertEquals("foo,bar,baz", joiner.join(Arrays.asList("foo", "bar", "baz")));
    }

    @Test
    public void testJoinerJoinIterator() {
        Joiner joiner = Joiner.on(",");
        Iterator<String> iter = Arrays.asList("foo", "bar", "baz").iterator();
        assertEquals("foo,bar,baz", joiner.join(iter));
    }

    // Test Joiner join on Object[]
    @Test
    public void testJoinerJoinObjectArray() {
        Joiner joiner = Joiner.on(",");
        assertEquals("foo,bar,baz", joiner.join(new String[]{"foo", "bar", "baz"}));
    }

    // Test Joiner join on null, single, and multiple elements
    @Test
    public void testJoinerJoinNull() {
        Joiner joiner = Joiner.on(",");
        assertEquals("", joiner.join(null));
    }

    @Test
    public void testJoinerJoinSingleElement() {
        Joiner joiner = Joiner.on(",");
        assertEquals("foo", joiner.join(Arrays.asList("foo")));
    }

    @Test
    public void testJoinerJoinMultipleElements() {
        Joiner joiner = Joiner.on(",");
        assertEquals("foo,bar,baz", joiner.join(Arrays.asList("foo", "bar", "baz")));
    }

    // Test Joiner useForNull
    @Test
    public void testJoinerUseForNull() {
        Joiner joiner = Joiner.on(",");
        assertEquals("null,foo,bar,baz", joiner.useForNull("null").join(Arrays.asList(null, "foo", "bar", "baz")));
    }

    // Test Joiner skipNulls
    @Test
    public void testJoinerSkipNulls() {
        Joiner joiner = Joiner.on(",");
        assertEquals("foo,bar,baz", joiner.skipNulls().join(Arrays.asList(null, "foo", "bar", "baz")));
    }

    // Test MapJoiner construction and basic functionality
    @Test
    public void testMapJoinerConstruction() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        assertEquals(",", joiner.separator);
        assertEquals(":", mapJoiner.keyValueSeparator);
    }

    @Test
    public void testMapJoinerAppendingToStringBuilder() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        StringBuilder sb = new StringBuilder();
        mapJoiner.appendTo(sb, new HashMap<String, String>(){{
            put("foo", "bar");
            put("baz", "qux");
        }});
        assertEquals("foo:bar,baz:qux", sb.toString());
    }

    @Test
    public void testMapJoinerAppendingToAppendable() throws IOException {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        StringJoiner sj = StringJoiner.on(",");
        StringWriter sw = new StringWriter(sj);
        mapJoiner.appendTo(sw, new HashMap<String, String>(){{
            put("foo", "bar");
            put("baz", "qux");
        }});
        assertEquals("foo:bar,baz:qux", sw.toString());
    }

    // Test MapJoiner appendTo on Map.Entry Iterator and Iterable
    @Test
    public void testMapJoinerAppendToMapEntryIterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        StringWriter sw = new StringWriter();
        Iterator<Map.Entry<String, String>> it = new HashMap<String, String>(){{
            put("foo", "bar");
            put("baz", "qux");
        }}.entrySet().iterator();
        mapJoiner.appendTo(sw, it);
        assertEquals("foo:bar,baz:qux", sw.toString());
    }

    @Test
    public void testMapJoinerAppendToMapEntryIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        StringWriter sw = new StringWriter();
        mapJoiner.appendTo(sw, new HashMap<String, String>(){{
            put("foo", "bar");
            put("baz", "qux");
        }}.entrySet());
        assertEquals("foo:bar,baz:qux", sw.toString());
    }

    // Test mapJoiner join on Map.Entry Iterator and Iterable
    @Test
    public void testMapJoinerJoinMapEntryIterator() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        assertEquals("foo:bar,baz:qux", mapJoiner.join(new HashMap<String, String>(){{
            put("foo", "bar");
            put("baz", "qux");
        }}.entrySet().iterator()));
    }

    @Test
    public void testMapJoinerJoinMapEntryIterable() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        assertEquals("foo:bar,baz:qux", mapJoiner.join(new HashMap<String, String>(){{
            put("foo", "bar");
            put("baz", "qux");
        }}.entrySet()));
    }

    // Test MapJoiner useForNull and skipNulls
    @Test
    public void testMapJoinerUseForNull() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        Map<String, String> map = new HashMap<String, String>(){{
            put("foo", "bar");
            put(null, null);
            put("baz", "qux");
        }};
        assertEquals("null:null,foo:bar,baz:qux", mapJoiner.useForNull("null:null").join(map.entrySet()));
    }

    @Test
    public void testMapJoinerSkipNulls() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        Map<String, String> map = new HashMap<String, String>(){{
            put("foo", "bar");
            put(null, null);
            put("baz", "qux");
        }};
        assertEquals("foo:bar,baz:qux", mapJoiner.skipNulls().join(map.entrySet()));
    }
}