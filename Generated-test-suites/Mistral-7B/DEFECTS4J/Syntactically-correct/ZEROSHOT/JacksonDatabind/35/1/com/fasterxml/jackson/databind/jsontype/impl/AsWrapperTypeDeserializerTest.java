package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ObjectMapperUtil;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Test;

public class AsWrapperTypeDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testDeserializeTypedFromObject() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 ClassUtil.weave(String.class), MAPPER.getTypeIdResolver(), "_class", true, String.class);
 Object actual = deserializer.deserializeTypedFromObject(MAPPER.createParser("{\"_class\":\"java.lang.String\",\"value\":\"Hello\"}"), MAPPER.contextFactory().applicationDeserializerProvider());
 assertEquals("Hello", actual);
 }

 @Test
 public void testDeserializeTypedFromArray() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 ClassUtil.weave(String[].class), MAPPER.getTypeIdResolver(), "_class", true, String[].class);
 Object actual = deserializer.deserializeTypedFromArray(MAPPER.createParser("{\"_class\":[\"java.lang.String\"]}"), MAPPER.contextFactory().applicationDeserializerProvider());
 assertArrayEquals(new String[]{""}, (String[])actual);
 }

 @Test
 public void testDeserializeTypedFromScalar() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 ClassUtil.weave(Integer.class), MAPPER.getTypeIdResolver(), "_class", true, Integer.class);
 Object actual = deserializer.deserializeTypedFromScalar(MAPPER.createParser("{\"_class\":\"java.lang.Integer\",\"value\":42}"), MAPPER.contextFactory().applicationDeserializerProvider());
 assertEquals((Integer)42, actual);
 }

 @Test
 public void testDeserializeTypedFromAny() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 ClassUtil.weave(Object.class), MAPPER.getTypeIdResolver(), "_class", true, Object.class);
 Object actual = deserializer.deserializeTypedFromAny(MAPPER.createParser("{\"_class\":\"java.lang.String\",\"value\":\"Hello\"}"), MAPPER.contextFactory().applicationDeserializerProvider());
 assertEquals("Hello", actual);
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 ClassUtil.weave(String.class), MAPPER.getTypeIdResolver(), "_class", true, String.class);
 ObjectMapper mapper = new ObjectMapper();
 ObjectWriter writer = mapper.writerForType(Map.class);
 Map<String, Object> map = new HashMap<>();
 map.put("_class", "java.lang.String");
 map.put("value", "Hello");
 String json = writer.writeValueAsString(map);
 JsonParser parser = mapper.getFactory().createParser(json);
 Object actual = deserializer.deserializeTypedFromObject(parser, MAPPER.contextFactory().applicationDeserializerProvider());
 assertEquals("Hello", actual);
 }

 @Test(expected = IOException.class)
 public void testInvalidJsonObject() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 ClassUtil.weave(String.class), MAPPER.getTypeIdResolver(), "_class", true, String.class);
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("{\"_class\":\"java.lang.String\"}");
 deserializer.deserializeTypedFromObject(parser, MAPPER.contextFactory().applicationDeserializerProvider());
 fail("Expected IOException");
 }

 @Test(expected = IOException.class)
 public void testMissingTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 ClassUtil.weave(String.class), MAPPER.getTypeIdResolver(), "_class", true, String.class);
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("{\"value\":\"Hello\"}");
 deserializer.deserializeTypedFromObject(parser, MAPPER.contextFactory().applicationDeserializerProvider());
 fail("Expected IOException");
 }

 @Test(expected = IOException.class)
 public void testMissingEndObject() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 ClassUtil.weave(String.class), MAPPER.getTypeIdResolver(), "_class", true, String.class);
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("{\"_class\":\"java.lang.String\",\"value\":\"Hello\"}");
 parser.nextToken(); // consume "value"
 deserializer.deserializeTypedFromObject(parser, MAPPER.contextFactory().applicationDeserializerProvider());
 fail("Expected IOException");
 }

 @Test
 public void testMergeTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 ClassUtil.weave(String.class), MAPPER.getTypeIdResolver(), "_class", true, String.class);
 ObjectMapper mapper = new ObjectMapper();
 ObjectWriter writer = mapper.writerForType(Map.class);
 Map<String, Object> map = new HashMap<>();
 map.put("_class", "java.lang.String");
 map.put("_typeId", "myTypeId");
 map.put("value", "Hello");
 String json = writer.writeValueAsString(map);
 JsonParser parser = mapper.getFactory().createParser(json);
 Object actual = deserializer.deserializeTypedFromObject(parser, MAPPER.contextFactory().applicationDeserializerProvider());
 assertEquals("Hello", actual);
 assertEquals("myTypeId", ObjectMapperUtil.getTypeId(actual));
 }

}