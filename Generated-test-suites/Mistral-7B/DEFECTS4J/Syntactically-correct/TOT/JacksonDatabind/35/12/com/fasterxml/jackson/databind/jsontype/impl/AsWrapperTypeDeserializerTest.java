package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;

import static org.junit.Assert.*;

public class AsWrapperTypeDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
            mapper.getTypeFactory().constructType(String.class),
            mapper.getTypeIdResolver(),
            "_type",
            true,
            String.class
    );

    @Test
    public void testDeserializationWithValidTypeId() throws Exception {
        String json = "{\"type\":\"java.lang.String\",\"value\":\"Hello World\"}";
        Object actual = mapper.readValue(json, deserializer.getType());
        assertEquals("Hello World", actual);
    }

    @Test
    public void testDeserializationWithInvalidTypeId() throws Exception {
        String json = "{\"type\":\"java.util.List\",\"value\":[1,2,3]}";
        try {
            mapper.readValue(json, deserializer.getType());
            fail("Expected JsonProcessingException");
        } catch (JsonMappingException e) {
            assertTrue(e.getCause() instanceof IOException);
            assertTrue(e.getCause().getMessage().contains("need JSON String that contains type id"));
        }
    }

    @Test
    public void testDeserializationWithMissingTypeId() throws Exception {
        String json = "{\"value\":\"Hello World\"}";
        try {
            mapper.readValue(json, deserializer.getType());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertTrue(e.getCause() instanceof IOException);
            assertTrue(e.getCause().getMessage().contains("need JSON Object to contain As.WRAPPER_OBJECT type information"));
        }
    }

    @Test
    public void testDeserializationWithMissingValue() throws Exception {
        String json = "{\"type\":\"java.lang.String\"}";
        try {
            mapper.readValue(json, deserializer.getType());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertTrue(e.getCause() instanceof IOException);
            assertTrue(e.getCause().getMessage().contains("expected closing END_OBJECT after type information and deserialized value"));
        }
    }

    @Test
    public void testDeserializationWithNullValue() throws Exception {
        String json = "{\"type\":\"java.lang.String\",\"value\":null}";
        Object actual = mapper.readValue(json, deserializer.getType());
        assertNull(actual);
    }

    @Test
    public void testDeserializationWithEmptyValue() throws Exception {
        String json = "{\"type\":\"java.lang.String\",\"value\":\"\"}";
        String actual = (String) mapper.readValue(json, deserializer.getType());
        assertEquals("", actual);
    }

    @Test
    public void testDeserializationWithEmptyObject() throws Exception {
        String json = "{}";
        try {
            mapper.readValue(json, deserializer.getType());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertTrue(e.getCause() instanceof IOException);
            assertTrue(e.getCause().getMessage().contains("need JSON Object to contain As.WRAPPER_OBJECT type information"));
        }
    }
}