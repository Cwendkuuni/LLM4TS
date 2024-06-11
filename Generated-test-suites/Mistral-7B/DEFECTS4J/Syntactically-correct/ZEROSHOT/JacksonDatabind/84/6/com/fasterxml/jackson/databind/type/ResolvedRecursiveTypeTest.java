package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

/**
 * Unit tests for {@link ResolvedRecursiveType} class.
 */
public class ResolvedRecursiveTypeTest {

    @Test
    public void testInstanciation() {
        // Test empty constructor
        ResolvedRecursiveType empty = new ResolvedRecursiveType(null, null);
        assertNotNull(empty);
        assertEquals("null type", empty.getErasedType().getName());
        assertNull(empty.getSelfReferencedType());

        // Test constructor with erased type and bindings
        Class<?> type = Object.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(type);
        ResolvedRecursiveType instance = new ResolvedRecursiveType(type, null);
        assertEquals(type.getName(), instance.getErasedType().getName());
        assertNull(instance.getSelfReferencedType());
    }

    @Test
    public void testSetReference() {
        Class<?> type = Object.class;
        JavaType referencedType = TypeFactory.defaultInstance().constructType(type);
        ResolvedRecursiveType self = new ResolvedRecursiveType(type, null);
        self.setReference(referencedType);
        JavaType result = self.getSelfReferencedType();
        assertEquals(referencedType.getClass(), result.getClass());
        assertEquals(referencedType, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<?> type = Object.class;
        JavaType referencedType = TypeFactory.defaultInstance().constructType(type);
        ResolvedRecursiveType self = new ResolvedRecursiveType(type, null);
        self.setReference(referencedType);
        self.setReference(referencedType);
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> type = Object.class;
        JavaType referencedType = TypeFactory.defaultInstance().constructType(type);
        ResolvedRecursiveType self = new ResolvedRecursiveType(type, null);
        self.setReference(referencedType);
        StringBuilder expected = new StringBuilder("Ljava/lang/Object;[");
        StringBuilder result = self.getGenericSignature(new StringBuilder());
        assertEquals(expected, result);
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> type = Object.class;
        JavaType referencedType = TypeFactory.defaultInstance().constructType(type);
        ResolvedRecursiveType self = new ResolvedRecursiveType(type, null);
        self.setReference(referencedType);
        StringBuilder expected = new StringBuilder(type.getName().replaceAll("\\.", "[L") + ";");
        StringBuilder result = self.getErasedSignature(new StringBuilder());
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        Class<?> type = Object.class;
        JavaType referencedType1 = TypeFactory.defaultInstance().constructType(type);
        JavaType referencedType2 = TypeFactory.defaultInstance().constructType(type);
        ResolvedRecursiveType self1 = new ResolvedRecursiveType(type, null);
        ResolvedRecursiveType self2 = new ResolvedRecursiveType(type, null);
        ResolvedRecursiveType self3 = new ResolvedRecursiveType(type, null);
        self1.setReference(referencedType1);
        self2.setReference(referencedType2);

        // self1 == self1
        assertTrue(self1.equals(self1));

        // self1 == self2 (different referenced types)
        assertFalse(self1.equals(self2));

        // self1 == self3 (same state)
        self3.setReference(referencedType1);
        assertTrue(self1.equals(self3));
    }
}