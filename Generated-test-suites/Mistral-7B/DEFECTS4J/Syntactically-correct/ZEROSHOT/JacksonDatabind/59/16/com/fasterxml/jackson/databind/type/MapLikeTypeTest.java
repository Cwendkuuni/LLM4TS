package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBase;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeCreation() {
        JavaType keyType = JavaType.constructParametricType(HashMap.class, String.class, Integer.class);
        JavaType valueType = JavaType.constructParametricType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);

        assertEquals(Map.class, mapLikeType.getRawType());
        assertEquals(String.class, mapLikeType.getKeyType().getRawType());
        assertEquals(Integer.class, mapLikeType.getValueType().getRawType());

        MapLikeType upgradedMapLikeType = MapLikeType.upgradeFrom(HashMap.class, keyType, valueType);
        assertEquals(mapLikeType, upgradedMapLikeType);

        TypeBase base = JavaType.constructParametricType(HashMap.class, String.class, Integer.class);
        JavaType upgradedMapLikeTypeFromBase = MapLikeType.upgradeFrom(base, keyType, valueType);
        assertEquals(mapLikeType, upgradedMapLikeTypeFromBase);
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        JavaType keyType = JavaType.constructParametricType(HashMap.class, String.class, Integer.class);
        JavaType valueType = JavaType.constructParametricType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);

        JavaType newKeyType = JavaType.constructParametricType(HashMap.class, String.class, Long.class);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);

        assertEquals(mapLikeType.getClass(), newMapLikeType.getClass());
        assertNotEquals(mapLikeType, newMapLikeType);
        assertEquals(newKeyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithValueType() {
        JavaType keyType = JavaType.constructParametricType(HashMap.class, String.class, Integer.class);
        JavaType valueType = JavaType.constructParametricType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);

        JavaType newValueType = JavaType.constructParametricType(Long.class);
        MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);

        assertEquals(mapLikeType.getClass(), newMapLikeType.getClass());
        assertNotEquals(mapLikeType, newMapLikeType);
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(newValueType, newMapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithTypeHandler() {
        JavaType keyType = JavaType.constructParametricType(HashMap.class, String.class, Integer.class);
        JavaType valueType = JavaType.constructParametricType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);

        Object handler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(handler);

        assertEquals(mapLikeType.getClass(), newMapLikeType.getClass());
        assertNotEquals(mapLikeType, newMapLikeType);
        assertEquals(handler, newMapLikeType.getTypeHandler());
        assertEquals(handler, newMapLikeType.getValueType().getTypeHandler());
        assertEquals(handler, newMapLikeType.getKeyType().getTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithContentTypeHandler() {
        JavaType keyType = JavaType.constructParametricType(HashMap.class, String.class, Integer.class);
        JavaType valueType = JavaType.constructParametricType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);

        Object handler = new Object();
        JavaType newValueType = JavaType.constructParametricType(Long.class).withTypeHandler(handler);
        MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(handler);

        assertEquals(mapLikeType.getClass(), newMapLikeType.getClass());
        assertNotEquals(mapLikeType, newMapLikeType);
        assertEquals(handler, newMapLikeType.getTypeHandler());
        assertEquals(newValueType, newMapLikeType.getValueType());
        assertEquals(valueType, newMapLikeType.getKeyType().getValueType());
        assertEquals(handler, newMapLikeType.getKeyType().getTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithValueHandler() {
        JavaType keyType = JavaType.constructParametricType(HashMap.class, String.class, Integer.class);
        JavaType valueType = JavaType.constructParametricType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);

        Object handler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withValueHandler(handler);

        assertEquals(mapLikeType.getClass(), newMapLikeType.getClass());
        assertNotEquals(mapLikeType, newMapLikeType);
        assertEquals(handler, newMapLikeType.getValueHandler());
        assertEquals(handler, newMapLikeType.getValueType().getValueHandler());
        assertEquals(handler, newMapLikeType.getKeyType().getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithContentValueHandler() {
        JavaType keyType = JavaType.constructParametricType(HashMap.class, String.class, Integer.class);
        JavaType valueType = JavaType.constructParametricType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);

        Object handler = new Object();
        JavaType newValueType = JavaType.constructParametricType(Long.class).withValueHandler(handler);
        MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(handler);

        assertEquals(mapLikeType.getClass(), newMapLikeType.getClass());
        assertNotEquals(mapLikeType, newMapLikeType);
        assertEquals(handler, newMapLikeType.getValueHandler());
        assertEquals(newValueType, newMapLikeType.getValueType());
        assertEquals(valueType, newMapLikeType.getKeyType().getValueType());
        assertEquals(handler, newMapLikeType.getKeyType().getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithStaticTyping() {
        JavaType keyType = JavaType.constructParametricType(HashMap.class, String.class, Integer.class);
        JavaType valueType = JavaType.constructParametricType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);

        MapLikeType newMapLikeType = mapLikeType.withStaticTyping();

        assertEquals(mapLikeType.getClass(), newMapLikeType.getClass());
        assertNotEquals(mapLikeType, newMapLikeType);
        assertTrue(newMapLikeType.isStaticTyping());
        assertFalse(mapLikeType.isStaticTyping());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeInvalidUpgrade() {
        JavaType keyType = JavaType.constructParametricType(HashMap.class, String.class, Integer.class);
        JavaType valueType = JavaType.constructParametricType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);

        JavaType baseType = JavaType.constructParametricType(ArrayList.class, Integer.class);
        MapLikeType upgradedMapLikeType = mapLikeType.withStaticTyping()._narrow(baseType);
    }
}