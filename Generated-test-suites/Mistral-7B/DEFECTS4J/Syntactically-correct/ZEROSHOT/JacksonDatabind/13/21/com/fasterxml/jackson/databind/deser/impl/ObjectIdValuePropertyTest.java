package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;
 private JsonDeserializer<String> deserializer;
 private DeserializationContext context;
 private JsonParser parser;
 private Object id;
 private StdSerializer<Object> idSerializer;
 private Object instance;

 @Before
 public void setup() throws IOException {
 IdType idType = IdType.STRING;
 objectIdReader = mock(ObjectIdReader.class);
 when(objectIdReader.getIdType()).thenReturn(idType);
 when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
 when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
 when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
 when(objectIdReader.getIdProperty()).thenReturn(null);

 deserializer = new None();
 idSerializer = new StdSerializer<Object>(Object.class);
 context = mock(DeserializationContext.class);
 parser = mock(JsonParser.class);
 id = "123";
 instance = new Object();

 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader,
 new PropertyMetadata(new JsonSerializer.ValueType(ClassUtil.constructType(Object.class)), null));
 }

 @Test
 public void testGetAnnotation() throws IOException {
 assertNull(objectIdValueProperty.getAnnotation(JsonDeserialize.class));
 }

 @Test
 public void testGetMember() {
 assertNull(objectIdValueProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 when(parser.getValueAsString()).thenReturn(id);
 when(context.findObjectId(any(), any(), any())).thenReturn(mock(ReadableObjectId.class));

 objectIdValueProperty.deserializeAndSet(parser, context, instance);

 // check that id has been set
 Object idProp = objectIdReader.getIdProperty();
 if (idProp != null) {
 assertNotNull(idProp.get(instance));
 assertEqualTo(id, idProp.get(instance));
 }
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 when(parser.getValueAsString()).thenReturn(id);
 when(context.findObjectId(any(), any(), any())).thenReturn(mock(ReadableObjectId.class));

 Object result = objectIdValueProperty.deserializeSetAndReturn(parser, context, instance);

 assertEquals(instance, result);
 }

 @Test
 public void testSet() throws IOException {
 objectIdValueProperty.set(instance, id);

 Object idProp = objectIdReader.getIdProperty();
 if (idProp != null) {
 assertNotNull(idProp.get(instance));
 assertEqualTo(id, idProp.get(instance));
 }
 }

 @Test
 public void testSetAndReturn() throws IOException {
 when(parser.getValueAsString()).thenReturn(id);
 when(context.findObjectId(any(), any(), any())).thenReturn(mock(ReadableObjectId.class));
 when(idProp.setAndReturn(instance, any())).thenReturn(instance);

 Object result = objectIdValueProperty.setAndReturn(parser, context, instance);

 assertEquals(instance, result);
 }

 @Test
 public void testWithName() {
 PropertyName newName = new PropertyName("newName");
 ObjectIdValueProperty result = objectIdValueProperty.withName(newName);
 assertEquals(newName, result.getName());
 }

 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer<String> newDeserializer = new None();
 ObjectIdValueProperty result = objectIdValueProperty.withValueDeserializer(newDeserializer);
 assertEquals(newDeserializer, result.getValueDeserializer());
 }
}