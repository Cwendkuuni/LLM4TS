package com.yourpackage.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstruction() {
        JavaType listType = MapLikeType.construct(Map.class, String.class,
                Integer.class);
        MapLikeType mapType = (MapLikeType) listType;
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(String.class, mapType.getKeyType().getRawClass());
        assertEquals(Integer.class, mapType.getValueType().getRawClass());
    }

    @Test
    public void testMapLikeTypeUpgrade() {
        JavaType listType = JavaType.constructType(ArrayList.class);
        JavaType mapType = MapLikeType.upgradeFrom(listType, String.class,
                Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        assertEquals(ArrayList.class, mapLikeType._class);
        assertEquals(String.class, mapLikeType.getKeyType().getRawClass());
        assertEquals(Integer.class, mapLikeType.getValueType().getRawClass());
    }

    @Test
    public void testMapLikeTypeFactory() {
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        MapLikeType mapType = new MapLikeType(HashMap.class, TypeBindings.emptyBindings(),
                null, null, keyType, valueType);
        assertEquals(HashMap.class, mapType.getRawClass());
        assertEquals(String.class, mapType.getKeyType().getRawClass());
        assertEquals(Integer.class, mapType.getValueType().getRawClass());
    }

    @Test
    public void testMapLikeTypeIsContainerAndMapLike() {
        JavaType mapType = JavaType.constructType(HashMap.class, String.class,
                Integer.class);
        assertTrue(mapType.isContainerType());
        assertTrue(mapType.isMapLikeType());
    }

    @Test
    public void testMapLikeTypeGetKeyType() {
        JavaType mapType = JavaType.constructType(HashMap.class, String.class,
                Integer.class);
        JavaType keyType = mapType.getKeyType();
        assertEquals(String.class, keyType.getRawClass());
    }

    @Test
    public void testMapLikeTypeGetValueType() {
        JavaType mapType = JavaType.constructType(HashMap.class, String.class,
                Integer.class);
        JavaType valueType = mapType.getValueType();
        assertEquals(Integer.class, valueType.getRawClass());
    }

    @Test
    public void testMapLikeTypeGetContentType() {
        JavaType mapType = JavaType.constructType(HashMap.class, String.class,
                Integer.class);
        JavaType contentType = mapType.getContentType();
        assertEquals(Integer.class, contentType.getRawClass());
    }

    @Test
    public void testMapLikeTypeGetContentValueHandler() {
        JavaType mapType = JavaType.constructType(HashMap.class, String.class,
                Integer.class);
        Object contentValueHandler = mapType.getContentValueHandler();
        assertNotNull(contentValueHandler);
    }

    @Test
    public void testMapLikeTypeHasHandlers() {
        JavaType mapType = JavaType.constructType(HashMap.class, String.class,
                Integer.class);
        assertTrue(mapType.hasHandlers());
    }

    @Test
    public void testMapLikeTypeIsTrueMapType() {
        JavaType mapType = JavaType.constructType(HashMap.class, String.class,
                Integer.class);
        assertTrue(mapType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        JavaType mapType = JavaType.constructType(HashMap.class, String.class,
                Integer.class);
        JavaType newKeyType = JavaType.constructType(StringBuffer.class);
        MapLikeType newMapType = mapType.withKeyType(newKeyType);
        assertEquals(newKeyType, newMapType.getKeyType());
    }

    @Test
    public void testMapLikeTypeWithContentType() {
        JavaType mapType = JavaType.constructType(HashMap.class, String.class,
                Integer.class);
        JavaType newContentType = JavaType.constructType(Long.class);
        MapLikeType newMapType = mapType.withContentType(newContentType);
        assertEquals(newContentType, newMapType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithTypeHandler() {
        JavaType mapType = JavaType.constructType(HashMap.class, String.class,
                Integer.class);
        Object newTypeHandler = new Object() {};
        MapLikeType newMapType = mapType.withTypeHandler(newTypeHandler);
        assertEquals(newTypeHandler, newMapType.getTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithContentTypeHandler() {
        JavaType mapType = JavaType.constructType(HashMap.class, String.class,
                Integer.class);
        Object newContentTypeHandler = new Object() {};
        MapLikeType newMapType = mapType.withContentTypeHandler(newContentTypeHandler);
        assertEquals(newContentTypeHandler, newMapType.getValueType()
                .getTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithValueHandler() {
        JavaType mapType = JavaType.constructType(HashMap.class, String.class,
                Integer.class);
        Object newValueHandler = new Object() {};
        MapLikeType newMapType = mapType.withValueHandler(newValueHandler);
        assertEquals(newValueHandler, newMapType.getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithContentValueHandler() {
        JavaType mapType = JavaType.constructType(HashMap.class, String.class,
                Integer.class);
        Object newContentValueHandler = new Object() {};
        MapLikeType newMapType = mapType.withContentValueHandler(newContentValueHandler);
        assertEquals(newContentValueHandler, mapType.getValueType()
                .getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithStaticTyping() {
        JavaType mapType = JavaType.constructType(HashMap.class, String.class,
                Integer.class);
        MapLikeType newMapType = mapType.withStaticTyping();
        assertTrue(newMapType.isStaticTyping());
    }

    @Test
    public void testMapLikeTypeEquals() {
        JavaType mapType1 = JavaType.constructType(HashMap.class, String.class,
                Integer.class);
        JavaType mapType2 = JavaType.constructType(HashMap.class, String.class,
                Integer.class);
        JavaType mapType3 = JavaType.constructType(HashMap.class, Integer.class,
                String.class);
        assertTrue(mapType1.equals(mapType1));
        assertTrue(mapType1.equals(mapType2));
        assertFalse(mapType1.equals(mapType3));
    }

    @Test
    public void testMapLikeTypeHashCode() {
        JavaType mapType1 = JavaType.constructType(HashMap.class, String.class,
                Integer.class);
        JavaType mapType2 = JavaType.constructType(HashMap.class, String.class,
                Integer.class);
        JavaType mapType3 = JavaType.constructType(HashMap.class, Integer.class,
                String.class);
        int hashCode1 = mapType1.hashCode();
        int hashCode2 = mapType2.hashCode();
        int hashCode3 = mapType3.hashCode();
        assertEquals(hashCode1, hashCode1);
        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testMapLikeTypeToString() {
        JavaType mapType = JavaType.constructType(HashMap.class, String.class,
                Integer.class);
        String toString = mapType.toString();
        String expected = "[map-like type; class java.util.HashMap, java.lang.String -> java.lang.Integer]";
        assertEquals(expected, toString);
    }
}