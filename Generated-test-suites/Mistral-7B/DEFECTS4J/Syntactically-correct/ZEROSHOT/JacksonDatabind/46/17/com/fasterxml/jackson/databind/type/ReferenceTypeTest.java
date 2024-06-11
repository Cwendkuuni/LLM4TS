package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<String> stringClass = String.class;
        JavaType stringType = JavaType.construct(String.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, stringType, null, null);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(stringType.hashCode(), referenceType.getHashCode());
        assertEquals(stringType, referenceType.getReferencedType());
        assertFalse(referenceType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType stringType = JavaType.construct(String.class);
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(stringClass, stringType, null, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(referenceType.getHashCode(), newReferenceType.getHashCode());
        assertEquals(stringType, newReferenceType.getReferencedType());
        assertNotSame(referenceType, newReferenceType);
        assertSame(typeHandler, newReferenceType.getTypeHandler());
        assertFalse(newReferenceType.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType stringType = JavaType.construct(String.class);
        JavaType stringTypeWithHandler = stringType.withTypeHandler(new Object());
        ReferenceType referenceType = new ReferenceType(stringClass, stringTypeWithHandler, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(new Object());
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(referenceType.getHashCode(), newReferenceType.getHashCode());
        assertEquals(stringTypeWithHandler, newReferenceType.getReferencedType());
        assertNotSame(referenceType, newReferenceType);
        assertSame(newReferenceType.getReferencedType().getTypeHandler(), newReferenceType.getContentTypeHandler());
        assertFalse(newReferenceType.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        Class<String> stringClass = String.class;
        JavaType stringType = JavaType.construct(String.class);
        Object valueHandler = new Object();
        ReferenceType referenceType = new ReferenceType(stringClass, stringType, null, null, false);
        ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(referenceType.getHashCode(), newReferenceType.getHashCode());
        assertEquals(stringType, newReferenceType.getReferencedType());
        assertNotSame(referenceType, newReferenceType);
        assertSame(valueHandler, newReferenceType.getValueHandler());
        assertFalse(newReferenceType.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<String> stringClass = String.class;
        JavaType stringType = JavaType.construct(String.class);
        Object contentValueHandler = new Object();
        ReferenceType referenceType = new ReferenceType(stringClass, stringType, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(contentValueHandler);
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(referenceType.getHashCode(), newReferenceType.getHashCode());
        assertEquals(stringType, newReferenceType.getReferencedType());
        assertNotSame(referenceType, newReferenceType);
        assertSame(contentValueHandler, newReferenceType.getContentValueHandler());
        assertFalse(newReferenceType.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        Class<String> stringClass = String.class;
        JavaType stringType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, stringType, null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(referenceType.getHashCode(), newReferenceType.getHashCode());
        assertEquals(stringType, newReferenceType.getReferencedType());
        assertTrue(newReferenceType.isStatic());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<String> stringClass = String.class;
        JavaType stringType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, stringType, null, null, false);
        String canonicalName = referenceType.buildCanonicalName();
        assertTrue(canonicalName.startsWith("[reference type, class " + stringClass.getName() + "]"));
        assertTrue(canonicalName.endsWith("<T>"));
    }

    @Test
    public void testIsReferenceType() {
        Class<String> stringClass = String.class;
        JavaType stringType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, stringType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        Class<String> stringClass = String.class;
        JavaType stringType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, stringType, null, null, false);
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        Class<String> stringClass = String.class;
        JavaType stringType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, stringType, null, null, false);
        JavaType containedType = referenceType.containedType(0);
        assertEquals(stringType, containedType);
    }

    @Test
    public void testContainedTypeName() {
        Class<String> stringClass = String.class;
        JavaType stringType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, stringType, null, null, false);
        String containedTypeName = referenceType.containedTypeName(0);
        assertEquals("T", containedTypeName);
    }

    @Test
    public void testGetParameterSource() {
        Class<String> stringClass = String.class;
        JavaType stringType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, stringType, null, null, false);
        assertEquals(stringClass, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> stringClass = String.class;
        JavaType stringType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, stringType, null, null, false);
        StringBuilder sb = new StringBuilder();
        referenceType.getErasedSignature(sb);
        assertTrue(sb.toString().startsWith("Ljava/lang/String;"));
    }

    @Test
    public void testGetGenericSignature() {
        Class<String> stringClass = String.class;
        JavaType stringType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, stringType, null, null, false);
        StringBuilder sb = new StringBuilder();
        referenceType.getGenericSignature(sb);
        assertTrue(sb.toString().startsWith("[Ljava/lang/String;<T>"));
    }

    @Test
    public void testToString() {
        Class<String> stringClass = String.class;
        JavaType stringType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, stringType, null, null, false);
        String toString = referenceType.toString();
        assertTrue(toString.startsWith("[reference type, class " + stringClass.getName()));
        assertTrue(toString.endsWith("]"));
    }

    @Test
    public void testEquals() {
        Class<String> stringClass = String.class;
        JavaType stringType = JavaType.construct(String.class);
        JavaType otherStringType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, stringType, null, null, false);
        Object otherReferenceType = new ReferenceType(stringClass, otherStringType, null, null, false);
        assertFalse(referenceType.equals(null));
        assertFalse(referenceType.equals(""));
        assertFalse(referenceType.equals(new Object()));
        assertTrue(referenceType.equals(referenceType));
        assertFalse(referenceType.equals(otherReferenceType));
        assertFalse(referenceType.equals(stringType));
        assertFalse(referenceType.equals(otherStringType));
    }
}