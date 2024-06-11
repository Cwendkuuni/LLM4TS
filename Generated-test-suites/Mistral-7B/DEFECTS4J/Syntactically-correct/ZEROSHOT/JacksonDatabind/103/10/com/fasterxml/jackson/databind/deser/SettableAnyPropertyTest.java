package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

 private ObjectMapper objectMapper;
 private SettableAnyProperty settableAnyProperty;

 @Before
 public void setup() throws JsonProcessingException {
 objectMapper = new ObjectMapper();
 SettableBeanProperty.Base property = new SettableBeanProperty.Std("anyProperty");
 AnnotatedMember setter = new AnnotatedMethod(SettableAnyPropertyTest.class, "setAnyProperty", new TypeReference<Object>() {});
 JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
 settableAnyProperty = new SettableAnyProperty(property, setter, javaType, null, null, null);
 }

 @Test
 public void testSettableAnyPropertyWithNullValueDeserializer() throws IOException {
 Object pojo = new Object();
 settableAnyProperty.deserializeAndSet(null, objectMapper.getDeserializationConfig(), pojo, "anyProperty");
 assertTrue(pojo instanceof String);
 assertNull(getPropertyValue(pojo, "anyProperty"));
 }

 @Test
 public void testSettableAnyPropertyWithValueDeserializer() throws IOException {
 Object pojo = new Object();
 JsonParser parser = objectMapper.getFactory().createParser("{\"anyProperty\":\"test\"}");
 settableAnyProperty = settableAnyProperty.withValueDeserializer(new StringDeserializer(objectMapper));
 settableAnyProperty.deserializeAndSet(parser, objectMapper.getDeserializationConfig(), pojo, "anyProperty");
 assertTrue(pojo instanceof String);
 assertEquals("test", getPropertyValue(pojo, "anyProperty"));
 }

 @Test(expected = JsonMappingException.class)
 public void testSettableAnyPropertyWithInvalidType() throws IOException {
 Object pojo = new Object();
 JsonParser parser = objectMapper.getFactory().createParser("{\"anyProperty\":1}");
 settableAnyProperty = new SettableAnyProperty(settableAnyProperty.getProperty(), settableAnyProperty.getSetter(), new TypeReference<Integer>() {}, null, null, null);
 settableAnyProperty.deserializeAndSet(parser, objectMapper.getDeserializationConfig(), pojo, "anyProperty");
 fail("Expected JsonMappingException but no exception was thrown.");
 }

 @Test
 public void testSettableAnyPropertyWithValueTypeDeserializer() throws IOException {
 Object pojo = new Object();
 JsonParser parser = objectMapper.getFactory().createParser("{\"anyProperty\":{\"type\":\"Integer\",\"value\":1}}");
 settableAnyProperty = settableAnyProperty.withValueDeserializer(null).withValueTypeDeserializer(new IntegerDeserializer(objectMapper));
 settableAnyProperty.deserializeAndSet(parser, objectMapper.getDeserializationConfig(), pojo, "anyProperty");
 assertTrue(pojo instanceof Map);
 Map map = (Map) pojo;
 assertEquals(1, map.get("value"));
 }

 @Test
 public void testSettableAnyPropertyWithKeyDeserializer() throws IOException {
 Object pojo = new Object();
 JsonParser parser = objectMapper.getFactory().createParser("{\"key\":\"testKey\",\"anyProperty\":{\"type\":\"Integer\",\"value\":1}}");
 settableAnyProperty = settableAnyProperty.withKeyDeserializer(new KeyDeserializer() {
 @Override
 public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
 return key.equals("testKey") ? 1 : null;
 }
 });
 settableAnyProperty.deserializeAndSet(parser, objectMapper.getDeserializationConfig(), pojo, null);
 assertTrue(pojo instanceof Map);
 Map map = (Map) pojo;
 assertEquals(1, map.get(1));
 }

 private Object getPropertyValue(Object pojo, String propertyName) {
 try {
 return ((Map) pojo).get(propertyName);
 } catch (NullPointerException e) {
 return null;
 }
 }

}