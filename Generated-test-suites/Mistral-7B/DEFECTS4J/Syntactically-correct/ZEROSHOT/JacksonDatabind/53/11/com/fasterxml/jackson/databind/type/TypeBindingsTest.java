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
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType("None"));
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateWithNoTypeArguments() {
        TypeBindings bindings = TypeBindings.create(List.class);
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType("None"));
        assertTrue(bindings.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithTooFewTypeArguments() {
        TypeBindings.create(List.class, null);
    }

    @Test
    public void testCreateWithOneTypeArgument() {
        JavaType intType = JavaType.constructUnboundedType(int.class);
        TypeBindings bindings = TypeBindings.create(List.class, intType);
        assertEquals(1, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(intType, bindings.getBoundType(0));
        assertNull(bindings.findBoundType("None"));
    }

    @Test
    public void testCreateWithTwoTypeArguments() {
        JavaType intType = JavaType.constructUnboundedType(int.class);
        JavaType stringType = JavaType.constructUnboundedType(String.class);
        TypeBindings bindings = TypeBindings.create(List.class, intType, stringType);
        assertEquals(2, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(intType, bindings.getBoundType(0));
        assertEquals("java.lang.String", bindings.getBoundName(1));
        assertEquals(stringType, bindings.getBoundType(1));
        assertNull(bindings.findBoundType("None"));
    }

    @Test
    public void testCreateWithUnboundTypeVariable() {
        JavaType intType = JavaType.constructUnboundedType(int.class);
        JavaType listType = JavaType.constructParameterizedType(List.class, intType);
        TypeBindings bindings = TypeBindings.create(List.class, listType);
        assertEquals(1, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(intType, bindings.getBoundType(0));
        assertNull(bindings.findBoundType("None"));
        assertFalse(bindings.hasUnbound("Integer"));
    }

    @Test
    public void testCreateWithUnboundTypeVariableAndUnboundParameter() {
        JavaType intType = JavaType.constructUnboundedType(int.class);
        JavaType stringType = JavaType.constructUnboundedType(String.class);
        JavaType listType = JavaType.constructParameterizedType(List.class, intType);
        TypeBindings bindings = TypeBindings.create(List.class, listType, stringType);
        assertEquals(2, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(intType, bindings.getBoundType(0));
        assertEquals("java.lang.String", bindings.getBoundName(1));
        assertEquals(stringType, bindings.getBoundType(1));
        assertTrue(bindings.hasUnbound("None"));
    }

    @Test
    public void testFindBoundType() {
        JavaType intType = JavaType.constructUnboundedType(int.class);
        JavaType stringType = JavaType.constructUnboundedType(String.class);
        JavaType listType = JavaType.constructParameterizedType(List.class, intType);
        TypeBindings bindings = TypeBindings.create(List.class, listType, stringType);
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(intType, bindings.getBoundType(0));
        assertEquals("java.lang.String", bindings.getBoundName(1));
        assertEquals(stringType, bindings.getBoundType(1));
        assertNull(bindings.findBoundType("None"));
        assertEquals(intType, bindings.findBoundType("Integer"));
    }

    @Test
    public void testToString() {
        JavaType intType = JavaType.constructUnboundedType(int.class);
        JavaType stringType = JavaType.constructUnboundedType(String.class);
        JavaType listType = JavaType.constructParameterizedType(List.class, intType);
        TypeBindings bindings = TypeBindings.create(List.class, listType, stringType);
        String expected = "<List<java.lang.Integer, java.lang.String>>";
        assertEquals(expected, bindings.toString());
    }

    @Test
    public void testHashCodeAndEquals() {
        JavaType intType = JavaType.constructUnboundedType(int.class);
        JavaType intType2 = JavaType.constructUnboundedType(int.class);
        JavaType stringType = JavaType.constructUnboundedType(String.class);
        JavaType listType = JavaType.constructParameterizedType(List.class, intType);
        JavaType listType2 = JavaType.constructParameterizedType(List.class, intType2);
        JavaType listType3 = JavaType.constructParameterizedType(List.class, stringType);
        TypeBindings bindings1 = TypeBindings.create(List.class, listType);
        TypeBindings bindings2 = TypeBindings.create(List.class, listType);
        TypeBindings bindings3 = TypeBindings.create(List.class, listType2);
        TypeBindings bindings4 = TypeBindings.create(List
                .class, listType3);
        assertEquals(bindings1, bindings1);
        assertEquals(bindings1, bindings2);
        assertEquals(bindings1.hashCode(), bindings2.hashCode());
        assertNotEquals(bindings1, bindings3);
        assertNotEquals(bindings1, bindings4);
    }

    @Test
    public void testWithUnboundVariable() {
        JavaType intType = JavaType.constructUnboundedType(int.class);
        JavaType stringType = JavaType.constructUnboundedType(String.class);
        JavaType listType = JavaType.constructParameterizedType(List.class, intType);
        TypeBindings bindings = TypeBindings.create(List.class, listType, stringType);
        TypeBindings bindingsWithUnboundVariable = bindings.withUnboundVariable("None");
        assertTrue(bindingsWithUnboundVariable.hasUnbound("None"));
        assertFalse(bindings.hasUnbound("None"));
    }
}