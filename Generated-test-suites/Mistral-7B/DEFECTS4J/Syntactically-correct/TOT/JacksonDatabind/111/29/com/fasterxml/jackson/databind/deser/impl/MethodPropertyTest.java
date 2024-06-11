package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MethodPropertyTest {

 private ObjectMapper mapper;
 private MethodProperty property;

 @Before
 public void setUp() {
 mapper = new ObjectMapper();
 property = new MethodProperty(new AnnotatedMethod() {
 @Override
 public Annotation[] getParameterAnnotations() {
 return new Annotation[0];
 }

 @Override
 public Annotation[] getDeclaredAnnotations() {
 return new Annotation[0];
 }

 @Override
 public Annotation[] getAnnotations() {
 return new Annotation[0];
 }

 @Override
 public Class<?> getDeclaringClass() {
 return TestClass.class;
 }

 @Override
 public String getName() {
 return "testProperty";
 }

 @Override
 public Method getMethod() {
 return TestClass.class.getMethod("setTestProperty", Object.class);
 }
 }, JavaType.getInstance(String.class), null, null, property.getAnnotated());
 }

 @Test
 public void testSetValue_null() throws JsonProcessingException {
 property.set(new Object(), null);
 assertEquals(null, new Object());
 }

 @Test
 public void testSetValue_valid() throws JsonProcessingException {
 ObjectNode node = mapper.createObjectNode();
 node.put("value", "test");
 JsonNode jsonNode = mapper.readTree(node.toString());
 property.deserializeAndSet(jsonNode.asIterable().next(), mapper.getDeserializationContext(), new Object());
 assertEquals("test", new Object());
 }

 @Test
 public void testSetValue_invalidType() throws JsonProcessingException {
 ObjectNode node = mapper.createObjectNode();
 node.put("value", 123);
 JsonNode jsonNode = mapper.readTree(node.toString());
 try {
 property.deserializeAndSet(jsonNode.asIterable().next(), mapper.getDeserializationContext(), new Object());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 assertEquals("Can not instantiate value of type [simple type, class java.lang.String] from JSON String: 123", e.getMessage());
 }
 }

 @Test
 public void testSetValue_emptyString() throws JsonProcessingException {
 ObjectNode node = mapper.createObjectNode();
 JsonNode jsonNode = mapper.readTree(node.toString());
 property.deserializeAndSet(jsonNode.asIterable().next(), mapper.getDeserializationContext(), new Object());
 assertNull(new Object());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetValue_noSetter() {
 property = new MethodProperty(new AnnotatedMethod() {
 @Override
 public Annotation[] getParameterAnnotations() {
 return new Annotation[0];
 }

 @Override
 public Annotation[] getDeclaredAnnotations() {
 return new Annotation[0];
 }

 @Override
 public Annotation[] getAnnotations() {
 return new Annotation[0];
 }

 @Override
 public Class<?> getDeclaringClass() {
 return TestClass.class;
 }

 @Override
 public String getName() {
 return "testProperty";
 }

 @Override
 public Method getMethod() {
 return TestClass.class.getMethod("testMethod", new Class[0]);
 }
 }, JavaType.getInstance(String.class), null, null, property.getAnnotated());
 property.set(new Object(), "test");
 }

 @Test
 public void testSetValue_throwException() throws JsonProcessingException {
 property = new MethodProperty(new AnnotatedMethod() {
 @Override
 public Annotation[] getParameterAnnotations() {
 return new Annotation[0];
 }

 @Override
 public Annotation[] getDeclaredAnnotations() {
 return new Annotation[0];
 }

 @Override
 public Annotation[] getAnnotations() {
 return new Annotation[0];
 }

 @Override
 public Class<?> getDeclaringClass() {
 return TestClass.class;
 }

 @Override
 public String getName() {
 return "testProperty";
 }

 @Override
 public Method getMethod() {
 return TestClass.class.getMethod("setTestProperty", Object.class);
 }
 }, JavaType.getInstance(String.class), null, null, property.getAnnotated()) {
 @Override
 public void invoke(Object obj, Object arg) throws Throwable {
 throw new RuntimeException("Test exception");
 }
 };
 ObjectNode node = mapper.createObjectNode();
 node.put("value", "test");
 JsonNode jsonNode = mapper.readTree(node.toString());
 try {
 property.deserializeAndSet(jsonNode.asIterable().next(), mapper.getDeserializationContext(), new Object());
 fail("Expected JsonMappingException or RuntimeException");
 } catch (JsonMappingException e) {
 // Expected JsonMappingException
 } catch (RuntimeException e) {
 // Expected RuntimeException
 }
 }

 class TestClass {
 private String testProperty;

 public void setTestProperty(String value) {
 this.testProperty = value;
 }

 public void testMethod() {
 // empty method
 }
 }
}