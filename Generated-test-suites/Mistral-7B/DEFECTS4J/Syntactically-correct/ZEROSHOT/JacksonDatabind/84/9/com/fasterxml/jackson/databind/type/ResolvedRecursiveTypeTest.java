package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

/**
 * Test class for {@link ResolvedRecursiveType}.
 */
public class ResolvedRecursiveTypeTest {

    @Test
    public void testBasic() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(erasedType, bindings);
        // initial state
        assertNull(resolvedType.getSelfReferencedType());

        JavaType refType = new SimpleType(String.class);
        resolvedType.setReference(refType);
        JavaType ref = resolvedType.getSelfReferencedType();
        assertEquals(refType, ref);

        // test get signatures
        StringBuilder sb = new StringBuilder(100);
        resolvedType.getGenericSignature(sb);
        String genericSignature = sb.toString();
        assertNotNull(genericSignature);
        resolvedType.getErasedSignature(sb);
        String erasedSignature = sb.toString();
        assertNotNull(erasedSignature);

        // test other methods
        assertEquals(resolvedType, resolvedType.withContentType(null));
        assertEquals(resolvedType, resolvedType.withTypeHandler(null));
        assertEquals(resolvedType, resolvedType.withContentTypeHandler(null));
        assertEquals(resolvedType, resolvedType.withValueHandler(null));
        assertEquals(resolvedType, resolvedType.withContentValueHandler(null));
        assertEquals(resolvedType, resolvedType.withStaticTyping());

        // test deprecated method
        try {
            resolvedType._narrow(Object.class);
            fail("_narrow() should not be called");
        } catch (UnsupportedOperationException expected) {
        }

        // test refine method
        try {
            resolvedType.refine(Object.class, bindings, null, new JavaType[0]);
            fail("refine() should not be called");
        } catch (UnsupportedOperationException expected) {
        }

        // test isContainerType()
        assertFalse(resolvedType.isContainerType());

        // test toString()
        String toString = resolvedType.toString();
        assertTrue(toString.contains("[recursive type;"));
        if (resolvedType.getSelfReferencedType() != null) {
            assertTrue(toString.contains(resolvedType.getSelfReferencedType().getRawClass().getName()));
        } else {
            assertTrue(toString.contains("UNRESOLVED"));
        }

        // test equals()
        ResolvedRecursiveType resolvedType2 = new ResolvedRecursiveType(erasedType, bindings);
        assertFalse(resolvedType.equals(new Object()));
        assertFalse(resolvedType.equals(null));
        assertFalse(resolvedType.equals(resolvedType2));
        assertFalse(resolvedType.equals(new SimpleType(String.class)));

        resolvedType2.setReference(refType);
        assertTrue(resolvedType.equals(resolvedType2));
    }
}