package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JsonTestUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AsWrapperTypeDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testDeserializeWrappedObjectWithValidTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 ObjectType.class, new StandardTypeIdResolver(), "type", false, String.class);
 ObjectNode jsonNode = JsonTestUtils.createEmptyObjectNode();
 jsonNode.put("type", "com.example.Person");
 Object person = objectMapper.readValue(jsonNode, deserializer.getType());
 assertEquals(Person.class, person.getClass());
 }

 @Test
 public void testDeserializeWrappedObjectWithInvalidTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 ObjectType.class, new StandardTypeIdResolver(), "type", false, String.class);
 ObjectNode jsonNode = JsonTestUtils.createEmptyObjectNode();
 jsonNode.put("type", "com.example.InvalidType");
 try {
 objectMapper.readValue(jsonNode, deserializer.getType());
 } catch (IOException e) {
 assertEquals("Unknown type id 'com.example.InvalidType' (note, it's defined in a separate class)"
 , e.getMessage());
 }
 }

 @Test
 public void testDeserializeWrappedObjectWithNullTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 ObjectType.class, new StandardTypeIdResolver(), "type", false, String.class);
 ObjectNode jsonNode = JsonTestUtils.createEmptyObjectNode();
 Object person = objectMapper.readValue(jsonNode, deserializer.getType());
 assertNull(person);
 }

 @Test
 public void testDeserializeWrappedObjectWithWrongJsonStructure() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 ObjectType.class, new StandardTypeIdResolver(), "type", false, String.class);
 ObjectNode jsonNode = JsonTestUtils.createEmptyObjectNode();
 jsonNode.put("name", "John");
 try {
 objectMapper.readValue(jsonNode, deserializer.getType());
 } catch (IOException e) {
 assertEquals("need JSON Object to contain As.WRAPPER_OBJECT type information for class "
 + ObjectType.class.getName()
 , e.getMessage());
 }
 }

 @Test
 public void testDeserializeWrappedObjectWithTokenMismatch() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 ObjectType.class, new StandardTypeIdResolver(), "type", false, String.class);
 ObjectNode jsonNode = JsonTestUtils.createEmptyObjectNode();
 jsonNode.put("type", "com.example.Person");
 ObjectParser parser = objectMapper.getFactory().createParser(jsonNode);
 parser.nextToken(); // should be START_OBJECT
 parser.nextToken(); // should be FIELD_NAME, but let's assume it's something else
 try {
 objectMapper.readValue(parser, deserializer.getType());
 } catch (IOException e) {
 assertEquals("expected closing END_OBJECT after type information and deserialized value"
 , e.getMessage());
 }
 }
}