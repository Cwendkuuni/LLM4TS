package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNull;

public class NullifyingDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testDeserializeNullifyingDeserializerWithEmptyJson() throws Exception {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{}");
 DeserializationContext context = objectMapper.getDeserializationContext();
 assertNull(NullifyingDeserializer.instance.deserialize(jsonParser, context));
 }

 @Test
 public void testDeserializeNullifyingDeserializerWithArrayJson() throws Exception {
 JsonParser jsonParser = objectMapper.getFactory().createParser("[1,2,3]");
 DeserializationContext context = objectMapper.getDeserializationContext();
 assertNull(NullifyingDeserializer.instance.deserialize(jsonParser, context));
 }

 @Test
 public void testDeserializeNullifyingDeserializerWithObjectJson() throws Exception {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"name\":\"John\",\"age\":30}");
 DeserializationContext context = objectMapper.getDeserializationContext();
 assertNull(NullifyingDeserializer.instance.deserialize(jsonParser, context));
 }

 @Test
 public void testDeserializeNullifyingDeserializerWithComplexJson() throws Exception {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"name\":[1,2,3],\"age\":30.5,\"city\":\"New York\"}");
 DeserializationContext context = objectMapper.getDeserializationContext();
 assertNull(NullifyingDeserializer.instance.deserialize(jsonParser, context));
 }

 @Test
 public void testDeserializeNullifyingDeserializerWithInvalidJson() throws Exception {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{");
 DeserializationContext context = objectMapper.getDeserializationContext();
 assertNull(NullifyingDeserializer.instance.deserialize(jsonParser, context));
 }

 @Test
 public void testDeserializeNullifyingDeserializerWithTypeDeserializer() throws Exception {
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jsonParser = jsonFactory.createParser("[]");
 TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructValueType(String[].class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 assertNull(NullifyingDeserializer.instance.deserializeWithType(jsonParser, context, typeDeserializer));
 }

 @Test
 public void testDeserializeNullifyingDeserializerWithStartArrayToken() throws Exception {
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jsonParser = jsonFactory.createParser("[1,2,3]");
 DeserializationContext context = objectMapper.getDeserializationContext();
 assertNull(NullifyingDeserializer.instance.deserializeWithType(jsonParser, context, null));
 }

 @Test
 public void testDeserializeNullifyingDeserializerWithStartObjectToken() throws Exception {
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jsonParser = jsonFactory.createParser("{\"name\":\"John\",\"age\":30}");
 DeserializationContext context = objectMapper.getDeserializationContext();
 assertNull(NullifyingDeserializer.instance.deserializeWithType(jsonParser, context, null));
 }

 @Test(expected = IOException.class)
 public void testDeserializeNullifyingDeserializerWithUnexpectedToken() throws Exception {
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jsonParser = jsonFactory.createParser("5");
 DeserializationContext context = objectMapper.getDeserializationContext();
 NullifyingDeserializer.instance.deserializeWithType(jsonParser, context, null);
 }
}