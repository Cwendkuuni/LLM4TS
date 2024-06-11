package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;

public class StdTypeResolverBuilderTest {

    @org.junit.BeforeClass
    public static void setup() {
        // Initialize JsonFactory and ObjectMapper
    }

    @org.junit.Test
    public void testDefaultConstructor() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder._idType);
        assertNull(builder._includeAs);
        assertNull(builder._typeProperty);
        assertFalse(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
        assertNull(builder._customIdResolver);
    }

    @org.junit.Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNull(builder._idType);
        assertNull(builder._includeAs);
        assertNull(builder._typeProperty);
        assertFalse(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
        assertNull(builder._customIdResolver);
    }

    @org.junit.Test
    public void testInitWithNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        try {
            builder.init(null, null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("idType can not be null", e.getMessage());
        }
    }

    @org.junit.Test
    public void testInitWithNullIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        try {
            builder.init(JsonTypeInfo.Id.CLASS, null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("idRes can not be null", e.getMessage());
        }
    }

    @org.junit.Test
    public void testInit() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idResolver = new TypeNameIdResolver(MapperFactory.getMapper().getConfig(), null, Arrays.asList(null), true, false);
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idResolver);
        assertEquals(idType, builder._idType);
        assertEquals(idResolver, builder._customIdResolver);
        assertEquals(idType.getDefaultPropertyName(), builder._typeProperty);
        assertFalse(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
    }

    @org.junit.Test
    public void testBuildTypeSerializerNullBaseType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertNull(builder.buildTypeSerializer(null, null, null));
    }

    @org.junit.Test
    public void testBuildTypeSerializerNoIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(builder.buildTypeSerializer(null, null, null));
    }

    @org.junit.Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        ObjectMapper mapper = MapperFactory.getMapper();
        JavaType baseType = mapper.getTypeFactory().constructValueType(int.class, String.class);
        Collection<NamedType> subtypes = Arrays.asList(new SimpleType("SubType1"), new SimpleType("SubType2"));
        TypeSerializer serializer = builder.buildTypeSerializer(mapper.getConfig(), baseType, subtypes);
        assertNotNull(serializer);
    }

    @org.junit.Test
    public void testBuildTypeDeserializerNullBaseType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertNull(builder.buildTypeDeserializer(null, null, null));
    }

    @org.junit.Test
    public void testBuildTypeDeserializerNoIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(builder.buildTypeDeserializer(null, null, null));
    }

    @org.junit.Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        ObjectMapper mapper = MapperFactory.getMapper();
        JavaType baseType = mapper.getTypeFactory().constructValueType(int.class, String.class);
        Collection<NamedType> subtypes = Arrays.asList(new SimpleType("SubType1"), new SimpleType("SubType2"));
        TypeDeserializer deserializer = builder.buildTypeDeserializer(mapper.getConfig(), baseType, subtypes);
        assertNotNull(deserializer);
    }

    @org.junit.Test
    public void testInclude() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        assertEquals(JsonTypeInfo.As.PROPERTY, builder._includeAs);
    }

    @org.junit.Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.typeProperty("typeId");
        assertEquals("typeId", builder._typeProperty);
    }

    @org.junit.Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.defaultImpl(String.class);
        assertEquals(String.class, builder._defaultImpl);
    }

    @org.junit.Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.typeIdVisibility(true);
        assertTrue(builder._typeIdVisible);
    }

    @org.junit.Test
    public void testGetDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.defaultImpl(String.class);
        assertEquals(String.class, builder.getDefaultImpl());
    }

    @org.junit.Test
    public void testGetTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.typeProperty("typeId");
        assertEquals("typeId", builder.getTypeProperty());
    }

    @org.junit.Test
    public void testIsTypeIdVisible() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
    }

    @org.junit.Test
    public void testIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        MapperConfig<?> config = MapperFactory.getMapper().getConfig();
        JavaType baseType = config.getTypeFactory().constructValueType(int.class, String.class);
        Collection<NamedType> subtypes = Arrays.asList(new SimpleType("SubType1"), new SimpleType("SubType2"));
        TypeIdResolver idResolver = builder.idResolver(config, baseType, subtypes, true, false);
        assertNotNull(idResolver);
    }
}