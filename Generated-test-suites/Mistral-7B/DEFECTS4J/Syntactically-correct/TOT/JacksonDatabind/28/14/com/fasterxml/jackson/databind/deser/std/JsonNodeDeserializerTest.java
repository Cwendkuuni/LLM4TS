package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testNullValueDeserialization() throws JsonMappingException, JsonProcessingException {
 JsonNode result = JsonNodeDeserializer.getDeserializer(NullNode.class).deserialize(mapper.getFactory().createParser("null"), null);
 assertInstanceOf(NullNode.class, result);
 }

 @Test
 public void testDeserializationOfEmptyObject() throws JsonMappingException, JsonProcessingException {
 JsonNode result = JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(mapper.getFactory().createParser("{}"), null);
 assertTrue(result instanceof ObjectNode);
 assertEquals(result.size(), 0);
 }

 @Test
 public void testDeserializationOfEmptyArray() throws JsonMappingException, JsonProcessingException {
 JsonNode result = JsonNodeDeserializer.getDeserializer(ArrayNode.class).deserialize(mapper.getFactory().createParser("[]"), null);
 assertTrue(result instanceof ArrayNode);
 assertEquals(result.size(), 0);
 }

 @Test
 public void testDeserializationOfSimpleJsonObject() throws JsonMappingException, JsonProcessingException {
 JsonNode result = JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(mapper.getFactory().createParser("{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}"), null);
 assertTrue(result instanceof ObjectNode);
 ObjectNode objectNode = (ObjectNode) result;
 assertEquals("John", objectNode.get("name").asText());
 assertEquals(30, objectNode.get("age").asInt());
 assertNull(objectNode.get("city").getText());
 }

 @Test
 public void testDeserializationOfSimpleJsonArray() throws JsonMappingException, JsonProcessingException {
 JsonNode result = JsonNodeDeserializer.getDeserializer(ArrayNode.class).deserialize(mapper.getFactory().createParser("[1,2,3]"), null);
 assertTrue(result instanceof ArrayNode);
 ArrayNode arrayNode = (ArrayNode) result;
 assertEquals(3, arrayNode.size());
 assertEquals(1, arrayNode.get(0).asInt());
 assertEquals(2, arrayNode.get(1).asInt());
 assertEquals(3, arrayNode.get(2).asInt());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationOfMalformedJsonObject() throws JsonMappingException, JsonProcessingException {
 JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(mapper.getFactory().createParser("{"), null);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationOfMalformedJsonArray() throws JsonMappingException, JsonProcessingException {
 JsonNodeDeserializer.getDeserializer(ArrayNode.class).deserialize(mapper.getFactory().createParser("[1"), null);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationOfInvalidJsonObject() throws JsonMappingException, JsonProcessingException {
 JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(mapper.getFactory().createParser("{\"name\":1}"), null);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationOfInvalidJsonArray() throws JsonMappingException, JsonProcessingException {
 JsonNodeDeserializer.getDeserializer(ArrayNode.class).deserialize(mapper.getFactory().createParser("[123]"), null);
 }

 @Test
 public void testDuplicateFieldDeserialization() throws JsonMappingException, JsonProcessingException {
 JsonDeserializer<ObjectNode> objectDeserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
 ObjectNode expected = mapper.readTree("{\"field1\":1,\"field1\":2}");
 ObjectNode result = objectDeserializer.deserialize(mapper.getFactory().createParser("{\"field1\":1,\"field1\":2}"), null);
 assertEquals(expected, result);
 }
}