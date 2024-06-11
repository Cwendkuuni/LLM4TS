package com.fasterxml.jackson.databind.deser.std.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testURLDeserialization() throws Exception {
 URL url = FromStringDeserializer.findDeserializer(URL.class).deserialize(MAPPER.getFactory(), MAPPER.getDeserializationContext(), "http://www.example.com");
 assertEquals("http://www.example.com", url.toString());
 }

 @Test
 public void testURIDeserialization() throws Exception {
 URI uri = FromStringDeserializer.findDeserializer(URI.class).deserialize(MAPPER.getFactory(), MAPper.getDeserializationContext(), "http://www.example.com/path/to/file.txt");
 assertEquals("http", uri.getScheme());
 assertEquals("/path/to/file.txt", uri.getPath());
 }

 @Test
 public void testFileDeserialization() throws Exception {
 File file = (File) FromStringDeserializer.findDeserializer(File.class).deserialize(MAPPER.getFactory(), MAPPER.getDeserializationContext(), "/path/to/file.txt");
 assertEquals("/path/to/file.txt", file.getAbsolutePath());
 }

 @Test
 public void testClassDeserialization() throws Exception {
 Class<?> clazz = (Class<?>) FromStringDeserializer.findDeserializer(String.class.getName()).deserialize(MAPPER.getFactory(), MAPPER.getDeserializationContext(), "java.lang.String.class");
 assertNotNull(clazz);
 assertEquals("java.lang.String", clazz.getName());
 }

 @Test
 public void testJavaTypeDeserialization() throws Exception {
 JavaType javaType = (JavaType) FromStringDeserializer.findDeserializer(JavaType.class).deserialize(MAPPER.getFactory(), MAPPER.getDeserializationContext(), "java.util.List<java.lang.String>.class");
 assertNotNull(javaType);
 assertEquals(JavaType.TypeId.COLLECTION_LIST_STRING, javaType.getId());
 }

 @Test
 public void testCurrencyDeserialization() throws Exception {
 Currency currency = (Currency) FromStringDeserializer.findDeserializer(Currency.class).deserialize(MAPPER.getFactory(), MAPPER.getDeserializationContext(), "USD");
 assertEquals(Currency.getInstance("USD"), currency);
 }

 @Test
 public void testPatternDeserialization() throws Exception {
 Pattern pattern = (Pattern) FromStringDeserializer.findDeserializer(Pattern.class).deserialize(MAPPER.getFactory(), MAPPER.getDeserializationContext(), "/^[A-Za-z]+$/");
 assertNotNull(pattern);
 assertTrue(pattern.matcher("abc").matches());
 assertFalse(pattern.matcher("123").matches());
 }

 @Test
 public void testLocaleDeserialization() throws Exception {
 Locale locale = (Locale) FromStringDeserializer.findDeserializer(Locale.class).deserialize(MAPPER.getFactory(), MAPPER.getDeserializationContext(), "en_US");
 assertEquals(Locale.US, locale);
 }

 @Test
 public void testCharsetDeserialization() throws Exception {
 Charset charset = (Charset) FromStringDeserializer.findDeserializer(Charset.class).deserialize(MAPPER.getFactory(), MAPPER.getDeserializationContext(), "UTF-8");
 assertEquals(Charset.forName("UTF-8"), charset);
 }

 @Test
 public void testTimeZoneDeserialization() throws Exception {
 TimeZone timeZone = (TimeZone) FromStringDeserializer.findDeserializer(TimeZone.class).deserialize(MAPPER.getFactory(), MAPPER.getDeserializationContext(), "UTC");
 assertEquals(TimeZone.getTimeZone("UTC"), timeZone);
 }

 @Test
 public void testInetAddressDeserialization() throws Exception {
 InetAddress inetAddress = (InetAddress) FromStringDeserializer.findDeserializer(InetAddress.class).deserialize(MAPPER.getFactory(), MAPPER.getDeserializationContext(), "192.168.1.1");
 assertNotNull(inetAddress);
 assertEquals("192.168.1.1", inetAddress.getHostAddress());
 }

 @Test
 public void testInetSocketAddressDeserialization() throws Exception {
 InetSocketAddress inetSocketAddress = (InetSocketAddress) FromStringDeserializer.findDeserializer(InetSocketAddress.class).deserialize(MAPPER.getFactory(), MAPPER.getDeserializationContext(), "[::1]:8080");
 assertNotNull(inetSocketAddress);
 assertEquals("[::1]", inetSocketAddress.getAddress().getHostAddress());
 assertEquals(8080, inetSocketAddress.getPort());
 }

 @Test
 public void testStringBuilderDeserialization() throws Exception {
 StringBuilder stringBuilder = (StringBuilder) FromStringDeserializer.findDeserializer(StringBuilder.class).deserialize(MAPPER.getFactory(), MAPPER.getDeserializationContext(), "");
 assertNotNull(stringBuilder);
 assertEquals("", stringBuilder.toString());
 }

 @Test
 public void testEmptyStringDeserialization() throws Exception {
 Object result;
 try {
 result = FromStringDeserializer.findDeserializer(Object.class).deserialize(MAPPER.getFactory(), MAPPER.getDeserializationContext(), "");
 fail("Should throw an exception for empty string");
 } catch (JsonMappingException e) {
 // Expected
 }
 assertNull(result);
 }

 @Test
 public void testInvalidDeserialization() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonFactory factory = mapper.getFactory();
 JsonParser parser = factory.getParser();
 parser.setValue("Invalid value");
 try {
 Object result = FromStringDeserializer.findDeserializer(Object.class).deserialize(factory, MAPPER.getDeserializationContext(), parser);
 fail("Should throw an exception for invalid value");
 } catch (JsonMappingException e) {
 // Expected
 assertEquals("Invalid value", e.getValue());
 }
 }
}