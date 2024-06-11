package com.example.tests;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 /**
 * Test deserialization of an empty JSON array to an empty String array.
 */
 @Test
 public void testEmptyJsonArray() throws Exception {
 String[] expected = new String[0];
 String[] actual = objectMapper.readValue(new String("[]"), String[].class);
 assertArrayEquals(expected, actual);
 }

 /**
 * Test deserialization of a JSON array with a single null value to a String array with a single null element.
 */
 @Test
 public void testJsonArrayWithNullValue() throws Exception {
 String[] expected = new String[] { null };
 JsonParser jsonParser = objectMapper.getFactory().createParser("[\n  null\n]");
 String[] actual = objectMapper.readValue(jsonParser, String[].class);
 assertArrayEquals(expected, actual);
 }

 /**
 * Test deserialization of a JSON array with a single non-null String value to a String array with a single element.
 */
 @Test
 public void testJsonArrayWithSingleStringValue() throws Exception {
 String[] expected = new String[] { "hello" };
 String[] actual = objectMapper.readValue(new String("[\n  \"hello\"\n]"), String[].class);
 assertArrayEquals(expected, actual);
 }

 /**
 * Test deserialization of a JSON array with multiple String values to a String array with multiple elements.
 */
 @Test
 public void testJsonArrayWithMultipleStringValues() throws Exception {
 String[] expected = new String[] { "hello", "world" };
 String[] actual = objectMapper.readValue(new String("[\n  \"hello\",\n  \"world\"\n]"), String[].class);
 assertArrayEquals(expected, actual);
 }

 /**
 * Test deserialization of a JSON array with non-String values to an empty String array.
 */
 @Test(expected = JsonMappingException.class)
 public void testJsonArrayWithNonStringValues() throws Exception {
 JsonParser jsonParser = objectMapper.getFactory().createParser("[\n  42\n]");
 objectMapper.readValue(jsonParser, String[].class);
 }

 /**
 * Test deserialization of a JSON array with an incorrect number of elements to a String array with the correct number of elements (padded with nulls).
 */
 @Test
 public void testJsonArrayWithIncorrectNumberOfElements() throws Exception {
 String[] expected = new String[] { "hello", null, "world" };
 String[] actual = objectMapper.readValue(new String("[\n  \"hello\",\n]"), String[].class);
 assertArrayEquals(expected, actual);
 }

 /**
 * Test deserialization of a JSON array with an incorrect type to an empty String array.
 */
 @Test(expected = IOException.class)
 public void testJsonArrayWithIncorrectType() throws Exception {
 String[] expected = new String[0];
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectReader reader = objectMapper.readerForArray(Integer[].class);
 actual = reader.readValue(new String("[\n  \"hello\",\n  \"world\"\n]"));
 assertArrayEquals(expected, actual);
 }

 /**
 * Test deserialization of a JSON array with a custom value deserializer.
 */
 @Test
 public void testJsonArrayWithCustomValueDeserializer() throws Exception {
 String[] expected = new String[] { "hello", "world" };
 ObjectMapper objectMapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addDeserializer(String.class, new StringDeserializer());
 objectMapper.registerModule(module);
 String[] actual = objectMapper.readValue(new String("[\n  \"hello\",\n  \"world\"\n]"), String[].class);
 assertArrayEquals(expected, actual);
 }
}