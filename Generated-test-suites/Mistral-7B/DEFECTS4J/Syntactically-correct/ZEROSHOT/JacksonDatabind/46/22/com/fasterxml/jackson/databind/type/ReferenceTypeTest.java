package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import com.fasterxml.jackson.databind.type.SimpleType;

import org.junit.Test;

/**
 * Test class for {@link ReferenceType}.
 */
public class ReferenceTypeTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Class<String> stringClass = String.class;
        JavaType javaType = SimpleType.constructor(String.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null, null);
        assertNotNull(referenceType);
        assertEquals(stringClass, referenceType.getRawClass());
        assertEquals(javaType.getClass(), referenceType.getContentType().getClass());
    }

    /**
     * Test withTypeHandler method.
     */
    @Test
    public void testWithTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType javaType = SimpleType.constructor(String.class);
        Object typeHandler = new Object() {};
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(stringClass, newReferenceType.getRawClass());
        assertEquals(javaType, newReferenceType.getContentType());
        assertEquals(typeHandler, newReferenceType.getTypeHandler());
    }

    /**
     * Test withContentTypeHandler method.
     */
    @Test
    public void testWithContentTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType javaType = SimpleType.constructor(String.class);
        Object contentTypeHandler = new Object() {};
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        JavaType newContentType = javaType.withTypeHandler(new Object() {});
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(contentTypeHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(stringClass, newReferenceType.getRawClass());
        assertEquals(newContentType, newReferenceType.getContentType());
        assertEquals(contentTypeHandler, newReferenceType.getReferencedType().getTypeHandler());
    }

    /**
     * Test withValueHandler method.
     */
    @Test
    public void testWithValueHandler() {
        Class<String> stringClass = String.class;
        JavaType javaType = SimpleType.constructor(String.class);
        Object valueHandler = new Object() {};
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(stringClass, newReferenceType.getRawClass());
        assertEquals(javaType, newReferenceType.getContentType());
        assertEquals(valueHandler, newReferenceType.getValueHandler());
    }

    /**
     * Test withContentValueHandler method.
     */
    @Test
    public void testWithContentValueHandler() {
        Class<String> stringClass = String.class;
        JavaType javaType = SimpleType.constructor(String.class);
        Object contentValueHandler = new Object() {};
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        JavaType newContentType = javaType.withValueHandler(new Object() {});
        ReferenceType newReferenceType = referenceType.withContentValueHandler(contentValueHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(stringClass, newReferenceType.getRawClass());
        assertEquals(newContentType, newReferenceType.getContentType());
        assertEquals(contentValueHandler, newReferenceType.getValueHandler());
    }

    /**
     * Test withStaticTyping method.
     */
    @Test
    public void testWithStaticTyping() {
        Class<String> stringClass = String.class;
        JavaType javaType = SimpleType.constructor(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertNotSame(referenceType, newReferenceType);
        assertEquals(stringClass, newReferenceType.getRawClass());
        assertEquals(javaType, newReferenceType.getContentType());
        assertTrue(newReferenceType.isStaticTyping());
    }

    /**
     * Test getReferencedType method.
     */
    @Test
    public void testGetReferencedType() {
        Class<String> stringClass = String.class;
        JavaType javaType = SimpleType.constructor(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        JavaType expectedJavaType = SimpleType.constructor(Integer.class);
        JavaType javaTypeWithTypeHandler = expectedJavaType.withTypeHandler(new Object() {});
        ReferenceType newReferenceType = new ReferenceType(String.class, javaTypeWithTypeHandler, null, null, false);
        ReferenceType referenceTypeWithReferencedType = referenceType.withReferencedType(newReferenceType);
        assertNotNull(referenceTypeWithReferencedType);
        assertEquals(stringClass, referenceTypeWithReferencedType.getRawClass());
        assertEquals(javaTypeWithTypeHandler, referenceTypeWithReferencedType.getContentType());
        assertEquals(newReferenceType.getRawClass(), referenceTypeWithReferencedType.getReferencedType().getRawClass());
        assertEquals(newReferenceType.getContentType(), referenceTypeWithReferencedType.getReferencedType().getContentType());
        assertTrue(referenceTypeWithReferencedType.isReferenceType());
    }

    /**
     * Test isReferenceType method.
     */
    @Test
    public void testIsReferenceType() {
        Class<String> stringClass = String.class;
        JavaType javaType = SimpleType.constructor(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    /**
     * Test containedTypeCount method.
     */
    @Test
    public void testContainedTypeCount() {
        Class<String> stringClass = String.class;
        JavaType javaType = SimpleType.constructor(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        assertEquals(1, referenceType.containedTypeCount());
    }

    /**
     * Test containedType method.
     */
    @Test
    public void testContainedType() {
        Class<String> stringClass = String.class;
        JavaType javaType = SimpleType.constructor(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        JavaType expectedJavaType = SimpleType.constructor(Integer.class);
        JavaType javaTypeWithTypeHandler = expectedJavaType.withTypeHandler(new Object() {});
        ReferenceType newReferenceType = new ReferenceType(String.class, javaTypeWithTypeHandler, null, null, false);
        ReferenceType referenceTypeWithReferencedType = referenceType.withReferencedType(newReferenceType);
        assertEquals(newReferenceType, referenceTypeWithReferencedType.containedType(0));
    }

    /**
     * Test containedTypeName method.
     */
    @Test
    public void testContainedTypeName() {
        Class<String> stringClass = String.class;
        JavaType javaType = SimpleType.constructor(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        JavaType expectedJavaType = SimpleType.constructor(Integer.class);
        JavaType javaTypeWithTypeHandler = expectedJavaType.withTypeHandler(new Object() {});
        ReferenceType newReferenceType = new ReferenceType(String.class, javaTypeWithTypeHandler, null, null, false);
        ReferenceType referenceTypeWithReferencedType = referenceType.withReferencedType(newReferenceType);
        assertEquals("T", referenceTypeWithReferencedType.containedTypeName(0));
    }

    /**
     * Test getParameterSource method.
     */
    @Test
    public void testGetParameterSource() {
        Class<String> stringClass = String.class;
        JavaType javaType = SimpleType.constructor(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        assertEquals(String.class, referenceType.getParameterSource());
    }

    /**
     * Test getErasedSignature method.
     */
    @Test
    public void testGetErasedSignature() {
        Class<String> stringClass = String.class;
        JavaType javaType = SimpleType.constructor(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        StringBuilder expectedSb = new StringBuilder("Ljava/lang/String;");
        StringBuilder actualSb = new StringBuilder();
        referenceType.getErasedSignature(actualSb);
        assertEquals(expectedSb.toString(), actualSb.toString());
    }

    /**
     * Test getGenericSignature method.
     */
    @Test
    public void testGetGenericSignature() {
        Class<String> stringClass = String.class;
        JavaType javaType = SimpleType.constructor(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        StringBuilder expectedSb = new StringBuilder("Ljava/lang/String;<");
        StringBuilder actualSb = new StringBuilder();
        referenceType.getGenericSignature(actualSb);
        assertEquals(expectedSb.toString(), actualSb.toString());
        expectedSb.append("Lcom.fasterxml.jackson.databind.type.ReferenceType;>");
        referenceType = referenceType.withReferencedType(SimpleType.constructor(Integer.class));
        referenceType.getGenericSignature(actualSb);
        assertEquals(expectedSb.toString(), actualSb.toString());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        Class<String> stringClass = String.class;
        JavaType javaType = SimpleType.constructor(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        String expectedString = "[reference type, class java.lang.String<java.lang.String>]";
        String actualString = referenceType.toString();
        assertEquals(expectedString, actualString);
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        Class<String> stringClass = String.class;
        JavaType javaType = SimpleType.constructor(String.class);
        ReferenceType referenceType1 = new ReferenceType(stringClass, javaType, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(stringClass, javaType, null, null, false);
        ReferenceType referenceType3 = new ReferenceType(String.class, SimpleType.constructor(Integer.class), null, null, false);
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(referenceType3));
    }
}