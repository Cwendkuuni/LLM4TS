package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SubTypeValidatorTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testValidType() throws JsonProcessingException, IOException {
 String json = "{\"className\": \"com.example.ValidType\"}";
 JavaType validType = MAPPER.getTypeFactory().constructType(String.class);
 DeserializationContext ctxt = MAPPER.getDeserializationContext();
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(ctxt, validType);
 }

 @Test
 public void testNastySpringType() throws JsonProcessingException, IOException {
 String json = "{\"className\": \"org.springframework.beans.factory.config.PropertyPathFactoryBean\"}";
 JavaType nastyType = MAPPER.getTypeFactory().constructType(String.class);
 DeserializationContext ctxt = MAPper.getDeserializationContext();
 SubTypeValidator validator = SubTypeValidator.instance();
 try {
 validator.validateSubType(ctxt, nastyType);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testNastyNonSpringType() throws JsonProcessingException, IOException {
 String json = "{\"className\": \"org.apache.commons.collections.functors.InstantiateTransformer\"}";
 JavaType nastyType = MAPPER.getTypeFactory().constructType(String.class);
 DeserializationContext ctxt = MAPPER.getDeserializationContext();
 SubTypeValidator validator = SubTypeValidator.instance();
 try {
 validator.validateSubType(ctxt, nastyType);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }
}