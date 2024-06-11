package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringCollectionDeserializerTest {

    @Test
    public void testEmptyCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        JsonNode node = mapper.readTree(json);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, null, null, null);

        List<String> result = deserializer.deserialize(mapper.treeToValues(node, String[].class)[0], mapper.getConfig());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testNullCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "null";
        JsonNode node = mapper.readTree(json);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, null, null, null);

        List<String> result = deserializer.deserialize(mapper.treeToValues(node, String[].class)[0], mapper.getConfig());
        assertNull(result);
    }

    @Test
    public void testSingleElementCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"element\"]";
        JsonNode node = mapper.readTree(json);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, null, null, null);

        List<String> result = deserializer.deserialize(mapper.treeToValues(node, String[].class)[0], mapper.getConfig());
        assertEquals("element", result.get(0));
    }

    @Test
    public void testMultipleElementsCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"element1\", \"element2\"]";
        JsonNode node = mapper.readTree(json);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, null, null, null);

        List<String> result = deserializer.deserialize(mapper.treeToValues(node, String[].class)[0], mapper.getConfig());
        List<String> expected = Arrays.asList("element1", "element2");
        assertEquals(expected, result);
    }

    @Test
    public void testCustomValueDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, null, null, null);
        String json = "[\"element1\", null, \"element3\"]";
        JsonNode node = mapper.readTree(json);
        JsonDeserializer<String> customDeserializer = new StdDeserializer<String>() {};
        StringCollectionDeserializer customDeserializerWrapper = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, null, null, customDeserializer);

        List<String> resultWithDefaultDeserializer = deserializer.deserialize(mapper.treeToValues(node, String[].class)[0], mapper.getConfig());
        List<String> resultWithCustomDeserializer = customDeserializerWrapper.deserialize(mapper.treeToValues(node, String[].class)[0], mapper.getConfig());

        List<String> expectedWithDefaultDeserializer = Arrays.asList("element1", null, "element3");
        List<String> expectedWithCustomDeserializer = Arrays.asList("element1", null, "element3"); // no modification of the values

        assertEquals(expectedWithDefaultDeserializer, resultWithDefaultDeserializer);
        assertEquals(expectedWithCustomDeserializer, resultWithCustomDeserializer);
    }

    @Test(expected = JsonMappingException.class)
    public void testUnexpectedToken() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"key\": \"value\"}";
        JsonNode node = mapper.readTree(json);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, null, null, null);

        deserializer.deserialize(mapper.treeToValues(node, String[].class)[0], mapper.getConfig());
    }
}