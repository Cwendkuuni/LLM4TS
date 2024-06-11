package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;

import org.junit.Test;
import static org.junit.Assert.*;

public class ValueInstantiatorTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testDefaultInstantiator() throws Exception {
 ValueInstantiator instantiator = new StdValueInstantiator();
 Object result = instantiator.createUsingDefault(MAPper.getDeserializationContext().disableActiveEnumRegistrations());
 assertEquals(Object.class, result.getClass());
 }

 @Test
 public void testStringInstantiator() throws Exception {
 ValueInstantiator instantiator = new StdValueInstantiator();
 String value = "true";
 Object result = instantiator.createFromString(MAPper.getDeserializationContext(), value);
 assertTrue(result instanceof Boolean);
 assertEquals(true, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidStringInstantiator() throws Exception {
 ValueInstantiator instantiator = new StdValueInstantiator();
 String value = "invalid";
 instantiator.createFromString(MAPper.getDeserializationContext(), value);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testIntInstantiator() throws Exception {
 ValueInstantiator instantiator = new StdValueInstantiator();
 int value = 1;
 Object result = instantiator.createFromInt(MAPper.getDeserializationContext(), value);
 assertEquals(Integer.class, result.getClass());
 assertEquals(value, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidIntInstantiator() throws Exception {
 ValueInstantiator instantiator = new StdValueInstantiator();
 int value = -1;
 instantiator.createFromInt(MAPper.getDeserializationContext(), value);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testLongInstantiator() throws Exception {
 ValueInstantiator instantiator = new StdValueInstantiator();
 long value = 1L;
 Object result = instantiator.createFromLong(MAPper.getDeserializationContext(), value);
 assertEquals(Long.class, result.getClass());
 assertEquals(value, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidLongInstantiator() throws Exception {
 ValueInstantiator instantiator = new StdValueInstantiator();
 long value = -1L;
 instantiator.createFromLong(MAPper.getDeserializationContext(), value);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testDoubleInstantiator() throws Exception {
 ValueInstantiator instantiator = new StdValueInstantiator();
 double value = 1.0;
 Object result = instantiator.createFromDouble(MAPper.getDeserializationContext(), value);
 assertEquals(Double.class, result.getClass());
 assertEquals(value, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidDoubleInstantiator() throws Exception {
 ValueInstantiator instantiator = new StdValueInstantiator();
 double value = -1.0;
 instantiator.createFromDouble(MAPper.getDeserializationContext(), value);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testBooleanInstantiator() throws Exception {
 ValueInstantiator instantiator = new StdValueInstantiator();
 boolean value = true;
 Object result = instantiator.createFromBoolean(MAPper.getDeserializationContext(), value);
 assertEquals(Boolean.class, result.getClass());
 assertEquals(value, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidBooleanInstantiator() throws Exception {
 ValueInstantiator instantiator = new StdValueInstantiator();
 boolean value = false;
 instantiator.createFromBoolean(MAPper.getDeserializationContext(), value);
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingDefaultCreator() throws Exception {
 ValueInstantiator instantiator = new TestValueInstantiator();
 instantiator.createUsingDefault(MAPper.getDeserializationContext());
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingStringCreator() throws Exception {
 ValueInstantiator instantiator = new TestValueInstantiator();
 instantiator.createFromString(MAPper.getDeserializationContext(), "value");
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingIntCreator() throws Exception {
 ValueInstantiator instantiator = new TestValueInstantiator();
 instantiator.createFromInt(MAPper.getDeserializationContext(), 1);
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingLongCreator() throws Exception {
 ValueInstantiator instantiator = new TestValueInstantiator();
 instantiator.createFromLong(MAPper.getDeserializationContext(), 1L);
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingDoubleCreator() throws Exception {
 ValueInstantiator instantiator = new TestValueInstantiator();
 instantiator.createFromDouble(MAPper.getDeserializationContext(), 1.0);
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingBooleanCreator() throws Exception {
 ValueInstantiator instantiator = new TestValueInstantiator();
 instantiator.createFromBoolean(MAPper.getDeserializationContext(), true);
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidDeserialization() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 ValueInstantiator instantiator = new TestValueInstantiator();
 String json = "{\"value\":\"invalid\"}";
 instantiator = (ValueInstantiator) mapper.readValue(json, ValueInstantiator.class);
 fail("Expected JsonProcessingException");
 }
}

class TestValueInstantiator extends ValueInstantiator {
 // Empty implementation for testing purposes
}