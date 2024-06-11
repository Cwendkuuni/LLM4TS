package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

 private ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testDeserializeUrl() throws IOException {
 FromStringDeserializer<URL> urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
 DeserializationContext context = Mockito.spy(objectMapper.getDeserializationContext());
 String urlString = "https://www.google.com";
 JsonParser parser = Mockito.mock(JsonParser.class);
 Mockito.when(parser.getValueAsString()).thenReturn(urlString);

 URL url = (URL) urlDeserializer.deserialize(parser, context);
 assertEquals("https://www.google.com", url.toString());
 }

 @Test
 public void testDeserializeUri() throws IOException {
 FromStringDeserializer<URI> uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
 DeserializationContext context = Mockito.spy(objectMapper.getDeserializationContext());
 String uriString = "http://www.google.com/path/to/file";
 JsonParser parser = Mockito.mock(JsonParser.class);
 Mockito.when(parser.getValueAsString()).thenReturn(uriString);

 URI uri = (URI) uriDeserializer.deserialize(parser, context);
 assertEquals("http", uri.getScheme());
 assertEquals("/path/to/file", uri.getPath());
 }

 @Test
 public void testDeserializeClass() throws IOException {
 FromStringDeserializer<Class<?>> classDeserializer = FromStringDeserializer.findDeserializer(Class.class);
 DeserializationContext context = Mockito.spy(objectMapper.getDeserializationContext());
 String className = "java.lang.String";
 JsonParser parser = Mockito.mock(JsonParser.class);
 Mockito.when(parser.getValueAsString()).thenReturn(className);

 Class<?> clazz = classDeserializer.deserialize(parser, context);
 assertEquals("java.lang.String", clazz.getName());
 }

 @Test
 public void testDeserializeJavaType() throws IOException {
 FromStringDeserializer<JavaType> javaTypeDeserializer = FromStringDeserializer.findDeserializer(JavaType.class);
 DeserializationContext context = Mockito.spy(objectMapper.getDeserializationContext());
 String javaTypeString = "com.example.MyClass";
 JsonParser parser = Mockito.mock(JsonParser.class);
 Mockito.when(parser.getValueAsString()).thenReturn(javaTypeString);

 JavaType javaType = javaTypeDeserializer.deserialize(parser, context);
 assertEquals("com.example.MyClass", javaType.getRawClass().getName());
 }

 @Test
 public void testDeserializeCurrency() throws IOException {
 FromStringDeserializer<Currency> currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
 DeserializationContext context = Mockito.spy(objectMapper.getDeserializationContext());
 String currencyCode = "USD";
 JsonParser parser = Mockito.mock(JsonParser.class);
 Mockito.when(parser.getValueAsString()).thenReturn(currencyCode);

 Currency currency = currencyDeserializer.deserialize(parser, context);
 assertEquals("USD", currency.getCurrencyCode());
 }

 @Test
 public void testDeserializePattern() throws IOException {
 FromStringDeserializer<Pattern> patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
 DeserializationContext context = Mockito.spy(objectMapper.getDeserializationContext());
 String patternString = "^[a-z]+";
 JsonParser parser = Mockito.mock(JsonParser.class);
 Mockito.when(parser.getValueAsString()).thenReturn(patternString);

 Pattern pattern = patternDeserializer.deserialize(parser, context);
 assertEquals("^[a-z]+", pattern.pattern());
 }

 @Test
 public void testDeserializeLocale() throws IOException {
 FromStringDeserializer<Locale> localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
 DeserializationContext context = Mockito.spy(objectMapper.getDeserializationContext());
 String localeString = "en_US";
 JsonParser parser = Mockito.mock(JsonParser.class);
 Mockito.when(parser.getValueAsString()).thenReturn(localeString);

 Locale locale = localeDeserializer.deserialize(parser, context);
 assertEquals("en", locale.getLanguage());
 assertEquals("US", locale.getCountry());
 }

 @Test
 public void testDeserializeCharset() throws IOException {
 FromStringDeserializer<Charset> charsetDeserializer = FromStringDeserializer.findDeserializer(Charset.class);
 DeserializationContext context = Mockito.spy(objectMapper.getDeserializationContext());
 String charsetName = "UTF-8";
 JsonParser parser = Mockito.mock(JsonParser.class);
 Mockito.when(parser.getValueAsString()).thenReturn(charsetName);

 Charset charset = charsetDeserializer.deserialize(parser, context);
 assertEquals("UTF-8", charset.name());
 }

 @Test
 public void testDeserializeTimeZone() throws IOException {
 FromStringDeserializer<TimeZone> timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
 DeserializationContext context = Mockito.spy(objectMapper.getDeserializationContext());
 String timeZoneId = "America/Los_Angeles";
 JsonParser parser = Mockito.mock(JsonParser.class);
 Mockito.when(parser.getValueAsString()).thenReturn(timeZoneId);

 TimeZone timeZone = timeZoneDeserializer.deserialize(parser, context);
 assertEquals("America/Los_Angeles", timeZone.getID());
 }

 @Test
 public void testDeserializeInetAddress() throws IOException {
 FromStringDeserializer<InetAddress> inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
 DeserializationContext context = Mockito.spy(objectMapper.getDeserializationContext());
 String ipAddressString = "192.168.1.1";
 JsonParser parser = Mockito.mock(JsonParser.class);
 Mockito.when(parser.getValueAsString()).thenReturn(ipAddressString);

 InetAddress inetAddress = inetAddressDeserializer.deserialize(parser, context);
 assertNotNull(inetAddress);
 assertEquals("192.168.1.1", inetAddress.getHostAddress());
 }

 @Test
 public void testDeserializeInetSocketAddress() throws IOException {
 FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
 DeserializationContext context = Mockito.spy(objectMapper.getDeserializationContext());
 String inetSocketAddressString = "[255.255.255.255]:80";
 JsonParser parser = Mockito.mock(JsonParser.class);
 Mockito.when(parser.getValueAsString()).thenReturn(inetSocketAddressString);

 InetSocketAddress inetSocketAddress = inetSocketAddressDeserializer.deserialize(parser, context);
 assertNotNull(inetSocketAddress);
 assertEquals("255.255.255.255", inetSocketAddress.getAddress().getHostAddress());
 assertEquals(80, inetSocketAddress.getPort());
 }

 @Test
 public void testDeserializeStringBuilder() throws IOException {
 FromStringDeserializer<StringBuilder> stringBuilderDeserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
 DeserializationContext context = Mockito.spy(objectMapper.getDeserializationContext());
 String stringBuilderString = "Hello, World!";
 JsonParser parser = Mockito.mock(JsonParser.class);
 Mockito.when(parser.getValueAsString()).thenReturn(stringBuilderString);

 StringBuilder stringBuilder = stringBuilderDeserializer.deserialize(parser, context);
 assertEquals("Hello, World!", stringBuilder.toString());
 }

 @Test
 public void testUnexpectedToken() throws IOException {
 FromStringDeserializer<Object> objectDeserializer = FromStringDeserializer.findDeserializer(Object.class);
 DeserializationContext context = Mockito.spy(objectMapper.getDeserializationContext());
 JsonParser parser = Mockito.mock(JsonParser.class);
 JsonToken token = JsonToken.START_ARRAY;
 Mockito.when(parser.getCurrentToken()).thenReturn(token);

 try {
 objectDeserializer.deserialize(parser, context);
 fail("Should have thrown unexpected token exception");
 } catch (IOException e) {
 assertEquals("Unexpected token (START_ARRAY)", e.getMessage());
 }
 }

 @Test
 public void testEmptyString() throws IOException {
 FromStringDeserializer<Object> objectDeserializer = FromStringDeserializer.findDeserializer(Object.class);
 DeserializationContext context = Mockito.spy(objectMapper.getDeserializationContext());
 JsonParser parser = Mockito.mock(JsonParser.class);

 Object result = objectDeserializer.deserialize(parser, context);
 assertNull(result);
 }

 @Test
 public void testDeserializeFromArray() throws IOException {
 FromStringDeserializer<Object> objectDeserializer = FromStringDeserializer.findDeserializer(Object.class);
 DeserializationContext context = Mockito.spy(objectMapper.getDeserializationContext());
 JsonFactory factory = new JsonFactory();
 JsonParser parser = factory.createParser("[]");

 Object result = objectDeserializer.deserialize(parser, context);
 assertArrayEquals(new Object[0], result);
 }

 @Test
 public void testDeserializeEmbeddedObject() throws IOException {
 FromStringDeserializer<Object> objectDeserializer = FromStringDeserializer.findDeserializer(Object.class);
 DeserializationContext context = Mockito.spy(objectMapper.getDeserializationContext());
 Object embeddedObject = new Object();
 JsonParser parser = Mockito.mock(JsonParser.class);
 Mockito.when(parser.getEmbeddedObject()).thenReturn(embeddedObject);

 Object result = objectDeserializer.deserialize(parser, context);
 assertEquals(embeddedObject, result);
 }
}