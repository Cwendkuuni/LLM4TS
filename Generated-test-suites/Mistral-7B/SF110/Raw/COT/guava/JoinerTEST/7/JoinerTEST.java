package com.example.junit; // replace with your package name if necessary

import com.google.common.base.Joiner;
import com.google.common.base.MapJoiner;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class JoinerTest {

    @Test
    public void testJoinerConstruction() {
        Joiner joiner = Joiner.on(",");
        assertEquals(",", joiner.separator());
    }

    @Test
    public void testJoinerAppendToIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        joiner.appendTo(outputStream, Lists.newArrayList("apple", "banana", "cherry"));
        assertEquals("apple,banana,cherry", outputStream.toString());
    }

    @Test
    public void testJoinerAppendToIterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        joiner.appendTo(outputStream, Lists.newArrayList("apple", "banana", "cherry").iterator());
        assertEquals("apple,banana,cherry", outputStream.toString());
    }

    @Test
    public void testJoinerAppendToObjectArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        joiner.appendTo(outputStream, new String[]{"apple", "banana", "cherry"});
        assertEquals("apple,banana,cherry", outputStream.toString());
    }

    @Test
    public void testJoinerAppendToObjectArrayWithNulls() throws IOException {
        Joiner joiner = Joiner.on(",");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Object[] array = new Object[]{"apple", null, "cherry"};
        joiner.appendTo(outputStream, array);
        assertEquals("apple,null,cherry", outputStream.toString());
    }

    @Test
    public void testJoinerJoinIterable() {
        Joiner joiner = Joiner.on(",");
        assertEquals("apple,banana,cherry", joiner.join(Lists.newArrayList("apple", "banana", "cherry")));
    }

    @Test
    public void testJoinerJoinIterator() {
        Joiner joiner = Joiner.on(",");
        assertEquals("apple,banana,cherry", joiner.join(Lists.newArrayList("apple", "banana", "cherry").iterator()));
    }

    @Test
    public void testJoinerJoinObjectArray() {
        Joiner joiner = Joiner.on(",");
        assertEquals("apple,banana,cherry", joiner.join(new String[]{"apple", "banana", "cherry"}));
    }

    @Test
    public void testJoinerJoinObjectArrayWithNulls() {
        Joiner joiner = Joiner.on(",");
        Object[] array = new Object[]{"apple", null, "cherry"};
        assertEquals("apple,null,cherry", joiner.join(array));
    }

    @Test
    public void testJoinerUseForNull() {
        Joiner joiner = Joiner.on(" -> ");
        Joiner nullTextJoiner = joiner.useForNull("N/A");
        assertEquals("apple -> N/A -> banana -> N/A -> cherry", nullTextJoiner.join(Arrays.asList("apple", null, "banana", null, "cherry")));
    }

    @Test
    public void testJoinerSkipNulls() {
        Joiner joiner = Joiner.on(",");
        Joiner skipNullsJoiner = joiner.skipNulls();
        assertEquals("apple,banana,cherry", skipNullsJoiner.join(Arrays.asList("apple", null, "banana", null, "cherry")));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testJoinerUseForNullAndSkipNulls() {
        Joiner joiner = Joiner.on(",");
        Joiner joinerWithUseForNull = joiner.useForNull("");
        joinerWithUseForNull.skipNulls();
    }

    @Test
    public void testMapJoiner() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        Map<String, Integer> map = new HashMap<>();
        map.put("apple", 1);
        map.put("banana", 2);
        map.put("cherry", 3);
        StringBuilder stringBuilder = new StringBuilder();
        mapJoiner.appendTo(stringBuilder, map);
        assertEquals("apple:1,banana:2,cherry:3", stringBuilder.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMapJoinerUseForNull() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.useForNull("");
        mapJoiner.withKeyValueSeparator(":");
        Map<String, Integer> map = new HashMap<>();
        map.put("apple", null);
        map.put("banana", 2);
        mapJoiner.appendTo(new StringBuilder(), map);
    }
}