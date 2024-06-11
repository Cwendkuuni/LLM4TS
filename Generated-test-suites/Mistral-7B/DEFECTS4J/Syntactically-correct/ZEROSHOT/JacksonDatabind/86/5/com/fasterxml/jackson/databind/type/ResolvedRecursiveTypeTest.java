package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeBindings.TypeSerializerBindings;

import org.junit.Test;

/**
 * JUnit 4 test file for {@link ResolvedRecursiveType} class.
 */
public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveTypeConstruction() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType javaType = mock(JavaType.class);

        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertEquals(erasedType, resolvedRecursiveType.getRawClass());
        assertNotNull(resolvedRecursiveType.getBindings());
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testSettingSelfReferencedType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType javaType = mock(JavaType.class);

        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = mock(JavaType.class);

        resolvedRecursiveType.setReference(ref);
        assertSame(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSettingSelfReferencedTypeTwice() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType javaType1 = mock(JavaType.class);
        JavaType javaType2 = mock(JavaType.class);

        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(javaType1);
        resolvedRecursiveType.setReference(javaType2);
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType javaType = mock(JavaType.class);
        StringBuilder signature = mock(StringBuilder.class);

        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        when(javaType.getGenericSignature(signature)).thenReturn(signature);

        resolvedRecursiveType.setReference(javaType);
        resolvedRecursiveType.getGenericSignature(signature);
        assertEquals(signature.toString(), resolvedRecursiveType.getGenericSignature(signature).toString());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType javaType = mock(JavaType.class);
        StringBuilder signature = mock(StringBuilder.class);

        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        when(javaType.getErasedSignature(signature)).thenReturn(signature);

        resolvedRecursiveType.setReference(javaType);
        resolvedRecursiveType.getErasedSignature(signature);
        assertEquals(signature.toString(), resolvedRecursiveType.getErasedSignature(signature).toString());
    }

    @Test
    public void testWithContentType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType javaType = mock(JavaType.class);

        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(javaType);

        JavaType contentType = mock(JavaType.class);
        JavaType resultType = resolvedRecursiveType.withContentType(contentType);

        assertEquals(resolvedRecursiveType, resultType);
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType javaType = mock(JavaType.class);
        Object handler = mock(Object.class);

        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(javaType);

        JavaType resultType = resolvedRecursiveType.withTypeHandler(handler);

        assertEquals(resolvedRecursiveType, resultType);
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType javaType = mock(JavaType.class);
        Object handler = mock(Object.class);

        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(javaType);

        JavaType resultType = resolvedRecursiveType.withContentTypeHandler(handler);

        assertEquals(resolvedRecursiveType, resultType);
    }

    @Test
    public void testWithValueHandler() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType javaType = mock(JavaType.class);
        Object handler = mock(Object.class);

        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(javaType);

        JavaType resultType = resolvedRecursiveType.withValueHandler(handler);

        assertEquals(resolvedRecursiveType, resultType);
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType javaType = mock(JavaType.class);
        Object handler = mock(Object.class);

        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(javaType);

        JavaType resultType = resolvedRecursiveType.withContentValueHandler(handler);

        assertEquals(resolvedRecursiveType, resultType);
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType javaType = mock(JavaType.class);

        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(javaType);

        JavaType resultType = resolvedRecursiveType.withStaticTyping();

        assertEquals(resolvedRecursiveType, resultType);
    }

    @Test
    public void testNarrow() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = mock(TypeBindings.class);
        Class<?> subClass = Mockito.mock(Class.class);

        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        assertNull(resolvedRecursiveType._narrow(subClass));
    }

    @Test
    public void testRefine() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType superClass = mock(JavaType.class);
        JavaType[] superInterfaces = new JavaType[] { mock(JavaType.class) };

        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        assertNull(resolvedRecursiveType.refine(null, bindings, superClass, superInterfaces));
    }

    @Test
    public void testIsContainerType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType javaType = mock(JavaType.class);

        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(javaType);

        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType javaType = mock(JavaType.class);

        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(javaType);

        StringBuilder stringBuilder = new StringBuilder(40);
        assertEquals("[[recursive type; UNRESOLVED]", resolvedRecursiveType.toString());

        when(javaType.getRawClass().getName()).thenReturn("java.lang.String");
        assertEquals("[recursive type; java.lang.String]", resolvedRecursiveType.toString());
    }

    @Test
    public void testEquals() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType javaType = mock(JavaType.class);

        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType, bindings);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType, bindings);

        // same object
        assertEquals(resolvedRecursiveType1, resolvedRecursiveType1);

        // same class and self reference
        when(javaType.getRawClass().getName()).thenReturn("java.lang.String");
        when(javaType.getSelfReferencedType()).thenReturn(javaType);

        resolvedRecursiveType1.setReference(javaType);
        resolvedRecursiveType2.setReference(javaType);

        assertEquals(resolvedRecursiveType1, resolvedRecursiveType2);

        // different classes and no self reference
        ResolvedRecursiveType resolvedRecursiveType3 = new ResolvedRecursiveType(Integer.class, bindings);
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType3));
    }
}