package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveType() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        // Test initial state
        assertNull(resolvedRecursiveType.getSelfReferencedType());

        // Test setting self-reference
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        resolvedRecursiveType.setReference(refType);
        assertSame(refType, resolvedRecursiveType.getSelfReferencedType());

        // Test getting self-reference
        JavaType selfReferencedType = resolvedRecursiveType.getSelfReferencedType();
        assertSame(refType, selfReferencedType);

        // Test methods that should return this object
        assertSame(resolvedRecursiveType, resolvedRecursiveType.withContentType(null));
        assertSame(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(null));
        assertSame(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(null));
        assertSame(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(null));
        assertSame(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(null));
        assertSame(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());

        // Test hashCode and equals
        ResolvedRecursiveType other = new ResolvedRecursiveType(erasedType, bindings);
        assertNotSame(resolvedRecursiveType, other);
        assertFalse(resolvedRecursiveType.equals(other));

        resolvedRecursiveType.setReference(refType);
        other.setReference(refType);
        assertTrue(resolvedRecursiveType.equals(other));

        // Test toString
        assertEquals("[-recursive type; [Ljava/lang/Class;@6d000000]", resolvedRecursiveType.toString());
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateException() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        resolvedRecursiveType.setReference(refType);
        resolvedRecursiveType.setReference(refType);
    }

}