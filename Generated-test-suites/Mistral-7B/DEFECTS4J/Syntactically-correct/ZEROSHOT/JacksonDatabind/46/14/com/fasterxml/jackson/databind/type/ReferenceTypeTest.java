package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        Class<Integer> intClass = Integer.class;
        JavaType javaType = new SimpleType.Constructor(int[].class);
        ReferenceType referenceType = ReferenceType.construct(intClass, javaType, null);
        assertEquals(intClass, referenceType.getRawClass());
        assertEquals(javaType.getClass(), referenceType.getContentType().getClass());
    }

    @Test
    public void testGetReferencedType() {
        Class<Integer> intClass = Integer.class;
        JavaType javaType = new SimpleType.Constructor(int[].class);
        ReferenceType referenceType = new ReferenceType(intClass, javaType, null, null, false);
        assertEquals(javaType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        Class<Integer> intClass = Integer.class;
        JavaType javaType = new SimpleType.Constructor(int[].class);
        ReferenceType referenceType = new ReferenceType(intClass, javaType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testGetContentTypeHandler() {
        Class<Integer> intClass = Integer.class;
        JavaType javaType = new SimpleType.Constructor(int[].class);
        Object typeHandler = new Object() {
        };
        ReferenceType referenceType = new ReferenceType(intClass, javaType, null, typeHandler, false);
        assertEquals(typeHandler, referenceType.getContentTypeHandler());
    }

    @Test
    public void testWithTypeHandler() {
        Class<Integer> intClass = Integer.class;
        JavaType javaType = new SimpleType.Constructor(int[].class);
        Object newTypeHandler = new Object() {
        };
        ReferenceType referenceType = new ReferenceType(intClass, javaType, null, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(newTypeHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(newTypeHandler, newReferenceType.getContentTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<Integer> intClass = Integer.class;
        JavaType javaType = new SimpleType.Constructor(int[].class);
        JavaType newContentType = new SimpleType.Constructor(String.class);
        Object typeHandler = new Object() {
        };
        ReferenceType referenceType = new ReferenceType(intClass, javaType, null, typeHandler, false);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(newContentType);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(newContentType, newReferenceType.getContentType());
    }

    @Test
    public void testWithValueHandler() {
        Class<Integer> intClass = Integer.class;
        JavaType javaType = new SimpleType.Constructor(int[].class);
        Object newValueHandler = new Object() {
        };
        ReferenceType referenceType = new ReferenceType(intClass, javaType, null, null, false);
        ReferenceType newReferenceType = referenceType.withValueHandler(newValueHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(newValueHandler, newReferenceType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<Integer> intClass = Integer.class;
        JavaType javaType = new SimpleType.Constructor(int[].class);
        Object newContentValueHandler = new Object() {
        };
        ReferenceType referenceType = new ReferenceType(intClass, javaType, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(newContentValueHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(newContentValueHandler, newReferenceType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<Integer> intClass = Integer.class;
        JavaType javaType = new SimpleType.Constructor(int[].class);
        ReferenceType referenceType = new ReferenceType(intClass, javaType, null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertNotSame(referenceType, newReferenceType);
        assertTrue(newReferenceType.isStatic());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<Integer> intClass = Integer.class;
        JavaType javaType = new SimpleType.Constructor(int[].class);
        String expected = "[reference type, class java.lang.Integer[]<java.lang.Integer>]";
        ReferenceType referenceType = new ReferenceType(intClass, javaType, null, null, false);
        assertEquals(expected, referenceType.buildCanonicalName());
    }

    @Test
    public void testNarrow() {
        Class<Integer> intClass = Integer.class;
        JavaType javaType = new SimpleType.Constructor(int[].class);
        JavaType javaTypeNew = new SimpleType.Constructor(String[].class);
        ReferenceType referenceType = new ReferenceType(intClass, javaType, null, null, false);
        JavaType newReferenceType = referenceType._narrow(String[].class);
        assertNotSame(javaType, newReferenceType);
        assertEquals(javaTypeNew.getClass(), newReferenceType.getClass());
    }

    @Test
    public void testGetErasedSignature() {
        Class<Integer> intClass = Integer.class;
        JavaType javaType = new SimpleType.Constructor(int[].class);
        StringBuilder expected = new StringBuilder("[I");
        ReferenceType referenceType = new ReferenceType(intClass, javaType, null, null, false);
        StringBuilder actual = new StringBuilder();
        referenceType.getErasedSignature(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<Integer> intClass = Integer.class;
        JavaType javaType = new SimpleType.Constructor(int[].class);
        StringBuilder expected = new StringBuilder("[reference type, class java.lang.Integer[]<java.lang.Integer>]");
        ReferenceType referenceType = new ReferenceType(intClass, javaType, null, null, false);
        StringBuilder actual = new StringBuilder();
        referenceType.getGenericSignature(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testToString() {
        Class<Integer> intClass = Integer.class;
        JavaType javaType = new SimpleType.Constructor(int[].class);
        String expected = "[reference type, class java.lang.Integer[]<java.lang.Integer>]";
        ReferenceType referenceType = new ReferenceType(intClass, javaType, null, null, false);
        String actual = referenceType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Class<Integer> intClass = Integer.class;
        JavaType javaType = new SimpleType.Constructor(int[].class);
        Object newTypeHandler = new Object() {
        };
        ReferenceType referenceType1 = new ReferenceType(intClass, javaType, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(intClass, javaType, null, null, false);
        ReferenceType referenceType3 = new ReferenceType(intClass, javaType, null, newTypeHandler, false);
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(new Object()));
        assertFalse(referenceType1.equals(referenceType3));
    }
}