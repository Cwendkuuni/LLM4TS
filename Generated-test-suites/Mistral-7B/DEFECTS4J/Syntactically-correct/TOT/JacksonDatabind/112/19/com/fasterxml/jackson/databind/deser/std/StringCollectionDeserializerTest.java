package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.test.BaseMapTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class StringCollectionDeserializerTest extends BaseMapTest {

    public static void main(String[] args) throws Exception {
        runTests();
    }

    public static void runTests() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        testEmptyCollection(deserializer);
        testStringCollectionWithDefaultSerializer(deserializer);
        testStringCollectionWithCustomSerializer(deserializer);
        testNullValue(deserializer);
        testInvalidJsonFormat(deserializer);
    }

    // Test for deserializing an empty collection
    public static void testEmptyCollection(StringCollectionDeserializer deserializer) throws IOException {
        JsonParser parser = createParser("[]");
        Collection<String> result = deserializer.deserialize(parser, null);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    // Test for deserializing a collection with default String deserializer
    public static void testStringCollectionWithDefaultSerializer(StringCollectionDeserializer deserializer) throws IOException {
        JsonParser parser = createParser("[\"hello\", \"world\"]");
        Collection<String> result = deserializer.deserialize(parser, null);
        assertEquals(2, result.size());
        assertEquals("hello", result.get(0));
        assertEquals("world", result.get(1));
    }

    // Test for deserializing a collection with custom String deserializer
    public static void testStringCollectionWithCustomSerializer(StringCollectionDeserializer deserializer) throws IOException {
        // Assuming a custom String deserializer is provided
        JsonDeserializer<String> customDeserializer = createMockStringDeserializer();
        StringCollectionDeserializer customDeserializerWrapper = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, customDeserializer, null, null);

        JsonParser parser = createParser("[\"custom1\", \"custom2\"]");
        Collection<String> result = customDeserializerWrapper.deserialize(parser, null);
        assertEquals(2, result.size());
        assertEquals("custom1", result.get(0));
        assertEquals("custom2", result.get(1));
    }

    // Test for deserializing a collection with a null value
    public static void testNullValue(StringCollectionDeserializer deserializer) throws IOException {
        JsonParser parser = createParser("[]");
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        NullValueProvider nullProvider = mapper.getFactory().getNullValueProvider();
        collectionType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);

        deserializer._nullProvider = nullProvider;

        Collection<String> result = deserializer.deserialize(parser, null);
        assertNotNull(result);
        assertEquals(0, result.size());

        parser = createParser("null");
        result = deserializer.deserialize(parser, null);
        assertNotNull(result);
        assertEquals(0, result.size());

        String nullValue = (String) nullProvider.getNullValue(null);
        Collection<String> resultWithNull = deserializer.deserialize(parser, null);
        assertNotNull(resultWithNull);
        assertEquals(1, resultWithNull.size());
        assertEquals(nullValue, resultWithNull.get(0));
    }

    // Test for deserializing a collection with invalid JSON format
    public static void testInvalidJsonFormat(StringCollectionDeserializer deserializer) throws IOException {
        JsonParser parser = createParser("{\"invalid\": []}");
        try {
            deserializer.deserialize(parser, null);
            fail("Expected JsonMappingException for invalid JSON format");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    // Helper method to create a JsonParser instance
    private static JsonParser createParser(String json) throws IOException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, createMockStringDeserializer());
        mapper.registerModule(module);
        ObjectReader reader = mapper.readerFor(collectionType);
        JsonParser parser = reader.readValueAsTree(json).traverse();
        parser.nextToken(); // Skip START_OBJECT
        return parser;
    }

    // Helper method to create a mock String deserializer
    private static JsonDeserializer<String> createMockStringDeserializer() {
        return new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String result = p.getValueAsString();
                return result;
            }
        };
    }
}