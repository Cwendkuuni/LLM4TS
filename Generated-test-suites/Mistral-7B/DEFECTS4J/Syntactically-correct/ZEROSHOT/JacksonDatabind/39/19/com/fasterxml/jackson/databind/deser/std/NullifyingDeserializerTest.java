package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerTestBase;

public class NullifyingDeserializerTest extends TypeDeserializerTestBase {

 private static final NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;

 @Override
 protected TypeDeserializer getTypeDeserializerUnderTest() {
 return nullifyingDeserializer;
 }

 public void testDeserializeNull() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser p = mapper.getFactory().createParser("null");
 assertNull(getTypeDeserializerUnderTest().deserialize(p, context));
 }

 public void testDeserializeEmptyArray() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser p = mapper.getFactory().createParser("[ ]");
 assertNull(getTypeDeserializerUnderTest().deserialize(p, context));
 }

 public void testDeserializeEmptyObject() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser p = mapper.getFactory().createParser("{}");
 assertNull(getTypeDeserializerUnderTest().deserialize(p, context));
 }

 public void testDeserializeSimpleValue() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser p = mapper.getFactory().createParser("123");
 assertNull(getTypeDeserializerUnderTest().deserialize(p, context));
 }

 public void testDeserializeComplexObject() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser p = mapper.getFactory().createParser(
 "{\"name\":\"John\", \"age\":30, \"city\":\"New York\"}");
 assertNull(getTypeDeserializerUnderTest().deserialize(p, context));
 }

 public void testDeserializeComplexArray() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser p = mapper.getFactory().createParser("[1, 2, 3]");
 assertNull(getTypeDeserializerUnderTest().deserialize(p, context));
 }

 public void testDeserializeTypeWithArray() throws JsonProcessingException, IOException {
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructType(int[].class);
 JsonParser p = mapper.getFactory().createParser("[1, 2, 3]");
 Object result = getTypeDeserializerUnderTest().deserializeWithType(p, context, typeDeserializer);
 assertNull(result);
 }

 public void testDeserializeTypeWithObject() throws JsonProcessingException, IOException {
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructType(Person.class);
 JsonParser p = mapper.getFactory().createParser("{\"name\":\"John\", \"age\":30, \"city\":\"New York\"}");
 Object result = getTypeDeserializerUnderTest().deserializeWithType(p, context, typeDeserializer);
 assertNull(result);
 }

 public void testDeserializeTypeWithStartArray() throws JsonProcessingException, IOException {
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructType(List.class);
 JsonParser p = mapper.getFactory().createParser("[1, 2, 3]");
 Object result = getTypeDeserializerUnderTest().deserializeWithType(p, context, typeDeserializer);
 assertNull(result);
 }

 public void testDeserializeTypeWithStartObject() throws JsonProcessingException, IOException {
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructType(HashMap.class);
 JsonParser p = mapper.getFactory().createParser("{\"name\":\"John\", \"age\":30, \"city\":\"New York\"}");
 Object result = getTypeDeserializerUnderTest().deserializeWithType(p, context, typeDeserializer);
 assertNull(result);
 }

 public void testDeserializeTypeWithFieldName() throws JsonProcessingException, IOException {
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructType(Person.class);
 JsonParser p = mapper.getFactory().createParser("{\"name\":\"John\", \"age\":30, \"city\":\"New York\"}");
 p.nextToken(); // consume name
 Object result = getTypeDeserializerUnderTest().deserializeWithType(p, context, typeDeserializer);
 assertNull(result);
 }

}

 class Person {
 String name;
 int age;
 String city;
 }