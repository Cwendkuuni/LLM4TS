package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Test for {@link ResolvedRecursiveType} class.
 */
public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        // Create a new instance of ResolvedRecursiveType
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        // Set a reference to another JavaType
        JavaType ref = new TypeFactory().constructType(String.class);
        type.setReference(ref);

        // Check if the reference was set correctly
        JavaType actual = type.getSelfReferencedType();
        assertSame("Expected reference not set correctly", ref, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        // Create a new instance of ResolvedRecursiveType
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        // Set a reference to another JavaType
        JavaType ref = new TypeFactory().constructType(String.class);
        type.setReference(ref);

        // Try to set a new reference
        JavaType ref2 = new TypeFactory().constructType(Integer.class);
        type.setReference(ref2);
    }

    @Test
    public void testGetSelfReferencedType() {
        // Create a new instance of ResolvedRecursiveType with a reference set
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new TypeFactory().constructType(String.class);
        type.setReference(ref);

        // Check if the self-referenced type was set correctly
        JavaType actual = type.getSelfReferencedType();
        assertSame("Expected self-referenced type not set correctly", ref, actual);
    }

    @Test
    public void testGetGenericSignature() {
        // Create a new instance of ResolvedRecursiveType with a reference set
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new TypeFactory().constructType(String.class);
        type.setReference(ref);

        // Create a StringBuilder for the generic signature
        StringBuilder expected = new StringBuilder();
        type.getGenericSignature(expected);

        // Check if the generic signature was generated correctly
        StringBuilder actual = new StringBuilder();
        JavaType selfReferencedType = type.getSelfReferencedType();
        selfReferencedType.getGenericSignature(actual);
        assertSame("Expected generic signatures not equal", expected.toString(), actual.toString());
    }

    @Test
    public void testGetErasedSignature() {
        // Create a new instance of ResolvedRecursiveType with a reference set
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new TypeFactory().constructType(String.class);
        type.setReference(ref);

        // Create a StringBuilder for the erased signature
        StringBuilder expected = new StringBuilder();
        type.getErasedSignature(expected);

        // Check if the erased signature was generated correctly
        StringBuilder actual = new StringBuilder();
        JavaType selfReferencedType = type.getSelfReferencedType();
        selfReferencedType.getErasedSignature(actual);
        assertSame("Expected erased signatures not equal", expected.toString(), actual.toString());
    }

    @Test
    public void testWithContentType() {
        // Create a new instance of ResolvedRecursiveType
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        // Set a content type and check if it was set correctly
        JavaType contentType = new TypeFactory().constructType(String[].class);
        JavaType actual = type.withContentType(contentType);
        assertSame("Expected content type not set correctly", type, actual);
    }

    @Test
    public void testEquals() {
        // Create two instances of ResolvedRecursiveType with the same self-referenced type
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new TypeFactory().constructType(String.class);
        type1.setReference(ref);

        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType, bindings);
        type2.setReference(ref);

        // Check if the instances are equal
        boolean actual = type1.equals(type2);
        assertTrue("Expected instances to be equal", actual);
    }

    @Test
    public void testNotEqual() {
        // Create two instances of ResolvedRecursiveType with different self-referenced types
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref1 = new TypeFactory().constructType(String.class);
        type1.setReference(ref1);

        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref2 = new TypeFactory().constructType(Integer.class);
        type2.setReference(ref2);

        // Check if the instances are not equal
        boolean actual = type1.equals(type2);
        assertFalse("Expected instances to be not equal", actual);
    }

    @Test
    public void testUnresolvedReferenceEquals() {
        // Create two instances of ResolvedRecursiveType with unresolved references
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, bindings);

        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType, bindings);

        // Check if the instances are not equal
        boolean actual = type1.equals(type2);
        assertFalse("Expected unresolved references not to be equal", actual);
    }
}