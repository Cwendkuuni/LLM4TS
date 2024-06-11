package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import com.fasterxml.jackson.databind.type.TypeBase;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for ResolvedRecursiveType.
 *
 * @author <Your Name>
 */
public class ResolvedRecursiveTypeTest {

    private JavaType testType;
    private JavaType referencedType;

    @Before
    public void setup() {
        Class<?> clazz = RecursiveTypeTest.class;
        TypeBindings bindings = new TypeBindings();

        testType = new ResolvedRecursiveType(clazz, bindings);
        referencedType = new ResolvedRecursiveType(clazz, bindings);
    }

    @Test
    public void testEmptyConstructor() {
        ResolvedRecursiveType emptyType = new ResolvedRecursiveType(null, null);
        assertNull(emptyType.getSelfReferencedType());
    }

    @Test
    public void testSetReference() {
        JavaType refType = new ResolvedRecursiveType(String.class, null);
        testType.setReference(refType);
        assertEquals(refType, testType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        JavaType refType = new ResolvedRecursiveType(String.class, null);
        testType.setReference(refType);
        testType.setReference(new ResolvedRecursiveType(Integer.class, null));
    }

    @Test
    public void testGetSelfReferencedType() {
        JavaType refType = new ResolvedRecursiveType(String.class, null);
        testType.setReference(refType);
        assertEquals(refType, testType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder expected = new StringBuilder();
        String result = testType.getGenericSignature(expected).toString();
        assertEquals(expected.toString(), result);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder expected = new StringBuilder();
        String result = testType.getErasedSignature(expected).toString();
        assertEquals(expected.toString(), result);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new ResolvedRecursiveType(Integer.class, null);
        JavaType result = testType.withContentType(contentType);
        assertEquals(testType, result);
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        JavaType result = testType.withTypeHandler(handler);
        assertEquals(testType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        JavaType result = testType.withContentTypeHandler(handler);
        assertEquals(testType, result);
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        JavaType result = testType.withValueHandler(handler);
        assertEquals(testType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        JavaType result = testType.withContentValueHandler(handler);
        assertEquals(testType, result);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType result = testType.withStaticTyping();
        assertEquals(testType, result);
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = Integer.class;
        JavaType result = testType._narrow(subclass);
        assertEquals(testType, result);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new ResolvedRecursiveType(String.class, bindings);
        JavaType[] superInterfaces = new JavaType[] { new ResolvedRecursiveType(List.class, bindings) };
        JavaType result = testType.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        assertFalse(testType.isContainerType());
    }

    @Test
    public void testToString() {
        String result = testType.toString();
        StringBuilder expected = new StringBuilder("[recursive type; ")
                .append(testType.getErasedType().getRawClass().getName())
                .append("]");
        assertEquals(expected.toString(), result);
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType otherType = new ResolvedRecursiveType(testType.getErasedType(), null);
        assertTrue(testType.equals(otherType));

        otherType = new ResolvedRecursiveType(testType.getErasedType(), null);
        otherType.setReference(new ResolvedRecursiveType(String.class, null));
        assertFalse(testType.equals(otherType));
    }
}