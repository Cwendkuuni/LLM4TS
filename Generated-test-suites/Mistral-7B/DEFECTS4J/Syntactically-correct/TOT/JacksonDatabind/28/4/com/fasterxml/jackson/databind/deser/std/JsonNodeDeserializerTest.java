package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JsonNodeDeserializerTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDeserializeObject() throws JsonMappingException, JsonProcessingException {
        String json = "{\"name\":\"John\",\"age\":30,\"isStudent\":false,\"skills\":[\"Java\",\"Python\"]}";
        ObjectNode actual = (ObjectNode) JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        ObjectNode expected = mapper.readValue(json, ObjectNode.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeObjectWithDuplicateKey() throws JsonMappingException, JsonProcessingException {
        String json = "{\"name\":\"John\",\"age\":30,\"name\":\"Jane\",\"skills\":[\"Java\",\"Python\"]}";
        try {
            JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        } catch (JsonMappingException e) {
            assertEquals("Duplicate field 'name' for ObjectNode: not allowed when FAIL_ON_READING_DUP_TREE_KEY enabled", e.getMessage());
        }
    }

    @Test
    public void testDeserializeArray() throws JsonMappingException, JsonProcessingException {
        String json = "[1,2,3,4]";
        ArrayNode actual = (ArrayNode) JsonNodeDeserializer.getDeserializer(ArrayNode.class).deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        ArrayNode expected = mapper.readValue(json, ArrayNode.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeNullValue() throws JsonMappingException, JsonProcessingException {
        String json = "null";
        JsonNode actual = JsonNodeDeserializer.getDeserializer(JsonNode.class).deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertNotNull(actual);
        assertEquals(JsonNode.Value.NULL, actual.getNodeType());
    }

    @Test
    public void testDeserializeEmptyObject() throws JsonMappingException, JsonProcessingException {
        String json = "{}";
        ObjectNode actual = (ObjectNode) JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals(mapper.createObjectNode(), actual);
    }

    @Test
    public void testDeserializeEmptyArray() throws JsonMappingException, JsonProcessingException {
        String json = "[]";
        ArrayNode actual = (ArrayNode) JsonNodeDeserializer.getDeserializer(ArrayNode.class).deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals(mapper.createArrayNode(), actual);
    }

    @Test
    public void testDeserializeInvalidJson() throws JsonMappingException, JsonProcessingException {
        String json = "invalidJson";
        try {
            JsonNodeDeserializer.getDeserializer(JsonNode.class).deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        } catch (JsonMappingException e) {
            assertEquals("Can not instantiate value of type [simple type, class com.fasterxml.jackson.databind.node.JsonNode] from JSON String; no single-value constructor/factory method", e.getMessage());
        }
    }

    @Test
    public void testDeserializeObjectWithCustomDeserializer() throws JsonMappingException, JsonProcessingException {
        String json = "{\"name\":\"John\",\"age\":30,\"isStudent\":false,\"skills\":[\"Java\",\"Python\"],\"class\":{\"id\":1}}";
        ObjectNode actual = (ObjectNode) JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        ObjectNode expected = mapper.readValue(json, ObjectNode.class);
        assertEquals(expected, actual);
        assertEquals(1, actual.path("class").get("id").asInt());
    }

    @Test
    public void testDeserializeArrayWithCustomDeserializer() throws JsonMappingException, JsonProcessingException {
        String json = "[1,2,3,{\"id\":4}]";
        ArrayNode actual = (ArrayNode) JsonNodeDeserializer.getDeserializer(ArrayNode.class).deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        ArrayNode expected = mapper.readValue(json, ArrayNode.class);
        assertEquals(expected, actual);
        assertEquals(4, actual.get(3).path("id").asInt());
    }

    @Test
    public void testDeserializeNullObject() throws JsonMappingException, JsonProcessingException {
        String json = "null";
        ObjectNode actual = (ObjectNode) JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals(mapper.createObjectNode(), actual);
    }

    @Test
    public void testDeserializeEmptyArrayWithCustomDeserializer() throws JsonMappingException, JsonProcessingException {
        String json = "[]";
        ArrayNode actual = (ArrayNode) JsonNodeDeserializer.getDeserializer(ArrayNode.class).deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals(mapper.createArrayNode(), actual);
    }

    @Test
    public void testDeserializeInvalidJsonWithCustomDeserializer() throws JsonMappingException, JsonProcessingException {
        String json = "invalidJson";
        try {
            JsonNodeDeserializer.getDeserializer(JsonNode.class).deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        } catch (JsonMappingException e) {
            assertEquals("Can not instantiate value of type [simple type, class com.fasterxml.jackson.databind.node.JsonNode] from JSON String; no single-value constructor/factory method", e.getMessage());
        }
    }

    @Test
    public void testDeserializeObjectWithStreamingApi() throws IOException {
        String json = "{\"name\":\"John\"";
        ObjectNode actual = JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(mapper.getFactory().createParser(json), null);
        ObjectNode expected = mapper.createObjectNode();
        expected.put("name", "John");
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeArrayWithStreamingApi() throws IOException {
        String json = "[1,";
        ArrayNode actual = (ArrayNode) JsonNodeDeserializer.getDeserializer(ArrayNode.class).deserialize(mapper.getFactory().createParser(json), null);
        ArrayNode expected = mapper.createArrayNode();
        expected.add(1);
        assertEquals(expected, actual);
    }
}