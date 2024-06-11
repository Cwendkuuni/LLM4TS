package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class StdKeyDeserializerTest {

 private DeserializationContext ctxt;
 private ObjectMapper mapper;

 @Before
 public void setup() throws Exception {
 ctxt = Mockito.mock(DeserializationContext.class);
 mapper = new ObjectMapper();
 }

 @Test
 public void testBoolean() throws Exception {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);

 assertEquals(Boolean.TRUE, deserializer.deserializeKey("true", ctxt));
 assertEquals(Boolean.FALSE, deserializer.deserializeKey("false", ctxt));
 }

 @Test
 public void testByte() throws Exception {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);

 assertEquals((byte) 127, deserializer.deserializeKey("127", ctxt));
 assertNull(deserializer.deserializeKey("256", ctxt));
 }

 @Test
 public void testShort() throws Exception {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);

 assertEquals((short) 32767, deserializer.deserializeKey("32767", ctxt));
 assertNull(deserializer.deserializeKey("65536", ctxt));
 }

 @Test
 public void testChar() throws Exception {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);

 assertEquals('\u00A9', deserializer.deserializeKey("\u00A9", ctxt));
 assertNull(deserializer.deserializeKey("", ctxt));
 }

 @Test
 public void testInt() throws Exception {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);

 assertEquals(123, deserializer.deserializeKey("123", ctxt));
 assertNull(deserializer.deserializeKey("2147483648", ctxt));
 }

 @Test
 public void testLong() throws Exception {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);

 assertEquals(Long.MAX_VALUE, deserializer.deserializeKey(Long.toString(Long.MAX_VALUE), ctxt));
 assertNull(deserializer.deserializeKey(Long.toString(Long.MAX_VALUE + 1), ctxt));
 }

 @Test
 public void testFloat() throws Exception {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);

 assertEquals(123.5f, deserializer.deserializeKey("123.5", ctxt), 0.001f);
 assertNull(deserializer.deserializeKey("1.2E10", ctxt));
 }

 @Test
 public void testDouble() throws Exception {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);

 assertEquals(123.5, deserializer.deserializeKey("123.5", ctxt), 0.001);
 assertNull(deserializer.deserializeKey("1.2E10", ctxt));
 }

 @Test
 public void testLocale() throws Exception {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);

 AnnotatedClass ac = Mockito.mock(AnnotatedClass.class);
 AnnotatedMethod am = Mockito.mock(AnnotatedMethod.class);
 Mockito.when(ac.findMethod(ArgumentMatchers.anyString(), ArgumentMatchers.eq(Locale.class))).thenReturn(am);
 Mockito.when(am.getParameterTypes()).thenReturn(new Class[] { String.class });
 Mockito.when(am.getDefaultValue()).thenCallRealMethod();
 Mockito.when(am.call1(ArgumentMatchers.any(String.class))).thenCallRealMethod();

 assertEquals(Locale.US, deserializer.deserializeKey("en_US", ctxt));
 assertNull(deserializer.deserializeKey("xyz", ctxt));
 }

 @Test
 public void testDate() throws Exception {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);

 assertEquals(new Date(), deserializer.deserializeKey("", ctxt));
 assertNull(deserializer.deserializeKey("xyz", ctxt));
 }

 @Test
 public void testCalendar() throws Exception {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);

 java.util.Date date = new Date();
 assertEquals(Mockito.mock(Calendar.class), deserializer.deserializeKey(date.toString(), ctxt));
 assertNull(deserializer.deserializeKey("xyz", ctxt));
 }

 @Test
 public void testURI() throws Exception {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);

 assertEquals(new URI("http://example.com"), deserializer.deserializeKey("http://example.com", ctxt));
 assertNull(deserializer.deserializeKey("xyz", ctxt));
 }

 @Test
 public void testURL() throws Exception {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);

 assertEquals(new URL("http://example.com"), deserializer.deserializeKey("http://example.com", ctxt));
 assertNull(deserializer.deserializeKey("xyz", ctxt));
 }

 @Test
 public void testClass() throws Exception {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(String[].class);

 assertEquals(new String[]{"test"}, deserializer.deserializeKey("[\"test\"]", ctxt));
 assertNull(deserializer.deserializeKey("xyz", ctxt));
 }

 @Test
 public void testEnum() throws Exception {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(MyEnum.class);

 assertNull(deserializer.deserializeKey("unknown", ctxt));
 assertEquals(MyEnum.A, deserializer.deserializeKey("A", ctxt));
 }

 @JsonDeserialize(using = StdKeyDeserializer.StringKD.class)
 enum MyEnum {
 A,
 B
 }

 private static class MyEnumKD extends StdKeyDeserializer {
 MyEnumKD(Class<MyEnum> clazz) { super(1, clazz); }
 }
}