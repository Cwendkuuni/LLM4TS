package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private DeserializationContext ctxt;

 @Before
 public void setup() {
 ctxt = new TestDeserializationContext();
 }

 @Test
 public void testBooleanTypeKeyDeserializer() throws Exception {
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Boolean.class);
 Object key = kd.deserializeKey("true", ctxt);
 assertEquals(Boolean.TRUE, key);
 key = kd.deserializeKey("false", ctxt);
 assertEquals(Boolean.FALSE, key);
 key = kd.deserializeKey("invalid", ctxt);
 assertNull(key);
 }

 @Test
 public void testByteTypeKeyDeserializer() throws Exception {
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Byte.class);
 Object key = kd.deserializeKey("1", ctxt);
 assertEquals((byte) 1, key);
 try {
 key = kd.deserializeKey("257", ctxt);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 key = kd.deserializeKey("invalid", ctxt);
 assertNull(key);
 }

 @Test
 public void testShortTypeKeyDeserializer() throws Exception {
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Short.class);
 Object key = kd.deserializeKey("123", ctxt);
 assertEquals((short) 123, key);
 try {
 key = kd.deserializeKey("32768", ctxt);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 key = kd.deserializeKey("invalid", ctxt);
 assertNull(key);
 }

 @Test
 public void testCharTypeKeyDeserializer() throws Exception {
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Character.class);
 Object key = kd.deserializeKey("a", ctxt);
 assertEquals('a', key);
 try {
 key = kd.deserializeKey("invalid", ctxt);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testIntTypeKeyDeserializer() throws Exception {
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Integer.class);
 Object key = kd.deserializeKey("123", ctxt);
 assertEquals(123, key);
 key = kd.deserializeKey("minus123", ctxt);
 assertEquals(-123, key);
 key = kd.deserializeKey("invalid", ctxt);
 assertNull(key);
 }

 @Test
 public void testLongTypeKeyDeserializer() throws Exception {
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Long.class);
 Object key = kd.deserializeKey("123456789012345", ctxt);
 assertEquals(123456789012345L, key);
 key = kd.deserializeKey("invalid", ctxt);
 assertNull(key);
 }

 @Test
 public void testFloatTypeKeyDeserializer() throws Exception {
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Float.class);
 Object key = kd.deserializeKey("1.23", ctxt);
 assertEquals(1.23f, key, 0.01f);
 key = kd.deserializeKey("invalid", ctxt);
 assertNull(key);
 }

 @Test
 public void testDoubleTypeKeyDeserializer() throws Exception {
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Double.class);
 Object key = kd.deserializeKey("1.23456789", ctxt);
 assertEquals(1.23456789, key, 0.0000001);
 key = kd.deserializeKey("invalid", ctxt);
 assertNull(key);
 }

 @Test
 public void testLocaleTypeKeyDeserializer() throws Exception {
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Locale.class);
 Object key = kd.deserializeKey("en_US", ctxt);
 assertEquals(Locale.US, key);
 key = kd.deserializeKey("invalid", ctxt);
 assertNull(key);
 }

 @Test
 public void testDateTypeKeyDeserializer() throws Exception {
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Date.class);
 Object key = kd.deserializeKey("2012-12-25", ctxt);
 assertEquals(new Date(1356352000000L), key);
 key = kd.deserializeKey("invalid", ctxt);
 assertNull(key);
 }

 @Test
 public void testCalendarTypeKeyDeserializer() throws Exception {
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Calendar.class);
 Object key = kd.deserializeKey("2012-12-25", ctxt);
 Calendar cal = (Calendar) key;
 assertEquals(2012, cal.get(Calendar.YEAR));
 assertEquals(11, cal.get(Calendar.MONTH));
 assertEquals(25, cal.get(Calendar.DAY_OF_MONTH));
 key = kd.deserializeKey("invalid", ctxt);
 assertNull(key);
 }

 @Test
 public void testUUIDTypeKeyDeserializer() throws Exception {
 StdKeyDeserializer kd = StdKeyDeserializer.forType(UUID.class);
 Object key = kd.deserializeKey("e89d4c7e-7be6-4562-9a14-d42e13b56b24", ctxt);
 assertEquals("e89d4c7e-7be6-4562-9a14-d42e13b56b24", key.toString());
 key = kd.deserializeKey("invalid", ctxt);
 assertNull(key);
 }

 @Test
 public void testURITypeKeyDeserializer() throws Exception {
 StdKeyDeserializer kd = StdKeyDeserializer.forType(URI.class);
 Object key = kd.deserializeKey("http://www.example.com/path/to/resource", ctxt);
 assertEquals("http://www.example.com/path/to/resource", key.toString());
 key = kd.deserializeKey("invalid", ctxt);
 assertNull(key);
 }

 @Test
 public void testURLTypeKeyDeserializer() throws Exception {
 StdKeyDeserializer kd = StdKeyDeserializer.forType(URL.class);
 Object key = kd.deserializeKey("http://www.example.com/path/to/resource", ctxt);
 assertEquals("http://www.example.com/path/to/resource", key.toString());
 key = kd.deserializeKey("invalid", ctxt);
 assertNull(key);
 }

 @Test
 public void testClassTypeKeyDeserializer() throws Exception {
 AnnotatedClass annotatedClass = new AnnotatedClass(String[].class, null);
 StdKeyDeserializer kd = new DelegatingKD(annotatedClass, String[].class, (JsonDeserializer<?>) new StringDeserializer());
 Object key = kd.deserializeKey("[Ljava.lang.String;", ctxt);
 assertArrayEquals(new String[]{"one", "two", "three"}, key);
 key = kd.deserializeKey("invalid", ctxt);
 assertNull(key);
 }

 @Test
 public void testByteArrayTypeKeyDeserializer() throws Exception {
 StdKeyDeserializer kd = StdKeyDeserializer.forType(byte[].class);
 Object key = kd.deserializeKey("VG8gZXJyIGlzIGh1bWFuLCBmaWxlIGlzIHVwIHlvdSBmYWNpbGl0aW9uIHBhZ2Ugb2YgZnVsbHNjZXB0IGRhdmU=", ctxt);
 byte[] expected = {0, 87, 103, 97, 121, 98, 114, 101, 99, 104, 111, 119, 112, 119, 111, 114, 101, 116, 116, 112, 114, 101, 108, 101};
 assertArrayEquals(expected, key);
 key = kd.deserializeKey("invalid", ctxt);
 assertNull(key);
 }
}