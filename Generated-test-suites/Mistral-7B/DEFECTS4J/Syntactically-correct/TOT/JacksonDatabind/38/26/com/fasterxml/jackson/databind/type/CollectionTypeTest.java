package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test for {@link CollectionType} class.
 */
public class CollectionTypeTest {

    /**
     * Test construct method with valid arguments.
     */
    @Test
    public void testConstruct() {
        Class<List<Integer>> listClass = List.class;
        JavaType javaType = CollectionType.construct(listClass, null,
                Object.class, new TypeReference[0], Integer.TYPE);
        CollectionType collectionType = (CollectionType) javaType;
        assertNotNull(collectionType);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(Integer.TYPE, collectionType.getContentType());
    }

    /**
     * Test construct deprecated method with valid arguments.
     */
    @Test
    public void testConstructDeprecated() {
        Class<List<Integer>> listClass = List.class;
        JavaType javaType = CollectionType.construct(listClass, null);
        CollectionType collectionType = (CollectionType) javaType;
        assertNotNull(collectionType);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(Integer.TYPE, collectionType.getContentType());
    }

    /**
     * Test withContentType method with valid arguments.
     */
    @Test
    public void testWithContentType() {
        Class<List<String>> listClass = List.class;
        JavaType collectionType = CollectionType.construct(listClass, null,
                Object.class, new TypeReference[0], String.class);
        JavaType javaType = collectionType.withContentType(new TypeReference<List<Integer>>() {});
        CollectionType newCollectionType = (CollectionType) javaType;
        assertEquals(List.class, newCollectionType.getRawClass());
        assertEquals(Integer.TYPE, newCollectionType.getContentType());
    }

    /**
     * Test withTypeHandler method with valid arguments.
     */
    @Test
    public void testWithTypeHandler() {
        Class<List<Integer>> listClass = List.class;
        JavaType collectionType = CollectionType.construct(listClass, null,
                Object.class, new TypeReference[0], Integer.TYPE);
        Object handler = new Object();
        JavaType javaType = collectionType.withTypeHandler(handler);
        CollectionType newCollectionType = (CollectionType) javaType;
        assertEquals(List.class, newCollectionType.getRawClass());
        assertEquals(Integer.TYPE, newCollectionType.getContentType());
        assertEquals(handler, newCollectionType.getTypeHandler());
    }

    /**
     * Test withContentTypeHandler method with valid arguments.
     */
    @Test
    public void testWithContentTypeHandler() {
        Class<List<Integer>> listClass = List.class;
        JavaType collectionType = CollectionType.construct(listClass, null,
                Object.class, new TypeReference[0], Integer.TYPE);
        Object handler = new Object();
        JavaType javaType = collectionType.withContentTypeHandler(handler);
        CollectionType newCollectionType = (CollectionType) javaType;
        assertEquals(List.class, newCollectionType.getRawClass());
        assertEquals(Integer.TYPE, newCollectionType.getContentType());
        assertEquals(handler, newCollectionType.getContentTypeHandler());
    }

    /**
     * Test withValueHandler method with valid arguments.
     */
    @Test
    public void testWithValueHandler() {
        Class<List<Integer>> listClass = List.class;
        JavaType collectionType = CollectionType.construct(listClass, null,
                Object.class, new TypeReference[0], Integer.TYPE);
        Object handler = new Object();
        JavaType javaType = collectionType.withValueHandler(handler);
        CollectionType newCollectionType = (CollectionType) javaType;
        assertEquals(List.class, newCollectionType.getRawClass());
        assertEquals(Integer.TYPE, newCollectionType.getContentType());
        assertEquals(handler, newCollectionType.getValueHandler());
    }

    /**
     * Test withContentValueHandler method with valid arguments.
     */
    @Test
    public void testWithContentValueHandler() {
        Class<List<Integer>> listClass = List.class;
        JavaType collectionType = CollectionType.construct(listClass, null,
                Object.class, new TypeReference[0], Integer.TYPE);
        Object handler = new Object();
        JavaType javaType = collectionType.withContentValueHandler(handler);
        CollectionType newCollectionType = (CollectionType) javaType;
        assertEquals(List.class, newCollectionType.getRawClass());
        assertEquals(Integer.TYPE, newCollectionType.getContentType());
        assertEquals(handler, newCollectionType.getContentValueHandler());
    }

    /**
     * Test withStaticTyping method with valid arguments.
     */
    @Test
    public void testWithStaticTyping() {
        Class<List<Integer>> listClass = List.class;
        JavaType collectionType = CollectionType.construct(listClass, null,
                Object.class, new TypeReference[0], Integer.TYPE);
        JavaType javaType = collectionType.withStaticTyping();
        CollectionType newCollectionType = (CollectionType) javaType;
        assertEquals(List.class, newCollectionType.getRawClass());
        assertEquals(Integer.TYPE, newCollectionType.getContentType());
        assertTrue(newCollectionType.isStaticTyping());
    }

    /**
     * Test refine method with valid arguments.
     */
    @Test
    public void testRefine() {
        Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
        JavaType collectionType = CollectionType.construct(List.class, null,
                Object.class, new TypeReference[0], Integer.TYPE);
        JavaType javaType = collectionType.refine(arrayListClass.getTypeParameters()[0].getType(), null,
                Object.class, new TypeReference[0]);
        CollectionType newCollectionType = (CollectionType) javaType;
        assertEquals(ArrayList.class, newCollectionType.getRawClass());
        assertEquals(Integer.TYPE, newCollectionType.getContentType());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        Class<List<Integer>> listClass = List.class;
        JavaType collectionType = CollectionType.construct(listClass, null,
                Object.class, new TypeReference[0], Integer.TYPE);
        String result = collectionType.toString();
        String expected = "[collection type; class java.util.List, contains class java.lang.Integer]";
        assertEquals(expected, result);
    }
}