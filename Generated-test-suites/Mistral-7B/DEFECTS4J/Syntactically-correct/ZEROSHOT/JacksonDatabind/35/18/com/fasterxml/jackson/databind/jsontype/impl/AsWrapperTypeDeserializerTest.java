package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.util.TypeFactory;

public class AsWrapperTypeDeserializerTest {

 private ObjectMapper mapper;
 private TypeFactory typeFactory;
 private JavaType javaType;
 private TypeIdResolver typeIdResolver;
 private AsWrapperTypeDeserializer deserializer;

 @Before
 public void setup() throws IOException {
 mapper = new ObjectMapper();
 typeFactory = mapper.getTypeFactory();
 javaType = typeFactory.constructType(String.class);
 typeIdResolver = mapper.getTypeIdResolver();
 deserializer = new AsWrapperTypeDeserializer(javaType, typeIdResolver, "_class", true, String.class);
 }

 @Test
 public void testDeserializeTypedFromObject() throws IOException {
 Object result = deserializer.deserializeTypedFromObject(mapper.getFactory().createParser("{\"type\":\"java.lang.String\",\"value\":\"Hello World\"}"), mapper.getDeserializationContext());
 assertEquals("Hello World", result);
 }

 @Test
 public void testDeserializeTypedFromArray() throws IOException {
 JsonParser jsonParser = mapper.getFactory().createParser("[[\"type\":\"java.lang.String\",\"value\":\"Hello World\"]]");
 Object result = deserializer.deserializeTypedFromArray(jsonParser, mapper.getDeserializationContext());
 assertEquals("Hello World", result);
 }

 @Test
 public void testDeserializeTypedFromScalar() throws IOException {
 Object result = deserializer.deserializeTypedFromScalar(mapper.getFactory().createParser("\"Hello World\""), mapper.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeTypedFromAny() throws IOException {
 Object result = deserializer.deserializeTypedFromAny(mapper.getFactory().createParser("{\"type\":\"java.lang.Integer\",\"value\":42}"), mapper.getDeserializationContext());
 assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeTypedFromObjectWrongToken() throws IOException {
 Object result = deserializer.deserializeTypedFromObject(mapper.getFactory().createParser("{\"type\":1,\"value\":\"Hello World\"}"), mapper.getDeserializationContext());
 }

 @Test(expected = IOException.class)
 public void testDeserializeTypedFromObjectMissingTypeId() throws IOException {
 Object result = deserializer.deserializeTypedFromObject(mapper.getFactory().createParser("{\"value\":\"Hello World\"}"), mapper.getDeserializationContext());
 }

 @Test(expected = IOException.class)
 public void testDeserializeTypedFromObjectMissingValue() throws IOException {
 Object result = deserializer.deserializeTypedFromObject(mapper.getFactory().createParser("{\"type\":\"java.lang.String\"}"), mapper.getDeserializationContext());
 }

 @Test(expected = IOException.class)
 public void testDeserializeTypedFromObjectTypeIdMismatch() throws IOException {
 Object result = deserializer.deserializeTypedFromObject(mapper.getFactory().createParser("{\"type\":\"java.lang.Integer\",\"value\":\"Hello World\"}"), mapper.getDeserializationContext());
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws IOException {
 Object result = deserializer.deserializeTypedFromObject(mapper.getFactory().createParser("{\"@type\":\"java.lang.String\",\"value\":\"Hello World\"}"), mapper.getDeserializationContext());
 assertEquals("Hello World", result);
 }
}