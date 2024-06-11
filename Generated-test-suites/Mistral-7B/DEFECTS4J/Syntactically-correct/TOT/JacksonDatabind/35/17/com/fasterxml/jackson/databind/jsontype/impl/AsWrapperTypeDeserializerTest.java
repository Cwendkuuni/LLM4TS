package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AsWrapperTypeDeserializer tests")
public class AsWrapperTypeDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 @DisplayName("Test deserialization with valid JSON content")
 void testDeserialization() throws Exception {
 ObjectNode input = MAPper.createObjectNode();
 input.put("typeId", "com.example.Person");
 input.put("person", new ObjectMapper().valueToTree(new Person("John Doe", 30)));

 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 JavaType.constructType(Person.class), MAPper.getTypeIdResolver(),
 "_class", true, Person.class);

 Object result = deserializer.deserialize(MAPper.parserForObject(new StringReader(input.toString())), MAPper.contextFactory().applicationContext());

 assertInstanceOf(Person.class, result);
 Person person = (Person) result;
 assertEquals("John Doe", person.getName());
 assertEquals(30, person.getAge());
 }

 @Test
 @DisplayName("Test deserialization with empty JSON content")
 void testDeserializationEmpty() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 JavaType.constructType(String.class), MAPper.getTypeIdResolver(),
 "_class", true, String.class);

 Object result = deserializer.deserialize(MAPper.parserForObject("{}"), MAPper.contextFactory().applicationContext());

 assertTrue(result instanceof String);
 assertEquals("", (String) result);
 }

 @Test
 @DisplayName("Test deserialization with missing typeId")
 void testDeserializationMissingTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 JavaType.constructType(Person.class), MAPper.getTypeIdResolver(),
 "_class", true, Person.class);

 ObjectParser parser = MAPper.parserForObject("{person: {}}");
 Object result = deserializer.deserialize(parser, MAPper.contextFactory().applicationContext());

 assertNull(result);
 }

 @Test
 @DisplayName("Test deserialization with invalid typeId")
 void testDeserializationInvalidTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 JavaType.constructType(Person.class), MAPper.getTypeIdResolver(),
 "_class", true, Person.class);

 ObjectParser parser = MAPper.parserForObject("{typeId: invalidTypeId, person: {}}");
 Object result = deserializer.deserialize(parser, MAPper.contextFactory().applicationContext());

 assertNull(result);
 }

 @Test
 @DisplayName("Test deserialization with missing value")
 void testDeserializationMissingValue() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 JavaType.constructType(Person.class), MAPper.getTypeIdResolver(),
 "_class", true, Person.class);

 ObjectParser parser = MAPper.parserForObject("{typeId: com.example.Person}");
 Object result = deserializer.deserialize(parser, MAPper.contextFactory().applicationContext());

 assertNull(result);
 }

 @Test
 @DisplayName("Test deserialization with null value")
 void testDeserializationNullValue() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 JavaType.constructType(String.class), MAPper.getTypeIdResolver(),
 "_class", true, String.class);

 ObjectParser parser = MAPper.parserForObject("{typeId: null}");
 Object result = deserializer.deserialize(parser, MAPper.contextFactory().applicationContext());

 assertNull(result);
 }

 @Test
 @DisplayName("Test deserialization with non-matching typeId")
 void testDeserializationNonMatchingTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 JavaType.constructType(Person.class), MAPper.getTypeIdResolver(),
 "_class", true, Person.class);

 ObjectParser parser = MAPper.parserForObject("{typeId: com.example.AnotherClass, person: {}}");
 Object result = deserializer.deserialize(parser, MAPper.contextFactory().applicationContext());

 assertNull(result);
 }
}