package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

class FromStringDeserializerTest {

 @BeforeAll
 static void setup() {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 void testDeserializeFile() throws IOException {
 File file = new File("path/to/file.txt");
 JsonParser parser = mapper.getFactory().createParser(file);
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(File.class);
 assertInstanceOf(File.class, deserializer.deserialize(parser, null));
 }

 @Test
 void testDeserializeURL() throws IOException {
 URL url = new URL("http://example.com/");
 JsonParser parser = mapper.getFactory().createParser(url.openStream());
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(URL.class);
 assertInstanceOf(URL.class, deserializer.deserialize(parser, null));
 }

 @Test
 void testDeserializeURI() throws IOException {
 URI uri = new URI("http://example.com/path/to/resource");
 JsonParser parser = mapper.getFactory().createParser(new StringReader(uri.toURIString()));
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(URI.class);
 assertInstanceOf(URI.class, deserializer.deserialize(parser, null));
 }

 @ParameterizedTest
 @ValueSource(classes = { Currency.class, Pattern.class, Locale.class, Charset.class, TimeZone.class, InetAddress.class, InetSocketAddress.class })
 void testDeserializeSupportedClasses(Class<?> clazz) throws IOException {
 JsonParser parser = mapper.getFactory().createParser("\"some valid string representation\"");
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(clazz);
 assertInstanceOf(clazz, deserializer.deserialize(parser, null));
 }

 @Test
 void testDeserializeEmptyString() throws IOException {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(Locale.class);
 assertNull(deserializer.deserialize(mapper.getFactory().createParser(""), null));
 }

 @Test
 void testDeserializeInvalidString() throws IOException {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(Locale.class);
 JsonParser parser = mapper.getFactory().createParser("InvalidString");
 assertThrows(JsonMappingException.class, () -> deserializer.deserialize(parser, null));
 }

 @Test
 void testDeserializeWrongClass() throws IOException {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(Integer.class);
 JsonParser parser = mapper.getFactory().createParser("\"some valid string representation\"");
 assertThrows(JsonMappingException.class, () -> deserializer.deserialize(parser, null));
 }

 @Test
 void testDeserializeEmbeddedObject() throws IOException {
 Object obj = new Object();
 JsonParser parser = mapper.getFactory().createParser(ObjectToJsonParser.serializeValue(obj));
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(Object.class);
 assertInstanceOf(Object.class, deserializer.deserialize(parser, null));
 }

 @Test
 void testDeserializeEmptyEmbeddedObject() throws IOException {
 Object obj = new Object();
 JsonParser parser = mapper.getFactory().createParser("{}");
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(Object.class);
 assertInstanceOf(Object.class, deserializer.deserialize(parser, null));
 assertNull(obj);
 }

 @Test
 void testDeserializeInvalidEmbeddedObject() throws IOException {
 Object obj = new Object();
 JsonParser parser = mapper.getFactory().createParser("{\"invalid\":\"json\"}");
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(Object.class);
 assertThrows(JsonMappingException.class, () -> deserializer.deserialize(parser, null));
 }
}