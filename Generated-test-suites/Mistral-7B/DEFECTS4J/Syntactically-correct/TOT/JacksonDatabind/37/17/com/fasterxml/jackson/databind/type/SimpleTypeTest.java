package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeCreation() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        JavaType javaType = simpleType.getRawType();
        assertEquals(String.class, javaType.getRawClass());
    }

    @Test
    public void testSimpleTypeCreationWithBindings() {
        Class<List<Integer>> listIntegerClass = List.class.getTypeParameters()[0].getType();
        SimpleType simpleType = new SimpleType(listIntegerClass, TypeBindings.defaultBindings());
        JavaType javaType = simpleType.getRawType();
        assertEquals(List.class, javaType.getRawClass());
        assertEquals(Integer.class, simpleType.getContentType().getRawClass());
    }

    @Test
    public void testSimpleTypeNarrowing() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        JavaType stringListType = new SimpleType(List.class, TypeBindings.defaultBindings())._narrow(String.class);
        assertEquals(simpleType, stringListType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Map.class);
        // This should not be reachable, as constructUnsafe() should not be used for Map/Collection/Array types
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstruct() {
        SimpleType simpleType = SimpleType.construct(Map.class);
        // This should not be reachable, as construct() should not be used for Map/Collection/Array types
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType javaType = simpleType.withTypeHandler(null);
        assertNull(javaType.getTypeHandler());
        JavaType javaTypeWithHandler = simpleType.withTypeHandler(new MyStringTypeHandler());
        assertNotNull(javaTypeWithHandler.getTypeHandler());
    }

    private static class MyStringTypeHandler {}
}