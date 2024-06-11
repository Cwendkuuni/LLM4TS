package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.base.ClassIdResolver;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

public class SubTypeValidatorTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testValidType() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType validType = MAPper.getTypeFactory().constructType(String.class);
 validator.validateSubType(MAPper.getContext(), validType);
 }

 @Test
 public void testSpringClassWithSubclassBanned() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 Class<AbstractApplicationContext> springClass = AbstractApplicationContext.class;
 JavaType javaType = MAPper.getTypeFactory().constructType(springClass);
 validator.validateSubType(MAPper.getContext(), javaType);
 }

 @Test(expected = JsonMappingException.class)
 public void testBannedClass() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 Class<org.apache.commons.collections.functors.InstantiateTransformer> bannedClass = InstantiateTransformer.class;
 JavaType javaType = MAPper.getTypeFactory().constructType(bannedClass);
 validator.validateSubType(MAPper.getContext(), javaType);
 }

 @Test(expected = JsonMappingException.class)
 public void testBannedClassWithPrefixSpring() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 Class<AbstractBeanFactoryPointcutAdvisor> bannedClass = AbstractBeanFactoryPointcutAdvisor.class;
 JavaType javaType = MAPper.getTypeFactory().constructType(bannedClass);
 validator.validateSubType(MAPper.getContext(), javaType);
 }

 @Test
 public void testUnknownClass() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 Class<UnknownClass> unknownClass = UnknownClass.class;
 JavaType javaType = MAPper.getTypeFactory().constructType(unknownClass);
 validator.validateSubType(MAPper.getContext(), javaType);
 }

 @Test
 public void testEmptyClass() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 Class<?> emptyClass = Object.class;
 JavaType javaType = MAPper.getTypeFactory().constructType(emptyClass);
 validator.validateSubType(MAPper.getContext(), javaType);
 }
}