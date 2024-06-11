package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

 private ObjectMapper mapper;
 private DeserializationContext ctxt;
 private JsonParser parser;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 ctxt = mapper.getDeserializationContext();
 parser = mapper.getFactory().getParser();
 }

 @Test
 public void testDeserializeNullValue() throws JsonProcessingException {
 JsonNode node = JsonNodeDeserializer.getInstance().deserialize(parser, ctxt);
 assertEquals(JsonNode.Value.nullValue(), node);
 }

 @Test
 public void testDeserializeObject() throws JsonProcessingException {
 parser.setValue("{\"name\":\"John\",\"age\":30}");
 JsonNode node = JsonNodeDeserializer.getInstance().deserialize(parser, ctxt);
 ObjectNode objNode = (ObjectNode) node;
 assertEquals("John", objNode.get("name").asText());
 assertEquals(30, objNode.get("age").asInt());
 }

 @Test
 public void testDeserializeArray() throws JsonProcessingException {
 parser.setValue("[1,2,3]");
 ArrayNode arrayNode = (ArrayNode) JsonNodeDeserializer.getInstance().deserialize(parser, ctxt);
 assertEquals(3, arrayNode.size());
 assertEquals(1, arrayNode.get(0).asInt());
 assertEquals(2, arrayNode.get(1).asInt());
 assertEquals(3, arrayNode.get(2).asInt());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJson() throws JsonProcessingException {
 parser.setValue("{invalid:json}");
 JsonNodeDeserializer.getInstance().deserialize(parser, ctxt);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmptyJsonObject() throws JsonProcessingException {
 parser.setValue("{}");
 JsonNodeDeserializer.getInstance().deserialize(parser, ctxt);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmptyJsonArray() throws JsonProcessingException {
 parser.setValue("[]");
 JsonNodeDeserializer.getInstance().deserialize(parser, ctxt);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidToken() throws JsonProcessingException {
 parser.nextToken();
 JsonNodeDeserializer.getInstance().deserialize(parser, ctxt);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeDuplicateField() throws JsonProcessingException {
 parser.setValue("{\"name\":\"John\",\"name\":\"Jane\"}");
 ObjectNode objNode = (ObjectNode) JsonNodeDeserializer.getInstance().deserialize(parser, ctxt);
 assertNotNull(objNode);
 JsonNode node = objNode.get("name");
 assertEquals(2, node.size());
 }

 @Test
 public void testDeserializeObjectWithArrayProperty() throws JsonProcessingException {
 parser.setValue("{\"name\":\"John\",\"hobbies\":[\"reading\",\"swimming\"]}");
 ObjectNode objNode = (ObjectNode) JsonNodeDeserializer.getInstance().deserialize(parser, ctxt);
 ArrayNode arrayNode = (ArrayNode) objNode.get("hobbies");
 assertEquals(2, arrayNode.size());
 assertEquals("reading", arrayNode.get(0).asText());
 assertEquals("swimming", arrayNode.get(1).asText());
 }

 @Test
 public void testDeserializeObjectWithEmptyArrayProperty() throws JsonProcessingException {
 parser.setValue("{\"name\":\"John\",\"hobbies\":[]}");
 ObjectNode objNode = (ObjectNode) JsonNodeDeserializer.getInstance().deserialize(parser, ctxt);
 ArrayNode arrayNode = (ArrayNode) objNode.get("hobbies");
 assertEquals(0, arrayNode.size());
 }

 @Test
 public void testDeserializeObjectWithNestedObjectArrayProperty() throws JsonProcessingException {
 // complex JSON structure for testing nested objects within an array
 parser.setValue("{\"name\":\"John\",\"hobbies\":[{\"activity\":\"reading\",\"books\":[{\"title\":\"Book1\",\"author\":\"Author1\"},{\"title\":\"Book2\",\"author\":\"Author2\"}]},{\"activity\":\"swimming\",\"pools\":[{\"name\":\"Pool1\",\"temp\":25},{\"name\":\"Pool2\",\"temp\":30}]}]}");
 ObjectNode objNode = (ObjectNode) JsonNodeDeserializer.getInstance().deserialize(parser, ctxt);
 ObjectNode hobbiesNode = (ObjectNode) objNode.get("hobbies");
 assertEquals(2, hobbiesNode.size());
 ArrayNode activitiesNode = (ArrayNode) hobbiesNode.get(0);
 assertEquals(2, activitiesNode.size());
 ObjectNode readingNode = (ObjectNode) activitiesNode.get(0);
 assertEquals("reading", readingNode.get("activity").asText());
 ArrayNode booksNode = (ArrayNode) readingNode.get("books");
 assertEquals(2, booksNode.size());
 ObjectNode book1Node = (ObjectNode) booksNode.get(0);
 assertEquals("Book1", book1Node.get("title").asText());
 assertEquals("Author1", book1Node.get("author").asText());
 ObjectNode book2Node = (ObjectNode) booksNode.get(1);
 assertEquals("Book2", book2Node.get("title").asText());
 assertEquals("Author2", book2Node.get("author").asText());
 ObjectNode swimmingNode = (ObjectNode) activitiesNode.get(1);
 assertEquals("swimming", swimmingNode.get("activity").asText());
 ArrayNode poolsNode = (ArrayNode) swimmingNode.get("pools");
 assertEquals(2, poolsNode.size());
 ObjectNode pool1Node = (ObjectNode) poolsNode.get(0);
 assertEquals("Pool1", pool1Node.get("name").asText());
 assertEquals(25, pool1Node.get("temp").asInt());
 ObjectNode pool2Node = (ObjectNode) poolsNode.get(1);
 assertEquals("Pool2", pool2Node.get("name").asText());
 assertEquals(30, pool2Node.get("temp").asInt());
 }
}