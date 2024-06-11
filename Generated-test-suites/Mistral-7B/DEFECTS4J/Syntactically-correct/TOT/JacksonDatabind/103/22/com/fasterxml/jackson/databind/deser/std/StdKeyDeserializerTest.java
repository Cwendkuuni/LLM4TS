package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private DeserializationContext ctxt;

 @Before
 public void setUp() {
 ctxt = new TestDeserializationContext();
 }

 @Test
 public void testBooleanType() throws JsonProcessingException {
 Object key = StdKeyDeserializer.forType(Boolean.class).deserializeKey("true", ctxt);
 assertTrue(key instanceof Boolean);
 assertEquals(true, ((Boolean) key).booleanValue());

 key = StdKeyDeserializer.forType(Boolean.class).deserializeKey("false", ctxt);
 assertTrue(key instanceof Boolean);
 assertEquals(false, ((Boolean) key).booleanValue());

 key = StdKeyDeserializer.forType(Boolean.class).deserializeKey("invalid", ctxt);
 assertNull(key);
 }

 @Test
 public void testByteType() throws JsonProcessingException {
 Object key = StdKeyDeserializer.forType(Byte.class).deserializeKey("0", ctxt);
 assertTrue(key instanceof Byte);
 assertEquals((byte) 0, ((Byte) key).byteValue());

 key = StdKeyDeserializer.forType(Byte.class).deserializeKey("-127", ctxt);
 assertTrue(key instanceof Byte);
 assertEquals((byte) -127, ((Byte) key).byteValue());

 key = StdKeyDeserializer.forType(Byte.class).deserializeKey("128", ctxt);
 assertNull(key); // Java byte is an 8-bit signed type, so this value is out of bounds
 }

 @Test
 public void testShortType() throws JsonProcessingException {
 Object key = StdKeyDeserializer.forType(Short.class).deserializeKey("0", ctxt);
 assertTrue(key instanceof Short);
 assertEquals((short) 0, ((Short) key).shortValue());

 key = StdKeyDeserializer.forType(Short.class).deserializeKey("-32767", ctxt);
 assertTrue(key instanceof Short);
 assertEquals((short) -32767, ((Short) key).shortValue());

 key = StdKeyDeserializer.forType(Short.class).deserializeKey("32768", ctxt);
 assertNull(key); // Java short is a 16-bit signed type, so this value is out of bounds
 }

 @Test
 public void testCharType() throws JsonProcessingException {
 Object key = StdKeyDeserializer.forType(Character.class).deserializeKey("'a'", ctxt);
 assertTrue(key instanceof Character);
 assertEquals('a', ((Character) key).charValue());

 key = StdKeyDeserializer.forType(Character.class).deserializeKey("'ö'", ctxt);
 assertTrue(key instanceof Character);
 assertEquals('ö', ((Character) key).charValue());

 key = StdKeyDeserializer.forType(Character.class).deserializeKey("invalid", ctxt);
 assertNull(key);
 }

 @Test
 public void testIntType() throws JsonProcessingException {
 Object key = StdKeyDeserializer.forType(Integer.class).deserializeKey("0", ctxt);
 assertTrue(key instanceof Integer);
 assertEquals(0, ((Integer) key).intValue());

 key = StdKeyDeserializer.forType(Integer.class).deserializeKey("-2147483647", ctxt);
 assertTrue(key instanceof Integer);
 assertEquals(-2147483647, ((Integer) key).intValue());

 key = StdKeyDeserializer.forType(Integer.class).deserializeKey("2147483648", ctxt);
 assertNull(key); // Java int is a 32-bit signed type, so this value is out of bounds
 }

 @Test
 public void testLongType() throws JsonProcessingException {
 Object key = StdKeyDeserializer.forType(Long.class).deserializeKey("0", ctxt);
 assertTrue(key instanceof Long);
 assertEquals(0L, ((Long) key).longValue());

 key = StdKeyDeserializer.forType(Long.class).deserializeKey("-9223372036854775807", ctxt);
 assertTrue(key instanceof Long);
 assertEquals(-9223372036854775807L, ((Long) key).longValue());

 key = StdKeyDeserializer.forType(Long.class).deserializeKey("9223372036854775808", ctxt);
 assertNull(key); // Java long is a 64-bit signed type, so this value is out of bounds
 }

 @Test
 public void testFloatType() throws JsonProcessingException {
 Object key = StdKeyDeserializer.forType(Float.class).deserializeKey("0.0", ctxt);
 assertTrue(key instanceof Float);
 assertEquals(0.0f, ((Float) key).floatValue(), 0.001f);

 key = StdKeyDeserializer.forType(Float.class).deserializeKey("-3.14159265358979323846", ctxt);
 assertTrue(key instanceof Float);
 assertEquals(-3.14159265358979323846f, ((Float) key).floatValue(), 0.0000001f);

 key = StdKeyDeserializer.forType(Float.class).deserializeKey("NaN", ctxt);
 assertTrue(key instanceof Float);
 assertEquals(Float.NaN, ((Float) key).floatValue());

 key = StdKeyDeserializer.forType(Float.class).deserializeKey("inf", ctxt);
 assertTrue(key instanceof Float);
 assertEquals(Float.POSITIVE_INFINITY, ((Float) key).floatValue());

 key = StdKeyDeserializer.forType(Float.class).deserializeKey("Invalid", ctxt);
 assertNull(key);
 }

 @Test
 public void testDoubleType() throws JsonProcessingException {
 Object key = StdKeyDeserializer.forType(Double.class).deserializeKey("0.0", ctxt);
 assertTrue(key instanceof Double);
 assertEquals(0.0, ((Double) key).doubleValue(), 0.0000001);

 key = StdKeyDeserializer.forType(Double.class).deserializeKey("-1.79769313486231E308", ctxt);
 assertTrue(key instanceof Double);
 assertEquals(-1.79769313486231E308, ((Double) key).doubleValue(), 0.000000000000001);

 key = StdKeyDeserializer.forType(Double.class).deserializeKey("1.79769313486231E308", ctxt);
 assertNull(key); // Java double is a 64-bit signed type, so this value is out of bounds

 key = StdKeyDeserializer.forType(Double.class).deserializeKey("NaN", ctxt);
 assertTrue(key instanceof Double);
 assertEquals(Double.NaN, ((Double) key).doubleValue());

 key = StdKeyDeserializer.forType(Double.class).deserializeKey("inf", ctxt);
 assertTrue(key instanceof Double);
 assertEquals(Double.POSITIVE_INFINITY, ((Double) key).doubleValue());

 key = StdKeyDeserializer.forType(Double.class).deserializeKey("Invalid", ctxt);
 assertNull(key);
 }

 @Test
 public void testLocaleType() throws JsonProcessingException {
 Object key = StdKeyDeserializer.forType(Locale.class).deserializeKey("en_US", ctxt);
 assertTrue(key instanceof Locale);
 assertEquals(Locale.US, (Locale) key);

 key = StdKeyDeserializer.forType(Locale.class).deserializeKey("fr_FR", ctxt);
 assertTrue(key instanceof Locale);
 assertEquals(Locale.FRENCH, (Locale) key);

 key = StdKeyDeserializer.forType(Locale.class).deserializeKey("es_ES", ctxt);
 assertTrue(key instanceof Locale);
 assertEquals(Locale.SPAIN, (Locale) key);

 key = StdKeyDeserializer.forType(Locale.class).deserializeKey("invalid", ctxt);
 assertNull(key);
 }

 @Test
 public void testDateType() throws JsonProcessingException, ParseException {
 Object key = StdKeyDeserializer.forType(Date.class).deserializeKey("2018-12-31", ctxt);
 assertTrue(key instanceof Date);
 assertEquals(new Date(1546348000000L), key);

 key = StdKeyDeserializer.forType(Date.class).deserializeKey("2018-12-31T23:59:59.999Z", ctxt);
 assertTrue(key instanceof Date);
 assertEquals(new Date(1546348799999L), key);

 key = StdKeyDeserializer.forType(Date.class).deserializeKey("Invalid", ctxt);
 assertNull(key);
 }

 @Test
 public void testCalendarType() throws JsonProcessingException, ParseException {
 Object key = StdKeyDeserializer.forType(Calendar.class).deserializeKey("2018-12-31", ctxt);
 assertTrue(key instanceof Calendar);
 assertEquals(Calendar.getInstance().getTime(), key.getTime());

 key = StdKeyDeserializer.forType(Calendar.class).deserializeKey("2018-12-31T23:59:59.999Z", ctxt);
 assertTrue(key instanceof Calendar);
 assertEquals(Calendar.getInstance().getTime(), key.getTime());

 key = StdKeyDeserializer.forType(Calendar.class).deserializeKey("Invalid", ctxt);
 assertNull(key);
 }

 @Test
 public void testURIType() throws JsonProcessingException, URISyntaxException {
 Object key = StdKeyDeserializer.forType(URI.class).deserializeKey("http://example.com", ctxt);
 assertTrue(key instanceof URI);
 assertEquals("http://example.com", key.toString());

 key = StdKeyDeserializer.forType(URI.class).deserializeKey("https://example.com/path/to/resource", ctxt);
 assertTrue(key instanceof URI);
 assertEquals("https://example.com/path/to/resource", key.toString());

 key = StdKeyDeserializer.forType(URI.class).deserializeKey("Invalid", ctxt);
 assertNull(key);
 }

 @Test
 public void testURLType() throws JsonProcessingException, MalformedURLException {
 Object key = StdKeyDeserializer.forType(URL.class).deserializeKey("http://example.com", ctxt);
 assertTrue(key instanceof URL);
 assertEquals("http://example.com", key.toString());

 key = StdKeyDeserializer.forType(URL.class).deserializeKey("https://example.com/path/to/resource", ctxt);
 assertTrue(key instanceof URL);
 assertEquals("https://example.com/path/to/resource", key.toString());

 key = StdKeyDeserializer.forType(URL.class).deserializeKey("Invalid", ctxt);
 assertNull(key);
 }

 @Test
 public void testClassType() throws JsonProcessingException, IllegalAccessException, InstantiationException {
 Class<?> keyClass = String[].class;
 Object key = StdKeyDeserializer.forType(keyClass).deserializeKey("[Ljava.lang.String;", ctxt);
 assertTrue(key instanceof Object[]);
 assertEquals(keyClass, key.getClass());
 assertArrayEquals(new String[0], (Object[]) key);

 key = StdKeyDeserializer.forType(String[].class).deserializeKey("[Ljava.lang.String;", ctxt);
 assertTrue(key instanceof String[]);
 assertEquals(keyClass, key.getClass());
 assertArrayEquals(new String[0], (String[]) key);

 key = StdKeyDeserializer.forType(Enum.class).deserializeKey("Invalid", ctxt);
 assertNull(key);
 }

 @Test
 public void testEnumType() throws JsonProcessingException {
 Enum enumType = MyEnum.FIRST;
 Object key = StdKeyDeserializer.forType(MyEnum.class).deserializeKey(enumType.name(), ctxt);
 assertTrue(key instanceof MyEnum);
 assertEquals(enumType, key);

 EnumResolver resolver = new EnumResolver(MyEnum.class);
 Enum enumValue = resolver.getDefaultValue();
 key = StdKeyDeserializer.forType(MyEnum.class).deserializeKey(enumValue.name(), ctxt);
 assertTrue(key instanceof MyEnum);
 assertEquals(enumValue, key);

 key = StdKeyDeserializer.forType(MyEnum.class).deserializeKey("Invalid", ctxt);
 assertNull(key);
 }

 @Test
 public void testByteArrayType() throws JsonProcessingException {
 Object key = StdKeyDeserializer.forType(byte[].class).deserializeKey("base64EncodedValue", ctxt);
 byte[] expected = "base64EncodedValue".getBytes();
 assertTrue(key instanceof byte[]);
 assertArrayEquals(expected, (byte[]) key);
 }
}