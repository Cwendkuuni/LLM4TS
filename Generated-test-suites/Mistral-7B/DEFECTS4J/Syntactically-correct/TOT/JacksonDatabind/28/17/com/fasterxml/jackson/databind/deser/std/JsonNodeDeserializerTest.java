package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testNullDeserialization() throws Exception {
 JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
 JsonNode result = deserializer.deserialize(mapper.getFactory().createParser("null"), new TestContext());
 assertEquals(NullNode.getInstance(), result);
 }

 @Test
 public void testEmptyObjectDeserialization() throws Exception {
 JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
 JsonNode result = deserializer.deserialize(mapper.getFactory().createParser("{}"), new TestContext());
 assertTrue(result.isObject());
 assertTrue(result.isEmpty());
 }

 @Test
 public void testEmptyArrayDeserialization() throws Exception {
 JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
 JsonNode result = deserializer.deserialize(mapper.getFactory().createParser("[]"), new TestContext());
 assertTrue(result.isArray());
 assertTrue(result.isEmpty());
 }

 @Test
 public void testScalarDeserialization() throws Exception {
 JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
 JsonNode result = deserializer.deserialize(mapper.getFactory().createParser("{\"name\":\"John\"}"), new TestContext());
 JsonNode expected = mapper.readTree("{\"name\":\"John\"}");
 assertEquals(expected, result);
 }

 @Test
 public void testObjectDeserialization() throws Exception {
 JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
 JsonNode result = deserializer.deserialize(mapper.getFactory().createParser("{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}"), new TestContext());
 JsonNode expected = mapper.readTree("{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}");
 assertEquals(expected, result);
 }

 @Test
 public void testArrayDeserialization() throws Exception {
 JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
 JsonNode result = deserializer.deserialize(mapper.getFactory().createParser("[1,2,3]"), new TestContext());
 ArrayNode expected = mapper.valueToTree(new int[]{1, 2, 3});
 assertEquals(expected, result);
 }

 @Test
 public void testNestedObjectDeserialization() throws Exception {
 JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
 JsonNode result = deserializer.deserialize(mapper.getFactory().createParser("{\"name\":{\"first\":\"John\",\"last\":\"Doe\"}}"), new TestContext());
 ObjectNode expected = mapper.readValue("{\"name\":{\"first\":\"John\",\"last\":\"Doe\"}}", ObjectNode.class);
 assertEquals(expected, result);
 }

 @Test
 public void testNestedArrayDeserialization() throws Exception {
 JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
 JsonNode result = deserializer.deserialize(mapper.getFactory().createParser("{\"numbers\":[1,[2,3],4,[5,6]]}"), new TestContext());
 ArrayNode expected = mapper.valueToTree(new int[]{1, new int[]{2, 3}, 4, new int[]{5, 6}});
 assertEquals(expected, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDuplicateFieldDeserialization() throws Exception {
 JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
 JsonNode result = deserializer.deserialize(mapper.getFactory().createParser("{\"name\":\"John\",\"name\":\"Doe\"}"), new TestContext());
 fail("Expected JsonMappingException but got: " + result);
 }

 @Test
 public void testInvalidJsonDeserialization() throws Exception {
 JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
 try {
 deserializer.deserialize(mapper.getFactory().createParser("{\"name\":\"John\"{"), new TestContext());
 fail("Expected JsonMappingException but got null");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testUnknownTypeDeserialization() throws Exception {
 JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
 try {
 deserializer.deserialize(mapper.getFactory().createParser("{\"@type\":\"com.test.UnknownType\"}"), new TestContext());
 fail("Expected JsonMappingException but got: " + deserializer.deserialize(mapper.getFactory().createParser("{\"@type\":\"com.test.UnknownType\"}"), new TestContext()));
 } catch (JsonMappingException e) {
 // Expected
 }
}

 class TestContext extends DeserializationContext {
 @Override
 public JsonNodeFactory nodeFactory() { return mapper.getNodeFactory(); }
 }
}