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
    }

    @Test
    public void testCreate() {
        Class<?> listClass = List.class;
        JavaType[] types = new JavaType[] { JavaType.getType(int.class), JavaType.getType(String.class) };
        TypeBindings bindings = TypeBindings.create(listClass, types);
        assertEquals(2, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals("K", bindings.getBoundName(1));
        assertEquals(JavaType.getType(int.class), bindings.getBoundType(0));
        assertEquals(JavaType.getType(String.class), bindings.getBoundType(1));
    }

    @Test
    public void testCreateIfNeeded() {
        Class<?> listClass = List.class;
        TypeBindings bindings = TypeBindings.createIfNeeded(listClass, null);
        assertNotNull(bindings);
        assertEquals(0, bindings.size());

        JavaType[] types = new JavaType[] { JavaType.getType(int.class), JavaType.getType(String.class) };
        bindings = TypeBindings.createIfNeeded(listClass, types);
        assertEquals(2, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals("K", bindings.getBoundName(1));
        assertEquals(JavaType.getType(int.class), bindings.getBoundType(0));
        assertEquals(JavaType.getType(String.class), bindings.getBoundType(1));
    }

    @Test
    public void testCreateForOneTypeParameter() {
        Class<?> listClass = ArrayList.class;
        JavaType typeArg1 = JavaType.getType(String.class);
        TypeBindings bindings = TypeBindings.create(listClass, typeArg1);
        assertEquals(1, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertEquals(JavaType.getType(String.class), bindings.getBoundType(0));
    }

    @Test
    public void testFindBoundType() {
        Class<?> listClass = List.class;
        JavaType[] types = new JavaType[] { JavaType.getType(int.class), JavaType.getType(String.class) };
        TypeBindings bindings = TypeBindings.create(listClass, types);
        JavaType boundType = bindings.findBoundType("K");
        assertNotNull(boundType);
        assertEquals(JavaType.getType(String.class), boundType);

        boundType = bindings.findBoundType("T");
        assertNotNull(boundType);
        assertEquals(JavaType.getType(int.class), boundType);

        boundType = bindings.findBoundType("X");
        assertNull(boundType);
    }

    @Test
    public void testHasUnbound() {
        Class<?> listClass = List.class;
        JavaType[] types = new JavaType[] { JavaType.getType(int.class), JavaType.getType(String.class) };
        TypeBindings bindings = TypeBindings.create(listClass, types);
        assertFalse(bindings.hasUnbound("K"));
        assertFalse(bindings.hasUnbound("T"));
        assertFalse(bindings.hasUnbound("X"));

        bindings = bindings.withUnboundVariable("X");
        assertTrue(bindings.hasUnbound("X"));
        assertFalse(bindings.hasUnbound("K"));
        assertFalse(bindings.hasUnbound("T"));
    }

    @Test
    public void testToString() {
        Class<?> listClass = List.class;
        JavaType[] types = new JavaType[] { JavaType.getType(int.class), JavaType.getType(String.class) };
        TypeBindings bindings = TypeBindings.create(listClass, types);
        String toString = bindings.toString();
        String expected = "<T, K>";
        assertEquals(expected, toString);
    }

    @Test
    public void testEquals() {
        Class<?> listClass = List.class;
        JavaType[] types1 = new JavaType[] { JavaType.getType(int.class), JavaType.getType(String.class) };
        JavaType[] types2 = new JavaType[] { JavaType.getType(int.class), JavaType.getType(String.class) };
        TypeBindings bindings1 = TypeBindings.create(listClass, types1);
        TypeBindings bindings2 = TypeBindings.create(listClass, types2);
        assertTrue(bindings1.equals(bindings2));

        JavaType type3 = JavaType.constructType(List.class, JavaType.getType(int.class), JavaType.getType(String.class));
        TypeBindings bindings3 = TypeBindings.create(listClass, type3.getComponentTypes());
        assertFalse(bindings1.equals(bindings3));
    }

    @Test
    public void testHashCode() {
        Class<?> listClass = List.class;
        JavaType[] types1 = new JavaType[] { JavaType.getType(int.class), JavaType.getType(String.class) };
        JavaType[] types2 = new JavaType[] { JavaType.getType(int.class), JavaType.getType(String.class) };
        TypeBindings bindings1 = TypeBindings.create(listClass, types1);
        TypeBindings bindings2 = TypeBindings.create(listClass, types2);
        assertEquals(bindings1.hashCode(), bindings2.hashCode());

        JavaType type3 = JavaType.constructType(List.class, JavaType.getType(int.class), JavaType.getType(String.class));
        TypeBindings bindings3 = TypeBindings.create(listClass, type3.getComponentTypes());
        assertNotEquals(bindings1.hashCode(), bindings3.hashCode());
    }

    @Test
    public void testTypeParameterArray() {
        Class<?> listClass = List.class;
        JavaType[] types = new JavaType[] { JavaType.getType(int.class), JavaType.getType(String.class) };
        TypeBindings bindings = TypeBindings.create(listClass, types);
        JavaType[] typesArray = bindings.typeParameterArray();
        assertEquals(types, typesArray);
    }
}