package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

/**
 * Test class for StringArrayDeserializer
 */
public class StringArrayDeserializerTest {

    /**
     * Test for deserializing an empty String array using the default deserializer.
     *
     * @throws IOException
     */
    @Test
    public void testDeserializeEmptyArray() throws IOException {
        String json = "[]";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        String[] result = mapper.readValue(json, deserializer.getType());
        assertArrayEquals(new String[0], result);
    }

    /**
     * Test for deserializing a String array with single value using the default deserializer.
     *
     * @throws IOException
     */
    @Test
    public void testDeserializeSingleValueArray() throws IOException {
        String json = "[\"hello\"]";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        String[] result = mapper.readValue(json, deserializer.getType());
        assertArrayEquals(new String[] {"hello"}, result);
    }

    /**
     * Test for deserializing a String array with multiple values using the default deserializer.
     *
     * @throws IOException
     */
    @Test
    public void testDeserializeMultipleValuesArray() throws IOException {
        String json = "[\"hello\",\"world\",null]";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        String[] result = mapper.readValue(json, deserializer.getType());
        assertArrayEquals(new String[] {"hello", "world", null}, result);
    }

    /**
     * Test for deserializing a String array with a custom element deserializer.
     *
     * @throws IOException
     */
    @Test
    public void testDeserializeCustomElementDeserializer() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer(new BasicJsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.getValueAsString();
            }
        });
        String json = "[\"hello\",\"world\",null]";
        ObjectMapper mapper = new ObjectMapper();
        String[] result = mapper.readValue(json, deserializer.getType());
        assertArrayEquals(new String[] {"hello", "world", null}, result);
    }

    /**
     * Test for deserializing a String array with no element deserializer.
     *
     * @throws IOException
     */
    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithoutElementDeserializer() throws IOException {
        String json = "[1,2,3]";
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        ObjectMapper mapper = new ObjectMapper();
        mapper.readValue(json, deserializer.getType());
    }

    /**
     * Test for deserializing a String array when the input is not an array.
     *
     * @throws IOException
     */
    @Test(expected = JsonMappingException.class)
    public void testDeserializeNonArray() throws IOException {
        String json = "{\"name\":\"John\"}";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        mapper.readValue(json, deserializer.getType());
    }

    /**
     * Test for deserializing a String array when an empty string is treated as a null value.
     *
     * @throws IOException
     */
    @Test
    public void testDeserializeEmptyStringAsNull() throws IOException {
        String json = "[\"\",\"hello\",null]";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForArrayOf(String.class);
        String[] result = reader.readValue(json);
        assertArrayEquals(new String[] {"", "hello", null}, result);
    }
}