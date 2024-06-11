package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SimpleModule;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testUpgradeFrom() throws Exception {
        JavaType simpleType = SimpleType.construct(String.class, null);
        JavaType referenceType = ReferenceType.upgradeFrom(simpleType, String[].class);
        assertEquals("Expected String[] as referenced type", String[].class, referenceType.getContentType());
        assertEquals("Expected String as base type", String.class, referenceType.getBaseType());
    }

    @Test
    public void testConstruct() throws Exception {
        JavaType referenceType = ReferenceType.construct(String[].class, null, null, new TypeReference[]{}, String.class);
        JavaType contentType = referenceType.withContentType(Integer.class);
        assertEquals("Expected Integer as content type", Integer.class, contentType.getContentType());
        assertEquals("Expected String[] as referenced type", String[].class, referenceType.getContentType());
        assertEquals("Expected String[] as type", String[].class, referenceType.getType());
    }

    @Test
    public void testWithContentType() throws Exception {
        JavaType stringType = SimpleType.construct(String.class, null);
        JavaType referenceType = new ReferenceType(stringType, null);
        JavaType integerType = SimpleType.construct(Integer.class, null);
        JavaType newReferenceType = referenceType.withContentType(integerType);
        assertEquals("Expected Integer as content type", Integer.class, newReferenceType.getContentType());
        assertEquals("Expected String as base type", String.class, newReferenceType.getBaseType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNullRefType() throws Exception {
        JavaType simpleType = SimpleType.construct(String.class, null);
        ReferenceType.upgradeFrom(simpleType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNonReferenceType() throws Exception {
        JavaType simpleType = SimpleType.construct(Integer.class, null);
        ReferenceType.upgradeFrom(simpleType, String.class);
    }

    @Test
    public void testGetContentType() throws Exception {
        JavaType referenceType = ReferenceType.construct(String[].class, null, null, new TypeReference[]{}, String.class);
        assertEquals("Expected String[] as referenced type", String[].class, referenceType.getContentType());
    }

    @Test
    public void testIsReferenceType() throws Exception {
        JavaType referenceType = ReferenceType.construct(String[].class, null, null, new TypeReference[]{}, String.class);
        assertTrue("Expected true for reference type", referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() throws Exception {
        JavaType referenceType = ReferenceType.construct(String[].class, null, null, new TypeReference[]{}, String.class);
        assertTrue("Expected true for reference type with content type", referenceType.hasContentType());
    }

    @Test
    public void testGetReferencedType() throws Exception {
        JavaType referenceType = ReferenceType.construct(String[].class, null, null, new TypeReference[]{}, String.class);
        assertEquals("Expected String[] as referenced type", String[].class, referenceType.getReferencedType());
    }

    @Test
    public void testToString() throws Exception {
        JavaType referenceType = ReferenceType.construct(String[].class, null, null, new TypeReference[]{}, String.class);
        String expectedString = "[reference type, class [Ljava.lang.String;<T> tinyint:358218515491872, referencedType class [Ljava.lang.String;]]";
        assertEquals("Expected correct string representation", expectedString, referenceType.toString());
    }

    @Test
    public void testEquals() throws Exception {
        JavaType referenceType1 = ReferenceType.construct(String[].class, null, null, new TypeReference[]{}, String.class);
        JavaType referenceType2 = ReferenceType.construct(String[].class, null, null, new TypeReference[]{}, String.class);
        assertTrue("Expected true for equals method", referenceType1.equals(referenceType2));
    }

    @Test
    public void testEqualsDifferentClass() throws Exception {
        JavaType referenceType1 = ReferenceType.construct(String[].class, null, null, new TypeReference[]{}, String.class);
        JavaType simpleType = SimpleType.construct(Integer.class, null);
        assertFalse("Expected false for equals method", referenceType1.equals(simpleType));
    }

    @Test
    public void testGetAnchorType() throws Exception {
        JavaType referenceType = ReferenceType.construct(String[].class, null, null, new TypeReference[]{}, String.class);
        assertEquals("Expected referenced type as anchor type", referenceType, referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() throws Exception {
        JavaType referenceType = ReferenceType.construct(String[].class, null, null, new TypeReference[]{}, String.class);
        assertTrue("Expected true for isAnchorType method", referenceType.isAnchorType());
    }
}