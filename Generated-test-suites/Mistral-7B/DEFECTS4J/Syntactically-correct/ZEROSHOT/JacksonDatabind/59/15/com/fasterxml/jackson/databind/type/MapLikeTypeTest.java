package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;

/**
 * Test class for MapLikeType
 */
public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstruction() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = MapLikeType.construct(mapClass, String.class,
                Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        assertEquals(String.class, mapLikeType.getKeyType());
        assertEquals(Integer.class, mapLikeType.getContentType());
        assertEquals(false, mapLikeType.isStaticTyping());
    }

    @Test
    public void testMapLikeTypeUpgrade() {
        Class<List<Map.Entry<String, Integer>>> listOfEntryClass = List.class;
        JavaType listOfEntryType = JavaType.constructType(listOfEntryClass,
                Map.Entry.class);
        JavaType mapType = MapLikeType.upgradeFrom(listOfEntryType, String.class,
                Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        assertEquals(String.class, mapLikeType.getKeyType());
        assertEquals(Integer.class, mapLikeType.getContentType());
        assertEquals(false, mapLikeType.isStaticTyping());
    }

    @Test
    public void testMapLikeTypeRefine() {
        Class<HashMap<String, Integer>> hashMapClass = HashMap.class;
        JavaType hashMapType = JavaType.constructType(hashMapClass,
                Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = (MapLikeType) hashMapType;
        JavaType refinedType = mapLikeType.refine(hashMapClass,
                TypeBindings.emptyBindings(), null, null);
        MapLikeType refinedMapLikeType = (MapLikeType) refinedType;
        assertEquals(String.class, refinedMapLikeType.getKeyType());
        assertEquals(Integer.class, refinedMapLikeType.getContentType());
        assertEquals(false, refinedMapLikeType.isStaticTyping());
    }

    @Test
    public void testMapLikeTypeEquals() {
        Class<Map<String, Integer>> mapClass1 = Map.class;
        Class<Map<String, Integer>> mapClass2 = Map.class;
        JavaType mapType1 = MapLikeType.construct(mapClass1, String.class,
                Integer.class);
        JavaType mapType2 = MapLikeType.construct(mapClass2, String.class,
                Integer.class);
        MapLikeType mapLikeType1 = (MapLikeType) mapType1;
        MapLikeType mapLikeType2 = (MapLikeType) mapType2;
        MapLikeType mapLikeType3 = new MapLikeType(mapClass1, TypeBindings.emptyBindings(),
                null, null, String.class, Integer.class, null, null, false);
        MapLikeType mapLikeType4 = new MapLikeType(mapClass1, TypeBindings.emptyBindings(),
                null, null, String.class, Integer.class, null, null, true);

        assertEquals(mapLikeType1, mapLikeType1);
        assertEquals(mapLikeType1, mapLikeType2);
        assertNotEquals(mapLikeType1, mapLikeType3);
        assertNotEquals(mapLikeType1, mapLikeType4);
    }

    @Test
    public void testMapLikeTypeHashCode() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType1 = MapLikeType.construct(mapClass, String.class,
                Integer.class);
        JavaType mapType2 = MapLikeType.construct(mapClass, String.class,
                Integer.class);
        MapLikeType mapLikeType1 = (MapLikeType) mapType1;
        MapLikeType mapLikeType2 = (MapLikeType) mapType2;

        int hashCode1 = mapLikeType1.hashCode();
        int hashCode2 = mapLikeType2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testMapLikeTypeToString() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = MapLikeType.construct(mapClass, String.class,
                Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        assertEquals("[" + mapClass.getName() + "; class " + mapClass.getName()
                + ", " + String.class.getName() + " -> " + Integer.class.getName()
                + "]", mapLikeType.toString());
    }

    @Test
    public void testMapLikeTypeIsContainerType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = MapLikeType.construct(mapClass, String.class,
                Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testMapLikeTypeIsMapLikeType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = MapLikeType.construct(mapClass, String.class,
                Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testMapLikeTypeIsTrueMapType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = MapLikeType.construct(mapClass, String.class,
                Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeGetKeyType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = MapLikeType.construct(mapClass, String.class,
                Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        assertEquals(String.class, mapLikeType.getKeyType());
    }

    @Test
    public void testMapLikeTypeGetContentType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = MapLikeType.construct(mapClass, String.class,
                Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        assertEquals(Integer.class, mapLikeType.getContentType());
    }

    @Test
    public void testMapLikeTypeGetContentValueHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = MapLikeType.construct(mapClass, String.class,
                Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        assertNull(mapLikeType.getContentValueHandler());
    }

    @Test
    public void testMapLikeTypeGetContentTypeHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = MapLikeType.construct(mapClass, String.class,
                Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        assertNull(mapLikeType.getContentTypeHandler());
    }

    @Test
    public void testMapLikeTypeHasHandlers() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = MapLikeType.construct(mapClass, String.class,
                Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        assertFalse(mapLikeType.hasHandlers());
    }

    @Test
    public void testMapLikeTypeGetErasedSignature() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = MapLikeType.construct(mapClass, String.class,
                Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        StringBuilder sb = new StringBuilder();
        mapLikeType.getErasedSignature(sb);
        assertEquals("Ljava/util/Map;", sb.toString());
    }

    @Test
    public void testMapLikeTypeGetGenericSignature() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = MapLikeType.construct(mapClass, String.class,
                Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        StringBuilder sb = new StringBuilder();
        mapLikeType.getGenericSignature(sb);
        assertEquals("[Ljava/util/Map<Ljava/lang/String;Ljava/lang/Integer;>;]",
                sb.toString());
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = MapLikeType.construct(mapClass, String.class,
                Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        JavaType newKeyType = String.class;
        JavaType newMapLikeType = mapLikeType.withKeyType(newKeyType);
        assertEquals(newKeyType, newMapLikeType.getKeyType());
        assertEquals(mapLikeType.getContentType(), newMapLikeType.getContentType());
        assertFalse(newMapLikeType.isStaticTyping());
    }

    @Test
    public void testMapLikeTypeWithContentType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = MapLikeType.construct(mapClass, String.class,
                Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        JavaType newContentType = Long.class;
        JavaType newMapLikeType = mapLikeType.withContentType(newContentType);
        assertEquals(mapLikeType.getKeyType(), newMapLikeType.getKeyType());
        assertEquals(newContentType, newMapLikeType.getContentType());
        assertFalse(newMapLikeType.isStaticTyping());
    }

    @Test
    public void testMapLikeTypeWithTypeHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = MapLikeType.construct(mapClass, String.class,
                Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        Object handler = new Object();
        JavaType newMapLikeType = mapLikeType.withTypeHandler(handler);
        assertNotEquals(mapLikeType.getKeyType(), newMapLikeType.getKeyType());
        assertNotEquals(mapLikeType.getContentType(), newMapLikeType.getContentType());
        assertEquals(handler, newMapLikeType.getTypeHandler());
        assertFalse(newMapLikeType.isStaticTyping());
    }

    @Test
    public void testMapLikeTypeWithContentTypeHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = MapLikeType.construct(mapClass, String.class,
                Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        Object handler = new Object();
        JavaType newMapLikeType = mapLikeType.withContentTypeHandler(handler);
        assertNotEquals(mapLikeType.getKeyType(), newMapLikeType.getKeyType());
        assertNotEquals(mapLikeType.getContentType(), newMapLikeType.getContentType());
        assertEquals(mapLikeType.getKeyType(), newMapLikeType.getKeyType());
        assertEquals(handler, newMapLikeType.getContentTypeHandler());
        assertFalse(newMapLikeType.isStaticTyping());
    }

    @Test
    public void testMapLikeTypeWithValueHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = MapLikeType.construct(mapClass, String.class,
                Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        Object handler = new Object();
        JavaType newMapLikeType = mapLikeType.withValueHandler(handler);
        assertNotEquals(mapLikeType.getKeyType(), newMapLikeType.getKeyType());
        assertNotEquals(mapLikeType.getContentType(), newMapLikeType.getContentType());
        assertNull(newMapLikeType.getValueHandler());
        assertEquals(handler, newMapLikeType.getContentValueHandler());
        assertFalse(newMapLikeType.isStaticTyping());
    }

    @Test
    public void testMapLikeTypeWithContentValueHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = MapLikeType.construct(mapClass, String.class,
                Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        Object handler = new Object();
        JavaType newMapLikeType = mapLikeType.withContentValueHandler(handler);
        assertNotEquals(mapLikeType.getKeyType(), newMapLikeType.getKeyType());
        assertNotEquals(mapLikeType.getContentType(), newMapLikeType.getContentType());
        assertNull(newMapLikeType.getValueHandler());
        assertEquals(handler, newMapLikeType.getContentValueHandler());
        assertFalse(newMapLikeType.isStaticTyping());
    }

    @Test
    public void testMapLikeTypeWithStaticTyping() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = MapLikeType.construct(mapClass, String.class,
                Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        JavaType newMapLikeType = mapLikeType.withStaticTyping();
        assertTrue(newMapLikeType.isStaticTyping());
        assertEquals(mapLikeType.getKeyType(), newMapLikeType.getKeyType());
        assertEquals(mapLikeType.getContentType(), newMapLikeType.getContentType());
    }

}