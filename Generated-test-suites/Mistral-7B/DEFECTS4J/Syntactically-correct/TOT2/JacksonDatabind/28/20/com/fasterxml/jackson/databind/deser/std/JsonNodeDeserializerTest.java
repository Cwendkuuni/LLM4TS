package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Test;

/**
 * Test for JsonNodeDeserializer class
 */
public class JsonNodeDeserializerTest {

 // Test for getNullValue method
 @Test
 public void testGetNullValue() throws JsonProcessingException {
 JsonNode deserializedNode = JsonNodeDeserializer.getInstance().deserialize(null, new TestContext(), null);
 assertEquals(JsonNode.ValueType.NULL, deserializedNode.getNodeType());
 }

 // Test for deserialize method with valid JSON Object
 @Test
 public void testDeserializeObject() throws JsonProcessingException {
 JsonParser mockParser = new MockParser();
 JsonParser p = mockParser.createParser("{\"name\": \"John\", \"age\": 30}");
 JsonNode expectedNode = ObjectMapper.getDefaultInstance()
 .readTree("{\"name\": \"John\", \"age\": 30}");
 JsonNode deserializedNode = JsonNodeDeserializer.getInstance().deserialize(p, new TestContext(), null);
 assertEquals(expectedNode, deserializedNode);
 }

 // Test for deserialize method with valid JSON Array
 @Test
 public void testDeserializeArray() throws JsonProcessingException {
 JsonParser mockParser = new MockParser();
 JsonParser p = mockParser.createParser("[1, 2, 3]");
 JsonNode expectedNode = ObjectMapper.getDefaultInstance()
 .readTree("[1, 2, 3]");
 JsonNode deserializedNode = JsonNodeDeserializer.getInstance().deserialize(p, new TestContext(), null);
 assertEquals(expectedNode, deserializedNode);
 }

 // Test for deserialize method with invalid JSON
 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJson() throws JsonProcessingException {
 JsonParser mockParser = new MockParser();
 JsonParser p = mockParser.createParser("{invalid: JSON}");
 JsonNodeDeserializer.getInstance().deserialize(p, new TestContext(), null);
 }

 // Test for deserialize method with empty JSON Object
 @Test
 public void testDeserializeEmptyObject() throws JsonProcessingException {
 JsonParser mockParser = new MockParser();
 JsonParser p = mockParser.createParser("{}");
 JsonNode deserializedNode = JsonNodeDeserializer.getInstance().deserialize(p, new TestContext(), null);
 assertTrue(deserializedNode.isMissingNode());
 }

 // Test for deserialize method with empty JSON Array
 @Test
 public void testDeserializeEmptyArray() throws JsonProcessingException {
 JsonParser mockParser = new MockParser();
 JsonParser p = mockParser.createParser("[]");
 JsonNode deserializedNode = JsonNodeDeserializer.getInstance().deserialize(p, new TestContext(), null);
 assertTrue(deserializedNode.isMissingNode());
 }

 // Test for deserialize method with complex JSON Object
 @Test
 public void testDeserializeComplexObject() throws JsonProcessingException {
 JsonParser mockParser = new MockParser();
 JsonParser p = mockParser.createParser("{\"name\": \"John\", \"age\": 30, \"address\": {" +
 "\"houseNumber\": 123, \"street\": \"Main St.\"}}");
 JsonNode expectedNode = ObjectMapper.getDefaultInstance()
 .readTree("{\"name\": \"John\", \"age\": 30, \"address\": {" +
 "\"houseNumber\": 123, \"street\": \"Main St.\", \"city\": \"New York\"}}");
 expectedNode.set("address.city", new TextNode("San Francisco"));
 JsonNode deserializedNode = JsonNodeDeserializer.getInstance().deserialize(p, new TestContext(), null);
 assertEquals(expectedNode, deserializedNode);
 }

 // Test for deserialize method with complex JSON Array
 @Test
 public void testDeserializeComplexArray() throws JsonProcessingException {
 JsonParser mockParser = new MockParser();
 JsonParser p = mockParser.createParser("[1, [2, 3], {\"name\": \"John\"}]");
 JsonNode expectedNode = ObjectMapper.getDefaultInstance()
 .readTree("[1, [2, 3], {\"name\": \"John\"}]");
 expectedNode.set(2, new ObjectNode(ObjectMapper.getDefaultInstance().createObjectNode()));
 expectedNode.get(2).put("name", new TextNode("Alice"));
 JsonNode deserializedNode = JsonNodeDeserializer.getInstance().deserialize(p, new TestContext(), null);
 assertEquals(expectedNode, deserializedNode);
 }

 // Test for deserialize method with missed object or array start token
 @Test(expected = JsonMappingException.class)
 public void testDeserializeMissedStartToken() throws JsonProcessingException {
 JsonParser mockParser = new MockParser();
 JsonParser p = mockParser.createParser("{\"name\": \"John\"};");
 JsonNodeDeserializer.getInstance().deserialize(p, new TestContext(), null);
 }

 // Test for deserialize method with missed object or array end token
 @Test(expected = JsonMappingException.class)
 public void testDeserializeMissedEndToken() throws JsonProcessingException {
 JsonParser mockParser = new MockParser();
 JsonParser p = mockParser.createParser("{\"name\": \"John\"");
 JsonNodeDeserializer.getInstance().deserialize(p, new TestContext(), null);
 }

 // Test for deserialize method with duplicate field
 @Test
 public void testDeserializeDuplicateField() throws JsonProcessingException {
 JsonParser mockParser = new MockParser();
 JsonParser p = mockParser.createParser("{\"name\": \"John\", \"name\": \"Alice\"}");
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode objectNode = mapper.readValue(p, ObjectNode.class);
 JsonNode deserializedNode = JsonNodeDeserializer.getInstance().deserialize(p, new TestContext(), null);
 JsonNodeNode expectedNode = new ObjectNode(JsonNodeFactory.instance.objectNode());
 expectedNode.put("name", new TextNode("Alice"));
 assertEquals(expectedNode, deserializedNode);
 }

 // Test for deserialize method with duplicate array element
 @Test
 public void testDeserializeDuplicateArrayElement() throws JsonProcessingException {
 JsonParser mockParser = new MockParser();
 JsonParser p = mockParser.createParser("[1, 1]");
 ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance.arrayNode());
 arrayNode.add(new IntNode(1));
 arrayNode.add(new IntNode(1));
 JsonNode deserializedNode = JsonNodeDeserializer.getInstance().deserialize(p, new TestContext(), null);
 assertEquals(arrayNode, deserializedNode);
 }
}

class TestContext extends BaseDeserializerMapper {
}

class MockParser {
 JsonParser createParser(String jsonString) {
 ObjectMapper mapper = new ObjectMapper();
 return mapper.getFactory().createParser(jsonString);
 }
}