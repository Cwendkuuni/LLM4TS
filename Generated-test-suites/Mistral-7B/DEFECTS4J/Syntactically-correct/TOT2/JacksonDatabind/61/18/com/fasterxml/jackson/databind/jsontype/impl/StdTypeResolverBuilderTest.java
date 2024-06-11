package com.example.junit;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class StdTypeResolverBuilderTest {

    // Test constructor
    @Test
    public void testConstructor() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNotNull(builder);
    }

    // Test null arguments
    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdRes() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
    }

    // Test different types of JsonTypeInfo.Id
    @Test
    public void testBuildTypeSerializerWithClassIdType() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, null);
        TypeSerializer serializer = builder.buildTypeSerializer(null, null, null);
        assertNotNull(serializer);
    }

    @Test
    public void testBuildTypeSerializerWithMinimalClassIdType() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.MINIMAL_CLASS;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, null);
        TypeSerializer serializer = builder.buildTypeSerializer(null, null, null);
        assertNotNull(serializer);
    }

    // Test TypeIdResolver
    @Test
    public void testBuildTypeSerializerWithCustomIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        TypeIdResolver customIdResolver = mock(TypeIdResolver.class);
        builder.init(JsonTypeInfo.Id.CLASS, customIdResolver);
        TypeSerializer serializer = builder.buildTypeSerializer(null, null, null);
        verify(customIdResolver).getId(any(), any(), any(), anyBoolean(), anyBoolean());
        assertNotNull(serializer);
    }

    // Test different types of inclusion
    @Test
    public void testBuildTypeDeserializerWithWrapperArrayInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        TypeDeserializer deserializer = builder.buildTypeDeserializer(null, null, null);
        assertNotNull(deserializer);
    }

    // Test typeProperty
    @Test
    public void testBuildTypeDeserializerWithTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("testProperty");
        TypeDeserializer deserializer = builder.buildTypeDeserializer(null, null, null);
        assertEquals("testProperty", builder.getTypeProperty());
        assertNotNull(deserializer);
    }

    // Test defaultImpl
    @Test
    public void testBuildTypeDeserializerWithDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(String.class);
        TypeDeserializer deserializer = builder.buildTypeDeserializer(null, null, null);
        assertNotNull(deserializer);
    }

    // Test typeIdVisibility
    @Test
    public void testBuildTypeDeserializerWithTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        TypeDeserializer deserializer = builder.buildTypeDeserializer(null, null, null);
        assertTrue(builder.isTypeIdVisible());
        assertNotNull(deserializer);
    }

}