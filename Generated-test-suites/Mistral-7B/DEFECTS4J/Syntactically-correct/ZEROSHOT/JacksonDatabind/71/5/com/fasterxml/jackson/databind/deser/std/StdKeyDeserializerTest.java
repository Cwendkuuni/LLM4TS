package com.example.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 @Test
 public void testBooleanDeserialization() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "true";
 Object result = deserializer.deserializeKey(key, ctxt);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testBooleanDeserializationFailure() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "invalid";
 try {
 deserializer.deserializeKey(key, ctxt);
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected
 }
 }

 @Test
 public void testByteDeserialization() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "123";
 Object result = deserializer.deserializeKey(key, ctxt);
 assertTrue(result instanceof Byte);
 assertEquals((byte) 123, result);
 }

 @Test
 public void testByteDeserializationFailure() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "456";
 try {
 deserializer.deserializeKey(key, ctxt);
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected
 }
 }

 @Test
 public void testShortDeserialization() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "12345";
 Object result = deserializer.deserializeKey(key, ctxt);
 assertTrue(result instanceof Short);
 assertEquals((short) 12345, result);
 }

 @Test
 public void testShortDeserializationFailure() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "65536";
 try {
 deserializer.deserializeKey(key, ctxt);
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected
 }
 }

 @Test
 public void testCharDeserialization() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "A";
 Object result = deserializer.deserializeKey(key, ctxt);
 assertTrue(result instanceof Character);
 assertEquals('A', result);
 }

 @Test
 public void testCharDeserializationFailure() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "AB";
 try {
 deserializer.deserializeKey(key, ctxt);
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected
 }
 }

 @Test
 public void testIntDeserialization() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "123456";
 Object result = deserializer.deserializeKey(key, ctxt);
 assertTrue(result instanceof Integer);
 assertEquals(123456, result);
 }

 @Test
 public void testIntDeserializationFailure() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "1234567";
 try {
 deserializer.deserializeKey(key, ctxt);
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected
 }
 }

 @Test
 public void testLongDeserialization() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "1234567890123456789";
 Object result = deserializer.deserializeKey(key, ctxt);
 assertTrue(result instanceof Long);
 assertEquals(1234567890123456789L, result);
 }

 @Test
 public void testLongDeserializationFailure() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "12345678901234567890";
 try {
 deserializer.deserializeKey(key, ctxt);
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected
 }
 }

 @Test
 public void testFloatDeserialization() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "123.45";
 Object result = deserializer.deserializeKey(key, ctxt);
 assertTrue(result instanceof Float);
 assertEquals(123.45f, result, 0.01f);
 }

 @Test
 public void testFloatDeserializationFailure() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "123.456";
 try {
 deserializer.deserializeKey(key, ctxt);
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected
 }
 }

 @Test
 public void testDoubleDeserialization() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "123.456789";
 Object result = deserializer.deserializeKey(key, ctxt);
 assertTrue(result instanceof Double);
 assertEquals(123.456789, result, 0.00001);
 }

 @Test
 public void testDoubleDeserializationFailure() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "123.4567890";
 try {
 deserializer.deserializeKey(key, ctxt);
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected
 }
 }

 @Test
 public void testLocaleDeserialization() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
 ObjectMapper mapper = new ObjectMapper();
 Object result = deserializer.deserializeKey("en_US", mapper.getDeserializationContext());
 assertTrue(result instanceof Locale);
 assertEquals(Locale.US, result);
 }

 @Test
 public void testLocaleDeserializationFailure() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
 ObjectMapper mapper = new ObjectMapper();
 try {
 deserializer.deserializeKey("invalid", mapper.getDeserializationContext());
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected
 }
 }

 @Test
 public void testDateDeserialization() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
 ObjectMapper mapper = new ObjectMapper();
 ObjectResultType resultType = mapper.getTypeFactory().constructType(Date.class);
 SimpleModule module = new SimpleModule();
 module.addSerializer(Date.class, new JacksonStdSerializer<Date>(Date.class));
 mapper.registerModule(module);
 Object result = deserializer.deserializeKey("2023-03-01", mapper.getDeserializationContext());
 assertTrue(result instanceof Date);
 assertEquals("Thu Mar 02 00:00:00 CET 2023", result.toString());
 }

 @Test
 public void testDateDeserializationFailure() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
 ObjectMapper mapper = new ObjectMapper();
 try {
 deserializer.deserializeKey("invalid", mapper.getDeserializationContext());
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected
 }
 }

 @Test
 public void testCalendarDeserialization() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
 ObjectMapper mapper = new ObjectMapper();
 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
 Calendar calendar = Calendar.getInstance();
 Object result = deserializer.deserializeKey(format.format(calendar.getTime()), mapper.getDeserializationContext());
 assertTrue(result instanceof Calendar);
 assertEquals(calendar, result);
 }

 @Test
 public void testCalendarDeserializationFailure() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
 ObjectMapper mapper = new ObjectMapper();
 try {
 deserializer.deserializeKey("invalid", mapper.getDeserializationContext());
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected
 }
 }

 @Test
 public void testUUIDDeserialization() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
 ObjectMapper mapper = new ObjectMapper();
 Object result = deserializer.deserializeKey("3f0d771b-35d1-4f3f-aaa1-d799b3acd339", mapper.getDeserializationContext());
 assertTrue(result instanceof UUID);
 assertEquals("3f0d771b-35d1-4f3f-aaa1-d799b3acd339", result.toString());
 }

 @Test
 public void testUUIDDeserializationFailure() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
 ObjectMapper mapper = new ObjectMapper();
 try {
 deserializer.deserializeKey("invalid", mapper.getDeserializationContext());
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected
 }
 }

 @Test
 public void testURIDeserialization() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
 ObjectMapper mapper = new ObjectMapper();
 Object result = deserializer.deserializeKey("http://example.com", mapper.getDeserializationContext());
 assertTrue(result instanceof URI);
 assertEquals("http://example.com", result.toString());
 }

 @Test
 public void testURIDeserializationFailure() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
 ObjectMapper mapper = new ObjectMapper();
 try {
 deserializer.deserializeKey("invalid", mapper.getDeserializationContext());
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected
 }
 }

 @Test
 public void testURLDeserialization() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
 ObjectMapper mapper = new ObjectMapper();
 Object result = deserializer.deserializeKey("http://example.com", mapper.getDeserializationContext());
 assertTrue(result instanceof URL);
 assertEquals("http://example.com", result.toString());
 }

 @Test
 public void testURLDeserializationFailure() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
 ObjectMapper mapper = new ObjectMapper();
 try {
 deserializer.deserializeKey("invalid", mapper.getDeserializationContext());
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected
 }
 }

 @Test
 public void testClassDeserialization() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String[].class);
 ObjectMapper mapper = new ObjectMapper();
 Object result = deserializer.deserializeKey("[Ljava.lang.String;", mapper.getDeserializationContext());
 assertTrue(result instanceof String[]);
 assertArrayEquals(new String[]{"one", "two", "three"}, result);
 }

 @Test
 public void testClassDeserializationFailure() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String[].class);
 ObjectMapper mapper = new ObjectMapper();
 try {
 deserializer.deserializeKey("invalid", mapper.getDeserializationContext());
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected
 }
 }

 @Test
 public void testCurrencyDeserialization() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Currency.class);
 ObjectMapper mapper = new ObjectMapper();
 Object result = deserializer.deserializeKey("USD", mapper.getDeserializationContext());
 assertTrue(result instanceof Currency);
 assertEquals(Currency.getInstance("USD"), result);
 }

 @Test
 public void testCurrencyDeserializationFailure() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Currency.class);
 ObjectMapper mapper = new ObjectMapper();
 try {
 deserializer.deserializeKey("invalid", mapper.getDeserializationContext());
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected
 }
 }
}