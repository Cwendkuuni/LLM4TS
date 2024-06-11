package com.example.junit;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.Std;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testFileDeserialization() throws Exception {
 FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
 File expected = new File("test.txt");
 JsonNode node = mapper.readTree("new File(\"test.txt\")");
 File actual = deserializer.deserialize(mapper.getFactory().createParser(node), mapper.getContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testURLDeserialization() throws Exception {
 FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
 URL expected = new URL("http://example.com");
 JsonNode node = mapper.readTree("new URL(\"http://example.com\")");
 URL actual = deserializer.deserialize(mapper.getFactory().createParser(node), mapper.getContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testURIDeserialization() throws Exception {
 FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
 URI expected = new URI("http://example.com");
 JsonNode node = mapper.readTree("new URI(\"http://example.com\")");
 URI actual = deserializer.deserialize(mapper.getFactory().createParser(node), mapper.getContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testClassDeserialization() throws Exception {
 FromStringDeserializer<String> deserializer = FromStringDeserializer.findDeserializer(String.class);
 String expected = "Hello, World!";
 JsonNode node = mapper.readTree("\"Hello, World!\"");
 String actual = deserializer.deserialize(mapper.getFactory().createParser(node), mapper.getContext()).getValueAsString();
 assertEquals(expected, actual);
 }

 @Test
 public void testJavaTypeDeserialization() throws Exception {
 FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
 JavaType expected = mapper.getTypeFactory().constructType(String.class);
 JsonNode node = mapper.readTree("org.example.MyClass");
 JavaType actual = deserializer.deserialize(mapper.getFactory().createParser(node), mapper.getContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testCurrencyDeserialization() throws Exception {
 FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
 Currency expected = Currency.getInstance("USD");
 JsonNode node = mapper.readTree("Currency.getInstance(\"USD\")");
 Currency actual = deserializer.deserialize(mapper.getFactory().createParser(node), mapper.getContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testPatternDeserialization() throws Exception {
 FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
 Pattern expected = Pattern.compile("\\w+");
 JsonNode node = mapper.readTree("Pattern.compile(\"\\w+\")");
 Pattern actual = deserializer.deserialize(mapper.getFactory().createParser(node), mapper.getContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testLocaleDeserialization() throws Exception {
 FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
 Locale expected = new Locale("en", "US");
 JsonNode node = mapper.readTree("new Locale(\"en\", \"US\")");
 Locale actual = deserializer.deserialize(mapper.getFactory().createParser(node), mapper.getContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testCharsetDeserialization() throws Exception {
 FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
 Charset expected = Charset.forName("UTF-8");
 JsonNode node = mapper.readTree("Charset.forName(\"UTF-8\")");
 Charset actual = deserializer.deserialize(mapper.getFactory().createParser(node), mapper.getContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testTimeZoneDeserialization() throws Exception {
 FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
 TimeZone expected = TimeZone.getTimeZone("UTC");
 JsonNode node = mapper.readTree("TimeZone.getTimeZone(\"UTC\")");
 TimeZone actual = deserializer.deserialize(mapper.getFactory().createParser(node), mapper.getContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testInvalidInputDeserialization() throws Exception {
 FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
 try {
 deserializer.deserialize(mapper.getFactory().createParser("{ \"bad\": 42 }"), mapper.getContext());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testEmptyStringDeserialization() throws Exception {
 FromStringDeserializer<String> deserializer = FromStringDeserializer.findDeserializer(String.class);
 String expected = "";
 String actual = deserializer.deserialize(mapper.getFactory().createParser(""), mapper.getContext()).getValueAsString();
 assertEquals(expected, actual);
 }

 @Test
 public void testEmptyStringDeserializationClass() throws Exception {
 FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
 Object expected = "";
 Object actual = deserializer.deserialize(mapper.getFactory().createParser(""), mapper.getContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testNullDeserialization() throws Exception {
 FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
 Object expected = null;
 Object actual = deserializer.deserialize(mapper.getFactory().createParser("null"), mapper.getContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testEmptyArrayDeserialization() throws Exception {
 FromStringDeserializer<Object[]> deserializer = FromStringDeserializer.findDeserializer(Object[].class);
 Object[] expected = new Object[0];
 Object[] actual = deserializer.deserialize(mapper.getFactory().createParser("[ ]"), mapper.getContext());
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testEmptyArrayDeserializationClass() throws Exception {
 FromStringDeserializer<List<Object>> deserializer = FromStringDeserializer.findDeserializer(ArrayList.class);
 List<Object> expected = new ArrayList<>();
 List<Object> actual = deserializer.deserialize(mapper.getFactory().createParser("[ ]"), mapper.getContext());
 assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testUnsupportedTypeDeserialization() throws Exception {
 FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
 deserializer.deserialize(mapper.getFactory().createParser("[ 1, 2, 3 ]"), mapper.getContext());
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testUnsupportedTypeDeserializationClass() throws Exception {
 FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
 deserializer.deserialize(mapper.getFactory().createParser("[ 1, 2, 3 ]"), mapper.getContext());
 fail("Expected JsonMappingException");
 }

 @Test
 public void testIPv4AddressDeserialization() throws Exception {
 FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
 InetSocketAddress expected = new InetSocketAddress("192.168.1.1", 80);
 JsonNode node = mapper.readTree("new InetSocketAddress(\"192.168.1.1\", 80)");
 InetSocketAddress actual = deserializer.deserialize(mapper.getFactory().createParser(node), mapper.getContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testIPv6AddressDeserialization() throws Exception {
 FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
 InetSocketAddress expected = new InetSocketAddress("[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:80");
 JsonNode node = mapper.readTree("new InetSocketAddress(\"[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:80\")");
 InetSocketAddress actual = deserializer.deserialize(mapper.getFactory().createParser(node), mapper.getContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testIPv6AddressDeserializationWithPort() throws Exception {
 FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
 InetSocketAddress expected = new InetSocketAddress("[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:8080");
 JsonNode node = mapper.readTree("new InetSocketAddress(\"[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:8080\")");
 InetSocketAddress actual = deserializer.deserialize(mapper.getFactory().createParser(node), mapper.getContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testIPv6AddressDeserializationWithPortAndBrackets() throws Exception {
 FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
 InetSocketAddress expected = new InetSocketAddress("[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:8080");
 JsonNode node = mapper.readTree("new InetSocketAddress(\"[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:8080\")");
 InetSocketAddress actual = deserializer.deserialize(mapper.getFactory().createParser(node), mapper.getContext());
 assertEquals(expected, actual);
 }
}