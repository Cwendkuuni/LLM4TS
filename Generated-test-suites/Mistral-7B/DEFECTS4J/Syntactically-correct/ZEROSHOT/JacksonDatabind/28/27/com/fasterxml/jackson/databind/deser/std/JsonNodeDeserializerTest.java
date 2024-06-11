package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.RawValue;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

 private JsonFactory jsonFactory;
 private JsonParser jsonParser;
 private ObjectMapper objectMapper;
 private JsonNodeDeserializer jsonNodeDeserializer;
 private JsonDeserializer<JsonNode> objectDeserializer;
 private JsonDeserializer<JsonNode> arrayDeserializer;

 @Before
 public void setUp() throws Exception {
 jsonFactory = new JsonFactory();
 objectMapper = new ObjectMapper(jsonFactory);
 jsonParser = jsonFactory.createParser("{}");
 jsonNodeDeserializer = JsonNodeDeserializer.instance;
 objectDeserializer = JsonNodeDeserializer.ObjectDeserializer.getInstance();
 arrayDeserializer = JsonNodeDeserializer.ArrayDeserializer.getInstance();
 }

 @Test
 public void testGetDeserializer() throws Exception {
 Class<?> objectNodeClass = ObjectNode.class;
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(objectNodeClass);
 assertEquals(JsonNodeDeserializer.ObjectDeserializer.class, deserializer.getClass());

 Class<?> arrayNodeClass = ArrayNode.class;
 deserializer = JsonNodeDeserializer.getDeserializer(arrayNodeClass);
 assertEquals(JsonNodeDeserializer.ArrayDeserializer.class, deserializer.getClass());
 }

 @Test
 public void testNullValue() throws Exception {
 JsonNode result = jsonNodeDeserializer.deserialize(jsonParser, null);
 assertTrue(result instanceof NullNode);
 }

 @Test
 public void testEmptyObject() throws Exception {
 JsonNode result = jsonNodeDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
 assertTrue(result instanceof ObjectNode);
 assertEquals(0, ((ObjectNode) result).size());
 }

 @Test
 public void testEmptyArray() throws Exception {
 JsonNode result = jsonNodeDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
 assertTrue(result instanceof ArrayNode);
 assertEquals(0, ((ArrayNode) result).size());
 }

 @Test
 public void testObject() throws Exception {
 jsonParser.nextValue(); // consume START_OBJECT
 String fieldName = jsonParser.nextFieldName();
 assertNotNull(fieldName);
 JsonNode value = jsonParser.nextValue();
 JsonNode result = jsonNodeDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
 assertEquals(JsonToken.FIELD_NAME, jsonParser.getCurrentToken());
 assertEquals(fieldName, result.get("").asText());

 jsonParser.nextToken(); // consume VALUE_STRING
 value = jsonParser.nextValue();
 result = jsonNodeDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
 assertEquals(JsonToken.START_OBJECT, jsonParser.getCurrentToken());
 JsonNode fieldNode = result.get(fieldName);
 assertEquals(value.getClass(), fieldNode.getClass());
 assertEquals(value.asText(), fieldNode.asText());
 }

 @Test
 public void testArray() throws Exception {
 jsonParser.nextValue(); // consume START_ARRAY
 JsonNode result = jsonNodeDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
 assertEquals(JsonToken.START_ARRAY, jsonParser.getCurrentToken());
 JsonNode element = result.get(0);
 assertEquals(JsonToken.START_OBJECT, jsonParser.nextToken());
 JsonNode innerResult = jsonNodeDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
 assertEquals(JsonToken.END_OBJECT, jsonParser.getCurrentToken());
 assertEquals(ObjectNode.class, element.getClass());
 assertEquals(innerResult.getClass(), element.get("").asText());
 }

 @Test(expected = JsonMappingException.class)
 public void testUnexpectedToken() throws Exception {
 JsonNode result = jsonNodeDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
 fail("Expected JsonMappingException but got " + result.getClass());
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidDeserializer() throws Exception {
 Class<?> invalidClass = String.class;
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(invalidClass);
 JsonNode result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
 fail("Expected JsonMappingException but got " + result.getClass());
 }

 @Test
 public void testHandleDuplicateField() throws Exception {
 jsonParser.nextValue(); // consume START_OBJECT
 jsonParser.nextFieldName();
 jsonParser.nextValue(); // consume VALUE_STRING
 jsonParser.nextFieldName();
 jsonParser.nextValue(); // consume VALUE_STRING
 jsonParser.nextValue(); // consume VALUE_STRING
 jsonParser.nextValue(); // consume VALUE_STRING

 JsonNode result = jsonNodeDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
 assertEquals(JsonToken.FIELD_NAME, jsonParser.getCurrentToken());
 String fieldName = jsonParser.getCurrentName();
 JsonNode firstValue = result.get(fieldName);
 JsonNode secondValue = result.get(fieldName);

 // By default we don't care, and the last value is used
 assertEquals(secondValue.getClass(), firstValue.getClass());
 assertEquals(secondValue.asText(), firstValue.asText());
 }

 @Test
 public void testDeserializeObject() throws Exception {
 JsonParser p = jsonFactory.createParser("{\"field1\":\"value1\",\"field2\":[1,2,3]}");
 ObjectNode result = (ObjectNode) objectDeserializer.deserialize(p, objectMapper.getDeserializationContext());
 assertEquals(2, result.size());
 assertEquals("field1", result.fieldNames().nextValue());
 JsonNode field1 = result.get("field1");
 assertEquals(JsonToken.VALUE_STRING, field1.getClassID().asInt());
 assertEquals("value1", field1.asText());
 JsonNode field2 = result.get("field2");
 assertEquals(JsonToken.START_ARRAY, field2.getClassID().asInt());
 ArrayNode arrayNode = (ArrayNode) field2;
 assertEquals(3, arrayNode.size());
 assertEquals(1, arrayNode.get(0).asInt());
 assertEquals(2, arrayNode.get(1).asInt());
 assertEquals(3, arrayNode.get(2).asInt());
 }

 @Test
 public void testDeserializeArray() throws Exception {
 JsonParser p = jsonFactory.createParser("[1,2,3]");
 ArrayNode result = (ArrayNode) arrayDeserializer.deserialize(p, objectMapper.getDeserializationContext());
 assertEquals(3, result.size());
 assertEquals(1, result.get(0).asInt());
 assertEquals(2, result.get(1).asInt());
 assertEquals(3, result.get(2).asInt());
 }
}