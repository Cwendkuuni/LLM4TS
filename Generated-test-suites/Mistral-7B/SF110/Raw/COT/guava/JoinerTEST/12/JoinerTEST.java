package com.example.junit; // replace with your test package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.base.MapJoiner;
import com.google.common.collect.ImmutableMap;

public class JoinerTest {

    private Joiner joiner;
    private Joiner joinerWithNullText;
    private Joiner joinerWithSkipNulls;
    private Joiner joinerWithMapJoiner;
    private MapJoiner mapJoiner;

    @Before
    public void setUp() {
        joiner = Joiner.on(":");
        joinerWithNullText = joiner.useForNull("(null)");
        joinerWithSkipNulls = joiner.skipNulls();
        joinerWithMapJoiner = joiner.withKeyValueSeparator(" = ");

        mapJoiner = joinerWithMapJoiner.useForNull("(null):(null)");
    }

    @Test
    public void testOnString() {
        assertEquals("a:b:c", joiner.join(Arrays.asList("a", "b", "c")));
    }

    @Test
    public void testOnChar() {
        assertEquals("a:b:c", joiner.join(Arrays.asList("a", "b", "c")));
    }

    @Test
    public void testAppendToIterable() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        joiner.appendTo(os, Arrays.asList("a", "b", "c"));
        assertEquals("a:b:c", os.toString());
    }

    @Test
    public void testAppendToIterator() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Iterator<String> it = Arrays.asList("a", "b", "c").iterator();
        joiner.appendTo(os, it);
        assertEquals("a:b:c", os.toString());
    }

    @Test
    public void testAppendToArray() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        joiner.appendTo(os, new Object[]{"a", "b", "c"});
        assertEquals("a:b:c", os.toString());
    }

    @Test
    public void testAppendToSingleObject() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        joiner.appendTo(os, "test");
        assertEquals("test", os.toString());
    }

    @Test
    public void testAppendToSingleNull() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        joinerWithNullText.appendTo(os, null);
        assertEquals("(null)", os.toString());
    }

    @Test
    public void testAppendToArrayWithNulls() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        joinerWithNullText.appendTo(os, new Object[]{"a", null, "c"});
        assertEquals("a:(null):c", os.toString());
    }

    @Test
    public void testSkipNulls() {
        assertEquals("b:c", joinerWithSkipNulls.join(Arrays.asList("a", null, "b", "c")));
    }

    @Test
    public void testMapJoiner() {
        Map<String, String> map = ImmutableMap.of("key1", "value1", "key2", null, "key3", "value3");
        assertEquals("key1 = value1:key2 = (null):key3 = value3", mapJoiner.join(map));
    }

    @Test
    public void testMapJoinerNullKey() {
        Map<String, String> map = ImmutableMap.of(null, "value1", "key2", null, "key3", "value3");
        assertEquals("(null) = value1:key2 = (null):key3 = value3", mapJoiner.join(map));
    }

    @Test
    public void testMapJoinerNullValue() {
        Map<String, Object> map = ImmutableMap.of("key1", "value1", "key2", null, "key3", null);
        assertEquals("key1 = value1:key2 = (null):key3 = (null)", mapJoiner.join(map));
    }

    @Test(expected = IOException.class)
    public void testAppendToWithIOException() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException("Test exception");
            }
        };
        joiner.appendTo(os, Arrays.asList("a", "b", "c"));
    }

    @Test
    public void testJoinNull() {
        assertNull(joiner.join(null));
    }

    @Test
    public void testJoinEmptyIterable() {
        assertEquals("", joiner.join(Collections.emptyList()));
    }

    @Test
    public void testJoinEmptyArray() {
        assertEquals("", joiner.join((Object[]) new Object[0]));
    }

}