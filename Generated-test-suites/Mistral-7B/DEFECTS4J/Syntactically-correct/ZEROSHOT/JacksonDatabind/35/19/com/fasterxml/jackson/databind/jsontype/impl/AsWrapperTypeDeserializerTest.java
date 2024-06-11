package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AsWrapperTypeDeserializerTest {

 private ObjectMapper mapper;
 private TypeIdResolver idResolver;
 private AsWrapperTypeDeserializer deserializer;

 @BeforeClass
 public static void setupClass() {
 mapper = new ObjectMapper();
 }

 @Before
 public void setup() throws Exception {
 idResolver = mapper.getTypeIdResolver();
 deserializer = new AsWrapperTypeDeserializer(mapper.constructType(String.class), idResolver, "_class", true, String.class);
 }

 @Test
 public void testDeserializeTypedFromObject_ValidWrapperObject() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("typeId", "com.example.MyClass");
 node.put("value", "Hello World!");
 JsonParser parser = mapper.parserForObject(node.toString());
 DeserializationContext ctxt = mapper.getDeserializationContext();
 TypeDeserializer deserializer = this.deserializer;
 Object result = deserializer.deserializeTypedFromObject(parser, ctxt);
 assertEquals("Hello World!", result);
 }

 @Test
 public void testDeserializeTypedFromObject_NullWrapperObject() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 JsonParser parser = mapper.parserForObject(node.toString());
 DeserializationContext ctxt = mapper.getDeserializationContext();
 TypeDeserializer deserializer = this.deserializer;
 Object result = deserializer.deserializeTypedFromObject(parser, ctxt);
 assertNull(result);
 }

 @Test
 public void testDeserializeTypedFromObject_EmptyWrapperObject() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 JsonParser parser = mapper.parserForObject(node.toString());
 DeserializationContext ctxt = mapper.getDeserializationContext();
 TypeDeserializer deserializer = this.deserializer;
 Object result = deserializer.deserializeTypedFromObject(parser, ctxt);
 assertNull(result);
 }

 @Test(expected = JsonParseException.class)
 public void testDeserializeTypedFromObject_MissingTypeId() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("value", "Hello World!");
 JsonParser parser = mapper.parserForObject(node.toString());
 DeserializationContext ctxt = mapper.getDeserializationContext();
 TypeDeserializer deserializer = this.deserializer;
 deserializer.deserializeTypedFromObject(parser, ctxt);
 fail("Expected JsonParseException but got none");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeTypedFromObject_InvalidTypeId() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("typeId", "com.example.InvalidType");
 node.put("value", "Hello World!");
 JsonParser parser = mapper.parserForObject(node.toString());
 DeserializationContext ctxt = mapper.getDeserializationContext();
 TypeDeserializer deserializer = this.deserializer;
 deserializer.deserializeTypedFromObject(parser, ctxt);
 fail("Expected JsonMappingException but got none");
 }

 @Test(expected = JsonParseException.class)
 public void testDeserializeTypedFromObject_InvalidJsonFormat() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("typeId", "com.example.MyClass");
 node.put("value", 123);
 JsonParser parser = mapper.parserForObject(node.toString());
 DeserializationContext ctxt = mapper.getDeserializationContext();
 TypeDeserializer deserializer = this.deserializer;
 deserializer.deserializeTypedFromObject(parser, ctxt);
 fail("Expected JsonParseException but got none");
 }

 @Test
 public void testDeserializeTypedFromArray_ValidWrapperArray() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 ObjectNode arrayNode = mapper.createObjectNode();
 arrayNode.put("0", node);
 node.put("typeId", "com.example.MyClass");
 node.put("value", "Hello World!");
 JsonParser parser = mapper.parserForObject(arrayNode.toString());
 DeserializationContext ctxt = mapper.getDeserializationContext();
 TypeDeserializer deserializer = this.deserializer;
 Object result = deserializer.deserializeTypedFromArray(parser, ctxt);
 assertEquals("Hello World!", result);
 }

 @Test(expected = JsonParseException.class)
 public void testDeserializeTypedFromArray_InvalidJsonFormat() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 ObjectNode arrayNode = mapper.createObjectNode();
 arrayNode.put("0", node);
 JsonParser parser = mapper.parserForObject(arrayNode.toString());
 DeserializationContext ctxt = mapper.getDeserializationContext();
 TypeDeserializer deserializer = this.deserializer;
 deserializer.deserializeTypedFromArray(parser, ctxt);
 fail("Expected JsonParseException but got none");
 }

 @Test(expected = JsonParseException.class)
 public void testDeserializeTypedFromScalar_InvalidJsonFormat() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("value", "Hello World!");
 JsonParser parser = mapper.parserForObject(node.toString());
 DeserializationContext ctxt = mapper.getDeserializationContext();
 TypeDeserializer deserializer = this.deserializer;
 deserializer.deserializeTypedFromScalar(parser, ctxt);
 fail("Expected JsonParseException but got none");
 }

 @Test
 public void testDeserializeTypedFromScalar_NullValue() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.putNull("value");
 JsonParser parser = mapper.parserForObject(node.toString());
 DeserializationContext ctxt = mapper.getDeserializationContext();
 TypeDeserializer deserializer = this.deserializer;
 Object result = deserializer.deserializeTypedFromScalar(parser, ctxt);
 assertNull(result);
 }

 @Test(expected = JsonParseException.class)
 public void testDeserializeTypedFromAny_InvalidJsonFormat() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 JsonParser parser = mapper.parserForObject(node.toString());
 DeserializationContext ctxt = mapper.getDeserializationContext();
 TypeDeserializer deserializer = this.deserializer;
 deserializer.deserializeTypedFromAny(parser, ctxt);
 fail("Expected JsonParseException but got none");
 }

 @Test(expected = JsonParseException.class)
 public void testDeserializeWithNativeTypeId_InvalidTypeId() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("typeId", "com.example.InvalidType");
 node.put("value", "Hello World!");
 JsonParser parser = mapper.parserForObject(node.toString());
 DeserializationContext ctxt = mapper.getDeserializationContext();
 TypeDeserializer deserializer = this.deserializer;
 Object result = deserializer._deserializeWithNativeTypeId(parser, ctxt, "com.example.InvalidType");
 fail("Expected JsonParseException but got none");
 }

 @Test
 public void testDeserializeWithNativeTypeId_ValidTypeId() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("typeId", "com.example.MyClass");
 node.put("value", "Hello World!");
 JsonParser parser = mapper.parserForObject(node.toString());
 DeserializationContext ctxt = mapper.getDeserializationContext();
 TypeDeserializer deserializer = this.deserializer;
 Object result = deserializer._deserializeWithNativeTypeId(parser, ctxt, "com.example.MyClass");
 assertEquals("Hello World!", result);
 }

}