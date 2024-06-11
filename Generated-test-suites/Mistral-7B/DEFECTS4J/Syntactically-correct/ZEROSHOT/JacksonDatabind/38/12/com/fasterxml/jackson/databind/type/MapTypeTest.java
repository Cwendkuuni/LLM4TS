package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

 @Test
 public void testConstruct() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType keyType = JavaType.construct(String.class, bindings);
 JavaType valueType = JavaType.construct(Integer.class, bindings);
 MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
 assertEquals(rawType, mapType.getRawClass());
 assertEquals(keyType, mapType.getKeyType());
 assertEquals(valueType, mapType.getValueType());
 }

 @Test
 public void testConstructDeprecated() {
 Class<?> rawType = Map.class;
 JavaType keyType = JavaType.construct(String.class, null);
 JavaType valueType = JavaType.construct(Integer.class, null);
 MapType mapType = MapType.construct(rawType, keyType, valueType);
 assertEquals(rawType, mapType.getRawClass());
 assertEquals(keyType, mapType.getKeyType());
 assertEquals(valueType, mapType.getValueType());
 }

 @Test
 public void testRefine() {
 Class<?> rawType = HashMap.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType keyType = JavaType.construct(String.class, bindings);
 JavaType valueType = JavaType.construct(Integer.class, bindings);
 MapType mapType = MapType.construct(Map.class, bindings, null, null, keyType, valueType);
 MapType refinedMapType = mapType.refine(rawType, bindings, null, null);
 assertEquals(rawType, refinedMapType.getRawClass());
 assertEquals(keyType, refinedMapType.getKeyType());
 assertEquals(valueType, refinedMapType.getValueType());
 }

 @Test
 public void testWithTypeHandler() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType keyType = JavaType.construct(String.class, bindings);
 JavaType valueType = JavaType.construct(Integer.class, bindings);
 Object handler = new Object();
 MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
 MapType withHandler = mapType.withTypeHandler(handler);
 assertNotSame(mapType, withHandler);
 assertEquals(handler, withHandler.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType keyType = JavaType.construct(String.class, bindings);
 JavaType valueType = JavaType.construct(Integer.class, bindings);
 JavaType contentType = JavaType.construct(Long.class, bindings);
 MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
 MapType withContentTypeHandler = mapType.withContentTypeHandler(contentType);
 assertNotSame(mapType, withContentTypeHandler);
 assertEquals(contentType, withContentTypeHandler.getValueType());
 }

 @Test
 public void testWithValueHandler() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType keyType = JavaType.construct(String.class, bindings);
 JavaType valueType = JavaType.construct(Integer.class, bindings);
 Object handler = new Object();
 MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
 MapType withHandler = mapType.withValueHandler(handler);
 assertNotSame(mapType, withHandler);
 assertEquals(handler, withHandler.getValueHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType keyType = JavaType.construct(String.class, bindings);
 JavaType valueType = JavaType.construct(Integer.class, bindings);
 JavaType contentValueHandler = JavaType.construct(String.class, bindings);
 MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
 MapType withContentValueHandler = mapType.withContentValueHandler(contentValueHandler);
 assertNotSame(mapType, withContentValueHandler);
 assertEquals(contentValueHandler, withContentValueHandler.getValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType keyType = JavaType.construct(String.class, bindings);
 JavaType valueType = JavaType.construct(Integer.class, bindings);
 MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
 MapType withStaticTyping = mapType.withStaticTyping();
 assertNotSame(mapType, withStaticTyping);
 assertTrue(withStaticTyping.isStatic());
 }

 @Test
 public void testWithKeyType() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType oldKeyType = JavaType.construct(String.class, bindings);
 JavaType newKeyType = JavaType.construct(Integer.class, bindings);
 MapType mapType = MapType.construct(rawType, bindings, null, null, oldKeyType, JavaType.construct(Integer.class, bindings));
 MapType withNewKeyType = mapType.withKeyType(newKeyType);
 assertNotSame(mapType, withNewKeyType);
 assertEquals(newKeyType, withNewKeyType.getKeyType());
 }

 @Test
 public void testWithContentType() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType oldValueType = JavaType.construct(Integer.class, bindings);
 JavaType newValueType = JavaType.construct(Long.class, bindings);
 MapType mapType = MapType.construct(rawType, bindings, null, null, JavaType.construct(String.class, bindings), oldValueType);
 MapType withNewValueType = mapType.withContentType(newValueType);
 assertNotSame(mapType, withNewValueType);
 assertEquals(newValueType, withNewValueType.getValueType());
 }
}