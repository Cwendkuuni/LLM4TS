package com.example.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

public class StdKeyDeserializerTest {

 @Test
 public void testStdKeyDeserializer() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
 Object key = keyDeserializer.deserializeKey("test", new TestContext());
 assertEquals("test", key);

 keyDeserializer = StdKeyDeserializer.forType(Integer.class);
 int keyInt = (Integer) keyDeserializer.deserializeKey("123", new TestContext());
 assertEquals(123, keyInt);

 keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 Boolean keyBool = (Boolean) keyDeserializer.deserializeKey("true", new TestContext());
 assertTrue(keyBool);

 keyDeserializer = StdKeyDeserializer.forType(Character.class);
 Character keyChar = (Character) keyDeserializer.deserializeKey("a", new TestContext());
 assertEquals('a', keyChar);

 keyDeserializer = StdKeyDeserializer.forType(Short.class);
 Short keyShort = (Short) keyDeserializer.deserializeKey("127", new TestContext());
 assertEquals((short) 127, keyShort);

 keyDeserializer = StdKeyDeserializer.forType(Float.class);
 Float keyFloat = (Float) keyDeserializer.deserializeKey("3.14", new TestContext());
 assertEquals(3.14f, keyFloat.floatValue(), 0.001f);

 keyDeserializer = StdKeyDeserializer.forType(Double.class);
 Double keyDouble = (Double) keyDeserializer.deserializeKey("3.14159265358979323846", new TestContext());
 assertEquals(3.14159265358979323846, keyDouble.doubleValue(), 0.000000000000001);

 keyDeserializer = StdKeyDeserializer.forType(Locale.class);
 Locale keyLocale = (Locale) keyDeserializer.deserializeKey("en_US", new TestContext());
 assertEquals("en_US", keyLocale.toString());

 keyDeserializer = StdKeyDeserializer.forType(Currency.class);
 Currency keyCurrency = (Currency) keyDeserializer.deserializeKey("USD", new TestContext());
 assertEquals("USD", keyCurrency.getCurrencyCode());

 keyDeserializer = StdKeyDeserializer.forType(Date.class);
 Date keyDate = (Date) keyDeserializer.deserializeKey("2022-01-01", new TestContext());
 assertNotNull(keyDate);

 keyDeserializer = StdKeyDeserializer.forType(Calendar.class);
 Calendar keyCalendar = (Calendar) keyDeserializer.deserializeKey("2022-01-01", new TestContext());
 assertNotNull(keyCalendar);

 keyDeserializer = StdKeyDeserializer.forType(URI.class);
 URI keyURI = (URI) keyDeserializer.deserializeKey("http://example.com", new TestContext());
 assertNotNull(keyURI);

 keyDeserializer = StdKeyDeserializer.forType(URL.class);
 URL keyURL = (URL) keyDeserializer.deserializeKey("http://example.com", new TestContext());
 assertNotNull(keyURL);

 keyDeserializer = StdKeyDeserializer.forType(Class.class);
 Class<?> keyClass = (Class<?>) keyDeserializer.deserializeKey("java.lang.String", new TestContext());
 assertEquals(String.class, keyClass);
 }

 @Test(expected = java.lang.IllegalArgumentException.class)
 public void testInvalidType() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Object.class);
 keyDeserializer.deserializeKey("test", new TestContext());
 fail("Should not reach this point");
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidKey() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
 keyDeserializer.deserializeKey("abcd", new TestContext());
 fail("Should not reach this point");
 }

 @Test(expected = IOException.class)
 public void testWeirdKey() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
 keyDeserializer.deserializeKey(null, new TestContext());
 fail("Should not reach this point");
 }

 private static class TestContext extends DeserializationContext {
 // Empty implementation for testing purposes
 }
}