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

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testFindForCollectionSingletonList() throws Exception {
 List<Boolean> singletonList = Arrays.asList(Boolean.TRUE);
 JavaType javaType = MAPPER.getTypeFactory().constructType(List.class, Boolean.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(MAPPER.getDeserializationContext(), javaType);
 assertNotNull(deserializer);
 Object readValue = MAPPER.readValue(new String("[]"), javaType, deserializer);
 assertEquals(singletonList, readValue);
 }

 @Test
 public void testFindForCollectionSingletonSet() throws Exception {
 Set<Boolean> singletonSet = Collections.singleton(Boolean.TRUE);
 JavaType javaType = MAPPER.getTypeFactory().constructType(Set.class, Boolean.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(MAPper.getDeserializationContext(), javaType);
 assertNotNull(deserializer);
 Object readValue = MAPPER.readValue(new String("{}"), javaType, deserializer);
 assertEquals(singletonSet, readValue);
 }

 @Test
 public void testFindForCollectionUnmodifiableList() throws Exception {
 List<Boolean> unmodifiableList = Collections.unmodifiableList(Arrays.asList(Boolean.TRUE));
 JavaType javaType = MAPPER.getTypeFactory().constructType(List.class, Boolean.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(MAPPER.getDeserializationContext(), javaType);
 assertNotNull(deserializer);
 Object readValue = MAPPER.readValue(new String("[]"), javaType, deserializer);
 assertEquals(unmodifiableList, readValue);
 }

 @Test
 public void testFindForCollectionUnmodifiableSet() throws Exception {
 Set<Boolean> unmodifiableSet = Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE));
 JavaType javaType = MAPPER.getTypeFactory().constructType(Set.class, Boolean.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(MAPPER.getDeserializationContext(), javaType);
 assertNotNull(deserializer);
 Object readValue = MAPPER.readValue(new String("{}"), javaType, deserializer);
 assertEquals(unmodifiableSet, readValue);
 }

 @Test
 public void testFindForMapSingletonMap() throws Exception {
 Map<String, String> singletonMap = new HashMap<String, String>() {{
 put("key", "value");
 }};
 JavaType javaType = MAPPER.getTypeFactory().constructType(Map.class, String.class, String.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(MAPPER.getDeserializationContext(), javaType);
 assertNotNull(deserializer);
 Object readValue = MAPPER.readValue(new String("{}"), javaType, deserializer);
 assertEquals(singletonMap, readValue);
 }

 @Test
 public void testFindForMapUnmodifiableMap() throws Exception {
 Map<String, String> unmodifiableMap = Collections.unmodifiableMap(singletonMap);
 JavaType javaType = MAPPER.getTypeFactory().constructType(Map.class, String.class, String.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(MAPPER.getDeserializationContext(), javaType);
 assertNotNull(deserializer);
 Object readValue = MAPPER.readValue(new String("{}"), javaType, deserializer);
 assertEquals(unmodifiableMap, readValue);
 }

 @Test
 public void testFindForUnknownType() throws Exception {
 JavaType javaType = MAPPER.getTypeFactory().constructType(String.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(MAPPER.getDeserializationContext(), javaType);
 assertNull(deserializer);
 }
}