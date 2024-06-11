package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertNull;

public class NullifyingDeserializerTest {

 private static ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testNullifyingDeserializer_EmptyJson() throws JsonMappingException, JsonProcessingException {
 String json = "{}";
 Object result = objectMapper.readValue(json, NullifyingDeserializer.class);
 assertNull(result);
 }

 @Test
 public void testNullifyingDeserializer_NullJson() throws JsonMappingException, JsonProcessingException {
 String json = null;
 Object result = objectMapper.readValue(json, NullifyingDeserializer.class);
 assertNull(result);
 }

 @Test
 public void testNullifyingDeserializer_ArrayJson() throws JsonMappingException, JsonProcessingException {
 String json = "[1, 2, 3]";
 Object result = objectMapper.readValue(json, NullifyingDeserializer.class);
 assertNull(result);
 }

 @Test
 public void testNullifyingDeserializer_ObjectJson() throws JsonMappingException, JsonProcessingException {
 String json = "{\"name\": \"John\", \"age\": 30}";
 Object result = objectMapper.readValue(json, NullifyingDeserializer.class);
 assertNull(result);
 }

 @Test
 public void testNullifyingDeserializer_FieldNameJson() throws JsonMappingException, JsonProcessingException {
 String json = "{\"name\": null}";
 Object result = objectMapper.readValue(json, NullifyingDeserializer.class);
 assertNull(result);
 }

 @Test
 public void testNullifyingDeserializer_StartArrayJson() throws JsonMappingException, JsonProcessingException {
 String json = "[1, 2, null]";
 Object result = objectMapper.readValue(json, NullifyingDeserializer.class);
 assertNull(result);
 }

 @Test
 public void testNullifyingDeserializer_StartObjectJson() throws JsonMappingException, JsonProcessingException {
 String json = "{\"name\": null, \"age\": 30}";
 Object result = objectMapper.readValue(json, NullifyingDeserializer.class);
 assertNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void testNullifyingDeserializer_InvalidJson() throws JsonMappingException, JsonProcessingException {
 String json = "{\"name\""; // Missing closing brace
 Object result = objectMapper.readValue(json, NullifyingDeserializer.class);
 }

 @Test(expected = IOException.class)
 public void testNullifyingDeserializer_InvalidUtf8Json() throws JsonMappingException, JsonProcessingException {
 String json = "{\"name\": \u0000John}"; // Null character in JSON
 Object result = objectMapper.readValue(json, NullifyingDeserializer.class);
 }
}