package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType1 = new SimpleType(simpleClass);
        SimpleType simpleType2 = new SimpleType(simpleClass, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType3 = new SimpleType(simpleClass, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType simpleType4 = new SimpleType(simpleType1);

        assertEquals(simpleClass, simpleType1.getRawClass());
        assertEquals(simpleType1.hashCode(), simpleType2.hashCode());
        assertEquals(simpleType1, simpleType4);
    }

    @Test
    public void testSimpleTypeUnsafeConstruction() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(simpleClass);
        assertEquals(simpleClass, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeInvalidConstruction() {
        Class<?> mapClass = HashMap.class;
        SimpleType simpleType = new SimpleType(mapClass); // Should not be able to construct
    }

    @Test
    public void testSimpleTypeWithContentType() {
        Class<?> listClass = ArrayList.class;
        JavaType javaType = TypeFactory.constructType(List.class, Integer.class);
        SimpleType simpleType = new SimpleType(listClass);

        try {
            simpleType.withContentType(javaType);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSimpleTypeWithContentTypeHandler() {
        Class<?> listClass = ArrayList.class;
        SimpleType simpleType = new SimpleType(listClass);

        try {
            simpleType.withContentTypeHandler(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            simpleType.withContentTypeHandler(new Object() {});
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<?> stringClass = String.class;
        Object stringHandler = new Object() {};
        SimpleType simpleType = new SimpleType(stringClass);

        SimpleType simpleTypeWithHandler = simpleType.withTypeHandler(stringHandler);
        assertEquals(simpleType, simpleTypeWithHandler);
        assertNotSame(simpleType, simpleTypeWithHandler);
        assertEquals(stringHandler, simpleTypeWithHandler.getTypeHandler());
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<?> intClass = Integer.class;
        Object intHandler = new Object() {};
        SimpleType simpleType = new SimpleType(intClass);

        SimpleType simpleTypeWithHandler = simpleType.withValueHandler(intHandler);
        assertEquals(simpleType, simpleTypeWithHandler);
        assertNotSame(simpleType, simpleTypeWithHandler);
        assertEquals(intHandler, simpleTypeWithHandler.getValueHandler());
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<?> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);

        SimpleType simpleTypeStatic = simpleType.withStaticTyping();
        assertEquals(simpleType, simpleTypeStatic);
        assertNotSame(simpleType, simpleTypeStatic);
        assertTrue(simpleTypeStatic.isStatic());
    }

    @Test
    public void testSimpleTypeRefine() {
        Class<?> intClass = Integer.class;
        SimpleType simpleType = new SimpleType(intClass);

        try {
            simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSimpleTypeBuildCanonicalName() {
        Class<?> listClass = ArrayList.class;
        SimpleType simpleType = new SimpleType(listClass);

        String canonicalName = simpleType.buildCanonicalName();
        assertTrue(canonicalName.contains("java.util.ArrayList"));
    }

    @Test
    public void testSimpleTypeGetErasedSignature() {
        Class<?> listClass = ArrayList.class;
        SimpleType simpleType = new SimpleType(listClass);

        StringBuilder expectedSignature = new StringBuilder("Ljava/util/ArrayList;");
        StringBuilder actualSignature = simpleType.getErasedSignature(new StringBuilder());

        assertEquals(expectedSignature.toString(), actualSignature.toString());
    }

    @Test
    public void testSimpleTypeGetGenericSignature() {
        Class<?> listClass = ArrayList.class;
        SimpleType simpleType = new SimpleType(listClass);

        StringBuilder expectedSignature = new StringBuilder("Ljava/util/List<Ljava/lang/Integer;>;");
        StringBuilder actualSignature = simpleType.getGenericSignature(new StringBuilder());

        assertEquals(expectedSignature.toString(), actualSignature.toString());
    }

    @Test
    public void testSimpleTypeIsContainerType() {
        Class<?> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);

        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testSimpleTypeEquals() {
        Class<?> stringClass = String.class;
        SimpleType simpleType1 = new SimpleType(stringClass);
        SimpleType simpleType2 = new SimpleType(stringClass);
        SimpleType simpleType3 = new SimpleType(String[].class);

        assertTrue(simpleType1.equals(simpleType2));
        assertTrue(simpleType1.equals(simpleType1));
        assertFalse(simpleType1.equals(simpleType3));
    }

    @Test
    public void testSimpleTypeHashCode() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType1 = new SimpleType(simpleClass);
        SimpleType simpleType2 = new SimpleType(simpleClass);

        int hashCode1 = simpleType1.hashCode();
        int hashCode2 = simpleType2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testSimpleTypeToString() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = new SimpleType(simpleClass);

        String toString = simpleType.toString();
        String expectedToString = "[simple type, class " + simpleClass.getName() + "]";

        assertEquals(expectedToString, toString);
    }
}