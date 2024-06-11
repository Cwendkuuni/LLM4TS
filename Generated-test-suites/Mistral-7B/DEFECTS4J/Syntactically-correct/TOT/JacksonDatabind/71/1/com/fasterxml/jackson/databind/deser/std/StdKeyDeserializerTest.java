package com.example.test;

import com.fasterxml.jackson.core.base.ParserArgs;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testBooleanDeserialization() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 ObjectReader objectReader = objectMapper.readerForMap().withValueReader(keyDeserializer);
 String jsonString = "{\"key\":true}";
 Map<String, Object> map = objectReader.readValue(new StringReader(jsonString), Map.class);
 assertTrue(map.get("key") instanceof Boolean);
 assertEquals(Boolean.TRUE, map.get("key"));
 }

 @Test
 public void testBooleanDeserializationWithInvalidInput() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 ObjectReader objectReader = objectMapper.readerForMap().withValueReader(keyDeserializer);
 String jsonString = "{\"key\":invalid}";
 try {
 map = objectReader.readValue(new StringReader(jsonString), Map.class);
 fail("Expected JacksonException");
 } catch (JsonMappingException e) {
 assertEquals("not a valid representation: not a valid Boolean.ParseBoolean: \"invalid\"", e.getMessage());
 }
 }

 @Test
 public void testByteDeserialization() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
 ObjectReader objectReader = objectMapper.readerForMap().withValueReader(keyDeserializer);
 String jsonString = "{\"key\":1}";
 Map<String, Object> map = objectReader.readValue(new StringReader(jsonString), Map.class);
 assertTrue(map.get("key") instanceof Byte);
 assertEquals((Byte) 1, map.get("key"));
 }

 @Test
 public void testByteDeserializationWithInvalidInput() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
 ObjectReader objectReader = objectMapper.readerForMap().withValueReader(keyDeserializer);
 String jsonString = "{\"key\":256}"; // Out of byte range
 try {
 map = objectReader.readValue(new StringReader(jsonString), Map.class);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 assertEquals("not a valid representation: overflow, value can not be represented as 8-bit value", e.getMessage());
 }
 }

 // Add test cases for other data types (Short, Char, Int, Long, Float, Double, Locale, Date, Calendar, UUID, URI, URL, Class, Currency)
}