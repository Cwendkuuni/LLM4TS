package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;
 private JsonDeserializer<Object> jsonDeserializer;

 @Before
 public void setUp() {
 objectIdReader = Mockito.mock(ObjectIdReader.class);
 jsonDeserializer = new StdSerializer<Object>(Object.class) {};
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), jsonDeserializer);
 }

 @Test
 public void testGetAnnotation() {
 assertNull(objectIdValueProperty.getAnnotation(JsonId.class));
 }

 @Test
 public void testGetMember() {
 assertNull(objectIdValueProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object id = Mockito.mock(Object.class);
 JsonParser jsonParser = Mockito.mock(JsonParser.class);
 DeserializationContext desContext = Mockito.mock(DeserializationContext.class);
 Object instance = Mockito.mock(Object.class);
 Object result = objectIdValueProperty.deserializeAndSet(jsonParser, desContext, instance);
 assertEquals(instance, result);
 Mockito.verify(desContext).findObjectId(id, Mockito.any(), Mockito.any());
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object id = Mockito.mock(Object.class);
 JsonParser jsonParser = Mockito.mock(JsonParser.class);
 DeserializationContext desContext = Mockito.mock(DeserializationContext.class);
 Object instance = Mockito.mock(Object.class);
 Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, desContext, instance);
 assertEquals(instance, result);
 Mockito.verify(desContext).findObjectId(id, Mockito.any(), Mockito.any());
 }

 @Test
 public void testSet() throws IOException {
 Object id = Mockito.mock(Object.class);
 Object instance = Mockito.mock(Object.class);
 objectIdValueProperty.set(instance, id);
 // need to verify that setter method of idProperty is called
 ObjectIdValueProperty idProperty = Mockito.mock(ObjectIdValueProperty.class);
 SettableBeanProperty settableBeanProperty = Mockito.mock(SettableBeanProperty.class);
 Mockito.when(idProperty.setAndReturn(instance, id)).thenReturn(instance);
 Mockito.when(objectIdValueProperty._objectIdReader.idProperty).thenReturn(idProperty);
 objectIdValueProperty.set(instance, id);
 Mockito.verify(idProperty).setAndReturn(instance, id);
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object id = Mockito.mock(Object.class);
 Object instance = Mockito.mock(Object.class);
 Object result = objectIdValueProperty.setAndReturn(instance, id);
 assertEquals(instance, result);
 }
}