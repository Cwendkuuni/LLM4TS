package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test for {@link ReferenceType} class.
 */
public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        // Test basic construction
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(int[].class);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        assertNotNull(referenceType);
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(refType.getRawClass(), referenceType.getContentType().getRawClass());
    }

    @Test
    public void testReferenceTypeSetters() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(int[].class);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);

        // Test withTypeHandler
        Object handler = new Object();
        ReferenceType newReferenceType = referenceType.withTypeHandler(handler);
        assertNotNull(newReferenceType);
        assertEquals(referenceType.getContentType(), newReferenceType.getContentType());
        assertEquals(handler, newReferenceType.getTypeHandler());

        // Test withContentTypeHandler
        JavaType newContentType = ClassType.construct(long[].class);
        ReferenceType newReferenceType2 = referenceType.withContentTypeHandler(newContentType);
        assertNotNull(newReferenceType2);
        assertEquals(referenceType.getContentType(), newReferenceType2.getContentType());
        assertEquals(newContentType, newReferenceType2.getContentType());

        // Test withValueHandler
        Object valueHandler = new Object();
        ReferenceType newReferenceType3 = referenceType.withValueHandler(valueHandler);
        assertNotNull(newReferenceType3);
        assertEquals(referenceType.getValueHandler(), newReferenceType3.getValueHandler());
        assertEquals(valueHandler, newReferenceType3.getValueHandler());

        // Test withContentValueHandler
        Object contentValueHandler = new Object();
        ReferenceType newReferenceType4 = referenceType.withContentValueHandler(contentValueHandler);
        assertNotNull(newReferenceType4);
        assertEquals(referenceType.getContentType().getValueHandler(), newReferenceType4.getContentType().getValueHandler());
        assertEquals(contentValueHandler, newReferenceType4.getContentType().getValueHandler());

        // Test withStaticTyping
        ReferenceType newReferenceType5 = referenceType.withStaticTyping();
        assertNotNull(newReferenceType5);
        assertEquals(!referenceType._asStatic, newReferenceType5._asStatic);
    }

    @Test
    public void testReferenceTypeMethods() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(int[].class);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);

        // Test getReferencedType
        JavaType getReferencedType = referenceType.getReferencedType();
        assertNotNull(getReferencedType);
        assertEquals(refType, getReferencedType);

        // Test isReferenceType
        assertTrue(referenceType.isReferenceType());
    }
}