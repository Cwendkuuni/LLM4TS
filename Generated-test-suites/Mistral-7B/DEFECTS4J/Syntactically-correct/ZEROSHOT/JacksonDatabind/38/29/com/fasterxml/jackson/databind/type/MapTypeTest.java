package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;

public class MapTypeTest {

    @Test
    public void testMapTypeConstruction() {
        Class<Map<Integer, String>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(String.class);

        MapType mapTypeInstance1 = MapType.construct(mapType, bindings, null, null, keyType, valueType);
        assertNotNull(mapTypeInstance1);

        MapType mapTypeInstance2 = MapType.construct(mapType, null, null, null, keyType, valueType);
        assertNotNull(mapTypeInstance2);

        MapType mapTypeInstance3 = MapType.construct(mapType, bindings, null, null, keyType, valueType, null, null, false);
        assertNotNull(mapTypeInstance3);

        MapType mapTypeInstance4 = MapType.construct(mapType, null, null, null, keyType, valueType, null, null, false);
        assertNotNull(mapTypeInstance4);
    }

    @Test
    public void testMapTypeWithTypeHandler() {
        Class<Map<Integer, String>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(String.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, null, null, keyType, valueType);
        Object handler = new Object();

        MapType mapTypeWithHandler = mapTypeInstance.withTypeHandler(handler);
        assertNotNull(mapTypeWithHandler);
        assertNull(mapTypeWithHandler._typeHandler);
        assertNotNull(mapTypeWithHandler._valueType.getTypeHandler());
        assertSame(handler, mapTypeWithHandler._valueType.getTypeHandler().getObject());

        JavaType valueTypeWithHandler = mapTypeWithHandler.getValueType();
        assertNotNull(valueTypeWithHandler);
        assertSame(valueTypeWithHandler, mapTypeWithHandler._valueType);
    }

    @Test
    public void testMapTypeWithContentTypeHandler() {
        Class<Map<Integer, String>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(String.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, null, null, keyType, valueType);
        Object handler = new Object();

        JavaType contentType = TypeFactory.defaultInstance().constructType(Integer[].class);
        MapType mapTypeWithHandler = mapTypeInstance.withContentTypeHandler(handler);

        JavaType valueTypeWithHandler = mapTypeWithHandler.getValueType();
        assertNotNull(valueTypeWithHandler);
        assertSame(valueTypeWithHandler, mapTypeWithHandler._valueType);
        assertNull(valueTypeWithHandler.getTypeHandler());
        assertNotNull(valueTypeWithHandler.getContentTypeHandler());
        assertSame(handler, valueTypeWithHandler.getContentTypeHandler().getObject());

        JavaType valueTypeWithContentType = valueTypeWithHandler.getContentType();
        assertNotNull(valueTypeWithContentType);
        assertSame(contentType, valueTypeWithContentType);
    }

    @Test
    public void testMapTypeWithValueHandler() {
        Class<Map<Integer, String>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(String.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, null, null, keyType, valueType);
        Object handler = new Object();

        MapType mapTypeWithHandler = mapTypeInstance.withValueHandler(handler);
        assertNotNull(mapTypeWithHandler);
        assertSame(mapTypeWithHandler._valueHandler, handler);
        assertNull(mapTypeWithHandler._valueType.getValueHandler());
    }

    @Test
    public void testMapTypeWithContentValueHandler() {
        Class<Map<Integer, String>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(String.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, null, null, keyType, valueType);
        Object handler = new Object();

        JavaType contentValueType = TypeFactory.defaultInstance().constructType(String[].class);
        MapType mapTypeWithHandler = mapTypeInstance.withContentValueHandler(handler);

        JavaType valueTypeWithHandler = mapTypeWithHandler.getValueType();
        assertNotNull(valueTypeWithHandler);
        assertSame(valueTypeWithHandler, mapTypeWithHandler._valueType);
        assertNull(valueTypeWithHandler.getValueHandler());
        assertNotNull(valueTypeWithHandler.getContentValueHandler());
        assertSame(handler, valueTypeWithHandler.getContentValueHandler().getObject());

        JavaType valueTypeWithContentValueType = valueTypeWithHandler.getContentType();
        assertNotNull(valueTypeWithContentValueType);
        assertSame(contentValueType, valueTypeWithContentValueType);
    }

