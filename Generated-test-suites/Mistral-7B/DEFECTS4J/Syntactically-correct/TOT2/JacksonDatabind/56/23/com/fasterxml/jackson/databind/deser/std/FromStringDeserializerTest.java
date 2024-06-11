package com.example.junit;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testDeserializeFile() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
 File expected = new File("test.txt");
 String json = "{\"className\":\"File\",\"name\":\"test.txt\"}";
 File result = deserializer.deserialize(MAPPER.readTree(json), MAPPER.getDeserializationContext());
 assertEquals(expected.getAbsolutePath(), result.getAbsolutePath());
 }

 @Test
 public void testDeserializeUrl() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
 String json = "{\"className\":\"URL\",\"stringRepresentation\":\"https://www.example.com\"}";
 URL expected = new URL("https://www.example.com");
 URL result = deserializer.deserialize(MAPPER.readTree(json), MAPper.getDeserializationContext());
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testDeserializeUri() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
 String json = "{\"className\":\"URI\",\"stringRepresentation\":\"http://www.example.com/path/to/file.txt\"}";
 URI expected = new URI("http://www.example.com/path/to/file.txt");
 URI result = deserializer.deserialize(MAPper.readTree(json), MAPper.getDeserializationContext());
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeClass() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
 String json = "{\"className\":\"java.lang.String\",\"stringRepresentation\":\"Hello, World!\"}";
 String result = deserializer.deserialize(MAPper.readTree(json), MAPper.getDeserializationContext());
 assertEquals("Hello, World!", result);
 }

 @Test
 public void testDeserializeJavaType() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Integer[].class);
 String json = "{\"className\":\"java.lang.Class\",\"name\":\"java.lang.Integer\\[\"}";
 Class<JavaType> javaTypeClass = JavaType.class;
 JavaType expected = MAPper.getTypeFactory().constructType(Integer[].class);
 JavaType result = deserializer.deserialize(MAPper.readTree(json), MAPper.getDeserializationContext());
 assertEquals(expected.getRawClass(), result.getRawClass());
 }

 @Test
 public void testDeserializeCurrency() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
 String json = "{\"className\":\"java.util.Currency\",\"currencyCode\":\"USD\"}";
 Currency expected = Currency.getInstance("USD");
 Currency result = deserializer.deserialize(MAPper.readTree(json), MAPper.getDeserializationContext());
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializePattern() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
 String json = "{\"className\":\"java.util.regex.Pattern\",\"regex\":\"\\w+\\.\\w+\"}";
 Pattern expected = Pattern.compile("\\w+\\.\\w+");
 Pattern result = deserializer.deserialize(MAPper.readTree(json), MAPper.getDeserializationContext());
 assertEquals(expected.pattern(), result.pattern());
 }

 @Test
 public void testDeserializeLocale() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
 String json = "{\"className\":\"java.util.Locale\",\"language\":\"fr_FR\"}";
 Locale expected = new Locale("fr", "FR");
 Locale result = deserializer.deserialize(MAPper.readTree(json), MAPper.getDeserializationContext());
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeCharset() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
 String json = "{\"className\":\"java.nio.charset.Charset\",\"name\":\"UTF-8\"}";
 Charset expected = Charset.forName("UTF-8");
 Charset result = deserializer.deserialize(MAPper.readTree(json), MAPper.getDeserializationContext());
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeTimeZone() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
 String json = "{\"className\":\"java.util.TimeZone\",\"id\":\"America/New_York\"}";
 TimeZone expected = TimeZone.getTimeZone("America/New_York");
 TimeZone result = deserializer.deserialize(MAPper.readTree(json), MAPper.getDeserializationContext());
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeInetAddress() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
 String json = "{\"className\":\"java.net.InetAddress\",\"ipAddress\":\"192.168.1.1\"}";
 InetAddress expected = InetAddress.getByName("192.168.1.1");
 InetAddress result = deserializer.deserialize(MAPper.readTree(json), MAPper.getDeserializationContext());
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeInetSocketAddress() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
 String json1 = "{\"className\":\"java.net.InetSocketAddress\",\"ipAddress\":\"192.168.1.1\",\"port\":1234}";
 InetSocketAddress expected1 = new InetSocketAddress("192.168.1.1", 1234);
 InetSocketAddress result1 = deserializer.deserialize(MAPper.readTree(json1), MAPper.getDeserializationContext());
 assertEquals(expected1, result1);

 String json2 = "{\"className\":\"java.net.InetSocketAddress\",\"ipAddress\":\"[2001:0db8:85a3:0000:0000:8a2e:0370:7334]\",\"port\":5678}";
 InetSocketAddress expected2 = new InetSocketAddress(java.net.InetAddress.getByName("[2001:0db8:85a3:0000:0000:8a2e:0370:7334]"), 5678);
 InetSocketAddress result2 = deserializer.deserialize(MAPper.readTree(json2), MAPper.getDeserializationContext());
 assertEquals(expected2, result2);
 }

 @Test
 public void testDeserializeEmptyString() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
 String json = "{\"className\":\"java.lang.String\"}";
 String result = deserializer.deserialize(MAPper.readTree(json), MAPper.getDeserializationContext());
 assertEquals("", result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidType() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Integer.class);
 String json = "{\"className\":\"java.util.Locale\",\"locale\":\"en_US\"}";
 deserializer.deserialize(MAPper.readTree(json), MAPper.getDeserializationContext());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidInput() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Integer.class);
 String json = "{\"className\":\"java.lang.String\",\"invalidInput\":\"not_a_number\"}";
 deserializer.deserialize(MAPper.readTree(json), MAPper.getDeserializationContext());
 }

}