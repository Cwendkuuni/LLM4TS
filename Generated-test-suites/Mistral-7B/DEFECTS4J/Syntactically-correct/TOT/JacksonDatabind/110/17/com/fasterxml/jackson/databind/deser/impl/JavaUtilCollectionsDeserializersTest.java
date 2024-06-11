package com.fasterxml.jackson.databind.deser.impl.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.*;

public class JavaUtilCollectionsDeserializersTest {

 private ObjectMapper mapper;

 @Before
 public void setUp() {
 mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testFindForCollection_SingletonList() throws JsonProcessingException {
 List list = Arrays.asList(1, 2, 3);
 JavaType javaType = mapper.getTypeFactory().constructType(ArrayList.class);
 JsonDeserializer deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);

 assertEquals(Arrays.asList(1), deserializer.deserialize(mapper, new int[]{1}));
 }

 @Test
 public void testFindForCollection_SingletonList_Empty() throws JsonProcessingException {
 List list = Collections.singletonList(Boolean.TRUE);
 JavaType javaType = mapper.getTypeFactory().constructType(ArrayList.class);
 JsonDeserializer deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);

 assertEquals(Collections.singletonList(Boolean.TRUE), deserializer.deserialize(mapper, new Object[]{null}));
 }

 @Test
 public void testFindForCollection_SingletonSet() throws JsonProcessingException {
 Set set = new HashSet() {{ add(Boolean.TRUE); }};
 JavaType javaType = mapper.getTypeFactory().constructType(HashSet.class);
 JsonDeserializer deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);

 assertEquals(Collections.singleton(Boolean.TRUE), deserializer.deserialize(mapper, new Object[]{null}));
 }

 @Test
 public void testFindForCollection_SingletonSet_Empty() throws JsonProcessingException {
 Set set = Collections.singleton(Boolean.TRUE);
 JavaType javaType = mapper.getTypeFactory().constructType(HashSet.class);
 JsonDeserializer deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);

 assertEquals(Collections.singleton(Boolean.TRUE), deserializer.deserialize(mapper, new Object[]{null}));
 }

 @Test
 public void testFindForCollection_UnmodifiableList() throws JsonProcessingException {
 List list = Collections.unmodifiableList(Arrays.asList(1, 2, 3));
 JavaType javaType = mapper.getTypeFactory().constructType(ArrayList.class);
 JsonDeserializer deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);

 assertNull(deserializer.deserialize(mapper, new Object[]{null}));
 }

 @Test
 public void testFindForCollection_UnmodifiableSet() throws JsonProcessingException {
 Set set = Collections.unmodifiableSet(new HashSet() {{ add(Boolean.TRUE); }});
 JavaType javaType = mapper.getTypeFactory().constructType(HashSet.class);
 JsonDeserializer deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);

 assertNull(deserializer.deserialize(mapper, new Object[]{null}));
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForCollection_UnknownType() throws JsonProcessingException {
 JavaType javaType = mapper.getTypeFactory().constructType(String.class);
 JsonDeserializer deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);

 deserializer.deserialize(mapper, new Object[]{null});
 }

 @Test
 public void testFindForMap_SingletonMap() throws JsonProcessingException {
 Map map = new HashMap() {{ put("key", "value"); }};
 JavaType javaType = mapper.getTypeFactory().constructType(HashMap.class);
 JsonDeserializer deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), javaType);

 assertEquals(Collections.singletonMap("key", "value"), deserializer.deserialize(mapper, new Object[]{null}));
 }

 @Test
 public void testFindForMap_SingletonMap_Empty() throws JsonProcessingException {
 Map map = Collections.singletonMap("key", "value");
 JavaType javaType = mapper.getTypeFactory().constructType(HashMap.class);
 JsonDeserializer deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.context, javaType);

 assertEquals(Collections.singletonMap("key", "value"), deserializer.deserialize(mapper, new Object[]{null}));
 }

 @Test
 public void testFindForMap_UnmodifiableMap() throws JsonProcessingException {
 Map map = Collections.unmodifiableMap(new HashMap() {{ put("key", "value"); }});
 JavaType javaType = mapper.getTypeFactory().constructType(HashMap.class);
 JsonDeserializer deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), javaType);

 assertNull(deserializer.deserialize(mapper, new Object[]{null}));
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForMap_UnknownType() throws JsonProcessingException {
 JavaType javaType = mapper.getTypeFactory().constructType(String.class);
 JsonDeserializer deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), javaType);

 deserializer.deserialize(mapper, new Object[]{null});
 }
}