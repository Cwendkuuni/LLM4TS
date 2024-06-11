package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Test file for JdkDeserializers class.
 */
public class JdkDeserializersTest {

    @org.junit.BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // Empty setup.
    }

    @org.junit.AfterClass
    public static void tearDownAfterClass() throws Exception {
        // Empty teardown.
    }

    /**
     * Test method for {@link JdkDeserializers#find(Class, String)}.
     *
     * @throws IOException if an error occurs during deserialization
     */
    @org.junit.Test
    public void testFind() throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final JsonFactory jsonFactory = objectMapper.getFactory();

        // UUID deserializer test
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
        assertNotNull(deserializer);
        assertInstanceOf(UUIDDeserializer.class, deserializer);

        // AtomicBoolean deserializer test
        deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
        assertNotNull(deserializer);
        assertInstanceOf(AtomicBooleanDeserializer.class, deserializer);

        // StackTraceElement deserializer test
        deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
        assertNotNull(deserializer);
        assertInstanceOf(StackTraceElementDeserializer.class, deserializer);

        // ByteBuffer deserializer test
        deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        assertNotNull(deserializer);
        assertInstanceOf(ByteBufferDeserializer.class, deserializer);

        // Unknown class test
        deserializer = JdkDeserializers.find(String.class, Object.class.getName());
        assertNull(deserializer);

        // Null test
        deserializer = JdkDeserializers.find(null, null);
        assertNull(deserializer);

        // Unsupported type test
        deserializer = JdkDeserializers.find(int[].class, Object.class.getName());
        assertNull(deserializer);

        // Test with FromStringDeserializer
        deserializer = JdkDeserializers.find(String.class, String.class.getName());
        assertNotNull(deserializer);
        assertInstanceOf(FromStringDeserializer.class, deserializer);

        // Test with null input
        deserializer = JdkDeserializers.find(null, null);
        assertNull(deserializer);
    }

    /**
     * Test method for {@link JdkDeserializers#find(Class, String)}.
     *
     * @throws IOException if an error occurs during deserialization
     */
    @org.junit.Test
    public void testFindWithObjectMapper() throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();

        // UUID deserializer test
        JsonDeserializer<?> deserializer = objectMapper.getFactory().findValueDeserializer(UUID.class, UUID.class.getName());
        assertNotNull(deserializer);
        assertInstanceOf(UUIDDeserializer.class, deserializer);

        // AtomicBoolean deserializer test
        deserializer = objectMapper.getFactory().findValueDeserializer(AtomicBoolean.class, AtomicBoolean.class.getName());
        assertNotNull(deserializer);
        assertInstanceOf(AtomicBooleanDeserializer.class, deserializer);

        // StackTraceElement deserializer test
        deserializer = objectMapper.getFactory().findValueDeserializer(StackTraceElement.class, StackTraceElement.class.getName());
        assertNotNull(deserializer);
        assertInstanceOf(StackTraceElementDeserializer.class, deserializer);

        // ByteBuffer deserializer test
        deserializer = objectMapper.getFactory().findValueDeserializer(ByteBuffer.class, ByteBuffer.class.getName());
        assertNotNull(deserializer);
        assertInstanceOf(ByteBufferDeserializer.class, deserializer);
    }

    /**
     * Test method for {@link JdkDeserializers#find(Class, String)} with different types.
     *
     * @throws IOException if an error occurs during deserialization
     */
    @org.junit.Test
    public void testFindWithDifferentTypes() throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();

        // Test with different input classes
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, Object.class.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(UUID.class, StackTraceElement.class.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(UUID.class, String.class.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(UUID.class, ByteBuffer.class.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(UUID.class, AtomicBoolean.class.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(AtomicBoolean.class, UUID.class.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(AtomicBoolean.class, StackTraceElement.class.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(AtomicBoolean.class, String.class.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(AtomicBoolean.class, ByteBuffer.class.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(AtomicBoolean.class, UUID.class.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(StackTraceElement.class, UUID.class.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
        assertNotNull(deserializer);
        assertInstanceOf(StackTraceElementDeserializer.class, deserializer);

        deserializer = JdkDeserializers.find(StackTraceElement.class, String.class.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(StackTraceElement.class, ByteBuffer.class.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(StackTraceElement.class, AtomicBoolean.class.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(String.class, UUID.class.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(String.class, StackTraceElement.class.getName());
        assertNotNull(deserializer);
        assertInstanceOf(FromStringDeserializer.class, deserializer);

        deserializer = JdkDeserializers.find(String.class, String.class.getName());
        assertNotNull(deserializer);
        assertInstanceOf(FromStringDeserializer.class, deserializer);

        deserializer = JdkDeserializers.find(String.class, ByteBuffer.class.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(String.class, AtomicBoolean.class.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(ByteBuffer.class, UUID.class.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(ByteBuffer.class, StackTraceElement.class.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(ByteBuffer.class, String.class.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        assertNotNull(deserializer);
        assertInstanceOf(ByteBufferDeserializer.class, deserializer);

        deserializer = JdkDeserializers.find(ByteBuffer.class, AtomicBoolean.class.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(AtomicBoolean.class, UUID.class.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(AtomicBoolean.class, StackTraceElement.class.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(AtomicBoolean.class, String.class.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(AtomicBoolean.class, ByteBuffer.class.getName());
        assertNull(deserializer);
    }

    /**
     * Test method for {@link JdkDeserializers#find(Class, String)} with custom input classes.
     *
     * @throws IOException if an error occurs during deserialization
     */
    @org.junit.Test
    public void testFindWithCustomClasses() throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();

        Class<?> customClass1 = new CustomClass1();
        Class<?> customClass2 = new CustomClass2();

        // Test with custom classes
        JsonDeserializer<?> deserializer = JdkDeserializers.find(customClass1, customClass1.getName());
        assertNotNull(deserializer);
        assertNotSame(customClass1, deserializer);
        assertInstanceOf(BaseDeserializer.class, deserializer);

        deserializer = JdkDeserializers.find(customClass2, customClass1.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(customClass2, customClass2.getName());
        assertNotNull(deserializer);
        assertInstanceOf(CustomClass2Deserializer.class, deserializer);

        deserializer = JdkDeserializers.find(customClass2, UUID.class.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(customClass2, StackTraceElement.class.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(customClass2, String.class.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(customClass2, ByteBuffer.class.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(customClass2, AtomicBoolean.class.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(UUID.class, customClass1.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(UUID.class, customClass2.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(StackTraceElement.class, customClass1.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(StackTraceElement.class, customClass2.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(String.class, customClass1.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(String.class, customClass2.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(ByteBuffer.class, customClass1.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(ByteBuffer.class, customClass2.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(AtomicBoolean.class, customClass1.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(AtomicBoolean.class, customClass2.getName());
        assertNull(deserializer);
    }

    private static class CustomClass1 {
        // Empty custom class.
    }

    private static class CustomClass2 {
        // Empty custom class.
    }

    private static class CustomClass2Deserializer extends JsonDeserializer<CustomClass2> {
        @Override
        public CustomClass2 deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new CustomClass2();
        }
    }
}