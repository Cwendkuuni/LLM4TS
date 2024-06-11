package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import com.fasterxml.jackson.databind.type.TypeBindings;

import org.junit.Test;

/**
 * Test class for {@link ResolvedRecursiveType}.
 */
public class ResolvedRecursiveTypeTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        assertNull(type.getSelfReferencedType());
    }

    /**
     * Test setReference method.
     */
    @Test
    public void testSetReference() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType baseType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new SimpleType(String.class);

        baseType.setReference(refType);
        ResolvedRecursiveType resolvedType = (ResolvedRecursiveType) baseType;
        assertNotNull(resolvedType.getSelfReferencedType());
        assertEquals(refType, resolvedType.getSelfReferencedType());

        try {
            baseType.setReference(refType);
            fail("Should not be able to set reference twice");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    /**
     * Test getSelfReferencedType method.
     */
    @Test
    public void testGetSelfReferencedType() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType baseType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new SimpleType(String.class);
        baseType.setReference(refType);

        ResolvedRecursiveType resolvedType = (ResolvedRecursiveType) baseType;
        JavaType selfReferencedType = resolvedType.getSelfReferencedType();
        assertEquals(refType, selfReferencedType);
    }

    /**
     * Test getGenericSignature method.
     */
    @Test
    public void testGetGenericSignature() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType baseType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new SimpleType(String.class);
        baseType.setReference(refType);

        StringBuilder sb = new StringBuilder();
        baseType.getGenericSignature(sb);
        String signature = sb.toString();
        assertTrue(signature.startsWith("[recursive type; "));
        assertTrue(signature.endsWith(refType.getRawType().getName() + "]"));
    }

    /**
     * Test getErasedSignature method.
     */
    @Test
    public void testGetErasedSignature() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType baseType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new SimpleType(String.class);
        baseType.setReference(refType);

        StringBuilder sb = new StringBuilder();
        baseType.getErasedSignature(sb);
        String signature = sb.toString();
        assertEquals(refType.getRawType().getName(), signature);
    }

    /**
     * Test withContentType method.
     */
    @Test
    public void testWithContentType() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType baseType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new SimpleType(String.class);
        baseType.setReference(refType);

        JavaType contentType = new SimpleType(Integer.class);
        ResolvedRecursiveType resolvedType = (ResolvedRecursiveType) baseType.withContentType(contentType);
        assertNotNull(resolvedType.getSelfReferencedType());
        assertEquals(refType, resolvedType.getSelfReferencedType());
    }

    /**
     * Test withTypeHandler method.
     */
    @Test
    public void testWithTypeHandler() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType baseType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new SimpleType(String.class);
        baseType.setReference(refType);

        Object handler = new Object();
        ResolvedRecursiveType resolvedType = (ResolvedRecursiveType) baseType.withTypeHandler(handler);
        assertSame(handler, resolvedType.getTypeHandler());
    }

    /**
     * Test withContentTypeHandler method.
     */
    @Test
    public void testWithContentTypeHandler() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType baseType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new SimpleType(String.class);
        baseType.setReference(refType);

        Object handler = new Object();
        ResolvedRecursiveType resolvedType = (ResolvedRecursiveType) baseType.withContentTypeHandler(handler);
        assertSame(handler, resolvedType.getContentTypeHandler());
    }

    /**
     * Test withValueHandler method.
     */
    @Test
    public void testWithValueHandler() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType baseType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new SimpleType(String.class);
        baseType.setReference(refType);

        Object handler = new Object();
        ResolvedRecursiveType resolvedType = (ResolvedRecursiveType) baseType.withValueHandler(handler);
        assertSame(handler, resolvedType.getValueHandler());
    }

    /**
     * Test withContentValueHandler method.
     */
    @Test
    public void testWithContentValueHandler() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType baseType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new SimpleType(String.class);
        baseType.setReference(refType);

        Object handler = new Object();
        ResolvedRecursiveType resolvedType = (ResolvedRecursiveType) baseType.withContentValueHandler(handler);
        assertSame(handler, resolvedType.getContentValueHandler());
    }

    /**
     * Test withStaticTyping method.
     */
    @Test
    public void testWithStaticTyping() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType baseType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new SimpleType(String.class);
        baseType.setReference(refType);

        ResolvedRecursiveType resolvedType = (ResolvedRecursiveType) baseType.withStaticTyping();
        assertTrue(resolvedType.isStaticTyping());
    }

    /**
     * Test isContainerType method.
     */
    @Test
    public void testIsContainerType() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType baseType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new SimpleType(String.class);
        baseType.setReference(refType);

        assertFalse(baseType.isContainerType());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType baseType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new SimpleType(String.class);
        baseType.setReference(refType);

        String expected = "[recursive type; " + refType.getRawType().getName() + "]";
        String actual = baseType.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType baseType1 = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new SimpleType(String.class);
        baseType1.setReference(refType);

        ResolvedRecursiveType baseType2 = new ResolvedRecursiveType(erasedType, bindings);
        baseType2.setReference(refType);

        ResolvedRecursiveType baseType3 = new ResolvedRecursiveType(erasedType, bindings);
        baseType3.setReference(new SimpleType(Integer.class));

        assertTrue(baseType1.equals(baseType1));
        assertTrue(baseType1.equals(baseType2));
        assertFalse(baseType1.equals(baseType3));
    }
}