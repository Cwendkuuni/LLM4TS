package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for ObjectIdValueProperty.
 */
public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty property;

 @Before
 public void setUp() {
 ObjectIdReader.Builder objectIdReaderBuilder = ObjectIdReader.stdBuilder("id");
 objectIdReader = objectIdReaderBuilder.build(new JsonFactory(), String.class);
 property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 }

 @Test
 public void testInstancesAreEqual() {
 ObjectIdValueProperty property1 = new ObjectIdValueProperty(property, new JsonDeserializer<>(), null);
 ObjectIdValueProperty property2 = property1;
 assertTrue(property.equals(property1));
 assertTrue(property.equals(property2));
 }

 @Test
 public void testInstancesAreNotEqualDifferentProperties() {
 ObjectIdValueProperty property1 = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 ObjectIdValueProperty property2 = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 property2 = new ObjectIdValueProperty(property2, new JsonDeserializer<>(), null);
 assertFalse(property.equals(property2));
 }

 @Test
 public void testGetAnnotation() {
 assertNull(property.getAnnotation(Object.class));
 }

 @Test
 public void testGetMember() {
 assertNull(property.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object id = "123";
 JsonParser parser = new JsonFactory().createParser(id);
 Object instance = new Object();
 property.deserializeAndSet(parser, new DeserializationContext(), instance);
 assertEquals(id, property.getValue(instance));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object id = "123";
 JsonParser parser = new JsonFactory().createParser(id);
 Object instance = new Object();
 Object result = property.deserializeSetAndReturn(parser, new DeserializationContext(), instance);
 assertEquals(id, result);
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object id = "123";
 Object instance = new Object();
 property.setAndReturn(instance, id);
 assertEquals(id, property.getValue(instance));
 }

 @Test
 public void testWithName() {
 PropertyName newName = new PropertyName("newName");
 SettableBeanProperty propertyWithNewName = property.withName(newName);
 assertEquals(newName, propertyWithNewName.getName());
 }

 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer<?> deserializer = new StandardDeserializer<String>(String.class);
 SettableBeanProperty propertyWithDeserializer = property.withValueDeserializer(deserializer);
 assertEquals(deserializer, propertyWithDeserializer.getValueDeserializer());
 }

 @Test
 public void testWithNullProvider() {
 NullValueProvider nva = new AlwaysNullProvider();
 SettableBeanProperty propertyWithNullProvider = property.withNullProvider(nva);
 assertEquals(nva, propertyWithNullProvider.getNullProvider());
 }

 @Test
 public void testDeserializeValueWithNullProvider() throws IOException {
 Object instance = new Object();
 JsonParser parser = new JsonFactory().createParser("null");
 propertyWithNullProvider.deserializeAndSet(parser, new DeserializationContext(), instance);
 assertNull(propertyWithNullProvider.getValue(instance));
 }

 @Test
 public void testDeserializeValueWithDeserializer() throws IOException {
 TypeDeserializer typeDeserializer = new StandardTypeDeserializer();
 Object id = "123";
 JsonParser parser = new JsonFactory().createParser(id);
 propertyWithDeserializer.setValueDeserializer(typeDeserializer);
 Object instance = new Object();propertyWithDeserializer.deserializeAndSet(parser, new DeserializationContext(), instance);
 assertEquals(Integer.class, typeDeserializer.handledType());
 assertEquals(123, propertyWithDeserializer.getValue(instance).intValue());
 }

}

class AlwaysNullProvider implements NullValueProvider {
 @Override
 public Object getNullValue(DeserializationContext context) {
 return null;
 }
}