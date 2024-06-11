package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

 private static final SubTypeValidator validator = SubTypeValidator.instance();

 @Test
 public void testValidTypes() throws JsonMappingException {
 JavaType validType = TypeFactory.defaultInstance().constructType(String.class);
 validator.validateSubType(createContext(), validType);
 }

 @Test
 public void testPrefixStringInterface() throws JsonMappingException {
 JavaType invalidType = TypeFactory.defaultInstance().constructType(AbstractApplicationContext.class);
 validator.validateSubType(createContext(), invalidType);
 }

 @Test
 public void testPrefixStringClass() throws JsonMappingException {
 JavaType invalidType = TypeFactory.defaultInstance().constructType(AbstractPointcutAdvisor.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), invalidType));
 }

 @Test
 public void testIllegalType() throws JsonMappingException {
 JavaType illegalType = TypeFactory.defaultInstance().constructType(Object.class, String.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), illegalType));
 }

 @Test
 public void testIllegalClassWithPrefixString() throws JsonMappingException {
 JavaType illegalType = TypeFactory.defaultInstance().constructType(PrefixStringNastyClass.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), illegalType));
 }

 @Test
 public void testDefaultNoDeserClassName() throws JsonMappingException {
 JavaType illegalType = TypeFactory.defaultInstance().constructType(NastyClass.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), illegalType));
 }

 private DeserializationContext createContext() {
 return new DeserializationContext(null, new EventTypes());
 }
}

class AbstractApplicationContext {}

class AbstractPointcutAdvisor {}

class PrefixStringNastyClass extends Object {}

class NastyClass extends Object {}