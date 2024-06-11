package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

 private final static SubTypeValidator validator = SubTypeValidator.instance();

 @Test
 public void testValidType() {
 // Test valid type
 JavaType validType = new JavaType(String.class);
 try {
 validator.validateSubType(new DeserializationContext(null), validType);
 } catch (JsonMappingException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testSpringClass() {
 // Test valid Spring class
 Class<Object> springClass = (Class<Object>) Class.forName("org.springframework.beans.factory.ObjectFactory");
 JavaType springJavaType = new JavaType(springClass);
 try {
 validator.validateSubType(new DeserializationContext(null), springJavaType);
 } catch (JsonMappingException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testInvalidType() {
 // Test invalid type
 Class<Object> invalidClass = Class.forName("org.apache.commons.collections4.functors.InstantiateTransformer");
 JavaType invalidJavaType = new JavaType(invalidClass);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), invalidJavaType));
 }

 @Test
 public void testPrefixSpringClass() {
 // Test valid Spring class starting with prefix "org.springframework."
 Class<Object> prefixSpringClass = Class.forName("org.springframework.beans.factory.config.PropertyPathFactoryBean");
 JavaType prefixSpringJavaType = new JavaType(prefixSpringClass);
 try {
 validator.validateSubType(new DeserializationContext(null), prefixSpringJavaType);
 } catch (JsonMappingException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testInvalidPrefixSpringClass() {
 // Test invalid Spring class starting with prefix "org.springframework."
 Class<Object> invalidPrefixSpringClass = Class.forName("org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor");
 JavaType invalidPrefixSpringJavaType = new JavaType(invalidPrefixSpringClass);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), invalidPrefixSpringJavaType));
 }

 @Test
 public void testHashSet() {
 Set<String> hashSet = new HashSet<>();
 hashSet.add(SubTypeValidator.PREFIX_SPRING + "AbstractApplicationContext");
 Set<String> illegalClassNames = validator.getIllegalClassNames();
 assertTrue(illegalClassNames.contains(SubTypeValidator.PREFIX_SPRING + "AbstractApplicationContext"));
 }

}