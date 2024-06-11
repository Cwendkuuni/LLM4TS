package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Test;

public class StdKeyDeserializerTest {

 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testStringKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 String key = "hello";
 Object deserialized = mapper.readValue("{\"key\":\"" + key + "\"}", String.class, StdKeyDeserializer.StringKD.class);
 Assert.assertEquals(key, deserialized);
 }

 @Test
 public void testNullKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 String key = null;
 Object deserialized = mapper.readValue("{\"key\":null}", String.class, StdKeyDeserializer.StringKD.class);
 Assert.assertNull(deserialized);
 }

 @Test
 public void testBooleanKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 String key = "true";
 Object deserialized = mapper.readValue("{\"key\":\"" + key + "\"}", Boolean.class, StdKeyDeserializer.class);
 Assert.assertTrue((Boolean) deserialized);

 key = "false";
 deserialized = mapper.readValue("{\"key\":\"" + key + "\"}", Boolean.class, StdKeyDeserializer.class);
 Assert.assertFalse((Boolean) deserialized);
 }

 @Test
 public void testByteKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 String key = "127";
 Object deserialized = mapper.readValue("{\"key\":\"" + key + "\"}", Byte.class, StdKeyDeserializer.class);
 Assert.assertEquals((byte) 127, deserialized);

 key = "-128";
 deserialized = mapper.readValue("{\"key\":\"" + key + "\"}", Byte.class, StdKeyDeserializer.class);
 Assert.assertEquals((byte) -128, deserialized);
 }

 @Test
 public void testShortKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 String key = "32767";
 Object deserialized = mapper.readValue("{\"key\":\"" + key + "\"}", Short.class, StdKeyDeserializer.class);
 Assert.assertEquals((short) 32767, deserialized);

 key = "-32768";
 deserialized = mapper.readValue("{\"key\":\"" + key + "\"}", Short.class, StdKeyDeserializer.class);
 Assert.assertEquals((short) -32768, deserialized);
 }

 @Test
 public void testCharKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 String key = "A";
 Object deserialized = mapper.readValue("{\"key\":\"" + key + "\"}", Character.class, StdKeyDeserializer.class);
 Assert.assertEquals('A', deserialized);
 }

 @Test
 public void testIntKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 String key = "42";
 Object deserialized = mapper.readValue("{\"key\":\"" + key + "\"}", Integer.class, StdKeyDeserializer.class);
 Assert.assertEquals(42, deserialized);
 }

 @Test
 public void testLongKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 String key = "1234567890123456789";
 Object deserialized = mapper.readValue("{\"key\":\"" + key + "\"}", Long.class, StdKeyDeserializer.class);
 Assert.assertEquals(1234567890123456789L, deserialized);
 }

 @Test
 public void testFloatKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 String key = "3.14";
 Object deserialized = mapper.readValue("{\"key\":\"" + key + "\"}", Float.class, StdKeyDeserializer.class);
 Assert.assertEquals(3.14f, deserialized);
 }

 @Test
 public void testDoubleKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 String key = "3.14159265359";
 Object deserialized = mapper.readValue("{\"key\":\"" + key + "\"}", Double.class, StdKeyDeserializer.class);
 Assert.assertEquals(3.14159265359, deserialized);
 }

 @Test
 public void testLocaleKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 ObjectMapper localizedMapper = new ObjectMapper();
 localizedMapper.setLocale(Locale.FRENCH);
 String key = "fr_FR";
 Object deserialized = localizedMapper.readValue("{\"key\":\"" + key + "\"}", Locale.class, StdKeyDeserializer.LocaleKD.class);
 Assert.assertEquals(Locale.FRENCH, deserialized);
 }

 @Test
 public void testDateKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 String key = "2022-01-01";
 Object deserialized = mapper.readValue("{\"key\":\"" + key + "\"}", Date.class, StdKeyDeserializer.class);
 Assert.assertEquals("Mon Jan 03 00:00:00 GMT+01:00 2022", deserialized.toString());
 }

 @Test
 public void testCalendarKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 String key = "2022-01-01";
 Object deserialized = mapper.readValue("{\"key\":\"" + key + "\"}", Calendar.class, StdKeyDeserializer.CalendarKD.class);
 // Assert.assertEquals(expected, deserialized); // Unable to write assertion for Calendar
 }

 @Test
 public void testEnumKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 ObjectMapper enumMapper = new ObjectMapper();
 String key = "DAY_NAME_FRIDAY";
 Enum enumConstant = Enum.valueOf(DayOfWeek.class, key);
 Object deserialized = enumMapper.readValue("{\"key\":\"" + key + "\"}", DayOfWeek.class, StdKeyDeserializer.EnumKD.class);
 Assert.assertEquals(enumConstant, deserialized);
 }

 @Test
 public void testUriKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 String key = "https://www.example.com";
 Object deserialized = mapper.readValue("{\"key\":\"" + key + "\"}", URI.class, StdKeyDeserializer.class);
 Assert.assertEquals(new URI(key), deserialized);
 }

 @Test
 public void testUrlKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 String key = "https://www.example.com";
 Object deserialized = mapper.readValue("{\"key\":\"" + key + "\"}", URL.class, StdKeyDeserializer.class);
 Assert.assertEquals(new URL(key), deserialized);
 }

 @Test
 public void testClassKeyDeserialization() throws JsonMappingException, JsonProcessingException, ClassNotFoundException {
 String key = "java.lang.String";
 Class<?> clazz = Class.forName(key);
 Object deserialized = mapper.readValue("{\"key\":\"" + key + "\"}", clazz, StdKeyDeserializer.class);
 Assert.assertEquals(clazz, deserialized.getClass());
 }

 @Test
 public void testByteArrayKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 String key = "Rm9udCAoKTs=";
 byte[] deserialized = mapper.readValue("{\"key\":\"" + key + "\"}", byte[].class, StdKeyDeserializer.class);
 Assert.assertArrayEquals(new byte[]{0, 102, 109, 111, 111, 111, 111, 32, 75, 105, 112, 125}, deserialized);
 }

 @Test
 public void testUnknownKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 String key = "unknown";
 Object deserialized = mapper.readValue("{\"key\":\"" + key + "\"}", String.class, StdKeyDeserializer.class);
 Assert.assertEquals("not a valid representation", deserialized);
 }
}