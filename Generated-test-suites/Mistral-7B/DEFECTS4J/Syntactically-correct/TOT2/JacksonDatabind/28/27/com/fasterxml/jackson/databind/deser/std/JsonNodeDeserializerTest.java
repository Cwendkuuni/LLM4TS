package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.*;

public class JsonNodeDeserializerTest {

 private static ObjectMapper mapper;

 @BeforeClass
 static void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testDeserializeNullValue() throws IOException {
 JsonNode node = JsonNodeDeserializer.getDeserializer(JsonNode.class).deserialize(mapper.getFactory(), null, null);
 assertEquals(JsonNode.Value.nullValue(), node);
 }

 @Test
 public void testDeserializeEmptyObject() throws IOException {
 JsonParser p = mapper.getFactory().createParser("{}");
 JsonNode node = JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(p, mapper.getDeserializationContext(), mapper.getNodeFactory());
 assertTrue(node instanceof ObjectNode);
 assertTrue(node.isEmpty());
 }

 @Test
 public void testDeserializeEmptyArray() throws IOException {
 JsonParser p = mapper.getFactory().createParser("[]");
 JsonNode node = JsonNodeDeserializer.getDeserializer(ArrayNode.class).deserialize(p, mapper.getDeserializationContext(), mapper.getNodeFactory());
 assertTrue(node instanceof ArrayNode);
 assertTrue(node.isEmpty());
 }

 @Test
 public void testDeserializeSimpleObject() throws IOException {
 JsonParser p = mapper.getFactory().createParser("{\"name\":\"John\", \"age\":30}");
 JsonNode node = JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(p, mapper.getDeserializationContext(), mapper.getNodeFactory());
 ObjectNode objNode = (ObjectNode) node;
 assertEquals("John", objNode.get("name").asText());
 assertEquals(30, objNode.get("age").asInt());
 }

 @Test
 public void testDeserializeSimpleArray() throws IOException {
 JsonParser p = mapper.getFactory().createParser("[1, 2, 3]");
 JsonNode node = JsonNodeDeserializer.getDeserializer(ArrayNode.class).deserialize(p, mapper.getDeserializationContext(), mapper.getNodeFactory());
 ArrayNode arrNode = (ArrayNode) node;
 assertEquals(3, arrNode.size());
 assertEquals(1, arrNode.get(0).asInt());
 assertEquals(2, arrNode.get(1).asInt());
 assertEquals(3, arrNode.get(2).asInt());
 }

 @Test
 public void testDeserializeComplexObject() throws IOException {
 JsonParser p = mapper.getFactory().createParser("{\"name\":\"John\", \"age\":30,\"address\":{\"street\":\"Main St\", \"city\":\"Anytown\"}}");
 JsonNode node = JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(p, mapper.getDeserializationContext(), mapper.getNodeFactory());
 ObjectNode objNode = (ObjectNode) node;
 assertEquals("John", objNode.get("name").asText());
 assertEquals(30, objNode.get("age").asInt());
 ObjectNode addressNode = (ObjectNode) objNode.get("address");
 assertEquals("Main St", addressNode.get("street").asText());
 assertEquals("Anytown", addressNode.get("city").asText());
 }

 @Test
 public void testDeserializeComplexArray() throws IOException {
 JsonParser p = mapper.getFactory().createParser("[1, [2, 3], {\"name\":\"John\"}]");
 JsonNode node = JsonNodeDeserializer.getDeserializer(ArrayNode.class).deserialize(p, mapper.getDeserializationContext(), mapper.getNodeFactory());
 ArrayNode arrNode = (ArrayNode) node;
 assertEquals(3, arrNode.size());
 JsonNode firstElement = arrNode.get(0);
 assertEquals(1, firstElement.asInt());
 JsonNode secondElement = arrNode.get(1);
 assertTrue(secondElement instanceof ArrayNode);
 ArrayNode arrayInside = (ArrayNode) secondElement;
 assertEquals(2, arrayInside.size());
 assertEquals(2, arrayInside.get(0).asInt());
 assertEquals(3, arrayInside.get(1).asInt());
 JsonNode thirdElement = arrNode.get(2);
 assertTrue(thirdElement instanceof ObjectNode);
 ObjectNode objInside = (ObjectNode) thirdElement;
 assertEquals("John", objInside.get("name").asText());
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeInvalidJson() throws IOException {
 JsonParser p = mapper.getFactory().createParser("{abcd}");
 JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(p, mapper.getDeserializationContext(), mapper.getNodeFactory());
 }
}