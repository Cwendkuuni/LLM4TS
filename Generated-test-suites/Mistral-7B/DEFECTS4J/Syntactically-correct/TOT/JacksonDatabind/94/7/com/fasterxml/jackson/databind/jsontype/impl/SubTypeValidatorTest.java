package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

public class SubTypeValidatorTest
{
    private static final ObjectMapper MAPPER = new JsonMapper();
    private static final DeserializationContext CTX = MAPPER.getDeserializationContext();

    @Rule
    public final JUnitRuleMockery context = new JUnitRuleMockery();

    private SubTypeValidator validator;

    @Before
    public void setUp()
    {
        validator = SubTypeValidator.instance();
    }

    @Test
    public void testValidClass()
    {
        JavaType validClassType = MAPPER.getTypeFactory().constructType(String.class);
        validator.validateSubType(CTX, validClassType);
    }

    @Test
    public void testInvalidClass()
    {
        JavaType invalidClassType = MAPPER.getTypeFactory().constructType(SubTypeValidator.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(CTX, invalidClassType));
    }

    @Test
    public void testSpringClass()
    {
        JavaType springClassType = MAPPER.getTypeFactory().constructType(String[].class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(CTX, springClassType));
    }

    @Test
    public void testSpringInterface()
    {
        JavaType springInterfaceType = MAPPER.getTypeFactory().constructType(Comparable.class);
        validator.validateSubType(CTX, springInterfaceType);
    }

    @Test
    public void testSpringClassWithParent()
    {
        JavaType springClassTypeWithParent = MAPPER.getTypeFactory().constructType(MySpringClass.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(CTX, springClassTypeWithParent));
    }

    static class MySpringClass extends AbstractPointcutAdvisor { /* empty */ }

    @Test
    public void testCustomInvalidClass()
    {
        JavaType customInvalidClassType = MAPPER.getTypeFactory()
                .constructType(new TypeReference<Class<?>>() {});

        validator.setCfgIllegalClassNames(new HashSet<String>(Arrays.asList("java.lang.Class")));

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(CTX, customInvalidClassType));
    }

    @Test
    public void testCustomValidClass()
    {
        JavaType customValidClassType = MAPPER.getTypeFactory().constructType(String.class);
        validator.validateSubType(CTX, customValidClassType);
    }

    @Test
    public void testSerializationFeatureOff() throws Exception
    {
        MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        JavaType invalidClassType = MAPPER.getTypeFactory().constructType(SubTypeValidator.class);
        validator = new SubTypeValidator();
        String json = MAPPER.writeValueAsString(validator);
        JavaType jsonType = MAPPER.getTypeFactory().constructType(SubTypeValidator.class);
        validator = MAPPER.readValue(json, jsonType);
        validator.validateSubType(CTX, invalidClassType);
    }
}