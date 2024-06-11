package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

 // Test for deserializing a String to File
 @Test
 public void testDeserializeStringToFile() throws Exception {
 File expected = new File("testFile.txt");
 String input = "testFile.txt";
 FromStringDeserializer<File> fileDeserializer = new FromStringDeserializer<File>(File.class);
 ObjectMapper mapper = new ObjectMapper();
 File result = mapper.readValue(mapper.textParser(input), fileDeserializer.getType());
 assertEquals(expected.getAbsolutePath(), result.getAbsolutePath());
 }

 // Test for deserializing a String to URL
 @Test
 public void testDeserializeStringToURL() throws Exception {
 String input = "http://test.com";
 FromStringDeserializer<URL> urlDeserializer = new FromStringDeserializer<URL>(URL.class);
 ObjectMapper mapper = new ObjectMapper();
 URL result = mapper.readValue(mapper.textParser(input), urlDeserializer.getType());
 assertEquals(new URI(input).toURL(), result);
 }

 // Test for deserializing a String to URI
 @Test
 public void testDeserializeStringToURI() throws Exception {
 String input = "http://test.com";
 FromStringDeserializer<URI> uriDeserializer = new FromStringDeserializer<URI>(URI.class);
 ObjectMapper mapper = new ObjectMapper();
 URI result = mapper.readValue(mapper.textParser(input), uriDeserializer.getType());
 assertEquals(new URI(input), result);
 }

 // Test for deserializing a String to Class
 @Test
 public void testDeserializeStringToClass() throws Exception {
 String input = "java.lang.String";
 FromStringDeserializer<Class<?>> classDeserializer = new FromStringDeserializer<Class<?>>(Class.class);
 ObjectMapper mapper = new ObjectMapper();
 Class<?> result = mapper.readValue(mapper.textParser(input), classDeserializer.getType());
 assertEquals(Class.forName(input), result);
 }

 // Test for deserializing a String to JavaType
 @Test
 public void testDeserializeStringToJavaType() throws Exception {
 String input = "java.util.List<java.lang.String>";
 ObjectMapper mapper = new ObjectMapper();
 JavaType javaType = mapper.getTypeFactory().constructType(input);
 FromStringDeserializer<JavaType> javaTypeDeserializer = new FromStringDeserializer<JavaType>(JavaType.class);
 Object result = mapper.readValue(mapper.textParser(javaType.toString()), javaTypeDeserializer.getType());
 assertEquals(javaType, result);
 }

 // Test for deserializing a String to Currency
 @Test
 public void testDeserializeStringToCurrency() throws Exception {
 String input = "USD";
 FromStringDeserializer<Currency> currencyDeserializer = new FromStringDeserializer<Currency>(Currency.class);
 ObjectMapper mapper = new ObjectMapper();
 Currency result = mapper.readValue(mapper.textParser(input), currencyDeserializer.getType());
 assertEquals(Currency.getInstance(input), result);
 }

 // Test for deserializing a String to Pattern
 @Test
 public void testDeserializeStringToPattern() throws Exception {
 String input = "\\d+";
 FromStringDeserializer<Pattern> patternDeserializer = new FromStringDeserializer<Pattern>(Pattern.class);
 ObjectMapper mapper = new ObjectMapper();
 Pattern result = mapper.readValue(mapper.textParser(input), patternDeserializer.getType());
 assertEquals(Pattern.compile(input), result);
 }

 // Test for deserializing a String to Locale
 @Test
 public void testDeserializeStringToLocale() throws Exception {
 String input = "en_US";
 FromStringDeserializer<Locale> localeDeserializer = new FromStringDeserializer<Locale>(Locale.class);
 ObjectMapper mapper = new ObjectMapper();
 Locale result = mapper.readValue(mapper.textParser(input), localeDeserializer.getType());
 assertEquals(new Locale("en", "US"), result);
 }

 // Test for deserializing a String to Charset
 @Test
 public void testDeserializeStringToCharset() throws Exception {
 String input = "UTF-8";
 FromStringDeserializer<Charset> charsetDeserializer = new FromStringDeserializer<Charset>(Charset.class);
 ObjectMapper mapper = new ObjectMapper();
 Charset result = mapper.readValue(mapper.textParser(input), charsetDeserializer.getType());
 assertEquals(Charset.forName(input), result);
 }

 // Test for deserializing a String to TimeZone
 @Test
 public void testDeserializeStringToTimeZone() throws Exception {
 String input = "America/New_York";
 FromStringDeserializer<TimeZone> timeZoneDeserializer = new FromStringDeserializer<TimeZone>(TimeZone.class);
 ObjectMapper mapper = new ObjectMapper();
 TimeZone result = mapper.readValue(mapper.textParser(input), timeZoneDeserializer.getType());
 assertEquals(TimeZone.getTimeZone(input), result);
 }

 // Test for deserializing a String to InetAddress
 @Test
 public void testDeserializeStringToInetAddress() throws Exception {
 String input = "192.168.1.1";
 FromStringDeserializer<InetAddress> inetAddressDeserializer = new FromStringDeserializer<InetAddress>(InetAddress.class);
 ObjectMapper mapper = new ObjectMapper();
 InetAddress result = mapper.readValue(mapper.textParser(input), inetAddressDeserializer.getType());
 assertEquals(InetAddress.getByName(input), result);
 }

 // Test for deserializing a String to InetSocketAddress
 @Test
 public void testDeserializeStringToInetSocketAddress() throws Exception {
 String input = "[255.255.255.255]:80";
 FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);
 ObjectMapper mapper = new ObjectMapper();
 InetSocketAddress result = mapper.readValue(mapper.textParser(input), inetSocketAddressDeserializer.getType());
 assertEquals(new InetSocketAddress(InetAddress.getByName("255.255.255.255"), 80), result);
 }

 // Test for deserializing a String to StringBuilder
 @Test
 public void testDeserializeStringToStringBuilder() throws Exception {
 String input = "Hello, World!";
 FromStringDeserializer<StringBuilder> stringBuilderDeserializer = new FromStringDeserializer<StringBuilder>(StringBuilder.class);
 ObjectMapper mapper = new ObjectMapper();
 StringBuilder result = mapper.readValue(mapper.textParser(input), stringBuilderDeserializer.getType());
 assertEquals(new StringBuilder(input), result);
 }

 // Test for deserializing an empty String
 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmptyString() throws Exception {
 String input = "";
 FromStringDeserializer<File> fileDeserializer = new FromStringDeserializer<File>(File.class);
 ObjectMapper mapper = new ObjectMapper();
 mapper.readValue(mapper.textParser(input), fileDeserializer.getType());
 }

 // Test for deserializing a String to an unknown type
 @Test(expected = JsonMappingException.class)
 public void testDeserializeUnknownType() throws Exception {
 String input = "com.example.MyClass";
 FromStringDeserializer<Object> objectDeserializer = new FromStringDeserializer<Object>(Object.class);
 ObjectMapper mapper = new ObjectMapper();
 mapper.readValue(mapper.textParser(input), objectDeserializer.getType());
 }

 // Test for deserializing a String with an unsupported type
 @Test(expected = JsonMappingException.class)
 public void testDeserializeUnsupportedType() throws Exception {
 String input = "[1, 2, 3]";
 FromStringDeserializer<File> fileDeserializer = new FromStringDeserializer<File>(File.class);
 ObjectMapper mapper = new ObjectMapper();
 mapper.readValue(mapper.textParser(input), fileDeserializer.getType());
 }

 // Test for deserializing a String with an invalid format for a particular type
 @Test(expected = InvalidFormatException.class)
 public void testDeserializeInvalidFormat() throws Exception {
 String input = "invalid_date";
 FromStringDeserializer<Date> dateDeserializer = new FromStringDeserializer<Date>(Date.class);
 ObjectMapper mapper = new ObjectMapper();
 mapper.readValue(mapper.textParser(input), dateDeserializer.getType());
 }

 // Test for deserializing a String with an invalid format for the Charset type
 @Test(expected = MalformedURLException.class)
 public void testDeserializeInvalidCharsetFormat() throws Exception {
 String input = "UTF%-8";
 FromStringDeserializer<Charset> charsetDeserializer = new FromStringDeserializer<Charset>(Charset.class);
 ObjectMapper mapper = new ObjectMapper();
 mapper.readValue(mapper.textParser(input), charsetDeserializer.getType());
 }

 // Test for deserializing a String with an invalid format for the TimeZone type
 @Test(expected = IllegalArgumentException.class)
 public void testDeserializeInvalidTimeZoneFormat() throws Exception {
 String input = "InvalidTimeZone";
 FromStringDeserializer<TimeZone> timeZoneDeserializer = new FromStringDeserializer<TimeZone>(TimeZone.class);
 ObjectMapper mapper = new ObjectMapper();
 mapper.readValue(mapper.textParser(input), timeZoneDeserializer.getType());
 }

 // Test for deserializing a String with an invalid format for the InetAddress type
 @Test(expected = MalformedURLException.class)
 public void testDeserializeInvalidInetAddressFormat() throws Exception {
 String input = "256.256.256.256";
 FromStringDeserializer<InetAddress> inetAddressDeserializer = new FromStringDeserializer<InetAddress>(InetAddress.class);
 ObjectMapper mapper = new ObjectMapper();
 mapper.readValue(mapper.textParser(input), inetAddressDeserializer.getType());
 }

 // Test for deserializing a String with an invalid format for the InetSocketAddress type
 @Test(expected = MalformedURLException.class)
 public void testDeserializeInvalidInetSocketAddressFormat() throws Exception {
 String input = "[256.256.256.256]:80";
 FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);
 ObjectMapper mapper = new ObjectMapper();
 mapper.readValue(mapper.textParser(input), inetSocketAddressDeserializer.getType());
 }
}