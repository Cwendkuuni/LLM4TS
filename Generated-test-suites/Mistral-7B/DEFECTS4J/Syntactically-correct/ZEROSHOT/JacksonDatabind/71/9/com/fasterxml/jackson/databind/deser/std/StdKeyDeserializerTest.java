package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Properties;
import java.util.UUID;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.EnumResolver;

public class StdKeyDeserializerTest {

 private static ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testBoolean() throws Exception {
 StdKeyDeserializer booleanDeserializer = StdKeyDeserializer.forType(Boolean.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "true";
 Object result = booleanDeserializer.deserializeKey(key, ctxt);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testFalseBoolean() throws Exception {
 StdKeyDeserializer booleanDeserializer = StdKeyDeserializer.forType(Boolean.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "false";
 Object result = booleanDeserializer.deserializeKey(key, ctxt);
 assertEquals(Boolean.FALSE, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidBoolean() throws Exception {
 StdKeyDeserializer booleanDeserializer = StdKeyDeserializer.forType(Boolean.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "invalid";
 booleanDeserializer.deserializeKey(key, ctxt);
 }

 @Test
 public void testByte() throws Exception {
 StdKeyDeserializer byteDeserializer = StdKeyDeserializer.forType(Byte.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "50";
 Object result = byteDeserializer.deserializeKey(key, ctxt);
 assertEquals(Byte.valueOf((byte) 50), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidByte() throws Exception {
 StdKeyDeserializer byteDeserializer = StdKeyDeserializer.forType(Byte.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "invalid";
 byteDeserializer.deserializeKey(key, ctxt);
 }

 @Test
 public void testShort() throws Exception {
 StdKeyDeserializer shortDeserializer = StdKeyDeserializer.forType(Short.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "123";
 Object result = shortDeserializer.deserializeKey(key, ctxt);
 assertEquals(Short.valueOf((short) 123), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidShort() throws Exception {
 StdKeyDeserializer shortDeserializer = StdKeyDeserializer.forType(Short.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "invalid";
 shortDeserializer.deserializeKey(key, ctxt);
 }

 @Test
 public void testChar() throws Exception {
 StdKeyDeserializer charDeserializer = StdKeyDeserializer.forType(Character.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "A";
 Object result = charDeserializer.deserializeKey(key, ctxt);
 assertEquals(Character.valueOf('A'), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidChar() throws Exception {
 StdKeyDeserializer charDeserializer = StdKeyDeserializer.forType(Character.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "invalid";
 charDeserializer.deserializeKey(key, ctxt);
 }

 @Test
 public void testInt() throws Exception {
 StdKeyDeserializer intDeserializer = StdKeyDeserializer.forType(Integer.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "1234";
 Object result = intDeserializer.deserializeKey(key, ctxt);
 assertEquals(Integer.valueOf(1234), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidInt() throws Exception {
 StdKeyDeserializer intDeserializer = StdKeyDeserializer.forType(Integer.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "invalid";
 intDeserializer.deserializeKey(key, ctxt);
 }

 @Test
 public void testLong() throws Exception {
 StdKeyDeserializer longDeserializer = StdKeyDeserializer.forType(Long.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "123456789101112131415";
 Object result = longDeserializer.deserializeKey(key, ctxt);
 assertEquals(Long.valueOf(123456789101112131415L), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidLong() throws Exception {
 StdKeyDeserializer longDeserializer = StdKeyDeserializer.forType(Long.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "invalid";
 longDeserializer.deserializeKey(key, ctxt);
 }

 @Test
 public void testFloat() throws Exception {
 StdKeyDeserializer floatDeserializer = StdKeyDeserializer.forType(Float.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "1.23";
 Object result = floatDeserializer.deserializeKey(key, ctxt);
 assertEquals(Float.valueOf(1.23f), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidFloat() throws Exception {
 StdKeyDeserializer floatDeserializer = StdKeyDeserializer.forType(Float.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "invalid";
 floatDeserializer.deserializeKey(key, ctxt);
 }

 @Test
 public void testDouble() throws Exception {
 StdKeyDeserializer doubleDeserializer = StdKeyDeserializer.forType(Double.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "1.234";
 Object result = doubleDeserializer.deserializeKey(key, ctxt);
 assertEquals(Double.valueOf(1.234), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidDouble() throws Exception {
 StdKeyDeserializer doubleDeserializer = StdKeyDeserializer.forType(Double.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "invalid";
 doubleDeserializer.deserializeKey(key, ctxt);
 }

 @Test
 public void testLocale() throws Exception {
 StdKeyDeserializer localeDeserializer = StdKeyDeserializer.forType(Locale.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "en_US";
 Object result = localeDeserializer.deserializeKey(key, ctxt);
 assertEquals(Locale.forLanguageTag(key), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidLocale() throws Exception {
 StdKeyDeserializer localeDeserializer = StdKeyDeserializer.forType(Locale.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "invalid";
 localeDeserializer.deserializeKey(key, ctxt);
 }

 @Test
 public void testDate() throws Exception {
 StdKeyDeserializer dateDeserializer = StdKeyDeserializer.forType(Date.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "2022-03-22";
 Object result = dateDeserializer.deserializeKey(key, ctxt);
 assertEquals(new Date(1647888000000L), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidDate() throws Exception {
 StdKeyDeserializer dateDeserializer = StdKeyDeserializer.forType(Date.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "invalid";
 dateDeserializer.deserializeKey(key, ctxt);
 }

 @Test
 public void testCalendar() throws Exception {
 StdKeyDeserializer calendarDeserializer = StdKeyDeserializer.forType(Calendar.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "2022-03-22";
 Object result = calendarDeserializer.deserializeKey(key, ctxt);
 Calendar calendar = GregorianCalendar.getInstance();
 calendar.setTime(new Date(1647888000000L));
 assertEquals(calendar, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidCalendar() throws Exception {
 StdKeyDeserializer calendarDeserializer = StdKeyDeserializer.forType(Calendar.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "invalid";
 calendarDeserializer.deserializeKey(key, ctxt);
 }

 @Test
 public void testUUID() throws Exception {
 StdKeyDeserializer uuidDeserializer = StdKeyDeserializer.forType(UUID.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "123e4567-e89b-12d3-a456-426614174000";
 Object result = uuidDeserializer.deserializeKey(key, ctxt);
 assertEquals(UUID.fromString(key), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidUUID() throws Exception {
 StdKeyDeserializer uuidDeserializer = StdKeyDeserializer.forType(UUID.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "invalid";
 uuidDeserializer.deserializeKey(key, ctxt);
 }

 @Test
 public void testURI() throws Exception {
 StdKeyDeserializer uriDeserializer = StdKeyDeserializer.forType(URI.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "http://www.example.com/";
 Object result = uriDeserializer.deserializeKey(key, ctxt);
 assertEquals(new URI(key), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidURI() throws Exception {
 StdKeyDeserializer uriDeserializer = StdKeyDeserializer.forType(URI.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "invalid";
 uriDeserializer.deserializeKey(key, ctxt);
 }

 @Test
 public void testURL() throws Exception {
 StdKeyDeserializer urlDeserializer = StdKeyDeserializer.forType(URL.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "http://www.example.com/";
 Object result = urlDeserializer.deserializeKey(key, ctxt);
 assertEquals(new URL(key), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidURL() throws Exception {
 StdKeyDeserializer urlDeserializer = StdKeyDeserializer.forType(URL.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "invalid";
 urlDeserializer.deserializeKey(key, ctxt);
 }

 @Test
 public void testEnum() throws Exception {
 Class<MyEnum> enumClass = MyEnum.class;
 StdKeyDeserializer enumDeserializer = StdKeyDeserializer.forType(enumClass);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "VALUE_1";
 Object result = enumDeserializer.deserializeKey(key, ctxt);
 assertEquals(MyEnum.VALUE_1, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidEnum() throws Exception {
 Class<MyEnum> enumClass = MyEnum.class;
 StdKeyDeserializer enumDeserializer = StdKeyDeserializer.forType(enumClass);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "invalid";
 enumDeserializer.deserializeKey(key, ctxt);
 }

 @Test
 public void testClass() throws Exception {
 Class<?> clazz = String[].class;
 StdKeyDeserializer classDeserializer = StdKeyDeserializer.forType(clazz);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "java.lang.String[]";
 Object result = classDeserializer.deserializeKey(key, ctxt);
 assertEquals(clazz, result.getClass());
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidClass() throws Exception {
 Class<?> clazz = String[].class;
 StdKeyDeserializer classDeserializer = StdKeyDeserializer.forType(clazz);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = "invalid";
 classDeserializer.deserializeKey(key, ctxt);
 }

 @Test
 public void testNull() throws Exception {
 StdKeyDeserializer stringDeserializer = StdKeyDeserializer.forType(String.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = null;
 Object result = stringDeserializer.deserializeKey(key, ctxt);
 assertNull(result);
 }

 @Test
 public void testNullEnum() throws Exception {
 Class<MyEnum> enumClass = MyEnum.class;
 StdKeyDeserializer enumDeserializer = StdKeyDeserializer.forType(enumClass);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultInstance();
 String key = null;
 enumDeserializer.deserializeKey(key, ctxt);
 assertNull(key);
 }
}

class MyEnum {
 @JsonValue
 public String value;

 @JsonCreator
 public static MyEnum VALUE_1 = new MyEnum("VALUE_1");
 public static MyEnum VALUE_2 = new MyEnum("VALUE_2");

 private MyEnum(String value) {
 this.value = value;
 }
}