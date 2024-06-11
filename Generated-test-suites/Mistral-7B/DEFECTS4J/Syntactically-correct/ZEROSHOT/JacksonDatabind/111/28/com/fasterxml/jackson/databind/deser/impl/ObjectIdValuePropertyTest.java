package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;

 @Before
 public void setUp() throws JsonProcessingException {
 objectIdReader = new ObjectIdReader() {
 @Override
 public String propertyName() {
 return "id";
 }

 @Override
 public Class<?> idType() {
 return Long.class;
 }

 @Override
 public ObjectIdGenerator generator() {
 return ObjectIdGenerators.IntGenerator.instance;
 }

 @Override
 public ObjectIdResolver resolver() {
 return new SimpleObjectIdResolver();
 }

 @Override
 public JsonDeserializer<Object> idDeserializer() {
 return new StdSerializer<Long>(Long.class) {
 @Override
 public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return p.getValueAsLong();
 }
 };
 }

 @Override
 public BeanPropertyDefinition idProperty() {
 return new BeanPropertyDefinition("idProperty", Long.class);
 }
 };
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
 }

 @Test
 public void testSetName() {
 ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withName(new PropertyName("name"));
 assertNotEquals(objectIdValueProperty, newObjectIdValueProperty);
 assertEquals("name", newObjectIdValueProperty.getName().asString());
 }

 @Test
 public void testSetValueDeserializer() throws JsonProcessingException {
 JsonDeserializer<String> stringDeserializer = new StdSerializer<String>(String.class) {
 @Override
 public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return p.getValueAsString();
 }
 };
 ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withValueDeserializer(stringDeserializer);
 assertNotEquals(objectIdValueProperty, newObjectIdValueProperty);
 assertEquals(stringDeserializer, newObjectIdValueProperty.getValueDeserializer());
 }

 @Test
 public void testSetNullProvider() throws JsonProcessingException {
 NullValueProvider nullValueProvider = new StandardNullValueProvider();
 ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withNullProvider(nullValueProvider);
 assertNotEquals(objectIdValueProperty, newObjectIdValueProperty);
 assertEquals(nullValueProvider, newObjectIdValueProperty.getNullProvider());
 }

 @Test
 public void testGetAnnotation() {
 assertNull(objectIdValueProperty.getAnnotation(ObjectIdGenerator.class));
 }

 @Test
 public void testGetMember() {
 assertNull(objectIdValueProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object testInstance = new Object();
 JsonParser jsonParser = new JsonFactory().createParser("{\"id\":1}");
 objectIdValueProperty.deserializeAndSet(jsonParser, new DeserializationContext(), testInstance);
 assertEquals(1, testInstance.hashCode());
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object testInstance = new Object();
 Object id = objectIdValueProperty.deserializeSetAndReturn(new JsonFactory().createParser("{\"id\":1}"),
 new DeserializationContext(), testInstance);
 assertEquals(1, id.hashCode());
 assertEquals(1, testInstance.hashCode());
 }

 @Test
 public void testSet() throws IOException {
 Object testInstance = new Object();
 objectIdValueProperty.set(testInstance, 1);
 assertEquals(1, testInstance.hashCode());
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object testInstance = new Object();
 Object id = objectIdValueProperty.setAndReturn(testInstance, 1);
 assertEquals(1, id.hashCode());
 assertEquals(1, testInstance.hashCode());
 }
}