package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;

public class MapTypeTest {

    @Test
    public void testConstructWithArguments() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[] {};
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);

        assertEquals("Expected mapType to be instance of MapType", MapType.class, mapTypeInstance.getClass());
        assertEquals("Expected mapType.getClass() to match Map<String,Integer>", mapType, mapTypeInstance.getRawClass());
        assertEquals("Expected mapType.getKeyType() to be String", keyType, mapTypeInstance.getKeyType());
        assertEquals("Expected mapType.getValueType() to be Integer", valueType, mapTypeInstance.getValueType());
    }

    @Test
    public void testConstructWithDeprecatedMethod() {
        Class<Map<String, Integer>> mapType = Map.class;
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);

        MapType mapTypeInstance = MapType.construct(mapType, keyType, valueType);

        assertEquals("Expected mapType to be instance of MapType", MapType.class, mapTypeInstance.getClass());
        assertEquals("Expected mapType.getClass() to match Map<String,Integer>", mapType, mapTypeInstance.getRawClass());
        assertEquals("Expected mapType.getKeyType() to be String", keyType, mapTypeInstance.getKeyType());
        assertEquals("Expected mapType.getValueType() to be Integer", valueType, mapTypeInstance.getValueType());
    }

    @Test
    public void testRefineMethod() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[] {};
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);

        JavaType refinedType = mapTypeInstance.refine(Map.class, bindings, superClass, superInts);

        assertEquals("Expected refinedType to be instance of MapType", MapType.class, refinedType.getClass());
        assertEquals("Expected refinedType.getClass() to match Map<String,Integer>", mapType, refinedType.getRawClass());
    }

    @Test
    public void testWithTypeHandlerMethod() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[] {};
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);

        Object handler = new Object();
        MapType withTypeHandler = mapTypeInstance.withTypeHandler(handler);

        assertEquals("Expected withTypeHandler to be instance of MapType", MapType.class, withTypeHandler.getClass());
        assertEquals("Expected withTypeHandler.getClass() to match Map<String,Integer>", mapType, withTypeHandler.getRawClass());
        assertNotSame("Expected withTypeHandler not to be equal to mapTypeInstance", mapTypeInstance, withTypeHandler);
        assertSame("Expected withTypeHandler.getTypeHandler() to be same handler", handler, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandlerMethod() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[] {};
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);

        Object handler = new Object();
        JavaType contentTypeHandler = JavaType.construct(Object.class);

        MapType withContentTypeHandler = mapTypeInstance.withContentTypeHandler(handler);

        assertEquals("Expected withContentTypeHandler to be instance of MapType", MapType.class, withContentTypeHandler.getClass());
        assertEquals("Expected withContentTypeHandler.getClass() to match Map<String,Integer>", mapType, withContentTypeHandler.getRawClass());
        assertNotSame("Expected withContentTypeHandler not to be equal to mapTypeInstance", mapTypeInstance, withContentTypeHandler);
        assertSame("Expected withContentTypeHandler.getContentTypeHandler() to be same handler", handler, withContentTypeHandler.getContentTypeHandler());
        assertNull("Expected withContentTypeHandler.getTypeHandler() to be null", withContentTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithValueHandlerMethod() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[] {};
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);

        Object handler = new Object();
        MapType withValueHandler = mapTypeInstance.withValueHandler(handler);

        assertEquals("Expected withValueHandler to be instance of MapType", MapType.class, withValueHandler.getClass());
        assertEquals("Expected withValueHandler.getClass() to match Map<String,Integer>", mapType, withValueHandler.getRawClass());
        assertNotSame("Expected withValueHandler not to be equal to mapTypeInstance", mapTypeInstance, withValueHandler);
        assertSame("Expected withValueHandler.getValueHandler() to be same handler", handler, withValueHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandlerMethod() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[] {};
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);

        Object handler = new Object();
        JavaType contentValueType = JavaType.construct(Object.class);

        MapType withContentValueHandler = mapTypeInstance.withContentValueHandler(handler);

        assertEquals("Expected withContentValueHandler to be instance of MapType", MapType.class, withContentValueHandler.getClass());
        assertEquals("Expected withContentValueHandler.getClass() to match Map<String,Integer>", mapType, withContentValueHandler.getRawClass());
        assertNotSame("Expected withContentValueHandler not to be equal to mapTypeInstance", mapTypeInstance, withContentValueHandler);
        assertSame("Expected withContentValueHandler.getContentValueHandler() to be same handler", handler, withContentValueHandler.getContentValueHandler());
        assertNull("Expected withContentValueHandler.getValueHandler() to be null", withContentValueHandler.getValueHandler());
    }

    @Test
    public void testWithStaticTypingMethod() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[] {};
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);

        MapType withStaticTyping = mapTypeInstance.withStaticTyping();

        assertTrue("Expected withStaticTyping to be statically typed", withStaticTyping.isStaticType());
        assertEquals("Expected withStaticTyping.getClass() to match Map<String,Integer>", mapType, withStaticTyping.getRawClass());
        assertNotSame("Expected withStaticTyping not to be equal to mapTypeInstance", mapTypeInstance, withStaticTyping);
    }

    @Test
    public void testWithContentTypeMethod() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[] {};
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);

        JavaType contentType = JavaType.construct(Object.class);

        MapType withContentType = mapTypeInstance.withContentType(contentType);

        assertEquals("Expected withContentType to be instance of MapType", MapType.class, withContentType.getClass());
        assertEquals("Expected withContentType.getClass() to match Map<String,Integer>", mapType, withContentType.getRawClass());
        assertNotSame("Expected withContentType not to be equal to mapTypeInstance", mapTypeInstance, withContentType);
        assertSame("Expected withContentType.getValueType() to be same contentType", contentType, withContentType.getValueType());
    }

    @Test
    public void testWithKeyTypeMethod() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[] {};
        JavaType keyType = JavaType.construct(Integer.class);
        JavaType valueType = JavaType.construct(Integer.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);

        JavaType newKeyType = JavaType.construct(String.class);

        MapType withKeyType = mapTypeInstance.withKeyType(newKeyType);

        assertEquals("Expected withKeyType to be instance of MapType", MapType.class, withKeyType.getClass());
        assertEquals("Expected withKeyType.getClass() to match Map<String,Integer>", mapType, withKeyType.getRawClass());
        assertNotSame("Expected withKeyType not to be equal to mapTypeInstance", mapTypeInstance, withKeyType);
        assertSame("Expected withKeyType.getKeyType() to be same newKeyType", newKeyType, withKeyType.getKeyType());
    }
}