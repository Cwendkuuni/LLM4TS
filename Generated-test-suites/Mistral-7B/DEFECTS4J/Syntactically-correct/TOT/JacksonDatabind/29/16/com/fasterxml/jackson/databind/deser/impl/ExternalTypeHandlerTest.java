package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private ObjectNode node;
 private MapperFactory mf;

 @BeforeAll
 public void setup() throws Exception {
 mf = new ObjectMapperFactory();
 node = mf.createObjectNode();
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
 .addExternal(new SettableBeanProperty("prop2", Integer.class), new NumberTypeDeserializer())
 .build());
 }

 @Test
 public void testHandleTypePropertyValue() throws Exception {
 JsonParser jp = mf.createParser(node);
 handler.handleTypePropertyValue(jp, new TestContext(), "prop1", null);
 assertEquals("typeId1", handler._typeIds[0]);
 jp.nextToken(); // consume next token
 handler.handleTypePropertyValue(jp, new TestContext(), "prop1", new Object());
 handler.tokens[0] = null;
 handler._typeIds[0] = null;
 jp.nextToken(); // consume next token
 handler.handleTypePropertyValue(jp, new TestContext(), "prop1", new Object());
 assertNull(handler._typeIds[0]);
 }

 @Test
 public void testHandleTypePropertyValue_missingProperty() throws Exception {
 JsonParser jp = mf.createParser(node);
 handler.handleTypePropertyValue(jp, new TestContext(), "nonExistentProperty", new Object());
 assertFalse(handler.tokens[0] != null);
 }

 @Test
 public void testHandlePropertyValue() throws Exception {
 JsonParser jp = mf.createParser(node);
 handler.handlePropertyValue(jp, new TestContext(), "prop1", null);
 assertEquals("value1", handler._tokens[0].textValue());
 jp.nextToken(); // consume next token
 handler.handlePropertyValue(jp, new TestContext(), "prop1", new Object());
 handler.tokens[0] = null;
 handler._typeIds[0] = null;
 jp.nextToken(); // consume next token
 handler.handlePropertyValue(jp, new TestContext(), "prop1", new Object());
 assertNull(handler._tokens[0]);
 }

 @Test
 public void testHandlePropertyValue_missingPropertyAndValue() throws Exception {
 JsonParser jp = mf.createParser(node);
 handler.handlePropertyValue(jp, new TestContext(), "nonExistentProperty", new Object());
 assertNull(handler._tokens[0]);
 }

 @Test
 public void testComplete() throws Exception {
 JsonParser jp = mf.createParser(node);
 handler.complete(jp, new TestContext(), new Object());
 assertEquals("typeId1", handler._typeIds[0]);
 assertEquals("value1", handler._properties[0].getProperty().get(handler.start().complete(jp, new TestContext(), new Object()).getClass().getDeclaredField("prop1").get(handler.start().complete(jp, new TestContext(), new Object()))));
 }

 @Test
 public void testComplete_missingTypeId() throws Exception {
 JsonParser jp = mf.createParser(node);
 handler.handleTypePropertyValue(jp, new TestContext(), "prop1", null);
 handler.handlePropertyValue(jp, new TestContext(), "prop1", "value1");
 jp.nextToken(); // consume next token
 handler.complete(jp, new TestContext(), new Object());
 assertNull(handler._typeIds[0]);
 }

 @Test
 public void testComplete_missingProperty() throws Exception {
 JsonParser jp = mf.createParser(node);
 handler.handleTypePropertyValue(jp, new TestContext(), "prop1", null);
 handler.handlePropertyValue(jp, new TestContext(), "nonExistentProperty", "value1");
 jp.nextToken(); // consume next token
 handler.complete(jp, new TestContext(), new Object());
 assertNull(handler._properties[1].getProperty().get(handler.start().complete(jp, new TestContext(), new Object()).getClass().getDeclaredField("prop2").get(handler.start().complete(jp, new TestContext(), new Object()))));
 }

 private static class TestContext extends DeserializationContext {
 @Override
 public DeserializationConfig config() {
 return new DeserializationConfig();
 }
 }

 static class StringTypeDeserializer extends TypeDeserializer {
 @Override
 public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer vd) throws IOException {
 return p.getValueAsString();
 }

 @Override
 public String getPropertyName() {
 return "StringType";
 }
 }

 static class NumberTypeDeserializer extends TypeDeserializer {
 @Override
 public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer vd) throws IOException {
 return p.getValueAsInt();
 }

 @Override
 public String getPropertyName() {
 return "NumberType";
 }
 }
}