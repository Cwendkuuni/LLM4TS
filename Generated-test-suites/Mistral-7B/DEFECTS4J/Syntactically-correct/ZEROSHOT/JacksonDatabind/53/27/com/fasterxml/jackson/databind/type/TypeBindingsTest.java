package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.JavaType.TypeParameterBound;

public class TypeBindingsTest {

    @Test
    public void testCreateEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertNull(bindings.findBoundType(null));
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
        assertEquals(0, bindings.getBoundName(0));
        assertEquals(null, bindings.getBoundType(0));
        assertEquals(Collections.emptyList(), bindings.getTypeParameters());
        assertFalse(bindings.hasUnbound("T"));
    }

    @Test
    public void testCreateBindingsForSimpleTypes() {
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(List.class);
        JavaType javaType3 = TypeFactory.defaultInstance().constructType(ArrayList.class);

        TypeBindings bindings = TypeBindings.create(Integer.class, javaType1);
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(javaType1, bindings.getBoundType(0));
        assertEquals(1, bindings.size());
        assertTrue(bindings.toString().contains("Integer"));

        bindings = TypeBindings.create(List.class, javaType2);
        TypeVariable<?>[] typeParameters = List.class.getTypeParameters();
        assertEquals("E", bindings.getBoundName(0));
        assertEquals(javaType2.getType(TypeParameterBound.VALUE), bindings.getBoundType(0));
        assertEquals(typeParameters.length, bindings.size());
        assertEquals(Arrays.asList("E"), bindings.getTypeParameters());

        bindings = TypeBindings.create(ArrayList.class, javaType3);
        typeParameters = ArrayList.class.getTypeParameters();
        assertEquals("E", bindings.getBoundName(0));
        assertEquals(javaType3.getType(TypeParameterBound.VALUE), bindings.getBoundType(0));
        assertEquals(typeParameters.length, bindings.size());
        assertEquals(Arrays.asList("E"), bindings.getTypeParameters());
    }

    @Test
    public void testCreateBindingsForComplexTypes() {
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(HashMap.class);

        TypeBindings bindings = TypeBindings.create(Map.class, javaType1, javaType2);
        TypeVariable<?>[] typeParameters = Map.class.getTypeParameters();
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(javaType1.getType(TypeParameterBound.KEY), bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(javaType2.getType(TypeParameterBound.VALUE), bindings.getBoundType(1));
        assertEquals(typeParameters.length, bindings.size());
        assertEquals(Arrays.asList("K", "V"), bindings.getTypeParameters());
    }

    @Test
    public void testCreateIfNeeded() {
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(List.class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(ArrayList.class);

        TypeBindings bindings = TypeBindings.createIfNeeded(List.class, javaType1);
        assertEquals("E", bindings.getBoundName(0));
        assertEquals(javaType1.getType(TypeParameterBound.VALUE), bindings.getBoundType(0));
        assertEquals(1, bindings.size());
        assertEquals(Arrays.asList("E"), bindings.getTypeParameters());

        bindings = TypeBindings.createIfNeeded(List.class, new TypeReference<List<String>>() {}.getType());
        assertEquals("E", bindings.getBoundName(0));
        assertEquals(TypeFactory.defaultInstance().constructType(String.class), bindings.getBoundType(0));
        assertEquals(1, bindings.size());
        assertEquals(Arrays.asList("E"), bindings.getTypeParameters());

        bindings = TypeBindings.createIfNeeded(ArrayList.class, javaType2);
        assertEquals("E", bindings.getBoundName(0));
        assertEquals(javaType2.getType(TypeParameterBound.VALUE), bindings.getBoundType(0));
        assertEquals(1, bindings.size());
        assertEquals(Arrays.asList("E"), bindings.getTypeParameters());

        bindings = TypeBindings.createIfNeeded(ArrayList.class, new TypeReference<ArrayList<Integer>>() {}.getType());
        assertEquals("E", bindings.getBoundName(0));
        assertEquals(TypeFactory.defaultInstance().constructType(Integer.class), bindings.getBoundType(0));
        assertEquals(1, bindings.size());
        assertEquals(Arrays.asList("E"), bindings.getTypeParameters());

        bindings = TypeBindings.createIfNeeded(ArrayList.class, new TypeReference<ArrayList<Object>>() {}.getType());
        assertEquals("E", bindings.getBoundName(0));
        assertEquals(TypeFactory.defaultInstance().constructType(Object.class), bindings.getBoundType(0));
        assertEquals(1, bindings.size());
        assertEquals(Arrays.asList("E"), bindings.getTypeParameters());
    }

    @Test
    public void testCreateWithUnboundVariable() {
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(List.class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(ArrayList.class);

        TypeBindings bindings = TypeBindings.create(List.class, javaType1);
        TypeBindings unboundBindings = bindings.withUnboundVariable("S");
        assertEquals("E", bindings.getBoundName(0));
        assertEquals(javaType1.getType(TypeParameterBound.VALUE), bindings.getBoundType(0));
        assertEquals(1, bindings.size());
        assertEquals(Arrays.asList("E"), bindings.getTypeParameters());
        assertTrue(bindings.hashCode() != unboundBindings.hashCode());

        assertEquals("S", unboundBindings.getBoundName(0));
        assertEquals(null, unboundBindings.getBoundType(0));
        assertEquals(bindings.size() + 1, unboundBindings.size());
        assertEquals(Arrays.asList("E", "S"), unboundBindings.getTypeParameters());
    }

    @Test
    public void testFindBoundType() {
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(List.class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(ArrayList.class);

        TypeBindings bindings = TypeBindings.create(List.class, javaType1);
        assertEquals(javaType1.getType(TypeParameterBound.VALUE), bindings.findBoundType("E"));
        assertEquals(null, bindings.findBoundType("F"));

        bindings = TypeBindings.create(ArrayList.class, javaType2);
        assertEquals(javaType2.getType(TypeParameterBound.VALUE), bindings.findBoundType("E"));
        assertEquals(null, bindings.findBoundType("F"));
    }
}