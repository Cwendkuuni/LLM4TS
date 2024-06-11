package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind. node.ObjectIdNode;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.ObjectIdReader;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private PropertyName propertyName;
 private ObjectIdValueProperty objectIdValueProperty;

 @Before
 public void setUp() throws IOException {
 ObjectIdReader mockObjectIdReader = mock(ObjectIdReader.class);
 propertyName = PropertyName.construct("id");
 objectIdReader = new ObjectIdReader() {
 @Override
 public PropertyName propertyName() {
 return propertyName;
 }

 @Override
 public Class<?> idType() {
 return ObjectIdNode.class;
 }

 @Override
 public JsonDeserializer<ObjectIdNode> idDeserializer() {
 return mock(JsonDeserializer.class);
 }

 @Override
 public ObjectIdGenerator idGenerator() {
 return mock(ObjectIdGenerator.class);
 }

 @Override
 public ObjectIdResolver idResolver() {
 return mock(ObjectIdResolver.class);
 }

 @Override
 public SettableBeanProperty idProperty() {
 return mock(SettableBeanProperty.class);
 }
 };
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 }

 @Test
 public void testNullGetAnnotation() {
 assertNull(objectIdValueProperty.getAnnotation(Annotation.class));
 }

 @Test
 public void testGetMember() {
 assertNull(objectIdValueProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 ObjectIdNode idNode = new ObjectIdNode("123");
 JsonParser mockJsonParser = mock(JsonParser.class);
 when(mockJsonParser.getValueAsToken()).thenReturn(idNode.asToken());
 DeserializationContext mockContext = mock(DeserializationContext.class);
 Object instance = new Object();
 when(mockContext.findObjectId(any(Object.class), any(ObjectIdGenerator.class), any(ObjectIdResolver.class)))
 .thenReturn(new ReadableObjectId("123", "123", null));

 objectIdValueProperty.deserializeAndSet(mockJsonParser, mockContext, instance);

 Object id = _getSettableBeanProperty(instance).get(instance);
 assertEquals("123", id);
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 ObjectIdNode idNode = new ObjectIdNode("123");
 JsonParser mockJsonParser = mock(JsonParser.class);
 when(mockJsonParser.getValueAsToken()).thenReturn(idNode.asToken());
 DeserializationContext mockContext = mock(DeserializationContext.class);
 Object instance = new Object();
 when(mockContext.findObjectId(any(Object.class), any(ObjectIdGenerator.class), any(ObjectIdResolver.class)))
 .thenReturn(new ReadableObjectId("123", "123", null));

 Object result = objectIdValueProperty.deserializeSetAndReturn(mockJsonParser, mockContext, instance);
 assertEquals(instance, result);
 }

 @Test
 public void testSet() throws IOException {
 Object id = new Object();
 Object instance = new Object();
 objectIdValueProperty.set(instance, id);
 Object actualId = _getSettableBeanProperty(instance).get(instance);
 assertEquals(id, actualId);
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object id = new Object();
 Object instance = new Object();
 Object result = objectIdValueProperty.setAndReturn(instance, id);
 assertEquals(instance, result);
 }

 private SettableBeanProperty _getSettableBeanProperty(Object instance) {
 return ClassUtil.findProperty(instance.getClass(), propertyName.getSimpleName());
 }
}