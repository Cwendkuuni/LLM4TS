package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        JavaType javaType = simpleType.getRawClass();
        assertEquals(cls, javaType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        Class<?> cls = HashMap.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        Class<?> cls = ArrayList.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        Class<?> cls = int[].class;
        SimpleType.construct(cls);
    }

    @Test
    public void testNarrow() {
        Class<?> cls = String.class;
        Class<?> subclass = Object.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        JavaType narrow = simpleType.narrow(subclass);
        assertNull(narrow);
    }

    @Test
    public void testNarrowWithSubclass() {
        Class<?> cls = String.class;
        Class<?> subclass = Object.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        SimpleType narrow = simpleType.narrow(String.class);
        assertEquals(simpleType.getClass(), narrow.getClass());
        assertEquals(simpleType.getRawClass(), narrow.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithNonSubclass() {
        Class<?> cls = String.class;
        Class<?> nonSubclass = Integer.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        simpleType.narrow(nonSubclass);
    }

    @Test
    public void testWithContentType() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        JavaType javaType = JavaType.constructCollectionType(ArrayList.class, SimpleType.construct(int.class));
        SimpleType withContentType = simpleType.withContentType(javaType);
        assertNotSame(simpleType, withContentType);
        assertEquals(javaType.getClass(), withContentType.getContentType().getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeWithInvalidType() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        JavaType invalidType = new SimpleType(int.class, null, null, null, null, null, false);
        simpleType.withContentType(invalidType);
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> cls = String.class;
        Object handler = new Object() {};
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        SimpleType withTypeHandler = simpleType.withTypeHandler(handler);
        assertNotSame(simpleType, withTypeHandler);
        assertEquals(handler, withTypeHandler.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidTypeHandler() {
        Class<?> cls = String.class;
        Object invalidHandler = new Object() {};
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        simpleType.withTypeHandler(invalidHandler);
    }

    @Test
    public void testWithValueHandler() {
        Class<?> cls = String.class;
        Object handler = new Object() {};
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        SimpleType withValueHandler = simpleType.withValueHandler(handler);
        assertNotSame(simpleType, withValueHandler);
        assertEquals(handler, withValueHandler.getValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidValueHandler() {
        Class<?> cls = String.class;
        Object invalidHandler = new Object() {};
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        simpleType.withValueHandler(invalidHandler);
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertNotSame(simpleType, withStaticTyping);
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        JavaType refine = simpleType.refine(String[].class, null, null, null);
        assertNull(refine);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = HashMap.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        String canonicalName = simpleType.getCanonicalName();
        String expectedCanonicalName = "java.util.Map<?,?>";
        assertEquals(expectedCanonicalName, canonicalName);
    }

    @Test
    public void testIsContainerType() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = HashMap.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        StringBuilder expectedSignature = new StringBuilder("java.util.Map<K,V>");
        StringBuilder actualSignature = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expectedSignature.toString(), actualSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = HashMap.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        StringBuilder expectedSignature = new StringBuilder("java.util.Map<K,V>");
        StringBuilder actualSignature = new StringBuilder();
        simpleType.getGenericSignature(actualSignature);
        assertEquals(expectedSignature.toString(), actualSignature.toString());
    }

    @Test
    public void testToString() {
        Class<?> cls = HashMap.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        String expectedString = "[simple type, class java.util.Map]";
        String actualString = simpleType.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testEquals() {
        Class<?> cls = HashMap.class;
        SimpleType simpleType1 = new SimpleType(cls, null, null, null, null, null, false);
        SimpleType simpleType2 = new SimpleType(cls, null, null, null, null, null, false);
        assertTrue(simpleType1.equals(simpleType2));
    }

    @Test
    public void testHashCode() {
        Class<?> cls = HashMap.class;
        SimpleType simpleType1 = new SimpleType(cls, null, null, null, null, null, false);
        SimpleType simpleType2 = new SimpleType(cls, null, null, null, null, null, false);
        int expectedHashcode = simpleType1.hashCode();
        int actualHashcode = simpleType2.hashCode();
        assertEquals(expectedHashcode, actualHashcode);
    }
}