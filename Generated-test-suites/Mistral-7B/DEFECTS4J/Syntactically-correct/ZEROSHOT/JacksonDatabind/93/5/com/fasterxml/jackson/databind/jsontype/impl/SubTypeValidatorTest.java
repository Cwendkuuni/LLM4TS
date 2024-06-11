package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.throwException;

import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

 private SubTypeValidator validator = SubTypeValidator.instance();

 @Test
 public void testValidTypes() {
 // Test valid types
 JavaType validType1 = JavaType.construct(String.class);
 JavaType validType2 = JavaType.construct(Integer.class);
 validator.validateSubType(createContext(), validType1);
 validator.validateSubType(createContext(), validType2);
 }

 @Test
 public void testNastyClasses() {
 // Test nasty classes
 JavaType nastyType1 = JavaType.construct(org.apache.commons.collections4.functors.InstantiateTransformer.class);
 JavaType nastyType2 = JavaType.construct(com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl.class);

 throwException(JsonMappingException.class, () -> validator.validateSubType(createContext(), nastyType1));
 throwException(JsonMappingException.class, () -> validator.validateSubType(createContext(), nastyType2));
 }

 @Test
 public void testSpringClasses() {
 // Test Spring classes
 JavaType springType1 = JavaType.construct(org.springframework.beans.factory.ObjectFactory.class);
 JavaType springType2 = JavaType.construct(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);

 throwException(JsonMappingException.class, () -> validator.validateSubType(createContext(), springType1));
 throwException(JsonMappingException.class, () -> validator.validateSubType(createContext(), springType2));
 }

 @Test
 public void testSpringPrefixClasses() {
 // Test Spring classes with prefix
 JavaType springPrefixType1 = JavaType.construct(org.springframework.aop.support.AbstractPointcutAdvisor.class);
 JavaType springPrefixType2 = JavaType.construct(org.springframework.context.ApplicationContext.class);

 validator.validateSubType(createContext(), springPrefixType1);
 throwException(JsonMappingException.class, () -> validator.validateSubType(createContext(), springPrefixType2));
 }

 @Test
 public void testValidSpringPrefixClasses() {
 // Test valid Spring classes with prefix
 JavaType validSpringPrefixType1 = JavaType.construct(org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor.class);
 JavaType validSpringPrefixType2 = JavaType.construct(org.springframework.beans.factory.config.BeanDefinitionHolder.class);

 validator.validateSubType(createContext(), validSpringPrefixType1);
 validator.validateSubType(createContext(), validSpringPrefixType2);
 }

 @Test
 public void testUnknownPrefixClasses() {
 // Test unknown prefix classes
 JavaType unknownPrefixType1 = JavaType.construct(com.abc.MyClass.class);

 validator.validateSubType(createContext(), unknownPrefixType1);
 }

 private DeserializationContext createContext() {
 return new DeserializationContext(null, new JavaClassInputFactory());
 }
}