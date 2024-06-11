package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = new SimpleType(simpleClass);
        JavaType constructedType = simpleType.construct();

        assertEquals(simpleClass, constructedType.getType());
        assertEquals(simpleType, constructedType);
    }

    @Test
    public void testSimpleTypeNarrowing() {
        Class<?> stringClass = String.class;
        Class<?> stringBuilderClass = StringBuilder.class;
        SimpleType stringType = new SimpleType(stringClass);
        SimpleType stringBuilderType = new SimpleType(stringBuilderClass);

        JavaType narrowedType = stringType.narrow(stringBuilderClass);
        assertEquals(stringBuilderType, narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeInvalidConstruction() {
        Class<?> mapClass = Map.class;
        SimpleType simpleType = new SimpleType(mapClass);
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<?> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        Object typeHandler = new Object() {
            // Dummy type handler
        };

        JavaType typeWithHandler = simpleType.withTypeHandler(typeHandler);
        assertEquals(typeHandler, typeWithHandler.getTypeHandler());
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<?> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        Object valueHandler = new Object() {
            // Dummy value handler
        };

        JavaType typeWithHandler = simpleType.withValueHandler(valueHandler);
        assertEquals(valueHandler, typeWithHandler.getValueHandler());
    }
}