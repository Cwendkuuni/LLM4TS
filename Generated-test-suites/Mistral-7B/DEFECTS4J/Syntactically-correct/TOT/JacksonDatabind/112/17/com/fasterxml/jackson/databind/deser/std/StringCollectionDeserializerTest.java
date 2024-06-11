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
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Unit tests for {@link StringCollectionDeserializer}.
 */
public class StringCollectionDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testEmptyCollectionDeserialization() throws IOException {
        String json = "[]";
        Collection<String> result = mapper.readValue(json, StringCollectionDeserializer.class);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSingleElementCollectionDeserialization() throws IOException {
        String json = "[\"Alice\"]";
        Collection<String> result = mapper.readValue(json, StringCollectionDeserializer.class);
        assertEquals(1, result.size());
        assertEquals("Alice", result.iterator().next());
    }

    @Test
    public void testMultipleElementsCollectionDeserialization() throws IOException {
        String json = "[\"Alice\", \"Bob\", \"Charlie\"]";
        Collection<String> result = mapper.readValue(json, StringCollectionDeserializer.class);
        assertEquals(3, result.size());
        List<String> expected = Arrays.asList("Alice", "Bob", "Charlie");
        assertEquals(expected, result);
    }

    @Test
    public void testNullValueCollectionDeserialization() throws IOException {
        String json = "[null]";
        Collection<String> result = mapper.readValue(json, StringCollectionDeserializer.class);
        assertEquals(1, result.size());
        assertTrue(result.contains(null));
    }

    @Test
    public void testCustomValueDeserializer() throws IOException {
        // Given: Custom String deserializer
        JsonDeserializer<String> customValueDeserializer = new StringDeserializer();

        // When: Deserialize a JSON string using the custom String deserializer
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.constructType(Collection.class, String.class),
                null, null, null, null, null);
        deserializer._valueDeserializer = customValueDeserializer;

        String json = "[\"Alice\", \"Bob\"]";
        Collection<String> result = mapper.readValue(json, deserializer.getClass());

        // Then: Verify the deserialized collection contains the expected strings
        assertEquals(2, result.size());
        List<String> expected = Arrays.asList("Alice", "Bob");
        assertEquals(expected, result);
    }

    @Test
    public void testEmptyArrayDeserialization() throws IOException {
        String json = "[]";
        try {
            mapper.readValue(json, StringCollectionDeserializer.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected: Error due to empty array without any content
            assertTrue(e.getMessage().contains("No content to map to Collection"));
        }
    }

    @Test
    public void testInvalidJsonFormat() throws IOException {
        String json = "[1, 2, 3]";
        try {
            // When: Attempt to deserialize a JSON array containing non-String elements
            mapper.readValue(json, StringCollectionDeserializer.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Then: Verify the exception message contains the correct error message
            assertTrue(e.getMessage().contains("Unexpected token (START_ARRAY), expected a String"));
        }
    }

    @Test
    public void testUnexpectedJsonToken() throws IOException {
        String json = "{\"name\": \"Alice\"}";
        try {
            // When: Attempt to deserialize a JSON object instead of an array
            mapper.readValue(json, StringCollectionDeserializer.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Then: Verify the exception message contains the correct error message
            assertTrue(e.getMessage().contains("Expected ARRAY BUT FOUND {"));
        }
    }

    private static class StringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }
}