package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Test class for MapType.
 */
public class MapTypeTest {

    @Test
    public void testCreateMapType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeFactory.defaultBindings();
        JavaType superClass = TypeFactory.unknownType();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType keyType = TypeFactory.constructType(String.class);
        JavaType valueType = TypeFactory.constructType(Integer.class);

        MapType mapType = MapType.construct(mapClass, bindings, superClass, superInterfaces, keyType, valueType);

        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(bindings, mapType.getTypeBindings());
        assertEquals(superClass, mapType.getSuperClass());
        assertArrayEquals(superInterfaces, mapType.getSuperInterfaces());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testCreateMapTypeWithKeyValueHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeFactory.defaultBindings();
        JavaType superClass = TypeFactory.unknownType();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType keyType = TypeFactory.constructType(String.class);
        JavaType valueType = TypeFactory.constructType(Integer.class);
        Object keyHandler = new Object() {};
        Object valueHandler = new Object() {};

        MapType mapType = MapType.construct(mapClass, bindings, superClass, superInterfaces, keyType, valueType,
                keyHandler, valueHandler, false);

        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(bindings, mapType.getTypeBindings());
        assertEquals(superClass, mapType.getSuperClass());
        assertArrayEquals(superInterfaces, mapType.getSuperInterfaces());
        assertFalse(mapType.isStatic());
        assertSame(keyHandler, mapType.getKeyHandler());
        assertSame(valueHandler, mapType.getValueHandler());
    }

    @Test
    public void testCreateMapTypeWithStaticTyping() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeFactory.defaultBindings();
        JavaType superClass = TypeFactory.unknownType();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType keyType = TypeFactory.constructType(String.class);
        JavaType valueType = TypeFactory.constructType(Integer.class);

        MapType mapType = MapType.construct(mapClass, bindings, superClass, superInterfaces, keyType, valueType);
        MapType staticMapType = mapType.withStaticTyping();

        assertEquals(mapType.getRawClass(), staticMapType.getRawClass());
        assertEquals(mapType.getKeyType(), staticMapType.getKeyType());
        assertEquals(mapType.getValueType(), staticMapType.getValueType());
        assertEquals(mapType.getTypeBindings(), staticMapType.getTypeBindings());
        assertEquals(mapType.getSuperClass(), staticMapType.getSuperClass());
        assertArrayEquals(mapType.getSuperInterfaces(), staticMapType.getSuperInterfaces());
        assertTrue(staticMapType.isStatic());
    }

    @Test
    public void testCreateMapTypeWithValueHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeFactory.defaultBindings();
        JavaType superClass = TypeFactory.unknownType();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType keyType = TypeFactory.constructType(String.class);
        JavaType valueType = TypeFactory.constructType(Integer.class);
        Object valueHandler = new Object() {};

        MapType mapType = MapType.construct(mapClass, bindings, superClass, superInterfaces, keyType, valueType);
        MapType valueHandledMapType = mapType.withValueHandler(valueHandler);

        assertEquals(Map.class, valueHandledMapType.getRawClass());
        assertEquals(keyType, valueHandledMapType.getKeyType());
        assertEquals(valueType, valueHandledMapType.getValueType());
        assertEquals(bindings, valueHandledMapType.getTypeBindings());
        assertEquals(superClass, valueHandledMapType.getSuperClass());
        assertArrayEquals(superInterfaces, valueHandledMapType.getSuperInterfaces());
        assertFalse(valueHandledMapType.isStatic());
        assertSame(valueHandler, valueHandledMapType.getValueHandler());
    }

    @Test
    public void testCreateMapTypeWithContentTypeHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeFactory.defaultBindings();
        JavaType superClass = TypeFactory.unknownType();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType keyType = TypeFactory.constructType(String.class);
        JavaType valueType = TypeFactory.constructType(Integer.class);
        Object typeHandler = new Object() {};

        MapType mapType = MapType.construct(mapClass, bindings, superClass, superInterfaces, keyType, valueType);
        MapType contentTypeHandledMapType = mapType.withContentTypeHandler(typeHandler);

        assertEquals(Map.class, contentTypeHandledMapType.getRawClass());
        assertEquals(keyType, contentTypeHandledMapType.getKeyType());
        assertEquals(valueType, contentTypeHandledMapType.getValueType());
        assertEquals(bindings, contentTypeHandledMapType.getTypeBindings());
        assertEquals(superClass, contentTypeHandledMapType.getSuperClass());
        assertArrayEquals(superInterfaces, contentTypeHandledMapType.getSuperInterfaces());
        assertFalse(contentTypeHandledMapType.isStatic());
        assertSame(typeHandler, contentTypeHandledMapType.getTypeHandler());
    }

    @Test
    public void testCreateMapTypeWithKeyTypeHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeFactory.defaultBindings();
        JavaType superClass = TypeFactory.unknownType();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType keyType = TypeFactory.constructType(String.class);
        JavaType valueType = TypeFactory.constructType(Integer.class);
        Object keyHandler = new Object() {};

        MapType mapType = MapType.construct(mapClass, bindings, superClass, superInterfaces, keyType, valueType);
        MapType keyHandledMapType = mapType.withKeyTypeHandler(keyHandler);

        assertEquals(Map.class, keyHandledMapType.getRawClass());
        assertEquals(keyType, keyHandledMapType.getKeyType());
        assertEquals(valueType, keyHandledMapType.getValueType());
        assertEquals(bindings, keyHandledMapType.getTypeBindings());
        assertEquals(superClass, keyHandledMapType.getSuperClass());
        assertArrayEquals(superInterfaces, keyHandledMapType.getSuperInterfaces());
        assertFalse(keyHandledMapType.isStatic());
        assertSame(keyHandler, keyHandledMapType.getKeyHandler());
    }

    @Test
    public void testCreateMapTypeWithContentValueHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeFactory.defaultBindings();
        JavaType superClass = TypeFactory.unknownType();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType keyType = TypeFactory.constructType(String.class);
        JavaType valueType = TypeFactory.constructType(Integer.class);
        Object valueHandler = new Object() {};

        MapType mapType = MapType.construct(mapClass, bindings, superClass, superInterfaces, keyType, valueType);
        MapType contentValueHandledMapType = mapType.withContentValueHandler(valueHandler);

        assertEquals(Map.class, contentValueHandledMapType.getRawClass());
        assertEquals(keyType, contentValueHandledMapType.getKeyType());
        assertEquals(valueType, contentValueHandledMapType.getValueType());
        assertEquals(bindings, contentValueHandledMapType.getTypeBindings());
        assertEquals(superClass, contentValueHandledMapType.getSuperClass());
        assertArrayEquals(superInterfaces, contentValueHandledMapType.getSuperInterfaces());
        assertFalse(contentValueHandledMapType.isStatic());
        assertSame(valueHandler, contentValueHandledMapType.getContentValueHandler());
    }

    @Test
    public void testCreateMapTypeWithValueType() {
        Class<Map<String, Serializable>> mapClass = Map.class;
        TypeBindings bindings = TypeFactory.defaultBindings();
        JavaType superClass = TypeFactory.unknownType();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType keyType = TypeFactory.constructType(String.class);
        JavaType valueType = TypeFactory.constructType(Serializable.class);

        MapType mapType = MapType.construct(mapClass, bindings, superClass, superInterfaces, keyType, valueType);

        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(bindings, mapType.getTypeBindings());
        assertEquals(superClass, mapType.getSuperClass());
        assertArrayEquals(superInterfaces, mapType.getSuperInterfaces());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testCreateMapTypeWithKeyType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeFactory.defaultBindings();
        JavaType superClass = TypeFactory.unknownType();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType keyType = TypeFactory.constructType(String.class);
        JavaType valueType = TypeFactory.constructType(Integer.class);

        MapType mapType = MapType.construct(mapClass, bindings, superClass, superInterfaces, keyType, valueType);

        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(bindings, mapType.getTypeBindings());
        assertEquals(superClass, mapType.getSuperClass());
        assertArrayEquals(superInterfaces, mapType.getSuperInterfaces());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testNarrowMapType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeFactory.defaultBindings();
        JavaType superClass = TypeFactory.unknownType();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType keyType = TypeFactory.constructType(String.class);
        JavaType valueType = TypeFactory.constructType(Integer.class);

        MapType parentMapType = MapType.construct(mapClass, bindings, superClass, superInterfaces, keyType, valueType);
        JavaType childMapType = parentMapType.narrow(Map.class);

        assertEquals(Map.class, childMapType.getRawClass());
        assertEquals(keyType, childMapType.getKeyType());
        assertEquals(valueType, childMapType.getValueType());
        assertEquals(bindings, childMapType.getTypeBindings());
        assertEquals(superClass, childMapType.getSuperClass());
        assertArrayEquals(superInterfaces, childMapType.getSuperInterfaces());
        assertFalse(childMapType.isStatic());
    }

    @Test
    public void testMapTypeToString() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeFactory.defaultBindings();
        JavaType superClass = TypeFactory.unknownType();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType keyType = TypeFactory.constructType(String.class);
        JavaType valueType = TypeFactory.constructType(Integer.class);

        MapType mapType = MapType.construct(mapClass, bindings, superClass, superInterfaces, keyType, valueType);

        String result = mapType.toString();

        assertEquals("[map type; class Map<java.lang.String, java.lang.Integer>, java.lang.String -> java.lang.Integer]", result);
    }
}