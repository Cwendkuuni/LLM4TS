package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.BaseNodeDeserializer;
import com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Test;

public class JsonNodeDeserializerTest {

    @Test
    public void testNullValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonParser parser = mapper.getFactory().createParser("null");
        JsonNode result = deserializer.deserialize(parser, null);
        assertEquals(JsonNode.Value.NULL, result.getValueAsToken());
    }

    @Test
    public void testDeserializeObject() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonParser parser = mapper.getFactory().createParser("{\"name\":\"John\",\"age\":30}");
        ObjectNode result = (ObjectNode) deserializer.deserialize(parser, null);
        assertEquals("John", result.get("name").asText());
        assertEquals(30, result.get("age").asInt());
    }

    @Test
    public void testDeserializeArray() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonParser parser = mapper.getFactory().createParser("[1,2,3]");
        ArrayNode result = (ArrayNode) deserializer.deserialize(parser, null);
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).asInt());
        assertEquals(2, result.get(1).asInt());
        assertEquals(3, result.get(2).asInt());
    }

    @Test
    public void testDeserializeAny() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonParser parser = mapper.getFactory().createParser("{\"name\":\"John\",\"age\":30}");
        JsonNode result = deserializer.deserializeAny(parser, null, null);
        assertEquals(JsonNode.Type.OBJECT, result.getType());
        ObjectNode objResult = (ObjectNode) result;
        assertEquals("John", objResult.get("name").asText());
        assertEquals(30, objResult.get("age").asInt());

        parser = mapper.getFactory().createParser("[1,2,3]");
        result = deserializer.deserializeAny(parser, null, null);
        assertEquals(JsonNode.Type.ARRAY, result.getType());
        ArrayNode arrResult = (ArrayNode) result;
        assertEquals(3, arrResult.size());
        assertEquals(1, arrResult.get(0).asInt());
        assertEquals(2, arrResult.get(1).asInt());
        assertEquals(3, arrResult.get(2).asInt());

        parser = mapper.getFactory().createParser("{\"name\":\"John\"}");
        result = deserializer.deserializeAny(parser, null, null);
        assertEquals(JsonNode.Type.OBJECT, result.getType());
        ObjectNode objResult2 = (ObjectNode) result;
        assertEquals("John", objResult2.get("name").asText());

        parser = mapper.getFactory().createParser("{}");
        result = deserializer.deserializeAny(parser, null, null);
        assertEquals(JsonNode.Type.OBJECT, result.getType());
        ObjectNode objResult3 = (ObjectNode) result;
        assertTrue(objResult3.isEmpty());

        parser = mapper.getFactory().createParser("\"Hello World\"");
        result = deserializer.deserializeAny(parser, null, null);
        assertEquals(JsonNode.Type.TEXT, result.getType());
        TextNode textResult = (TextNode) result;
        assertEquals("Hello World", textResult.asText());

        parser = mapper.getFactory().createParser("true");
        result = deserializer.deserializeAny(parser, null, null);
        assertEquals(JsonNode.Type.BOOLEAN, result.getType());
        BooleanNode boolResult = (BooleanNode) result;
        assertTrue(boolResult.booleanValue());

        parser = mapper.getFactory().createParser("false");
        result = deserializer.deserializeAny(parser, null, null);
        assertEquals(JsonNode.Type.BOOLEAN, result.getType());
        BooleanNode boolResult2 = (BooleanNode) result;
        assertFalse(boolResult2.booleanValue());

        parser = mapper.getFactory().createParser("null");
        result = deserializer.deserializeAny(parser, null, null);
        assertEquals(JsonNode.Type.NULL, result.getType());
    }

    @Test
    public void testDeserializeWithType() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        ObjectNode node = mapper.createObjectNode();
        node.put("name", "John");
        node.put("age", 30);
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        JsonParser parser = mapper.getFactory().createParser(json);

        Object result = deserializer.deserializeWithType(parser, null, null);
        ObjectNode deserializedNode = (ObjectNode) result;
        assertEquals("John", deserializedNode.get("name").asText());
        assertEquals(30, deserializedNode.get("age").asInt());
    }

    @Test
    public void testIsCachable() throws JsonProcessingException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        assertTrue(deserializer.isCachable());
    }

    @Test(expected = JsonMappingException.class)
    public void testDuplicateField() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonParser parser = mapper.getFactory().createParser("{\"name\":\"John\",\"name\":\"Doe\"}");
        deserializer._handleDuplicateField(parser, null, null, "name", null, null, null);
    }
}