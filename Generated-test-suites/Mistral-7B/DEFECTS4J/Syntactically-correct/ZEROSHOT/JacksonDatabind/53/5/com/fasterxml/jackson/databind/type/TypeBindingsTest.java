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
    public void testCreateEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertEquals(false, bindings.hasUnbound("T"));
    }

    @Test
    public void testCreateTypeBindings() {
        Class<List<String>> listOfStringType = List.class.getGenericType();
        JavaType[] types = new JavaType[] { String.class };
        TypeBindings bindings = TypeBindings.create(listOfStringType, Arrays.asList(types));
        assertEquals(1, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0));
        assertTrue(bindings.hasUnbound("T"));

        List<JavaType> typeParameters = bindings.getTypeParameters();
        assertEquals(Collections.singletonList(types[0]), typeParameters);
    }

    @Test
    public void testCreateIfNeededTypeBindings() {
        Class<List<String>> listOfStringType = List.class.getGenericType();
        JavaType[] types = new JavaType[] { String.class };
        TypeBindings bindings = TypeBindings.createIfNeeded(listOfStringType, Arrays.asList(types));
        assertEquals(1, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0));
        assertTrue(bindings.hasUnbound("T"));

        List<JavaType> typeParameters = bindings.getTypeParameters();
        assertEquals(Collections.singletonList(types[0]), typeParameters);

        bindings = TypeBindings.createIfNeeded(listOfStringType, null);
        assertEquals(0, bindings.size());
        assertFalse(bindings.hasUnbound("T"));
    }

    @Test
    public void testFindBoundType() {
        Class<List<String>> listOfStringType = List.class.getGenericType();
        JavaType[] types = new JavaType[] { String.class };
        TypeBindings bindings = TypeBindings.create(listOfStringType, Arrays.asList(types));
        TypeVariable<String> typeVariable = listOfStringType.getTypeParameters()[0];

        JavaType boundType = bindings.findBoundType(typeVariable.getName());
        assertEquals(types[0], boundType);

        bindings = TypeBindings.emptyBindings();
        assertNull(bindings.findBoundType(typeVariable.getName()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidNumberOfTypeArguments() {
        Class<?> erasedType = List.class;
        JavaType[] types = new JavaType[] { String.class, Integer.class };
        TypeBindings bindings = TypeBindings.create(erasedType, types);
    }

    @Test
    public void testCreateWithUnmatchedNumberOfTypeArguments() {
        Class<List<String>> listOfStringType = List.class.getGenericType();
        JavaType[] types = new JavaType[] { String.class, Integer.class };

        try {
            TypeBindings bindings = TypeBindings.create(listOfStringType, Arrays.asList(types));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateWithClassWithoutTypeArguments() {
        Class<?> erasedType = String[].class;
        JavaType[] types = new JavaType[] {};
        TypeBindings bindings = TypeBindings.create(erasedType, types);
        assertEquals(0, bindings.size());
    }

    @Test
    public void testWithUnboundVariable() {
        String name = "T";
        TypeBindings bindings = TypeBindings.emptyBindings();
        TypeBindings newBindings = bindings.withUnboundVariable(name);
        assertTrue(newBindings.hasUnbound(name));
        assertFalse(bindings.hasUnbound(name));
    }

    @Test
    public void testToString() {
        Class<List<List<String>>> listListOfStringType = List.class.getGenericType();
        JavaType[] types = new JavaType[] { List.class, String.class };
        TypeBindings bindings = TypeBindings.create(listListOfStringType, Arrays.asList(types));

        String expected = "<List<T>>";
        String actual = bindings.toString();
        assertEquals(expected, actual);
    }
}