package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class StringArrayDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @After
 public void teardown() throws Exception {
 objectMapper.close();
 }

 @Test
 public void testDeserializeStandardArray() throws JsonProcessingException {
 String[] input = new String[] {"test1", "test2", "test3"};
 String jsonString = objectMapper.writeValueAsString(input);
 String[] result = objectMapper.readValue(JsonFactory.instance.createParser(jsonString), String[].class);
 assertArrayEquals(input, result);
 }

 @Test
 public void testDeserializeCustomElementDeserializer() throws JsonProcessingException {
 JsonDeserializer<String> customDeserializer = new StringDeserializer();
 String[] input = new String[] {"test1", "test2", "test3"};
 String jsonString = objectMapper.writeValueAsString(input);
 StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
 String[] result = objectMapper.readValue(JsonFactory.instance.createParser(jsonString), deserializer.getType());
 assertArrayEquals(input, result);
 }

 @Test
 public void testDeserializeEmptyArray() throws JsonProcessingException {
 String jsonString = "[]";
 String[] result = objectMapper.readValue(JsonFactory.instance.createParser(jsonString), String[].class);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeNullArray() throws JsonProcessingException {
 String jsonString = "null";
 String[] result = objectMapper.readValue(JsonFactory.instance.createParser(jsonString), String[].class);
 assertNull(result);
 }

 @Test
 public void testDeserializeArrayWithCustomNullValue() throws JsonProcessingException {
 String[] input = new String[] {"test1", null, "test3"};
 String jsonString = objectMapper.writeValueAsString(input);
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 String[] result = objectMapper.readValue(JsonFactory.instance.createParser(jsonString), deserializer.getType());
 assertArrayEquals(input, result);
 }

 @Test
 public void testDeserializeArrayWithInvalidJson() throws JsonProcessingException {
 String jsonString = "{ \"invalid\": \"json\" }";
 assertThrows(JsonParseException.class, () -> objectMapper.readValue(JsonFactory.instance.createParser(jsonString), String[].class));
 }

 @Test
 public void testDeserializeArrayWithInvalidType() throws JsonProcessingException {
 String jsonString = "[1, 2, 3]";
 assertThrows(JsonMappingException.class, () -> objectMapper.readValue(JsonFactory.instance.createParser(jsonString), String[].class));
 }

 @Test
 public void testDeserializeArrayWithEmptyStringAsNull() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.disable(DeserializationFeature.FAIL_ON_EMPTY_BEANS);
 mapper.disable(DeserializationFeature.FAIL_ON_EMPTY_STRING_AS_EMPTY_ARRAY);
 mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
 String jsonString = "[\"\"]";
 String[] result = mapper.readValue(JsonFactory.instance.createParser(jsonString), String[].class);
 assertNull(result);
 }

 @Test
 public void testDeserializeContextualization() throws JsonProcessingException {
 String jsonString = "[\"test1\", \"test2\"]";
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 ArrayType arrayType = TypeFactory.defaultInstance().constructArrayType(String.class);
 JsonDeserializer<String> customDeserializer = new StringDeserializer();
 JsonDeserializer<String[]> contextualDeserializer = deserializer.createContextual(objectMapper.getDeserializationContext(), null);
 assertNotNull(contextualDeserializer);
 assertFalse(contextualDeserializer.isEnabled(DeserializationFeature.FAIL_ON_EMPTY_BEANS));
 assertEquals(String[].class, contextualDeserializer.getType());
 assertNull(contextualDeserializer.getNullValue());
 assertSame(deserializer.getClass(), contextualDeserializer.getClass().getSuperclass());
 assertSame(customDeserializer.getClass(), contextualDeserializer.getDelegate());
 String[] result = objectMapper.readValue(JsonFactory.instance.createParser(jsonString), contextualDeserializer.getType());
 assertArrayEquals(new String[] {"test1", "test2"}, result);
 }
}