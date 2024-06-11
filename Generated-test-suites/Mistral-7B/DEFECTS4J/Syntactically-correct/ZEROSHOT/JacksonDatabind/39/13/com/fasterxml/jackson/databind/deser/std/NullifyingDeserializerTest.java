package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeDeserializer;

public class NullifyingDeserializerTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDeserializeNullifyingWithEmptyJson() throws IOException, JsonProcessingException {
 JsonNode jsonNode = mapper.readTree("{}");
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserialize(mapper.parserForTesting(), new TestDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingWithArrayJson() throws IOException, JsonProcessingException {
 JsonNode jsonNode = mapper.readTree("[1,2,3]");
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserialize(mapper.parserForTesting(), new TestDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingWithObjectJson() throws IOException, JsonProcessingException {
 JsonNode jsonNode = mapper.readTree("{\"name\":\"John\", \"age\":30}");
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserialize(mapper.parserForTesting(), new TestDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingWithStartObjectToken() throws IOException {
 JsonParser jsonParser = mapper.parserForTesting();
 jsonParser.nextToken(); // ID_START_OBJECT
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserialize(jsonParser, new TestDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingWithStartArrayToken() throws IOException {
 JsonParser jsonParser = mapper.parserForTesting();
 jsonParser.nextToken(); // ID_START_ARRAY
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserialize(jsonParser, new TestDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingWithFieldNameToken() throws IOException {
 JsonParser jsonParser = mapper.parserForTesting();
 jsonParser.nextToken(); // ID_FIELD_NAME
 jsonParser.nextText("name");
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserialize(jsonParser, new TestDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingWithValueToken() throws IOException {
 JsonParser jsonParser = mapper.parserForTesting();
 jsonParser.nextToken(); // ID_VALUE_STRING
 jsonParser.nextText("Hello");
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserialize(jsonParser, new TestDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingWithOtherTokens() throws IOException {
 JsonParser jsonParser = mapper.parserForTesting();
 jsonParser.nextToken(); // ID_VALUE_NUMBER_INT
 jsonParser.nextInt(10);
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserialize(jsonParser, new TestDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingWithTypedDeserialization() throws IOException, JsonProcessingException {
 TypeDeserializer typeDeserializer = mapper.getTypeDeserializerForAnnotation(JsonDeserialize.class);
 JsonNode jsonNode = mapper.readTree("{\"name\":\"John\", \"age\":30}");
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserializeWithType(mapper.parserForTesting(), new TestDeserializationContext(), typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingWithArrayTokenTypedDeserialization() throws IOException, JsonProcessingException {
 TypeDeserializer typeDeserializer = mapper.getTypeDeserializerForAnnotation(JsonDeserialize.Array.class);
 JsonNode jsonNode = mapper.readTree("[1,2,3]");
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserializeWithType(mapper.parserForTesting(), new TestDeserializationContext(), typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingWithObjectTokenTypedDeserialization() throws IOException, JsonProcessingException {
 TypeDeserializer typeDeserializer = mapper.getTypeDeserializerForAnnotation(JsonDeserialize.as.class);
 JsonNode jsonNode = mapper.readTree("{\"name\":\"John\", \"age\":30}");
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserializeWithType(mapper.parserForTesting(), new TestDeserializationContext(), typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingWithValueStringTypedDeserialization() throws IOException, JsonProcessingException {
 TypeDeserializer typeDeserializer = mapper.getTypeDeserializerForAnnotation(JsonDeserialize.as.class);
 JsonNode jsonNode = mapper.readTree("\"Hello\"");
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserializeWithType(mapper.parserForTesting(), new TestDeserializationContext(), typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingWithValueNumberIntTypedDeserialization() throws IOException, JsonProcessingException {
 TypeDeserializer typeDeserializer = mapper.getTypeDeserializerForAnnotation(JsonDeserialize.as.class);
 JsonNode jsonNode = mapper.readTree(10);
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserializeWithType(mapper.parserForTesting(), new TestDeserializationContext(), typeDeserializer);
 assertNull(result);
 }

 private static class TestDeserializationContext extends DeserializationContext {
 }

}