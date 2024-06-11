package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.ClassCastException;
import java.lang.ClassNotFoundException;
import java.lang.reflect.Type;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveTypeConstruction() {
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, bindings);
        rrt.setReference(refType);
        assertNotNull(rrt.getSelfReferencedType());
        assertEquals(refType.getClass(), rrt.getSelfReferencedType().getClass());
    }

    @Test(expected = IllegalStateException.class)
    public void testResolvedRecursiveTypeDoubleSetReference() {
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, bindings);
        rrt.setReference(refType);
        rrt.setReference(new TypeReference<JavaType>() {}.getType());
    }

    @Test
    public void testResolvedRecursiveTypeGetSignatures() {
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, bindings);
        rrt.setReference(refType);

        StringBuilder sb = new StringBuilder(50);
        String expectedSelfRefSignature = refType.getGenericSignature(sb).toString();
        String expectedErasedSignature = refType.getErasedSignature(sb).toString();

        assertEquals(expectedSelfRefSignature, rrt.getGenericSignature(sb).toString());
        assertEquals(expectedErasedSignature, rrt.getErasedSignature(sb).toString());
    }

    @Test
    public void testResolvedRecursiveTypeWithContentType() {
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, bindings);
        rrt.setReference(refType);

        JavaType contentType = new TypeReference<JavaType>() {}.getType();
        assertEquals(rrt, rrt.withContentType(contentType));
    }

    @Test
    public void testResolvedRecursiveTypeWithTypeHandler() {
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, bindings);
        rrt.setReference(refType);

        Object handler = new Object();
        assertEquals(rrt, rrt.withTypeHandler(handler));
    }

    @Test
    public void testResolvedRecursiveTypeWithContentTypeHandler() {
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, bindings);
        rrt.setReference(refType);

        Object handler = new Object();
        assertEquals(rrt, rrt.withContentTypeHandler(handler));
    }

    @Test
    public void testResolvedRecursiveTypeWithValueHandler() {
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, bindings);
        rrt.setReference(refType);

        Object handler = new Object();
        assertEquals(rrt, rrt.withValueHandler(handler));
    }

    @Test
    public void testResolvedRecursiveTypeWithContentValueHandler() {
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, bindings);
        rrt.setReference(refType);

        Object handler = new Object();
        assertEquals(rrt, rrt.withContentValueHandler(handler));
    }

    @Test
    public void testResolvedRecursiveTypeWithStaticTyping() {
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, bindings);
        rrt.setReference(refType);

        assertEquals(rrt, rrt.withStaticTyping());
    }

    @Test
    public void testResolvedRecursiveTypeIsContainerType() {
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, bindings);
        rrt.setReference(refType);

        assertFalse(rrt.isContainerType());
    }

    @Test
    public void testResolvedRecursiveTypeToString() {
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, bindings);
        rrt.setReference(refType);

        String expectedToString = "[recursive type; " + refType.getName() + "]";
        assertEquals(expectedToString, rrt.toString());
    }

    @Test
    public void testResolvedRecursiveTypeEquals() {
        TypeBindings bindings = new TypeBindings();
        JavaType refTypeA = new TypeReference<JavaType>() {}.getType();
        JavaType refTypeB = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType rrtA = new ResolvedRecursiveType(String.class, bindings);
        ResolvedRecursiveType rrtB = new ResolvedRecursiveType(String.class, bindings);
        rrtA.setReference(refTypeA);
        rrtB.setReference(refTypeB);

        assertFalse(rrtA.equals(rrtB));

        rrtB.setReference(refTypeA);
        assertTrue(rrtA.equals(rrtB));
    }

    @Test(expected = ClassCastException.class)
    public void testResolvedRecursiveTypeEqualsDifferentClass() {
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, bindings);
        rrt.setReference(refType);

        Object other = new Object();
        assertFalse(rrt.equals(other));
    }

    @Test(expected = NullPointerException.class)
    public void testResolvedRecursiveTypeEqualsNull() {
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, bindings);
        rrt.setReference(refType);

        assertFalse(rrt.equals(null));
    }

    @Test(expected = ClassNotFoundException.class)
    public void testResolvedRecursiveTypeEqualsUnresolvedType() {
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, bindings);
        rrt.setReference(refType);

        JavaType unresolvedType = new TypeReference<JavaType>() {}.getType();
        assertFalse(rrt.equals(unresolvedType));
    }

    @Test
    public void testResolvedRecursiveTypeEqualsSelf() {
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, bindings);
        rrt.setReference(refType);

        assertEquals(rrt, rrt);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetReferenceNullRefType() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, bindings);
        assertNull(rrt.getSelfReferencedType());
        rrt.setReference(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetReferenceInvalidRefType() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, bindings);
        assertNull(rrt.getSelfReferencedType());
        rrt.setReference(new Object());
    }

    @Test
    public void testSetReferenceSameInstance() {
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, bindings);
        rrt.setReference(refType);

        JavaType sameInstance = refType;
        rrt.setReference(sameInstance);
        assertEquals(refType, rrt.getSelfReferencedType());
    }

    @Test
    public void testSetReferenceDifferentInstance() {
        TypeBindings bindings = new TypeBindings();
        JavaType refTypeA = new TypeReference<JavaType>() {}.getType();
        JavaType refTypeB = new TypeReference<JavaType>() {}.getType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, bindings);
        rrt.setReference(refTypeA);

        JavaType differentInstance = refTypeB;
        rrt.setReference(differentInstance);
        assertNotSame(refTypeA, rrt.getSelfReferencedType());
        assertEquals(differentInstance, rrt.getSelfReferencedType());
    }

    @Test
    public void testSetReferenceWithTypeIdResolver() {
        TypeBindings bindings = new TypeBindings();
        JavaType refType = new TypeReference<JavaType>() {}.getType();
        TypeIdResolver idResolver = new TypeIdResolver() {};
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, bindings);
        rrt.setReference(refType);

        Object handler = new Object();
        assertEquals(rrt, rrt.withTypeHandler(handler));
        assertEquals(handler, rrt.getSelfReferencedType().getHandler());

        assertNull(rrt.getSelfReferencedType().getTypeIdResolver());
        rrt.setReference(new TypeReference<JavaType>() {}.getType());
        assertEquals(idResolver, rrt.getSelfReferencedType().getTypeIdResolver());
    }
}