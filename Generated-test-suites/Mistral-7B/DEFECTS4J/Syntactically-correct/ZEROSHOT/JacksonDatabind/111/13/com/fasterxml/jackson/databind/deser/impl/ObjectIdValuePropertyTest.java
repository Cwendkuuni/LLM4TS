package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ObjectIdValuePropertyTest {

 private ObjectMapper objectMapper;
 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;

 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 objectIdReader = new ObjectIdReader() {
 @Override
 public PropertyName propertyName() {
 return new PropertyName("id");
 }

 @Override
 public Class<?> idType() {
 return Long.class;
 }

 @Override
 public JsonDeserializer<Object> idDeserializer() {
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
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
 }

 @Test
 public void testGetAnnotation() throws Exception {
 Annotation annotation = objectIdValueProperty.getAnnotation(JsonId.class);
 assertNull(annotation);
 }

 @Test
 public void testGetMember() {
 assertNull(objectIdValueProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object id = 1L;
 JsonParser jsonParser = objectMapper.getFactory()
 .createParser("{\"id\": " + id + "}");
 DeserializationContext context = objectMapper.getDeserializationContext();
 Object instance = new Object();
 objectIdValueProperty.deserializeAndSet(jsonParser, context, instance);
 assertEquals(id, objectIdValueProperty.getValue(instance));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException, JsonProcessingException {
 Object id = 1L;
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"id\": " + id + "}");
 DeserializationContext context = objectMapper.getDeserializationContext();
 Object instance = new Object();
 Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, context, instance);
 assertEquals(id, result);
 }

 @Test
 public void testSet() throws IOException, JsonProcessingException {
 Object id = 1L;
 Object instance = new Object();
 objectIdValueProperty.set(instance, id);
 assertEquals(id, objectIdValueProperty.getValue(instance));
 }

 @Test
 public void testSetAndReturn() throws IOException, JsonProcessingException {
 Object id = 1L;
 Object instance = new Object();
 Object result = objectIdValueProperty.setAndReturn(instance, id);
 assertEquals(id, result);
 }

 @Test
 public void testWithName() {
 ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty
 .withName(new PropertyName("newId"));
 assertEquals("newId", newObjectIdValueProperty.getName().getSimpleName());
 }

 @Test
 public void testWithValueDeserializer() {
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty
 .withValueDeserializer(typeDeserializer);
 assertEquals(typeDeserializer, newObjectIdValueProperty.getValueDeserializer());
 }

 @Test
 public void testWithNullProvider() {
 NullValueProvider nullValueProvider = mock(NullValueProvider.class);
 ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty
 .withNullProvider(nullValueProvider);
 assertEquals(nullValueProvider, newObjectIdValueProperty.getNullProvider());
 }
}