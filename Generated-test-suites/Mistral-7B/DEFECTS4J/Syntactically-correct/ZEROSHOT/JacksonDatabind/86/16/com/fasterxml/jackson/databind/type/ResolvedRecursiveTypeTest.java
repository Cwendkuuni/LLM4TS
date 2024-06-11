package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Tests for {@link ResolvedRecursiveType} class.
 */
public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveType() {
        // Test constructor
        Class<ResolvedRecursiveType> clazz = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        Class<?> erasedType = ResolvedRecursiveType.class;
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        assertSame(erasedType, type.getRawClass());
        assertNull(type.getSelfReferencedType());
        assertNull(type.toString());

        // Test setReference method
        JavaType referencedType = createTestType();
        type.setReference(referencedType);
        assertSame(referencedType, type.getSelfReferencedType());

        // Test getSelfReferencedType method
        assertSame(referencedType, type.getSelfReferencedType());

        // Test equals method
        ResolvedRecursiveType otherType = new ResolvedRecursiveType(erasedType, bindings);
        assertFalse(type.equals(otherType));
        type.setReference(referencedType);
        assertTrue(type.equals(otherType));

        otherType = new ResolvedRecursiveType(Object.class, bindings);
        assertFalse(type.equals(otherType));

        // Test getGenericSignature and getErasedSignature methods
        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        assertSame("[]", sb.toString());
        type.getErasedSignature(sb);
        assertSame("Ljava/lang/Object;", sb.toString());

        // Test other methods
        assertNull(type.withContentType(JavaType.construct(String.class, new TypeReference[] {})));
        assertNull(type.withTypeHandler(null));
        assertNull(type.withContentTypeHandler(null));
        assertNull(type.withValueHandler(null));
        assertNull(type.withContentValueHandler(null));
        assertNull(type.withStaticTyping());
        assertNull(type.refine(Object.class, bindings, null, new JavaType[0]));
        assertFalse(type.isContainerType());
    }

    private JavaType createTestType() {
        JavaType elementType = TypeFactory.defaultInstance().constructType(String.class);
        return TypeFactory.defaultInstance().constructCollectionType(List.class, elementType);
    }
}