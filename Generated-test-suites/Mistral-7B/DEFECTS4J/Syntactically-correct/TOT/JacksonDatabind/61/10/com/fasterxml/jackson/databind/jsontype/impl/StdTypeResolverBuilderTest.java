package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class StdTypeResolverBuilderTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    /**
     * Test initialization with default settings.
     */
    @Test
    public void testInitWithDefaults() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        StdTypeResolverBuilder initializedBuilder = builder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(JsonTypeInfo.Id.CLASS, initializedBuilder._idType);
        assertNull(initializedBuilder._includeAs);
        assertNull(initializedBuilder._typeProperty);
        assertFalse(initializedBuilder._typeIdVisible);
        assertNull(initializedBuilder._defaultImpl);
        assertNull(initializedBuilder._customIdResolver);
    }

    /**
     * Test initialization with custom settings.
     */
    @Test
    public void testInitWithCustomSettings() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        String typeProperty = "typeProperty";
        StdTypeResolverBuilder initializedBuilder = builder.init(JsonTypeInfo.Id.CLASS, null)
                .typeProperty(typeProperty)
                .typeIdVisibility(true)
                .defaultImpl(String.class)
                .customIdResolver(new AlwaysReturnStringTypeIdResolver());

        assertEquals(JsonTypeInfo.Id.CLASS, initializedBuilder._idType);
        assertSame(typeProperty, initializedBuilder._typeProperty);
        assertTrue(initializedBuilder._typeIdVisible);
        assertSame(String.class, initializedBuilder._defaultImpl);
        assertSame(AlwaysReturnStringTypeIdResolver.class, initializedBuilder._customIdResolver);
    }

    /**
     * Test building type serializer with default settings.
     */
    @Test
    public void testBuildTypeSerializerWithDefaults() throws JsonProcessingException {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);

        TypeSerializer typeSerializer = builder.buildTypeSerializer(objectMapper.getSerializationConfig(),
                JavaType.valueOf("com.example.MyClass"), null);
        assertNull(typeSerializer);
    }

    /**
     * Test building type serializer with custom settings.
     */
    @Test
    public void testBuildTypeSerializerWithCustomSettings() throws JsonProcessingException {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        String typeProperty = "typeProperty";
        builder.init(JsonTypeInfo.Id.PROPERTY, null)
                .typeProperty(typeProperty)
                .typeIdVisibility(true)
                .defaultImpl(String.class)
                .customIdResolver(new AlwaysReturnStringTypeIdResolver());

        TypeSerializer typeSerializer = builder.buildTypeSerializer(objectMapper.getSerializationConfig(),
                JavaType.valueOf("com.example.MyClass"), null);

        // verify serializer type and properties
        assertSame(AsPropertyTypeSerializer.class, typeSerializer.getClass());
        assertEquals(typeProperty, typeSerializer.getPropertyName());
        assertTrue(((AsPropertyTypeSerializer) typeSerializer).isTypeIdVisible());
        assertSame(String.class, typeSerializer.getBoundType());
    }

    /**
     * Test building type deserializer with default settings.
     */
    @Test
    public void testBuildTypeDeserializerWithDefaults() throws JsonProcessingException {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);

        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(objectMapper.getDeserializationConfig(),
                JavaType.valueOf("com.example.MyClass"), null);
        assertNull(typeDeserializer);
    }

    /**
     * Test building type deserializer with custom settings.
     */
    @Test
    public void testBuildTypeDeserializerWithCustomSettings() throws JsonProcessingException {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        String typeProperty = "typeProperty";
        builder.init(JsonTypeInfo.Id.PROPERTY, null)
                .typeProperty(typeProperty)
                .typeIdVisibility(true)
                .defaultImpl(String.class)
                .customIdResolver(new AlwaysReturnStringTypeIdResolver());

        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(objectMapper.getDeserializationConfig(),
                JavaType.valueOf("com.example.MyClass"), null);

        // verify deserializer type and properties
        assertSame(AsPropertyTypeDeserializer.class, typeDeserializer.getClass());
        assertEquals(typeProperty, typeDeserializer.getBeanPropertyName());
        assertTrue(((AsPropertyTypeDeserializer) typeDeserializer).isTypeIdVisible());
        assertSame(String.class, typeDeserializer.getTypeIdResolver().resolveId(_nullContext, null));
        assertSame(String.class, typeDeserializer.getDefaultValue());
    }
}