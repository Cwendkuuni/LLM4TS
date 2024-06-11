package com.example.junit; // replace with your test package name

import com.google.common.base.Joiner;
import com.google.common.base.MapJoiner;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class JoinerTest {

    @Test
    public void testJoinerInstanceWithStringSeparator() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(List.of("apple", "banana", "orange"));
        assertEquals("apple,banana,orange", result);
    }

    @Test
    public void testJoinerInstanceWithCharSeparator() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(new String[]{"apple", "banana", "orange"});
        assertEquals("apple,banana,orange", result);
    }

    @Test
    public void testAppendToWithStringBuilderAndIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder stringBuilder = new StringBuilder();
        Iterable<String> iterable = List.of("apple", "banana", "orange").iterator();
        joiner.appendTo(stringBuilder, iterable);
        assertEquals("apple,banana,orange", stringBuilder.toString());
    }

    @Test
    public void testAppendToWithStringBuilderAndObjectArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, new String[]{"apple", "banana", "orange"});
        assertEquals("apple,banana,orange", stringBuilder.toString());
    }

    @Test
    public void testAppendToWithStringBuilderAndIterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<String> iterator = List.of("apple", "banana", "orange").iterator();
        joiner.appendTo(stringBuilder, iterator);
        assertEquals("apple,banana,orange", stringBuilder.toString());
    }

    @Test
    public void testJoinWithIterable() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(List.of("apple", "banana", "orange"));
        assertEquals("apple,banana,orange", result);
    }

    @Test
    public void testJoinWithObjectArray() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(new String[]{"apple", "banana", "orange"});
        assertEquals("apple,banana,orange", result);
    }

    @Test
    public void testJoinWithNullFirstObjectAndNullSecondObject() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(null, null, new String[]{"apple", "banana", "orange"});
        assertEquals("null,null,apple,banana,orange", result);
    }

    @Test
    public void testJoinWithNullFirstObjectAndNotNullSecondObject() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(null, "separator", new String[]{"apple", "banana", "orange"});
        assertEquals("separator,apple,banana,orange", result);
    }

    @Test
    public void testJoinWithNotNullFirstObjectAndNullSecondObject() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join("separator", null, new String[]{"apple", "banana", "orange"});
        assertEquals("separator,apple,banana,orange", result);
    }

    @Test
    public void testAppendToWithIOException() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = new StringWriter();
        assertThrows(IOException.class, () -> joiner.appendTo(stringWriter, List.of("apple", "banana", "orange").iterator()));
    }

    @Test
    public void testAppendToWithNullIterable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder stringBuilder = new StringBuilder();
        assertThrows(NullPointerException.class, () -> joiner.appendTo(stringBuilder, null));
    }

    @Test
    public void testAppendToWithNullIterator() {
        Joiner joiner = Joiner.on(",");
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<String> iterator = null;
        assertThrows(NullPointerException.class, () -> joiner.appendTo(stringBuilder, iterator));
    }

    @Test
    public void testMapJoinerInstanceWithStringKeyValueSeparator() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(",");
        Map<String, String> map = Maps.newHashMap();
        map.put("one", "oneValue");
        map.put("two", "twoValue");
        String result = mapJoiner.join(map);
        assertEquals("one=oneValue,two=twoValue", result);
    }

    @Test
    public void testMapJoinerAppendToWithStringBuilderAndMap() throws IOException {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(",");
        StringBuilder stringBuilder = new StringBuilder();
        Map<String, String> map = Maps.newHashMap();
        map.put("one", "oneValue");
        map.put("two", "twoValue");
        mapJoiner.appendTo(stringBuilder, map);
        assertEquals("one=oneValue,two=twoValue", stringBuilder.toString());
    }

    @Test
    public void testMapJoinerJoinWithMap() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(",");
        Map<String, String> map = Maps.newHashMap();
        map.put("one", "oneValue");
        map.put("two", "twoValue");
        String result = mapJoiner.join(map);
        assertEquals("one=oneValue,two=twoValue", result);
    }

    @Test
    public void testMapJoinerJoinWithNullMap() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(",");
        String result = mapJoiner.join(null);
        assertEquals("", result);
    }

    @Test
    public void testMapJoinerUseForNull() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(",");
        String result = mapJoiner.useForNull("nullValue").join(Map.of("one", "oneValue"));
        assertEquals("one=oneValue,null=nullValue", result);
    }
}