package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.core.type.ResolvedType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals("", bindings.toString());
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType(""));
    }

    @Test
    public void testCreateWithNoTypeArgs() {
        Class<List<String>> clazz = new ArrayList<String>().getClass();
        TypeBindings bindings = TypeBindings.create(clazz, null);
        assertEquals(Arrays.asList("java.lang.Object", "java.util.List", "java.lang.String"), bindings.getTypeParameters());
        assertEquals("java.util.List<java.lang.String>", bindings.toString());
        assertEquals(2, bindings.size());
        assertEquals("List", bindings.getBoundName(0));
        assertEquals(JavaType.constructType(ArrayList.class), bindings.getBoundType(0));
        assertEquals("java.lang.String", bindings.getBoundName(1));
        assertNull(bindings.findBoundType(""));
    }

    @Test
    public void testCreateWithOneTypeArg() {
        // Test for java.util.List<Integer>
        Class<List<Integer>> clazz = new ArrayList<Integer>().getClass();
        JavaType javaType = new TypeReference<List<Integer>>() {}.getType();
        TypeBindings bindings = TypeBindings.create(clazz, javaType);
        assertEquals(Arrays.asList("java.lang.Object", "java.util.List", "java.lang.Integer"), bindings.getTypeParameters());
        assertEquals("java.util.List<java.lang.Integer>", bindings.toString());
        assertEquals(2, bindings.size());
        assertEquals("List", bindings.getBoundName(0));
        assertEquals(JavaType.constructType(ArrayList.class), bindings.getBoundType(0));
        assertEquals("java.lang.Integer", bindings.getBoundName(1));
        assertEquals(JavaType.constructType(Integer.class), bindings.findBoundType("Integer"));
    }

    @Test
    public void testCreateWithTwoTypeArgs() {
        // Test for java.util.Map<String, Integer>
        Class<Map<String, Integer>> clazz = HashMap.class;
        TypeBindings bindings = TypeBindings.create(clazz, Arrays.asList(JavaType.constructType(String.class), JavaType.constructType(Integer.class)));
        assertEquals(Arrays.asList("java.lang.Object", "java.util.Map", "java.lang.String", "java.lang.Integer"), bindings.getTypeParameters());
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", bindings.toString());
        assertEquals(4, bindings.size());
        assertEquals("Map", bindings.getBoundName(0));
        assertEquals(JavaType.constructType(HashMap.class), bindings.getBoundType(0));
        assertEquals("java.lang.String", bindings.getBoundName(1));
        assertEquals(JavaType.constructType(String.class), bindings.findBoundType("String"));
        assertEquals("java.lang.Integer", bindings.getBoundName(2));
        assertEquals(JavaType.constructType(Integer.class), bindings.findBoundType("Integer"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidNumberOfTypeArgs() {
        Class<List<String>> clazz = new ArrayList<String>().getClass();
        // Test for java.util.List<String> with 3 type args
        TypeBindings.create(clazz, Arrays.asList(JavaType.constructType(ArrayList.class), JavaType.constructType(String.class), JavaType.constructType(Integer.class)));
    }

    @Test
    public void testCreateIfNeededWithOneTypeArg() {
        Class<List<String>> clazz = new ArrayList<String>().getClass();
        TypeBindings bindings = TypeBindings.createIfNeeded(clazz, JavaType.constructType(String.class));
        assertEquals(Arrays.asList("java.lang.Object", "java.util.List", "java.lang.String"), bindings.getTypeParameters());
        assertEquals("java.util.List<java.lang.String>", bindings.toString());
        assertEquals(2, bindings.size());
        assertEquals("List", bindings.getBoundName(0));
        assertEquals(JavaType.constructType(ArrayList.class), bindings.getBoundType(0));
        assertEquals("java.lang.String", bindings.getBoundName(1));
        assertEquals(JavaType.constructType(String.class), bindings.findBoundType("String"));
    }

    @Test
    public void testCreateIfNeededWithTwoTypeArgs() {
        Class<Map<String, Integer>> clazz = HashMap.class;
        TypeBindings bindings = TypeBindings.createIfNeeded(clazz, Arrays.asList(JavaType.constructType(String.class), JavaType.constructType(Integer.class)));
        assertEquals(Arrays.asList("java.lang.Object", "java.util.Map", "java.lang.String", "java.lang.Integer"), bindings.getTypeParameters());
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", bindings.toString());
        assertEquals(4, bindings.size());
        assertEquals("Map", bindings.getBoundName(0));
        assertEquals(JavaType.constructType(HashMap.class), bindings.getBoundType(0));
        assertEquals("java.lang.String", bindings.getBoundName(1));
        assertEquals(JavaType.constructType(String.class), bindings.findBoundType("String"));
        assertEquals("java.lang.Integer", bindings.getBoundName(2));
        assertEquals(JavaType.constructType(Integer.class), bindings.findBoundType("Integer"));
    }

    @Test
    public void testCreateIfNeededWithNoTypeArgs() {
        Class<Object> clazz = Object.class;
        TypeBindings bindings = TypeBindings.createIfNeeded(clazz, null);
        assertEquals(Arrays.asList("java.lang.Object"), bindings.getTypeParameters());
        assertEquals("<>", bindings.toString());
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType(""));
    }

    @Test
    public void testCreateIfNeededWithInvalidTypeArgsNumber() {
        Class<Map<String, Integer>> clazz = HashMap.class;
        // Test for java.util.Map<String, Integer> with 3 type args
        TypeBindings.createIfNeeded(clazz, Arrays.asList(JavaType.constructType(String.class), JavaType.constructType(Integer.class), JavaType.constructType(List.class)));
    }
}