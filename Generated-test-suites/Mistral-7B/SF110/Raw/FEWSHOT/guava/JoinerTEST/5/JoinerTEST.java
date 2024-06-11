package com.google.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class JoinerTest {

    @Test
    public void testJoinerOnString() {
        Joiner joiner = Joiner.on(":");
        String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
        assertEquals("apple:banana:cherry", result);
    }

    @Test
    public void testJoinerOnChar() {
        Joiner joiner = Joiner.on(';');
        String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
        assertEquals("apple;banana;cherry", result);
    }

    @Test
    public void testAppendToIterable() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Joiner joiner = Joiner.on(";");
        joiner.appendTo(stringBuilder, Arrays.asList("apple", "banana", "cherry"));
        assertEquals("apple;banana;cherry", stringBuilder.toString());
    }

    @Test
    public void testAppendToArray() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Joiner joiner = Joiner.on(";");
        joiner.appendTo(stringBuilder, new Object[] {"apple", "banana", "cherry"});
        assertEquals("apple;banana;cherry", stringBuilder.toString());
    }

    @Test
    public void testAppendToObject() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Joiner joiner = Joiner.on(";");
        joiner.appendTo(stringBuilder, "apple", "banana", "cherry");
        assertEquals("apple;banana;cherry", stringBuilder.toString());
    }

    @Test
    public void testAppendToIterableStringBuilder() {
        StringBuilder stringBuilder = new StringBuilder();
        Joiner joiner = Joiner.on(";");
        joiner.appendTo(stringBuilder, Arrays.asList("apple", "banana", "cherry"));
        assertEquals("apple;banana;cherry", stringBuilder.toString());
    }

    @Test
    public void testAppendToArrayStringBuilder() {
        StringBuilder stringBuilder = new StringBuilder();
        Joiner joiner = Joiner.on(";");
        joiner.appendTo(stringBuilder, new Object[] {"apple", "banana", "cherry"});
        assertEquals("apple;banana;cherry", stringBuilder.toString());
    }

    @Test
    public void testAppendToObjectStringBuilder() {
        StringBuilder stringBuilder = new StringBuilder();
        Joiner joiner = Joiner.on(";");
        joiner.appendTo(stringBuilder, "apple", "banana", "cherry");
        assertEquals("apple;banana;cherry", stringBuilder.toString());
    }

    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(";");
        String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
        assertEquals("apple;banana;cherry", result);
    }

    @Test
    public void testJoinArray() {
        Joiner joiner = Joiner.on(";");
        String result = joiner.join(new Object[] {"apple", "banana", "cherry"});
        assertEquals("apple;banana;cherry", result);
    }

    @Test
    public void testJoinObject() {
        Joiner joiner = Joiner.on(";");
        String result = joiner.join("apple", "banana", "cherry");
        assertEquals("apple;banana;cherry", result);
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(":").useForNull("N/A");
        String result = joiner.join(Arrays.asList("apple", null, "cherry"));
        assertEquals("apple:N/A:cherry", result);
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(":").skipNulls();
        String result = joiner.join(Arrays.asList("apple", null, "cherry"));
        assertEquals("apple", result);
    }

    @Test
    public void testMapJoinerWithKeyValueSeparator() {
        Joiner joiner = Joiner.on("=");
        Map<String, String> map = new HashMap<>();
        map.put("name", "John Doe");
        map.put("age", "30");
        map.put("city", null);

        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        String result = mapJoiner.join(map);
        assertEquals("name:John Doe:age:30", result);
    }
}