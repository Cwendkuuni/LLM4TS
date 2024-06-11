package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private ObjectMap<String, SettableBeanProperty> properties;
 private Object testInstance;

 @Before
 public void setUp() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 properties = mapper.getTypeFactory().constructMapType(ObjectMap.class, String.class, SettableBeanProperty.class);

 // Simulate creation of ExternalTypeHandler instance with some properties
 JsonDeserializer<Object> deserializer = new PropertyNameDeserializer();
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(properties.put("prop1", new SettableBeanProperty(TestClass.class, "prop1")), deserializer)
 .addExternal(properties.put("prop2", new SettableBeanProperty(TestClass.class, "prop2")), deserializer)
 .build());
 testInstance = new TestClass();
 }

 @Test
 public void testHandleTypePropertyValue() throws IOException, JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{\"prop1\":\"StringType1\"}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 handler.handleTypePropertyValue(p, ctxt, "prop1", testInstance);
 assertEquals("StringType1", properties.get("prop1").get(testInstance));
 }

 @Test
 public void testHandleTypePropertyValue_MissingProperty() throws IOException, JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{\"missingProp\":\"StringType1\"}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 handler.handleTypePropertyValue(p, ctxt, "missingProp", testInstance);
 assertNull(properties.get("missingProp"));
 }

 @Test
 public void testHandleTypePropertyValue_MissingTypeId() throws IOException, JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{\"prop1\":null}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 handler.handleTypePropertyValue(p, ctxt, "prop1", testInstance);
 assertNull(properties.get("prop1"));
 }

 @Test
 public void testHandleTypePropertyValue_TypeIdAlreadySet() throws IOException, JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{\"prop1\":\"StringType1\", \"prop1\":\"StringType2\"}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 handler.handleTypePropertyValue(p, ctxt, "prop1", testInstance);
 assertEquals("StringType2", properties.get("prop1").get(testInstance));
 }

 @Test
 public void testHandleTypePropertyValue_TypeDeserializerThrowsIOException() throws IOException, JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{\"prop1\":\"InvalidTypeId\"}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 when(typeDeserializer.deserialize).thenThrow(new IOException("Test Exception"));
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(properties.put("prop1", new SettableBeanProperty(TestClass.class, "prop1")), deserializer)
 .build());
 handler.handleTypePropertyValue(p, ctxt, "prop1", testInstance);
 fail("Expected mapping exception to be thrown");
 }

 @Test
 public void testHandlePropertyValue() throws IOException, JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{\"prop2\":123}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 handler.handlePropertyValue(p, ctxt, "prop2", testInstance);
 assertEquals(123, properties.get("prop2").get(testInstance));
 }

 @Test
 public void testHandlePropertyValue_MissingProperty() throws IOException, JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{\"missingProp\":123}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 handler.handlePropertyValue(p, ctxt, "missingProp", testInstance);
 assertNull(properties.get("missingProp"));
 }

 @Test
 public void testHandlePropertyValue_TypeIdAlreadySet() throws IOException, JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{\"prop2\":123,\"prop2\":\"StringType1\"}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 handler.handlePropertyValue(p, ctxt, "prop2", testInstance);
 assertEquals("StringType1", properties.get("prop2").get(testInstance));
 }

 @Test
 public void testHandlePropertyValue_TypeDeserializerThrowsIOException() throws IOException, JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{\"prop2\":\"InvalidTypeId\"}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 when(typeDeserializer.deserialize).thenThrow(new IOException("Test Exception"));
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(properties.put("prop2", new SettableBeanProperty(TestClass.class, "prop2")), deserializer)
 .build());
 handler.handlePropertyValue(p, ctxt, "prop2", testInstance);
 fail("Expected mapping exception to be thrown");
 }

 @Test
 public void testComplete() throws IOException, JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{" +
 "\"prop1\":\"StringType1\"," +
 "\"prop2\":123" +
 "}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 handler.complete(p, ctxt, testInstance);
 assertEquals(123, properties.get("prop2").get(testInstance));
 assertEquals("StringType1", properties.get("prop1").get(testInstance));
 }

 @Test
 public void testComplete_MissingProperty() throws IOException, JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{" +
 "\"prop1\":\"StringType1\"" +
 "}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 handler.complete(p, ctxt, testInstance);
 assertEquals("StringType1", properties.get("prop1").get(testInstance));
 assertNull(properties.get("prop2"));
 }

 @Test
 public void testComplete_MissingTypeId() throws IOException, JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{" +
 "\"prop1\":null," +
 "\"prop2\":123" +
 "}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 handler.complete(p, ctxt, testInstance);
 assertNull(properties.get("prop2"));
 assertEquals(null, properties.get("prop1").get(testInstance));
 }

 @Test
 public void testComplete_TypeIdAlreadySet() throws IOException, JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{" +
 "\"prop1\":\"StringType1\"," +
 "\"prop2\":\"StringType2\"" +
 "}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 handler.complete(p, ctxt, testInstance);
 assertEquals("StringType2", properties.get("prop2").get(testInstance));
 assertEquals("StringType1", properties.get("prop1").get(testInstance));
 }

 @Test
 public void testComplete_TypeDeserializerThrowsIOException() throws IOException, JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{" +
 "\"prop1\":\"InvalidTypeId\"," +
 "\"prop2\":123" +
 "}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 when(typeDeserializer.deserialize).thenThrow(new IOException("Test Exception"));
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(properties.put("prop1", new SettableBeanProperty(TestClass.class, "prop1")), deserializer)
 .addExternal(properties.put("prop2", new SettableBeanProperty(TestClass.class, "prop2")), deserializer)
 .build());
 handler.complete(p, ctxt, testInstance);
 fail("Expected mapping exception to be thrown");
 }

 @Test
 public void testCompleteWithCreator() throws IOException, JsonProcessingException {
 PropertyValueBuffer buffer = new PropertyValueBuffer();
 PropertyBasedCreator creator = new TestCreator();
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(properties.put("prop1", new SettableBeanProperty(TestClass.class, "prop1")), deserializer)
 .addExternal(properties.put("prop2", new SettableBeanProperty(TestClass.class, "prop2")), deserializer)
 .build());
 handler.complete(p, ctxt, buffer, creator);
 assertEquals(123, properties.get("prop2").get(testInstance));
 assertEquals("StringType1", properties.get("prop1").get(testInstance));
 }

 @Test
 public void testCompleteWithCreator_MissingProperty() throws IOException, JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{" +
 "\"prop1\":\"StringType1\"" +
 "}");
 PropertyValueBuffer buffer = new PropertyValueBuffer();
 PropertyBasedCreator creator = new TestCreator();
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(properties.put("prop1", new SettableBeanProperty(TestClass.class, "prop1")), deserializer)
 .addExternal(properties.put("prop2", new SettableBeanProperty(TestClass.class, "prop2")), deserializer)
 .build());
 handler.complete(p, ctxt, buffer, creator);
 assertEquals(123, properties.get("prop2").get(testInstance));
 assertNull(properties.get("prop1").get(testInstance));
 }

 @Test
 public void testCompleteWithCreator_MissingTypeId() throws IOException, JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{" +
 "\"prop1\":null," +
 "\"prop2\":123" +
 "}");
 PropertyValueBuffer buffer = new PropertyValueBuffer();
 PropertyBasedCreator creator = new TestCreator();
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(properties.put("prop1", new SettableBeanProperty(TestClass.class, "prop1")), deserializer)
 .addExternal(properties.put("prop2", new SettableBeanProperty(TestClass.class, "prop2")), deserializer)
 .build());
 handler.complete(p, ctxt, buffer, creator);
 assertNull(properties.get("prop2").get(testInstance));
 assertNull(properties.get("prop1").get(testInstance));
 }

 @Test
 public void testCompleteWithCreator_TypeIdAlreadySet() throws IOException, JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{" +
 "\"prop1\":\"StringType1\"," +
 "\"prop2\":\"StringType2\"" +
 "}");
 PropertyValueBuffer buffer = new PropertyValueBuffer();
 PropertyBasedCreator creator = new TestCreator();
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(properties.put("prop1", new SettableBeanProperty(TestClass.class, "prop1")), deserializer)
 .addExternal(properties.put("prop2", new SettableBeanProperty(TestClass.class, "prop2")), deserializer)
 .build());
 handler.complete(p, ctxt, buffer, creator);
 assertEquals("StringType2", properties.get("prop2").get(testInstance));
 assertEquals("StringType1", properties.get("prop1").get(testInstance));
 }

 @Test
 public void testCompleteWithCreator_TypeDeserializerThrowsIOException() throws IOException, JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{" +
 "\"prop1\":\"InvalidTypeId\"," +
 "\"prop2\":123" +
 "}");
 PropertyValueBuffer buffer = new PropertyValueBuffer();
 PropertyBasedCreator creator = new TestCreator();
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(properties.put("prop1", new SettableBeanProperty(TestClass.class, "prop1")), deserializer)
 .addExternal(properties.put("prop2", new SettableBeanProperty(TestClass.class, "prop2")), deserializer)
 .build());
 handler.complete(p, ctxt, buffer, creator);
 fail("Expected mapping exception to be thrown");
 }
}