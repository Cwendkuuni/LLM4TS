package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer.Std;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

 // Test for File deserialization
 @Test
 public void testFileDeserialization() throws IOException {
 String fileString = "C:/temp/test.txt";
 ObjectMapper mapper = new ObjectMapper();
 Std deserializer = Std.findDeserializer(File.class);
 File expectedFile = new File(fileString);
 File actualFile = mapper.readValue(mapper.createParser(new StringReader(fileString)), deserializer.getValueClass());
 assertEquals(expectedFile, actualFile);
 }

 // Test for URL deserialization
 @Test
 public void testURLDeserialization() throws IOException {
 String urlString = "http://www.google.com";
 ObjectMapper mapper = new ObjectMapper();
 Std deserializer = Std.findDeserializer(URL.class);
 URL expectedURL = new URL(urlString);
 URL actualURL = mapper.readValue(mapper.createParser(new StringReader(urlString)), deserializer.getValueClass());
 assertEquals(expectedURL, actualURL);
 }

 // Test for URI deserialization
 @Test
 public void testURIDeserialization() throws IOException {
 String uriString = "http://user:password@www.google.com:8080/test/path/file.txt";
 ObjectMapper mapper = new ObjectMapper();
 Std deserializer = Std.findDeserializer(URI.class);
 URI expectedURI = new URI(uriString);
 URI actualURI = mapper.readValue(mapper.createParser(new StringReader(uriString)), deserializer.getValueClass());
 assertEquals(expectedURI, actualURI);
 }

 // Test for Class deserialization
 @Test
 public void testClassDeserialization() throws IOException {
 String className = "java.util.Date";
 ObjectMapper mapper = new ObjectMapper();
 Std deserializer = Std.findDeserializer(Date.class);
 Date expectedDate = new Date();
 Date actualDate = mapper.readValue(mapper.createParser(new StringReader(className)), deserializer.getValueClass());
 assertEquals(expectedDate.getClass(), actualDate.getClass());
 }

 // Test for JavaType deserialization
 @Test
 public void testJavaTypeDeserialization() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 Std deserializer = Std.findDeserializer(JavaType.class);
 JavaType expectedJavaType = mapper.getTypeFactory().constructType(String.class);
 JavaType actualJavaType = mapper.readValue(mapper.createParser(new StringReader("java.lang.String")), deserializer.getValueClass());
 assertEquals(expectedJavaType, actualJavaType);
 }

 // Test for Currency deserialization
 @Test
 public void testCurrencyDeserialization() throws IOException {
 String currencyString = "USD";
 ObjectMapper mapper = new ObjectMapper();
 Std deserializer = Std.findDeserializer(Currency.class);
 Currency expectedCurrency = Currency.getInstance(Locale.US);
 Currency actualCurrency = mapper.readValue(mapper.createParser(new StringReader(currencyString)), deserializer.getValueClass());
 assertEquals(expectedCurrency, actualCurrency);
 }

 // Test for Pattern deserialization
 @Test
 public void testPatternDeserialization() throws IOException {
 String patternString = "[a-z]+";
 ObjectMapper mapper = new ObjectMapper();
 Std deserializer = Std.findDeserializer(Pattern.class);
 Pattern expectedPattern = Pattern.compile(patternString);
 Pattern actualPattern = mapper.readValue(mapper.createParser(new StringReader(patternString)), deserializer.getValueClass());
 assertEquals(expectedPattern.pattern(), actualPattern.pattern());
 }

 // Test for Locale deserialization
 @Test
 public void testLocaleDeserialization() throws IOException {
 String localeString = "en_US";
 ObjectMapper mapper = new ObjectMapper();
 Std deserializer = Std.findDeserializer(Locale.class);
 Locale expectedLocale = new Locale("en", "US");
 Locale actualLocale = mapper.readValue(mapper.createParser(new StringReader(localeString)), deserializer.getValueClass());
 assertEquals(expectedLocale, actualLocale);
 }

 // Test for Charset deserialization
 @Test
 public void testCharsetDeserialization() throws IOException {
 String charsetString = "UTF-8";
 ObjectMapper mapper = new ObjectMapper();
 Std deserializer = Std.findDeserializer(Charset.class);
 Charset expectedCharset = Charset.forName(charsetString);
 Charset actualCharset = mapper.readValue(mapper.createParser(new StringReader(charsetString)), deserializer.getValueClass());
 assertEquals(expectedCharset, actualCharset);
 }

 // Test for TimeZone deserialization
 @Test
 public void testTimeZoneDeserialization() throws IOException {
 String timeZoneString = "America/Los_Angeles";
 ObjectMapper mapper = new ObjectMapper();
 Std deserializer = Std.findDeserializer(TimeZone.class);
 TimeZone expectedTimeZone = TimeZone.getTimeZone(timeZoneString);
 TimeZone actualTimeZone = mapper.readValue(mapper.createParser(new StringReader(timeZoneString)), deserializer.getValueClass());
 assertEquals(expectedTimeZone, actualTimeZone);
 }

 // Test for InetAddress deserialization
 @Test
 public void testInetAddressDeserialization() throws IOException {
 String inetAddressString = "192.168.1.1";
 ObjectMapper mapper = new ObjectMapper();
 Std deserializer = Std.findDeserializer(InetAddress.class);
 InetAddress expectedAddress = InetAddress.getByName(inetAddressString);
 InetAddress actualAddress = mapper.readValue(mapper.createParser(new StringReader(inetAddressString)), deserializer.getValueClass());
 assertEquals(expectedAddress, actualAddress);
 }

 // Test for InetSocketAddress deserialization
 @Test
 public void testInetSocketAddressDeserialization() throws IOException {
 String inetSocketAddressString = "[192.168.1.1]:8080";
 ObjectMapper mapper = new ObjectMapper();
 Std deserializer = Std.findDeserializer(InetSocketAddress.class);
 InetSocketAddress expectedAddress = new InetSocketAddress("192.168.1.1", 8080);
 InetSocketAddress actualAddress = mapper.readValue(mapper.createParser(new StringReader(inetSocketAddressString)), deserializer.getValueClass());
 assertEquals(expectedAddress, actualAddress);
 }

 // Test for StringBuilder deserialization
 @Test
 public void testStringBuilderDeserialization() throws IOException {
 String stringBuilderString = "Hello World";
 ObjectMapper mapper = new ObjectMapper();
 Std deserializer = Std.findDeserializer(StringBuilder.class);
 StringBuilder expectedStringBuilder = new StringBuilder("Hello World");
 StringBuilder actualStringBuilder = mapper.readValue(mapper.createParser(new StringReader(stringBuilderString)), deserializer.getValueClass());
 assertEquals(expectedStringBuilder, actualStringBuilder);
 }

 // Test for empty string deserialization
 @Test
 public void testEmptyStringDeserialization() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 Std deserializer = Std.findDeserializer(String.class);
 String expectedEmptyString = "";
 String actualEmptyString = mapper.readValue(mapper.createParser(new StringReader("")), deserializer.getValueClass());
 assertEquals(expectedEmptyString, actualEmptyString);
 }

 // Test for unexpected token deserialization
 @Test(expected = JsonMappingException.class)
 public void testUnexpectedTokenDeserialization() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 Std deserializer = Std.findDeserializer(String.class);
 JsonParser parser = mapper.getFactory().createParser("[1, 2, 3]");
 mapper.readValue(parser, deserializer.getValueClass());
 }

 // Test for null deserialization
 @Test
 public void testNullDeserialization() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 Std deserializer = Std.findDeserializer(String.class);
 String expectedNull = null;
 String actualNull = mapper.readValue(mapper.createParser(new StringReader(null)), deserializer.getValueClass());
 assertEquals(expectedNull, actualNull);
 }

 // Test for invalid format exception deserialization
 @Test(expected = InvalidFormatException.class)
 public void testInvalidFormatExceptionDeserialization() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 Std deserializer = Std.findDeserializer(Integer.class);
 JsonParser parser = mapper.getFactory().createParser("invalidFormat");
 mapper.readValue(parser, deserializer.getValueClass());
 }
}