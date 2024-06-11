package com.fasterxml.jackson.databind.deser.impl.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class JavaUtilCollectionsDeserializersTest {

 private ObjectMapper mapper;

 @Before
 public void setUp() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testFindForCollection_SingletonSet() throws JsonProcessingException {
 Set<Boolean> expectedSet = new HashSet<>(Arrays.asList(true));
 JavaType javaTypeSet = mapper.getTypeFactory().constructType(Set.class, Boolean.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaTypeSet);
 String jsonSet = "{\"class\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$JavaUtilCollectionsConverter\",\"kind\":1,\"inputType\":{\"rawClass\":\"java.util.HashSet\"}}";
 Object actual = mapper.readValue(jsonSet, Object.class, deserializer);
 assertEquals(expectedSet, actual);
 }

 @Test
 public void testFindForCollection_SingletonList() throws JsonProcessingException {
 List<Boolean> expectedList = Arrays.asList(true);
 JavaType javaTypeList = mapper.getTypeFactory().constructType(List.class, Boolean.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaTypeList);
 String jsonList = "{\"class\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$JavaUtilCollectionsConverter\",\"kind\":2,\"inputType\":{\"rawClass\":\"java.util.ArrayList\"}}";
 Object actual = mapper.readValue(jsonList, Object.class, deserializer);
 assertEquals(expectedList, actual);
 }

 @Test
 public void testFindForCollection_SingleMap() throws JsonProcessingException {
 Map<String, String> expectedMap = new HashMap<>();
 expectedMap.put("key", "value");
 JavaType javaTypeMap = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaTypeMap);
 String jsonMap = "{\"class\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$JavaUtilCollectionsConverter\",\"kind\":3,\"inputType\":{\"rawClass\":\"java.util.LinkedHashMap\"}}";
 Object actual = mapper.readValue(jsonMap, Object.class, deserializer);
 assertEquals(expectedMap, actual);
 }

 @Test
 public void testFindForCollection_UnmodifiableSet() throws JsonProcessingException {
 Set<Boolean> expectedSet = Collections.unmodifiableSet(Arrays.asList(true));
 JavaType javaTypeSet = mapper.getTypeFactory().constructType(Set.class, Boolean.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaTypeSet);
 String jsonSet = "{\"class\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$JavaUtilCollectionsConverter\",\"kind\":4,\"inputType\":{\"rawClass\":\"java.util.HashSet\"}}";
 Object actual = mapper.readValue(jsonSet, Object.class, deserializer);
 assertEquals(expectedSet, actual);
 }

 @Test
 public void testFindForCollection_UnmodifiableList() throws JsonProcessingException {
 List<Boolean> expectedList = Collections.unmodifiableList(Arrays.asList(true));
 JavaType javaTypeList = mapper.getTypeFactory().constructType(List.class, Boolean.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaTypeList);
 String jsonList = "{\"class\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$JavaUtilCollectionsConverter\",\"kind\":5,\"inputType\":{\"rawClass\":\"java.util.ArrayList\"}}";
 Object actual = mapper.readValue(jsonList, Object.class, deserializer);
 assertEquals(expectedList, actual);
 }

 @Test
 public void testFindForCollection_UnmodifiableMap() throws JsonProcessingException {
 Map<String, String> expectedMap = Collections.unmodifiableMap(new HashMap<String, String>() {{ put("key", "value"); }});
 JavaType javaTypeMap = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaTypeMap);
 String jsonMap = "{\"class\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$JavaUtilCollectionsConverter\",\"kind\":6,\"inputType\":{\"rawClass\":\"java.util.LinkedHashMap\"}}";
 Object actual = mapper.readValue(jsonMap, Object.class, deserializer);
 assertEquals(expectedMap, actual);
 }

 @Test (expected = JsonMappingException.class)
 public void testFindForCollection_InvalidType() throws JsonProcessingException {
 JavaType javaTypeInvalid = mapper.getTypeFactory().constructType(String.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaTypeInvalid);
 String jsonInvalid = "{\"class\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$JavaUtilCollectionsConverter\",\"kind\":0,\"inputType\":{\"rawClass\":\"java.lang.String\"}}";
 mapper.readValue(jsonInvalid, Object.class, deserializer);
 }
}