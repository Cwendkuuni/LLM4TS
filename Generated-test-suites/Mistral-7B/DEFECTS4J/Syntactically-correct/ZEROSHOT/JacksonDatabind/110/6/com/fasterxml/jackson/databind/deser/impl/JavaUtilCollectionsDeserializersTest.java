package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdValueDocumentDeserializer;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class JavaUtilCollectionsDeserializersTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testFindForCollection_SingletonSet() throws Exception {
 final Set<Boolean> set = Collections.singleton(Boolean.TRUE);
 JavaType javaType = mapper.getTypeFactory().constructType(Set.class, Boolean.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
 Object readValue = mapper.readValue(mapper.createParser(new StdValueDocumentDeserializer().getEmptyValueAsString()), javaType);
 assertEquals(deserializer, readValue.getClass());
 assertEquals(set, readValue);
 }

 @Test
 public void testFindForCollection_SingletonList() throws Exception {
 final List<Boolean> list = Collections.singletonList(Boolean.TRUE);
 JavaType javaType = mapper.getTypeFactory().constructType(List.class, Boolean.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
 Object readValue = mapper.readValue(mapper.createParser(new StdValueDocumentDeserializer().getEmptyValueAsString()), javaType);
 assertEquals(deserializer, readValue.getClass());
 assertEquals(list, readValue);
 }

 @Test
 public void testFindForCollection_SingletonMap() throws Exception {
 final Map<String, String> map = Collections.singletonMap("a", "b");
 JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
 Object readValue = mapper.readValue(mapper.createParser(new StdValueDocumentDeserializer().getEmptyValueAsString()), javaType);
 assertEquals(deserializer, readValue.getClass());
 assertEquals(map, readValue);
 }

 @Test
 public void testFindForCollection_UnmodifiableSet() throws Exception {
 final Set<Boolean> set = Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE));
 JavaType javaType = mapper.getTypeFactory().constructType(Set.class, Boolean.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
 Object readValue = mapper.readValue(mapper.createParser(new StdValueDocumentDeserializer().getEmptyValueAsString()), javaType);
 assertEquals(deserializer, readValue.getClass());
 assertEquals(set, readValue);
 }

 @Test
 public void testFindForCollection_UnmodifiableList() throws Exception {
 final List<Boolean> list = Collections.unmodifiableList(Collections.singletonList(Boolean.TRUE));
 JavaType javaType = mapper.getTypeFactory().constructType(List.class, Boolean.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
 Object readValue = mapper.readValue(mapper.createParser(new StdValueDocumentDeserializer().getEmptyValueAsString()), javaType);
 assertEquals(deserializer, readValue.getClass());
 assertEquals(list, readValue);
 }

 @Test
 public void testFindForCollection_UnmodifiableMap() throws Exception {
 final Map<String, String> map = Collections.unmodifiableMap(Collections.singletonMap("a", "b"));
 JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
 Object readValue = mapper.readValue(mapper.createParser(new StdValueDocumentDeserializer().getEmptyValueAsString()), javaType);
 assertEquals(deserializer, readValue.getClass());
 assertEquals(map, readValue);
 }

 @Test
 public void testFindForMap() throws Exception {
 final Map<String, String> map = Collections.singletonMap("a", "b");
 JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), javaType);
 Object readValue = mapper.readValue(mapper.createParser(new StdValueDocumentDeserializer().getEmptyValueAsString()), javaType);
 assertEquals(deserializer, readValue.getClass());
 assertEquals(map, readValue);
 }

 @Test
 public void testFindForMap_UnmodifiableMap() throws Exception {
 final Map<String, String> map = Collections.unmodifiableMap(Collections.singletonMap("a", "b"));
 JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), javaType);
 Object readValue = mapper.readValue(mapper.createParser(new StdValueDocumentDeserializer().getEmptyValueAsString()), javaType);
 assertEquals(deserializer, readValue.getClass());
 assertEquals(map, readValue);
 }
}