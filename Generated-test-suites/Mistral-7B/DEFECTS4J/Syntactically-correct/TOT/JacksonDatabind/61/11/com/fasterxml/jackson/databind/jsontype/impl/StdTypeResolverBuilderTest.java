package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test cases for {@link StdTypeResolverBuilder}.
 */
public class StdTypeResolverBuilderTest {

    private ObjectMapper _mapper;

    @Before
    public void setup() {
        MapperFactory mapperFactory = new MapperFactory();
        _mapper = mapperFactory.builder().build();
    }

    /**
     * Test building a standard type resolver with no type information.
     */
    @Test
    public void testNoTypeInfo() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        TypeSerializer serializer = builder.buildTypeSerializer(_mapper.getSerializationConfig(),
                Object.class.getType(), null);
        assertNull(serializer);
    }

    /**
     * Test building a standard type resolver with JsonTypeInfo.Id.CLASS and
     * As.PROPERTY inclusion type.
     */
    @Test
    public void testClassProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(As.PROPERTY);
        builder.typeProperty("type");
        TypeSerializer serializer = builder.buildTypeSerializer(_mapper.getSerializationConfig(),
                Object.class.getType(), null);
        TypeSerializer expected = _mapper.getTypeFactory().constructType(new TypeReference<Object>() {});
        assertEquals(expected, serializer);

        TypeDeserializer deserializer = builder.buildTypeDeserializer(_mapper.getDeserializationConfig(),
                Object.class.getType(), null);
        assertEquals(Object.class, deserializer.getType());
    }

    /**
     * Test building a standard type resolver with JsonTypeInfo.Id.NONE and
     * As.WRAPPER_ARRAY inclusion type.
     */
    @Test
    public void testNoneWrapperArray() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.NONE, null);
        builder.inclusion(As.WRAPPER_ARRAY);
        TypeSerializer serializer = builder.buildTypeSerializer(_mapper.getSerializationConfig(),
                Object[].class.getType(), null);
        assertNull(serializer);

        TypeDeserializer deserializer = builder.buildTypeDeserializer(_mapper.getDeserializationConfig(),
                Object[].class.getType(), null);
        assertNull(deserializer);
    }

    /**
     * Test building a standard type resolver with JsonTypeInfo.Id.CUSTOM and
     * As.EXTERNAL_PROPERTY inclusion type.
     */
    @Test
    public void testCustomExternalProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.CUSTOM, null);
        builder.inclusion(As.EXTERNAL_PROPERTY);
        builder.typeProperty("type");
        TypeSerializer serializer = builder.buildTypeSerializer(_mapper.getSerializationConfig(),
                Object.class.getType(), null);
        assertNull(serializer);

        TypeDeserializer deserializer = builder.buildTypeDeserializer(_mapper.getDeserializationConfig(),
                Object.class.getType(), null);
        assertNull(deserializer);

        builder.typeIdVisibility(true);
        builder.defaultImpl(String.class);
        serializer = builder.buildTypeSerializer(_mapper.getSerializationConfig(),
                Object.class.getType(), null);
        assertEquals(String.class, serializer.getType());

        deserializer = builder.buildTypeDeserializer(_mapper.getDeserializationConfig(),
                Object.class.getType(), null);
        assertEquals(String.class, deserializer.getType());
    }
}