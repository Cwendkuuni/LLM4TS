package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for ResolvedRecursiveType.
 */
public class ResolvedRecursiveTypeTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Class<ResolvedRecursiveType> resolvedRecursiveTypeClass = ResolvedRecursiveType.class;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        assertNotNull(resolvedRecursiveType);
        assertEquals(Object.class, resolvedRecursiveType.getErasedType());
        assertEquals(null, resolvedRecursiveType.getSelfReferencedType());
    }

    /**
     * Test setReference method.
     */
    @Test
    public void testSetReference() {
        Class<ResolvedRecursiveType> resolvedRecursiveTypeClass = ResolvedRecursiveType.class;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType javaType = new SimpleType(int[].class);

        resolvedRecursiveType.setReference(javaType);
        ResolvedRecursiveType expectedResolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        expectedResolvedRecursiveType.setReference(javaType);

        assertEquals(expectedResolvedRecursiveType, resolvedRecursiveType);
        assertEquals(javaType, resolvedRecursiveType.getSelfReferencedType());

        // Test exception when trying to re-set self reference
        try {
            resolvedRecursiveType.setReference(javaType);
        } catch (IllegalStateException e) {
            String expectedMessage = "Trying to re-set self reference; old value = " + javaType + ", new = " + javaType;
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    /**
     * Test getSelfReferencedType method.
     */
    @Test
    public void testGetSelfReferencedType() {
        Class<ResolvedRecursiveType> resolvedRecursiveTypeClass = ResolvedRecursiveType.class;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType javaType = new SimpleType(int[].class);
        resolvedRecursiveType.setReference(javaType);

        JavaType expectedSelfReferencedType = javaType;
        JavaType actualSelfReferencedType = resolvedRecursiveType.getSelfReferencedType();

        assertEquals(expectedSelfReferencedType, actualSelfReferencedType);
    }

    /**
     * Test getGenericSignature method.
     */
    @Test
    public void testGetGenericSignature() {
        Class<ResolvedRecursiveType> resolvedRecursiveTypeClass = ResolvedRecursiveType.class;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType javaType = new SimpleType(int[].class);
        resolvedRecursiveType.setReference(javaType);

        StringBuilder expectedSb = new StringBuilder("java.lang.int[]");
        StringBuilder actualSb = new StringBuilder(32);
        resolvedRecursiveType.getGenericSignature(actualSb);

        assertEquals(expectedSb.toString(), actualSb.toString());
    }

    /**
     * Test getErasedSignature method.
     */
    @Test
    public void testGetErasedSignature() {
        Class<ResolvedRecursiveType> resolvedRecursiveTypeClass = ResolvedRecursiveType.class;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType javaType = new SimpleType(int[].class);
        resolvedRecursiveType.setReference(javaType);

        StringBuilder expectedSb = new StringBuilder("java.lang.Object");
        StringBuilder actualSb = new StringBuilder(16);
        resolvedRecursiveType.getErasedSignature(actualSb);

        assertEquals(expectedSb.toString(), actualSb.toString());
    }

    /**
     * Test withContentType method.
     */
    @Test
    public void testWithContentType() {
        Class<ResolvedRecursiveType> resolvedRecursiveTypeClass = ResolvedRecursiveType.class;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType javaType = new SimpleType(int[].class);
        resolvedRecursiveType.setReference(javaType);
        JavaType contentType = new SimpleType(String.class);

        ResolvedRecursiveType expectedResolvedRecursiveType = resolvedRecursiveType;
        ResolvedRecursiveType actualResolvedRecursiveType = resolvedRecursiveType.withContentType(contentType);

        assertEquals(expectedResolvedRecursiveType, actualResolvedRecursiveType);
        assertSame(resolvedRecursiveType, actualResolvedRecursiveType.getSelfReferencedType());
    }

    /**
     * Test withTypeHandler method.
     */
    @Test
    public void testWithTypeHandler() {
        Class<ResolvedRecursiveType> resolvedRecursiveTypeClass = ResolvedRecursiveType.class;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType javaType = new SimpleType(int[].class);
        resolvedRecursiveType.setReference(javaType);
        Object typeHandler = new Object();

        ResolvedRecursiveType expectedResolvedRecursiveType = resolvedRecursiveType;
        ResolvedRecursiveType actualResolvedRecursiveType = resolvedRecursiveType.withTypeHandler(typeHandler);

        assertEquals(expectedResolvedRecursiveType, actualResolvedRecursiveType);
        assertSame(resolvedRecursiveType, actualResolvedRecursiveType.getSelfReferencedType());
    }

    /**
     * Test withContentTypeHandler method.
     */
    @Test
    public void testWithContentTypeHandler() {
        Class<ResolvedRecursiveType> resolvedRecursiveTypeClass = ResolvedRecursiveType.class;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType javaType = new SimpleType(int[].class);
        resolvedRecursiveType.setReference(javaType);
        Object contentTypeHandler = new Object();

        ResolvedRecursiveType expectedResolvedRecursiveType = resolvedRecursiveType;
        ResolvedRecursiveType actualResolvedRecursiveType = resolvedRecursiveType.withContentTypeHandler(contentTypeHandler);

        assertEquals(expectedResolvedRecursiveType, actualResolvedRecursiveType);
        assertSame(resolvedRecursiveType, actualResolvedRecursiveType.getSelfReferencedType());
    }

    /**
     * Test withValueHandler method.
     */
    @Test
    public void testWithValueHandler() {
        Class<ResolvedRecursiveType> resolvedRecursiveTypeClass = ResolvedRecursiveType.class;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType javaType = new SimpleType(int[].class);
        resolvedRecursiveType.setReference(javaType);
        Object valueHandler = new Object();

        ResolvedRecursiveType expectedResolvedRecursiveType = resolvedRecursiveType;
        ResolvedRecursiveType actualResolvedRecursiveType = resolvedRecursiveType.withValueHandler(valueHandler);

        assertEquals(expectedResolvedRecursiveType, actualResolvedRecursiveType);
        assertSame(resolvedRecursiveType, actualResolvedRecursiveType.getSelfReferencedType());
    }

    /**
     * Test withContentValueHandler method.
     */
    @Test
    public void testWithContentValueHandler() {
        Class<ResolvedRecursiveType> resolvedRecursiveTypeClass = ResolvedRecursiveType.class;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType javaType = new SimpleType(int[].class);
        resolvedRecursiveType.setReference(javaType);
        Object contentValueHandler = new Object();

        ResolvedRecursiveType expectedResolvedRecursiveType = resolvedRecursiveType;
        ResolvedRecursiveType actualResolvedRecursiveType = resolvedRecursiveType.withContentValueHandler(contentValueHandler);

        assertEquals(expectedResolvedRecursiveType, actualResolvedRecursiveType);
        assertSame(resolvedRecursiveType, actualResolvedRecursiveType.getSelfReferencedType());
    }

    /**
     * Test withStaticTyping method.
     */
    @Test
    public void testWithStaticTyping() {
        Class<ResolvedRecursiveType> resolvedRecursiveTypeClass = ResolvedRecursiveType.class;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType javaType = new SimpleType(int[].class);
        resolvedRecursiveType.setReference(javaType);

        ResolvedRecursiveType expectedResolvedRecursiveType = resolvedRecursiveType;
        ResolvedRecursiveType actualResolvedRecursiveType = resolvedRecursiveType.withStaticTyping();

        assertEquals(expectedResolvedRecursiveType, actualResolvedRecursiveType);
        assertSame(resolvedRecursiveType, actualResolvedRecursiveType.getSelfReferencedType());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        Class<ResolvedRecursiveType> resolvedRecursiveTypeClass = ResolvedRecursiveType.class;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType javaType = new SimpleType(int[].class);
        resolvedRecursiveType.setReference(javaType);

        String expectedString = "[recursive type; UNRESOLVED]";
        String actualString = resolvedRecursiveType.toString();

        assertEquals(expectedString, actualString);

        javaType = new SimpleType(String.class);
        resolvedRecursiveType.setReference(javaType);
        expectedString = "[recursive type; " + javaType.getRawClass().getName() + "]";
        actualString = resolvedRecursiveType.toString();

        assertEquals(expectedString, actualString);
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        Class<ResolvedRecursiveType> resolvedRecursiveTypeClass = ResolvedRecursiveType.class;
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(Object.class, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(Object.class, null);
        JavaType javaType = new SimpleType(int[].class);
        resolvedRecursiveType1.setReference(javaType);

        assertFalse(resolvedRecursiveType1.equals(null));
        assertFalse(resolvedRecursiveType1.equals(new Object()));
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));

        resolvedRecursiveType2.setReference(javaType);
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }
}