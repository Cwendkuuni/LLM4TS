package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.ObjectArrayDeserializerTest; // For TestCase annotation
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for StringArrayDeserializer
 */
@JacksonStdImpl
public final class StringArrayDeserializerTest {

    @Test
    public void testEmptyArraySerialization() throws Exception {
        String json = "[]";
        ObjectMapper mapper = new ObjectMapper();
        assertArrayEquals(new String[0], mapper.readValue(json, String[].class));
    }

    @Test
    public void testSingleElementSerialization() throws Exception {
        String json = "[\"hello\"]";
        ObjectMapper mapper = new ObjectMapper();
        String[] actual = mapper.readValue(json, String[].class);
        String[] expected = {"hello"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testMultipleElementsSerialization() throws Exception {
        String json = "[\"hello\", \"world\", null]";
        ObjectMapper mapper = new ObjectMapper();
        String[] actual = mapper.readValue(json, String[].class);
        String[] expected = {"hello", "world", null};
        assertArrayEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testNonArraySerialization() throws Exception {
        String json = "\"hello\"";
        ObjectMapper mapper = new ObjectMapper();
        String[] actual = mapper.readValue(json, String[].class);
        fail("Expected JsonMappingException but got: " + actual);
    }

    @Test
    public void testNullElementSerialization() throws Exception {
        String json = "[null]";
        ObjectMapper mapper = new ObjectMapper();
        String[] actual = mapper.readValue(json, String[].class);
        assertArrayEquals(new String[]{null}, actual);
    }

    @Test
    public void testCustomDeserializer() throws Exception {
        String json = "[\"Foo\", \"Bar\"]";
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> customDeserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.getText().toUpperCase();
            }
        };
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        String[] actual = mapper.readValue(json, deserializer.getType());
        String[] expected = {"FOO", "BAR"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testCustomDeserializerWithContextualization() throws Exception {
        String json = "[null, \"Foo\", \"Bar\"]";
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> customDeserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                if (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)) {
                    if (jp.getText().length() == 0) {
                        return null;
                    }
                }
                return jp.getText().toUpperCase();
            }
        };
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        String[] actual = mapper.readValue(json, deserializer.getType());
        String[] expected = {null, "FOO", "BAR"};
        assertArrayEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyObjectBuffer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getFactory().createContextualDeserializerContext();
        ObjectBuffer buffer = ctxt.leaseObjectBuffer();
        String[] array = buffer.completeAndClearBuffer(new Object[0], 0, String[].class);
        assertEquals(0, array.length);
        // This should fail because the buffer is empty
        mapper.readValue(new StringReader(""), new TypeReference<String[]>() {});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullTypeDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"hello\", \"world\"]";
        DeserializationContext ctxt = mapper.getFactory().createContextualDeserializerContext();
        TypeDeserializer nullTypeDeserializer = null;
        try {
            mapper.readValue(new StringReader(json), nullTypeDeserializer.getType());
            fail("Expected IllegalArgumentException but got: null");
        } catch (JsonProcessingException e) {
            // Expected exception
        }
    }

    @Test
    public void testNullElementInArray() throws Exception {
        String json = "[\"hello\", null, \"world\"]";
        ObjectMapper mapper = new ObjectMapper();
        String[] actual = mapper.readValue(json, String[].class);
        String[] expected = {"hello", null, "world"};
        assertArrayEquals(expected, actual);
    }
}