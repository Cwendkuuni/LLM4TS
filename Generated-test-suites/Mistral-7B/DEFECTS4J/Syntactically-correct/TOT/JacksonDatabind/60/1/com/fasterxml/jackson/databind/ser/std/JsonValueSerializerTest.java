package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonValueSerializerTest {

    private ObjectMapper mapper;

    public JsonValueSerializerTest() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testSimpleJsonValueSerialization() throws JsonProcessingException {
        JsonValueSerializer serializer = new JsonValueSerializer(AnnotatedMethodTest.getSimpleJsonValueMethod(), null);
        Object testObject = new AnnotatedMethodTest().getValue();

        String expectedJson = "{\"value\":1}";

        String actualJson = mapper.writeValueAsString(testObject, new TypeSerializer() {});

        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testJsonValueSerializationWithContextualizer() throws JsonProcessingException {
        JsonValueSerializer serializer = new JsonValueSerializer(AnnotatedMethodTest.getContextualizerMethod(), null, null, false);
        Object testObject = new AnnotatedMethodTest().getValue();

        String expectedJson = "{\"value\":\"hello\"}";

        String actualJson = mapper.writeValueAsString(testObject, new TypeSerializer() {});

        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testJsonValueSerializationWithNullValue() throws JsonProcessingException {
        JsonValueSerializer serializer = new JsonValueSerializer(AnnotatedMethodTest.getSimpleJsonValueMethod(), null);
        Object testObject = new AnnotatedMethodTest().getValueWithNull();

        String expectedJson = "{}";

        String actualJson = mapper.writeValueAsString(testObject, new TypeSerializer() {});

        assertEquals(expectedJson, actualJson);
    }

    @Test(expected = JsonGenerationException.class)
    public void testJsonValueSerializationWithException() throws JsonProcessingException {
        JsonValueSerializer serializer = new JsonValueSerializer(AnnotatedMethodTest.getThrowingMethod(), null);
        Object testObject = new AnnotatedMethodTest().getValueWithException();

        mapper.writeValueAsString(testObject, new TypeSerializer() {});

        fail("Expected JsonGenerationException but did not occur.");
    }
}