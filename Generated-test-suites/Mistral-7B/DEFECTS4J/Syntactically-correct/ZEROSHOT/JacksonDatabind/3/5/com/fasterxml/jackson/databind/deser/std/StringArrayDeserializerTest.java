package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

import org.junit.Test;

/**
 * Test cases for {@link StringArrayDeserializer} class.
 */
public class StringArrayDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Test deserialization of simple String array using default deserializer.
     */
    @Test
    public void testDeserializationDefault() throws Exception {
        String json = "[\"hello\",\"world\"]";
        String[] result = mapper.readValue(json, StringArrayDeserializer.class);
        assertEquals(2, result.length);
        assertEquals("hello", result[0]);
        assertEquals("world", result[1]);
    }

    /**
     * Test deserialization of simple String array using custom element deserializer.
     */
    @Test
    public void testDeserializationCustom() throws Exception {
        String json = "[{\"value\":\"hello\"},{\"value\":\"world\"}]";
        JsonDeserializer<String> elementDeserializer = mapper.getFactory().createValueDeserializer(String.class, mapper.getConfiguration());
        StringArrayDeserializer serializer = new StringArrayDeserializer(elementDeserializer);
        String[] result = mapper.readValue(json, StringArrayDeserializer.class);
        assertEquals(2, result.length);
        assertEquals("hello", result[0]);
        assertEquals("world", result[1]);
    }

    /**
     * Test handling non-array input.
     */
    @Test(expected = JsonMappingException.class)
    public void testHandlingNonArray() throws Exception {
        String json = "\"hello\"";
        mapper.readValue(json, StringArrayDeserializer.class);
        fail("Expected JsonMappingException");
    }

    /**
     * Test deserialization with type handler.
     */
    @Test
    public void testDeserializationWithTypeHandler() throws Exception {
        String json = "[1, 2, 3]"; // This should be deserialized as String[] because of the type handler
        Object result = mapper.readValue(json, String[].class);
        assertArrayEquals(new String[]{"1", "2", "3"}, (String[]) result);
    }

    /**
     * Test contextual deserialization.
     */
    @Test
    public void testContextualDeserialization() throws Exception {
        String json = "[{\"value\":\"hello\"},{\"value\":\"world\"}]";
        ObjectMapper mapperWithContextualizer = new ObjectMapper();
        mapperWithContextualizer.registerModule(new SimpleModule() {
            @Override
            public void setupModule(SerializationConfig config) throws Exception {
                addSerializer(config.findValueSerializer(String[].class), new StringArraySerializer(mapperWithContextualizer.getFactory().createValueDeserializer(String.class, mapperWithContextualizer.getConfiguration())));
            }
        });

        String[] result = mapperWithContextualizer.readValue(json, StringArrayDeserializer.class);
        assertEquals(2, result.length);
        assertEquals("hello", result[0]);
        assertEquals("world", result[1]);
    }
}