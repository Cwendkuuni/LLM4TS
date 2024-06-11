package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class StdTypeResolverBuilderTest {

    private StdTypeResolverBuilder builder;

    @Before
    public void setUp() {
        builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.CLASS, null);
    }

    @Test
    public void testBuildTypeSerializer_ClassIdType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MapperConfig config = new MapperConfig(mapper.getFactory());
        JavaType baseType = mapper.getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypes = Arrays.asList(
                new SimpleName("StringType1"),
                new SimpleName("StringType2")
        );
        TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        Assert.assertNotNull(serializer);
    }

    @Test
    public void testBuildTypeSerializer_NoneIdType() throws Exception {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        TypeSerializer serializer = builder.buildTypeSerializer(null, null, null);
        Assert.assertNull(serializer);
    }

    @Test
    public void testBuildTypeDeserializer_ClassIdType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MapperConfig config = new MapperConfig(mapper.getFactory());
        JavaType baseType = mapper.getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypes = Arrays.asList(
                new SimpleName("StringType1"),
                new SimpleName("StringType2")
        );
        TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        Assert.assertNotNull(deserializer);
    }

    @Test
    public void testBuildTypeDeserializer_NoneIdType() throws Exception {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        TypeDeserializer deserializer = builder.buildTypeDeserializer(null, null, null);
        Assert.assertNull(deserializer);
    }

    @Test
    public void testTypeProperty() throws Exception {
        builder.typeProperty("myTypeProperty");
        Assert.assertEquals("myTypeProperty", builder.getTypeProperty());
    }

    @Test
    public void testDefaultImpl() throws Exception {
        builder.defaultImpl(String[].class);
        Assert.assertEquals(String[].class, builder.getDefaultImpl());
    }

    @Test
    public void testIncludeAs() throws Exception {
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        Assert.assertEquals(JsonTypeInfo.As.PROPERTY, builder._includeAs);
    }

    @Test
    public void testTypeIdVisibility() throws Exception {
        builder.typeIdVisibility(true);
        Assert.assertTrue(builder.isTypeIdVisible());
    }

    @Test
    public void testCustomIdResolver() throws Exception {
        TypeIdResolver customIdResolver = new SimpleTypeIdResolver();
        builder.defaultImpl(String.class);
        builder.init(JsonTypeInfo.Id.NAME, customIdResolver);
        TypeSerializer serializer = builder.buildTypeSerializer(null, null, null);
        Assert.assertNotNull(serializer);
        Assert.assertEquals(customIdResolver, builder._customIdResolver);
    }
}

class SimpleName {
    private final String name;

    public SimpleName(String name) {
        this.name = name;
    }
}

class SimpleTypeIdResolver implements TypeIdResolver {
    @Override
    public TypeId idFromValue(Serializable value, JavaType baseType) {
        return new TypeId(new SimpleName("MyType"), baseType);
    }

    @Override
    public TypeId idFromValueAndType(Serializable value, JavaType baseType, JavaType valueType) {
        return idFromValue(value, baseType);
    }

    @Override
    public JavaType resolveType(TypeId id) {
        if (id.getName().equals("MyType")) {
            return mapper.getTypeFactory().constructType(Integer.class);
        }
        return null;
    }
}