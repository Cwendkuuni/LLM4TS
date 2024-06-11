package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Test class for {@link SubTypeValidator}.
 */
public class SubTypeValidatorTest {

 private static final SubTypeValidator INSTANCE = SubTypeValidator.instance();

 @Test
 public void testValidateSubTypeWithAllowedClass() throws Exception {
 // Given
 JavaType javaType = new JavaType(Object.class);

 // When
 INSTANCE.validateSubType(createMockContext(), javaType);

 // Then
 // Nothing should be thrown
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubTypeWithDisallowedClass() throws Exception {
 // Given
 String disallowedClassName = "org.apache.commons.collections.functors.InvokerTransformer";
 JavaType javaType = new JavaType(Class.forName(disallowedClassName));

 // When
 // Then an exception should be thrown
 INSTANCE.validateSubType(createMockContext(), javaType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubTypeWithDisallowedSpringClass() throws Exception {
 // Given
 String disallowedSpringClassName = "org.springframework.beans.factory.config.PropertyPathFactoryBean";
 JavaType javaType = new JavaType(Class.forName(disallowedSpringClassName));

 // When
 // Then an exception should be thrown
 INSTANCE.validateSubType(createMockContext(), javaType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubTypeWithDisallowedThirdPartyClass() throws Exception {
 // Given
 String disallowedThirdPartyClassName = "com.sun.org.apache.bcel.internal.util.ClassLoader";
 JavaType javaType = new JavaType(Class.forName(disallowedThirdPartyClassName));

 // When
 // Then an exception should be thrown
 INSTANCE.validateSubType(createMockContext(), javaType);
 }

 @Test
 public void testValidateSubTypeWithInterface() throws Exception {
 // Given
 Class<?> interfaceType = Object.class.getInterface("AnyInterface");
 JavaType javaType = new JavaType(interfaceType);

 // When
 // Then no exception should be thrown
 INSTANCE.validateSubType(createMockContext(), javaType);
 }

 @Test
 public void testValidateSubTypeWithSpringClassNotInBlackList() throws Exception {
 // Given
 String notInBlackListSpringClassName = "org.springframework.beans.factory.ObjectFactory";
 JavaType javaType = new JavaType(Class.forName(notInBlackListSpringClassName));

 // When
 // Then no exception should be thrown
 INSTANCE.validateSubType(createMockContext(), javaType);
 }

 @Test
 public void testValidateSubTypeWithSuperclassChain() throws Exception {
 // Given
 String superclassChainSpringClassName = "org.springframework.beans.factory.AbstractApplicationContext";
 Class<?> raw = Class.forName(superclassChainSpringClassName);
 JavaType javaType = new JavaType(raw);

 // When
 // Then no exception should be thrown
 INSTANCE.validateSubType(createMockContext(), javaType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubTypeWithSpringClassInBlackList() throws Exception {
 // Given
 String springClassNameInBlackList = "org.springframework.beans.factory.AbstractBeanFactoryPointcutAdvisor";
 Class<?> raw = Class.forName(springClassNameInBlackList);
 JavaType javaType = new JavaType(raw);

 // When
 // Then an exception should be thrown
 INSTANCE.validateSubType(createMockContext(), javaType);
 }

 private DeserializationContext createMockContext() {
 return mock(DeserializationContext.class);
 }

}