package com.example.test;

import org.junit.*;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testConstructor() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertEquals(erasedType, resolvedRecursiveType.getErasedType());
        assertEquals(bindings, resolvedRecursiveType.getBindings());
    }

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new ResolvedRecursiveType(Integer.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        assertNotNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceException() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new ResolvedRecursiveType(Integer.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        resolvedRecursiveType.setReference(ref);
    }

    @Test
    public void testGetSelfReferencedType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new ResolvedRecursiveType(Integer.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        assertNotNull(resolvedRecursiveType.getSelfReferencedType());
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetSelfReferencedTypeNullReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetReferenceNullRef() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new ResolvedRecursiveType(Integer.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        resolvedRecursiveType.setReference(ref);
    }
}