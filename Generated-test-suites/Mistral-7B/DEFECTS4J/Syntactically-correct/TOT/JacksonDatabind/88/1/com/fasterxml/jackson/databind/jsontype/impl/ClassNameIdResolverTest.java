package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClassNameIdResolverTest {

 private ObjectMapper objectMapper;
 private TypeFactory typeFactory;

 @Before
 public void setUp() {
 objectMapper = new ObjectMapper();
 typeFactory = objectMapper.getTypeFactory();
 }

 @Test
 public void testIdFromValue() throws Exception {
 String json = "{\"value\":42,\"type\":\"java.lang.Integer\"}";
 JsonNode node = objectMapper.readTree(json);
 JavaType javaType = objectMapper.getTypeFactory().constructType(int.class);
 ClassNameIdResolver resolver = new ClassNameIdResolver(javaType, typeFactory);
 ObjectReader reader = objectMapper.readerForUpdating(Object.class);
 Object result = reader.readValue(node);
 assertEquals("java.lang.Integer", resolver.idFromValue(result));
 }

 @Test
 public void testIdFromValueAndType() throws Exception {
 String json = "{\"value\":42,\"type\":\"java.lang.Integer\"}";
 JsonNode node = objectMapper.readTree(json);
 JavaType javaType = objectMapper.getTypeFactory().constructType(int.class);
 ClassNameIdResolver resolver = new ClassNameIdResolver(javaType, typeFactory);
 ObjectReader reader = objectMapper.readerForUpdating(Object.class);
 Object result = reader.readValue(node);
 Class<?> actualType = result.getClass();
 assertEquals("java.lang.Integer", resolver.idFromValueAndType(result, actualType));
 }

 @Test
 public void testTypeFromId() throws Exception {
 String json = "{\"id\":\"java.lang.String\"}";
 String id = "java.lang.String";
 JavaType javaType = JavaType.construct(String.class);
 ClassNameIdResolver resolver = new ClassNameIdResolver(javaType, typeFactory);
 ObjectReader reader = objectMapper.readerForUpdating(Object.class);
 Object result = reader.readValue(json);
 JavaType actualType = resolver.typeFromId(objectMapper.getDeserializationContext(), (String) result);
 assertEquals(javaType, actualType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTypeId() throws Exception {
 String json = "{\"id\":\"invalidTypeId\"}";
 String id = "invalidTypeId";
 JavaType javaType = JavaType.construct(String.class);
 ClassNameIdResolver resolver = new ClassNameIdResolver(javaType, typeFactory);
 ObjectReader reader = objectMapper.readerForUpdating(Object.class);
 Object result = reader.readValue(json);
 try {
 resolver.typeFromId(objectMapper.getDeserializationContext(), (String) result);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testIdFromValueWithEnum() throws Exception {
 String json = "{\"value\":Color.RED,\"type\":\"com.example.Color\"}";
 JsonNode node = objectMapper.readTree(json);
 JavaType javaType = objectMapper.getTypeFactory().constructType(Color.class);
 ClassNameIdResolver resolver = new ClassNameIdResolver(javaType, typeFactory);
 ObjectReader reader = objectMapper.readerForUpdating(Object.class);
 Object result = reader.readValue(node);
 assertEquals("com.example.Color", resolver.idFromValue(result));
 }

 @Test
 public void testIdFromValueWithArray() throws Exception {
 int[] arr = {1, 2, 3};
 String json = "{\"value\":[" + Arrays.toString(arr) + "],\"type\":\"java.lang.int[]\"}";
 JsonNode node = objectMapper.readTree(json);
 JavaType javaType = new TypeReference<int[]>(){}.getType();
 ClassNameIdResolver resolver = new ClassNameIdResolver(javaType, typeFactory);
 ObjectReader reader = objectMapper.readerForUpdating(Object.class);
 Object result = reader.readValue(node);
 assertEquals("java.lang.int[]", resolver.idFromValue(result));
 }

 @Test
 public void testIdFromValueWithCollection() throws Exception {
 List<String> list = Arrays.asList("one", "two", "three");
 String json = "{\"value\":" + jsonString(list) + ",\"type\":\"java.util.List\"}";
 JsonNode node = objectMapper.readTree(json);
 JavaType javaType = new TypeReference<List<String>>(){}.getType();
 ClassNameIdResolver resolver = new ClassNameIdResolver(javaType, typeFactory);
 ObjectReader reader = objectMapper.readerForUpdating(Object.class);
 Object result = reader.readValue(node);
 assertEquals("java.util.ArrayList", resolver.idFromValue(result));
 }

 private String jsonString(Object obj) throws Exception {
 ObjectWriter writer = objectMapper.writerForObject(obj);
 return writer.writeValueAsString(obj);
 }
}