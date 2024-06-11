package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

/**
 * Test class for NullifyingDeserializer.
 */
public class NullifyingDeserializerTest {

 @Test
 public void testDeserializeNullifyingDeserializer() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
 JsonParser jsonParser = objectMapper.getFactory()
 .createParser("{\"someKey\": null}");

 Object result = nullifyingDeserializer.deserialize(jsonParser, new DeserializationContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeWithTypeNullifyingDeserializer() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
 TypeDeserializer typeDeserializer = new StandardTypeDeserializer();
 JsonParser jsonParser = objectMapper.getFactory()
 .createParser("[]");

 Object result = nullifyingDeserializer.deserializeWithType(jsonParser, new DeserializationContext(), typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithTypeNullifyingDeserializer_array() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
 TypeDeserializer typeDeserializer = new StandardTypeDeserializer();
 JsonParser jsonParser = objectMapper.getFactory()
 .createParser("[1, 2, 3]");

 Object result = nullifyingDeserializer.deserializeWithType(jsonParser, new DeserializationContext(), typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithTypeNullifyingDeserializer_object() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
 TypeDeserializer typeDeserializer = new StandardTypeDeserializer();
 JsonParser jsonParser = objectMapper.getFactory()
 .createParser("{\"someKey\": 1, \"anotherKey\": \"test\"}");

 Object result = nullifyingDeserializer.deserializeWithType(jsonParser, new DeserializationContext(), typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithTypeNullifyingDeserializer_fieldName() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
 TypeDeserializer typeDeserializer = new StandardTypeDeserializer();
 JsonParser jsonParser = objectMapper.getFactory()
 .createParser("{\"someKey\": null}");

 Object result = nullifyingDeserializer.deserializeWithType(jsonParser, new DeserializationContext(), typeDeserializer);
 assertNull(result);
 }
}