package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class JavaUtilCollectionsDeserializersTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testFindForCollection_SingletonSet() throws Exception {
 Set<Boolean> singletonSet = new HashSet<>(Collections.singleton(true));
 JavaType javaTypeSet = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
 JsonDeserializer<?> deserializerSet = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaTypeSet);
 Object resultSet = mapper.readValue("true", new TypeReference<List<Object>>() {}.getType());
 assertEquals(deserializerSet.getClass().getSimpleName(), StdDelegatingDeserializer.class.getSimpleName());
 assertEquals(deserializerSet.getClass().getSuperclass().getSimpleName(), JavaUtilCollectionsConverter.class.getSimpleName());
 assertEquals(singletonSet.size(), 1);
 assertEquals(deserializerSet.deserialize(mapper.getDeserializationContext(), resultSet), singletonSet);
 }

 @Test
 public void testFindForCollection_UnmodifiableSet() throws Exception {
 Set<Boolean> unmodifiableSet = Collections.unmodifiableSet(singletonSet);
 JavaType javaTypeUnmodifiableSet = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
 JsonDeserializer<?> deserializerUnmodifiableSet = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaTypeUnmodifiableSet);
 Object resultUnmodifiableSet = mapper.readValue("true", new TypeReference<List<Object>>() {}.getType());
 assertEquals(deserializerUnmodifiableSet.getClass().getSimpleName(), StdDelegatingDeserializer.class.getSimpleName());
 assertEquals(deserializerUnmodifiableSet.getClass().getSuperclass().getSimpleName(), JavaUtilCollectionsConverter.class.getSimpleName());
 assertEquals(unmodifiableSet.size(), 1);
 assertEquals(deserializerUnmodifiableSet.deserialize(mapper.getDeserializationContext(), resultUnmodifiableSet), unmodifiableSet);
 }

 @Test
 public void testFindForCollection_SingletonList() throws Exception {
 List<Boolean> singletonList = Collections.singletonList(true);
 JavaType javaTypeList = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
 JsonDeserializer<?> deserializerList = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaTypeList);
 Object resultList = mapper.readValue("true", new TypeReference<List<Object>>() {}.getType());
 assertEquals(deserializerList.getClass().getSimpleName(), StdDelegatingDeserializer.class.getSimpleName());
 assertEquals(deserializerList.getClass().getSuperclass().getSimpleName(), JavaUtilCollectionsConverter.class.getSimpleName());
 assertEquals(singletonList.size(), 1);
 assertEquals(deserializerList.deserialize(mapper.getDeserializationContext(), resultList), singletonList);
 }

 @Test
 public void testFindForCollection_UnmodifiableList() throws Exception {
 List<Boolean> unmodifiableList = Collections.unmodifiableList(singletonList);
 JavaType javaTypeUnmodifiableList = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
 JsonDeserializer<?> deserializerUnmodifiableList = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaTypeUnmodifiableList);
 Object resultUnmodifiableList = mapper.readValue("true", new TypeReference<List<Object>>() {}.getType());
 assertEquals(deserializerUnmodifiableList.getClass().getSimpleName(), StdDelegatingDeserializer.class.getSimpleName());
 assertEquals(deserializerUnmodifiableList.getClass().getSuperclass().getSimpleName(), JavaUtilCollectionsConverter.class.getSimpleName());
 assertEquals(unmodifiableList.size(), 1);
 assertEquals(deserializerUnmodifiableList.deserialize(mapper.getDeserializationContext(), resultUnmodifiableList), unmodifiableList);
 }

 @Test
 public void testFindForMap_SingletonMap() throws Exception {
 Map<String, String> singletonMap = new HashMap<String, String>() {{
 put("key", "value");
 }};
 JavaType javaTypeMap = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
 JsonDeserializer<?> deserializerMap = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaTypeMap);
 Object resultMap = mapper.readValue("{\"key\":\"value\"}", new TypeReference<Map<String, Object>>() {}.getType());
 assertEquals(deserializerMap.getClass().getSimpleName(), StdDelegatingDeserializer.class.getSimpleName());
 assertEquals(deserializerMap.getClass().getSuperclass().getSimpleName(), JavaUtilCollectionsConverter.class.getSimpleName());
 assertEquals(singletonMap.size(), 1);
 assertEquals(deserializerMap.deserialize(mapper.getDeserializationContext(), resultMap), singletonMap);
 }

 @Test
 public void testFindForMap_UnmodifiableMap() throws Exception {
 Map<String, String> unmodifiableMap = Collections.unmodifiableMap(singletonMap);
 JavaType javaTypeUnmodifiableMap = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
 JsonDeserializer<?> deserializerUnmodifiableMap = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaTypeUnmodifiableMap);
 Object resultUnmodifiableMap = mapper.readValue("{\"key\":\"value\"}", new TypeReference<Map<String, Object>>() {}.getType());
 assertEquals(deserializerUnmodifiableMap.getClass().getSimpleName(), StdDelegatingDeserializer.class.getSimpleName());
 assertEquals(deserializerUnmodifiableMap.getClass().getSuperclass().getSimpleName(), JavaUtilCollectionsConverter.class.getSimpleName());
 assertEquals(unmodifiableMap.size(), 1);
 assertEquals(deserializerUnmodifiableMap.deserialize(mapper.getDeserializationContext(), resultUnmodifiableMap), unmodifiableMap);
 }

 @Test
 public void testFindForCollection_AsList() throws Exception {
 List<Boolean> list = Arrays.asList(true);
 JavaType javaTypeList = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
 JsonDeserializer<?> deserializerList = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaTypeList);
 Object resultList = mapper.readValue("true", new TypeReference<List<Object>>() {}.getType());
 assertEquals(deserializerList.getClass().getSimpleName(), StdDelegatingDeserializer.class.getSimpleName());
 assertEquals(deserializerList.getClass().getSuperclass().getSimpleName(), JavaUtilCollectionsConverter.class.getSimpleName());
 assertEquals(list.size(), 1);
 assertEquals(deserializerList.deserialize(mapper.getDeserializationContext(), resultList), list);
 }
}