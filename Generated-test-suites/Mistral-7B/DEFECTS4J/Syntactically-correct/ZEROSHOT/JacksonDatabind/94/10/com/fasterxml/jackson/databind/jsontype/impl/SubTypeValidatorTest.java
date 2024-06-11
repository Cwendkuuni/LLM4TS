package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.SubTypeValidator;

/**
 * Test file for SubTypeValidator class
 *
 * @author John Doe
 */
public class SubTypeValidatorTest {

    @Test
    public void testValidTypes() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        JavaType validType = new JavaType(String.class);
        validator.validateSubType(new DeserializationContext(null), validType);
    }

    @Test
    public void testSpringTypes() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        JavaType springType = new JavaType(Object.class, "org.springframework.SomeSpringClass");
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), springType));
    }

    @Test
    public void testNastyClassTypes() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();

        Set<String> nastyClassNames = new HashSet<String>();
        nastyClassNames.add("org.apache.commons.collections4.functors.InvokerTransformer");
        validator._cfgIllegalClassNames = nastyClassNames;

        JavaType nastyClassType = new JavaType(Object.class, "org.apache.commons.collections4.functors.InvokerTransformer");
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), nastyClassType));
    }
}