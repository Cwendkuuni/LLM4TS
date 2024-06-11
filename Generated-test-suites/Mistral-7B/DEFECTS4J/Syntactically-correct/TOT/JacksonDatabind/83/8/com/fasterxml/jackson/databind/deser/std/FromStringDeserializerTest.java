package com.example.junit4.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer.Std;

/**
 * Test class for {@link FromStringDeserializer}.
 */
public class FromStringDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testUrlDeserialization() throws IOException {
 Std urlDeserializer = Std.findDeserializer(URL.class);
 DeserializationContext ctxt = MAPPER.getDeserializationContext();
 JsonFactory jsonFactory = MAPPER.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("\"http://example.com\"");

 URL url = urlDeserializer.deserialize(jsonParser, ctxt);
 assertEquals("http://example.com", url.toString());
 }

 @Test
 public void testUriDeserialization() throws IOException {
 Std uriDeserializer = Std.findDeserializer(URI.class);
 DeserializationContext ctxt = MAPPER.getDeserializationContext();
 JsonFactory jsonFactory = MAPPER.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("\"http://example.com/path/to/file\"");

 URI uri = uriDeserializer.deserialize(jsonParser, ctxt);
 assertEquals("http", uri.getScheme());
 assertEquals("/path/to/file", uri.getPath());
 }

 @Test
 public void testClassDeserialization() throws IOException {
 Std classDeserializer = Std.findDeserializer(StringBuilder.class);
 DeserializationContext ctxt = MAPPER.getDeserializationContext();
 JsonFactory jsonFactory = MAPPER.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("\"new StringBuilder()\"");

 StringBuilder stringBuilder = (StringBuilder) classDeserializer.deserialize(jsonParser, ctxt);
 assertTrue(stringBuilder instanceof StringBuilder);
 }

 @Test(expected = IllegalStateException.class)
 public void testUnknownTypeDeserialization() throws IOException {
 Std unknownTypeDeserializer = Std.findDeserializer(int.class);
 DeserializationContext ctxt = MAPPER.getDeserializationContext();
 JsonFactory jsonFactory = MAPPER.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("\"42\"");

 unknownTypeDeserializer.deserialize(jsonParser, ctxt);
 // This should throw an IllegalStateException since int is not a string type
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidStringDeserialization() throws IOException {
 Std stringDeserializer = Std.findDeserializer(String.class);
 DeserializationContext ctxt = MAPPER.getDeserializationContext();
 JsonFactory jsonFactory = MAPPER.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("\"invalidString\"");

 try {
 String string = stringDeserializer.deserialize(jsonParser, ctxt);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testNullStringDeserialization() throws IOException {
 Std stringDeserializer = Std.findDeserializer(String.class);
 DeserializationContext ctxt = MAPPER.getDeserializationContext();
 JsonFactory jsonFactory = MAPPER.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("\"\"");

 try {
 String string = stringDeserializer.deserialize(jsonParser, ctxt);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testEmptyArrayDeserialization() throws IOException {
 Std deserializer = Std.findDeserializer(URL.class);
 DeserializationContext ctxt = MAPPER.getDeserializationContext();
 JsonFactory jsonFactory = MAPPER.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("[]");

 try {
 deserializer.deserialize(jsonParser, ctxt);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testEmbeddedObjectDeserialization() throws IOException {
 Std deserializer = Std.findDeserializer(URL.class);
 DeserializationContext ctxt = MAPPER.getDeserializationContext();
 ObjectMapper mapper = MAPPER.copy();
 ObjectMapper.DefaultTypingContext typingContext = mapper.getTypeFactory().getDefaultTypingContext();
 TypeDeserializer stringDeserializer = mapper.getTypeDeserializerForAnnotation(JsonDeserialize.class);
 mapper.setDefaultTyping(typingContext);
 JsonFactory jsonFactory = mapper.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("{\"url\": \"http://example.com\"}");

 try {
 URL url = deserializer.deserialize(jsonParser, ctxt);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }

 // Now with a custom deserializer for URL
 mapper.addSerializer(URL.class, new CustomUrlSerializer());
 mapper.setDefaultTyping(typingContext);
 jsonParser = jsonFactory.createParser("{\"url\": \"http://example.com\"}");

 try {
 URL url = deserializer.deserialize(jsonParser, ctxt);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }

 // Now with a custom deserializer for URL and a custom deserializer for String
 mapper.addSerializer(URL.class, new CustomUrlSerializer());
 mapper.addDeserializer(String.class, new CustomStringDeserializer());
 mapper.setDefaultTyping(typingContext);
 jsonParser = jsonFactory.createParser("{\"url\": \"http://example.com\"}");

 URL url = deserializer.deserialize(jsonParser, ctxt);
 assertEquals("http://example.com", url.toString());
 }
}

class CustomStringDeserializer extends StdStringDeserializer<String> {
 @Override
 protected String _deserialize(String value, DeserializationContext ctxt) throws IOException {
 // Custom deserialization logic for String here
 return super._deserialize(value, ctxt);
 }
}

class CustomUrlSerializer extends JsonSerializer<URL> {
 @Override
 public void serialize(URL value, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
 // Custom serialization logic for URL here
 super.serialize(value, generator, provider);
 }
}