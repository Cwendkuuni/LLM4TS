package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AsPropertyTypeDeserializerTest {

 private ObjectMapper objectMapper;
 private TypeIdResolver typeIdResolver;
 private AsPropertyTypeDeserializer asPropertyTypeDeserializer;

 @BeforeClass
 public void setup() {
 objectMapper = new ObjectMapper();
 typeIdResolver = mock(TypeIdResolver.class);
 asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(
 new SimpleType(Integer.class), typeIdResolver, "type", false, null, As.PROPERTY);
 }

 @Test
 public void testDeserializeTypedFromObjectWithPropertyName() throws Exception {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("type", "java.lang.Integer");
 jsonNode.put("value", 42);
 JsonParser jsonParser = objectMapper.treeAsTokens(jsonNode);
 Object result = asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, null);
 assertEquals(Integer.valueOf(42), result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithNativeTypeId() throws Exception {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("typeId", "com.fasterxml.jackson.databind.jsontype.TypeId");
 jsonNode.put("value", 42);
 JsonParser jsonParser = objectMapper.treeAsTokens(jsonNode);
 Object result = asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, null);
 assertInstanceOf(TypeId.class, result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithDefaultImpl() throws Exception {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("value", 42);
 JsonParser jsonParser = objectMapper.treeAsTokens(jsonNode);
 Object result = asPropertyTypeDeserializer.deserializeTypedUsingDefaultImpl(jsonParser, null, null);
 assertNotNull(result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithMissingProperty() throws Exception {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonParser = objectMapper.treeAsTokens(jsonNode);
 try {
 asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, null);
 fail();
 } catch (IOException e) {
 // expected
 }
 }
}