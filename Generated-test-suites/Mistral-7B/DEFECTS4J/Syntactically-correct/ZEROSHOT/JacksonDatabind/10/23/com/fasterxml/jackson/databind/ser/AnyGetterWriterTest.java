package com.fasterxml.jackson.databind.ser.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonAnyGetter;
import com.fasterxml.jackson.databind.annotation.JsonInclude;
import com.fasterxml.jackson.databind.bean.BeanProperty;
import com.fasterxml.jackson.databind.deser.std.MapDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer.StdSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;

public class AnyGetterWriterTest {

 private ObjectMapper objectMapper;
 private BeanPropertyWriter propertyWriter;
 private AnyGetterWriter anyGetterWriter;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 objectMapper.findAndRegisterModules();

 BeanDescription beanDescription = objectMapper.findBeanDescription(TestClass.class, null);
 AnnotatedField anyGetterField = AnnotationIntrospector.findAnnotated(beanDescription, JsonAnyGetter.class, true);
 BeanProperty property = BeanUtil.findProperty(anyGetterField.getAnnotation(), beanDescription);
 propertyWriter = new BeanPropertyWriter(property);
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, anyGetterField, new StdSerializer(Map.class, MapDeserializer.anyMapSerializer()));
 this.anyGetterWriter = anyGetterWriter;
 }

 @Test
 public void testGetAndSerialize() throws JsonProcessingException, IOException {
 Object testObject = new TestClass();
 testObject.setMap((Map<String, String>) new HashMap<String, String>());
 testObject.getMap().put("key1", "value1");
 testObject.getMap().put("key2", "value2");

 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 anyGetterWriter.getAndSerialize(testObject, gen, objectMapper.getSerializerProvider());

 String expectedOutput = "{\"key1\":\"value1\",\"key2\":\"value2\"}";
 String actualOutput = readOutputFromStream(gen.getOutputStream());
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testGetAndFilter() throws JsonProcessingException, IOException {
 Object testObject = new TestClass();
 testObject.setMap((Map<String, String>) new HashMap<String, String>());
 testObject.getMap().put("key1", "value1");
 testObject.getMap().put("key2", "value2");

 PropertyFilter filter = new Filter();

 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 anyGetterWriter.getAndFilter(testObject, gen, objectMapper.getSerializerProvider(), filter);

 String expectedOutput = "{\"key1\":\"value1\"}";
 String actualOutput = readOutputFromStream(gen.getOutputStream());
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerialize_InvalidType() throws JsonProcessingException, IOException {
 Object testObject = new TestClass();
 testObject.setMap((Object) new int[10]);

 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 anyGetterWriter.getAndSerialize(testObject, gen, objectMapper.getSerializerProvider());
 fail();
 }

 @Test
 public void testResolve() throws JsonMappingException {
 SerializerProvider provider = objectMapper.getSerializerProvider();
 anyGetterWriter.resolve(provider);
 assertNotNull(anyGetterWriter.getMapSerializer());
 }

 private String readOutputFromStream(java.io.OutputStream out) throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 byte[] buffer = new byte[4096];
 int n = 0;
 while (-1 != (n = out.read(buffer, 0, buffer.length))) {
 byteArrayOutputStream.write(buffer, 0, n);
 }
 return byteArrayOutputStream.toString();
 }

 static class TestClass {
 @JsonAnyGetter
 @JsonInclude(JsonInclude.Include.NON_NULL)
 private Map<String, String> map;

 public Map<String, String> getMap() {
 return map;
 }

 public void setMap(Map<String, String> map) {
 this.map = map;
 }
 }

 static class Filter {
}
}