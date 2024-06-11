package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import com.fasterxml.jackson.databind.type.TypeBindings;

import org.junit.Test;

/**
 * Tests for {@link ResolvedRecursiveType} class.
 */
public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveTypeConstruction() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(erasedType, bindings);
        assertNull(resolvedType.getSelfReferencedType());
    }

    @Test
    public void testSetSelfReferencedType() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new TypeFactory().constructType(int[].class);

        resolvedType.setReference(refType);
        JavaType selfReferencedType = resolvedType.getSelfReferencedType();
        assertSame(refType, selfReferencedType);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetSelfReferencedTypeTwice() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new TypeFactory().constructType(int[].class);

        resolvedType.setReference(refType);
        resolvedType.setReference(refType);
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new TypeFactory().constructType(int[].class);
        resolvedType.setReference(refType);

        StringBuilder sb = new StringBuilder();
        resolvedType.getGenericSignature(sb);
        assertSame(refType.getGenericSignature(sb).toString(), sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new TypeFactory().constructType(int[].class);
        resolvedType.setReference(refType);

        StringBuilder sb = new StringBuilder();
        resolvedType.getErasedSignature(sb);
        assertSame(refType.getErasedSignature(sb).toString(), sb.toString());
    }

    @Test
    public void testGetSelfReferencedTypeWhenNull() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(erasedType, bindings);

        assertNull(resolvedType.getSelfReferencedType());
    }

    @Test
    public void testIsContainerType() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(erasedType, bindings);

        assertFalse(resolvedType.isContainerType());
    }

    @Test
    public void testToString() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new TypeFactory().constructType(int[].class);
        resolvedType.setReference(refType);

        String stringRepresentation = resolvedType.toString();
        StringBuilder expectedStringRepresentation = new StringBuilder("[recursive type; [I]");
        expectedStringRepresentation.append(refType.getRawClass().getName());

        assertSame(expectedStringRepresentation.toString(), stringRepresentation);
    }

    @Test
    public void testEquals() {
        Class<?> erasedType1 = ResolvedRecursiveType.class;
        TypeBindings bindings1 = new TypeBindings();
        ResolvedRecursiveType resolvedType1 = new ResolvedRecursiveType(erasedType1, bindings1);

        Class<?> erasedType2 = ResolvedRecursiveType.class;
        TypeBindings bindings2 = new TypeBindings();
        ResolvedRecursiveType resolvedType2 = new ResolvedRecursiveType(erasedType2, bindings2);

        JavaType refType = new TypeFactory().constructType(int[].class);
        resolvedType1.setReference(refType);
        resolvedType2.setReference(refType);

        Object obj = new Object();
        assertFalse(resolvedType1.equals(obj));

        ResolvedRecursiveType resolvedType3 = new ResolvedRecursiveType(erasedType1, bindings1);
        assertFalse(resolvedType1.equals(resolvedType3));

        ResolvedRecursiveType resolvedType4 = new ResolvedRecursiveType(erasedType1, bindings1);
        assertTrue(resolvedType1.equals(resolvedType4));

        ResolvedRecursiveType resolvedType5 = new ResolvedRecursiveType(erasedType2, bindings2);
        assertFalse(resolvedType1.equals(resolvedType5));
    }

    @Test
    public void testEqualsWithSameSelfReferencedType() {
        Class<?> erasedType1 = ResolvedRecursiveType.class;
        TypeBindings bindings1 = new TypeBindings();
        ResolvedRecursiveType resolvedType1 = new ResolvedRecursiveType(erasedType1, bindings1);

        JavaType refType = new TypeFactory().constructType(int[].class);
        resolvedType1.setReference(refType);

        Class<?> erasedType2 = ResolvedRecursiveType.class;
        TypeBindings bindings2 = new TypeBindings();
        ResolvedRecursiveType resolvedType2 = new ResolvedRecursiveType(erasedType2, bindings2);
        resolvedType2.setReference(refType);

        assertTrue(resolvedType1.equals(resolvedType2));
    }

    @Test
    public void testEqualsWithDifferentSelfReferencedTypes() {
        Class<?> erasedType1 = ResolvedRecursiveType.class;
        TypeBindings bindings1 = new TypeBindings();
        ResolvedRecursiveType resolvedType1 = new ResolvedRecursiveType(erasedType1, bindings1);

        JavaType refType1 = new TypeFactory().constructType(int[].class);
        resolvedType1.setReference(refType1);

        JavaType refType2 = new TypeFactory().constructType(String.class);
        ResolvedRecursiveType resolvedType2 = new ResolvedRecursiveType(erasedType1, bindings1);
        resolvedType2.setReference(refType2);

        assertFalse(resolvedType1.equals(resolvedType2));
    }
}