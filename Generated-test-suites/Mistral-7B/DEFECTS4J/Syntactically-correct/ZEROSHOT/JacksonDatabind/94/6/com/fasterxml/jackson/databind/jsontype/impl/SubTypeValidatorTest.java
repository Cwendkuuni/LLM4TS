package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test cases for {@link SubTypeValidator} class.
 */
public class SubTypeValidatorTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testValidateSubType_ValidTypes() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();

 // Test valid types
 JavaType validType1 = JavaTypeFactory.instance.constructType(String.class);
 validator.validateSubType(mockContext(), validType1);

 JavaType validType2 = JavaTypeFactory.instance.constructType(Integer.class);
 validator.validateSubType(mockContext(), validType2);
 }

 @Test
 public void testValidateSubType_InvalidTypes_SpringFramework() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();

 // Test invalid types with Spring framework classes
 JavaType invalidType1 = JavaTypeFactory.instance.constructType(SubTypeValidator.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), invalidType1));

 JavaType invalidType2 = JavaTypeFactory.instance.constructType(SubTypeValidator.AbstractApplicationContext.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), invalidType2));
 }

 @Test
 public void testValidateSubType_InvalidTypes_NastyClasses() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();

 // Test invalid types with "nasty classes"
 JavaType invalidType1 = JavaTypeFactory.instance.constructType(InvokerTransformer.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), invalidType1));

 JavaType invalidType2 = JavaTypeFactory.instance.constructType(InstantiateTransformer.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), invalidType2));
 }

 @Test
 public void testValidateSubType_InvalidTypes_GroovyRuntime() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();

 // Test invalid types with Groovy runtime classes
 JavaType invalidType1 = JavaTypeFactory.instance.constructType(ConvertedClosure.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), invalidType1));

 JavaType invalidType2 = JavaTypeFactory.instance.constructType(MethodClosure.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), invalidType2));
 }

 @Test
 public void testValidateSubType_InvalidTypes_JdkProvider() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();

 // Test invalid types with JDK provided classes
 JavaType invalidType1 = JavaTypeFactory.instance.constructType(java.util.logging.FileHandler.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), invalidType1));

 JavaType invalidType2 = JavaTypeFactory.instance.constructType(java.rmi.server.UnicastRemoteObject.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), invalidType2));
 }

 @Test
 public void testValidateSubType_InvalidTypes_ThirdParty() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();

 // Test invalid types with third party classes
 JavaType invalidType1 = JavaTypeFactory.instance.constructType(org.apache.tomcat.dbcp.dbcp2.BasicDataSource.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), invalidType1));

 JavaType invalidType2 = JavaTypeFactory.instance.constructType(com.mchange.v2.c3p0.ComboPooledDataSource.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), invalidType2));
 }

 /**
 * Mocks {@link DeserializationContext}.
 */
 private DeserializationContext mockContext() {
 return mock(DeserializationContext.class);
 }
}