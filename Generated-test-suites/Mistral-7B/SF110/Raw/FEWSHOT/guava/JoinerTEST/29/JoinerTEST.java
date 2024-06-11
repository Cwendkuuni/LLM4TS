package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class JoinerTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testJoinerConstruction() {
        Joiner joiner1 = Joiner.on(":");
        Joiner joiner2 = Joiner.on(',');
        Joiner joiner3 = new Joiner(";");
        Joiner joiner4 = new Joiner(joiner1);

        assertEquals(":", joiner1.separator);
        assertEquals(',', joiner2.separator);
        assertEquals(";", joiner3.separator);
        assertEquals(":", joiner4.separator);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAppendToIterable() throws IOException {
        Joiner joiner = Joiner.on(":");
        StringWriter writer = new StringWriter();

        joiner.appendTo(writer, Arrays.asList("foo", "bar", "baz"));

        assertEquals("foo:bar:baz", writer.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAppendToArray() throws IOException {
        Joiner joiner = Joiner.on(":");
        StringWriter writer = new StringWriter();

        joiner.appendTo(writer, new Object[] {"foo", "bar", "baz"});

        assertEquals("foo:bar:baz", writer.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAppendToObjectArray() throws IOException {
        Joiner joiner = Joiner.on(":");
        StringWriter writer = new StringWriter();

        joiner.appendTo(writer, new Object[] {"foo", null, "bar", "baz"});

        assertEquals("foo:null:bar:baz", writer.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAppendToIterableNullFirst() throws IOException {
        Joiner joiner = Joiner.on(":");
        StringWriter writer = new StringWriter();

        joiner.appendTo(writer, Collections.singletonList(null), ":", "foo", "bar", "baz");

        assertEquals(":foo:bar:baz", writer.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAppendToIterableEmpty() throws IOException {
        Joiner joiner = Joiner.on(":");
        StringWriter writer = new StringWriter();

        joiner.appendTo(writer, Collections.emptyList());

        assertEquals("", writer.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAppendToIterableNullLast() throws IOException {
        Joiner joiner = Joiner.on(":");
        StringWriter writer = new StringWriter();

        joiner.appendTo(writer, Arrays.asList("foo", "bar", null));

        assertEquals("foo:bar", writer.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAppendToIterator() throws IOException {
        Joiner joiner = Joiner.on(":");
        StringWriter writer = new StringWriter();

        joiner.appendTo(writer, Arrays.asList("foo", "bar", "baz").iterator());

        assertEquals("foo:bar:baz", writer.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAppendToIteratorEmpty() throws IOException {
        Joiner joiner = Joiner.on(":");
        StringWriter writer = new StringWriter();

        joiner.appendTo(writer, Collections.emptyList().iterator());

        assertEquals("", writer.toString());
    }

    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(":");

        assertEquals("foo:bar:baz", joiner.join(Arrays.asList("foo", "bar", "baz")));
    }

    @Test
    public void testJoinObjectArray() {
        Joiner joiner = Joiner.on(":");

        assertEquals("foo:bar:baz", joiner.join(new Object[] {"foo", "bar", "baz"}));
    }

    @Test
    public void testJoinObjectArrayNullFirst() {
        Joiner joiner = Joiner.on(":");

        assertEquals(":foo:bar:baz", joiner.join(new Object[] {null, "foo", "bar", "baz"}));
    }

    @Test
    public void testJoinObjectArrayEmpty() {
        Joiner joiner = Joiner.on(":");

        assertEquals("", joiner.join(new Object[0]));
    }

    @Test
    public void testJoinObjectArrayNullLast() {
        Joiner joiner = Joiner.on(":");

        assertEquals("foo:bar", joiner.join(new Object[] {"foo", "bar", null}));
    }

    @Test
    public void testJoinIterator() {
        Joiner joiner = Joiner.on(":");

        assertEquals("foo:bar:baz", joiner.join(Arrays.asList("foo", "bar", "baz").iterator()));
    }

    @Test
    public void testJoinIteratorEmpty() {
        Joiner joiner = Joiner.on(":");

        assertEquals("", joiner.join(Collections.emptyList().iterator()));
    }

    @Test
    public void testJoinNullText() {
        Joiner joiner = Joiner.on(":");
        Joiner joinerNullText = joiner.useForNull("N/A");

        assertEquals("foo:bar:N/A:baz", joinerNullText.join(Arrays.asList("foo", "bar", null, "baz")));
    }

    @Test
    public void testJoinUseForNull() {
        Joiner joiner = Joiner.on(":");
        Joiner joinerUseForNull = joiner.useForNull("N/A");

        assertEquals("foo:bar:N/A:baz", joinerUseForNull.join(Arrays.asList("foo", "bar", null, "baz")));
        assertEquals("N/A", joinerUseForNull.toString(null));
    }

    @Test
    public void testJoinUseForNullWithMap() {
        Joiner joiner = Joiner.on(":");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("=>");

        Map<String, String> map = new HashMap<>();
        map.put("foo", "bar");
        map.put("baz", null);

        assertEquals("foo:bar:=>:baz:N/A", mapJoiner.join(map.entrySet()));
    }

    @Test
    public void testJoinSkipNulls() {
        Joiner joiner = Joiner.on(":");
        Joiner joinerSkipNulls = joiner.skipNulls();

        assertEquals("foo:bar:baz", joinerSkipNulls.join(Arrays.asList("foo", null, "bar", "baz")));
    }

    @Test
    public void testJoinMap() {
        Joiner joiner = Joiner.on(":");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("=>");

        Map<String, String> map = new HashMap<>();
        map.put("foo", "bar");
        map.put("baz", null);

        assertEquals("foo=>bar:baz", mapJoiner.join(map));
    }

    @Test
    public void testJoinMapEmpty() {
        Joiner joiner = Joiner.on(":");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("=>");

        assertEquals("", mapJoiner.join(Collections.emptyMap()));
    }

    @Test
    public void testJoinMapNullKey() {
        Joiner joiner = Joiner.on(":");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("=>");

        Map<String, String> map = new HashMap<>();
        map.put(null, "bar");

        assertEquals("", mapJoiner.join(map));
    }

    @Test
    public void testJoinMapNullValue() {
        Joiner joiner = Joiner.on(":");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("=>");

        Map<String, String> map = new HashMap<>();
        map.put("foo", null);

        assertEquals("foo:=>", mapJoiner.join(map));
    }

    @Test
    public void testJoinMapNullKeyNullValue() {
        Joiner joiner = Joiner.on(":");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("=>");

        Map<String, String> map = new HashMap<>();
        map.put(null, null);

        assertEquals("", mapJoiner.join(map));
    }

    @Test
    public void testJoinMapUseForNull() {
        Joiner joiner = Joiner.on(":");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("=>");
        MapJoiner mapJoinerUseForNull = mapJoiner.useForNull("N/A");

        Map<String, String> map = new HashMap<>();
        map.put("foo", null);

        assertEquals("foo:=>:N/A", mapJoinerUseForNull.join(map));
    }

    @Test
    public void testJoinMapUseForNullWithMap() {
        Joiner joiner = Joiner.on(":");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("=>");
        MapJoiner mapJoinerUseForNull = mapJoiner.useForNull("N/A");

        Map<String, String> map = new HashMap<>();
        map.put("foo", null);

        assertEquals("foo:=>:N/A", mapJoinerUseForNull.join(map));
    }

    @Test
    public void testJoinMapSkipNulls() {
        Joiner joiner = Joiner.on(":");
        MapJoiner mapJoiner = joiner.skipNulls();

        Map<String, String> map = new HashMap<>();
        map.put("foo", null);
        map.put("bar", "baz");

        Set<Map.Entry<String, String>> entries = new HashSet<>(map.entrySet());
        Iterator<Map.Entry<String, String>> iterator = entries.iterator();

        assertEquals("bar:baz", mapJoiner.join(iterator));
    }

    @Test
    public void testJoinMapUseForNullWithMapSkipNulls() {
        Joiner joiner = Joiner.on(":");
        MapJoiner mapJoiner = joiner.skipNulls();
        MapJoiner mapJoinerUseForNull = mapJoiner.useForNull("N/A");

        Map<String, String> map = new HashMap<>();
        map.put("foo", null);
        map.put("bar", "baz");

        Set<Map.Entry<String, String>> entries = new HashSet<>(map.entrySet());
        Iterator<Map.Entry<String, String>> iterator = entries.iterator();

        assertEquals("bar:baz:=>:N/A", mapJoinerUseForNull.join(iterator));
    }
}