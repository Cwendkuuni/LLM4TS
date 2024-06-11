package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

 private static ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testValidClass() throws JsonProcessingException, IOException {
 String json = "{\"class\":\"com.example.ValidClass\"}";
 JavaType type = mapper.getTypeFactory().constructType(JsonType.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mapper.getDeserializationContext(), type);
 }

 @Test(expected = JsonMappingException.class)
 public void testNastyClass() throws JsonProcessingException, IOException {
 String json = "{\"class\":\"org.apache.commons.collections.functors.InstantiateTransformer\"}";
 JavaType type = mapper.getTypeFactory().constructType(JsonType.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mapper.getDeserializationContext(), type);
 }

 @Test(expected = JsonMappingException.class)
 public void testSpringClass() throws JsonProcessingException, IOException {
 String json = "{\"class\":\"org.springframework.beans.factory.config.PropertyPathFactoryBean\"}";
 JavaType type = mapper.getTypeFactory().constructType(JsonType.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mapper.getDeserializationContext(), type);
 }

 @Test(expected = JsonMappingException.class)
 public void testNestedSpringClass() throws JsonProcessingException, IOException {
 String json = "{\"class\":\"org.springframework.beans.factory.ObjectFactory\"}";
 JavaType type = mapper.getTypeFactory().constructType(JsonType.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mapper.getDeserializationContext(), type);
 }

 @Test(expected = JsonMappingException.class)
 public void testCustomNastyClass() throws JsonProcessingException, IOException {
 String json = "{\"class\":\"com.custom.CustomNastyClass\"}";
 JavaType type = mapper.getTypeFactory().constructType(JsonType.class, mapper.getTypeFactory().constructType(String.class), String.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mapper.getDeserializationContext(), type);
 }

 @Test(expected = JsonMappingException.class)
 public void testEmptyClassName() throws JsonProcessingException, IOException {
 String json = "{\"class\":\"\"}";
 JavaType type = mapper.getTypeFactory().constructType(JsonType.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mapper.getDeserializationContext(), type);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidClassName() throws JsonProcessingException, IOException {
 String json = "{\"class\":\"com.invalid.InvalidClass\"}";
 JavaType type = mapper.getTypeFactory().constructType(JsonType.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mapper.getDeserializationContext(), type);
 }

 @Test
 public void testValidClassWithGenericType() throws JsonProcessingException, IOException {
 JavaType type = mapper.getTypeFactory()
 .constructType(Map.class, String.class, JavaType.constructNumberType(Long.TYPE));
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mapper.getDeserializationContext(), type);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidClassWithGenericType() throws JsonProcessingException, IOException {
 JavaType type = mapper.getTypeFactory()
 .constructType(Map.class, String.class, JavaType.constructNumberType(int.class));
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mapper.getDeserializationContext(), type);
 }

 @Test
 public void testValidClassWithCustomType() throws JsonProcessingException, IOException {
 JavaType type = mapper.getTypeFactory()
 .constructType(JsonType.SubTypeValidatorCustomType.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mapper.getDeserializationContext(), type);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidClassWithCustomType() throws JsonProcessingException, IOException {
 JavaType type = mapper.getTypeFactory()
 .constructType(JsonType.SubTypeValidatorCustomType.class, int.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mapper.getDeserializationContext(), type);
 }
}

class JsonType {
 static JavaType constructNumberType(Class<Number> numberType) {
 return mapper.getTypeFactory().constructType(numberType, null);
 }
}

class SubTypeValidatorCustomType {
 // Custom type to test if SubTypeValidator works correctly with custom types
}