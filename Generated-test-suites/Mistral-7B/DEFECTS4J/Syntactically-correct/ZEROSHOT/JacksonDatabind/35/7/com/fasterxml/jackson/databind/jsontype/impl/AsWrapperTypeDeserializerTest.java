package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Test;

public class AsWrapperTypeDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testDeserializeTypedFromObject() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 MapperFactory.stdFromCollections(), MAPPER.getTypeIdResolver(), "_class", true, String.class);
 JsonNode node = MAPPER.readTree("{\"typeId\":\"java.lang.String\", \"value\":\"test\"}");
 Object result = deserializer.deserialize(MAPPER.createParser(node), new TestContext());
 assertEquals("test", result);
 }

 @Test
 public void testDeserializeTypedFromArray() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 MapperFactory.stdFromCollections(), MAPPER.getTypeIdResolver(), "_class", true, Integer.class);
 JsonNode node = MAPPER.readTree("[\"" + AsWrapperTypeDeserializer.class.getName() + "\", {\"typeId\":\"java.lang.Integer\", \"value\":42}]");
 Object result = deserializer.deserialize(MAPPER.createParser(node), new TestContext());
 assertEquals(42, result);
 }

 @Test
 public void testDeserializeTypedFromScalar() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 MapperFactory.stdFromCollections(), MAPPER.getTypeIdResolver(), "_class", true, BigDecimal.class);
 JsonParser jp = MAPPER.getFactory().createParser("42.123");
 Object result = deserializer.deserialize(jp, new TestContext());
 assertTrue(result instanceof BigDecimal);
 assertEquals("42.123", result.toString());
 }

 @Test
 public void testDeserializeTypedFromAny() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 MapperFactory.stdFromCollections(), MAPPER.getTypeIdResolver(), "_class", true, Boolean.class);
 JsonNode node = MAPPER.readTree("{\"typeId\":\"java.lang.String\", \"value\":true}");
 Object result = deserializer.deserialize(MAPPER.createParser(node), new TestContext());
 assertTrue(result instanceof Boolean);
 assertEquals(true, result);
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 MapperFactory.stdFromCollections(), MAPPER.getTypeIdResolver(), "_class", true, String.class);
 JsonParser jp = MAPPER.getFactory().createParser("{\"@class\":\"java.lang.String\", \"value\":\"test\"}");
 Object result = deserializer.deserialize(jp, new TestContext());
 assertEquals("test", result);
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidJsonObject() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 MapperFactory.stdFromCollections(), MAPPER.getTypeIdResolver(), "_class", true, String.class);
 JsonParser jp = MAPPER.getFactory().createParser("{\"value\":\"test\"}");
 deserializer.deserialize(jp, new TestContext());
 fail("Expected JsonProcessingException but no exception thrown");
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidTypeIdKey() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 MapperFactory.stdFromCollections(), MAPPER.getTypeIdResolver(), "_class", true, String.class);
 JsonNode node = MAPPER.readTree("{\"XtypeId\":\"java.lang.String\", \"value\":\"test\"}");
 JsonParser jp = MAPPER.createParser(node);
 deserializer.deserialize(jp, new TestContext());
 fail("Expected JsonProcessingException but no exception thrown");
 }

 @Test(expected = ClassCastException.class)
 public void testDeserializeWithWrongTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 MapperFactory.stdFromCollections(), MAPPER.getTypeIdResolver(), "_class", true, String.class);
 JsonNode node = MAPPER.readTree("{\"typeId\":\"java.lang.Integer\", \"value\":\"test\"}");
 JsonParser jp = MAPPER.createParser(node);
 deserializer.deserialize(jp, new TestContext());
 fail("Expected ClassCastException but no exception thrown");
 }

 @Test
 public void testMergeTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 MapperFactory.stdFromCollections(), MAPPER.getTypeIdResolver(), "_class", true, String.class);
 JsonNode node = MAPPER.readTree("{\"typeId\":\"java.lang.String\", \"value\":\"test\", \"_class\":\"java.lang.Long\"}");
 Object result = deserializer.deserialize(MAPPER.createParser(node), new TestContext());
 assertNull(result);
 }

 static class TestContext extends DeserializationContext {
 }
}