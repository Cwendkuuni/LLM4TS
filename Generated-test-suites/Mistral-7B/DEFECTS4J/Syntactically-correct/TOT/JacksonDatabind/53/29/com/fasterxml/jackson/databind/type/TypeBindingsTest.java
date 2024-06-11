package com.example.junit4;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertEquals("[]", empty.toString());
        assertNull(empty.findBoundType("T"));
    }

    @Test
    public void testBindingsForSimpleType() {
        JavaType javaIntType = TypeFactory.defaultInstance().constructType(int.class);
        TypeBindings bindings = TypeBindings.create(Integer.class, javaIntType);
        assertEquals("<Integer>", bindings.toString());
        assertEquals(javaIntType, bindings.findBoundType("T"));
    }

    @Test
    public void testBindingsForTypeWithTwoTypeParameters() {
        JavaType javaIntType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings bindings = TypeBindings.create(Map.class, javaIntType, javaStringType);
        assertEquals("<K,V>", bindings.toString());
        assertEquals(javaIntType, bindings.findBoundType("K"));
        assertEquals(javaStringType, bindings.findBoundType("V"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBindingsForIncorrectNumberOfTypeParameters() {
        JavaType javaIntType = TypeFactory.defaultInstance().constructType(int.class);
        TypeBindings bindings = TypeBindings.create(Map.class, javaIntType);
        // The Map class expects 2 type parameters, but we only provided 1
    }

    @Test
    public void testBindingsForSelfReferencingType() {
        JavaType javaListType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType javaIntType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType javaListOfIntType = TypeFactory.defaultInstance().constructType(List.class, javaIntType);
        TypeBindings bindings = TypeBindings.create(javaListOfIntType.getRawClass(), javaListType, javaIntType);
        assertEquals("<T, java.util.List<T>>", bindings.toString());
        assertEquals(javaListType, bindings.findBoundType("T"));
    }
}