package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    @Test
    public void testCollectionLikeTypeCreation() {
        Class<Collection<String>> collectionClass = Collection.class;
        JavaType javaType = CollectionLikeType.construct(collectionClass, null, null, null, JavaType.constructRawListType(String.class));
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;

        assertEquals(collectionClass, collectionLikeType.getRawClass());
        assertEquals(JavaType.constructRawListType(String.class), collectionLikeType.getContentType());
    }

    @Test
    public void testCollectionLikeTypeUpgrade() {
        Class<List<String>> listClass = List.class;
        JavaType javaType = JavaType.constructRawType(listClass);
        JavaType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, JavaType.constructRawType(String.class));

        assertEquals(listClass, collectionLikeType.getRawClass());
        assertEquals(JavaType.constructRawListType(String.class), collectionLikeType.getContentType());
    }

    @Test
    public void testCollectionLikeTypeIsCollectionLikeType() {
        Class<Collection<String>> collectionClass = Collection.class;
        JavaType javaType = CollectionLikeType.construct(collectionClass, null, null, null, JavaType.constructRawListType(String.class));

        assertTrue(javaType.isCollectionLikeType());
        assertTrue(javaType.isContainerType());
    }

    @Test
    public void testCollectionLikeTypeIsTrueCollectionType() {
        Class<Collection<String>> collectionClass = Collection.class;
        JavaType javaType = CollectionLikeType.construct(collectionClass, null, null, null, JavaType.constructRawListType(String.class));

        assertTrue(javaType.isTrueCollectionType());
    }

    @Test
    public void testCollectionLikeTypeHasHandlers() {
        Class<Collection<String>> collectionClass = Collection.class;
        JavaType javaType = CollectionLikeType.construct(collectionClass, null, null, null, JavaType.constructRawListType(String.class));

        assertTrue(javaType.hasHandlers());
        assertTrue(javaType.getContentType().hasHandlers());
    }

    @Test
    public void testCollectionLikeTypeGetCanonicalName() {
        Class<Collection<String>> collectionClass = Collection.class;
        JavaType javaType = CollectionLikeType.construct(collectionClass, null, null, null, JavaType.constructRawListType(String.class));

        assertEquals("java.util.Collection<java.lang.String>", javaType.getCanonicalName());
    }

    @Test
    public void testCollectionLikeTypeGetErasedSignature() {
        Class<Collection<String>> collectionClass = Collection.class;
        JavaType javaType = CollectionLikeType.construct(collectionClass, null, null, null, JavaType.constructRawListType(String.class));

        assertEquals("java.util.Collection", javaType.getErasedSignature().toString());
    }

    @Test
    public void testCollectionLikeTypeGetGenericSignature() {
        Class<Collection<String>> collectionClass = Collection.class;
        JavaType javaType = CollectionLikeType.construct(collectionClass, null, null, null, JavaType.constructRawListType(String.class));

        assertEquals("java.util.Collection<java.lang.String>", javaType.getGenericSignature().toString());
    }
}