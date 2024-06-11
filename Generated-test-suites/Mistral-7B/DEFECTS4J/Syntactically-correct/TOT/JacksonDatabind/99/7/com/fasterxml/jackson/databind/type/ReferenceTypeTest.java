package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testConstructWithReferencedTypeAndSuperType() {
        Class<String> stringClass = String.class;
        JavaType referencedType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType referenceType = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), null, new JavaType[0], referencedType);

        assertEquals(referenceType.getClass(), String.class);
        assertEquals(referenceType.getContentType(), int.class);
        assertFalse(referenceType.isStatic());
    }

    @Test
    public void testUpgradeFrom() {
        Class<String> stringClass = String.class;
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType referenceType = ReferenceType.upgradeFrom(simpleType, referencedType);

        assertEquals(referenceType.getClass(), String.class);
        assertEquals(referenceType.getContentType(), int.class);
        assertFalse(referenceType.isStatic());
    }

    @Test
    public void testUpgradeFromWithNullReferencedType() {
        Class<String> stringClass = String.class;
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);

        try {
            ReferenceType.upgradeFrom(simpleType, null);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetContentType() {
        Class<String> stringClass = String.class;
        JavaType referencedType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType referenceType = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), null, new JavaType[0], referencedType);

        assertEquals(referenceType.getContentType(), referencedType);
    }

    @Test
    public void testIsReferenceType() {
        Class<String> stringClass = String.class;
        JavaType referencedType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType referenceType = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), null, new JavaType[0], referencedType);

        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        Class<String> stringClass = String.class;
        JavaType referencedType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType referenceType = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), null, new JavaType[0], referencedType);

        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testGetAnchorType() {
        Class<String> stringClass = String.class;
        JavaType referencedType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType referenceType = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), null, new JavaType[0], referencedType);

        assertNull(referenceType.getAnchorType());
        ReferenceType anchorType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], referenceType);
        assertEquals(referenceType.getAnchorType(), anchorType);
    }

    @Test
    public void testIsAnchorType() {
        Class<String> stringClass = String.class;
        JavaType referencedType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType referenceType = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), null, new JavaType[0], referencedType);
        ReferenceType anchorType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], referenceType);

        assertFalse(referenceType.isAnchorType());
        assertTrue(anchorType.isAnchorType());
    }
}