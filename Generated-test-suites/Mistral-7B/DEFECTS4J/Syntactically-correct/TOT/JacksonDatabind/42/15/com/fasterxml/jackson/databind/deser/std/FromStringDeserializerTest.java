package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.*;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

public class FromStringDeserializerTest {

 private static ObjectMapper objectMapper;

 static {
 try {
 objectMapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addDeserializer(URI.class, FromStringDeserializer.findDeserializer(URI.class));
 objectMapper.registerModule(module);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testURIDeserialization() throws IOException, URISyntaxException {
 String json = "\"http://example.com/path/to/resource\"";
 JsonFactory factory = new JsonFactory();
 JsonParser jp = factory.createParser(new ByteArrayInputStream(json.getBytes()));
 T value = (T) objectMapper.readValue(jp, URI.class);
 assertEquals("http://example.com/path/to/resource", value.toString());
 }

 @Test
 public void testEmptyURIDeserialization() throws IOException {
 String json = "\"\"";
 JsonFactory factory = new JsonFactory();
 JsonParser jp = factory.createParser(new ByteArrayInputStream(json.getBytes()));
 T value = (T) objectMapper.readValue(jp, URI.class);
 assertTrue(value.equals(URI.create("")));
 }

 @Test
 public void testInvalidURIDeserialization() throws IOException {
 String json = "\"invalid:format\"";
 JsonFactory factory = new JsonFactory();
 JsonParser jp = factory.createParser(new ByteArrayInputStream(json.getBytes()));
 try {
 objectMapper.readValue(jp, URI.class);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Pass
 }
 }

 @Test
 public void testLocaleDeserialization() throws IOException {
 String json = "\"en_US\"";
 JsonFactory factory = new JsonFactory();
 JsonParser jp = factory.createParser(new ByteArrayInputStream(json.getBytes()));
 T value = (T) objectMapper.readValue(jp, Locale.class);
 assertEquals("en_US", value.toString());
 }

 @Test
 public void testEmptyLocaleDeserialization() throws IOException {
 String json = "\"\"";
 JsonFactory factory = new JsonFactory();
 JsonParser jp = factory.createParser(new ByteArrayInputStream(json.getBytes()));
 T value = (T) objectMapper.readValue(jp, Locale.class);
 assertTrue(value.equals(Locale.getDefault()));
 }

 @Test
 public void testInvalidLocaleDeserialization() throws IOException {
 String json = "\"xy_XY\"";
 JsonFactory factory = new JsonFactory();
 JsonParser jp = factory.createParser(new ByteArrayInputStream(json.getBytes()));
 try {
 objectMapper.readValue(jp, Locale.class);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Pass
 }
 }

 @Test
 public void testFileDeserialization() throws IOException {
 String json = "\"test.txt\"";
 JsonFactory factory = new JsonFactory();
 JsonParser jp = factory.createParser(new ByteArrayInputStream(json.getBytes()));
 T value = (T) objectMapper.readValue(jp, File.class);
 assertEquals("test.txt", value.getName());
 }

 @Test
 public void testNullFileDeserialization() throws IOException {
 String json = "\"null\"";
 JsonFactory factory = new JsonFactory();
 JsonParser jp = factory.createParser(new ByteArrayInputStream(json.getBytes()));
 T value = (T) objectMapper.readValue(jp, File.class);
 assertNull(value);
 }

 @Test
 public void testURLDeserialization() throws IOException {
 String json = "\"http://example.com\"";
 JsonFactory factory = new JsonFactory();
 JsonParser jp = factory.createParser(new ByteArrayInputStream(json.getBytes()));
 T value = (T) objectMapper.readValue(jp, URL.class);
 assertEquals("http://example.com", value.toString());
 }

 @Test
 public void testEmptyURLDeserialization() throws IOException {
 String json = "\"\"";
 JsonFactory factory = new JsonFactory();
 JsonParser jp = factory.createParser(new ByteArrayInputStream(json.getBytes()));
 T value = (T) objectMapper.readValue(jp, URL.class);
 assertNull(value);
 }

 @Test
 public void testInvalidURLDeserialization() throws IOException {
 String json = "\"invalid:format\"";
 JsonFactory factory = new JsonFactory();
 JsonParser jp = factory.createParser(new ByteArrayInputStream(json.getBytes()));try {
 objectMapper.readValue(jp, URL.class);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Pass
 }
 }

 @Test
 public void testClassDeserialization() throws IOException {
 String json = "\"java.lang.String\"";
 JsonFactory factory = new JsonFactory();
 JsonParser jp = factory.createParser(new ByteArrayInputStream(json.getBytes()));
 T value = (T) objectMapper.readValue(jp, Class.class);
 assertEquals(String.class, value);
 }

 @Test
 public void testInvalidClassDeserialization() throws IOException {
 String json = "\"java.lang.Xyzzy\"";
 JsonFactory factory = new JsonFactory();
 JsonParser jp = factory.createParser(new ByteArrayInputStream(json.getBytes()));
 try {
 objectMapper.readValue(jp, Class.class);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Pass
 }
 }

 @Test
 public void testJavaTypeDeserialization() throws IOException {
 String json = "\"java.util.List<java.lang.String>\"".replace("\"", "\\\"");
 JsonFactory factory = new JsonFactory();
 JsonParser jp = factory.createParser(new ByteArrayInputStream(json.getBytes()));
 JavaType javaType = (JavaType) objectMapper.readValue(jp, JavaType.class);
 assertEquals(java.util.List.class, javaType.getRawClass());
 assertEquals(java.lang.String.class, javaType.getContentType());
 }

 @Test
 public void testInvalidJavaTypeDeserialization() throws IOException {
 String json = "\"java.util.List<java.lang.Xyzzy>\"".replace("\"", "\\\"");
 JsonFactory factory = new JsonFactory();
 JsonParser jp = factory.createParser(new ByteArrayInputStream(json.getBytes()));
 try {
 objectMapper.readValue(jp, JavaType.class);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Pass
 }
 }

 @Test
 public void testCurrencyDeserialization() throws IOException {
 String json = "\"USD\"";
 JsonFactory factory = new JsonFactory();
 JsonParser jp = factory.createParser(new ByteArrayInputStream(json.getBytes()));
 T value = (T) objectMapper.readValue(jp, Currency.class);
 assertEquals(Currency.getInstance("USD"), value);
 }

 @Test
 public void testInvalidCurrencyDeserialization() throws IOException {
 String json = "\"XYZ\"";
 JsonFactory factory = new JsonFactory();
 JsonParser jp = factory.createParser(new ByteArrayInputStream(json.getBytes()));
 try {
 objectMapper.readValue(jp, Currency.class);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Pass
 }
 }

 @Test
 public void testPatternDeserialization() throws IOException {
 String json = "/.*/";
 JsonFactory factory = new JsonFactory();
 JsonParser jp = factory.createParser(new ByteArrayInputStream(json.getBytes()));
 T value = (T) objectMapper.readValue(jp, Pattern.class);
 assertEquals(Pattern.compile(json), value);
 }

 @Test
 public void testInvalidPatternDeserialization() throws IOException {
 String json = "/[xyz]/";
 JsonFactory factory = new JsonFactory();
 JsonParser jp = factory.createParser(new ByteArrayInputStream(json.getBytes()));
 try {
 objectMapper.readValue(jp, Pattern.class);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Pass
 }
 }
}