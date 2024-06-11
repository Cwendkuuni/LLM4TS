package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.util.Collections;

import org.junit.Test;

import com.fasterxml.jackson.core.base.ClassIdResolver;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

 private static final SubTypeValidator validator = SubTypeValidator.instance();

 @Test
 public void testValidateSubTypeWithValidType() throws Exception {
 JavaType validType = TypeFactory.defaultInstance().constructType(String.class);
 DeserializationContext ctxt = new DeserializationContext(new StandardSerializer(String.class), null, null);
 validator.validateSubType(ctxt, validType);
 }

 @Test
 public void testValidateSubTypeWithInvalidPrefixClass() throws Exception {
 ClassIdResolver classIdResolver = new DefaultClassIdResolver();
 JavaType prefixClass = TypeFactory.defaultInstance().constructType(ClassIdResolver.class, classIdResolver.getClassId(Object.class));
 DeserializationContext ctxt = new DeserializationContext(new StandardSerializer(Object.class), null, null);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, prefixClass));
 }

 @Test
 public void testValidateSubTypeWithInvalidClass() throws Exception {
 ClassIdResolver classIdResolver = new DefaultClassIdResolver();
 JavaType invalidClass = TypeFactory.defaultInstance().constructType(ClassIdResolver.class, classIdResolver.getClassId("org.apache.commons.collections.functors.InvokerTransformer"));
 DeserializationContext ctxt = new DeserializationContext(new StandardSerializer(Object.class), null, null);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, invalidClass));
 }

 @Test
 public void testValidateSubTypeWithValidSpringClass() throws Exception {
 ClassIdResolver classIdResolver = new DefaultClassIdResolver();
 JavaType validSpringClass = TypeFactory.defaultInstance().constructType(ClassIdResolver.class, classIdResolver.getClassId("org.springframework.beans.factory.config.PropertyPathFactoryBean"));
 DeserializationContext ctxt = new DeserializationContext(new StandardSerializer(Object.class), null, null);
 validator.validateSubType(ctxt, validSpringClass);
 }

 @Test
 public void testValidateSubTypeWithInvalidSpringClass() throws Exception {
 ClassIdResolver classIdResolver = new DefaultClassIdResolver();
 JavaType invalidSpringClass = TypeFactory.defaultInstance().constructType(ClassIdResolver.class, classIdResolver.getClassId("AbstractBeanFactoryPointcutAdvisor"));
 DeserializationContext ctxt = new DeserializationContext(new StandardSerializer(Object.class), null, null);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, invalidSpringClass));
 }
}