package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

 private final JsonFactory jsonFactory = new JsonFactory();
 private final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDeserializeNull() throws Exception {
 JsonParser p = jsonFactory.createParser("null");
 JsonDeserializer<? extends JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
 JsonNode node = deserializer.deserialize(p, mapper.getDeserializationContext());
 assertEquals(JsonNode.Value.NULL, node.getNodeType());
 }

 @Test
 public void testDeserializeEmptyObject() throws Exception {
 JsonParser p = jsonFactory.createParser("{}");
 JsonDeserializer<? extends JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
 ObjectNode node = (ObjectNode) deserializer.deserialize(p, mapper.getDeserializationContext());
 assertTrue(node.isEmpty());
 }

 @Test
 public void testDeserializeEmptyArray() throws Exception {
 JsonParser p = jsonFactory.createParser("[{}]");
 JsonDeserializer<? extends JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
 ArrayNode node = (ArrayNode) deserializer.deserialize(p, mapper.getDeserializationContext());
 assertTrue(node.isEmpty());
 }

 @Test
 public void testDeserializeObject() throws Exception {
 JsonParser p = jsonFactory.createParser("{\"name\":\"John\", \"age\":30}");
 JsonDeserializer<? extends JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
 ObjectNode node = (ObjectNode) deserializer.deserialize(p, mapper.getDeserializationContext());
 assertEquals("John", node.get("name").asText());
 assertEquals(30, node.get("age").asInt());
 }

 @Test
 public void testDeserializeArray() throws Exception {
 JsonParser p = jsonFactory.createParser("[1, 2, 3]");
 JsonDeserializer<? extends JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
 ArrayNode node = (ArrayNode) deserializer.deserialize(p, mapper.getDeserializationContext());
 assertEquals(3, node.size());
 assertEquals(1, node.get(0).asInt());
 assertEquals(2, node.get(1).asInt());
 assertEquals(3, node.get(2).asInt());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeUnknownType() throws Exception {
 JsonParser p = jsonFactory.createParser("{\"unknown\":1}");
 JsonDeserializer<? extends JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(Integer.class);
 deserializer.deserialize(p, mapper.getDeserializationContext());
 fail("Should have thrown JsonMappingException");
 }
}