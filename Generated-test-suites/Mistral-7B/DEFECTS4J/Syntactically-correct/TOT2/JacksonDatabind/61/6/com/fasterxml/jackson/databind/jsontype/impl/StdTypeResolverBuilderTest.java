package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;

public class StdTypeResolverBuilderTest {

    private StdTypeResolverBuilder builder;

    @Before
    public void setup() {
        builder = new StdTypeResolverBuilder();
    }

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder noTypeInfoBuilder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNull(noTypeInfoBuilder.init(null, null));
    }

    @Test
    public void testInit() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = new TypeIdResolver() {}; // dummy implementation
        builder.init(idType, idRes);
        assertEquals(idType, builder._idType);
        assertEquals(idRes, builder._customIdResolver);
        assertEquals(idType.getDefaultPropertyName(), builder._typeProperty);
    }

    // Add test cases for other methods here...

    @Test
    public void testBuildTypeSerializer() {
        // Test with valid arguments
        MapperConfig config = new MapperConfig();
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(serializer);

        // Test with null arguments
        MapperConfig config2 = null;
        JavaType baseType2 = null;
        Collection<NamedType> subtypes2 = null;
        StdTypeResolverBuilder builder2 = new StdTypeResolverBuilder();
        TypeSerializer serializer2 = builder2.buildTypeSerializer(config2, baseType2, subtypes2);
        assertNull(serializer2);

        // Test with invalid inclusion type
        StdTypeResolverBuilder builder3 = new StdTypeResolverBuilder();
        JsonTypeInfo.As includeAs = JsonTypeInfo.As.INVALID;
        builder3.inclusion(includeAs);
        try {
            builder3.buildTypeSerializer(config, baseType, subtypes);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Do not know how to construct standard type serializer for inclusion type: INVALID", e.getMessage());
        }
    }

    // Add test cases for other methods and their edge cases here...

    
    // Add your complete test suite here.
    
}