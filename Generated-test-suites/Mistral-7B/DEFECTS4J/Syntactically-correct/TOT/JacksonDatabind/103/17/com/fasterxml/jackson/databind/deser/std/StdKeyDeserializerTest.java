package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private DeserializationContext ctxt;

 @Before
 public void setUp() throws Exception {
 ctxt = new TestContext();
 }

 @Test
 public void testBooleanKeyDeserializer() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 Object key = keyDeserializer.deserializeKey("true", ctxt);
 assertTrue((Boolean) key == Boolean.TRUE);
 keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 key = keyDeserializer.deserializeKey("false", ctxt);
 assertTrue((Boolean) key == Boolean.FALSE);
 keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 key = keyDeserializer.deserializeKey("invalid", ctxt);
 assertNotNull(key); // should return an error message
 }

 @Test
 public void testByteKeyDeserializer() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
 Object key = keyDeserializer.deserializeKey("127", ctxt);
 assertEquals((Byte) key, Byte.valueOf((byte) 127));
 keyDeserializer = StdKeyDeserializer.forType(Byte.class);
 key = keyDeserializer.deserializeKey("-128", ctxt);
 assertEquals((Byte) key, Byte.valueOf((byte) -128));
 keyDeserializer = StdKeyDeserializer.forType(Byte.class);
 key = keyDeserializer.deserializeKey("256", ctxt);
 assertNotNull(key); // should return an error message
 }

 @Test
 public void testShortKeyDeserializer() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
 Object key = keyDeserializer.deserializeKey("32767", ctxt);
 assertEquals((Short) key, Short.valueOf((short) 32767));
 keyDeserializer = StdKeyDeserializer.forType(Short.class);
 key = keyDeserializer.deserializeKey("-32768", ctxt);
 assertEquals((Short) key, Short.valueOf((short) -32768));
 keyDeserializer = StdKeyDeserializer.forType(Short.class);
 key = keyDeserializer.deserializeKey("65536", ctxt);
 assertNotNull(key); // should return an error message
 }

 @Test
 public void testCharKeyDeserializer() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
 Object key = keyDeserializer.deserializeKey("A", ctxt);
 assertEquals((Character) key, Character.valueOf('A'));
 keyDeserializer = StdKeyDeserializer.forType(Character.class);
 key = keyDeserializer.deserializeKey("\\u0061", ctxt);
 assertEquals((Character) key, Character.valueOf('a'));
 keyDeserializer = StdKeyDeserializer.forType(Character.class);
 key = keyDeserializer.deserializeKey("invalid", ctxt);
 assertNotNull(key); // should return an error message
 }

 @Test
 public void testIntKeyDeserializer() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
 Object key = keyDeserializer.deserializeKey("2147483647", ctxt);
 assertEquals((Integer) key, Integer.valueOf(2147483647));
 keyDeserializer = StdKeyDeserializer.forType(Integer.class);
 key = keyDeserializer.deserializeKey("-2147483648", ctxt);
 assertEquals((Integer) key, Integer.valueOf(-2147483648));
 keyDeserializer = StdKeyDeserializer.forType(Integer.class);
 key = keyDeserializer.deserializeKey("4294967297", ctxt);
 assertNotNull(key); // should return an error message
 }

 @Test
 public void testLongKeyDeserializer() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
 Object key = keyDeserializer.deserializeKey("9223372036854775807", ctxt);
 assertEquals((Long) key, Long.valueOf(9223372036854775807L));
 keyDeserializer = StdKeyDeserializer.forType(Long.class);
 key = keyDeserializer.deserializeKey("-9223372036854775808", ctxt);
 assertEquals((Long) key, Long.valueOf(-9223372036854775808L));
 keyDeserializer = StdKeyDeserializer.forType(Long.class);
 key = keyDeserializer.deserializeKey("18446744073709551616", ctxt);
 assertNotNull(key); // should return an error message
 }

 @Test
 public void testFloatKeyDeserializer() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
 Object key = keyDeserializer.deserializeKey("3.14", ctxt);
 assertEquals((Float) key, Float.valueOf(3.14f));
 keyDeserializer = StdKeyDeserializer.forType(Float.class);
 key = keyDeserializer.deserializeKey("1.79769313486231E38", ctxt);
 assertEquals((Float) key, Float.valueOf(1.79769313486231E38f));
 keyDeserializer = StdKeyDeserializer.forType(Float.class);
 key = keyDeserializer.deserializeKey("invalid", ctxt);
 assertNotNull(key); // should return an error message
 }

 @Test
 public void testDoubleKeyDeserializer() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
 Object key = keyDeserializer.deserializeKey("3.14", ctxt);
 assertEquals((Double) key, Double.valueOf(3.14));
 keyDeserializer = StdKeyDeserializer.forType(Double.class);
 key = keyDeserializer.deserializeKey("1.79769313486231E38", ctxt);
 assertEquals((Double) key, Double.valueOf(1.79769313486231E38));
 keyDeserializer = StdKeyDeserializer.forType(Double.class);
 key = keyDeserializer.deserializeKey("invalid", ctxt);
 assertNotNull(key); // should return an error message
 }

 @Test
 public void testLocaleKeyDeserializer() throws JsonProcessingException, IllegalArgumentException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
 Object key = keyDeserializer.deserializeKey("en_US", ctxt);
 assertEquals(Locale.US, key);
 keyDeserializer = StdKeyDeserializer.forType(Locale.class);
 key = keyDeserializer.deserializeKey("invalid", ctxt);
 assertNotNull(key); // should return an error message
 }

 @Test
 public void testDateKeyDeserializer() throws JsonProcessingException, ParseException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
 Object key = keyDeserializer.deserializeKey("2022-03-01", ctxt);
 assertEquals(new Date(2022, 2, 1), key);
 keyDeserializer = StdKeyDeserializer.forType(Date.class);
 key = keyDeserializer.deserializeKey("invalid", ctxt);
 assertNotNull(key); // should return an error message
 }

 @Test
 public void testCalendarKeyDeserializer() throws JsonProcessingException, ParseException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);
 Object key = keyDeserializer.deserializeKey("2022-03-01", ctxt);
 Calendar calendar = (Calendar) key;
 assertEquals(Calendar.getInstance().get(Calendar.YEAR), calendar.get(Calendar.YEAR));
 assertEquals(Calendar.getInstance().get(Calendar.MONTH), calendar.get(Calendar.MONTH));
 assertEquals(Calendar.getInstance().get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.DAY_OF_MONTH));
 keyDeserializer = StdKeyDeserializer.forType(Calendar.class);
 key = keyDeserializer.deserializeKey("invalid", ctxt);
 assertNotNull(key); // should return an error message
 }

 @Test
 public void testUriKeyDeserializer() throws JsonProcessingException, URISyntaxException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
 Object key = keyDeserializer.deserializeKey("http://example.com", ctxt);
 assertEquals(new URI("http://example.com"), key);
 keyDeserializer = StdKeyDeserializer.forType(URI.class);
 key = keyDeserializer.deserializeKey("invalid", ctxt);
 assertNotNull(key); // should return an error message
 }

 @Test
 public void testUrlKeyDeserializer() throws JsonProcessingException, MalformedURLException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
 Object key = keyDeserializer.deserializeKey("http://example.com", ctxt);
 assertEquals(new URL("http://example.com"), key);
 keyDeserializer = StdKeyDeserializer.forType(URL.class);
 key = keyDeserializer.deserializeKey("invalid", ctxt);
 assertNotNull(key); // should return an error message
 }

 @Test
 public void testClassKeyDeserializer() throws JsonProcessingException, ClassNotFoundException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
 Object key = keyDeserializer.deserializeKey("java.lang.String", ctxt);
 assertEquals(String.class, key);
 keyDeserializer = StdKeyDeserializer.forType(String.class);
 key = keyDeserializer.deserializeKey("java.util.List", ctxt);
 assertNotNull(key); // should return an error message
 }

 @Test
 public void testEnumKeyDeserializer() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(MyEnum.class);
 Object key = keyDeserializer.deserializeKey("MY_ENUM_VALUE", ctxt);
 assertEquals(MyEnum.MY_ENUM_VALUE, key);
 keyDeserializer = StdKeyDeserializer.forType(MyEnum.class);
 key = keyDeserializer.deserializeKey("invalid", ctxt);
 assertNotNull(key); // should return an error message
 }

 @Test
 public void testByteArrayKeyDeserializer() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(byte[].class);
 Object key = keyDeserializer.deserializeKey("RgkVgw==", ctxt);
 assertEquals(new byte[] { 0x52, 0x6b, 0x67, 0x65, 0x56, 0x65, 0x74, 0x57 }, key);
 keyDeserializer = StdKeyDeserializer.forType(byte[].class);
 key = keyDeserializer.deserializeKey("invalid", ctxt);
 assertNotNull(key); // should return an error message
 }

}