package com.example.junit;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MapLikeTypeTest {

 private JavaType keyType = new SimpleType("java.lang.String").getType();
 private JavaType valueType = new SimpleType("java.lang.Integer").getType();

 @Test
 public void testInstanciationWithSimpleTypes() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
 assertEquals("java.util.Map", mapLikeType.getClassName());
 assertEquals(keyType, mapLikeType.getKeyType());
 assertEquals(valueType, mapLikeType.getValueType());
 }

 @Test
 public void testInstanciationWithBaseTypeAndTypes() {
 JavaType baseType = new ClassType(Map.class);
 MapLikeType mapLikeType = new MapLikeType(baseType, keyType, valueType);
 assertEquals("java.util.Map", mapLikeType.getClassName());
 assertEquals(keyType, mapLikeType.getKeyType());
 assertEquals(valueType, mapLikeType.getValueType());
 }

 @Test
 public void testInstanciationWithBindings() {
 TypeBindings bindings = new TypeBindings();
 MapLikeType mapLikeType = new MapLikeType(Map.class, bindings, null, null, keyType, valueType);
 assertEquals("java.util.Map", mapLikeType.getClassName());
 assertEquals(keyType, mapLikeType.getKeyType());
 assertEquals(valueType, mapLikeType.getValueType());
 }

 @Test
 public void testInstanciationWithAsStatic() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, false);
 assertEquals(false, mapLikeType.isStatic());

 MapLikeType mapLikeTypeStatic = new MapLikeType(Map.class, null, null, null, keyType, valueType, true);
 assertEquals(true, mapLikeTypeStatic.isStatic());
 }

 @Test
 public void testUpgradeFromJavaType() {
 JavaType javaType = new MapType(Map.class, keyType, valueType);
 MapLikeType mapLikeType = MapLikeType.upgradeFrom(javaType, keyType, valueType);
 assertEquals("java.util.Map", mapLikeType.getClassName());
 assertEquals(keyType, mapLikeType.getKeyType());
 assertEquals(valueType, mapLikeType.getValueType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidUpgradeFromJavaType() {
 JavaType javaType = new SimpleType("java.lang.String").getType();
 MapLikeType mapLikeType = MapLikeType.upgradeFrom(javaType, keyType, valueType);
 }

 @Test
 public void testNarrow() {
 JavaType javaType = new MapType(HashMap.class, keyType, valueType);
 MapLikeType mapLikeType = new MapLikeType(javaType, keyType, valueType);
 JavaType narrowedType = mapLikeType.narrow(HashSet.class);
 assertEquals("java.util.HashSet", narrowedType.getClassName());
 }

 @Test
 public void testIsTrueMapType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
 assertTrue(mapLikeType.isTrueMapType());

 JavaType javaType = new SimpleType("java.lang.List").getType();
 MapLikeType mapLikeTypeList = new MapLikeType(javaType, keyType, valueType);
 assertFalse(mapLikeTypeList.isTrueMapType());
 }

 @Test
 public void testGetErasedSignature() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
 StringBuilder sb = new StringBuilder();
 mapLikeType.getErasedSignature(sb);
 assertEquals("java.util.Map", sb.toString());
 }

 @Test
 public void testGetGenericSignature() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
 StringBuilder sb = new StringBuilder();
 mapLikeType.getGenericSignature(sb);
 assertEquals("[map-like type; class java.util.Map<java.lang.String,java.lang.Integer>, java.lang.String -> java.lang.Integer]", sb.toString());
 }

 @Test
 public void testWithKeyType() {
 JavaType javaType = new MapType(Map.class, new SimpleType("String").getType(), new SimpleType("Integer").getType());
 MapLikeType mapLikeType = new MapLikeType(javaType, keyType, valueType);
 MapLikeType mapLikeTypeNewKey = mapLikeType.withKeyType(new SimpleType("Integer").getType());
 assertEquals(new SimpleType("Integer").getType(), mapLikeTypeNewKey.getKeyType());
 }

 @Test
 public void testWithValueType() {
 JavaType javaType = new MapType(Map.class, keyType, valueType);
 MapLikeType mapLikeType = new MapLikeType(javaType, keyType, valueType);
 MapLikeType mapLikeTypeNewValue = mapLikeType.withValueType(new SimpleType("Long").getType());
 assertEquals(new SimpleType("Long").getType(), mapLikeTypeNewValue.getValueType());
 }

 @Test
 public void testWithTypeHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
 Object handler = new Handler();
 MapLikeType mapLikeTypeNewHandler = mapLikeType.withTypeHandler(handler);
 assertEquals(handler, mapLikeTypeNewHandler.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
 Object handler = new Handler();
 ValueHandler valueHandler = new ValueHandler();
 MapLikeType mapLikeTypeNewContentHandler = mapLikeType.withContentTypeHandler(handler);
 MapLikeType mapLikeTypeNewContentValueHandler = mapLikeTypeNewContentHandler.withContentValueHandler(valueHandler);
 assertEquals(valueHandler, mapLikeTypeNewContentValueHandler.getValueHandler());
 }

 @Test
 public void testWithKeyTypeHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
 Object handler = new Handler();
 MapLikeType mapLikeTypeNewKeyHandler = mapLikeType.withKeyTypeHandler(handler);
 assertEquals(handler, mapLikeTypeNewKeyHandler.getKeyTypeHandler());
 }

 @Test
 public void testWithKeyValueHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
 Object handler = new Handler();
 ValueHandler valueHandler = new ValueHandler();
 MapLikeType mapLikeTypeNewKeyValueHandler = mapLikeType.withKeyValueHandler(handler);
 MapLikeType mapLikeTypeNewHandler = mapLikeTypeNewKeyValueHandler.withTypeHandler(valueHandler);
 assertEquals(valueHandler, mapLikeTypeNewHandler.getTypeHandler());
 assertEquals(handler, mapLikeTypeNewHandler.getKeyTypeHandler());
 }

 @Test
 public void testWithStaticTyping() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
 MapLikeType mapLikeTypeStatic = mapLikeType.withStaticTyping();
 assertTrue(mapLikeTypeStatic.isStatic());
 }

 @Test
 public void testRefine() {
 JavaType javaType = new MapType(Map.class, keyType, valueType);
 MapLikeType mapLikeType = new MapLikeType(javaType, keyType, valueType);
 JavaType refinedType = mapLikeType.refine(HashMap.class, null, null, null);
 assertEquals("java.util.HashMap", refinedType.getClassName());
 }

}