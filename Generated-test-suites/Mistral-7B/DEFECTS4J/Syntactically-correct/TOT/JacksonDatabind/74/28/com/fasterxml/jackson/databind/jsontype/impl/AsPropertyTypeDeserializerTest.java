package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.As;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class AsPropertyTypeDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testDeserializeSimpleObject() throws JsonProcessingException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 Map.of("type", String.class),
 Map.of(As.Property.class),
 "typePropertyName",
 true,
 String.class);
 ObjectNode input = MAPPER.createObjectNode();
 input.put("type", "String");
 input.put("name", "John Doe");

 Object result = deserializer.deserialize(MAPPER.readTree(MAPPER.writeValueAsString(input)), null);
 assertEquals("John Doe", result);
 }

 @Test
 public void testDeserializeObjectWithTypeId() throws JsonProcessingException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 Map.of("type", String.class),
 Map.of(As.Property.class),
 "typePropertyName",
 true,
 String.class);
 ObjectNode input = MAPPER.createObjectNode();
 input.put("type", "String");
 input.put("name", "John Doe");
 input.put("typeId", "customTypeId");

 Object result = deserializer.deserialize(MAPPER.readTree(MAPPER.writerWithView(MAPPER.writerForBuffer().writeValueAsString(input))
 .with(SerializationFeature.FAIL_ON_EMPTY_BEANS).writeValueAsString()), null);
 assertNotNull(result);
 // Assuming customTypeId is registered as a valid type id
 assertEquals("customTypeId", result.getClass().getName());
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeMissingTypeProperty() throws JsonProcessingException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 Map.of("type", String.class),
 Map.of(As.Property.class),
 "typePropertyName",
 true,
 String.class);
 ObjectNode input = MAPPER.createObjectNode();
 input.put("name", "John Doe");

 try {
 deserializer.deserialize(MAPPER.readTree(MAPPER.writeValueAsString(input)), null);
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected exception
 }
 }

 @Test
 public void testDeserializeArray() throws JsonProcessingException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 Map.of("type", Integer[].class),
 Map.of(As.Property.class),
 "typePropertyName",
 true,
 Integer[].class);
 ObjectNode input = MAPPER.createObjectNode();
 input.put("type", "java.lang.Integer[]");
 input.put("name", "[1, 2, 3]");

 Object result = deserializer.deserialize(MAPPER.readTree(MAPPER.writeValueAsString(input)), null);
 assertArrayEquals(new Integer[]{1, 2, 3}, (Integer[]) result);
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeMissingStartObject() throws JsonProcessingException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 Map.of("type", String.class),
 Map.of(As.Property.class),
 "typePropertyName",
 true,
 String.class);
 ObjectNode input = MAPPER.createObjectNode();
 input.put("name", "John Doe");

 try {
 deserializer.deserialize(MAPPER.readTree(MAPPER.writeValueAsString(input)), null);
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected exception
 }
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeMissingFieldName() throws JsonProcessingException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 Map.of("type", String.class),
 Map.of(As.Property.class),
 "typePropertyName",
 true,
 String.class);
 ObjectNode input = MAPPER.createObjectNode();
 input.put("type", "String");

 try {
 deserializer.deserialize(MAPPER.readTree(MAPPER.writeValueAsString(input)), null);
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected exception
 }
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeMissingTypePropertyWithDefaultImpl() throws JsonProcessingException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 Map.of("type", String.class),
 Map.of(As.Property.class),
 "typePropertyName",
 true,
 null);
 ObjectNode input = MAPPER.createObjectNode();
 input.put("name", "John Doe");

 try {
 deserializer.deserialize(MAPPER.readTree(MAPPER.writeValueAsString(input)), null);
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected exception
 }
 }

 @Test
 public void testDeserializeWithInvalidTypeProperty() throws JsonProcessingException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 Map.of("type", String.class),
 Map.of(As.Property.class),
 "typePropertyName",
 true,
 String.class);
 ObjectNode input = MAPPER.createObjectNode();
 input.put("type", "InvalidType");
 input.put("name", "John Doe");

 try {
 deserializer.deserialize(MAPPER.readTree(MAPPER.writeValueAsString(input)), null);
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected exception
 }
 }

 @Test
 public void testDeserializeWithInvalidTypeId() throws JsonProcessingException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 Map.of("type", String.class),
 Map.of(As.Property.class),
 "typePropertyName",
 true,
 String.class);
 ObjectNode input = MAPPER.createObjectNode();
 input.put("type", "invalidTypeId");
 input.put("name", "John Doe");

 try {
 deserializer.deserialize(MAPPER.readTree(MAPPER.writeValueAsString(input)), null);
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected exception
 }
 }

 @Test
 public void testDeserializeWithNullTypeProperty() throws JsonProcessingException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 Map.of("type", String.class),
 Map.of(As.Property.class),
 "typePropertyName",
 true,
 String.class);
 ObjectNode input = MAPPER.createObjectNode();
 input.putNull("typePropertyName");
 input.put("name", "John Doe");

 try {
 deserializer.deserialize(MAPPER.readTree(MAPPER.writeValueAsString(input)), null);
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected exception
 }
 }

 @Test
 public void testDeserializeWithEmptyTypeProperty() throws JsonProcessingException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 Map.of("type", String.class),
 Map.of(As.Property.class),
 "typePropertyName",
 true,
 String.class);
 ObjectNode input = MAPPER.createObjectNode();
 input.put("typePropertyName", "");
 input.put("name", "John Doe");

 try {
 deserializer.deserialize(MAPPER.readTree(MAPPER.writeValueAsString(input)), null);
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected exception
 }
 }

 @Test
 public void testDeserializeWithEmptyObject() throws JsonProcessingException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 Map.of("type", String.class),
 Map.of(As.Property.class),
 "typePropertyName",
 true,
 String.class);
 ObjectNode input = MAPPER.createObjectNode();
 input.setAllNull();

 try {
 deserializer.deserialize(MAPPER.readTree(MAPPER.writeValueAsString(input)), null);
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected exception
 }
 }
}