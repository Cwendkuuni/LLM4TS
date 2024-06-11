package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeReference;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeCreation() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Object.class, refType, null, null);
        assertEquals("Object[type, String]", referenceType.toString());
        assertEquals("java.lang.Object", referenceType.getRawClass().getName());
        assertEquals("java.lang.String", referenceType.getContentType().getRawClass().getName());
    }

    @Test
    public void testReferenceTypeWithTypeHandler() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Object.class, refType, null, null);
        JavaType withTypeHandler = referenceType.withTypeHandler(new JavaTypeFactory().constructType(Integer.class));
        assertEquals("Object[type, String, java.lang.Integer]", withTypeHandler.toString());
    }

    @Test
    public void testReferenceTypeWithContentTypeHandler() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Object.class, refType, null, null);
        JavaType withContentTypeHandler = referenceType.withContentTypeHandler(new JavaTypeFactory().constructType(Integer[].class));
        assertEquals("Object[type, [I]", withContentTypeHandler.toString());
    }

    @Test
    public void testReferenceTypeWithValueHandler() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Object.class, refType, null, null);
        JavaType withValueHandler = referenceType.withValueHandler(new JavaTypeFactory().constructType(Integer.class));
        assertEquals("Object[type, java.lang.Integer]", withValueHandler.toString());
    }

    @Test
    public void testReferenceTypeWithContentValueHandler() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Object.class, refType, null, null);
        JavaType withContentValueHandler = referenceType.withContentValueHandler(new JavaTypeFactory().constructType(Integer[].class));
        assertEquals("Object[type, [I]", withContentValueHandler.toString());
    }

    @Test
    public void testReferenceTypeWithStaticTyping() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Object.class, refType, null, null);
        JavaType withStaticTyping = referenceType.withStaticTyping();
        assertEquals("java.lang.Object[type, java.lang.String]", withStaticTyping.toString());
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Object.class, refType, null, null);
        JavaType referencedType = referenceType.getReferencedType();
        assertEquals("java.lang.String", referencedType.getRawClass().getName());
    }

    @Test
    public void testIsReferenceType() {
        JavaType referenceType = TypeFactory.defaultInstance().constructType(Object.class);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testCanonicalName() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Object.class, refType, null, null);
        assertEquals("java.lang.Object[type, java.lang.String]", referenceType.buildCanonicalName());
    }

    @Test
    public void testNarrow() {
        JavaType refType1 = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType refType2 = TypeFactory.defaultInstance().constructType(Long.class);
        JavaType referenceType = ReferenceType.construct(Object.class, refType1, null, null);
        JavaType narrowType = referenceType._narrow(Integer.class);
        assertEquals("java.lang.Object[type, java.lang.Integer]", narrowType.toString());
    }

    @Test
    public void testContainsType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Object.class, refType, null, null);
        JavaType[] containedTypes = referenceType.containedTypes();
        assertEquals(1, containedTypes.length);
        JavaType containedType = containedTypes[0];
        assertEquals("java.lang.String", containedType.getRawClass().getName());
    }

    @Test
    public void testGetParameterSource() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Object.class, refType, null, null);
        assertEquals(Object.class, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Object.class, refType, null, null);
        StringBuilder signature = new StringBuilder();
        referenceType.getErasedSignature(signature);
        assertEquals("Object", signature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Object.class, refType, null, null);
        StringBuilder signature = new StringBuilder();
        referenceType.getGenericSignature(signature);
        assertEquals("Object<T>", signature.toString());
    }

    @Test
    public void testEquals() {
        JavaType refType1 = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType refType2 = TypeFactory.defaultInstance().constructType(Long.class);
        JavaType referenceType1 = ReferenceType.construct(Object.class, refType1, null, null);
        JavaType referenceType2 = ReferenceType.construct(Object.class, refType2, null, null);
        JavaType referenceType3 = ReferenceType.construct(Object.class, refType1, null, null);
        assertTrue(referenceType1.equals(referenceType3));
        assertFalse(referenceType1.equals(referenceType2));
    }

}