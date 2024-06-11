package com.example.junit;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        JavaType javaType = simpleType.getType();
        assertEquals(String.class, javaType.getRawClass());
    }

    @Test
    public void testSimpleTypeWithBindings() {
        Class<List<String>> listClass = List.class.getGenericType();
        JavaType javaType = new SimpleType(listClass).withoutRaw().getType();
        assertEquals(List.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getContentType().getRawClass());
    }

    @Test
    public void testSimpleTypeWithSuperClass() {
        Class<?> superClass = Object[].class;
        SimpleType simpleType = new SimpleType(String.class, null, new SimpleType(superClass));
        JavaType javaType = simpleType.getType();
        assertEquals(String.class, javaType.getRawClass());
        assertEquals(Object[].class, javaType.getSuperClass());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType originalSimpleType = new SimpleType(stringClass);
        SimpleType copiedSimpleType = new SimpleType(originalSimpleType);
        assertEquals(originalSimpleType, copiedSimpleType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithNullClass() {
        SimpleType.constructUnsafe(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidClass() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidSubclass() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(int.class);
        simpleType.narrow(stringClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeWithSimpleType() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentType(new SimpleType(int.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithTypeHandlerWithNullObject() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withTypeHandler(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithValueHandlerWithNullObject() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withValueHandler(null);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType javaType1 = simpleType.withStaticTyping().getType();
        JavaType javaType2 = simpleType.getType();
        assertFalse(javaType1.isStatic());
        assertTrue(javaType2.isStatic());
        assertEquals(javaType1, javaType2.asInstanceable());
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(List.class);
        assertEquals("java.util.List<java.lang.Object>", simpleType.buildCanonicalName());

        SimpleType simpleTypeWithElementType = new SimpleType(List.class).with(Integer.TYPE);
        assertEquals("java.util.List<java.lang.Integer>", simpleTypeWithElementType.buildCanonicalName());
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(List.class);
        StringBuilder expected = new StringBuilder("java.util.List");
        StringBuilder actual = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(List.class);
        StringBuilder expected = new StringBuilder("java.util.List<T>");
        StringBuilder actual = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(List.class);
        StringBuilder expected = new StringBuilder("[simple type, class java.util.List]");
        StringBuilder actual = new StringBuilder(simpleType.toString());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(Integer.TYPE);
        SimpleType simpleType2 = new SimpleType(Integer.TYPE);
        SimpleType simpleType3 = new SimpleType(String.class);
        SimpleType simpleType4 = new SimpleType(Integer.TYPE);

        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType4));
        assertFalse(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }
}