package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FromStringDeserializer.class})
public class FromStringDeserializerTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testDeserializeFile() throws IOException {
 FromStringDeserializer<File> fileDeserializer = new FromStringDeserializer<File>(File.class);
 String jsonString = "\"/tmp/test.txt\"";
 File expected = new File("/tmp/test.txt");
 JsonParser parser = new JsonFactory().createParser(jsonString);
 DeserializationContext context = objectMapper.getDeserializationContext();
 File result = fileDeserializer.deserialize(parser, context);
 assertEquals(expected.getAbsolutePath(), result.getAbsolutePath());
 }

 @Test
 public void testDeserializeURL() throws IOException {
 FromStringDeserializer<URL> urlDeserializer = new FromStringDeserializer<URL>(URL.class);
 String jsonString = "\"http://test.com\"";
 URL expected = new URL("http://test.com");
 JsonParser parser = new JsonFactory().createParser(jsonString);
 DeserializationContext context = objectMapper.getDeserializationContext();
 URL result = urlDeserializer.deserialize(parser, context);
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testDeserializeURI() throws IOException {
 FromStringDeserializer<URI> uriDeserializer = new FromStringDeserializer<URI>(URI.class);
 String jsonString = "\"http://test.com/path/to/file\"";
 URI expected = new URI("http://test.com/path/to/file");
 JsonParser parser = new JsonFactory().createParser(jsonString);
 DeserializationContext context = objectMapper.getDeserializationContext();
 URI result = uriDeserializer.deserialize(parser, context);
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testDeserializeClass() throws IOException {
 FromStringDeserializer<Object> classDeserializer = new FromStringDeserializer<Object>(Object.class);
 String jsonString = "\"java.lang.String\"";
 Class<?> expected = String.class;
 JsonParser parser = new JsonFactory().createParser(jsonString);
 DeserializationContext context = objectMapper.getDeserializationContext();
 Class<?> result = classDeserializer.deserialize(parser, context);
 assertEquals(expected.getName(), result.getName());
 }

 @Test
 public void testDeserializeJavaType() throws IOException {
 FromStringDeserializer<JavaType> javaTypeDeserializer = new FromStringDeserializer<JavaType>(JavaType.class);
 String jsonString = "\"com.test.MyClass\"";
 JavaType expected = objectMapper.getTypeFactory().constructType(JavaType.class, "com.test.MyClass");
 JsonParser parser = new JsonFactory().createParser(jsonString);
 DeserializationContext context = objectMapper.getDeserializationContext();
 JavaType result = javaTypeDeserializer.deserialize(parser, context);
 assertEquals(expected.getRawClass(), result.getRawClass());
 }

 @Test
 public void testDeserializeCurrency() throws IOException {
 FromStringDeserializer<Currency> currencyDeserializer = new FromStringDeserializer<Currency>(Currency.class);
 String jsonString = "\"USD\"";
 Currency expected = Currency.getInstance("USD");
 JsonParser parser = new JsonFactory().createParser(jsonString);
 DeserializationContext context = objectMapper.getDeserializationContext();
 Currency result = currencyDeserializer.deserialize(parser, context);
 assertEquals(expected.getCurrencyCode(), result.getCurrencyCode());
 }

 @Test
 public void testDeserializePattern() throws IOException {
 FromStringDeserializer<Pattern> patternDeserializer = new FromStringDeserializer<Pattern>(Pattern.class);
 String jsonString = "/[a-z]+/i";
 Pattern expected = Pattern.compile("/[a-z]+/i");
 JsonParser parser = new JsonFactory().createParser(jsonString);
 DeserializationContext context = objectMapper.getDeserializationContext();
 Pattern result = patternDeserializer.deserialize(parser, context);
 assertEquals(expected.pattern(), result.pattern());
 }

 @Test
 public void testDeserializeLocale() throws IOException {
 FromStringDeserializer<Locale> localeDeserializer = new FromStringDeserializer<Locale>(Locale.class);
 String jsonString = "\"en_US\"";
 Locale expected = Locale.forLanguageTag("en_US");
 JsonParser parser = new JsonFactory().createParser(jsonString);
 DeserializationContext context = objectMapper.getDeserializationContext();
 Locale result = localeDeserializer.deserialize(parser, context);
 assertEquals(expected.getLanguage(), result.getLanguage());
 assertEquals(expected.getCountry(), result.getCountry());
 }

 @Test
 public void testDeserializeCharset() throws IOException {
 FromStringDeserializer<Charset> charsetDeserializer = new FromStringDeserializer<Charset>(Charset.class);
 String jsonString = "\"UTF-8\"";
 Charset expected = Charset.forName("UTF-8");
 JsonParser parser = new JsonFactory().createParser(jsonString);
 DeserializationContext context = objectMapper.getDeserializationContext();
 Charset result = charsetDeserializer.deserialize(parser, context);
 assertEquals(expected.name(), result.name());
 }

 @Test
 public void testDeserializeTimeZone() throws IOException {
 FromStringDeserializer<TimeZone> timeZoneDeserializer = new FromStringDeserializer<TimeZone>(TimeZone.class);
 String jsonString = "\"America/Los_Angeles\"";
 TimeZone expected = TimeZone.getTimeZone("America/Los_Angeles");
 JsonParser parser = new JsonFactory().createParser(jsonString);
 DeserializationContext context = objectMapper.getDeserializationContext();
 TimeZone result = timeZoneDeserializer.deserialize(parser, context);
 assertEquals(expected.getID(), result.getID());
 }

 @Test
 public void testDeserializeInetAddress() throws IOException {
 FromStringDeserializer<InetAddress> inetAddressDeserializer = new FromStringDeserializer<InetAddress>(InetAddress.class);
 String jsonString = "\"192.168.1.1\"";
 InetAddress expected = InetAddress.getByName("192.168.1.1");
 JsonParser parser = new JsonFactory().createParser(jsonString);
 DeserializationContext context = objectMapper.getDeserializationContext();
 InetAddress result = inetAddressDeserializer.deserialize(parser, context);
 assertEquals(expected.getHostAddress(), result.getHostAddress());
 }

 @Test
 public void testDeserializeInetSocketAddress() throws IOException {
 FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);
 String jsonStringIPv4 = "\"192.168.1.1:80\"";
 InetSocketAddress expectedIPv4 = new InetSocketAddress("192.168.1.1", 80);
 String jsonStringIPv6 = "[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:80";
 InetSocketAddress expectedIPv6 = new InetSocketAddress(new InetSocketAddress(URI.create("[" + jsonStringIPv6 + "]")).getAddress(), 80);
 JsonParser parserIPv4 = new JsonFactory().createParser(jsonStringIPv4);
 JsonParser parserIPv6 = new JsonFactory().createParser(jsonStringIPv6);
 DeserializationContext context = objectMapper.getDeserializationContext();
 InetSocketAddress resultIPv4 = inetSocketAddressDeserializer.deserialize(parserIPv4, context);
 InetSocketAddress resultIPv6 = inetSocketAddressDeserializer.deserialize(parserIPv6, context);
 assertEquals(expectedIPv4.getAddress().getHostAddress(), resultIPv4.getAddress().getHostAddress());
 assertEquals(expectedIPv4.getPort(), resultIPv4.getPort());
 assertEquals(expectedIPv6.getAddress().getHostAddress(), resultIPv6.getAddress().getHostAddress());
 assertEquals(expectedIPv6.getPort(), resultIPv6.getPort());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidType() throws IOException {
 FromStringDeserializer<Object> invalidDeserializer = new FromStringDeserializer<Object>(int.class);
 String jsonString = "42";
 JsonParser parser = new JsonFactory().createParser(jsonString);
 DeserializationContext context = objectMapper.getDeserializationContext();
 Object result = invalidDeserializer.deserialize(parser, context);
 assertNotNull(result); // should not reach here
 }
}