package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for {@link JsonNodeDeserializer} class.
 */
public class JsonNodeDeserializerTest {

    /**
     * Test deserialization of null value.
     */
    @Test
    public void testDeserializeNullValue() throws IOException {
        JsonParser p = new JsonFactory().createParser("null");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(null.getClass());
        JsonNode actual = deserializer.deserialize(p, new TestContext());
        assertEquals(JsonNode.Value.nullValue(), actual);
    }

    /**
     * Test deserialization of empty JSON object.
     */
    @Test
    public void testDeserializeEmptyObject() throws IOException {
        JsonParser p = new JsonFactory().createParser("{}");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode actual = (ObjectNode) deserializer.deserialize(p, new TestContext());
        assertTrue(actual.isEmpty());
    }

    /**
     * Test deserialization of JSON object with a single field.
     */
    @Test
    public void testDeserializeObjectWithSingleField() throws IOException {
        JsonParser p = new JsonFactory().createParser("{\"name\":\"John\"}");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode actual = (ObjectNode) deserializer.deserialize(p, new TestContext());
        assertEquals("John", actual.get("name").asText());
    }

    /**
     * Test deserialization of JSON object with multiple fields.
     */
    @Test
    public void testDeserializeObjectWithMultipleFields() throws IOException {
        JsonParser p = new JsonFactory().createParser("{" +
                "\"name\":\"John\"," +
                "\"age\":30" +
                "}");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode actual = (ObjectNode) deserializer.deserialize(p, new TestContext());
        assertEquals("John", actual.get("name").asText());
        assertEquals(30, actual.get("age").asInt());
    }

    /**
     * Test deserialization of JSON array.
     */
    @Test
    public void testDeserializeArray() throws IOException {
        JsonParser p = new JsonFactory().createParser("[1, 2, 3]");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        ArrayNode actual = (ArrayNode) deserializer.deserialize(p, new TestContext());
        assertEquals(3, actual.size());
        assertEquals(1, actual.get(0).asInt());
        assertEquals(2, actual.get(1).asInt());
        assertEquals(3, actual.get(2).asInt());
    }

    /**
     * Test deserialization of JSON object with duplicate fields.
     */
    @Test(expected = JsonMappingException.class)
    public void testDeserializeObjectWithDuplicateFields() throws IOException {
        JsonParser p = new JsonFactory().createParser("{" +
                "\"name\":\"John\"," +
                "\"name\":\"Doe\"" +
                "}");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode actual = (ObjectNode) deserializer.deserialize(p, new TestContext());
    }

    /**
     * Test deserialization of JSON object with empty array as a field.
     */
    @Test
    public void testDeserializeObjectWithEmptyArrayField() throws IOException {
        JsonParser p = new JsonFactory().createParser("{" +
                "\"name\":\"John\"," +
                "\"items\":[]" +
                "}");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode actual = (ObjectNode) deserializer.deserialize(p, new TestContext());
        ArrayNode items = (ArrayNode) actual.get("items");
        assertEquals(0, items.size());
    }

    /**
     * Test deserialization of JSON object with a field that is not a JSON object or array.
     */
    @Test
    public void testDeserializeObjectWithNonObjectArrayField() throws IOException {
        JsonParser p = new JsonFactory().createParser("{" +
                "\"name\":\"John\"," +
                "\"age\":30," +
                "\"value\":true" +
                "}");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode actual = (ObjectNode) deserializer.deserialize(p, new TestContext());
        assertTrue(actual.has("name"));
        assertTrue(actual.has("age"));
        assertTrue(actual.has("value"));
        assertEquals("John", actual.get("name").asText());
        assertEquals(30, actual.get("age").asInt());
        assertTrue(actual.get("value").isBoolean());
    }

