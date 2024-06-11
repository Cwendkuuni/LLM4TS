package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

public class FromStringDeserializerTest {

 private static ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testDeserializeFile() throws JsonProcessingException {
 File file = new File("testFile.txt");
 FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
 File deserializedObject = deserializer.deserialize(objectMapper.getFactory().createParser("\"testFile.txt\""), null);
 assertEquals(file, deserializedObject);
 }

 @Test
 public void testDeserializeUrl() throws JsonProcessingException {
 URL url = new URL("http://www.example.com");
 FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
 URL deserializedObject = deserializer.deserialize(objectMapper.getFactory().createParser("\"http://www.example.com\""), null);
 assertEquals(url, deserializedObject);
 }

 @Test
 public void testDeserializeUri() throws JsonProcessingException {
 URI uri = new URI("http://www.example.com");
 FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
 URI deserializedObject = deserializer.deserialize(objectMapper.getFactory().createParser("\"http://www.example.com\""), null);
 assertEquals(uri, deserializedObject);
 }

 @Test
 public void testDeserializeClass() throws JsonProcessingException, InstantiationException, IllegalAccessException {
 Class<?> clazz = String.class;
 FromStringDeserializer<Class<?>> deserializer = FromStringDeserializer.findDeserializer(Class.class);
 Class<?> deserializedObject = (Class<?>) deserializer.deserialize(objectMapper.getFactory().createParser("\"java.lang.String\""), null);
 assertEquals(clazz, deserializedObject);
 }

 @Test
 public void testDeserializeJavaType() throws JsonProcessingException {
 JavaType javaType = objectMapper.getTypeFactory().constructType(int[].class);
 FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
 JavaType deserializedObject = deserializer.deserialize(objectMapper.getFactory().createParser("\"int[]\""), null);
 assertEquals(javaType, deserializedObject);
 }

 @Test
 public void testDeserializeCurrency() throws JsonProcessingException {
 Currency currency = Currency.getInstance("USD");
 FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
 Currency deserializedObject = deserializer.deserialize(objectMapper.getFactory().createParser("\"USD\""), null);
 assertEquals(currency, deserializedObject);
 }

 @Test
 public void testDeserializePattern() throws JsonProcessingException {
 Pattern pattern = Pattern.compile("(.*?)");
 FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
 Pattern deserializedObject = deserializer.deserialize(objectMapper.getFactory().createParser("\"(.*?)\""), null);
 assertEquals(pattern, deserializedObject);
 }

 @Test
 public void testDeserializeLocale() throws JsonProcessingException {
 Locale locale = new Locale("en_US");
 FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
 Locale deserializedObject = deserializer.deserialize(objectMapper.getFactory().createParser("\"en_US\""), null);
 assertEquals(locale, deserializedObject);
 }

 @Test
 public void testDeserializeCharset() throws JsonProcessingException {
 Charset charset = Charset.forName("UTF-8");
 FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
 Charset deserializedObject = deserializer.deserialize(objectMapper.getFactory().createParser("\"UTF-8\""), null);
 assertEquals(charset, deserializedObject);
 }

 @Test
 public void testDeserializeTimeZone() throws JsonProcessingException {
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
 TimeZone deserializedObject = deserializer.deserialize(objectMapper.getFactory().createParser("\"UTC\""), null);
 assertEquals(timeZone, deserializedObject);
 }

 @Test
 public void testDeserializeInetAddress() throws JsonProcessingException {
 InetAddress inetAddress = InetAddress.getByName("192.168.1.1");
 FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
 InetAddress deserializedObject = deserializer.deserialize(objectMapper.getFactory().createParser("\"192.168.1.1\""), null);
 assertEquals(inetAddress, deserializedObject);
 }

 @Test
 public void testDeserializeInetSocketAddress() throws JsonProcessingException {
 InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 8080);
 FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
 InetSocketAddress deserializedObject = deserializer.deserialize(objectMapper.getFactory().createParser("\"localhost:8080\""), null);
 assertEquals(inetSocketAddress, deserializedObject);
 }

 @Test
 public void testDeserializeStringBuilder() throws JsonProcessingException {
 StringBuilder stringBuilder = new StringBuilder("Hello, World!");
 FromStringDeserializer<StringBuilder> deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
 StringBuilder deserializedObject = deserializer.deserialize(objectMapper.getFactory().createParser("\"Hello, World!\""), null);
 assertEquals(stringBuilder, deserializedObject);
 }

 @Test (expected = JsonMappingException.class)
 public void testDeserializeInvalidType() throws JsonProcessingException {
 FromStringDeserializer<Integer> deserializer = FromStringDeserializer.findDeserializer(Integer.class);
 deserializer.deserialize(objectMapper.getFactory().createParser("\"Hello, World!\""), null);
 }

 @Test (expected = MalformedURLException.class)
 public void testDeserializeInvalidUrl() throws JsonProcessingException {
 FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
 deserializer.deserialize(objectMapper.getFactory().createParser("\"invalidUrl\""), null);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testDeserializeInvalidLocale() throws JsonProcessingException {
 FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
 deserializer.deserialize(objectMapper.getFactory().createParser("\"invalidLocale\""), null);
 }
}