package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;

/**
 * Test class for {@link ObjectIdValueProperty}
 */
public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;

 @Before
 public void setup() {
 objectIdReader = Mockito.mock(ObjectIdReader.class);
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader,
 new PropertyMetadata(null, null, null, null, null, null));
 }

 @Test
 public void testGetAnnotation() {
 assertNull(objectIdValueProperty.getAnnotation(ObjectId.class));
 }

 @Test
 public void testGetMember() {
 assertNull(objectIdValueProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 ObjectId id = Mockito.mock(ObjectId.class);
 JsonParser jsonParser = Mockito.mock(JsonParser.class);
 DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
 Object instance = Mockito.mock(Object.class);

 when(objectIdReader.getIdType()).thenReturn(ObjectId.Class);
 when(objectIdReader.idProperty).thenReturn(Mockito.mock(SettableBeanProperty.class));

 Object result = objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
 assertEquals(instance, result);

 verify(instance, Mockito.times(1)).setId(id);
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 ObjectId id = Mockito.mock(ObjectId.class);
 JsonParser jsonParser = Mockito.mock(JsonParser.class);
 DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
 Object instance = Mockito.mock(Object.class);

 when(objectIdReader.getIdType()).thenReturn(ObjectId.Class);
 when(objectIdReader.idProperty).thenReturn(Mockito.mock(SettableBeanProperty.class));

 Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
 assertEquals(instance, result);

 verify(instance, Mockito.times(1)).setId(id);
 }

 @Test
 public void testSet() throws IOException {
 ObjectId id = Mockito.mock(ObjectId.class);
 Object instance = Mockito.mock(Object.class);

 when(objectIdReader.getIdType()).thenReturn(ObjectId.Class);
 when(objectIdReader.idProperty).thenReturn(Mockito.mock(SettableBeanProperty.class));

 objectIdValueProperty.set(instance, id);
 assertEquals(id, BeanUtil.findValueRequired(instance, objectIdValueProperty.getName()));
 }

 @Test
 public void testSetAndReturn() throws IOException {
 ObjectId id = Mockito.mock(ObjectId.class);
 Object instance = Mockito.mock(Object.class);

 when(objectIdReader.getIdType()).thenReturn(ObjectId.Class);
 when(objectIdReader.idProperty).thenReturn(Mockito.mock(SettableBeanProperty.class));

 Object result = objectIdValueProperty.setAndReturn(instance, id);
 assertEquals(instance, result);
 }

 @Test
 public void testWithName() {
 PropertyName newName = Mockito.mock(PropertyName.class);
 ObjectIdValueProperty objectIdValuePropertyNew = objectIdValueProperty.withName(newName);
 assertEquals(newName, objectIdValuePropertyNew.getName());
 }

 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer<?> deserializer = Mockito.mock(JsonDeserializer.class);
 ObjectIdValueProperty objectIdValuePropertyNew = objectIdValueProperty.withValueDeserializer(deserializer);
 assertEquals(deserializer, objectIdValuePropertyNew.getValueDeserializer());
 }

 @Test
 public void testClone() {
 ObjectIdValueProperty objectIdValuePropertyNew = objectIdValueProperty.clone();
 assertEquals(objectIdValueProperty, objectIdValuePropertyNew);
 }
}