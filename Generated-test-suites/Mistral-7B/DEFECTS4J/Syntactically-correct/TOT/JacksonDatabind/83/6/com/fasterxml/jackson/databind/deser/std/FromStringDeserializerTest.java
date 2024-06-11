package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

 private static ObjectMapper mapper = JsonMapperFactory.getMapper();

 @Test
 public void testURLDeserialization() throws JsonProcessingException {
 String json = "\"http://www.example.com\"";
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
 URL result = (URL) deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext(mapper));
 assertEquals("http://www.example.com", result.toString());
 }

 @Test
 public void testInvalidURLDeserialization() throws JsonProcessingException {
 String json = "\"not-a-url\"";
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
 try {
 URL result = (URL) deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext(mapper));
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testLocaleDeserialization() throws JsonProcessingException {
 String jsonFr_CA = "\"fr_CA\"";
 String jsonEn_US = "\"en_US\"";
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
 Locale resultFr_CA = (Locale) deserializer.deserialize(mapper.getFactory().createParser(jsonFr_CA), new DeserializationContext(mapper));
 assertEquals("fr_CA", resultFr_CA.toString());
 Locale resultEn_US = (Locale) deserializer.deserialize(mapper.getFactory().createParser(jsonEn_US), new DeserializationContext(mapper));
 assertEquals("en_US", resultEn_US.toString());
 }

 @Test
 public void testInvalidLocaleDeserialization() throws JsonProcessingException {
 String jsonInvalidLocale = "\"InvalidLocale\"";
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
 try {
 Locale result = (Locale) deserializer.deserialize(mapper.getFactory().createParser(jsonInvalidLocale), new DeserializationContext(mapper));
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testFileDeserialization() throws IOException {
 String json = "\"test-file.txt\"";
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
 File result = (File) deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext(mapper));
 assertTrue(result.exists());
 }

 @Test
 public void testInvalidFileDeserialization() throws JsonProcessingException {
 String json = "\"not-a-file\"";
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
 try {
 File result = (File) deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext(mapper));
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testJSONWithArray() throws JsonProcessingException {
 String json = "[\"http://www.example.com\", \"https://www.example2.com\"]";
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL[].class);
 URL[] result = (URL[]) deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext(mapper));
 assertEquals(2, result.length);
 assertEquals("http://www.example.com", result[0].toString());
 assertEquals("https://www.example2.com", result[1].toString());
 }

 @Test
 public void testEmptyJSONString() throws JsonProcessingException {
 String json = "\"\"";
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
 // different behavior depending on the target type
 if (deserializer.getClass() == FromStringDeserializer.Std.class) {
 URL result = (URL) deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext(mapper));
 assertEquals("", result.toString());
 } else {
 // other deserializers return null
 assertNull(deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext(mapper)));
 }
 }

 @Test
 public void testJSONWithNullValue() throws JsonProcessingException {
 String json = "null";
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
 assertNull(deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext(mapper)));
 }

 @Before
 public void setUp() throws Exception {
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }
}