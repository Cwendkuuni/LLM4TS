package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstruction() {
        JavaType mapType = Map.class;
        TypeVariable<?>[] vars = mapType.getTypeParameters();
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapLikeType mapLikeType = MapLikeType.construct(mapType, keyType, valueType);
        assertNotNull(mapLikeType);
        assertEquals(mapType, mapLikeType.getRawType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeUpgrade() {
        JavaType baseType = List.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
        assertNotNull(mapLikeType);
        assertEquals(MapLikeType.class, mapLikeType.getClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeBasicFunctionality() {
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        JavaType mapType = new TypeFactory().constructMapType(int[].class, String.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, keyType, valueType);

        assertNotNull(mapLikeType);
        assertEquals(mapType, mapLikeType.getRawType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeInvalidUpgradeFrom() {
        JavaType baseType = Integer.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapLikeType.upgradeFrom(baseType, keyType, valueType);
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        JavaType baseType = Map.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapLikeType mapLikeType = new MapLikeType(baseType, keyType, valueType);
        JavaType newKeyType = Long.class;
        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);

        assertEquals(newKeyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getValueType());
        assertEquals(baseType, newMapLikeType.getRawType());
    }

    @Test
    public void testMapLikeTypeWithContentType() {
        JavaType keyType = String.class;
        JavaType baseType = Map.class;
        JavaType valueType = Integer.class;
        JavaType newValueType = Double.class;

        MapLikeType mapLikeType = new MapLikeType(baseType, keyType, valueType);
        MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);

        assertEquals(newValueType, newMapLikeType.getValueType());
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(baseType, newMapLikeType.getRawType());
    }

    @Test
    public void testMapLikeTypeWithTypeHandler() {
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        MapLikeType mapLikeType = new MapLikeType(Map.class, keyType, valueType);
        Object handler = new Object() {
            @SuppressWarnings("unchecked")
            public void handle(String key, Integer value) {
            }
        };

        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(handler);
        Object newHandler = newMapLikeType.getTypeHandler();
        assertEquals(handler, newHandler);
    }

    @Test
    public void testMapLikeTypeWithContentTypeHandler() {
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        MapLikeType mapLikeType = new MapLikeType(Map.class, keyType, valueType);
        Object handler = new Object() {
            @SuppressWarnings("unchecked")
            public void handle(Integer value) {
            }
        };

        MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(handler);
        Object newHandler = newMapLikeType.getContentTypeHandler();
        assertEquals(handler, newHandler);
    }

    @Test
    public void testMapLikeTypeWithValueHandler() {
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        MapLikeType mapLikeType = new MapLikeType(Map.class, keyType, valueType);
        Object handler = new Object() {
            @SuppressWarnings("unchecked")
            public void handle(Map.Entry<String, Integer> entry) {
            }
        };

        MapLikeType newMapLikeType = mapLikeType.withValueHandler(handler);
        Object newHandler = newMapLikeType.getValueHandler();
        assertEquals(handler, newHandler);
    }

    @Test
    public void testMapLikeTypeWithContentValueHandler() {
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        MapLikeType mapLikeType = new MapLikeType(Map.class, keyType, valueType);
        Object handler = new Object() {
            @SuppressWarnings("unchecked")
            public void handle(Integer value) {
            }
        };

        MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(handler);
        Object newHandler = newMapLikeType.getContentValueHandler();
        assertEquals(handler, newHandler);
    }

    @Test
    public void testMapLikeTypeWithStaticTyping() {
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        MapLikeType mapLikeType = new MapLikeType(Map.class, keyType, valueType);

        MapLikeType staticMapLikeType = mapLikeType.withStaticTyping();
        assertEquals(true, staticMapLikeType.isStatic());
    }
}