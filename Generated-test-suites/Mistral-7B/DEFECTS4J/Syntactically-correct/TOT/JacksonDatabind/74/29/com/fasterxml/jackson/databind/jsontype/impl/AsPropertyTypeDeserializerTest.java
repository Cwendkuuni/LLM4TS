package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AsPropertyTypeDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testDeserializeWithPropertyName() throws JsonProcessingException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 MapperFactory.standardInstance().constructType(String.class),
 MapperFactory.standardInstance().getTypeIdResolver(),
 "type", true, MapperFactory.standardInstance().constructType(Integer.class),
 As.PROPERTY);
 ObjectNode input = MapperFactory.standardInstance().createObjectNode();
 input.put("type", "java.lang.Integer");
 input.put("value", 42);
 JsonParser parser = MAPPER.treeAsTokens(input);
 Object result = deserializer.deserializeTypedFromObject(parser, MAPPER.getDeserializationContext());
 assertEquals(42, result);
 }

 @Test
 public void testDeserializeWithMissingPropertyName() throws JsonProcessingException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 MapperFactory.standardInstance().constructType(String.class),
 MapperFactory.standardInstance().getTypeIdResolver(),
 "type", true, MapperFactory.standardInstance().constructType(Integer.class),
 As.PROPERTY);
 ObjectNode input = MapperFactory.standardInstance().createObjectNode();
 input.put("value", 42);
 JsonParser parser = MAPPER.treeAsTokens(input);
 assertNull(deserializer.deserializeTypedFromObject(parser, MAPPER.getDeserializationContext()));
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws JsonProcessingException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 MapperFactory.standardInstance().constructType(String.class),
 MapperFactory.standardInstance().getTypeIdResolver(),
 "type", true, MapperFactory.standardInstance().constructType(Integer.class),
 As.PROPERTY);
 ObjectNode input = MapperFactory.standardInstance().createObjectNode();
 input.put("type", "java.lang.Integer");
 input.put("value", 42);
 JsonParser parser = MAPPER.treeAsTokens(input);
 Object result = deserializer.deserializeWithNativeTypeId(parser, MAPPER.getDeserializationContext(), null);
 assertEquals(42, result);
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeWithMissingTypeName() throws JsonProcessingException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 MapperFactory.standardInstance().constructType(String.class),
 MapperFactory.standardInstance().getTypeIdResolver(),
 "type", true, MapperFactory.standardInstance().constructType(Integer.class),
 As.PROPERTY);
 ObjectNode input = MapperFactory.standardInstance().createObjectNode();
 input.put("value", 42);
 JsonParser parser = MAPPER.treeAsTokens(input);
 deserializer.deserializeTypedFromObject(parser, MAPPER.getDeserializationContext());
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeWithUnexpectedToken() throws JsonProcessingException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 MapperFactory.standardInstance().constructType(String.class),
 MapperFactory.standardInstance().getTypeIdResolver(),
 "type", true, MapperFactory.standardInstance().constructType(Integer.class),
 As.PROPERTY);
 ObjectNode input = MapperFactory.standardInstance().createObjectNode();
 input.put("value", 42);
 input.put("unexpected", "token");
 JsonParser parser = MAPPER.treeAsTokens(input);
 deserializer.deserializeTypedFromObject(parser, MAPPER.getDeserializationContext());
 }

 @Test
 public void testDeserializeWithDefaultImpl() throws JsonProcessingException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 MapperFactory.standardInstance().constructType(String.class),
 MapperFactory.standardInstance().getTypeIdResolver(),
 "type", true, MapperFactory.standardInstance().constructType(Integer.class),
 As.PROPERTY);
 ObjectNode input = MapperFactory.standardInstance().createObjectNode();
 input.put("value", "hello");
 JsonParser parser = MAPPER.treeAsTokens(input);
 String result = (String) deserializer.deserializeTypedUsingDefaultImpl(parser, MAPPER.getDeserializationContext(), null);
 assertEquals("hello", result);
 }

 @Test
 public void testDeserializeWithArrayWrapper() throws JsonProcessingException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 MapperFactory.standardInstance().constructType(String.class),
 MapperFactory.standardInstance().getTypeIdResolver(),
 "type", true, MapperFactory.standardInstance().constructType(Integer[].class),
 As.PROPERTY);
 ObjectNode input = MapperFactory.standardInstance().createObjectNode();
 input.put("value", "[1, 2, 3]");
 JsonParser parser = MAPPER.treeAsTokens(input);
 Object result = deserializer.deserializeTypedFromAny(parser, MAPPER.getDeserializationContext());
 assertEquals(new Integer[]{1, 2, 3}, result);
 }
}