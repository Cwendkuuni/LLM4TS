package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeBase;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import junit.framework.Assert;

public class MapLikeTypeTest {

 // Test case for constructor with Class, TypeBindings, JavaType, JavaType, Object, Object, boolean
 @Test
 public void testConstruct() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null,
 String.class, Integer.class, null, null, false);
 assertEquals("java.util.Map", mapLikeType.getClassName());
 assertEquals(String.class, mapLikeType.getKeyType());
 assertEquals(Integer.class, mapLikeType.getValueType());
 assertFalse(mapLikeType.isStatic());
 }

 // Test case for upgradeFrom method
 @Test
 public void testUpgradeFrom() {
 JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
 MapLikeType mapLikeType = MapLikeType.upgradeFrom(javaType, String.class, Integer.class);
 assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", mapLikeType.getClassName());
 assertEquals(String.class, mapLikeType.getKeyType());
 assertEquals(Integer.class, mapLikeType.getValueType());
 assertFalse(mapLikeType.isStatic());
 }

 // Test case for isContainerType() and isMapLikeType() methods
 @Test
 public void testIsContainerTypeAndIsMapLikeType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null,
 String.class, Integer.class, null, null, false);
 assertTrue(mapLikeType.isContainerType());
 assertTrue(mapLikeType.isMapLikeType());
 }

 // Test case for getKeyType() and getValueType() methods
 @Test
 public void testGetKeyTypeAndgetValueType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null,
 String.class, Integer.class, null, null, false);
 assertEquals(String.class, mapLikeType.getKeyType());
 assertEquals(Integer.class, mapLikeType.getValueType());
 }

 // Test case for isTrueMapType() method
 @Test
 public void testIsTrueMapType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null,
 String.class, Integer.class, null, null, false);
 assertTrue(mapLikeType.isTrueMapType());
 }

 // Test case for withKeyType() method
 @Test
 public void testWithKeyType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null,
 String.class, Integer.class, null, null, false);
 JavaType newKeyType = TypeFactory.defaultInstance().constructType(Number.class);
 MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);
 assertEquals(newKeyType, newMapLikeType.getKeyType());
 }

 // Test case for withContentType() method
 @Test
 public void testWithContentType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null,
 String.class, Integer.class, null, null, false);
 JavaType newValueType = TypeFactory.defaultInstance().constructType(Double.class);
 MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);
 assertEquals(newValueType, newMapLikeType.getValueType());
 }

 // Test case for withValueHandler() method
 @Test
 public void testWithValueHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null,
 String.class, Integer.class, null, null, false);
 Integer value = 10;
 Object valueHandler = new Object() {
 @SuppressWarnings("unchecked")
 public <T> T handle(Map.Entry<String, T> entry) {
 return (T) entry.getValue();
 }
 };
 MapLikeType newMapLikeType = mapLikeType.withValueHandler(valueHandler);
 assertEquals(valueHandler, newMapLikeType.getValueHandler());
 }

 // Test case for withContentValueHandler() method
 @Test
 public void testWithContentValueHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null,
 String.class, Integer.class, null, null, false);
 Integer value = 10;
 Object valueHandler = new Object() {
 @SuppressWarnings("unchecked")
 public <T> T handle(Map.Entry<String, T> entry) {
 return (T) entry.getValue();
 }
 };
 Object contentValueHandler = new Object() {
 @SuppressWarnings("unchecked")
 public <T> T handle(Map.Entry<String, T> entry, TypeSerializer ts) {
 return (T) entry.getValue();
 }
 };
 MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(contentValueHandler);
 assertEquals(contentValueHandler, newMapLikeType.getContentValueHandler());
 }

 // Test case for withKeyTypeHandler() method
 @Test
 public void testWithKeyTypeHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null,
 String.class, Integer.class, null, null, false);
 String key = "test";
 Object keyHandler = new Object() {
 @SuppressWarnings("unchecked")
 public <T> T handle(TypeSerializer ts, JavaType typeHint) {
 return (T) key;
 }
 };
 MapLikeType newMapLikeType = mapLikeType.withKeyTypeHandler(keyHandler);
 assertEquals(keyHandler, newMapLikeType.getKeyTypeHandler());
 }

 // Test case for withKeyValueHandler() method
 @Test
 public void testWithKeyValueHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null,
 String.class, Integer.class, null, null, false);
 String key = "test";
 Integer value = 10;
 Object keyValueHandler = new Object() {
 @SuppressWarnings("unchecked")
 public <T> T handle(Map.Entry<String, T> entry) {
 return (T) entry.getKey();
 }
 };
 MapLikeType newMapLikeType = mapLikeType.withKeyValueHandler(keyValueHandler);
 assertEquals(keyValueHandler, newMapLikeType.getKeyValueHandler());
 }

 // Test case for refine() method
 @Test
 public void testRefine() {
 JavaType javaType = TypeFactory.defaultInstance().constructType(HashMap.class, String.class, Integer.class);
 MapLikeType mapLikeType = (MapLikeType) javaType;
 Map<String, Integer> map = new HashMap<>();
 map.put("test", 10);
 TypeBindings bindings = TypeBindings.create(map.getClass(), String.class, Integer.class);
 JavaType refinedType = mapLikeType.refine(map.getClass(), bindings, null, null);
 assertEquals(map.getClass(), refinedType.getRawClass());
 }

 // Test case for toString() method
 @Test
 public void testToString() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null,
 String.class, Integer.class, null, null, false);
 String expected = "[map-like type; class java.util.Map, java.lang.String -> java.lang.Integer]";
 assertEquals(expected, mapLikeType.toString());
 }

 // Test case for equals() method
 @Test
 public void testEquals() {
 MapLikeType mapLikeType1 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null,
 String.class, Integer.class, null, null, false);
 MapLikeType mapLikeType2 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null,
 String.class, Integer.class, null, null, false);
 MapLikeType mapLikeType3 = new MapLikeType(List.class, TypeBindings.emptyBindings(), null, null,
 String.class, Integer.class, null, null, false);
 assertTrue(mapLikeType1.equals(mapLikeType2));
 assertFalse(mapLikeType1.equals(mapLikeType3));
 }
}