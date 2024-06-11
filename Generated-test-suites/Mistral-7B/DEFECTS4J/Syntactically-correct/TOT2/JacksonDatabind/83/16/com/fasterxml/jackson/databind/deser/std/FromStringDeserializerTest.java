package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@RunWith(Parameterized.class)
public class FromStringDeserializerTest {

 private FromStringDeserializer<Object> deserializer;
 private Object expected;

 public FromStringDeserializerTest(Object expected) {
 this.expected = expected;
 }

 @Parameters
 public static Object[] createTestData() {
 return new Object[] {
 new File("test.txt"),
 new URL("http://test.com"),
 new URI("http://test.com/path"),
 Currency.getInstance("EUR"),
 new Locale("en_US"),
 new TimeZone("America/Los_Angeles"),
 new InetAddress("127.0.0.1"),
 new InetSocketAddress("127.0.0.1", 8080),
 new StringBuilder()
 };
 }

 @SuppressWarnings("unchecked")
 @Before
 public void setup() throws Exception {
 SimpleModule module = new SimpleModule();
 module.addDeserializer(File.class, FromStringDeserializer.findDeserializer(File.class));
 module.addDeserializer(URL.class, FromStringDeserializer.findDeserializer(URL.class));
 module.addDeserializer(URI.class, FromStringDeserializer.findDeserializer(URI.class));
 module.addDeserializer(Currency.class, FromStringDeserializer.findDeserializer(Currency.class));
 module.addDeserializer(Locale.class, FromStringDeserializer.findDeserializer(Locale.class));
 module.addDeserializer(TimeZone.class, FromStringDeserializer.findDeserializer(TimeZone.class));
 module.addDeserializer(InetAddress.class, FromStringDeserializer.findDeserializer(InetAddress.class));
 module.addDeserializer(InetSocketAddress.class, FromStringDeserializer.findDeserializer(InetSocketAddress.class));
 module.addDeserializer(StringBuilder.class, FromStringDeserializer.findDeserializer(StringBuilder.class));
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerModule(module);
 this.deserializer = (FromStringDeserializer<Object>) mapper.getFactory()
 .getDeserializerFactory().findValueDeserializer(null, Object.class);
 }

 @Test
 public void testDeserializeString() throws IOException {
 String jsonString = "\"testString\"";
 JsonParser parser = new JsonFactory().createParser(jsonString);
 Object actual = deserializer.deserialize(parser, new TestContext());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testDeserializeEmptyString() throws IOException {
 String jsonString = "\"\"";
 JsonParser parser = new JsonFactory().createParser(jsonString);
 Object actual = deserializer.deserialize(parser, new TestContext());
 if (expected instanceof String) {
 assertEquals("", actual.toString());
 } else {
 assertNull(actual);
 }
 }

 @Test
 public void testDeserializeNullString() throws IOException {
 String jsonString = "null";
 JsonParser parser = new JsonFactory().createParser(jsonString);
 Object actual = deserializer.deserialize(parser, new TestContext());
 assertNull(actual);
 }

 @Test
 public void testDeserializeArray() throws IOException {
 String jsonString = "[1, 2, 3]";
 JsonParser parser = new JsonFactory().createParser(jsonString);
 Object actual = deserializer.deserialize(parser, new TestContext());
 assertNotNull(actual);
 assertTrue(actual instanceof Number[]);
 Number[] numbers = (Number[]) actual;
 assertEquals(3, numbers.length);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidFormat() throws IOException {
 String jsonString = "invalid";
 JsonParser parser = new JsonFactory().createParser(jsonString);
 deserializer.deserialize(parser, new TestContext());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeUnknownType() throws IOException {
 String jsonString = "{\"type\":\"UnknownType\", \"value\":\"testString\"}";
 JsonParser parser = new JsonFactory().createParser(jsonString);
 deserializer.deserialize(parser, new TestContext());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeserializeInvalidURL() throws IOException {
 String jsonString = "\"malformed://test.com\"";
 JsonParser parser = new JsonFactory().createParser(jsonString);
 deserializer.deserialize(parser, new TestContext());
 }

 @Test(expected = MalformedURLException.class)
 public void testDeserializeInvalidURLWithSlashes() throws IOException {
 String jsonString = "\"malformed://test.com\"";
 JsonParser parser = new JsonFactory().createParser(jsonString);
 deserializer.deserialize(parser, new TestContext());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidLocale() throws IOException {
 String jsonString = "\"en_US_InvalidCountry\"";
 JsonParser parser = new JsonFactory().createParser(jsonString);
 deserializer.deserialize(parser, new TestContext());
 }

 @Test(expected = Exception.class)
 public void testDeserializeInvalidTimeZone() throws IOException {
 String jsonString = "\"InvalidTimeZone\"";
 JsonParser parser = new JsonFactory().createParser(jsonString);
 deserializer.deserialize(parser, new TestContext());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidCurrency() throws IOException {
 String jsonString = "\"InvalidCurrency\"";
 JsonParser parser = new JsonFactory().createParser(jsonString);
 deserializer.deserialize(parser, new TestContext());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidInetAddress() throws IOException {
 String jsonString = "\"invalidAddress\"";
 JsonParser parser = new JsonFactory().createParser(jsonString);
 deserializer.deserialize(parser, new TestContext());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidInetSocketAddress() throws IOException {
 String jsonString = "{\"inetAddress\":\"invalidAddress\", \"port\":123}";
 JsonParser parser = new JsonFactory().createParser(jsonString);
 deserializer.deserialize(parser, new TestContext());
 }

 private static class TestContext extends DeserializationContext {
 }
}