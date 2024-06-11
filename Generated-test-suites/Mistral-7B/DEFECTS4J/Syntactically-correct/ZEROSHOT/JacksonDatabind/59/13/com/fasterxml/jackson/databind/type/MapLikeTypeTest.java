package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeCreation() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = TypeFactory.defaultInstance().constructType(mapClass);
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, null, null, keyType, valueType);
        assertNotNull(mapLikeType);
        assertEquals(mapType, mapLikeType.getRawType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
    }

    @Test
    public void testMapLikeTypeUpgrade() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType listType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType listLikeType = new MapLikeType(listType, null, null, null, null, null);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(listLikeType, keyType, valueType);
        assertNotNull(mapLikeType);
        assertEquals(mapClass, mapLikeType.getRawType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = TypeFactory.defaultInstance().constructType(mapClass);
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, null, null, keyType, valueType);
        JavaType newKeyType = TypeFactory.defaultInstance().constructType(Long.class);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);
        assertNotNull(newMapLikeType);
        assertEquals(mapClass, newMapLikeType.getRawType());
        assertEquals(newKeyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getContentType());
    }

    @Test
    public void testMapLikeTypeWithValueType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = TypeFactory.defaultInstance().constructType(mapClass);
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, null, null, keyType, valueType);
        JavaType newValueType = TypeFactory.defaultInstance().constructType(Double.class);
        MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);
        assertNotNull(newMapLikeType);
        assertEquals(mapClass, newMapLikeType.getRawType());
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(newValueType, newMapLikeType.getContentType());
    }

    @Test
    public void testMapLikeTypeWithTypeHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = TypeFactory.defaultInstance().constructType(mapClass);
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, null, null, keyType, valueType);
        Object handler = new Object() {};
        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(handler);
        assertNotNull(newMapLikeType);
        assertEquals(mapClass, newMapLikeType.getRawType());
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getContentType());
        assertNull(newMapLikeType.getValueHandler());
        assertNull(newMapLikeType.getTypeHandler());
    }

    @Test
    public void testMapLikeTypeIsContainer() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = TypeFactory.defaultInstance().constructType(mapClass);
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, null, null, keyType, valueType);
        assertTrue(mapLikeType.isContainerType());
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testMapLikeTypeIsTrueMapType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = TypeFactory.defaultInstance().constructType(mapClass);
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, null, null, keyType, valueType);
        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeEquals() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = TypeFactory.defaultInstance().constructType(mapClass);
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType1 = new MapLikeType(mapType, bindings, null, null, keyType, valueType);
        MapLikeType mapLikeType2 = new MapLikeType(mapType, bindings, null, null, keyType, valueType);
        MapLikeType mapLikeType3 = new MapLikeType(new HashMap().getClass(), bindings, null, null, keyType, valueType);

        assertTrue(mapLikeType1.equals(mapLikeType2));
        assertTrue(mapLikeType1.equals(mapLikeType3));
    }

    @Test
    public void testMapLikeTypeToString() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = TypeFactory.defaultInstance().constructType(mapClass);
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, null, null, keyType, valueType);
        String expected = String.format("[map-like type; class %s, %s -> %s]",
                mapClass.getName(), keyType, valueType);
        assertEquals(expected, mapLikeType.toString());
    }
}