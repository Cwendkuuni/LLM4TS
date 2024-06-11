package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReferenceTypeTest {

    private JavaType simpleIntType;
    private JavaType referenceIntType;

    @Before
    public void setup() {
        simpleIntType = TypeReference.getType(int[].class);
        referenceIntType = ReferenceType.construct(int[].class, null, null, null);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType upgradedType = ReferenceType.upgradeFrom(simpleIntType, referenceIntType);
        assertEquals(referenceIntType, upgradedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNullRefdType() {
        JavaType upgradedType = ReferenceType.upgradeFrom(simpleIntType, null);
    }

    @Test
    public void testGetContentType() {
        assertEquals(referenceIntType.getContentType(), referenceIntType._referencedType);
    }

    @Test
    public void testGetReferencedType() {
        assertEquals(referenceIntType.getReferencedType(), referenceIntType._referencedType);
    }

    @Test
    public void testHasContentType() {
        assertTrue(referenceIntType.hasContentType());
    }

    @Test
    public void testIsReferenceType() {
        assertTrue(referenceIntType.isReferenceType());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        referenceIntType.getErasedSignature(sb);
        assertEquals("[Ljava/lang/Object;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        referenceIntType.getGenericSignature(sb);
        assertEquals("[Ljava/lang/Object;<T;>]", sb.toString());
    }

    @Test
    public void testWithContentType() {
        JavaType newContentType = TypeReference.getType(String[].class);
        ReferenceType newReferenceType = (ReferenceType) referenceIntType.withContentType(newContentType);
        assertEquals(newContentType, newReferenceType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        ReferenceType newReferenceType = (ReferenceType) referenceIntType.withTypeHandler(handler);
        assertNotNull(newReferenceType._typeHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        ReferenceType newReferenceType = (ReferenceType) referenceIntType.withContentTypeHandler(handler);
        assertNotNull(newReferenceType._referencedType._typeHandler);
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        ReferenceType newReferenceType = (ReferenceType) referenceIntType.withValueHandler(handler);
        assertNotNull(newReferenceType._valueHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        ReferenceType newReferenceType = (ReferenceType) referenceIntType.withContentValueHandler(handler);
        assertNotNull(newReferenceType._referencedType._valueHandler);
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType newReferenceType = (ReferenceType) referenceIntType.withStaticTyping();
        assertTrue(newReferenceType._asStatic);
    }

    @Test
    public void testRefine() {
        JavaType refinedType = referenceIntType.refine(int[].class, null, null, null);
        assertEquals(referenceIntType, refinedType);
    }

    @Test
    public void testBuildCanonicalName() {
        assertEquals("[Ljava/lang/Object;<Ljava/lang/Integer;>;>", referenceIntType.buildCanonicalName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType baseType = TypeReference.getType(String.class);
        ReferenceType upgradedType = ReferenceType.upgradeFrom(baseType, referenceIntType);
    }
}