package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.MapLikeType;

/**
 * JUnit test file for MapType class.
 */
public class MapTypeTest {

    @Test
    public void testMapTypeConstruction() {
        Class<Map<String, String>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = BasicTypeFactory.instance.constructType(String.class);
        JavaType valueType = BasicTypeFactory.instance.constructType(String.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);
        MapType expectedMapType = new MapType(mapType, bindings, superClass, superInts, keyType, valueType);
        assertEquals(expectedMapType.toString(), mapTypeInstance.toString());
    }

    @Test
    public void testMapTypeWithTypeHandler() {
        Class<Map<String, String>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = BasicTypeFactory.instance.constructType(String.class);
        JavaType valueType = BasicTypeFactory.instance.constructType(String.class);

        MapType mapTypeInstance = mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType);
        Object valueHandler = new Object();
        MapType mapTypeWithTypeHandler = mapTypeInstance.withTypeHandler(valueHandler);

        JavaType expectedMapTypeWithTypeHandler = new MapType(mapType, bindings, superClass, superInts, keyType, valueType, valueHandler, null, false);
        assertEquals(expectedMapTypeWithTypeHandler.toString(), mapTypeWithTypeHandler.toString());
    }

    @Test
    public void testMapTypeWithContentTypeHandler() {
        Class<Map<String, String>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = BasicTypeFactory.instance.constructType(String.class);
        JavaType valueType = BasicTypeFactory.instance.constructType(String.class);

        MapType mapTypeInstance = mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType);
        Object contentTypeHandler = new Object();
        JavaType contentType = BasicTypeFactory.instance.constructType(Integer.class);
        MapType mapTypeWithContentTypeHandler = mapTypeInstance.withContentTypeHandler(contentTypeHandler);
        JavaType expectedMapTypeWithContentTypeHandler = new MapType(mapType, bindings, superClass, superInts, keyType, contentType, keyType, contentTypeHandler, false);

        assertEquals(expectedMapTypeWithContentTypeHandler.toString(), mapTypeWithContentTypeHandler.toString());
    }

    @Test
    public void testMapTypeWithValueHandler() {
        Class<Map<String, String>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = BasicTypeFactory.instance.constructType(String.class);
        JavaType valueType = BasicTypeFactory.instance.constructType(String.class);

        MapType mapTypeInstance = mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType);
        Object valueHandler = new Object();
        MapType mapTypeWithValueHandler = mapTypeInstance.withValueHandler(valueHandler);

        JavaType expectedMapTypeWithValueHandler = new MapType(mapType, bindings, superClass, superInts, keyType, valueType, valueHandler, null, false);
        assertEquals(expectedMapTypeWithValueHandler.toString(), mapTypeWithValueHandler.toString());
    }

    @Test
    public void testMapTypeWithContentValueHandler() {
        Class<Map<String, String>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = BasicTypeFactory.instance.constructType(String.class);
        JavaType valueType = BasicTypeFactory.instance.constructType(String.class);

        MapType mapTypeInstance = mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType);
        Object contentValueHandler = new Object();
        JavaType contentValueType = BasicTypeFactory.instance.constructType(Integer.class);
        MapType mapTypeWithContentValueHandler = mapTypeInstance.withContentValueHandler(contentValueHandler);
        JavaType expectedMapTypeWithContentValueHandler = new MapType(mapType, bindings, superClass, superInts, keyType, valueType.withValueHandler(contentValueHandler), valueHandler, null, false);

        assertEquals(expectedMapTypeWithContentValueHandler.toString(), mapTypeWithContentValueHandler.toString());
    }

    @Test
    public void testMapTypeWithStaticTyping() {
        Class<Map<String, String>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = BasicTypeFactory.instance.constructType(String.class);
        JavaType valueType = BasicTypeFactory.instance.constructType(String.class);

        MapType mapTypeInstance = mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType);
        MapType mapTypeWithStaticTyping = mapTypeInstance.withStaticTyping();

        JavaType expectedMapTypeWithStaticTyping = new MapType(mapType, bindings, superClass, superInts, keyType.withStaticTyping(), valueType.withStaticTyping(), null, null, true);
        assertEquals(expectedMapTypeWithStaticTyping.toString(), mapTypeWithStaticTyping.toString());
    }

    @Test
    public void testMapTypeWithContentType() {
        Class<Map<String, String>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = BasicTypeFactory.instance.constructType(String.class);
        JavaType valueType = BasicTypeFactory.instance.constructType(String.class);

        MapType mapTypeInstance = mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType);
        JavaType contentType = BasicTypeFactory.instance.constructType(Integer.class);
        MapType mapTypeWithContentType = mapTypeInstance.withContentType(contentType);

        JavaType expectedMapTypeWithContentType = new MapType(mapType, bindings, superClass, superInts, keyType, contentType, null, null, false);
        assertEquals(expectedMapTypeWithContentType.toString(), mapTypeWithContentType.toString());
    }

    @Test
    public void testMapTypeWithKeyType() {
        Class<Map<String, String>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = BasicTypeFactory.instance.constructType(String.class);
        JavaType valueType = BasicTypeFactory.instance.constructType(String.class);

        MapType mapTypeInstance = mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType);
        JavaType newKeyType = BasicTypeFactory.instance.constructType(Integer.class);
        MapType mapTypeWithNewKeyType = mapTypeInstance.withKeyType(newKeyType);

        JavaType expectedMapTypeWithNewKeyType = new MapType(mapType, bindings, superClass, superInts, newKeyType, valueType, null, null, false);
        assertEquals(expectedMapTypeWithNewKeyType.toString(), mapTypeWithNewKeyType.toString());
    }

    @Test
    public void testMapTypeRefine() {
        Class<Map<String, String>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = BasicTypeFactory.instance.constructType(String.class);
        JavaType valueType = BasicTypeFactory.instance.constructType(String.class);

        MapType mapTypeInstance = mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType);
        Class<Map<Integer, Integer>> newMapType = Map.class;
        TypeBindings newBindings = TypeBindings.defaultBindings();
        JavaType newSuperClass = null;
        JavaType[] newSuperInts = new JavaType[0];

        MapType refinedMapType = mapTypeInstance.refine(newMapType.getClass(), newBindings, newSuperClass, newSuperInts);

        JavaType expectedRefinedMapType = new MapType(newMapType, newBindings, null, newJavaType[0], keyType, valueType, null, null, false);
        assertEquals(expectedRefinedMapType.toString(), refinedMapType.toString());
    }

    @Test
    public void testMapTypeWithKeyTypeHandler() {
        Class<Map<String, String>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = BasicTypeFactory.instance.constructType(String.class);
        JavaType valueType = BasicTypeFactory.instance.constructType(String.class);

        MapType mapTypeInstance = mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType);
        Object keyTypeHandler = new Object();
        MapType mapTypeWithKeyTypeHandler = mapTypeInstance.withKeyTypeHandler(keyTypeHandler);

        JavaType expectedMapTypeWithKeyTypeHandler = new MapType(mapType, bindings, superClass, superInts, keyType.withTypeHandler(keyTypeHandler), valueType,
                null, null, false);
        assertEquals(expectedMapTypeWithKeyTypeHandler.toString(), mapTypeWithKeyTypeHandler.toString());
    }

    @Test
    public void testMapTypeWithKeyValueHandler() {
        Class<Map<String, String>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = BasicTypeFactory.instance.constructType(String.class);
        JavaType valueType = BasicTypeFactory.instance.constructType(String.class);

        MapType mapTypeInstance = mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType);
        Object keyValueHandler = new Object();
        MapType mapTypeWithKeyValueHandler = mapTypeInstance.withKeyValueHandler(keyValueHandler);

        JavaType expectedMapTypeWithKeyValueHandler = new MapType(mapType, bindings, superClass, superInts, keyType.withValueHandler(keyValueHandler), valueType,
                null, null, false);
        assertEquals(expectedMapTypeWithKeyValueHandler.toString(), mapTypeWithKeyValueHandler.toString());
    }
}