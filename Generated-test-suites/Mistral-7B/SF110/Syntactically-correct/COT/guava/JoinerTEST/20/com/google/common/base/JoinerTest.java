package com.example.junit; // replace with your test package name

import static org.junit.Assert.*;
import com.google.common.base.Joiner;
import com.google.common.base.MapJoiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JoinerTest {

    @SuppressWarnings("unchecked")
    @ org.junit.Test
    public <T> void testAppendTo_Iterable_Appendable() throws IOException {
        Joiner joiner = Joiner.on(":");
        StringWriter stringWriter = new StringWriter();
        Iterable<T> iterable = Lists.newArrayList("Test1", "Test2", "Test3");
        joiner.appendTo(stringWriter, iterable);
        String expected = "Test1:Test2:Test3";
        assertEquals(expected, stringWriter.toString());
    }

    @SuppressWarnings("unchecked")
    @ org.junit.Test
    public <T> void testAppendTo_Iterator_Appendable() throws IOException {
        Joiner joiner = Joiner.on(":");
        StringWriter stringWriter = new StringWriter();
        Iterator<T> iterator = Arrays.asList("Test1", "Test2", "Test3").iterator();
        joiner.appendTo(stringWriter, iterator);
        String expected = "Test1:Test2:Test3";
        assertEquals(expected, stringWriter.toString());
    }

    @SuppressWarnings("unchecked")
    @ org.junit.Test
    public <T> void testAppendTo_ObjectArray_Appendable() throws IOException {
        Joiner joiner = Joiner.on(":");
        StringWriter stringWriter = new StringWriter();
        joiner.appendTo(stringWriter, new Object[]{"Test1", "Test2", "Test3"});
        String expected = "Test1:Test2:Test3";
        assertEquals(expected, stringWriter.toString());
    }

    @SuppressWarnings("unchecked")
    @ org.junit.Test
    public <T> void testAppendTo_Object_Object_Object_Appendable() throws IOException {
        Joiner joiner = Joiner.on(":");
        StringWriter stringWriter = new StringWriter();
        joiner.appendTo(stringWriter, "First:", "Second:", "Third");
        String expected = "First:Second:Third";
        assertEquals(expected, stringWriter.toString());
    }

    @SuppressWarnings("unchecked")
    @ org.junit.Test
    public void testAppendTo_Iterable_StringBuilder() {
        Joiner joiner = Joiner.on(":");
        StringBuilder stringBuilder = new StringBuilder();
        Iterable<String> iterable = Arrays.asList("Test1", "Test2", "Test3");
        joiner.appendTo(stringBuilder, iterable);
        String expected = "Test1:Test2:Test3";
        assertEquals(expected, stringBuilder.toString());
    }

    @SuppressWarnings("unchecked")
    @ org.junit.Test
    public void testAppendTo_Iterator_StringBuilder() {
        Joiner joiner = Joiner.on(":");
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<String> iterator = Arrays.asList("Test1", "Test2", "Test3").iterator();
        joiner.appendTo(stringBuilder, iterator);
        String expected = "Test1:Test2:Test3";
        assertEquals(expected, stringBuilder.toString());
    }

    @SuppressWarnings("unchecked")
    @ org.junit.Test
    public void testAppendTo_ObjectArray_StringBuilder() {
        Joiner joiner = Joiner.on(":");
        StringBuilder stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, new Object[]{"Test1", "Test2", "Test3"});
        String expected = "Test1:Test2:Test3";
        assertEquals(expected, stringBuilder.toString());
    }

    @SuppressWarnings("unchecked")
    @ org.junit.Test
    public void testAppendTo_Object_Object_Object_StringBuilder() {
        Joiner joiner = Joiner.on(":");
        StringBuilder stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, "First:", "Second:", "Third");
        String expected = "First:Second:Third";
        assertEquals(expected, stringBuilder.toString());
    }

    @SuppressWarnings("unchecked")
    @ org.junit.Test
    public void testJoin_Iterable() {
        Joiner joiner = Joiner.on(":");
        Iterable<String> iterable = Arrays.asList("Test1", "Test2", "Test3");
        String expected = "Test1:Test2:Test3";
        assertEquals(expected, joiner.join(iterable));
    }

    @SuppressWarnings("unchecked")
    @ org.junit.Test
    public void testJoin_Iterator() {
        Joiner joiner = Joiner.on(":");
        Iterator<String> iterator = Arrays.asList("Test1", "Test2", "Test3").iterator();
        String expected = "Test1:Test2:Test3";
        assertEquals(expected, joiner.join(iterator));
    }

    @SuppressWarnings("unchecked")
    @ org.junit.Test
    public void testJoin_ObjectArray() {
        Joiner joiner = Joiner.on(":");
        String[] array = {"Test1", "Test2", "Test3"};
        String expected = "Test1:Test2:Test3";
        assertEquals(expected, joiner.join(array));
    }

    @SuppressWarnings("unchecked")
    @ org.junit.Test
    public void testJoin_Object_Object_Object() {
        Joiner joiner = Joiner.on(":");
        String expected = "First:Second:Third";
        assertEquals(expected, joiner.join("First", "Second", "Third"));
    }

    @SuppressWarnings("unchecked")
    @ org.junit.Test
    public void testJoin_NullObjects() {
        Joiner joiner = Joiner.on(":");
        Joiner joinerWithNullText = joiner.useForNull("NULL");
        Object[] arrayWithNullObjects = {"Test1", null, "Test3"};
        String expected = "Test1:NULL:Test3";
        assertEquals(expected, joinerWithNullText.join(arrayWithNullObjects));
    }

    @SuppressWarnings("unchecked")
    @ org.junit.Test
    public void testJoin_EmptyIterable() {
        Joiner joiner = Joiner.on(":");
        Iterable<String> emptyIterable = Collections.emptyList();
        String expected = "";
        assertEquals(expected, joiner.join(emptyIterable));
    }

    @SuppressWarnings("unchecked")
    @ org.junit.Test
    public void testJoin_EmptyObjectArray() {
        Joiner joiner = Joiner.on(":");
        String[] emptyArray = {};
        String expected = "";
        assertEquals(expected, joiner.join(emptyArray));
    }

    @SuppressWarnings("unchecked")
    @ org.junit.Test
    public void testMapJoiner_Map_Appendable() throws IOException {
        Joiner joiner = Joiner.on(":");
        Map<String, String> map = Maps.newHashMap();
        map.put("Key1", "Value1");
        map.put("Key2", "Value2");
        map.put("Key3", "Value3");
        StringWriter stringWriter = new StringWriter();
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("->");
        mapJoiner.appendTo(stringWriter, map);
        String expected = "Key1->Value1:Key2->Value2:Key3->Value3";
        assertEquals(expected, stringWriter.toString());
    }

    @SuppressWarnings("unchecked")
    @ org.junit.Test
    public void testMapJoiner_Map_StringBuilder() {
        Joiner joiner = Joiner.on(":");
        Map<String, String> map = Maps.newHashMap();
        map.put("Key1", "Value1");
        map.put("Key2", "Value2");
        map.put("Key3", "Value3");
        StringBuilder stringBuilder = new StringBuilder();
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("->");
        mapJoiner.appendTo(stringBuilder, map);
        String expected = "Key1->Value1:Key2->Value2:Key3->Value3";
        assertEquals(expected, stringBuilder.toString());
    }

    @SuppressWarnings("unchecked")
    @ org.junit.Test
    public void testMapJoiner_Map_Iterable() {
        Joiner joiner = Joiner.on(":");
        Map<String, String> map = Maps.newHashMap();
        map.put("Key1", "Value1");
        map.put("Key2", "Value2");
        map.put("Key3", "Value3");
        String expected = "Key1->Value1:Key2->Value2:Key3->Value3";
        assertEquals(expected, joiner.withKeyValueSeparator("->").join(map.entrySet()));
    }

    @SuppressWarnings("unchecked")
    @ org.junit.Test
    public void testMapJoiner_Map_Iterator() {
        Joiner joiner = Joiner.on(":");
        Map<String, String> map = Maps.newHashMap();
        map.put("Key1", "Value1");
        map.put("Key2", "Value2");
        map.put("Key3", "Value3");
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        String expected = "Key1->Value1:Key2->Value2:Key3->Value3";
        assertEquals(expected, joiner.withKeyValueSeparator("->").join(iterator));
    }

    @SuppressWarnings("unchecked")
    @ org.junit.Test
    public void testMapJoiner_Map_NullObjects() {
        Joiner joiner = Joiner.on(":");
        Map<String, String> map = Maps.newHashMap();
        map.put("Key1", "Value1");
        map.put(null, null);
        map.put("Key3", "Value3");
        MapJoiner mapJoiner = joiner.useForNull("NULL");
        String[] arrayWithNullObjects = {null, null, "Test3"};
        String expected = "Key1->Value1:NULL:NULL:Key3->Value3";
        assertEquals(expected, mapJoiner.join(map.entrySet().toArray(new Map.Entry[0])).split(":"));
    }
}