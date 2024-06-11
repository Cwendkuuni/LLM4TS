package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

 private static Object mapperSetup() {
 ObjectMapper mapper = new ObjectMapper();
 return mapper;
 }

 @Test
 public void testHandleTypePropertyValue() throws IOException {
 ObjectMapper mapper = mapperSetup();
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
 .build());
 JsonParser jp = mapper.getFactory().createParser("{\"prop1\":\"type1\"}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object bean = new Object();
 handler.handleTypePropertyValue(jp, ctxt, "prop1", bean);
 assertEquals("type1", handler._typeIds[0]);
 }

 @Test
 public void testHandleTypePropertyValue_WithNullBean() throws IOException {
 ObjectMapper mapper = mapperSetup();
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
 .build());
 JsonParser jp = mapper.getFactory().createParser("{\"prop1\":\"type1\"}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 handler.handleTypePropertyValue(jp, ctxt, "prop1", null);
 assertNull(handler._typeIds[0]);
 }

 @Test
 public void testHandleTypePropertyValue_WithEmptyPropertyList() throws IOException {
 ObjectMapper mapper = mapperSetup();
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .build());
 JsonParser jp = mapper.getFactory().createParser("{\"prop1\":\"type1\"}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object bean = new Object();
 boolean result = handler.handleTypePropertyValue(jp, ctxt, "prop1", bean);
 assertFalse(result);
 }

 @Test(expected = IOException.class)
 public void testHandleTypePropertyValue_MissingProperty() throws IOException {
 ObjectMapper mapper = mapperSetup();
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
 .build());
 JsonParser jp = mapper.getFactory().createParser("{\"missingProp\":\"type1\"}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object bean = new Object();
 handler.handleTypePropertyValue(jp, ctxt, "missingProp", bean);
 fail("Expected IOException");
 }

 @Test
 public void testHandlePropertyValue() throws IOException {
 ObjectMapper mapper = mapperSetup();
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
 .build());
 JsonParser jp = mapper.getFactory().createParser("{\"prop1\":\"value1\"}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object bean = new Object();
 handler.handlePropertyValue(jp, ctxt, "prop1", bean);
 assertEquals("value1", handler._properties[0].getProperty().get(bean));
 }

 @Test
 public void testHandlePropertyValue_MissingProperty() throws IOException {
 ObjectMapper mapper = mapperSetup();
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
 .build());
 JsonParser jp = mapper.getFactory().createParser("{\"missingProp\":\"value1\"}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object bean = new Object();
 handler.handlePropertyValue(jp, ctxt, "missingProp", bean);
 assertEquals(0, handler._tokens.length);
 }

 @Test
 public void testHandlePropertyValue_WithMultipleProperties() throws IOException {
 ObjectMapper mapper = mapperSetup();
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
 .addExternal(new SettableBeanProperty("prop2", Integer.class), new IntegerTypeDeserializer())
 .build());
 JsonParser jp = mapper.getFactory().createParser("{\"prop1\":\"value1\",\"prop2\":42}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object bean = new Object();
 handler.handlePropertyValue(jp, ctxt, "prop1", bean);
 assertEquals("value1", handler._properties[0].getProperty().get(bean));
 handler.handlePropertyValue(jp, ctxt, "prop2", bean);
 assertEquals((int) 42, handler._properties[1].getProperty().get(bean));
 }

 @Test
 public void testComplete() throws IOException {
 ObjectMapper mapper = mapperSetup();
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
 .build())
 .start();
 JsonParser jp = mapper.getFactory().createParser("{\"prop1\":\"value1\"}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object bean = new Object();
 handler.complete(jp, ctxt, bean);
 assertEquals("value1", handler._properties[0].getProperty().get(bean));
 }

 @Test
 public void testComplete_MissingProperty() throws IOException {
 ObjectMapper mapper = mapperSetup();
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
 .build())
 .start();
 JsonParser jp = mapper.getFactory().createParser("{\"prop2\":\"value2\"}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object bean = new Object();
 try {
 handler.complete(jp, ctxt, bean);
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testComplete_WithMultipleProperties() throws IOException {
 ObjectMapper mapper = mapperSetup();
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
 .addExternal(new SettableBeanProperty("prop2", Integer.class), new IntegerTypeDeserializer())
 .build())
 .start();
 JsonParser jp = mapper.getFactory().createParser("{\"prop1\":\"value1\",\"prop2\":42}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object bean = new Object();
 handler.complete(jp, ctxt, bean);
 assertEquals("value1", handler._properties[0].getProperty().get(bean));
 assertEquals((int) 42, handler._properties[1].getProperty().get(bean));
 }

 @Test
 public void testComplete_NullBean() throws IOException {
 ObjectMapper mapper = mapperSetup();
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
 .build())
 .start();
 JsonParser jp = mapper.getFactory().createParser("{\"prop1\":\"value1\"}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object bean = null;
 try {
 handler.complete(jp, ctxt, bean);
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testComplete_WithCreator() throws IOException {
 ObjectMapper mapper = mapperSetup();
 JsonFactory jsonFactory = mapper.getFactory();
 PropertyBasedCreator creator = new PropertyBasedCreator() {
 @Override
 public Object createUsingDefaults() throws IOException {
 return new Object();
 }
 };
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
 .build())
 .start();
 Object bean = jsonFactory.readValue(new ByteArrayInputStream("{\"prop1\":\"value1\"}".getBytes()),
 new TypeReference<Map<String, Object>>(){}.getType());
 PropertyValueBuffer buffer = new PropertyValueBuffer();
 handler.complete(jsonFactory.getParser(new ByteArrayInputStream("{\"prop1\":\"value1\"}".getBytes())),
 mapper.getDeserializationContext(), buffer, creator);
 assertEquals("value1", handler._properties[0].getProperty().get(bean));
 }

 @Test
 public void testComplete_WithMissingProperty() throws IOException {
 ObjectMapper mapper = mapperSetup();
 JsonFactory jsonFactory = mapper.getFactory();
 PropertyBasedCreator creator = new PropertyBasedCreator() {
 @Override
 public Object createUsingDefaults() throws IOException {
 return new Object();
 }
 };
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
 .build())
 .start();
 Object bean = jsonFactory.readValue(new ByteArrayInputStream("{\"prop2\":\"value2\"}".getBytes()),
 new TypeReference<Map<String, Object>>(){}.getType());
 try {
 handler.complete(jsonFactory.getParser(new ByteArrayInputStream("{\"prop2\":\"value2\"}".getBytes())),
 mapper.getDeserializationContext(), buffer, creator);
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testComplete_WithMultiplePropertiesAndCreator() throws IOException {
 ObjectMapper mapper = mapperSetup();
 JsonFactory jsonFactory = mapper.getFactory();
 PropertyBasedCreator creator = new PropertyBasedCreator() {
 @Override
 public Object createUsingDefaults() throws IOException {
 return new Object();
 }
 };
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
 .addExternal(new SettableBeanProperty("prop2", Integer.class), new IntegerTypeDeserializer())
 .build())
 .start();
 Object bean = jsonFactory.readValue(new ByteArrayInputStream("{\"prop1\":\"value1\",\"prop2\":42}".getBytes()),
 new TypeReference<Map<String, Object>>(){}.getType());
 PropertyValueBuffer buffer = new PropertyValueBuffer();
 handler.complete(jsonFactory.getParser(new ByteArrayInputStream("{\"prop1\":\"value1\",\"prop2\":42}".getBytes())),
 mapper.getDeserializationContext(), buffer, creator);
 assertEquals("value1", handler._properties[0].getProperty().get(bean));
 assertEquals((int) 42, handler._properties[1].getProperty().get(bean));
 }
}