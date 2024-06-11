package com.example.junit;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeCreation() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType baseType = JavaType.construct(mapClass, JavaType.construct(String.class), JavaType.construct(Integer.class));
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);

        assertEquals(MapLikeType.class, mapLikeType.getClass());
        assertEquals(mapClass, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeGetters() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType baseType = JavaType.construct(mapClass, JavaType.construct(String.class), JavaType.construct(Integer.class));
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);

        assertEquals(mapClass, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());

        JavaType newValueType = JavaType.construct(Long.class);
        MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);

        assertEquals(mapClass, newMapLikeType.getRawClass());
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(newValueType, newMapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeIsContainerTypeAndIsMapLikeType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType baseType = JavaType.construct(mapClass, JavaType.construct(String.class), JavaType.construct(Integer.class));
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);

        assertTrue(mapLikeType.isContainerType());
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testMapLikeTypeIsTrueMapType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType baseType = JavaType.construct(mapClass, JavaType.construct(String.class), JavaType.construct(Integer.class));
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);

        assertTrue(mapLikeType.isTrueMapType());

        Class<HashMap<String, String>> hashMapClass = HashMap.class;
        JavaType hashMapBaseType = JavaType.construct(hashMapClass, JavaType.construct(String.class), JavaType.construct(String.class));
        JavaType hashMapKeyType = JavaType.construct(String.class);
        JavaType hashMapValueType = JavaType.construct(String.class);
        MapLikeType hashMapMapLikeType = MapLikeType.upgradeFrom(hashMapBaseType, hashMapKeyType, hashMapValueType);

        assertFalse(hashMapMapLikeType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeWithKeyTypeHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType baseType = JavaType.construct(mapClass, JavaType.construct(String.class), JavaType.construct(Integer.class));
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);

        Object handler = new Object() {};
        MapLikeType newMapLikeType = mapLikeType.withKeyTypeHandler(handler);

        assertEquals(mapLikeType.getClass(), newMapLikeType.getClass());
        assertNotSame(mapLikeType.getKeyTypeHandler(), newMapLikeType.getKeyTypeHandler());
        assertEquals(handler, newMapLikeType.getKeyTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithContentTypeHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType baseType = JavaType.construct(mapClass, JavaType.construct(String.class), JavaType.construct(Integer.class));
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);

        Object handler = new Object() {};
        JavaType newValueType = JavaType.construct(Long.class);
        MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(handler);

        assertEquals(mapLikeType.getClass(), newMapLikeType.getClass());
        assertNotSame(mapLikeType.getValueTypeHandler(), newMapLikeType.getValueTypeHandler());
        assertEquals(handler, newMapLikeType.getValueTypeHandler());
        assertNotSame(mapLikeType.getValueType(), newMapLikeType.getValueType());
        assertEquals(newValueType, newMapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithValueHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType baseType = JavaType.construct(mapClass, JavaType.construct(String.class), JavaType.construct(Integer.class));
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);

        Object handler = new Object() {};
        MapLikeType newMapLikeType = mapLikeType.withValueHandler(handler);

        assertEquals(mapLikeType.getClass(), newMapLikeType.getClass());
        assertNotSame(mapLikeType.getValueHandler(), newMapLikeType.getValueHandler());
        assertEquals(handler, newMapLikeType.getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithContentValueHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType baseType = JavaType.construct(mapClass, JavaType.construct(String.class), JavaType.construct(Integer.class));
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);

        Object handler = new Object() {};
        MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(handler);

        assertEquals(mapLikeType.getClass(), newMapLikeType.getClass());
        assertNotSame(mapLikeType.getValueTypeHandler(), newMapLikeType.getValueTypeHandler());
        assertNotSame(mapLikeType.getValueType(), newMapLikeType.getValueType());
        assertEquals(handler, newMapLikeType.getValueTypeHandler());
        assertEquals(valueType, newMapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType baseType = JavaType.construct(mapClass, JavaType.construct(String.class), JavaType.construct(Integer.class));
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);

        JavaType newKeyType = JavaType.construct(Long.class);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);

        assertEquals(mapLikeType.getClass(), newMapLikeType.getClass());
        assertNotSame(mapLikeType.getKeyType(), newMapLikeType.getKeyType());
        assertEquals(newKeyType, newMapLikeType.getKeyType());
    }

    @Test
    public void testMapLikeTypeWithStaticTyping() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType baseType = JavaType.construct(mapClass, JavaType.construct(String.class), JavaType.construct(Integer.class));
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);

        MapLikeType newMapLikeType = mapLikeType.withStaticTyping();

        assertTrue(newMapLikeType.isStaticTyping());
        assertFalse(mapLikeType.isStaticTyping());
    }
}