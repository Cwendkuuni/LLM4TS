package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;

public class MapTypeTest {

  @Test
  public void testConstructMapType() throws Exception {
    Class<Map<String, Integer>> rawType = Map.class;
    TypeBindings bindings = TypeFactory.defaultInstance();
    JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
    JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
    JavaType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
    ObjectMapper mapper = new ObjectMapper();
    // Test serialization and deserialization
    String json = mapper.writeValueAsString(new java.util.HashMap<String, Integer>());
    java.util.Map<String, Integer> map = mapper.readValue(json, mapType.getType());
    // Test key and value types
    assertEquals(String.class, mapType.getKeyType());
    assertEquals(Integer.class, mapType.getValueType());
  }

  @Test
  public void testConstructMapTypeWithNullBindings() throws Exception {
    Class<Map<String, Integer>> rawType = Map.class;
    JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
    JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
    JavaType mapType = MapType.construct(rawType, null, null, null, keyType, valueType);
    ObjectMapper mapper = new ObjectMapper();
    // Test serialization and deserialization
    String json = mapper.writeValueAsString(new java.util.HashMap<String, Integer>());
    java.util.Map<String, Integer> map = mapper.readValue(json, mapType.getType());
    // Test key and value types
    assertEquals(String.class, mapType.getKeyType());
    assertEquals(Integer.class, mapType.getValueType());
    // Test that _bindings is null
    assertNull(mapType._bindings);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructMapTypeWithWrongBindings() throws Exception {
    Class<Map<String, Integer>> rawType = Map.class;
    TypeBindings bindings = TypeFactory.defaultInstance();
    JavaType keyType = TypeFactory.defaultInstance().constructType(Integer.class);
    JavaType valueType = TypeFactory.defaultInstance().constructType(String.class);
    JavaType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
  }

  @Test
  public void testConstructMapTypeWithDeprecatedMethod() throws Exception {
    Class<Map<String, Integer>> rawType = Map.class;
    JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
    JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
    JavaType mapType = MapType.construct(rawType, null, null, null, keyType, valueType);
    // Test that constructor with no bindings is deprecated
    JavaType deprecatedMapType = MapType.construct(rawType, keyType, valueType);
    // Test that deserialization fails with deprecated constructor
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(new java.util.HashMap<String, Integer>());
    try {
      mapper.readValue(json, deprecatedMapType.getType());
      fail();
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test
  public void testRefineMapType() throws Exception {
    Class<Map<String, Integer>> rawType = Map.class;
    Class<Map<String, Long>> refinedRawType = Map.class;
    TypeBindings bindings = TypeFactory.defaultInstance();
    JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
    JavaType valueType = TypeFactory.defaultInstance().constructType(Long.class);
    JavaType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
    JavaType refinedMapType = mapType.refine(refinedRawType, bindings, null, null);
    ObjectMapper mapper = new ObjectMapper();
    // Test serialization and deserialization
    String json = mapper.writeValueAsString(new java.util.HashMap<String, Long>());
    java.util.Map<String, Long> map = mapper.readValue(json, refinedMapType.getType());
    // Test key and value types
    assertEquals(String.class, refinedMapType.getKeyType());
    assertEquals(Long.class, refinedMapType.getValueType());
  }

  @Test
  public void testWithKeyTypeHandler() throws Exception {
    Class<Map<String, Integer>> rawType = Map.class;
    TypeBindings bindings = TypeFactory.defaultInstance();
    JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
    JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
    JavaType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
    ObjectMapperKeyTypeHandler handler = new ObjectMapperKeyTypeHandler();
    JavaType mappedKeyType = mapType.withKeyTypeHandler(handler);
    ObjectMapper mapper = new ObjectMapper();
    // Test serialization and deserialization
    String json = mapper.writeValueAsString(new java.util.HashMap<String, Integer>());
    java.util.Map<String, Integer> map = mapper.readValue(json, mappedKeyType.getType());
    // Test key handler
    assertEquals(As.PROPERTY, handler.getSerializer().getContentAsType());
  }
}