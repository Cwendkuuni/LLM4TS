package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

public class SubTypeValidatorTest {

 private SubTypeValidator validator;
 private Set<String> defaultNoDeserClassNames;

 @Before
 public void setUp() {
 validator = SubTypeValidator.instance();
 defaultNoDeserClassNames = SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES;
 }

 @Test
 public void testValidateSubType_validType() throws Exception {
 JavaType validType = JavaTypeFactory.instance.constructType(String.class);
 validator.validateSubType(createContext(), validType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_emptyClass() throws Exception {
 JavaType emptyClass = JavaTypeFactory.instance.constructType(void.class);
 validator.validateSubType(createContext(), emptyClass);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_illegalType() throws Exception {
 JavaType illegalType = JavaTypeFactory.instance.constructType(SubTypeValidator.class);
 validator.validateSubType(createContext(), illegalType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_springPrefix() throws Exception {
 JavaType springType = JavaTypeFactory.instance.constructType(org.springframework.beans.factory.ObjectFactory.class);
 validator.validateSubType(createContext(), springType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_springPrefixWithSuperclasses() throws Exception {
 JavaType springType = JavaTypeFactory.instance.constructType(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);
 validator.validateSubType(createContext(), springType);
 }

 @Test
 public void testValidateSubType_notInList() throws Exception {
 JavaType notInList = JavaTypeFactory.instance.constructType(java.util.ArrayList.class);
 validator.validateSubType(createContext(), notInList);
 }

 private DeserializationContext createContext() {
 return new DeserializationContext(new TestAnnotationIntrospector(), null, null);
 }
}