package com.google.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.junit.Test;

public class JoinerTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testJoinerOnString() {
        Joiner joiner = Joiner.on(",");
        List<String> list = Arrays.asList("apple", "banana", "cherry");
        StringBuilder expected = new StringBuilder();
        expected.append("apple").append(joiner.separator).append("banana").append(joiner.separator).append("cherry");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, list);
        assertEquals(expected.toString(), actual.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testJoinerOnChar() {
        Joiner joiner = Joiner.on('-');
        List<String> list = Arrays.asList("apple", "banana", "cherry");
        StringBuilder expected = new StringBuilder();
        expected.append("apple").append(joiner.separator).append("banana").append(joiner.separator).append("cherry");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, list);
        assertEquals(expected.toString(), actual.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testJoinerOnIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        List<String> list = Arrays.asList("apple", "banana", "cherry");
        StringBuilder expected = new StringBuilder();
        expected.append("apple").append(joiner.separator).append("banana").append(joiner.separator).append("cherry");
        StringJoiner stringJoiner = StringJoiner.on(",");
        String actual = stringJoiner.join(list);
        assertEquals(expected.toString(), actual);

        StringJoiner stringJoiner2 = Joiner.on(",").appendTo(new StringBuilder(), list);
        assertEquals(expected.toString(), stringJoiner2.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testJoinerOnArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        Object[] array = new Object[] {"apple", "banana", "cherry"};
        StringBuilder expected = new StringBuilder();
        expected.append("apple").append(joiner.separator).append("banana").append(joiner.separator).append("cherry");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, array);
        assertEquals(expected.toString(), actual.toString());

        StringJoiner stringJoiner = Joiner.on(",").appendTo(new StringBuilder(), Arrays.asList(array));
        assertEquals(expected.toString(), stringJoiner.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testJoinerOnIterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        List<String> list = Arrays.asList("apple", "banana", "cherry");
        Iterator<String> iterator = list.iterator();
        StringBuilder expected = new StringBuilder();
        expected.append("apple").append(joiner.separator).append("banana").append(joiner.separator).append("cherry");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, iterator);
        assertEquals(expected.toString(), actual.toString());

        StringJoiner stringJoiner = Joiner.on(",").appendTo(new StringBuilder(), list.iterator());
        assertEquals(expected.toString(), stringJoiner.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testJoinerUseForNull() {
        Joiner joiner = Joiner.on(",");
        joiner = joiner.useForNull("N/A");
        Object[] array = new Object[] {"apple", null, "cherry"};
        StringBuilder expected = new StringBuilder();
        expected.append("apple").append(joiner.separator).append("N/A").append(joiner.separator).append("cherry");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, array);
        assertEquals(expected.toString(), actual
.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testJoinerSkipNulls() {
        Joiner joiner = Joiner.on(",");
        joiner = joiner.skipNulls();
        Object[] array = new Object[] {"apple", null, "cherry"};
        StringBuilder expected = new StringBuilder();
        expected.append("apple").append(joiner.separator).append("cherry");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, array);
        assertEquals(expected.toString(), actual.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testJoinerMap() throws IOException {
        Joiner joiner = Joiner.on(" -> ");
        Map<String, String> map = new HashMap<>();
        map.put("apple", "fruit");
        map.put("banana", "fruit");
        map.put("cherry", null);
        StringBuilder expected = new StringBuilder();
        expected.append("apple -> fruit").append(joiner.separator).append("banana -> fruit");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, map.entrySet());
        assertEquals(expected.toString(), actual.toString());

        StringJoiner stringJoiner = Joiner.on(" -> ").appendTo(new StringBuilder(), map.entrySet());
        assertEquals(expected.toString(), stringJoiner.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testJoinerMapUseForNull() {
        Joiner joiner = Joiner.on(" -> ");
        joiner = joiner.useForNull("N/A");
        Map<String, String> map = new HashMap<>();
        map.put("apple", "fruit");
        map.put("banana", null);
        map.put("cherry", "fruit");
        StringBuilder expected = new StringBuilder();
        expected.append("apple -> fruit").append(joiner.separator).append("N/A").append(joiner.separator).append("cherry -> fruit");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, map.entrySet());
        assertEquals(expected.toString(), actual.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testJoinerMapSkipNulls() {
        Joiner joiner = Joiner.on(" -> ");
        joiner = joiner.skipNulls();
        Map<String, String> map = new HashMap<>();
        map.put("apple", "fruit");
        map.put("banana", null);
        map.put("cherry", "fruit");
        StringBuilder expected = new StringBuilder();
        expected.append("apple -> fruit").append(joiner.separator).append("cherry -> fruit");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, map.entrySet());
        assertEquals(expected.toString(), actual.toString());
    }
}