package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaTypeResolverBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class SubTypeValidatorTest {

 private SubTypeValidator validator;
 private DeserializationContext context;

 @Before
 public void setup() {
 validator = SubTypeValidator.instance();
 context = mock(DeserializationContext.class);
 }

 @Test
 public void testValidType() throws JsonMappingException {
 JavaType type = JavaTypeResolverBase.constructType(JavaType.TypeReference.of(String.class));
 validator.validateSubType(context, type);
 }

 @Test
 public void testInvalidType() throws JsonMappingException {
 String invalidTypeName = "org.apache.commons.collections4.functors.InstantiateTransformer";
 JavaType invalidType = JavaTypeResolverBase.constructType(JavaType.TypeReference.of(Class.forName(invalidTypeName).getName()));
 validator.validateSubType(context, invalidType);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidTypeThrowsException() throws JsonMappingException {
 String invalidTypeName = "org.apache.commons.collections4.functors.InstantiateTransformer";
 JavaType invalidType = JavaTypeResolverBase.constructType(JavaType.TypeReference.of(Class.forName(invalidTypeName).getName()));
 when(context.getTypeFactory().constructType(invalidTypeName)).thenReturn(invalidType);
 validator.validateSubType(context, JavaTypeResolverBase.constructType(JavaType.TypeReference.of(invalidTypeName)));
 }

 @Test
 public void testPrefixStartingType() throws JsonMappingException {
 String prefixStartingTypeName = "org.springframework.beans.factory.config.PropertyPathFactoryBean";
 JavaType prefixStartingType = JavaTypeResolverBase.constructType(JavaType.TypeReference.of(Class.forName(prefixStartingTypeName).getName()));
 validator.validateSubType(context, prefixStartingType);
 }

 @Test(expected = JsonMappingException.class)
 public void testPrefixStartingTypeThrowsException() throws JsonMappingException {
 String prefixStartingTypeName = "org.springframework.beans.factory.config.AbstractApplicationContext";
 JavaType prefixStartingType = JavaTypeResolverBase.constructType(JavaType.TypeReference.of(Class.forName(prefixStartingTypeName).getName()));
 when(context.getTypeFactory().constructType(prefixStartingTypeName)).thenReturn(prefixStartingType);
 validator.validateSubType(context, JavaTypeResolverBase.constructType(JavaType.TypeReference.of(prefixStartingTypeName)));
 }
}