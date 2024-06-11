package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testFindForCollectionSingletonSet() throws Exception {
 Set<Boolean> set = new HashSet<Boolean>() {{
 add(Boolean.TRUE);
 }} ;
 JavaType type = mapper.getTypeFactory().constructType(Set.class, Boolean.class);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);
 Object result = mapper.readValue("[]", type);
 assertEquals(Collections.singleton(Boolean.TRUE), result);
 }

 @Test
 public void testFindForCollectionSingletonList() throws Exception {
 List<Boolean> list = new ArrayList<Boolean>() {{
 add(Boolean.TRUE);
 }} ;
 JavaType type = mapper.getTypeFactory().constructType(List.class, Boolean.class);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);
 Object result = mapper.readValue("[]", type);
 assertEquals(Collections.singletonList(Boolean.TRUE), result);
 }

 @Test
 public void testFindForCollectionSingletonMap() throws Exception {
 Map<String, String> map = new HashMap<String, String>() {{
 put("a", "b");
 }} ;
 JavaType type = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);
 Object result = mapper.readValue("{}", type);
 assertEquals(Collections.singletonMap("a", "b"), result);
 }

 @Test
 public void testFindForCollectionUnmodifiableSet() throws Exception {
 Set<Boolean> set = Collections.unmodifiableSet(new HashSet<Boolean>() {{
 add(Boolean.TRUE);
 }} );
 JavaType type = mapper.getTypeFactory().constructType(Set.class, Boolean.class);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);
 Object result = mapper.readValue("[]", type);
 assertEquals(Collections.unmodifiableSet(set), result);
 }

 @Test
 public void testFindForCollectionUnmodifiableList() throws Exception {
 List<Boolean> list = Collections.unmodifiableList(new ArrayList<Boolean>() {{
 add(Boolean.TRUE);
 }} );
 JavaType type = mapper.getTypeFactory().constructType(List.class, Boolean.class);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);
 Object result = mapper.readValue("[]", type);
 assertEquals(Collections.unmodifiableList(list), result);
 }

 @Test
 public void testFindForCollectionUnmodifiableMap() throws Exception {
 Map<String, String> map = Collections.unmodifiableMap(new HashMap<String, String>() {{
 put("a", "b");
 }} );
 JavaType type = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);
 Object result = mapper.readValue("{}", type);
 assertEquals(Collections.unmodifiableMap(map), result);
 }

 @Test
 public void testFindForCollectionNull() throws Exception {
 JavaType type = mapper.getTypeFactory().constructType(List.class, String.class);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);
 Object result = mapper.readValue("null", type);
 assertNull(result);
 }

 @Test
 public void testFindForCollectionInvalidType() throws Exception {
 JavaType type = new TypeReference<Object>() {}.getType();
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);
 assertNull(deserializer);
 }

 @Test
 public void testFindForMapSingletonMap() throws Exception {
 Map<String, String> map = new HashMap<String, String>() {{
 put("a", "b");
 }} ;
 JavaType type = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), type);
 Object result = mapper.readValue("{}", type);
 assertEquals(Collections.singletonMap("a", "b"), result);
 }

 @Test
 public void testFindForMapUnmodifiableMap() throws Exception {
 Map<String, String> map = Collections.unmodifiableMap(new HashMap<String, String>() {{
 put("a", "b");
 }} );
 JavaType type = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), type);
 Object result = mapper.readValue("{}", type);
 assertEquals(Collections.unmodifiableMap(map), result);
 }

 @Test
 public void testFindForMapNull() throws Exception {
 JavaType type = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), type);
 Object result = mapper.readValue("null", type);
 assertNull(result);
 }

 @Test
 public void testFindForMapInvalidType() throws Exception {
 JavaType type = new TypeReference<Object>() {}.getType();
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), type);
 assertNull(deserializer);
 }

 @Test
 public void testConverterSingletonSet() throws Exception {
 Set<Boolean> set = new HashSet<Boolean>() {{
 add(Boolean.TRUE);
 }} ;
 JavaType inputType = mapper.getTypeFactory().constructType(Set.class, Boolean.class);
 JavaType outputType = mapper.getTypeFactory().constructType(Map.class, Object.class, Object.class);
 JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(1, inputType, Set.class);
 Object result = converter.convert(set);
 assertEquals(Collections.singletonMap("class com.fasterxml.jackson.core.type.TypeReference$1", set), result);
 }

 @Test
 public void testConverterSingletonList() throws Exception {
 List<Boolean> list = new ArrayList<Boolean>() {{
 add(Boolean.TRUE);
 }} ;
 JavaType inputType = mapper.getTypeFactory().constructType(List.class, Boolean.class);
 JavaType outputType = mapper.getTypeFactory().constructType(Map.class, Object.class, Object.class);
 JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(2, inputType, List.class);
 Object result = converter.convert(list);
 assertEquals(Collections.singletonMap("class java.util.ArrayList", list), result);
 }

 @Test
 public void testConverterSingletonMap() throws Exception {
 Map<String, String> map = new HashMap<String, String>() {{
 put("a", "b");
 }} ;
 JavaType inputType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
 JavaType outputType = mapper.getTypeFactory().constructType(Map.class, Object.class, Object.class);
 JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(3, inputType, Map.class);
 Object result = converter.convert(map);
 assertEquals(Collections.singletonMap("class java.util.HashMap", map), result);
 }

 @Test
 public void testConverterUnmodifiableSet() throws Exception {
 Set<Boolean> set = Collections.unmodifiableSet(new HashSet<Boolean>() {{
 add(Boolean.TRUE);
 }} );
 JavaType inputType = mapper.getTypeFactory().constructType(Set.class, Boolean.class);
 JavaType outputType = mapper.getTypeFactory().constructType(Map.class, Object.class, Object.class);
 JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(1, inputType, Set.class);
 Object result = converter.convert(set);
 assertEquals(Collections.unmodifiableMap("class com.fasterxml.jackson.core.type.TypeReference$1", set), result);
 }

 @Test
 public void testConverterUnmodifiableList() throws Exception {
 List<Boolean> list = Collections.unmodifiableList(new ArrayList<Boolean>() {{
 add(Boolean.TRUE);
 }} );
 JavaType inputType = mapper.getTypeFactory().constructType(List.class, Boolean.class);
 JavaType outputType = mapper.getTypeFactory().constructType(Map.class, Object.class, Object.class);
 JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(5, inputType, List.class);
 Object result = converter.convert(list);
 assertEquals(Collections.unmodifiableMap("class java.util.ArrayList", list), result);
 }

 @Test
 public void testConverterUnmodifiableMap() throws Exception {
 Map<String, String> map = Collections.unmodifiableMap(new HashMap<String, String>() {{
 put("a", "b");
 }} );
 JavaType inputType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
 JavaType outputType = mapper.getTypeFactory().constructType(Map.class, Object.class, Object.class);
 JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(6, inputType, Map.class);
 Object result = converter.convert(map);
 assertEquals(Collections.unmodifiableMap("class java.util.HashMap", map), result);
 }

 @Test
 public void testConverterAsList() throws Exception {
 List<Boolean> list = new ArrayList<Boolean>() {{
 add(Boolean.TRUE);
 }} ;
 JavaType inputType = mapper.getTypeFactory().constructType(List.class, Boolean.class);
 JavaType outputType = mapper.getTypeFactory().constructType(List.class, Boolean.class);
 JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(7, inputType, List.class);
 Object result = converter.convert(list);
 assertEquals(list, result);
 }
}