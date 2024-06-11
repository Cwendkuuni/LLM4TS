package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonAnyGetter;
import com.fasterxml.jackson.databind.introspect.BeanPropertyIntrospector;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class AnyGetterWriterTest {

 private static class TestClass {

 @JsonAnyGetter
 Map<String, String> getMap() {
 return map;
 }

 private Map<String, String> map = new HashMap<>();
 }

 @Test
 public void testGetAndSerialize() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 TestClass obj = new TestClass();
 obj.map.put("key1", "value1");
 obj.map.put("key2", "value2");

 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(
 BeanProperty.stdProperty("map", Map.class, TestClass.class),
 BeanPropertyIntrospector.findAnnotated(TestClass.class, _ -> _ instanceof JsonAnyGetter),
 new MapSerializer(Map.class, ObjectMapper.DEFAULT_PROPERTY_NAME_TRANSFORMER_INSTANCE, null, null, null));

 JsonGenerator gen = mapper.getFactory().createGenerator(System.out, new ObjectWriter.DefaultPrettyPrinter());
 anyGetterWriter.getAndSerialize(obj, gen, mapper.getSerializerProvider());

 String expectedJson = "{\"key1\":\"value1\",\"key2\":\"value2\"}";
 String actualJson = mapper.writerFor(TestClass.class).writeValueAsString(obj);
 assertEquals(expectedJson, actualJson);
 }

 @Test
 public void testGetAndFilter() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 TestClass obj = new TestClass();
 obj.map.put("key1", "value1");
 obj.map.put("key2", "value2");

 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(
 BeanProperty.stdProperty("map", Map.class, TestClass.class),
 BeanPropertyIntrospector.findAnnotated(TestClass.class, _ -> _ instanceof JsonAnyGetter),
 new MapSerializer(Map.class, ObjectMapper.DEFAULT_PROPERTY_NAME_TRANSFORMER_INSTANCE, null, null, null));

 JsonGenerator gen = mapper.getFactory().createGenerator(System.out, new ObjectWriter.DefaultPrettyPrinter());
 PropertyFilter filter = new PropertyFilter() {
 @Override
 public boolean filterOut(String name, JsonGenerator gen, SerializerProvider provider) {
 return name.equals("key1");
 }
 };
 anyGetterWriter.getAndFilter(obj, gen, mapper.getSerializerProvider(), filter);

 String expectedJson = "{\"key2\":\"value2\"}";
 String actualJson = mapper.writerFor(TestClass.class).writeValueAsString(obj);
 assertEquals(expectedJson, actualJson);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeThrowsException() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 TestClass obj = new TestClass();
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(
 BeanProperty.stdProperty("map", Map.class, TestClass.class),
 BeanPropertyIntrospector.findAnnotated(TestClass.class, _ -> _ instanceof JsonAnyGetter),
 new MapSerializer(Map.class, ObjectMapper.DEFAULT_PROPERTY_NAME_TRANSFORMER_INSTANCE, null, null, null));

 JsonGenerator gen = mapper.getFactory().createGenerator(System.out, new ObjectWriter.DefaultPrettyPrinter());
 anyGetterWriter.getAndSerialize(obj, gen, mapper.getSerializerProvider());
 fail("Should throw JsonMappingException");
 }
}