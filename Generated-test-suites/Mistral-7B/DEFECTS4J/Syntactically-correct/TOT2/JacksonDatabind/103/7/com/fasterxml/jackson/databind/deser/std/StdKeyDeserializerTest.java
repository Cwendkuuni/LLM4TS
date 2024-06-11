package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.ObjectIdGenerator;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

public class StdKeyDeserializerTest {

 private ObjectMapper mapper;

 @Before
 public void setUp() {
 mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testBooleanTypeDeserializer() throws IOException {
 StdKeyDeserializer booleanDeserializer = StdKeyDeserializer.forType(Boolean.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = booleanDeserializer.deserializeKey("true", ctxt);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 result = booleanDeserializer.deserializeKey("false", ctxt);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testByteTypeDeserializer() throws IOException {
 StdKeyDeserializer byteDeserializer = StdKeyDeserializer.forType(Byte.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = byteDeserializer.deserializeKey("127", ctxt);
 assertTrue(result instanceof Byte);
 assertEquals((Byte) Byte.parseByte("127"), result);
 result = byteDeserializer.deserializeKey("-128", ctxt);
 assertTrue(result instanceof Byte);
 assertEquals((Byte) Byte.parseByte("-128"), result);
 result = byteDeserializer.deserializeKey("129", ctxt);
 assertArrayEquals(new byte[] { (byte) 129 }, result.getClass().getComponentType().getArray(), (Object[]) result);
 }

 @Test
 public void testShortTypeDeserializer() throws IOException {
 StdKeyDeserializer shortDeserializer = StdKeyDeserializer.forType(Short.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = shortDeserializer.deserializeKey("32767", ctxt);
 assertTrue(result instanceof Short);
 assertEquals((Short) Short.parseShort("32767"), result);
 result = shortDeserializer.deserializeKey("-32768", ctxt);
 assertTrue(result instanceof Short);
 assertEquals((Short) Short.parseShort("-32768"), result);
 result = shortDeserializer.deserializeKey("32768", ctxt);
 assertArrayEquals(new short[] { 32768 }, result.getClass().getComponentType().getArray(), (Object[]) result);
 }

 @Test
 public void testCharTypeDeserializer() throws IOException {
 StdKeyDeserializer charDeserializer = StdKeyDeserializer.forType(Character.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = charDeserializer.deserializeKey("'A'", ctxt);
 assertEquals(Character.valueOf('A'), result);
 result = charDeserializer.deserializeKey("'\\u0001'", ctxt);
 assertEquals(Character.valueOf('\u0001'), result);
 result = charDeserializer.deserializeKey("'\\u41C6'", ctxt);
 assertEquals(Character.valueOf('\u41C6'), result);
 }

 @Test
 public void testIntTypeDeserializer() throws IOException {
 StdKeyDeserializer intDeserializer = StdKeyDeserializer.forType(Integer.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = intDeserializer.deserializeKey("2147483647", ctxt);
 assertTrue(result instanceof Integer);
 assertEquals((Integer) Integer.MAX_VALUE, result);
 result = intDeserializer.deserializeKey("-2147483648", ctxt);
 assertTrue(result instanceof Integer);
 assertEquals((Integer) Integer.MIN_VALUE, result);
 }

 @Test
 public void testLongTypeDeserializer() throws IOException {
 StdKeyDeserializer longDeserializer = StdKeyDeserializer.forType(Long.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = longDeserializer.deserializeKey("9223372036854775807", ctxt);
 assertTrue(result instanceof Long);
 assertEquals((Long) Long.MAX_VALUE, result);
 result = longDeserializer.deserializeKey("-9223372036854775808", ctxt);
 assertTrue(result instanceof Long);
 assertEquals((Long) Long.MIN_VALUE, result);
 result = longDeserializer.deserializeKey("9223372036854775808", ctxt);
 assertArrayEquals(new long[] { 9223372036854775808L }, result.getClass().getComponentType().getArray(), (Object[]) result);
 }

 @Test
 public void testFloatTypeDeserializer() throws IOException {
 StdKeyDeserializer floatDeserializer = StdKeyDeserializer.forType(Float.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = floatDeserializer.deserializeKey("3.14159265", ctxt);
 assertTrue(result instanceof Float);
 assertEquals(Float.valueOf(3.14159265f), result);
 }

 @Test
 public void testDoubleTypeDeserializer() throws IOException {
 StdKeyDeserializer doubleDeserializer = StdKeyDeserializer.forType(Double.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = doubleDeserializer.deserializeKey("3.14159265358979323846", ctxt);
 assertTrue(result instanceof Double);
 assertEquals(Double.valueOf(3.14159265358979323846), result);
 }

 @Test
 public void testLocaleTypeDeserializer() throws IOException {
 StdKeyDeserializer localeDeserializer = StdKeyDeserializer.forType(Locale.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = localeDeserializer.deserializeKey("en_US", ctxt);
 assertEquals(Locale.US, result);
 result = localeDeserializer.deserializeKey("es_ES", ctxt);
 assertEquals(Locale.SPAIN, result);
 result = localeDeserializer.deserializeKey("de_DE@calendars/Gregorian", ctxt);
 assertEquals(new Locale("de", "DE", new Locale.LanguageTag("de"), "", new Locale.Variant("calendars/Gregorian")), result);
 }

 @Test
 public void testDateTypeDeserializer() throws IOException {
 StdKeyDeserializer dateDeserializer = StdKeyDeserializer.forType(Date.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = dateDeserializer.deserializeKey("2022-12-31T23:59:59.999Z", ctxt);
 assertTrue(result instanceof Date);
 assertEquals(new Date(1672502399999L), result);
 result = dateDeserializer.deserializeKey("2022-12-31T23:59:59.999-08:00", ctxt);
 assertEquals(new Date(1672502399999L - 8 * 60 * 60 * 1000), result);
 result = dateDeserializer.deserializeKey("2022-12-31", ctxt);
 assertEquals(new Date(1672502399999L), result);
 }

 @Test
 public void testCalendarTypeDeserializer() throws IOException {
 StdKeyDeserializer calendarDeserializer = StdKeyDeserializer.forType(Calendar.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = calendarDeserializer.deserializeKey("2022-12-31", ctxt);
 assertTrue(result instanceof Calendar);
 assertEquals(Calendar.getInstance(), result);
 result = calendarDeserializer.deserializeKey("2022-12-31T23:59:59.999Z", ctxt);
 assertEquals(Calendar.getInstance().setTime(new Date(1672502399999L)), result);
 result = calendarDeserializer.deserializeKey("2022-12-31T23:59:59.999-08:00", ctxt);
 assertEquals(Calendar.getInstance().setTime(new Date(1672502399999L - 8 * 60 * 60 * 1000)), result);
 }

 @Test
 public void testURITypeDeserializer() throws IOException {
 StdKeyDeserializer uriDeserializer = StdKeyDeserializer.forType(URI.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = uriDeserializer.deserializeKey("https://www.example.com", ctxt);
 assertEquals(new URI("https", "www.example.com", null), result);
 result = uriDeserializer.deserializeKey("http://example.com:8080/test/path?query1=value1&query2=value2#fragment", ctxt);
 assertEquals(new URI("http", "example.com", null, 8080, "/test/path", new HashMap<String, String>() {
 {
 put("query1", "value1");
 put("query2", "value2");
 }
 }, "fragment"), result);
 }

 @Test
 public void testURLTypeDeserializer() throws IOException {
 StdKeyDeserializer urlDeserializer = StdKeyDeserializer.forType(URL.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = urlDeserializer.deserializeKey("https://www.example.com", ctxt);
 assertEquals(new URL("https", "www.example.com", null), result);
 result = urlDeserializer.deserializeKey("http://example.com:8080/test/path?query1=value1&query2=value2#fragment", ctxt);
 assertEquals(new URL("http", "example.com", 8080, "/test/path", new HashMap<String, String>() {
 {
 put("query1", "value1");
 put("query2", "value2");
 }
 }, "fragment"), result);
 }

 @Test
 public void testEnumTypeDeserializer() throws IOException {
 enum MyEnum { ONE, TWO, THREE }
 StdKeyDeserializer enumDeserializer = StdKeyDeserializer.forType(MyEnum.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = enumDeserializer.deserializeKey("ONE", ctxt);
 assertEquals(MyEnum.ONE, result);
 result = enumDeserializer.deserializeKey("TWO", ctxt);
 assertEquals(MyEnum.TWO, result);
 result = enumDeserializer.deserializeKey("THREE", ctxt);
 assertEquals(MyEnum.THREE, result);
 result = enumDeserializer.deserializeKey("FOO", ctxt);
 assertNull(result);
 }

 @Test
 public void testCustomTypeDeserializer() throws IOException {
 static class CustomType {
 private final String name;

 CustomType(String name) {
 this.name = name;
 }

 @JsonDeserialize(using = CustomTypeDeserializer.class)
 public String getName() {
 return name;
 }
 }

 static class CustomTypeDeserializer extends StdKeyDeserializer {
 CustomTypeDeserializer() {
 super(TypeSerializer.empty, null, null, null);
 }

 @Override
 public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
 return new CustomType(key);
 }
 }

 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = StdKeyDeserializer.forType(CustomType.class).deserializeKey("CustomTypeName", ctxt);
 assertEquals("CustomTypeName", ((CustomType) result).getName());
 }

 @Test
 public void testNullValueProvider() throws IOException {
 StdKeyDeserializer nullValueProviderDeserializer = StdKeyDeserializer.forType(String.class, new NullValueProvider() {
 @Override
 public Object getNullValue(DeserializationContext ctxt) {
 return null;
 }
 });
 Object result = nullValueProviderDeserializer.deserializeKey(null, mapper.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testTypeDeserializer() throws IOException {
 ObjectMapper mapperWithTypeDeserializer = new ObjectMapper();
 mapperWithTypeDeserializer.registerModule(new SimpleModule() {
 {
 addDeserializer(Date.class, new StdDateDeserializer());
 }
 });
 StdKeyDeserializer typeDeserializer = StdKeyDeserializer.forType(Date.class, mapperWithTypeDeserializer.getDeserializationContext());
 Object result = typeDeserializer.deserializeKey("2022-12-31T23:59:59.999Z", mapperWithTypeDeserializer.getDeserializationContext());
 assertEquals(new Date(1672502399999L), result);
 }
}