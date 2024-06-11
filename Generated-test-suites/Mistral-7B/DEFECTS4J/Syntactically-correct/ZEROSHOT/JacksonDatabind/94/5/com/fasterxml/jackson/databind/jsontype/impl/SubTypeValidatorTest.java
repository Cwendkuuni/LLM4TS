package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test class for SubTypeValidator
 *
 * @author John Doe
 */
public class SubTypeValidatorTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testValidTypes() throws JsonProcessingException {
 Set<String> illegalClassNames = new HashSet<String>();
 SubTypeValidator validator = new SubTypeValidator();
 JavaType validType = mapper.getTypeFactory().constructType(String.class);
 JavaType validSubType = mapper.getTypeFactory().constructType(Integer.class);

 validator.validateSubType(setupContext(), validType);
 validator.validateSubType(setupContext(), validSubType);
 }

 @Test
 public void testIllegalTypes() throws JsonProcessingException {
 Set<String> illegalClassNames = new HashSet<String>(SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES);
 SubTypeValidator validator = SubTypeValidator.instance();

 // Test with a valid class name that is not in the black list
 JavaType validType = mapper.getTypeFactory().constructType(String.class);
 validator.validateSubType(setupContext(), validType);

 // Test with a class name in the black list
 JavaType invalidType1 = mapper.getTypeFactory().constructType("org.apache.commons.collections.functors.InvokerTransformer");
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(setupContext(), invalidType1));

 // Test with a class name that starts with Spring prefix
 JavaType invalidType2 = mapper.getTypeFactory().constructType("org.springframework.beans.factory.config.PropertyPathFactoryBean");
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(setupContext(), invalidType2));

 // Test with a class name that is an interface
 JavaType invalidType3 = mapper.getTypeFactory().constructType(java.lang.reflect.InvokerSynchronizer.class);
 validator.validateSubType(setupContext(), invalidType3);
 }

 @Test
 public void testSpringClasses() throws JsonProcessingException {
 Set<String> illegalClassNames = new HashSet<String>(SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES);
 SubTypeValidator validator = SubTypeValidator.instance();

 // Test with a class name that extends AbstractApplicationContext but is not a Spring bean factory
 JavaType invalidType1 = mapper.getTypeFactory().constructType("com.mycompany.MyApplicationContext");
 validator.validateSubType(setupContext(), invalidType1);

 // Test with a class name that extends AbstractPointcutAdvisor but is not a Spring pointcut advisor
 JavaType invalidType2 = mapper.getTypeFactory().constructType("com.mycompany.MyPointcutAdvisor");
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(setupContext(), invalidType2));
 }

 private DeserializationContext setupContext() {
 return new DeserializationContext(mapper.getConfig(), null);
 }
}