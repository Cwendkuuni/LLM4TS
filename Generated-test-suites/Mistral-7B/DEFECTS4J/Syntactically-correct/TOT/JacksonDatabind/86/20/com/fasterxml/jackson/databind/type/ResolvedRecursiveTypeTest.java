package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        Class<ResolvedRecursiveType> selfType = ResolvedRecursiveType.class;
        TypeBindings bindings = TypeFactory.defaultInstance().constructTypeBindings();
        ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(selfType, bindings);

        JavaType referencedType = TypeFactory.defaultInstance().constructType(String.class, bindings);
        selfRefType.setReference(referencedType);

        JavaType selfTypeFromRef = selfRefType.getSelfReferencedType();
        assertEquals(selfType, selfTypeFromRef);
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<ResolvedRecursiveType> selfType = ResolvedRecursiveType.class;
        TypeBindings bindings = TypeFactory.defaultInstance().constructTypeBindings();
        ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(selfType, bindings);

        JavaType referencedType = TypeFactory.defaultInstance().constructType(String.class, bindings);
        selfRefType.setReference(referencedType);

        JavaType selfTypeFromRef = selfRefType.getSelfReferencedType();
        assertEquals(selfType, selfTypeFromRef);
    }

    @Test
    public void testGetGenericSignature() {
        Class<ResolvedRecursiveType> selfType = ResolvedRecursiveType.class;
        TypeBindings bindings = TypeFactory.defaultInstance().constructTypeBindings();
        ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(selfType, bindings);
        StringBuilder sb = new StringBuilder(100);

        JavaType referencedType = TypeFactory.defaultInstance().constructType(String.class, bindings);
        selfRefType.setReference(referencedType);

        selfRefType.getGenericSignature(sb);

        String expected = "[recursive type; class com.fasterxml.jackson.databind.type.ResolvedRecursiveType<java.lang.String>]";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testEquals() {
        Class<ResolvedRecursiveType> selfType = ResolvedRecursiveType.class;
        TypeBindings bindings = TypeFactory.defaultInstance().constructTypeBindings();

        ResolvedRecursiveType selfRefType1 = new ResolvedRecursiveType(selfType, bindings);
        ResolvedRecursiveType selfRefType2 = new ResolvedRecursiveType(selfType, bindings);

        JavaType referencedType = TypeFactory.defaultInstance().constructType(String.class, bindings);
        selfRefType1.setReference(referencedType);

        ResolvedRecursiveType selfRefType3 = new ResolvedRecursiveType(selfType, bindings);
        selfRefType3.setReference(referencedType);

        ResolvedRecursiveType selfRefType4 = new ResolvedRecursiveType(String.class, bindings);

        List<JavaType> types = new ArrayList<>();
        types.add(selfRefType1);
        types.add(selfRefType2);
        types.add(selfRefType3);
        types.add(selfRefType4);

        for (JavaType type : types) {
            for (JavaType other : types) {
                if (type != other) {
                    assertFalse(type.equals(other));
                } else {
                    assertTrue(type.equals(other));
                }
            }
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<ResolvedRecursiveType> selfType = ResolvedRecursiveType.class;
        TypeBindings bindings = TypeFactory.defaultInstance().constructTypeBindings();
        ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(selfType, bindings);

        JavaType referencedType = TypeFactory.defaultInstance().constructType(String.class, bindings);
        selfRefType.setReference(referencedType);

        selfRefType.setReference(referencedType);
    }
}