package com.example.junit4test;

import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {
    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType javaType = new SimpleType(int.class);
        resolvedRecursiveType.setReference(javaType);
        JavaType selfReferencedType = resolvedRecursiveType.getSelfReferencedType();
        assertEquals(javaType, selfReferencedType);
    }

    @Test
    public void testSetReferenceMultipleTimes() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType javaType = new SimpleType(int.class);
        resolvedRecursiveType.setReference(javaType);
        try {
            resolvedRecursiveType.setReference(javaType);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testSetNullReferencedType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        resolvedRecursiveType.setReference(null);
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testSetDifferentClassType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType javaType1 = new SimpleType(int.class);
        JavaType javaType2 = new SimpleType(String.class);
        resolvedRecursiveType.setReference(javaType1);
        try {
            resolvedRecursiveType.setReference(javaType2);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testGetSelfReferencedTypeNullReferencedType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetSignaturesWithNullStringBuilder() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        StringBuilder sb = null;
        try {
            resolvedRecursiveType.getGenericSignature(sb);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            resolvedRecursiveType.getErasedSignature(sb);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}