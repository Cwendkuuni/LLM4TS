package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonAnyGetter;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;

public class AnyGetterWriterTest {

 private ObjectMapper objectMapper;
 private AnyGetterWriter anyGetterWriter;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 BeanProperty property = BeanUtil.findProperty(TestClass.class, "mapProperty");
 AnnotatedMember accessor = BeanUtil.findGetter(TestClass.class, "getMapProperty");
 MapSerializer mapSerializer = objectMapper.getSerializerProvider().findValueSerializer(Map.class);
 anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
 }

 @Test
 public void testGetAndSerialize() throws JsonGenerationException, IOException {
 Object bean = new TestClass();
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 anyGetterWriter.getAndSerialize(bean, gen, objectMapper.getSerializerProvider());
 Map<String, String> expectedMap = new HashMap<>();
 expectedMap.put("key1", "value1");
 expectedMap.put("key2", "value2");
 Map<String, String> actualMap = new HashMap<>();
 gen.readValueAs(Map.class);
 assertEquals(expectedMap, actualMap);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNullValue() throws JsonGenerationException, IOException {
 Object bean = new TestClass();
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 anyGetterWriter.getAndSerialize(bean, gen, objectMapper.getSerializerProvider());
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNonMapValue() throws JsonGenerationException, IOException {
 Object bean = new TestClassWithNonMapValue();
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 anyGetterWriter.getAndSerialize(bean, gen, objectMapper.getSerializerProvider());
 }

 @Test
 public void testGetAndFilter() throws JsonGenerationException, IOException {
 Object bean = new TestClass();
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 PropertyFilter filter = new Filter();
 anyGetterWriter.getAndFilter(bean, gen, objectMapper.getSerializerProvider(), filter);
 Map<String, String> expectedMap = new HashMap<>();
 expectedMap.put("key2", "value2");
 Map<String, String> actualMap = new HashMap<>();
 gen.readValueAs(Map.class);
 assertEquals(expectedMap, actualMap);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterWithNullValue() throws JsonGenerationException, IOException {
 Object bean = new TestClass();
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 PropertyFilter filter = new Filter();
 anyGetterWriter.getAndFilter(bean, gen, objectMapper.getSerializerProvider(), filter);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterWithNonMapValue() throws JsonGenerationException, IOException {
 Object bean = new TestClassWithNonMapValue();
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 PropertyFilter filter = new Filter();
 anyGetterWriter.getAndFilter(bean, gen, objectMapper.getSerializerProvider(), filter);
 }

 @Test
 public void testResolve() throws JsonMappingException {
 SerializerProvider provider = objectMapper.getSerializerProvider();
 anyGetterWriter.resolve(provider);
 assertNotNull(anyGetterWriter._mapSerializer);
 }

}

class TestClass {

 @JsonAnyGetter
 public Map<String, String> getMapProperty() {
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 return map;
 }

}

class TestClassWithNonMapValue {

 public Object getNonMapProperty() {
 return new Object();
 }

}

class Filter {

 public boolean include(String name) {
 return name.equals("key2");
 }

}