package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstructor() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, refType);
        assertEquals(Integer.class, referenceType.getRawClass());
        assertEquals(String.class, referenceType.getContentType().getRawClass());
        assertSame(refType, referenceType.getReferencedType());
    }

    @Test
    public void testReferenceTypeUpgradeFrom() {
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(JavaType.class);
        JavaType referenceType = ReferenceType.upgradeFrom(simpleType, refType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(JavaType.class, referenceType.getContentType().getRawClass());
        assertSame(refType, referenceType.getReferencedType());
    }

    @Test
    public void testGetContentType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, refType);
        assertEquals(String.class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, refType);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, refType);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, refType);
        assertSame(refType, referenceType.getReferencedType());
    }

    @Test
    public void testGetAnchorType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, refType);
        assertNotNull(referenceType.getAnchorType());
        assertEquals(referenceType.getClass(), referenceType.getAnchorType().getClass());
    }

    @Test
    public void testIsAnchorType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, refType);
        assertFalse(referenceType.isAnchorType());

        JavaType anchorType = ReferenceType.construct(referenceType.getClass(), refType);
        assertTrue(anchorType.isAnchorType());
    }

    @Test
    public void testWithContentType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, refType);

        JavaType newContentType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType newReferenceType = referenceType.withContentType(newContentType);
        assertEquals(Integer.class, newReferenceType.getContentType().getRawClass());
        assertSame(referenceType.getReferencedType(), newReferenceType.getReferencedType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, refType);
        Object handler = new Object();
        JavaType newReferenceType = referenceType.withTypeHandler(handler);
        assertSame(handler, newReferenceType.<Object>getTypeHandler());
        assertSame(referenceType.getReferencedType(), newReferenceType.getReferencedType());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, refType);
        Object handler = new Object();
        JavaType newReferenceType = referenceType.withContentTypeHandler(handler);
        assertSame(handler, newReferenceType.<Object>getValueHandler());
        assertSame(referenceType.getReferencedType(), newReferenceType.getReferencedType());
    }

    @Test
    public void testWithValueHandler() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, refType);
        Object handler = new Object();
        JavaType newReferenceType = referenceType.withValueHandler(handler);
        assertSame(handler, newReferenceType.<Object>getValueHandler());
        assertSame(referenceType.getReferencedType(), newReferenceType.getReferencedType());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, refType);
        Object handler = new Object();
        JavaType newReferenceType = referenceType.withContentValueHandler(handler);
        assertSame(handler, newReferenceType.<Object>getValueHandler());
        assertSame(referenceType.getReferencedType(), newReferenceType.getReferencedType());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, refType);
        JavaType newReferenceType = referenceType.withStaticTyping();
        assertTrue(newReferenceType.isStatic());
        assertSame(referenceType.getReferencedType(), newReferenceType.getReferencedType());
    }

    @Test
    public void testRefine() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(JavaType.class);
        JavaType referenceType = ReferenceType.construct(baseType.getClass(), refType);
        JavaType newReferenceType = referenceType.refine(String[].class, TypeBindings.emptyBindings(), null, null);
        assertEquals(String[].class, newReferenceType.getRawClass());
        assertSame(refType, newReferenceType.getReferencedType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeWithNullReferencedType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(baseType.getClass(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeWithInvalidBaseType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(JavaType.class);
        JavaType referenceType = ReferenceType.construct(new Object().getClass(), refType);
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, refType);
        assertEquals("java.lang.Integer[java.lang.String]", referenceType.buildCanonicalName());
    }
}