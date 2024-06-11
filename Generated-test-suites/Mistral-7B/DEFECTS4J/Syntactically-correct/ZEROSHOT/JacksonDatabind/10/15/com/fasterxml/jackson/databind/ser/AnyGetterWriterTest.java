package com.fasterxml.jackson.databind.ser.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonAnyGetter;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.ser.test.BeanWithAnyGetter;

public class AnyGetterWriterTest {

 private ObjectMapper objectMapper;
 private BeanProperty property;
 private AnnotatedMember accessor;
 private AnyGetterWriter anyGetterWriter;
 private MapSerializer mapSerializer;
 private SerializerProvider serializerProvider;

 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 property = new BeanProperty.Std(BeanWithAnyGetter.class, "mapProperty");
 accessor = new AnnotatedMember(property, JsonTestData.class, "anyGetterMap");
 mapSerializer = objectMapper.getDefaultInstance().getSerializerProvider().findValueSerializer(Map.class, null);
 anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
 serializerProvider = new SerializerProvider();
 serializerProvider.setContextHandler(objectMapper.getDefaultInstance().getSerializationConfig().getContextHandler());
 }

 @Test
 public void testGetAndSerialize() throws Exception {
 Object bean = new JsonTestData();
 JsonGenerator gen = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), serializerProvider);
 anyGetterWriter.getAndSerialize(bean, gen, serializerProvider);
 gen.flush();
 ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) gen.getOutputTarget();
 String jsonString = new String(byteArrayOutputStream.toByteArray());
 ObjectNode jsonNode = objectMapper.readTree(jsonString);
 Map<String, Object> map = jsonNode.asTree().asMap();
 assertTrue(map.containsKey("key1"));
 assertTrue(map.containsKey("key2"));
 assertEquals("value1", map.get("key1"));
 assertEquals("value2", map.get("key2"));
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNullValue() throws Exception {
 Object bean = new JsonTestData();
 JsonGenerator gen = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), serializerProvider);
 anyGetterWriter.getAndSerialize(bean, gen, serializerProvider);
 gen.flush();
 ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) gen.getOutputTarget();
 String jsonString = new String(byteArrayOutputStream.toByteArray());
 ObjectMapper mapper = new ObjectMapper();
 mapper.readTree(jsonString);
 fail("Expected JsonMappingException, but did not receive one.");
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNonMapValue() throws Exception {
 Object bean = new JsonTestDataWithNonMapValue();
 JsonGenerator gen = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), serializerProvider);
 anyGetterWriter.getAndSerialize(bean, gen, serializerProvider);
 gen.flush();
 ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) gen.getOutputTarget();
 String jsonString = new String(byteArrayOutputStream.toByteArray());
 ObjectMapper mapper = new ObjectMapper();
 mapper.readTree(jsonString);
 fail("Expected JsonMappingException, but did not receive one.");
 }

 @Test
 public void testGetAndFilter() throws Exception {
 Object bean = new JsonTestData();
 PropertyFilter filter = new PropertyFilter() {
 @Override
 public boolean filterOut(String name, Object value) {
 return "value2".equals(value);
 }
 };
 JsonGenerator gen = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), serializerProvider);
 anyGetterWriter.getAndFilter(bean, gen, serializerProvider, filter);
 gen.flush();
 ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) gen.getOutputTarget();
 String jsonString = new String(byteArrayOutputStream.toByteArray());
 ObjectNode jsonNode = objectMapper.readTree(jsonString);
 Map<String, Object> map = jsonNode.asTree().asMap();
 assertFalse(map.containsValue("value2"));
 assertTrue(map.containsKey("key1"));
 assertEquals("value1", map.get("key1"));
 }

 @Test
 public void testResolve() throws Exception {
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, null);
 assertNull(anyGetterWriter.getMapSerializer());
 anyGetterWriter.resolve(serializerProvider);
 assertNotNull(anyGetterWriter.getMapSerializer());
 }
}

class JsonTestData {
 @JsonAnyGetter
 public Map<String, String> getMapProperty() {
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 return map;
 }
}

class JsonTestDataWithNonMapValue {
 @JsonAnyGetter
 public Object getMapProperty() {
 return new Integer(1);
 }
}