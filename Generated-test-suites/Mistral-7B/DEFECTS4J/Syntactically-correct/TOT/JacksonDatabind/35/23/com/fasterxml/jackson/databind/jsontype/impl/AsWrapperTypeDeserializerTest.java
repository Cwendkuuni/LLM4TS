package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AsWrapperTypeDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testDeserializeTypedFromObject_ValidWrapperObject() throws IOException {
 Object typeIdResolver = new MapTypeIdResolver();
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("com.example.Person"),
 (TypeIdResolver) typeIdResolver,
 "_class",
 true,
 Person.class);

 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.setTypeIdResolver(typeIdResolver);
 ObjectNode node = objectMapper.createObjectNode();
 node.put("typeId", "com.example.Person");
 node.put("value", "{\"name\":\"John Doe\",\"age\":30}");

 JsonParser parser = objectMapper.treeAsTokens(node);
 Object result = deserializer.deserializeTypedFromObject(parser, new TestContext());

 assertEquals("John Doe", result.getClass().getMethod("getName").invoke(result));
 assertEquals(30, result.getClass().getMethod("getAge").invoke(result));
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeTypedFromObject_MissingTypeId() throws IOException {
 Object typeIdResolver = new MapTypeIdResolver();
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("com.example.Person"),
 (TypeIdResolver) typeIdResolver,
 "_class",
 true,
 Person.class);

 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.setTypeIdResolver(typeIdResolver);
 ObjectNode node = objectMapper.createObjectNode();
 node.put("value", "{\"name\":\"John Doe\",\"age\":30}");

 JsonParser parser = objectMapper.treeAsTokens(node);
 deserializer.deserializeTypedFromObject(parser, new TestContext());
 fail("Expected JsonProcessingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeTypedFromObject_InvalidTypeId() throws IOException {
 Object typeIdResolver = new MapTypeIdResolver();
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("com.example.Person"),
 (TypeIdResolver) typeIdResolver,
 "_class",
 true,
 Person.class);

 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.setTypeIdResolver(typeIdResolver);
 ObjectNode node = objectMapper.createObjectNode();
 node.put("typeId", "com.example.InvalidType");
 node.put("value", "{\"name\":\"John Doe\",\"age\":30}");

 JsonParser parser = objectMapper.treeAsTokens(node);
 deserializer.deserializeTypedFromObject(parser, new TestContext());
 fail("Expected JsonMappingException");
 }

 @Test
 public void testDeserializeTypedFromObject_TypeIdVisible() throws IOException {
 Object typeIdResolver = new MapTypeIdResolver();
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("com.example.Person"),
 (TypeIdResolver) typeIdResolver,
 "_class",
 true,
 Person.class);

 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.setTypeIdResolver(typeIdResolver);
 ObjectNode node = objectMapper.createObjectNode();
 node.put("typeId", "com.example.Person");
 node.put("_class", "com.example.PersonWithClass");
 node.put("value", "{\"name\":\"John Doe\",\"age\":30}");

 JsonParser parser = objectMapper.treeAsTokens(node);
 Object result = deserializer.deserializeTypedFromObject(parser, new TestContext());
 assertEquals("John Doe", result.getClass().getMethod("getName").invoke(result));
 assertEquals(30, result.getClass().getMethod("getAge").invoke(result));
 assertEquals("com.example.PersonWithClass", result.getClass().getName());
 }

 @Test
 public void testDeserializeTypedFromArray() throws IOException {
 Object typeIdResolver = new MapTypeIdResolver();
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("com.example.Person[]"),
 (TypeIdResolver) typeIdResolver,
 "_class",
 true,
 Person[].class);

 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.setTypeIdResolver(typeIdResolver);
 ObjectNode node = objectMapper.createObjectNode();
 node.put("typeId", "com.example.Person");
 node.put("value", "[{\"name\":\"John Doe1\",\"age\":30},{\"name\":\"John Doe2\",\"age\":31}]");

 JsonParser parser = objectMapper.treeAsTokens(node);
 Object result = deserializer.deserializeTypedFromArray(parser, new TestContext());
 assertEquals(2, result.length);
 for (int i = 0; i < result.length; i++) {
 assertEquals("John Doe" + (i + 1), result[i].getClass().getMethod("getName").invoke(result[i]));
 assertEquals(30 + i, result[i].getClass().getMethod("getAge").invoke(result[i]));
 }
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeTypedFromArray_MissingTypeId() throws IOException {
 Object typeIdResolver = new MapTypeIdResolver();
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("com.example.Person[]"),
 (TypeIdResolver) typeIdResolver,
 "_class",
 true,
 Person[].class);

 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.setTypeIdResolver(typeIdResolver);
 ObjectNode node = objectMapper.createObjectNode();
 node.put("value", "[{\"name\":\"John Doe1\",\"age\":30},{\"name\":\"John Doe2\",\"age\":31}]");

 JsonParser parser = objectMapper.treeAsTokens(node);
 deserializer.deserializeTypedFromArray(parser, new TestContext());
 fail("Expected JsonProcessingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeTypedFromArray_InvalidTypeId() throws IOException {
 Object typeIdResolver = new MapTypeIdResolver();
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("com.example.Person[]"),
 (TypeIdResolver) typeIdResolver,
 "_class",
 true,
 Person[].class);

 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.setTypeIdResolver(typeIdResolver);
 ObjectNode node = objectMapper.createObjectNode();
 node.put("typeId", "com.example.InvalidType");
 node.put("value", "[{\"name\":\"John Doe1\",\"age\":30},{\"name\":\"John Doe2\",\"age\":31}]");

 JsonParser parser = objectMapper.treeAsTokens(node);
 deserializer.deserializeTypedFromArray(parser, new TestContext());
 fail("Expected JsonMappingException");
 }

 @Test
 public void testDeserializeTypedFromScalar() throws IOException {
 Object typeIdResolver = new MapTypeIdResolver();
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("java.lang.Integer"),
 (TypeIdResolver) typeIdResolver,
 "_class",
 true,
 Integer.class);

 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.setTypeIdResolver(typeIdResolver);
 ObjectNode node = objectMapper.createObjectNode();
 node.put("typeId", "java.lang.Integer");
 node.put("value", "42");

 JsonParser parser = objectMapper.treeAsTokens(node);
 Object result = deserializer.deserializeTypedFromScalar(parser, new TestContext());
 assertEquals(42, result);
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeTypedFromScalar_MissingTypeId() throws IOException {
 Object typeIdResolver = new MapTypeIdResolver();
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("java.lang.Integer"),
 (TypeIdResolver) typeIdResolver,
 "_class",
 true,
 Integer.class);

 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.setTypeIdResolver(typeIdResolver);
 ObjectNode node = objectMapper.createObjectNode();
 node.put("value", "42");

 JsonParser parser = objectMapper.treeAsTokens(node);
 deserializer.deserializeTypedFromScalar(parser, new TestContext());
 fail("Expected JsonProcessingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeTypedFromScalar_InvalidTypeId() throws IOException {
 Object typeIdResolver = new MapTypeIdResolver();
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("java.lang.Integer"),
 (TypeIdResolver) typeIdResolver,
 "_class",
 true,
 Integer.class);

 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.setTypeIdResolver(typeIdResolver);
 ObjectNode node = objectMapper.createObjectNode();
 node.put("typeId", "com.example.InvalidType");
 node.put("value", "42");

 JsonParser parser = objectMapper.treeAsTokens(node);
 deserializer.deserializeTypedFromScalar(parser, new TestContext());
 fail("Expected JsonMappingException");
 }

 @Test
 public void testDeserializeTypedFromAny() throws IOException {
 Object typeIdResolver = new MapTypeIdResolver();
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("java.lang.Object"),
 (TypeIdResolver) typeIdResolver,
 "_class",
 true,
 Object.class);

 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.setTypeIdResolver(typeIdResolver);
 ObjectNode node = objectMapper.createObjectNode();
 node.put("typeId", "java.lang.String");
 node.put("value", "\"Hello World\"");

 JsonParser parser = objectMapper.treeAsTokens(node);
 Object result = deserializer.deserializeTypedFromAny(parser, new TestContext());
 assertEquals("Hello World", result);
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeTypedFromAny_MissingTypeId() throws IOException {
 Object typeIdResolver = new MapTypeIdResolver();
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("java.lang.Object"),
 (TypeIdResolver) typeIdResolver,
 "_class",
 true,
 Object.class);

 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.setTypeIdResolver(typeIdResolver);
 ObjectNode node = objectMapper.createObjectNode();
 node.put("value", "\"Hello World\"");

 JsonParser parser = objectMapper.treeAsTokens(node);
 deserializer.deserializeTypedFromAny(parser, new TestContext());
 fail("Expected JsonProcessingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeTypedFromAny_InvalidTypeId() throws IOException {
 Object typeIdResolver = new MapTypeIdResolver();
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("java.lang.Object"),
 (TypeIdResolver) typeIdResolver,
 "_class",
 true,
 Object.class);

 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.setTypeIdResolver(typeIdResolver);
 ObjectNode node = objectMapper.createObjectNode();
 node.put("typeId", "com.example.InvalidType");
 node.put("value", "\"Hello World\"");

 JsonParser parser = objectMapper.treeAsTokens(node);
 deserializer.deserializeTypedFromAny(parser, new TestContext());
 fail("Expected JsonMappingException");
 }

 class TestContext extends DeserializationContext {
 }

 class SimpleType extends Type {
 String name;

 public SimpleType(String name) {
 this.name = name;
 }

 @Override
 public String getName() {
 return name;
 }
 }

}