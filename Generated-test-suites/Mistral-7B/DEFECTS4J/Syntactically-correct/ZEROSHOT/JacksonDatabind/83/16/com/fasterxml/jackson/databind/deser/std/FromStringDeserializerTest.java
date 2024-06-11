package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FromStringDeserializerTest {

 private ObjectMapper objectMapper;
 private JsonParser jsonParser;
 private DeserializationContext deserializationContext;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 jsonParser = mock(JsonParser.class);
 deserializationContext = mock(DeserializationContext.class);
 }

 @Test
 public void testDeserializeFile() throws JsonProcessingException {
 FromStringDeserializer deserializer = new FromStringDeserializer<File>() {
 @Override
 protected File _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return new File(value);
 }
 };

 when(jsonParser.getValueAsString()).thenReturn("path/to/file.txt");
 when(deserializationContext.handleUnexpectedToken(File.class, jsonParser)).thenThrow(new JsonMappingException("Unexpected token"));

 assertEquals(new File("path/to/file.txt"), deserializer.deserialize(jsonParser, deserializationContext));
 }

 @Test
 public void testDeserializeURL() throws JsonProcessingException {
 FromStringDeserializer deserializer = new FromStringDeserializer<URL>() {
 @Override
 protected URL _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return new URL(value);
 }
 };

 when(jsonParser.getValueAsString()).thenReturn("http://example.com");
 when(deserializationContext.handleUnexpectedToken(URL.class, jsonParser)).thenThrow(new JsonMappingException("Unexpected token"));

 assertEquals(new URL("http://example.com"), deserializer.deserialize(jsonParser, deserializationContext));
 }

 @Test
 public void testDeserializeUri() throws JsonProcessingException {
 FromStringDeserializer deserializer = new FromStringDeserializer<URI>() {
 @Override
 protected URI _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return URI.create(value);
 }
 };

 when(jsonParser.getValueAsString()).thenReturn("http://example.com/path/to/resource");
 when(deserializationContext.handleUnexpectedToken(URI.class, jsonParser)).thenThrow(new JsonMappingException("Unexpected token"));

 assertEquals(URI.create("http://example.com/path/to/resource"), deserializer.deserialize(jsonParser, deserializationContext));
 }

 @Test
 public void testDeserializeClass() throws JsonProcessingException {
 FromStringDeserializer deserializer = new FromStringDeserializer<Class<?>>() {
 @Override
 protected Class<?> _deserialize(String value, DeserializationContext ctxt) throws IOException {
 try {
 return Class.forName(value);
 } catch (ClassNotFoundException e) {
 throw new JsonMappingException(e.getMessage(), e);
 }
 }
 };

 when(jsonParser.getValueAsString()).thenReturn("com.example.MyClass");
 when(deserializationContext.handleUnexpectedToken(Class.class, jsonParser)).thenThrow(new JsonMappingException("Unexpected token"));

 assertEquals(Class.forName("com.example.MyClass"), deserializer.deserialize(jsonParser, deserializationContext));
 }

 @Test
 public void testDeserializeJavaType() throws JsonProcessingException {
 FromStringDeserializer deserializer = new FromStringDeserializer<JavaType>() {
 @Override
 protected JavaType _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return objectMapper.getTypeFactory().constructFromCanonical(value);
 }
 };

 when(jsonParser.getValueAsString()).thenReturn("java.util.List<java.lang.String>");
 when(deserializationContext.handleUnexpectedToken(JavaType.class, jsonParser)).thenThrow(new JsonMappingException("Unexpected token"));

 assertEquals(JavaType.constructFromCanonical("java.util.List<java.lang.String>"), deserializer.deserialize(jsonParser, deserializationContext));
 }

 @Test
 public void testDeserializeCurrency() throws JsonProcessingException {
 FromStringDeserializer deserializer = new FromStringDeserializer<Currency>() {
 @Override
 protected Currency _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return Currency.getInstance(value);
 }
 };

 when(jsonParser.getValueAsString()).thenReturn("USD");
 when(deserializationContext.handleUnexpectedToken(Currency.class, jsonParser)).thenThrow(new JsonMappingException("Unexpected token"));

 assertEquals(Currency.getInstance("USD"), deserializer.deserialize(jsonParser, deserializationContext));
 }

 @Test
 public void testDeserializePattern() throws JsonProcessingException {
 FromStringDeserializer deserializer = new FromStringDeserializer<Pattern>() {
 @Override
 protected Pattern _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return Pattern.compile(value);
 }
 };

 when(jsonParser.getValueAsString()).thenReturn("/(.*).txt$/");
 when(deserializationContext.handleUnexpectedToken(Pattern.class, jsonParser)).thenThrow(new JsonMappingException("Unexpected token"));

 assertEquals(Pattern.compile("/(.*).txt$/"), deserializer.deserialize(jsonParser, deserializationContext));
 }

 @Test
 public void testDeserializeLocale() throws JsonProcessingException {
 FromStringDeserializer deserializer = new FromStringDeserializer<Locale>() {
 @Override
 protected Locale _deserialize(String value, DeserializationContext ctxt) throws IOException {
 int ix = value.indexOf('-');
 if (ix == -1) {
 return new Locale(value);
 } else {
 return new Locale(value.substring(0, ix), value.substring(ix+1));
 }
 }
 };

 when(jsonParser.getValueAsString()).thenReturn("en_US");
 when(deserializationContext.handleUnexpectedToken(Locale.class, jsonParser)).thenThrow(new JsonMappingException("Unexpected token"));

 assertEquals(new Locale("en", "US"), deserializer.deserialize(jsonParser, deserializationContext));
 }

 @Test
 public void testDeserializeCharset() throws JsonProcessingException {
 FromStringDeserializer deserializer = new FromStringDeserializer<Charset>() {
 @Override
 protected Charset _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return Charset.forName(value);
 }
 };

 when(jsonParser.getValueAsString()).thenReturn("UTF-8");
 when(deserializationContext.handleUnexpectedToken(Charset.class, jsonParser)).thenThrow(new JsonMappingException("Unexpected token"));

 assertEquals(Charset.forName("UTF-8"), deserializer.deserialize(jsonParser, deserializationContext));
 }

 @Test
 public void testDeserializeTimeZone() throws JsonProcessingException {
 FromStringDeserializer deserializer = new FromStringDeserializer<TimeZone>() {
 @Override
 protected TimeZone _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return TimeZone.getTimeZone(value);
 }
 };

 when(jsonParser.getValueAsString()).thenReturn("UTC");
 when(deserializationContext.handleUnexpectedToken(TimeZone.class, jsonParser)).thenThrow(new JsonMappingException("Unexpected token"));

 assertEquals(TimeZone.getTimeZone("UTC"), deserializer.deserialize(jsonParser, deserializationContext));
 }

 @Test
 public void testDeserializeInetAddress() throws JsonProcessingException {
 FromStringDeserializer deserializer = new FromStringDeserializer<InetAddress>() {
 @Override
 protected InetAddress _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return InetAddress.getByName(value);
 }
 };

 when(jsonParser.getValueAsString()).thenReturn("192.168.1.1");
 when(deserializationContext.handleUnexpectedToken(InetAddress.class, jsonParser)).thenThrow(new JsonMappingException("Unexpected token"));

 assertEquals(InetAddress.getByName("192.168.1.1"), deserializer.deserialize(jsonParser, deserializationContext));
 }

 @Test
 public void testDeserializeInetSocketAddress() throws JsonProcessingException {
 FromStringDeserializer deserializer = new FromStringDeserializer<InetSocketAddress>() {
 @Override
 protected InetSocketAddress _deserialize(String value, DeserializationContext ctxt) throws IOException {
 try {
 String[] parts = value.split(":");
 if (parts.length != 2) {
 throw new JsonMappingException("Format error: " + value);
 }
 InetAddress address = InetAddress.getByName(parts[0]);
 int port = Integer.parseInt(parts[1]);
 return new InetSocketAddress(address, port);
 } catch (UnknownHostException e) {
 throw new JsonMappingException("Unknown host: " + value, e);
 } catch (NumberFormatException e) {
 throw new JsonMappingException("Invalid port number: " + value, e);
 }
 }
 };

 when(jsonParser.getValueAsString()).thenReturn("[192.168.1.1]:8080");
 when(deserializationContext.handleUnexpectedToken(InetSocketAddress.class, jsonParser)).thenThrow(new JsonMappingException("Unexpected token"));

 assertEquals(new InetSocketAddress(InetAddress.getByName("192.168.1.1"), 8080), deserializer.deserialize(jsonParser, deserializationContext));
 }

 @Test
 public void testDeserializeStringBuilder() throws JsonProcessingException {
 FromStringDeserializer deserializer = new FromStringDeserializer<StringBuilder>() {
 @Override
 protected StringBuilder _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return new StringBuilder(value);
 }
 };

 when(jsonParser.getValueAsString()).thenReturn("Hello World!");
 when(deserializationContext.handleUnexpectedToken(StringBuilder.class, jsonParser)).thenThrow(new JsonMappingException("Unexpected token"));

 assertEquals(new StringBuilder("Hello World!"), deserializer.deserialize(jsonParser, deserializationContext));
 }

 @Test
 public void testDeserializeEmptyString() throws JsonProcessingException {
 FromStringDeserializer deserializer = new FromStringDeserializer<Object>() {
 @Override
 protected Object _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return null;
 }

 @Override
 protected Object _deserializeFromEmptyString() throws IOException {
 return null;
 }
 };

 when(jsonParser.getValueAsString()).thenReturn("");
 when(deserializationContext.handleUnexpectedToken(Object.class, jsonParser)).thenThrow(new JsonMappingException("Unexpected token"));

 assertEquals(null, deserializer.deserialize(jsonParser, deserializationContext));
 }

 @Test
 public void testDeserializeEmptyJSONArray() throws JsonProcessingException {
 FromStringDeserializer deserializer = new FromStringDeserializer<Object>() {
 @Override
 protected Object _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return null;
 }
 };

 when(jsonParser.getValueAsString()).thenReturn("");
 when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
 when(deserializationContext.handleUnexpectedToken(Object.class, jsonParser)).thenThrow(new JsonMappingException("Unexpected token"));

 assertNull(deserializer.deserialize(jsonParser, deserializationContext));
 }

 @Test
 public void testDeserializeEmbeddedObject() throws JsonProcessingException {
 FromStringDeserializer deserializer = new FromStringDeserializer<Object>() {
 @Override
 protected Object _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return new Object(); // Not actually used in this test
 }

 @Override
 protected Object _deserializeEmbedded(Object ob, DeserializationContext ctxt) throws IOException {
 return ob;
 }
 };

 Object embeddedObject = new Object();
 when(jsonParser.getValueAsString()).thenReturn("{\"class\":\"test.EmbeddedClass\",\"value\":{\"name\":\"test\"}}");
 when(jsonParser.getEmbeddedObject()).thenReturn(embeddedObject);
 when(deserializationContext.handleUnexpectedToken(Object.class, jsonParser)).thenThrow(new JsonMappingException("Unexpected token"));

 assertEquals(embeddedObject, deserializer.deserialize(jsonParser, deserializationContext));
 }
}