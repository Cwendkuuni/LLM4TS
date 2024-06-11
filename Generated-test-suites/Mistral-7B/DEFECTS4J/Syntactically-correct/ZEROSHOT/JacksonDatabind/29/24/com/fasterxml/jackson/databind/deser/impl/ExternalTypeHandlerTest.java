package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Test;

/**
 * Test class for ExternalTypeHandler.
 */
public class ExternalTypeHandlerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testHandleTypePropertyValue() throws Exception {
 ExternalTypeHandler handler = createHandler();
 JsonParser jp = MAPPER.getFactory().createParser("{\"externalProperty\":\"StringType\"}");
 DeserializationContext ctxt = MAPPER.getFactory().createContext();
 Object bean = new Object();
 String expectedType = "StringType";

 handler.handleTypePropertyValue(jp, ctxt, "externalProperty", bean);
 assertEquals(expectedType, handler._typeIds[0]);
 }

 @Test
 public void testHandleTypePropertyValue_missingProperty() throws Exception {
 ExternalTypeHandler handler = createHandler();
 JsonParser jp = MAPPER.getFactory().createParser("{\"missingProperty\":\"StringType\"}");
 DeserializationContext ctxt = MAPPER.getFactory().createContext();
 Object bean = new Object();

 handler.handleTypePropertyValue(jp, ctxt, "missingProperty", bean);
 assertNull(handler._typeIds);
 }

 @Test
 public void testHandlePropertyValue() throws Exception {
 ExternalTypeHandler handler = createHandler();
 JsonParser jp = MAPPER.getFactory().createParser("{\"externalProperty\":1}");
 DeserializationContext ctxt = MAPPER.getFactory().createContext();
 Object bean = new Object();
 String expectedType = "NUMBER";

 handler.handlePropertyValue(jp, ctxt, "externalProperty", bean);
 assertEquals(expectedType, handler._typeIds[0]);
 }

 @Test
 public void testHandlePropertyValue_missingTypeId() throws Exception {
 ExternalTypeHandler handler = createHandler();
 JsonParser jp = MAPPER.getFactory().createParser("{\"externalProperty\":\"value\"}");
 DeserializationContext ctxt = MAPPER.getFactory().createContext();
 Object bean = new Object();

 handler.handlePropertyValue(jp, ctxt, "externalProperty", bean);
 assertNull(handler._typeIds);
 }

 @Test
 public void testComplete() throws Exception {
 ExternalTypeHandler handler = createHandler();
 JsonParser jp = MAPPER.getFactory().createParser("{\"externalProperty1\":\"StringType1\",\"externalProperty2\":\"StringType2\"}");
 DeserializationContext ctxt = MAPPER.getFactory().createContext();
 Object bean = new Object();

 handler.handleTypePropertyValue(jp, ctxt, "externalProperty1", bean);
 handler.handleTypePropertyValue(jp, ctxt, "externalProperty2", bean);
 handler.complete(jp, ctxt, bean);

 assertEquals("StringType1", handler._typeIds[0]);
 assertEquals("StringType2", handler._typeIds[1]);
 }

 @Test(expected = JsonMappingException.class)
 public void testComplete_missingProperty() throws Exception {
 ExternalTypeHandler handler = createHandler();
 JsonParser jp = MAPPER.getFactory().createParser("{\"externalProperty1\":\"StringType1\"}");
 DeserializationContext ctxt = MAPPER.getFactory().createContext();
 Object bean = new Object();

 handler.handleTypePropertyValue(jp, ctxt, "externalProperty1", bean);
 handler.complete(jp, ctxt, bean);

 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testComplete_missingTypeId() throws Exception {
 ExternalTypeHandler handler = createHandler();
 JsonParser jp = MAPPER.getFactory().createParser("{\"externalProperty1\":\"value\"}");
 DeserializationContext ctxt = MAPPER.getFactory().createContext();
 Object bean = new Object();

 handler.handlePropertyValue(jp, ctxt, "externalProperty1", bean);
 handler.complete(jp, ctxt, bean);

 fail("Expected JsonMappingException");
 }

 @Test
 public void testComplete_nullValue() throws Exception {
 ExternalTypeHandler handler = createHandler();
 JsonParser jp = MAPPER.getFactory().createParser("{\"externalProperty\":null}");
 DeserializationContext ctxt = MAPper.getFactory().createContext();
 Object bean = new Object();

 handler.handleTypePropertyValue(jp, ctxt, "externalProperty", bean);
 handler.complete(jp, ctxt, bean);

 assertNull(handler._typeIds);
 }

 private ExternalTypeHandler createHandler() {
 SettableBeanProperty property1 = new StubSettableBeanProperty("externalProperty1");
 SettableBeanProperty property2 = new StubSettableBeanProperty("externalProperty2");
 TypeDeserializer typeDeserializer1 = new StubTypeDeserializer("StringType1", property1.getType());
 TypeDeserializer typeDeserializer2 = new StubTypeDeserializer("StringType2", property2.getType());
 ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
 builder.addExternal(property1, typeDeserializer1);
 builder.addExternal(property2, typeDeserializer2);
 return builder.build();
 }

 private static class StubSettableBeanProperty extends PropertyName {
 private StubSettableBeanProperty(String name) {
 super(name);
 }

 public <T> T get(Object bean) {
 throw new UnsupportedOperationException();
 }

 public void set(Object bean, T value) {
 // no-op
 }

 public ObjectType getType() {
 throw new UnsupportedOperationException();
 }
 }

 private static class StubTypeDeserializer implements TypeDeserializer {
 private final String _typeId;
 private final Class<?> _type;

 public StubTypeDeserializer(String typeId, Class<?> type) {
 _typeId = typeId;
 _type = type;
 }

 public void deserialize(JsonParser p, DeserializationContext ctxt, PropertyName name, TypeDeserializer typeDeserializer)
 throws IOException {
 fail("deserialize should not be called");
 }

 public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
 throws IOException {
 fail("deserializeWithType should not be called");
 }

 public Object deserializeValue(JsonParser p, DeserializationContext ctxt) throws IOException {
 return null;
 }

 public String getPropertyName() {
 return _typeId;
 }

 public Class<?> getValueType() {
 return _type;
 }

 public JsonDeserializer<?> getContentDeserializer() {
 return null;
 }
 }
}