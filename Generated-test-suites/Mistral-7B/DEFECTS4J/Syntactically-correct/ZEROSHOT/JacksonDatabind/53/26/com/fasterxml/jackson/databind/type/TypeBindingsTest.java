package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertFalse(bindings.hasUnbound(""));
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType(""));
        assertEquals("", bindings.toString());
    }

    @Test
    public void testCreate() {
        Class<List<String>> listClass = List.class;
        JavaType[] types = new JavaType[] { JavaType.getType(String.class) };
        TypeBindings bindings = TypeBindings.create(listClass, types);

        assertTrue(bindings.hasUnbound(""));
        assertEquals(1, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(JavaType.getType(String.class), bindings.getBoundType(0));
        assertEquals("[Ljava.lang.String;", bindings.toString());

        String unboundName = "T";
        TypeBindings bindingsWithUnbound = bindings.withUnboundVariable(unboundName);
        assertTrue(bindingsWithUnbound.hasUnbound(unboundName));
        assertEquals(2, bindingsWithUnbound.size());
        assertEquals("java.lang.String", bindingsWithUnbound.getBoundName(0));
        assertEquals(JavaType.getType(String.class), bindingsWithUnbound.getBoundType(0));
        assertEquals("T", bindingsWithUnbound.getBoundName(1));
        assertEquals("Ljava/lang/Object;T", bindingsWithUnbound.toString());

        JavaType boundType = JavaType.constructParametricType(listClass, JavaType.getType(String.class));
        TypeBindings bindingsWithBound = TypeBindings.create(listClass, boundType);
        assertFalse(bindingsWithBound.hasUnbound(""));
        assertEquals(1, bindingsWithBound.size());
        assertEquals("java.lang.String", bindingsWithBound.getBoundName(0));
        assertEquals(boundType, bindingsWithBound.getBoundType(0));
        assertEquals("[Ljava.lang.String;", bindingsWithBound.toString());
    }

    @Test
    public void testCreateIfNeeded() {
        Class<List<String>> listClass = List.class;
        JavaType[] types = new JavaType[] {};
        TypeBindings bindings = TypeBindings.createIfNeeded(listClass, types);
        assertFalse(bindings.hasUnbound(""));
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType(""));
        assertEquals("<>", bindings.toString());

        types = new JavaType[] { JavaType.getType(String.class) };
        bindings = TypeBindings.createIfNeeded(listClass, types);
        assertTrue(bindings.hasUnbound(""));
        assertEquals(1, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(JavaType.getType(String.class), bindings.getBoundType(0));
        assertEquals("[Ljava.lang.String;", bindings.toString());
    }

    @Test
    public void testFindBoundType() {
        Class<List<String>> listClass = List.class;
        JavaType[] types = new JavaType[] { JavaType.getType(String.class) };
        TypeBindings bindings = TypeBindings.create(listClass, types);

        assertEquals(JavaType.getType(String.class), bindings.findBoundType(""));
        assertNull(bindings.findBoundType("X"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalidNumberOfTypeParameters() {
        Class<List<String>> listClass = List.class;
        JavaType[] types = new JavaType[3];
        TypeBindings.create(listClass, types);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedNumberOfTypeParameters() {
        Class<List<List<String>>> listOfListClass = List.class;
        JavaType[] listTypes = new JavaType[] { JavaType.getType(List.class), JavaType.getType(String.class) };
        JavaType listType = JavaType.constructParametricType(List.class, listTypes);
        TypeBindings bindings = TypeBindings.create(listOfListClass, listType);

        assertEquals(2, bindings.size());
        assertEquals("java.util.List", bindings.getBoundName(0));
        assertEquals(JavaType.getType(List.class), bindings.getBoundType(0));
        assertEquals("T", bindings.getBoundName(1));
        assertEquals("Ljava/util/List;T", bindings.toString());

        TypeVariable<?>[] listTypeParameters = listType.getTypeParameters();
        assertEquals(1, listTypeParameters.length);
        assertEquals("T", listTypeParameters[0].getName());

        TypeVariable<?>[] mapTypeParameters = Map.class.getTypeParameters();
        assertEquals(2, mapTypeParameters.length);
        assertEquals("K", mapTypeParameters[0].getName());
        assertEquals("V", mapTypeParameters[1].getName());

        try {
            TypeBindings bindings2 = TypeBindings.create(listOfListClass, listType, mapTypeParameters[0], mapTypeParameters[1]);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("java.lang.IllegalArgumentException: Can not create TypeBindings for class java.util.List<java.util.List<java.lang.String>> with 2 type parameter"
                    + "s: class expects 1", e.getMessage());
        }
    }
}