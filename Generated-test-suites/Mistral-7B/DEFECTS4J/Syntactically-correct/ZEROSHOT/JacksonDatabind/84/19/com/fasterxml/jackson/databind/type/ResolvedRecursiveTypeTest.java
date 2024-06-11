package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for ResolvedRecursiveType.
 */
public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveType() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();

        // Test constructor
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType, bindings);
        assertNotNull(resolvedRecursiveType1);
        assertEquals(erasedType, resolvedRecursiveType1.getRawClass());
        assertNull(resolvedRecursiveType1.getSelfReferencedType());

        // Test setReference method
        JavaType javaType = new SimpleModule().getTypeFactory().constructType(String.class);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType2.setReference(javaType);
        ResolvedRecursiveType expectedResolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        expectedResolvedRecursiveType.setReference(javaType);
        assertEquals(expectedResolvedRecursiveType, resolvedRecursiveType2);
        assertEquals(javaType, resolvedRecursiveType2.getSelfReferencedType());

        // Test getSelfReferencedType method
        assertEquals(javaType, resolvedRecursiveType2.getSelfReferencedType());
        assertNull(resolvedRecursiveType1.getSelfReferencedType());

        // Test getGenericSignature and getErasedSignature methods
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType2.getGenericSignature(sb);
        String expectedGenericSignature = "<T>" + javaType.getGenericSignature(sb).toString();
        String actualGenericSignature = sb.toString();
        assertEquals(expectedGenericSignature, actualGenericSignature);
        String expectedErasedSignature = javaType.getErasedSignature(new StringBuilder()).toString();
        String actualErasedSignature = resolvedRecursiveType2.getErasedSignature(new StringBuilder()).toString();
        assertEquals(expectedErasedSignature, actualErasedSignature);

        // Test other methods that return this
        JavaType withContentType = resolvedRecursiveType2.withContentType(javaType);
        assertEquals(resolvedRecursiveType2, withContentType);
        JavaType withTypeHandler = resolvedRecursiveType2.withTypeHandler(new Object());
        assertEquals(resolvedRecursiveType2, withTypeHandler);
        JavaType withContentTypeHandler = resolvedRecursiveType2.withContentTypeHandler(new Object());
        assertEquals(resolvedRecursiveType2, withContentTypeHandler);
        JavaType withValueHandler = resolvedRecursiveType2.withValueHandler(new Object());
        assertEquals(resolvedRecursiveType2, withValueHandler);
        JavaType withContentValueHandler = resolvedRecursiveType2.withContentValueHandler(new Object());
        assertEquals(resolvedRecursiveType2, withContentValueHandler);
        JavaType withStaticTyping = resolvedRecursiveType2.withStaticTyping();
        assertEquals(resolvedRecursiveType2, withStaticTyping);

        // Test deprecated method _narrow
        Class<?> subclass = Object.class;
        JavaType _narrowType = resolvedRecursiveType2._narrow(subclass);
        assertNull(_narrowType);

        // Test refine method
        JavaType refinedType = new SimpleModule().getTypeFactory().constructType(Map.class);
        JavaType result = resolvedRecursiveType2.refine(Map.class, bindings, null, new JavaType[] { javaType });
        assertNull(result);

        // Test isContainerType method
        assertFalse(resolvedRecursiveType2.isContainerType());

        // Test toString method
        String expectedToString = "[recursive type; " + javaType.getRawClass().getName() + "]";
        String actualToString = resolvedRecursiveType2.toString();
        assertEquals(expectedToString, actualToString);

        // Test equals method
        ResolvedRecursiveType resolvedRecursiveType3 = new ResolvedRecursiveType(erasedType, bindings);
        assertNotEquals(resolvedRecursiveType2, resolvedRecursiveType3);
        resolvedRecursiveType3.setReference(javaType);
        assertTrue(resolvedRecursiveType2.equals(resolvedRecursiveType3));
    }

}