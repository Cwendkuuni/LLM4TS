package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.KeyDeserializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@JacksonStdImpl
public class StdKeyDeserializerTest {

 private DeserializationContext ctxt;
 private ObjectMapper mapper;

 @Before
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 ctxt = mapper.getDeserializationContext();
 }

 @Test
 public void testBooleanKeyDeserializer() throws Exception {
 KeyDeserializer kd = StdKeyDeserializer.forType(Boolean.class);
 Object key = kd.deserializeKey("true", ctxt);
 Assert.assertEquals(Boolean.TRUE, key);
 key = kd.deserializeKey("false", ctxt);
 Assert.assertEquals(Boolean.FALSE, key);
 }

 @Test
 public void testByteKeyDeserializer() throws Exception {
 KeyDeserializer kd = StdKeyDeserializer.forType(Byte.class);
 Object key = kd.deserializeKey("127", ctxt);
 Assert.assertEquals((byte) 127, key);
 key = kd.deserializeKey("-128", ctxt);
 Assert.assertEquals((byte) -128, key);
 }

 @Test
 public void testShortKeyDeserializer() throws Exception {
 KeyDeserializer kd = StdKeyDeserializer.forType(Short.class);
 Object key = kd.deserializeKey("32767", ctxt);
 Assert.assertEquals((short) 32767, key);
 key = kd.deserializeKey("-32768", ctxt);
 Assert.assertEquals((short) -32768, key);
 }

 @Test
 public void testCharKeyDeserializer() throws Exception {
 KeyDeserializer kd = StdKeyDeserializer.forType(Character.class);
 Object key = kd.deserializeKey("A", ctxt);
 Assert.assertEquals('A', key);
 }

 @Test
 public void testIntKeyDeserializer() throws Exception {
 KeyDeserializer kd = StdKeyDeserializer.forType(Integer.class);
 Object key = kd.deserializeKey("123456", ctxt);
 Assert.assertEquals(123456, key);
 }

 @Test
 public void testLongKeyDeserializer() throws Exception {
 KeyDeserializer kd = StdKeyDeserializer.forType(Long.class);
 Object key = kd.deserializeKey("9223372036854775807", ctxt);
 Assert.assertEquals(9223372036854775807L, key);
 }

 @Test
 public void testFloatKeyDeserializer() throws Exception {
 KeyDeserializer kd = StdKeyDeserializer.forType(Float.class);
 Object key = kd.deserializeKey("3.14159265", ctxt);
 Assert.assertEquals(3.14159265f, key);
 }

 @Test
 public void testDoubleKeyDeserializer() throws Exception {
 KeyDeserializer kd = StdKeyDeserializer.forType(Double.class);
 Object key = kd.deserializeKey("6.28318530718", ctxt);
 Assert.assertEquals(6.28318530718, key);
 }

 @Test
 public void testLocaleKeyDeserializer() throws Exception {
 KeyDeserializer kd = StdKeyDeserializer.forType(Locale.class);
 Object key = kd.deserializeKey("en_US", ctxt);
 Assert.assertEquals(Locale.US, key);
 }

 @Test
 public void testDateKeyDeserializer() throws Exception {
 KeyDeserializer kd = StdKeyDeserializer.forType(java.util.Date.class);
 Object key = kd.deserializeKey("2023-04-12", ctxt);
 Assert.assertEquals("Thu Apr 12 00:00:00 CEST 2023", key.toString());
 }

 @Test
 public void testCalendarKeyDeserializer() throws Exception {
 KeyDeserializer kd = StdKeyDeserializer.forType(java.util.Calendar.class);
 Object key = kd.deserializeKey("2023-04-12", ctxt);
 Assert.assertEquals(java.util.GregorianCalendar.getInstance(java.util.TimeZone.getTimeZone("CEST")), key);
 }

 @Test
 public void testURIKeyDeserializer() throws Exception {
 KeyDeserializer kd = StdKeyDeserializer.forType(URI.class);
 Object key = kd.deserializeKey("http://example.com", ctxt);
 Assert.assertEquals(new URI("http://example.com"), key);
 }

 @Test
 public void testURLKeyDeserializer() throws Exception {
 KeyDeserializer kd = StdKeyDeserializer.forType(URL.class);
 Object key = kd.deserializeKey("http://example.com", ctxt);
 Assert.assertEquals(new URL("http://example.com"), key);
 }

 @Test
 public void testClassKeyDeserializer() throws Exception {
 KeyDeserializer kd = StdKeyDeserializer.forType(String.class);
 Object key = kd.deserializeKey("java.lang.String", ctxt);
 Assert.assertEquals(String.class, key);
 }

 @Test
 public void testEnumKeyDeserializer() throws Exception {
 EnumSet<MyEnum> expectedValues = EnumSet.allOf(MyEnum.class);
 KeyDeserializer kd = new EnumKD(ctxt.getAnnotationIntrospector().findAnnotation(MyEnum.class),
 getEnumFactoryMethod(MyEnum.class));
 Object key = kd.deserializeKey("FOO", ctxt);
 Assert.assertEquals(expectedValues.stream().filter(e -> e.name().equals("FOO")).findFirst().get(), key);
 }

 @Test
 public void testDelegateKeyDeserializer() throws Exception {
 Class<?> testClass = TestDelegateKeyDeserializer.class;
 KeyDeserializer kd = StdKeyDeserializer.forType(testClass);
 Object key = kd.deserializeKey("test", ctxt);
 Assert.assertEquals(new TestDelegateKeyDeserializer("test"), key);
 }

 private static Method getEnumFactoryMethod(Class<?> enumType) throws Exception {
 return enumType.getDeclaredMethod("values");
 }

 static class MyEnum {
 @ jackson.annotation.JsonValue
 String value();
 String name();
 }

 static class TestDelegateKeyDeserializer {
 private final String name;

 TestDelegateKeyDeserializer(String name) {
 this.name = name;
 }

 @ jackson.annotation.JsonValue
 String value() {
 return name;
 }
 }

}