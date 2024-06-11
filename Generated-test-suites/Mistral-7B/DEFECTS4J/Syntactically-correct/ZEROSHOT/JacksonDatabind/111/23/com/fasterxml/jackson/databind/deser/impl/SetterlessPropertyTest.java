package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class SetterlessPropertyTest {

 private Object testObject;
 private SetterlessProperty property;
 private Method getter;

 @Before
 public void setup() throws InstantiationException, IllegalAccessException, NoSuchMethodException {
 testObject = new TestBean();
 property = new SetterlessProperty(new BeanPropertyDefinition("testProperty", TestBean.class, null, null),
 new TypeFactory().constructType(TestBean.class), null, Annotations.empty(),
 getTestPropertyGetter());
 getter = getTestPropertyGetter();
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 JsonParser parser = createContext().createParser("[]");
 property.deserializeAndSet(parser, createContext(), testObject);
 assertNotNull(testObject.testProperty);
 parser = createContext().createParser("[1,2,3]");
 property.deserializeAndSet(parser, createContext(), testObject);
 assertEquals("[1,2,3]", testObject.testProperty.toString());
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object result = property.deserializeSetAndReturn(createContext().createParser("[]"), createContext(), testObject);
 assertSame(testObject, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() throws IOException {
 property.set(testObject, new Object());
 fail();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetAndReturn() throws IOException {
 property.setAndReturn(testObject, new Object());
 fail();
 }

 @Test
 public void testGetAnnotation() throws IOException {
 JsonParser parser = createContext().createParser("{\"foo\":\"bar\"}");
 assertEquals(JsonProperty.class, property.getAnnotation(JsonProperty.class));
 assertEquals(JsonGetter.class, property.getAnnotation(JsonGetter.class));
 assertEquals(null, property.getAnnotation(JsonPropertyDescription.class));
 }

 @Test
 public void testGetMember() throws IOException {
 assertEquals(getTestPropertyGetter(), property.getMember());
 }

 private static Object newTestBeanWithProperty(Object value) throws InstantiationException, IllegalAccessException {
 return new TestBean() {{ testProperty = value; }};
 }

 private static Method getTestPropertyGetter() throws NoSuchMethodException {
 return TestBean.class.getMethod("getTestProperty");
 }

 private DeserializationContext createContext() {
 return new TestDeserializationContext();
 }

 static class TestBean {
 @JsonProperty("testProperty")
 private Object testProperty;

 public Object getTestProperty() {
 return testProperty;
 }

 public static class TestDeserializationContext extends ObjectMapper {
 TestDeserializationContext() { super(); }
 }
 }
}