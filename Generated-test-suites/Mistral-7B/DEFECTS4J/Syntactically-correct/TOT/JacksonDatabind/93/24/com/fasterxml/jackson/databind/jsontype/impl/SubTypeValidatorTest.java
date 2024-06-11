package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SubTypeValidatorTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testValidType() throws Exception {
 final JavaType validType = mapper.getTypeFactory().constructType(String.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mapper.getDeserializationContext(), validType);
 }

 @Test
 public void testNastyType() throws Exception {
 final JavaType nastyType = mapper.getTypeFactory().constructType(com.mchange.v2.c3p0.WrapperConnectionPoolDataSource.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 try {
 validator.validateSubType(mapper.getDeserializationContext(), nastyType);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testNastySubclass() throws Exception {
 final JavaType nastySuperclass = mapper.getTypeFactory().constructType(com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator.class);
 final JavaType nastySubclass = mapper.getTypeFactory().constructType(com.mchange.v2.c3p0.WrapperConnectionPoolDataSource.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 try {
 validator.validateSubType(mapper.getDeserializationContext(), mapper.getTypeFactory().constructType(nastySuperclass.getRawClass().getSuperclass(), nastySubclass));
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testPrefixString() throws Exception {
 final JavaType nastyType = mapper.getTypeFactory().constructType(com.springframework.beans.factory.config.PropertyPathFactoryBean.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 try {
 validator.validateSubType(mapper.getDeserializationContext(), nastyType);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }
}