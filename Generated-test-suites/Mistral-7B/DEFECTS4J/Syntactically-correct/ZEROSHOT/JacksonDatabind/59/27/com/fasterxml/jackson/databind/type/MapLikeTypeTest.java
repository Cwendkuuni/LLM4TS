package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstruction() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, String.class, Integer.class);
        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(String.class, mapLikeType.getKeyType().getRawClass());
        assertEquals(Integer.class, mapLikeType.getValueType().getRawClass());
    }

    @Test
    public void testMapLikeTypeUpgrade() {
        JavaType baseType = new TypeBase() {
            private static final long serialVersionUID = 1L;

            @Override
            public Class<?> getRawClass() {
                return HashMap.class;
            }

            @Override
            public boolean isContainerType() {
                return true;
            }
        }.noSuper();

        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType,
                valueType);

        assertEquals(HashMap.class, mapLikeType.getRawClass());
        assertEquals(String.class, mapLikeType.getKeyType().getRawClass());
        assertEquals(Integer.class, mapLikeType.getValueType().getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeUpgradeInvalidBaseType() {
        JavaType baseType = Boolean.TYPE;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapLikeType.upgradeFrom(baseType, keyType, valueType);
        fail("Expected an IllegalArgumentException");
    }

    @Test
    public void testMapLikeTypeFactory() {
        JavaType rawType = Map.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        TypeBindings bindings = TypeBindings.create(rawType, keyType, valueType);
        MapLikeType mapLikeType = new MapLikeType(rawType, bindings, null, null,
                keyType, valueType);

        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(String.class, mapLikeType.getKeyType().getRawClass());
        assertEquals(Integer.class, mapLikeType.getValueType().getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeFactoryWithInvalidBindings() {
        JavaType rawType = Map.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        TypeBindings bindings = TypeBindings.create(rawType, null, valueType);
        try {
            new MapLikeType(rawType, bindings, null, null, keyType, valueType);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        JavaType rawType = Map.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapLikeType mapLikeType = new MapLikeType(rawType, null, null, null,
                keyType, valueType);
        JavaType newKeyType = Character.TYPE;

        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);

        assertEquals(Map.class, newMapLikeType.getRawClass());
        assertEquals(Character.TYPE, newMapLikeType.getKeyType().getRawClass());
        assertEquals(Integer.class, newMapLikeType.getValueType().getRawClass());
    }

    @Test
    public void testMapLikeTypeWithValueType() {
        JavaType rawType = Map.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapLikeType mapLikeType = new MapLikeType(rawType, null, null, null,
                keyType, valueType);
        JavaType newValueType = Long.TYPE;

        MapLikeType newMapLikeType = mapLikeType.withValueType(newValueType);

        assertEquals(Map.class, newMapLikeType.getRawClass());
        assertEquals(String.class, newMapLikeType.getKeyType().getRawClass());
        assertEquals(Long.TYPE, newMapLikeType.getValueType().getRawClass());
    }

    @Test
    public void testMapLikeTypeWithTypeHandler() {
        JavaType rawType = Map.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapLikeType mapLikeType = new MapLikeType(rawType, null, null, null,
                keyType, valueType);
        Object handler = new Object() {};

        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(handler);

        assertEquals(Map.class, newMapLikeType.getRawClass());
        assertEquals(String.class, newMapLikeType.getKeyType().getRawClass());
        assertEquals(Integer.class, newMapLikeType.getValueType().getRawClass());
        assertEquals(handler, newMapLikeType.getTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithContentTypeHandler() {
        JavaType rawType = Map.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapLikeType mapLikeType = new MapLikeType(rawType, null, null, null,
                keyType, valueType);
        Object handler = new Object() {};

        JavaType newValueType = Long.TYPE;
        MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(handler);

        assertEquals(Map.class, newMapLikeType.getRawClass());
        assertEquals(String.class, newMapLikeType.getKeyType().getRawClass());
        assertEquals(Long.TYPE, newMapLikeType.getValueType().getRawClass());
        assertEquals(handler, newMapLikeType.getTypeHandler());
        assertEquals(handler, newMapLikeType.getValueType().getTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithValueHandler() {
        JavaType rawType = Map.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapLikeType mapLikeType = new MapLikeType(rawType, null, null, null,
                keyType, valueType);
        Object handler = new Object() {};

        MapLikeType newMapLikeType = mapLikeType.withValueHandler(handler);

        assertEquals(Map.class, newMapLikeType.getRawClass());
        assertEquals(String.class, newMapLikeType.getKeyType().getRawClass());
        assertEquals(Integer.class, newMapLikeType.getValueType().getRawClass());
        assertEquals(handler, newMapLikeType.getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithContentValueHandler() {
        JavaType rawType = Map.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapLikeType mapLikeType = new MapLikeType(rawType, null, null, null,
                keyType, valueType);
        Object handler = new Object() {};

        JavaType newValueType = Long.TYPE;
        MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(handler);

        assertEquals(Map.class, newMapLikeType.getRawClass());
        assertEquals(String.class, newMapLikeType.getKeyType().getRawClass());
        assertEquals(Long.TYPE, newMapLikeType.getValueType().getRawClass());
        assertEquals(handler, newMapLikeType.getValueHandler());
        assertEquals(handler, newMapLikeType.getValueType().getValueHandler());
    }

    @Test
    public void testMapLikeTypeIsContainerType() {
        JavaType rawType = Map.class;
        MapLikeType mapLikeType = new MapLikeType(rawType, null, null, null,
                String.class, Integer.class);

        assertTrue(mapLikeType.isContainerType());
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testMapLikeTypeIsTrueMapType() {
        JavaType rawType = Map.class;
        MapLikeType mapLikeType = new MapLikeType(rawType, null, null, null,
                String.class, Integer.class);

        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeEquals() {
        JavaType rawType = Map.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapLikeType mapLikeType1 = new MapLikeType(rawType, null, null, null,
                keyType, valueType);
        MapLikeType mapLikeType2 = new MapLikeType(rawType, null, null, null,
                keyType, valueType);

        assertTrue(mapLikeType1.equals(mapLikeType2));
    }

    @Test
    public void testMapLikeTypeNotEqualsDifferentClasses() {
        JavaType rawType1 = Map.class;
        JavaType rawType2 = ArrayList.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapLikeType mapLikeType1 = new MapLikeType(rawType1, null, null, null,
                keyType, valueType);
        MapLikeType mapLikeType2 = new MapLikeType(rawType2, null, null, null,
                keyType, valueType);

        assertFalse(mapLikeType1.equals(mapLikeType2));
    }

    @Test
    public void testMapLikeTypeNotEqualsDifferentKeyTypes() {
        JavaType rawType = Map.class;
        JavaType keyType1 = String.class;
        JavaType valueType = Integer.class;

        MapLikeType mapLikeType1 = new MapLikeType(rawType, null, null, null,
                keyType1, valueType);
        JavaType keyType2 = Character.TYPE;
        MapLikeType mapLikeType2 = new MapLikeType(rawType, null, null, null,
                keyType2, valueType);

        assertFalse(mapLikeType1.equals(mapLikeType2));
    }

    @Test
    public void testMapLikeTypeNotEqualsDifferentValueTypes() {
        JavaType rawType = Map.class;
        JavaType keyType = String.class;
        JavaType valueType1 = Integer.class;
        JavaType valueType2 = Long.TYPE;

        MapLikeType mapLikeType1 = new MapLikeType(rawType, null, null, null,
                keyType, valueType1);
        MapLikeType mapLikeType2 = new MapLikeType(rawType, null, null, null,
                keyType, valueType2);

        assertFalse(mapLikeType1.equals(mapLikeType2));
    }

    @Test
    public void testMapLikeTypeToString() {
        JavaType rawType = Map.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapLikeType mapLikeType = new MapLikeType(rawType, null, null, null,
                keyType, valueType);

        String result = mapLikeType.toString();

        String expected = "[map-like type; class " + Map.class.getName()
                + ", " + keyType + " -> " + valueType + "]";

        assertEquals(expected, result);
    }
}