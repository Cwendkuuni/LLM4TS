package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveType_DefaultConstruction() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        assertNull(resolvedRecursiveType.getSelfReferencedType());
        assertEquals("[$type; UNRESOLVED]", resolvedRecursiveType.toString());
    }

    @Test
    public void testResolvedRecursiveType_SetReference() {
        JavaType referenceType = new TypeReference<Object[]>() {}.getType();

        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object[].class, null);
        resolvedRecursiveType.setReference(referenceType);

        JavaType selfReferencedType = resolvedRecursiveType.getSelfReferencedType();
        assertEquals(referenceType.getClass(), selfReferencedType.getClass());
        assertEquals(referenceType, selfReferencedType);
        assertEquals("[recursive type; [" + referenceType.getRawClass().getName() + "]]", resolvedRecursiveType.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testResolvedRecursiveType_SetReference_MultipleTimes() {
        JavaType referenceType = new TypeReference<Object[]>() {}.getType();

        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object[].class, null);
        resolvedRecursiveType.setReference(referenceType);

        try {
            resolvedRecursiveType.setReference(referenceType);
        } finally {
            // Swallow the exception as JUnit will do it for us
        }
        fail("IllegalStateException not thrown");
    }

    @Test
    public void testResolvedRecursiveType_GetSignatures() {
        JavaType referenceType = new TypeReference<Object[]>() {}.getType();

        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object[].class, null);
        resolvedRecursiveType.setReference(referenceType);

        StringBuilder stringBuilder = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(stringBuilder);
        assertEquals("[Ljava.lang.Object;]", stringBuilder.toString());

        stringBuilder = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(stringBuilder);
        assertEquals("[Ljava.lang.Object;", stringBuilder.toString());
    }

    @Test
    public void testResolvedRecursiveType_WithTypeHandlers() {
        JavaType referenceType = new TypeReference<Object[]>() {}.getType();

        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object[].class, null);
        resolvedRecursiveType.setReference(referenceType);

        Object handler = new Object() {};

        resolvedRecursiveType = resolvedRecursiveType.withContentTypeHandler(handler);
        assertTrue(resolvedRecursiveType instanceof ResolvedRecursiveType);

        resolvedRecursiveType = resolvedRecursiveType.withValueHandler(handler);
        assertTrue(resolvedRecursiveType instanceof ResolvedRecursiveType);

        resolvedRecursiveType = resolvedRecursiveType.withContentValueHandler(handler);
        assertTrue(resolvedRecursiveType instanceof ResolvedRecursiveType);
    }

    @Test
    public void testResolvedRecursiveType_Equals() {
        JavaType referenceType1 = new TypeReference<Object[]>() {}.getType();
        JavaType referenceType2 = new TypeReference<Object[]>() {}.getType();

        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(Object[].class, null);
        resolvedRecursiveType1.setReference(referenceType1);

        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(Object[].class, null);
        resolvedRecursiveType2.setReference(referenceType2);

        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));

        ResolvedRecursiveType resolvedRecursiveType3 = new ResolvedRecursiveType(Object[].class, null);
        resolvedRecursiveType3.setReference(referenceType1);

        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType3));
    }
}