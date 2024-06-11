package com.example.junit;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstruction() {
        JavaType listType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(listType, String.class, Integer.class);
        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(String.class, mapLikeType.getKeyType().getRawClass());
        assertEquals(Integer.class, mapLikeType.getValueType().getRawClass());
    }

    @Test
    public void testUpgradeFromJavaType() {
        JavaType listType = new MapLikeType(ArrayList.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(ArrayList.class), null, String.class, Integer.class);
        JavaType mapLikeType = MapLikeType.upgradeFrom(listType, String.class, Integer.class);
        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(String.class, mapLikeType.getKeyType().getRawClass());
        assertEquals(Integer.class, mapLikeType.getValueType().getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidType() {
        JavaType listType = new MapLikeType(ArrayList.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(ArrayList.class), null, String.class, Integer.class);
        MapLikeType.upgradeFrom(new Object(), String.class, Integer.class);
    }

    @Test
    public void testWithKeyType() {
        JavaType listType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);
        JavaType mapLikeType = listType.withKeyType(String.class);
        assertEquals(String.class, mapLikeType.getKeyType().getRawClass());
    }

    @Test
    public void testWithValueType() {
        JavaType listType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);
        JavaType mapLikeType = listType.withContentType(Integer.class);
        assertEquals(Integer.class, mapLikeType.getValueType().getRawClass());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType listType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);
        Object handler = new Object();
        JavaType mapLikeType = listType.withTypeHandler(handler);
        assertNotNull(mapLikeType.getTypeHandler());
        assertEquals(handler, mapLikeType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType listType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);
        Object handler = new Object();
        JavaType valueType = new JavaType(HashMap.class, TypeBindings.emptyBindings());
        JavaType mapLikeType = listType.withContentTypeHandler(handler);
        assertEquals(handler, mapLikeType.getValueType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType listType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);
        Object handler = new Object();
        JavaType mapLikeType = listType.withValueHandler(handler);
        assertNotNull(mapLikeType.getValueHandler());
        assertEquals(handler, mapLikeType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType listType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);
        Object handler = new Object();
        JavaType valueType = new JavaType(HashMap.class, TypeBindings.emptyBindings());
        JavaType mapLikeType = listType.withContentValueHandler(handler);
        assertEquals(handler, mapLikeType.getValueType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType listType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);
        JavaType mapLikeType = listType.withStaticTyping();
        assertTrue(mapLikeType.isStatic());
    }

    @Test
    public void testRefine() {
        JavaType listType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);
        TypeBindings newBindings = TypeBindings.create(HashMap.class, String.class, Integer.class);
        JavaType newListType = listType.refine(HashMap.class, newBindings, null, null);
        assertEquals(HashMap.class, newListType.getRawClass());
    }

    @Test
    public void testIsContainerType() {
        JavaType listType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);
        assertTrue(listType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        JavaType listType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);
        assertTrue(listType.isMapLikeType());
    }

    @Test
    public void testGetKeyType() {
        JavaType listType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);
        assertEquals(String.class, listType.getKeyType().getRawClass());
    }

    @Test
    public void testGetContentType() {
        JavaType listType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);
        assertEquals(Integer.class, listType.getValueType().getRawClass());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType listType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);
        assertEquals(null, listType.getValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType listType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);
        assertEquals(null, listType.getTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        JavaType listType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);
        assertFalse(listType.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType listType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);
        StringBuilder sb = new StringBuilder();
        listType.getErasedSignature(sb);
        assertEquals("[Ljava.util.Map;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType listType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);
        StringBuilder sb = new StringBuilder();
        listType.getGenericSignature(sb);
        assertEquals("[Ljava.util.Map<Ljava.lang.String;Ljava.lang.Integer;>;]", sb.toString());
    }

    @Test
    public void testIsTrueMapType() {
        JavaType listType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);
        assertTrue(listType.isTrueMapType());
    }

    private static Class<?> _bogusSuperClass(Class<?> clazz) {
        return null;
    }
}