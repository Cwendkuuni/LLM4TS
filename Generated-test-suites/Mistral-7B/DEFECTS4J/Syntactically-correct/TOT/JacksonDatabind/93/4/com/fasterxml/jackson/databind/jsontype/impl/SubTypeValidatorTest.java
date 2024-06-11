package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.base.ClassIdResolver;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ClassType;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertThrows;

public class SubTypeValidatorTest {

 private SubTypeValidator validator;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 validator = SubTypeValidator.instance();
 }

 @Test
 public void testValidateSubType_validType() throws JsonMappingException {
 JavaType type = mapper.getTypeFactory().constructType(String.class);
 validator.validateSubType(mapper.getDeserializationContext(), type);
 }

 @Test
 public void testValidateSubType_unsupportedType() throws JsonMappingException {
 JavaType type = mapper.getTypeFactory().constructType(SubTypeValidator.class);
 validator.validateSubType(mapper.getDeserializationContext(), type);
 }

 @Test
 public void testValidateSubType_blacklistedType() throws JsonMappingException {
 // Create a validator instance with a custom blacklist
 Set<String> customBlacklist = new HashSet<>();
 customBlacklist.add("org.springframework.beans.factory.config.PropertyPathFactoryBean");
 SubTypeValidator customValidator = new SubTypeValidator() {
 @Override
 protected Set<String> _cfgIllegalClassNames() {
 return customBlacklist;
 }
 };
 JavaType type = mapper.getTypeFactory().constructType(String.class);
 assertThrows(JsonMappingException.class, () -> customValidator.validateSubType(mapper.getDeserializationContext(), type));
 }

 @Test
 public void testValidateSubType_springApplicationContext() throws JsonMappingException {
 JavaType type = mapper.getTypeFactory().constructType(ClassType.valueOf(ClassIdResolver.getInstance().resolveClassName("org.springframework.context.ApplicationContext")));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), type));
 }

 @Test
 public void testValidateSubType_springBeanFactory() throws JsonMappingException {
 JavaType type = mapper.getTypeFactory().constructType(ClassType.valueOf(ClassIdResolver.getInstance().resolveClassName("org.springframework.beans.factory.config.AbstractApplicationContext")));
 validator.validateSubType(mapper.getDeserializationContext(), type);
 }

 @Test
 public void testValidateSubType_springPointcutAdvisor() throws JsonMappingException {
 JavaType type = mapper.getTypeFactory().constructType(ClassType.valueOf(ClassIdResolver.getInstance().resolveClassName("org.springframework.aop.support.AbstractPointcutAdvisor")));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), type));
 }
}