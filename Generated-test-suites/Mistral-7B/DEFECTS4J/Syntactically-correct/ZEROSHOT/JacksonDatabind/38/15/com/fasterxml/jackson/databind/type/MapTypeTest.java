package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testMapTypeConstruction() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapTypeInstance = MapType.construct(mapType, bindings, null, null, keyType, valueType);
        assertEquals("map type; class Map<java.lang.String, java.lang.Integer>, java.lang.String -> java.lang.Integer", mapTypeInstance.toString());
        assertEquals(Map.class, mapTypeInstance._getClass());
        assertEquals(keyType, mapTypeInstance._getKeyType());
        assertEquals(valueType, mapTypeInstance._getValueType());
        assertNull(mapTypeInstance._getValueHandler());
        assertNull(mapTypeInstance._TypeHandler());
        assertFalse(mapTypeInstance._asStatic());
    }

    @Test
    public void testMapTypeWithTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object valueHandler = new Object() {};
        MapType mapTypeInstance = new MapType(mapType, bindings, null, null, keyType, valueType, valueHandler, null, false);
        assertEquals("map type; class Map<java.lang.String, java.lang.Integer>, java.lang.String -> java.lang.Integer", mapTypeInstance.toString());
        assertEquals(Map.class, mapTypeInstance._getClass());
        assertEquals(keyType, mapTypeInstance._getKeyType());
        assertEquals(valueType, mapTypeInstance._getValueType());
        assertEquals(valueHandler, mapTypeInstance._getValueHandler());
        assertNull(mapTypeInstance._TypeHandler());
        assertFalse(mapTypeInstance._asStatic());
    }

    @Test
    public void testMapTypeWithKeyTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object keyHandler = new Object() {};
        MapType mapTypeInstance = new MapType(mapType, bindings, null, null, keyType.withTypeHandler(keyHandler), valueType, null, null, false);
        assertEquals("map type; class Map<java.lang.String, java.lang.Integer>", mapTypeInstance.toString());
        assertEquals(Map.class, mapTypeInstance._getClass());
        assertEquals(keyType.withTypeHandler(keyHandler), mapTypeInstance._getKeyType());
        assertEquals(valueType, mapTypeInstance._getValueType());
        assertNull(mapTypeInstance._getValueHandler());
        assertNull(mapTypeInstance._TypeHandler());
        assertFalse(mapTypeInstance._asStatic());
    }

    @Test
    public void testMapTypeWithKeyValueHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object keyHandler = new Object() {};
        Object valueHandler = new Object() {};
        MapType mapTypeInstance = new MapType(mapType, bindings, null, null, keyType, valueType, null, null, false);
        MapType mapTypeInstanceWithKeyValueHandler = new MapType(mapType, bindings, null, null, keyType.withValueHandler(keyHandler), valueType.withValueHandler(valueHandler), null, null, false);
        assertEquals(mapTypeInstance, mapTypeInstanceWithKeyValueHandler);
        assertEquals(mapTypeInstance._getClass(), mapTypeInstanceWithKeyValueHandler._getClass());
        assertEquals(mapTypeInstance._getKeyType(), mapTypeInstanceWithKeyValueHandler._getKeyType());
        assertEquals(mapTypeInstance._getValueType(), mapTypeInstanceWithKeyValueHandler._getValueType());
        assertEquals(keyHandler, mapTypeInstanceWithKeyValueHandler._getValueHandler());
        assertEquals(valueHandler, mapTypeInstanceWithKeyValueHandler._getValueHandler());
        assertFalse(mapTypeInstanceWithKeyValueHandler._asStatic());
    }

    @Test
    public void testMapTypeStaticTyping() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class).withStaticTyping();
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class).withStaticTyping();
        MapType mapTypeInstance = new MapType(mapType, bindings, null, null, keyType, valueType, null, null, false);
        MapType mapTypeInstanceStatic = mapTypeInstance.withStaticTyping();
        assertEquals(mapTypeInstance, mapTypeInstanceStatic);
        assertTrue(mapTypeInstanceStatic._asStatic());
        assertEquals(mapTypeInstance._getClass(), mapTypeInstanceStatic._getClass());
        assertEquals(mapTypeInstance._getKeyType(), mapTypeInstanceStatic._getKeyType());
        assertEquals(mapTypeInstance._getValueType(), mapTypeInstanceStatic._getValueType());
        assertNull(mapTypeInstanceStatic._getValueHandler());
        assertNull(mapTypeInstanceStatic._TypeHandler());
    }

    @Test
    public void testMapTypeWithContentTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object valueHandler = new Object() {};
        JavaType contentTypeHandler = TypeFactory.defaultInstance().constructType(String[].class);
        MapType mapTypeInstance = new MapType(mapType, bindings, null, null, keyType, valueType, null, null, false);
        MapType mapTypeInstanceWithContentTypeHandler = mapTypeInstance.withContentTypeHandler(contentTypeHandler);
        assertEquals(mapTypeInstance, mapTypeInstanceWithContentTypeHandler);
        assertEquals(mapTypeInstance._getClass(), mapTypeInstanceWithContentTypeHandler._getClass());
        assertEquals(mapTypeInstance._getKeyType(), mapTypeInstanceWithContentTypeHandler._getKeyType());
        assertEquals(valueType.withTypeHandler(contentTypeHandler), mapTypeInstanceWithContentTypeHandler._getValueType());
        assertNull(mapTypeInstanceWithContentTypeHandler._getValueHandler());
        assertNull(mapTypeInstanceWithContentTypeHandler._TypeHandler());
        assertFalse(mapTypeInstanceWithContentTypeHandler._asStatic());
    }

    @Test
    public void testMapTypeWithValueHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object valueHandler = new Object() {};
        MapType mapTypeInstance = new MapType(mapType, bindings, null, null, keyType, valueType, null, null, false);
        MapType mapTypeInstanceWithValueHandler = new MapType(mapType, bindings, null, null, keyType, valueType, valueHandler, null, false);
        assertEquals(mapTypeInstance, mapTypeInstanceWithValueHandler);
        assertEquals(mapTypeInstance._getClass(), mapTypeInstanceWithValueHandler._getClass());
        assertEquals(mapTypeInstance._getKeyType(), mapTypeInstanceWithValueHandler._getKeyType());
        assertEquals(mapTypeInstance._getValueType(), mapTypeInstanceWithValueHandler._getValueType());
        assertEquals(valueHandler, mapTypeInstanceWithValueHandler._getValueHandler());
        assertNull(mapTypeInstanceWithValueHandler._TypeHandler());
        assertFalse(mapTypeInstanceWithValueHandler._asStatic());
    }

    @Test
    public void testMapTypeWithContentValueHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object contentValueHandler = new Object() {};
        MapType mapTypeInstance = new MapType(mapType, bindings, null, null, keyType, valueType, null, null, false);
        MapType mapTypeInstanceWithContentValueHandler = mapTypeInstance.withContentValueHandler(contentValueHandler);
        assertEquals(mapTypeInstance, mapTypeInstanceWithContentValueHandler);
        assertEquals(mapTypeInstance._getClass(), mapTypeInstanceWithContentValueHandler._getClass());
        assertEquals(mapTypeInstance._getKeyType(), mapTypeInstanceWithContentValueHandler._getKeyType());
        assertEquals(valueType.withValueHandler(contentValueHandler), mapTypeInstanceWithContentValueHandler._getValueType());
        assertNull(mapTypeInstanceWithContentValueHandler._getValueHandler());
        assertNull(mapTypeInstanceWithContentValueHandler._TypeHandler());
        assertFalse(mapTypeInstanceWithContentValueHandler._asStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeWithInvalidValues() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};
        MapType mapTypeInstance = new MapType(mapType, bindings, null, null, keyType, valueType, valueHandler, typeHandler, false);
        MapType mapTypeInstanceWithInvalidValueHandler = new MapType(mapType, bindings, null, null, keyType, valueType, valueHandler, null, true);
        MapType mapTypeInstanceWithInvalidTypeHandler = new MapType(mapType, bindings, null, null, keyType, valueType, null, typeHandler, false);
    }
}