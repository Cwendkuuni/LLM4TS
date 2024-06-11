package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StdSerializerTest {

 private ObjectMapper objectMapper;
 private SerializerProvider serializerProvider;
 private FilterProvider filterProvider;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 serializerProvider = mock(SerializerProvider.class);
 filterProvider = mock(FilterProvider.class);
 when(serializerProvider.getFilterProvider()).thenReturn(filterProvider);
 }

 @After
 public void teardown() {
 objectMapper = null;
 serializerProvider = null;
 filterProvider = null;
 }

 @Test
 public void testStdSerializer_StringHandler() throws JsonProcessingException {
 StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
 ObjectNode resultNode = objectMapper.writeValueAsTree(stringSerializer, "Hello World!");
 ObjectNode schemaNode = stringSerializer.getSchema(serializerProvider, String.class);
 ObjectNode expectedSchemaNode = createSchemaNode("string");
 assertEquals(expectedSchemaNode, schemaNode);
 }

 @Test
 public void testStdSerializer_NumberHandler() throws JsonProcessingException {
 StdSerializer<Number> numberSerializer = new StdSerializer<Number>(Number.class);
 ObjectNode resultNode = objectMapper.writeValueAsTree(numberSerializer, 123L);
 ObjectNode schemaNode = numberSerializer.getSchema(serializerProvider, Number.class);
 ObjectNode expectedSchemaNode = createSchemaNode("number");
 assertEquals(expectedSchemaNode, schemaNode);
 }

 @Test
 public void testStdSerializer_NullHandler() throws JsonProcessingException {
 StdSerializer<Object> nullSerializer = new StdSerializer<Object>(Object.class);
 ObjectNode resultNode = objectMapper.writeValueAsTree(nullSerializer, null);
 ObjectNode schemaNode = nullSerializer.getSchema(serializerProvider, Object.class);
 ObjectNode expectedSchemaNode = createSchemaNode("null");
 assertEquals(expectedSchemaNode, schemaNode);
 }

 @Test
 public void testStdSerializer_EmptyListHandler() throws JsonProcessingException {
 StdSerializer<List<String>> emptyListSerializer = new StdSerializer<List<String>>(ArrayList.class);
 ObjectNode resultNode = objectMapper.writeValueAsTree(emptyListSerializer, new ArrayList<>());
 ObjectNode schemaNode = emptyListSerializer.getSchema(serializerProvider, new TypeReference<List<String>>(){});
 ObjectNode expectedSchemaNode = createSchemaNode("array");
 ObjectNode arraySchemaNode = createSchemaNode("items", "string");
 expectedSchemaNode.set("type", "array");
 expectedSchemaNode.set("items", arraySchemaNode);
 assertEquals(expectedSchemaNode, schemaNode);
 }

 @Test
 public void testStdSerializer_EmptyArrayHandler() throws JsonProcessingException {
 StdSerializer<String[]> emptyArraySerializer = new StdSerializer<String[]>(String[].class);
 ObjectNode resultNode = objectMapper.writeValueAsTree(emptyArraySerializer, new String[0]);
 ObjectNode schemaNode = emptyArraySerializer.getSchema(serializerProvider, String[].class);
 ObjectNode expectedSchemaNode = createSchemaNode("array");
 assertEquals(expectedSchemaNode, schemaNode);
 }

 @Test
 public void testStdSerializer_CustomSchema_StringHandler() throws JsonProcessingException {
 StdSerializer<String> customSchemaSerializer = new StdSerializer<String>(String.class) {
 @Override
 public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
 return createSchemaNode("string", true);
 }
 };
 ObjectNode resultNode = objectMapper.writeValueAsTree(customSchemaSerializer, "Hello World!");
 ObjectNode schemaNode = customSchemaSerializer.getSchema(serializerProvider, String.class);
 ObjectNode expectedSchemaNode = createSchemaNode("string", true);
 assertEquals(expectedSchemaNode, schemaNode);
 }

 @Test
 public void testStdSerializer_WrapAndThrow() throws JsonMappingException {
 try {
 StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
 throw new RuntimeException();
 } catch (JsonMappingException e) {
 // expected
 }
 StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
 ObjectNode inputNode = createObjectNode();
 try {
 stringSerializer.serialize(null, serializerProvider, inputNode);
 } catch (IOException e) {
 // expected
 }
 StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
 ObjectNode inputNode = createObjectNode();
 try {
 stringSerializer.serialize(new Object(), serializerProvider, inputNode);
 } catch (JsonMappingException e) {
 // expected
 }
 StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
 ObjectNode inputNode = createObjectNode();
 try {
 stringSerializer.serialize(new Object(), serializerProvider, null);
 } catch (NullPointerException e) {
 // expected
 }
 }

 private ObjectNode createSchemaNode(String type, boolean isOptional) {
 return createSchemaNode(type);
 }

 private ObjectNode createSchemaNode(String type) {
 ObjectNode schema = createObjectNode();
 schema.put("type", type);
 return schema;
 }

}