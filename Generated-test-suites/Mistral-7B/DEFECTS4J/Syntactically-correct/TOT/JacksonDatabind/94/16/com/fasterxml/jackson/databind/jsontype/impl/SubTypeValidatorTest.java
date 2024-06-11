package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SubTypeValidatorTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testValidType() throws JsonMappingException, IOException {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType validType = MAPPER.getTypeFactory().constructType(String.class);
 validator.validateSubType(MAPPER.getContext(), validType);
 }

 @Test
 public void testNastyType() throws JsonMappingException, IOException {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType nastyType = MAPPER.getTypeFactory().constructType(Object.class, "org.apache.commons.collections4.functors.InstantiateTransformer");
 try {
 validator.validateSubType(MAPPER.getContext(), nastyType);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testNastyInterface() throws JsonMappingException, IOException {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType nastyInterface = MAPPER.getTypeFactory().constructType(Object.class, "org.apache.commons.collections.functors.AbstractPointcutAdvisor");
 try {
 validator.validateSubType(MAPPER.getContext(), nastyInterface);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testSpringClassWithSubClassThatIsBlocked() throws JsonMappingException, IOException {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType springClass = MAPPER.getTypeFactory().constructType(Object.class, "org.springframework.beans.factory.config.PropertyPathFactoryBean");
 JavaType blockedClass = MAPPER.getTypeFactory().constructType(Object.class, "org.apache.tomcat.dbcp.dbcp2.BasicDataSource");
 JavaType springClassWithBlockedSubClass = MAPPER.getTypeFactory().constructType(Object.class, springClass.getRawClass().getPackage().getName() + ".PropertyPathFactoryBeanWithBasicDataSource");
 try {
 validator.validateSubType(MAPPER.getContext(), springClassWithBlockedSubClass);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testValidSpringClass() throws JsonMappingException, IOException {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType validSpringClass = MAPPER.getTypeFactory().constructType(Object.class, "org.springframework.beans.factory.config.PropertyPlaceholderConfigurer");
 validator.validateSubType(MAPper.getContext(), validSpringClass);
 }
}