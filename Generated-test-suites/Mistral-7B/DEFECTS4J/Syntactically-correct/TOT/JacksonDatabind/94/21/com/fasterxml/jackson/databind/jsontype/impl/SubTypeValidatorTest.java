package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

 private ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testValidateSubTypeWithValidClass() throws IOException, JsonProcessingException {
 JavaType validType = TypeFactory.defaultInstance()
 .constructType(String.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(objectMapper.getDeserializationContext(), validType);
 }

 @Test
 public void testValidateSubTypeWithNastyClass() throws IOException, JsonProcessingException {
 JavaType nastyType = TypeFactory.defaultInstance()
 .constructType(SubTypeValidator.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), nastyType));
 }

 @Test
 public void testValidateSubTypeWithSpringClass() throws IOException, JsonProcessingException {
 JavaType springType = TypeFactory.defaultInstance()
 .constructType(AbstractApplicationContext.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), springType));
 }

 @Test
 public void testValidateSubTypeWithInterface() throws IOException, JsonProcessingException {
 JavaType interfaceType = TypeFactory.defaultInstance()
 .constructType(Comparable.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(objectMapper.getDeserializationContext(), interfaceType);
 }

 @Test
 public void testValidateSubTypeWithSpringInterface() throws IOException, JsonProcessingException {
 JavaType springInterfaceType = TypeFactory.defaultInstance()
 .constructType(AbstractPointcutAdvisor.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), springInterfaceType));
 }

 @Test
 public void testValidateSubTypeWithThirdPartyClass() throws IOException, JsonProcessingException {
 JavaType thirdPartyType = TypeFactory.defaultInstance()
 .constructType(org.apache.tomcat.dbcp.dbcp2.BasicDataSource.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), thirdPartyType));
 }

 @Test
 public void testValidateSubTypeWithArray() throws IOException, JsonProcessingException {
 JavaType arrayType = TypeFactory.defaultInstance()
 .constructType(int[].class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(objectMapper.getDeserializationContext(), arrayType);
 }

 @Test
 public void testValidateSubTypeWithCollection() throws IOException, JsonProcessingException {
 JavaType collectionType = TypeFactory.defaultInstance()
 .constructType(HashSet.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(objectMapper.getDeserializationContext(), collectionType);
 }

 @Test
 public void testValidateSubTypeWithMap() throws IOException, JsonProcessingException {
 JavaType mapType = TypeFactory.defaultInstance()
 .constructType(java.util.Map.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(objectMapper.getDeserializationContext(), mapType);
 }

 @Test
 public void testValidateSubTypeWithCustomClass() throws IOException, JsonProcessingException {
 JavaType customType = TypeFactory.defaultInstance()
 .constructType(CustomClass.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(objectMapper.getDeserializationContext(), customType);
 }

 class CustomClass {
 // Empty custom class
 }
}