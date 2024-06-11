package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstruction() {
        JavaType mapType = new TypeReference<>(){}.getType();
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapLikeType mapLikeType = MapLikeType.upgradeFrom(mapType, keyType, valueType);

        assertEquals("java.util.Map", mapLikeType.getRawClass().getName());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        JavaType mapType = new TypeReference<>(){}.getType();
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapLikeType mapLikeType = new MapLikeType(mapType, keyType, valueType);

        assertEquals("java.util.Map", mapLikeType.getRawClass().getName());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
    }

    @Test
    public void testMapLikeTypeWithKeyTypeHandler() {
        JavaType mapType = new TypeReference<>(){}.getType();
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        ObjectMapper objectMapper = new ObjectMapper();

        MapLikeType mapLikeType = new MapLikeType(mapType, keyType, valueType);
        Object keyHandler = new Object() {};

        MapLikeType mapLikeTypeWithKeyHandler = mapLikeType.withKeyTypeHandler(keyHandler);

        assertEquals(mapLikeType, mapLikeTypeWithKeyHandler);
        assertTrue(mapLikeTypeWithKeyHandler.hasHandlers());
    }

    @Test
    public void testMapLikeTypeWithContentType() {
        JavaType mapType = new TypeReference<>(){}.getType();
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        JavaType contentType = Long.class;

        MapLikeType mapLikeType = new MapLikeType(mapType, keyType, valueType);

        MapLikeType mapLikeTypeWithContentType = mapLikeType.withContentType(contentType);

        assertEquals(mapLikeType, mapLikeTypeWithContentType);
        assertEquals(contentType, mapLikeTypeWithContentType.getContentType());
    }

    @Test
    public void testMapLikeTypeWithContentTypeHandler() {
        JavaType mapType = new TypeReference<>(){}.getType();
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        JavaType contentType = Long.class;
        ObjectMapper objectMapper = new ObjectMapper();

        MapLikeType mapLikeType = new MapLikeType(mapType, keyType, valueType);
        Object contentTypeHandler = new Object() {};

        MapLikeType mapLikeTypeWithContentTypeHandler = mapLikeType.withContentTypeHandler(contentTypeHandler);

        assertEquals(mapLikeType, mapLikeTypeWithContentTypeHandler);
        assertTrue(mapLikeTypeWithContentTypeHandler.hasHandlers());
    }

    @Test
    public void testMapLikeTypeIsContainerType() {
        JavaType mapType = new TypeReference<>(){}.getType();
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapLikeType mapLikeType = new MapLikeType(mapType, keyType, valueType);

        assertTrue(mapLikeType.isContainerType());
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeConstructInvalid() {
        JavaType mapType = new TypeReference<>(){}.getType();
        JavaType keyType = String.class;
        JavaType valueType = String.class;

        MapLikeType mapLikeType = MapLikeType.construct(mapType.getRawClass(), keyType, valueType);
    }

    @Test
    public void testMapLikeTypeToString() {
        JavaType mapType = new TypeReference<>(){}.getType();
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapLikeType mapLikeType = new MapLikeType(mapType, keyType, valueType);

        String mapLikeTypeString = mapLikeType.toString();
        String expected = String.format("[map-like type; class %s, %s -> %s]",
                mapType.getRawClass().getName(), keyType, valueType);

        assertEquals(expected, mapLikeTypeString);
    }
}