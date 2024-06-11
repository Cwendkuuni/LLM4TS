package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeCreation() {
        JavaType referencedType = new ClassType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, referencedType, null, null, false);
        assertNotNull(referenceType);
        assertEquals(Integer.class, referenceType.getRawClass());
        assertEquals(referencedType.getClass(), referenceType.getContentType().getClass());
        assertFalse(referenceType.isStatic());
    }

    @Test
    public void testReferenceTypeWithTypeHandler() {
        JavaType referencedType = new ClassType(String.class);
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(Integer.class, referencedType, null, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(typeHandler, newReferenceType.getTypeHandler());
    }

    @Test
    public void testReferenceTypeWithContentTypeHandler() {
        JavaType referencedType = new ClassType(String.class);
        Object contentTypeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(Integer.class, referencedType, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(contentTypeHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(contentTypeHandler, newReferenceType.getReferencedType().getTypeHandler());
    }

    @Test
    public void testReferenceTypeWithValueHandler() {
        JavaType referencedType = new ClassType(String.class);
        Object valueHandler = new Object();
        ReferenceType referenceType = new ReferenceType(Integer.class, referencedType, null, null, false);
        ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(valueHandler, newReferenceType.getValueHandler());
    }

    @Test
    public void testReferenceTypeWithContentValueHandler() {
        JavaType referencedType = new ClassType(String.class);
        Object contentValueHandler = new Object();
        ReferenceType referenceType = new ReferenceType(Integer.class, referencedType, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(contentValueHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(contentValueHandler, newReferenceType.getReferencedType().getValueHandler());
    }

    @Test
    public void testReferenceTypeWithStaticTyping() {
        JavaType referencedType = new ClassType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, referencedType, null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertNotSame(referenceType, newReferenceType);
        assertTrue(newReferenceType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidReferenceTypeCreation() {
        JavaType referencedType = new ClassType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, referencedType, null, null, false);
    }

    @Test
    public void testGetReferencedType() {
        JavaType referencedType = new ClassType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, referencedType, null, null, false);
        assertEquals(referencedType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType referencedType = new ClassType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, referencedType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        JavaType referencedType = new ClassType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, referencedType, null, null, false);
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType referencedType = new ClassType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, referencedType, null, null, false);
        assertSame(referencedType, referenceType.containedType(0));
    }

    @Test
    public void testContainedTypeName() {
        JavaType referencedType = new ClassType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, referencedType, null, null, false);
        assertNull(referenceType.containedTypeName(1));
    }

    @Test
    public void testGetParameterSource() {
        JavaType referencedType = new ClassType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, referencedType, null, null, false);
        assertEquals(Integer.class, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType referencedType = new ClassType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, referencedType, null, null, false);
        StringBuilder expectedSignature = new StringBuilder();
        assertEquals(expectedSignature.toString(), referenceType.getErasedSignature(expectedSignature).toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType referencedType = new ClassType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, referencedType, null, null, false);
        StringBuilder expectedSignature = new StringBuilder();
        assertEquals(expectedSignature.append("[reference type, class ").append(Integer.class.getName()).append("<").append(String.class.getName()).append(">]").toString(), referenceType.getGenericSignature(expectedSignature).toString());
    }

    @Test
    public void testToString() {
        JavaType referencedType = new ClassType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, referencedType, null, null, false);
        assertEquals("[reference type, class java.lang.Integer<java.lang.String>]", referenceType.toString());
    }

    @Test
    public void testEquals() {
        JavaType referencedTypeA = new ClassType(String.class);
        JavaType referencedTypeB = new ClassType(String.class);
        ReferenceType referenceTypeA = new ReferenceType(Integer.class, referencedTypeA, null, null, false);
        ReferenceType referenceTypeB = new ReferenceType(Integer.class, referencedTypeB, null, null, false);
        ReferenceType referenceTypeC = new ReferenceType(Integer.class, referencedTypeA, null, null, false);
        assertEquals(referenceTypeA, referenceTypeA);
        assertEquals(referenceTypeA, referenceTypeC);
        assertNotEquals(referenceTypeA, referenceTypeB);
    }
}