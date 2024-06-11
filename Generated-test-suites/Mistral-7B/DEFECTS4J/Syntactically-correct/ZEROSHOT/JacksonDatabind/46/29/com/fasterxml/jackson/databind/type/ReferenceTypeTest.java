package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ReferenceTypeTest {

    private JavaType getReferenceType(Class<?> clazz, JavaType referencedType) {
        Object valueHandler = null;
        Object typeHandler = null;
        return ReferenceType.construct(clazz, referencedType, valueHandler, typeHandler);
    }

    @Test
    public testConstructor() {
        Class<Integer> intClass = Integer.class;
        JavaType javaIntType = TypeFactory.defaultInstance().constructType(intClass);

        ReferenceType referenceType = getReferenceType(intClass, javaIntType);
        assertEquals(intClass, referenceType.getRawClass());
        assertEquals(javaIntType, referenceType.getContentType());
        assertFalse(referenceType.isStatic());
    }

    @Test
    public testGetReferencedType() {
        Class<Integer> intClass = Integer.class;
        JavaType javaIntType = TypeFactory.defaultInstance().constructType(intClass);

        ReferenceType referenceType = getReferenceType(intClass, javaIntType);
        JavaType referencedType = referenceType.getReferencedType();
        assertEquals(javaIntType, referencedType);
    }

    @Test
    public testIsReferenceType() {
        Class<Integer> intClass = Integer.class;
        JavaType javaIntType = TypeFactory.defaultInstance().constructType(intClass);

        ReferenceType referenceType = getReferenceType(intClass, javaIntType);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public testWithTypeHandler() {
        Class<Integer> intClass = Integer.class;
        JavaType javaIntType = TypeFactory.defaultInstance().constructType(intClass);
        Object typeHandler = new Object();

        ReferenceType referenceType = getReferenceType(intClass, javaIntType);
        ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(typeHandler, newReferenceType.getTypeHandler());
    }

    @Test
    public testWithContentTypeHandler() {
        Class<Integer> intClass = Integer.class;
        JavaType javaIntType = TypeFactory.defaultInstance().constructType(intClass);
        Object contentTypeHandler = new Object();

        ReferenceType referenceType = getReferenceType(intClass, javaIntType);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(contentTypeHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(contentTypeHandler, newReferenceType._referencedType.getTypeHandler());
    }

    @Test
    public testWithValueHandler() {
        Class<Integer> intClass = Integer.class;
        JavaType javaIntType = TypeFactory.defaultInstance().constructType(intClass);
        Object valueHandler = new Object();

        ReferenceType referenceType = getReferenceType(intClass, javaIntType);
        ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(valueHandler, newReferenceType._valueHandler);
    }

    @Test
    public testWithContentValueHandler() {
        Class<Integer> intClass = Integer.class;
        JavaType javaIntType = TypeFactory.defaultInstance().constructType(intClass);
        Object contentValueHandler = new Object();

        ReferenceType referenceType = getReferenceType(intClass, javaIntType);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(contentValueHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(contentValueHandler, newReferenceType._referencedType.<Object>getValueHandler());
    }

    @Test
    public testWithStaticTyping() {
        Class<Integer> intClass = Integer.class;
        JavaType javaIntType = TypeFactory.defaultInstance().constructType(intClass);

        ReferenceType referenceType = getReferenceType(intClass, javaIntType);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertNotSame(referenceType, newReferenceType);
        assertTrue(newReferenceType.isStatic());
    }

    @Test
    public testBuildCanonicalName() {
        Class<Integer> intClass = Integer.class;
        JavaType javaIntType = TypeFactory.defaultInstance().constructType(intClass);

        ReferenceType referenceType = getReferenceType(intClass, javaIntType);
        assertEquals("[reference type, class java.lang.Integer<T>]"
                + "<T: class java.lang.Integer>", referenceType.buildCanonicalName());
    }

    @Test
    public testNarrow() {
        Class<Integer> intClass = Integer.class;
        JavaType javaIntType = TypeFactory.defaultInstance().constructType(intClass);
        Class<Long> longClass = Long.class;

        ReferenceType referenceType = getReferenceType(intClass, javaIntType);
        JavaType narrowedType = referenceType._narrow(longClass);
        assertEquals(new ReferenceType(longClass, javaIntType, null, null, false), narrowedType);
    }

    @Test
    public testContainedTypeCount() {
        Class<Integer> intClass = Integer.class;
        JavaType javaIntType = TypeFactory.defaultInstance().constructType(intClass);

        ReferenceType referenceType = getReferenceType(intClass, javaIntType);
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public testContainedType() {
        Class<Integer> intClass = Integer.class;
        JavaType javaIntType = TypeFactory.defaultInstance().constructType(intClass);

        ReferenceType referenceType = getReferenceType(intClass, javaIntType);
        JavaType containedType = referenceType.containedType(0);
        assertEquals(javaIntType, containedType);
    }

    @Test
    public testContainedTypeName() {
        Class<Integer> intClass = Integer.class;
        JavaType javaIntType = TypeFactory.defaultInstance().constructType(intClass);

        ReferenceType referenceType = getReferenceType(intClass, javaIntType);
        String containedTypeName = referenceType.containedTypeName(0);
        assertEquals("T", containedTypeName);
    }

    @Test
    public testGetParameterSource() {
        Class<Integer> intClass = Integer.class;
        JavaType javaIntType = TypeFactory.defaultInstance().constructType(intClass);

        ReferenceType referenceType = getReferenceType(intClass, javaIntType);
        assertEquals(intClass, referenceType.getParameterSource());
    }

    @Test
    public testGetErasedSignature() {
        Class<Integer> intClass = Integer.class;
        JavaType javaIntType = TypeFactory.defaultInstance().constructType(intClass);

        ReferenceType referenceType = getReferenceType(intClass, javaIntType);
        StringBuilder expected = new StringBuilder("I");
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public testGetGenericSignature() {
        Class<Integer> intClass = Integer.class;
        JavaType javaIntType = TypeFactory.defaultInstance().constructType(intClass);

        ReferenceType referenceType = getReferenceType(intClass, javaIntType);
        StringBuilder expected = new StringBuilder("[reference type, class ");
        StringBuilder actual = new StringBuilder();
        referenceType.getGenericSignature(actual);
        expected.append(intClass.getName());
        expected.append('<');
        expected.append(javaIntType.toGenericString());
        expected.append('>');
        expected.append(']');
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public testToString() {
        Class<Integer> intClass = Integer.class;
        JavaType javaIntType = TypeFactory.defaultInstance().constructType(intClass);

        ReferenceType referenceType = getReferenceType(intClass, javaIntType);
        String expected = "[reference type, class " + intClass.getName()
                + "<T>, contentType=" + javaIntType.toCanonical() + ']';
        String actual = referenceType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public testEquals() {
        Class<Integer> intClass = Integer.class;
        JavaType javaIntType = TypeFactory.defaultInstance().constructType(intClass);

        ReferenceType referenceType1 = getReferenceType(intClass, javaIntType);
        ReferenceType referenceType2 = getReferenceType(intClass, javaIntType);
        assertTrue(referenceType1.equals(referenceType2));

        SimpleType simpleType = new SimpleType(intClass, javaIntType.hashCode(), null, null, false);
        assertFalse(referenceType1.equals(simpleType));
    }

}