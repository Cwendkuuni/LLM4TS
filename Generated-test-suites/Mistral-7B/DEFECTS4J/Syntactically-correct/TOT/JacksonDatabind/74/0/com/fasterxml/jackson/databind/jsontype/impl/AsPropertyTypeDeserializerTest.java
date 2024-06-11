package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;
import static org.junit.Assert.*;

public class AsPropertyTypeDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDeserializeWithPropertyType() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(), "type", true, mapper.getTypeFactory().constructType(Integer.class));
 Object result = deserializer.deserializeTypedFromObject(mapper.createParser("{\"type\": \"java.lang.Integer\", \"value\": 123}"), new TestContext());
 assertEquals(123, result);
 }

 @Test
 public void testDeserializeWithMissingTypeProperty() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(), "type", true, mapper.getTypeFactory().constructType(Integer.class));
 Object result = deserializer.deserializeTypedFromObject(mapper.createParser("{\"value\": 123}"), new TestContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeWithMalformedTypeProperty() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(), "type", true, mapper.getTypeFactory().constructType(Integer.class));
 Object result = deserializer.deserializeTypedFromObject(mapper.createParser("{\"type\": 123, \"value\": 123}"), new TestContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeWithSelfReferentialType() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(), "type", true, mapper.getTypeFactory().constructType(AsPropertyTypeDeserializer.class));
 Object result = deserializer.deserializeTypedFromObject(mapper.createParser("{\"type\": \"com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer\", \"value\": \"test\"}"), new TestContext());
 assertInstanceOf(AsPropertyTypeDeserializer.class, result);
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(), "type", true, mapper.getTypeFactory().constructType(Integer.class));
 Object result = deserializer.deserializeTypedFromObject(mapper.createParser("{\"type\": \"int\", \"value\": 123}"), new TestContext());
 assertEquals(123, result);
 }

 @Test
 public void testDeserializeWithDefaultImpl() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(), "type", true, mapper.getTypeFactory().constructType(String.class), As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(mapper.createParser("{\"value\": \"test\"}"), new TestContext());
 assertEquals("test", result);
 }

 @Test
 public void testDeserializeWithEmptyObject() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(), "type", true, mapper.getTypeFactory().constructType(Integer.class));
 Object result = deserializer.deserializeTypedFromObject(mapper.createParser("{}"), new TestContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeWithArrayWrapper() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(), "type", true, mapper.getTypeFactory().constructType(Integer[].class), As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(mapper.createParser("[\"java.lang.Integer\", 123]"), new TestContext());
 assertArrayEquals(new Integer[]{123}, (Integer[])result);
 }

 @Test
 public void testDeserializeWithScalarValue() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(), "type", true, mapper.getTypeFactory().constructType(Integer.class));
 Object result = deserializer.deserializeTypedFromObject(mapper.createParser("{\"value\": 123}"), new TestContext());
 assertEquals(123, result);
 }

 @Test
 public void testDeserializeWithMissingTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(), "type", true, mapper.getTypeFactory().constructType(Integer.class));
 Object result = deserializer.deserializeTypedFromObject(mapper.createParser("{\"value\": 123}"), new TestContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeWithNullTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(), "type", true, mapper.getTypeFactory().constructType(Integer.class));
 Object result = deserializer.deserializeTypedFromObject(mapper.createParser("{\"type\": null, \"value\": 123}"), new TestContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeWithNonStringTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(), "type", true, mapper.getTypeFactory().constructType(Integer.class));
 Object result = deserializer.deserializeTypedFromObject(mapper.createParser("{\"type\": 123, \"value\": 123}"), new TestContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeWithEmptyArrayWrapper() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(), "type", true, mapper.getTypeFactory().constructType(Integer[].class), As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(mapper.createParser("[\"java.lang.Integer\"]"), new TestContext());
 assertArrayEquals(new Integer[0], (Integer[])result);
 }

 @Test
 public void testDeserializeWithEmptyObjectAndDefaultImpl() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(), "type", true, mapper.getTypeFactory().constructType(String.class), As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(mapper.createParser("{}"), new TestContext());
 assertEquals("", result);
 }

 @Test
 public void testDeserializeWithEmptyObjectAndDefaultImplWithTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(), "type", true, mapper.getTypeFactory().constructType(String.class), As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(mapper.createParser("{\"@type\": \"java.lang.String\"}"), new TestContext());
 assertEquals("", result);
 }

 private static class TestContext extends DeserializationContext {
 @Override
 public Object instantiateValue(Class<?> valueType) throws InstantiationException, IllegalAccessException {
 throw new RuntimeException("Should not be instantiating value objects here.");
 }
 }
}