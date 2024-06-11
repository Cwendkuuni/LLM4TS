package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.ser.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer.StdSerializers;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AnyGetterWriterTest {

 private ObjectMapper objectMapper;
 private AnyGetterWriter anyGetterWriter;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 AnnotatedMember accessor = new AnnotatedMember(MyTestClass.class.getDeclaredField("anyMap"), null);
 BeanProperty property = new BeanProperty("anyMap", accessor);
 MapSerializer mapSerializer = new MapSerializer(StdSerializers.EmptyArraySerializer.instance, ObjectMapper.DEFAULT_MAP_SERIALIZER);
 anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
 }

 @Test
 public void testGetAndSerialize() throws Exception {
 Object bean = new MyTestClass();
 Map<String, Object> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 MyTestClass.setAnyMap(bean, map);

 JsonGenerator gen = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), objectMapper.getOutputFactory());
 SerializerProvider provider = objectMapper.getSerializerProvider();
 anyGetterWriter.getAndSerialize(bean, gen, provider);
 gen.close();

 // Check serialized output
 byte[] serializedOutput = ((ByteArrayOutputStream) gen.getOutputStream()).toByteArray();
 String expectedOutput = "{\"key1\":\"value1\",\"key2\":\"value2\"}";
 assertEquals(expectedOutput, new String(serializedOutput, "UTF-8"));
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeNullMap() throws Exception {
 Object bean = new MyTestClass();
 MyTestClass.setAnyMap(bean, null);
 anyGetterWriter.getAndSerialize(bean, null, null);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeNonMapType() throws Exception {
 Object bean = new MyTestClass();
 String nonMapType = "nonMapType";
 MyTestClass.setAnyMap(bean, nonMapType);
 anyGetterWriter.getAndSerialize(bean, null, null);
 }

 @Test
 public void testGetAndFilter() throws Exception {
 Object bean = new MyTestClass();
 Map<String, Object> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 map.put("key3", "value3"); // This key/value should be filtered out
 MyTestClass.setAnyMap(bean, map);

 PropertyFilter filter = new Filter();
 JsonGenerator gen = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), objectMapper.getOutputFactory());
 SerializerProvider provider = objectMapper.getSerializerProvider();
 anyGetterWriter.getAndFilter(bean, gen, provider, filter);
 gen.close();

 // Check serialized output
 byte[] serializedOutput = ((ByteArrayOutputStream) gen.getOutputStream()).toByteArray();
 String expectedOutput = "{\"key1\":\"value1\"}";
 assertEquals(expectedOutput, new String(serializedOutput, "UTF-8"));
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterNullMap() throws Exception {
 Object bean = new MyTestClass();
 MyTestClass.setAnyMap(bean, null);
 anyGetterWriter.getAndFilter(bean, null, null, null);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterNonMapType() throws Exception {
 Object bean = new MyTestClass();
 String nonMapType = "nonMapType";
 MyTestClass.setAnyMap(bean, nonMapType);
 anyGetterWriter.getAndFilter(bean, null, null, null);
 }

 @Test
 public void testResolve() throws Exception {
 MyTestClass bean = new MyTestClass();
 Map<String, Object> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 MyTestClass.setAnyMap(bean, map);

 SerializerProvider provider = objectMapper.getSerializerProvider();
 anyGetterWriter.resolve(provider);
 ObjectMapper resolvedMapper = (ObjectMapper) provider.getConfig();
 MapSerializer mapSerializer = (MapSerializer) resolvedMapper.getSerializerProvider().getSerializer(Map.class);
 assertNotNull(mapSerializer);
 assertEquals("AnyGetterWriter", mapSerializer.getSerializerName());
 }
}

class MyTestClass {
 private Map<String, Object> anyMap;

 public static void setAnyMap(MyTestClass instance, Map<String, Object> map) {
 instance.anyMap = map;
 }

 // Getter for the "anyMap" (Map<String, Object>) property
 @JsonAnyGetter
 public Map<String, Object> getAnyMap() {
 return anyMap;
 }
}

class Filter {
 // Implement your custom filter logic here, if needed
}