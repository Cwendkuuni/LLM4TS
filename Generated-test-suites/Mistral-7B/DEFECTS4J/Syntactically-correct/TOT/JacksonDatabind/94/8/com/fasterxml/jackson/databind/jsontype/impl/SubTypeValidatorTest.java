package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.io.IOError;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolverBase;

/**
 * Test class for {@link SubTypeValidator}.
 */
public class SubTypeValidatorTest {

    private static final SubTypeValidator validator = SubTypeValidator.instance();

    /**
     * Test valid subtype.
     */
    @Test
    public void testValidSubType() throws Exception {
        JavaType validType = TypeIdResolverBase.constructType(getClass(), ClassIdType.BASE_TYPE);
        validator.validateSubType(createContext(), validType);
    }

    /**
     * Test invalid subtype - a known "nasty class".
     */
    @Test
    public void testInvalidSubType() throws Exception {
        JavaType invalidType = TypeIdResolverBase.constructType(InvokerTransformer.class, ClassIdType.BASE_TYPE);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), invalidType));
    }

    /**
     * Test invalid subtype - a Spring framework type.
     */
    @Test
    public void testInvalidSubTypeSpring() throws Exception {
        JavaType invalidType = TypeIdResolverBase.constructType(AbstractApplicationContext.class, ClassIdType.BASE_TYPE);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), invalidType));
    }

    /**
     * Test invalid subtype - a custom class.
     */
    @Test
    public void testInvalidSubTypeCustom() throws Exception {
        JavaType invalidType = TypeIdResolverBase.constructType(CustomNastyClass.class, ClassIdType.BASE_TYPE);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), invalidType));
    }

    /**
     * Test valid subtype with a superclass that is a known "nasty class".
     */
    @Test
    public void testValidSubTypeSuperClass() throws Exception {
        JavaType validType = TypeIdResolverBase.constructType(SuperNastyClass.class, ClassIdType.BASE_TYPE);
        validator.validateSubType(createContext(), validType);
    }

    /**
     * Create a mock context for testing.
     */
    private DeserializationContext createContext() {
        return new DeserializationContext(new TestDeserializationConfig(), new TestAnnotationIntrospector()) {
            @Override
            public JavaType findTypeById(ClassIdType id) {
                return null;
            }
        };
    }

    /**
     * Mock deserialization configuration.
     */
    public static class TestDeserializationConfig extends DeserializationConfig {
        // Empty.
    }

    /**
     * Mock annotation introspector.
     */
    public static class TestAnnotationIntrospector extends AnnotationIntrospector {
        // Empty.
    }

    /**
     * Test class that should not be deserialized.
     */
    private static class CustomNastyClass {
        // Empty.
    }

    /**
     * Test class with a known "nasty class" as a superclass.
     */
    private static class SuperNastyClass extends InvokerTransformer {
        // Empty.
    }
}