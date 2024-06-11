package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class MapTypeTest {

 // Test case for the constructor method
 @Test
 public void testConstruct() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType keyType = JavaType.constructParametricType(HashMap.class, String.class, Integer.class);
 JavaType valueType = JavaType.constructParametricType(Integer.class);
 MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
 assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", mapType.getRawClass().getName());
 assertEquals(keyType, mapType.getKeyType());
 assertEquals(valueType, mapType.getValueType());
 }

 // Test case for the deprecated construct method
 @Test
 public void testConstructDeprecated() {
 Class<?> rawType = Map.class;
 JavaType keyType = JavaType.constructParametricType(String.class);
 JavaType valueType = JavaType.constructParametricType(Integer.class);
 MapType mapType = MapType.construct(rawType, keyType, valueType);
 assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", mapType.getRawClass().getName());
 assertEquals(keyType, mapType.getKeyType());
 assertEquals(valueType, mapType.getValueType());
 }

 // Test case for the _narrow method
 @Test
 public void testNarrow() {
 MapType originalMapType = MapType.construct(Map.class, null, null, null,
 String.class, Integer.class);
 Class<?> subclass = HashMap.class;
 MapType narrowedMapType = originalMapType._narrow(subclass);
 assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", narrowedMapType.getRawClass().getName());
 }

 // Test case for the withTypeHandler method
 @Test
 public void testWithTypeHandler() {
 MapType mapType = MapType.construct(Map.class, null, null, null,
 String.class, Integer.class);
 Object handler = new Object();
 MapType newMapType = mapType.withTypeHandler(handler);
 assertNotSame(mapType, newMapType);
 assertEquals(handler, newMapType.getTypeHandler());
 }

 // Test case for the withContentTypeHandler method
 @Test
 public void testWithContentTypeHandler() {
 MapType mapType = MapType.construct(Map.class, null, null, null,
 String.class, JavaType.constructParametricType(Integer.class, String.class), null, null, false);
 Object handler = new Object();
 MapType newMapType = mapType.withContentTypeHandler(handler);
 assertNotSame(mapType, newMapType);
 assertEquals(handler, newMapType.getContentTypeHandler());
 }

 // Test case for the withValueHandler method
 @Test
 public void testWithValueHandler() {
 MapType mapType = MapType.construct(Map.class, null, null, null,
 String.class, Integer.class);
 Object handler = new Object();
 MapType newMapType = mapType.withValueHandler(handler);
 assertNotSame(mapType, newMapType);
 assertEquals(handler, newMapType.getValueHandler());
 }

 // Test case for the withContentValueHandler method
 @Test
 public void testWithContentValueHandler() {
 MapType mapType = MapType.construct(Map.class, null, null, null,
 String.class, JavaType.constructParametricType(Integer.class, String.class), null, null, false);
 Object handler = new Object();
 MapType newMapType = mapType.withContentValueHandler(handler);
 assertNotSame(mapType, newMapType);
 assertEquals(handler, newMapType.getContentValueHandler());
 }

 // Test case for the withStaticTyping method
 @Test
 public void testWithStaticTyping() {
 MapType mapType = MapType.construct(Map.class, null, null, null,
 String.class, Integer.class, null, null, false);
 MapType staticMapType = mapType.withStaticTyping();
 assertNotSame(mapType, staticMapType);
 assertTrue(staticMapType.isStatic());
 }

 // Test case for the withContentType method
 @Test
 public void testWithContentType() {
 MapType originalMapType = MapType.construct(Map.class, null, null, null,
 String.class, JavaType.constructParametricType(Integer.class, String.class), null, null, false);
 JavaType newContentType = JavaType.constructParametricType(Float.class);
 MapType newMapType = originalMapType.withContentType(newContentType);
 assertNotSame(originalMapType, newMapType);
 assertEquals(newContentType, newMapType.getValueType());
 }

 // Test case for the withKeyType method
 @Test
 public void testWithKeyType() {
 MapType originalMapType = MapType.construct(Map.class, null, null, null,
 String.class, Integer.class, null, null, false);
 JavaType newKeyType = JavaType.constructParametricType(Long.class);
 MapType newMapType = originalMapType.withKeyType(newKeyType);
 assertNotSame(originalMapType, newMapType);
 assertEquals(newKeyType, newMapType.getKeyType());
 }

 // Test case for the refine method
 @Test
 public void testRefine() {
 MapType originalMapType = MapType.construct(Map.class, null, null, null,
 String.class, Integer.class, null, null, false);
 Class<?> rawType = HashMap.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType superClass = Object.class;
 JavaType[] superInterfaces = new JavaType[] { List.class };
 MapType newMapType = originalMapType.refine(rawType, bindings, superClass, superInterfaces);
 assertNotSame(originalMapType, newMapType);
 assertEquals(rawType, newMapType.getRawClass());
 }

 // Test case for the withKeyTypeHandler method
 @Test
 public void testWithKeyTypeHandler() {
 MapType mapType = MapType.construct(Map.class, null, null, null,
 String.class, Integer.class, null, null, false);
 Object handler = new Object();
 MapType newMapType = mapType.withKeyTypeHandler(handler);
 assertNotSame(mapType, newMapType);
 assertEquals(handler, newMapType.getKeyTypeHandler());
 }

 // Test case for the withKeyValueHandler method
 @Test
 public void testWithKeyValueHandler() {
 MapType mapType = MapType.construct(Map.class, null, null, null,
 String.class, Integer.class, null, null, false);
 Object handler = new Object();
 MapType newMapType = mapType.withKeyValueHandler(handler);
 assertNotSame(mapType, newMapType);
 assertEquals(handler, newMapType.getKeyValueHandler());
 }
}