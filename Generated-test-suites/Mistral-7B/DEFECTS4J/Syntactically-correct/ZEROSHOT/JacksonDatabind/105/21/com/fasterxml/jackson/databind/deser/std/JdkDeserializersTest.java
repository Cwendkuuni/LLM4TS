package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * JUnit 4 test file for the JdkDeserializers class.
 */
public class JdkDeserializersTest {

    @org.junit.Test
    public void testFindDeserializerUuid() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<UUID> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
        UUID actual = mapper.readValue(new ObjectReader(mapper.getFactory()), new TypeReference<UUID>() {});
        UUID expected = new UUID(1L, 2L);
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        UUID input = mapper.readValue(new ObjectReader(mapper.getFactory()), new TypeReference<UUID>() {});
        assertEquals("Deserialized UUID should be equal to input UUID", expected, input);
        JsonDeserializer<UUID> deserializerFromInput = JdkDeserializers.find(UUID.class, UUID.class.getName());
        UUID deserializedUuid = deserializerFromInput.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(jsonString.getBytes())), null);
        assertSame("Deserialized UUID should be the same instance as the initial UUID", expected, deserializedUuid);
        assertSame("Deserializer should be the same instance as the one found initially", deserializer, deserializerFromInput);
    }

    @org.junit.Test
    public void testFindDeserializerAtomicBoolean() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
        AtomicBoolean actual = mapper.readValue(new ObjectReader(mapper.getFactory()), new TypeReference<AtomicBoolean>() {});
        AtomicBoolean expected = new AtomicBoolean(true);
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        AtomicBoolean input = mapper.readValue(new ObjectReader(mapper.getFactory()), new TypeReference<AtomicBoolean>() {});
        assertEquals("Deserialized AtomicBoolean should be equal to input AtomicBoolean", expected, input);
        JsonDeserializer<AtomicBoolean> deserializerFromInput = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
        AtomicBoolean deserializedAtomicBoolean = deserializerFromInput.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(jsonString.getBytes())), null);
        assertSame("Deserialized AtomicBoolean should be the same instance as the initial AtomicBoolean", expected, deserializedAtomicBoolean);
        assertSame("Deserializer should be the same instance as the one found initially", deserializer, deserializerFromInput);
    }

    @org.junit.Test
    public void testFindDeserializerStackTraceElement() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
        List<StackTraceElement> actualList = mapper.readValue(new ObjectReader(mapper.getFactory()), new TypeReference<List<StackTraceElement>>() {});
        StackTraceElement expectedFirst = new StackTraceElement("TestClass", "testMethod", "TestFile.java", 1);
        StackTraceElement expectedLast = new StackTraceElement("TestClass", "main", "TestFile.java", 3);
        StackTraceElement[] expectedStackTrace = {expectedFirst, expectedLast};
        List<StackTraceElement> expectedList = Arrays.asList(expectedStackTrace);
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expectedList.toArray());
        List<StackTraceElement> inputList = mapper.readValue(new ObjectReader(mapper.getFactory()), new TypeReference<List<StackTraceElement>>() {});
        assertEquals("Deserialized StackTraceElements list should be equal to input list", expectedList, inputList);
        JsonDeserializer<StackTraceElement> deserializerFromInput = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
        List<StackTraceElement> deserializedList = deserializerFromInput.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(jsonString.getBytes())), null);
        assertSame("Deserialized StackTraceElements list should be the same instance as the initial list", expectedList, deserializedList);
        assertSame("Deserializer should be the same instance as the one found initially", deserializer, deserializerFromInput);
    }

    @org.junit.Test
    public void testFindDeserializerByteBuffer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        ByteBuffer actual = deserializer.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream("TestData".getBytes())), null);
        ByteBuffer expected = ByteBuffer.wrap("TestData".getBytes());
        ByteBuffer input = ByteBuffer.wrap("TestData".getBytes());
        assertEquals("Deserialized ByteBuffer should be equal to input ByteBuffer", expected, actual);
        JsonDeserializer<ByteBuffer> deserializerFromInput = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        ByteBuffer deserializedByteBuffer = deserializerFromInput.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream("TestData".getBytes().getBytes())), null);
        assertSame("Deserialized ByteBuffer should be the same instance as the initial ByteBuffer", expected, deserializedByteBuffer);
        assertSame("Deserializer should be the same instance as the one found initially", deserializer, deserializerFromInput);
    }

    @org.junit.Test
    public void testFindDeserializerNull() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(String.class, "com.non.existent.class.name");
        assertNull("Deserializer should be null for non-existent class name", deserializer);
    }
}