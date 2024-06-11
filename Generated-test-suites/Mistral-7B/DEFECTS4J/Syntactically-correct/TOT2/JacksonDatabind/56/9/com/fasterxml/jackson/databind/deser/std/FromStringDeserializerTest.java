package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.test.BaseTest;

@RunWith(BaseTest.class)
public class FromStringDeserializerTest {

 // Testing for File deserializer
 @Test
 public void testFileDeserialization() throws JsonProcessingException, IOException {
 File expectedFile = new File("testFile.txt");
 FromStringDeserializer<File> fileDeserializer = new FromStringDeserializer<File>(File.class);
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 String inputString = "testFile.txt";
 File actualFile = fileDeserializer.deserialize(Mockito.mock(JsonParser.class), context);
 assertEquals(expectedFile, actualFile);
 }

 // Testing for URL deserializer
 @Test
 public void testURLDeserialization() throws JsonProcessingException, IOException {
 URL expectedURL = new URL("http://example.com");
 FromStringDeserializer<URL> urlDeserializer = new FromStringDeserializer<URL>(URL.class);
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 String inputString = "http://example.com";
 URL actualURL = urlDeserializer.deserialize(Mockito.mock(JsonParser.class), context);
 assertEquals(expectedURL, actualURL);
 }

 // Testing for URI deserializer
 @Test
 public void testURIDeserialization() throws JsonProcessingException, IOException {
 URI expectedURI = new URI("http://example.com/test");
 FromStringDeserializer<URI> uriDeserializer = new FromStringDeserializer<URI>(URI.class);
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 String inputString = "http://example.com/test";
 URI actualURI = uriDeserializer.deserialize(Mockito.mock(JsonParser.class), context);
 assertEquals(expectedURI, actualURI);
 }

 // Testing for Class deserializer
 @Test
 public void testClassDeserialization() throws JsonProcessingException, IOException, InstantiationException, IllegalAccessException {
 Class<String> expectedClass = String.class;
 FromStringDeserializer<Class<String>> classDeserializer = new FromStringDeserializer<Class<String>>(String.class);
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 String inputString = "java.lang.String";
 Class<String> actualClass = classDeserializer.deserialize(Mockito.mock(JsonParser.class), context);
 assertEquals(expectedClass, actualClass);
 }

 // Testing for JavaType deserializer
 @Test
 public void testJavaTypeDeserialization() throws JsonProcessingException, IOException {
 JavaType expectedJavaType = JavaType.constructType(String[].class);
 FromStringDeserializer<JavaType> javaTypeDeserializer = new FromStringDeserializer<JavaType>(JavaType.class);
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 String inputString = "class [Ljava.lang.String;";
 JavaType actualJavaType = javaTypeDeserializer.deserialize(Mockito.mock(JsonParser.class), context);
 assertEquals(expectedJavaType, actualJavaType);
 }

 // Testing for Currency deserializer
 @Test
 public void testCurrencyDeserialization() throws JsonProcessingException, IOException {
 Currency expectedCurrency = Currency.getInstance("USD");
 FromStringDeserializer<Currency> currencyDeserializer = new FromStringDeserializer<Currency>(Currency.class);
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 String inputString = "USD";
 Currency actualCurrency = currencyDeserializer.deserialize(Mockito.mock(JsonParser.class), context);
 assertEquals(expectedCurrency, actualCurrency);
 }

 // Testing for Pattern deserializer
 @Test
 public void testPatternDeserialization() throws JsonProcessingException, IOException {
 Pattern expectedPattern = Pattern.compile("[a-z]+");
 FromStringDeserializer<Pattern> patternDeserializer = new FromStringDeserializer<Pattern>(Pattern.class);
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 String inputString = "[a-z]+";
 Pattern actualPattern = patternDeserializer.deserialize(Mockito.mock(JsonParser.class), context);
 assertEquals(expectedPattern, actualPattern);
 }

