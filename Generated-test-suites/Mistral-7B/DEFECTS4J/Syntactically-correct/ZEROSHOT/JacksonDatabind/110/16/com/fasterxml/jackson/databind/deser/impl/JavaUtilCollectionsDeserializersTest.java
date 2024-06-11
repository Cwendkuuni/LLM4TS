package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

 private static Object mapper() {
 ObjectMapper mapper = new ObjectMapper();
 return mapper;
 }

 @Test
 public void testFindForCollectionSingletonList() throws Exception {
 ObjectMapper mapper = mapper();
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<List<Boolean>>() {});
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
 assertNotNull(deserializer);
 List<Boolean> list = mapper.readValue(new String("[]"), deserializer.getType());
 assertEquals(1, list.size());
 assertTrue(list.get(0));
 }

 @Test
 public void testFindForCollectionSingletonSet() throws Exception {
 ObjectMapper mapper = mapper();
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Set<Boolean>>() {});
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
 assertNotNull(deserializer);
 Set<Boolean> set = mapper.readValue(new String("{}"), deserializer.getType());
 assertEquals(1, set.size());
 assertTrue(set.contains(Boolean.TRUE));
 }

 @Test
 public void testFindForCollectionUnmodifiableList() throws Exception {
 ObjectMapper mapper = mapper();
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<List<Boolean>>() {}.where(CollectionType.Parameterized.construct(List.class, Boolean.class)).withoutRaw());
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
 assertNotNull(deserializer);
 List<Boolean> list = mapper.readValue(new String("[true]"), deserializer.getType());
 assertFalse(list.getClass().isInstance(Collections.emptyList().getClass()));
 assertTrue(list.contains(Boolean.TRUE));
 }

 @Test
 public void testFindForCollectionUnmodifiableSet() throws Exception {
 ObjectMapper mapper = mapper();
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Set<Boolean>>() {}.where(CollectionType.Parameterized.construct(Set.class, Boolean.class)).withoutRaw());
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
 assertNotNull(deserializer);
 Set<Boolean> set = mapper.readValue(new String("{true}"), deserializer.getType());
 assertFalse(set.getClass().isInstance(Collections.emptySet().getClass()));
 assertTrue(set.contains(Boolean.TRUE));
 }

 @Test
 public void testFindForMapSingletonMap() throws Exception {
 ObjectMapper mapper = mapper();
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Map<String, Boolean>>() {});
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaType);
 assertNotNull(deserializer);
 Map<String, Boolean> map = mapper.readValue(new String("{\"key\":true}"), deserializer.getType());
 assertEquals(1, map.size());
 assertTrue(map.get("key"));
 }

 @Test
 public void testFindForMapUnmodifiableMap() throws Exception {
 ObjectMapper mapper = mapper();
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Map<String, Boolean>>() {}.where(MapType.Parameterized.construct(Map.class, String.class, Boolean.class)).withoutRaw());
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaType);
 assertNotNull(deserializer);
 Map<String, Boolean> map = mapper.readValue(new String("{\"key\":true}"), deserializer.getType());
 assertFalse(map.getClass().isInstance(Collections.emptyMap().getClass()));
 assertTrue(map.containsKey("key"));
 assertTrue(map.get("key"));
 }
}