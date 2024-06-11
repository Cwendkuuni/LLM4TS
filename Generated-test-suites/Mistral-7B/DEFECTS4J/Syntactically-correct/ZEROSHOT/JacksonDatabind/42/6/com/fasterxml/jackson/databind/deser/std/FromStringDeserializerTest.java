package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

 @Test
 public void testFileDeserialization() throws Exception {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
 DeserializationContext context = new TestDeserializationContext();
 File result = deserializer.deserialize(new TestJsonParser("\"/test.txt\""), context);
 assertNotNull(result);
 assertEquals("/test.txt", result.getAbsolutePath());
 }

 @Test
 public void testUrlDeserialization() throws Exception {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
 DeserializationContext context = new TestDeserializationContext();
 URL result = deserializer.deserialize(new TestJsonParser("\"https://test.com\""), context);
 assertNotNull(result);
 assertEquals("https://test.com", result.toString());
 }

 @Test
 public void testUriDeserialization() throws Exception {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
 DeserializationContext context = new TestDeserializationContext();
 URI result = deserializer.deserialize(new TestJsonParser("\"http://test.com/path?query=value#fragment\""), context);
 assertNotNull(result);
 assertEquals("http", result.getScheme());
 assertEquals("/path", result.getPath());
 assertEquals("query=value", result.getQuery());
 assertEquals("fragment", result.getFragment());
 }

 @Test
 public void testClassDeserialization() throws Exception {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(String.class, FromStringDeserializer.Std.STD_CLASS);
 DeserializationContext context = new TestDeserializationContext();
 String result = deserializer.deserialize(new TestJsonParser("\"java.lang.String\""), context);
 assertNotNull(result);
 assertEquals("java.lang.String", result.getClass().getName());
 }

 @Test
 public void testJavaTypeDeserialization() throws Exception {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
 DeserializationContext context = new TestDeserializationContext();
 JavaType result = deserializer.deserialize(new TestJsonParser("\"com.fasterxml.jackson.databind.JsonNode\""), context);
 assertNotNull(result);
 assertEquals("com.fasterxml.jackson.databind.JsonNode", result.getType().toString());
 }

 @Test
 public void testCurrencyDeserialization() throws Exception {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
 DeserializationContext context = new TestDeserializationContext();
 Currency result = deserializer.deserialize(new TestJsonParser("\"USD\""), context);
 assertNotNull(result);
 assertEquals(Currency.getInstance("USD"), result);
 }

 @Test
 public void testPatternDeserialization() throws Exception {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
 DeserializationContext context = new TestDeserializationContext();
 Pattern result = deserializer.deserialize(new TestJsonParser("\"/[A-Za-z]+\\.txt\""), context);
 assertNotNull(result);
 assertPattern(Pattern.compile("/[A-Za-z]+\\.txt"), result);
 }

 @Test
 public void testLocaleDeserialization() throws Exception {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
 DeserializationContext context = new TestDeserializationContext();
 Locale result = deserializer.deserialize(new TestJsonParser("\"en_US\""), context);
 assertNotNull(result);
 assertEquals(Locale.US, result);

 result = deserializer.deserialize(new TestJsonParser("\"nl_NL\""), context);
 assertNotNull(result);
 assertEquals(Locale.NETHERLANDS, result);

 result = deserializer.deserialize(new TestJsonParser("\"fr_FR\"_\"Europe\""), context);
 assertNotNull(result);
 assertEquals(new Locale("fr", "FR", "Europe"), result);
 }

 @Test
 public void testCharsetDeserialization() throws Exception {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
 DeserializationContext context = new TestDeserializationContext();
 Charset result = deserializer.deserialize(new TestJsonParser("\"UTF-8\""), context);
 assertNotNull(result);
 assertEquals(Charset.forName("UTF-8"), result);
 }

 @Test
 public void testTimeZoneDeserialization() throws Exception {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
 DeserializationContext context = new TestDeserializationContext();
 TimeZone result = deserializer.deserialize(new TestJsonParser("\"UTC\""), context);
 assertNotNull(result);
 assertEquals(TimeZone.getTimeZone("UTC"), result);
 }

 @Test
 public void testInetAddressDeserialization() throws Exception {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
 DeserializationContext context = new TestDeserializationContext();
 InetAddress result = deserializer.deserialize(new TestJsonParser("\"127.0.0.1\""), context);
 assertNotNull(result);
 assertEquals("127.0.0.1", result.getHostAddress());

 result = deserializer.deserialize(new TestJsonParser("\"192.168.1.1\""), context);
 assertNotNull(result);
 assertEquals("192.168.1.1", result.getHostAddress());

 result = deserializer.deserialize(new TestJsonParser("\"255.255.255.255\""), context);
 assertNotNull(result);
 assertEquals("255.255.255.255", result.getHostAddress());

 result = deserializer.deserialize(new TestJsonParser("\"::1\""), context); // IPv6 address with no port
 assertNotNull(result);
 assertEquals("::1", result.getHostAddress());

 result = deserializer.deserialize(new TestJsonParser("\"2001:db8:85a3:0:0:8a2e:370:7334\""), context); // IPv6 address with port
 assertNotNull(result);
 assertEquals("2001:db8:85a3:0:0:8a2e:370:7334", result.getHostAddress());
 }

 @Test
 public void testInetSocketAddressDeserialization() throws Exception {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
 DeserializationContext context = new TestDeserializationContext();
 InetSocketAddress result = deserializer.deserialize(new TestJsonParser("\"localhost:8080\""), context);
 assertNotNull(result);
 assertEquals("localhost", result.getAddress().getHostAddress());
 assertEquals(8080, result.getPort());

 result = deserializer.deserialize(new TestJsonParser("\"[2001:db8:85a3:0:0:8a2e:370:7334]:80\""), context);
 assertNotNull(result);
 assertEquals("2001:db8:85a3:0:0:8a2e:370:7334", result.getAddress().getHostAddress());
 assertEquals(80, result.getPort());
 }

 @Test
 public void testInvalidEmptyString() throws Exception {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(String.class, FromStringDeserializer.Std.STD_STRING);
 DeserializationContext context = new TestDeserializationContext();
 assertThrows(JsonMappingException.class, () -> deserializer.deserialize(new TestJsonParser("\"\""), context));
 }

 @Test
 public void testInvalidNull() throws Exception {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(String.class, FromStringDeserializer.Std.STD_STRING);
 DeserializationContext context = new TestDeserializationContext();
 assertThrows(JsonMappingException.class, () -> deserializer.deserialize(new TestJsonParser("null"), context));
 }

 @Test
 public void testInvalidInvalidType() throws Exception {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(Integer.class, FromStringDeserializer.Std.STD_STRING);
 DeserializationContext context = new TestDeserializationContext();
 assertThrows(JsonMappingException.class, () -> deserializer.deserialize(new TestJsonParser("\"test\""), context));
 }

 private static class TestJsonParser extends JsonParser {
 private final String jsonString;

 TestJsonParser(String jsonString) {
 this.jsonString = jsonString;
 }

 @Override
 public boolean nextToken() throws IOException {
 StringTokenizer st = new StringTokenizer(jsonString, "[},]");
 return nextValue(st.nextToken());
 }

 @Override
 public String getValueAsString() throws IOException {
 return jsonString;
 }
 }

 private static class TestDeserializationContext extends DeserializationContext {
 @Override
 public void failureContext(String msg, Throwable p) {
 throw new RuntimeException(msg, p);
 }
 }

 private static void assertPattern(Pattern expected, Pattern actual) {
 assertTrue("Expected pattern not equal to actual pattern", expected.matcher("").matches());
 assertTrue("Expected pattern matches actual", expected.matcher("").reset().matches(actual.pattern()));
 }
}