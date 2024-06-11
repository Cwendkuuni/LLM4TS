package com.google.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

public class JoinerTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testJoinStringSeparator() {
        Joiner joiner = Joiner.on(",");
        List<String> list = Arrays.asList("apple", "banana", "cherry");
        String result = joiner.join(list);
        assertEquals("apple,banana,cherry", result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testJoinCharSeparator() {
        Joiner joiner = Joiner.on(';');
        List<String> list = Arrays.asList("apple", "banana", "cherry");
        String result = joiner.join(list);
        assertEquals("apple;banana;cherry", result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(":");
        List<Object> list = Arrays.asList("apple", 1, "cherry");
        String result = joiner.join(list);
        assertEquals("apple:1:cherry", result);
    }

    @Test
    public void testJoinNullObject() {
        Joiner joiner = Joiner.on(":");
        String result = joiner.join(new Object[0]);
        assertNull(result);
    }

    @Test
    public void testJoinNullIterable() {
        Joiner joiner = Joiner.on(":");
        String result = joiner.join(null);
        assertNull(result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testJoinArray() {
        Joiner joiner = Joiner.on(".");
        String[] array = {"apple", "banana", "cherry"};
        String result = joiner.join(array);
        assertEquals("apple.banana.cherry", result);
    }

    @Test
    public void testJoinSingleObject() {
        Joiner joiner = Joiner.on(":");
        Object obj = "apple";
        String result = joiner.join(obj);
        assertEquals("apple", result);
    }

    @Test
    public void testJoinTwoObjects() {
        Joiner joiner = Joiner.on(":");
        Object obj1 = "apple";
        Object obj2 = "banana";
        String result = joiner.join(obj1, obj2);
        assertEquals("apple:banana", result);
    }

    @Test
    public void testJoinThreeObjects() {
        Joiner joiner = Joiner.on(":");
        Object obj1 = "apple";
        Object obj2 = "banana";
        Object obj3 = "cherry";
        String result = joiner.join(obj1, obj2, obj3);
        assertEquals("apple:banana:cherry", result);
    }

    @Test
    public void testJoinStringBuilder() {
        Joiner joiner = Joiner.on(":");
        StringBuilder builder = new StringBuilder();
        List<Object> list = Arrays.asList("apple", 1, "cherry");
        joiner.appendTo(builder, list);
        String result = builder.toString();
        assertEquals("apple:1:cherry", result);
    }

    @Test
    public void testJoinMap() {
        Joiner joiner = Joiner.on(":");
        Map<String, Object> map = new ConcurrentHashMap<>();
        map.put("apple", "fruit");
        map.put("banana", 1);
        map.put("cherry", "tree");
        String result = joiner.join(map.entrySet());
        assertEquals("apple:fruit:banana:1:cherry:tree", result);
    }

    @Test
    public void testJoinMapEntryIterator() {
        Joiner joiner = Joiner.on(":");
        Map<String, Object> map = new ConcurrentHashMap<>();
        map.put("apple", "fruit");
        map.put("banana", 1);
        map.put("cherry", "tree");
        Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
        String result = joiner.join(it);
        assertEquals("apple:fruit:banana:1:cherry:tree", result);
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(":");
        String result = joiner.useForNull("NA").join(new Object[]{"apple", null, "cherry"});
        assertEquals("apple:NA:cherry", result);
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(":").skipNulls();
        String result = joiner.join(new Object[]{"apple", null, "cherry"});
        assertEquals("apple:cherry", result);
    }

    @Test
    public void testMapJoiner() {
        Joiner joiner = Joiner.on(":");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
        Map<String, Object> map = new ConcurrentHashMap<>();
        map.put("apple", "fruit");
        map.put("banana", 1);
        map.put("cherry", "tree");
        String result = mapJoiner.join(map);
        assertEquals("apple=fruit:banana=1:cherry=tree", result);
    }

    @Test
    public void testMapJoinerUseForNull() {
        Joiner joiner = Joiner.on(":");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("=").useForNull("NA");
        Map<String, Object> map = new ConcurrentHashMap<>();
        map.put("apple", "fruit");
        map.put("banana", null);
        map.put("cherry", "tree");
        String result = mapJoiner.join(map);
        assertEquals("apple=fruit:cherry=tree:banana=NA", result);
    }
}