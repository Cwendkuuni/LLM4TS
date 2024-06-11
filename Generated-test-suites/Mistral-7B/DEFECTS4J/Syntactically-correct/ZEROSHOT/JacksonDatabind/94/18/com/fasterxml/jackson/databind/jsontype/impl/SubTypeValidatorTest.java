package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Collections;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

public class SubTypeValidatorTest {

 private static ObjectMapper mapper;
 private static SubTypeValidator validator;

 @BeforeClass
 public static void setup() {
 mapper = new ObjectMapper();
 validator = SubTypeValidator.instance();
 }

 @Test
 public void testValidTypes() throws Exception {
 JavaType validType = mapper.getTypeFactory().constructType(String.class);
 validator.validateSubType(mapper.getDeserializationContext(), validType);
 }

 @Test
 public void testNastyClassNames() throws Exception {
 JavaType nastyType1 = mapper.getTypeFactory().constructType(Object.class.forName("org.apache.commons.collections.functors.InvokerTransformer"));
 JavaType nastyType2 = mapper.getTypeFactory().constructType(Object.class.forName("org.springframework.beans.factory.ObjectFactory"));
 JavaType nastyType3 = mapper.getTypeFactory().constructType(Object.class.forName("org.apache.tomcat.dbcp.dbcp2.BasicDataSource"));

 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), nastyType1));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), nastyType2));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), nastyType3));
 }

 @Test
 public void testSpringClasses() throws Exception {
 JavaType springType = mapper.getTypeFactory().constructType(Object.class.forName("org.springframework.beans.factory.config.PropertyPathFactoryBean"));

 // This class is an interface and should not trigger the validation
 JavaType springInterfaceType = mapper.getTypeFactory().constructType(Object.class.forName("org.springframework.beans.factory.config.AbstractApplicationContext"));

 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), springType));
 assertEquals(JavaType.TypeFlags.INTERFACE, springInterfaceType.getFlags());
 }

 @Test
 public void testValidSpringClasses() throws Exception {
 JavaType validSpringType = mapper.getTypeFactory().constructType(Object.class.forName("java.lang.String"));

 validator.validateSubType(mapper.getDeserializationContext(), validSpringType);
 }

}