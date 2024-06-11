package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testFindForCollectionSingletonSet() throws Exception {
 JavaType setType = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
 JsonNode input = mapper.readTree("{ \"class\": \"java.util.Collections$SingletonSet\", \"elements\": [true] }");
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), setType);
 Object result = mapper.readerForUpdating(Collections.emptySet()).readValue(mapper.getFactory().constructValueTypeFromInputs(Set.class, input));
 assertEquals(Collections.singleton(Boolean.TRUE), result);
 }

 @Test
 public void testFindForCollectionSingletonList() throws Exception {
 JavaType listType = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
 JsonNode input = mapper.readTree("{ \"class\": \"java.util.Collections$SingletonList\", \"elements\": [true] }");
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), listType);
 Object result = mapper.readerForUpdating(Collections.emptyList()).readValue(mapper.getFactory().constructValueTypeFromInputs(List.class, input));
 assertEquals(Collections.singletonList(Boolean.TRUE), result);
 }

 @Test
 public void testFindForCollectionSingletonMap() throws Exception {
 JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
 JsonNode input = mapper.readTree("{ \"class\": \"java.util.Collections$SingletonMap\", \"value\": { \"key\": \"a\", \"value\": \"b\" } }");
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), mapType);
 Object result = mapper.readerForUpdating(Collections.emptyMap()).readValue(mapper.getFactory().constructValueTypeFromInputs(Map.class, input));
 assertEquals(Collections.singletonMap("a", "b"), result);
 }

 @Test
 public void testFindForCollectionUnmodifiableSet() throws Exception {
 JavaType setType = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
 Set<Boolean> set = Collections.singleton(Boolean.TRUE);
 JsonNode input = mapper.valueToTree(set);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), setType);
 Object result = mapper.readerForUpdating(Collections.unmodifiableSet(Collections.emptySet())).readValue(mapper.getFactory().constructValueTypeFromInputs(Set.class, input));
 assertSame(set, result);
 }

 @Test
 public void testFindForCollectionUnmodifiableList() throws Exception {
 JavaType listType = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
 List<Boolean> list = Collections.singletonList(Boolean.TRUE);
 JsonNode input = mapper.valueToTree(list);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), listType);
 Object result = mapper.readerForUpdating(Collections.unmodifiableList(Collections.emptyList())).readValue(mapper.getFactory().constructValueTypeFromInputs(List.class, input));
 assertSame(list, result);
 }

 @Test
 public void testFindForCollectionUnmodifiableMap() throws Exception {
 JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
 Map<String, String> map = Collections.singletonMap("a", "b");
 JsonNode input = mapper.valueToTree(map);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), mapType);
 Object result = mapper.readerForUpdating(Collections.unmodifiableMap(Collections.emptyMap())).readValue(mapper.getFactory().constructValueTypeFromInputs(Map.class, input));
 assertSame(map, result);
 }

 @Test
 public void testFindForCollectionAsList() throws Exception {
 JavaType listType = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
 List<Boolean> inputList = Arrays.asList(Boolean.TRUE);
 JsonNode input = mapper.valueToTree(inputList);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), listType);
 Object result = mapper.readerForUpdating(inputList).readValue(mapper.getFactory().constructValueTypeFromInputs(List.class, input));
 assertSame(inputList, result);
 }

 @Test
 public void testFindForMapSingletonMap() throws Exception {
 JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
 JsonNode input = mapper.readTree("{ \"class\": \"java.util.Collections$SingletonMap\", \"value\": { \"key\": \"a\", \"value\": \"b\" } }");
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), mapType);
 Object result = mapper.readerForUpdating(Collections.emptyMap()).readValue(mapper.getFactory().constructValueTypeFromInputs(Map.class, input));
 assertEquals(Collections.singletonMap("a", "b"), result);
 }

 @Test
 public void testFindForMapUnmodifiableMap() throws Exception {
 JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
 Map<String, String> map = Collections.singletonMap("a", "b");
 JsonNode input = mapper.valueToTree(map);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), mapType);
 Object result = mapper.readerForUpdating(Collections.unmodifiableMap(map)).readValue(mapper.getFactory().constructValueTypeFromInputs(Map.class, input));
 assertSame(map, result);
 }

}