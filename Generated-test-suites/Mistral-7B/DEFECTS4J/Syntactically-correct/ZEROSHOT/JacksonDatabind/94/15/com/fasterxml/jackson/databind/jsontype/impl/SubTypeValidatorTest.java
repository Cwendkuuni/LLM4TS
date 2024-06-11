package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

 private static SubTypeValidator validator;

 @BeforeClass
 public static void setUpBeforeClass() {
 validator = SubTypeValidator.instance();
 }

 @Test
 public void testValidTypes() throws JsonMappingException {
 JavaType javaType1 = TypeFactory.defaultInstance().constructType(String.class);
 validator.validateSubType(new DeserializationContext(null, JavaTypeFactory.instance(), null), javaType1);

 JavaType javaType2 = TypeFactory.defaultInstance().constructType(Integer.class);
 validator.validateSubType(new DeserializationContext(null, JavaTypeFactory.instance(), null), javaType2);
 }

 @Test
 public void testNastyClasses() throws JsonMappingException {
 JavaType javaType1 = TypeFactory.defaultInstance().constructType(String.class);
 JavaType javaType2 = TypeFactory.defaultInstance().constructType(com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator.class);
 JavaType javaType3 = TypeFactory.defaultInstance().constructType(org.apache.commons.collections.functors.InvokerTransformer.class);
 JavaType javaType4 = TypeFactory.defaultInstance().constructType(org.springframework.beans.factory.ObjectFactory.class);
 JavaType javaType5 = TypeFactory.defaultInstance().constructType(com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl.class);

 validator.validateSubType(new DeserializationContext(null, JavaTypeFactory.instance(), null), javaType1);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null, JavaTypeFactory.instance(), null), javaType2));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null, JavaTypeFactory.instance(), null), javaType3));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null, JavaTypeFactory.instance(), null), javaType4));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null, JavaTypeFactory.instance(), null), javaType5));
 }

 @Test
 public void testSpringClasses() throws JsonMappingException {
 JavaType javaType1 = TypeFactory.defaultInstance().constructType(String.class);
 JavaType javaType2 = TypeFactory.defaultInstance().constructType(com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator.class);
 JavaType javaType3 = TypeFactory.defaultInstance().constructType(org.springframework.beans.factory.AbstractApplicationContext.class);
 JavaType javaType4 = TypeFactory.defaultInstance().constructType(org.springframework.beans.factory.AbstractPointcutAdvisor.class);

 validator.validateSubType(new DeserializationContext(null, JavaTypeFactory.instance(), null), javaType1);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null, JavaTypeFactory.instance(), null), javaType2));
 // AbstractApplicationContext is not in the list, let it pass
 validator.validateSubType(new DeserializationContext(null, JavaTypeFactory.instance(), null), javaType3);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null, JavaTypeFactory.instance(), null), javaType4));
 }

 @Test
 public void testPrefixSpringClasses() throws JsonMappingException {
 JavaType javaType1 = TypeFactory.defaultInstance().constructType(String.class);
 JavaType javaType2 = TypeFactory.defaultInstance().constructType(com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator.class);
 JavaType javaType3 = TypeFactory.defaultInstance().constructType(org.springframework.beans.factory.AbstractBeanFactory.class);
 JavaType javaType4 = TypeFactory.defaultInstance().constructType(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);

 validator.validateSubType(new DeserializationContext(null, JavaTypeFactory.instance(), null), javaType1);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null, JavaTypeFactory.instance(), null), javaType2));
 // AbstractBeanFactory is not in the list, let it pass
 validator.validateSubType(new DeserializationContext(null, JavaTypeFactory.instance(), null), javaType3);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null, JavaTypeFactory.instance(), null), javaType4));
 }
}