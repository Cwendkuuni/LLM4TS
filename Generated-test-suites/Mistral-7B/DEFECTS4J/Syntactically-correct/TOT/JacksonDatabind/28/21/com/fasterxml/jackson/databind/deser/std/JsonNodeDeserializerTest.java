package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonNodeFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

 private JsonNodeFactory nodeFactory;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 nodeFactory = new DefaultNodeFactory();
 mapper = new ObjectMapper();
 }

 @Test
 public void testDeserializeNull() throws IOException {
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
 JsonParser mockParser = createMockParser(JsonTokenId.ID_NULL);
 JsonNode result = deserializer.deserialize(mockParser, new TestContext(), nodeFactory);
 assertEquals(JsonNode.NULL, result);
 }

 @Test
 public void testDeserializeObject() throws IOException {
 String json = "{\"name\":\"John\",\"age\":30}";
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
 JsonParser mockParser = createMockParser(json);
 JsonNode result = deserializer.deserialize(mockParser, new TestContext(), nodeFactory);
 ObjectNode expected = nodeFactory.objectNode();
 expected.put("name", new TextNode("John"));
 expected.put("age", new TextNode("30"));
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeArray() throws IOException {
 String json = "[1,2,3]";
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
 JsonParser mockParser = createMockParser(json);
 JsonNode result = deserializer.deserialize(mockParser, new TestContext(), nodeFactory);
 ArrayNode expected = nodeFactory.arrayNode();
 expected.add(new TextNode("1"));
 expected.add(new TextNode("2"));
 expected.add(new TextNode("3"));
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeObjectWithEmptyArray() throws IOException {
 String json = "{\"name\":\"John\",\"hobbies\":[]}";
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
 JsonParser mockParser = createMockParser(json);
 JsonNode result = deserializer.deserialize(mockParser, new TestContext(), nodeFactory);
 ObjectNode expected = nodeFactory.objectNode();
 expected.put("name", new TextNode("John"));
 expected.put("hobbies", nodeFactory.arrayNode());
 assertEquals(expected, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeArrayWithObject() throws IOException {
 String json = "[{\"name\":\"John\"}]";
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
 JsonParser mockParser = createMockParser(json);
 deserializer.deserialize(mockParser, new TestContext(), nodeFactory);
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeObjectWithArray() throws IOException {
 String json = "{\"name\":[\"John\"]}";
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
 JsonParser mockParser = createMockParser(json);
 deserializer.deserialize(mockParser, new TestContext(), nodeFactory);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testDeserializeCustomClass() throws IOException {
 class TestObject {
 String name;
 int age;
 }
 JsonNodeFactory nodeFactory = new DefaultNodeFactory();
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerModule(new SimpleModule().setSerializerFactory(new BaseJsonNodeModule.StdSerializerFactory()));
 String json = "{\"name\":\"John\",\"age\":30}";
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(TestObject.class);
 JsonParser mockParser = createMockParser(json);
 TestObject result = mapper.readValue(mockParser, TestObject.class);
 TestObject expected = new TestObject();
 expected.name = "John";
 expected.age = 30;
 assertEquals(expected, result);
 }

 private JsonParser createMockParser(String json) throws IOException {
 JsonParser mockParser = mapper.getFactory().createParser(new StringReader(json));
 return mockParser;
 }

 private class TestContext extends DeserializationContext {
 JsonParser p = createMockParser(null);
 JsonNodeFactory nodeFactory = new DefaultNodeFactory();
 JsonNodeFactory nodeFactoryForTests = new DefaultNodeFactory();
 ObjectNode node = nodeFactoryForTests.objectNode();
 @Override
 public JsonNodeFactory getNodeFactory() {
 return nodeFactoryForTests;
 }

 @Override
 public JsonParser _currentLocation() {
 return p;
 }

 @Override
 public JsonParser _currentInput() {
 return p;
 }

 @Override
 public JsonParser _rootValueParser() {
 return p;
 }

 @Override
 public ObjectReader _valueHandler() {
 return null;
 }

 @Override
 public ObjectReader _valueHandlerInRoot() {
 return null;
 }

 @Override
 public ObjectReader _valueHandlerInArray() {
 return null;
 }

 @Override
 public ObjectReader _valueHandlerInObject() {
 return null;
 }
 }
}