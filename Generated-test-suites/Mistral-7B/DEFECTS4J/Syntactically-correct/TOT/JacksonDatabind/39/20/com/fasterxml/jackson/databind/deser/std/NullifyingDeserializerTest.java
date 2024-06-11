package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.BaseJsonParser;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerImpl;

import org.junit.Before;
import org.junit.Test;

public class NullifyingDeserializerTest {

 private JsonFactory jsonFactory;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 jsonFactory = new JsonFactory();
 objectMapper = new ObjectMapper(jsonFactory);
 }

 @Test
 public void testDeserializeNullifyingDeserializer() throws Exception {
 JsonParser jsonParser = jsonFactory.createParser("{}");
 NullifyingDeserializer serializerUnderTest = NullifyingDeserializer.instance;
 Object result = objectMapper.readValue(jsonParser, Object.class, serializerUnderTest);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithTypeNullifyingDeserializer() throws Exception {
 TypeDeserializer typeDeserializer = new TypeDeserializerImpl(null, null);
 JsonParser jsonParser = jsonFactory.createParser("[]");
 Object result = objectMapper.readValue(jsonParser, Object[].class, serializerUnderTest, typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithInvalidInputNullifyingDeserializer() throws Exception {
 JsonParser jsonParser = jsonFactory.createParser("Hello World");
 NullifyingDeserializer serializerUnderTest = NullifyingDeserializer.instance;
 try {
 objectMapper.readValue(jsonParser, Object.class, serializerUnderTest);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testDeserializeWithObjectStartNullifyingDeserializer() throws Exception {
 JsonParser jsonParser = jsonFactory.createParser("{\"name\":\"John\"}");
 NullifyingDeserializer serializerUnderTest = NullifyingDeserializer.instance;
 try {
 objectMapper.readValue(jsonParser, String.class, serializerUnderTest);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testDeserializeWithArrayStartNullifyingDeserializer() throws Exception {
 JsonParser jsonParser = jsonFactory.createParser("[1, 2, 3]");
 NullifyingDeserializer serializerUnderTest = NullifyingDeserializer.instance;
 TypeDeserializer typeDeserializer = new TypeDeserializerImpl(null, null);
 Object result = objectMapper.readValue(jsonParser, Object[].class, serializerUnderTest, typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithFieldNameNullifyingDeserializer() throws Exception {
 JsonParser jsonParser = jsonFactory.createParser("{\"name\": null}");
 NullifyingDeserializer serializerUnderTest = NullifyingDeserializer.instance;
 DeserializationContext context = mock(DeserializationContext.class);
 Object result = objectMapper.readValue(jsonParser, String.class, serializerUnderTest, context);
 assertNull(result);
 }
}