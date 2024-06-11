package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collections;
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
    public void testCreateSimpleBindings() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);

        TypeBindings bindings = TypeBindings.create(List.class, intType, stringType);

        assertEquals(2, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(intType, bindings.getBoundType(0));
        assertEquals("java.lang.String", bindings.getBoundName(1));
        assertEquals(stringType, bindings.getBoundType(1));
    }

    @Test
    public void testCreateBindingsWithUnboundVariable() {
        JavaType listType = TypeFactory.defaultInstance().constructTypeList(Integer.class);
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, listType, String.class);

        TypeBindings bindings = TypeBindings.create(Map.class, mapType);
        TypeBindings bindingsWithUnboundVariable = bindings.withUnboundVariable("K");

        assertEquals(3, bindingsWithUnboundVariable.size());
        assertTrue(bindingsWithUnboundVariable.hasUnbound("K"));
        assertFalse(bindings.hasUnbound("K"));
    }

    @Test
    public void testFindBoundType() {
        JavaType listType = TypeFactory.defaultInstance().constructTypeList(Integer.class);
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, listType, String.class);

        TypeBindings bindings = TypeBindings.create(Map.class, mapType);

        JavaType intType = bindings.findBoundType("java.lang.Integer");
        JavaType stringType = bindings.findBoundType("java.lang.String");
        JavaType unboundType = bindings.findBoundType("java.util.List");

        assertNotNull(intType);
        assertNotNull(stringType);
        assertNull(unboundType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBindingsWithInvalidNumberOfTypeArguments() {
        JavaType listType = TypeFactory.defaultInstance().constructTypeList(Integer.class);
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, listType, String.class);

        TypeBindings bindings = TypeBindings.create(Map.class);
        TypeBindings bindingsWithInvalidNumberOfArguments = TypeBindings.create(Map.class, mapType, listType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBindingsWithInvalidTypeArgumentTypes() {
        JavaType listType = TypeFactory.defaultInstance().constructTypeList(Integer.class);
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, listType);

        TypeBindings bindings = TypeBindings.create(Map.class, mapType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBindingsWithInvalidErasedType() {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);

        try {
            TypeBindings bindings = TypeBindings.create(List.class, mapType);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not create TypeBindings for class java.util.Map with 0 type parameter: class expects 2",
                    e.getMessage());
        }
    }
}