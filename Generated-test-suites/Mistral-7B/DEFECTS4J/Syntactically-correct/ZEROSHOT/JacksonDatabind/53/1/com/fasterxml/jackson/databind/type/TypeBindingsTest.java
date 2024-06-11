package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateSimple() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(List.class, javaType.getTypeArguments());
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(javaType.getTypeArguments()[0], bindings.getBoundType(0));
    }

    @Test
    public void testCreateWithUnboundVariable() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, TypeVariable.class);
        TypeBindings bindings = TypeBindings.create(List.class, javaType.getTypeArguments());
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());
        assertEquals("java.lang.TypeVariable<T>", bindings.getBoundName(0));
        assertNull(bindings.getBoundType(0));
        TypeBindings bindingsWithUnbound = bindings.withUnboundVariable("T");
        assertTrue(bindingsWithUnbound.hasUnbound("T"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidNumberOfTypeArguments() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
        TypeBindings bindings = TypeBindings.create(List.class, javaType.getTypeArguments());
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testCreateWithTwoTypeArguments() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, Integer.class, String.class);
        TypeBindings bindings = TypeBindings.create(Map.class, javaType.getTypeArguments());
        assertFalse(bindings.isEmpty());
        assertEquals(2, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(javaType.getTypeArguments()[0], bindings.getBoundType(0));
        assertEquals("java.lang.String", bindings.getBoundName(1));
        assertEquals(javaType.getTypeArguments()[1], bindings.getBoundType(1));
    }

    @Test
    public void testFindBoundType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, Integer.class, String.class);
        TypeBindings bindings = TypeBindings.create(Map.class, javaType.getTypeArguments());
        assertEquals(Integer.class, bindings.findBoundType("java.lang.Integer"));
        assertEquals(String.class, bindings.findBoundType("java.lang.String"));
        assertNull(bindings.findBoundType("java.lang.Object"));
    }

    @Test
    public void testToString() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(List.class, javaType.getTypeArguments());
        assertEquals("[Ljava.lang.Integer;", bindings.toString());
    }

    @Test
    public void testHashCodeAndEquals() {
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        TypeBindings bindings1 = TypeBindings.create(List.class, javaType1.getTypeArguments());
        TypeBindings bindings2 = TypeBindings.create(List.class, javaType2.getTypeArguments());
        assertEquals(bindings1.hashCode(), bindings2.hashCode());
        assertTrue(bindings1.equals(bindings2));
    }

    @Test
    public void testNotEqualWhenTypesAreDifferent() {
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(List.class, String.class);
        TypeBindings bindings1 = TypeBindings.create(List.class, javaType1.getTypeArguments());
        TypeBindings bindings2 = TypeBindings.create(List.class, javaType2.getTypeArguments());
        assertFalse(bindings1.equals(bindings2));
    }

    @Test
    public void testEqualsWithNull() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(List.class, javaType.getTypeArguments());
        assertFalse(bindings.equals(null));
    }

    @Test
    public void testToStringWithEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals("<>", bindings.toString());
    }
}