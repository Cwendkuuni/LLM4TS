package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.JavaType.GenericTypeResolver;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        // Test default constructor
        Class<?> intType = Integer.TYPE;
        SimpleType simpleType1 = new SimpleType(intType);
        assertNotNull(simpleType1);
        assertEquals(Integer.TYPE, simpleType1.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType1.getBindings());
        assertEquals(null, simpleType1.getSuperClass());
        assertEquals(null, simpleType1.getSuperTypes());
        assertEquals(0, simpleType1.getHashCode());
        assertEquals(false, simpleType1.isStatic());

        // Test constructor with bindings
        TypeBindings bindings = TypeBindings.builder()
                .constructRawType(Long.TYPE)
                .constructRawType(String.class)
                .build();
        Class<?> stringClass = String.class;
        SimpleType simpleType2 = new SimpleType(stringClass, bindings, null, null);
        assertNotNull(simpleType2);
        assertEquals(String.class, simpleType2.getRawClass());
        assertEquals(bindings, simpleType2.getBindings());
        assertEquals(null, simpleType2.getSuperClass());
        assertEquals(null, simpleType2.getSuperTypes());
        assertEquals(1, simpleType2.getHashCode());
        assertEquals(false, simpleType2.isStatic());

        // Test copy constructor
        SimpleType simpleType3 = new SimpleType(simpleType2);
        assertNotNull(simpleType3);
        assertEquals(simpleType2, simpleType3);
        assertEquals(String.class, simpleType3.getRawClass());
        assertEquals(bindings, simpleType3.getBindings());
        assertEquals(null, simpleType3.getSuperClass());
        assertEquals(null, simpleType3.getSuperTypes());
        assertEquals(1, simpleType3.getHashCode());
        assertEquals(false, simpleType3.isStatic());

        // Test constructor with superclass and superinterfaces
        SimpleType simpleType4 = new SimpleType(String.class, bindings,
                TypeFactory.constructType(List.class, Integer.TYPE),
                new JavaType[0]);
        assertNotNull(simpleType4);
        assertEquals(String.class, simpleType4.getRawClass());
        assertEquals(bindings, simpleType4.getBindings());
        assertEquals(Arrays.asList(TypeFactory.constructType(List.class, Integer.TYPE)),
                simpleType4.getTypes());
        assertEquals(1, simpleType4.getHashCode());
        assertEquals(false, simpleType4.isStatic());
    }

    @Test
    public void testNarrowing() {
        Class<?> intType = Integer.TYPE;
        Class<?> stringClass = String.class;
        SimpleType intSimpleType = new SimpleType(intType);
        SimpleType stringSimpleType = new SimpleType(stringClass);

        JavaType narrowed = intSimpleType.narrow(String.class);
        assertNull(narrowed);

        JavaType narrowedString = stringSimpleType.narrow(intType);
        assertEquals(intSimpleType, narrowedString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(ArrayList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(int[].class);
    }
}