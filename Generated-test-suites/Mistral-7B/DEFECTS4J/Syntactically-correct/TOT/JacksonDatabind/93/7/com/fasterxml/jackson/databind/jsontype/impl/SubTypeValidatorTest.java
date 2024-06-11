package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.assertThrows;

public class SubTypeValidatorTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testValidType() throws Exception {
 JavaType validType = objectMapper.getTypeFactory().constructType(String.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(objectMapper.getDeserializationContext(), validType);
 }

 @Test
 public void testNastyTypeWithPrefix() throws Exception {
 JavaType nastyType = objectMapper.getTypeFactory().constructType(AbstractApplicationContext.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), nastyType));
 }

 @Test
 public void testNastyTypeWithoutPrefix() throws Exception {
 JavaType nastyType = objectMapper.getTypeFactory().constructType(AbstractPointcutAdvisor.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), nastyType));
 }

 @Test
 public void testNastyTypeWithCustomPrefix() throws Exception {
 JavaType nastyType = objectMapper.getTypeFactory().constructType(new TypeReference<MyNastyClass>() {}.getType());
 SubTypeValidator validator = new SubTypeValidator();
 validator._cfgIllegalClassNames = Collections.singleton("com.mycompany.MyNastyClass");
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), nastyType));
 }
}