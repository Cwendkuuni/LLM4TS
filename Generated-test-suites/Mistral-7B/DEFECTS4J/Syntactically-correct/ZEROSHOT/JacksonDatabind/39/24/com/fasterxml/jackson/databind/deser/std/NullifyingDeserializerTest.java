package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class NullifyingDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDeserializeNullifyingDeserializer() throws JsonProcessingException {
 ObjectNode jsonNode = mapper.createObjectNode();
 jsonNode.put("test", "testValue");

 JsonParser jsonParser = mapper.getFactory().createParser(jsonNode);
 StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserialize(jsonParser, new DeserializationContext(mapper, null, null));
 assertNull(result);
 }

 @Test
 public void testDeserializeWithTypeNullifyingDeserializer() throws JsonProcessingException {
 ObjectNode jsonNode = mapper.createObjectNode();
 jsonNode.put("test", "testValue");

 JsonParser jsonParser = mapper.getFactory().createParser(jsonNode);
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructType(String.class);
 StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserializeWithType(jsonParser, new DeserializationContext(mapper, null, typeDeserializer), typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithStartArrayTokenNullifyingDeserializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser jsonParser = mapper.getFactory().createParser("[1, 2, 3]");
 StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserialize(jsonParser, new DeserializationContext(mapper, null, null));
 assertNull(result);
 }

 @Test
 public void testDeserializeWithStartObjectTokenNullifyingDeserializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser jsonParser = mapper.getFactory().createParser("{\"test\":1}");
 StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserialize(jsonParser, new DeserializationContext(mapper, null, null));
 assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithFieldNameTokenNullifyingDeserializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser jsonParser = mapper.getFactory().createParser("{\"test\":1}");
 StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
 deserializer.deserialize(jsonParser, new DeserializationContext(mapper, null, null));
 }

 @Test
 public void testDeserializeWithIdStartArrayTokenNullifyingDeserializerTypeDeserializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser jsonParser = mapper.getFactory().createParser("[1, 2, 3]");
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructType(int[].class);
 StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserializeWithType(jsonParser, new DeserializationContext(mapper, null, typeDeserializer), typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithIdStartObjectTokenNullifyingDeserializerTypeDeserializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser jsonParser = mapper.getFactory().createParser("{\"test\":1}");
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructType(String.class);
 StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserializeWithType(jsonParser, new DeserializationContext(mapper, null, typeDeserializer), typeDeserializer);
 assertNull(result);
 }
}