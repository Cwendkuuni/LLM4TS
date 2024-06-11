package com.example.junit;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = new SimpleType(simpleClass);
        assertEquals(simpleClass, simpleType.getRawClass());
        assertNull(simpleType.getSuperclass());
        assertArrayEquals(new Class<?>[0], simpleType.getSuperTypes());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testSimpleTypeConstructionWithSuperClass() {
        Class<?> simpleClass = String.class;
        Class<?> superClass = Object.class;
        SimpleType simpleType = new SimpleType(simpleClass, null, superClass, null);
        assertEquals(simpleClass, simpleType.getRawClass());
        assertSame(superClass, simpleType.getSuperclass());
        assertArrayEquals(new Class<?>[0], simpleType.getSuperTypes());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testSimpleTypeConstructionWithBindings() {
        Class<?> simpleClass = List.class;
        Class<?> elementType = Integer.class;
        SimpleType simpleType = new SimpleType(simpleClass, null, null, null, null, null, false, Arrays.asList(new JavaType[] { new SimpleType(elementType) }));
        assertEquals(simpleClass, simpleType.getRawClass());
        assertNull(simpleType.getSuperclass());
        assertArrayEquals(new Class<?>[] { elementType }, simpleType.getSuperTypes());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<?> simpleClass = String.class;
        SimpleType originalSimpleType = new SimpleType(simpleClass);
        SimpleType copiedSimpleType = new SimpleType(originalSimpleType);
        assertEquals(simpleClass, copiedSimpleType.getRawClass());
        assertEquals(originalSimpleType.getSuperclass(), copiedSimpleType.getSuperclass());
        assertArrayEquals(originalSimpleType.getSuperTypes(), copiedSimpleType.getSuperTypes());
        assertSame(originalSimpleType.getValueHandler(), copiedSimpleType.getValueHandler());
        assertSame(originalSimpleType.getTypeHandler(), copiedSimpleType.getTypeHandler());
        assertEquals(originalSimpleType.isStatic(), copiedSimpleType.isStatic());
    }

    @Test
    public void testSimpleTypeUnsafeConstruction() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(simpleClass);
        assertEquals(simpleClass, simpleType.getRawClass());
        assertNull(simpleType.getSuperclass());
        assertArrayEquals(new Class<?>[0], simpleType.getSuperTypes());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeInvalidConstruction() {
        Class<?> simpleClass = List.class;
        SimpleType simpleType = new SimpleType(simpleClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeNarrowingToInvalidSubclass() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = new SimpleType(simpleClass);
        Class<?> invalidSubclass = Integer.class;
        JavaType narrowedType = simpleType.narrow(invalidSubclass);
        fail("Narrowing to invalid subclass should have thrown an exception.");
    }

    @Test
    public void testSimpleTypeBuildCanonicalName() {
        Class<?> simpleClass = List.class;
        Class<?> elementType = Integer.class;
        SimpleType simpleType = new SimpleType(simpleClass, null, null, null, null, null, false, Arrays.asList(new JavaType[] { new SimpleType(elementType) }));
        assertEquals("[class java.util.List<java.lang.Integer>]", simpleType.buildCanonicalName());
    }

    @Test
    public void testSimpleTypeEquals() {
        Class<?> simpleClass1 = List.class;
        Class<?> elementType1 = Integer.class;
        SimpleType simpleType1 = new SimpleType(simpleClass1, null, null, null, null, null, false, Arrays.asList(new JavaType[] { new SimpleType(elementType1) }));

        Class<?> simpleClass2 = ArrayList.class;
        Class<?> elementType2 = Integer.class;
        SimpleType simpleType2 = new SimpleType(simpleClass2, null, null, null, null, null, false, Arrays.asList(new JavaType[] { new SimpleType(elementType2) }));

        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType1.withTypeHandler(null)));
        assertTrue(simpleType1.equals(simpleType1.withValueHandler(null)));
        assertTrue(simpleType1.equals(simpleType1.withStaticTyping()));
        assertTrue(simpleType1.equals(simpleType1.withContentTypeHandler(null)));
        assertTrue(simpleType1.equals(simpleType1.withContentValueHandler(null)));

        assertTrue(simpleType1.equals(simpleType2));
        assertTrue(simpleType1.equals(simpleType2.withTypeHandler(null)));
        assertTrue(simpleType1.equals(simpleType2.withValueHandler(null)));
        assertTrue(simpleType1.equals(simpleType2.withStaticTyping()));
        assertTrue(simpleType1.equals(simpleType2.withContentTypeHandler(null)));
        assertTrue(simpleType1.equals(simpleType2.withContentValueHandler(null)));

        assertFalse(simpleType1.equals(null));
        assertFalse(simpleType1.equals(""));
        assertFalse(simpleType1.equals(new Object()));

        assertFalse(simpleType1.equals(new SimpleType(simpleClass1, null, null, null, null, null, false, Arrays.asList(new JavaType[] { new SimpleType(Boolean.class) }))));
        assertFalse(simpleType1.equals(new SimpleType(simpleClass1, null, null, null, null, null, false, Arrays.asList(new JavaType[] { new SimpleType(elementType1) })).withStaticTyping()));
    }

}