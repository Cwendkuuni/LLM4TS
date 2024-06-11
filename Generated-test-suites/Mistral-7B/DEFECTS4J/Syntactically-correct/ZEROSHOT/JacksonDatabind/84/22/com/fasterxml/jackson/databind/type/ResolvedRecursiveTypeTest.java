package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import com.fasterxml.jackson.databind.type.TypeBindings;

import org.junit.Test;

/**
 * Test class for the ResolvedRecursiveType class.
 */
public class ResolvedRecursiveTypeTest {

    @Test
    public void testConstructor() {
        Class<ResolvedRecursiveType> clazz = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType erasedType = JavaType.construct(clazz, bindings);
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType.getRawClass(), bindings);
        assertNotNull(type);
        assertEquals(erasedType.getRawClass(), type.getErasedType().getRawClass());
        assertNull(type.getSelfReferencedType());
    }

    @Test
    public void testSetReference() {
        Class<ResolvedRecursiveType> clazz = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType erasedType = JavaType.construct(clazz, bindings);
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType.getRawClass(), bindings);
        JavaType ref = JavaType.construct(String.class, bindings);
        type.setReference(ref);
        JavaType selfReferencedType = type.getSelfReferencedType();
        assertSame(ref, selfReferencedType);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<ResolvedRecursiveType> clazz = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType erasedType = JavaType.construct(clazz, bindings);
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType.getRawClass(), bindings);
        JavaType ref1 = JavaType.construct(String.class, bindings);
        JavaType ref2 = JavaType.construct(Integer.class, bindings);
        type.setReference(ref1);
        type.setReference(ref2);
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<ResolvedRecursiveType> clazz = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType erasedType = JavaType.construct(clazz, bindings);
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType.getRawClass(), bindings);
        JavaType ref = JavaType.construct(String.class, bindings);
        type.setReference(ref);
        JavaType selfReferencedType = type.getSelfReferencedType();
        assertSame(ref, selfReferencedType);
    }

    @Test
    public void testGetGenericSignature() {
        Class<ResolvedRecursiveType> clazz = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType erasedType = JavaType.construct(clazz, bindings);
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType.getRawClass(), bindings);
        JavaType ref = JavaType.construct(String.class, bindings);
        type.setReference(ref);
        StringBuilder expected = new StringBuilder("[Ljava.lang.String;");
        StringBuilder actual = new StringBuilder();
        type.getGenericSignature(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetErasedSignature() {
        Class<ResolvedRecursiveType> clazz = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType erasedType = JavaType.construct(clazz, bindings);
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType.getRawClass(), bindings);
        JavaType ref = JavaType.construct(String.class, bindings);
        type.setReference(ref);
        StringBuilder expected = new StringBuilder("Ljava/lang/String;");
        StringBuilder actual = new StringBuilder();
        type.getErasedSignature(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testWithContentType() {
        Class<ResolvedRecursiveType> clazz = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType erasedType = JavaType.construct(clazz, bindings);
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType.getRawClass(), bindings);
        JavaType contentType = JavaType.construct(Integer.class, bindings);
        JavaType result = type.withContentType(contentType);
        assertSame(type, result);
    }

    @Test
    public void testWithTypeHandler() {
        Class<ResolvedRecursiveType> clazz = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType erasedType = JavaType.construct(clazz, bindings);
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType.getRawClass(), bindings);
        Object handler = new Object();
        JavaType result = type.withTypeHandler(handler);
        assertSame(type, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<ResolvedRecursiveType> clazz = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType erasedType = JavaType.construct(clazz, bindings);
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType.getRawClass(), bindings);
        Object handler = new Object();
        JavaType result = type.withContentTypeHandler(handler);
        assertSame(type, result);
    }

    @Test
    public void testWithValueHandler() {
        Class<ResolvedRecursiveType> clazz = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType erasedType = JavaType.construct(clazz, bindings);
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType.getRawClass(), bindings);
        Object handler = new Object();
        JavaType result = type.withValueHandler(handler);
        assertSame(type, result);
    }

    @Test
    public void testWithContentValueHandler() {
        Class<ResolvedRecursiveType> clazz = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType erasedType = JavaType.construct(clazz, bindings);
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType.getRawClass(), bindings);
        Object handler = new Object();
        JavaType result = type.withContentValueHandler(handler);
        assertSame(type, result);
    }

    @Test
    public void testWithStaticTyping() {
        Class<ResolvedRecursiveType> clazz = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType erasedType = JavaType.construct(clazz, bindings);
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType.getRawClass(), bindings);
        JavaType result = type.withStaticTyping();
        assertSame(type, result);
    }

    @Test
    public void testNarrow() {
        Class<ResolvedRecursiveType> clazz = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType erasedType = JavaType.construct(clazz, bindings);
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType.getRawClass(), bindings);
        Class<?> subclass = ResolvedRecursiveType.class;
        JavaType result = type.narrow(subclass);
        assertNull(result);
    }

    @Test
    public void testRefine() {
        Class<ResolvedRecursiveType> clazz = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType erasedType = JavaType.construct(clazz, bindings);
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType.getRawClass(), bindings);
        Class<?> rawType = String[].class;
        TypeBindings newBindings = new TypeBindings();
        JavaType superClass = JavaType.construct(Object.class, newBindings);
        JavaType[] superInterfaces = new JavaType[0];
        JavaType result = type.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        Class<ResolvedRecursiveType> clazz = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType erasedType = JavaType.construct(clazz, bindings);
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType.getRawClass(), bindings);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testToString() {
        Class<ResolvedRecursiveType> clazz = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType erasedType = JavaType.construct(clazz, bindings);
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType.getRawClass(), bindings);
        StringBuilder expected = new StringBuilder("[recursive type; UNRESOLVED]");
        StringBuilder actual = new StringBuilder();
        type.toString(actual);
        assertEquals(expected.toString(), actual.toString());

        JavaType ref = JavaType.construct(String.class, bindings);
        type.setReference(ref);
        expected = new StringBuilder("[recursive type; class java.lang.String]");
        actual = new StringBuilder();
        type.toString(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testEquals() {
        Class<ResolvedRecursiveType> clazz = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType erasedType1 = JavaType.construct(clazz, bindings);
        JavaType erasedType2 = JavaType.construct(clazz, bindings);
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType1.getRawClass(), bindings);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType2.getRawClass(), bindings);
        assertFalse(type1.equals(type2));

        JavaType ref1 = JavaType.construct(String.class, bindings);
        JavaType ref2 = JavaType.construct(Integer.class, bindings);
        type1.setReference(ref1);
        type2.setReference(ref2);
        assertFalse(type1.equals(type2));

        JavaType ref = JavaType.construct(String.class, bindings);
        type1.setReference(ref);
        type2.setReference(ref);
        assertTrue(type1.equals(type2));
    }
}