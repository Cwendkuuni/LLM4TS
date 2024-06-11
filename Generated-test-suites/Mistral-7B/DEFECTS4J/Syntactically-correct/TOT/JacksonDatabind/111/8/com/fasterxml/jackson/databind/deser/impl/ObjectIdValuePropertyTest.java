package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectMapper objectMapper;
 private SettableBeanProperty property;
 private DeserializationContext context;

 @Before
 public void setup() throws Exception {
 objectIdReader = mock(ObjectIdReader.class);
 ObjectIdValueProperty propertyDefinition = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 property = new ObjectIdValueProperty(propertyDefinition, null);
 objectMapper = new ObjectMapper();
 context = mock(DeserializationContext.class);
 }

 @Test
 public void testDeserializeAndSetNull() throws IOException {
 when(objectIdReader.getIdType()).thenReturn(String.class);
 when(objectIdReader.getDeserializer()).thenReturn(new FromStringDeserializer<>(String.class));

 Object instance = new Object();
 property.deserializeAndSet(mock(JsonParser.class), context, instance);
 assertNull(property.get(instance));
 }

 @Test
 public void testDeserializeAndSetString() throws IOException, JsonProcessingException {
 when(objectIdReader.getIdType()).thenReturn(String.class);
 when(objectIdReader.getDeserializer()).thenReturn(new FromStringDeserializer<>(String.class));
 when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

 String id = "12345";
 Object instance = new Object();
 JsonNode node = objectMapper.readTree("{\"id\":\"" + id + "\"}");
 property.deserializeAndSet(mock(JsonParser.class), context, instance);
 assertEquals(id, property.get(instance));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetNull() throws IOException {
 Object instance = new Object();
 property.set(instance, null);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetUnsupportedType() throws IOException {
 Object instance = new Object();
 property.set(instance, new Object());
 }

 @Test
 public void testSetString() throws IOException {
 Object instance = new Object();
 property.set(instance, "12345");
 SettableBeanProperty idProperty = mock(SettableBeanProperty.class);
 doReturn(idProperty).when(objectIdReader).idProperty;
 assertEquals("12345", idProperty.get(instance));
 }
}