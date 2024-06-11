package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testStdTypeResolverBuilder_DefaultConstruction() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());
        assertNull(builder._customIdResolver);
        assertNull(builder._idType);
        assertNull(builder._includeAs);
    }

    @Test
    public void testStdTypeResolverBuilder_Init() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = new MinimalClassNameIdResolver(Object.class, null);
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        assertEquals(idType, builder.getIdType());
        assertEquals(idRes, builder._customIdResolver);
        assertEquals(JsonTypeInfo.Property.STANDARD_PROPERTY_NAME, builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());
    }

    @Test
    public void testStdTypeResolverBuilder_Include() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        assertEquals(JsonTypeInfo.As.PROPERTY, builder.getIncludeAs());
    }

    @Test
    public void testStdTypeResolverBuilder_TypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("customTypePropertyName");
        assertEquals("customTypePropertyName", builder.getTypeProperty());
    }

    @Test
    public void testStdTypeResolverBuilder_DefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(String.class);
        assertEquals(String.class, builder.getDefaultImpl());
    }

    @Test
    public void testStdTypeResolverBuilder_TypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
    }

    @Test
    public void testStdTypeResolverBuilder_BuildTypeSerializer() throws JacksonException {
        ObjectMapper mapper = new ObjectMapper();
        MapperConfig<ObjectMapper> config = mapper.getConfig();
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        TypeSerializer serializer = builder.buildTypeSerializer(config.getDefaultConfig(), JavaType.constructType(String[].class), null);
        assertNotNull(serializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testStdTypeResolverBuilder_BuildTypeSerializer_NullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.buildTypeSerializer(null, JavaType.constructType(String[].class), null);
    }

    @Test
    public void testStdTypeResolverBuilder_BuildTypeDeserializer() throws JacksonException {
        ObjectMapper mapper = new ObjectMapper();
        MapperConfig<ObjectMapper> config = mapper.getConfig();
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        TypeDeserializer deserializer = builder.buildTypeDeserializer(config.getDefaultConfig(), JavaType.constructType(String.class), null);
        assertNotNull(deserializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testStdTypeResolverBuilder_BuildTypeDeserializer_NullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.buildTypeDeserializer(null, JavaType.constructType(String.class), null);
    }
}