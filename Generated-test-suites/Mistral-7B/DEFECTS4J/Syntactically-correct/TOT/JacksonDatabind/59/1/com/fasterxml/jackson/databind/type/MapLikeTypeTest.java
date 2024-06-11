package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class MapLikeTypeTest {

    @Test
    public void testValidMapLikeTypeCreation() {
        Class<Map<Integer, String>> rawType = Map.class;
        JavaType keyType = JavaType.constructType(Integer.class);
        JavaType valueType = JavaType.constructType(String.class);
        MapLikeType mapLikeType = new MapLikeType(rawType, TypeBindings.emptyBindings(), _bogusSuperClass(rawType), null, keyType, valueType);
        assertEquals("Expected MapLikeType instance", mapLikeType.getClass(), MapLikeType.class);
        assertEquals("Expected correct keyType", keyType, mapLikeType.getKeyType());
        assertEquals("Expected correct valueType", valueType, mapLikeType.getContentType());
    }

    @Test
    public void testMapLikeTypeWithKeyTypeHandler() {
        Class<Map<Integer, String>> rawType = Map.class;
        JavaType keyType = JavaType.constructType(Integer.class);
        JavaType valueType = JavaType.constructType(String.class);
        MapLikeType mapLikeType = new MapLikeType(rawType, TypeBindings.emptyBindings(), _bogusSuperClass(rawType), null, keyType, valueType);
        Object keyHandler = new Object();
        MapLikeType mapLikeTypeWithKeyHandler = mapLikeType.withKeyTypeHandler(keyHandler);
        assertEquals("Expected MapLikeType instance", mapLikeTypeWithKeyHandler.getClass(), MapLikeType.class);
        assertEquals("Expected correct keyType", keyType, mapLikeTypeWithKeyHandler.getKeyType());
        assertEquals("Expected correct keyHandler", keyHandler, mapLikeTypeWithKeyHandler.getKeyTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithValueTypeHandler() {
        Class<Map<Integer, String>> rawType = Map.class;
        JavaType keyType = JavaType.constructType(Integer.class);
        JavaType valueType = JavaType.constructType(String.class);
        MapLikeType mapLikeType = new MapLikeType(rawType, TypeBindings.emptyBindings(), _bogusSuperClass(rawType), null, keyType, valueType);
        Object valueHandler = new Object();
        MapLikeType mapLikeTypeWithValueHandler = mapLikeType.withValueHandler(valueHandler);
        assertEquals("Expected MapLikeType instance", mapLikeTypeWithValueHandler.getClass(), MapLikeType.class);
        assertEquals("Expected correct valueType", valueType, mapLikeTypeWithValueHandler.getContentType());
        assertEquals("Expected correct valueHandler", valueHandler, mapLikeTypeWithValueHandler.getValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenConstructingMapLikeTypeWithInvalidBaseType() {
        Class<List<Integer>> rawType = List.class; // Invalid base type
        JavaType keyType = JavaType.constructType(Integer.class);
        JavaType valueType = JavaType.constructType(String.class);
        new MapLikeType(rawType, TypeBindings.emptyBindings(), _bogusSuperClass(rawType), null, keyType, valueType); // Should throw IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenUpgradingFromInvalidBaseType() {
        JavaType baseType = JavaType.constructType(Long.class); // Invalid base type
        JavaType keyType = JavaType.constructType(Integer.class);
        JavaType valueType = JavaType.constructType(String.class);
        new MapLikeType.upgradeFrom(baseType, keyType, valueType); // Should throw IllegalArgumentException
    }

    @Test
    public void testIsTrueMapType() {
        Class<Map<Integer, String>> rawType = Map.class;
        JavaType keyType = JavaType.constructType(Integer.class);
        JavaType valueType = JavaType.constructType(String.class);
        MapLikeType mapLikeType = new MapLikeType(rawType, TypeBindings.emptyBindings(), _bogusSuperClass(rawType), null, keyType, valueType);
        assertTrue("Expected trueMapType", mapLikeType.isTrueMapType());
    }

    @Test
    public void testIsNotTrueMapType() {
        Class<List<Integer>> rawType = List.class;
        JavaType keyType = JavaType.constructType(Integer.class);
        JavaType valueType = JavaType.constructType(String.class);
        MapLikeType mapLikeType = new MapLikeType(rawType, TypeBindings.emptyBindings(), _bogusSuperClass(rawType), null, keyType, valueType);
        assertFalse("Expected falseMapType", mapLikeType.isTrueMapType());
    }

    @Test
    public void testIsMapLikeType() {
        Class<Map<Integer, String>> rawType = Map.class;
        JavaType keyType = JavaType.constructType(Integer.class);
        JavaType valueType = JavaType.constructType(String.class);
        MapLikeType mapLikeType = new MapLikeType(rawType, TypeBindings.emptyBindings(), _bogusSuperClass(rawType), null, keyType, valueType);
        assertTrue("Expected trueMapLikeType", mapLikeType.isMapLikeType());
    }

    @Test
    public void testIsNotMapLikeType() {
        Class<List<Integer>> rawType = List.class;
        JavaType keyType = JavaType.constructType(Integer.class);
        JavaType valueType = JavaType.constructType(String.class);
        MapLikeType mapLikeType = new MapLikeType(rawType, TypeBindings.emptyBindings(), _bogusSuperClass(rawType), null, keyType, valueType);
        assertFalse("Expected falseMapLikeType", mapLikeType.isMapLikeType());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<Map<Integer, String>> rawType = Map.class;
        JavaType keyType = JavaType.constructType(Integer.class);
        JavaType valueType = JavaType.constructType(String.class);
        MapLikeType mapLikeType = new MapLikeType(rawType, TypeBindings.emptyBindings(), _bogusSuperClass(rawType), null, keyType, valueType);
        assertEquals("Expected correct canonical name", "[map-like type; class java.util.Map, java.lang.Integer -> java.lang.String]", mapLikeType.buildCanonicalName());
    }

    @Test
    public void testGetErasedSignature() {
        Class<Map<Integer, String>> rawType = Map.class;
        JavaType keyType = JavaType.constructType(Integer.class);
        JavaType valueType = JavaType.constructType(String.class);
        MapLikeType mapLikeType = new MapLikeType(rawType, TypeBindings.emptyBindings(), _bogusSuperClass(rawType), null, keyType, valueType);
        StringBuilder expected = new StringBuilder("[map-like type; class java.util.Map, java.lang.Integer, java.lang.String]");
        assertEquals("Expected correct erased signature", expected.toString(), mapLikeType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<Map<Integer, String>> rawType = Map.class;
        JavaType keyType = JavaType.constructType(Integer.class);
        JavaType valueType = JavaType.constructType(String.class);
        MapLikeType mapLikeType = new MapLikeType(rawType, TypeBindings.emptyBindings(), _bogusSuperClass(rawType), null, keyType, valueType);
        StringBuilder expected = new StringBuilder("[map-like type; class java.util.Map<java.lang.Integer,java.lang.String>]");
        assertEquals("Expected correct generic signature", expected.toString(), mapLikeType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testIsContainerType() {
        Class<Map<Integer, String>> rawType = Map.class;
        JavaType keyType = JavaType.constructType(Integer.class);
        JavaType valueType = JavaType.constructType(String.class);
        MapLikeType mapLikeType = new MapLikeType(rawType, TypeBindings.emptyBindings(), _bogusSuperClass(rawType), null, keyType, valueType);
        assertTrue("Expected trueContainerType", mapLikeType.isContainerType());
    }

    @Test
    public void testIsNotContainerType() {
        Class<List<Integer>> rawType = List.class;
        JavaType keyType = JavaType.constructType(Integer.class);
        JavaType valueType = JavaType.constructType(String.class);
        MapLikeType mapLikeType = new MapLikeType(rawType, TypeBindings.emptyBindings(), _bogusSuperClass(rawType), null, keyType, valueType);
        assertFalse("Expected falseContainerType", mapLikeType.isContainerType());
    }

    @Test
    public void testEquals() {
        Class<Map<Integer, String>> rawType = Map.class;
        JavaType keyType = JavaType.constructType(Integer.class);
        JavaType valueType = JavaType.constructType(String.class);
        MapLikeType mapLikeType1 = new MapLikeType(rawType, TypeBindings.emptyBindings(), _bogusSuperClass(rawType), null, keyType, valueType);
        MapLikeType mapLikeType2 = new MapLikeType(rawType, TypeBindings.emptyBindings(), _bogusSuperClass(rawType), null, keyType, valueType);
        MapLikeType mapLikeType3 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(rawType), null, keyType, valueType);
        MapLikeType mapLikeType4 = new MapLikeType(rawType, TypeBindings.emptyBindings(), _bogusSuperClass(rawType), null, JavaType.constructType(Integer.class), valueType);
        MapLikeType mapLikeType5 = new MapLikeType(rawType, TypeBindings.emptyBindings(), _bogusSuperClass(rawType), null, keyType, JavaType.constructType(String.class));

        assertEquals("Expected equal instances", mapLikeType1, mapLikeType2);
        assertNotEquals("Expected not equal instances", mapLikeType1, mapLikeType3);
        assertNotEquals("Expected not equal instances", mapLikeType1, mapLikeType4);
        assertNotEquals("Expected not equal instances", mapLikeType1, mapLikeType5);
    }

    @Test
    public void testHashCode() {
        Class<Map<Integer, String>> rawType = Map.class;
        JavaType keyType = JavaType.constructType(Integer.class);
        JavaType valueType = JavaType.constructType(String.class);
        MapLikeType mapLikeType1 = new MapLikeType(rawType, TypeBindings.emptyBindings(), _bogusSuperClass(rawType), null, keyType, valueType);
        MapLikeType mapLikeType2 = new MapLikeType(rawType, TypeBindings.emptyBindings(), _bogusSuperClass(rawType), null, keyType, valueType);
        MapLikeType mapLikeType3 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(rawType), null, keyType, valueType);
        MapLikeType mapLikeType4 = new MapLikeType(rawType, TypeBindings.emptyBindings(), _bogusSuperClass(rawType), null, JavaType.constructType(Integer.class), valueType);
        MapLikeType mapLikeType5 = new MapLikeType(rawType, TypeBindings.emptyBindings(), _bogusSuperClass(rawType), null, keyType, JavaType.constructType(String.class));

        int hashCode1 = mapLikeType1.hashCode();
        int hashCode2 = mapLikeType2.hashCode();
        int hashCode3 = mapLikeType3.hashCode();
        int hashCode4 = mapLikeType4.hashCode();
        int hashCode5 = mapLikeType5.hashCode();

        assertEquals("Expected equal hashcodes", hashCode1, hashCode2);
        assertNotEquals("Expected not equal hashcodes", hashCode1, hashCode3);
        assertNotEquals("Expected not equal hashcodes", hashCode1, hashCode4);
        assertNotEquals("Expected not equal hashcodes", hashCode1, hashCode5);
    }

    @Test
    public void testToString() {
        Class<Map<Integer, String>> rawType = Map.class;
        JavaType keyType = JavaType.constructType(Integer.class);
        JavaType valueType = JavaType.constructType(String.class);
        MapLikeType mapLikeType = new MapLikeType(rawType, TypeBindings.emptyBindings(), _bogusSuperClass(rawType), null, keyType, valueType);
        String expected = "Expected [map-like type; class java.util.Map, java.lang.Integer -> java.lang.String]";
        assertEquals("Expected correct toString", expected, mapLikeType.toString());
    }

    private static Class<?> _bogusSuperClass(Class<?> clazz) {
        return Object.class; // This is a bogus superclass, used only for testing
    }
}