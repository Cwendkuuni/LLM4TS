package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<?> intClass = Integer.TYPE;
        SimpleType intType = new SimpleType(intClass);
        assertNotNull(intType);
        assertEquals(intClass, intType.getRawClass());
        assertNull(intType.getSuperClass());
        assertArrayEquals(new JavaType[0], intType.getSuperTypes());
        assertNull(intType.getValueHandler());
        assertNull(intType.getTypeHandler());
        assertFalse(intType.isStatic());

        JavaType intTypeWithBindings = new TypeFactory().constructType(intClass);
        SimpleType intTypeWithBindings2 = new SimpleType(intTypeWithBindings);
        assertEquals(intType, intTypeWithBindings2);
    }

    @Test
    public void testSimpleTypeWithSuperClass() {
        Class<?> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass, null, null, null);
        assertNotNull(stringType);
        assertEquals(stringClass, stringType.getRawClass());
        assertNull(stringType.getSuperClass());
        assertArrayEquals(new JavaType[0], stringType.getSuperTypes());
        assertNull(stringType.getValueHandler());
        assertNull(stringType.getTypeHandler());
        assertFalse(stringType.isStatic());

        JavaType stringArrayType = TypeFactory.defaultInstance().constructType(String[].class);
        SimpleType stringArrayTypeSimple = new SimpleType(stringArrayType);
        assertNotNull(stringArrayTypeSimple);
        assertEquals(String[].class, stringArrayTypeSimple.getRawClass());
        assertNotNull(stringArrayTypeSimple.getSuperClass());
        assertArrayEquals(new JavaType[] { SimpleType.construct(Object[].class) }, stringArrayTypeSimple.getSuperTypes());
        assertNull(stringArrayTypeSimple.getValueHandler());
        assertNull(stringArrayTypeSimple.getTypeHandler());
        assertFalse(stringArrayTypeSimple.isStatic());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<?> intClass = Integer.TYPE;
        SimpleType intType = new SimpleType(intClass);
        SimpleType intTypeCopy = new SimpleType(intType);
        assertEquals(intType, intTypeCopy);
    }

    @Test
    public void testSimpleTypeNarrowing() {
        Class<?> intClass = Integer.TYPE;
        SimpleType intType = new SimpleType(intClass);
        Class<?> intSubClass = Integer.class;
        SimpleType intSubType = intType._narrow(intSubClass);
        assertEquals(intSubClass, intSubType.getRawClass());
        assertEquals(intType, intSubType.getSuperClass());
        assertArrayEquals(new JavaType[] { intType }, intSubType.getSuperTypes());
        assertNull(intSubType.getValueHandler());
        assertNull(intSubType.getTypeHandler());
        assertFalse(intSubType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeNarrowingInvalidSubclass() {
        Class<?> intClass = Integer.TYPE;
        SimpleType intType = new SimpleType(intClass);
        Class<?> stringClass = String.class;
        SimpleType stringType = intType._narrow(stringClass);
    }

    @Test
    public void testGetCanonicalName() {
        Class<?> intClass = Integer.TYPE;
        SimpleType intType = new SimpleType(intClass);
        String canonicalName = intType.getCanonicalName();
        assertEquals("java.lang.Integer", canonicalName);

        Class<?> stringArrayClass = String[].class;
        SimpleType stringArrayType = new SimpleType(stringArrayClass);
        canonicalName = stringArrayType.getCanonicalName();
        assertEquals("java.lang.Object[]", canonicalName);
    }

    @Test
    public void testIsContainerType() {
        Class<?> intClass = Integer.TYPE;
        SimpleType intType = new SimpleType(intClass);
        assertTrue(intType.isContainerType());
        assertFalse(intType.isContainerType());
    }

    @Test
    public void testToString() {
        Class<?> intClass = Integer.TYPE;
        SimpleType intType = new SimpleType(intClass);
        String stringRepresentation = intType.toString();
        String expected = "[simple type, class java.lang.Integer]";
        assertEquals(expected, stringRepresentation);
    }

    @Test
    public void testEquals() {
        Class<?> intClass = Integer.TYPE;
        SimpleType intType = new SimpleType(intClass);
        SimpleType intType2 = new SimpleType(intClass);
        assertTrue(intType.equals(intType2));

        JavaType intTypeWithValueHandler = TypeFactory.defaultInstance().constructType(intClass, new TypeSerializer.Std(int.class));
        SimpleType intTypeWithValueHandlerSimple = new SimpleType(intTypeWithValueHandler);
        assertFalse(intType.equals(intTypeWithValueHandlerSimple));

        SimpleType intTypeWithBindings = new SimpleType(intClass, TypeBindings.emptyBindings());
        assertFalse(intType.equals(intTypeWithBindings));
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> intClass = Integer.TYPE;
        SimpleType intType = new SimpleType(intClass);
        StringBuilder expected = new StringBuilder("I");
        StringBuilder actual = intType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> intClass = Integer.TYPE;
        SimpleType intType = new SimpleType(intClass);
        StringBuilder expected = new StringBuilder("java.lang.Integer");
        StringBuilder actual = intType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testWithContentType() {
        IllegalArgumentException exception = null;
        try {
            Class<?> intClass = Integer.TYPE;
            SimpleType intType = new SimpleType(intClass);
            JavaType contentType = TypeFactory.defaultInstance().constructType(Object.class);
            JavaType result = intType.withContentType(contentType);
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals("Simple types have no content types; can not call withContentType()", exception.getMessage());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> intClass = Integer.TYPE;
        SimpleType intType = new SimpleType(intClass);
        Object handler = new Object();
        JavaType result = intType.withTypeHandler(handler);
        assertNotNull(result);
        assertEquals(intType.getRawClass(), result.getRawClass());
        assertEquals(handler, result.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        IllegalArgumentException exception = null;
        try {
            Class<?> intClass = Integer.TYPE;
            SimpleType intType = new SimpleType(intClass);
            Object handler = new Object();
            JavaType result = intType.withContentTypeHandler(handler);
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals("Simple types have no content types; can not call withContenTypeHandler()", exception.getMessage());
    }

    @Test
    public void testWithValueHandler() {
        Class<?> intClass = Integer.TYPE;
        SimpleType intType = new SimpleType(intClass);
        Object handler = new Object();
        JavaType result = intType.withValueHandler(handler);
        assertNotNull(result);
        assertEquals(intType.getRawClass(), result.getRawClass());
        assertEquals(handler, result.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        IllegalArgumentException exception = null;
        try {
            Class<?> intClass = Integer.TYPE;
            SimpleType intType = new SimpleType(intClass);
            Object handler = new Object();
            JavaType result = intType.withContentValueHandler(handler);
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals("Simple types have no content types; can not call withContenValueHandler()", exception.getMessage());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> intClass = Integer.TYPE;
        SimpleType intType = new SimpleType(intClass);
        JavaType result = intType.withStaticTyping();
        assertTrue(result.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> intClass = Integer.TYPE;
        SimpleType intType = new SimpleType(intClass);
        Class<?> intSubclass = Integer.class;
        JavaType result = intType.refine(intSubclass, TypeBindings.emptyBindings(), null, null);
        assertNull(result);
    }
}