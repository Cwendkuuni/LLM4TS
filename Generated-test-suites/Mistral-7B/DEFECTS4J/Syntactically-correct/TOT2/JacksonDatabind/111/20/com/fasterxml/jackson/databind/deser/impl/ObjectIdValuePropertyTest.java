package com.example.junit4;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNodeDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

 private ObjectMapper mapper;
 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 objectIdReader = new ObjectIdReader(ObjectIdType.STRING, null, null);
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STANDARD_REQUIRED);
 }

 // Test case for constructor
 @Test
 public void testConstructor() {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STANDARD_REQUIRED);
 assertNotNull(objectIdValueProperty);
 }

 // Test case for getName
 @Test
 public void testGetName() {
 assertEquals("propertyName", objectIdValueProperty.getName());
 }

 // Test case for getIdType
 @Test
 public void testGetIdType() {
 assertEquals(ObjectIdType.DEFAULT, objectIdValueProperty.getIdType());
 }

 // Test case for getMetadata
 @Test
 public void testGetMetadata() {
 assertNotNull(objectIdValueProperty.getMetadata());
 }

 // Test case for getValueDeserializer
 @Test
 public void testGetValueDeserializer() {
 JsonDeserializer<?> valueDeserializer = objectIdValueProperty.getValueDeserializer();
 assertNotNull(valueDeserializer);
 }

 // Test case for deserializeAndSet
 @Test
 public void testDeserializeAndSet() throws Exception {
 ObjectNode jsonNode = mapper.createObjectNode();
 jsonNode.put("id", "123");
 JsonParser parser = new JsonFactory().createParser(jsonNode.toString());
 Object instance = new Object();
 objectIdValueProperty.deserializeAndSet(parser, null, instance);
 assertEquals("123", instance.getClass().getDeclaredField("_id").get(instance));
 }

 // Test case for deserializeSetAndReturn
 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 ObjectNode jsonNode = mapper.createObjectNode();
 jsonNode.put("id", null);
 ObjectInstance instance = new ObjectInstance();
 Object result = objectIdValueProperty.deserializeSetAndReturn(new JsonFactory().createParser(jsonNode.toString()), null, instance);
 assertNull(result);
 }

 // Test case for set
 @Test
 public void testSet() throws Exception {
 ObjectInstance instance = new ObjectInstance();
 objectIdValueProperty.set(instance, "123");
 assertEquals("123", instance.getClass().getDeclaredField("_id").get(instance));
 }

 // Test case for setAndReturn
 @Test
 public void testSetAndReturn() throws Exception {
 ObjectInstance instance = new ObjectInstance();
 Object result = objectIdValueProperty.setAndReturn(instance, "123");
 assertEquals(instance, result);
 }

 // Test case for set with null valueDeserializer
 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithNullValueDeserializer() throws Exception {
 ObjectInstance instance = new ObjectInstance();
 objectIdValueProperty.setValueDeserializer(null);
 objectIdValueProperty.set(instance, null);
 }

 // Test case for getAnnotation
 @Test
 public void testGetAnnotation() {
 assertNull(objectIdValueProperty.getAnnotation(JsonDeserialize.class));
 }

 // Test case for getMember
 @Test
 public void testGetMember() {
 assertNull(objectIdValueProperty.getMember());
 }
}