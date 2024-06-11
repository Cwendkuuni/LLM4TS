package com.example.junit;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstruction() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, keyType, valueType);
        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, keyType, valueType);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(new SimpleType("java.lang.Integer")
                .withoutErasing());
        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(new SimpleType("java.lang.Integer").withoutErasing(), newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithContentType() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, keyType, valueType);
        MapLikeType newMapLikeType = mapLikeType.withContentType(new SimpleType("java.lang.Double")
                .withoutErasing());
        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(new SimpleType("java.lang.Double").withoutErasing(), newMapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, keyType, valueType);
        Object handler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(handler);
        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(handler, newMapLikeType.getTypeHandler());
        assertEquals(handler, newMapLikeType.getValueType().getTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithContentTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, keyType, valueType);
        Object handler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(handler);
        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(handler, newMapLikeType.getValueType().getTypeHandler());
        assertEquals(mapLikeType.getTypeHandler(), newMapLikeType.getTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, keyType, valueType);
        Object handler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withValueHandler(handler);
        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(handler, newMapLikeType.getValueHandler());
        assertEquals(valueType.getValueHandler(), newMapLikeType.getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithContentValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, keyType, valueType);
        Object handler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(handler);
        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(handler, newMapLikeType.getValueType().getValueHandler());
        assertEquals(valueType.getValueHandler(), newMapLikeType.getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithStaticTyping() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, keyType, valueType);
        MapLikeType newMapLikeType = mapLikeType.withStaticTyping();
        assertNotSame(mapLikeType, newMapLikeType);
        assertTrue(newMapLikeType.isStaticTyping());
        assertFalse(mapLikeType.isStaticTyping());
    }

    @Test
    public void testMapLikeTypeIsContainerType() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, keyType, valueType);
        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testMapLikeTypeIsMapLikeType() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, keyType, valueType);
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testMapLikeTypeGetKeyType() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, keyType, valueType);
        assertEquals(keyType, mapLikeType.getKeyType());
    }

    @Test
    public void testMapLikeTypeGetContentType() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, keyType, valueType);
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeHasHandlers() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, keyType, valueType);
        assertTrue(mapLikeType.hasHandlers());
        MapLikeType mapLikeTypeWithHandlers = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, keyType, valueType, new Object(),
                new Object(), false);
        assertTrue(mapLikeTypeWithHandlers.hasHandlers());
    }

    @Test
    public void testMapLikeTypeIsTrueMapType() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, keyType, valueType);
        assertTrue(mapLikeType.isTrueMapType());
    }

    private static JavaType _bogusSuperClass(Class<?> clazz) {
        return null;
    }
}