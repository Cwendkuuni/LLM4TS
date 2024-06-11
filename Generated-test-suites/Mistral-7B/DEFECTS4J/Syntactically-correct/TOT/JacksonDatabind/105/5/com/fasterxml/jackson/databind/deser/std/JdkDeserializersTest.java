package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class JdkDeserializersTest {

    @org.junit.Test
    public void testFind_UUID() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, "java.util.UUID");
        UUID actual = mapper.readValue(mapper.writeValueAsString(new UUID(1, 2)), UUID.class, deserializer);
        UUID expected = new UUID(1, 2);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testFind_AtomicBoolean() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(AtomicBoolean.class, "java.util.concurrent.atomic.AtomicBoolean");
        AtomicBoolean actual = mapper.readValue(mapper.writeValueAsString(true), AtomicBoolean.class, deserializer);
        AtomicBoolean expected = new AtomicBoolean(true);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testFind_StackTraceElement() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(StackTraceElement.class, "java.lang.StackTraceElement");
        StackTraceElement actual = mapper.readValue(mapper.writeValueAsString(new StackTraceElement("test", "test", "test.java", 1, 1)), StackTraceElement.class, deserializer);
        StackTraceElement expected = new StackTraceElement("test", "test", "test.java", 1, 1);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testFind_ByteBuffer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(ByteBuffer.class, "java.nio.ByteBuffer");
        ByteBuffer actual = mapper.readValue(mapper.writeValueAsString(ByteBuffer.allocate(10)), ByteBuffer.class, deserializer);
        ByteBuffer expected = ByteBuffer.allocate(10);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testFind_UnknownClass() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(String.class, "java.util.UnknownClass"); // replace with actual unknown class name
        assertNull(deserializer);
    }

    @org.junit.Test
    public void testFind_FromStringDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(String.class, "com.fasterxml.jackson.databind.deser.FromStringDeserializer");
        String actual = mapper.readValue(mapper.writeValueAsString("test"), String.class, deserializer);
        String expected = "test";
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testFind_NullClass() {
        assertNull(JdkDeserializers.find(null, "test"));
    }

    @org.junit.Test
    public void testFind_NullClassName() {
        assertNull(JdkDeserializers.find(UUID.class, null));
    }
}