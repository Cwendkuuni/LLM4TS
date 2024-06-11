package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class FromStringDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testStringDeserialization() throws IOException {
 String jsonString = "{\"className\":\"java.net.URL\",\"value\":\"https://www.example.com\"}";
 JavaType javaType = mapper.getTypeFactory().constructType(URL.class);
 URL url = mapper.readValue(jsonString, javaType);
 assertEquals("https://www.example.com", url.toString());

 jsonString = "{\"className\":\"java.util.Currency\",\"value\":\"EUR\"}";
 JavaType currencyType = mapper.getTypeFactory().constructType(Currency.class);
 Currency currency = mapper.readValue(jsonString, currencyType);
 assertEquals(Currency.EUR, currency);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithInvalidJson() throws IOException {
 String jsonString = "{\"className\":\"java.net.URL\",\"value\":\"<invalid>" ;
 JavaType urlType = mapper.getTypeFactory().constructType(URL.class);
 mapper.readValue(jsonString, urlType);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithInvalidClassName() throws IOException {
 String jsonString = "{\"className\":\"java.net.URL2\",\"value\":\"https://www.example.com\"}";
 JavaType urlType = mapper.getTypeFactory().constructType(URL.class);
 mapper.readValue(jsonString, urlType);
 }

 @Test
 public void testDeserializationWithEmptyString() throws IOException {
 String jsonString = "{\"className\":\"java.net.URI\",\"value\":\"\"}";
 JavaType uriType = mapper.getTypeFactory().constructType(URI.class);
 URI uri = mapper.readValue(jsonString, uriType);
 assertTrue(uri.toString().isEmpty());
 }

 @Test
 public void testDeserializationWithEmptyStringClass() throws IOException {
 String jsonString = "{\"className\":\"java.lang.String\",\"value\":\"\"}";
 String string = mapper.readValue(jsonString, String.class);
 assertTrue(string.isEmpty());
 }

 @Test
 public void testDeserializationWithNullValue() throws IOException {
 String jsonString = "{\"className\":\"java.net.URL\",\"value\":null}";
 JavaType urlType = mapper.getTypeFactory().constructType(URL.class);
 URL url = mapper.readValue(jsonString, urlType);
 assertNull(url);
 }

 @Test
 public void testDeserializationWithEmbeddedObject() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("className", "java.util.Locale");
 node.put("value", "en_US");
 String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
 JavaType localeType = mapper.getTypeFactory().constructType(Locale.class);
 Locale locale = mapper.readValue(jsonString, localeType);
 assertEquals("en_US", locale.toString());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithInvalidEmbeddedObject() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("className", "java.util.Locale");
 node.put("value", new Object());
 String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
 JavaType localeType = mapper.getTypeFactory().constructType(Locale.class);
 mapper.readValue(jsonString, localeType);
 }
}