package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class StdSerializerTest {

 private ObjectMapper mapper;
 private StdSerializer<String> stringSerializer;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 stringSerializer = mapper.findValueSerializer(String.class);
 }

 @Test
 public void testSerializedType() throws JsonProcessingException {
 assertEquals(String.class, stringSerializer.handledType());
 }

 @Test
 public void testSerializeNull() throws JsonProcessingException {
 String expected = null;
 ObjectNode resultNode = mapper.writeValueAsTree(null);
 String actual = resultNode.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeString() throws JsonProcessingException {
 String input = "Hello, World!";
 ObjectNode resultNode = mapper.writeValueAsTree(input);
 String actual = resultNode.toString();
 String expected = "\"Hello, World!\"";
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSchemaNullTypeHint() throws JsonMappingException {
 ObjectNode result = stringSerializer.getSchema(mapper.getDefaultSerializerProvider(), null);
 String expectedType = "null";
 String actualType = result.get("type").asText();
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetSchemaStringTypeHint() throws JsonMappingException {
 ObjectNode result = stringSerializer.getSchema(mapper.getDefaultSerializerProvider(), String.class);
 String expectedType = "string";
 String actualType = result.get("type").asText();
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testAcceptJsonFormatVisitorNull() throws JsonMappingException {
 // Default implementation specifies no format
 stringSerializer.acceptJsonFormatVisitor(mapper.getDefaultSerializerProvider().getSerializerProvider(), null);
 }

 @Test
 public void testAcceptJsonFormatVisitorCustomFormat() throws JsonMappingException {
 // Implementation can be overridden by custom serializers
 fail("Method not tested: should be overridden by custom serializers");
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeWithException() throws JsonProcessingException, JsonMappingException {
 String input = "An error occurred.";
 try {
 stringSerializer.serialize(input, mapper.getJsonFactory().createGenerator(System.out), mapper.getDefaultSerializerProvider());
 fail("Expected exception: JsonMappingException");
 } catch (IOException e) {
 stringSerializer.wrapAndThrow(mapper.getDefaultSerializerProvider(), e, input, "input");
 }
 }
}