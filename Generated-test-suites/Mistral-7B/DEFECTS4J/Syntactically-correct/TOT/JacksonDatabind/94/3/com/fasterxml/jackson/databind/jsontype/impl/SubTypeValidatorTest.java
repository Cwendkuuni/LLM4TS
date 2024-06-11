package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertThrows;

import static org.hamcrest.CoreMatchers.is;

public class SubTypeValidatorTest {

 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testValidType() throws JsonMappingException {
 String json = "{\"type\":\"com.example.ValidType\"}";
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<JavaType>() {}.getType());
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mapper.getDeserializationContext(), javaType);
 }

 @Test
 public void testInvalidTypeWithPrefixSpring() throws JsonMappingException {
 String json = "{\"type\":\"org.springframework.InvalidType\"}";
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<JavaType>() {}.getType());
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), javaType));
 }

 @Test
 public void testInvalidTypeWithSpringSuperclass() throws JsonMappingException {
 String json = "{\"type\":\"com.example.ParentType\"}";
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<JavaType>() {}.getType());
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), javaType));
 }

 @Test
 public void testValidTypeWithPrefixSpring() throws JsonMappingException {
 String json = "{\"type\":\"org.springframework.ValidTypeWithPrefixSpring\"}";
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<JavaType>() {}.getType());
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mapper.getDeserializationContext(), javaType);
 }

 @Test
 public void testValidTypeWithEmptySuperclass() throws JsonMappingException {
 Class<?> emptyClass = new Class<Object>() {};
 String json = "{\"type\":\"" + emptyClass.getName() + "\"}";
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<JavaType>() {}.getType());
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mapper.getDeserializationContext(), javaType);
 }
}