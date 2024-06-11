package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new ClassType(String.class);

        resolvedRecursiveType.setReference(ref);
        JavaType expected = ref;
        JavaType actual = resolvedRecursiveType.getSelfReferencedType();

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref1 = new ClassType(String.class);
        JavaType ref2 = new ClassType(Integer.class);

        resolvedRecursiveType.setReference(ref1);
        resolvedRecursiveType.setReference(ref2);
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new ClassType(String.class);
        resolvedRecursiveType.setReference(ref);

        JavaType expected = ref;
        JavaType actual = resolvedRecursiveType.getSelfReferencedType();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new ClassType(String.class);
        resolvedRecursiveType.setReference(ref);
        StringBuilder expected = new StringBuilder("Ljava/lang/String;");
        StringBuilder actual = resolvedRecursiveType.getGenericSignature(new StringBuilder());

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new ClassType(String.class);
        resolvedRecursiveType.setReference(ref);
        StringBuilder expected = new StringBuilder("Ljava/lang/String;");
        StringBuilder actual = resolvedRecursiveType.getErasedSignature(new StringBuilder());

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testEquals() {
        Class<?> erasedType1 = Object.class;
        TypeBindings bindings1 = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType1, bindings1);
        JavaType ref1 = new ClassType(String.class);
        resolvedRecursiveType1.setReference(ref1);

        Class<?> erasedType2 = Object.class;
        TypeBindings bindings2 = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType2, bindings2);
        JavaType ref2 = new ClassType(String.class);
        resolvedRecursiveType2.setReference(ref2);

        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType1));
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
        assertFalse(resolvedRecursiveType1.equals(null));
        assertFalse(resolvedRecursiveType1.equals(""));
        assertFalse(resolvedRecursiveType1.equals(new Object()));

        resolvedRecursiveType1.setReference(null);
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));

        resolvedRecursiveType2.setReference(new ClassType(Integer.class));
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }
}