package com.example.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertTrue(empty.isEmpty());
        assertNull(empty.findBoundType("T1"));
        assertEquals(0, empty.size());
    }

    @Test
    public void testCreateWithEmptyClass() {
        Class<?> emptyClass = Object.class;
        TypeBindings bindings = TypeBindings.create(emptyClass, Collections.emptyList());
        assertTrue(bindings.isEmpty());
        assertNull(bindings.findBoundType("T1"));
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateWithOneTypeVariable() {
        Class<List<String>> listType = List.class.getTypeParameters()[0].getRawType();
        JavaType typeArg = null; // let JVM infer type
        TypeBindings bindings = TypeBindings.create(listType, typeArg);

        assertFalse(bindings.isEmpty());
        assertArrayEquals(new String[] {"T"}, _names(bindings));
        assertSame(String.class, bindings.getBoundType(0).getRawType());
    }

    @Test
    public void testCreateWithTwoTypeVariables() {
        Class<Map<String, List<Integer>>> mapType = Map.class.getTypeParameters();
        JavaType keyType = mapType[0].getRawType();
        JavaType valueType = mapType[1].getRawType();

        TypeBindings bindings = TypeBindings.create(mapType, keyType, valueType);

        assertFalse(bindings.isEmpty());
        assertArrayEquals(new String[] {"K", "V"}, _names(bindings));
        assertSame(String.class, bindings.getBoundType(0).getRawType());
        assertSame(List.class, bindings.getBoundType(1).getRawType());
    }

    @Test
    public void testCreateWithClassWithoutTypeParameters() {
        Class<String> stringClass = String.class;
        JavaType[] types = new JavaType[0];
        TypeBindings bindings = TypeBindings.create(stringClass, types);
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateWithNullList() {
        Class<List<String>> listType = List.class.getTypeParameters()[0].getRawType();
        TypeBindings bindings = TypeBindings.create(listType, null);
        assertTrue(bindings.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedLength() {
        Class<List<String>> listType = List.class.getTypeParameters()[0].getRawType();
        JavaType[] types = new JavaType[2];
        TypeBindings bindings = TypeBindings.create(listType, types);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedTypes() {
        Class<List<String>> listType = List.class.getTypeParameters()[0].getRawType();
        JavaType[] types = new JavaType[] { IntType.getInstance(), String.class };
        TypeBindings bindings = TypeBindings.create(listType, types);
    }

    private static String[] _names(TypeBindings bindings) {
        return bindings._names;
    }
}