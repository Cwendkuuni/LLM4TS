package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializerFactory;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class StdKeyDeserializerTest {

 private DeserializationContext ctxt;
 private StdKeyDeserializer keyDeserializer;

 @Before
 public void setup() {
 ctxt = mock(DeserializationContext.class);
 keyDeserializer = new StdKeyDeserializer(1, String.class);
 }

 @Test
 public void testBooleanDeserialization() throws Exception {
 Object key = keyDeserializer.deserializeKey("true", ctxt);
 assertEquals(Boolean.TRUE, key);
 key = keyDeserializer.deserializeKey("false", ctxt);
 assertEquals(Boolean.FALSE, key);
 key = keyDeserializer.deserializeKey("invalid", ctxt);
 assertNull(key);
 }

 @Test
 public void testByteDeserialization() throws Exception {
 Object key = keyDeserializer.deserializeKey("1", ctxt);
 assertEquals((byte) 1, key);
 key = keyDeserializer.deserializeKey("-127", ctxt);
 assertEquals((byte) -127, key);
 key = keyDeserializer.deserializeKey("128", ctxt);
 assertThrows(JsonMappingException.class, () -> keyDeserializer.deserializeKey("256", ctxt));
 }

 @Test
 public void testShortDeserialization() throws Exception {
 Object key = keyDeserializer.deserializeKey("1", ctxt);
 assertEquals((short) 1, key);
 key = keyDeserializer.deserializeKey("-32767", ctxt);
 assertEquals((short) -32767, key);
 key = keyDeserializer.deserializeKey("32768", ctxt);
 assertThrows(JsonMappingException.class, () -> keyDeserializer.deserializeKey("32769", ctxt));
 }

 @Test
 public void testCharDeserialization() throws Exception {
 Object key = keyDeserializer.deserializeKey("'a'", ctxt);
 assertEquals((char) 'a', key);
 key = keyDeserializer.deserializeKey("'\\u00E9'", ctxt);
 assertEquals((char) 0xE9, key);
 key = keyDeserializer.deserializeKey("invalid", ctxt);
 assertNull(key);
 }

 @Test
 public void testIntDeserialization() throws Exception {
 Object key = keyDeserializer.deserializeKey("1", ctxt);
 assertEquals(1, key);
 key = keyDeserializer.deserializeKey("-2147483648", ctxt);
 assertEquals(-2147483648, key);
 key = keyDeserializer.deserializeKey("2147483647", ctxt);
 assertThrows(JsonMappingException.class, () -> keyDeserializer.deserializeKey("2147483648", ctxt));
 }

 @Test
 public void testLongDeserialization() throws Exception {
 Object key = keyDeserializer.deserializeKey("1", ctxt);
 assertEquals(1L, key);
 key = keyDeserializer.deserializeKey("-9223372036854775808", ctxt);
 assertEquals(-9223372036854775808L, key);
 key = keyDeserializer.deserializeKey("9223372036854775807", ctxt);
 assertThrows(JsonMappingException.class, () -> keyDeserializer.deserializeKey("9223372036854775809", ctxt));
 }

 @Test
 public void testFloatDeserialization() throws Exception {
 Object key = keyDeserializer.deserializeKey("1.0", ctxt);
 assertEquals(1.0f, key);
 key = keyDeserializer.deserializeKey("-3.14", ctxt);
 assertEquals(-3.14f, key);
 key = keyDeserializer.deserializeKey("inf", ctxt);
 assertThrows(JsonMappingException.class, () -> keyDeserializer.deserializeKey("nan", ctxt));
 }

 @Test
 public void testDoubleDeserialization() throws Exception {
 Object key = keyDeserializer.deserializeKey("1.0", ctxt);
 assertEquals(1.0, key);
 key = keyDeserializer.deserializeKey("-3.14", ctxt);
 assertEquals(-3.14, key);
 key = keyDeserializer.deserializeKey("inf", ctxt);
 assertThrows(JsonMappingException.class, () -> keyDeserializer.deserializeKey("nan", ctxt));
 }

 @Test
 public void testLocaleDeserialization() throws Exception {
 Locale locale = Locale.US;
 DeserializerFactory deserializerFactory = mock(DeserializerFactory.class);
 when(deserializerFactory.findValueDeserializer(locale, ctxt)).thenReturn(mock(FromStringDeserializer.class));
 keyDeserializer = new StdKeyDeserializer(13, locale.getClass(), deserializerFactory);
 Object key = keyDeserializer.deserializeKey("en_US", ctxt);
 assertEquals(locale, key);
 key = keyDeserializer.deserializeKey("fr_FR", ctxt);
 assertEquals(Locale.FRENCH, key);
 key = keyDeserializer.deserializeKey("invalid", ctxt);
 assertNull(key);
 }

 @Test
 public void testUriDeserialization() throws Exception {
 Object key = keyDeserializer.deserializeKey("http://example.com", ctxt);
 assertEquals(new URI("http://example.com"), key);
 key = keyDeserializer.deserializeKey("invalid", ctxt);
 assertNull(key);
 }

 @Test
 public void testUrlDeserialization() throws Exception {
 Object key = keyDeserializer.deserializeKey("http://example.com", ctxt);
 assertEquals(new URL("http://example.com"), key);
 key = keyDeserializer.deserializeKey("invalid", ctxt);
 assertNull(key);
 }

 @Test
 public void testClassDeserialization() throws Exception {
 Class<?> clazz = String.class;
 Object key = keyDeserializer.deserializeKey(clazz.getName(), ctxt);
 assertEquals(clazz, key);
 key = keyDeserializer.deserializeKey("java.lang.Integer", ctxt);
 assertEquals(Integer.class, key);
 key = keyDeserializer.deserializeKey("invalid", ctxt);
 assertNull(key);
 }

 @Test
 public void testEnumDeserialization() throws Exception {
 Enum enumValue = Enum.values()[0];
 Object key = keyDeserializer.deserializeKey(enumValue.name(), ctxt);
 assertEquals(enumValue, key);
 key = keyDeserializer.deserializeKey("invalid", ctxt);
 assertNull(key);
 }
}