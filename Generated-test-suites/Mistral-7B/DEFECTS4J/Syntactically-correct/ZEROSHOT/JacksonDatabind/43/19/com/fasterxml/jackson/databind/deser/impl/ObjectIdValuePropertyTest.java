package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

public class ObjectIdValuePropertyTest {

 private ObjectIdValueProperty objectIdValueProperty;
 private ObjectIdReader objectIdReader;

 @Before
 public void setup() throws InstantiationException, IllegalAccessException, NoSuchFieldException {
 ObjectIdReader objectIdReader = new ObjectIdReader() {
 @Override
 public PropertyName propertyName() {
 return PropertyName.construct("id");
 }

 @Override
 public Class<?> idType() {
 return Long.class;
 }

 @Override
 public JsonDeserializer<?> idDeserializer() {
 return null;
 }

 @Override
 public ObjectIdGenerator idGenerator() {
 return null;
 }

 @Override
 public ObjectIdResolver idResolver() {
 return null;
 }

 @Override
 public SettableBeanProperty idProperty() {
 return null;
 }
 };
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(null, null, null, null, null, null, null));
 Field _objectIdReader = ObjectIdValueProperty.class.getDeclaredField("_objectIdReader");
 _objectIdReader.setAccessible(true);
 _objectIdReader.set(objectIdValueProperty, objectIdReader);
 }

 @Test
 public void testGetAnnotation() throws Exception {
 assertNull(objectIdValueProperty.getAnnotation(ObjectIdValueProperty.class));
 }

 @Test
 public void testGetMember() throws Exception {
 assertNull(objectIdValueProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object instance = new Object();
 JsonParser jsonParser = createMockParser();
 Object id = 1L;
 objectIdValueProperty.deserializeAndSet(jsonParser, new DeserializationContext() {}, instance);
 ObjectIdReader objectIdReader = (ObjectIdReader) _objectIdReader.get(objectIdValueProperty);
 ObjectIdReader spyObjectIdReader = createMock(ObjectIdReader.class);
 when(spyObjectIdReader.generator).thenReturn(objectIdReader.generator);
 when(spyObjectIdReader.resolver).thenReturn(objectIdReader.resolver);
 objectIdValueProperty = new ObjectIdValueProperty(spyObjectIdReader, new PropertyMetadata(null, null, null, null, null, null, null));
 Field _objectIdReader2 = ObjectIdValueProperty.class.getDeclaredField("_objectIdReader");
 _objectIdReader2.setAccessible(true);
 _objectIdReader2.set(objectIdValueProperty, spyObjectIdReader);
 Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, new DeserializationContext() {}, instance);
 assertEquals(id, result);
 verify(spyObjectIdReader).bindItem(instance);
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object instance = new Object();
 Object id = 1L;
 Object result = objectIdValueProperty.deserializeSetAndReturn(createMockParser(), new DeserializationContext() {}, instance, id);
 assertEquals(id, result);
 }

 @Test
 public void testSet() throws IOException {
 Object instance = new Object();
 Object id = 1L;
 objectIdValueProperty.set(instance, id);
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object instance = new Object();
 Object id = 1L;
 Object result = objectIdValueProperty.setAndReturn(instance, id);
 assertEquals(id, result);
 }

 @Test
 public void testWithName() {
 PropertyName newName = PropertyName.construct("newName");
 ObjectIdValueProperty result = objectIdValueProperty.withName(newName);
 assertEquals(newName, result.getName());
 }

 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer<?> deserializer = new StandardDeserializer<>(String.class);
 ObjectIdValueProperty result = objectIdValueProperty.withValueDeserializer(deserializer);
 assertEquals(deserializer, result.getValueDeserializer());
 }

 @Test
 public void testWithPropertyName() {
 PropertyName newName = PropertyName.construct("newName");
 ObjectIdValueProperty result = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(newName, null, null, null, null, null, null));
 assertEquals(newName, result.getName());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 ObjectIdValueProperty original = objectIdValueProperty;
 ObjectIdValueProperty cloned = (ObjectIdValueProperty) original.clone();
 assertEquals(original, cloned);
 }

 private JsonParser createMockParser() {
 return mock(JsonParser.class);
 }
}