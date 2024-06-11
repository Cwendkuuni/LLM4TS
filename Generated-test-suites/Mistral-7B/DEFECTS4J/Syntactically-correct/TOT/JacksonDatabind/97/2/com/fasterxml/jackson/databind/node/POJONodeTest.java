package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class POJONodeTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testPOJONodeWithStringValue() throws JsonProcessingException {
 Object pojo = new String("testString");
 POJONode pojoNode = new POJONode(pojo);
 JsonNode expectedJsonNode = mapper.valueToTree(pojo);

 assertEquals(expectedJsonNode.getNodeType(), pojoNode.getNodeType());
 assertEquals(expectedJsonNode.asText(), pojoNode.asText());
 assertEquals(expectedJsonNode.asText(null), pojoNode.asText(null));
 assertEquals(expectedJsonNode.asBoolean(false), pojoNode.asBoolean(false));
 assertEquals(expectedJsonNode.asInt(0), pojoNode.asInt(0));
 assertEquals(expectedJsonNode.asLong(0), pojoNode.asLong(0));
 assertEquals(expectedJsonNode.asDouble(0.0), pojoNode.asDouble(0.0));

 Object deserializedPojo = mapper.treeToValue(pojoNode, String.class);
 assertEquals(pojo, deserializedPojo);
 }

 @Test
 public void testPOJONodeWithBooleanValue() throws JsonProcessingException {
 Object pojo = true;
 POJONode pojoNode = new POJONode(pojo);
 JsonNode expectedJsonNode = mapper.valueToTree(pojo);

 assertEquals(expectedJsonNode.getNodeType(), pojoNode.getNodeType());
 assertEquals(expectedJsonNode.asBoolean(false), pojoNode.asBoolean(false));
 assertEquals(expectedJsonNode.asBoolean(true), pojoNode.asBoolean(true));

 Object deserializedPojo = mapper.treeToValue(pojoNode, Boolean.class);
 assertEquals(pojo, deserializedPojo);
 }

 @Test
 public void testPOJONodeWithIntegerValue() throws JsonProcessingException {
 Object pojo = 123;
 POJONode pojoNode = new POJONode(pojo);
 JsonNode expectedJsonNode = mapper.valueToTree(pojo);

 assertEquals(expectedJsonNode.getNodeType(), pojoNode.getNodeType());
 assertEquals(expectedJsonNode.asInt(0), pojoNode.asInt(0));
 assertEquals(expectedJsonNode.asLong(0), pojoNode.asLong(0));
 assertEquals(expectedJsonNode.asDouble(123.0), pojoNode.asDouble(0.0));

 Object deserializedPojo = mapper.treeToValue(pojoNode, Integer.class);
 assertEquals(pojo, deserializedPojo);
 }

 @Test
 public void testPOJONodeWithLongValue() throws JsonProcessingException {
 Object pojo = 123L;
 POJONode pojoNode = new POJONode(pojo);
 JsonNode expectedJsonNode = mapper.valueToTree(pojo);

 assertEquals(expectedJsonNode.getNodeType(), pojoNode.getNodeType());
 assertEquals(expectedJsonNode.asInt(0), pojoNode.asInt(0));
 assertEquals(expectedJsonNode.asLong(123), pojoNode.asLong(0));
 assertEquals(expectedJsonNode.asDouble(123.0), pojoNode.asDouble(0.0));

 Object deserializedPojo = mapper.treeToValue(pojoNode, Long.class);
 assertEquals(pojo, deserializedPojo);
 }

 @Test
 public void testPOJONodeWithDoubleValue() throws JsonProcessingException {
 Object pojo = 123.45;
 POJONode pojoNode = new POJONode(pojo);
 JsonNode expectedJsonNode = mapper.valueToTree(pojo);

 assertEquals(expectedJsonNode.getNodeType(), pojoNode.getNodeType());
 assertEquals(expectedJsonNode.asInt(0), pojoNode.asInt(0));
 assertEquals(expectedJsonNode.asLong(0), pojoNode.asLong(0));
 assertEquals(expectedJsonNode.asDouble(123.45), pojoNode.asDouble(0.0));

 Object deserializedPojo = mapper.treeToValue(pojoNode, Double.class);
 assertEquals(pojo, deserializedPojo);
 }

 @Test
 public void testPOJONodeWithNullValue() throws JsonProcessingException {
 POJONode pojoNode = new POJONode(null);
 JsonNode expectedJsonNode = mapper.valueToTree(null);

 assertEquals(expectedJsonNode.getNodeType(), pojoNode.getNodeType());
 assertNull(pojoNode.asText());
 assertNull(pojoNode.asText(null));
 assertFalse(pojoNode.asBoolean(false));
 assertFalse(pojoNode.asBoolean(true));
 assertNull(pojoNode.asInt(0));
 assertNull(pojoNode.asLong(0));
 assertNull(pojoNode.asDouble(0.0));
 assertEquals(expectedJsonNode.getNodeType(), pojoNode.getNodeType());
 }

 @Test
 public void testPOJONodeWithByteArrayValue() throws JsonProcessingException {
 byte[] byteArray = {1, 2, 3, 4};
 POJONode pojoNode = new POJONode(byteArray);
 JsonNode expectedJsonNode = mapper.valueToTree(byteArray);

 assertEquals(expectedJsonNode.getNodeType(), pojoNode.getNodeType());
 assertSame(expectedJsonNode.binaryValue(), pojoNode.binaryValue());
 }

 @Test(expected = IOException.class)
 public void testPOJONodeWithNonSerializableValue() throws Exception {
 // This test case intentionally causes an IOException
 POJONode pojoNode = new POJONode(new Object());
 }

 @Test
 public void testPOJONodeSerialization() throws JsonProcessingException {
 Object pojo = new String("testString");
 POJONode pojoNode = new POJONode(pojo);

 StringBuilder sb = new StringBuilder();
 JsonSerializer serializer = mapper.getSerializerProvider().getSerializers().findValueSerializer(pojoNode.getClass(), sb);

 serializer.serialize(pojoNode, mapper.getSerializerProvider(), mapper.getSerializerProvider().getRootValueSerializer());

 JsonNode expectedJsonNode = mapper.valueToTree(pojo);

 assertEquals(expectedJsonNode.getNodeType(), pojoNode.getNodeType());
 assertEquals(expectedJsonNode.asText(), sb.toString());
 }
}