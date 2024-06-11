package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private JsonParser parser;
 private DeserializationContext ctxt;

 @Before
 public void setup() throws JsonProcessingException {
 JsonFactory factory = new JsonFactory();
 parser = factory.createParser("[{\"typeId\":\"Foo\",\"prop1\":1,\"prop2\":2}," +
 "{\"prop1\":3,\"prop2\":4}]");
 ctxt = new TestDeserializationContext();
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer(String.class))
 .addExternal(new SettableBeanProperty("prop2", Integer.class), new IntegerTypeDeserializer())
 .build());
 }

 @Test
 public void testHandleTypePropertyValue() throws JsonProcessingException {
 handler.handleTypePropertyValue(parser, ctxt, "typeId", null);
 assertEquals("Foo", handler.getTypeIds()[0]);
 }

 @Test
 public void testHandleTypePropertyValueWithNullBean() throws JsonProcessingException {
 handler.handleTypePropertyValue(parser, ctxt, "typeId", null);
 assertNull(handler.getTokens()[0]);
 }

 @Test
 public void testHandleTypePropertyValueWithEmptyTokensArray() throws JsonProcessingException {
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer(String.class))
 .addExternal(new SettableBeanProperty("prop2", Integer.class), new IntegerTypeDeserializer())
 .build());
 handler.handleTypePropertyValue(parser, ctxt, "typeId", null);
 handler.handleTypePropertyValue(parser, ctxt, "typeId", null);
 assertEquals(2, handler.getTypeIds().length);
 }

 @Test
 public void testHandlePropertyValue() throws JsonProcessingException {
 handler.handlePropertyValue(parser, ctxt, "prop1", new Object());
 assertEquals("1", handler.getTypeIds()[0]);
 }

 @Test
 public void testHandlePropertyValueWithEmptyTokensArray() throws JsonProcessingException {
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer(String.class))
 .addExternal(new SettableBeanProperty("prop2", Integer.class), new IntegerTypeDeserializer())
 .build());
 handler.handlePropertyValue(parser, ctxt, "prop1", new Object());
 handler.handlePropertyValue(parser, ctxt, "prop1", new Object());
 assertEquals(2, handler.getTypeIds().length);
 }

 @Test
 public void testHandlePropertyValueWithNullBean() throws JsonProcessingException {
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer(String.class))
 .addExternal(new SettableBeanProperty("prop2", Integer.class), new IntegerTypeDeserializer())
 .build());
 handler.handlePropertyValue(parser, ctxt, "prop1", null);
 assertNull(handler.getTokens()[0]);
 }

 @Test
 public void testHandlePropertyValueWithNullTypeId() throws JsonProcessingException {
 handler.handlePropertyValue(parser, ctxt, "prop1", new Object());
 handler.handlePropertyValue(parser, ctxt, "prop1", new Object());
 handler.handlePropertyValue(parser, ctxt, "prop2", new Object());
 handler.handlePropertyValue(parser, ctxt, "prop2", new Object());
 handler.handlePropertyValue(parser, ctxt, "prop1", new Object());
 assertEquals(4, handler.getTokens().length);
 assertEquals(null, handler.getTypeIds()[2]);
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingTypeIdProperty() throws JsonProcessingException {
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer(String.class))
 .addExternal(new SettableBeanProperty("prop2", Integer.class), new IntegerTypeDeserializer())
 .build());
 handler.complete(parser, ctxt, new Object());
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingPropertyForExternalTypeId() throws JsonProcessingException {
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer(String.class))
 .build());
 handler.complete(parser, ctxt, new Object());
 fail("Expected JsonMappingException");
 }

 @Test
 public void testCompleteSuccess() throws JsonProcessingException {
 Object result = handler.start().complete(parser, ctxt, new Object());
 assertEquals(new Object[]{"Foo", "1", "2", "3"}, result.getClass().getComponentType().getComponentValues(result));
 }

 @Test
 public void testCompleteSuccessWithBufferedCreatorProperties() throws JsonProcessingException {
 Object result = handler.start().complete(parser, ctxt, null, new PropertyBasedCreator(){
 @Override public Object build(DeserializationContext ctxt, PropertyValueBuffer buffer){
 return new Object[]{1, 2};
 }});
 assertEquals(new Object[]{"Foo", 1, 2}, result.getClass().getComponentType().getComponentValues(result));
 }
}

class TestDeserializationContext extends DeserializationContext {
 @Override public Object findValueInRoot(Deserializer deserializer, JsonParser p, String name)
 throws IOException {
 throw new UnsupportedOperationException();
 }
}

class StringTypeDeserializer extends StdScalarDeserializer {
 protected StringTypeDeserializer(Class<String> vc) { super(String.class); }
 @Override public String getPropertyName() { return "typeId"; }
}

class IntegerTypeDeserializer extends StdScalarDeserializer {
 protected IntegerTypeDeserializer() { super(Integer.class); }
 @Override public String getPropertyName() { return "typeId"; }
}