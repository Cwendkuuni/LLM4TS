package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

@JacksonStdImpl
public class StdKeyDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testForTypeBoolean() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
 String key = "true";
 Object result = deserializer.deserializeKey(key, objectMapper.getDeserializationContext());
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 key = "false";
 result = deserializer.deserializeKey(key, objectMapper.getDeserializationContext());
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testForTypeByte() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
 String key = "127";
 Object result = deserializer.deserializeKey(key, objectMapper.getDeserializationContext());
 assertTrue(result instanceof Byte);
 assertEquals((byte) 127, result);
 key = "-128";
 result = deserializer.deserializeKey(key, objectMapper.getDeserializationContext());
 assertTrue(result instanceof Byte);
 assertEquals((byte) -128, result);
 }

 @Test
 public void testForTypeShort() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
 String key = "32767";
 Object result = deserializer.deserializeKey(key, objectMapper.getDeserializationContext());
 assertTrue(result instanceof Short);
 assertEquals((short) 32767, result);
 key = "-32768";
 result = deserializer.deserializeKey(key, objectMapper.getDeserializationContext());
 assertTrue(result instanceof Short);
 assertEquals((short) -32768, result);
 }

 @Test
 public void testForTypeChar() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
 String key = "A";
 Object result = deserializer.deserializeKey(key, objectMapper.getDeserializationContext());
 assertTrue(result instanceof Character);
 assertEquals((char) 'A', result);
 }

 @Test
 public void testForTypeInt() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
 String key = "100";
 Object result = deserializer.deserializeKey(key, objectMapper.getDeserializationContext());
 assertTrue(result instanceof Integer);
 assertEquals(100, result);
 }

 @Test
 public void testForTypeLong() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
 String key = "1000000000000";
 Object result = deserializer.deserializeKey(key, objectMapper.getDeserializationContext());
 assertTrue(result instanceof Long);
 assertEquals(1000000000000L, result);
 }

 @Test
 public void testForTypeFloat() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
 String key = "3.14159265";
 Object result = deserializer.deserializeKey(key, objectMapper.getDeserializationContext());
 assertTrue(result instanceof Float);
 assertEquals(3.14159265f, result, 0.00001f);
 }

 @Test
 public void testForTypeDouble() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
 String key = "3.14159265358979323846";
 Object result = deserializer.deserializeKey(key, objectMapper.getDeserializationContext());
 assertTrue(result instanceof Double);
 assertEquals(3.14159265358979323846, result, 0.00000000000001);
 }

 @Test
 public void testForTypeLocale() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
 String key = "en_US";
 Object result = deserializer.deserializeKey(key, objectMapper.getDeserializationContext());
 assertTrue(result instanceof Locale);
 assertEquals(Locale.US, result);
 }

 @Test
 public void testForTypeDate() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
 String key = "2022-12-31";
 Object result = deserializer.deserializeKey(key, objectMapper.getDeserializationContext());
 assertTrue(result instanceof Date);
 assertEquals("Sun Dec 31 00:00:00 GMT 2022", result.toString());
 }

 @Test
 public void testForTypeCalendar() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
 String key = "2022-12-31";
 Object result = deserializer.deserializeKey(key, objectMapper.getDeserializationContext());
 assertTrue(result instanceof Calendar);
 assertEquals(2022, ((Calendar) result).get(Calendar.YEAR));
 assertEquals(12, ((Calendar) result).get(Calendar.MONTH));
 assertEquals(31, ((Calendar) result).get(Calendar.DAY_OF_MONTH));
 }

 @Test
 public void testForTypeUuid() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
 String key = "f81d4fae-7dec-11d0-a765-00a0c91e6bf6";
 Object result = deserializer.deserializeKey(key, objectMapper.getDeserializationContext());
 assertTrue(result instanceof UUID);
 assertEquals(new UUID("f81d4fae-7dec-11d0-a765-00a0c91e6bf6"), result);
 }

 @Test
 public void testForTypeUri() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
 String key = "https://www.example.com/";
 Object result = deserializer.deserializeKey(key, objectMapper.getDeserializationContext());
 assertTrue(result instanceof URI);
 assertEquals("https://www.example.com/", result.toString());
 }

 @Test
 public void testForTypeUrl() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
 String key = "https://www.example.com/";
 Object result = deserializer.deserializeKey(key, objectMapper.getDeserializationContext());
 assertTrue(result instanceof URL);
 assertEquals("https://www.example.com/", result.toString());
 }

 @Test
 public void testForTypeClass() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String[].class);
 String key = "[Ljava.lang.String;";
 Object result = deserializer.deserializeKey(key, objectMapper.getDeserializationContext());
 assertTrue(result instanceof Object);
 assertTrue(result.getClass().isArray());
 assertTrue(result.getClass().getComponentType() == String.class);
 }

 @Test
 public void testForTypeCurrency() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Currency.class);
 String key = "EUR";
 Object result = deserializer.deserializeKey(key, objectMapper.getDeserializationContext());
 assertTrue(result instanceof Currency);
 assertEquals(Currency.getInstance("EUR"), result);
 }
}