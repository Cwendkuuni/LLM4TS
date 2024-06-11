package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private JsonDeserializer<Object> valueDeserializer;
 private ObjectIdValueProperty objectIdValueProperty;
 private JsonDeserializationContext context;
 private Object id;
 private Object instance;

 @Before
 public void setUp() throws InstantiationException, IllegalAccessException, InvocationTargetException {
 objectIdReader = mock(ObjectIdReader.class);
 valueDeserializer = new StdSerializer<Object>(Object.class) {};
 context = mock(JsonDeserializationContext.class);
 id = new Object();
 instance = new Object();

 when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
 when(objectIdReader.getIdType()).thenReturn(String.class);
 when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
 when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
 when(objectIdReader.idProperty).thenReturn(new SettableBeanProperty("idProp", null, null, null));

 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 }

 @Test
 public void testWithName() {
 ObjectIdValueProperty result = objectIdValueProperty.withName(new PropertyName("newName"));
 assert(result != objectIdValueProperty);
 }

 @Test
 public void testWithValueDeserializer() {
 ObjectIdValueProperty result = objectIdValueProperty.withValueDeserializer(valueDeserializer);
 assert(result != objectIdValueProperty);
 }

 @Test
 public void testGetAnnotation() {
 assertNull(objectIdValueProperty.getAnnotation(Null.class));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetNull() throws IOException {
 objectIdValueProperty.set(instance, null);
 }

 @Test
 public void testSerializeAndSet() throws IOException {
 String json = "{\"id\":\"testId\"}";
 JsonParser parser = new JsonFactory().createParser(json);
 objectIdValueProperty.deserializeAndSet(parser, context, instance);
 ObjectId id = (ObjectId) context.findObjectId(id, objectIdReader.generator, objectIdReader.resolver);
 assert(id != null);
 assert(id.getValue().equals("testId"));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object id = context.findValueAsObject(new ObjectId(1L));
 Object result = objectIdValueProperty.deserializeSetAndReturn(mock(JsonParser.class), context, instance);
 assert(result.equals(instance));
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object id = context.findValueAsObject(new ObjectId(1L));
 Object result = objectIdValueProperty.setAndReturn(instance, id);
 assert(result.equals(instance));
 }
}