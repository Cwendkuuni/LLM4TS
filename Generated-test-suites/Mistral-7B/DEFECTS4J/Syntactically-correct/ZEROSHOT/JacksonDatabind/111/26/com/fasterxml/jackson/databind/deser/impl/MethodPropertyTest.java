package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.TypeDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class MethodPropertyTest {

 private MethodProperty property;
 private Object target;
 private PropertyName name;
 private SettableBeanProperty expected;

 @Before
 public void before() throws InstantiationException, IllegalAccessException {
 Class<?> clazz = ClassUtil.findClass("com.test.Target");
 target = clazz.newInstance();
 name = PropertyName.construct("testProperty");
 AnnotatedMethod method = AnnotatedMethod.construct(clazz.getMethod("setTestProperty", Object.class));
 BeanPropertyDefinition def = new BeanPropertyDefinition(name, null, null, null, null, null, method);
 TypeDeserializer typeDeserializer = null;
 JsonDeserializer<Object> valueDeserializer = null;
 Annotations contextAnnotations = Annotations.empty();
 property = new MethodProperty(def, null, typeDeserializer, contextAnnotations, method);
 expected = new SettableBeanProperty(def, null, valueDeserializer, null) {
 @Override
 public void set(Object instance, Object value) throws IOException {
 super.set(instance, value);
 }
 };
 }

 @Test
 public void testSettableBeanPropertyWithName() {
 SettableBeanProperty result = property.withName(name);
 assertEquals(expected.getClass(), result.getClass());
 assertEquals(name, result.getName());
 assertEquals(property._annotated, result._annotated);
 assertEquals(property._setter, result._setter);
 assertEquals(property._skipNulls, result._skipNulls);
 }

 @Test
 public void testSettableBeanPropertyWithValueDeserializer() throws IOException {
 JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>() {
 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return "test";
 }
 };
 SettableBeanProperty result = property.withValueDeserializer(deserializer);
 assertEquals(expected.getClass(), result.getClass());
 assertEquals(name, result.getName());
 assertEquals(property._annotated, result._annotated);
assertEquals(property._setter, result._setter);
 assertEquals(property._skipNulls, result._skipNulls);
 assertEquals(deserializer, result._valueDeserializer);
 assertEquals(property._nullProvider, result._nullProvider);
 }

 @Test
 public void testSettableBeanPropertyWithNullProvider() throws IOException {
 NullValueProvider nva = new NullValueProvider() {
 @Override
 public Object getNullValue(DeserializationContext ctxt) throws IOException {
 return null;
 }
 };
 SettableBeanProperty result = property.withNullProvider(nva);
 assertEquals(expected.getClass(), result.getClass());
 assertEquals(name, result.getName());
 assertEquals(property._annotated, result._annotated);
 assertEquals(property._setter, result._setter);
 assertEquals(property._skipNulls, result._skipNulls);
 assertEquals(property._valueDeserializer, result._valueDeserializer);
 assertEquals(nva, result._nullProvider);
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 JsonParser parser = new JsonFactory().createParser("{\"testProperty\": null}");
 parser.nextToken();
 property.deserializeAndSet(parser, new DeserializationContext() {}, target);
 assertTrue(target instanceof Target);
 assertEquals(null, property.getBean().get(name));
 }

 @Test
 public void testDeserializeAndSetNull() throws IOException {
 JsonParser parser = new JsonFactory().createParser("{\"testProperty\": null}");
 property.deserializeAndSet(parser, new DeserializationContext() {}, target);
 parser = new JsonFactory().createParser("{\"testProperty\": null}");
 parser.nextToken();
 property.deserializeAndSet(parser, new DeserializationContext() {}, target);
 assertNull(property.getBean().get(name));
 }

 @Test
 public void testDeserializeAndSetNotNull() throws IOException {
 JsonParser parser = new JsonFactory().createParser("{\"testProperty\": \"test\"}");
 property.deserializeAndSet(parser, new DeserializationContext() {}, target);
 assertEquals("test", property.getBean().get(name));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 JsonParser parser = new JsonFactory().createParser("{\"testProperty\": null}");
 assertEquals(target, property.deserializeSetAndReturn(parser, new DeserializationContext() {}, target));
 parser = new JsonFactory().createParser("{\"testProperty\": null}");
 parser.nextToken();
 assertEquals(target, property.deserializeSetAndReturn(parser, new DeserializationContext() {}, target));
 assertNull(property.getBean().get(name));

 parser = new JsonFactory().createParser("{\"testProperty\": \"test\"}");
 Object result = property.deserializeSetAndReturn(parser, new DeserializationContext() {}, target);
 assertEquals(target, result);
 assertEquals("test", property.getBean().get(name));
 }

 @Test
 public void testSet() throws IOException, InvocationTargetException, IllegalAccessException {
 property.set(target, "test");
 assertEquals("test", property.getBean().get(name));
 }

 @Test
 public void testSetNull() throws IOException, InvocationTargetException, IllegalAccessException {
 property.set(target, null);
 assertNull(property.getBean().get(name));
 }

 @Test
 public void testSetAndReturnNull() throws IOException, InvocationTargetException, IllegalAccessException {
 Object result = property.setAndReturn(target, null);
 assertEquals(target, result);
 assertNull(property.getBean().get(name));
 }

 @Test
 public void testSetAndReturn() throws IOException, InvocationTargetException, IllegalAccessException {
 Object result = property.setAndReturn(target, "test");
 assertEquals(target, result);
 assertEquals("test", property.getBean().get(name));
 }

 @Test
 public void testReadResolve() throws IOException {
 MethodProperty result = (MethodProperty) property.readResolve();
 assertEquals(property._annotated, result._annotated);
 assertEquals(property._setter, result._setter);
 assertEquals(property._skipNulls, result._skipNulls);
 }
}

class Target {
 private Object testProperty;
 // getter and setter methods here
}