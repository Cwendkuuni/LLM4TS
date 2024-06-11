package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Test cases for {@link SubTypeValidator} class.
 */
public class SubTypeValidatorTest {

 private static ObjectMapper objectMapper;

 static {
 objectMapper = new JsonMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testValidateSubType() throws Exception {
 // Test case for valid classes
 String validClass1 = "com.example.ValidType1";
 String validClass2 = "com.example.ValidType2";
 JavaType validType1 = TypeFactory.defaultInstance().constructType(Class.forName(validClass1));
 JavaType validType2 = TypeFactory.defaultInstance().constructType(Class.forName(validClass2));
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(ctxt, validType1);
 validator.validateSubType(ctxt, validType2);

 // Test case for classes in the list of "nasty classes"
 String nastyClass1 = "org.apache.commons.collections.functors.InvokerTransformer";
 JavaType nastyType1 = TypeFactory.defaultInstance().constructType(Class.forName(nastyClass1));
 try {
 validator.validateSubType(ctxt, nastyType1);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }

 // Test case for classes starting with "org.springframework."
 String springClass1 = "org.springframework.beans.factory.ObjectFactory";
 JavaType springType1 = TypeFactory.defaultInstance().constructType(Class.forName(springClass1));
 try {
 validator.validateSubType(ctxt, springType1);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testValidateSubTypeWithCustomInstance() throws Exception {
 // Test case for custom instance with custom illegal class names
 Set<String> customIllegalClassNames = new HashSet<>();
 customIllegalClassNames.add("com.example.CustomIllegalType");
 SubTypeValidator customValidator = new SubTypeValidator() {
 protected Set<String> _cfgIllegalClassNames = customIllegalClassNames;
 };
 JavaType customType = TypeFactory.defaultInstance().constructType(Class.forName("com.example.CustomType"));
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 try {
 customValidator.validateSubType(ctxt, customType);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }
}