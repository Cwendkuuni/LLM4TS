package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

 private static class TestBean {
 private String typeId;
 private List<NestedBean> nestedBeans;

 public void setTypeId(String typeId) {
 this.typeId = typeId;
 }

 public String getTypeId() {
 return typeId;
 }

 public void setNestedBeans(List<NestedBean> nestedBeans) {
 this.nestedBeans = nestedBeans;
 }

 public List<NestedBean> getNestedBeans() {
 return nestedBeans;
 }

 public static class NestedBean {
 private String name;

 public void setName(String name) {
 this.name = name;
 }

 public String getName() {
 return name;
 }
 }
 }

 @SuppressWarnings("unchecked")
 @org.junit.BeforeClass
 public static void setup() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerFor(TestBean.class);
 JsonNode node = mapper.readTree("{\"typeId\":\"StringType\",\"nestedBeans\":[{\"name\":\"Bean1\"},{\"name\":\"Bean2\"}]}");
 TestBean bean = reader.readValue(node);
 ExternalTypeHandler handler = bean.getNestedBeans() == null ? bean.start() : bean;
 TestBean deserialized = mapper.readValue(node, TestBean.class);
 assertEquals(handler, deserialized.getNestedBeans() == null ? deserialized.start() : deserialized);
 }

 @org.junit.Test
 public void testHandleTypePropertyValue() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerFor(TestBean.class);
 String json = "{\"typeId\":\"StringType\",\"typePropertyName\":\"typeId\",\"Bean\":{\"name\":\"Bean1\"}}";
 TestBean bean = reader.readValue(json);
 ExternalTypeHandler handler = bean.getNestedBeans() == null ? bean.start() : bean;
 DeserializationContext context = mapper.getContext();
 JsonParser p = mapper.parserForObject(new ByteArrayInputStream(json.getBytes()));
 assertTrue(handler.handleTypePropertyValue(p, context, "typePropertyName", bean));
 assertEquals("StringType", handler.getProperties()[0].getTypePropertyName());
 assertEquals("StringType", handler.getTypeIds()[0]);
 }

 @org.junit.Test
 public void testHandlePropertyValue() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerFor(TestBean.class);
 String json = "{\"typeId\":\"StringType\",\"typePropertyName\":\"typeId\",\"Bean\":{\"name\":\"Bean1\"},\"name\":\"Test\"}";
 TestBean bean = reader.readValue(json);
 ExternalTypeHandler handler = bean.getNestedBeans() == null ? bean.start() : bean;
 DeserializationContext context = mapper.getContext();
 JsonParser p = mapper.parserForObject(new ByteArrayInputStream(json.getBytes()));
 assertTrue(handler.handlePropertyValue(p, context, "name", bean));
 assertEquals("Test", handler.getProperties()[1].getProperty().getName());
 assertEquals("Test", handler.getTokens()[1].valueAsString());
 }

 @org.junit.Test
 public void testComplete() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerFor(TestBean.class);
 String json = "{\"typeId\":\"StringType\",\"typePropertyName\":\"typeId\"}";
 TestBean bean = reader.readValue(json);
 ExternalTypeHandler handler = bean.getNestedBeans() == null ? bean.start() : bean;
 DeserializationContext context = mapper.getContext();
 JsonParser p = mapper.parserForObject(new ByteArrayInputStream(json.getBytes()));
 Object result = handler.complete(p, context, null);
 TestBean deserialized = (TestBean) result;
 assertEquals("StringType", deserialized.getTypeId());
 assertNull(deserialized.getNestedBeans());
 }

 @org.junit.Test
 public void testCompleteWithNestedBeans() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerFor(TestBean.class);
 String json = "{\"typeId\":\"StringType\",\"typePropertyName\":\"typeId\",\"nestedBeans\":[{\"name\":\"Bean1\"},{\"name\":\"Bean2\"}]}";
 TestBean bean = reader.readValue(json);
 ExternalTypeHandler handler = bean.start();
 DeserializationContext context = mapper.getContext();
 JsonParser p = mapper.parserForObject(new ByteArrayInputStream(json.getBytes()));
 Object result = handler.complete(p, context, null, null);
 TestBean deserialized = (TestBean) result;
 List<NestedBean> nestedBeans = deserialized.getNestedBeans();
 assertEquals(2, nestedBeans.size());
 assertEquals("Bean1", nestedBeans.get(0).getName());
 assertEquals("Bean2", nestedBeans.get(1).getName());
 }

 @org.junit.Test
 public void testCompleteWithMissingTypeId() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerFor(TestBean.class);
 String json = "{\"typeId\":null,\"typePropertyName\":\"typeId\",\"nestedBeans\":[{\"name\":\"Bean1\"},{\"name\":\"Bean2\"}]}";
 TestBean bean = reader.readValue(json);
 ExternalTypeHandler handler = bean.start();
 DeserializationContext context = mapper.getContext();
 JsonParser p = mapper.parserForObject(new ByteArrayInputStream(json.getBytes()));
 try {
 handler.complete(p, context, null);
 fail("Expected MappingException for missing type id");
 } catch (MappingException e) {
 // expected
 }
 }

 @org.junit.Test
 public void testCompleteWithMissingProperty() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerFor(TestBean.class);
 String json = "{\"typeId\":\"StringType\",\"typePropertyName\":\"typeId\"}";
 TestBean bean = reader.readValue(json);
 ExternalTypeHandler handler = bean.start();
 DeserializationContext context = mapper.getContext();
 JsonParser p = mapper.parserForObject(new ByteArrayInputStream(json.getBytes()));
 try {
 handler.complete(p, context, null, new PropertyName("wrongPropertyName"));
 fail("Expected MappingException for missing property");
 } catch (MappingException e) {
 // expected
 }
 }
}