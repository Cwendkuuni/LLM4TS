package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.Class;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeBindings.TypeSerializable;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(ClassLoader.getSystemClassLoader(), List.class);
        CollectionType collectionType = (CollectionType) javaType;
        assertNotNull(collectionType);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(TypeSerializable.INSTANCE, collectionType.getTypeBindings().getSerializationConfig());
        assertEquals(java.util.List.class, collectionType.getRawClass());
        assertEquals(java.lang.Object.class, collectionType.getContentType());
    }

    @Test
    public void testConstructWithBindings() {
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(ClassLoader.getSystemClassLoader(), List.class, TypeBindings.empty());
        CollectionType collectionType = (CollectionType) javaType;
        assertNotNull(collectionType);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(TypeSerializable.INSTANCE, collectionType.getTypeBindings());
        assertEquals(java.util.List.class, collectionType.getRawClass());
        assertEquals(java.lang.Object.class, collectionType.getContentType());
    }

    @Test
    public void testConstructWithSuperClass() {
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(ClassLoader.getSystemClassLoader(), ArrayList.class);
        CollectionType collectionType = (CollectionType) javaType;
        assertNotNull(collectionType);
        assertEquals(ArrayList.class, collectionType.getRawClass());
        assertEquals(TypeSerializable.INSTANCE, collectionType.getTypeBindings());
        assertEquals(java.util.List.class, collectionType.getRawClass());
        assertEquals(java.lang.Object.class, collectionType.getContentType());
    }

    @Test
    public void testConstructWithSuperInterfaces() {
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(ClassLoader.getSystemClassLoader(), HashSet.class, JavaType.construct(Collection.class));
        CollectionType collectionType = (CollectionType) javaType;
        assertNotNull(collectionType);
        assertEquals(HashSet.class, collectionType.getRawClass());
        assertEquals(TypeSerializable.INSTANCE, collectionType.getTypeBindings());
        assertEquals(java.util.Set.class, collectionType.getRawClass());
        assertEquals(java.lang.Object.class, collectionType.getContentType());
        assertEquals(Collection.class, collectionType.getSuperClass());
    }

    @Test
    public void testConstructWithElementType() {
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(ClassLoader.getSystemClassLoader(), List.class, Integer.TYPE);
        CollectionType collectionType = (CollectionType) javaType;
        assertNotNull(collectionType);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(TypeSerializable.INSTANCE, collectionType.getTypeBindings());
        assertEquals(java.util.List.class, collectionType.getRawClass());
        assertEquals(Integer.TYPE, collectionType.getContentType());
    }

    @Test
    public void testConstructWithValueHandler() {
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(ClassLoader.getSystemClassLoader(), List.class, Integer.TYPE, NumberValueHandler.class);
        CollectionType collectionType = (CollectionType) javaType;
        assertNotNull(collectionType);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(TypeSerializable.INSTANCE, collectionType.getTypeBindings());
        assertEquals(java.util.List.class, collectionType.getRawClass());
        assertEquals(Integer.TYPE, collectionType.getContentType());
        assertNotNull(collectionType.getValueHandler());
    }

    @Test
    public void testConstructWithTypeHandler() {
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(ClassLoader.getSystemClassLoader(), List.class, Integer.TYPE, new TypeHandler());
        CollectionType collectionType = (CollectionType) javaType;
        assertNotNull(collectionType);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(TypeSerializable.INSTANCE, collectionType.getTypeBindings());
        assertEquals(java.util.List.class, collectionType.getRawClass());
        assertEquals(Integer.TYPE, collectionType.getContentType());
        assertNotNull(collectionType.getTypeHandler());
    }

    @Test
    public void testConstructWithContentTypeHandler() {
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(ClassLoader.getSystemClassLoader(), List.class, Integer.TYPE, new ContentTypeHandler());
        CollectionType collectionType = (CollectionType) javaType;
        assertNotNull(collectionType);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(TypeSerializable.INSTANCE, collectionType.getTypeBindings());
        assertEquals(java.util.List.class, collectionType.getRawClass());
        assertEquals(Integer.TYPE, collectionType.getContentType());
        assertNotNull(collectionType.getContentTypeHandler());
    }

    @Test
    public void testConstructWithValueHandlerAndTypeHandler() {
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(ClassLoader.getSystemClassLoader(), List.class, Integer.TYPE, NumberValueHandler.class, new TypeHandler());
        CollectionType collectionType = (CollectionType) javaType;
        assertNotNull(collectionType);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(TypeSerializable.INSTANCE, collectionType.getTypeBindings());
        assertEquals(java.util.List.class, collectionType.getRawClass());
        assertEquals(Integer.TYPE, collectionType.getContentType());
        assertNotNull(collectionType.getValueHandler());
        assertNotNull(collectionType.getTypeHandler());
    }

    @Test
    public void testConstructWithContentTypeHandlerAndTypeHandler() {
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(ClassLoader.getSystemClassLoader(), List.class, Integer.TYPE, new ContentTypeHandler(), new TypeHandler());
        CollectionType collectionType = (CollectionType) javaType;
        assertNotNull(collectionType);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(TypeSerializable.INSTANCE, collectionType.getTypeBindings());
        assertEquals(java.util.List.class, collectionType.getRawClass());
        assertEquals(Integer.TYPE, collectionType.getContentType());
        assertNotNull(collectionType.getContentTypeHandler());
        assertNotNull(collectionType.getTypeHandler());
    }

    @Test
    public void testConstructWithStaticTyping() {
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(ClassLoader.getSystemClassLoader(), List.class, Integer.TYPE, new TypeHandler(), TypeSerializable.Static.INSTANCE);
        CollectionType collectionType = (CollectionType) javaType;
        assertNotNull(collectionType);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(TypeSerializable.INSTANCE, collectionType.getTypeBindings());
        assertEquals(java.util.List.class, collectionType.getRawClass());
        assertEquals(Integer.TYPE, collectionType.getContentType());
        assertNotNull(collectionType.getValueHandler());
        assertNotNull(collectionType.getTypeHandler());
        assertTrue(collectionType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(ClassLoader.getSystemClassLoader(), ArrayList.class, Integer.TYPE);
        CollectionType collectionType = (CollectionType) javaType;
        CollectionType refinedType = collectionType.refine(LinkedList.class, TypeBindings.empty(), null, null);
        assertNotNull(refinedType);
        assertEquals(LinkedList.class, refinedType.getRawClass());
        assertEquals(TypeSerializable.INSTANCE, refinedType.getTypeBindings());
        assertEquals(java.util.List.class, refinedType.getRawClass());
        assertEquals(Integer.TYPE, refinedType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidClass() {
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(ClassLoader.getSystemClassLoader(), String.class);
        CollectionType collectionType = (CollectionType) javaType;
        assertNotNull(collectionType);
        // This should fail as String is not a Collection type
        collectionType.withContentType(Integer.TYPE);
    }
}