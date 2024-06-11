package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SubTypeValidatorTest {

 private static ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testValidType() throws JsonProcessingException, JsonMappingException {
 Class<String> type = String.class;
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mapper.getDeserializationContext(), mapper.getTypeFactory().constructType(type));
 }

 @Test
 public void testNastyClass() throws JsonMappingException, IOException {
 String json = "{\"className\": \"org.apache.commons.collections4.functors.InstantiateTransformer\"}";
 Class<Object> type = mapper.getTypeFactory().constructType(Object.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mapper.getDeserializationContext(), mapper.readValue(json, SimpleType.constructorFromJson(mapper.readTree(json), type)));
 }

 @Test(expected = JsonMappingException.class)
 public void testNastyClassWithPrefix() throws JsonMappingException, IOException {
 String json = "{\"className\": \"org.springframework.beans.factory.config.PropertyPathFactoryBean\"}";
 Class<Object> type = mapper.getTypeFactory().constructType(Object.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mapper.getDeserializationContext(), mapper.readValue(json, SimpleType.constructorFromJson(mapper.readTree(json), type)));
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidClass() throws JsonMappingException, IOException {
 String json = "{\"className\": \"com.test.InvalidClass\"}";
 Class<Object> type = mapper.getTypeFactory().constructType(Object.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mapper.getDeserializationContext(), mapper.readValue(json, SimpleType.constructorFromJson(mapper.readTree(json), type)));
 }
}