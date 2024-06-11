package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private DeserializationContext ctxt;
 private ObjectMapper mapper;

 @Before
 public void setup() throws Exception {
 ctxt = new TestContext();
 mapper = new ObjectMapper();
 }

 @Test
 public void testBooleanType() throws JsonProcessingException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
 Object result = deserializer.deserializeKey("true", ctxt);
 assertTrue((Boolean) result);
 result = deserializer.deserializeKey("false", ctxt);
 assertFalse((Boolean) result);
 }

 @Test
 public void testByteType() throws JsonProcessingException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
 Object result = deserializer.deserializeKey("1", ctxt);
 assertEquals((Byte) 1, result);
 result = deserializer.deserializeKey("127", ctxt);
 assertEquals((Byte) 127, result);
 try {
 result = deserializer.deserializeKey("128", ctxt);
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected
 }
 }

 @Test
 public void testShortType() throws JsonProcessingException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
 Object result = deserializer.deserializeKey("1", ctxt);
 assertEquals((Short) 1, result);
 result = deserializer.deserializeKey("32767", ctxt);
 assertEquals((Short) 32767, result);
 try {
 result = deserializer.deserializeKey("32768", ctxt);
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected
 }
 }

 @Test
 public void testCharType() throws JsonProcessingException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
 Object result = deserializer.deserializeKey("A", ctxt);
 assertEquals((Character) 'A', result);
 result = deserializer.deserializeKey("Z", ctxt);
 assertEquals((Character) 'Z', result);
 try {
 result = deserializer.deserializeKey("", ctxt);
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected
 }
 }

 @Test
 public void testIntType() throws JsonProcessingException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
 Object result = deserializer.deserializeKey("1", ctxt);
 assertEquals((Integer) 1, result);
 result = deserializer.deserializeKey("-1", ctxt);
 assertEquals((Integer) -1, result);
 }

 @Test
 public void testLongType() throws JsonProcessingException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
 Object result = deserializer.deserializeKey("1", ctxt);
 assertEquals((Long) 1, result);
 result = deserializer.deserializeKey("-1", ctxt);
 assertEquals((Long) -1, result);
 result = deserializer.deserializeKey("9223372036854775807", ctxt);
 assertEquals((Long) Long.MAX_VALUE, result);
 }

 @Test
 public void testFloatType() throws JsonProcessingException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
 Object result = deserializer.deserializeKey("1.0", ctxt);
 assertEquals((Float) 1.0f, result, 0.001f);
 result = deserializer.deserializeKey("-1.0", ctxt);
 assertEquals((Float) -1.0f, result, 0.001f);
 result = deserializer.deserializeKey("1.23456e-5", ctxt);
 assertEquals((Float) 0.0000123456f, result, 0.0000000000001f);
 }

 @Test
 public void testDoubleType() throws JsonProcessingException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
 Object result = deserializer.deserializeKey("1.0", ctxt);
 assertEquals((Double) 1.0, result, 0.001);
 result = deserializer.deserializeKey("-1.0", ctxt);
 assertEquals((Double) -1.0, result, 0.001);
 result = deserializer.deserializeKey("1.23456e-5", ctxt);
 assertEquals((Double) 0.0000123456, result, 0.0000000001);
 }

 @Test
 public void testLocaleType() throws IOException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
 Object result = deserializer.deserializeKey("en_US", ctxt);
 assertEquals(Locale.US, result);
 result = deserializer.deserializeKey("es_ES", ctxt);
 assertEquals(Locale.SPAIN, result);
 result = deserializer.deserializeKey("fr_CA", ctxt);
 assertEquals(Locale.CANADA, result);
 try {
 result = deserializer.deserializeKey("invalid_locale", ctxt);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testDateType() throws JsonProcessingException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
 Object result = deserializer.deserializeKey("2022-01-01", ctxt);
 assertEquals(new Date(2022, 0, 1), result);
 result = deserializer.deserializeKey("2022-12-31", ctxt);
 assertEquals(new Date(2022, 11, 31), result);
 result = deserializer.deserializeKey("2022-02-29", ctxt); // assuming leap year
 assertEquals(new Date(2022, 1, 29), result);
 }

 @Test
 public void testCalendarType() throws JsonProcessingException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
 Object result = deserializer.deserializeKey("2022-01-01", ctxt);
 Calendar expected = Calendar.getInstance();
 expected.set(2022, 0, 1);
 assertEquals(expected, result);
 result = deserializer.deserializeKey("2022-12-31", ctxt);
 expected.set(2022, 11, 31);
 assertEquals(expected, result);
 result = deserializer.deserializeKey("2022-02-29", ctxt); // assuming leap year
 expected.set(2022, 1, 29);
 assertEquals(expected, result);
 }

 @Test
 public void testUUIDType() throws JsonProcessingException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
 Object result = deserializer.deserializeKey("00000000-0000-0000-0000-000000000000", ctxt);
 assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), result);
 result = deserializer.deserializeKey("11111111-1111-1111-1111-111111111111", ctxt);
 assertEquals(UUID.fromString("11111111-1111-1111-1111-111111111111"), result);
 }

 @Test
 public void testURIType() throws JsonProcessingException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
 Object result = deserializer.deserializeKey("http://example.com", ctxt);
 assertEquals(new URI("http://example.com"), result);
 result = deserializer.deserializeKey("https://example.com", ctxt);
 assertEquals(new URI("https://example.com"), result);
 result = deserializer.deserializeKey("ftp://example.com", ctxt);
 assertEquals(new URI("ftp://example.com"), result);
 }

 @Test
 public void testURLType() throws JsonProcessingException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
 Object result = deserializer.deserializeKey("http://example.com", ctxt);
 assertEquals(new URL("http://example.com"), result);
 result = deserializer.deserializeKey("https://example.com", ctxt);
 assertEquals(new URL("https://example.com"), result);
 result = deserializer.deserializeKey("ftp://example.com", ctxt);
 assertEquals(new URL("ftp://example.com"), result);
 }

 @Test
 public void testClassType() throws JsonProcessingException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
 try {
 result = deserializer.deserializeKey("java.lang.String", ctxt);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 result = deserializer.deserializeKey("java.util.Date", ctxt);
 assertEquals(Date.class, result.getClass());
 result = deserializer.deserializeKey("java.util.Calendar", ctxt);
 assertEquals(Calendar.class, result.getClass());
 result = deserializer.deserializeKey("java.util.UUID", ctxt);
 assertEquals(UUID.class, result.getClass());
 result = deserializer.deserializeKey("java.net.URI", ctxt);
 assertEquals(URI.class, result.getClass());
 result = deserializer.deserializeKey("java.net.URL", ctxt);
 assertEquals(URL.class, result.getClass());
 }

 @Test
 public void testEnumType() throws JsonProcessingException, NoSuchMethodException, IllegalAccessException, InstantiationException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(MyEnum.class);
 Object result = deserializer.deserializeKey("VALUE_1", ctxt);
 assertEquals(MyEnum.VALUE_1, result);
 result = deserializer.deserializeKey("VALUE_2", ctxt);
 assertEquals(MyEnum.VALUE_2, result);
 result = deserializer.deserializeKey("VALUE_3", ctxt);
 assertEquals(MyEnum.VALUE_3, result);
 try {
 result = deserializer.deserializeKey("INVALID_VALUE", ctxt);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testNullKey() throws JsonProcessingException {
 KeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
 Object result = deserializer.deserializeKey(null, ctxt);
 assertNull(result);
 }
}