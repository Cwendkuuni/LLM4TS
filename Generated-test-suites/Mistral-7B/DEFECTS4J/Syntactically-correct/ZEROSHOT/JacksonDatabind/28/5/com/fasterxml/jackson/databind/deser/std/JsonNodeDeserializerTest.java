package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JsonNodeDeserializer Test")
public class JsonNodeDeserializerTest {

 @BeforeEach
 public void setup() {}

 @AfterEach
 public void teardown() {}

 @Test
 @DisplayName("Test null deserialization")
 void testNullDeserialization() throws JsonProcessingException {
 JsonParser parser = createParser("null");
 JsonNode expected = JsonNodeFactory.instance.nullNode();
 JsonNode actual = JsonNodeDeserializer.instance.deserialize(parser, new DeserializationContext(), JsonNodeFactory.instance);
 assertEquals(expected, actual);
 }

 @Test
 @DisplayName("Test empty JSON Object deserialization")
 void testEmptyJsonObjectDeserialization() throws JsonProcessingException {
 JsonParser parser = createParser("{}");
 ObjectNode expected = JsonNodeFactory.instance.objectNode();
 ObjectNode actual = JsonNodeDeserializer.ObjectDeserializer.getInstance().deserialize(parser, new DeserializationContext(), JsonNodeFactory.instance);
 assertEquals(expected, actual);
 }

 @Test
 @DisplayName("Test empty JSON Array deserialization")
 void testEmptyJsonArrayDeserialization() throws JsonProcessingException {
 JsonParser parser = createParser("[]");
 ArrayNode expected = JsonNodeFactory.instance.arrayNode();
 ArrayNode actual = JsonNodeDeserializer.ArrayDeserializer.getInstance().deserialize(parser, new DeserializationContext(), JsonNodeFactory.instance);
 assertEquals(expected, actual);
 }

 @Test
 @DisplayName("Test deserialization of JSON Object with String field")
 void testJsonObjectWithStringFieldDeserialization() throws JsonProcessingException {
 JsonParser parser = createParser("{\"name\": \"John\"}");
 ObjectNode expected = JsonNodeFactory.instance.objectNode();
 expected.put("name", JsonNodeFactory.instance.textNode("John"));
 ObjectNode actual = JsonNodeDeserializer.ObjectDeserializer.getInstance().deserialize(parser, new DeserializationContext(), JsonNodeFactory.instance);
 assertEquals(expected, actual);
 }

 @Test
 @DisplayName("Test deserialization of JSON Object with integer field")
 void testJsonObjectWithIntegerFieldDeserialization() throws JsonProcessingException {
 JsonParser parser = createParser("{\"age\": 25}");
 ObjectNode expected = JsonNodeFactory.instance.objectNode();
 expected.put("age", JsonNodeFactory.instance.numberNode(25));
 ObjectNode actual = JsonNodeDeserializer.ObjectDeserializer.getInstance().deserialize(parser, new DeserializationContext(), JsonNodeFactory.instance);
 assertEquals(expected, actual);
 }

 @Test
 @DisplayName("Test deserialization of JSON Object with boolean field")
 void testJsonObjectWithBooleanFieldDeserialization() throws JsonProcessingException {
 JsonParser parser = createParser("{\"isMarried\": true}");
 ObjectNode expected = JsonNodeFactory.instance.objectNode();
 expected.put("isMarried", JsonNodeFactory.instance.booleanNode(true));
 ObjectNode actual = JsonNodeDeserializer.ObjectDeserializer.getInstance().deserialize(parser, new DeserializationContext(), JsonNodeFactory.instance);
 assertEquals(expected, actual);
 }

 @Test
 @DisplayName("Test deserialization of JSON Array with String elements")
 void testJsonArrayWithStringElementsDeserialization() throws JsonProcessingException {
 JsonParser parser = createParser("[\"Apple\", \"Banana\", \"Orange\"]");
 ArrayNode expected = JsonNodeFactory.instance.arrayNode();
 expected.add(JsonNodeFactory.instance.textNode("Apple"));
 expected.add(JsonNodeFactory.instance.textNode("Banana"));
 expected.add(JsonNodeFactory.instance.textNode("Orange"));
 ArrayNode actual = JsonNodeDeserializer.ArrayDeserializer.getInstance().deserialize(parser, new DeserializationContext(), JsonNodeFactory.instance);
 assertEquals(expected, actual);
 }

