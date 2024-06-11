package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<Integer> intType = Integer.class;
        SimpleType simpleType = new SimpleType(intType);
        JavaType javaType = simpleType;

        assertEquals(intType, javaType.getRawClass());
        assertNull(javaType.getSuperClass());
        assertNull(javaType.getSuperTypes());
        assertFalse(javaType.isContainerType());

        // Test toString()
        assertEquals("[simple type, class java.lang.Integer]", javaType.toString());
    }

    @Test
    public void testSimpleTypeWithSuperClass() {
        Class<Number> numberType = Number.class;
        SimpleType simpleType = new SimpleType(numberType, null, TypeFactory.defaultInstance.constructType(Integer.class), null);
        JavaType javaType = simpleType;

        assertEquals(numberType, javaType.getRawClass());
        assertEquals(Integer.class, javaType.getSuperClass());
        assertArrayEquals(new JavaType[0], javaType.getSuperTypes());
        assertFalse(javaType.isContainerType());

        // Test toString()
        assertEquals("[simple type, class java.lang.Number, superclass class java.lang.Integer]", javaType.toString());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringType = String.class;
        SimpleType originalSimpleType = new SimpleType(stringType, TypeBindings.emptyBindings(), null, null);
        JavaType originalJavaType = originalSimpleType;

        SimpleType copiedSimpleType = new SimpleType(originalJavaType);
        JavaType copiedJavaType = copiedSimpleType;

        assertEquals(originalJavaType.getRawClass(), copiedJavaType.getRawClass());
        assertEquals(originalJavaType.getSuperClass(), copiedJavaType.getSuperClass());
        assertArrayEquals(originalJavaType.getSuperTypes(), copiedJavaType.getSuperTypes());
        assertFalse(originalJavaType.isContainerType());
        assertFalse(copiedJavaType.isContainerType());

        // Test toString()
        assertEquals(originalJavaType.toString(), copiedJavaType.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeInvalidConstruction() {
        Class<Map<Integer, String>> mapType = new Class[2][].getClass();
        SimpleType simpleType = new SimpleType(mapType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeUnsupportedConstruction() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
    }
}