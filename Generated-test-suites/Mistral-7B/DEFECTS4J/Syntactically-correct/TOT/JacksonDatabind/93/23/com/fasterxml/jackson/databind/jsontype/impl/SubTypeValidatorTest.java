package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

public class SubTypeValidatorTest {

    @Test
    public void testValidType() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType validType = mapper.getTypeFactory().constructType(String.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(mapper.getDeserializationContext(), validType);
    }

    @Test
    public void testNastyTypeWithPrefix() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType nastyType = mapper.getTypeFactory().constructType(AbstractApplicationContext.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), nastyType));
    }

    @Test
    public void testNastyTypeWithoutPrefix() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType nastyType = mapper.getTypeFactory().constructType(AbstractPointcutAdvisor.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), nastyType));
    }

    @Test
    public void testValidTypeWithPrefix() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType validTypeWithPrefix = mapper.getTypeFactory().constructType(AbstractApplicationContext.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        // This should pass since AbstractApplicationContext is not in the list of 'nasty classes' and starts with the prefix
        validator.validateSubType(mapper.getDeserializationContext(), validTypeWithPrefix);
    }

    @Test
    public void testNullType() {
        SubTypeValidator validator = SubTypeValidator.instance();
        assertThrows(IllegalArgumentException.class, () -> validator.validateSubType(null, null));
    }

    @Test
    public void testNullJavaType() {
        SubTypeValidator validator = SubTypeValidator.instance();
        assertThrows(IllegalArgumentException.class, () -> validator.validateSubType(null, new ClassType(int.class)));
    }
}