package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.base.ClassIdResolver;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class SubTypeValidatorTest {

 private SubTypeValidator validator;
 private DeserializationContext context;
 private TypeFactory typeFactory;
 private ClassIdResolver classIdResolver;

 @Before
 public void setup() {
 context = mock(DeserializationContext.class);
 typeFactory = mock(TypeFactory.class);
 classIdResolver = mock(ClassIdResolver.class);
 validator = new SubTypeValidator();
 }

 @Test
 public void testValidClass() throws JsonMappingException {
 String validClass = "com.example.ValidClass";
 JavaType validType = typeFactory.constructType(validClass);
 validator.validateSubType(context, validType);
 verify(context, never()).throwException(any(JsonMappingException.class));
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidClassStartsWithPrefix() throws JsonMappingException {
 String invalidClass = "org.springframework.InvalidClass";
 JavaType invalidType = typeFactory.constructType(invalidClass);
 validator.validateSubType(context, invalidType);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidClassContainsAbstractPointcutAdvisor() throws JsonMappingException {
 String abstractPointcutAdvisorClass = "org.springframework.beans.factory.config.AbstractBeanFactoryPointcutAdvisor";
 JavaType abstractPointcutAdvisorType = typeFactory.constructType(abstractPointcutAdvisorClass);
 validator.validateSubType(context, abstractPointcutAdvisorType);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidClassContainsAbstractApplicationContext() throws JsonMappingException {
 String abstractApplicationContextClass = "org.springframework.context.ApplicationContext";
 JavaType abstractApplicationContextType = typeFactory.constructType(abstractApplicationContextClass);
 validator.validateSubType(context, abstractApplicationContextType);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidClassInList() throws JsonMappingException {
 Set<String> customIllegalClassNames = new HashSet<>();
 customIllegalClassNames.add("com.example.CustomIllegalClass");
 validator = new SubTypeValidator() {{
 _cfgIllegalClassNames = customIllegalClassNames;
 }};
 JavaType customIllegalClassType = typeFactory.constructType("com.example.CustomIllegalClass");
 validator.validateSubType(context, customIllegalClassType);
 }
}