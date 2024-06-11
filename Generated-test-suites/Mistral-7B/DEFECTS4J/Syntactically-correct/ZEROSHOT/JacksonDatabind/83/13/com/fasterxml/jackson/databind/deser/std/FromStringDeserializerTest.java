package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.BaseJsonParser;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeFactory;

public class FromStringDeserializerTest {

 private JsonFactory jsonFactory;
 private ObjectMapper objectMapper;
 private DeserializationContext context;
 private FromStringDeserializer<URL> urlDeserializer;
 private FromStringDeserializer<Currency> currencyDeserializer;
 private FromStringDeserializer<Locale> localeDeserializer;
 private FromStringDeserializer<TimeZone> timeZoneDeserializer;

 @Before
 public void setup() {
 jsonFactory = new JsonFactory();
 objectMapper = new ObjectMapper(jsonFactory);
 context = new DeserializationContext(objectMapper);
 urlDeserializer = new FromStringDeserializer<URL>(URL.class);
 currencyDeserializer = new FromStringDeserializer<Currency>(Currency.class);
 localeDeserializer = new FromStringDeserializer<Locale>(Locale.class);
 timeZoneDeserializer = new FromStringDeserializer<TimeZone>(TimeZone.class);
 }

 @Test
 public void testDeserializeUrl() throws IOException, JsonProcessingException {
 String json = "\"http://www.example.com\"";
 JsonParser parser = jsonFactory.createParser(json);
 URL result = urlDeserializer.deserialize(parser, context);
 assertEquals("http://www.example.com", result.toExternalForm());
 }

 @Test
 public void testDeserializeInvalidUrl() throws IOException, JsonProcessingException {
 String json = "\"invalidUrl\"";
 JsonParser parser = jsonFactory.createParser(json);
 assertNull(urlDeserializer.deserialize(parser, context));
 }

 @Test
 public void testDeserializeCurrency() throws IOException, JsonProcessingException {
 String json = "\"EUR\"";
 JsonParser parser = jsonFactory.createParser(json);
 Currency result = currencyDeseralizer.deserialize(parser, context);
 assertEquals(Currency.EUR, result);
 }

 @Test
 public void testDeserializeInvalidCurrency() throws IOException, JsonProcessingException {
 String json = "\"XYZ\"";
 JsonParser parser = jsonFactory.createParser(json);
 assertNull(currencyDeserializer.deserialize(parser, context));
 }

 @Test
 public void testDeserializeLocale() throws IOException, JsonProcessingException {
 String json = "\"en_US\"";
 JsonParser parser = jsonFactory.createParser(json);
 Locale result = localeDeserializer.deserialize(parser, context);
 assertEquals("en_US", result.toString());
 }

 @Test
 public void testDeserializeInvalidLocale() throws IOException, JsonProcessingException {
 String json = "\"en_XX\"";
 JsonParser parser = jsonFactory.createParser(json);
 assertNull(localeDeserializer.deserialize(parser, context));
 }

 @Test
 public void testDeserializeTimeZone() throws IOException, JsonProcessingException {
 String json = "\"UTC\"";
 JsonParser parser = jsonFactory.createParser(json);
 TimeZone result = timeZoneDeserializer.deserialize(parser, context);
 assertEquals("UTC", result.getID());
 }

 @Test
 public void testDeserializeInvalidTimeZone() throws IOException, JsonProcessingException {
 String json = "\"XXX\"";
 JsonParser parser = jsonFactory.createParser(json);
 assertNull(timeZoneDeserializer.deserialize(parser, context));
 }

 @Test
 public void testFindDeserializer() {
 Class<?>[] supportedTypes = FromStringDeserializer.types();
 Std deserializer = null;
 for (Class<?> clazz : supportedTypes) {
 deserializer = FromStringDeserializer.findDeserializer(clazz);
 if (deserializer != null) {
 break;
 }
 }
 assertTrue(deserializer != null);
 }

 @Test
 public void testFindDeserializerUnknownType() {
 assertNull(FromStringDeserializer.findDeserializer(String.class));
 }
}