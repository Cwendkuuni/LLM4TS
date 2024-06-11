package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new SimpleType(String.class);

        type1.setReference(refType);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType, bindings);
        type2.setReference(type1);

        assertEquals(refType, type1.getSelfReferencedType());
        assertEquals(type1, type2.getSelfReferencedType());

        try {
            type1.setReference(refType);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new SimpleType(String.class);
        type.setReference(refType);

        assertEquals(refType, type.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new SimpleType(String.class);
        type.setReference(refType);

        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);

        assertEquals("[Ljava.lang.Class;", sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new SimpleType(String.class);
        type.setReference(refType);

        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);

        assertEquals("Lcom.fasterxml.jackson.databind.type.ResolvedRecursiveType;", sb.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWithContentType() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType contentType = new SimpleType(int.class);

        type.withContentType(contentType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWithTypeHandler() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();

        type.withTypeHandler(handler);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWithContentTypeHandler() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();

        type.withContentTypeHandler(handler);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWithValueHandler() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();

        type.withValueHandler(handler);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWithContentValueHandler() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();

        type.withContentValueHandler(handler);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWithStaticTyping() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        type.withStaticTyping();
    }
}