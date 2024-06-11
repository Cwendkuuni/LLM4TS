package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeCreation() throws Exception {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType javaType = new MapLikeType.Builder()
                .baseType(mapClass)
                .keyType(String.class)
                .valueType(Integer.class)
                .build();

        MapLikeType mapLikeType = (MapLikeType) javaType;
        assertEquals("Map<String, Integer>", mapLikeType.getCanonicalName());
        assertEquals(String.class, mapLikeType.getKeyType());
        assertEquals(Integer.class, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithKeyTypeHandler() throws Exception {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType javaType = new MapLikeType.Builder()
                .baseType(mapClass)
                .keyType(String.class)
                .valueType(Integer.class)
                .keyTypeHandler(String::toUpperCase)
                .build();

        MapLikeType mapLikeType = (MapLikeType) javaType;
        assertEquals("Map<String, Integer>", mapLikeType.getCanonicalName());
        assertEquals(String.class, mapLikeType.getKeyType());
        assertEquals(Integer.class, mapLikeType.getValueType());
        assertNotNull(mapLikeType.getKeyTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidBaseType() throws Exception {
        JavaType baseType = TypeBase.construct(int[].class, String.class,
                Integer.class);
        new MapLikeType.Builder().baseType(baseType).build();
    }

    @Test
    public void testUpgradeFrom() throws Exception {
        JavaType listType = TypeFactory.defaultInstance().constructCollectionType(
                ArrayList.class, String.class);
        JavaType mapType = MapLikeType.upgradeFrom(listType, String.class,
                Integer.class);
        assertEquals("Map<String, Integer>", mapType.getCanonicalName());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithInvalidBaseType() throws Exception {
        JavaType baseType = TypeBase.construct(int[].class, String.class,
                Integer.class);
        new MapLikeType.Builder().baseType(baseType).build();
    }

    @Test
    public void testWithKeyType() throws Exception {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType javaType = new MapLikeType(_class, null, mapClass, null,
                String.class, Integer.class).negative();

        JavaType updatedJavaType = javaType.withKeyType(Long.TYPE);

        MapLikeType mapLikeType = (MapLikeType) updatedJavaType;
        assertEquals("Map<Long, Integer>", mapLikeType.getCanonicalName());
        assertEquals(Long.TYPE, mapLikeType.getKeyType());
        assertEquals(Integer.class, mapLikeType.getValueType());
    }

    @Test
    public void testWithValueType() throws Exception {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType javaType = new MapLikeType(_class, null, mapClass, null,
                String.class, Integer.class).negative();

        JavaType updatedJavaType = javaType.withContentType(Double.class);

        MapLikeType mapLikeType = (MapLikeType) updatedJavaType;
        assertEquals("Map<String, Double>", mapLikeType.getCanonicalName());
        assertEquals(String.class, mapLikeType.getKeyType());
        assertEquals(Double.class, mapLikeType.getValueType());
    }

    @Test
    public void testIsTrueMapType() throws Exception {
        JavaType javaType = new MapLikeType(HashMap.class, null, null, null,
                String.class, Integer.class).negative();
        assertTrue(javaType.isTrueMapType());

        JavaType javaType2 = TypeFactory.defaultInstance()
                .constructCollectionType(ArrayList.class, String.class);
        assertFalse(javaType2.isTrueMapType());
    }
}