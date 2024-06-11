package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

 // Test for deserializing an empty String
 @Test
 public void testDeserializeEmptyString() throws IOException, JsonProcessingException {
  ObjectMapper mapper = new ObjectMapper();
  mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
  JsonFactory jsonFactory = mapper.getFactory();
  DeserializationContext ctxt = mapper.getDeserializationContext();
  FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class);
  String json = "\"\"";
  JsonParser parser = jsonFactory.createParser(json.getBytes(StandardCharsets.UTF_8));
  parser.nextToken(); // skip token type
  String result = deserializer.deserialize(parser, ctxt);
  assertEquals("", result);
 }

 // Test for deserializing a null String
 @Test
 public void testDeserializeNullString() throws IOException, JsonProcessingException {
  ObjectMapper mapper = new ObjectMapper();
  JsonFactory jsonFactory = mapper.getFactory();
  DeserializationContext ctxt = mapper.getDeserializationContext();
  FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class);
  String json = "null";
  JsonParser parser = jsonFactory.createParser(json.getBytes(StandardCharsets.UTF_8));
  parser.nextToken(); // skip token type
  String result = deserializer.deserialize(parser, ctxt);
  assertNull(result);
 }

 // Test for deserializing a valid String for each supported type
 @Test
 public void testDeserializeValidStrings() throws IOException, JsonProcessingException {
  ObjectMapper mapper = new ObjectMapper();
  JsonFactory jsonFactory = mapper.getFactory();
  DeserializationContext ctxt = mapper.getDeserializationContext();
  FromStringDeserializer<File> fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
  FromStringDeserializer<URL> urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
  FromStringDeserializer<URI> uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
  FromStringDeserializer<Class<?>> classDeserializer = FromStringDeserializer.findDeserializer(Class.class);
  FromStringDeserializer<JavaType> javaTypeDeserializer = FromStringDeserializer.findDeserializer(JavaType.class);
  FromStringDeserializer<Currency> currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
  FromStringDeserializer<Pattern> patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
  FromStringDeserializer<Locale> localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
  FromStringDeserializer<Charset> charsetDeserializer = FromStringDeserializer.findDeserializer(Charset.class);
  FromStringDeserializer<TimeZone> timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
  FromStringDeserializer<InetAddress> inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
  FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);

  String fileString = "path/to/file.txt";
  String urlString = "http://www.example.com/";
  String uriString = "http://www.example.com/path/to/file.txt";
  String classNameString = "java.lang.String";
  String javaTypeString = "com.example.MyClass";
  String currencyString = "USD";
  String patternString = "\\d+";
  String localeString = "en_US";
  String charsetString = "UTF-8";
  String timeZoneString = "America/Los_Angeles";
  String inetAddressString = "192.168.1.1";
  String inetSocketAddressString = "[255.255.255.255]:80";

  JsonParser parser = jsonFactory.createParser(fileString.getBytes(StandardCharsets.UTF_8));
  parser.nextToken(); // skip token type
  File fileResult = (File) fileDeserializer.deserialize(parser, ctxt);
  assertEquals(new File(fileString), fileResult);

  parser.close();
  parser = jsonFactory.createParser(urlString.getBytes(StandardCharsets.UTF_8));
  parser.nextToken(); // skip token type
  URL urlResult = (URL) urlDeserializer.deserialize(parser, ctxt);
  assertEquals(new URL(urlString), urlResult);

  parser.close();
  parser = jsonFactory.createParser(uriString.getBytes(StandardCharsets.UTF_8));
  parser.nextToken(); // skip token type
  URI uriResult = (URI) uriDeserializer.deserialize(parser, ctxt);
  assertEquals(new URI(uriString), uriResult);

  parser.close();
  parser = jsonFactory.createParser(classNameString.getBytes(StandardCharsets.UTF_8));
  parser.nextToken(); // skip token type
  Class<?> clazzResult = (Class<?>) classDeserializer.deserialize(parser, ctxt);
  assertEquals(Class.forName(classNameString), clazzResult);

  parser.close();
  parser = jsonFactory.createParser(javaTypeString.getBytes(StandardCharsets.UTF_8));
  parser.nextToken(); // skip token type
  JavaType javaTypeResult = javaTypeDeserializer.deserialize(parser, ctxt);
  assertEquals(mapper.getTypeFactory().constructFromCanonical(javaTypeString), javaTypeResult);

  parser.close();
  parser = jsonFactory.createParser(currencyString.getBytes(StandardCharsets.UTF_8));
  parser.nextToken(); // skip token type
  Currency currencyResult = (Currency) currencyDeserializer.deserialize(parser, ctxt);
  assertEquals(Currency.getInstance(currencyString), currencyResult);

  parser.close();
  parser = jsonFactory.createParser(patternString.getBytes(StandardCharsets.UTF_8));
  parser.nextToken(); // skip token type
  Pattern patternResult = (Pattern) patternDeserializer.deserialize(parser, ctxt);
  assertEquals(Pattern.compile(patternString), patternResult);

  parser.close();
  parser = jsonFactory.createParser(localeString.getBytes(StandardCharsets.UTF_8));
  parser.nextToken(); // skip token type
  Locale localeResult = (Locale) localeDeserializer.deserialize(parser, ctxt);
  assertEquals(new Locale(localeString.substring(0, localeString.indexOf("_")),
      localeString.substring(localeString.indexOf("_") + 1)), localeResult);

  parser.close();
  parser = jsonFactory.createParser(charsetString.getBytes(StandardCharsets.UTF_8));
  parser.nextToken(); // skip token type
  Charset charsetResult = (Charset) charsetDeserializer.deserialize(parser, ctxt);
  assertEquals(Charset.forName(charsetString), charsetResult);

  parser.close();
  parser = jsonFactory.createParser(timeZoneString.getBytes(StandardCharsets.UTF_8));
  parser.nextToken(); // skip token type
  TimeZone timeZoneResult = (TimeZone) timeZoneDeserializer.deserialize(parser, ctxt);
  assertEquals(TimeZone.getTimeZone(timeZoneString), timeZoneResult);

  parser.close();
  parser = jsonFactory.createParser(inetAddressString.getBytes(StandardCharsets.UTF_8));
  parser.nextToken(); // skip token type
  InetAddress inetAddressResult = (InetAddress) inetAddressDeserializer.deserialize(parser, ctxt);
  assertEquals(InetAddress.getByName(inetAddressString), inetAddressResult);

  parser.close();
  parser = jsonFactory.createParser(inetSocketAddressString.getBytes(StandardCharsets.UTF_8));
  parser.nextToken(); // skip token type
  InetSocketAddress inetSocketAddressResult = (InetSocketAddress) inetSocketAddressDeserializer.deserialize(parser, ctxt);
  if (inetSocketAddressString.startsWith("[")) {
    assertEquals(new InetSocketAddress(InetAddress.getByName(inetSocketAddressString.substring(0, inetSocketAddressString.indexOf("]"))),
        Integer.parseInt(inetSocketAddressString.substring(inetSocketAddressString.indexOf(":") + 1))), inetSocketAddressResult);
  } else {
    assertEquals(new InetSocketAddress(InetAddress.getByName(inetSocketAddressString), 0), inetSocketAddressResult);
  }

  parser.close();
 }

 // Test for deserializing a malformed String for each supported type
 @Test(expected = JsonMappingException.class)
 public void testDeserializeMalformedStrings() throws IOException, JsonProcessingException {
  ObjectMapper mapper = new ObjectMapper();
  JsonFactory jsonFactory = mapper.getFactory();
  DeserializationContext ctxt = mapper.getDeserializationContext();
  FromStringDeserializer<File> fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
  FromStringDeserializer<URL> urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
  FromStringDeserializer<URI> uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
  FromStringDeserializer<Class<?>> classDeserializer = FromStringDeserializer.findDeserializer(Class.class);
  FromStringDeserializer<JavaType> javaTypeDeserializer = FromStringDeserializer.findDeserializer(JavaType.class);
  FromStringDeserializer<Currency> currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
  FromStringDeserializer<Pattern> patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
  FromStringDeserializer<Locale> localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
  FromStringDeserializer<Charset> charsetDeserializer = FromStringDeserializer.findDeserializer(Charset.class);
  FromStringDeserializer<TimeZone> timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
  FromStringDeserializer<InetAddress> inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
  FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);

  String fileString = "path/to/file.txt123";
  String urlString = "http://www.example.com/badURL";
  String uriString = "http://www.example.com/path/to/file.txt123";
  String classNameString = "java.lang.StringX";
  String javaTypeString = "com.example.MyClassX";
  String currencyString = "USDX";
  String patternString = "\\d+X";
  String localeString = "en_US_X";
  String charsetString = "UTF-8X";
  String timeZoneString = "America/Los_AngelesX";
  String inetAddressString = "192.168.1.1X";
  String inetSocketAddressString = "[255.255.255.255]:80X";

  JsonParser parser = jsonFactory.createParser(fileString.getBytes(StandardCharsets.UTF_8));
  parser.nextToken(); // skip token type
 }

 // Test for deserializing a null Object
 @Test
 public void testDeserializeNullObject() throws IOException, JsonProcessingException {
  ObjectMapper mapper = new ObjectMapper();
  JsonFactory jsonFactory = mapper.getFactory();
  DeserializationContext ctxt = mapper.getDeserializationContext();
  FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
  String json = "null";
  JsonParser parser = jsonFactory.createParser(json.getBytes(StandardCharsets.UTF_8));
  parser.nextToken(); // skip token type
  Object result = deserializer.deserialize(parser, ctxt);
  assertNull(result);
 }

 // Test for deserializing an invalid Object
 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidObject() throws IOException, JsonProcessingException {
  ObjectMapper mapper = new ObjectMapper();
  JsonFactory jsonFactory = mapper.getFactory();
  DeserializationContext ctxt = mapper.getDeserializationContext();
  FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
  String json = "invalid_object";
  JsonParser parser = jsonFactory.createParser(json.getBytes(StandardCharsets.UTF_8));
  parser.nextToken(); // skip token type
  deserializer.deserialize(parser, ctxt);
 }

 // Test for deserializing an embedded Object
 @Test
 public void testDeserializeEmbeddedObject() throws IOException, JsonProcessingException {
  ObjectMapper mapper = new ObjectMapper();
  JsonFactory jsonFactory = mapper.getFactory();
  DeserializationContext ctxt = mapper.getDeserializationContext();
  FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
  String json = "{\"embeddedObject\":\"JavaType:com.example.MyClass\"}";
  JsonParser parser = jsonFactory.createParser(json.getBytes(StandardCharsets.UTF_8));
  parser.nextToken(); // skip token type
  Object object = deserializer.deserialize(parser, ctxt);
  assertEquals(mapper.getTypeFactory().constructType(JavaType.class, "com.example.MyClass"), object);
 }

 // Test for deserializing an embedded null Object
 @Test
 public void testDeserializeEmbeddedNullObject() throws IOException, JsonProcessingException {
  ObjectMapper mapper = new ObjectMapper();
  JsonFactory jsonFactory = mapper.getFactory();
  DeserializationContext ctxt = mapper.getDeserializationContext();
  FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
  String json = "{\"embeddedObject\":null}";
  JsonParser parser = jsonFactory.createParser(json.getBytes(StandardCharsets.UTF_8));
  parser.nextToken(); // skip token type
  Object object = deserializer.deserialize(parser, ctxt);
  assertNull(object);
 }

 // Test for deserializing an embedded invalid Object
 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmbeddedInvalidObject() throws IOException, JsonProcessingException {
  ObjectMapper mapper = new ObjectMapper();
  JsonFactory jsonFactory = mapper.getFactory();
  DeserializationContext ctxt = mapper.getDeserializationContext();
  FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
  String json = "{\"embeddedObject\":\"invalid_object\"}";
  JsonParser parser = jsonFactory.createParser(json.getBytes(StandardCharsets.UTF_8));
  parser.nextToken(); // skip token type
  deserializer.deserialize(parser, ctxt);
 }

}