package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<Integer> intType = Integer.class;
        SimpleType intTypeSimple = new SimpleType(intType);
        SimpleType intTypeSimple2 = new SimpleType(intTypeSimple);
        SimpleType intTypeSimple3 = new SimpleType(intTypeSimple2);

        assertEquals(intType, intTypeSimple.getRawClass());
        assertEquals(intTypeSimple, intTypeSimple2);
        assertEquals(intTypeSimple, intTypeSimple3);
        assertEquals(intTypeSimple.getClass(), SimpleType.class);
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<Integer> intType = Integer.class;
        SimpleType intTypeSimple = new SimpleType(intType);
        SimpleType intTypeSimpleCopy = new SimpleType(intTypeSimple);

        assertNotSame(intTypeSimple, intTypeSimpleCopy);
        assertEquals(intType, intTypeSimple.getRawClass());
        assertEquals(intType, intTypeSimpleCopy.getRawClass());
    }

    @Test
    public void testSimpleTypeWithBindings() {
        Class<Integer> intType = Integer.class;
        SimpleType intTypeSimple = new SimpleType(intType, TypeBindings.emptyBindings());
        SimpleType intTypeSimple2 = new SimpleType(intType, TypeBindings.emptyBindings());

        assertEquals(intType, intTypeSimple.getRawClass());
        assertEquals(intTypeSimple, intTypeSimple2);

        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, intType);
        SimpleType listIntTypeSimple = new SimpleType(javaType);

        assertFalse(intTypeSimple.equals(listIntTypeSimple));
        assertEquals(intType, intTypeSimple.getRawClass());
        assertEquals(JavaType.class, listIntTypeSimple.getRawClass());
    }

    @Test
    public void testSimpleTypeWithSuperClassesAndInterfaces() {
        Class<String> stringClass = String.class;
        Class<List<Integer>> listIntClass = List.class.getTypeParams()[0].getType();
        JavaType listIntJavaType = TypeFactory.defaultInstance().constructType(List.class, intType);
        SimpleType listIntTypeSimple = new SimpleType(listIntJavaType, TypeBindings.emptyBindings(), null,
                new JavaType[] { TypeFactory.defaultInstance().constructType(List.class) });

        assertArrayEquals(new JavaType[] { listIntJavaType }, listIntTypeSimple.getSuperTypes());
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<String> stringClass = String.class;
        SimpleType stringTypeSimple = new SimpleType(stringClass);
        SimpleType stringTypeSimpleWithHandler = new SimpleType(stringClass, null, null, null, null, null, false,
                new StringHandler());

        assertNotSame(stringTypeSimple, stringTypeSimpleWithHandler);
        assertEquals(stringClass, stringTypeSimple.getRawClass());
        assertEquals(stringClass, stringTypeSimpleWithHandler.getRawClass());
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<String> stringClass = String.class;
        SimpleType stringTypeSimple = new SimpleType(stringClass);
        SimpleType stringTypeSimpleWithHandler = new SimpleType(stringClass, null, null, null, null,
                new TypeHandler(), false);

        assertNotSame(stringTypeSimple, stringTypeSimpleWithHandler);
        assertEquals(stringClass, stringTypeSimple.getRawClass());
        assertEquals(stringClass, stringTypeSimpleWithHandler.getRawClass());
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<String> stringClass = String.class;
        SimpleType stringTypeSimple = new SimpleType(stringClass);
        SimpleType stringTypeSimpleStatic = new SimpleType(stringClass, null, null, null, null, null, false);
        SimpleType stringTypeSimpleStatic2 = new SimpleType(stringClass, null, null, null, null, null, true);

        assertFalse(stringTypeSimple.isStaticTyping());
        assertTrue(stringTypeSimpleStatic.isStaticTyping());
        assertTrue(stringTypeSimpleStatic2.isStaticTyping());
        assertEquals(stringClass, stringTypeSimple.getRawClass());
        assertEquals(stringClass, stringTypeSimpleStatic.getRawClass());
        assertEquals(stringClass, stringTypeSimpleStatic2.getRawClass());
    }

    @Test
    public void testSimpleTypeWithContentTypeHandler() {
        Class<String> stringClass = String.class;
        SimpleType stringTypeSimple = new SimpleType(stringClass);

        assertNull(stringTypeSimple.getContentTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeMethod() {
        new SimpleType(String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMethod() {
        new SimpleType(String[].class);
    }

    @Test
    public void testNarrowMethod() {
        Class<String> stringClass = String.class;
        SimpleType stringTypeSimple = new SimpleType(stringClass);
        SimpleType stringTypeSimpleSubClass = new SimpleType(StringBuilder.class);

        SimpleType narrowedType = stringTypeSimple.narrow(StringBuilder.class);
        assertEquals(stringTypeSimpleSubClass, narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowMethodWithInvalidSubclass() {
        Class<String> stringClass = String.class;
        SimpleType stringTypeSimple = new SimpleType(stringClass);
        SimpleType stringTypeSimpleSubClass = new SimpleType(Integer.class);

        stringTypeSimple.narrow(Integer.class);
    }

}

class StringHandler {

}