    /**
     * Test deserialization of JSON object with a field that is an embedded JSON object.
     */
    @Test
    public void testDeserializeObjectWithEmbeddedObjectField() throws IOException {
        JsonParser p = new JsonFactory().createParser("{" +
                "\"name\":\"John\"," +
                "\"address\":{" +
                "\"street\":\"Main St\"," +
                "\"city\":\"Anytown\"" +
                "}" +
                "}");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode actual = (ObjectNode) deserializer.deserialize(p, new TestContext());
        ObjectNode address = (ObjectNode) actual.get("address");
        assertEquals("Main St", address.get("street").asText());
        assertEquals("Anytown", address.get("city").asText());
    }

    /**
     * Test deserialization of JSON object with a field that is an empty JSON object.
     */
    @Test
    public void testDeserializeObjectWithEmptyEmbeddedObjectField() throws IOException {
        JsonParser p = new JsonFactory().createParser("{" +
                "\"name\":\"John\"," +
                "\"address\":{}" +
                "}");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode actual = (ObjectNode) deserializer.deserialize(p, new TestContext());
        ObjectNode address = (ObjectNode) actual.get("address");
        assertTrue(address.isEmpty());
    }

    /**
     * Test deserialization of JSON object with a field that is a JSON array.
     */
    @Test
    public void testDeserializeObjectWithArrayField() throws IOException {
        JsonParser p = new JsonFactory().createParser("{" +
                "\"name\":\"John\"," +
                "\"items\":[1, 2, 3]" +
                "}");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode actual = (ObjectNode) deserializer.deserialize(p, new TestContext());
        ArrayNode items = (ArrayNode) actual.get("items");
        assertEquals(3, items.size());
        assertEquals(1, items.get(0).asInt());
        assertEquals(2, items.get(1).asInt());
        assertEquals(3, items.get(2).asInt());
    }

    /**
     * Test deserialization of JSON object with a field that is an array of empty JSON objects.
     */
    @Test
    public void testDeserializeObjectWithArrayOfEmptyEmbeddedObjects() throws IOException {
        JsonParser p = new JsonFactory().createParser("{" +
                "\"name\":\"John\"," +
                "\"items\":[]" +
                "}");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode actual = (ObjectNode) deserializer.deserialize(p, new TestContext());
        ArrayNode items = (ArrayNode) actual.get("items");
        assertEquals(0, items.size());
        for (JsonNode node : items) {
            assertTrue(node.isMissingNode());
        }
    }

    /**
     * Test deserialization of JSON object with a field that is an array of JSON objects.
     */
    @Test
    public void testDeserializeObjectWithArrayOfObjects() throws IOException {
        JsonParser p = new JsonFactory().createParser("{" +
                "\"name\":\"John\"," +
                "\"items\":[" +
                "{" +
                "\"name\":\"Item 1\"" +
                "}," +
                "{" +
                "\"name\":\"Item 2\"" +
                "}" +
                "]" +
                "}");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode actual = (ObjectNode) deserializer.deserialize(p, new TestContext());
        ArrayNode items = (ArrayNode) actual.get("items");
        assertEquals(2, items.size());
        ObjectNode item1 = (ObjectNode) items.get(0);
        assertEquals("Item 1", item1.get("name").asText());
        ObjectNode item2 = (ObjectNode) items.get(1);
        assertEquals("Item 2", item2.get("name").asText());
    }

    /**
     * Test deserialization of JSON object with a field that is an array of JSON arrays.
     */
    @Test
    public void testDeserializeObjectWithArrayOfArrays() throws IOException {
        JsonParser p = new JsonFactory().createParser("{" +
                "\"name\":\"John\"," +
                "\"items\":[" +
                "[1, 2]," +
                "[3, 4]" +
                "]" +
                "}");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode actual = (ObjectNode) deserializer.deserialize(p, new TestContext());
        ArrayNode items = (ArrayNode) actual.get("items");
        assertEquals(2, items.size());
        ArrayNode item1 = (ArrayNode) items.get(0);
        assertEquals(2, item1.size());
        assertEquals(1, item1.get(0).asInt());
        assertEquals(2, item1.get(1).asInt());
        ArrayNode item2 = (ArrayNode) items.get(1);
        assertEquals(2, item2.size());
        assertEquals(3, item2.get(0).asInt());
        assertEquals(4, item2.get(1).asInt());
    }

