package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

import java.util.Arrays;
import java.util.List;

public class TypeBindingsTest {

    @org.junit.Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertEquals(0, empty.size());
        assertNull(empty.findBoundType("T"));
    }

    @org.junit.Test
    public void testCreateWithOneType() {
        Class<List<Integer>> listOfInt = List.class.resolveGenericType(Integer.class);
        TypeBindings bindings = TypeBindings.create(listOfInt, null);
        assertEquals(1, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(JavaType.constructUnresolvedType(Integer.TYPE.getName()), bindings.getBoundType(0));
    }

    @org.junit.Test
    public void testCreateWithTwoTypes() {
        Class<Map<Integer, List<String>>> mapOfIntListString = Map.class.resolveGenericType(Integer.class, List.class, String.class);
        TypeBindings bindings = TypeBindings.create(mapOfIntListString, null);
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(JavaType.constructUnresolvedType(Integer.TYPE.getName()), bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(JavaType.constructUnresolvedType(List.class.getTypeName().replaceAll("\\<.*>", "")), bindings.getBoundType(1));
    }

    @org.junit.Test
    public void testCreateIfNeededWithOneType() {
        Class<List<Integer>> listOfInt = List.class.resolveGenericType(Integer.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(listOfInt, null);
        assertEquals(1, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(JavaType.constructUnresolvedType(Integer.TYPE.getName()), bindings.getBoundType(0));
    }

    @org.junit.Test
    public void testCreateIfNeededWithTwoTypes() {
        Class<Map<Integer, List<String>>> mapOfIntListString = Map.class.resolveGenericType(Integer.class, List.class, String.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(mapOfIntListString, null);
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(JavaType.constructUnresolvedType(Integer.TYPE.getName()), bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(JavaType.constructUnresolvedType(List.class.getTypeName().replaceAll("\\<.*>", "")), bindings.getBoundType(1));
    }

    @org.junit.Test
    public void testFindBoundType() {
        Class<List<Integer>> listOfInt = List.class.resolveGenericType(Integer.class);
        TypeBindings bindings = TypeBindings.create(listOfInt, null);
        assertEquals(JavaType.constructUnresolvedType(Integer.TYPE.getName()), bindings.findBoundType("T"));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidNumberOfTypes() {
        Class<List<Integer>> listOfInt = List.class.resolveGenericType(Integer.class);
        TypeBindings.create(listOfInt, Arrays.asList(JavaType.constructUnresolvedType(String.class.getName())));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidTypes() {
        Class<List<Integer>> listOfInt = List.class.resolveGenericType(Integer.class);
        TypeBindings.create(listOfInt, new JavaType[] { JavaType.constructUnresolvedType("java.lang.String") });
    }

    @org.junit.Test
    public void testWithUnboundVariable() {
        TypeBindings bindings = TypeBindings.create(List.class, null);
        TypeBindings bindingsWithUnbound = bindings.withUnboundVariable("T");
        assertEquals("T", bindingsWithUnbound.getBoundName(0));
        assertNull(bindingsWithUnbound.getBoundType(0));
    }
}