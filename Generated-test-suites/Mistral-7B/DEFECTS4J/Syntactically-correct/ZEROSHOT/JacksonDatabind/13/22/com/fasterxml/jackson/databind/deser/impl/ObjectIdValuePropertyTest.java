package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 objectIdReader = new SimpleObjectIdReader(new PropertyName("id"));
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
 mapper = new ObjectMapper();
 }

 @After
 public void teardown() {
 objectIdReader = null;
 mapper = null;
 }

 @Test
 public void testObjectIdValueProperty() {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
 assertEquals("id", property.getName().asString());
 assertNull(property.getValueDeserializer());
 assertNull(property.getMetadata());
 assertNull(property.getAnnotations());
 assertNotNull(property._objectIdReader);
 }

 @Test
 public void testConstructWithValueDeserializer() throws Exception {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null, new StdScalarValueDeserializer(JsonType.INT_TYPE));
 assertEquals("id", property.getName().asString());
 assertNotNull(property.getValueDeserializer());
 assertNull(property.getMetadata());
 assertNull(property.getAnnotations());
 assertNotNull(property._objectIdReader);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithNameNull() throws Exception {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
 try {
 property = property.withName(null);
 } finally {
 fail("Expected UnsupportedOperationException when calling withName(null)");
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithNameEmptyString() throws Exception {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
 try {
 property = property.withName("");
 } finally {
 fail("Expected UnsupportedOperationException when calling withName(\"\")");
 }
 }

 @Test
 public void testWithName() throws Exception {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
 PropertyName newName = new PropertyName("newName");
 ObjectIdValueProperty propertyWithNewName = property.withName(newName);
 assertEquals("newName", propertyWithNewName.getName().asString());
 assertEquals(property, propertyWithNewName.getBackReference());
 }

 @Test
 public void testGetAnnotation() throws Exception {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
 assertNull(property.getAnnotation(ObjectIdValue.class));
 }

 @Test
 public void testGetMember() throws Exception {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
 assertNull(property.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
 Object id = mapper.readValue(ObjectFactory.instance.createObjectNode().setTextField("id", "1"), JsonType.INT_TYPE);
 JsonParser jp = mapper.getFactory().createParser(ObjectFactory.instance.createObjectNode().setTextField("id", "1").getRawValue());
 property.deserializeAndSet(jp, mapper.getDeserializationContext(), new Object());
 assertEquals(1, mapper.readValue(mapper.writeValueAsString(new Object()), JsonType.INT_TYPE));
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
 Object id = mapper.readValue(ObjectFactory.instance.createObjectNode().setTextField("id", "1"), JsonType.INT_TYPE);
 JsonParser jp = mapper.getFactory().createParser(ObjectFactory.instance.createObjectNode().setTextField("id", "1").getRawValue());
 Object result = property.deserializeSetAndReturn(jp, mapper.getDeserializationContext(), new Object());
 assertEquals(1, result.getClass().getField("id").get(result));
 }

 @Test
 public void testSetAndReturn() throws Exception {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
 Object id = mapper.readValue(ObjectFactory.instance.createObjectNode().setTextField("id", "1"), JsonType.INT_TYPE);
 Object instance = new Object() {{ setId(id); }};
 Object result = property.setAndReturn(instance, id);
 assertEquals(instance, result);
 assertEquals(id, instance.getClass().getField("id").get(instance));
 }

 @Test
 public void testSet() throws Exception {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
 Object id = mapper.readValue(ObjectFactory.instance.createObjectNode().setTextField("id", "1"), JsonType.INT_TYPE);
 Object instance = new Object() {{ setId(id); }};
 property.set(instance, id);
 assertEquals(id, instance.getClass().getField("id").get(instance));
 }

 @Test
 public void testWithValueDeserializer() throws Exception {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
 JsonDeserializer<Object> deserializer = new StdScalarValueDeserializer(JsonType.INT_TYPE);
 ObjectIdValueProperty propertyWithDeserializer = property.withValueDeserializer(deserializer);
 assertEquals(deserializer, propertyWithDeserializer.getValueDeserializer());
 }
}