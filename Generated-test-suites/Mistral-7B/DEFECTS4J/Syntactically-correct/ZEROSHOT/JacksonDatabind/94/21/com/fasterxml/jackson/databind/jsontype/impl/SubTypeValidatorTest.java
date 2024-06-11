package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.SubTypeValidator;

public class SubTypeValidatorTest {

 private static final SubTypeValidator VALIDATOR = SubTypeValidator.instance();

 @Test
 public void testValidType() throws JsonMappingException {
 JavaType validType = JavaType.construct(String.class);
 VALIDATOR.validateSubType(new DeserializationContext(null), validType);
 }

 @Test
 public void testInvalidTypeWithPrefixSpring() throws JsonMappingException {
 JavaType invalidType = JavaType.construct(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);
 assertThrows(JsonMappingException.class, () -> VALIDATOR.validateSubType(new DeserializationContext(null), invalidType));
 }

 @Test
 public void testInvalidTypeWithSpringSubclass() throws JsonMappingException {
 JavaType invalidTypeSuperclass = JavaType.construct(Object.class);
 JavaType invalidTypeSubclass = JavaType.construct(AbstractApplicationContext.class);
 JavaType invalidTypeInstance = JavaType.construct(AbstractApplicationContext.class.getSuperclass());
 JavaType invalidType = JavaType.construct(AbstractApplicationContext.class, invalidTypeSubclass, invalidTypeInstance, invalidTypeSuperclass);
 assertThrows(JsonMappingException.class, () -> VALIDATOR.validateSubType(new DeserializationContext(null), invalidType));
 }

 @Test
 public void testInvalidTypeWithCustomPrefix() throws JsonMappingException {
 String customPrefix = "com.custom.";
 SubTypeValidator customValidator = new SubTypeValidator() {
 @Override
 protected Set<String> DEFAULT_NO_DESER_CLASS_NAMES = new HashSet<String>() {{
 add("com.custom.CustomBeanFactoryPointcutAdvisor");
 }};

 @Override
 protected String PREFIX_SPRING = customPrefix;
 };
 JavaType invalidType = JavaType.construct(CustomBeanFactoryPointcutAdvisor.class);
 assertThrows(JsonMappingException.class, () -> customValidator.validateSubType(new DeserializationContext(null), invalidType));
 }

 @Test
 public void testNoIllegalType() {
 JavaType validType = JavaType.construct(Object.class);
 assertThrows(NullPointerException.class, () -> VALIDATOR.validateSubType(null, validType));
 }

 @Test
 public void testIllegalTypeNullName() throws JsonMappingException {
 JavaType illegalType = JavaType.construct((Class<?>) null);
 assertThrows(JsonMappingException.class, () -> VALIDATOR.validateSubType(new DeserializationContext(null), illegalType));
 }

 @Test
 public void testIllegalTypeEmptyName() throws JsonMappingException {
 JavaType illegalType = JavaType.construct(String[].class);
 assertThrows(JsonMappingException.class, () -> VALIDATOR.validateSubType(new DeserializationContext(null), illegalType));
 }

 @Test
 public void testIllegalTypeEmptySuperclass() throws JsonMappingException {
 JavaType illegalType = JavaType.construct(Object.class, Collections.emptyList());
 assertThrows(JsonMappingException.class, () -> VALIDATOR.validateSubType(new DeserializationContext(null), illegalType));
 }

 @Test
 public void testIllegalTypeEmptyInterface() throws JsonMappingException {
 JavaType illegalType = JavaType.construct(Collections.emptyList(), Object.class);
 assertThrows(JsonMappingException.class, () -> VALIDATOR.validateSubType(new DeserializationContext(null), illegalType));
 }

 @Test
 public void testIllegalTypeEmptyClassHierarchy() throws JsonMappingException {
 JavaType illegalType = JavaType.construct(Collections.emptyList(), Collections.emptyList(), Object.class);
 assertThrows(JsonMappingException.class, () -> VALIDATOR.validateSubType(new DeserializationContext(null), illegalType));
 }

 @Test
 public void testIllegalTypeWithoutRawClass() throws JsonMappingException {
 JavaType illegalType = JavaType.construct(Collections.emptyList(), Collections.emptyList());
 assertThrows(IllegalArgumentException.class, () -> VALIDATOR.validateSubType(new DeserializationContext(null), illegalType));
 }

 @Test
 public void testIllegalTypeWithoutDeserializationContext() throws JsonMappingException {
 assertThrows(IllegalArgumentException.class, () -> VALIDATOR.validateSubType((DeserializationContext) null, JavaType.construct(String.class)));
 }
}