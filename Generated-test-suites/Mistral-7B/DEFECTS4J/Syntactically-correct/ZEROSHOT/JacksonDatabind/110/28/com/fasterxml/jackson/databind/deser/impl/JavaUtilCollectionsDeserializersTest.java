package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

 private ObjectMapper objectMapper = new JsonMapper();

 @Test
 public void testFindForCollection_SingletonList() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<List<Integer>>(){}.getType());
 DeserializationContext context = objectMapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, javaType);
 assertNotNull(deserializer);

 List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
 String json = "1"; // single element JSON representation
 Object deserializedObject = objectMapper.readValue(json, javaType.withRawType(List.class));
 assertEquals(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_LIST, deserializedObject.getClass());

 List<Integer> deserializedList = deserializer.deserialize(context, deserializedObject);
 assertEquals(1, deserializedList.size());
 assertEquals(1, deserializedList.get(0).intValue());
 }

 @Test
 public void testFindForCollection_SingletonSet() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<Set<Integer>>(){}.getType());
 DeserializationContext context = objectMapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, javaType);
 assertNotNull(deserializer);

 Set<Integer> set = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5));
 String json = "1"; // single element JSON representation
 Object deserializedObject = objectMapper.readValue(json, javaType.withRawType(Set.class));
 assertEquals(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_SET, deserializedObject.getClass());

 Set<Integer> deserializedSet = deserializer.deserialize(context, deserializedObject);
 assertEquals(1, deserializedSet.size());
 assertEquals(1, deserializedSet.iterator().next().intValue());
 }

 @Test
 public void testFindForCollection_UnmodifiableList() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<List<Integer>>(){}.getType());
 DeserializationContext context = objectMapper.getDeserializationContext();
 context.enable(SerializationFeature.FIND_PROPERTIES_ON_EMPTY_ARRAYS);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, javaType);
 assertNotNull(deserializer);

 List<Integer> list = Arrays.asList(1, 2, 3, 4, 5).stream().map(i -> Collections.singletonList(i)).collect(Collectors.toList());
 String json = "[]"; // empty JSON representation
 Object deserializedObject = objectMapper.readValue(json, javaType.withRawType(List.class));
 assertEquals(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_LIST, deserializedObject.getClass());

 List<Integer> deserializedList = deserializer.deserialize(context, deserializedObject);
 assertEquals(0, deserializedList.size());
 }

 @Test
 public void testFindForCollection_UnmodifiableSet() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<Set<Integer>>(){}.getType());
 DeserializationContext context = objectMapper.getDeserializationContext();
 context.enable(SerializationFeature.FIND_PROPERTIES_ON_EMPTY_ARRAYS);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, javaType);
 assertNotNull(deserializer);

 Set<Integer> set = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5)).stream().map(i -> Collections.singleton(i)).collect(Collectors.toSet());
 String json = "{}"; // empty JSON representation
 Object deserializedObject = objectMapper.readValue(json, javaType.withRawType(Set.class));
 assertEquals(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_SET, deserializedObject.getClass());

 Set<Integer> deserializedSet = deserializer.deserialize(context, deserializedObject);
 assertEquals(0, deserializedSet.size());
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForCollection_UnknownType() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructType(Integer[].class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, javaType);
 assertNull(deserializer);
 }

 @Test
 public void testFindForMap_SingletonMap() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<Map<String,Integer>>(){}.getType());
 DeserializationContext context = objectMapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(context, javaType);
 assertNotNull(deserializer);

 Map<String, Integer> map = new HashMap<>();
 map.put("key", 1);
 String json = "{\"key\":1}";
 Object deserializedObject = objectMapper.readValue(json, javaType.withRawType(Map.class));
 assertEquals(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP, deserializedObject.getClass());

 Map<String, Integer> deserializedMap = deserializer.deserialize(context, deserializedObject);
 assertEquals(1, deserializedMap.size());
 assertEquals("key", deserializedMap.keySet().iterator().next());
 assertEquals(1, deserializedMap.get("key"));
 }

 @Test
 public void testFindForMap_UnmodifiableMap() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<Map<String,Integer>>(){}.getType());
 DeserializationContext context = objectMapper.getDeserializationContext();
 context.enable(SerializationFeature.FIND_PROPERTIES_ON_EMPTY_ARRAYS);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(context, javaType);
 assertNotNull(deserializer);

 Map<String, Integer> map = new HashMap<>();
 map.put("key", 1);
 Map<String, Integer> unmodifiableMap = Collections.unmodifiableMap(map);
 String json = "{}"; // empty JSON representation
 Object deserializedObject = objectMapper.readValue(json, javaType.withRawType(Map.class));
 assertEquals(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP, deserializedObject.getClass());

 Map<String, Integer> deserializedMap = deserializer.deserialize(context, deserializedObject);
 assertEquals(0, deserializedMap.size());
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForMap_UnknownType() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(context, javaType);
 assertNull(deserializer);
 }
}