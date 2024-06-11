package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.JdkDeserializers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

/**
 * Junit test file for the JdkDeserializers class.
 */
public class JdkDeserializersTest {

    @Test
    public void testFindDeserializerForUUID() throws IOException {
        Class<?> rawType = UUID.class;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, UUID.class.getName());
        assertNotNull(deserializer);
        assertInstanceof(UUIDDeserializer.class, deserializer);
    }

    @Test
    public void testFindDeserializerForAtomicBoolean() throws IOException {
        Class<?> rawType = AtomicBoolean.class;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, AtomicBoolean.class.getName());
        assertNotNull(deserializer);
        assertInstanceof(AtomicBooleanDeserializer.class, deserializer);
    }

    @Test
    public void testFindDeserializerForStackTraceElement() throws IOException {
        Class<?> rawType = StackTraceElement.class;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, StackTraceElement.class.getName());
        assertNotNull(deserializer);
        assertInstanceof(StackTraceElementDeserializer.class, deserializer);
    }

    @Test
    public void testFindDeserializerForByteBuffer() throws IOException {
        Class<?> rawType = ByteBuffer.class;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, ByteBuffer.class.getName());
        assertNotNull(deserializer);
        assertInstanceof(ByteBufferDeserializer.class, deserializer);
    }

    @Test
    public void testFindDeserializerForUnknownClass() throws IOException {
        Class<?> rawType = String.class;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, "UnknownClass.class".replace(".", "/"));
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializerForNullRawType() {
        Class<?> rawType = null;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, UUID.class.getName());
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializerForNullClassName() {
        Class<?> rawType = UUID.class;
        String className = null;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, className);
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializerForFromStringDeserializer() throws IOException {
        Class<?> rawType = String.class;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, FromStringDeserializer.class.getName());
        assertNotNull(deserializer);
        assertInstanceof(FromStringDeserializer.class, deserializer);
    }

    @Test
    public void testFindDeserializerForArrayList() throws IOException {
        Class<?> rawType = new TypeReference<List<String>>(){}.getType();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, ArrayList.class.getName());
        assertNull(deserializer);
    }
}