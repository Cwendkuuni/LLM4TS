package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        SimpleType simpleTypeConstruct = SimpleType.construct(cls);

        assertEquals(simpleType, simpleTypeConstruct);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructExceptionForMap() {
        Class<Map<String, Integer>> mapClass = Map.class;
        SimpleType.construct(mapClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructExceptionForCollection() {
        Class<List<String>> collectionClass = List.class;
        SimpleType.construct(collectionClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructExceptionForArray() {
        Class<int[]> arrayClass = int[].class;
        SimpleType.construct(arrayClass);
    }

    @Test
    public void testNarrowMethod() {
        Class<String> stringClass = String.class;
        Class<StringBuilder> stringBuilderClass = StringBuffer.class;

        SimpleType simpleType = new SimpleType(stringClass);
        JavaType narrowedType = simpleType._narrow(stringBuilderClass);

        assertNotNull(narrowedType);
        assertTrue(SimpleType.class.isInstance(narrowedType));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowMethodException() {
        Class<String> stringClass = String.class;
        Class<Integer> integerClass = Integer.class;

        SimpleType simpleType = new SimpleType(stringClass);
        simpleType._narrow(integerClass);
    }

    @Test
    public void testIsContainerType() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);

        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);

        assertEquals("[simple type, class java.lang.String]", simpleType.buildCanonicalName());

        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = new TypeBindings();
        bindings.put(0, new SimpleType(String.class));
        bindings.put(1, new SimpleType(Integer.class));
        SimpleType mapType = new SimpleType(mapClass, bindings, null, null);

        assertEquals("[simple type, class java.util.Map<java.lang.String, java.lang.Integer>]", mapType.buildCanonicalName());
    }

    @Test
    public void testEquals() {
        Class<String> stringClass = String.class;
        SimpleType simpleType1 = new SimpleType(stringClass);
        SimpleType simpleType2 = new SimpleType(stringClass);
        SimpleType simpleType3 = new SimpleType(Integer.class);

        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);

        StringBuilder expected = new StringBuilder("[simple type, class java.lang.String]");
        StringBuilder actual = new StringBuilder();
        simpleType.getErasedSignature(actual);

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<Map<String, Integer>> mapClass = Map.class;
        SimpleType mapType = new SimpleType(mapClass, new TypeBindings(), null, null);

        StringBuilder expected = new StringBuilder("[simple type, class java.util.Map<java.lang.String, java.lang.Integer>]");
        StringBuilder actual = new StringBuilder();
        mapType.getGenericSignature(actual);

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testToString() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);

        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }
}