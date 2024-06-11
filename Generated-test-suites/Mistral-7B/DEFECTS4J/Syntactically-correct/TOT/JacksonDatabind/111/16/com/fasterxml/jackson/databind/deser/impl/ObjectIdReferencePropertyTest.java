package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdReferencePropertyTest {

 private ObjectMapper mapper;
 private JsonDeserializer<String> stringDeserializer;
 private ObjectIdInfo objectIdInfo;
 private ObjectIdReferenceProperty objectIdReferenceProperty;

 @Before
 public void setup() throws JsonProcessingException {
 mapper = new ObjectMapper();
 stringDeserializer = new StandardDeserializer<>(String.class);
 objectIdInfo = mapper.findValueAnnotation(ObjectIdProperty.class, String.class);
 objectIdReferenceProperty = new ObjectIdReferenceProperty(new ObjectIdReferenceProperty(null, stringDeserializer), objectIdInfo);
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object pojo = new Object();
 Object value = "testValue";
 Object result = objectIdReferenceProperty.setAndReturn(pojo, value);
 assertEquals(value, result);
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object pojo = new Object();
 ObjectNode node = mapper.createObjectNode();
 node.put("test", "testValue");
 Object result = objectIdReferenceProperty.deserializeAndSet(mapper.parse(node.toString()), mapper.getDeserializationContext(), pojo);
 assertEquals("testValue", pojo.getClass().getMethod("getTest").invoke(pojo));
 }

 @Test
 public void testDeserializeSetAndReturnWithUnresolvedForwardReference() throws IOException {
 Object pojo1 = new Object();
 Object pojo2 = new Object();
 ObjectNode node1 = mapper.createObjectNode();
 node1.put("test1", mapper.valueToTree("$ref: #/properties/test2"));
 ObjectNode node2 = mapper.createObjectNode();
 node2.put("test2", "testValue");
 Object result = objectIdReferenceProperty.deserializeAndSet(mapper.parse(node1.toString()), mapper.getDeserializationContext(), pojo1);
 assertEquals(null, result);
 Object result2 = objectIdReferenceProperty.deserializeAndSet(mapper.parse(node2.toString()), mapper.getDeserializationContext(), pojo2);
 assertNotNull(result2);
 Object value = node2.get("test2").asText();
 Object testValue = pojo2.getClass().getMethod("getTest").invoke(pojo2);
 assertEquals(value, testValue);
 }

 @Test
 public void testDeserializeSetAndReturnWithNullValue() throws IOException {
 Object pojo = new Object();
 ObjectNode node = mapper.createObjectNode();
 node.putNull("test");
 Object result = objectIdReferenceProperty.deserializeAndSet(mapper.parse(node.toString()), mapper.getDeserializationContext(), pojo);
 assertNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeSetAndReturnWithInvalidJson() throws IOException {
 Object pojo = new Object();
 ObjectNode node = mapper.createObjectNode();
 node.put("test", "invalidJson");
 Object result = objectIdReferenceProperty.deserializeAndSet(mapper.parse(node.toString()), mapper.getDeserializationContext(), pojo);
 fail("Expected JsonMappingException but no exception was thrown");
 }

 @Test
 public void testFixAccess() {
 Object pojo = new Object();
 objectIdReferenceProperty.fixAccess(mapper.getDeserializationConfig());
 assertNotNull(objectIdReferenceProperty.getMember());
 }

 @Test
 public void testGetAnnotation() {
 AnnotatedMember annotatedMember = objectIdReferenceProperty.getMember();
 assertEquals(ObjectIdProperty.class, annotatedMember.getAnnotation(ObjectIdProperty.class));
 }

 @Test
 public void testGetCreatorIndex() {
 assertEquals(-1, objectIdReferenceProperty.getCreatorIndex());
 }
}