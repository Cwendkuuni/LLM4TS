package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class FromStringDeserializerTest {

 private static ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDeserializeFile() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
 File file = (File) deserializer.deserialize(mapper.getFactory().createParser("path/to/file.txt"), mapper.getDeserializationContext());
 assertEquals("path/to/file.txt", file.getAbsolutePath());
 }

 @Test
 public void testDeserializeURL() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
 URL url = (URL) deserializer.deserialize(mapper.getFactory().createParser("http://example.com"), mapper.getDeserializationContext());
 assertEquals("http", url.getProtocol());
 assertEquals("example.com", url.getHost());
 assertEquals(80, url.getPort());
 }

 @Test
 public void testDeserializeURI() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
 URI uri = (URI) deserializer.deserialize(mapper.getFactory().createParser("http://example.com/path/to/resource"), mapper.getDeserializationContext());
 assertEquals("http", uri.getScheme());
 assertEquals("example.com", uri.getHost());
 assertEquals("/path/to/resource", uri.getPath());
 }

 @Test
 public void testDeserializeClass() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
 Class<?> clazz = (Class<?>) deserializer.deserialize(mapper.getFactory().createParser("java.lang.String"), mapper.getDeserializationContext());
 assertEquals(String.class, clazz);
 }

 @Test
 public void testDeserializeJavaType() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Integer.class);
 JavaType javaType = (JavaType) deserializer.deserialize(mapper.getFactory().createParser("java.lang.Integer"), mapper.getDeserializationContext());
 assertEquals(Integer.class, javaType.getRawClass());
 }

 @Test
 public void testDeserializeCurrency() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
 Currency currency = (Currency) deserializer.deserialize(mapper.getFactory().createParser("USD"), mapper.getDeserializationContext());
 assertEquals(Currency.getInstance("USD"), currency);
 }

 @Test
 public void testDeserializePattern() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
 Pattern pattern = (Pattern) deserializer.deserialize(mapper.getFactory().createParser("/[a-z]$/gi"), mapper.getDeserializationContext());
 assertEquals(Pattern.compile("/[a-z]$/gi"), pattern);
 }

 @Test
 public void testDeserializeLocale() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
 Locale locale = (Locale) deserializer.deserialize(mapper.getFactory().createParser("en_US"), mapper.getDeserializationContext());
 assertEquals(Locale.US, locale);
 }

 @Test
 public void testDeserializeCharset() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
 Charset charset = (Charset) deserializer.deserialize(mapper.getFactory().createParser("UTF-8"), mapper.getDeserializationContext());
 assertEquals(Charset.forName("UTF-8"), charset);
 }

 @Test
 public void testDeserializeTimezone() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
 TimeZone timeZone = (TimeZone) deserializer.deserialize(mapper.getFactory().createParser("UTC"), mapper.getDeserializationContext());
 assertEquals(TimeZone.getTimeZone("UTC"), timeZone);
 }

 @Test
 public void testDeserializeInetAddress() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
 InetAddress inetAddress = (InetAddress) deserializer.deserialize(mapper.getFactory().createParser("192.168.1.1"), mapper.getDeserializationContext());
 assertNotNull(inetAddress);
 assertEquals("192.168.1.1", inetAddress.getHostAddress());
 }

 @Test
 public void testDeserializeInetSocketAddress() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
 InetSocketAddress inetSocketAddress = (InetSocketAddress) deserializer.deserialize(mapper.getFactory().createParser("[255.255.255.255]:80"), mapper.getDeserializationContext());
 assertNotNull(inetSocketAddress);
 assertEquals("[255.255.255.255]", inetSocketAddress.getAddress().getHostAddress());
 assertEquals(80, inetSocketAddress.getPort());
 }

 @Test
 public void testDeserializeStringBuilder() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
 StringBuilder stringBuilder = (StringBuilder) deserializer.deserialize(mapper.getFactory().createParser("abc"), mapper.getDeserializationContext());
 assertEquals("abc", stringBuilder.toString());
 }

 @Test
 public void testEmptyStringDeserialization() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
 try {
 File file = (File) deserializer.deserialize(mapper.getFactory().createParser(""), mapper.getDeserializationContext());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testInvalidFormatExceptionForURL() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
 try {
 URL url = (URL) deserializer.deserialize(mapper.getFactory().createParser("invalid-url"), mapper.getDeserializationContext());
 fail("Expected InvalidFormatException");
 } catch (InvalidFormatException e) {
 // Expected
 }
 }

 @Test
 public void testInvalidFormatExceptionForURI() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
 try {
 URI uri = (URI) deserializer.deserialize(mapper.getFactory().createParser("invalid-uri"), mapper.getDeserializationContext());
 fail("Expected InvalidFormatException");
 } catch (InvalidFormatException e) {
 // Expected
 }
 }

 @Test
 public void testInvalidFormatExceptionForClass() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
 try {
 Class<?> clazz = (Class<?>) deserializer.deserialize(mapper.getFactory().createParser("invalid-class"), mapper.getDeserializationContext());
 fail("Expected InvalidFormatException");
 } catch (InvalidFormatException e) {
 // Expected
 }
 }

 @Test
 public void testInvalidFormatExceptionForJavaType() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Integer.class);
 try {
 JavaType javaType = (JavaType) deserializer.deserialize(mapper.getFactory().createParser("invalid-java-type"), mapper.getDeserializationContext());
 fail("Expected InvalidFormatException");
 } catch (InvalidFormatException e) {
 // Expected
 }
 }

 @Test
 public void testInvalidFormatExceptionForCurrency() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
 try {
 Currency currency = (Currency) deserializer.deserialize(mapper.getFactory().createParser("invalid-currency"), mapper.getDeserializationContext());
 fail("Expected InvalidFormatException");
 } catch (InvalidFormatException e) {
 // Expected
 }
 }

 @Test
 public void testInvalidFormatExceptionForPattern() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
 try {
 Pattern pattern = (Pattern) deserializer.deserialize(mapper.getFactory().createParser("invalid-pattern"), mapper.getDeserializationContext());
 fail("Expected InvalidFormatException");
 } catch (InvalidFormatException e) {
 // Expected
 }
 }

 @Test
 public void testInvalidFormatExceptionForLocale() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
 try {
 Locale locale = (Locale) deserializer.deserialize(mapper.getFactory().createParser("invalid-locale"), mapper.getDeserializationContext());
 fail("Expected InvalidFormatException");
 } catch (InvalidFormatException e) {
 // Expected
 }
 }

 @Test
 public void testInvalidFormatExceptionForCharset() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
 try {
 Charset charset = (Charset) deserializer.deserialize(mapper.getFactory().createParser("invalid-charset"), mapper.getDeserializationContext());
 fail("Expected InvalidFormatException");
 } catch (InvalidFormatException e) {
 // Expected
 }
 }

 @Test
 public void testInvalidFormatExceptionForTimezone() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
 try {
 TimeZone timeZone = (TimeZone) deserializer.deserialize(mapper.getFactory().createParser("invalid-timezone"), mapper.getDeserializationContext());
 fail("Expected InvalidFormatException");
 } catch (InvalidFormatException e) {
 // Expected
 }
 }

 @Test
 public void testInvalidFormatExceptionForInetAddress() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
 try {
 InetAddress inetAddress = (InetAddress) deserializer.deserialize(mapper.getFactory().createParser("invalid-inet-address"), mapper.getDeserializationContext());
 fail("Expected InvalidFormatException");
 } catch (InvalidFormatException e) {
 // Expected
 }
 }

 @Test
 public void testInvalidFormatExceptionForInetSocketAddress() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
 try {
 InetSocketAddress inetSocketAddress = (InetSocketAddress) deserializer.deserialize(mapper.getFactory().createParser("invalid-inet-socket-address"), mapper.getDeserializationContext());
 fail("Expected InvalidFormatException");
 } catch (InvalidFormatException e) {
 // Expected
 }
 }

 @Test
 public void testInvalidFormatExceptionForStringBuilder() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
 try {
 StringBuilder stringBuilder = (StringBuilder) deserializer.deserialize(mapper.getFactory().createParser("invalid-string-builder"), mapper.getDeserializationContext());
 fail("Expected InvalidFormatException");
 } catch (InvalidFormatException e) {
 // Expected
 }
 }

 @Test
 public void testDeserializeFromArray() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Integer[].class);
 ObjectNode node = mapper.readValueAsTree("[1, 2, 3]");
 Integer[] integers = (Integer[]) deserializer.deserialize(mapper.getFactory().createParser(node.getValuesAsArray().toString()), mapper.getDeserializationContext());
 assertNotNull(integers);
 assertEquals(3, integers.length);
 assertEquals(1, integers[0]);
 assertEquals(2, integers[1]);
 assertEquals(3, integers[2]);
 }

 @Test
 public void testDeserializeEmbeddedObject() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
 ObjectNode node = mapper.readValueAsTree("{\"language\":\"en\",\"country\":\"US\"}");
 Locale locale = (Locale) deserializer.deserialize(mapper.getFactory().createParser(node.toString()), mapper.getDeserializationContext());
 assertEquals("en", locale.getLanguage());
 assertEquals("US", locale.getCountry());
 }

 @Test
 public void testUnexpectedToken() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Integer.class);
 ObjectNode node = mapper.createObjectNode();
 node.put("i", 7);
 try {
 deserializer.deserialize(mapper.getFactory().createParser(node.toString()), mapper.getDeserializationContext());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }
}