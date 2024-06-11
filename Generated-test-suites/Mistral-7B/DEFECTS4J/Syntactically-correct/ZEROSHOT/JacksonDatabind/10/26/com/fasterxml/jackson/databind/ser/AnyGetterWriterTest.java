package com.fasterxml.jackson.databind.ser.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.ser.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer.StdSerializers;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;

public class AnyGetterWriterTest {

 private ObjectMapper objectMapper;
 private AnyGetterWriter anyGetterWriter;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 AnnotatedClass ac = objectMapper.getTypeFactory()
 .constructType((Class<?>) AnnotatedGetterWriter.class);
 BeanProperty property = ac.findProperty("mapProperty");
 AnnotatedMember accessor = ac.findMember("getMapProperty");
 AnnotatedMethod getter = ac.findMethod("getMapProperty", null);
 anyGetterWriter = new AnyGetterWriter(property, accessor,
 new MapSerializer(objectMapper.getConfig(), StdSerializers.emptyMapSerializer(), TypeFactory.defaultInstance()));
 }

 @Test
 public void testGetAndSerialize() throws Exception {
 Object bean = new AnnotatedGetterWriter();
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 try {
 anyGetterWriter.getAndSerialize(bean, gen, objectMapper.getSerializerProvider());
 } finally {
 gen.close();
 }
 Map<String, Object> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 AnnotatedGetterWriter annotatedBean = new AnnotatedGetterWriter();
 annotatedBean.setMapProperty(map);
 JsonGenerator gen2 = objectMapper.getFactory().createGenerator(System.out);
 try {
 anyGetterWriter.getAndSerialize(annotatedBean, gen2, objectMapper.getSerializerProvider());
 } finally {
 gen2.close();
 }

 Map<String, Object> serializedMap = objectMapper.readValue(System.in, Map.class);
 assertEquals("Value order might depend on serializer implementation", map, serializedMap);
 }

 @Test
 public void testGetAndFilter() throws Exception {
 Object bean = new AnnotatedGetterWriter();
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 SerializerProvider provider = objectMapper.getSerializerProvider();
 try {
 anyGetterWriter.getAndFilter(bean, gen, provider, new FilterProvider().emptyFilterProvider());
 } finally {
 gen.close();
 }

 Map<String, Object> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 AnnotatedGetterWriter annotatedBean = new AnnotatedGetterWriter();
 annotatedBean.setMapProperty(map);
 JsonGenerator gen2 = objectMapper.getFactory().createGenerator(System.out);
 try {
 anyGetterWriter.getAndFilter(annotatedBean, gen2, provider, new FilterProvider().filter("key1", null));
 } finally {
 gen2.close();
 }

 Map<String, Object> filteredMap = objectMapper.readValue(System.in, Map.class);
 assertNotNull(filteredMap);
 assertEquals("Value order might depend on serializer implementation", map.get("key1"), filteredMap.get("key1"));
 assertNull(filteredMap.get("key2"));
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeNullMap() throws Exception {
 Object bean = new AnnotatedGetterWriter();
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 try {
 anyGetterWriter.getAndSerialize(bean, gen, objectMapper.getSerializerProvider());
 } finally {
 gen.close();
 }

 AnnotatedGetterWriter annotatedBean = new AnnotatedGetterWriter();
 try {
 anyGetterWriter.getAndSerialize(annotatedBean, gen, objectMapper.getSerializerProvider());
 } finally {
 gen.close();
 }

 assertNull(annotatedBean.getMapProperty());
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeNonMapValue() throws Exception {
 Object bean = new AnnotatedGetterWriter();
 bean.setMapProperty(new String[]{"value1", "value2"});
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 try {
 anyGetterWriter.getAndSerialize(bean, gen, objectMapper.getSerializerProvider());
 } finally {
 gen.close();
 }
 }

}

class AnnotatedGetterWriter {
 private Map<String, Object> mapProperty;

 @JsonAnyGetter
 public Map<String, Object> getMapProperty() {
 return mapProperty;
 }

 @JsonProperty("mapProperty")
 public void setMapProperty(Map<String, Object> value) {
 this.mapProperty = value;
 }

 @PropertyName("mapProperty")
 public static class MapProperty {
 @JsonProperty
 public Map<String, Object> value;
 }
}