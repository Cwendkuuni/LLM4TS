package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        // Test constructor with no arguments
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = new SimpleType(stringClass);
        assertEquals(String.class, stringSimpleType.getRawClass());
        assertNull(stringSimpleType.getSuperClass());
        assertNull(stringSimpleType.getSuperInterfaces());
        assertFalse(stringSimpleType.isContainerType());
        assertEquals("java.lang.String", stringSimpleType.getCanonicalName());

        // Test constructor with arguments
        Class<List<Integer>> listIntClass = List.class.getGenericType();
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = JavaType.constructCollectionType(List.class, Integer.class);
        JavaType[] superInterfaces = new JavaType[] {};
        SimpleType listIntSimpleType = new SimpleType(listIntClass, bindings, superClass, superInterfaces);
        assertEquals(List.class, listIntSimpleType.getRawClass());
        assertEquals(List.class, listIntSimpleType.getSuperClass());
        assertArrayEquals(new JavaType[] {Integer.class}, listIntSimpleType.getSuperInterfaces());
        assertTrue(listIntSimpleType.isContainerType());
        assertEquals("java.util.List<java.lang.Integer>", listIntSimpleType.getCanonicalName());
    }

    @Test
    public void testTypeFactoryConstruction() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = JavaType.constructType(stringClass);
        SimpleType stringSimpleType = new SimpleType(stringJavaType);
        assertEquals(String.class, stringSimpleType.getRawClass());
        assertNull(stringSimpleType.getSuperClass());
        assertNull(stringSimpleType.getSuperInterfaces());
        assertFalse(stringSimpleType.isContainerType());
        assertEquals("java.lang.String", stringSimpleType.getCanonicalName());
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = new SimpleType(stringClass);
        Object stringHandler = new Object() {};
        SimpleType stringSimpleTypeWithHandler = stringSimpleType.withTypeHandler(stringHandler);
        assertEquals(stringSimpleType, stringSimpleTypeWithHandler);
        assertNotSame(stringHandler, stringSimpleTypeWithHandler.getTypeHandler());
        assertEquals(stringHandler, stringSimpleTypeWithHandler.getTypeHandler());
    }

    @Test
    public void testWithContainerTypeHandler() {
        Class<List<Integer>> listIntClass = List.class.getGenericType();
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = JavaType.constructCollectionType(List.class, Integer.class);
        JavaType[] superInterfaces = new JavaType[] {};
        SimpleType listIntSimpleType = new SimpleType(listIntClass, bindings, superClass, superInterfaces);
        Object containerHandler = new Object() {};
        SimpleType listIntSimpleTypeWithContainerHandler = listIntSimpleType.withTypeHandler(containerHandler);
        assertNotSame(listIntSimpleType, listIntSimpleTypeWithContainerHandler);
        assertNull(listIntSimpleTypeWithContainerHandler.getTypeHandler());
        assertSame(containerHandler, listIntSimpleTypeWithContainerHandler.getValueHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = new SimpleType(stringClass);
        Object stringHandler = new Object() {};
        SimpleType stringSimpleTypeWithHandler = stringSimpleType.withValueHandler(stringHandler);
        assertEquals(stringSimpleType, stringSimpleTypeWithHandler);
        assertNotSame(stringHandler, stringSimpleTypeWithHandler.getValueHandler());
        assertEquals(stringHandler, stringSimpleTypeWithHandler.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = new SimpleType(stringClass);
        SimpleType stringSimpleTypeWithStaticTyping = stringSimpleType.withStaticTyping();
        assertFalse(stringSimpleType.isStaticTyping());
        assertTrue(stringSimpleTypeWithStaticTyping.isStaticTyping());
        assertEquals(stringSimpleType, stringSimpleTypeWithStaticTyping);
    }

    @Test
    public void testIsContainerType() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = new SimpleType(stringClass);
        assertFalse(stringSimpleType.isContainerType());

        Class<List<Integer>> listIntClass = List.class.getGenericType();
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = JavaType.constructCollectionType(List.class, Integer.class);
        JavaType[] superInterfaces = new JavaType[] {};
        SimpleType listIntSimpleType = new SimpleType(listIntClass, bindings, superClass, superInterfaces);
        assertTrue(listIntSimpleType.isContainerType());
    }

    @Test
    public void testCanonicalName() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = new SimpleType(stringClass);
        assertEquals("java.lang.String", stringSimpleType.getCanonicalName());

        Class<List<Integer>> listIntClass = List.class.getGenericType();
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = JavaType.constructCollectionType(List.class, Integer.class);
        JavaType[] superInterfaces = new JavaType[] {};
        SimpleType listIntSimpleType = new SimpleType(listIntClass, bindings, superClass, superInterfaces);
        assertEquals("java.util.List<java.lang.Integer>", listIntSimpleType.getCanonicalName());
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = new SimpleType(stringClass);
        StringBuilder erasedSignature = new StringBuilder();
        stringSimpleType.getErasedSignature(erasedSignature);
        assertEquals("java.lang.String", erasedSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<List<List<Integer>>> listListIntClass = List.class.getGenericType();
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = JavaType.constructCollectionType(List.class, List.class);
        JavaType[] superInterfaces = new JavaType[] {};
        SimpleType listListIntSimpleType = new SimpleType(listListIntClass, bindings, superClass, superInterfaces);
        StringBuilder genericSignature = new StringBuilder();
        listListIntSimpleType.getGenericSignature(genericSignature);
        assertEquals("java.util.List<java.util.List<java.lang.Integer>>", genericSignature.toString());
    }

    @Test
    public void testToString() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = new SimpleType(stringClass);
        assertEquals("[simple type, class java.lang.String]", stringSimpleType.toString());

        Class<List<List<Integer>>> listListIntClass = List.class.getGenericType();
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = JavaType.constructCollectionType(List.class, List.class);
        JavaType[] superInterfaces = new JavaType[] {};
        SimpleType listListIntSimpleType = new SimpleType(listListIntClass, bindings, superClass, superInterfaces);
        assertEquals("[simple type, class java.util.List<java.util.List<java.lang.Integer>>]", listListIntSimpleType.toString());
    }

    @Test
    public void testEquals() {
        Class<String> stringClass1 = String.class;
        Class<String> stringClass2 = String.class;
        SimpleType stringSimpleType1 = new SimpleType(stringClass1);
        SimpleType stringSimpleType2 = new SimpleType(stringClass2);
        assertTrue(stringSimpleType1.equals(stringSimpleType2));

        SimpleType stringSimpleType3 = new SimpleType(Object.class);
        assertFalse(stringSimpleType1.equals(stringSimpleType3));
    }
}