package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerProvider;
import org.junit.Test;
import static org.junit.Assert.assertNull;

public class NullifyingDeserializerTest {

 private final ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testNullifyingDeserializer_EmptyJson() throws JsonProcessingException {
 JerarcyTestHelper.setup(objectMapper); // setup objectMapper for TypeDeserializerProvider
 TypeDeserializer typeDeserializer = objectMapper.getTypeDeserializer();
 JsonParser jsonParser = objectMapper.getFactory().createParser("{}");

 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, new DeserializationContext.Instance(), typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testNullifyingDeserializer_ArrayJson() throws JsonProcessingException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("[1, 2, 3]");
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, new DeserializationContext.Instance(), null);
 assertNull(result);
 }

 @Test
 public void testNullifyingDeserializer_ObjectJson() throws JsonProcessingException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"name\": \"John\", \"age\": 30}");
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, new DeserializationContext.Instance(), null);
 assertNull(result);
 }

 @Test
 public void testNullifyingDeserializer_InvalidJson() throws JsonProcessingException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{name: John}"); // missing comma
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, new DeserializationContext.Instance(), null);
 assertNull(result);
 }

 @Test
 public void testNullifyingDeserializer_NullTypeDeserializer() throws JsonProcessingException {
 Object result = new NullifyingDeserializer().deserializeWithType(objectMapper.getFactory().createParser("{}"), new DeserializationContext.Instance(), null);
 assertNull(result);
 }

 @Test
 public void testNullifyingDeserializer_StartArrayToken() throws JsonProcessingException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("[1, 2, 3]");
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, new DeserializationContext.Instance(), null);
 assertNull(result);
 }

 @Test
 public void testNullifyingDeserializer_StartObjectToken() throws JsonProcessingException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"name\": \"John\"}");
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, new DeserializationContext.Instance(), null);
 assertNull(result);
 }

 @Test
 public void testNullifyingDeserializer_FieldNameToken() throws JsonProcessingException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"name\": \"John\", \"age\": 30}");
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, new DeserializationContext.Instance(), null);
 assertNull(result);
 }

 @Test
 public void testNullifyingDeserializer_UnknownToken() throws JsonProcessingException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("123"); // number token
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, new DeserializationContext.Instance(), null);
 assertNull(result);
 }
}