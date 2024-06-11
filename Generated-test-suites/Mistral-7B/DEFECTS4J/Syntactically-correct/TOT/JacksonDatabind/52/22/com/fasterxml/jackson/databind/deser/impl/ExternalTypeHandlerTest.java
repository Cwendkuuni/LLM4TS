package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

 private ObjectMapper mapper;
 private ExternalTypeHandler handler;

 @Before
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 ObjectNode node = mapper.createObjectNode();
 node.put("prop1", "value1");
 node.put("prop2", "type1");
 node.put("typeId", "MyClass");
 handler = mapper.readerForUpdating(ExternalTypeHandler.class).readValue(node);
 }

 @Test
 public void testHandleTypePropertyValue() throws Exception {
 JsonParser p = mapper.getFactory().createParser("{\"prop1\":\"value1\",\"typeId\":\"MyClass\"}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object bean = new Object();
 handler.handleTypePropertyValue(p, ctxt, "prop1", bean);
 assertEquals("value1", handler.getProperty(0).get(bean));
 assertEquals("MyClass", handler.getTypeIds()[0]);
 }

 @Test
 public void testHandlePropertyValue() throws Exception {
 JsonParser p = mapper.getFactory().createParser("{\"prop1\":\"value1\",\"typeId\":\"MyClass\",\"prop2\":1}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object bean = new Object();
 handler.handlePropertyValue(p, ctxt, "prop1", bean);
 assertEquals("value1", handler.getProperty(0).get(bean));
 assertEquals("MyClass", handler.getTypeIds()[0]);
 }

 @Test
 public void testComplete() throws Exception {
 Object bean = handler.complete(mapper.getFactory().createParser("{\"prop1\":\"value1\",\"typeId\":\"MyClass\",\"prop2\":1}"), ctxt, new Object());
 assertEquals("value1", handler.getProperty(0).get(bean));
 assertEquals("MyClass", handler.getTypeIds()[0]);
 }

 @Test(expected = IOException.class)
 public void testMissingProperty() throws Exception {
 Object bean = handler.start().complete(mapper.getFactory().createParser("{\"typeId\":\"MyClass\",\"prop2\":1}"), ctxt, new Object());
 fail("Should throw exception for missing property");
 }

 @Test(expected = IOException.class)
 public void testMissingTypeId() throws Exception {
 Object bean = handler.start().complete(mapper.getFactory().createParser("{\"prop1\":\"value1\",\"prop2\":1}"), ctxt, new Object());
 fail("Should throw exception for missing type id");
 }

 @Test
 public void testNullValue() throws Exception {
 JsonParser p = mapper.getFactory().createParser("{\"prop1\":null,\"typeId\":\"MyClass\",\"prop2\":1}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object bean = handler.start().complete(p, ctxt, new Object());
 assertNull(handler.getProperty(0).get(bean));
 }

 @Test(expected = IOException.class)
 public void testInvalidTypeId() throws Exception {
 Object bean = handler.start().complete(mapper.getFactory().createParser("{\"prop1\":\"value1\",\"typeId\":\"InvalidType\",\"prop2\":1}"), ctxt, new Object());
 fail("Should throw exception for invalid type id");
 }
}