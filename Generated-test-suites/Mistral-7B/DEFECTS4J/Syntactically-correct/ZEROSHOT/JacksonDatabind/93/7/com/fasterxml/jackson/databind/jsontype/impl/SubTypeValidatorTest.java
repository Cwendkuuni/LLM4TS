package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SubTypeValidatorTest {

 private static final String PREFIX_STRING = "org.springframework.";

 @Mock
 private DeserializationContext deserializationContext;
 @Mock
 private JavaType javaType;

 @InjectMocks
 private SubTypeValidator subTypeValidator;

 private Set<String> defaultNoDeserClassNameSet;

 @Before
 public void setUp() {
 defaultNoDeserClassNameSet = new HashSet<>();
 defaultNoDeserClassNameSet.add("org.apache.commons.collections.functors.InvokerTransformer");
 defaultNoDeserClassNameSet.addAll(subTypeValidator.getDefaultNoDeserClassNames());
 }

 @Test
 public void testValidateSubType_validType() {
 String validTypeName = "java.lang.String";
 when(javaType.getRawClass().getName()).thenReturn(validTypeName);
 subTypeValidator.validateSubType(deserializationContext, javaType);
 // No exception should be thrown
 }

 @Test
 public void testValidateSubType_invalidType() {
 String invalidTypeName = "org.apache.commons.collections.functors.InvokerTransformer";
 when(javaType.getRawClass().getName()).thenReturn(invalidTypeName);
 assertThrows("JsonMappingException", () -> subTypeValidator.validateSubType(deserializationContext, javaType));
 }

 @Test
 public void testValidateSubType_invalidTypeWithPrefix() {
 String invalidTypeName = "org.springframework.beans.factory.config.PropertyPathFactoryBean";
 when(javaType.getRawClass().getName()).thenReturn(invalidTypeName);
 assertThrows("JsonMappingException", () -> subTypeValidator.validateSubType(deserializationContext, javaType));
 }

 @Test
 public void testValidateSubType_prefixTypeWithSubclass() {
 String parentTypeName = "org.springframework.beans.factory.config.AbstractApplicationContext";
 String childTypeName = "org.springframework.web.context.ConfigurableWebApplicationContext";
 Class<?> parentType = mock(parentTypeName);
 Class<?> childType = mock(childTypeName);
 when(parentType.getSuperclass()).thenReturn(childType.getClass());
 when(javaType.getRawClass()).thenReturn(parentType);
 assertThrows("JsonMappingException", () -> subTypeValidator.validateSubType(deserializationContext, javaType));
 }

 @Test
 public void testValidateSubType_prefixTypeWithoutSubclass() {
 String parentTypeName = "org.springframework.beans.factory.config.AbstractApplicationContext";
 when(javaType.getRawClass().getName()).thenReturn(parentTypeName);
 subTypeValidator.validateSubType(deserializationContext, javaType);
 // No exception should be thrown
 }

 @Test
 public void testValidateSubType_prefixTypeWithIncludedClassName() {
 String parentTypeName = "org.springframework.beans.factory.config.AbstractApplicationContext";
 String validTypeName = "org.springframework.web.context.ConfigurableWebApplicationContext";
 when(javaType.getRawClass().getName()).thenReturn(validTypeName);
 assertEquals(defaultNoDeserClassNameSet, subTypeValidator.getDefaultNoDeserClassNames());
 }

 @Test
 public void testValidateSubType_prefixTypeWithExcludedClassName() {
 String parentTypeName = "org.springframework.beans.factory.config.AbstractApplicationContext";
 String excludedTypeName = "org.springframework.beans.factory.config.PropertyPathFactoryBean";
 defaultNoDeserClassNameSet.remove(excludedTypeName);
 when(javaType.getRawClass().getName()).thenReturn(excludedTypeName);
 subTypeValidator = new SubTypeValidator();
 subTypeValidator.setDefaultNoDeserClassNames(defaultNoDeserClassNameSet);
 subTypeValidator.validateSubType(deserializationContext, javaType);
 // No exception should be thrown
 }
}