package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FromStringDeserializerTest {

 // Test for deserializing a valid URL
 @Test
 public void testDeserializeUrl() throws IOException, JsonMappingException {
 FromStringDeserializer urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
 ObjectMapper mapper = new ObjectMapper();
 String validUrlString = "http://www.example.com";
 URL expectedResult = new URL("http://www.example.com");

 // Test deserialization using deserializer factory
 URL result = (URL) urlDeserializer.deserialize(mapper.getFactory().createParser(validUrlString), mapper.getDeserializationContext());
 // Test equality of deserialized object and expected result
 assertEquals(expectedResult, result);
 }

 // Test for deserializing a valid URI
 @Test
 public void testDeserializeUri() throws IOException, JsonMappingException {
 FromStringDeserializer uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
 ObjectMapper mapper = new ObjectMapper();
 String validUriString = "http://www.example.com/path/to/resource";
 URI expectedResult = new URI("http", "www.example.com", "/path/to/resource");

 // Test deserialization using deserializer factory
 URI result = (URI) uriDeserializer.deserialize(mapper.getFactory().createParser(validUriString), mapper.getDeserializationContext());
 // Test equality of deserialized object and expected result
 assertEquals(expectedResult, result);
 }

 // Test for deserializing a valid class
 @Test
 public void testDeserializeClass() throws IOException, JsonMappingException {
 FromStringDeserializer classDeserializer = FromStringDeserializer.findDeserializer(String.class);
 ObjectMapper mapper = new ObjectMapper();
 String validClassName = "java.lang.String";
 Class<?> expectedResult = String.class;

 // Test deserialization using deserializer factory
 Class<?> result = (Class<?>) classDeserializer.deserialize(mapper.getFactory().createParser(validClassName), mapper.getDeserializationContext());
 // Test equality of deserialized object and expected result
 assertEquals(expectedResult, result);
 }

 // Test for deserializing an empty string for a supported type (StringBuilder)
 @Test
 public void testDeserializeEmptyStringForSupportedType() throws IOException, JsonMappingException {
 FromStringDeserializer stringBuilderDeserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
 ObjectMapper mapper = new ObjectMapper();
 String emptyString = "";
 StringBuilder expectedResult = new StringBuilder();

 // Test deserialization using deserializer factory
 StringBuilder result = (StringBuilder) stringBuilderDeserializer.deserialize(mapper.getFactory().createParser(emptyString), mapper.getDeserializationContext());
 // Test equality of deserialized object and expected result
 assertEquals(expectedResult, result);
 }

 // Test for deserializing an empty string for an unsupported type (Integer)
 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmptyStringForUnsupportedType() throws IOException, JsonMappingException {
 FromStringDeserializer integerDeserializer = FromStringDeserializer.findDeserializer(int.class);
 ObjectMapper mapper = new ObjectMapper();
 String emptyString = "";

 // Test deserialization using deserializer factory
 integerDeserializer.deserialize(mapper.getFactory().createParser(emptyString), mapper.getDeserializationContext());
 }

 // Test for deserializing a String representation of a Locale
 @Test
 public void testDeserializeLocale() throws IOException, JsonMappingException {
 FromStringDeserializer localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
 ObjectMapper mapper = new ObjectMapper();
 String localeString = "en_US";
 Locale expectedResult = Locale.US;

 // Test deserialization using deserializer factory
 Locale result = (Locale) localeDeserializer.deserialize(mapper.getFactory().createParser(localeString), mapper.getDeserializationContext());
 // Test equality of deserialized object and expected result
 assertEquals(expectedResult,result);
 }

 // Test for deserializing an invalid String representation of a Locale
 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidLocale() throws IOException, JsonMappingException {
 FromStringDeserializer localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
 ObjectMapper mapper = new ObjectMapper();
 String invalidLocaleString = "invalid_locale";

 // Test deserialization using deserializer factory
 localeDeserializer.deserialize(mapper.getFactory().createParser(invalidLocaleString), mapper.getDeserializationContext());
 }

 // Test for deserializing a MalformedURLException when parsing an invalid URL
 @Test(expected = MalformedURLException.class)
 public void testDeserializeMalformedURLException() throws IOException, JsonMappingException {
 FromStringDeserializer urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
 ObjectMapper mapper = new ObjectMapper();
 String invalidUrlString = "invalid_url";

 // Test deserialization using deserializer factory
 urlDeserializer.deserialize(mapper.getFactory().createParser(invalidUrlString), mapper.getDeserializationContext());
 }

 // Test for deserializing an IllegalArgumentException when parsing an invalid string for a supported type (Integer)
 @Test(expected = IllegalArgumentException.class)
 public void testDeserializeIllegalArgumentException() throws IOException, JsonMappingException {
 FromStringDeserializer intDeserializer = FromStringDeserializer.findDeserializer(int.class);
 ObjectMapper mapper = new ObjectMapper();
 String invalidString = "abc";

 // Test deserialization using deserializer factory
 intDeserializer.deserialize(mapper.getFactory().createParser(invalidString), mapper.getDeserializationContext());
 }

 // Test for deserializing a JsonMappingException when parsing an invalid string for an unsupported type (CustomClass)
 @Test(expected = JsonMappingException.class)
 public void testDeserializeJsonMappingException() throws IOException, JsonMappingException {
 FromStringDeserializer customClassDeserializer = FromStringDeserializer.findDeserializer(CustomClass.class);
 ObjectMapper mapper = new ObjectMapper();
 String invalidString = "invalid_string";

 // Test deserialization using deserializer factory
 customClassDeserializer.deserialize(mapper.getFactory().createParser(invalidString), mapper.getDeserializationContext());
 }
}