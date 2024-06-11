package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

 private ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testFindForCollectionSingletonSet() throws JsonMappingException {
 JavaType javaTypeSet = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
 JavaType inputTypeSet = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_SET);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaTypeSet);
 assertEquals(Boolean.TRUE, deserializer.deserialize(new Object[] { Collections.singleton(Boolean.TRUE) }, javaTypeSet, objectMapper.getDeserializationContext()));
 }

 @Test
 public void testFindForCollectionSingletonList() throws JsonMappingException {
 JavaType javaTypeList = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
 JavaType inputTypeList = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_LIST);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaTypeList);
 assertEquals(Boolean.TRUE, deserializer.deserialize(new Object[] { Collections.singletonList(Boolean.TRUE) }, javaTypeList, objectMapper.getDeserializationContext()));
 }

 @Test
 public void testFindForCollectionSingletonMap() throws JsonMappingException {
 JavaType javaTypeMap = new TypeReference<Map<String, String>>() {}.getType();
 JavaType inputTypeMap = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaTypeMap);
 Map<String, String> map = new LinkedHashMap<>();
 map.put("key", "value");
 assertEquals(map, deserializer.deserialize(new Object[] { Collections.singletonMap("key", "value") }, javaTypeMap, objectMapper.getDeserializationContext()));
 }

 @Test
 public void testFindForCollectionUnmodifiableSet() throws JsonMappingException {
 JavaType javaTypeUnmodifiableSet = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
 JavaType inputTypeUnmodifiableSet = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_SET);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaTypeUnmodifiableSet);
 Set<Boolean> set = Collections.singleton(Boolean.TRUE);
 assertEquals(set, deserializer.deserialize(new Object[] { Collections.unmodifiableSet(set) }, javaTypeUnmodifiableSet, objectMapper.getDeserializationContext()));
 }

 @Test
 public void testFindForCollectionUnmodifiableList() throws JsonMappingException {
 JavaType javaTypeUnmodifiableList = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
 JavaType inputTypeUnmodifiableList = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_LIST);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaTypeUnmodifiableList);
 List<Boolean> list = Arrays.asList(Boolean.TRUE);
 assertEquals(list, deserializer.deserialize(new Object[] { Collections.unmodifiableList(list) }, javaTypeUnmodifiableList, objectMapper.getDeserializationContext()));
 }

 @Test
 public void testFindForCollectionUnmodifiableMap() throws JsonMappingException {
 JavaType javaTypeUnmodifiableMap = new TypeReference<Map<String, String>>() {}.getType();
 JavaType inputTypeUnmodifiableMap = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaTypeUnmodifiableMap);
 Map<String, String> map = Collections.singletonMap("key", "value");
 assertEquals(map, deserializer.deserialize(new Object[] { Collections.unmodifiableMap(map) }, javaTypeUnmodifiableMap, objectMapper.getDeserializationContext()));
 }

 @Test
 public void testFindForCollectionEmpty() throws JsonMappingException {
 JavaType javaTypeList = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaTypeList);
 assertNull(deserializer.deserialize(new Object[0], javaTypeList, objectMapper.getDeserializationContext()));
 }

 @Test
 public void testFindForMapSingletonMap() throws JsonMappingException {
 JavaType javaTypeMap = new TypeReference<Map<String, String>>() {}.getType();
 JavaType inputTypeMap = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(objectMapper.getDeserializationContext(), javaTypeMap);
 Map<String, String> map = new LinkedHashMap<>();
 map.put("key", "value");
 assertEquals(map, deserializer.deserialize(new Object[] { Collections.singletonMap("key", "value") }, javaTypeMap, objectMapper.getDeserializationContext()));
 }

 @Test
 public void testFindForMapUnmodifiableMap() throws JsonMappingException {
 JavaType javaTypeMap = new TypeReference<Map<String, String>>() {}.getType();
 JavaType inputTypeUnmodifiableMap = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(objectMapper.getDeserializationContext(), javaTypeMap);
 Map<String, String> map = Collections.singletonMap("key", "value");
 assertEquals(map, deserializer.deserialize(new Object[] { Collections.unmodifiableMap(map) }, javaTypeMap, objectMapper.getDeserializationContext()));
 }

 @Test
 public void testFindForMapEmpty() throws JsonMappingException {
 JavaType javaTypeMap = new TypeReference<Map<String, String>>() {}.getType();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(objectMapper.getDeserializationContext(), javaTypeMap);
 assertNull(deserializer.deserialize(new Object[0], javaTypeMap, objectMapper.getDeserializationContext()));
 }
}