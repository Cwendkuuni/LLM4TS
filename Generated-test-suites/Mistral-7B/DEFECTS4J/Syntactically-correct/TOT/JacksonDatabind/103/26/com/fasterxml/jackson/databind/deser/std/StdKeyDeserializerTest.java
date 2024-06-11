package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class StdKeyDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new JsonMapper();
 }

 @Test
 public void testBooleanTypeKeyDeserializer() throws JsonProcessingException {
 String key = "true";
 StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
 Object result = serializer.deserializeKey(key, new DeserializationContext());
 assertEquals(true, result);

 key = "false";
 result = serializer.deserializeKey(key, new DeserializationContext());
 assertEquals(false, result);
 }

 @Test
 public void testByteTypeKeyDeserializer() throws JsonProcessingException {
 StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
 String key = "127";
 Object result = serializer.deserializeKey(key, new DeserializationContext());
 assertEquals((byte) 127, result);

 key = "-1";
 result = serializer.deserializeKey(key, new DeserializationContext());
 assertThrows(IllegalArgumentException.class, () -> serializer.deserializeKey(key, new DeserializationContext()));
 }

 @Test
 public void testStringTypeKeyDeserializer() throws JsonProcessingException {
 StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
 String key = "test";
 Object result = serializer.deserializeKey(key, new DeserializationContext());
 assertEquals("test", result);
 }

 @Test
 public void testDateTypeKeyDeserializer() throws JsonProcessingException {
 StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
 String key = "2022-01-01";
 Object result = serializer.deserializeKey(key, new DeserializationContext());
 assertNotNull(result);
 }

 @Test(expected = IOException.class)
 public void testUnknownTypeKeyDeserializer() throws JsonProcessingException {
 StdKeyDeserializer serializer = new StdKeyDeserializer(100, Object.class);
 String key = "test";
 Object result = serializer.deserializeKey(key, new DeserializationContext());
 assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testInvalidURITypeKeyDeserializer() throws JsonProcessingException {
 StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
 String key = "invalid://test";
 Object result = serializer.deserializeKey(key, new DeserializationContext());
 assertNull(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidURLTypeKeyDeserializer() throws JsonProcessingException {
 StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
 String key = "invalid:test";
 Object result = serializer.deserializeKey(key, new DeserializationContext());
 assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testNullKey() throws JsonProcessingException {
 StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
 Object result = serializer.deserializeKey(null, new DeserializationContext());
 assertNull(result);
 }
}