package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class AsWrapperTypeDeserializerTest {

 private ObjectMapper objectMapper;
 private AsWrapperTypeDeserializer deserializer;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 deserializer = new AsWrapperTypeDeserializer(
 ObjectType.class, null, "_type", true, String.class);
 }

 @Test
 public void testDeserializeObject() throws IOException {
 ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();
 jsonNode.put("type", "com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializerTest$TestClass");
 jsonNode.put("value", "Test Value");
 Object result = objectMapper.readValue(jsonNode.toString(), Object.class, objectMapper.findValueDeserializer(Object.class, objectMapper.getFactory()));

 Assert.assertEquals("Test Value", result);
 Assert.assertEquals("com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializerTest$TestClass", deserializer.getTypeId(objectMapper.readTree(jsonNode.toString())));
 }

 @Test
 public void testDeserializeObjectWithEmptyTypeId() throws IOException {
 ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();
 jsonNode.put("value", "Test Value");
 Object result = objectMapper.readValue(jsonNode.toString(), Object.class, deserializer);

 Assert.assertEquals("Test Value", result);
 Assert.assertNull(deserializer.getTypeId(objectMapper.readTree(jsonNode.toString())));
 }

 @Test
 public void testDeserializeObjectWithInvalidTypeId() throws IOException {
 ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();
 jsonNode.put("type", "InvalidTypeId");
 jsonNode.put("value", "Test Value");

 Assert.assertThrows("com.fasterxml.jackson.databind.JsonMappingException", () -> objectMapper.readValue(jsonNode.toString(), Object.class, deserializer));
 }

 @Test
 public void testDeserializeObjectWithNullTypeId() throws IOException {
 ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();
 jsonNode.putNull("type");
 jsonNode.put("value", "Test Value");

 Assert.assertThrows("com.fasterxml.jackson.core.JsonParseException: Unrecognized token 'null' (expecting FIELD_NAME): not a valid JSON Object key", () -> objectMapper.readValue(jsonNode.toString(), Object.class, deserializer));
 }

 @Test
 public void testDeserializeArray() throws IOException {
 ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();
 jsonNode.put("type", "com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializerTest$TestClass");
 jsonNode.put("value", "[1, 2, 3]");
 Object[] result = objectMapper.readValue(jsonNode.toString(), Object[].class, objectMapper.findValueDeserializer(Object[].class, objectMapper.getFactory()));

 Assert.assertArrayEquals(new Object[]{1, 2, 3}, result);
 }

 @Test
 public void testDeserializeScalar() throws IOException {
 ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();
 jsonNode.put("type", "com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializerTest$TestClass");
 jsonNode.put("value", 123);
 Object result = objectMapper.readValue(jsonNode.toString(), Object.class, deserializer);

 Assert.assertEquals(123, result);
 }

 @Test
 public void testDeserializeScalarWithEmptyTypeId() throws IOException {
 ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();
 jsonNode.putNull("type");
 jsonNode.put(0, 123);
 Object result = objectMapper.readValue(jsonNode.toString(), Object.class, deserializer);

 Assert.assertEquals(123, result);
 }

 @Test
 public void testDeserializeScalarWithInvalidTypeId() throws IOException {
 ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();
 jsonNode.put("type", "InvalidTypeId");
 jsonNode.put(0, 123);

 Assert.assertThrows("com.fasterxml.jackson.databind.JsonMappingException", () -> objectMapper.readValue(jsonNode.toString(), Object.class, deserializer));
 }

 @Test
 public void testDeserializeScalarWithNullTypeId() throws IOException {
 ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();
 jsonNode.putNull("type");
 jsonNode.set(0, JsonNodeFactory.instance.valueInt(123));

 Assert.assertThrows("com.fasterxml.jackson.core.JsonParseException: Unrecognized token 'null' (expecting FIELD_NAME): not a valid JSON Object key", () -> objectMapper.readValue(jsonNode.toString(), Object.class, deserializer));
 }

 @Test
 public void testDeserializeAny() throws IOException {
 ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();
 jsonNode.put("type", "com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializerTest$TestClass");
 jsonNode.put("value", 123);
 Object result = objectMapper.readValue(jsonNode.toString(), Object.class, deserializer);

 Assert.assertEquals(123, result);
 }

 @Test
 public void testDeserializeAnyWithEmptyTypeId() throws IOException {
 ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();
 jsonNode.putNull("type");
 jsonNode.put("value", 123);
 Object result = objectMapper.readValue(jsonNode.toString(), Object.class, deserializer);

 Assert.assertEquals(123, result);
 }

 @Test
 public void testDeserializeAnyWithInvalidTypeId() throws IOException {
 ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();
 jsonNode.put("type", "InvalidTypeId");
 jsonNode.put("value", 123);

 Assert.assertThrows("com.fasterxml.jackson.databind.JsonMappingException", () -> objectMapper.readValue(jsonNode.toString(), Object.class, deserializer));
 }

 @Test
 public void testDeserializeAnyWithNullTypeId() throws IOException {
 ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();
 jsonNode.putNull("type");
 jsonNode.set("value", JsonNodeFactory.instance.valueInt(123));

 Assert.assertThrows("com.fasterxml.jackson.core.JsonParseException: Unrecognized token 'null' (expecting FIELD_NAME): not a valid JSON Object key", () -> objectMapper.readValue(jsonNode.toString(), Object.class, deserializer));
 }

 @Test
 public void testDeserializeNativeTypeId() throws IOException {
 ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();
 jsonNode.put("type", "@type com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializerTest$TestClass");
 jsonNode.put("value", "Test Value");
 Object result = objectMapper.readValue(jsonNode.toString(), Object.class, deserializer);

 Assert.assertEquals("Test Value", result);
 Assert.assertEquals("com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializerTest$TestClass", deserializer.getTypeId(objectMapper.readTree(jsonNode.toString())));
 }

 @Test
 public void testDeserializeNativeTypeIdWithEmptyTypeId() throws IOException {
 ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();
 jsonNode.put("value", "Test Value");
 Object result = objectMapper.readValue(jsonNode.toString(), Object.class, deserializer);

 Assert.assertEquals("Test Value", result);
 Assert.assertNull(deserializer.getTypeId(objectMapper.readTree(jsonNode.toString())));
 }

 @Test
 public void testDeserializeNativeTypeIdWithInvalidTypeId() throws IOException {
 ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();
 jsonNode.put("type", "@type InvalidTypeId");
 jsonNode.put("value", "Test Value");

 Assert.assertThrows("com.fasterxml.jackson.databind.JsonMappingException", () -> objectMapper.readValue(jsonNode.toString(), Object.class, deserializer));
 }

 @Test
 public void testDeserializeNativeTypeIdWithNullTypeId() throws IOException {
 ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();
 jsonNode.putNull("type");
 jsonNode.put("value", "Test Value");

 Assert.assertThrows("com.fasterxml.jackson.core.JsonParseException: Unrecognized token 'null' (expecting FIELD_NAME): not a valid JSON Object key", () -> objectMapper.readValue(jsonNode.toString(), Object.class, deserializer));
 }

 @Test
 public void testFindDeserializer() {
 Class<?> testClass = AsWrapperTypeDeserializerTest.TestClass.class;
 JsonDeserializer<Object> result = deserializer.findDeserializer(objectMapper, "TestClass");

 Assert.assertEquals(StdSerializer.instance, result);
 }

 @Test
 public void testFindDeserializerWithNullMap() throws IOException {
 JsonDeserializer<Object> result = deserializer.findDeserializer(objectMapper, null);

 Assert.assertNull(result);
 }

 @Test
 public void testFindDeserializerWithEmptyMap() throws IOException {
 Map<String, JsonDeserializer<?>> map = Collections.emptyMap();
 JsonDeserializer<Object> result = deserializer.findDeserializer(objectMapper, map);

 Assert.assertNull(result);
 }

 @Test
 public void testFindDeserializerWithMissingTypeId() throws IOException {
 Map<String, JsonDeserializer<?>> map = Collections.singletonMap("TestClass2", StdSerializer.instance);
 JsonDeserializer<Object> result = deserializer.findDeserializer(objectMapper, map);

 Assert.assertNull(result);
 }

 @Test
 public void testFindDeserializerWithTypeIdMatched() throws IOException {
 Map<String, JsonDeserializer<?>> map = Collections.singletonMap("TestClass", StdSerializer.instance);
 JsonDeserializer<Object> result = deserializer.findDeserializer(objectMapper, map);

 Assert.assertEquals(StdSerializer.instance, result);
 }

 @Test
 public void testFindDeserializerWithTypeIdMismatched() throws IOException {
 Map<String, JsonDeserializer<?>> map = Collections.singletonMap("TestClass2", StdSerializer.instance);
 JsonDeserializer<Object> result = deserializer.findDeserializer(objectMapper, map);

 Assert.assertNull(result);
 }

 @Test
 public void testFindDeserializerWithNullTypeId() throws IOException {
 Map<String, JsonDeserializer<?>> map = Collections.singletonMap(null, StdSerializer.instance);
 JsonDeserializer<Object> result = deserializer.findDeserializer(objectMapper, map);

 Assert.assertNull(result);
 }

 @Test
 public void testFindDeserializerWithEmptyTypeId() throws IOException {
 Map<String, JsonDeserializer<?>> map = Collections.singletonMap("", StdSerializer.instance);
 JsonDeserializer<Object> result = deserializer.findDeserializer(objectMapper, map);

 Assert.assertNull(result);
 }

 @Test
 public void testFindDeserializerWithInvalidTypeId() throws IOException {
 Map<String, JsonDeserializer<?>> map = Collections.singletonMap("InvalidTypeId", StdSerializer.instance);
 JsonDeserializer<Object> result = deserializer.findDeserializer(objectMapper, map);

 Assert.assertNull(result);
 }

 private static class TestClass {

 }
}