package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StdTypeResolverBuilderTest {

    @org.junit.Test
    public void testStdTypeResolverBuilder_defaultValues() throws JsonProcessingException {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        StdTypeResolver resolver = builder.build();
        assertEquals(JsonTypeInfo.Id.NONE, resolver.getId());
        assertNull(resolver.getTypeIdResolver());
        assertFalse(resolver.isTypeIdVisible());
    }

    @org.junit.Test
    public void testStdTypeResolverBuilder_init() throws JsonProcessingException {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        StdTypeResolver resolver = builder.build();
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getId());
        assertNull(resolver.getTypeIdResolver());
        assertFalse(resolver.isTypeIdVisible());

        builder = new StdTypeResolverBuilder();
        TypeIdResolver customIdResolver = mock(TypeIdResolver.class);
        builder.init(JsonTypeInfo.Id.CLASS, customIdResolver);
        resolver = builder.build();
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getId());
        assertEquals(customIdResolver, resolver.getTypeIdResolver());
        assertFalse(resolver.isTypeIdVisible());
    }

    @org.junit.Test
    public void testStdTypeResolverBuilder_buildTypeSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);

        JavaType baseType = mapper.getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypes = Arrays.asList(
            mapper.getTypeFactory().constructType(Integer.class),
            mapper.getTypeFactory().constructType(Double.class)
        );

        TypeSerializer serializer = builder.buildTypeSerializer(mapper.getSerializationConfig(), baseType, subtypes);
        assertNull(serializer);

        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        serializer = builder.buildTypeSerializer(mapper.getSerializationConfig(), baseType, subtypes);
        assertNotNull(serializer);
        assertEquals(AsPropertyTypeSerializer.class, serializer.getClass());
    }

    @org.junit.Test
    public void testStdTypeResolverBuilder_buildTypeDeserializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);

        JavaType baseType = mapper.getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypes = Arrays.asList(
            mapper.getTypeFactory().constructType(Integer.class),
            mapper.getTypeFactory().constructType(Double.class)
        );

        TypeDeserializer deserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(), baseType, subtypes);
        assertNull(deserializer);

        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        deserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(), baseType, subtypes);
        assertNotNull(deserializer);
        assertEquals(AsPropertyTypeDeserializer.class, deserializer.getClass());
    }

    @org.junit.Test
    public void testStdTypeResolverBuilder_inclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, builder.getIncludeAs());
    }

    @org.junit.Test
    public void testStdTypeResolverBuilder_typeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        builder.typeProperty("typeId");
        assertEquals("typeId", builder.getTypeProperty());
    }

    @org.junit.Test
    public void testStdTypeResolverBuilder_defaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        builder.defaultImpl(String.class);
        assertEquals(String.class, builder.getDefaultImpl());
    }

    @org.junit.Test
    public void testStdTypeResolverBuilder_typeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
    }

    @org.junit.Test
    public void testStdTypeResolverBuilder_constructStandardTypeSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);

        JavaType baseType = mapper.getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypes = Arrays.asList(
            mapper.getTypeFactory().constructType(Integer.class),
            mapper.getTypeFactory().constructType(Double.class)
        );

        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        builder.typeProperty("typeId");
        TypeSerializer serializer = builder.buildTypeSerializer(mapper.getSerializationConfig(), baseType, subtypes);

        ObjectNode node = mapper.createObjectNode();
        node.put("foo", "bar");
        node.put("typeId", "int");
        ObjectNode result = mapper.readerForUpdating(new ObjectNode(""))
            .readValueAs(mapper.getTypeFactory().constructType(baseType.getRawClass()));
        assertEquals(Integer.class, result.getClass());

        ObjectReader reader = mapper.readerForUpdating(serializer);
        result = reader.readValue(node);
        assertEquals(Integer.class, result.getClass());
        assertEquals(1, ((Number) result).intValue());

        node = mapper.createObjectNode();
        node.put("foo", "bar");
        node.put("typeId", "double");
        result = reader.readValue(node);
        assertEquals(Double.class, result.getClass());
        assertEquals(2.0, ((Number) result).doubleValue());
    }

    @org.junit.Test
    public void testStdTypeResolverBuilder_constructStandardTypeDeserializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);

        JavaType baseType = mapper.getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypes = Arrays.asList(
            mapper.getTypeFactory().constructType(Integer.class),
            mapper.getTypeFactory().constructType(Double.class)
        );

        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        builder.typeProperty("typeId");
        builder.defaultImpl(Integer.class);
        TypeDeserializer deserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(), baseType, subtypes);

        ObjectNode node = mapper.createObjectNode();
        node.put("foo", "bar");
        node.put("typeId", "int");
        ObjectNode result = mapper.readerForUpdating(new ObjectNode(""))
            .readValueAs(mapper.getTypeFactory().constructType(baseType.getRawClass()));
        assertEquals(Integer.class, result.getClass());
        assertEquals(1, ((Number) result).intValue());

        ObjectReader reader = mapper.readerForUpdating(deserializer);
        result = reader.readValue(node);
        assertEquals(Integer.class, result.getClass());
        assertEquals(1, ((Number) result).intValue());

        node = mapper.createObjectNode();
        node.put("foo", "bar");
        node.put("typeId", "double");
        result = reader.readValue(node);
        assertEquals(Double.class, result.getClass());
        assertEquals(2.0, ((Number) result).doubleValue());

        node = mapper.createObjectNode();
        node.put("foo", "bar");
        result = reader.readValue(node);
        assertEquals(String.class, result.getClass());
        assertEquals("bar", result.asText());
    }
}