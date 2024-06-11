package com.google.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class JoinerTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testJoiner() {
        Joiner joiner = Joiner.on(",");
        StringBuilder stringBuilder = new StringBuilder();

        joiner.appendTo(stringBuilder, joiner.useForNull("N/A"), Arrays.asList("Apples", "Oranges", "Bananas"));
        assertEquals("Apples, Oranges, Bananas", stringBuilder.toString());

        joiner = Joiner.on(" -> ");

        stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, joiner.skipNulls(), Arrays.asList("Apples", null, "Oranges", "Bananas"));
        assertEquals("Apples -> Oranges -> Bananas", stringBuilder.toString());

        stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, joiner.skipNulls(), Arrays.asList("Apples", "Oranges", null, "Bananas"));
        assertEquals("Apples -> Oranges -> Bananas", stringBuilder.toString());

        stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, joiner.skipNulls(), new HashSet<String>() {{
            add("Apples");
            add(null);
            add("Oranges");
            add("Bananas");
        }}.toArray(new String[0]));
        assertEquals("Apples -> Oranges -> Bananas", stringBuilder.toString());

        stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, joiner.skipNulls(), new HashMap<String, String>() {{
            put("Apples", "Fruit");
            put(null, null);
            put("Oranges", "Fruit");
            put("Bananas", "Fruit");
        }}.entrySet().toArray(new Map.Entry[0][]));
        assertEquals("Apples -> Oranges -> Bananas", stringBuilder.toString());

        stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, joiner.skipNulls(), Arrays.asList("Apples", "Oranges", "Bananas").toArray(new String[0])).append(" -> ").append("Last");
        assertEquals("Apples -> Oranges -> Bananas -> Last", stringBuilder.toString());

        stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, joiner.useForNull("N/A"), Arrays.asList("Apples", null, "Oranges", "Bananas").toArray(new String[0])).append(" -> ").append("Last");
        assertEquals("Apples -> N/A -> Oranges -> Bananas -> Last", stringBuilder.toString());

        stringBuilder = new StringBuilder();
        try {
            joiner.appendTo(stringBuilder, joiner.skipNulls(), Arrays.asList(null).toArray(new String[0]));
            fail();
        } catch (NullPointerException e) {
            // expected
        }

        stringBuilder = new StringBuilder();
        try {
            joiner.appendTo(stringBuilder, joiner.useForNull("N/A"), Arrays.asList(null).toArray(new String[0]));
            assertEquals("N/A", stringBuilder.toString());
        } catch (NullPointerException e) {
            fail();
        }
    }

    @Test
    public void testMapJoiner() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(": ");

        Map<String, String> map = new HashMap<>();
        map.put("Apples", "Fruit");
        map.put("Oranges", "Fruit");
        map.put("Bananas", null);

        StringWriter writer = new StringWriter();
        mapJoiner.appendTo(writer, map);
        assertEquals("Apples: Fruit, Oranges: Fruit, Bananas: null", writer.toString());

        mapJoiner = joiner.useForNull("N/A").withKeyValueSeparator(": ");

        writer = new StringWriter();
        mapJoiner.appendTo(writer, map);
        assertEquals("Apples: Fruit, Oranges: Fruit, Bananas: N/A", writer.toString());

        writer = new StringWriter();
        mapJoiner.appendTo(writer, map, Arrays.asList("Key1", "Key2", "Key3"));
        assertEquals("Key1: Fruit, Key2: Fruit, Key3: Fruit, Apples: Fruit, Oranges: Fruit, Bananas: N/A", writer.toString());
    }
}