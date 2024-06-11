package com.example.junit; // replace with your test package name

import static org.junit.Assert.*;
import com.google.common.base.Joiner;
import com.google.common.base.MapJoiner;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JoinerTest {

    @Test
    public void testJoinerConstruction() {
        Joiner joiner = Joiner.on(",");
        assertEquals(",", joiner.separator());

        Joiner joinerWithCharSeparator = Joiner.on(';');
        assertEquals(";", joinerWithCharSeparator.separator());
    }

    @Test
    public void testAppendToIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = new StringWriter();
        Iterable<String> iterable = Arrays.asList("apple", "banana", "cherry");

        joiner.appendTo(stringWriter, iterable);
        assertEquals("apple,banana,cherry", stringWriter.toString());
    }

    @Test
    public void testAppendToObjectArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = new StringWriter();
        Object[] objectArray = {"apple", "banana", "cherry"};

        joiner.appendTo(stringWriter, objectArray);
        assertEquals("apple,banana,cherry", stringWriter.toString());
    }

    @Test
    public void testAppendToObjectNulls() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = new StringWriter();
        Object[] objectArray = {"apple", null, "cherry"};

        joiner.appendTo(stringWriter, objectArray);
        assertEquals("apple,null,cherry", stringWriter.toString());
    }

    @Test
    public void testAppendToIterableEmpty() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = new StringWriter();
        Iterable<String> iterable = Collections.emptyList();

        joiner.appendTo(stringWriter, iterable);
        assertEquals("", stringWriter.toString());
    }

    @Test
    public void testAppendToIteratorEmpty() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = new StringWriter();
        Iterator<String> iterator = Collections.emptyIterator();

        joiner.appendTo(stringWriter, iterator);
        assertEquals("", stringWriter.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testAppendToNullIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = new StringWriter();
        Iterable<String> iterable = null;

        joiner.appendTo(stringWriter, iterable);
    }

    @Test(expected = NullPointerException.class)
    public void testAppendToNullIterableParts() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = new StringWriter();
        Iterator<String> iterator = Arrays.asList("apple", null).iterator();

        joiner.appendTo(stringWriter, iterator);
    }

    @Test(expected = NullPointerException.class)
    public void testAppendToNullAppendable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = null;
        Iterator<String> parts = Arrays.asList("apple", "banana", "cherry").iterator();

        joiner.appendTo(stringWriter, parts);
    }

    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(",");
        List<String> list = Arrays.asList("apple", "banana", "cherry");

        assertEquals("apple,banana,cherry", joiner.join(list));
    }

    @Test
    public void testJoinObjectArray() {
        Joiner joiner = Joiner.on(",");
        Object[] array = {"apple", "banana", "cherry"};

        assertEquals("apple,banana,cherry", joiner.join(array));
    }

    @Test
    public void testJoinObjectNulls() {
        Joiner joiner = Joiner.on(",");
        Object[] array = {"apple", null, "cherry"};

        assertEquals("apple,null,cherry", joiner.join(array));
    }

    @Test
    public void testJoinIterableEmpty() {
        Joiner joiner = Joiner.on(",");
        Iterable<String> iterable = Collections.emptyList();

        assertEquals("", joiner.join(iterable));
    }

    @Test
    public void testJoinIteratorEmpty() {
        Joiner joiner = Joiner.on(",");
        Iterator<String> iterator = Collections.emptyIterator();

        assertEquals("", joiner.join(iterator));
    }

    @Test
    public void testJoinNullIterator() {
        Joiner joiner = Joiner.on(",");
        Iterator<String> iterator = null;

        // NullPointerException is expected
        try {
            joiner.join(iterator);
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testJoinNullIterable() {
        Joiner joiner = Joiner.on(",");
        Iterable<String> iterable = null;

        joiner.join(iterable);
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",");
        Joiner useForNullJoiner = joiner.useForNull("NA");

        assertEquals("NA,apple,NA,banana,NA,cherry", useForNullJoiner.join(Arrays.asList("apple", "banana", "cherry")));
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",");
        Joiner skipNullsJoiner = joiner.skipNulls();

        assertEquals("apple,banana", skipNullsJoiner.join(Arrays.asList("apple", null, "banana")));
    }

    @Test
    public void testMapJoiner() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        Map<String, String> map = ImmutableMap.of("apple", "red", "banana", "yellow");

        assertEquals("apple:red,banana:yellow", mapJoiner.join(map.entrySet()));
    }

    @Test
    public void testMapJoinerUseForNull() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":").useForNull("NA");
        Map<String, String> map = ImmutableMap.of("apple", "red", null, "banana", "yellow");

        assertEquals("apple:red,NA:NA,banana:yellow", mapJoiner.join(map.entrySet()));
    }
}