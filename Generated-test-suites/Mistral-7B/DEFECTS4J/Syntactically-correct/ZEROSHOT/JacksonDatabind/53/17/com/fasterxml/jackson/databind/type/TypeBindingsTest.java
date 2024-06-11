package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertTrue(bindings.isEmpty());
        assertNull(bindings.findBoundType("test"));
    }

    @Test
    public void testCreate() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);

        TypeBindings bindings = TypeBindings.create(List.class, stringType, intType);
        assertEquals(2, bindings.size());
        assertFalse(bindings.isEmpty());
        assertNotNull(bindings.findBoundType("java.util.List"));
        assertNotNull(bindings.findBoundType("E"));
        assertEquals(String.class, bindings.getBoundType(0).getRawClass());
        assertEquals(Integer.class, bindings.getBoundType(1).getRawClass());

        bindings = TypeBindings.create(List.class);
        assertEquals(0, bindings.size());
        assertTrue(bindings.isEmpty());
        assertNull(bindings.findBoundType("java.util.List"));
        assertNull(bindings.findBoundType("E"));
    }

    @Test
    public void testCreateWithUnboundVariable() {
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);

        TypeBindings bindings = TypeBindings.create(List.class, stringType);
        TypeBindings bindingsWithUnbound = bindings.withUnboundVariable("T");

        assertFalse(bindings.hasUnbound("T"));
        assertTrue(bindingsWithUnbound.hasUnbound("T"));
        assertNotNull(bindingsWithUnbound.findBoundType("java.util.List"));
        assertNotNull(bindingsWithUnbound.findBoundType("E"));
        assertNotNull(bindingsWithUnbound.findBoundType("T"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedTypeParameters() {
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);

        TypeBindings bindings = TypeBindings.create(List.class, stringType, intType, intType);
        fail("Should throw IllegalArgumentException: Can not create TypeBindings for class java.util.List with 1 type parameter: class expects 2");
    }

    @Test
    public void testGetTypeParameters() {
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);

        TypeBindings bindings = TypeBindings.create(List.class, stringType, intType);
        List<JavaType> typeParameters = bindings.getTypeParameters();
        assertEquals(2, typeParameters.size());
        assertTrue(typeParameters.contains(stringType));
        assertTrue(typeParameters.contains(intType));

        TypeBindings emptyBindings = TypeBindings.emptyBindings();
        typeParameters = emptyBindings.getTypeParameters();
        assertEquals(0, typeParameters.size());
        assertFalse(typeParameters.isEmpty());
    }
}