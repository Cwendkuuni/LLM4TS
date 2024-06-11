package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstruction() {
        JavaType keyType = JavaType.constructType(String.class, int.class);
        JavaType valueType = JavaType.constructType(String.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, keyType, valueType);
        assertEquals("java.util.Map", mapLikeType.getClassName());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
    }

    @Test
    public void testMapLikeTypeUpgrade() {
        JavaType baseType = JavaType.constructType(HashMap.class, String.class, Integer.class);
        JavaType keyType = JavaType.constructType(String.class, int.class);
        JavaType valueType = JavaType.constructType(String.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
        assertEquals("com.fasterxml.jackson.core.type.MapLikeType", mapLikeType.getClassName());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        JavaType keyType = JavaType.constructType(String.class, int.class);
        JavaType valueType = JavaType.constructType(String.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, keyType, valueType);
        MapLikeType mapLikeTypeWithKeyType = mapLikeType.withKeyType(JavaType.constructType(String.class));
        assertEquals("com.fasterxml.jackson.core.type.MapLikeType", mapLikeTypeWithKeyType.getClassName());
        assertNotSame(keyType, mapLikeTypeWithKeyType.getKeyType());
        assertEquals(valueType, mapLikeTypeWithKeyType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeUpgradeInvalidBaseType() {
        JavaType baseType = JavaType.constructType(List.class, String.class);
        JavaType keyType = JavaType.constructType(String.class, int.class);
        JavaType valueType = JavaType.constructType(String.class);
        MapLikeType.upgradeFrom(baseType, keyType, valueType);
    }

    // other test cases for the methods "isContainerType", "isMapLikeType", "getErasedSignature", "getGenericSignature", "isTrueMapType", "toString", "equals" and edge cases
}