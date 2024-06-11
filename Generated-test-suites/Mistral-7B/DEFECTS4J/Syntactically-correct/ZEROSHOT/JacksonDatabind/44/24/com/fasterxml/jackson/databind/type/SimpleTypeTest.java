package com.example.junit; // replace with your package name

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<String> stringClass = String.class;
        SimpleType stringType = SimpleType.constructUnsafe(stringClass);
        assertNotNull(stringType);
        assertEquals(String.class, stringType.getRawClass());
        assertNull(stringType.getSuperClass());
        assertNull(stringType.getSuperInterfaces());
        assertFalse(stringType.isStatic());
    }

    @Test
    public void testConstruct() {
        Class<String> stringClass = String.class;
        SimpleType stringType = SimpleType.construct(stringClass);
        assertNotNull(stringType);
        assertEquals(String.class, stringType.getRawClass());
        assertNull(stringType.getSuperClass());
        assertNull(stringType.getSuperInterfaces());
        assertFalse(stringType.isStatic());

        try {
            SimpleType listType = SimpleType.construct(ArrayList.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNarrow() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType stringSubclassType = new SimpleType(StringBuilder.class, TypeBindings.emptyBindings(), stringType, null, null, null, false);

        JavaType result = stringType.narrow(StringBuilder.class);
        assertEquals(stringSubclassType, result);

        try {
            stringType.narrow(Object.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIsContainerType() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, null, false);
        assertFalse(stringType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, null, false);
        StringBuilder expected = new StringBuilder("Ljava/lang/String;");
        StringBuilder actual = stringType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, null, false);
        StringBuilder expected = new StringBuilder("Ljava/lang/String;");
        StringBuilder actual = stringType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());

        SimpleType listType = new SimpleType(ArrayList.class, TypeBindings.emptyBindings(), null, null, null, null, false);
        StringBuilder listExpected = new StringBuilder("Ljava/util/ArrayList;<Ljava/lang/Object;>");
        StringBuilder listActual = listType.getGenericSignature(new StringBuilder());
        assertEquals(listExpected.toString(), listActual.toString());
    }

    @Test
    public void testToString() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, null, false);
        String expected = "[simple type, class " + stringClass.getName() + "]";
        String actual = stringType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Class<String> stringClass = String.class;
        SimpleType stringType1 = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType stringType2 = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, null, false);
        assertTrue(stringType1.equals(stringType2));

        SimpleType stringType3 = new SimpleType(Integer.class, TypeBindings.emptyBindings(), null, null, null, null, false);
        assertFalse(stringType1.equals(stringType3));
    }
}