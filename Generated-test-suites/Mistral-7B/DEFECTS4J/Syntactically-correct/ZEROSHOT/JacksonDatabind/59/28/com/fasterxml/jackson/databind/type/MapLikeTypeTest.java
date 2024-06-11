package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstruction() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);
        assertEquals("java.util.Map", mapType.getCanonicalName());
        assertEquals(JavaType.MapOf(String.class, Integer.class), mapType);
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testMapLikeTypeUpgrade() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(LinkedHashMap.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", mapLikeType.getCanonicalName());
        assertEquals(JavaType.MapOf(String.class, Integer.class), mapLikeType);
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(baseType, keyType, valueType);
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", mapLikeType.getCanonicalName());
        assertEquals(JavaType.MapOf(String.class, Integer.class), mapLikeType);
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());

        JavaType newKeyType = TypeFactory.defaultInstance().constructType(Long.class);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);
        assertEquals("java.util.Map<java.lang.Long, java.lang.Integer>", newMapLikeType.getCanonicalName());
        assertEquals(JavaType.MapOf(Long.class, Integer.class), newMapLikeType);
        assertEquals(newKeyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithContentType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(baseType, keyType, valueType);
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", mapLikeType.getCanonicalName());
        assertEquals(JavaType.MapOf(String.class, Integer.class), mapLikeType);
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());

        JavaType newValueType = TypeFactory.defaultInstance().constructType(Double.class);
        MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);
        assertEquals("java.util.Map<java.lang.String, java.lang.Double>", newMapLikeType.getCanonicalName());
        assertEquals(JavaType.MapOf(String.class, Double.class), newMapLikeType);
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(newValueType, newMapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithTypeHandler() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(baseType, keyType, valueType);
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", mapLikeType.getCanonicalName());
        assertEquals(JavaType.MapOf(String.class, Integer.class), mapLikeType);
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());

        Object handler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(handler);
        assertNotNull(newMapLikeType.getTypeHandler());
        assertEquals(handler, newMapLikeType.getTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithKeyTypeHandler() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(baseType, keyType, valueType);
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", mapLikeType.getCanonicalName());
        assertEquals(JavaType.MapOf(String.class, Integer.class), mapLikeType);
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());

        Object handler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withKeyTypeHandler(handler);
        assertNotNull(newMapLikeType.getKeyTypeHandler());
        assertEquals(handler, newMapLikeType.getKeyTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithContentValueHandler() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(baseType, keyType, valueType);
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", mapLikeName(mapLikeType));
        assertEquals(JavaType.MapOf(String.class, Integer.class), mapLikeType);
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());

        Object handler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(handler);
        assertNotNull(newMapLikeType.getValueHandler());
        assertEquals(handler, newMapLikeType.getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithStaticTyping() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(baseType, keyType, valueType);
        assertEquals(false, mapLikeType.isStaticTyping());

        MapLikeType newMapLikeType = mapLikeType.withStaticTyping();
        assertEquals(true, newMapLikeType.isStaticTyping());
    }

    @Test
    public void testMapLikeTypeEquals() {
        JavaType baseType1 = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType keyType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType1 = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType baseType2 = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType keyType2 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType2 = TypeFactory.defaultInstance().constructType(Integer.class);

        MapLikeType mapLikeType1 = new MapLikeType(baseType1, keyType1, valueType1);
        MapLikeType mapLikeType2 = new MapLikeType(baseType2, keyType2, valueType2);

        assertFalse(mapLikeType1.equals(null));
        assertFalse(mapLikeType1.equals(new Object()));
        assertFalse(mapLikeType1.equals(mapLikeType2));

        JavaType sameType = new MapLikeType(baseType1, keyType1, valueType1);
        assertTrue(mapLikeType1.equals(sameType));
    }

    @Test
    public void testMapLikeTypeHashCode() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

        MapLikeType mapLikeType1 = new MapLikeType(baseType, keyType, valueType);
        MapLikeType mapLikeType2 = new MapLikeType(baseType, keyType, valueType);

        int hashCode1 = mapLikeType1.hashCode();
        int hashCode2 = mapLikeType2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testMapLikeTypeToString() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

        MapLikeType mapLikeType = new MapLikeType(baseType, keyType, valueType);

        String expected = String.format("[map-like type; class %s, %s -> %s]",
                baseType.getName(), keyType, valueType);
        assertEquals(expected, mapLikeType.toString());
    }

    private static JavaType javaTypeOfMap(Class<?> rawType) {
        if (Map.class.isAssignableFrom(rawType)) {
            return JavaType.MapOf(getTypeVariable(rawType, 0), getTypeVariable(rawType, 1));
        }
        throw new IllegalArgumentException("Expected Map type");
    }

    private static TypeVariable<?> getTypeVariable(Class<?> clazz, int index) {
        TypeVariable<?>[] typeVariables = clazz.getTypeParameters();
        if (typeVariables != null && index >= 0 && index < typeVariables.length) {
            return typeVariables[index];
        }
        throw new IllegalArgumentException("Type " + clazz.getName()
                + " does not have " + index + " type variables");
    }

    private static String mapLikeName(JavaType javaType) {
        if (javaType instanceof MapLikeType) {
            MapLikeType mapLikeType = (MapLikeType) javaType;
            return mapLikeType.getCanonicalName();
        }
        throw new IllegalArgumentException("Expected MapLikeType");
    }
}