    @Test
    public void testMapTypeWithStaticTyping() {
        Class<Map<Integer, String>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(String.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, null, null, keyType, valueType);
        MapType mapTypeWithStaticTyping = mapTypeInstance.withStaticTyping();

        assertFalse(mapTypeInstance._asStatic);
        assertTrue(mapTypeWithStaticTyping._asStatic);
    }

    @Test
    public void testMapTypeWithContentType() {
        Class<Map<Integer, String>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(String.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, null, null, keyType, valueType);
        JavaType contentType = TypeFactory.defaultInstance().constructType(Integer[].class);
        MapType mapTypeWithContentType = mapTypeInstance.withContentType(contentType);

        JavaType valueTypeWithContentType = mapTypeWithContentType.getValueType();
        assertNotNull(valueTypeWithContentType);
        assertSame(valueTypeWithContentType, mapTypeWithContentType._valueType);
        assertNotNull(valueTypeWithContentType.getContentType());
        assertSame(contentType, valueTypeWithContentType.getContentType());
    }

    @Test
    public void testMapTypeWithKeyType() {
        Class<Map<Integer, String>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(String.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, null, null, keyType, valueType);
        JavaType newKeyType = TypeFactory.defaultInstance().constructType(Long.class);
        MapType mapTypeWithNewKeyType = mapTypeInstance.withKeyType(newKeyType);

        JavaType keyTypeInMapTypeWithNewKeyType = mapTypeWithNewKeyType.getKeyType();
        assertNotNull(keyTypeInMapTypeWithNewKeyType);
        assertSame(newKeyType, keyTypeInMapTypeWithNewKeyType);
    }

    @Test
    public void testMapTypeRefine() {
        Class<Map<Integer, String>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(String.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, null, null, keyType, valueType);
        Class<Map<Long, String>> newMapType = Map.class;
        TypeBindings newBindings = TypeBindings.empty();
        JavaType newSuperClass = null;
        JavaType[] newSuperInterfaces = null;

        MapType newMapTypeInstance = mapTypeInstance.refine(newMapType.getClass(), newBindings, newSuperClass, newSuperInterfaces);

        assertSame(newMapType, newMapTypeInstance._class);
        assertSame(newBindings, newMapTypeInstance._bindings);
        assertNull(newMapTypeInstance._superClass);
        assertNull(newMapTypeInstance._superInterfaces);
        assertSame(keyType, newMapTypeInstance._keyType);
        assertSame(valueType, newMapTypeInstance._valueType);
    }

    @Test
    public void testMapTypeWithKeyTypeHandler() {
        Class<Map<Integer, String>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(String.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, null, null, keyType, valueType);
        Object handler = new Object();

        MapType mapTypeWithHandler = mapTypeInstance.withKeyTypeHandler(handler);
        assertNotNull(mapTypeWithHandler);
        assertSame(mapTypeWithHandler._keyTypeHandler, handler);
        assertNull(mapTypeWithHandler._keyType.getKeyTypeHandler());

        JavaType keyTypeWithHandler = mapTypeWithHandler.getKeyType();
        assertNotNull(keyTypeWithHandler);
        assertSame(keyTypeWithHandler, mapTypeWithHandler._keyType);
    }

    @Test
    public void testMapTypeWithKeyValueHandler() {
        Class<Map<Integer, String>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(String.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, null, null, keyType, valueType);
        Object handler = new Object();

        MapType mapTypeWithHandler = mapTypeInstance.withKeyValueHandler(handler);
        assertNotNull(mapTypeWithHandler);
        assertSame(mapTypeWithHandler._keyValueHandler, handler);
        assertNull(mapTypeWithHandler._keyType.getKeyValueHandler());

        JavaType keyTypeWithHandler = mapTypeWithHandler.getKeyType();
        assertNotNull(keyTypeWithHandler);
        assertSame(keyTypeWithHandler, mapTypeWithHandler._keyType);
    }
}