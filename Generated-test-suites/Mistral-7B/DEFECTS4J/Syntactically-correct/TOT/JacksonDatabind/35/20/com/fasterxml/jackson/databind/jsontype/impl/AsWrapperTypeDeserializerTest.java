package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AsWrapperTypeDeserializerTest {

 private ObjectMapper mapper;
 private AsWrapperTypeDeserializer deserializer;

 @Before
 public void setUp() throws Exception {
 mapper = new ObjectMapper();
 deserializer = mapper.getTypeFactory().constructType(AsWrapperTypeDeserializer.class)
 .<AsWrapperTypeDeserializer>getInstance(Object.class, mapper.getTypeIdResolver(), "_typeProperty", true, String.class);
 }

 @Test
 public void testDeserializeValidWrapperObject() throws Exception {
 ObjectNode node = mapper.createObjectNode();
 node.put("typeId", "com.test.Person");
 node.put("person", mapper.valueToTree(new Person("John Doe", 30)));
 JsonParser jp = mapper.parser().readValue(node.toString());
 assertEquals(Person.class, deserializer.deserializeTypedFromObject(jp, mapper.getConfiguration()).getClass());
 }

 @Test
 public void testDeserializeMissingTypeId() throws Exception {
 JsonParser jp = mapper.parser().readValue("{}");
 try {
 deserializer.deserializeTypedFromObject(jp, mapper.getConfiguration());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 assertEquals("Need JSON Object to contain As.WRAPPER_OBJECT type information for class java.lang.Object", e.getMessage());
 }
 }

 @Test
 public void testDeserializeMissingTypeIdProperty() throws Exception {
 ObjectNode node = mapper.createObjectNode();
 node.put("person", mapper.valueToTree(new Person("John Doe", 30)));
 JsonParser jp = mapper.parser().readValue(node.toString());
 try {
 deserializer.deserializeTypedFromObject(jp, mapper.getConfiguration());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 assertEquals("Need JSON Object to contain As.WRAPPER_OBJECT type information for class java.lang.Object", e.getMessage());
 }
 }

 @Test
 public void testDeserializeMissingValue() throws Exception {
 ObjectNode node = mapper.createObjectNode();
 node.put("typeId", "com.test.Person");
 JsonParser jp = mapper.parser().readValue(node.toString());
 try {
 deserializer.deserializeTypedFromObject(jp, mapper.getConfiguration());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 assertEquals("expected closing END_OBJECT after type information and deserialized value", e.getMessage());
 }
 }

 @Test
 public void testDeserializeInvalidTypeId() throws Exception {
 ObjectNode node = mapper.createObjectNode();
 node.put("typeId", "com.test.InvalidType");
 JsonParser jp = mapper.parser().readValue(node.toString());
 try {
 deserializer.deserializeTypedFromObject(jp, mapper.getConfiguration());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 assertTrue(e.getCause() instanceof ClassNotFoundException);
 }
 }

 @Test
 public void testDeserializeEmptyTypeId() throws Exception {
 ObjectNode node = mapper.createObjectNode();
 node.put("typeId", "");
 JsonParser jp = mapper.parser().readValue(node.toString());
 try {
 deserializer.deserializeTypedFromObject(jp, mapper.getConfiguration());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 assertEquals("Need JSON Object to contain As.WRAPPER_OBJECT type information for class java.lang.Object", e.getMessage());
 }
 }

 @Test
 public void testDeserializeNullTypeId() throws Exception {
 ObjectNode node = mapper.createObjectNode();
 node.putNull("typeId");
 JsonParser jp = mapper.parser().readValue(node.toString());
 try {
 deserializer.deserializeTypedFromObject(jp, mapper.getConfiguration());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 assertEquals("Need JSON Object to contain As.WRAPPER_OBJECT type information for class java.lang.Object", e.getMessage());
 }
 }
}