 @Test
 @DisplayName("Test deserialization of JSON Array with integer elements")
 void testJsonArrayWithIntegerElementsDeserialization() throws JsonProcessingException {
 JsonParser parser = createParser("[1, 2, 3]");
 ArrayNode expected = JsonNodeFactory.instance.arrayNode();
 expected.add(JsonNodeFactory.instance.numberNode(1));
 expected.add(JsonNodeFactory.instance.numberNode(2));
 expected.add(JsonNodeFactory.instance.numberNode(3));
 ArrayNode actual = JsonNodeDeserializer.ArrayDeserializer.getInstance().deserialize(parser, new DeserializationContext(), JsonNodeFactory.instance);
 assertEquals(expected, actual);
 }

 @Test
 @DisplayName("Test deserialization of JSON Array with boolean elements")
 void testJsonArrayWithBooleanElementsDeserialization() throws JsonProcessingException {
 JsonParser parser = createParser("[true, false, true]");
 ArrayNode expected = JsonNodeFactory.instance.arrayNode();
 expected.add(JsonNodeFactory.instance.booleanNode(true));
 expected.add(JsonNodeFactory.instance.booleanNode(false));
 expected.add(JsonNodeFactory.instance.booleanNode(true));
 ArrayNode actual = JsonNodeDeserializer.ArrayDeserializer.getInstance().deserialize(parser, new DeserializationContext(), JsonNodeFactory.instance);
 assertEquals(expected, actual);
 }

 @Test
 @DisplayName("Test deserialization of JSON Object with nested JSON Object")
 void testJsonObjectWithNestedJsonObjectDeserialization() throws JsonProcessingException {
 JsonParser parser = createParser("{\"person\": {\"name\": \"John\", \"age\": 25}}");
 ObjectNode expected = JsonNodeFactory.instance.objectNode();
 ObjectNode personNode = JsonNodeFactory.instance.objectNode();
 personNode.put("name", JsonNodeFactory.instance.textNode("John"));
 personNode.put("age", JsonNodeFactory.instance.numberNode(25));
 expected.set("person", personNode);
 ObjectNode actual = JsonNodeDeserializer.ObjectDeserializer.getInstance().deserialize(parser, new DeserializationContext(), JsonNodeFactory.instance);
 assertEquals(expected, actual);
 }

 @Test
 @DisplayName("Test deserialization of JSON Object with nested JSON Array")
 void testJsonObjectWithNestedJsonArrayDeserialization() throws JsonProcessingException {
 JsonParser parser = createParser("{\"hobbies\": [\"Reading\", \"Coding\", \"Gardening\"]}");
 ObjectNode expected = JsonNodeFactory.instance.objectNode();
 ArrayNode hobbiesNode = JsonNodeFactory.instance.arrayNode();
 hobbiesNode.add(JsonNodeFactory.instance.textNode("Reading"));
 hobbiesNode.add(JsonNodeFactory.instance.textNode("Coding"));
 hobbiesNode.add(JsonNodeFactory.instance.textNode("Gardening"));
 expected.set("hobbies", hobbiesNode);
 ObjectNode actual = JsonNodeDeserializer.ObjectDeserializer.getInstance().deserialize(parser, new DeserializationContext(), JsonNodeFactory.instance);
 assertEquals(expected, actual);
 }

 @Test
 @DisplayName("Test deserialization of JSON Object with duplicate field")
 void testJsonObjectWithDuplicateFieldDeserialization() throws JsonProcessingException {
 JsonParser parser = createParser("{\"name\": \"John\", \"name\": \"Doe\"}");
 ObjectNode expected = JsonNodeFactory.instance.objectNode();
 expected.put("name", JsonNodeFactory.instance.textNode("John"));
 ObjectNode actual = JsonNodeDeserializer.ObjectDeserializer.getInstance().deserialize(parser, new DeserializationContext(), JsonNodeFactory.instance);
 assertEquals(expected, actual);
 }

 private JsonParser createParser(String json) throws IOException {
 JsonFactory factory = new JsonFactory();
 return factory.createParser(json);
 }

}