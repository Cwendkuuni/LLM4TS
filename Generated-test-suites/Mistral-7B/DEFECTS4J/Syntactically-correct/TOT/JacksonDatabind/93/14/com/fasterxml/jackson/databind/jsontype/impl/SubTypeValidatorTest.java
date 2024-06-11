package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.HashSet;

import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

 private static ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testValidType() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType validType = TypeFactory.defaultInstance().constructType(String.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 validator.validateSubType(ctxt, validType);
 }

 @Test
 public void testNastyClassWithSimpleName() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType nastyClass = mapper.getTypeFactory().constructType(org.apache.commons.collections4.functors.InstantiateTransformer.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, nastyClass));
 }

 @Test
 public void testNastyClassWithPrefix() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType nastyClass = mapper.getTypeFactory().constructType(com.springframework.beans.factory.config.PropertyPathFactoryBean.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, nastyClass));
 }

 @Test
 public void testValidTypeWithSuperClass() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType validType = mapper.getTypeFactory().constructType(AbstractPointcutAdvisor.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 validator.validateSubType(ctxt, validType);
 }

 @Test(expected = JsonMappingException.class)
 public void testNastyClassWithSuperClass() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType nastyClass = mapper.getTypeFactory().constructType(AbstractBeanFactoryPointcutAdvisor.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 validator.validateSubType(ctxt, nastyClass);
 }

}