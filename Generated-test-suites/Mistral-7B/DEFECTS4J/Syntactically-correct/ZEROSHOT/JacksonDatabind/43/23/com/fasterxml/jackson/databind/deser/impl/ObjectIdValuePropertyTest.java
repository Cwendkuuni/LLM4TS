package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.std.ObjectIdReaderForString;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeDescriptor;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.BeanUtil;

public class ObjectIdValuePropertyTest {

 private ObjectMapper mapper;
 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 mapper = new ObjectMapper();
 ObjectIdReaderForString idReader = new ObjectIdReaderForString();
 objectIdReader = new ObjectIdReader(idReader.getClass().getName(), idReader.getIdType(), null, null);
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
 }

 @Test
 public void testWithName() {
 PropertyName newName = PropertyName.construct("newName");
 ObjectIdValueProperty property = objectIdValueProperty.withName(newName);
 assertEquals(newName.getFullName(), property.getName().getFullName());
 }

 @Test
 public void testWithValueDeserializer() {
 ObjectDeserializer deserializer = mock(ObjectDeserializer.class);
 ObjectIdValueProperty property = objectIdValueProperty.withValueDeserializer(deserializer);
 assertEquals(deserializer, property.getValueDeserializer());
 }

 @Test
 public void testGetAnnotation() {
 fail("Should not be called as this property does not have any annotation.");
 }

 @Test
 public void testGetMember() {
 fail("Should not be called as this property does not have any member.");
 }

 @Test
 public void testDeserializeAndSet() throws JsonProcessingException {
 Object id = mapper.readTree("123").get(0).asText();
 DeserializationContext ctxt = mock(DeserializationContext.class);
 Object instance = mapper.readValue("{}", Object.class);
 objectIdValueProperty.deserializeAndSet(mapper.parserForTesting(), ctxt, instance);
 ObjectIdReader idReader = (ObjectIdReader) objectIdValueProperty._objectIdReader;
 Object idProperty = idReader.idProperty;
 assertEquals("123", idProperty.get(instance).asText());
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object id = mapper.readTree("123").get(0).asText();
 DeserializationContext ctxt = mock(DeserializationContext.class);
 Object instance = mapper.readValue("{}", Object.class);
 Object result = objectIdValueProperty.deserializeSetAndReturn(mapper.parserForTesting(), ctxt, instance);
 ObjectIdReader idReader = (ObjectIdReader) objectIdValueProperty._objectIdReader;
 Object idProperty = idReader.idProperty;
 assertEquals("123", idProperty.get(instance).asText());
 assertEquals(instance, result);
 }

 @Test
 public void testSet() throws IOException {
 Object id = mapper.readTree("123").get(0).asText();
 Object instance = mapper.readValue("{}", Object.class);
 objectIdValueProperty.set(instance, id);
 ObjectIdReader idReader = (ObjectIdReader) objectIdValueProperty._objectIdReader;
 Object idProperty = idReader.idProperty;
 assertEquals("123", idProperty.get(instance).asText());
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object id = mapper.readTree("123").get(0).asText();
 Object instance = mapper.readValue("{}", Object.class);
 Object result = objectIdValueProperty.setAndReturn(instance, id);
 ObjectIdReader idReader = (ObjectIdReader) objectIdValueProperty._objectIdReader;
 Object idProperty = idReader.idProperty;
 assertEquals("123", idProperty.get(instance).asText());
 assertEquals(instance, result);
 }

 @Test
 public void testWithNameAndValueDeserializer() {
 PropertyName newName = PropertyName.construct("newName");
 ObjectDeserializer deserializer = mock(ObjectDeserializer.class);
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, newName, null, null, deserializer);
 assertEquals(newName.getFullName(), property.getName().getFullName());
 assertEquals(deserializer, property.getValueDeserializer());
 }

 @Test
 public void testWithNameAndIdProperty() {
 PropertyName newName = PropertyName.construct("newName");
 SettableBeanProperty idProperty = mock(SettableBeanProperty.class);
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, newName, null, null, null, idProperty);
 assertEquals(newName.getFullName(), property.getName().getFullName());
 assertEquals(idProperty, property.getIdProperty());
 }

 @Test
 public void testWithNameAndIdReader() {
 PropertyName newName = PropertyName.construct("newName");
 ObjectIdReader idReader = mock(ObjectIdReader.class);
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, newName, null, null, null, null, idReader);
 assertEquals(newName.getFullName(), property.getName().getFullName());
 assertEquals(idReader, property.getObjectIdReader());
 }
}