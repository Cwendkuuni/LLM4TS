package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType(""));
    }

    @Test
    public void testSingleTypeBinding() {
        Class<List<Integer>> clazz = ArrayList.class.generateInterfaces(List.class, Integer.class);
        TypeVariable<Integer> typeVariable = clazz.getTypeParameters()[0];
        JavaType javaType = JavaType.construct(clazz, Integer.TYPE);
        TypeBindings bindings = TypeBindings.create(clazz, javaType);
        assertEquals(1, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(Integer.TYPE, bindings.getBoundType(0).getRawType());
    }

    @Test
    public void testMultipleTypeBindings() {
        Class<Map<String, List<Integer>>> clazz = HashMap.class.generateInterfaces(Map.class, String.class, List.class, Integer.class);
        TypeVariable<?>[] typeVariables = clazz.getTypeParameters();
        TypeVariable<String> stringVariable = typeVariables[0];
        TypeVariable<List<Integer>> listVariable = typeVariables[1];
        JavaType stringType = JavaType.construct(String.class);
        JavaType listType = JavaType.construct(List.class, Integer.TYPE);
        TypeBindings bindings = TypeBindings.create(clazz, stringType, listType);
        assertEquals(2, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0).getRawType());
        assertEquals("java.util.List", bindings.getBoundName(1));
        assertEquals(JavaType.construct(List.class, Integer.TYPE), bindings.getBoundType(1));
    }

    @Test
    public void testFindBoundType() {
        Class<Map<List<List<Integer>>, Map<String, String>>> clazz = LinkedHashMap.class.generateInterfaces(LinkedHashMap.class, List.class, List.class, Map.class, String.class, String.class);
        TypeVariable<?>[] typeVariables = clazz.getTypeParameters();
        TypeVariable<List<List<Integer>>> listListVariable = typeVariables[0];
        TypeVariable<Map<String, String>> mapVariable = typeVariables[1];
        JavaType listListType = JavaType.construct(List.class, List.class, Integer.TYPE);
        JavaType mapType = JavaType.construct(Map.class, String.class, String.class);
        TypeBindings bindings = TypeBindings.create(clazz, listListType, mapType);
        assertEquals("java.util.List<java.util.List<java.lang.Integer>>", bindings.getBoundName(0));
        assertEquals("java.util.List<java.util.List<java.lang.Integer>>", bindings.getBoundName(0));
        assertEquals("java.util.List<java.util.List<java.lang.Integer>>", bindings.findBoundType("java.util.List<java.util.List<java.lang.Integer>>").getRawClass());
        assertEquals("java.util.Map<java.lang.String, java.lang.String>", bindings.getBoundName(1));
        assertEquals("java.util.Map<java.lang.String, java.lang.String>", bindings.getBoundName(1));
        assertEquals("java.util.Map<java.lang.String, java.lang.String>", bindings.findBoundType("java.util.Map<java.lang.String, java.lang.String>").getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNumberOfTypeArguments() {
        Class<List<List<List<Integer>>>> clazz = ArrayList.class.generateInterfaces(List.class, List.class, List.class, Integer.TYPE);
        JavaType javaType = JavaType.construct(clazz, Integer.TYPE);
        TypeBindings bindings = TypeBindings.create(clazz, javaType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTypeArgumentTypes() {
        Class<Map<String, Integer>> clazz = HashMap.class.generateInterfaces(Map.class, Integer.class, String.class);
        TypeVariable<?>[] typeVariables = clazz.getTypeParameters();
        JavaType stringType = JavaType.construct(String.class);
        JavaType intType = JavaType.construct(Integer.class);
        TypeBindings bindings = TypeBindings.create(clazz, stringType, intType);
    }

    @Test
    public void testWithUnboundVariable() {
        Class<Map<String, String>> clazz = HashMap.class.generateInterfaces(Map.class, String.class, String.class);
        TypeBindings bindings = TypeBindings.create(clazz, String.class);
        TypeBindings unboundBindings = bindings.withUnboundVariable("key");
        assertEquals("java.lang.String", unboundBindings.getBoundName(0));
        assertEquals("java.lang.String", unboundBindings.getBoundName(1));
        assertEquals(2, unboundBindings.size());
        assertArrayEquals(new String[]{"java.lang.String", "java.lang.String"}, unboundBindings.getTypeParameters().toArray(new String[0]));
        assertNull(unboundBindings.findBoundType("key"));
    }
}