package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstruction() {
        // Test constructing from various inputs
        JavaType keyType = JavaType.constructParametrized(HashMap.class, String.class, Integer.class);
        JavaType valueType = JavaType.constructParametrized(String.class, String.class);
        MapLikeType mapLikeType = MapLikeType.construct(HashMap.class, keyType, valueType);

        assertEquals(HashMap.class, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
        assertNull(mapLikeType.getValueHandler());
        assertNull(mapLikeType.getTypeHandler());
        assertFalse(mapLikeType.isStatic());

        JavaType upgradedMapLikeType = MapLikeType.upgradeFrom(keyType, keyType, valueType);
        assertEquals(mapLikeType, upgradedMapLikeType);

        JavaType baseType = JavaType.construct(Map.class);
        JavaType upgradedBaseType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
        assertEquals(mapLikeType, upgradedBaseType);

        // Test withTypeHandler and withTypeHandler methods
        Object handler = new Object();
        MapLikeType withHandler = mapLikeType.withTypeHandler(handler);
        assertEquals(handler, withHandler.getTypeHandler());

        MapLikeType withStaticTyping = mapLikeType.withStaticTyping();
        assertTrue(withStaticTyping.isStatic());
        assertSame(mapLikeType, withStaticTyping.withKeyType(keyType));
    }

    @Test
    public void testMapLikeTypeMethods() {
        JavaType keyType = JavaType.constructParametrized(HashMap.class, String.class, Integer.class);
        JavaType valueType = JavaType.constructParametrized(String.class, String.class);
        MapLikeType mapLikeType = MapLikeType.construct(HashMap.class, keyType, valueType);

        // Test isContainerType and isMapLikeType methods
        assertTrue(mapLikeType.isContainerType());
        assertTrue(mapLikeType.isMapLikeType());

        // Test getKeyType, getContentType, getContentValueHandler and getContentTypeHandler methods
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
        assertNull(mapLikeType.getValueHandler());
        assertNull(mapLikeType.getTypeHandler());

        // Test hasHandlers method
        assertFalse(mapLikeType.hasHandlers());

        // Test withKeyType, withContentType, withValueHandler, withContentValueHandler, withTypeHandler and withContentTypeHandler methods
        JavaType newKeyType = JavaType.constructParametrized(TreeMap.class, String.class, Integer.class);
        JavaType newValueType = JavaType.constructParametrized(StringBuilder.class, String.class);
        MapLikeType withNewKeyType = mapLikeType.withKeyType(newKeyType);
        assertEquals(newKeyType, withNewKeyType.getKeyType());
        assertEquals(valueType, withNewKeyType.getValueType());

        MapLikeType withNewValueType = withNewKeyType.withContentType(newValueType);
        assertEquals(newKeyType, withNewValueType.getKeyType());
        assertEquals(newValueType, withNewValueType.getValueType());

        MapLikeType withHandlerValue = withNewKeyType.withValueHandler(new Object());
        assertEquals(newKeyType, withHandlerValue.getKeyType());
        assertEquals(newValueType, withHandlerValue.getValueType());
        assertSame(new Object(), withHandlerValue.getValueHandler());

        MapLikeType withHandlerType = withHandlerValue.withTypeHandler(new Object());
        assertEquals(newKeyType, withHandlerType.getKeyType());
        assertEquals(newValueType, withHandlerType.getValueType());
        assertSame(new Object(), withHandlerType.getTypeHandler());
    }

    @Test
    public void testMapLikeTypeToString() {
        JavaType keyType = JavaType.constructParametrized(HashMap.class, String.class, Integer.class);
        JavaType valueType = JavaType.constructParametrized(String.class, String.class);
        MapLikeType mapLikeType = MapLikeType.construct(HashMap.class, keyType, valueType);
        String expected = String.format("[map-like type; class %s, %s -> %s]",
                HashMap.class.getName(), keyType, valueType);
        assertEquals(expected, mapLikeType.toString());
    }
}