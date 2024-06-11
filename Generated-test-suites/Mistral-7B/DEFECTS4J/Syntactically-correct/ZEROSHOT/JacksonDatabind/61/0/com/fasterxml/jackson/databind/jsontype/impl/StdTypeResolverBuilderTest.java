package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collections;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import org.junit.Test;

/**
 * Unit tests for {@link StdTypeResolverBuilder}.
 */
public class StdTypeResolverBuilderTest {

    @Test
    public void testDefaultBuilder() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
        assertFalse(builder.isTypeIdVisible());
        assertNull(builder.getCustomIdResolver());
    }

    @Test
    public void testInitBasic() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, null);
        assertSame(idType, builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
        assertFalse(builder.isTypeIdVisible());
        assertNull(builder.getCustomIdResolver());
    }

    @Test
    public void testInitWithCustomIdResolver() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver customIdResolver = new TypeNameIdResolver(new MapperConfig(new ObjectMapper()), JavaType.construct(), Collections.emptyList(), false, false);
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, customIdResolver);
        assertSame(idType, builder.getIdType());
        assertSame(customIdResolver, builder.getCustomIdResolver());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
        assertFalse(builder.isTypeIdVisible());
    }

    @Test
    public void testBuildTypeSerializer() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, null);

        JavaType baseType = JavaType.construct(String.class);
        Collection<NamedType> subtypes = Arrays.asList(new NamedType(Integer.class, "INT"));

        TypeSerializer serializer = builder.buildTypeSerializer(new SerializationConfig(), baseType, subtypes);
        assertNull(serializer);

        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        serializer = builder.buildTypeSerializer(new SerializationConfig(), baseType, subtypes);
        assertNull(serializer);

        builder.typeProperty("typeIdProperty");
        serializer = builder.buildTypeSerializer(new SerializationConfig(), baseType, subtypes);
        assertNull(serializer);

        builder.defaultImpl(int[].class);
        serializer = builder.buildTypeSerializer(new SerializationConfig(), baseType, subtypes);
        assertNull(serializer);

        builder.typeIdVisibility(true);
        serializer = builder.buildTypeSerializer(new SerializationConfig(), baseType, subtypes);
        assertNull(serializer);

        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        serializer = builder.buildTypeSerializer(new SerializationConfig(), baseType, subtypes);
        assertNull(serializer);

        builder.typeProperty("typeIdProperty");
        builder.defaultImpl(Integer.class);
        serializer = builder.buildTypeSerializer(new SerializationConfig(), baseType, subtypes);
        assertNotNull(serializer);
    }

    @Test
    public void testBuildTypeDeserializer() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, null);

        JavaType baseType = JavaType.construct(String.class);
        Collection<NamedType> subtypes = Arrays.asList(new NamedType(Integer.class, "INT"));

        TypeDeserializer deserializer = builder.buildTypeDeserializer(new DeserializationConfig(), baseType, subtypes);
        assertNull(deserializer);

        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        deserializer = builder.buildTypeDeserializer(new DeserializationConfig(), baseType, subtypes);
        assertNull(deserializer);

        builder.typeProperty("typeIdProperty");
        deserializer = builder.buildTypeDeserializer(new DeserializationConfig(), baseType, subtypes);
        assertNull(deserializer);

        builder.defaultImpl(int[].class);
        deserializer = builder.buildTypeDeserializer(new DeserializationConfig(), baseType, subtypes);
        assertNull(deserializer);

        builder.typeIdVisibility(true);
        deserializer = builder.buildTypeDeserializer(new DeserializationConfig(), baseType, subtypes);
        assertNull(deserializer);

        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        deserializer = builder.buildTypeDeserializer(new DeserializationConfig(), baseType, subtypes);
        assertNull(deserializer);

        builder.typeProperty("typeIdProperty");
        builder.defaultImpl(Integer.class);
        deserializer = builder.buildTypeDeserializer(new DeserializationConfig(), baseType, subtypes);
        assertNotNull(deserializer);
    }

    @Test
    public void testTypeIdVisibility() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, null);

        builder.typeIdVisibility(true);
        assertFalse(builder.isTypeIdVisible());

        builder.typeIdVisibility(false);
        assertFalse(builder.isTypeIdVisible());

        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
    }

    @Test
    public void testTypeProperty() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, null);

        builder.typeProperty("typeIdProperty");
        assertSame("typeIdProperty", builder.getTypeProperty());

        builder.typeProperty(null);
        assertNull(builder.getTypeProperty());

        builder.typeProperty("");
        assertNull(builder.getTypeProperty());

        builder.typeProperty("typeIdProperty");
        builder.typeProperty("newTypeIdProperty");
        assertSame("newTypeIdProperty", builder.getTypeProperty());
    }

    @Test
    public void testDefaultImpl() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, null);

        builder.defaultImpl(int[].class);
        assertSame(int[].class, builder.getDefaultImpl());

        builder.defaultImpl(null);
        assertNull(builder.getDefaultImpl());

        builder.defaultImpl(int.class);
        assertSame(int.class, builder.getDefaultImpl());

        builder.defaultImpl(NoClass.class);
        assertSame(NoClass.class, builder.getDefaultImpl());

        builder.defaultImpl(Object.class);
        assertSame(Object.class, builder.getDefaultImpl());

        builder.defaultImpl(Integer.TYPE);
        assertSame(Integer.TYPE, builder.getDefaultImpl());

        builder.defaultImpl(Void.class);
        assertSame(Void.class, builder.getDefaultImpl());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullTypeResolver() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, null);
        builder.buildTypeSerializer(new SerializationConfig(), JavaType.construct(String.class), Collections.emptyList());
        builder.init(idType, (TypeIdResolver)null);
    }
}