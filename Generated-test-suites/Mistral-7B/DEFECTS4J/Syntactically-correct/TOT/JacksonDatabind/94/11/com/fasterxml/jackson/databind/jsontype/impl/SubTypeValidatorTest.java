package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.assertThrows;

public class SubTypeValidatorTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testValidateSubType_ValidClass() throws Exception {
 JavaType validType = mapper.getTypeFactory().constructType(String.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mapper.getDeserializationContext(), validType);
 }

 @Test
 public void testValidateSubType_NastyClass() throws Exception {
 JavaType nastyType = mapper.getTypeFactory().constructType(InvokerTransformer.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> {
 validator.validateSubType(mapper.getDeserializationContext(), nastyType);
 });
 }

 @Test
 public void testValidateSubType_SpringClass() throws Exception {
 JavaType springType = mapper.getTypeFactory().constructType(AbstractApplicationContext.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> {
 validator.validateSubType(mapper.getDeserializationContext(), springType);
 });
 }

 @Test
 public void testValidateSubType_Interface() throws Exception {
 JavaType interfaceType = mapper.getTypeFactory().constructType(Comparable.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mapper.getDeserializationContext(), interfaceType);
 }
}