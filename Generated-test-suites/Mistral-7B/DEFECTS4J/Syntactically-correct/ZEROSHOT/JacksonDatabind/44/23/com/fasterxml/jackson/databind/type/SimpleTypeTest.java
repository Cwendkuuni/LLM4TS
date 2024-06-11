package com.example.junit;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<Integer> intType = Integer.class;
        SimpleType simpleType = new SimpleType(intType);
        assertEquals(intType, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testSimpleTypeWithBindings() {
        Class<List<Integer>> listType = List.class.getTypeParams()[0];
        SimpleType simpleType = new SimpleType(listType, TypeBindings.emptyBindings());
        assertEquals(listType, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testSimpleTypeWithSuperClass() {
        Class<String> stringType = String.class;
        JavaType superClass = TypeFactory.defaultInstance().constructType(List.class);
        SimpleType simpleType = new SimpleType(stringType, TypeBindings.emptyBindings(), superClass, null);
        assertEquals(stringType, simpleType.getRawClass());
        assertEquals(superClass, simpleType.getSuperClass());
        assertArrayEquals(new Class[]{}, simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testSimpleTypeWithSuperInterfaces() {
        Class<String> stringType = String.class;
        JavaType superInterface1 = TypeFactory.defaultInstance().constructType(Comparable.class);
        JavaType superInterface2 = TypeFactory.defaultInstance().constructType(Serializable.class);
        SimpleType simpleType = new SimpleType(stringType, TypeBindings.emptyBindings(), null,
                new JavaType[] { superInterface1, superInterface2 });
        assertEquals(stringType, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertArrayEquals(new JavaType[] { superInterface1, superInterface2 }, simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<Integer> intType = Integer.class;
        SimpleType simpleType = new SimpleType(intType, TypeBindings.emptyBindings(), null, null, null, new IntegerToString(), false);
        assertEquals(intType, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
        assertEquals(new IntegerToString(), simpleType.getValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidType() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowInvalidSubtype() {
        Class<String> stringType = String.class;
        SimpleType simpleType = new SimpleType(intType, TypeBindings.emptyBindings(), null, null, null, null, false);
        simpleType.narrow(String.class);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<List<List<Integer>>> listOfListIntType = List.class.getTypeParams()[0].getTypeParams()[0].getTypeParams()[0];
        SimpleType simpleType = new SimpleType(listOfListIntType, TypeBindings.emptyBindings());
        assertEquals("[java.util.List, java.util.List, java.lang.Integer]", simpleType.buildCanonicalName());
    }

    @Test
    public void testIsContainerType() {
        assertFalse(new SimpleType(Integer.class).isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<List<Integer>> listType = List.class.getTypeParams()[0];
        SimpleType simpleType = new SimpleType(listType, TypeBindings.emptyBindings());
        StringBuilder expected = new StringBuilder("[Ljava.util.List;");
        StringBuilder actual = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<List<List<Integer>>> listOfListIntType = List.class.getTypeParams()[0].getTypeParams()[0].getTypeParams()[0];
        SimpleType simpleType = new SimpleType(listOfListIntType, TypeBindings.emptyBindings());
        StringBuilder expected = new StringBuilder("[List<List<Integer>>]");
        StringBuilder actual = new StringBuilder();
        simpleType.getGenericSignature(actual);
        assertEquals(expected.toString(), actual.toString());
    }

}