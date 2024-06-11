package com.google.common.base;

import static com.google.common.base.Joiner.Util.iterable;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class JoinerTest {

    @Test
    public void testJoinerOnString() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
        assertEquals("apple,banana,cherry", result);
    }

    @Test
    public void testJoinerOnChar() {
        Joiner joiner = Joiner.on('-');
        String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
        assertEquals("apple-banana-cherry", result);
    }

    @Test
    public void testAppendToIterable() throws IOException {
        StringBuilder expected = new StringBuilder();
        expected.append("apple");
        expected.append(Joiner.ON);
        expected.append("banana");
        expected.append(Joiner.ON);
        expected.append("cherry");

        StringBuilder actual = new StringBuilder();
        Joiner joiner = Joiner.on(",");
        joiner.appendTo(actual, Arrays.asList("apple", "banana", "cherry"));

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testAppendToArray() throws IOException {
        StringBuilder expected = new StringBuilder();
        expected.append("apple");
        expected.append(Joiner.ON);
        expected.append("banana");
        expected.append(Joiner.ON);
        expected.append("cherry");

        StringBuilder actual = new StringBuilder();
        Joiner joiner = Joiner.on(",");
        joiner.appendTo(actual, new Object[]{"apple", "banana", "cherry"});

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testAppendToNullFirst() throws IOException {
        StringBuilder expected = new StringBuilder();
        expected.append("null");
        expected.append(Joiner.ON);
        expected.append("apple");
        expected.append(Joiner.ON);
        expected.append("cherry");

        StringBuilder actual = new StringBuilder();
        Joiner joiner = Joiner.on(",");
        joiner.appendTo(actual, null, "apple", "cherry");

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testAppendToNullSecond() throws IOException {
        StringBuilder expected = new StringBuilder();
        expected.append("apple");
        expected.append(Joiner.ON);
        expected.append("null");
        expected.append(Joiner.ON);
        expected.append("cherry");

        StringBuilder actual = new StringBuilder();
        Joiner joiner = Joiner.on(",");
        joiner.appendTo(actual, "apple", null, "cherry");

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testAppendToNullAndOther() throws IOException {
        StringBuilder expected = new StringBuilder();
        expected.append("null");
        expected.append(Joiner.ON);
        expected.append("apple");
        expected.append(Joiner.ON);
        expected.append("null");
        expected.append(Joiner.ON);
        expected.append("cherry");

        StringBuilder actual = new StringBuilder();
        Joiner joiner = Joiner.on(",");
        joiner.appendTo(actual, null, "apple", null, "cherry");

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testJoinIterable() {
        String result = Joiner.on(",").join(Arrays.asList("apple", "banana", "cherry"));
        assertEquals("apple,banana,cherry", result);
    }

    @Test
    public void testJoinArray() {
        String result = Joiner.on(",").join(new Object[]{"apple", "banana", "cherry"});
        assertEquals("apple,banana,cherry", result);
    }

    @Test
    public void testJoinNullFirst() {
        String result = Joiner.on(",").join(new Object[]{"null", "apple", "cherry"});
        assertEquals("null,apple,cherry", result);
    }

    @Test
    public void testJoinNullSecond() {
        String result = Joiner.on(",").join(new Object[]{"apple", "null", "cherry"});
        assertEquals("apple,null,cherry", result);
    }

    @Test
    public void testJoinNullAndOther() {
        String result = Joiner.on(",").join(new Object[]{"null", "apple", "null", "cherry"});
        assertEquals("null,apple,null,cherry", result);
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("N/A");
        String result = joiner.join(new Object[]{"null", "apple", "cherry"});
        assertEquals("N/A,apple,cherry", result);
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        String result = joiner.join(new Object[]{"null", "apple", "null", "cherry"});
        assertEquals("apple,cherry", result);
    }

    @Test
    public void testMapJoiner() {
        Map<String, String> map = new HashMap<>();
        map.put("apple", "red");
        map.put("banana", "yellow");
        map.put("cherry", "red");

        Joiner joiner = Joiner.on(" -> ");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(" -> ");

        StringBuilder expected = new StringBuilder();
        expected.append("apple -> red");
        expected.append(Joiner.ON);
        expected.append("banana -> yellow");
        expected.append(Joiner.ON);
        expected.append("cherry -> red");

        StringBuilder actual = new StringBuilder();
        mapJoiner.appendTo(actual, map);

        assertEquals(expected.toString(), actual.toString());

        String result = mapJoiner.join(map);
        assertEquals("apple -> red" + Joiner.ON + "banana -> yellow" + Joiner.ON + "cherry -> red", result);
    }
}