    /**
     * Test deserialization of JSON object with a field that contains invalid JSON.
     */
    @Test(expected = JsonMappingException.class)
    public void testDeserializeObjectWithInvalidJsonField() throws IOException {
        JsonParser p = new JsonFactory().createParser("{" +
                "\"name\":\"John\"," +
                "\"items\":[{ 'invalid': 1 }]" +
                "}");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode actual = (ObjectNode) deserializer.deserialize(p, new TestContext());
    }

    /**
     * Test deserialization of JSON object with a field that is an array of null values.
     */
    @Test
    public void testDeserializeObjectWithArrayOfNullValues() throws IOException {
        JsonParser p = new JsonFactory().createParser("{" +
                "\"name\":\"John\"," +
                "\"items\":[null, null]" +
                "}");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode actual = (ObjectNode) deserializer.deserialize(p, new TestContext());
        ArrayNode items = (ArrayNode) actual.get("items");
        assertEquals(2, items.size());
        for (JsonNode node : items) {
            assertNull(node);
        }
    }

    /**
     * Test deserialization of JSON object with a field that is an array of different types.
     */
    @Test
    public void testDeserializeObjectWithArrayOfDifferentTypes() throws IOException {
        JsonParser p = new JsonFactory().createParser("{" +
                "\"name\":\"John\"," +
                "\"items\":[" +
                "1," +
                "\"John\"" +
                "]" +
                "}");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode actual = (ObjectNode) deserializer.deserialize(p, new TestContext());
        ArrayNode items = (ArrayNode) actual.get("items");
        assertEquals(2, items.size());
        assertEquals(1, items.get(0).asInt());
        assertEquals("John", items.get(1).asText());
    }

    /**
     * Test deserialization of JSON object with a field that includes multiple JSON structures.
     */
    @Test
    public void testDeserializeObjectWithMultipleJsonStructures() throws IOException {
        JsonParser p = new JsonFactory().createParser("{" +
                "\"name\":\"John\"," +
                "\"items\":[" +
                "{" +
                "\"name\":\"Item 1\"" +
                "}," +
                "{ \"id\": 1, \"name\":\"Item 2\" }" +
                "]" +
                "}");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode actual = (ObjectNode) deserializer.deserialize(p, new TestContext());
        ArrayNode items = (ArrayNode) actual.get("items");
        assertEquals(2, items.size());
        ObjectNode item1 = (ObjectNode) items.get(0);
        assertEquals("Item 1", item1.get("name").asText());
        ObjectNode item2 = (ObjectNode) items.get(1);
        assertEquals(1, item2.get("id").asInt());
        assertEquals("Item 2", item2.get("name").asText());
    }

    /**
     * Test deserialization of JSON object with a missing field.
     */
    @Test(expected = JsonMappingException.class)
    public void testDeserializeObjectWithMissingField() throws IOException {
        JsonParser p = new JsonFactory().createParser("{" +
                "\"name\":\"John\"" +
                "}");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode actual = (ObjectNode) deserializer.deserialize(p, new TestContext());
        assertFalse(actual.has("missingField"));
    }

    /**
     * Test deserialization of JSON object with a missing root-level field.
     */
    @Test(expected = JsonMappingException.class)
    public void testDeserializeObjectWithMissingRootLevelField() throws IOException {
        JsonParser p = new JsonFactory().createParser("{}");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode actual = (ObjectNode) deserializer.deserialize(p, new TestContext());
        assertFalse(actual.has("missingField"));
    }

    /**
     * Return a shared instance of {@link TestContext} for all test methods.
     */
    static class TestContext extends BaseDeserializerTest.TestContext {
        // Empty implementation.
    }
}