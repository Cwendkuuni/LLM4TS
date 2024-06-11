package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.ThatAnnotation;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.BeanUtil;

/**
 * Test class for ObjectIdValueProperty.
 */
public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;
 private JsonDeserializer<String> stringDeserializer;

 @Before
 public void setUp() throws Exception {
 ObjectIdReader.ObjectIdGenerator objectIdGenerator = Mockito.mock(ObjectIdReader.ObjectIdGenerator.class);
 ObjectIdReader.ObjectIdResolver objectIdResolver = Mockito.mock(ObjectIdReader.ObjectIdResolver.class);
 ObjectIdReader idReader = new ObjectIdReader(ObjectIdReader.class, "id", objectIdGenerator, objectIdResolver);
 stringDeserializer = new StdSerializer<String>(String.class);
 objectIdValueProperty = new ObjectIdValueProperty(idReader, PropertyMetadata.empty());
 }

 @Test
 public void testObjectIdValueProperty() {
 assertEquals("id", objectIdValueProperty.getName());
 assertEquals(Object.class, objectIdValueProperty.getParameterType());
 assertNotNull(objectIdValueProperty.getDeserializer());
 assertNull(objectIdValueProperty.getAnnotation(JsonProperty.class));
 assertNull(objectIdValueProperty.getMember());
 }

 @Test
 public void testGetPropertyDefinition() throws Exception {
 BeanPropertyDefinition propertyDefinition = AnnotationIntrospector.findPropertyDefinition(
 ObjectIdValuePropertyTest.class.getDeclaredField("objectIdValueProperty"), new AnnotationIntrospector());
 assertNotNull(propertyDefinition);
 assertEquals("objectIdValueProperty", propertyDefinition.getName());
 assertNotNull(propertyDefinition.getGetter());
 assertEquals(ObjectIdValueProperty.class, propertyDefinition.getHandlerType());
 }

 @Test
 public void testSettableBeanProperty() throws Exception {
 Object testInstance = new Object();
 // test setter methods
 objectIdValueProperty.setValueDeserializer(stringDeserializer);
 objectIdValueProperty.set(testInstance, "testId");
 Object id = "testId";
 assertEquals(id, BeanUtil.findValueRequired(testInstance, objectIdValueProperty.getName()));
 // test getter methods
 Object idFromGet = objectIdValueProperty.get(testInstance);
 assertEquals(id, idFromGet);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetOnObjectIdValuePropertyWithNoIdProperty() throws Exception {
 Object testInstance = new Object();
 objectIdValueProperty.setAndReturn(testInstance, "testId");
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 Object testInstance = new Object();
 JsonParser parser = Mockito.mock(JsonParser.class);
 Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 Mockito.when(parser.getText()).thenReturn("testId");
 ObjectIdValueProperty deserializer = Mockito.spy(objectIdValueProperty);
 Object result = deserializer.deserializeAndSet(parser, Mockito.mock(DeserializationContext.class), testInstance);
 assertEquals(testInstance, result);
 Mockito.verify(parser, Mockito.times(1)).nextToken();
 Mockito.verify(parser, Mockito.times(1)).getText();
 Object id = "testId";
 assertEquals(id, BeanUtil.findValueRequired(testInstance, objectIdValueProperty.getName()));
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 Object testInstance = new Object();
 JsonParser parser = Mockito.mock(JsonParser.class);
 Mockito.when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
 Mockito.when(parser.hasToken(JsonToken.VALUE_STRING)).thenReturn(true);
 Mockito.when(parser.getText()).thenReturn("testId");
 ObjectIdValueProperty deserializer = Mockito.spy(objectIdValueProperty);
 Object result = deserializer.deserializeSetAndReturn(parser, Mockito.mock(DeserializationContext.class), testInstance);
 assertEquals(testInstance, result);
 Object id = "testId";
 assertEquals(id, BeanUtil.findValueRequired(testInstance, objectIdValueProperty.getName()));
 }
}