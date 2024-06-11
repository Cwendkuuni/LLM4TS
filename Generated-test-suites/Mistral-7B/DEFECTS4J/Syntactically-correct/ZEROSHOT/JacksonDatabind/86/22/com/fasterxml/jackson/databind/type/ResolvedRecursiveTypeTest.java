package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

/**
 * Test class for {@link ResolvedRecursiveType}.
 *
 * @author <a href="mailto:kostas.kostadis@gmail.com">Kostas Kostadis</a>
 */
public class ResolvedRecursiveTypeTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType instance = new ResolvedRecursiveType(erasedType, bindings);
        assertEquals(erasedType, instance.getRawClass());
        assertNull(instance.getSelfReferencedType());
    }

    /**
     * Test setReference method.
     */
    @Test
    public void testSetReference() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType instance = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new TypeReference<Object>() {}.getType();
        instance.setReference(refType);
        JavaType expected = refType;
        JavaType actual = instance.getSelfReferencedType();
        assertEquals(expected, actual);
    }

    /**
     * Test getSelfReferencedType method.
     */
    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType instance = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new TypeReference<Object>() {}.getType();
        instance.setReference(refType);
        JavaType expected = refType;
        JavaType actual = instance.getSelfReferencedType();
        assertEquals(expected, actual);
    }

    /**
     * Test getGenericSignature method.
     */
    @Test
    public void testGetGenericSignature() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType instance = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder expected = new StringBuilder("[recursive type; ");
        StringBuilder actual;
        try {
            actual = instance.getGenericSignature(new StringBuilder());
        } catch (IllegalStateException e) {
            actual = e.getMessage();
            fail("Unexpected exception: " + actual);
            return;
        }
        assertEquals(expected.toString(), actual.toString());

        // Test with self-reference
        JavaType refType = new TypeReference<Object>() {}.getType();
        instance.setReference(refType);
        expected = new StringBuilder("[recursive type; ");
        expected.append(refType.getGenericSignature(new StringBuilder()).toString()).append(']');
        actual = instance.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    /**
     * Test getErasedSignature method.
     */
    @Test
    public void testGetErasedSignature() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType instance = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder expected = new StringBuilder("[recursive type; ");
        StringBuilder actual;
        try {
            actual = instance.getErasedSignature(new StringBuilder());
        } catch (IllegalStateException e) {
            actual = e.getMessage();
            fail("Unexpected exception: " + actual);
            return;
        }
        assertEquals(expected.toString(), actual.toString());

        // Test with self-reference
        JavaType refType = new TypeReference<Object>() {}.getType();
        instance.setReference(refType);
        expected = new StringBuilder("[Ljava/lang/Object;");
        actual = instance.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        Class<?> erasedType1 = ResolvedRecursiveType.class;
        TypeBindings bindings1 = new TypeBindings();
        ResolvedRecursiveType instance1 = new ResolvedRecursiveType(erasedType1, bindings1);

        Class<?> erasedType2 = ResolvedRecursiveType.class;
        TypeBindings bindings2 = new TypeBindings();
        ResolvedRecursiveType instance2 = new ResolvedRecursiveType(erasedType2, bindings2);

        JavaType refType = new TypeReference<Object>() {}.getType();
        instance1.setReference(refType);
        instance2.setReference(refType);

        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance2));
        assertTrue(instance2.equals(instance1));
        assertTrue(instance1.equals(instance2));

        // Test with null
        assertFalse(instance1.equals(null));

        // Test with different classes
        Class<?> otherType = Object.class;
        ResolvedRecursiveType otherInstance = new ResolvedRecursiveType(otherType, bindings1);
        assertFalse(instance1.equals(otherInstance));

        // Test with different self-reference
        JavaType otherRefType = new TypeReference<String>() {}.getType();
        instance1.setReference(refType);
        instance2.setReference(otherRefType);
        assertFalse(instance1.equals(instance2));
    }
}