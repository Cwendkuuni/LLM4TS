package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import org.junit.*;

/**
 * Tests for {@link JsonNodeDeserializer}.
 */
public class JsonNodeDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testNullValueDeserialization() throws Exception {
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.getFactory().createParser("null"), null);
        assertNotNull(node);
        assertEquals(JsonNodeType.NULL, node.getNodeType());
    }

    @Test
    public void testDeserializationOfJsonObject() throws Exception {
        String json = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.getFactory().createParser(json), null);

        ObjectNode objNode = (ObjectNode) node;
        assertEquals("John", objNode.get("name").textValue());
        assertEquals(30, objNode.get("age").asInt());
        assertEquals("New York", objNode.get("city").textValue());
    }

    @Test
    public void testDeserializationOfJsonObjectWithEmptyName() throws Exception {
        String json = "{}";
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.getFactory().createParser(json), null);

        ObjectNode objNode = (ObjectNode) node;
        assertTrue(objNode.isEmpty());
    }

    @Test
    public void testDeserializationOfJsonArray() throws Exception {
        String json = "[1, 2, 3]";
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.getFactory().createParser(json), null);

        ArrayNode arrayNode = (ArrayNode) node;
        assertEquals(3, arrayNode.size());
        assertEquals(1, arrayNode.get(0).asInt());
        assertEquals(2, arrayNode.get(1).asInt());
        assertEquals(3, arrayNode.get(2).asInt());
    }

    @Test
    public void testDeserializationOfMixedJsonObjectAndJsonArray() throws Exception {
        String json = "{\"name\":\"John\", 1, 2, [\"apple\", \"banana\"]}";
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.getFactory().createParser(json), null);

        ObjectNode objNode = (ObjectNode) node;
        assertEquals("John", objNode.get("name").textValue());

        ArrayNode arrayNode = (ArrayNode) objNode.get(1);
        assertEquals(2, arrayNode.size());
        assertEquals("apple", arrayNode.get(0).textValue());
        assertEquals("banana", arrayNode.get(1).textValue());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithInvalidJson() throws Exception {
        String json = "{\"name\":\"John\"age:30}"; // missing comma
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.getFactory().createParser(json), null);
        fail("Expected JsonMappingException but got " + node.getClass().getName());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithInvalidJson2() throws Exception {
        String json = "[1, 2,\"apple"; // missing closing bracket
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.getFactory().createParser(json), null);
        fail("Expected JsonMappingException but got " + node.getClass().getName());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithInvalidJson3() throws Exception {
        String json = "[1, 2, 3, 4.5]"; // invalid JsonNode type
        JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.getFactory().createParser(json), null);
        fail("Expected JsonMappingException but got " + node.getClass().getName());
    }

    @AfterClass
    public static void cleanup() throws Exception {
        mapper.close();
    }
}