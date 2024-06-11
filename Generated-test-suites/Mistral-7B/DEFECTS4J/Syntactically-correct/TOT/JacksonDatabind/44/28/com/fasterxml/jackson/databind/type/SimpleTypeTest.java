package com.fasterxml.jackson.databind.type;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = new SimpleType(simpleClass);
        assertEquals(simpleClass, simpleType.getRawClass());
        assertNotNull(simpleType.getBindings());
        assertNotNull(simpleType.getSuperClass());
        assertNotNull(simpleType.getSuperInterfaces());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<?> simpleClass = String.class;
        SimpleType original = new SimpleType(simpleClass);
        SimpleType copied = new SimpleType(original);
        assertEquals(original, copied);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructIllegalArgument() {
        Class<?> mapClass = HashMap.class;
        SimpleType.construct(mapClass);
    }

    @Test
    public void testCanonicalName() {
        Class<?> listClass = ArrayList.class;
        SimpleType listSimpleType = new SimpleType(listClass);
        assertEquals("java.util.ArrayList", listSimpleType.getCanonicalName());
    }

    @Test
    public void testEquals() {
        Class<?> stringClass1 = String.class;
        Class<?> stringClass2 = String.class;
        SimpleType simpleType1 = new SimpleType(stringClass1);
        SimpleType simpleType2 = new SimpleType(stringClass2);
        SimpleType simpleType3 = new SimpleType(Object.class);
        assertEquals(simpleType1, simpleType1);
        assertEquals(simpleType1, simpleType2);
        assertNotEquals(simpleType1, simpleType3);
    }

    @Test
    public void testRefine() {
        Class<?> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        JavaType refinedType = simpleType.refine(String[].class, null, null, null);
        assertNull(refinedType);
    }
}