package com.google.common.base;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class JoinerTest {

    @SuppressWarnings("unchecked")
    @Test
    public <A extends Appendable> void testAppendTo_Iterable_Appendable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder expected = new StringBuilder();
        expected.append("test1").append(joiner.separator).append("test2").append(joiner.separator).append("test3");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, Arrays.asList(new String[]{"test1", "test2", "test3"}));
        assertEquals(expected.toString(), actual.toString());
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NullPointerException.class)
    public <A extends Appendable> void testAppendTo_Iterable_Appendable_NullAppendable() throws IOException {
        Joiner joiner = Joiner.on(",");
        joiner.appendTo((Appendable) null, Arrays.asList(new String[]{"test1", "test2", "test3"}));
    }

    @SuppressWarnings("unchecked")
    @Test(expected = IllegalArgumentException.class)
    public <A extends Appendable> void testAppendTo_Iterable_Appendable_EmptyIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        joiner.appendTo(new StringBuilder(), Collections.emptyList());
    }

    @SuppressWarnings("unchecked")
    @Test(expected = IllegalArgumentException.class)
    public <A extends Appendable> void testAppendTo_Iterator_Appendable() throws IOException {
        Joiner joiner = Joiner.on(",");
        Iterator<String> iterator = Collections.emptyIterator();
        joiner.appendTo(new StringBuilder(), iterator);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NullPointerException.class)
    public <A extends Appendable> void testAppendTo_Iterator_Appendable_NullIterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        joiner.appendTo(new StringBuilder(), (Iterator<String>) null);
    }

    @SuppressWarnings("unchecked")
    @Test
    public <A extends Appendable> void testAppendTo_ObjectArray_Appendable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder expected = new StringBuilder();
        expected.append("test1").append(joiner.separator).append("test2").append(joiner.separator).append("test3");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, new String[]{"test1", "test2", "test3"});
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testAppendTo_Iterable_StringBuilder() {
        Joiner joiner = Joiner.on(",");
        StringBuilder expected = new StringBuilder();
        expected.append("test1,").append("test2,").append("test3");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, Arrays.asList(new String[]{"test1", "test2", "test3"}));
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testJoin_Iterable() {
        Joiner joiner = Joiner.on(",");
        String expected = "test1,test2,test3";
        String actual = joiner.join(Arrays.asList(new String[]{"test1", "test2", "test3"}));
        assertEquals(expected, actual);
    }

    @Test
    public void testJoin_StringArray() {
        Joiner joiner = Joiner.on(",");
        String expected = "test1,test2,test3";
        String actual = joiner.join(new String[]{"test1", "test2", "test3"});
        assertEquals(expected, actual);
    }

    @Test
    public void testJoin_ObjectArray() {
        Joiner joiner = Joiner.on(",");
        String expected = "test1,test2,test3";
        String actual = joiner.join(new Object[]{"test1", "test2", "test3"});
        assertEquals(expected, actual);
    }

    @Test
    public void testJoin_Iterable_NullFirst() {
        Joiner joiner = Joiner.on(",");
        String expected = "";
        String actual = joiner.join(Arrays.asList((Object) null, new String[]{"test2", "test3"}));
        assertEquals(expected, actual);
    }

    @Test
    public void testJoin_Iterable_NullSecond() {
        Joiner joiner = Joiner.on(",");
        String expected = "test2,";
        String actual = joiner.join(Arrays.asList(new String[]{"test1", (Object) null}, "test3".toString()));
        assertEquals(expected, actual);
    }

    @Test
    public void testJoin_Iterable_NullRest() {
        Joiner joiner = Joiner.on(",");
        String expected = "test1,";
        String actual = joiner.join(Arrays.asList(new String[]{"test1", "test2"}, (Object[]) null));
        assertEquals(expected, actual);
    }

    @Test
    public void testJoin_Map() {
        Joiner joiner = Joiner.on(" => ");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        String expected = "key1 => value1, key2 => value2";
        String actual = joiner.join(map.entrySet());
        assertEquals(expected, actual);
    }

    @Test
    public void testJoin_Map_NullValueFirst() {
        Joiner joiner = Joiner.on(" => ");
        Map<String, String> map = new HashMap<>();
        map.put("key1", null);
        map.put("key2", "value2");
        String expected = "key1 => , key2 => value2";
        String actual = joiner.join(map.entrySet());
        assertEquals(expected, actual);
    }

    @Test
    public void testJoin_Map_NullValueSecond() {
        Joiner joiner = Joiner.on(" => ");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", null);
        String expected = "key1 => value1, key2 => ";
        String actual = joiner.join(map.entrySet());
        assertEquals(expected, actual);
    }

    @Test
    public void testJoin_Map_NullValueRest() {
        Joiner joiner = Joiner.on(" => ");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", null);
        String expected = "key1 => value1, key2 => value2, key3 => ";
        String actual = joiner.join(map.entrySet());
        assertEquals(expected, actual);
    }
}