package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        assertEquals(cls, referenceType.getClass());
        assertEquals(refType.hashCode(), referenceType.hashCode());
        assertEquals(refType, referenceType.getReferencedType());
        assertFalse(referenceType.isStatic());
    }

    @Test
    public void testGetContentType() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testIsReferenceType() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        Object newTypeHandler = new Object();
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        ReferenceType newReferenceType = referenceType.withTypeHandler(newTypeHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(newTypeHandler, newReferenceType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        Object newContentTypeHandler = new Object();
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(newContentTypeHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(newContentTypeHandler, newReferenceType.getReferencedType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        Object newValueHandler = new Object();
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        ReferenceType newReferenceType = referenceType.withValueHandler(newValueHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(newValueHandler, newReferenceType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        Object newContentValueHandler = new Object();
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(newContentValueHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(newContentValueHandler, newReferenceType.getReferencedType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertNotSame(referenceType, newReferenceType);
        assertTrue(newReferenceType.isStatic());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        String canonicalName = referenceType.buildCanonicalName();
        String expectedCanonicalName = "[reference type, class java.lang.String<int>]";
        assertEquals(expectedCanonicalName, canonicalName);
    }

    @Test
    public void testNarrow() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        JavaType subclass = new SimpleType(long.class);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        JavaType narrowedType = referenceType.narrow(subclass);
        assertEquals(subclass, narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidSubclass() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        JavaType invalidSubclass = new SimpleType(double.class);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        referenceType.narrow(invalidSubclass);
    }

    @Test
    public void testGetReferencedType() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testEquals() {
        Class<?> cls1 = String.class;
        JavaType refType1 = new SimpleType(int.class);
        JavaType refType2 = new SimpleType(long.class);
        ReferenceType referenceType1 = ReferenceType.construct(cls1, refType1, null, null);
        Class<?> cls2 = Integer.class;
        JavaType refType3 = new SimpleType(int.class);
        ReferenceType referenceType2 = ReferenceType.construct(cls2, refType3, null, null);
        ReferenceType referenceType3 = new ReferenceType(cls1, refType1, null, null, false);
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType3));
        assertFalse(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(new Object()));
    }
}