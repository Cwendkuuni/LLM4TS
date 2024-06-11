package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        // Test simple type construction with a class
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(simpleType.getClass(), SimpleType.class);

        // Test copy constructor
        SimpleType simpleType2 = new SimpleType(simpleType);
        assertEquals(simpleType, simpleType2);

        // Test constructor with bindings, superclass, and interfaces
        JavaType superClass = null;
        JavaType[] superInterfaces = new JavaType[0];
        SimpleType simpleType3 = new SimpleType(String.class, TypeBindings.emptyBindings(), superClass, superInterfaces);
        assertEquals(String.class, simpleType3.getRawClass());
        assertEquals(simpleType3.getClass(), SimpleType.class);
        assertNull(simpleType3.getSuperClass());
        assertNull(simpleType3.getSuperInterfaces());

        // Test constructor with valueHandler and typeHandler
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        SimpleType simpleType4 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, valueHandler, typeHandler, false);
        assertEquals(String.class, simpleType4.getRawClass());
        assertEquals(simpleType4.getClass(), SimpleType.class);
        assertEquals(valueHandler, simpleType4.getValueHandler());
        assertEquals(typeHandler, simpleType4.getTypeHandler());
        assertFalse(simpleType4.isStatic());

        // Test constructor with asStatic flag
        SimpleType simpleType5 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, true);
        assertTrue(simpleType5.isStatic());
    }

    @Test
    public void testConstructUnsafe() {
        // Test constructUnsafe method
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(stringClass);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(simpleType.getClass(), SimpleType.class);
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructIllegalArgumentException() {
        // Test construct method with an array class
        Class<int[]> intArrayClass = int[].class;
        SimpleType simpleType = new SimpleType(intArrayClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMapIllegalArgumentException() {
        // Test construct method with a Map class
        Class<Map<String, Integer>> mapClass = Map.class;
        SimpleType simpleType = new SimpleType(mapClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructCollectionIllegalArgumentException() {
        // Test construct method with a Collection class
        Class<List<String>> listClass = List.class;
        SimpleType simpleType = new SimpleType(listClass);
    }

    @Test
    public void testIsContainerType() {
        // Test isContainerType method
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testBuildCanonicalName() {
        // Test buildCanonicalName method
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        String canonicalName = simpleType.getCanonicalName();
        assertEquals("java.lang.String", canonicalName);

        Class<List<String>> listStringClass = List.class;
        JavaType listType = new SimpleType(listStringClass, TypeBindings.emptyBindings(), null, null);
        canonicalName = listType.getCanonicalName();
        assertEquals("java.util.List<java.lang.String>", canonicalName);
    }

    @Test
    public void testWithContentType() {
        // Test withContentType method
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentType(new SimpleType(int.class));
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithTypeHandler() {
        // Test withTypeHandler method
        SimpleType simpleType = new SimpleType(String.class);
        Object typeHandler = new Object();
        SimpleType simpleType2 = simpleType.withTypeHandler(typeHandler);
        assertEquals(simpleType, simpleType2);
        assertEquals(typeHandler, simpleType2.getTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        // Test withStaticTyping method
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType simpleType2 = simpleType.withStaticTyping();
        assertTrue(simpleType2.isStatic());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testRefine() {
        // Test refine method
        SimpleType simpleType = new SimpleType(String.class);
        Class<Integer> intClass = Integer.class;
        JavaType intType = new SimpleType(intClass);
        JavaType refinedType = simpleType.refine(intClass, TypeBindings.emptyBindings(), null, new JavaType[0]);
        assertNull(refinedType);
    }

    @Test
    public void testGetErasedSignature() {
        // Test getErasedSignature method
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("java.lang.String", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        // Test getGenericSignature method
        Class<List<String>> listStringClass = List.class;
        SimpleType simpleType = new SimpleType(listStringClass, TypeBindings.emptyBindings(), null, new JavaType[0]);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("java.util.List<java.lang.String>", sb.toString());
    }

    @Test
    public void testEquals() {
        // Test equals method
        Class<String> stringClass = String.class;
        SimpleType simpleType1 = new SimpleType(stringClass);
        SimpleType simpleType2 = new SimpleType(stringClass);
        SimpleType simpleType3 = new SimpleType(int.class);
        assertEquals(simpleType1, simpleType1);
        assertEquals(simpleType1, simpleType2);
        assertNotEquals(simpleType1, simpleType3);
    }
}