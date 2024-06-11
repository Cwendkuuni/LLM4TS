package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Before;
import org.junit.Test;

public class AsPropertyTypeDeserializerTest {

 private ObjectMapper objectMapper;
 private AsPropertyTypeDeserializer deserializer;

 @Before
 public void setup() throws IOException {
 objectMapper = new ObjectMapper();
 JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
 deserializer = new AsPropertyTypeDeserializer(javaType, null, "typePropertyName", true, null, As.PROPERTY);
 }

 @Test
 public void testDeserializeTypedFromObject_withNativeTypeId() throws IOException {
 ObjectNode input = new ObjectMapper().createObjectNode();
 input.put("typePropertyName", "StringType");
 JsonParser jp = objectMapper.treeAsTokens(input);
 DeserializationContext ctxt = new TestDeserializationContext();
assertEquals(String.class, deserializer.deserializeTypedFromObject(jp, ctxt).getClass());
 }

 @Test
 public void testDeserializeTypedFromObject_withFieldName() throws IOException {
 ObjectNode input = new ObjectMapper().createObjectNode();
 input.put("typePropertyName", new ObjectMapper().valueToTree("StringType"));
 JsonParser jp = objectMapper.treeAsTokens(input);
 DeserializationContext ctxt = new TestDeserializationContext();
assertEquals(String.class, deserializer.deserializeTypedFromObject(jp, ctxt).getClass());
 }

 @Test
 public void testDeserializeTypedFromObject_withoutTypeProperty() throws IOException {
 ObjectNode input = new ObjectMapper().createObjectNode();
 JsonParser jp = objectMapper.treeAsTokens(input);
 DeserializationContext ctxt = new TestDeserializationContext();
assertNull(deserializer.deserializeTypedFromObject(jp, ctxt));
 }

 @Test
 public void testDeserializeTypedFromObject_withDefaultImpl() throws IOException {
 ObjectNode input = new ObjectMapper().createObjectNode();
 input.put("typePropertyName", new ObjectMapper().valueToTree(Integer.class));
 JsonParser jp = objectMapper.treeAsTokens(input);
 DeserializationContext ctxt = new TestDeserializationContext();
assertEquals(Integer.class, deserializer.deserializeTypedFromObject(jp, ctxt).getClass());
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeTypedFromObject_withInvalidTypeProperty() throws IOException {
 ObjectNode input = new ObjectMapper().createObjectNode();
 input.put("invalidTypePropertyName", "StringType");
 JsonParser jp = objectMapper.treeAsTokens(input);
 DeserializationContext ctxt = new TestDeserializationContext();
deserializer.deserializeTypedFromObject(jp, ctxt);
 }

 @Test
 public void testDeserializeTypedFromArray_withNativeTypeId() throws IOException {
 ObjectNode input = new ObjectMapper().createObjectNode();
 input.put("typePropertyName", "StringType");
 input.put("items", new ObjectMapper().valueToTree(new String[]{"item1", "item2"}));
 JsonParser jp = objectMapper.treeAsTokens(input);
 DeserializationContext ctxt = new TestDeserializationContext();
assertEquals(String[].class, deserializer.deserializeTypedFromAny(jp, ctxt).getClass());
 }

 @Test
 public void testDeserializeTypedFromAny_withFieldName() throws IOException {
 ObjectNode input = new ObjectMapper().createObjectNode();
 input.put("typePropertyName", new ObjectMapper().valueToTree("StringType"));
 input.put("propertyName", "propertyValue");
 JsonParser jp = objectMapper.treeAsTokens(input);
 DeserializationContext ctxt = new TestDeserializationContext();
assertEquals("propertyValue", deserializer.deserializeTypedFromObject(jp, ctxt).toString());
 }

 @Test
 public void testDeserializeTypedFromAny_withoutTypeProperty() throws IOException {
 ObjectNode input = new ObjectMapper().createObjectNode();
 JsonParser jp = objectMapper.treeAsTokens(input);
 DeserializationContext ctxt = new TestDeserializationContext();
assertNull(deserializer.deserializeTypedFromObject(jp, ctxt));
 }
}

class TestDeserializationContext extends DeserializationContext {
 @Override
 public JsonDeserializer<?> findBeanDeserializer(BeanDescription beanDesc, JsonParser p) throws IOException {
 return null;
 }

 @Override
 public JsonDeserializer<?> findValueDeserializer(Class<?> vc, JsonParser p) throws IOException {
 return null;
 }
}