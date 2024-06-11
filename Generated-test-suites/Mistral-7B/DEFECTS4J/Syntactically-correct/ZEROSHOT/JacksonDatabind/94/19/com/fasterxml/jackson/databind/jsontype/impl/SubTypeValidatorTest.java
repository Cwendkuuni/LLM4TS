package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.HashSet;

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
 public static void setup() {
 validator = SubTypeValidator.instance();
 }

 @Test
 public void testValidTypes() throws JsonMappingException {
 JavaType type = TypeFactory.defaultInstance().constructType(String.class);
 validator.validateSubType(new DeserializationContext(null), type);
 }

 @Test
 public void testNastyClassNames() throws JsonMappingException {
 JavaType type1 = TypeFactory.defaultInstance().constructType(Object[].class);
 JavaType type2 = TypeFactory.defaultInstance().constructType(com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator.class);
 JavaType type3 = TypeFactory.defaultInstance().constructType(java.util.Set.class);
 JavaType type4 = TypeFactory.defaultInstance().constructType(HashSet.class);
 JavaType type5 = TypeFactory.defaultInstance().constructType(java.lang.Runtime.class);
 JavaType type6 = TypeFactory.defaultInstance().constructType(java.util.logging.FileHandler.class);
 JavaType type7 = TypeFactory.defaultInstance().constructType(java.rmi.server.UnicastRemoteObject.class);
 JavaType type8 = TypeFactory.defaultInstance().constructType(com.apache.commons.collections4.functors.InstantiateTransformer.class);
 JavaType type9 = TypeFactory.defaultInstance().constructType(org.apache.commons.collections4.functors.InvokerTransformer.class);
 JavaType type10 = TypeFactory.defaultInstance().constructType(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);
 JavaType type11 = TypeFactory.defaultInstance().constructType(org.apache.tomcat.dbcp.dbcp2.BasicDataSource.class);
 JavaType type12 = TypeFactory.defaultInstance().constructType(com.mchange.v2.c3p0.ComboPooledDataSource.class);
 JavaType type13 = TypeFactory.defaultInstance().constructType(com.mchange.v2.c3p0.debug.AfterCloseLoggingComboPooledDataSource.class);

 validator.validateSubType(new DeserializationContext(null), type1);
 validator.validateSubType(new DeserializationContext(null), type2);
 validator.validateSubType(new DeserializationContext(null), type3);
 validator.validateSubType(new DeserializationContext(null), type4);
 validator.validateSubType(new DeserializationContext(null), type5);
 validator.validateSubType(new DeserializationContext(null), type6);
 validator.validateSubType(new DeserializationContext(null), type7);
 validator.validateSubType(new DeserializationContext(null), type8);
 validator.validateSubType(new DeserializationContext(null), type9);
 validator.validateSubType(new DeserializationContext(null), type10);
 validator.validateSubType(new DeserializationContext(null), type11);
 validator.validateSubType(new DeserializationContext(null), type12);
 validator.validateSubType(new DeserializationContext(null), type13);
 }

 @Test(expected = JsonMappingException.class)
 public void testNastyClassNames_Exception() throws JsonMappingException {
 JavaType type = JavaTypeFactory.instance.constructType(org.springframework.beans.factory.ObjectFactory.class);
 validator.validateSubType(new DeserializationContext(null), type);
 }
}