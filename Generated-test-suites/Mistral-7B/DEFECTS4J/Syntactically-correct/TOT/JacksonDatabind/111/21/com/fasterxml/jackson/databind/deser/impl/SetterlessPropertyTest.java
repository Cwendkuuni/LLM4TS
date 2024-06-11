package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Before;
import org.junit.Test;

public class SetterlessPropertyTest {

 private ObjectMapper objectMapper;
 private SetterlessProperty setterlessProperty;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 setterlessProperty = new SetterlessProperty(
 new BeanPropertyDefinition(null, "testList", List.class, null, null),
 null, null, null, new AnnotatedMethod() {
 @Override public Annotation[] getAnnotations() { return new Annotation[0]; }
 @Override public Method getAnnotatedMember() { return null; }
 @Override public Method getMethod() { throw new UnsupportedOperationException(); }
 @Override public Annotation getAnnotation(Class<?> annotationType) { return null; }
 });
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object testObject = new Object();
 List testList = new ArrayList();
 testList.add("test");
 setterlessProperty.setName("testList");
 setterlessProperty.setType(List.class);
 setterlessProperty.setValueDeserializer(new StringDeserializer());
 Object deserializedObject = objectMapper.readValue(
 "[\"test\"]", SetterlessProperty.class);
 assertEquals(deserializedObject, setterlessProperty);
 assertSame(testList, deserializedObject._getter.invoke(testObject, (Object[]) null));
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetWithNullValue() throws IOException {
 Object testObject = new Object();
 List testList = new ArrayList();
 setterlessProperty.setName("testList");
 setterlessProperty.setType(List.class);
 setterlessProperty.setValueDeserializer(new StringDeserializer());
 Object deserializedObject = objectMapper.readValue(
 "[null]", SetterlessProperty.class);
 fail("Expected IOException when deserializing null value");
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeAndSetWithInvalidJson() throws IOException {
 Object testObject = new Object();
 List testList = new ArrayList();
 setterlessProperty.setName("testList");
 setterlessProperty.setType(List.class);
 setterlessProperty.setValueDeserializer(new StringDeserializer());
 Object deserializedObject = objectMapper.readValue(
 "[]", SetterlessProperty.class);
 fail("Expected JsonProcessingException when deserializing empty JSON array");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeAndSetWithMissingPropertyAnnotation() throws IOException {
 Object testObject = new Object();
 setterlessProperty.setName("testList");
 setterlessProperty.setType(List.class);
 Object deserializedObject = objectMapper.readValue(
 "[\"test\"]", SetterlessProperty.class);
 fail("Expected JsonMappingException when property does not have @JsonProperty annotation");
 }

 @Test
 public void testSetterlessPropertyWithTypeDeserializer() throws IOException {
 Object testObject = new Object();
 List testList = new ArrayList();
 testList.add(new Foo());
 TypeDeserializer typeDeserializer = new StdTypeDeserializer();
 setterlessProperty.setName("testList");
 setterlessProperty.setType(List.class);
 setterlessProperty.setValueDeserializer(null);
 setterlessProperty.setTypeDeserializer(typeDeserializer);
 Object deserializedObject = objectMapper.readValue(
 "[{\"bar\": \"foo\"}]", SetterlessProperty.class);
 assertEquals(deserializedObject, setterlessProperty);
 assertTrue(testList.equals(deserializedObject._getter.invoke(testObject, (Object[]) null)));
 assertEquals(Foo.class, typeDeserializer.deserializeValueAs(deserializedObject, Foo.class));
 }

 @Test
 public void testSetterlessPropertyWithNullValueProvider() throws IOException {
 Object testObject = new Object();
 List testList = new ArrayList();
 setterlessProperty.setName("testList");
 setterlessProperty.setType(List.class);
 NullValueProvider nva = new StandardNullValueProvider();
 setterlessProperty.setValueDeserializer(null);
 setterlessProperty.setNullProvider(nva);
 Object deserializedObject = objectMapper.readValue(
 "[null]", SetterlessProperty.class);
 assertEquals(deserializedObject, setterlessProperty);
 assertNull(deserializedObject._getter.invoke(testObject, (Object[]) null));
 }

 @Test
 public void testFixAccess() throws IOException {
 Object testObject = new Object();
 List testList = new ArrayList();
 testList.add("test");
 setterlessProperty.setName("testList");
 setterlessProperty.setType(List.class);
 setterlessProperty.setValueDeserializer(new StringDeserializer());
 Object deserializedObject = objectMapper.readValue(
 "[\"test\"]", SetterlessProperty.class);
 deserializedObject.fixAccess(objectMapper.getConfiguration());
 assertFalse(deserializedObject._getter.isAccessible());
 try {
 deserializedObject._getter.invoke(deserializedObject, (Object[]) null);
 fail("Expected IllegalAccessException when trying to access getter directly");
 } catch (IllegalAccessException e) {
 // Expected
 }
 }

 @Test
 public void testSetAndReturn() throws IOException, InvocationTargetException, IllegalAccessException {
 Object testObject = new Object();
 List testList = new ArrayList();
 testList.add("test");
 setterlessProperty.setName("testList");
 setterlessProperty.setType(List.class);
 setterlessProperty.setValueDeserializer(new StringDeserializer());
 Object deserializedObject = objectMapper.readValue(
 "[\"test\"]", SetterlessProperty.class);
 Object newValue = new Object();
 deserializedObject.set(testObject, newValue);
 assertEquals(newValue, deserializedObject.setAndReturn(testObject, newValue));
 }
}

class Foo {
 private String bar;

 public String getBar() {
 return bar;
 }

 public void setBar(String bar) {
 this.bar = bar;
 }
}