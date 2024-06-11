package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.NullValueProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ObjectIdValuePropertyTest {

 private ObjectMapper objectMapper;
 private ObjectIdReader objectIdReader;
 private BeanPropertyDefinition propertyDefinition;
 private ObjectIdValueProperty property;
 private JsonDeserializer<String> valueDeserializer;
 private NullValueProvider nullValueProvider;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 objectIdReader = mock(ObjectIdReader.class);
 propertyDefinition = mock(BeanPropertyDefinition.class);
 when(propertyDefinition.getName()).thenReturn("id");
 when(propertyDefinition.getBeanProperty()).thenReturn(mock(BeanProperty.class));
 valueDeserializer = mock(JsonDeserializer.class);
 when(valueDeserializer.getClass()).thenReturn(String.class);
 when(objectIdReader.getIdProperty()).thenReturn(mock(BeanProperty.class));
 nullValueProvider = mock(NullValueProvider.class);
 property = new ObjectIdValueProperty(objectIdReader, propertyDefinition, valueDeserializer, nullValueProvider);
 }

 @Test
 public void testWithName() {
 BeanProperty newProperty = mock(BeanProperty.class);
 property = property.withName(newProperty);
 assertEquals(newProperty, property.getMember());
 }

 @Test
 public void testWithValueDeserializer() throws Exception {
 JsonDeserializer<Integer> newDeserializer = mock(JsonDeserializer.class);
 when(newDeserializer.getClass()).thenReturn(Integer.class);
 property = property.withValueDeserializer(newDeserializer);
 assertEquals(newDeserializer, property.getValueDeserializer());
 }

 @Test
 public void testWithNullProvider() throws Exception {
 NullValueProvider newNullProvider = mock(NullValueProvider.class);
 property = property.withNullProvider(newNullProvider);
 assertEquals(newNullProvider, property.getNullProvider());
 }

 @Test
 public void testGetAnnotation() {
 assertEquals(null, property.getAnnotation(String.class));
 }

 @Test
 public void testGetMember() {
 assertNull(property.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 Object testInstance = new Object();
 String id = "testId";
 when(objectIdReader.generator.fromString(id)).thenReturn(new ObjectIdentifier("testId"));
 when(valueDeserializer.deserialize(mock(JsonParser.class), mock(DeserializationContext.class))).thenReturn(id);
 property.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), testInstance);
 ObjectIdentifier actualId = ((ObjectIdentifier) testInstance.getClass().getDeclaredField("id").get(testInstance));
 assertEquals(id, actualId.getId());
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 Object testInstance = new Object();
 String id = "testId";
 when(objectIdReader.generator.fromString(id)).thenReturn(new ObjectIdentifier("testId"));
 when(valueDeserializer.deserialize(mock(JsonParser.class), mock(DeserializationContext.class))).thenReturn(id);
 when(property.getIdProperty().setAndReturn(testInstance, id)).thenReturn(testInstance);
 Object actualObject = property.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), testInstance);
 assertEquals(testInstance, actualObject);
 }

 @Test
 public void testSet() throws Exception {
 Object testInstance = new Object();
 String id = "testId";
 property.set(testInstance, id);
 ObjectIdentifier actualId = ((ObjectIdentifier) testInstance.getClass().getDeclaredField("id").get(testInstance));
 assertEquals(id, actualId.getId());
 }

 @Test
 public void testSetAndReturn() throws Exception {
 Object testInstance = new Object();
 String id = "testId";
 when(property.getIdProperty().setAndReturn(testInstance, id)).thenReturn(testInstance);
 Object actualObject = property.setAndReturn(testInstance, id);
 assertEquals(testInstance, actualObject);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithoutIdProperty() throws Exception {
 Object testInstance = new Object();
 property.set(testInstance, "testId");
 }
}