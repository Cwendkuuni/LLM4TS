package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link ResolvedRecursiveType}
 */
public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveType() {
        Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(type, bindings);

        // Test constructor
        assertNotNull(resolvedRecursiveType);
        assertEquals(type, resolvedRecursiveType.getRawClass());
        assertNull(resolvedRecursiveType.getSelfReferencedType());
        assertTrue(resolvedRecursiveType.isContainerType());

        // Test setReference method
        JavaType refType = new SimpleType(int[].class);
        resolvedRecursiveType.setReference(refType);
        JavaType selfReferencedType = resolvedRecursiveType.getSelfReferencedType();
        assertEquals(refType, selfReferencedType);

        // Test getSelfReferencedType method
        assertEquals(refType, resolvedRecursiveType.getSelfReferencedType());

        // Test getGenericSignature and getErasedSignature methods
        StringBuilder signatureSb = new StringBuilder(64);
        resolvedRecursiveType.getGenericSignature(signatureSb);
        String expectedGenericSignature = "[Ljava.lang.Class;";
        assertEquals(expectedGenericSignature, signatureSb.toString());

        resolvedRecursiveType.getErasedSignature(signatureSb);
        String expectedErasedSignature = "[Ljava.lang.Class;";
        assertEquals(expectedErasedSignature, signatureSb.toString());

        // Test methods returning this instance
        JavaType withContentType = resolvedRecursiveType.withContentType(String.class.getTypeName());
        assertEquals(resolvedRecursiveType, withContentType);

        Object h = new Object();
        JavaType withTypeHandler = resolvedRecursiveType.withTypeHandler(h);
        assertEquals(resolvedRecursiveType, withTypeHandler);

        JavaType withContentTypeHandler = resolvedRecursiveType.withContentTypeHandler(h);
        assertEquals(resolvedRecursiveType, withContentTypeHandler);

        JavaType withValueHandler = resolvedRecursiveType.withValueHandler(h);
        assertEquals(resolvedRecursiveType, withValueHandler);

        JavaType withContentValueHandler = resolvedRecursiveType.withContentValueHandler(h);
        assertEquals(resolvedRecursiveType, withContentValueHandler);

        JavaType withStaticTyping = resolvedRecursiveType.withStaticTyping();
        assertEquals(resolvedRecursiveType, withStaticTyping);

        // Test deprecated _narrow method
        assertNull(resolvedRecursiveType._narrow(int[].class));

        // Test refine method
        assertNull(resolvedRecursiveType.refine(ResolvedRecursiveType.class, bindings, null, new JavaType[0]));

        // Test isContainerType method
        assertFalse(resolvedRecursiveType.isContainerType());

        // Test toString method
        String expectedToString = "[recursive type; [Ljava.lang.Class;]";
        assertEquals(expectedToString, resolvedRecursiveType.toString());

        // Test equals method
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(type, bindings);
        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType2));

        JavaType refType2 = new SimpleType(int[].class);
        resolvedRecursiveType.setReference(refType2);
        resolvedRecursiveType2.setReference(refType);
        assertFalse(resolvedRecursiveType.equals(resolvedRecursiveType2));

        resolvedRecursiveType2.setReference(refType2);
        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType2));
    }
}