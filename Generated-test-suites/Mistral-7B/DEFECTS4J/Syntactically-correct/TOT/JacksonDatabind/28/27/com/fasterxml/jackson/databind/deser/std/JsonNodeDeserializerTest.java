package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JsonNodeDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 void testDeserializeNull() throws IOException {
 JsonParser p = MAPPER.getFactory().createParser("null");
 DeserializationContext ctxt = new TestContext();
 JsonDeserializer<? extends JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
 JsonNode result = deserializer.deserialize(p, ctxt);
 Assertions.assertInstanceOf(JsonNode.class, result);
 Assertions.assertEquals(JsonNode.ValueType.NULL, result.getNodeType());
 }

 @Test
 void testDeserializeEmptyObject() throws IOException {
 JsonParser p = MAPPER.getFactory().createParser("{}");
 DeserializationContext ctxt = new TestContext();
 JsonDeserializer<? extends JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
 JsonNode result = deserializer.deserialize(p, ctxt);
 Assertions.assertInstanceOf(ObjectNode.class, result);
 Assertions.assertTrue(result.isEmpty());
 }

 @Test
 void testDeserializeObjectWithSingleField() throws IOException {
 JsonParser p = MAPPER.getFactory().createParser("{\"name\": \"John\"}");
 DeserializationContext ctxt = new TestContext();
 JsonDeserializer<? extends JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
 JsonNode result = deserializer.deserialize(p, ctxt);
 Assertions.assertInstanceOf(ObjectNode.class, result);
 Assertions.assertEquals("John", result.get("name").asText());
 }

 @Test
 void testDeserializeObjectWithArrayField() throws IOException {
 JsonParser p = MAPPER.getFactory().createParser("{\"numbers\": [1, 2, 3]}");
 DeserializationContext ctxt = new TestContext();
 JsonDeserializer<? extends JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
 JsonNode result = deserializer.deserialize(p, ctxt);
 Assertions.assertInstanceOf(ObjectNode.class, result);
 ArrayNode arrayNode = (ArrayNode) result.get("numbers");
 Assertions.assertIterableEquals(List.of(1, 2, 3), arrayNode);
 }

 @Test
 void testDeserializeArray() throws IOException {
 JsonParser p = MAPPER.getFactory().createParser("[1, 2, 3]");
 DeserializationContext ctxt = new TestContext();
 JsonDeserializer<? extends JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
 JsonNode result = deserializer.deserialize(p, ctxt);
 Assertions.assertInstanceOf(ArrayNode.class, result);
 Assertions.assertIterableEquals(List.of(1, 2, 3), ((ArrayNode) result).elements());
 }

 @Test
 void testDeserializeObjectWithEmptyArrayField() throws IOException {
 JsonParser p = MAPPER.getFactory().createParser("{\"numbers\": []}");
 DeserializationContext ctxt = new TestContext();
 JsonDeserializer<? extends JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
 JsonNode result = deserializer.deserialize(p, ctxt);
 Assertions.assertInstanceOf(ObjectNode.class, result);
 Assertions.assertNull(result.get("numbers"));
 }

 @Test
 void testDeserializeObjectWithMissingField() throws IOException {
 JsonParser p = MAPPER.getFactory().createParser("{\"name\": \"John\"}"); // no numbers field
 DeserializationContext ctxt = new TestContext();
 JsonDeserializer<? extends JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
 JsonNode result = deserializer.deserialize(p, ctxt);
 Assertions.assertInstanceOf(ObjectNode.class, result);
 Assertions.assertNull(result.get("numbers"));
 }

 @Test
 void testDeserializeObjectWithDuplicateField() throws IOException {
 JsonParser p = MAPPER.getFactory().createParser("{" +
 "\"name\": \"John\"," +
 "\"name\": 42" +
 "}");
 DeserializationContext ctxt = new TestContext();
 ctxt.disable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY); // disable error
 JsonDeserializer<? extends JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
 JsonNode result = deserializer.deserialize(p, ctxt);
 Assertions.assertInstanceOf(ObjectNode.class, result);
 Assertions.assertEquals(1, result.size());
 }

 @Test
 void testDeserializeObjectWithInvalidJson() throws IOException {
 JsonParser p = MAPPER.getFactory().createParser("{this is not valid JSON}");
 DeserializationContext ctxt = new TestContext();
 JsonDeserializer<? extends JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
 JsonNode result = deserializer.deserialize(p, ctxt);
 Assertions.assertNull(result);
 }

 @Test
 void testDeserializeCustomClass() throws IOException {
 JsonParser p = MAPPER.getFactory().createParser("{\"name\": \"John\", \"age\": 30}");
 DeserializationContext ctxt = new TestContext();
 JsonDeserializer<? extends JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(MyPojo.class);
 MyPojo result = deserializer.deserialize(p, ctxt);
 Assertions.assertEquals("John", result.getName());
 Assertions.assertEquals(30, result.getAge());
 }

 // Helper class to provide common test context
 static class TestContext extends DeserializationContext {
 TestContext() {
 super(new ObjectMapper());
 }
 }

 class MyPojo {
 private String name;
 private int age;

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public int getAge() {
 return age;
 }

 public void setAge(int age) {
 this.age = age;
 }
 }
}