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
 public void testDeserializeWithNullifyingDeserializer() throws Exception {
 JsonFactory jsonFactory = objectMapper.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("[]");
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserialize(jsonParser, ctxt);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithTypeDeserializerNull() throws Exception {
 JsonFactory jsonFactory = objectMapper.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("[]");
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 TypeDeserializer typeDeserializer = null;
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, ctxt, typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithTypeDeserializerNonArrayOrObject() throws Exception {
 JsonFactory jsonFactory = objectMapper.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("123");
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 TypeDeserializer typeDeserializer = null;
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, ctxt, typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithTypeDeserializerArray() throws Exception {
 JsonFactory jsonFactory = objectMapper.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("[1, 2, 3]");
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 TypeDeserializer typeDeserializer = null;
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, ctxt, typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithTypeDeserializerObject() throws Exception {
 JsonFactory jsonFactory = objectMapper.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("{\"name\":\"John\", \"age\":30}");
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 TypeDeserializer typeDeserializer = null;
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, ctxt, typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithTypeDeserializerTypedArray() throws Exception {
 JsonFactory jsonFactory = objectMapper.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("[\"apple\", \"banana\", \"orange\"]");
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 TypeDeserializer typeDeserializer = new ArrayTypeDeserializer(new SimpleType("java.lang.String[]"));
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, ctxt, typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithTypeDeserializerTypedObject() throws Exception {
 JsonFactory jsonFactory = objectMapper.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("{\"name\":\"John\", \"age\":30, \"favoriteFruits\":[\"apple\", \"banana\", \"orange\"]}");
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 TypeDeserializer typeDeserializer = new BeanTypeDeserializer(new SimpleType("MyClass"), new JacksonAnnotationIntrospector());
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, ctxt, typeDeserializer);
 assertNull(result);
 }
}