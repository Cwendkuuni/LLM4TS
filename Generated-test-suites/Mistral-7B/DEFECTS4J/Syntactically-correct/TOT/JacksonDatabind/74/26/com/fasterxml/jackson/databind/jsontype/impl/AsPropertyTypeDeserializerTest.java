package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class AsPropertyTypeDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDeserializeWithValidTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(),
 "type",
 true,
 mapper.getTypeFactory().constructType(Integer.class)
 );

 ObjectNode input = mapper.createObjectNode();
 input.put("type", "StringType");
 input.put("value", "Hello World");

 Object result = deserializer.deserialize(mapper.parserForTesting(input), null);
 assertEquals("Hello World", result);
 }

 @Test
 public void testDeserializeWithMissingTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(),
 "type",
 true,
 mapper.getTypeFactory().constructType(Integer.class)
 );

 ObjectNode input = mapper.createObjectNode();
 input.put("value", "Hello World");

 try {
 deserializer.deserialize(mapper.parserForTesting(input), null);
 fail();
 } catch (IOException e) {
 // Expected
 }
 }

 @Test
 public void testDeserializeWithMissingProperty() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(),
 "type",
 true,
 mapper.getTypeFactory().constructType(Integer.class),
 As.PROPERTY
 );

 ObjectNode input = mapper.createObjectNode();
 input.put("invalidProperty", "Hello World");

 try {
 deserializer.deserialize(mapper.parserForTesting(input), null);
 fail();
 } catch (IOException e) {
 // Expected
 }
 }

 @Test
 public void testDeserializeWithEmptyInput() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(),
 "type",
 true,
 mapper.getTypeFactory().constructType(Integer.class),
 As.PROPERTY
 );

 ObjectNode input = mapper.createObjectNode();

 try {
 deserializer.deserialize(mapper.parserForTesting(input), null);
 fail();
 } catch (IOException e) {
 // Expected
 }
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(),
 "type",
 true,
 mapper.getTypeFactory().constructType(Integer.class),
 As.PROPERTY
 );

 ObjectNode input = mapper.createObjectNode();
 input.put("type", "java.lang.Integer");
 input.put("value", 42);

 Object result = deserializer.deserialize(mapper.parserForTesting(input), null);
 assertEquals(42, result);
 }

 @Test
 public void testDeserializeWithUnexpectedToken() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(),
 "type",
 true,
 mapper.getTypeFactory().constructType(Integer.class),
 As.PROPERTY
 );

 ObjectNode input = mapper.createObjectNode();
 input.put("value", 42);
 input.put("invalid", 42);

 try {
 deserializer.deserialize(mapper.parserForTesting(input), null);
 fail();
 } catch (IOException e) {
 // Expected
 }
 }

 @Test
 public void testDeserializeWithEmptyTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(),
 "type",
 true,
 mapper.getTypeFactory().constructType(Integer.class),
 As.PROPERTY
 );

 ObjectNode input = mapper.createObjectNode();
 input.put("type", "");
 input.put("value", 42);

 Object result = deserializer.deserialize(mapper.parserForTesting(input), null);
 assertEquals(42, result);
 }

 @Test
 public void testDeserializeWithInvalidTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(),
 "type",
 true,
 mapper.getTypeFactory().constructType(Integer.class),
 As.PROPERTY
 );

 ObjectNode input = mapper.createObjectNode();
 input.put("type", "InvalidType");
 input.put("value", 42);

 try {
 deserializer.deserialize(mapper.parserForTesting(input), null);
 fail();
 } catch (IOException e) {
 // Expected
 }
 }

 @Test
 public void testDeserializeWithNullTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(),
 "type",
 true,
 mapper.getTypeFactory().constructType(Integer.class),
 As.PROPERTY
 );

 ObjectNode input = mapper.createObjectNode();
 input.put("type", null);
 input.put("value", 42);

 try {
 deserializer.deserialize(mapper.parserForTesting(input), null);
 fail();
 } catch (IOException e) {
 // Expected
 }
 }
}