package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeCreation() {
        JavaType mapType = MapLikeType.upgradeFrom(Collections.map.class, String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, String.class, Integer.class);
        assertEquals("MapLikeType with correct key and value types", String.class, mapLikeType.getKeyType());
        assertEquals("MapLikeType with correct value type", Integer.class, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithKeyTypeHandler() {
        JavaType mapType = MapLikeType.upgradeFrom(Collections.map.class, String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, String.class, Integer.class);
        Object keyHandler = new Object() {}; // Assume this is a valid key handler
        MapLikeType mapLikeTypeWithKeyHandler = mapLikeType.withKeyTypeHandler(keyHandler);
        assertEquals("MapLikeType with correct key handler", keyHandler, mapLikeTypeWithKeyHandler.getKeyHandler());
    }

    @Test
    public void testMapLikeTypeWithValueTypeHandler() {
        JavaType mapType = MapLikeType.upgradeFrom(Collections.map.class, String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, String.class, Integer.class);
        Object valueHandler = new Object() {}; // Assume this is a valid value handler
        MapLikeType mapLikeTypeWithValueHandler = mapLikeType.withValueHandler(valueHandler);
        assertEquals("MapLikeType with correct value handler", valueHandler, mapLikeTypeWithValueHandler.getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithBothKeyAndValueTypeHandlers() {
        JavaType mapType = MapLikeType.upgradeFrom(Collections.map.class, String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, String.class, Integer.class);
        Object keyHandler = new Object() {}; // Assume this is a valid key handler
        Object valueHandler = new Object() {}; // Assume this is a valid value handler
        MapLikeType mapLikeTypeWithKeyAndValueHandlers = mapLikeType.withKeyTypeHandler(keyHandler).withValueHandler(valueHandler);
        assertEquals("MapLikeType with correct key handler", keyHandler, mapLikeTypeWithKeyAndValueHandlers.getKeyHandler());
        assertEquals("MapLikeType with correct value handler", valueHandler, mapLikeTypeWithKeyAndValueHandlers.getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        JavaType mapType = MapLikeType.upgradeFrom(Collections.map.class, String[].class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, String.class, Integer.class);
        JavaType stringArrayKeyType = new ArrayType(String.class, 1);
        MapLikeType mapLikeTypeWithKeyType = mapLikeType.withKeyType(stringArrayKeyType);
        assertEquals("MapLikeType with correct key type", stringArrayKeyType, mapLikeTypeWithKeyType.getKeyType());
    }

    @Test
    public void testMapLikeTypeWithValueType() {
        JavaType mapType = MapLikeType.upgradeFrom(Collections.map.class, String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, String.class, Integer.class);
        JavaType listIntType = new ArrayType(Integer.class, 1).getComponentType();
        MapLikeType mapLikeTypeWithValueType = mapLikeType.withContentType(listIntType);
        assertEquals("MapLikeType with correct value type", listIntType, mapLikeTypeWithValueType.getValueType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeInvalidBaseType() {
        JavaType baseType = new ClassType(String.class);
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        MapLikeType mapLikeType = new MapLikeType(baseType, keyType, valueType);
    }
}