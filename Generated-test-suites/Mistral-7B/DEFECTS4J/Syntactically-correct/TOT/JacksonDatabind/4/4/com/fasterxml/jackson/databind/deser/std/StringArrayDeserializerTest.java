package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Arrays;

/**
 * Test cases for {@link StringArrayDeserializer}.
 */
public class StringArrayDeserializerTest {

 private ObjectMapper objectMapper;

 @BeforeEach
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testEmptyArray() throws IOException {
 String[] expected = new String[0];
 String[] result = StringArrayDeserializer.instance.deserialize(JsonFactory.instance.createParser("[]"), objectMapper.getDeserializationContext());
 assertArrayEquals(expected, result);
 }

 @Test
 public void testSingleValueDeserialization() throws IOException {
 String[] expected = new String[] { "test" };
 String[] result = StringArrayDeserializer.instance.deserialize(JsonFactory.instance.createParser("[\"" + "test" + "\"]"), objectMapper.getDeserializationContext());
 assertArrayEquals(expected, result);
 }

 @Test
 public void testNullValueDeserialization() throws IOException {
 String[] expected = new String[0];
 String[] result = StringArrayDeserializer.instance.deserialize(JsonFactory.instance.createParser("[]"), objectMapper.getDeserializationContext());
 assertArrayEquals(expected, result);
 String[] result2 = StringArrayDeserializer.instance.deserialize(JsonFactory.instance.createParser("null"), objectMapper.getDeserializationContext());
 assertArrayEquals(expected, result2);
 }

 @Test
 public void testNullElementDeserializer() throws IOException, JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String[] expected = new String[] { null };
 JsonDeserializer<String> deserializer = null;
 StringArrayDeserializer deserializerUnderTest = new StringArrayDeserializer(deserializer);
 JsonParser parser = JsonFactory.instance.createParser("[\nnull\n]");
 String[] result = deserializerUnderTest.deserialize(parser, mapper.getDeserializationContext());
 assertArrayEquals(expected, result);
 }

 @Test
 public void testCustomDeserializer() throws IOException, JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectBuffer buffer = mapper.getDeserializationContext().leaseObjectBuffer();
 String[] expected = new String[] { "test1", "test2" };
 JsonDeserializer<String> customDeserializer = new StdDeserializer<>(String.class) {
 @Override
 public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 String text = jp.getText();
 return text.toUpperCase();
 }
 };
 StringArrayDeserializer deserializerUnderTest = new StringArrayDeserializer(customDeserializer);
 JsonParser parser = JsonFactory.instance.createParser("[\n\"test1\",\n\"test2\"\n]");
 String[] result = deserializerUnderTest.deserialize(parser, mapper.getDeserializationContext());
 buffer.release();
 assertArrayEquals(expected, result);
 }

 @Test
 public void testEmptyStringAsNullValue() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
 String[] expected = new String[] { null };
 String[] result = StringArrayDeserializer.instance.deserialize(JsonFactory.instance.createParser("[\n\"]"), mapper.getDeserializationContext());
 assertArrayEquals(expected, result);
 }

 @Test
 public void testNonArrayParsing() throws IOException {
 String[] expected = new String[] { "test" };
 StringArrayDeserializer deserializerUnderTest = StringArrayDeserializer.instance;
 JsonParser parser = JsonFactory.instance.createParser("{\"test\": \"value\"}");
 assertThrows(JsonMappingException.class, () -> deserializerUnderTest.deserialize(parser, objectMapper.getDeserializationContext()));
 }

 @Test
 public void testContextualDeserialization() throws IOException, JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String[] expected = new String[] { "test1", "test2" };
 JsonDeserializer<String> customDeserializer = new StdDeserializer<>(String.class) {
 @Override
 public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 String text = jp.getText();
 return text.toUpperCase();
 }
 };
 StringArrayDeserializer deserializerUnderTest = new StringArrayDeserializer(customDeserializer);
 ObjectBuffer buffer = mapper.getDeserializationContext().leaseObjectBuffer();
 JsonParser parser = JsonFactory.instance.createParser("{\"foo\": [\n" +
 "{\"type\":\"testClass\",\"testField\":[\n" +
 "{\"name\":\"test1\",\"value\":\"test1\"},\n" +
 "{\"name\":\"test2\",\"value\":\"test2\"}\n" +
 "]}\n" +
 "]}");
 String[] result = deserializerUnderTest.createContextual(mapper.getDeserializationContext(), null).deserialize(parser, mapper.getDeserializationContext());
 buffer.release();
 assertArrayEquals(expected, result);
 }
}