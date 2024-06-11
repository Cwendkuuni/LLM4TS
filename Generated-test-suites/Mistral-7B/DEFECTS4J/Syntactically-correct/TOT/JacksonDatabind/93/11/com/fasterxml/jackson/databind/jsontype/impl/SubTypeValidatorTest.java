package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SubTypeValidatorTest {

 private static ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testValidTypes() throws JsonMappingException {
 String json = "{\"class\": \"java.lang.String\"}";
 JavaType type = mapper.getTypeFactory().constructType(String.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 DeserializationContext ctxt = mapper.getDeserializationContext();
 validator.validateSubType(ctxt, type);
 }

 @Test
 public void testNastyClass() throws JsonMappingException {
 String json = "{\"class\": \"org.apache.commons.collections.functors.InvokerTransformer\"}";
 JavaType type = mapper.getTypeFactory().constructType(Object.class);
 type = mapper.getTypeFactory().constructType(type.getId(), ClassIdType.ClassIdTypeId.ID_CLASS);
 SubTypeValidator validator = SubTypeValidator.instance();
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, type));
 }

 @Test
 public void testSpringClassWithSubclass() throws JsonMappingException {
 String json = "{\"class\": \"com.mycompany.MyApplicationContext\"}";
 JavaType type = mapper.getTypeFactory().constructType(MyApplicationContext.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 DeserializationContext ctxt = mapper.getDeserializationContext();
 validator.validateSubType(ctxt, type);

 // Test with a subclass of MyApplicationContext
 JavaType subType = mapper.getTypeFactory().constructType(SubClassOfMyApplicationContext.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, subType));
 }

 @Test
 public void testPrefixString() throws JsonMappingException {
 String json = "{\"class\": \"com.mycompany.MyBeanFactoryPointcutAdvisor\"}";
 JavaType type = mapper.getTypeFactory().constructType(Object.class);
 type = mapper.getTypeFactory().constructType(type.getId(), ClassIdType.ClassIdTypeId.ID_CLASS);
 SubTypeValidator validator = SubTypeValidator.instance();
 DeserializationContext ctxt = mapper.getDeserializationContext();
 // This should not fail, since "MyBeanFactoryPointcutAdvisor" is not in the list of "nasty classes"
 validator.validateSubType(ctxt, type);
 }

 @Test
 public void testNastyClassesWithSuperclass() throws JsonMappingException {
 // Test a class that is a superclass of a nasty class, but not itself a nasty class
 String json = "{\"class\": \"java.rmi.server.UnicastRemoteObject\"}";
 JavaType type = mapper.getTypeFactory().constructType(Object.class);
 type = mapper.getTypeFactory().constructType(type.getId(), ClassIdType.ClassIdTypeId.ID_CLASS);
 SubTypeValidator validator = SubTypeValidator.instance();
 DeserializationContext ctxt = mapper.getDeserializationContext();
 // This should not fail
 validator.validateSubType(ctxt, type);

 // Test the nasty class itself
 JavaType nastyType = mapper.getTypeFactory().constructType("java.rmi.server.UnicastRemoteObject");
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, nastyType));
 }

 @Test
 public void testListOfNastyClasses() throws JsonMappingException {
 // Test deserializing a List of nasty classes
 List<JavaType> types = new ArrayList<>();
 String json = "{\"classes\": [{\"class\": \"org.apache.commons.collections.functors.InvokerTransformer\"}, {\"class\": \"com.mchange.v2.c3p0.WrapperConnectionPoolDataSource\"}]}";
 JavaType listType = mapper.getTypeFactory().constructType(List.class, JavaType.constructRawType(Object.class));
 JavaType mapType = mapper.getTypeFactory().constructType(Map.class, String.class, JavaType.constructRawType(Object.class));
 JavaType rootType = mapper.getTypeFactory().constructType(Map.class, String.class, listType);
 Object readValue = mapper.readValue(json, rootType);
 List<JavaType> nestedList = (List<JavaType>) ((Map) readValue).get("classes");
 for (JavaType type : nestedList) {
 SubTypeValidator validator = SubTypeValidator.instance();
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, type));
 }
 }
}