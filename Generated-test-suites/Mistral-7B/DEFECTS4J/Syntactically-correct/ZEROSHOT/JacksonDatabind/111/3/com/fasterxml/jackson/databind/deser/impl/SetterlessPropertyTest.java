package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TestBase;

public class SetterlessPropertyTest extends TestBase {

 private static final String TEST_CLASS = SetterlessPropertyTest.class.getName();

 private ObjectMapper objectMapper;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testSetterlessProperty() throws IOException {
 // Test constructor
 SetterlessProperty setterlessProperty = new SetterlessProperty(
 new BeanPropertyDefinition(new PropertyName("test"), int[].class, null, null, null),
 null, null, null, new AnnotatedMethod(new Method("test", new Class[0][]) {
 @Override
 public Annotation[] getAnnotations() {
 return new Annotation[] { new JsonProperty("test") };
 }
 }, TestClass.class));

 assertEquals("test", setterlessProperty.getName());
 assertEquals(int[].class, setterlessProperty.getType());
 assertNull(setterlessProperty.getValueDeserializer());
 assertNull(setterlessProperty.getNullProvider());

 // Test withName method
 SetterlessProperty setterlessPropertyWithName = setterlessProperty.withName(new PropertyName("newTest"));
 assertEquals("newTest", setterlessPropertyWithName.getName());

 // Test withValueDeserializer method
 JsonDeserializer<Integer> intDeserializer = new StandardDeserializer<>(Integer.class);
 SetterlessProperty setterlessPropertyWithDeserializer = setterlessProperty.withValueDeserializer(intDeserializer);
 assertEquals(intDeserializer, setterlessPropertyWithDeserializer.getValueDeserializer());

 // Test withNullProvider method
 NullValueProvider nullValueProvider = new NullValueProvider() {
 @Override
 public Object nullValue(DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
 return null;
 }
 };
 SetterlessProperty setterlessPropertyWithNullProvider = setterlessProperty.withNullProvider(nullValueProvider);
 assertEquals(nullValueProvider, setterlessPropertyWithNullProvider.getNullProvider());

 // Test fixAccess method
 BeanDescription beanDescription = objectMapper.getIntrospector().findBeanDescription(TestClass.class);
 setterlessPropertyWithNullProvider.fixAccess(beanDescription.getConfig());
 Method getter = TestClass.class.getMethod("test");
 AnnotatedMember annotatedMember = AnnotatedMember.Std(getter);
 assertTrue(annotatedMember.isAccessible());

 // Test deserializeAndSet method
 TestClass testClass = new TestClass();
 Object testArray = new Object[0];
 setterlessPropertyWithNullProvider.deserializeAndSet(objectMapper.getFactory().createParser("[]"), objectMapper.getDeserializationContext(), testClass);
 assertArrayEquals((int[]) testArray, new int[0]);
 Object newTestArray = new Object[1];
 setterlessPropertyWithNullProvider.deserializeAndSet(objectMapper.getFactory().createParser("1"), objectMapper.getDeserializationContext(), testClass);
 assertArrayEquals((int[]) testArray, new int[] { 1 });

 // Test deserializeSetAndReturn method
 TestClass testClassWithSetAndReturn = new TestClass();
 assertEquals(testClassWithSetAndReturn, setterlessPropertyWithNullProvider.deserializeSetAndReturn(objectMapper.getFactory().createParser("[]"), objectMapper.getDeserializationContext(), testClassWithSetAndReturn));
 assertEquals(testClassWithSetAndReturn, setterlessPropertyWithNullProvider.deserializeSetAndReturn(objectMapper.getFactory().createParser("1"), objectMapper.getDeserializationContext(), testClassWithSetAndReturn));

 // Test set method with IllegalArgumentException
 try {
 setterlessPropertyWithNullProvider.set(testClass, null);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }

 // Test set method with UnsupportedOperationException
 try {
 setterlessPropertyWithNullProvider.set(testClass, new Object() {});
 fail();
 } catch (UnsupportedOperationException e) {
 // Expected
 }

 // Test setAndReturn method with IllegalArgumentException
 try {
 TestClass testClassWithSetAndReturn = new TestClass();
 assertEquals(testClassWithSetAndReturn, setterlessPropertyWithNullProvider.setAndReturn(testClassWithSetAndReturn, null));
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }

 // Test setAndReturn method with UnsupportedOperationException
 try {
 TestClass testClassWithSetAndReturn = new TestClass();
 assertEquals(testClassWithSetAndReturn, setterlessPropertyWithNullProvider.setAndReturn(testClassWithSetAndReturn, new Object() {}));
 fail();
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @JsonDeserialize(using = SetterlessProperty.class)
 static class TestClass {
 @JsonProperty("test")
 int[] test;
 }
}