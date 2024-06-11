package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass);
        assertEquals(Integer.class, intType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), intType.getBindings());
        assertNull(intType.getSuperClass());
        assertNull(intType.getSuperInterfaces());
        assertFalse(intType.isStatic());

        TypeBindings bindings = TypeBindings.emptyBindings();
        bindings.put(Integer.TYPE, JavaType.of(String.class));

        SimpleType intTypeWithBindings = new SimpleType(intClass, bindings, null, null);
        assertEquals(Integer.class, intTypeWithBindings.getRawClass());
        assertNotSame(TypeBindings.emptyBindings(), intTypeWithBindings.getBindings());
        assertNull(intTypeWithBindings.getSuperClass());
        assertNull(intTypeWithBindings.getSuperInterfaces());
        assertFalse(intTypeWithBindings.isStatic());
        assertEquals(JavaType.of(String.class), intTypeWithBindings.containedType(0));

        SimpleType intTypeWithSuperClass = new SimpleType(intClass, null, JavaType.of(Object.class), null);
        assertEquals(Integer.class, intTypeWithSuperClass.getRawClass());
        assertNull(intTypeWithSuperClass.getBindings());
        assertEquals(JavaType.of(Object.class), intTypeWithSuperClass.getSuperClass());
        assertNull(intTypeWithSuperClass.getSuperInterfaces());
        assertFalse(intTypeWithSuperClass.isStatic());

        SimpleType intTypeWithSuperClassAndInterfaces = new SimpleType(intClass, null, JavaType.of(Object.class),
                new JavaType[] { JavaType.of(List.class), JavaType.of(Map.class) });
        assertEquals(Integer.class, intTypeWithSuperClassAndInterfaces.getRawClass());
        assertNull(intTypeWithSuperClassAndInterfaces.getBindings());
        assertEquals(JavaType.of(Object.class), intTypeWithSuperClassAndInterfaces.getSuperClass());
        assertArrayEquals(new JavaType[] { JavaType.of(List.class), JavaType.of(Map.class) }, intTypeWithSuperClassAndInterfaces.getSuperInterfaces());
        assertFalse(intTypeWithSuperClassAndInterfaces.isStatic());
    }

    @Test
    public void testSimpleTypeNarrowing() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass, null, null, null);
        SimpleType intSubClassType = new SimpleType(intClass.getSuperclass(), null, null, null);

        assertNull(intType.narrow(intClass));
        assertEquals(intType, intType.narrow(intClass));
        assertEquals(intSubClassType, intType.narrow(intClass.getSuperclass()));
        assertNotSame(intType, intType.narrow(intClass.getSuperclass()));
    }

    @Test
    public void testSimpleTypeWithContentType() {
        Class<Map<String, Integer>> mapClass = Map.class.cast(Class.forName("java.util.Map<java.lang.String, java.lang.Integer>"));
        SimpleType mapType = new SimpleType(mapClass, null, null, null);

        try {
            mapType.withContentType(JavaType.of(String[].class));
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass, null, null, null);
        Object intHandler = new Object() {};

        SimpleType intTypeWithHandler = intType.withTypeHandler(intHandler);
        assertNotSame(intType, intTypeWithHandler);
        assertEquals(intType.getRawClass(), intTypeWithHandler.getRawClass());
        assertEquals(intHandler, intTypeWithHandler.getTypeHandler());
    }

    @Test
    public void testSimpleTypeWithContentTypeHandler() {
        Class<Map<String, Integer>> mapClass = Map.class.cast(Class.forName("java.util.Map<java.lang.String, java.lang.Integer>"));
        SimpleType mapType = new SimpleType(mapClass, null, null, null);

        try {
            mapType.withContentTypeHandler(JavaType.of(String[].class).getValueHandler());
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSimpleTypeGetCanonicalName() {
        Class<Map<String, List<Integer>>> mapListClass = Map.class.cast(Class.forName("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>"));
        SimpleType mapListType = new SimpleType(mapListClass, null, null, null);

        String canonicalName = mapListType.getCanonicalName();
        assertTrue(canonicalName.startsWith("java.util.Map"));
        assertTrue(canonicalName.endsWith(" java.util.List<java.lang.Integer>"));
    }

    @Test
    public void testSimpleTypeIsContainerType() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass, null, null, null);

        assertFalse(intType.isContainerType());
    }

    @Test
    public void testSimpleTypeToString() {
        Class<Map<String, List<Integer>>> mapListClass = Map.class.cast(Class.forName("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>"));
        SimpleType mapListType = new SimpleType(mapListClass, null, null, null);

        String toString = mapListType.toString();
        assertTrue(toString.contains("[simple type, class "));
        assertTrue(toString.contains(mapListClass.getName()));
        assertTrue(toString.contains("]"));
    }

    @Test
    public void testSimpleTypeEquals() {
        Class<Map<String, List<Integer>>> mapListClass = Map.class.cast(Class.forName("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>"));
        SimpleType mapListType1 = new SimpleType(mapListClass, null, null, null);
        SimpleType mapListType2 = new SimpleType(mapListClass, null, null, null);

        assertEquals(mapListType1, mapListType2);

        SimpleType mapListType3 = new SimpleType(Map.class, null, null, null);
        assertNotEquals(mapListType1, mapListType3);
    }

    @Test
    public void testSimpleTypeHashCode() {
        Class<Map<String, List<Integer>>> mapListClass = Map.class.cast(Class.forName("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>"));
        SimpleType mapListType1 = new SimpleType(mapListClass, null, null, null);
        SimpleType mapListType2 = new SimpleType(mapListClass, null, null, null);

        int hashCode1 = mapListType1.hashCode();
        int hashCode2 = mapListType2.hashCode();

        assertEquals(hashCode1, hashCode2);

        SimpleType mapListType3 = new SimpleType(Map.class, null, null, null);
        assertNotEquals(hashCode1, hashCode3);
    }
}