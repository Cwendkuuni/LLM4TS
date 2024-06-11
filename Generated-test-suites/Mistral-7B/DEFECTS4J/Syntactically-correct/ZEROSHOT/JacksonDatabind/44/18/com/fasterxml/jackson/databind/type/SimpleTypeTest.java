package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType1 = new SimpleType(simpleClass, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(simpleClass, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType simpleType3 = new SimpleType(simpleType1);

        assertEquals(simpleClass, simpleType1.getRawClass());
        assertEquals(simpleType1, simpleType2);
        assertEquals(simpleType1.hashCode(), simpleType2.hashCode());
        assertEquals(simpleType1, simpleType3);
        assertEquals(simpleType1.hashCode(), simpleType3.hashCode());
    }

    @Test
    public void testConstructUnsafe() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(simpleClass);
        assertEquals(simpleClass, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidClass() {
        Class<?> invalidClass = new ArrayList<Object>().getClass();
        SimpleType.construct(invalidClass);
    }

    @Test
    public void testNarrowType() {
        Class<?> parentClass = Object.class;
        Class<?> childClass = String.class;
        SimpleType parentType = new SimpleType(parentClass, TypeBindings.emptyBindings(), null, null);
        SimpleType childType = new SimpleType(childClass, TypeBindings.emptyBindings(), null, null);

        SimpleType narrowedType = childType.narrow(parentClass);
        assertNotNull(narrowedType);
        assertEquals(childType, narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowInvalidType() {
        Class<?> parentClass = Object.class;
        Class<?> childClass = Integer.class;
        SimpleType parentType = new SimpleType(parentClass, TypeBindings.emptyBindings(), null, null);
        SimpleType childType = new SimpleType(childClass, TypeBindings.emptyBindings(), null, null);

        SimpleType narrowedType = childType.narrow(parentClass);
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = new SimpleType(simpleClass, TypeBindings.emptyBindings(), null, null);
        Object handler = new Object();

        SimpleType typeWithHandler = simpleType.withTypeHandler(handler);
        assertNotNull(typeWithHandler);
        assertNotSame(simpleType, typeWithHandler);
        assertEquals(handler, typeWithHandler.getTypeHandler());
    }

    @Test
    public void testIsContainerType() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = new SimpleType(simpleClass, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testToString() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = new SimpleType(simpleClass, TypeBindings.emptyBindings(), null, null);

        String result = simpleType.toString();
        String expected = "[simple type, class " + simpleClass.getName() + "]";
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        Class<?> simpleClass1 = String.class;
        Class<?> simpleClass2 = Integer.class;

        SimpleType simpleType1 = new SimpleType(simpleClass1, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(simpleClass2, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType3 = new SimpleType(simpleClass1, TypeBindings.emptyBindings(), null, null);

        SimpleType simpleType4 = new SimpleType(simpleClass1, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType5 = new SimpleType(simpleClass1, TypeBindings.emptyBindings(), null, null, null, null, true);

        assertEquals(simpleType1, simpleType1);
        assertEquals(simpleType1.hashCode(), simpleType1.hashCode());
        assertEquals(simpleType1, simpleType3);
        assertEquals(simpleType1.hashCode(), simpleType3.hashCode());
        assertNotEquals(simpleType1, simpleType2);
        assertNotEquals(simpleType1.hashCode(), simpleType2.hashCode());
        assertNotEquals(simpleType1, simpleType4);
        assertNotEquals(simpleType1.hashCode(), simpleType4.hashCode());
        assertNotEquals(simpleType1, simpleType5);
        assertNotEquals(simpleType1.hashCode(), simpleType5.hashCode());
    }
}