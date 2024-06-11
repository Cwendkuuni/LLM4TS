package com.google.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

public class JoinerTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testJoiner() {
        Joiner joiner = Joiner.on(",");

        List<String> list = Arrays.asList("apple", "banana", "cherry");
        StringBuilder expected = new StringBuilder();
        expected.append("apple").append(",").append("banana").append(",").append("cherry");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, list.iterator());
        assertEquals(expected.toString(), actual.toString());

        Object[] array = new Object[]{"apple", "banana", "cherry"};
        expected = new StringBuilder();
        expected.append("apple").append(",").append("banana").append(",").append("cherry");
        actual = new StringBuilder();
        joiner.appendTo(actual, array);
        assertEquals(expected.toString(), actual.toString());

        Map<String, String> map = new TreeMap<>();
        map.put("apple", "one");
        map.put("banana", "two");
        map.put("cherry", "three");

        StringBuilder mapExpected = new StringBuilder();
        mapExpected.append("apple=one").append(Joiner.ON).append("banana=two").append(Joiner.ON).append("cherry=three");
        StringBuilder mapActual = new StringBuilder();
        joiner.appendTo(mapActual, map.entrySet());
        assertEquals(mapExpected.toString(), mapActual.toString());

        Joiner mapJoiner = Joiner.on("=").useForNull("null");
        map.put(null, "value");
        mapExpected = new StringBuilder();
        mapExpected.append("null=value");
        mapActual = new StringBuilder();
        mapJoiner.appendTo(mapActual, map.entrySet());
        assertEquals(mapExpected.toString(), mapActual.toString());

        Joiner nullJoiner = Joiner.on(",").useForNull("null");
        nullJoiner.appendTo(actual, null);
        assertNull(actual.toString());

        List<String> emptyList = Collections.emptyList();
        actual = new StringBuilder();
        joiner.appendTo(actual, emptyList.iterator());
        assertEquals("", actual.toString());

        Iterator<String> emptyIterator = Collections.emptyIterator();
        actual = new StringBuilder();
        joiner.appendTo(actual, emptyIterator);
        assertEquals("", actual.toString());
    }

    @Test
    public void testMapJoiner() {
        Joiner joiner = Joiner.on("=").useForNull("null");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("=>");

        Map<String, String> map = new TreeMap<>();
        map.put("apple", "one");
        map.put("banana", "two");
        map.put("cherry", "three");

        StringBuilder expected = new StringBuilder();
        expected.append("apple=>one").append(Joiner.ON).append("banana=>two").append(Joiner.ON).append("cherry=>three");
        StringBuilder actual = new StringBuilder();
        mapJoiner.appendTo(actual, map);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMapJoinerWithSkipNulls() {
        Joiner joiner = Joiner.on("=").useForNull("null");
        MapJoiner mapJoiner = joiner.skipNulls();
        Map<String, String> map = new TreeMap<>();
        map.put("apple", "one");
        map.put(null, null);
        map.put("banana", "two");

        String expected = "apple=one";
        StringBuilder actual = new StringBuilder();
        mapJoiner.appendTo(actual, map.entrySet());
        assertEquals(expected, actual.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMapJoinerWithUseForNull() {
        Joiner joiner = Joiner.on("=").skipNulls();
        MapJoiner mapJoiner = joiner.useForNull("null");
        Map<String, String> map = new TreeMap<>();
        map.put("apple", "one");
        map.put("banana", null);
        map.put("cherry", "three");

        String expected = "apple=one,cherry=three";
        StringBuilder actual = new StringBuilder();
        mapJoiner.appendTo(actual, map.entrySet());
        assertEquals(expected, actual.toString());
    }
}