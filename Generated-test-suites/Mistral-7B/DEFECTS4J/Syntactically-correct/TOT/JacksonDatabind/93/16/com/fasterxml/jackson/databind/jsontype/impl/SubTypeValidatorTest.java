package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.base.ClassIdResolver;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubTypeValidatorTest {

 private SubTypeValidator validator = SubTypeValidator.instance();

 @Test
 public void testValidType() throws JsonMappingException {
 String validType = "com.example.valid.ClassName";
 JavaType validTypeJavaType = ClassIdResolver.instance.resolveType(validType, null);
 validator.validateSubType(mock(DeserializationContext.class), validTypeJavaType);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidTypeWithPrefix() throws JsonMappingException {
 String invalidType = "org.springframework.beans.factory.config.PropertyPathFactoryBean";
 JavaType invalidTypeJavaType = ClassIdResolver.instance.resolveType(invalidType, null);
 validator.validateSubType(mock(DeserializationContext.class), invalidTypeJavaType);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidTypeWithSimpleName() throws JsonMappingException {
 String invalidType = "AbstractApplicationContext";
 JavaType invalidTypeJavaType = ClassIdResolver.instance.resolveType(SubTypeValidator.class.getPackage().getName() + "." + invalidType, null);
 validator.validateSubType(mock(DeserializationContext.class), invalidTypeJavaType);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidTypeWithFullName() throws JsonMappingException {
 String invalidType = "org.springframework.beans.factory.config.PropertyPathFactoryBean";
 JavaType invalidTypeJavaType = ClassIdResolver.instance.resolveType(invalidType, null);
 validator.validateSubType(mock(DeserializationContext.class), invalidTypeJavaType);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidTypeWithNestedSuperClass() throws JsonMappingException {
 String invalidType = "com.example.nesting.NestedClassWithAbstractPointcutAdvisor";
 JavaType invalidTypeJavaType = ClassIdResolver.instance.resolveType(invalidType, null);
 validator.validateSubType(mock(DeserializationContext.class), invalidTypeJavaType);
 }

 @Test
 public void testValidTypeWithPrefixButNotProblematic() throws JsonMappingException {
 String validType = "org.springframework.beans.factory.BeanFactory";
 JavaType validTypeJavaType = ClassIdResolver.instance.resolveType(validType, null);
 validator.validateSubType(mock(DeserializationContext.class), validTypeJavaType);
 }

 @Test
 public void testEmptyType() throws JsonMappingException {
 String emptyType = "";
 JavaType emptyTypeJavaType = ClassIdResolver.instance.resolveType(emptyType, null);
 validator.validateSubType(mock(DeserializationContext.class), emptyTypeJavaType);
 }

 @Test
 public void testNullType() throws JsonMappingException {
 JavaType nullType = null;
 validator.validateSubType(mock(DeserializationContext.class), nullType);
 }
}