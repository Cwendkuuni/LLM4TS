package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Collections;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

public class SubTypeValidatorTest {

 private static SubTypeValidator validator;

 @BeforeClass
 public static void setup() {
 validator = SubTypeValidator.instance();
 }

 @Test
 public void testValidClasses() throws Exception {
 JavaType validType = JavaTypeFactory.instance.constructType(String.class);
 validator.validateSubType(mockContext(), validType);
 }

 @Test
 public void testSpringClasses() throws Exception {
 JavaType springType1 = JavaTypeFactory.instance.constructType(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);
 JavaType springType2 = JavaTypeFactory.instance.constructType(org.springframework.beans.factory.config.AbstractApplicationContext.class);

 // Spring classes should be flagged as invalid
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), springType1));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), springType2));
 }

 @Test
 public void testNastyClasses() throws Exception {
 JavaType nastyType1 = JavaTypeFactory.instance.constructType(org.apache.commons.collections4.functors.InvokerTransformer.class);
 JavaType nastyType2 = JavaTypeFactory.instance.constructType(com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl.class);

 // Nasty classes should be flagged as invalid
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), nastyType1));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), nastyType2));
 }

 @Test
 public void testValidInterface() throws Exception {
 JavaType interfaceType = JavaTypeFactory.instance.constructType(java.io.Serializable.class);
 validator.validateSubType(mockContext(), interfaceType);
 }

 @Test
 public void testUnknownClass() throws Exception {
 JavaType unknownType = JavaTypeFactory.instance.constructType(java.lang.Object.class);
 validator.validateSubType(mockContext(), unknownType);
 }

 // Helper method to create a mock DeserializationContext
 private DeserializationContext mockContext() {
 return mock(DeserializationContext.class);
 }
}