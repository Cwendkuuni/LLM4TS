package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testReferenceType() {
        Class<Integer> intClass = Integer.class;
        JavaType javaTypeRef = JavaType.construct(String.class);
        ReferenceType referenceType = ReferenceType.construct(intClass, javaTypeRef, null, null);

        // Test getClass()
        assertEquals(intClass, referenceType.getClass());

        // Test getContentType()
        assertEquals(javaTypeRef, referenceType.getContentType());

        // Test withTypeHandler()
        JavaType typeHandlerRef = JavaType.construct(String[].class);
        ReferenceType withTypeHandler = referenceType.withTypeHandler(typeHandlerRef);
        assertEquals(typeHandlerRef, withTypeHandler.getTypeHandler());

        // Test withContentTypeHandler()
        JavaType valueHandlerRef = JavaType.construct(Integer[].class);
        ReferenceType withContentTypeHandler = withTypeHandler.withContentTypeHandler(valueHandlerRef);
        assertEquals(valueHandlerRef, withContentTypeHandler.getValueHandler());

        // Test withValueHandler()
        ReferenceType withValueHandler = withContentTypeHandler.withValueHandler(JavaType.construct(Float.class));
        assertEquals(JavaType.construct(Float.class), withValueHandler.getValueHandler());

        // Test withContentValueHandler()
        JavaType contentValueHandlerRef = JavaType.construct(Character[].class);
        ReferenceType withContentValueHandler = withValueHandler.withContentValueHandler(contentValueHandlerRef);
        assertEquals(contentValueHandlerRef, withContentValueHandler.getContentValueHandler());

        // Test withStaticTyping()
        ReferenceType withStaticTyping = withContentValueHandler.withStaticTyping();
        assertTrue(withStaticTyping.isStatic());

        // Test buildCanonicalName()
        assertEquals("[reference type, class java.lang.String, class java.lang.String]", withTypeHandler.buildCanonicalName());

        // Test narrow()
        Class<Long> longClass = Long.class;
        JavaType narrow = referenceType.narrow(longClass);
        assertEquals(new ReferenceType(longClass, javaTypeRef, null, null), narrow);

        // Test getReferencedType()
        assertEquals(javaTypeRef, referenceType.getReferencedType());

        // Test isReferenceType()
        assertTrue(referenceType.isReferenceType());

        // Test containedTypeCount()
        assertEquals(1, referenceType.containedTypeCount());

        // Test containedType()
        assertEquals(javaTypeRef, referenceType.containedType(0));

        // Test containedTypeName()
        assertEquals("T", referenceType.containedTypeName(0));

        // Test getParameterSource()
        assertEquals(intClass, referenceType.getParameterSource());

        // Test getErasedSignature()
        StringBuilder sbErased = new StringBuilder(50);
        referenceType.getErasedSignature(sbErased);
        assertEquals("[class java.lang.Integer]", sbErased.toString());

        // Test getGenericSignature()
        StringBuilder sbGeneric = new StringBuilder(100);
        referenceType.getGenericSignature(sbGeneric);
        assertEquals("[reference type, class java.lang.Integer, class java.lang.String]", sbGeneric.toString());

        // Test toString()
        assertEquals("[reference type, class java.lang.Integer, class java.lang.String]", referenceType.toString());

        // Test equals()
        ReferenceType referenceType1 = new ReferenceType(intClass, javaTypeRef, null, null, false);
        assertTrue(referenceType.equals(referenceType1));
        ReferenceType referenceType2 = new ReferenceType(longClass, javaTypeRef, null, null, false);
        assertFalse(referenceType.equals(referenceType2));
    }
}