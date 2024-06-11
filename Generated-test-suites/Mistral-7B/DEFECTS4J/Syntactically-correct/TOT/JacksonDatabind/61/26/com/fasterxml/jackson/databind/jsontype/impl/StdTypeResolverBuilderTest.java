package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Test cases for {@link StdTypeResolverBuilder} class.
 */
public class StdTypeResolverBuilderTest {

    private static final ObjectMapper objectMapper;

    static {
        SimpleModule module = new SimpleModule();
        module.setSerializerFactory(Maps.newHashMap());
        module.setDeserializerFactory(Maps.newHashMap());
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);
    }

    // Test method for init(JsonTypeInfo.Id idType, TypeIdResolver idRes)
    @org.junit.Test
    public void testInit() throws Exception {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = new ClassNameIdResolver(String.class, null);
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        assertSame(idType, builder._idType);
        assertSame(idRes, builder._customIdResolver);
    }

    // Test method for init with null idType
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInitNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, new ClassNameIdResolver(String.class, null));
    }

    // Test method for buildTypeSerializer(SerializationConfig config, JavaType baseType, Collection<NamedType> subtypes)
    @org.junit.Test
    public void testBuildTypeSerializer() throws Exception {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        JavaType intType = objectMapper.getTypeFactory().constructType(int.class);
        Collection<NamedType> subtypes = Arrays.asList(new ClassType(String.class, null), new ClassType(Integer.class, null));
        TypeSerializer serializer = builder.buildTypeSerializer(objectMapper.getSerializationConfig(), intType, subtypes);
        assertNull(serializer);

        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        serializer = builder.buildTypeSerializer(objectMapper.getSerializationConfig(), intType, subtypes);
        assertNull(serializer);

        builder.init(JsonTypeInfo.Id.CLASS, null);
        serializer = builder.buildTypeSerializer(objectMapper.getSerializationConfig(), intType, subtypes);
        assertNotNull(serializer);
    }

    // Test method for buildTypeDeserializer(DeserializationConfig config, JavaType baseType, Collection<NamedType> subtypes)
    @org.junit.Test
    public void testBuildTypeDeserializer() throws Exception {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        JavaType intType = objectMapper.getTypeFactory().constructType(int.class);
        Collection<NamedType> subtypes = Arrays.asList(new ClassType(String.class, null), new ClassType(Integer.class, null));
        TypeDeserializer deserializer = builder.buildTypeDeserializer(objectMapper.getDeserializationConfig(), intType, subtypes);
        assertNull(deserializer);

        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        deserializer = builder.buildTypeDeserializer(objectMapper.getDeserializationConfig(), intType, subtypes);
        assertNull(deserializer);

        builder.init(JsonTypeInfo.Id.CLASS, null);
        deserializer = builder.buildTypeDeserializer(objectMapper.getDeserializationConfig(), intType, subtypes);
        assertNotNull(deserializer);
    }

    // Test method for inclusion(JsonTypeInfo.As includeAs)
    @org.junit.Test
    public void testInclusion() throws Exception {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        assertEquals(JsonTypeInfo.As.PROPERTY, builder._includeAs);
    }

    // Test method for typeProperty(String typeIdPropName)
    @org.junit.Test
    public void testTypeProperty() throws Exception {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.typeProperty("typeId");
        assertEquals("typeId", builder._typeProperty);
    }

    // Test method for defaultImpl(Class<?> defaultImpl)
    @org.junit.Test
    public void testDefaultImpl() throws Exception {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.defaultImpl(String.class);
        assertEquals(String.class, builder._defaultImpl);
    }

    // Test method for typeIdVisibility(boolean isVisible)
    @org.junit.Test
    public void testTypeIdVisibility() throws Exception {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.typeIdVisibility(true);
        assertTrue(builder._typeIdVisible);
    }

    /**
     * Helper class for creating {@link NamedType} instances for testing.
     */
    private static class ClassType implements NamedType {
        private final Class<?> _clazz;
        private final String _name;

        public ClassType(Class<?> clazz, String name) {
            _clazz = clazz;
            _name = name;
        }

        @Override
        public Class<?> getRawClass() {
            return _clazz;
        }

        @Override
        public String getName() {
            return _name;
        }
    }
}