 // Testing for Locale deserializer
 @Test
 public void testLocaleDeserialization() throws JsonProcessingException, IOException {
 Locale expectedLocale = new Locale("en_US");
 FromStringDeserializer<Locale> localeDeserializer = new FromStringDeserializer<Locale>(Locale.class);
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 String inputString = "en_US";
 Locale actualLocale = localeDeserializer.deserialize(Mockito.mock(JsonParser.class), context);
 assertEquals(expectedLocale, actualLocale);
 }

 // Testing for Charset deserializer
 @Test
 public void testCharsetDeserialization() throws JsonProcessingException, IOException {
 Charset expectedCharset = Charset.forName("UTF-8");
 FromStringDeserializer<Charset> charsetDeserializer = new FromStringDeserializer<Charset>(Charset.class);
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 String inputString = "UTF-8";
 Charset actualCharset = charsetDeserializer.deserialize(Mockito.mock(JsonParser.class), context);
 assertEquals(expectedCharset, actualCharset);
 }

 // Testing for TimeZone deserializer
 @Test
 public void testTimeZoneDeserialization() throws JsonProcessingException, IOException {
 TimeZone expectedTimeZone = TimeZone.getTimeZone("UTC");
 FromStringDeserializer<TimeZone> timeZoneDeserializer = new FromStringDeserializer<TimeZone>(TimeZone.class);
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 String inputString = "UTC";
 TimeZone actualTimeZone = timeZoneDeserializer.deserialize(Mockito.mock(JsonParser.class), context);
 assertEquals(expectedTimeZone, actualTimeZone);
 }

 // Testing for InetAddress deserializer
 @Test
 public void testInetAddressDeserialization() throws JsonProcessingException, IOException {
 InetAddress expectedAddress = InetAddress.getByName("127.0.0.1");
 FromStringDeserializer<InetAddress> inetAddressDeserializer = new FromStringDeserializer<InetAddress>(InetAddress.class);
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 String inputString = "127.0.0.1";
 InetAddress actualAddress = inetAddressDeserializer.deserialize(Mockito.mock(JsonParser.class), context);
 assertEquals(expectedAddress, actualAddress);
 }

 // Testing for InetSocketAddress deserializer
 @Test
 public void testInetSocketAddressDeserialization() throws JsonProcessingException, IOException {
 InetSocketAddress expectedAddress = new InetSocketAddress("127.0.0.1", 8080);
 FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 String inputString = "[127.0.0.1]:8080";
 InetSocketAddress actualAddress = inetSocketAddressDeserializer.deserialize(Mockito.mock(JsonParser.class), context);
 assertEquals(expectedAddress, actualAddress);
 }

 // Testing for empty String input
 @Test(expected = JsonMappingException.class)
 public void testEmptyStringDeserialization() throws JsonProcessingException, IOException {
 FromStringDeserializer<String> emptyStringDeserializer = new FromStringDeserializer<String>(String.class);
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 String inputString = "";
 emptyStringDeserializer.deserialize(Mockito.mock(JsonParser.class), context);
 fail("Should throw JsonMappingException");
 }

 // Testing for non-supported class
 @Test(expected = JsonMappingException.class)
 public void testNonSupportedClassDeserialization() throws JsonProcessingException, IOException {
 FromStringDeserializer<Object> nonSupportedClassDeserializer = new FromStringDeserializer<Object>(Object.class);
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 String inputString = "java.util.ArrayList";
 nonSupportedClassDeserializer.deserialize(Mockito.mock(JsonParser.class), context);
 fail("Should throw JsonMappingException");
 }

 // Testing for null input
 @Test(expected = JsonMappingException.class)
 public void testNullDeserialization() throws JsonProcessingException, IOException {
 FromStringDeserializer<String> nullDeserializer = new FromStringDeserializer<String>(String.class);
 ObjectMapper mapper = new ObjectMapper();
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 String inputString = null;
 nullDeserializer.deserialize(Mockito.mock(JsonParser.class), context);
 fail("Should throw JsonMappingException");
 }
}