package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = new SimpleType(stringClass);
        SimpleType stringSimpleType2 = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        SimpleType stringSimpleType3 = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType stringSimpleType4 = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, 0, null, null, false);

        assertEquals(stringClass, stringSimpleType.getRawClass());
        assertEquals(stringSimpleType, stringSimpleType2);
        assertEquals(stringSimpleType, stringSimpleType3);
        assertEquals(stringSimpleType, stringSimpleType4);
    }

    @Test
    public void testCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = new SimpleType(stringClass);
        SimpleType stringSimpleTypeCopy = new SimpleType(stringSimpleType);

        assertEquals(stringSimpleType, stringSimpleTypeCopy);
    }

    @Test
    public void testConstructUnsafe() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = SimpleType.constructUnsafe(stringClass);
        SimpleType stringSimpleTypeExpected = new SimpleType(stringClass, null, null, null);

        assertEquals(stringSimpleTypeExpected, stringSimpleType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructDeprecated() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = SimpleType.construct(stringClass);
    }

    @Test
    public void testNarrow() {
        Class<String> stringClass = String.class;
        Class<Integer> integerClass = Integer.class;

        SimpleType stringSimpleType = new SimpleType(stringClass);
        SimpleType integerSimpleType = new SimpleType(integerClass);
        SimpleType narrowedType = stringSimpleType.narrow(integerClass);

        assertNull(narrowedType);
    }

    @Test
    public void testEquals() {
        Class<String> stringClass = String.class;
        Class<Integer> integerClass = Integer.class;
        SimpleType stringSimpleType = new SimpleType(stringClass);
        SimpleType integerSimpleType = new SimpleType(integerClass);
        SimpleType stringSimpleType2 = new SimpleType(stringClass);

        assertTrue(stringSimpleType.equals(stringSimpleType2));
        assertFalse(stringSimpleType.equals(integerSimpleType));
    }

    @Test
    public void testCanonicalName() {
        Class<List<String>> listStringClass = new ArrayList<String>().getClass();

        SimpleType listStringType = new SimpleType(listStringClass);
        String expected = "java.util.List<java.lang.String>";
        String result = listStringType.buildCanonicalName();

        assertEquals(expected, result);
    }

    @Test
    public void testGetErasedSignature() {
        Class<List<String>> listStringClass = new ArrayList<String>().getClass();
        SimpleType listStringType = new SimpleType(listStringClass);

        StringBuilder expected = new StringBuilder("java.util.List");
        StringBuilder result = listStringType.getErasedSignature(new StringBuilder());

        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<List<String>> listStringClass = new ArrayList<String>().getClass();
        SimpleType listStringType = new SimpleType(listStringClass);

        StringBuilder expected = new StringBuilder("java.util.List<");
        StringBuilder result = listStringType.getGenericSignature(new StringBuilder());

        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testIsContainerType() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = new SimpleType(stringClass);

        assertFalse(stringSimpleType.isContainerType());
    }

    @Test
    public void testToString() {
        Class<List<String>> listStringClass = new ArrayList<String>().getClass();
        SimpleType listStringType = new SimpleType(listStringClass);

        String expected = "[simple type, class java.util.List<java.lang.String>]";
        String result = listStringType.toString();

        assertEquals(expected, result);
    }
}