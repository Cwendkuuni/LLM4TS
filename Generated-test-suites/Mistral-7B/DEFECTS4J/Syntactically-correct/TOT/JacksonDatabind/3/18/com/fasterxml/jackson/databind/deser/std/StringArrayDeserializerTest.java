package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class StringArrayDeserializerTest {

 private ObjectMapper objectMapper;
 private ObjectReader objectReader;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 objectReader = objectMapper.readerForArrayOf(String.class);
 }

 @Test
 public void testDeserializeEmptyArray() throws Exception {
 String[] result = objectReader.readValue(JsonFactory.instance.createParser("[]"));
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeSingleValue() throws Exception {
 String[] result = objectReader.readValue(JsonFactory.instance.createParser("\"hello\""));
 assertEquals("hello", result[0]);
 }

 @Test
 public void testDeserializeNull() throws Exception {
 String[] result = objectReader.readValue(JsonFactory.instance.createParser("null"));
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeArrayWithValues() throws Exception {
 String[] result = objectReader.readValue(JsonFactory.instance.createParser("[\"one\",\"two\",\"three\"]"));
 assertArrayEquals(new String[]{"one", "two", "three"}, result);
 }

 @Test
 public void testDeserializeArrayWithNullValues() throws Exception {
 String[] result = objectReader.readValue(JsonFactory.instance.createParser("[\"one\",null,\"three\"]"));
 assertArrayEquals(new String[]{"one", null, "three"}, result);
 }

 @Test
 public void testDeserializeArrayWithCustomDeserializer() throws Exception {
 String[] result = objectMapper.readValue(JsonFactory.instance.createParser("[{\"number\":1},{\"number\":2}]"), String[].class, new CustomStringDeserializer());
 assertArrayEquals(new String[]{"1", "2"}, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeInvalidJson() throws Exception {
 objectReader.readValue(JsonFactory.instance.createParser("{"));
 }
}

class CustomStringDeserializer extends StdDeserializer<String> {
 @Override
 public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 return jp.getText();
 }
}