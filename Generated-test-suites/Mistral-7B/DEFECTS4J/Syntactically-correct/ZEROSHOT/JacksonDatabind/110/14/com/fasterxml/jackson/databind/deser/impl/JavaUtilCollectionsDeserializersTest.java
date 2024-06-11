package com.example.junit;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

 private static ObjectMapper mapper;

 @BeforeClass
 public static void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testFindForCollectionSingletonList() throws JsonMappingException {
 JavaType javaType = mapper.getTypeFactory().constructType(List.class, Boolean.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);

 List<Boolean> expected = Arrays.asList(Boolean.TRUE);
 List<Boolean> actual = mapper.readValue(Arrays.toString(new Object[] { expected }).getBytes(), deserializer.getType());

 assertEquals(expected, actual);
 }

 @Test
 public void testFindForCollectionSingletonSet() throws JsonMappingException {
 JavaType javaType = mapper.getTypeFactory().constructType(Set.class, Boolean.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);

 Set<Boolean> expected = Collections.singleton(Boolean.TRUE);
 Set<Boolean> actual = mapper.readValue(Arrays.toString(new Object[] { expected }).getBytes(), deserializer.getType());

 assertEquals(expected, actual);
 }

 @Test
 public void testFindForCollectionUnmodifiableList() throws JsonMappingException {
 JavaType javaType = mapper.getTypeFactory().constructType(List.class, Boolean.class);
 JavaType superClass = mapper.getTypeFactory().constructType(List.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);

 List<Boolean> expected = Collections.unmodifiableList(Arrays.asList(Boolean.TRUE));
 List<Boolean> actual = mapper.readValue(Arrays.toString(new Object[] { expected }).getBytes(), deserializer.getType());

 assertEquals(expected, actual);
 }

 @Test
 public void testFindForCollectionUnmodifiableSet() throws JsonMappingException {
 JavaType javaType = mapper.getTypeFactory().constructType(Set.class, Boolean.class);
 JavaType superClass = mapper.getTypeFactory().constructType(Set.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);

 Set<Boolean> expected = Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE));
 Set<Boolean> actual = mapper.readValue(Arrays.toString(new Object[] { expected }).getBytes(), deserializer.getType());

 assertEquals(expected, actual);
 }

 @Test
 public void testFindForMapSingletonMap() throws JsonMappingException {
 JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaType);

 Map<String, String> expected = Collections.singletonMap("a", "b");
 Map<String, String> actual = mapper.readValue(Arrays.toString(new Object[] { expected }).getBytes(), deserializer.getType());

 assertEquals(expected, actual);
 }

 @Test
 public void testFindForMapUnmodifiableMap() throws JsonMappingException {
 JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
 JavaType superClass = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaType);

 Map<String, String> expected = Collections.unmodifiableMap(Collections.singletonMap("a", "b"));
 Map<String, String> actual = mapper.readValue(Arrays.toString(new Object[] { expected }).getBytes(), deserializer.getType());

 assertEquals(expected, actual);
 }

}