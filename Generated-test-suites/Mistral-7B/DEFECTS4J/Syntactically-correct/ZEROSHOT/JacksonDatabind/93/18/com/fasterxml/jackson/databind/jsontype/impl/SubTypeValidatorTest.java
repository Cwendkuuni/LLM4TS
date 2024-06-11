package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.util.Collections;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SubTypeValidatorTest {

 private static ObjectMapper mapper;
 private static SubTypeValidator validator;

 @BeforeClass
 public static void setup() {
 mapper = new ObjectMapper();
 validator = SubTypeValidator.instance();
 }

 @Test
 public void testValidClass() throws Exception {
 JavaType validClass = mapper.getTypeFactory().constructType(String.class);
 validator.validateSubType(mapper.getDeserializationContext(), validClass);
 }

 @Test
 public void testNastyClass() throws Exception {
 String nastyClassName = "org.apache.commons.collections4.functors.InstantiateTransformer";
 JavaType nastyClassType = mapper.getTypeFactory().constructType(Class.forName(nastyClassName));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), nastyClassType));
 }

 @Test
 public void testSpringClassWithSubClass() throws Exception {
 String springBaseClassName = "org.springframework.beans.factory.config.PropertyPathFactoryBean";
 String springSubClassName = "com.mchange.v2.c3p0.WrapperConnectionPoolDataSource";
 JavaType springBaseType = mapper.getTypeFactory().constructType(Class.forName(springBaseClassName));
 JavaType springSubType = mapper.getTypeFactory().constructType(Class.forName(springSubClassName));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), springSubType.getInstanceType()));
 // Make sure the base class is valid
 validator.validateSubType(mapper.getDeserializationContext(), springBaseType);
 }

 @Test
 public void testSpringClassWithInterface() throws Exception {
 String springInterfaceName = "org.springframework.beans.factory.config.BeanDefinitionValidator";
 JavaType springInterfaceType = mapper.getTypeFactory().constructType(Class.forName(springInterfaceName.replaceAll("^.*interface", "")));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), springInterfaceType));
 }

 @Test
 public void testPrefixString() throws Exception {
 String springClassWithPrefix = "org.springframework.beans.factory.ObjectFactory";
 JavaType springClassType = mapper.getTypeFactory().constructType(Class.forName(springClassWithPrefix));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), springClassType));
 }

 @Test
 public void testIllegalClassWithPrefix() throws Exception {
 String illegalClassWithPrefix = "com.fasterxml.illegal.ObjectFactory";
 JavaType illegalClassType = mapper.getTypeFactory().constructType(Class.forName(illegalClassWithPrefix));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), illegalClassType));
 }

 @Test
 public void testEmptyString() throws Exception {
 JavaType emptyStringType = mapper.getTypeFactory().constructType(String.class);
 validator.validateSubType(mapper.getDeserializationContext(), JavaType.ValueType.STRING);
 }
}