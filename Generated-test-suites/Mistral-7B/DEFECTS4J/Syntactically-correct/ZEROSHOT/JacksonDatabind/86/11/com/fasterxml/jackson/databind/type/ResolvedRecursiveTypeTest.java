package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.lang.reflect.Type;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testCreateResolvedRecursiveType() {
        TypeBinding binding = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType type = TypeFactory.defaultInstance().constructType(ResolvedRecursiveType.class);
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(int[].class.getTypeName(), binding);
        assertNull(resolvedType.getSelfReferencedType());
        assertNull(resolvedType.getErasedSignature(new StringBuilder()).toString());
        assertNull(resolvedType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testSetReference() {
        TypeBinding binding = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType type = TypeFactory.defaultInstance().constructType(ResolvedRecursiveType.class);
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(int[].class.getTypeName(), binding);
        resolvedType.setReference(referencedType);
        assertSame(referencedType, resolvedType.getSelfReferencedType());
        assertSame(TypeFactory.defaultInstance().constructType(String.class),
                resolvedType.getErasedSignature(new StringBuilder()).toString());
        assertSame(TypeFactory.defaultInstance().constructType(String.class),
                resolvedType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        TypeBinding binding = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType type = TypeFactory.defaultInstance().constructType(ResolvedRecursiveType.class);
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(int[].class.getTypeName(), binding);
        resolvedType.setReference(referencedType);
        resolvedType.setReference(referencedType);
    }

    @Test
    public void testGetSelfReferencedType() {
        TypeBinding binding = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType type = TypeFactory.defaultInstance().constructType(ResolvedRecursiveType.class);
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(int[].class.getTypeName(), binding);
        resolvedType.setReference(referencedType);
        assertSame(referencedType, resolvedType.getSelfReferencedType());
        assertNull(resolvedType.getErasedSignature(new StringBuilder()).toString());
        assertNull(resolvedType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetErasedSignatureAndGenericSignature() {
        TypeBinding binding = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType type = TypeFactory.defaultInstance().constructType(ResolvedRecursiveType.class);
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(int[].class.getTypeName(), binding);
        resolvedType.setReference(referencedType);

        StringBuilder erasedSignatureBuilder = new StringBuilder();
        StringBuilder genericSignatureBuilder = new StringBuilder();

        assertNull(resolvedType.getSelfReferencedType());
        assertNull(resolvedType.getErasedSignature(erasedSignatureBuilder).toString());
        assertNull(resolvedType.getGenericSignature(genericSignatureBuilder).toString());

        resolvedType.setReference(referencedType);

        assertSame(TypeFactory.defaultInstance().constructType(String.class),
                resolvedType.getErasedSignature(erasedSignatureBuilder).toString());
        assertSame(TypeFactory.defaultInstance().constructType(ResolvedRecursiveType.class, new Type[]{TypeFactory.defaultInstance().constructType(String.class)}),
                resolvedType.getGenericSignature(genericSignatureBuilder).toString());
    }

    @Test
    public void testWithContentType() {
        TypeBinding binding = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType type = TypeFactory.defaultInstance().constructType(ResolvedRecursiveType.class);
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(int[].class.getTypeName(), binding);
        resolvedType.setReference(referencedType);
        assertSame(resolvedType, resolvedType.withContentType(referencedType));
    }

    @Test
    public void testWithTypeHandler() {
        TypeBinding binding = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType type = TypeFactory.defaultInstance().constructType(ResolvedRecursiveType.class);
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(int[].class.getTypeName(), binding);
        resolvedType.setReference(referencedType);
        assertSame(resolvedType, resolvedType.withTypeHandler(null));
    }

    @Test
    public void testWithContentTypeHandler() {
        TypeBinding binding = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType type = TypeFactory.defaultInstance().constructType(ResolvedRecursiveType.class);
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(int[].class.getTypeName(), binding);
        resolvedType.setReference(referencedType);
        assertSame(resolvedType, resolvedType.withContentTypeHandler(null));
    }

    @Test
    public void testWithValueHandler() {
        TypeBinding binding = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType type = TypeFactory.defaultInstance().constructType(ResolvedRecursiveType.class);
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(int[].class.getTypeName(), binding);
        resolvedType.setReference(referencedType);
        assertSame(resolvedType, resolvedType.withValueHandler(null));
    }

    @Test
    public void testWithContentValueHandler() {
        TypeBinding binding = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType type = TypeFactory.defaultInstance().constructType(ResolvedRecursiveType.class);
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(int[].class.getTypeName(), binding);
        resolvedType.setReference(referencedType);
        assertSame(resolvedType, resolvedType.withContentValueHandler(null));
    }

    @Test
    public void testWithStaticTyping() {
        TypeBinding binding = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType type = TypeFactory.defaultInstance().constructType(ResolvedRecursiveType.class);
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(int[].class.getTypeName(), binding);
        resolvedType.setReference(referencedType);
        assertSame(resolvedType, resolvedType.withStaticTyping());
    }

    @Test
    public void testNarrow() {
        JavaType type = TypeFactory.defaultInstance().constructType(ResolvedRecursiveType.class);
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(int[].class.getTypeName(), null);
        assertNull(resolvedType.narrow(Object.class));
    }

    @Test
    public void testRefine() {
        JavaType type = TypeFactory.defaultInstance().constructType(ResolvedRecursiveType.class);
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(int[].class.getTypeName(), null);
        assertNull(resolvedType.refine(int[].class, null, null, null));
    }

    @Test
    public void testIsContainerType() {
        JavaType type = TypeFactory.defaultInstance().constructType(ResolvedRecursiveType.class);
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(int[].class.getTypeName(), null);
        assertFalse(resolvedType.isContainerType());
    }

    @Test
    public void testToString() {
        TypeBinding binding = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType type = TypeFactory.defaultInstance().constructType(ResolvedRecursiveType.class);
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(int[].class.getTypeName(), binding);
        resolvedType.setReference(referencedType);

        String expected = "[recursive type; [Ljava.lang.int;@6d000000] (ResolvedRecursiveType) @1: java.lang.String]";
        assertSame(expected, resolvedType.toString());
    }

    @Test
    public void testEquals() {
        TypeBinding binding1 = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType referencedType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType type1 = TypeFactory.defaultInstance().constructType(ResolvedRecursiveType.class);
        ResolvedRecursiveType resolvedType1 = new ResolvedRecursiveType(int[].class.getTypeName(), binding1);
        resolvedType1.setReference(referencedType1);

        TypeBinding binding2 = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType referencedType2 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType type2 = TypeFactory.defaultInstance().constructType(ResolvedRecursiveType.class);
        ResolvedRecursiveType resolvedType2 = new ResolvedRecursiveType(int[].class.getTypeName(), binding2);
        resolvedType2.setReference(referencedType2);

        TypeBinding binding3 = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType referencedType3 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType type3 = TypeFactory.defaultInstance().constructType(ResolvedRecursiveType.class);
        ResolvedRecursiveType resolvedType3 = new ResolvedRecursiveType(int[].class.getTypeName(), binding3);
        resolvedType3.setReference(referencedType3);

        assertFalse(resolvedType1.equals(null));
        assertFalse(resolvedType1.equals(type1));
        assertFalse(resolvedType1.equals(resolvedType2));
        assertFalse(resolvedType1.equals(resolvedType3));
        assertTrue(resolvedType1.equals(resolvedType1));
        assertFalse(resolvedType1.equals(resolvedType1, new ResolvedRecursiveType(int[].class.getTypeName(), binding1)));
        assertTrue(resolvedType1.equals(resolvedType1, new ResolvedRecursiveType(int[].class.getTypeName(), binding1) {
            @Override
            public JavaType getSelfReferencedType() {
                return referencedType1;
            }
        }));
    }
}