package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;

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
    public void testCreateWithNoTypeArgs() {
        Class<?> clazz = Collection.class;
        TypeBindings bindings = TypeBindings.create(clazz, null);
        assertEquals(1, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertNotNull(bindings.findBoundType("E"));
    }

    @Test
    public void testCreateWithOneTypeArg() {
        Class<?> clazz = ArrayList.class;
        TypeBindings bindings = TypeBindings.create(clazz, JavaType.constructType(Integer.TYPE));
        assertEquals(1, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertNotNull(bindings.findBoundType("E"));
    }

    @Test
    public void testCreateWithTwoTypeArgs() {
        Class<?> clazz = LinkedHashMap.class;
        TypeBindings bindings = TypeBindings.create(clazz, JavaType.constructType(String.class),
                JavaType.constructType(Integer.TYPE));
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals("V", bindings.getBoundName(1));
        assertNotNull(bindings.findBoundType("K"));
        assertNotNull(bindings.findBoundType("V"));
    }

    @Test
    public void testCreateIfNeededWithNoTypeArgs() {
        Class<?> clazz = Collection.class;
        TypeBindings bindings = TypeBindings.createIfNeeded(clazz, null);
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateIfNeededWithOneTypeArg() {
        Class<?> clazz = ArrayList.class;
        TypeBindings bindings = TypeBindings.createIfNeeded(clazz, JavaType.constructType(Integer.TYPE));
        assertEquals(1, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertNotNull(bindings.findBoundType("E"));
    }

    @Test
    public void testCreateIfNeededWithTwoTypeArgs() {
        Class<?> clazz = LinkedHashMap.class;
        TypeBindings bindings = TypeBindings.createIfNeeded(clazz, JavaType.constructType(String.class),
                JavaType.constructType(Integer.TYPE));
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals("V", bindings.getBoundName(1));
        assertNotNull(bindings.findBoundType("K"));
        assertNotNull(bindings.findBoundType("V"));
    }

    @Test
    public void testWithUnboundVariable() {
        String name = "T";
        TypeBindings bindings = TypeBindings.create(Object.class, JavaType.constructType(String.class),
                JavaType.constructType(JavaType.constructType(TypeVariable.class, name)));
        TypeBindings withUnbound = bindings.withUnboundVariable(name);
        assertNotSame(bindings, withUnbound);
        assertEquals(1 + bindings.size(), withUnbound.size());
        assertTrue(withUnbound.hasUnbound(name));
        assertNull(bindings.findBoundType(name));
        assertNull(withUnbound.findBoundType(name));
        assertNotNull(withUnbound.findBoundType("E"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedNamesAndTypes() {
        Class<?> clazz = LinkedHashMap.class;
        TypeBindings bindings = TypeBindings.create(clazz, JavaType.constructType(String.class),
                JavaType.constructType(Integer.TYPE), JavaType.constructType(Long.TYPE));
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedNumberOfNamesAndTypes() {
        Class<?> clazz = LinkedHashMap.class;
        TypeBindings bindings = TypeBindings.create(clazz, JavaType.constructType(String.class),
                JavaType.constructType(Integer.TYPE), JavaType.constructType(Integer.TYPE),
                JavaType.constructType(Long.TYPE));
        fail("Expected IllegalArgumentException");
    }

}