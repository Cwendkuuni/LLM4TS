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

 private ObjectMapper objectMapper;
 private AsPropertyTypeDeserializer asPropertyTypeDeserializer;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(
 Object.class, null, "typePropertyName", true, Object.class, As.PROPERTY);
 }

 @Test
 public void testDeserializeTypedFromObjectWithTypeId() throws Exception {
 ObjectNode rootNode = objectMapper.createObjectNode();
 rootNode.put("typePropertyName", "MyTypeId");
 rootNode.put("value", "Test Value");

 JsonParser jsonParser = objectMapper.treeAsTokens(rootNode);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 TypeIdResolver typeIdResolver = new StandardTypeIdResolver();
 TypeDeserializer deserializer = asPropertyTypeDeserializer.forProperty(new StdHandlerMethodPropertyDescriptor("testProperty", Object.class));

 Object deserializedObject = deserializer.deserializeTypedFromObject(jsonParser, ctxt);
 assertEquals("Test Value", deserializedObject);
 }

 @Test
 public void testDeserializeTypedFromObjectWithoutTypeId() throws Exception {
 ObjectNode rootNode = objectMapper.createObjectNode();
 rootNode.put("value", "Test Value");

 JsonParser jsonParser = objectMapper.treeAsTokens(rootNode);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 TypeIdResolver typeIdResolver = new StandardTypeIdResolver();
 TypeDeserializer deserializer = asPropertyTypeDeserializer.forProperty(new StdHandlerMethodPropertyDescriptor("testProperty", Object.class));

 Object deserializedObject = deserializer.deserializeTypedFromObject(jsonParser, ctxt);
 assertNull(deserializedObject);
 }

 @Test
 public void testDeserializeTypedFromObjectWithPropertyNameButNoTypeId() throws Exception {
 ObjectNode rootNode = objectMapper.createObjectNode();
 rootNode.put("testPropertyName", "Test Value");

 JsonParser jsonParser = objectMapper.treeAsTokens(rootNode);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 TypeIdResolver typeIdResolver = new StandardTypeIdResolver();
 TypeDeserializer deserializer = asPropertyTypeDeserializer.forProperty(new StdHandlerMethodPropertyDescriptor("testProperty", Object.class));

 Object deserializedObject = deserializer.deserializeTypedFromObject(jsonParser, ctxt);
 assertNull(deserializedObject);
 }

 @Test
 public void testDeserializeTypedFromObjectWithPropertyNameAndTypeId() throws Exception {
 ObjectNode rootNode = objectMapper.createObjectNode();
 rootNode.put("testPropertyName", "MyTypeId");
 rootNode.put("value", "Test Value");

 JsonParser jsonParser = objectMapper.treeAsTokens(rootNode);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 TypeIdResolver typeIdResolver = new StandardTypeIdResolver();
 TypeDeserializer deserializer = asPropertyTypeDeserializer.forProperty(new StdHandlerMethodPropertyDescriptor("testProperty", Object.class));

 Object deserializedObject = deserializer.deserializeTypedFromObject(jsonParser, ctxt);
 assertEquals("Test Value", deserializedObject);
 }

 @Test
 public void testDeserializeTypedFromObjectWithDefaultImpl() throws Exception {
 ObjectNode rootNode = objectMapper.createObjectNode();
 rootNode.put("value", "Test Value");

 JsonParser jsonParser = objectMapper.treeAsTokens(rootNode);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 TypeIdResolver typeIdResolver = new StandardTypeIdResolver();
 TypeDeserializer deserializer = asPropertyTypeDeserializer.forProperty(new StdHandlerMethodPropertyDescriptor("testProperty", Integer.class));

 Object deserializedObject = deserializer.deserializeTypedFromObject(jsonParser, ctxt);
 assertEquals(Integer.valueOf(0), deserializedObject);
 }
}