package com.google.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class JoinerTest {

    @Test
    public void testJoinerCreation() {
        Joiner joiner = Joiner.on(",");
        assertEquals(",", joiner.separator);

        joiner = Joiner.on("--");
        assertEquals("--", joiner.separator);

        joiner = new Joiner(":");
        assertEquals(":", joiner.separator);

        Joiner joinerPrototype = new Joiner(",");
        Joiner joinerClone = new Joiner(joinerPrototype);
        assertEquals(",", joinerClone.separator);
    }

    @Test
    public void testAppendToIterable() throws IOException {
        StringWriter appendable = new StringWriter();
        Joiner joiner = Joiner.on(",");
        Iterable<String> parts = Arrays.asList("apple", "banana", "cherry");
        joiner.appendTo(appendable, parts);
        assertEquals("apple,banana,cherry", appendable.toString());
    }

    @Test
    public void testAppendToObjectArray() throws IOException {
        StringWriter appendable = new StringWriter();
        Joiner joiner = Joiner.on(" ");
        Object[] parts = {"apple", "banana", "cherry"};
        joiner.appendTo(appendable, parts);
        assertEquals("apple banana cherry", appendable.toString());
    }

    @Test
    public void testAppendToNullFirst() throws IOException {
        StringWriter appendable = new StringWriter();
        Joiner joiner = Joiner.on(":");
        joiner.appendTo(appendable, null, "apple", "banana", "cherry");
        assertEquals(":apple:banana:cherry", appendable.toString());
    }

    @Test
    public void testAppendToNullSecond() throws IOException {
        StringWriter appendable = new StringWriter();
        Joiner joiner = Joiner.on(":");
        joiner.appendTo(appendable, "apple", null, "banana", "cherry");
        assertEquals("apple:banana:cherry", appendable.toString());
    }

    @Test
    public void testAppendToObjectArrayNullLast() throws IOException {
        StringWriter appendable = new StringWriter();
        Joiner joiner = Joiner.on(":");
        joiner.appendTo(appendable, "apple", "banana", null);
        assertEquals("apple:banana", appendable.toString());
    }

    @Test
    public void testAppendToIterableNullFirst() throws IOException {
        StringWriter appendable = new StringWriter();
        Joiner joiner = Joiner.on(":");
        joiner.appendTo(appendable, null, Arrays.asList("apple", "banana", "cherry"));
        assertEquals(":apple:banana:cherry", appendable.toString());
    }

    @Test
    public void testAppendToIterableNullLast() throws IOException {
        StringWriter appendable = new StringWriter();
        Joiner joiner = Joiner.on(":");
        Iterator<String> iterator = Arrays.asList("apple", null, "banana", "cherry").iterator();
        joiner.appendTo(appendable, iterator);
        assertEquals(":apple:banana:cherry", appendable.toString());
    }

    @Test
    public void testJoinIterable() {
        String join = Joiner.on(",").join(Arrays.asList("apple", "banana", "cherry"));
        assertEquals("apple,banana,cherry", join);
    }

    @Test
    public void testJoinObjectArray() {
        String join = Joiner.on(" ").join(new String[]{"apple", "banana", "cherry"});
        assertEquals("apple banana cherry", join);
    }

    @Test
    public void testJoinNullFirst() {
        String join = Joiner.on(":").join(new String[]{"", "apple", "banana", "cherry"});
        assertEquals(":apple:banana:cherry", join);
    }

    @Test
    public void testJoinNullSecond() {
        String join = Joiner.on(":").join(new String[]{"apple", "", "banana", "cherry"});
        assertEquals("apple:banana:cherry", join);
    }

    @Test
    public void testJoinObjectArrayNullLast() {
        String join = Joiner.on(":").join(new String[]{"apple", "banana", null});
        assertEquals("apple:banana", join);
    }

    @Test
    public void testJoinIterableNullFirst() {
        String join = Joiner.on(":").join(Collections.singletonList(null));
        assertEquals("", join);
    }

    @Test
    public void testJoinIterableNullLast() {
        String join = Joiner.on(":").join(Arrays.asList("apple", null, "banana", "cherry"));
        assertEquals("apple:banana:cherry", join);
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("N/A");
        String join = joiner.join(new String[]{"apple", null, "banana", "cherry"});
        assertEquals("apple,N/A,banana,cherry", join);
    }

    @Test
    public void testUseForNullWithJoin() {
        Joiner joiner = Joiner.on(",").useForNull("N/A");
        Iterable<String> parts = Arrays.asList(null, "apple", "banana", "cherry");
        String join = joiner.join(parts);
        assertEquals("N/A,apple,banana,cherry", join);
    }

    @Test
    public void testUseForNullWithAppendTo() throws IOException {
        StringWriter appendable = new StringWriter();
        Joiner joiner = Joiner.on(",").useForNull("N/A");
        joiner.appendTo(appendable, new String[]{"apple", null, "banana", "cherry"});
        assertEquals("N/A,apple,banana,cherry", appendable.toString());
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        String join = joiner.join(new String[]{"apple", null, "banana", "cherry"});
        assertEquals("apple,banana,cherry", join);
    }

    @Test
    public void testSkipNullsWithJoin() {
        Joiner joiner = Joiner.on(",").skipNulls();
        Iterable<String> parts = Arrays.asList(null, "apple", null, "banana", "cherry");
        String join = joiner.join(parts);
        assertEquals("apple,banana,cherry", join);
    }

    @Test
    public void testSkipNullsWithAppendTo() throws IOException {
        StringWriter appendable = new StringWriter();
        Joiner joiner = Joiner.on(",").skipNulls();
        joiner.appendTo(appendable, new String[]{"apple", null, "banana", "cherry"});
        assertEquals("apple,banana,cherry", appendable.toString());
    }

    @Test
    public void testMapJoinerCreation() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        assertEquals(",", mapJoiner.joiner.separator);
        assertEquals(":", mapJoiner.keyValueSeparator);
    }

    @Test
    public void testMapJoinerAppendToMapEntryIterator() throws IOException {
        StringWriter appendable = new StringWriter();
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        Map<String, Integer> map = new HashMap<>();
        map.put("apple", 1);
        map.put("banana", 2);
        map.put("cherry", 3);
        mapJoiner.appendTo(appendable, map.entrySet());
        assertEquals("apple:1,banana:2,cherry:3", appendable.toString());
    }

    @Test
    public void testMapJoinerJoinMapEntrySet() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        Map<String, Integer> map = new HashMap<>();
        map.put("apple", 1);
        map.put("banana", 2);
        map.put("cherry", 3);
        String join = mapJoiner.join(map.entrySet());
        assertEquals("apple:1,banana:2,cherry:3", join);
    }

    @Test
    public void testMapJoinerUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("N/A");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        Map<String, Integer> map = new HashMap<>();
        map.put("apple", 1);
        map.put(null, 2);
        map.put("banana", 3);
        map.put("cherry", 4);
        String join = mapJoiner.join(map.entrySet());
        assertEquals("apple:1,N/A:2,banana:3,cherry:4", join);
    }

    @Test
    public void testMapJoinerSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        Map<String, Integer> map = new HashMap<>();
        map.put("apple", 1);
        map.put(null, null);
        map.put("banana", 3);
        map.put("cherry", 4);
        String join = mapJoiner.join(map.entrySet());
        assertEquals("apple:1,banana:3,cherry:4", join);
    }
}