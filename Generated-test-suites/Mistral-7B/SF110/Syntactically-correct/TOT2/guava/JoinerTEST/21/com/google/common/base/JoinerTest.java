package com.google.common.base;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class JoinerTest {

    private Joiner joiner;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setup() {
        joiner = Joiner.on("/");
    }

    @Test
    public void testAppendToIterable() throws IOException {
        Iterator<String> iterator = Arrays.asList("Hello", "World", "!").iterator();
        Joiner joiner = Joiner.on("/");

        joiner.appendTo(outputStream, iterator);

        assertEquals("/Hello/World/!", outputStream.toString());
    }

    @Test
    public void testAppendToIterableEmpty() throws IOException {
        Iterator<String> iterator = Collections.emptyIterator();
        Joiner joiner = Joiner.on("/");

        joiner.appendTo(outputStream, iterator);

        assertEquals("", outputStream.toString());
    }

    @Test
    public void testAppendToIterableNull() throws IOException {
        Iterator<String> iterator = null;
        Joiner joiner = Joiner.on("/");

        try {
            joiner.appendTo(outputStream, iterator);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAppendToObjectArray() throws IOException {
        String[] parts = {"Hello", "World", "!"};
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        joiner.appendTo(outputStream, parts);

        assertEquals("/Hello/World/!", outputStream.toString());
    }

    @Test
    public void testAppendToObjectArrayEmpty() throws IOException {
        String[] parts = {};
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        joiner.appendTo(outputStream, parts);

        assertEquals("", outputStream.toString());
    }

    @Test
    public void testAppendToObjectArrayNull() throws IOException {
        String[] parts = {null};
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Joiner joiner = Joiner.on("/").useForNull("Null");

        joiner.appendTo(outputStream, parts);

        assertEquals("/Null/", outputStream.toString());
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on("/");
        Joiner joinerNullText = joiner.useForNull("Null");

        assertEquals("/", joiner.toString(null));
        assertEquals("Null", joinerNullText.toString(null));
    }
}