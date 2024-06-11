package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

 private final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDeserializeObject() throws JsonProcessingException {
 JsonNode node = mapper.readTree("{\"name\":\"John\",\"age\":30}");
 ObjectNode obj = (ObjectNode) JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(mapper.getFactory(), mapper.getParser(), mapper.getFactory());
 assertEquals("John", obj.get("name").asText());
 assertEquals(30, obj.get("age").asInt());
 }

 @Test
 public void testDeserializeArray() throws JsonProcessingException {
 JsonNode node = mapper.readTree("[1,2,3]");
 ArrayNode arr = (ArrayNode) JsonNodeDeserializer.getDeserializer(ArrayNode.class).deserialize(mapper.getFactory(), mapper.getParser(), mapper.getFactory());
 assertEquals(3, arr.size());
 assertEquals(1, arr.get(0).asInt());
 assertEquals(2, arr.get(1).asInt());
 assertEquals(3, arr.get(2).asInt());
 }

 @Test
 public void testDeserializeNull() throws JsonProcessingException {
 JsonNode node = mapper.readTree("null");
 JsonNode result = JsonNodeDeserializer.getDeserializer(JsonNode.class).deserialize(mapper.getFactory(), mapper.getParser(), mapper.getFactory());
 assertNull(result);
 }

 @Test
 public void testDeserializeInvalidJson() throws JsonProcessingException {
 JsonNode node = mapper.readTree("{name: John, age:30, city: ["); // missing end array
 JsonNode result = JsonNodeDeserializer.getDeserializer(JsonNode.class).deserialize(mapper.getFactory(), mapper.getParser(), mapper.getFactory());
 assertNotNull(result);
 assertTrue(result.isMissingNode());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJsonWithException() throws JsonProcessingException {
 JsonNode node = mapper.readTree("{name: John, age:30, city: ["); // missing end array
 JsonNode result = JsonNodeDeserializer.getInstance().deserialize(mapper.getFactory(), mapper.getParser(), mapper.getFactory());
 }

 @Test
 public void testDeserializeArrayWithObject() throws JsonProcessingException {
 JsonNode node = mapper.readTree("[{\"name\":\"John\",\"age\":30},{\"name\":\"Jane\",\"age\":25}]");
 ArrayNode arr = (ArrayNode) JsonNodeDeserializer.getDeserializer(ArrayNode.class).deserialize(mapper.getFactory(), mapper.getParser(), mapper.getFactory());
 assertEquals(2, arr.size());
 ObjectNode firstObject = (ObjectNode) arr.get(0);
 assertEquals("John", firstObject.get("name").asText());
 assertEquals(30, firstObject.get("age").asInt());
 ObjectNode secondObject = (ObjectNode) arr.get(1);
 assertEquals("Jane", secondObject.get("name").asText());
 assertEquals(25, secondObject.get("age").asInt());
 }

 @Test
 public void testDeserializeObjectWithArray() throws JsonProcessingException {
 JsonNode node = mapper.readTree("{\"name\":\"John\",\"hobbies\":[\"reading\",\"swimming\",\"running\"]}");
 ObjectNode obj = (ObjectNode) JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(mapper.getFactory(), mapper.getParser(), mapper.getFactory());
 assertEquals("John", obj.get("name").asText());
 ArrayNode hobbies = (ArrayNode) obj.get("hobbies");
 assertEquals(3, hobbies.size());
 assertEquals("reading", hobbies.get(0).asText());
 assertEquals("swimming", hobbies.get(1).asText());
 assertEquals("running", hobbies.get(2).asText());
 }
}