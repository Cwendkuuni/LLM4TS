package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        JavaType stringType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        assertEquals(String.class, stringType.getRawClass());
        assertNull(stringType.getSuperClass());
        assertArrayEquals(new Class[0], stringType.getSuperTypes());
        assertFalse(stringType.isContainerType());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        JavaType originalStringType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        JavaType copiedStringType = new SimpleType(originalStringType);
        assertEquals(originalStringType, copiedStringType);
    }

    @Test
    public void testSimpleTypeWithContentType() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType stringType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        JavaType testType = stringType.withContentType(intType);
        assertNotNull(testType);
        assertEquals(String[].class, testType.getRawClass());
        assertEquals(String.class, testType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithInvalidContentType() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType stringType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        JavaType testType = stringType.withContentType(intType);
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType stringType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        JavaType testType = stringType.withTypeHandler(new IntDeserializer());
        assertNotNull(testType);
        assertEquals(String.class, testType.getRawClass());
        assertSame(IntDeserializer.class, testType.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithInvalidTypeHandler() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType stringType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        JavaType testType = stringType.withTypeHandler(new Object());
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType stringType = new SimpleType(String.class, TypeBindings.emptyBindings(), intType, null);
        JavaType testType = stringType.withValueHandler(new IntSerializer());
        assertNotNull(testType);
        assertEquals(String.class, testType.getRawClass());
        assertSame(IntSerializer.class, testType.getValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithInvalidValueHandler() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType stringType = new SimpleType(String.class, TypeBindings.emptyBindings(), intType, null);
        JavaType testType = stringType.withValueHandler(new Object());
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType stringType = new SimpleType(String.class, TypeBindings.emptyBindings(), intType, null);
        JavaType testType = stringType.withStaticTyping();
        assertNotNull(testType);
        assertTrue(testType.isStatic());
    }

    @Test
    public void testSimpleTypeWithRefine() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType listIntType = new SimpleType(int[].class, TypeBindings.emptyBindings(), null, null);
        JavaType testType = listIntType.refine(intType.getRawClass(), TypeBindings.emptyBindings(), null, null);
        assertNull(testType);
    }

    @Test
    public void testSimpleTypeWithInvalidClassArgument() {
        Class<Object> objectClass = Object.class;
        JavaType stringType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        JavaType testType = null;
        try {
            testType = stringType.refine(objectClass, TypeBindings.emptyBindings(), null, null);
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertNull(testType);
    }

    @Test
    public void testSimpleTypeWithInvalidBindingsArgument() {
        Class<String> stringClass = String.class;
        JavaType stringType = new SimpleType(stringClass, null, null, null);
        JavaType testType = null;
        try {
            testType = stringType.refine(stringClass, null, null, null);
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertNull(testType);
    }

    @Test
    public void testSimpleTypeConstructUnsafe() {
        Class<String> stringClass = String.class;
        JavaType stringType = SimpleType.constructUnsafe(stringClass);
        assertEquals(String.class, stringType.getRawClass());
        assertNull(stringType.getSuperClass());
        assertArrayEquals(new Class[0], stringType.getSuperTypes());
        assertFalse(stringType.isContainerType());
    }

    @Test(expected = DeprecatedException.class)
    public void testSimpleTypeConstructDeprecated() {
        Class<String> stringClass = String.class;
        JavaType stringType = SimpleType.construct(stringClass);
    }

    @Test
    public void testSimpleTypeWithNarrowing() {
        Class<String> stringClass = String.class;
        JavaType stringType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        JavaType narrowedType = stringType.narrow(StringBuilder.class);
        assertNotNull(narrowedType);
        assertSame(StringBuilder.class, narrowedType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithInvalidNarrowingArgument() {
        Class<String> stringClass = String.class;
        JavaType stringType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        JavaType narrowedType = stringType.narrow(Object.class);
    }

    private static class IntDeserializer {}

    private static class IntSerializer {}
}