package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AsPropertyTypeDeserializerTest {

 private ObjectMapper mapper;
 private AsPropertyTypeDeserializer deserializer;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(), "typePropertyName", true,
 mapper.getTypeFactory().constructType(Integer.class), As.PROPERTY);
 }

 @Test
 public void testDeserializeTypedFromObjectWithPropertyName() throws Exception {
 ObjectNode node = mapper.createObjectNode();
 node.put("typePropertyName", "intTypeId");
 node.put("value", 42);
 JsonParser parser = mapper.treeAsTokens(node);
 Object result = deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());
 assertEquals(Integer.TYPE, result.getClass());
 assertEquals(42, result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithMissingPropertyName() throws Exception {
 ObjectNode node = mapper.createObjectNode();
 node.put("value", 42);
 JsonParser parser = mapper.treeAsTokens(node);
 assertNull(deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext()));
 }

 @Test
 public void testDeserializeTypedFromObjectWithNativeTypeId() throws Exception {
 ObjectNode node = mapper.createObjectNode();
 node.put("typePropertyName", "intTypeId");
 node.put("value", 42);
 node.put("@class", "java.lang.Integer");
 JsonParser parser = mapper.treeAsTokens(node);
 Object result = deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());
 assertEquals(Integer.TYPE, result.getClass());
 assertEquals(42, result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithDefaultImpl() throws Exception {
 ObjectNode node = mapper.createObjectNode();
 node.put("value", 42);
 JsonParser parser = mapper.treeAsTokens(node);
 Object result = deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());
 assertEquals(Integer.class, result.getClass());
 assertEquals(42, result);
 }

 @Test
 public void testDeserializeTypedFromArray() throws Exception {
 JsonParser parser = mapper.parse("[1, 2, 3]");
 Object result = deserializer.deserializeTypedFromArray(parser, mapper.getDeserializationContext());
 assertArrayEquals(new Integer[]{1, 2, 3}, (Object[])result);
 }

 @Test
 public void testDeserializeTypedFromScalar() throws Exception {
 JsonParser parser = mapper.parse("42");
 Object result = deserializer.deserializeTypedFromScalar(parser, mapper.getDeserializationContext());
 assertEquals(Integer.TYPE, result.getClass());
 assertEquals(42, result);
 }
}