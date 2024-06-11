package com.example.junit4.test.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer;
import org.junit.Before;
import org.junit.Test;

import java.util.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AtomicReferenceDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDeserializeNullValue() throws JsonProcessingException {
        AtomicReference<Object> expectedNullValue = new AtomicReference<>();
        String json = "null";

        AtomicReference<Object> actualValue = objectMapper.readValue(json, new TypeReference<AtomicReference<Object>>() {});
        assertEquals(expectedNullValue, actualValue);
    }

    @Test
    public void testDeserializeEmptyValue() throws JsonProcessingException {
        AtomicReference<Object> expectedEmptyValue = new AtomicReference<>();
        String json = "{}";

        AtomicReference<Object> actualValue = objectMapper.readValue(json, new TypeReference<AtomicReference<Object>>() {});
        assertEquals(expectedEmptyValue, actualValue);
    }

    @Test
    public void testDeserializeReferenceValue() throws JsonProcessingException {
        Object referenceValue = new Object();
        AtomicReference<Object> expectedValue = new AtomicReference<>(referenceValue);
        String json = "{\"@type\":\"com.fasterxml.jackson.databind.deser.std.AtomicReference\",\"referenceValue\":{\"@type\":\"java.lang.Object\",\"$ref\":\"#/properties/referenceValue\"}}";

        AtomicReference<Object> actualValue = objectMapper.readValue(json, new TypeReference<AtomicReference<Object>>() {});
        assertEquals(expectedValue, actualValue);
        assertEquals(referenceValue, actualValue.get());
    }

    @Test
    public void testDeserializeUpdateReference() throws JsonProcessingException {
        Object updateValue = new Object();
        AtomicReference<Object> expectedValue = new AtomicReference<>(new Object());
        expectedValue = objectMapper.readerForUpdating(expectedValue).readValue(new ObjectMapper(), jsonForUpdate(updateValue));

        String json = "{\"@type\":\"com.fasterxml.jackson.databind.deser.std.AtomicReference\",\"referenceValue\":{\"@type\":\"java.lang.Object\",\"$ref\":\"#/properties/referenceValue\"}}";

        AtomicReference<Object> actualValue = objectMapper.readValue(json, new TypeReference<AtomicReference<Object>>() {});
        assertEquals(expectedValue, actualValue);
        assertEquals(updateValue, actualValue.get());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidJson() throws JsonProcessingException {
        String json = "{\"@type\":\"com.fasterxml.jackson.databind.deser.std.AtomicReference\",\"referenceValue\":\"invalidJson\"}";
        objectMapper.readValue(json, new TypeReference<AtomicReference<Object>>() {});
    }

    @Test
    public void testDeserializeNullJson() throws JsonProcessingException {
        String json = null;
        assertNull(objectMapper.readValue(json, new TypeReference<AtomicReference<Object>>() {}));
    }

    private String jsonForUpdate(Object updateValue) {
        return String.format("{\"@type\":\"com.fasterxml.jackson.databind.deser.std.AtomicReference\",\"referenceValue\":{\"@type\":\"java.lang.Object\",\"$ref\":\"#/properties/referenceValue\"}}");
    }
}