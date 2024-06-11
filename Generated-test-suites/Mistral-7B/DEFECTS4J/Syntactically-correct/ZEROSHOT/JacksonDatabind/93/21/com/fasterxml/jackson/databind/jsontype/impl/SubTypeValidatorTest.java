package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

public class SubTypeValidatorTest {

 private static SubTypeValidator validator;
 static Set<String> defaultNoDeserClassNameSet;

 @BeforeClass
 public static void setUpBeforeClass() {
 SubTypeValidator.instance(); // initialize the validator instance
 defaultNoDeserClassNameSet = SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES;
 }

 @Test
 public void testValidSubType() throws JsonMappingException {
 JavaType validType = JavaType.construct(int[].class);
 validator.validateSubType(new DeserializationContext(new TestContext()), validType);
 }

 @Test
 public void testInvalidSubTypeWithSamePrefix() throws JsonMappingException {
 JavaType invalidType = JavaType.construct(SubTypeValidator.class.getName().substring(0, 23) + "MyCustomClass.class");
 validator.validateSubType(new DeserializationContext(new TestContext()), invalidType);
 }

 @Test
 public void testInvalidSubTypeWithSameClassName() throws JsonMappingException {
 JavaType invalidType = JavaType.construct(int[].class);
 validator.validateSubType(new DeserializationContext(new TestContext()), invalidType);
 }

 @Test
 public void testInvalidSubTypeWithPrefixAndSuffix() throws JsonMappingException {
 JavaType invalidType = JavaType.construct(SubTypeValidator.class.getName().substring(0, 23) + "MyCustomClassWithSuffix.class");
 validator.validateSubType(new DeserializationContext(new TestContext()), invalidType);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidSubTypeWithIllegalClassName() throws JsonMappingException {
 JavaType invalidType = JavaType.construct(org.apache.commons.collections4.functors.InstantiateTransformer.class);
 validator.validateSubType(new DeserializationContext(new TestContext()), invalidType);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidSubTypeWithIllegalClassNameSubclass() throws JsonMappingException {
 JavaType invalidType = JavaType.construct(org.apache.commons.collections4.Transformer.class);
 validator.validateSubType(new DeserializationContext(new TestContext()), invalidType);
 }

 @Test
 public void testHasDefaultNoDeserClassNames() {
 assertEquals(defaultNoDeserClassNameSet, SubTypeValidator._cfgIllegalClassNames);
 }

 @Test
 public void testValidateSubTypeThrowsJsonMappingExceptionForIllegalClasses() {
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(new TestContext()), JavaType.construct(org.apache.commons.collections4.functors.InstantiateTransformer.class)));
 }

 @Test
 public void testValidateSubTypeReturnsEmptyForValidClasses() throws JsonMappingException {
 JavaType validType = JavaType.construct(String[].class);
 assertEquals(null, validator.validateSubType(new DeserializationContext(new TestContext()), validType));
 }
}

class TestContext extends DeserializationContext {
 @Override
 public JavaType constructType(Class<?> baseType) {
 return null;
 }
}