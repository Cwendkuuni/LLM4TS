package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases for StringArrayDeserializer class.
 */
public class StringArrayDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    /**
     * Test deserialization of empty JSON array to empty String array.
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testEmptyArray() throws JsonProcessingException {
        String json = "[]";
        String[] result = mapper.readValue(json, String[].class);
        assertArrayEquals(new String[0], result);
    }

    /**
     * Test deserialization of JSON array containing single null value to String array with single null element.
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testNullValue() throws JsonProcessingException {
        String json = "[null]";
        String[] result = mapper.readValue(json, String[].class);
        assertArrayEquals(new String[]{null}, result);
    }

    /**
     * Test deserialization of JSON array containing single string value to String array with single string element.
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testStringValue() throws JsonProcessingException {
        String json = "[\"foo\"]";
        String[] result = mapper.readValue(json, String[].class);
        assertArrayEquals(new String[]{"foo"}, result);
    }

    /**
     * Test deserialization of JSON array containing multiple string values to String array.
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testMultipleStringValues() throws JsonProcessingException {
        String json = "[\"foo\", \"bar\", \"baz\"]";
        String[] result = mapper.readValue(json, String[].class);
        assertArrayEquals(new String[]{"foo", "bar", "baz"}, result);
    }

    /**
     * Test deserialization of JSON array with custom value deserializer to String array with custom string elements.
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testCustomDeserializer() throws JsonProcessingException {
        JsonDeserializer<String> customDeserializer = new StdDeserializer<>(String.class) {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.getValueAsString().toUpperCase();
            }
        };
        StringArrayDeserializer serializerWithCustomDeserializer = new StringArrayDeserializer(customDeserializer);
        ObjectMapper mapperWithCustomDeserializer = new ObjectMapper();
        mapperWithCustomDeserializer.registerSerializer(String[].class, serializerWithCustomDeserializer);
        String json = "[\"foo\", \"bar\", \"baz\"]";
        String[] result = mapperWithCustomDeserializer.readValue(json, String[].class);
        assertArrayEquals(new String[]{"FOO", "BAR", "BAZ"}, result);
    }

    /**
     * Test deserialization of JSON array with invalid JSON to throw JsonMappingException.
     * @throws JsonProcessingException if deserialization fails
     */
    @Test(expected = JsonMappingException.class)
    public void testInvalidJson() throws JsonProcessingException {
        String json = "{";
        mapper.readValue(json, String[].class);
    }
}