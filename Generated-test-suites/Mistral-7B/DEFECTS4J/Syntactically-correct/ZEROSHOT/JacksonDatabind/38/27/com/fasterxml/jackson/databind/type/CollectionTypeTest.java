package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<List<Integer>> listIntType = new ArrayList<Integer>().getClass();
        JavaType javaListIntType = CollectionType.construct(listIntType, null, null, null, JavaType.getType(Integer.class));
        assertEquals(List.class, javaListIntType.getRawClass());
        assertEquals(JavaType.getType(List.class), javaListIntType.getAndroidId());
        assertNull(javaListIntType.getSuperClass());
        assertArrayEquals(new JavaType[0].getClass(), javaListIntType.getSuperTypes());
        assertEquals(JavaType.getType(Integer.class), javaListIntType.getContentType());
        assertEquals(CollectionLikeType.class, javaListIntType.getBaseType());
        assertNull(javaListIntType.getValueHandler());
        assertNull(javaListIntType.getTypeHandler());
        assertFalse(javaListIntType.isStatic());
    }

    @Test
    public void testConstructDeprecated() {
        Class<List<Integer>> listIntType = new ArrayList<Integer>().getClass();
        JavaType javaListIntType = CollectionType.construct(listIntType, JavaType.getType(Integer.class));
        assertEquals(List.class, javaListIntType.getRawClass());
        assertEquals(JavaType.getType(List.class), javaListIntType.getAndroidId());
        assertNull(javaListIntType.getSuperClass());
        assertArrayEquals(new JavaType[0].getClass(), javaListIntType.getSuperTypes());
        assertEquals(JavaType.getType(Integer.class), javaListIntType.getContentType());
        assertEquals(CollectionLikeType.class, javaListIntType.getBaseType());
        assertNull(javaListIntType.getValueHandler());
        assertNull(javaListIntType.getTypeHandler());
        assertFalse(javaListIntType.isStatic());
    }

    @Test
    public void testNarrow() {
        Class<ArrayList<Integer>> arrayListIntType = new ArrayList<Integer>().getClass();
        JavaType javaListIntType = CollectionType.construct(ArrayList.class, null, null, null, JavaType.getType(Integer.class));
        JavaType javaArrayListIntType = javaListIntType.narrow(ArrayList.class);
        assertEquals(ArrayList.class, javaArrayListIntType.getRawClass());
        assertEquals(JavaType.getType(ArrayList.class), javaArrayListIntType.getAndroidId());
        assertNull(javaArrayListIntType.getSuperClass());
        assertArrayEquals(new JavaType[0].getClass(), javaArrayListIntType.getSuperTypes());
        assertEquals(JavaType.getType(Integer.class), javaArrayListIntType.getContentType());
        assertEquals(CollectionLikeType.class, javaArrayListIntType.getBaseType());
        assertNull(javaArrayListIntType.getValueHandler());
        assertNull(javaArrayListIntType.getTypeHandler());
        assertFalse(javaArrayListIntType.isStatic());
    }

    @Test
    public void testWithContentType() {
        Class<List<Integer>> listIntType = new ArrayList<Integer>().getClass();
        JavaType javaListIntType = CollectionType.construct(listIntType, null, null, null, JavaType.getType(Integer.class));
        JavaType javaListStringType = javaListIntType.withContentType(JavaType.getType(String.class));
        assertEquals(List.class, javaListStringType.getRawClass());
        assertEquals(JavaType.getType(List.class), javaListStringType.getAndroidId());
        assertNull(javaListStringType.getSuperClass());
        assertArrayEquals(new JavaType[0].getClass(), javaListStringType.getSuperTypes());
        assertEquals(JavaType.getType(String.class), javaListStringType.getContentType());
        assertEquals(CollectionLikeType.class, javaListStringType.getBaseType());
        assertNull(javaListStringType.getValueHandler());
        assertNull(javaListStringType.getTypeHandler());
        assertFalse(javaListStringType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Class<List<Integer>> listIntType = new ArrayList<Integer>().getClass();
        JavaType javaListIntType = CollectionType.construct(listIntType, null, null, null, JavaType.getType(Integer.class));
        JavaType javaListIntTypeWithHandler = javaListIntType.withTypeHandler(new Object(){});
        assertEquals(List.class, javaListIntTypeWithHandler.getRawClass());
        assertEquals(JavaType.getType(List.class), javaListIntTypeWithHandler.getAndroidId());
        assertNull(javaListIntTypeWithHandler.getSuperClass());
        assertArrayEquals(new JavaType[0].getClass(), javaListIntTypeWithHandler.getSuperTypes());
        assertEquals(JavaType.getType(Integer.class), javaListIntTypeWithHandler.getContentType());
        assertEquals(CollectionLikeType.class, javaListIntTypeWithHandler.getBaseType());
        assertNotNull(javaListIntTypeWithHandler.getTypeHandler());
        assertNull(javaListIntTypeWithHandler.getValueHandler());
        assertFalse(javaListIntTypeWithHandler.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<List<Integer>> listIntType = new ArrayList<Integer>().getClass();
        JavaType javaListIntType = CollectionType.construct(listIntType, null, null, null, JavaType.getType(Integer.class));
        JavaType javaListIntTypeWithContentHandler = javaListIntType.withContentTypeHandler(new Object(){});
        assertEquals(List.class, javaListIntTypeWithContentHandler.getRawClass());
        assertEquals(JavaType.getType(List.class), javaListIntTypeWithContentHandler.getAndroidId());
        assertNull(javaListIntTypeWithContentHandler.getSuperClass());
        assertArrayEquals(new JavaType[0].getClass(), javaListIntTypeWithContentHandler.getSuperTypes());
        assertEquals(JavaType.getType(Integer.class), javaListIntTypeWithContentHandler.getContentType());
        assertEquals(CollectionLikeType.class, javaListIntTypeWithContentHandler.getBaseType());
        assertNull(javaListIntTypeWithContentHandler.getValueHandler());
        assertNotNull(javaListIntTypeWithContentHandler.getTypeHandler());
        assertFalse(javaListIntTypeWithContentHandler.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        Class<List<Integer>> listIntType = new ArrayList<Integer>().getClass();
        JavaType javaListIntType = CollectionType.construct(listIntType, null, null, null, JavaType.getType(Integer.class));
        JavaType javaListIntTypeWithValueHandler = javaListIntType.withValueHandler(new Object(){});
        assertEquals(List.class, javaListIntTypeWithValueHandler.getRawClass());
        assertEquals(JavaType.getType(List.class), javaListIntTypeWithValueHandler.getAndroidId());
        assertNull(javaListIntTypeWithValueHandler.getSuperClass());
        assertArrayEquals(new JavaType[0].getClass(), javaListIntTypeWithValueHandler.getSuperTypes());
        assertEquals(JavaType.getType(Integer.class), javaListIntTypeWithValueHandler.getContentType());
        assertEquals(CollectionLikeType.class, javaListIntTypeWithValueHandler.getBaseType());
        assertNotNull(javaListIntTypeWithValueHandler.getValueHandler());
        assertNull(javaListIntTypeWithValueHandler.getTypeHandler());
        assertFalse(javaListIntTypeWithValueHandler.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<List<Integer>> listIntType = new ArrayList<Integer>().getClass();
        JavaType javaListIntType = CollectionType.construct(listIntType, null, null, null, JavaType.getType(Integer.class));
        JavaType javaListIntTypeWithContentValueHandler = javaListIntType.withContentValueHandler(new Object(){});
        assertEquals(List.class, javaListIntTypeWithContentValueHandler.getRawClass());
        assertEquals(JavaType.getType(List.class), javaListIntTypeWithContentValueHandler.getAndroidId());
        assertNull(javaListIntTypeWithContentValueHandler.getSuperClass());
        assertArrayEquals(new JavaType[0].getClass(), javaListIntTypeWithContentValueHandler.getSuperTypes());
        assertEquals(JavaType.getType(Integer.class), javaListIntTypeWithContentValueHandler.getContentType());
        assertEquals(CollectionLikeType.class, javaListIntTypeWithContentValueHandler.getBaseType());
        assertNull(javaListIntTypeWithContentValueHandler.getValueHandler());
        assertNotNull(javaListIntTypeWithContentValueHandler.getTypeHandler());
        assertFalse(javaListIntTypeWithContentValueHandler.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        Class<List<Integer>> listIntType = new ArrayList<Integer>().getClass();
        JavaType javaListIntType = CollectionType.construct(listIntType, null, null, null, JavaType.getType(Integer.class));
        JavaType javaListIntTypeStatic = javaListIntType.withStaticTyping();
        assertEquals(List.class, javaListIntTypeStatic.getRawClass());
        assertEquals(JavaType.getType(List.class), javaListIntTypeStatic.getAndroidId());
        assertNull(javaListIntTypeStatic.getSuperClass());
        assertArrayEquals(new JavaType[0].getClass(), javaListIntTypeStatic.getSuperTypes());
        assertEquals(JavaType.getType(Integer.class), javaListIntTypeStatic.getContentType());
        assertEquals(CollectionLikeType.class, javaListIntTypeStatic.getBaseType());
        assertNull(javaListIntTypeStatic.getValueHandler());
        assertNull(javaListIntTypeStatic.getTypeHandler());
        assertTrue(javaListIntTypeStatic.isStatic());
    }

    @Test
    public void testRefine() {
        Class<ArrayList<Integer>> arrayListIntType = new ArrayList<Integer>().getClass();
        JavaType javaArrayListIntType = CollectionType.construct(ArrayList.class, null, null, null, JavaType.getType(Integer.class));
        JavaType javaArrayListListIntType = javaArrayListIntType.refine(ArrayList.class, null, null, null, JavaType.getType(List.class));
        assertEquals(ArrayList.class, javaArrayListListIntType.getRawClass());
        assertEquals(JavaType.getType(ArrayList.class), javaArrayListListIntType.getAndroidId());
        assertNull(javaArrayListListIntType.getSuperClass());
        assertArrayEquals(new JavaType[0].getClass(), javaArrayListListIntType.getSuperTypes());
        assertEquals(JavaType.getType(List.class), javaArrayListListIntType.getContentType());
        assertEquals(CollectionLikeType.class, javaArrayListListIntType.getBaseType());
        assertNull(javaArrayListListIntType.getValueHandler());
        assertNull(javaArrayListListIntType.getTypeHandler());
        assertFalse(javaArrayListListIntType.isStatic());
    }
}