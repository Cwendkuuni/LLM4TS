package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Test class for SubTypeValidator
 *
 * @author John Doe
 */
public class SubTypeValidatorTest {

 private ObjectMapper objectMapper;
 private SubTypeValidator subTypeValidator;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 subTypeValidator = SubTypeValidator.instance();
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)} with a valid class
 */
 @Test
 public void testValidClass() throws IOException, JsonMappingException {
 JavaType javaType = TypeFactory.defaultInstance()
 .constructType(String.class, Integer.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 subTypeValidator.validateSubType(ctxt, javaType);
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)} with a forbidden class
 */
 @Test(expected = JsonMappingException.class)
 public void testForbiddenClass() throws IOException, JsonMappingException {
 JavaType javaType = TypeFactory.defaultInstance()
 .constructType(SubTypeValidator.class, Integer.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 subTypeValidator.validateSubType(ctxt, javaType);
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)} with a Spring class
 */
 @Test
 public void testSpringClass() throws IOException, JsonMappingException {
 JavaType javaType = TypeFactory.defaultInstance()
 .constructType(org.springframework.beans.factory.config.PropertySourcesPlaceholderConfigurer.class, Integer.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 subTypeValidator.validateSubType(ctxt, javaType);
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)} with a class starting with "org.apache.commons."
 */
 @Test
 public void testCommonsClass() throws IOException, JsonMappingException {
 JavaType javaType = TypeFactory.defaultInstance()
 .constructType(org.apache.commons.collections4.map.LinkedHashMap.class, Integer.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 subTypeValidator.validateSubType(ctxt, javaType);
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)} with a class starting with "com.sun.org.apache.bcel."
 */
 @Test
 public void testBcelClass() throws IOException, JsonMappingException {
 JavaType javaType = TypeFactory.defaultInstance()
 .constructType(com.sun.org.apache.bcel.classfile.ClassFormat.class, Integer.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 try {
 subTypeValidator.validateSubType(ctxt, javaType);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)} with a null JavaType
 */
 @Test(expected = IllegalArgumentException.class)
 public void testNullJavaType() {
 subTypeValidator.validateSubType(objectMapper.getDeserializationContext(), null);
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)} with a null DeserializationContext
 */
 @Test(expected = IllegalArgumentException.class)
 public void testNullDeserializationContext() {
 subTypeValidator.validateSubType(null, TypeFactory.defaultInstance().constructType(String.class, Integer.class));
 }
}