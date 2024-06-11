package com.google.common.base;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import java.util.*;

import org.junit.Test;

public class JoinerTest {

    @Test
    public void testJoinWithStringSeparator() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.join(Arrays.asList("a", "b", "c")));
    }

    @Test
    public void testJoinWithCharSeparator() {
        Joiner joiner = Joiner.on('-');
        assertEquals("a-b-c", joiner.join(Arrays.asList("a", "b", "c")));
    }

    @Test
    public void testAppendingToAppendableWithIterable() throws IOException {
        Joiner joiner = Joiner.on(":");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, Arrays.asList("a", "b", "c"));
        assertEquals("a:b:c", actual.toString());
    }

    @Test
    public void testAppendingToAppendableWithArray() throws IOException {
        Joiner joiner = Joiner.on("|");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, new Object[]{"a", "b", "c"});
        assertEquals("a|b|c", actual.toString());
    }

    @Test
    public void testAppendingToAppendingNullWithIterable() throws IOException {
        Joiner joiner = Joiner.on(";");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, Arrays.asList(null, "b", "c"));
        assertEquals(";b;c", actual.toString());
    }

    @Test
    public void testAppendingToAppendingNullWithArray() throws IOException {
        Joiner joiner = Joiner.on("!");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, new Object[]{null, "b", "c"});
        assertEquals("!b!c", actual.toString());
    }

    @Test
    public void testJoinWithNullsSkipped() {
        Joiner joiner = Joiner.on("&").skipNulls();
        assertEquals("a&b&c", joiner.join(Arrays.asList("a", null, "b", null, "c")));
    }

    @Test
    public void testUseForNullWithStringText() {
        Joiner joiner = Joiner.on("*").useForNull("NA");
        assertEquals("a*NA*b*NA*c", joiner.join(Arrays.asList("a", null, "b", null, "c")));
    }

    @Test
    public void testUseForNullWithCharText() {
        Joiner joiner = Joiner.on('%').useForNull('X');
        assertEquals("a%X%b%X%c", joiner.join(Arrays.asList("a", null, "b", null, "c")));
    }

    @Test
    public void testMapJoinerWithStringKeyValueSeparator() {
        Joiner joiner = Joiner.on(":");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", null);
        map.put("key3", "value3");
        assertEquals("key1:value1=key2:NA=key3:value3", mapJoiner.join(map));
    }

    @Test
    public void testMapJoinerWithCharKeyValueSeparator() {
        Joiner joiner = Joiner.on(';');
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(':');
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", null);
        map.put("key3", "value3");
        assertEquals("key1;key1:value1=key2;key2:NA=key3;key3:value3", mapJoiner.join(map));
    }
}