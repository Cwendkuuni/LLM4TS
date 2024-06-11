package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
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
 public void testBooleanTypeDeserialization() throws Exception {
 StdKeyDeserializer booleanDeserializer = StdKeyDeserializer.forType(Boolean.class);
 Object key = booleanDeserializer.deserializeKey("true", new TestContext());
 assertTrue(key instanceof Boolean);
 assertEquals(Boolean.TRUE, key);

 key = booleanDeserializer.deserializeKey("false", new TestContext());
 assertTrue(key instanceof Boolean);
 assertEquals(Boolean.FALSE, key);
 }

 @Test
 public void testByteTypeDeserialization() throws Exception {
 StdKeyDeserializer byteDeserializer = StdKeyDeserializer.forType(Byte.class);
 Object key = byteDeserializer.deserializeKey("127", new TestContext());
 assertTrue(key instanceof Byte);
 assertEquals((byte) 127, key);

 key = byteDeserializer.deserializeKey("256", new TestContext());
 assertNotNull(key);
 assertFalse(key instanceof Byte);
 }

 @Test
 public void testShortTypeDeserialization() throws Exception {
 StdKeyDeserializer shortDeserializer = StdKeyDeserializer.forType(Short.class);
 Object key = shortDeserializer.deserializeKey("32767", new TestContext());
 assertTrue(key instanceof Short);
 assertEquals((short) 32767, key);

 key = shortDeserializer.deserializeKey("32768", new TestContext());
 assertNotNull(key);
 assertFalse(key instanceof Short);
 }

 @Test
 public void testCharTypeDeserialization() throws Exception {
 StdKeyDeserializer charDeserializer = StdKeyDeserializer.forType(Character.class);
 Object key = charDeserializer.deserializeKey("a", new TestContext());
 assertTrue(key instanceof Character);
 assertEquals('a', key);

 key = charDeserializer.deserializeKey("z", new TestContext());
 assertTrue(key instanceof Character);
 assertEquals('z', key);
 }

 @Test
 public void testIntTypeDeserialization() throws Exception {
 StdKeyDeserializer intDeserializer = StdKeyDeserializer.forType(Integer.class);
 Object key = intDeserializer.deserializeKey("100", new TestContext());
 assertTrue(key instanceof Integer);
 assertEquals(100, key);
 }

 @Test
 public void testLongTypeDeserialization() throws Exception {
 StdKeyDeserializer longDeserializer = StdKeyDeserializer.forType(Long.class);
 Object key = longDeserializer.deserializeKey("1000000", new TestContext());
 assertTrue(key instanceof Long);
 assertEquals(1000000L, key);
 }

 @Test
 public void testFloatTypeDeserialization() throws Exception {
 StdKeyDeserializer floatDeserializer = StdKeyDeserializer.forType(Float.class);
 Object key = floatDeserializer.deserializeKey("1.23", new TestContext());
 assertTrue(key instanceof Float);
 assertEquals(1.23f, key, 0.001f);
 }

 @Test
 public void testDoubleTypeDeserialization() throws Exception {
 StdKeyDeserializer doubleDeserializer = StdKeyDeserializer.forType(Double.class);
 Object key = doubleDeserializer.deserializeKey("3.14", new TestContext());
 assertTrue(key instanceof Double);
 assertEquals(3.14, key, 0.001);
 }

 @Test
 public void testLocaleTypeDeserialization() throws Exception {
 StdKeyDeserializer localeDeserializer = StdKeyDeserializer.forType(Locale.class);
 Object key = localeDeserializer.deserializeKey("en_US", new TestContext());
 assertTrue(key instanceof Locale);
 assertEquals(Locale.US, key);
 }

 @Test
 public void testURLTypeDeserialization() throws Exception {
 StdKeyDeserializer urlDeserializer = StdKeyDeserializer.forType(URL.class);
 Object key = urlDeserializer.deserializeKey("http://www.example.com", new TestContext());
 assertTrue(key instanceof URL);
 assertEquals("http://www.example.com", key.toString());
 }

 @Test
 public void testURITypeDeserialization() throws Exception {
 StdKeyDeserializer uriDeserializer = StdKeyDeserializer.forType(URI.class);
 Object key = uriDeserializer.deserializeKey("http://www.example.com", new TestContext());
 assertTrue(key instanceof URI);
 assertEquals("http://www.example.com", key.toString());
 }

 @Test
 public void testClassTypeDeserialization() throws Exception {
 StdKeyDeserializer classDeserializer = StdKeyDeserializer.forType(String.class);
 Object key = classDeserializer.deserializeKey("java.lang.String", new TestContext());
 assertTrue(key instanceof Class<?>);
 assertEquals(String.class, key);
 }

 @Test
 public void testEnumTypeDeserialization() throws Exception {
 StdKeyDeserializer enumDeserializer = StdKeyDeserializer.forType(MyEnum.class);
 Object key = enumDeserializer.deserializeKey("VALUE_B", new TestContext());
 assertTrue(key instanceof MyEnum);
 assertEquals(MyEnum.VALUE_B, key);
 }

 @Test(expected = IOException.class)
 public void testUnknownTypeDeserialization() throws Exception {
 StdKeyDeserializer unknownDeserializer = StdKeyDeserializer.forType(Object.class);
 Object key = unknownDeserializer.deserializeKey("Xyz", new TestContext());
 fail("Unexpected non-null key: " + key);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidValueDeserialization() throws Exception {
 StdKeyDeserializer boolDeserializer = StdKeyDeserializer.forType(Boolean.class);
 Object key = boolDeserializer.deserializeKey("invalid", new TestContext());
 fail("Unexpected non-null key: " + key);
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidNumberDeserialization() throws Exception {
 StdKeyDeserializer intDeserializer = StdKeyDeserializer.forType(Integer.class);
 Object key = intDeserializer.deserializeKey("invalidNumber", new TestContext());
 fail("Unexpected non-null key: " + key);
 }

 @Test(expected = MalformedURLException.class)
 public void testInvalidURLDeserialization() throws Exception {
 StdKeyDeserializer urlDeserializer = StdKeyDeserializer.forType(URL.class);
 Object key = urlDeserializer.deserializeKey("invalidURL", new TestContext());
 fail("Unexpected non-null key: " + key);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullKey() throws Exception {
 StdKeyDeserializer nullDeserializer = StdKeyDeserializer.forType(Object.class);
 Object key = nullDeserializer.deserializeKey(null, new TestContext());
 assertNull(key);
 }

 @Test(expected = JsonProcessingException.class)
 public void testUnexpectedJsonProcessingException() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 String json = "{\"key\": \"invalidJson\"}";
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Object.class);
 Object key = mapper.readTree(json).path("key").getValueAsString();
 Object result = deserializer.deserializeKey(key, new TestContext());
 fail("Unexpected result: " + result);
 }

 private static class TestContext extends DeserializationContext {
 @Override
 public ObjectMapper getMapper() { return null; }

 @Override
 public JavaType getTypeFactory() { return null; }

 @Override
 public BeanDescription findBeanDescription(JavaType type) { return null; }

 @Override
 public Object createContextualObjectInstance(Class<?> beanDescription, JavaType beanType) throws InstantiationException, IllegalAccessException { return null; }

 @Override
 public Object _valueInstantiator() { return null; }

 @Override
 public void reportBadDefinition(String msg, Object data) { fail(msg); }
 }
}