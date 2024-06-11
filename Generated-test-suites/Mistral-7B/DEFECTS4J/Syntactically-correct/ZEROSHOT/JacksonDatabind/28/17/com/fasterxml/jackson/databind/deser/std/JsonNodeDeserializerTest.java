package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * Test class for {@link JsonNodeDeserializer}
 */
public class JsonNodeDeserializerTest {

    private static ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void setup() throws Exception {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetNullValue() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.getNullValue(objectMapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testGetNullValueDeprecated() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.getNullValue();
        assertNull(result);
    }

    @Test
    public void testDeserializeObjectEmpty() throws IOException {
        JsonParser parser = objectMapper.getFactory().createParser("{}");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode result = (ObjectNode) deserializer.deserialize(parser, objectMapper.getDeserializationContext());
        assertEquals(0, result.size());
    }

    @Test
    public void testDeserializeObject() throws IOException {
        JsonParser parser = objectMapper.getFactory().createParser("{\"name\":\"John\",\"age\":30}");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode result = (ObjectNode) deserializer.deserialize(parser, objectMapper.getDeserializationContext());
        assertEquals("John", result.get("name").asText());
        assertEquals(30, result.get("age").asInt());
    }

    @Test
    public void testDeserializeArrayEmpty() throws IOException {
        JsonParser parser = objectMapper.getFactory().createParser("[]");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        ArrayNode result = (ArrayNode) deserializer.deserialize(parser, objectMapper.getDeserializationContext());
        assertEquals(0, result.size());
    }

    @Test
    public void testDeserializeArray() throws IOException {
        JsonParser parser = objectMapper.getFactory().createParser("[1,2,\"three\",{\"name\":\"John\"}]");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        ArrayNode result = (ArrayNode) deserializer.deserialize(parser, objectMapper.getDeserializationContext());
        assertEquals(4, result.size());
        assertEquals(1, result.get(0).asInt());
        assertEquals(2, result.get(1).asInt());
        assertEquals("three", result.get(2).asText());
        ObjectNode objNode = (ObjectNode) result.get(3);
        assertEquals("John", objNode.get("name").asText());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidJson() throws IOException {
        JsonParser parser = objectMapper.getFactory().createParser("{InvalidJson}");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        deserializer.deserialize(parser, objectMapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNonJsonObject() throws IOException {
        String json = "[1,2,3]";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        deserializer.deserialize(parser, objectMapper.getDeserializationContext());
    }

    @Test
    public void testDeserializeObjectWithDuplicateField() throws IOException {
        JsonParser parser = objectMapper.getFactory().createParser("{\"name\":\"John\",\"age\":30,\"name\":\"Alice\"}");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode result = (ObjectNode) deserializer.deserialize(parser, objectMapper.getDeserializationContext());
        assertEquals("Alice", result.get("name").asText());
    }

    @Test
    public void testDeserializeArrayWithDuplicateValue() throws IOException {
        JsonParser parser = objectMapper.getFactory().createParser("[1,2,1]");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        ArrayNode result = (ArrayNode) deserializer.deserialize(parser, objectMapper.getDeserializationContext());
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).asInt());
        assertEquals(1, result.get(1).asInt());
        assertEquals(1, result.get(2).asInt());
    }

}