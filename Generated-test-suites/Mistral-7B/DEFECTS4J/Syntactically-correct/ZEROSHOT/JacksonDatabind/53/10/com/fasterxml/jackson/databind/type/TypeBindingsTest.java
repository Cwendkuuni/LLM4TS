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
        TypeBindings empty = TypeBindings.emptyBindings();
        assertTrue(empty.isEmpty());
        assertEquals(0, empty.size());
        assertNull(empty.findBoundType(""));
    }

    @Test
    public void testCreate() {
        Class<?> listClass = List.class;
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(listClass, listType);
        assertEquals(1, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertEquals(listType, bindings.getBoundType(0));

        assertArrayEquals(new String[] {"E"}, bindings.getTypeParameters().toArray(new String[0]));
    }

    @Test
    public void testCreateWithNullTypes() {
        Class<?> listClass = List.class;
        TypeBindings bindings = TypeBindings.create(listClass, null);
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateWithTypes() {
        Class<?> listClass = List.class;
        JavaType listType1 = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        JavaType listType2 = TypeFactory.defaultInstance().constructType(List.class, String.class);
        TypeBindings bindings = TypeBindings.create(listClass, listType1, listType2);
        assertEquals(2, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertEquals(listType1, bindings.getBoundType(0));
        assertEquals("K", bindings.getBoundName(1));
        assertEquals(listType2, bindings.getBoundType(1));

        assertArrayEquals(new String[] {"E", "K"}, bindings.getTypeParameters().toArray(new String[0]));
    }

    @Test
    public void testCreateIfNeeded() {
        Class<?> listClass = List.class;
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(listClass, listType);
        assertEquals(1, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertEquals(listType, bindings.getBoundType(0));

        assertArrayEquals(new String[] {"E"}, bindings.getTypeParameters().toArray(new String[0]));

        TypeBindings emptyBindings = TypeBindings.emptyBindings();
        TypeBindings bindings2 = bindings.withUnboundVariable("K");
        assertNotSame(bindings, bindings2);
        assertEquals(1, bindings2.size());
        assertEquals("E", bindings2.getBoundName(0));
        assertEquals(listType, bindings2.getBoundType(0));
        assertTrue(bindings2.hasUnbound("K"));
    }

    @Test
    public void testCreateIfNeededWithNullTypes() {
        Class<?> listClass = List.class;
        TypeBindings bindings = TypeBindings.createIfNeeded(listClass, null);
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateIfNeededWithTypes() {
        Class<?> listClass = List.class;
        JavaType listType1 = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        JavaType listType2 = TypeFactory.defaultInstance().constructType(List.class, String.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(listClass, listType1, listType2);
        assertEquals(2, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertEquals(listType1, bindings.getBoundType(0));
        assertEquals("K", bindings.getBoundName(1));
        assertEquals(listType2, bindings.getBoundType(1));

        assertArrayEquals(new String[] {"E", "K"}, bindings.getTypeParameters().toArray(new String[0]));

        TypeBindings emptyBindings = TypeBindings.emptyBindings();
        TypeBindings bindings2 = bindings.withUnboundVariable("K");
        assertNotSame(bindings, bindings2);
        assertEquals(1, bindings2.size());
        assertEquals("E", bindings2.getBoundName(0));
        assertEquals(listType1, bindings2.getBoundType(0));
        assertTrue(bindings2.hasUnbound("K"));
    }

    @Test
    public void testFindBoundType() {
        Class<?> listClass = List.class;
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(listClass, listType);
        assertEquals("E", bindings.findBoundType("E").getName());
        assertNull(bindings.findBoundType("K"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalid() {
        Class<?> listClass = List.class;
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(listClass, listType, String.class);
        fail("Expected IllegalArgumentException");
    }

}