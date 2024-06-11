package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setUp() {
 objectMapper = new ObjectMapper();
 }

 @After
 public void tearDown() {
 objectMapper = null;
 }

 @Test
 public void testDeserializeFile() throws IOException {
 Class<FromStringDeserializer.Std.StdFile> fileDeserializerType = FromStringDeserializer.Std.File.class;
 FromStringDeserializer<File> fileDeserializer = new FromStringDeserializer<File>(File.class);
 File expected = new File("test.txt");
 String input = "'/test.txt'";
 File actual = fileDeserializer.deserialize(objectMapper.getFactory().createParser(input), objectMapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeUrl() throws IOException {
 Class<FromStringDeserializer.Std.StdUrl> urlDeserializerType = FromStringDeserializer.Std.Url.class;
 FromStringDeserializer<URL> urlDeserializer = new FromStringDeserializer<URL>(URL.class);
 URL expected = new URL("http://example.com");
 String input = "http://example.com";
 URL actual = urlDeserializer.deserialize(objectMapper.getFactory().createParser(input), objectMapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeUri() throws IOException {
 Class<FromStringDeserializer.Std.StdUri> uriDeserializerType = FromStringDeserializer.Std.Uri.class;
 FromStringDeserializer<URI> uriDeserializer = new FromStringDeserializer<URI>(URI.class);
 URI expected = new URI("http://example.com");
 String input = "http://example.com";
 URI actual = uriDeserializer.deserialize(objectMapper.getFactory().createParser(input), objectMapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeClass() throws IOException {
 Class<FromStringDeserializer.Std.StdClass> classDeserializerType = FromStringDeserializer.Std.Class.class;
 FromStringDeserializer<Class<?>> classDeserializer = new FromStringDeserializer<Class<?>>(Class.class);
 Class<?> expected = String.class;
 String input = "java.lang.String";
 Class<?> actual = classDeserializer.deserialize(objectMapper.getFactory().createParser(input), objectMapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeJavaType() throws IOException {
 Class<FromStringDeserializer.Std.StdJavaType> javaTypeDeserializerType = FromStringDeserializer.Std.JavaType.class;
 FromStringDeserializer<JavaType> javaTypeDeserializer = new FromStringDeserializer<JavaType>(JavaType.class);
 JavaType expected = objectMapper.getTypeFactory().constructType(String[].class);
 String input = "java.lang.Class<java.lang.String[]>";
 JavaType actual = javaTypeDeserializer.deserialize(objectMapper.getFactory().createParser(input), objectMapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeCurrency() throws IOException {
 Class<FromStringDeserializer.Std.StdCurrency> currencyDeserializerType = FromStringDeserializer.Std.Currency.class;
 FromStringDeserializer<Currency> currencyDeserializer = new FromStringDeserializer<Currency>(Currency.class);
 Currency expected = Currency.getInstance("USD");
 String input = "USD";
 Currency actual = currencyDeserializer.deserialize(objectMapper.getFactory().createParser(input), objectMapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializePattern() throws IOException {
 Class<FromStringDeserializer.Std.StdPattern> patternDeserializerType = FromStringDeserializer.Std.Pattern.class;
 FromStringDeserializer<Pattern> patternDeserializer = new FromStringDeserializer<Pattern>(Pattern.class);
 Pattern expected = Pattern.compile("\\d+");
 String input = "\\d+";
 Pattern actual = patternDeserializer.deserialize(objectMapper.getFactory().createParser(input), objectMapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeLocale() throws IOException {
 Class<FromStringDeserializer.Std.StdLocale> localeDeserializerType = FromStringDeserializer.Std.Locale.class;
 FromStringDeserializer<Locale> localeDeserializer = new FromStringDeserializer<Locale>(Locale.class);
 Locale expected = new Locale("en", "US");
 String input = "en_US";
 Locale actual = localeDeserializer.deserialize(objectMapper.getFactory().createParser(input), objectMapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeCharset() throws IOException {
 Class<FromStringDeserializer.Std.StdCharset> charsetDeserializerType = FromStringDeserializer.Std.Charset.class;
 FromStringDeserializer<Charset> charsetDeserializer = new FromStringDeserializer<Charset>(Charset.class);
 Charset expected = Charset.forName("UTF-8");
 String input = "UTF-8";
 Charset actual = charsetDeserializer.deserialize(objectMapper.getFactory().createParser(input), objectMapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeTimeZone() throws IOException {
 Class<FromStringDeserializer.Std.StdTimeZone> timeZoneDeserializerType = FromStringDeserializer.Std.TimeZone.class;
 FromStringDeserializer<TimeZone> timeZoneDeserializer = new FromStringDeserializer<TimeZone>(TimeZone.class);
 TimeZone expected = TimeZone.getTimeZone("UTC");
 String input = "UTC";
 TimeZone actual = timeZoneDeserializer.deserialize(objectMapper.getFactory().createParser(input), objectMapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeInetAddress() throws IOException {
 Class<FromStringDeserializer.Std.StdInetAddress> inetAddressDeserializerType = FromStringDeserializer.Std.InetAddress.class;
 FromStringDeserializer<InetAddress> inetAddressDeserializer = new FromStringDeserializer<InetAddress>(InetAddress.class);
 InetAddress expected = InetAddress.getByName("localhost");
 String input = "127.0.0.1";
 InetAddress actual = inetAddressDeserializer.deserialize(objectMapper.getFactory().createParser(input), objectMapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeInetSocketAddress() throws IOException {
 Class<FromStringDeserializer.Std.StdInetSocketAddress> inetSocketAddressDeserializerType = FromStringDeserializer.Std.InetSocketAddress.class;
 FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);
 InetSocketAddress expected = new InetSocketAddress("localhost", 80);
 String input = "[::1]:80";
 InetSocketAddress actual = inetSocketAddressDeserializer.deserialize(objectMapper.getFactory().createParser(input), objectMapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidType() throws IOException {
 FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Integer.class);
 String input = "42";
 deserializer.deserialize(objectMapper.getFactory().createParser(input), objectMapper.getDeserializationContext());
 fail("Should throw JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeArray() throws IOException {
 Class<FromStringDeserializer.Std.StdFile[]> arrayDeserializerType = FromStringDeserializer.Std.File[].class;
 FromStringDeserializer<FromStringDeserializer.Std.StdFile[]> arrayDeserializer = new FromStringDeserializer<FromStringDeserializer.Std.StdFile[]>(arrayDeserializerType);
 String input = "[\"test1.txt\", \"test2.txt\"]";
 FromStringDeserializer.Std.StdFile[] expected = new FromStringDeserializer.Std.StdFile[]{new File("test1.txt"), new File("test2.txt")};
 FromStringDeserializer.Std.StdFile[] actual = arrayDeserializer.deserialize(objectMapper.getFactory().createParser(input), objectMapper.getDeserializationContext());
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testDeserializeEmptyString() throws IOException {
 FromStringDeserializer<String> stringDeserializer = new FromStringDeserializer<String>(String.class);
 String input = "";
 String actual = stringDeserializer._deserializeFromEmptyString();
 assertNull(actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmptyStringWithStringType() throws IOException {
 FromStringDeserializer<String> stringDeserializer = new FromStringDeserializer<String>(String.class);
 JsonParser parser = objectMapper.getFactory().createParser("\"\"");
 try {
 stringDeserializer.deserialize(parser, objectMapper.getDeserializationContext());
 fail("Should throw JsonMappingException");
 } catch (JsonMappingException e) {
 assertEquals("not a valid textual representation: empty String", e.getMessage());
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmptyStringWithClassType() throws IOException {
 FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(String.class);
 String input = "";
 deserializer.deserialize(objectMapper.getFactory().createParser(input), objectMapper.getDeserializationContext());
 fail("Should throw JsonMappingException");
 }

 @Test
 public void testDeserializeNull() throws IOException {
 FromStringDeserializer<String> stringDeserializer = new FromStringDeserializer<String>(String.class);
 String input = "null";
 String actual = stringDeserializer.deserialize(objectMapper.getFactory().createParser(input), objectMapper.getDeserializationContext());
 assertNull(actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeNullWithStringType() throws IOException {
 FromStringDeserializer<String> stringDeserializer = new FromStringDeserializer<String>(String.class);
 JsonParser parser = objectMapper.getFactory().createParser("null");
 try {
 stringDeserializer.deserialize(parser, objectMapper.getDeserializationContext());
 fail("Should throw JsonMappingException");
 } catch (JsonMappingException e) {
 assertEquals("not a valid textual representation: null (through reference value)", e.getMessage());
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeNullWithClassType() throws IOException {
 FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(String.class);
 String input = "null";
 deserializer.deserialize(objectMapper.getFactory().createParser(input), objectMapper.getDeserializationContext());
 fail("Should throw JsonMappingException");
 }

 @Test
 public void testDeserializeEmbeddedObject() throws IOException {
 FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(String.class);
 String input = "{\"value\": \"test\"}";
 Object expected = "test";
 Object actual = deserializer._deserializeEmbedded(new SimpleObjectProperty(new SimpleObject().setValue("value")), objectMapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmbeddedInvalidObject() throws IOException {
 FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(String.class);
 String input = "{\"invalid\": \"test\"}";
 deserializer.deserializeEmbedded(new SimpleObjectProperty(new SimpleObject().setValue("invalid")), objectMapper.getDeserializationContext());
 fail("Should throw JsonMappingException");
 }
}