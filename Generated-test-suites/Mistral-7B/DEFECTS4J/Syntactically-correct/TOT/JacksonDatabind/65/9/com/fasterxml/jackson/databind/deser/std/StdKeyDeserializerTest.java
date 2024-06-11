package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
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
import java.util.Locale;
import java.util.Map;

public class StdKeyDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 objectMapper.setSerializationInclusion(JsonMapper.SerializationConfig.Feature.UNSET_NULLS);
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testBooleanType() throws IOException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ctxt.setCurrentValue("true");
 Object key = deserializer.deserializeKey("true", ctxt);
 Assert.assertTrue((Boolean) key);

 ctxt.setCurrentValue("false");
 key = deserializer.deserializeKey("false", ctxt);
 Assert.assertFalse((Boolean) key);
 }

 @Test
 public void testByteType() throws IOException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ctxt.setCurrentValue("0");
 Object key = deserializer.deserializeKey("0", ctxt);
 Assert.assertEquals((Byte) 0, key);

 ctxt.setCurrentValue("127");
 key = deserializer.deserializeKey("127", ctxt);
 Assert.assertEquals((Byte) 127, key);

 ctxt.setCurrentValue("128");
 key = deserializer.deserializeKey("128", ctxt);
 Assert.assertNull(key);
 }

 @Test
 public void testShortType() throws IOException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ctxt.setCurrentValue("0");
 Object key = deserializer.deserializeKey("0", ctxt);
 Assert.assertEquals((Short) 0, key);

 ctxt.setCurrentValue("32767");
 key = deserializer.deserializeKey("32767", ctxt);
 Assert.assertEquals((Short) 32767, key);

 ctxt.setCurrentValue("32768");
 key = deserializer.deserializeKey("32768", ctxt);
 Assert.assertNull(key);
 }

 @Test
 public void testCharType() throws IOException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ctxt.setCurrentValue("A");
 Object key = deserializer.deserializeKey("A", ctxt);
 Assert.assertEquals((Character) 'A', key);
 }

 @Test
 public void testIntType() throws IOException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ctxt.setCurrentValue("0");
 Object key = deserializer.deserializeKey("0", ctxt);
 Assert.assertEquals((Integer) 0, key);

 ctxt.setCurrentValue("123456");
 key = deserializer.deserializeKey("123456", ctxt);
 Assert.assertEquals((Integer) 123456, key);
 }

 @Test
 public void testLongType() throws IOException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ctxt.setCurrentValue("0");
 Object key = deserializer.deserializeKey("0", ctxt);
 Assert.assertEquals((Long) 0L, key);

 ctxt.setCurrentValue("9223372036854775807");
 key = deserializer.deserializeKey("9223372036854775807", ctxt);
 Assert.assertEquals((Long) 9223372036854775807L, key);

 ctxt.setCurrentValue("9223372036854775808");
 key = deserializer.deserializeKey("92233372036854775808", ctxt); // expected failure
 Assert.assertNull(key);
 }

 @Test
 public void testFloatType() throws IOException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ctxt.setCurrentValue("0.0");
 Object key = deserializer.deserializeKey("0.0", ctxt);
 Assert.assertEquals((Float) 0.0f, key);

 ctxt.setCurrentValue("3.14159265");
 key = deserializer.deserializeKey("3.14159265", ctxt);
 Assert.assertEquals((Float) 3.14159265f, key);
 }

 @Test
 public void testDoubleType() throws IOException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ctxt.setCurrentValue("0.0");
 Object key = deserializer.deserializeKey("0.0", ctxt);
 Assert.assertEquals((Double) 0.0d, key);

 ctxt.setCurrentValue("3.14159265");
 key = deserializer.deserializeKey("3.14159265", ctxt);
 Assert.assertEquals((Double) 3.14159265d, key);
 }

 @Test
 public void testLocaleType() throws IOException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ctxt.setCurrentValue("en_US");
 Object key = deserializer.deserializeKey("en_US", ctxt);
 Assert.assertEquals(Locale.US, key);

 ctxt.setCurrentValue("fr_FR");
 key = deserializer.deserializeKey("fr_FR", ctxt);
 Assert.assertEquals(Locale.FRANCE, key);
 }

 @Test
 public void testDateType() throws IOException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ctxt.setCurrentValue("2022-12-31");
 Object key = deserializer.deserializeKey("2022-12-31", ctxt);
 Assert.assertNotNull(key);
 Assert.assertEquals(new Date(2022, 11, 31), key);
 }

 @Test
 public void testCalendarType() throws IOException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ctxt.setCurrentValue("2022-12-31");
 Object key = deserializer.deserializeKey("2022-12-31", ctxt);
 Assert.assertNotNull(key);

 GregorianCalendar gregorianCalendar = new GregorianCalendar(2022, 11, 31);
 Assert.assertEquals(gregorianCalendar, key);
 }

 @Test
 public void testUriType() throws IOException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ctxt.setCurrentValue("https://www.example.com");
 Object key = deserializer.deserializeKey("https://www.example.com", ctxt);
 Assert.assertTrue(key instanceof URI);
 Assert.assertEquals(new URI("https://www.example.com"), key);
 }

 @Test
 public void testUrlType() throws IOException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ctxt.setCurrentValue("http://www.example.com");
 Object key = deserializer.deserializeKey("http://www.example.com", ctxt);
 Assert.assertTrue(key instanceof URL);
 Assert.assertEquals(new URL("http://www.example.com"), key);
 }

 @Test
 public void testEnumType() throws IOException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(EnumSet.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ctxt.setCurrentValue("of(APPLE, ORANGE)");
 Object key = deserializer.deserializeKey("of(APPLE, ORANGE)", ctxt);
 Map<String, Object> enumConstants = EnumSet.class.getDeclaredFields()[0].get(null);
 EnumSet enumSet = EnumSet.valueOf((Enum) enumConstants.get("APPLE"), (Enum) enumConstants.get("ORANGE"));
 Assert.assertEquals(enumSet, key);
 }

 @Test
 public void testClassType() throws IOException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(String[].class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ctxt.setCurrentValue("[Ljava.lang.String;");
 Object key = deserializer.deserializeKey("[Ljava.lang.String;", ctxt);
 String[] stringArray = new String[]{"hello", "world"};
 Assert.assertEquals(Arrays.toString(stringArray), Arrays.toString((Object[]) key));
 }

 @Test
 public void testUnknownType() throws IOException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ctxt.setCurrentValue("unknownType");
 Object key = deserializer.deserializeKey("unknownType", ctxt);
 Assert.assertNull(key);
 }

 @Test
 public void testNullType() throws IOException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ctxt.setCurrentValue(null);
 Object key = deserializer.deserializeKey(null, ctxt);
 Assert.assertNull(key);
 }
}