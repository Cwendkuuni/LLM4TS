package com.google.common.base;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import java.util.*;

public class JoinerTest {

    @Test
    public void testJoinerOnString() {
        Joiner joiner = Joiner.on(":");
        List<String> list = Arrays.asList("foo", "bar", "baz");
        StringBuilder expected = new StringBuilder();
        expected.append("foo:bar:baz");

        StringBuilder result = joiner.appendTo(expected, list);
        assertEquals(expected.toString(), result.toString());

        String join = joiner.join(list);
        assertEquals(expected.toString(), join);
    }

    @Test
    public void testJoinerOnChar() {
        Joiner joiner = Joiner.on(':');
        List<String> list = Arrays.asList("foo", "bar", "baz");
        StringBuilder expected = new StringBuilder();
        expected.append("foo:bar:baz");

        StringBuilder result = joiner.appendTo(expected, list);
        assertEquals(expected.toString(), result.toString());

        String join = joiner.join(list);
        assertEquals(expected.toString(), join);
    }

    @Test
    public void testJoinStringArray() {
        Joiner joiner = Joiner.on(":");
        String[] array = new String[]{"foo", "bar", "baz"};

        StringBuilder expected = new StringBuilder();
        expected.append("foo:bar:baz");

        StringBuilder result = joiner.appendTo(new StringBuilder(), array);
        assertEquals(expected.toString(), result.toString());

        String join = joiner.join(array);
        assertEquals(expected.toString(), join);
    }

    @Test
    public void testJoinNullFirstAndSecond() {
        Joiner joiner = Joiner.on(":");

        String join = joiner.join(null, null, "one", "two", "three");
        assertNull(join);
    }

    @Test
    public void testJoinNullInArray() {
        Joiner joiner = Joiner.on(":");

        String join = joiner.join(new String[]{"foo", null, "bar", "baz"});
        assertEquals("foo:bar:baz", join);
    }

    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(":");
        List<String> list = Arrays.asList("foo", "bar", "baz");

        StringBuilder expected = new StringBuilder();
        expected.append("foo:bar:baz");

        StringBuilder result = joiner.appendTo(expected, list.iterator());
        assertEquals(expected.toString(), result.toString());

        String join = joiner.join(list);
        assertEquals(expected.toString(), join);
    }

    @Test
    public void testJoinUseForNull() {
        Joiner joiner = Joiner.on(":").useForNull("null");

        String join = joiner.join(new String[]{"foo", null, "bar", "baz"});
        assertEquals("foo:null:bar:baz", join);
    }

    @Test
    public void testJoinUseForNullAndSkipNulls() {
        Joiner joiner = Joiner.on(":").skipNulls().useForNull("null");

        String join = joiner.join(new String[]{"foo", null, "bar", "baz"});
        assertEquals("foo:bar:baz", join);
    }

    @Test
    public void testJoinMap() {
        Joiner joiner = Joiner.on(":");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        StringBuilder expected = new StringBuilder();
        expected.append("key1:value1:key2:value2");

        StringBuilder result = joiner.appendTo(expected, map.entrySet());
        assertEquals(expected.toString(), result.toString());

        String join = joiner.join(map.entrySet());
        assertEquals(expected.toString(), join);
    }

    @Test
    public void testJoinMapUseForNull() {
        Joiner joiner = Joiner.on(":").useForNull("null");
        Map<String, String> map = new HashMap<>();
        map.put("key1", null);
        map.put("key2", "value2");

        StringBuilder expected = new StringBuilder();
        expected.append("key1:null:key2:value2");

        StringBuilder result = joiner.appendTo(expected, map.entrySet());
        assertEquals(expected.toString(), result.toString());

        String join = joiner.join(map.entrySet());
        assertEquals(expected.toString(), join);
    }
}