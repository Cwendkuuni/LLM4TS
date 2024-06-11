package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

 private ObjectMapper objectMapper;
 private DeserializationContext deserializationContext;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 JavaTypeFactory typeFactory = objectMapper.getTypeFactory();
 deserializationContext = objectMapper.getDeserializationContext();
 }

 @Test
 public void testValidSubTypes() {
 Set<String> illegalClassNames = SubTypeValidator.instance()._cfgIllegalClassNames;
 // Test valid subtypes
 JavaType validType1 = typeFactory.constructType(SubTypeValidator.class);
 SubTypeValidator validator1 = objectMapper.readValue(objectMapper.writeValueAsBytes(validType1), SubTypeValidator.class);
 validator1.validateSubType(deserializationContext, validType1);
 JavaType validType2 = typeFactory.constructType(String.class);
 SubTypeValidator validator2 = objectMapper.readValue(objectMapper.writeValueAsBytes(validType2), SubTypeValidator.class);
 validator2.validateSubType(deserializationContext, validType2);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidSubType1() {
 JavaType invalidType1 = typeFactory.constructType("org.apache.commons.collections.functors.InvokerTransformer");
 SubTypeValidator validator1 = objectMapper.readValue(objectMapper.writeValueAsBytes(invalidType1), SubTypeValidator.class);
 validator1.validateSubType(deserializationContext, invalidType1);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidSubType2() {
 JavaType invalidType2 = typeFactory.constructType("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl");
 SubTypeValidator validator2 = objectMapper.readValue(objectMapper.writeValueAsBytes(invalidType2), SubTypeValidator.class);
 validator2.validateSubType(deserializationContext, invalidType2);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidSubType3() {
 JavaType invalidType3 = typeFactory.constructType("java.util.logging.FileHandler");
 SubTypeValidator validator3 = objectMapper.readValue(objectMapper.writeValueAsBytes(invalidType3), SubTypeValidator.class);
 validator3.validateSubType(deserializationContext, invalidType3);
 }

 @Test
 public void testCheckNestedClasses() {
 JavaType nestedType1 = typeFactory.constructType("java.lang.String$CaseInsensitiveComparator");
 SubTypeValidator validator1 = objectMapper.readValue(objectMapper.writeValueAsBytes(nestedType1), SubTypeValidator.class);
 validator1.validateSubType(deserializationContext, nestedType1);
 }

 @Test(expected = JsonMappingException.class)
 public void testCheckNestedClassesInvalid() {
 JavaType nestedType2 = typeFactory.constructType("java.lang.String$CaseInsensitiveComparator$NullSafeComparator");
 SubTypeValidator validator2 = objectMapper.readValue(objectMapper.writeValueAsBytes(nestedType2), SubTypeValidator.class);
 validator2.validateSubType(deserializationContext, nestedType2);
 }

 @Test
 public void testCheckSpringClassesWithinLimit() {
 JavaType springType1 = typeFactory.constructType("org.springframework.beans.factory.config.PropertyPathFactoryBean");
 SubTypeValidator validator1 = objectMapper.readValue(objectMapper.writeValueAsBytes(springType1), SubTypeValidator.class);
 validator1.validateSubType(deserializationContext, springType1);
 }

 @Test(expected = JsonMappingException.class)
 public void testCheckSpringClassesBeyondLimit() {
 JavaType springType2 = typeFactory.constructType("org.springframework.beans.factory.support.DefaultListableBeanFactory");
 SubTypeValidator validator2 = objectMapper.readValue(objectMapper.writeValueAsBytes(springType2), SubTypeValidator.class);
 validator2.validateSubType(deserializationContext, springType2);
 }

 @Test
 public void testCheckSpringClassesWithinLimitPrefix() {
 JavaType springType3 = typeFactory.constructType("org.springframework.beans.factory.config.PropertyPlaceholderConfigurer");
 SubTypeValidator validator3 = objectMapper.readValue(objectMapper.writeValueAsBytes(springType3), SubTypeValidator.class);
 validator3.validateSubType(deserializationContext, springType3);
 }

 @Test(expected = JsonMappingException.class)
 public void testCheckSpringClassesBeyondLimitPrefix() {
 JavaType springType4 = typeFactory.constructType("org.springframework.core.io.support.AbstractPropertySource");
 SubTypeValidator validator4 = objectMapper.readValue(objectMapper.writeValueAsBytes(springType4), SubTypeValidator.class);
 validator4.validateSubType(deserializationContext, springType4);
 }

 @Test
 public void testCheckSpringClassesWithinLimitSuffix() {
 JavaType springType5 = typeFactory.constructType("org.springframework.beans.factory.config.MethodInvokingFactoryBean");
 SubTypeValidator validator5 = objectMapper.readValue(objectMapper.writeValueAsBytes(springType5), SubTypeValidator.class);
 validator5.validateSubType(deserializationContext, springType5);
 }

 @Test(expected = JsonMappingException.class)
 public void testCheckSpringClassesBeyondLimitSuffix() {
 JavaType springType6 = typeFactory.constructType("org.springframework.beans.factory.support.DefaultSingletonBeanRegistry");
 SubTypeValidator validator6 = objectMapper.readValue(objectMapper.writeValueAsBytes(springType6), SubTypeValidator.class);
 validator6.validateSubType(deserializationContext, springType6);
 }

 @Test
 public void testCheckOtherClassesWithinLimit() {
 JavaType otherType1 = typeFactory.constructType("com.mchange.v2.c3p0.JndiRefForwardingDataSource");
 SubTypeValidator validator1 = objectMapper.readValue(objectMapper.writeValueAsBytes(otherType1), SubTypeValidator.class);
 validator1.validateSubType(deserializationContext, otherType1);
 }

 @Test(expected = JsonMappingException.class)
 public void testCheckOtherClassesBeyondLimit() {
 JavaType otherType2 = typeFactory.constructType("com.mchange.v2.c3p0.impl.AbstractPoolBackedDataSource");
 SubTypeValidator validator2 = objectMapper.readValue(objectMapper.writeValueAsBytes(otherType2), SubTypeValidator.class);
 validator2.validateSubType(deserializationContext, otherType2);
 }

 @Test
 public void testCheckOtherClassesWithinLimitPrefix() {
 JavaType otherType3 = typeFactory.constructType("com.sun.org.apache.bcel.internal.util.ClassLoader");
 SubTypeValidator validator3 = objectMapper.readValue(objectMapper.writeValueAsBytes(otherType3), SubTypeValidator.class);
 validator3.validateSubType(deserializationContext, otherType3);
 }

 @Test(expected = JsonMappingException.class)
 public void testCheckOtherClassesBeyondLimitPrefix() {
 JavaType otherType4 = typeFactory.constructType("com.sun.org.apache.bcel.internal.classfile.ClassParser");
 SubTypeValidator validator4 = objectMapper.readValue(objectMapper.writeValueAsBytes(otherType4), SubTypeValidator.class);
 validator4.validateSubType(deserializationContext, otherType4);
 }

 @Test
 public void testCheckOtherClassesWithinLimitSuffix() {
 JavaType otherType5 = typeFactory.constructType("java.util.concurrent.locks.ReentrantLock");
 SubTypeValidator validator5 = objectMapper.readValue(objectMapper.writeValueAsBytes(otherType5), SubTypeValidator.class);
 validator5.validateSubType(deserializationContext, otherType5);
 }

 @Test(expected = JsonMappingException.class)
 public void testCheckOtherClassesBeyondLimitSuffix() {
 JavaType otherType6 = typeFactory.constructType("java.util.concurrent.locks.AbstractQueuedSynchronizer");
 SubTypeValidator validator6 = objectMapper.readValue(objectMapper.writeValueAsBytes(otherType6), SubTypeValidator.class);
 validator6.validateSubType(deserializationContext, otherType6);
 }

 @Test
 public void testCheckSpringClassesWithinLimitPrefixAndSuffix() {
 JavaType springType7 = typeFactory.constructType("org.springframework.beans.factory.support.DefaultListableBeanFactory$DependentBean");
 SubTypeValidator validator7 = objectMapper.readValue(objectMapper.writeValueAsBytes(springType7), SubTypeValidator.class);
 validator7.validateSubType(deserializationContext, springType7);
 }

 @Test(expected = JsonMappingException.class)
 public void testCheckSpringClassesBeyondLimitPrefixAndSuffix() {
 JavaType springType8 = typeFactory.constructType("org.springframework.beans.factory.support.AbstractBeanDefinition");
 SubTypeValidator validator8 = objectMapper.readValue(objectMapper.writeValueAsBytes(springType8), SubTypeValidator.class);
 validator8.validateSubType(deserializationContext, springType8);
 }

 @Test
 public void testCheckOtherClassesWithinLimitPrefixAndSuffix() {
 JavaType otherType7 = typeFactory.constructType("java.lang.reflect.Method$JavaMethod");
 SubTypeValidator validator7 = objectMapper.readValue(objectMapper.writeValueAsBytes(otherType7), SubTypeValidator.class);
 validator7.validateSubType(deserializationContext, otherType7);
 }

 @Test(expected = JsonMappingException.class)
 public void testCheckOtherClassesBeyondLimitPrefixAndSuffix() {
 JavaType otherType8 = typeFactory.constructType("java.lang.reflect.Method");
 SubTypeValidator validator8 = objectMapper.readValue(objectMapper.writeValueAsBytes(otherType8), SubTypeValidator.class);
 validator8.validateSubType(deserializationContext, otherType8);
 }
}