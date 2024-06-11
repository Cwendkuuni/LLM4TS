package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

 private ObjectMapper mapper;
 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty property;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 objectIdReader = new SimpleObjectIdReader("idProperty");
 property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STANDARD_PROPERTY_METADATA);
 }

 @Test
 public void testDeserializeAndSetNullValue() throws Exception {
 Object id = null;
 Object testInstance = mapper.readValue(ObjectNode.class.getDeclaredConstructor().newInstance(), TestClass.class);
 property.deserializeAndSet(mapper.getFactory().createParser("null"), mapper.getDeserializationContext(), testInstance);
 assertNull(testInstance.getId());
 }

 @Test
 public void testDeserializeAndSetValidId() throws Exception {
 ObjectNode testData = mapper.createObjectNode();
 testData.put("id", "123");
 Object id = 123;
 TestClass testInstance = mapper.readValue(testData, TestClass.class);
 property.deserializeAndSet(mapper.getFactory().createParser(testData.toString()), mapper.getDeserializationContext(), testInstance);
 assertEquals(id, testInstance.getId());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetNullValue() throws Exception {
 TestClass testInstance = mapper.readValue(ObjectNode.class.getDeclaredConstructor().newInstance(), TestClass.class);
 property.set(testInstance, null);
 fail("Should not allow setting null value to ObjectIdValueProperty");
 }

 @Test
 public void testSetValidId() throws Exception {
 TestClass testInstance = mapper.readValue(ObjectNode.class.getDeclaredConstructor().newInstance(), TestClass.class);
 Object id = 123;
 property.setAndReturn(testInstance, id);
 assertEquals(id, testInstance.getId());
 }

 @Test
 public void testDeserializeAndSetInvalidIdType() throws Exception {
 ObjectParser parser = mapper.getFactory().createParser("{\"id\":[1,2,3]}");
 TestClass testInstance = mapper.readValue(ObjectNode.class.getDeclaredConstructor().newInstance(), TestClass.class);
 try {
 property.deserializeAndSet(parser, mapper.getDeserializationContext(), testInstance);
 fail("Should not allow deserializing non-numeric id value to ObjectIdValueProperty");
 } catch (JsonProcessingException e) {
 // expected
 }
 }

 @Test
 public void testGetAnnotation() {
 JsonId jsonIdAnnotation = property.getAnnotation(JsonId.class);
 assertNotNull("Missing JsonId annotation", jsonIdAnnotation);
 }

 @Test
 public void testGetMember() {
 AnnotatedMember member = property.getMember();
 assertNull("Property should not have member", member);
 }

 @Test
 public static class TestClass {
 @JsonId
 private Long id;

 public Long getId() {
 return id;
 }
 }
}