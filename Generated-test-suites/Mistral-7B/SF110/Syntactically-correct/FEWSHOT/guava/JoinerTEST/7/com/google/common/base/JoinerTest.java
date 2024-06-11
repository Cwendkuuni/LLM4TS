package com.google.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;

public class JoinerTest {

    @Test
    public void testJoinerOnString() {
        Joiner joiner = Joiner.on(",");
        StringBuilder expected = new StringBuilder();
        expected.append("a").append(joiner.separator).append("b").append(joiner.separator).append("c");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, Arrays.asList("a", "b", "c"));
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testJoinerOnChar() {
        Joiner joiner = Joiner.on('-');
        StringBuilder expected = new StringBuilder();
        expected.append("a").append(joiner.separator).append("b").append(joiner.separator).append("c");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, Arrays.asList("a", "b", "c"));
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testAppendToWithIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringJoiner expected = new StringJoiner(",", "", "");
        expected.add("a");
        expected.add("b");
        expected.add("c");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, Arrays.asList("a", "b", "c"));
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testAppendToWithArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder expected = new StringBuilder();
        expected.append("a").append(joiner.separator).append("b").append(joiner.separator).append("c");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, new Object[]{"a", "b", "c"});
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testAppendToWithSingleElement() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder expected = new StringBuilder();
        expected.append("a");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, Collections.singletonList("a"));
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testAppendToWithNull() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder expected = new StringBuilder();
        expected.append("null");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, (Iterable<?>) Arrays.asList((Object) null));
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",");
        Joiner nullJoiner = joiner.useForNull("NA");
        StringBuilder expected = new StringBuilder();
        expected.append("a,NA");
        StringBuilder actual = new StringBuilder();
        nullJoiner.appendTo(actual, Arrays.asList("a", null));
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",");
        Joiner nullJoiner = joiner.skipNulls();
        StringBuilder expected = new StringBuilder();
        expected.append("a,b");
        StringBuilder actual = new StringBuilder();
        nullJoiner.appendTo(actual, Arrays.asList("a", null, "b"));
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testMapJoiner() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", null);
        StringBuilder expected = new StringBuilder();
        expected.append("key1:value1,").append(joiner.separator).append("key2:value2");
        StringBuilder actual = new StringBuilder();
        mapJoiner.appendTo(actual, map);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testMapJoinerWithNullValue() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", null);
        StringBuilder expected = new StringBuilder();
        expected.append("key1:value1,").append(joiner.separator).append("key2:null");
        StringBuilder actual = new StringBuilder();
        mapJoiner.appendTo(actual, map);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMapJoinerWithUseForNull() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        mapJoiner.useForNull("NA");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMapJoinerWithSkipNulls() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        mapJoiner.skipNulls();
    }
}