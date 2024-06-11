package com.example.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StringCollectionDeserializerTest {

    @Test
    public void testDefaultDeserialization() throws JsonProcessingException {
        String json = "[\"abc\", \"def\", \"ghi\"]";
        ObjectMapper mapper = new ObjectMapper();
        Collection<String> result = mapper.readValue(json, StringCollectionDeserializer.class);
        assertEquals(3, result.size());
        assertEquals("abc", result.get(0));
        assertEquals("def", result.get(1));
        assertEquals("ghi", result.get(2));
    }

    @Test
    public void testCustomDeserialization() throws JsonProcessingException {
        // Provide a custom JsonDeserializer for String
        JsonDeserializer<String> customStringDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                String value = p.nextTextValue();
                if ("custom".equals(value)) {
                    return "CustomValue";
                }
                return value;
            }
        };

        String json = "[\"abc\", \"def\", \"custom\"]";
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForArrayOf(String.class);
        reader = reader.withValueDeserializer(customStringDeserializer);
        Collection<String> result = mapper.readValue(json, reader);
        assertEquals(3, result.size());
        assertEquals("abc", result.get(0));
        assertEquals("CustomValue", result.get(2));
    }

    @Test
    public void testNullValues() throws JsonProcessingException {
        String json = "[null, \"abc\", null]";
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForArrayOf(String.class);
        Collection<String> result = mapper.readValue(json, reader);
        assertEquals(3, result.size());
        assertNull(result.get(0));
        assertEquals("abc", result.get(1));
        assertNull(result.get(2));
    }

    @Test
    public void testEmptyArray() throws JsonProcessingException {
        String json = "[]";
        ObjectMapper mapper = new ObjectMapper();
        Collection<String> result = mapper.readValue(json, StringCollectionDeserializer.class);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testValueDeserializerIsNull() throws JsonProcessingException {
        String json = "[\"abc\", \"def\", \"ghi\"]";
        ObjectMapper mapper = new ObjectMapper();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                JavaType.constructCollectionType(List.class, String.class), null, null);
        Collection<String> result = mapper.readerForListOf(String.class).readValue(json, deserializer);
        assertEquals(3, result.size());
        assertEquals("abc", result.get(0));
        assertEquals("def", result.get(1));
        assertEquals("ghi", result.get(2));
    }

    @Test
    public void testUnwrappingSingleValue() throws JsonProcessingException {
        String json = "[\"abc\"]";
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForArrayOf(String.class);
        reader = reader.with(DeserializationFeature.UNWRAP_SINGLE_VALUE_AS_ARRAY_FLAG, true);
        Collection<String> result = mapper.readValue(json, reader);
        assertEquals(1, result.size());
        assertEquals("abc", result.get(0));
    }

    @Test
    public void testNullContentConverter() throws JsonProcessingException {
        String json = "[null]";
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> valueDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                return null;
            }
        };
        Collection<String> result = mapper.readerForArrayOf(String.class)
                .withValueDeserializer(valueDeserializer).readValue(json);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testEmptyContentConverter() throws JsonProcessingException {
        String json = "[]";
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> valueDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                return "";
            }
        };
        Collection<String> result = mapper.readerForArrayOf(String.class)
                .withValueDeserializer(valueDeserializer).readValue(json);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testContentConverterThrowsException() throws JsonProcessingException {
        String json = "[\"abc\"]";
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> valueDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                throw new RuntimeException("Test exception");
            }
        };
        assertThrows(JsonMappingException.class, () -> {
            mapper.readerForArrayOf(String.class)
                    .withValueDeserializer(valueDeserializer).readValue(json);
        });
    }
}