package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;
import static org.junit.Assert.*;

public class AsPropertyTypeDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testDeserializeTypedObjectWithTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 MapperFactory.defaultInstance(),
 MapperFactory.defaultInstance().getTypeIdResolver(),
 "typePropertyName",
 true,
 MapperFactory.defaultInstance().getTypeFactory().constructType(String.class),
 As.PROPERTY);
 ObjectNode jsonNode = MAPPER.createObjectNode();
 jsonNode.put("typePropertyName", "StringTypeId");
 jsonNode.put("value", "testString");
 Object result = deserializer.deserialize(MapperFactory.defaultInstance().getFactory().getParser(jsonNode), null);
 assertEquals("testString", result);
 }

 @Test
 public void testDeserializeTypedObjectWithoutTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 MapperFactory.defaultInstance(),
 MapperFactory.defaultInstance().getTypeIdResolver(),
 "typePropertyName",
 true,
 MapperFactory.defaultInstance().getTypeFactory().constructType(String.class),
 As.PROPERTY);
 ObjectNode jsonNode = MAPPER.createObjectNode();
 jsonNode.put("value", "testString");
 Object result = deserializer.deserialize(MapperFactory.defaultInstance().getFactory().getParser(jsonNode), null);
 assertEquals("testString", result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithInvalidTypePropertyName() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 MapperFactory.defaultInstance(),
 MapperFactory.defaultInstance().getTypeIdResolver(),
 "invalidTypePropertyName",
 true,
 MapperFactory.defaultInstance().getTypeFactory().constructType(String.class),
 As.PROPERTY);
 ObjectNode jsonNode = MAPPER.createObjectNode();
 jsonNode.put("typePropertyName", "StringTypeId");
 jsonNode.put("value", "testString");
 deserializer.deserialize(MapperFactory.defaultInstance().getFactory().getParser(jsonNode), null);
 fail("Expected IOException");
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithNullTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 MapperFactory.defaultInstance(),
 MapperFactory.defaultInstance().getTypeIdResolver(),
 "typePropertyName",
 true,
 MapperFactory.defaultInstance().getTypeFactory().constructType(String.class),
 As.PROPERTY);
 ObjectNode jsonNode = MAPPER.createObjectNode();
 jsonNode.put("typePropertyName", null);
 jsonNode.put("value", "testString");
 deserializer.deserialize(MapperFactory.defaultInstance().getFactory().getParser(jsonNode), null);
 fail("Expected IOException");
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithEmptyTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 MapperFactory.defaultInstance(),
 MapperFactory.defaultInstance().getTypeIdResolver(),
 "typePropertyName",
 true,
 MapperFactory.defaultInstance().getTypeFactory().constructType(String.class),
 As.PROPERTY);
 ObjectNode jsonNode = MAPPER.createObjectNode();
 jsonNode.put("typePropertyName", "");
 jsonNode.put("value", "testString");
 deserializer.deserialize(MapperFactory.defaultInstance().getFactory().getParser(jsonNode), null);
 fail("Expected IOException");
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 MapperFactory.defaultInstance(),
 MapperFactory.defaultInstance().getTypeIdResolver(),
 "typePropertyName",
 true,
 MapperFactory.defaultInstance().getTypeFactory().constructType(String.class),
 As.PROPERTY);
 ObjectNode jsonNode = MAPPER.createObjectNode();
 jsonNode.put("typePropertyName", "com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializerTest$StringType");
 jsonNode.put("value", "testString");
 Object result = deserializer.deserialize(MapperFactory.defaultInstance().getFactory().getParser(jsonNode), null);
 assertEquals("testString", result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithInvalidNativeTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 MapperFactory.defaultInstance(),
 MapperFactory.defaultInstance().getTypeIdResolver(),
 "typePropertyName",
 true,
 MapperFactory.defaultInstance().getTypeFactory().constructType(String.class),
 As.PROPERTY);
 ObjectNode jsonNode = MAPPER.createObjectNode();
 jsonNode.put("typePropertyName", "com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializerTest$InvalidType");
 jsonNode.put("value", "testString");
 deserializer.deserialize(MapperFactory.defaultInstance().getFactory().getParser(jsonNode), null);
 fail("Expected IOException");
 }
}