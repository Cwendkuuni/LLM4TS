package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * Unit tests for {@link SubTypeValidator} class
 */
public class SubTypeValidatorTest {

 private static final SubTypeValidator VALIDATOR = SubTypeValidator.instance();

 @Test
 public void testValidateSubType_validTypes() throws JsonMappingException, JsonProcessingException {
 // Test valid types
 JavaType validType1 = JavaType.construct(int[].class);
 VALIDATOR.validateSubType(new TestContext(), validType1);

 JavaType validType2 = JavaType.construct(String.class);
 VALIDATOR.validateSubType(new TestContext(), validType2);
 }

 @Test
 public void testValidateSubType_invalidTypes() throws JsonMappingException, JsonProcessingException {
 // Test invalid types
 Set<String> invalidSet = new HashSet<String>();
 invalidSet.add("org.apache.commons.collections.functors.InvokerTransformer");

 SubTypeValidator validator = new SubTypeValidator();
 invalidSet.addAll(validator._cfgIllegalClassNames);

 JavaType invalidType1 = JavaType.construct(invalidSet.toArray(new String[0]));
 assertThrows(JsonMappingException.class, () -> {
 VALIDATOR.validateSubType(new TestContext(), invalidType1);
 });

 JavaType invalidType2 = JavaType.construct(Object[].class);
 assertThrows(JsonMappingException.class, () -> {
 VALIDATOR.validateSubType(new TestContext(), invalidType2);
 });
 }

 @Test
 public void testValidateSubType_springTypes() throws JsonMappingException, JsonProcessingException {
 // Test Spring types
 JavaType springType1 = JavaType.construct(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);
 VALIDATOR.validateSubType(new TestContext(), springType1);

 JavaType springType2 = JavaType.construct(org.springframework.beans.factory.ObjectFactory.class);
 assertThrows(JsonMappingException.class, () -> {
 VALIDATOR.validateSubType(new TestContext(), springType2);
 });
 }

 @Test
 public void testValidateSubType_customIllegalTypes() throws JsonMappingException, JsonProcessingException {
 // Test custom illegal types
 SubTypeValidator customValidator = new SubTypeValidator();
 Set<String> customIllegalSet = new HashSet<String>();
 customIllegalSet.add("com.example.MyIllegalType");
 customValidator._cfgIllegalClassNames = customIllegalSet;

 JavaType customIllegalType = JavaType.construct(CustomIllegalType.class);
 assertThrows(JsonMappingException.class, () -> {
 customValidator.validateSubType(new TestContext(), customIllegalType);
 });
 }

 private static class TestContext extends DeserializationContext {
 @Override
 public Set<Class<?>> activeValueTypes() {
 return Collections.emptySet();
 }
 }

 private static class CustomIllegalType {
 // Empty class for testing
 }

}