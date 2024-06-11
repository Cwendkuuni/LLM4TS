package com.fasterxml.jackson.databind.module.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;

/**
 * Tests for {@link SimpleAbstractTypeResolver} class
 */
public class SimpleAbstractTypeResolverTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new SimpleAbstractTypeResolver().addMapping(Collection.class, ArrayList.class)
                .addMapping(List.class, ArrayList.class));
    }

    /**
     * Test for {@link SimpleAbstractTypeResolver#addMapping(Class, Class)} method with valid arguments
     */
    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, ArrayList.class);
        resolver.addMapping(List.class, ArrayList.class);

        JavaType type = mapper.getTypeFactory().constructType(List.class);
        JavaType expectedType = mapper.getTypeFactory().constructType(ArrayList.class);
        JavaType actualType = resolver.findTypeMapping(new DeserializationConfig.Builder().build(), type);

        assertEquals(expectedType, actualType);
    }

    /**
     * Test for {@link SimpleAbstractTypeResolver#addMapping(Class, Class)} method with invalid arguments (different types)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingInvalidArgs() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(String.class, Integer.class);
    }

    /**
     * Test for {@link SimpleAbstractTypeResolver#addMapping(Class, Class)} method with invalid arguments (same types)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingSameTypes() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(String.class, String.class);
    }

    /**
     * Test for {@link SimpleAbstractTypeResolver#addMapping(Class, Class)} method with invalid argument (non-abstract super class)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingNonAbstractSuperClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(String.class, StringBuffer.class);
    }

    /**
     * Test for {@link SimpleAbstractTypeResolver#findTypeMapping(DeserializationConfig, JavaType)} method with null arguments
     */
    @Test(expected = NullPointerException.class)
    public void testFindTypeMappingNullArguments() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, ArrayList.class);
        resolver.addMapping(List.class, ArrayList.class);

        JavaType type = null;
        JavaType actualType = resolver.findTypeMapping(new DeserializationConfig.Builder().build(), type);
    }

    /**
     * Test for {@link SimpleAbstractTypeResolver#findTypeMapping(DeserializationConfig, JavaType)} method with valid arguments
     */
    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, ArrayList.class);
        resolver.addMapping(List.class, ArrayList.class);

        JavaType type = mapper.getTypeFactory().constructType(List.class);
        JavaType expectedType = mapper.getTypeFactory().constructType(ArrayList.class);
        JavaType actualType = resolver.findTypeMapping(new DeserializationConfig.Builder().build(), type);

        assertEquals(expectedType, actualType);
    }

    /**
     * Test for {@link SimpleAbstractTypeResolver#findTypeMapping(DeserializationConfig, JavaType)} method with null mapping
     */
    @Test
    public void testFindTypeMappingNullMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        JavaType actualType = resolver.findTypeMapping(new DeserializationConfig.Builder().build(), mapper.getTypeFactory().constructType(List.class));
        assertEquals(null, actualType);
    }

    /**
     * Test for {@link SimpleAbstractTypeResolver#resolveAbstractType(DeserializationConfig, JavaType)} method with valid arguments
     */
    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        JavaType actualType = resolver.resolveAbstractType(new DeserializationConfig.Builder().build(), mapper.getTypeFactory().constructType(List.class));
        assertEquals(null, actualType);
    }
}