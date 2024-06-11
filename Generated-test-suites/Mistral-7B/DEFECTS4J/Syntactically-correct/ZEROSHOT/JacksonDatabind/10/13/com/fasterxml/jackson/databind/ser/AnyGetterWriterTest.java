package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.ser.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer.StdSerializer;

public class AnyGetterWriterTest {

 private ObjectMapper objectMapper;
 private AnyGetterWriter anyGetterWriter;

 @Before
 public void setUp() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 BeanProperty property = mapper.findProperty("anyMap", Map.class, TestClass.class);
 AnnotatedMember accessor = mapper.findMember("getAnyMap", TestClass.class);
 MapSerializer mapSerializer = mapper.getSerializationConfig().findValueSerializer(Map.class);
 anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
 objectMapper = mapper;
 }

 @Test
 public void testGetAndSerialize() throws JsonMappingException, JsonProcessingException {
 Object bean = new TestClass();
 TestClass testClass = new TestClass();
 testClass.anyMap.put("key1", "value1");
 testClass.anyMap.put("key2", "value2");

 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 anyGetterWriter.getAndSerialize(bean, gen, objectMapper.getSerializerProvider());

 Map<String, Object> expectedMap = new HashMap<>();
 expectedMap.put("key1", "value1");
 expectedMap.put("key2", "value2");
 Map<String, Object> actualMap = mapper.readValue(System.out, Map.class);
 assertEquals(expectedMap, actualMap);
 }

 @Test
 public void testGetAndFilter() throws JsonMappingException, JsonProcessingException {
 Object bean = new TestClass();
 TestClass testClass = new TestClass();
 testClass.anyMap.put("key1", "value1");
 testClass.anyMap.put("key2", "value2");

 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 SerializerProvider provider = objectMapper.getSerializerProvider();
 PropertyFilter filter = new PropertyFilter() {
 @Override
 public boolean includes(Object beanProperty, Object value) {
 return "value1".equals(value);
 }
 };
 anyGetterWriter.getAndFilter(bean, gen, provider, filter);

 Map<String, Object> expectedMap = new HashMap<>();
 expectedMap.put("key1", "value1");
 Map<String, Object> actualMap = mapper.readValue(System.out, Map.class);
 assertEquals(expectedMap, actualMap);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidValueType() throws JsonMappingException, JsonProcessingException {
 Object bean = new TestClass();
 anyGetterWriter.getAndSerialize(bean, objectMapper.getFactory().createGenerator(System.out), objectMapper.getSerializerProvider());
 }

 @Test
 public void testResolve() throws JsonMappingException {
 SerializerProvider provider = objectMapper.getSerializerProvider();
 anyGetterWriter.resolve(provider);
 assertNotNull(anyGetterWriter._mapSerializer);
 StdSerializer<?> mapSerializer = (StdSerializer<?>) anyGetterWriter._mapSerializer;
 assertEquals("MapSerializer", mapSerializer.getClass().getSimpleName());
 }

 static class TestClass {
 @JsonAnyGetter
 public Map<String, String> getAnyMap() {
 return anyMap;
 }

 private Map<String, String> anyMap = new HashMap<>();
 }
}