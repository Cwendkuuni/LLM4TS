package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, bindings);

        JavaType refType = new ClassType(String.class);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType, bindings);

        type1.setReference(refType);
        assertNotNull(type1.getSelfReferencedType());
        assertSame(refType, type1.getSelfReferencedType());

        type2.setReference(null);
        assertNull(type2.getSelfReferencedType());

        try {
            type1.setReference(refType); // test re-setting
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // expected
        }
    }
}