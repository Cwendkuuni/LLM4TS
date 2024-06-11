package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() throws Exception {
        JavaType javaType = CollectionLikeType.construct(java.util.List.class, TypeBindings.emptyBindings(), null, null, JavaType.construct(String.class));
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        assertEquals(java.util.List.class, collectionLikeType.getRawClass());
        assertEquals(String.class, collectionLikeType.getContentType().getRawClass());
    }

    @Test
    public void testConstructWithBindings() throws Exception {
        JavaType javaType = CollectionLikeType.construct(List.class, TypeBindings.defaultBindings(), null, null, JavaType.construct(String.class));
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        assertEquals(List.class, collectionLikeType.getRawClass());
        assertEquals(String.class, collectionLikeType.getContentType().getRawClass());
    }

    @Test
    public void testConstructWithTypeVariable() throws Exception {
        JavaType javaType = CollectionLikeType.construct(List.class, null, null, null, JavaType.construct(String.class));
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        assertEquals(List.class, collectionLikeType.getRawClass());
        assertEquals(String.class, collectionLikeType.getContentType().getRawClass());
    }

    @Test
    public void testUpgradeFrom() throws Exception {
        JavaType javaType = JavaType.construct(ArrayList.class);
        JavaType upgradedType = CollectionLikeType.upgradeFrom(javaType, JavaType.construct(String.class));
        CollectionLikeType collectionLikeType = (CollectionLikeType) upgradedType;
        assertEquals(ArrayList.class, collectionLikeType.getRawClass());
        assertEquals(String.class, collectionLikeType.getContentType().getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithInvalidBaseType() throws Exception {
        JavaType javaType = JavaType.construct(Object.class);
        CollectionLikeType.upgradeFrom(javaType, JavaType.construct(String.class));
    }

    @Test
    public void testGetContentType() throws Exception {
        JavaType javaType = CollectionLikeType.construct(List.class, TypeBindings.defaultBindings(), null, null, JavaType.construct(String.class));
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        assertEquals(String.class, collectionLikeType.getContentType().getRawClass());
    }

    @Test
    public void testIsCollectionLikeType() throws Exception {
        JavaType javaType = CollectionLikeType.construct(List.class, TypeBindings.defaultBindings(), null, null, JavaType.construct(String.class));
        assertTrue(javaType.isCollectionLikeType());
    }

    @Test
    public void testIsTrueCollectionType() throws Exception {
        JavaType javaType = CollectionLikeType.construct(List.class, TypeBindings.defaultBindings(), null, null, JavaType.construct(String.class));
        assertTrue(javaType.isTrueCollectionType());

        JavaType javaType2 = JavaType.construct(ArrayList.class);
        assertFalse(javaType2.isTrueCollectionType());
    }

    @Test
    public void testRefine() throws Exception {
        JavaType javaType = CollectionLikeType.construct(List.class, TypeBindings.defaultBindings(), null, null, JavaType.construct(String.class));
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        JavaType refinedType = collectionLikeType.refine(ArrayList.class, TypeBindings.defaultBindings(), null, null);
        CollectionLikeType refinedCollectionLikeType = (CollectionLikeType) refinedType;
        assertEquals(ArrayList.class, refinedCollectionLikeType.getRawClass());
        assertEquals(String.class, refinedCollectionLikeType.getContentType().getRawClass());
    }

    @Test
    public void testNarrow() throws Exception {
        JavaType javaType = CollectionLikeType.construct(List.class, TypeBindings.defaultBindings(), null, null, JavaType.construct(String.class));
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        JavaType narrowedType = collectionLikeType.narrow(LinkedList.class);
        assertEquals(LinkedList.class, narrowedType.getRawClass());
        assertEquals(String.class, narrowedType.getContentType().getRawClass());
    }

    @Test
    public void testWithContentType() throws Exception {
        JavaType javaType = CollectionLikeType.construct(List.class, TypeBindings.defaultBindings(), null, null, JavaType.construct(String.class));
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        JavaType withContentType = collectionLikeType.withContentType(JavaType.construct(Integer.class));
        assertEquals(List.class, withContentType.getRawClass());
        assertEquals(Integer.class, withContentType.getContentType().getRawClass());
    }

    @Test
    public void testWithContentTypeHandler() throws Exception {
        JavaType javaType = CollectionLikeType.construct(List.class, TypeBindings.defaultBindings(), null, null, JavaType.construct(String.class));
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        Object stringHandler = new StringHandler();
        JavaType withContentTypeHandler = collectionLikeType.withContentTypeHandler(stringHandler);
        assertEquals(List.class, withContentTypeHandler.getRawClass());
        assertEquals(String.class, withContentTypeHandler.getContentType().getRawClass());
        assertEquals(stringHandler, withContentTypeHandler.getContentHandler());
    }

    @Test
    public void testWithTypeHandler() throws Exception {
        JavaType javaType = CollectionLikeType.construct(List.class, TypeBindings.defaultBindings(), null, null, JavaType.construct(String.class));
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        Object stringHandler = new StringHandler();
        JavaType withTypeHandler = collectionLikeType.withTypeHandler(stringHandler);
        assertEquals(List.class, withTypeHandler.getRawClass());
        assertEquals(String.class, withTypeHandler.getContentType().getRawClass());
        assertEquals(stringHandler, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithContentValueHandler() throws Exception {
        JavaType javaType = CollectionLikeType.construct(List.class, TypeBindings.defaultBindings(), null, null, JavaType.construct(String.class));
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        Object stringHandler = new StringHandler();
        JavaType withContentValueHandler = collectionLikeType.withContentValueHandler(stringHandler);
        assertEquals(List.class, withContentValueHandler.getRawClass());
        assertEquals(String.class, withContentValueHandler.getContentType().getRawClass());
        assertEquals(stringHandler, withContentValueHandler.getContentValueHandler());
    }

    @Test
    public void testWithValueHandler() throws Exception {
        JavaType javaType = CollectionLikeType.construct(List.class, TypeBindings.defaultBindings(), null, null, JavaType.construct(String.class));
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        Object stringHandler = new StringHandler();
        JavaType withValueHandler = collectionLikeType.withValueHandler(stringHandler);
        assertEquals(List.class, withValueHandler.getRawClass());
        assertEquals(String.class, withValueHandler.getContentType().getRawClass());
        assertEquals(stringHandler, withValueHandler.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() throws Exception {
        JavaType javaType = CollectionLikeType.construct(List.class, TypeBindings.defaultBindings(), null, null, JavaType.construct(String.class));
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        JavaType withStaticTyping = collectionLikeType.withStaticTyping();
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testEquals() throws Exception {
        JavaType javaType1 = CollectionLikeType.construct(List.class, TypeBindings.defaultBindings(), null, null, JavaType.construct(String.class));
        JavaType javaType2 = CollectionLikeType.construct(List.class, TypeBindings.defaultBindings(), null, null, JavaType.construct(String.class));
        JavaType javaType3 = CollectionLikeType.construct(ArrayList.class, TypeBindings.defaultBindings(), null, null, JavaType.construct(String.class));

        assertTrue(javaType1.equals(javaType1));
        assertTrue(javaType1.equals(javaType2));
        assertFalse(javaType1.equals(javaType3));
        assertFalse(javaType1.equals(null));
    }

    @Test
    public void testToString() throws Exception {
        JavaType javaType = CollectionLikeType.construct(List.class, TypeBindings.defaultBindings(), null, null, JavaType.construct(String.class));
        assertEquals("collection-like type; class java.util.List, contains class java.lang.String", javaType.toString());
    }

    @Test
    public void testGetErasedSignature() throws Exception {
        JavaType javaType = CollectionLikeType.construct(List.class, TypeBindings.defaultBindings(), null, null, JavaType.construct(String.class));
        assertEquals("java.util.List<T>", javaType.getErasedSignature().toString());
    }

    @Test
    public void testGetGenericSignature() throws Exception {
        JavaType javaType = CollectionLikeType.construct(List.class, TypeBindings.defaultBindings(), null, null, JavaType.construct(String.class));
        assertEquals("java.util.List<T>", javaType.getGenericSignature().toString());
    }

    @Test
    public void testBuildCanonicalName() throws Exception {
        JavaType javaType = CollectionLikeType.construct(List.class, TypeBindings.defaultBindings(), null, null, JavaType.construct(String.class));
        assertEquals("java.util.List<java.lang.String>", javaType.getCanonicalName());
    }

    @Test
    public void testHashCode() throws Exception {
        JavaType javaType1 = CollectionLikeType.construct(List.class, TypeBindings.defaultBindings(), null, null, JavaType.construct(String.class));
        JavaType javaType2 = CollectionLikeType.construct(List.class, TypeBindings.defaultBindings(), null, null, JavaType.construct(String.class));
        JavaType javaType3 = CollectionLikeType.construct(ArrayList.class, TypeBindings.defaultBindings(), null, null, JavaType.construct(String.class));

        assertEquals(javaType1.hashCode(), javaType1.hashCode());
        assertEquals(javaType1.hashCode(), javaType2.hashCode());
        assertNotEquals(javaType1.hashCode(), javaType3.hashCode());
    }

    @Test
    public void testIsContainerType() throws Exception {
        JavaType javaType = CollectionLikeType.construct(List.class, TypeBindings.defaultBindings(), null, null, JavaType.construct(String.class));
        assertTrue(javaType.isContainerType());
    }

    @Test
    public void testHasHandlers() throws Exception {
        JavaType javaType = CollectionLikeType.construct(List.class, TypeBindings.defaultBindings(), null, null, JavaType.construct(String.class));
        assertTrue(javaType.hasHandlers());
    }

    private static class StringHandler {}

    private static class List<T> extends AbstractList<T> implements Collection<T> {}

    private static class ArrayList<T> extends AbstractList<T> implements Collection<T> {}

}