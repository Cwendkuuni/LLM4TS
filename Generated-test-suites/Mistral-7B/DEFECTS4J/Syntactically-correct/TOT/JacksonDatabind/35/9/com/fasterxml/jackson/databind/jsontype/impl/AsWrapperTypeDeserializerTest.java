package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class AsWrapperTypeDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDeserializeWrapperObject() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(String.class);
 TypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, mapper.getTypeIdResolver(), "_type", true, String.class);
 ObjectNode input = mapper.createObjectNode();
 input.put("type", "com.example.TestStringClass");
 input.put("com.example.TestStringClass", "Test String");
 Object result = mapper.readValue(input.traverse(), deserializer.getType());
 assertEquals("Test String", result);
 }

 @Test
 public void testDeserializeWithNullTypeId() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(String.class);
 TypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, mapper.getTypeIdResolver(), "_type", true, String.class);
 ObjectNode input = mapper.createObjectNode();
 JsonProcessingException e = assertThrows(JsonProcessingException.class, () -> mapper.readValue(input, deserializer.getType()));
 assertTrue(e.getMessage().contains("need JSON Object to contain As.WRAPPER_OBJECT type information for class " + javaType.getClassName()));
 }

 @Test
 public void testDeserializeWithInvalidTypeId() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(String.class);
 TypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, mapper.getTypeIdResolver(), "_type", true, String.class);
 ObjectNode input = mapper.createObjectNode();
 input.put("type", "com.example.NonExistentClass");
 input.put("com.example.NonExistentClass", "Test String");
 JsonProcessingException e = assertThrows(JsonProcessingException.class, () -> mapper.readValue(input, deserializer.getType()));
 assertTrue(e.getMessage().contains("can't find a Creator for value class com.example.NonExistentClass"));
 }

 @Test
 public void testDeserializeWithMissingTypeId() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(String.class);
 TypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, mapper.getTypeIdResolver(), "_type", true, String.class);
 ObjectNode input = mapper.createObjectNode();
 input.put("nonTypeIdKey", "Test String");
 JsonProcessingException e = assertThrows(JsonProcessingException.class, () -> mapper.readValue(input, deserializer.getType()));
 assertTrue(e.getMessage().contains("need JSON Object to contain As.WRAPPER_OBJECT type information for class " + javaType.getClassName()));
 }

 @Test
 public void testDeserializeWithEmptyTypeId() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(String.class);
 TypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, mapper.getTypeIdResolver(), "_type", true, String.class);
 ObjectNode input = mapper.createObjectNode();
 JsonProcessingException e = assertThrows(JsonProcessingException.class, () -> mapper.readValue(input, deserializer.getType()));
 assertTrue(e.getMessage().contains("need JSON Object to contain As.WRAPPER_OBJECT type information for class " + javaType.getClassName()));
 }

 @Test
 public void testDeserializeWithEmptyObject() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(String.class);
 TypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, mapper.getTypeIdResolver(), "_type", true, String.class);
 ObjectNode input = mapper.createObjectNode();
 input.put("type", "com.example.TestStringClass");
 input.put("", ""); // empty object
 JsonProcessingException e = assertThrows(JsonProcessingException.class, () -> mapper.readValue(input, deserializer.getType()));
 assertTrue(e.getMessage().contains("expected closing END_OBJECT after type information and deserialized value"));
 }

 @Test
 public void testDeserializeWithMissingClosingBracket() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(String.class);
 TypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, mapper.getTypeIdResolver(), "_type", true, String.class);
 ObjectNode input = mapper.createObjectNode();
 input.put("type", "com.example.TestStringClass");
 input.put("com.example.TestStringClass", "Test String");
 input.nextToken(); // skip closing bracket
 JsonProcessingException e = assertThrows(JsonProcessingException.class, () -> mapper.readValue(input, deserializer.getType()));
 assertTrue(e.getMessage().contains("expected closing END_OBJECT after type information and deserialized value"));
 }

 @Test
 public void testDeserializeWithTypeIdNotString() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(Integer.class);
 TypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, mapper.getTypeIdResolver(), "_type", true, Integer.class);
 ObjectNode input = mapper.createObjectNode();
 input.put("type", 1); // int instead of string
 input.put(1, 42); // invalid key
 JsonProcessingException e = assertThrows(JsonProcessingException.class, () -> mapper.readValue(input, deserializer.getType()));
 assertTrue(e.getMessage().contains("need JSON Object to contain As.WRAPPER_OBJECT type information for class " + javaType.getClassName()));
 }

 @Test
 public void testDeserializeWithCircularReference() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(CircularReference.class);
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectNode input = objectMapper.createObjectNode();
 input.put("type", "com.example.CircularReference");
 input.put("com.example.CircularReference", new Object()); // circular reference
 TypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, objectMapper.getTypeIdResolver(), "_type", true, CircularReference.class);
 Object result = objectMapper.readValue(input, deserializer.getType());
 fail("Should not deserialize circular reference");
 }

 @Test
 public void testDeserializeWithInvalidTypeIdValue() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(String.class);
 TypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, mapper.getTypeIdResolver(), "_type", true, String.class);
 ObjectNode input = mapper.createObjectNode();
 input.put("type", "com.example.TestStringClass");
 input.put("com.example.TestStringClass", new Object()); // not a string
 JsonProcessingException e = assertThrows(JsonProcessingException.class, () -> mapper.readValue(input, deserializer.getType()));
 assertTrue(e.getMessage().contains("Can not instantiate value of type [simple type, class com.example.TestStringClass] from JSON String"));
 }

 @Test
 public void testDeserializeWithTypeIdWithDots() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(TestWithDots.class);
 TypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, mapper.getTypeIdResolver(), "_type", true, TestWithDots.class);
 ObjectNode input = mapper.createObjectNode();
 input.put("type", "com.example.TestWithDots.NestedClass");
 input.put("com.example.TestWithDots.NestedClass", new Object());
 Object result = mapper.readValue(input, deserializer.getType());
 assertEquals(TestWithDots.NestedClass.class, result.getClass());
 }

 @Test
 public void testDeserializeWithTypeIdWithDashes() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(TestWithDashes.class);
 TypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, mapper.getTypeIdResolver(), "_type", true, TestWithDashes.class);
 ObjectNode input = mapper.createObjectNode();
 input.put("type", "com.example.TestWithDashes-NestedClass");
 input.put("com.example.TestWithDashes-NestedClass", new Object());
 Object result = mapper.readValue(input, deserializer.getType());
 assertEquals(TestWithDashes.NestedClass.class, result.getClass());
 }

 @Test
 public void testDeserializeWithTypeIdWithBackslashes() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(TestWithBackslashes.class);
 TypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, mapper.getTypeIdResolver(), "_type", true, TestWithBackslashes.class);
 ObjectNode input = mapper.createObjectNode();
 input.put("type", "com.example.TestWithBackslashes\\.NestedClass");
 input.put("com.example.TestWithBackslashes\\.NestedClass", new Object());
 Object result = mapper.readValue(input, deserializer.getType());
 assertEquals(TestWithBackslashes.NestedClass.class, result.getClass());
 }
}

class CircularReference {
}

class TestWithDots {
 static class NestedClass {
 }
}

class TestWithDashes {
 static class NestedClass {
 }
}

class TestWithBackslashes {
 static class NestedClass {
 }
}

class StdSerializerWithType extends StdSerializer<Object> {
 StdSerializerWithType(Class<Object> t) { super(t); }
 @Override
 public void serializeWithType(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
 jgen.writeStartObject();
 jgen.writeFieldName("type");
 jgen.writeValue(getType(value.getClass()));
 jgen.writeFieldName("value");
 jgen.writeValue(value);
 jgen.writeEndObject();
 }
}