package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.core.type.TypeReference;

import org.junit.Before;
import org.junit.Test;

public class StdTypeResolverBuilderTest {

    private StdTypeResolverBuilder builder;
    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        builder = new StdTypeResolverBuilder();
        mapper = new ObjectMapper();
    }

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder noTypeInfoBuilder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNull(noTypeInfoBuilder.buildTypeSerializer(null, null, null));
        assertNull(noTypeInfoBuilder.buildTypeDeserializer(null, null, null));
    }

    @Test
    public void testInit() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals("", builder._typeProperty);
        assertFalse(builder._typeIdVisible);
        assertNull(builder._defaultImpl);

        builder.init(JsonTypeInfo.Id.MINIMAL_CLASS, null);
        assertEquals(JsonTypeInfo.Id.MINIMAL_CLASS, builder._idType);

        builder.init(JsonTypeInfo.Id.NAME, null);
        assertEquals(JsonTypeInfo.Id.NAME, builder._idType);

        builder.init(JsonTypeInfo.Id.NONE, null);
        assertEquals(JsonTypeInfo.Id.NONE, builder._idType);

        builder.init(JsonTypeInfo.Id.CUSTOM, new CustomTypeIdResolver());
        assertEquals(JsonTypeInfo.Id.CUSTOM, builder._idType);
        assertNotNull(builder._customIdResolver);
    }

    @Test
    public void testBuildTypeSerializer() throws Exception {
        builder.init(JsonTypeInfo.Id.CLASS, null);

        TypeReference<String> stringType = new TypeReference<String>() {};
        Collection<NamedType> subtypes = Arrays.asList(
                new NamedType(String.class, "stringType"),
                new NamedType(Integer.class, "integerType"));

        TypeSerializer typeSerializer = builder.buildTypeSerializer(mapper.getConfiguration(), stringType, subtypes);
        assertNotNull(typeSerializer);

        JsonSerializer serializer = mapper.findValueSerializer(String.class);
        assertEquals(serializer.getClass(), typeSerializer.getClass());

        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        typeSerializer = builder.buildTypeSerializer(mapper.getConfiguration(), stringType, subtypes);
        assertNotNull(typeSerializer);
        assertNotSame(serializer.getClass(), typeSerializer.getClass());

        builder.init(JsonTypeInfo.Id.WRAPPER_ARRAY, null);
        typeSerializer = builder.buildTypeSerializer(mapper.getConfiguration(), stringType, subtypes);
        assertNull(typeSerializer);

        builder.init(JsonTypeInfo.Id.WRAPPER_OBJECT, null);
        typeSerializer = builder.buildTypeSerializer(mapper.getConfiguration(), stringType, subtypes);
        assertNull(typeSerializer);
    }

    @Test
    public void testBuildTypeDeserializer() throws Exception {
        builder.init(JsonTypeInfo.Id.CLASS, null);

        TypeReference<String> stringType = new TypeReference<String>() {};
        Collection<NamedType> subtypes = Arrays.asList(
                new NamedType(String.class, "stringType"),
                new NamedType(Integer.class, "integerType"));

        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(mapper.getConfiguration(), stringType, subtypes);
        assertNull(typeDeserializer);

        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        typeDeserializer = builder.buildTypeDeserializer(mapper.getConfiguration(), stringType, subtypes);
        assertNotNull(typeDeserializer);

        JsonDeserializer deserializer = mapper.findValueDeserializer(String.class);
        assertEquals(deserializer.getClass(), typeDeserializer.getClass());

        builder.init(JsonTypeInfo.Id.WRAPPER_ARRAY, null);
        typeDeserializer = builder.buildTypeDeserializer(mapper.getConfiguration(), stringType, subtypes);
        assertNull(typeDeserializer);

        builder.init(JsonTypeInfo.Id.WRAPPER_OBJECT, null);
        typeDeserializer = builder.buildTypeDeserializer(mapper.getConfiguration(), stringType, subtypes);
        assertNull(typeDeserializer);
    }

    @Test
    public void testInclusion() {
        builder.init(JsonTypeInfo.Id.CLASS, null);

        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        assertEquals(JsonTypeInfo.As.PROPERTY, builder._includeAs);

        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        assertEquals(JsonTypeInfo.As.WRAPPER_OBJECT, builder._includeAs);

        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        assertEquals(JsonTypeInfo.As.EXTERNAL_PROPERTY, builder._includeAs);

        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, builder._includeAs);
    }

    @Test
    public void testTypeProperty() {
        builder.init(JsonTypeInfo.Id.CLASS, null);

        builder.typeProperty("typeProperty");
        assertEquals("typeProperty", builder._typeProperty);

        builder.typeProperty("");
        assertEquals("", builder._typeProperty);

        builder.typeProperty("typeIdPropName");
        assertEquals("typeIdPropName", builder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        builder.init(JsonTypeInfo.Id.CLASS, null);

        builder.defaultImpl(String.class);
        assertSame(String.class, builder._defaultImpl);

        builder.defaultImpl(Integer.class);
        assertSame(Integer.class, builder._defaultImpl);

        builder.defaultImpl(NoClass.class);
        assertSame(NoClass.class, builder._defaultImpl);

        builder.defaultImpl(Void.class);
        assertSame(Void.class, builder._defaultImpl);

        builder.defaultImpl(null);
        assertNull(builder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        builder.init(JsonTypeInfo.Id.CLASS, null);

        builder.typeIdVisibility(true);
        assertTrue(builder._typeIdVisible);

        builder.typeIdVisibility(false);
        assertFalse(builder._typeIdVisible);
    }

    private static class CustomTypeIdResolver extends TypeIdResolverBase {
        @Override
        public TypeId resolve(MapperConfig<?> config, TypeSerializer serializer) {
            return null;
        }

        @Override
        public TypeId resolve(DeserializationConfig config, TypeDeserializer deserializer) {
            return null;
        }
    }
}