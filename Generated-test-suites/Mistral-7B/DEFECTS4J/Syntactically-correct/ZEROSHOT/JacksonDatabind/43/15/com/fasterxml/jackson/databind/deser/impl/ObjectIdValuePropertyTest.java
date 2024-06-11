package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;
 private JsonDeserializer<Object> jsonDeserializer;
 private JsonDeserializer<Object> valueDeserializer;
 private StdSerializer<Object> idSerializer;

 @Before
 public void setup() throws Exception {
 ObjectIdReader reader = mock(ObjectIdReader.class);
 when(reader.generator).thenReturn(mock(ObjectIdGenerator.class));
 when(reader.propertyName).thenReturn(PropertyName.construct("id"));
 when(reader.getIdType()).thenReturn(TypeFactory.defaultInstance().constructType(Long.class));
 when(reader.resolver).thenReturn(mock(ObjectIdResolver.class));
 when(reader.idProperty).thenReturn(mock(SettableBeanProperty.class));

 jsonDeserializer = mock(JsonDeserializer.class);
 valueDeserializer = mock(JsonDeserializer.class);
 idSerializer = new StdSerializer<>(Long.class);
 when(reader.getDeserializer()).thenReturn(jsonDeserializer);
 when(jsonDeserializer.getIdProperty()).thenReturn(objectIdValueProperty);
 when(objectIdReader.setValueDeserializer(valueDeserializer)).thenReturn(objectIdReader);
 when(objectIdReader.getName(reader)).thenReturn(PropertyName.construct("objectId"));
 when(reader.getMember()).thenReturn(mock(AnnotatedMember.class));
 objectIdReader = new ObjectIdReader(reader);

 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
 }

 @Test
 public void testGettersAndSetters() {
 assertSame(objectIdReader.propertyName, objectIdValueProperty.getName());
 assertSame(objectIdReader.getIdType(), objectIdValueProperty.getType());
 assertNull(objectIdValueProperty.getAnnotation(Mockito.any(Annotation.class)));
 assertSame(objectIdReader, objectIdValueProperty.getMember().getAnnotated());
 assertSame(jsonDeserializer, objectIdValueProperty.getDeserializer());
 assertSame(objectIdReader, objectIdValueProperty.getParent());
 }

 @Test
 public void testWithName() throws JsonProcessingException {
 ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withName(PropertyName.construct("newName"));
 assertSame(newObjectIdValueProperty, new ObjectIdValueProperty(objectIdValueProperty, PropertyName.construct("newName")));
 }

 @Test
 public void testWithValueDeserializer() throws JsonProcessingException {
 ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withValueDeserializer(valueDeserializer);
 assertSame(newObjectIdValueProperty, new ObjectIdValueProperty(objectIdValueProperty, valueDeserializer));
 }

 @Test
 public void testGetValueDeserializer() throws Exception {
 assertSame(jsonDeserializer, objectIdValueProperty.getValueDeserializer());
 }

 @Test
 public void testSetAndReturn() throws Exception {
 Object instance = new Object();
 Long id = 1L;
 when(jsonDeserializer.deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class)))
 .thenReturn(id);
 when(objectIdReader.idProperty.setAndReturn(instance, id)).thenReturn(instance);
 Object result = objectIdValueProperty.setAndReturn(instance, id);
 assertSame(instance, result);
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 Object instance = new Object();
 String jsonString = "{\"id\":1}";
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsLong()).thenReturn(1L);
 when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getCurrentName()).thenReturn("id");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
 when(parser.getValueAsLong()).thenReturn(1L);
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);
 when(jsonDeserializer.deserialize(parser, Mockito.any(DeserializationContext.class))).thenReturn(1L);
 when(objectIdReader.idProperty.setAndReturn(instance, 1L)).thenReturn(instance);
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 objectIdValueProperty.deserializeAndSet(parser, Mockito.mock(DeserializationContext.class), instance);
 assertSame(instance, parser.getCurrentValueAs(Object.class));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWhenIdPropertyIsNull() throws Exception {
 Object instance = new Object();
 when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(parser.getCurrentName()).thenReturn("id");
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 objectIdValueProperty.set(instance, null);
 }
}