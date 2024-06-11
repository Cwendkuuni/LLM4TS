package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * Test class for {@link SubTypeValidator}
 */
public class SubTypeValidatorTest {

 private static SubTypeValidator validator;

 @BeforeClass
 public static void setup() {
 SubTypeValidator.instance(); // initialize static instance
 validator = SubTypeValidator.instance();
 }

 @Test
 public void testValidateSubType_withValidType() throws JsonMappingException {
 JavaType validType = JavaType.construct(int.class);
 validator.validateSubType(new DeserializationContext(new TestContext()), validType);
 }

 @Test
 public void testValidateSubType_withInvalidType() throws JsonMappingException {
 JavaType invalidType = JavaType.construct(SubTypeValidator.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(new TestContext()), invalidType));
 }

 @Test
 public void testValidateSubType_withSpringType() throws JsonMappingException {
 JavaType springType = JavaType.construct(SubTypeValidator.class.getPackage().getName() + ".AbstractApplicationContext");
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(new TestContext()), springType));
 }

 @Test
 public void testValidateSubType_withCustomInvalidType() throws JsonMappingException {
 Set<String> customIllegalClassNames = new HashSet<>();
 customIllegalClassNames.add("com.custom.NastyClass");
 SubTypeValidator customValidator = new SubTypeValidator() {
 @Override
 protected Set<String> _cfgIllegalClassNames = customIllegalClassNames;
 };
 JavaType customInvalidType = JavaType.construct(CustomClass.class);
 assertThrows(JsonMappingException.class, () -> customValidator.validateSubType(new DeserializationContext(new TestContext()), customInvalidType));
 }

 @Test
 public void testValidateSubType_withSpringInterface() throws JsonMappingException {
 JavaType springInterface = JavaType.construct(SubTypeValidator.class.getPackage().getName() + ".AbstractPointcutAdvisor");
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(new TestContext()), springInterface));
 }

 @Test
 public void testValidateSubType_withCustomSpringInterface() throws JsonMappingException {
 Set<String> customIllegalClassNames = new HashSet<>();
 customIllegalClassNames.add("com.custom.CustomPointcutAdvisor");
 SubTypeValidator customValidator = new SubTypeValidator() {
 @Override
 protected Set<String> _cfgIllegalClassNames = customIllegalClassNames;
 };
 JavaType customSpringInterface = JavaType.construct(CustomPointcutAdvisor.class);
 assertThrows(JsonMappingException.class, () -> customValidator.validateSubType(new DeserializationContext(new TestContext()), customSpringInterface));
 }

 @Test
 public void testValidateSubType_withSuperclassOfSpringType() throws JsonMappingException {
 JavaType superclassOfSpringType = JavaType.construct(SubTypeValidator.class.getPackage().getName() + ".AbstractBeanFactory");
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(new TestContext()), superclassOfSpringType));
 }

 @Test
 public void testValidateSubType_withCustomSuperclassOfSpringType() throws JsonMappingException {
 Set<String> customIllegalClassNames = new HashSet<>();
 customIllegalClassNames.add("com.custom.CustomBeanFactory");
 SubTypeValidator customValidator = new SubTypeValidator() {
 @Override
 protected Set<String> _cfgIllegalClassNames = customIllegalClassNames;
 };
 JavaType customSuperclassOfSpringType = JavaType.construct(CustomBeanFactory.class);
 assertThrows(JsonMappingException.class, () -> customValidator.validateSubType(new DeserializationContext(new TestContext()), customSuperclassOfSpringType));
 }

 /**
 * Test context for deserialization context in tests
 */
 static class TestContext extends DeserializationContext {
 @Override
 public DeserializationConfig getConfig() {
 return null;
 }
 }

 static class CustomClass {
 // empty class for testing
 }

 static class CustomPointcutAdvisor {
 // empty interface for testing
 }

 static class CustomBeanFactory {
 // empty class for testing
 }
}