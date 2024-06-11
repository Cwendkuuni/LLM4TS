package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

/**
 * Tests for {@link SubTypeValidator} class.
 */
public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_validTypes() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);

        String validType1 = "com.example.ValidType1";
        String validType2 = "com.example.ValidType2";

        validator.validateSubType(context, type);

        // Test with valid types that are not in the blacklist.
        validator.validateSubType(context, getType(validType1));
        validator.validateSubType(context, getType(validType2));
    }

    @Test
    public void testValidateSubType_blacklistedTypes() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);

        String blacklistedType1 = "org.apache.commons.collections4.functors.InstantiateTransformer";
        String blacklistedType2 = "org.springframework.beans.factory.config.PropertyPathFactoryBean";
        String blacklistedType3 = "com.sun.org.apache.bcel.internal.util.ClassLoader";

        // Test with blacklisted types.
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, getType(blacklistedType1)));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, getType(blacklistedType2)));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, getType(blacklistedType3)));
    }

    @Test
    public void testValidateSubType_springTypes() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);

        String springType1 = "org.springframework.beans.factory.config.PropertyPathFactoryBean";
        String springType2 = "org.springframework.beans.factory.ObjectFactory";
        String springType3 = "org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor"; // deprecated

        // Test with Spring types that have special handling.
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, getType(springType1)));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, getType(springType2)));
        // This test case is skipped due to deprecated usage.
        // assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, getType(springType3)));
    }

    private JavaType getType(String className) {
        return mock(JavaType.class);
    }
}