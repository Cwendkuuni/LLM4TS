package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = ReferenceType.construct(int[].class, TypeBindings.emptyBindings(), null, null, refType);
        assertEquals(int[].class, referenceType.getRawClass());
        assertEquals(String.class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals(int[].class, referenceType.getRawClass());
        assertEquals(String.class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testGetContentType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, null, refType);
        assertEquals(String.class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testIsReferenceType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, null, refType);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, null, refType);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testGetReferencedType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, null, refType);
        assertEquals(String.class, referenceType.getReferencedType().getRawClass());
    }

    @Test
    public void testToString() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, null, refType);
        String expected = "[reference type, class [I<String>]";
        assertEquals(expected, referenceType.toString());
    }

    @Test
    public void testEquals() {
        JavaType baseType1 = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType refType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType baseType2 = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType refType2 = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType1 = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, null, refType1);
        ReferenceType referenceType2 = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, null, refType2);
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));
        ReferenceType referenceType3 = new ReferenceType(String[].class, TypeBindings.emptyBindings(), null, null, refType1);
        assertFalse(referenceType1.equals(referenceType3));
    }

    @Test
    public void testGetAnchorType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, null, refType);
        assertNull(referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, null, refType);
        assertFalse(referenceType.isAnchorType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullRefType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType.construct(int[].class, TypeBindings.emptyBindings(), null, null, null);
    }
}