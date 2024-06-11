package com.example.junit;

import org.junit.*;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonFactory;
import java.io.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class StdDelegatingSerializerTest {

    @Test
    public void testStdDelegatingSerializerConstructor() throws Exception {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer();
        assertNotNull(serializer);
    }

    @Test
    public void testSerializeNullValue() throws Exception {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null, null, null);
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        serializer.serialize(null, gen, new DefaultSerializerProvider());
        gen.flush();
        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) gen.getOutputFile();
        String jsonString = new String(outputStream.toByteArray());
        assertTrue(jsonString.isEmpty());
    }

    @Test
    public void testSerializePrimitiveType() throws Exception {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null, null, null);
        int value = 123;
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        serializer.serialize(value, gen, new DefaultSerializerProvider());
        gen.flush();
        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) gen.getOutputFile();
        String jsonString = new String(outputStream.toByteArray());
        assertEquals("123", jsonString);
    }

    @Test
    public void testSerializeStringType() throws Exception {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null, null, null);
        String value = "abcdef";
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        serializer.serialize(value, gen, new DefaultSerializerProvider());
        gen.flush();
        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) gen.getOutputFile();
        String jsonString = new String(outputStream.toByteArray());
        assertEquals("\"abcdef\"", jsonString);
    }

    @Test(expected = NullPointerException.class)
    public void testSerializeNullConverter() throws Exception {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null, null, null);
        Object value = new Object();
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        serializer.serialize(value, gen, new DefaultSerializerProvider());
    }
}