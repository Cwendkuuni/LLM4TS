package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.integration.DeserializationTestBase;
import com.fasterxml.jackson.databind.node.*;

import static org.junit.Assert.*;

public class JsonNodeDeserializerTest extends DeserializationTestBase {

    public JsonNodeDeserializerTest() throws Exception {
        super(new JsonNodeDeserializer());
    }

    /**
     * Test deserializing an empty JSON object using the generic deserializer.
     */
    public void testDeserializeEmptyObject() throws Exception {
        String json = "{}";
        ObjectNode result = mapper.readValue(json, ObjectNode.class);
        assertTrue(result.isEmpty());
    }

    /**
     * Test deserializing a JSON object with a single string value using the generic deserializer.
     */
    public void testDeserializeObjectWithStringValue() throws Exception {
        String json = "{\"name\": \"John\"}";
        ObjectNode result = mapper.readValue(json, ObjectNode.class);
        assertEquals("John", result.get("name").asText());
    }

    /**
     * Test deserializing a JSON object with a single integer value using the generic deserializer.
     */
    public void testDeserializeObjectWithIntegerValue() throws Exception {
        String json = "{\"age\": 25}";
        ObjectNode result = mapper.readValue(json, ObjectNode.class);
        assertEquals(25, result.get("age").asInt());
    }

    /**
     * Test deserializing a JSON object with a single boolean value using the generic deserializer.
     */
    public void testDeserializeObjectWithBooleanValue() throws Exception {
        String json = "{\"isValid\": true}";
        ObjectNode result = mapper.readValue(json, ObjectNode.class);
        assertTrue(result.get("isValid").asBoolean());
    }

    /**
     * Test deserializing a JSON object with a single null value using the generic deserializer.
     */
    public void testDeserializeObjectWithNullValue() throws Exception {
        String json = "{\"age\": null}";
        ObjectNode result = mapper.readValue(json, ObjectNode.class);
        assertNull(result.get("age"));
    }

    /**
     * Test deserializing a JSON object with multiple fields using the generic deserializer.
     */
    public void testDeserializeObjectWithMultipleFields() throws Exception {
        String json = "{\"name\": \"John\", \"age\": 25}";
        ObjectNode result = mapper.readValue(json, ObjectNode.class);
        assertEquals("John", result.get("name").asText());
        assertEquals(25, result.get("age").asInt());
    }

    /**
     * Test deserializing a JSON array using the generic deserializer.
     */
    public void testDeserializeArray() throws Exception {
        String json = "[1, 2, 3]";
        ArrayNode result = mapper.readValue(json, ArrayNode.class);
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).asInt());
        assertEquals(2, result.get(1).asInt());
        assertEquals(3, result.get(2).asInt());
    }

    /**
     * Test deserializing a JSON object with duplicate fields using the generic deserializer.
     */
    public void testDeserializeObjectWithDuplicateFields() throws Exception {
        String json = "{\"name\": \"John\", \"name\": \"Jane\"}";
        ObjectNode result = mapper.readValue(json, ObjectNode.class);
        assertEquals("Jane", result.get("name").asText());
    }

    /**
     * Test deserializing a JSON object with an empty array field using the generic deserializer.
     */
    public void testDeserializeObjectWithEmptyArrayField() throws Exception {
        String json = "{\"items\": []}";
        ObjectNode result = mapper.readValue(json, ObjectNode.class);
        assertTrue(result.get("items").isEmpty());
    }

    /**
     * Test deserializing a JSON object with an embedded JSON object using the generic deserializer.
     */
    public void testDeserializeObjectWithEmbeddedObject() throws Exception {
        String json = "{\"person\": {\"name\": \"John\", \"age\": 25}}";
        ObjectNode result = mapper.readValue(json, ObjectNode.class);
        ObjectNode personNode = (ObjectNode) result.get("person");
        assertEquals("John", personNode.get("name").asText());
        assertEquals(25, personNode.get("age").asInt());
    }

    /**
     * Test deserializing a JSON object with an embedded JSON array using the generic deserializer.
     */
    public void testDeserializeObjectWithEmbeddedArray() throws Exception {
        String json = "{\"items\": [1, 2, 3]}";
        ObjectNode result = mapper.readValue(json, ObjectNode.class);
        ArrayNode itemsNode = (ArrayNode) result.get("items");
        assertEquals(3, itemsNode.size());
        assertEquals(1, itemsNode.get(0).asInt());
        assertEquals(2, itemsNode.get(1).asInt());
        assertEquals(3, itemsNode.get(2).asInt());
    }

    /**
     * Test deserializing a JSON object with a field that is expected to be an array but contains an object instead.
     */
    public void testDeserializeObjectWithUnexpectedObject() throws Exception {
        String json = "{\"items\": {\"name\": \"John\"}}";
        ObjectNode result = mapper.readValue(json, ObjectNode.class);
        ObjectNode itemsNode = (ObjectNode) result.get("items");
        assertEquals("John", itemsNode.get("name").asText());
        // This test should fail because the JSON object does not match the expected array type
        //assertArrayEquals(new Object[]{}, itemsNode.toArray());
        fail("Unexpectedly deserialized object instead of array");
    }

    /**
     * Test deserializing a JSON object with a field that is expected to be a string but contains an array instead.
     */
    public void testDeserializeObjectWithUnexpectedArray() throws Exception {
        String json = "{\"name\": [1, 2, 3]}";
        ObjectNode result = mapper.readValue(json, ObjectNode.class);
        ArrayNode nameNode = (ArrayNode) result.get("name");
        assertEquals(3, nameNode.size());
        assertEquals(1, nameNode.get(0).asInt());
        assertEquals(2, nameNode.get(1).asInt());
        assertEquals(3, nameNode.get(2).asInt());
        // This test should pass because the JSON array was correctly deserialized instead of failing
    }
}