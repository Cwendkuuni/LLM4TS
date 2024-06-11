package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

 private static ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testFindForCollectionSingletonSet() throws Exception {
 JavaType type = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
 JsonNode node = mapper.readTree("{ \"class\": \"java.util.Collections$SingletonSet\" }");
 ObjectNode objectNode = (ObjectNode) node;
 Object deserialized = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type).deserialize(mapper, objectNode, null);
 Set<Boolean> expected = Collections.singleton(Boolean.TRUE);
 assertEquals(expected, deserialized);
 }

 @Test
 public void testFindForCollectionSingletonList() throws Exception {
 JavaType type = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
 JsonNode node = mapper.readTree("{ \"class\": \"java.util.Collections$SingletonList\" }");
 ObjectNode objectNode = (ObjectNode) node;
 Object deserialized = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type).deserialize(mapper, objectNode, null);
 List<Boolean> expected = Collections.singletonList(Boolean.TRUE);
 assertEquals(expected, deserialized);
 }

 @Test
 public void testFindForCollectionSingletonMap() throws Exception {
 JavaType type = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
 JsonNode node = mapper.readTree("{ \"class\": \"java.util.Collections$SingletonMap\" }");
 ObjectNode objectNode = (ObjectNode) node;
 Object deserialized = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), type).deserialize(mapper, objectNode, null);
 Map<String, String> expected = Collections.singletonMap("a", "b");
 assertEquals(expected, deserialized);
 }

 @Test
 public void testFindForCollectionUnmodifiableSet() throws Exception {
 JavaType type = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
 Set<Boolean> set = Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE));
 Object deserialized = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type).deserialize(mapper, mapper.valueToTree(set), null);
 Set<Boolean> expected = Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE));
 assertEquals(expected, deserialized);
 }

 @Test
 public void testFindForCollectionUnmodifiableList() throws Exception {
 JavaType type = TypeReference.collectionType(List.class, Boolean.TYPE);
 List<Boolean> list = Collections.unmodifiableList(Collections.singletonList(Boolean.TRUE));
 Object deserialized = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type).deserialize(mapper, mapper.valueToTree(list), null);
 List<Boolean> expected = Collections.unmodifiableList(Collections.singletonList(Boolean.TRUE));
 assertEquals(expected, deserialized);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForCollectionUnsupportedType() throws Exception {
 JavaType type = TypeFactory.defaultInstance().constructType(Integer.TYPE);
 Object deserialized = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type).deserialize(mapper, mapper.createValueNode("{ \"class\": \"java.util.Collections$SingletonMap\" }"), null);
 fail();
 }

 @Test
 public void testFindForMapSingletonMap() throws Exception {
 JavaType type = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
 JsonNode node = mapper.readTree("{ \"class\": \"java.util.Collections$SingletonMap\" }");
 ObjectNode objectNode = (ObjectNode) node;
 Object deserialized = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), type).deserialize(mapper, objectNode, null);
 Map<String, String> expected = Collections.singletonMap("a", "b");
 assertEquals(expected, deserialized);
 }

 @Test
 public void testFindForMapUnmodifiableMap() throws Exception {
 JavaType type = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
 Map<String, String> map = Collections.unmodifiableMap(Collections.singletonMap("a", "b"));
 Object deserialized = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), type).deserialize(mapper, mapper.valueToTree(map), null);
 Map<String, String> expected = Collections.unmodifiableMap(Collections.singletonMap("a", "b"));
 assertEquals(expected, deserialized);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForMapUnsupportedType() throws Exception {
 JavaType type = TypeReference.type(List.class);
 Object deserialized = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), type).deserialize(mapper, mapper.createValueNode("{ \"class\": \"java.util.Collections$SingletonMap\" }"), null);
 fail();
 }
}