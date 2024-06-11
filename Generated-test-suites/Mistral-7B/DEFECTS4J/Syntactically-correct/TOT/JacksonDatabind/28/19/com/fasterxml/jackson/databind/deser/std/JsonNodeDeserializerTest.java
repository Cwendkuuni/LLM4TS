package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNodeFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

 private JsonMapper mapper;
 private DeserializationContext ctxt;
 private JsonNodeFactory nodeFactory;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 ctxt = mapper.getDeserializationContext();
 nodeFactory = mapper.getNodeFactory();
 }

 @Test
 public void testDeserializeNullValue() throws JsonProcessingException {
 JsonNode node = JsonNodeDeserializer.instance.getNullValue(ctxt);
 assertEquals(NullNode.getInstance(), node);
 }

 @Test
 public void testDeserializeStartObject() throws JsonProcessingException {
 JsonParser p = mapper.getFactory().getParser("{\"name\":\"John\",\"age\":42}");
 JsonNode node = JsonNodeDeserializer.instance.deserialize(p, ctxt);
 assertEquals(JsonNode.class, node.getClass());
 ObjectNode objNode = (ObjectNode) node;
 assertEquals("John", objNode.get("name").asText());
 assertEquals(42, objNode.get("age").asInt());
 }

 @Test
 public void testDeserializeStartObjectWithEmptyFields() throws JsonProcessingException {
 JsonParser p = mapper.getFactory().getParser("{}");
 JsonNode node = JsonNodeDeserializer.instance.deserialize(p, ctxt);
 assertEquals(JsonNode.class, node.getClass());
 ObjectNode objNode = (ObjectNode) node;
 assertTrue(objNode.isEmpty());
 }

 @Test
 public void testDeserializeEmptyObjectWithFieldName() throws JsonProcessingException {
 JsonParser p = mapper.getFactory().getParser("{name: null}");
 JsonNode node = JsonNodeDeserializer.instance.deserialize(p, ctxt);
 assertEquals(JsonNode.class, node.getClass());
 ObjectNode objNode = (ObjectNode) node;
 assertTrue(objNode.isEmpty());
 assertEquals("name", objNode.fieldNames().nextValue().asText());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeStartObjectWithDuplicateField() throws JsonProcessingException {
 JsonParser p = mapper.getFactory().getParser("{\"name\":\"John\",\"name\":\"Jane\"}");
 JsonNodeDeserializer.instance._handleDuplicateField(p, ctxt, nodeFactory, "name", (ObjectNode) JsonNode.class.cast(JsonNodeDeserializer.instance.deserialize(p, ctxt)), JsonNode.class.cast(JsonNodeDeserializer.instance.deserialize(p, ctxt)), null);
 }

 @Test
 public void testDeserializeStartArray() throws JsonProcessingException {
 JsonParser p = mapper.getFactory().getParser("[1,2,3]");
 JsonNode node = JsonNodeDeserializer.instance.deserialize(p, ctxt);
 assertEquals(JsonNode.class, node.getClass());
 ArrayNode arrayNode = (ArrayNode) node;
 assertEquals(3, arrayNode.size());
 assertEquals(1, arrayNode.get(0).asInt());
 assertEquals(2, arrayNode.get(1).asInt());
 assertEquals(3, arrayNode.get(2).asInt());
 }

 @Test
 public void testDeserializeStartArrayWithEmptyArray() throws JsonProcessingException {
 JsonParser p = mapper.getFactory().getParser("[]");
 JsonNode node = JsonNodeDeserializer.instance.deserialize(p, ctxt);
 assertEquals(JsonNode.class, node.getClass());
 ArrayNode arrayNode = (ArrayNode) node;
 assertTrue(arrayNode.isEmpty());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeStartArrayWithDuplicateValue() throws JsonProcessingException {
 JsonParser p = mapper.getFactory().getParser("[1,1]");
 JsonNodeDeserializer.instance._handleDuplicateField(p, ctxt, nodeFactory, "", (ArrayNode) JsonNode.class.cast(JsonNodeDeserializer.instance.deserialize(p, ctxt)), JsonNode.class.cast(JsonNodeDeserializer.instance.deserialize(p, ctxt)), null);
 }
}