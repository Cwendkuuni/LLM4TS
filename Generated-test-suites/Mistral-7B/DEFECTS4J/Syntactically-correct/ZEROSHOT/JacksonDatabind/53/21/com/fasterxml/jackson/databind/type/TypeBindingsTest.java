package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateWithNoTypeArgs() {
        Class<?> clazz = Collection.class;
        TypeBindings bindings = TypeBindings.create(clazz, null);
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateWithOneTypeArg() {
        Class<?> clazz = List.class;
        JavaType javaType = JavaType.constructParametricType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(clazz, javaType);
        assertEquals(1, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(javaType, bindings.getBoundType(0));
    }

    @Test
    public void testCreateWithTwoTypeArgs() {
        Class<?> clazz = Map.class;
        JavaType javaTypeKey = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        JavaType javaTypeValue = JavaType.constructParametricType(Map.class, Integer.class, String.class);
        TypeBindings bindings = TypeBindings.create(clazz, javaTypeKey, javaTypeValue);
        assertEquals(2, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(javaTypeKey, bindings.getBoundType(0));
        assertEquals("java.lang.Integer", bindings.getBoundName(1));
        assertEquals(javaTypeValue, bindings.getBoundType(1));
    }

    @Test
    public void testFindBoundType() {
        Class<?> clazz = Map.class;
        JavaType javaTypeKey = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        JavaType javaTypeValue = JavaType.constructParametricType(Map.class, Integer.class, String.class);
        TypeBindings bindings = TypeBindings.create(clazz, javaTypeKey, javaTypeValue);
        JavaType foundTypeKey = bindings.findBoundType("java.lang.String");
        JavaType foundTypeValue = bindings.findBoundType("java.lang.Integer");
        assertEquals(javaTypeKey, foundTypeKey);
        assertEquals(javaTypeValue, foundTypeValue);
        JavaType notFoundType = bindings.findBoundType("java.lang.Long");
        assertNull(notFoundType);
    }

    @Test
    public void testWithUnboundVariable() {
        Class<?> clazz = Map.class;
        JavaType javaTypeKey = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        JavaType javaTypeValue = JavaType.constructParametricType(Map.class, Integer.class, String.class);
        TypeBindings bindings = TypeBindings.create(clazz, javaTypeKey, javaTypeValue);
        TypeBindings bindingsWithUnboundVariable = bindings.withUnboundVariable("java.lang.Object");
        assertTrue(bindingsWithUnboundVariable.hasUnbound("java.lang.Object"));
        assertFalse(bindings.hasUnbound("java.lang.Object"));
    }

    @Test
    public void testToString() {
        Class<?> clazz = Map.class;
        JavaType javaTypeKey = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        JavaType javaTypeValue = JavaType.constructParametricType(Map.class, Integer.class, String.class);
        TypeBindings bindings = TypeBindings.create(clazz, javaTypeKey, javaTypeValue);
        String expected = "<java.util.Map<java.lang.String, java.lang.Integer, java.lang.Object, java.util.LinkedHashMap<java.lang.String, java.lang.Integer>>>";
        assertEquals(expected, bindings.toString());
    }
}