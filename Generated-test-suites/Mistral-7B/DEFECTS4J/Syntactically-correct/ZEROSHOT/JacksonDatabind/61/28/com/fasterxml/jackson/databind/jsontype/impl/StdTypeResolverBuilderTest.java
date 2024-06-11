package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StdTypeResolverBuilderTest {

    @org.junit.Test
    public void testStdTypeResolverBuilder_DefaultConstruction() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder._idType);
        assertNull(builder._includeAs);
        assertNull(builder._typeProperty);
        assertNull(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
        assertNull(builder._customIdResolver);
    }

    @org.junit.Test
    public void testStdTypeResolverBuilder_NoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNull(builder._idType);
        assertNull(builder._includeAs);
        assertNull(builder._typeProperty);
        assertFalse(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
        assertNull(builder._customIdResolver);
    }

    @org.junit.Test
    public void testStdTypeResolverBuilder_Init() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = new MinimalClassNameIdResolver(JavaType.TYPE_REFERENCE_TO(String.class), null);
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        assertEquals(idType, builder._idType);
        assertEquals(idRes, builder._customIdResolver);
        assertEquals(idType.getDefaultPropertyName(), builder._typeProperty);
        assertFalse(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
    }

    @org.junit.Test
    public void testStdTypeResolverBuilder_InitWithNullIdType() {
        TypeIdResolver idRes = new MinimalClassNameIdResolver(JavaType.TYPE_REFERENCE_TO(String.class), null);
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> builder.init(null, idRes));
        assertEquals("idType can not be null", exception.getMessage());
    }

    @org.junit.Test
    public void testStdTypeResolverBuilder_InitWithNullIdResolver() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> builder.init(idType, null));
        assertEquals("idRes can not be null", exception.getMessage());
    }

    @org.junit.Test
    public void testStdTypeResolverBuilder_BuildTypeSerializer() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        MapperConfig<?> config = new MapperConfig.Builder().build();
        JavaType baseType = JavaType.TYPE_REFERENCE_TO(Object[].class);
        Collection<NamedType> subtypes = Arrays.asList(new NamedType(String.class, "StringType"));
        TypeIdResolver idRes = new MinimalClassNameIdResolver(baseType, config.getTypeFactory());
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);

        TypeSerializer result = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(result);
        assertEquals(idType, builder._idType);
        assertEquals(idRes, builder._customIdResolver);

        // Test null result when idType is NONE
        builder.init(JsonTypeInfo.Id.NONE, null);
        result = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(result);
        assertEquals(JsonTypeInfo.Id.NONE, builder._idType);
    }

    @org.junit.Test
    public void testStdTypeResolverBuilder_BuildTypeDeserializer() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        MapperConfig<?> config = new MapperConfig.Builder().build();
        JavaType baseType = JavaType.TYPE_REFERENCE_TO(Object.class);
        Collection<NamedType> subtypes = Arrays.asList(new NamedType(String.class, "StringType"));
        TypeIdResolver idRes = new MinimalClassNameIdResolver(baseType, config.getTypeFactory());
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);

        TypeDeserializer result = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(result);
        assertEquals(idType, builder._idType);
        assertEquals(idRes, builder._customIdResolver);

        // Test null result when idType is NONE
        builder.init(JsonTypeInfo.Id.NONE, null);
        result = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(result);
        assertEquals(JsonTypeInfo.Id.NONE, builder._idType);
    }

    @org.junit.Test
    public void testStdTypeResolverBuilder_Inclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JsonTypeInfo.As includeAs = JsonTypeInfo.As.PROPERTY;
        builder.inclusion(includeAs);
        assertEquals(includeAs, builder._includeAs);
    }

    @org.junit.Test
    public void testStdTypeResolverBuilder_InclusionNull() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> builder.inclusion(null));
        assertEquals("includeAs can not be null", exception.getMessage());
    }

    @org.junit.Test
    public void testStdTypeResolverBuilder_TypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        String typeProperty = "myTypeProperty";
        builder.typeProperty(typeProperty);
        assertEquals(typeProperty, builder._typeProperty);
    }

    @org.junit.Test
    public void testStdTypeResolverBuilder_TypePropertyNull() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty(null);
        assertNull(builder._typeProperty);
    }

    @org.junit.Test
    public void testStdTypeResolverBuilder_DefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        Class<?> defaultImpl = String.class;
        builder.defaultImpl(defaultImpl);
        assertEquals(defaultImpl, builder._defaultImpl);
    }

    @org.junit.Test
    public void testStdTypeResolverBuilder_DefaultImplNull() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(null);
        assertNull(builder._defaultImpl);
    }

    @org.junit.Test
    public void testStdTypeResolverBuilder_TypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder._typeIdVisible);
    }

    @org.junit.Test
    public void testStdTypeResolverBuilder_TypeIdVisibilityFalse() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(false);
        assertFalse(builder._typeIdVisible);
    }

    @org.junit.Test
    public void testStdTypeResolverBuilder_Accessors() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        initBuilderWithDefaultValues(builder);

        assertEquals(JsonTypeInfo.Id.NONE, builder.getDefaultImpl());
        assertEquals("", builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());

        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = new MinimalClassNameIdResolver(JavaType.TYPE_REFERENCE_TO(Object.class), null);
        builder.init(idType, idRes);

        assertEquals(idType, builder.getDefaultImpl());
        assertEquals(idType.getDefaultPropertyName(), builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());
    }

    @org.junit.Test
    public void testStdTypeResolverBuilder_AccessorsWithCustomIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        initBuilderWithDefaultValues(builder);

        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = new MinimalClassNameIdResolver(JavaType.TYPE_REFERENCE_TO(Object.class), null);
        builder.init(idType, idRes);

        assertEquals(idType, builder.getDefaultImpl());
        assertEquals(idType.getDefaultPropertyName(), builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());
        assertEquals(idRes, builder._customIdResolver);
    }

    private void initBuilderWithDefaultValues(StdTypeResolverBuilder builder) {
        assertNull(builder._defaultImpl);
        assertEquals("", builder._typeProperty);
        assertFalse(builder._typeIdVisible);
        assertNull(builder._customIdResolver);
    }

    @org.junit.Test
    public void testStdTypeResolverBuilder_IdResolver() {
        MapperConfig<?> config = new MapperConfig.Builder().build();
        JavaType baseType = JavaType.TYPE_REFERENCE_TO(Object.class);
        Collection<NamedType> subtypes = Arrays.asList(new NamedType(String.class, "StringType"));
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = new MinimalClassNameIdResolver(baseType, config.getTypeFactory());

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        assertEquals(idType, builder._idType);
        assertEquals(idRes, builder._customIdResolver);

        ObjectNode result = builder.buildTypeSerializer(config, baseType, subtypes).serialize(config.getFactory().createObjectNode());
        assertNotNull(result);
        assertNull(result.get("@type"));

        result = builder.buildTypeDeserializer(config, baseType, subtypes).deserialize(config, new ObjectNode(config.getFactory()));
        assertNotNull(result);
    }

    @org.junit.Test
    public void testStdTypeResolverBuilder_IdResolverNull() {
        MapperConfig<?> config = new MapperConfig.Builder().build();
        JavaType baseType = JavaType.TYPE_REFERENCE_TO(Object.class);
        Collection<NamedType> subtypes = Arrays.asList(new NamedType(String.class, "StringType"));
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> builder.init(idType, null));
        assertEquals("idRes can not be null", exception.getMessage());
    }

    @org.junit.Test
    public void testStdTypeResolverBuilder_IdResolverDifferentBaseType() {
        MapperConfig<?> config = new MapperConfig.Builder().build();
        JavaType baseType = JavaType.TYPE_REFERENCE_TO(Object[].class);
        Collection<NamedType> subtypes = Arrays.asList(new NamedType(String[].class, "StringArrayType"));
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = new MinimalClassNameIdResolver(JavaType.TYPE_REFERENCE_TO(Object.class), null);

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> builder.init(idType, idRes));
        assertEquals("Base types do not match: [class java.lang.Object], [class [Ljava.lang.Object;]", exception.getMessage());
    }

    @org.junit.Test
    public void testStdTypeResolverBuilder_IdResolverDifferentSubtypes() {
        MapperConfig<?> config = new MapperConfig.Builder().build();
        JavaType baseType = JavaType.TYPE_REFERENCE_TO(Object.class);
        Collection<NamedType> subtypes1 = Arrays.asList(new NamedType(String.class, "StringType"), new NamedType(Integer.class, "IntegerType"));
        Collection<NamedType> subtypes2 = Arrays.asList(new NamedType(String.class, "StringType"), new NamedType(Double.class, "DoubleType"));
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = new MinimalClassNameIdResolver(baseType, config.getTypeFactory());

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> builder.init(idType, idRes));
        assertEquals("Subtypes specified do not match: [" + subtypes1 + "], [" + subtypes2 + "]", exception.getMessage());
    }
}