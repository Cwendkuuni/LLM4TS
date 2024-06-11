package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class JoinerTest {

    @Test
    public void testOnString() {
        Joiner joiner = Joiner.on("|");
        assertEquals("|", joiner.separator);
    }

    @Test
    public void testOnChar() {
        Joiner joiner = Joiner.on('|');
        assertEquals("|", joiner.separator);
    }

    @Test
    public void testEmptyIterable() throws IOException {
        Joiner joiner = Joiner.on("|");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        joiner.appendTo(outputStream, Collections.emptyIterator());
        Assert.assertEquals("", outputStream.toString());
    }

    @Test
    public void testAppendToIterator() throws IOException {
        Joiner joiner = Joiner.on("|");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Iterator<String> iterator = Arrays.asList("test", "append", "to").iterator();
        joiner.appendTo(outputStream, iterator);
        Assert.assertEquals("test|append|to", outputStream.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testNullAppendable() {
        Joiner joiner = Joiner.on("|");
        joiner.appendTo(null, Arrays.asList("test", "append", "to").iterator());
    }

    @Test
    public void testAppendToObjectArray() {
        Joiner joiner = Joiner.on("|");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("test", "append", "to").toArray());
        Assert.assertEquals("test|append|to", builder.toString());
    }

    @Test
    public void testEmptyObjectArray() {
        Joiner joiner = Joiner.on("|");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, new Object[0]);
        Assert.assertEquals("", builder.toString());
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on("").useForNull("null");
        String[] nullArray = new String[0];
        String test = null;
        joiner.appendTo(new StringBuilder(), Arrays.asList(test, "test", null, "append", null, new Object[]{"nullTest", "appendTest", null}));
        Assert.assertEquals("null|test|null|append|null|nullTest|appendTest|null", joiner.join(Arrays.asList(test, "test", null, "append", null, new Object[]{"nullTest", "appendTest", null})));
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on("").skipNulls();
        String[] nullArray = new String[0];
        String test = null;
        joiner.appendTo(new StringBuilder(), Arrays.asList(test, "test", null, "append", null, new Object[]{"nullTest", "appendTest", null}));
        Assert.assertEquals("test|append|nullTest|appendTest", joiner.join(Arrays.asList(test, "test", null, "append", null, new Object[]{"nullTest", "appendTest", null})));
    }

    @Test
    public void testMapJoiner() {
        Joiner joiner = Joiner.on(":");
        Map<String, String> map = new HashMap<>();
        map.put("test1", "append1");
        map.put("test2", "append2");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        StringBuilder stringBuilder = new StringBuilder();
        mapJoiner.appendTo(stringBuilder, map.entrySet());
        Assert.assertEquals("test1:append1:test2:append2", stringBuilder.toString());
    }

    @Test
    public void testUseForNullMapJoiner() {
        Joiner joiner = Joiner.on("").useForNull("null");
        Map<String, String> map = new HashMap<>();
        map.put("test1", "append1");
        map.put("test2", null);
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        StringBuilder stringBuilder = new StringBuilder();
        mapJoiner.appendTo(stringBuilder, map.entrySet());
        Assert.assertEquals("null:test1:append1:test2:null", stringBuilder.toString());
    }

    @Test
    public void testSkipNullsMapJoiner() {
        Joiner joiner = Joiner.on("").skipNulls();
        Map<String, String> map = new HashMap<>();
        map.put("test1", "append1");
        map.put("test2", null);
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        StringBuilder stringBuilder = new StringBuilder();
        mapJoiner.appendTo(stringBuilder, map.entrySet());
        Assert.assertEquals("test1:append1", stringBuilder.toString());
    }

    @Test
    public void testMapJoinerEmpty() {
        Joiner joiner = Joiner.on("").skipNulls();
        Map<String, String> map = new HashMap<>();
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        StringBuilder stringBuilder = new StringBuilder();
        mapJoiner.appendTo(stringBuilder, map.entrySet());
        Assert.assertEquals("", stringBuilder.toString());
    }

}