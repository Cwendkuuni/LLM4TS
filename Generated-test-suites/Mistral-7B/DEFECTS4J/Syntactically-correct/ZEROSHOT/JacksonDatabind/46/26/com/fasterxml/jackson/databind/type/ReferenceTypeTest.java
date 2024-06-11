package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.type.ReferenceType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class ReferenceTypeTest {

    @Test
    public void testReferenceType() {
        JavaType refType = JavaTypeFactory.instance.constructType(String[].class);
        ReferenceType referenceType = ReferenceType.construct(String.class, refType, null, null);

        // Test getters
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(refType, referenceType.getContentType());

        // Test overridden methods
        ReferenceType newReferenceType = referenceType.withTypeHandler(null);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(refType, newReferenceType.getContentType());

        newReferenceType = newReferenceType.withContentTypeHandler(null);
        assertNotSame(referenceType, newReferenceType);
        assertNotSame(refType, newReferenceType.getContentType());

        newReferenceType = newReferenceType.withValueHandler(null);
        assertNotSame(referenceType, newReferenceType);
        assertSame(referenceType.getRawClass(), newReferenceType.getRawClass());

        newReferenceType = newReferenceType.withContentValueHandler(null);
        assertNotSame(referenceType, newReferenceType);
        assertNotSame(refType, newReferenceType.getContentType());

        newReferenceType = newReferenceType.withStaticTyping();
        assertTrue(newReferenceType.isStatic());

        // Test Hashcode
        assertEquals(referenceType.hashCode(), newReferenceType.hashCode());

        // Test equals
        ReferenceType anotherReferenceType = ReferenceType.construct(String.class, refType, null, null);
        assertTrue(referenceType.equals(anotherReferenceType));

        SimpleType simpleType = JavaTypeFactory.instance.constructType(String.class);
        assertFalse(referenceType.equals(simpleType));

        // Test buildCanonicalName
        assertEquals("class java.lang.String<[Ljava.lang.String;>", referenceType.buildCanonicalName());

        // Test narrow
        JavaType narrowType = referenceType.narrow(int[].class);
        assertEquals(ReferenceType.class, narrowType.getClass());
        assertEquals(int[].class, ((ReferenceType) narrowType).getContentType().getRawClass());

        // Test getReferencedType and isReferenceType
        assertTrue(referenceType.isReferenceType());
        assertEquals(refType, referenceType.getReferencedType());

        // Test containedTypeCount and containedType
        assertEquals(1, referenceType.containedTypeCount());
        assertEquals(refType, referenceType.containedType(0));

        // Test getParameterSource, getErasedSignature and getGenericSignature
        assertEquals(String.class, referenceType.getParameterSource());
        String erasedSignature = "[reference type, class java.lang.String<java.lang.String[]>]";
        StringBuilder expectedErasedSignature = new StringBuilder(erasedSignature.length());
        referenceType.getErasedSignature(expectedErasedSignature);
        assertEquals(erasedSignature, expectedErasedSignature.toString());

        StringBuilder expectedGenericSignature = new StringBuilder(26);
        expectedGenericSignature.append("[referencetype class java.lang.String<");
        expectedGenericSignature.append("class java.lang.String<java.lang.String[]>>]");
        String genericSignature = referenceType.getGenericSignature().toString();
        assertEquals(expectedGenericSignature.toString(), genericSignature);
    }

    @Test
    public void testReferenceTypeWithValueHandler() {
        JavaType refType = JavaTypeFactory.instance.constructType(String[].class);
        Object valueHandler = new Object() {};
        ReferenceType referenceType = ReferenceType.construct(String.class, refType, valueHandler, null);

        // Test getters
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(refType, referenceType.getContentType());
        assertEquals(valueHandler, referenceType.getValueHandler());

        // Test overridden methods
        ReferenceType newReferenceType = referenceType.withTypeHandler(null);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(refType, newReferenceType.getContentType());
        assertSame(valueHandler, newReferenceType.getValueHandler());

        newReferenceType = newReferenceType.withContentTypeHandler(null);
        assertNotSame(referenceType, newReferenceType);
        assertNotSame(refType, newReferenceType.getContentType());
        assertSame(valueHandler, newReferenceType.getValueHandler());

        newReferenceType = newReferenceType.withValueHandler(null);
        assertNotSame(referenceType, newReferenceType);
        assertSame(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertNull(newReferenceType.getValueHandler());

        newReferenceType = newReferenceType.withContentValueHandler(null);
        assertNotSame(referenceType, newReferenceType);
        assertNotSame(refType, newReferenceType.getContentType());
        assertNull(newReferenceType.getValueHandler());

        newReferenceType = newReferenceType.withStaticTyping();
        assertTrue(newReferenceType.isStatic());

        // Test Hashcode
        assertEquals(referenceType.hashCode(), newReferenceType.hashCode());

        // Test equals
        ReferenceType anotherReferenceType = ReferenceType.construct(String.class, refType, valueHandler, null);
        assertTrue(referenceType.equals(anotherReferenceType));

        SimpleType simpleType = JavaTypeFactory.instance.constructType(String.class);
        assertFalse(referenceType.equals(simpleType));

        // Test buildCanonicalName
        assertEquals("class java.lang.String<[Ljava.lang.String;>", referenceType.buildCanonicalName());

        // Test narrow
        JavaType narrowType = referenceType.narrow(int[].class);
        assertEquals(ReferenceType.class, narrowType.getClass());
        assertEquals(int[].class, ((ReferenceType) narrowType).getContentType().getRawClass());

        // Test getReferencedType and isReferenceType
        assertTrue(referenceType.isReferenceType());
        assertEquals(refType, referenceType.getReferencedType());

        // Test containedTypeCount and containedType
        assertEquals(1, referenceType.containedTypeCount());
        assertEquals(refType, referenceType.containedType(0));

        // Test getParameterSource, getErasedSignature and getGenericSignature
        assertEquals(String.class, referenceType.getParameterSource());
        String erasedSignature = "[referencetype class java.lang.String<java.lang.String[]>]";
        StringBuilder expectedErasedSignature = new StringBuilder(erasedSignature.length());
        referenceType.getErasedSignature(expectedErasedSignature);
        assertEquals(erasedSignature, expectedErasedSignature.toString());

        StringBuilder expectedGenericSignature = new StringBuilder(26);
        expectedGenericSignature.append("[referencetype class java.lang.String<");
        expectedGenericSignature.append("class java.lang.String<java.lang.String[]>>]");
        String genericSignature = referenceType.getGenericSignature().toString();
        assertEquals(expectedGenericSignature.toString(), genericSignature);
    }

    @Test
    public void testReferenceTypeWithContentTypeHandler() {
        JavaType refType = JavaTypeFactory.instance.constructType(String[].class);
        JavaType contentTypeHandler = JavaTypeFactory.instance.constructType(String.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, refType, null, contentTypeHandler);

        // Test getters
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(refType, referenceType.getContentType());
        assertEquals(contentTypeHandler, referenceType.getContentTypeHandler());

        // Test overridden methods
        ReferenceType newReferenceType = referenceType.withTypeHandler(null);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(refType, newReferenceType.getContentType());
        assertEquals(contentTypeHandler, newReferenceType.getContentTypeHandler());

        newReferenceType = newReferenceType.withContentTypeHandler(null);
        assertNotSame(referenceType, newReferenceType);
        assertNotSame(refType, newReferenceType.getContentType());
        assertNull(newReferenceType.getContentTypeHandler());

        newReferenceType = newReferenceType.withValueHandler(null);
        assertNotSame(referenceType, newReferenceType);
        assertSame(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertNull(newReferenceType.getValueHandler());

        newReferenceType = newReferenceType.withContentValueHandler(null);
        assertNotSame(referenceType, newReferenceType);
        assertNotSame(refType, newReferenceType.getContentType());
        assertNull(newReferenceType.getValueHandler());

        newReferenceType = newReferenceType.withStaticTyping();
        assertTrue(newReferenceType.isStatic());

        // Test Hashcode
        assertEquals(referenceType.hashCode(), newReferenceType.hashCode());

        // Test equals
        ReferenceType anotherReferenceType = ReferenceType.construct(String.class, refType, null, contentTypeHandler);
        assertTrue(referenceType.equals(anotherReferenceType));

        SimpleType simpleType = JavaTypeFactory.instance.constructType(String.class);
        assertFalse(referenceType.equals(simpleType));

        // Test buildCanonicalName
        assertEquals("class java.lang.String<[Ljava.lang.String;>", referenceType.buildCanonicalName());

        // Test narrow
        JavaType narrowType = referenceType.narrow(int[].class);
        assertEquals(ReferenceType.class, narrowType.getClass());
        assertEquals(int[].class, ((ReferenceType) narrowType).getContentType().getRawClass());

        // Test getReferencedType and isReferenceType
        assertTrue(referenceType.isReferenceType());
        assertEquals(refType, referenceType.getReferencedType());

        // Test containedTypeCount and containedType
        assertEquals(1, referenceType.containedTypeCount());
        assertEquals(refType, referenceType.containedType(0));

        // Test getParameterSource, getErasedSignature and getGenericSignature
        assertEquals(String.class, referenceType.getParameterSource());
        String erasedSignature = "[referencetype class java.lang.String<java.lang.String[]>]";
        StringBuilder expectedErasedSignature = new StringBuilder(erasedSignature.length());
        referenceType.getErasedSignature(expectedErasedSignature);
        assertEquals(erasedSignature, expectedErasedSignature.toString());

        StringBuilder expectedGenericSignature = new StringBuilder(26);
        expectedGenericSignature.append("[referencetype class java.lang.String<");
        expectedGenericSignature.append("class java.lang.String<java.lang.String[]>>]");
        String genericSignature = referenceType.getGenericSignature().toString();
        assertEquals(expectedGenericSignature.toString(), genericSignature);
    }
}