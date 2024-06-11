package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

/**
 * Test for StringArrayDeserializer class
 */
public class StringArrayDeserializerTest {

 private ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 ObjectMapper.DefaultTypingResolver typingResolver = new ObjectMapper.DefaultTypingResolver();
 SimpleModule module = new SimpleModule();
 module.addDeserializer(String[].class, StringArrayDeserializer.instance);
 objectMapper = new ObjectMapper();
 objectMapper.setDefaultTyping(typingResolver);
 objectMapper.registerModule(module);
 }

 /**
 * Test deserialization with null value
 */
 @Test
 public void testDeserializationWithNullValue() throws IOException, JsonProcessingException {
 String[] result = objectMapper.readValue("[null]", String[].class);
 Assert.assertArrayEquals(new String[] { null }, result);
 }

 /**
 * Test deserialization with custom value deserializer
 */
 @Test
 public void testDeserializationWithCustomValueDeserializer() throws IOException, JsonProcessingException {
 SimpleModule module = new SimpleModule();
 module.addDeserializer(String.class, new TesterStringDeserializer());
 objectMapper = new ObjectMapper();
 objectMapper.registerModule(module);
 String[] result = objectMapper.readValue("[\"test\"]", String[].class);
 Assert.assertArrayEquals(new String[] { "test" }, result);
 }

 /**
 * Test deserialization with single value
 */
 @Test
 public void testDeserializationWithSingleValue() throws IOException, JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
 String[] result = mapper.readValue("\"test\"", String[].class);
 Assert.assertArrayEquals(new String[] { "test" }, result);
 }

 /**
 * Test deserialization with empty array
 */
 @Test
 public void testDeserializationWithEmptyArray() throws IOException, JsonProcessingException {
 String[] result = objectMapper.readValue("[]", String[].class);
 Assert.assertArrayEquals(new String[0], result);
 }

 /**
 * Test deserialization with invalid JSON
 */
 @Test(expected = IOException.class)
 public void testDeserializationWithInvalidJson() throws IOException, JsonProcessingException {
 String json = "{ \"invalid\": [] }";
 String[] result = objectMapper.readValue(json, String[].class);
 }

 /**
 * Test deserialization with StdDeserializer contextualization
 */
 @Test
 public void testDeserializationWithStdDeserializerContextualization() throws IOException, JsonProcessingException {
 SimpleModule module = new SimpleModule();
 module.addDeserializer(String.class, new TesterStringDeserializer());
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerModule(module);
 String[] result = mapper.readValue("[\"test1\", \"test2\"]", String[].class);
 Assert.assertArrayEquals(new String[] { "test1", "test2" }, result);
 }

 /**
 * Test deserialization with custom deserializer contextualization
 */
 @Test
 public void testDeserializationWithCustomDeserializerContextualization() throws IOException, JsonProcessingException {
 SimpleModule module = new SimpleModule();
 module.addDeserializer(String.class, new TesterStringDeserializer());
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerModule(module);
 mapper.handleEmptyStringAsNull(Boolean.TRUE);
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 String[] result = mapper.readValue("[\"test1\", \"test2\", , \"test3\"]", String[].class);
 Assert.assertArrayEquals(new String[] { "test1", "test2", null, "test3" }, result);
 }

 /**
 * Test deserialization with custom deserializer and type handler
 */
 @Test
 public void testDeserializationWithCustomDeserializerAndTypeHandler() throws IOException, JsonProcessingException {
 SimpleModule module = new SimpleModule();
 module.addDeserializer(String.class, new TesterStringDeserializer());
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerModule(module);
 mapper.addHandler(new TesterStringDeserializerTypeHandler());
 String[] result = mapper.readValue("[\"test1\", \"test2\"]", String[].class);
 Assert.assertArrayEquals(new String[] { "test1_TYPE_1", "test2_TYPE_2" }, result);
 }

 /**
 * Test deserialization with custom deserializer and contextualization
 */
 @Test
 public void testDeserializationWithCustomDeserializerAndContextualization() throws IOException, JsonProcessingException {
 SimpleModule module = new SimpleModule();
 module.addDeserializer(String.class, new TesterStringDeserializer());
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerModule(module);
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 SimpleBeanProperty property = new SimpleBeanProperty();
 String[] result = mapper.readValue("[\"test1\", \"test2\", null, \"test3\"]", String[].class, property);
 Assert.assertArrayEquals(new String[] { "test1", "test2", null, "test3" }, result);
 }
}

class TesterStringDeserializer extends StdDeserializer<String> {
 @Override
 public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
 return jsonParser.getValueAsString();
 }
}

class TesterStringDeserializerTypeHandler extends BaseJsonTypeDeserializer {
 @Override
 public void deserialize(JsonParser jsonParser, DeserializationContext deserializationContext, Object beanOrClass) throws IOException {
 String value = jsonParser.getValueAsString();
 super.deserialize(jsonParser, deserializationContext, value.split("_")[0]);
 }
}