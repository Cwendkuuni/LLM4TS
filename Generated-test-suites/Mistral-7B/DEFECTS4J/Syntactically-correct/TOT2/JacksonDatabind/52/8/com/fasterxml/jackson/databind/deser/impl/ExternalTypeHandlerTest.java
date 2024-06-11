package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.*;

/**
 * Test class for ExternalTypeHandler class.
 */
public class ExternalTypeHandlerTest {

 private ObjectMapper mapper;

 @BeforeClass
 public static void setupClass() {
 ObjectMapper mapper = new ObjectMapper();
 }

 @Before
 public void setup() {
 this.mapper = new ObjectMapper();
 }

 /**
 * Test method for {@link ExternalTypeHandler#start()} method.
 *
 * @throws IOException
 */
 @Test
 public void testStart() throws IOException {
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
 ExternalTypeHandler startedHandler = handler.start();
 assertNotNull(startedHandler);
 }

 /**
 * Test method for {@link ExternalTypeHandler#handleTypePropertyValue(JsonParser, DeserializationContext, String, Object)}
 * method with a valid external type id.
 *
 * @throws IOException
 */
 @Test
 public void testHandleTypePropertyValueWithValidTypeId() throws IOException {
 String json = "{\"typeId\": \"StringType\"}";
 JsonParser parser = mapper.getFactory().createParser(json);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
 handler.handleTypePropertyValue(parser, ctxt, "typeId", null);
 assertTrue(handler._typeIds[0] != null);
 assertEquals("StringType", handler._typeIds[0]);
 }

 /**
 * Test method for {@link ExternalTypeHandler#handleTypePropertyValue(JsonParser, DeserializationContext, String, Object)}
 * method with an invalid external type id.
 *
 * @throws IOException
 */
 @Test(expected = IOException.class)
 public void testHandleTypePropertyValueWithInvalidTypeId() throws IOException {
 String json = "{\"typeId\": 42}";
 JsonParser parser = mapper.getFactory().createParser(json);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
 handler.handleTypePropertyValue(parser, ctxt, "typeId", null);
 fail("Should have thrown an IOException.");
 }

 /**
 * Test method for {@link ExternalTypeHandler#handlePropertyValue(JsonParser, DeserializationContext, String, Object)}
 * method with a valid property value.
 *
 * @throws IOException
 */
 @Test
 public void testHandlePropertyValueWithValidPropertyValue() throws IOException {
 String json = "{\"name\": \"John\", \"age\": 30}";
 Object bean = new Object();
 JsonParser parser = mapper.getFactory().createParser(json);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
 handler.handlePropertyValue(parser, ctxt, "name", bean);
 assertEquals("John", handler._tokens[0].asText());
 handler.handlePropertyValue(parser, ctxt, "age", bean);
 assertEquals(30, handler._tokens[1].asInt());
 }

 /**
 * Test method for {@link ExternalTypeHandler#handlePropertyValue(JsonParser, DeserializationContext, String, Object)}
 * method with an invalid property value.
 *
 * @throws IOException
 */
 @Test(expected = IOException.class)
 public void testHandlePropertyValueWithInvalidPropertyValue() throws IOException {
 String json = "{\"name\": [1, 2, 3], \"age\": \"John\"}";
 Object bean = new Object();
 JsonParser parser = mapper.getFactory().createParser(json);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
 handler.handlePropertyValue(parser, ctxt, "name", bean);
 fail("Should have thrown an IOException.");
 }

 /**
 * Test method for {@link ExternalTypeHandler#complete(JsonParser, DeserializationContext, Object)} method
 * with missing type ids.
 *
 * @throws IOException
 */
 @Test(expected = IOException.class)
 public void testCompleteWithMissingTypeIds() throws IOException {
 String json = "{\"name\": \"John\"}";
 Object bean = new Object();
 JsonParser parser = mapper.getFactory().createParser(json);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
 handler.handlePropertyValue(parser, ctxt, "name", bean);
 handler.complete(parser, ctxt, bean);
 fail("Should have thrown an IOException.");
 }

 /**
 * Test method for {@link ExternalTypeHandler#complete(JsonParser, DeserializationContext, Object)} method
 * with missing properties.
 *
 * @throws IOException
 */
 @Test(expected = IOException.class)
 public void testCompleteWithMissingProperties() throws IOException {
 String json = "{\"typeId\": \"StringType\"}";
 Object bean = new Object();
 JsonParser parser = mapper.getFactory().createParser(json);
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
 handler.handleTypePropertyValue(parser, null, "typeId", bean);
 handler.complete(parser, null, bean);
 fail("Should have thrown an IOException.");
 }

 /**
 * Test method for {@link ExternalTypeHandler#complete(JsonParser, DeserializationContext, Object)} method
 * with missing external type id properties.
 *
 * @throws IOException
 */
 @Test(expected = IOException.class)
 public void testCompleteWithMissingExternalTypeIdProperties() throws IOException {
 String json = "{\"name\": \"John\"}";
 Object bean = new Object();
 JsonParser parser = mapper.getFactory().createParser(json);
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
 handler.handlePropertyValue(parser, null, "name", bean);
 handler.complete(parser, null, bean);
 fail("Should have thrown an IOException.");
 }

 /**
 * Test method for {@link ExternalTypeHandler#complete(JsonParser, DeserializationContext, Object)} method
 * with natural types.
 *
 * @throws IOException
 */
 @Test
 public void testCompleteWithNaturalTypes() throws IOException {
 String json = "{\"name\": null, \"int\": 42, \"double\": 3.14, \"boolean\": true}";
 Object bean = new Object();
 JsonParser parser = mapper.getFactory().createParser(json);
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
 handler.handlePropertyValue(parser, null, "name", bean);
 handler.handlePropertyValue(parser, null, "int", bean);
 handler.handlePropertyValue(parser, null, "double", bean);
 handler.handlePropertyValue(parser, null, "boolean", bean);
 handler.complete(parser, null, bean);
 assertEquals(null, bean.getClass().getField("name").get(bean));
 assertEquals(42, bean.getClass().getField("int").get(bean));
 assertEquals(3.14, bean.getClass().getField("double").get(bean));
 assertEquals(true, bean.getClass().getField("boolean").get(bean));
 }

 /**
 * Test method for {@link ExternalTypeHandler#complete(JsonParser, DeserializationContext, Object)} method
 * with a custom creator.
 *
 * @throws IOException
 */
 @Test
 public void testCompleteWithCreator() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("[]");
 PropertyValueBuffer buffer = new PropertyValueBuffer();
 PropertyBasedCreator creator = new PropertyBasedCreator() {
 @Override
 public Object build(DeserializationContext ctxt, PropertyValueBuffer buffer) throws IOException {
 return new MyClass(buffer.getBooleanValue(0), buffer.getIntValue(1));
 }
 };
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
 handler.handlePropertyValue(parser, null, "bool", null);
 handler.handlePropertyValue(parser, null, "int", null);
 handler.complete(parser, null, buffer, creator);
 MyClass obj = (MyClass) handler.getDeserializedValue();
 assertEquals(true, obj.getBooleanField());
 assertEquals(1, obj.getIntField());
 }

 /**
 * Test method for {@link ExternalTypeHandler#complete(JsonParser, DeserializationContext, PropertyValueBuffer, PropertyBasedCreator)}
 * method with missing creator properties.
 *
 * @throws IOException
 */
 @Test(expected = IOException.class)
 public void testCompleteWithMissingCreatorProperties() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("[]");
 PropertyValueBuffer buffer = new PropertyValueBuffer();
 PropertyBasedCreator creator = new PropertyBasedCreator() {
 @Override
 public Object build(DeserializationContext ctxt, PropertyValueBuffer buffer) throws IOException {
 return new MyClass(buffer.getBooleanValue(0), buffer.getIntValue(1));
 }
 };
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
 handler.handlePropertyValue(parser, null, "bool", null);
 handler.handlePropertyValue(parser, null, "int", null);
 handler.complete(parser, null, buffer, creator);
 fail("Should have thrown an IOException.");
 }

 /**
 * Test method for {@link ExternalTypeHandler#_deserialize(JsonParser, DeserializationContext, int, String)} method.
 *
 * @throws IOException
 */
 @Test
 public void test_deserialize() throws IOException {
 String json = "[\"StringType\"]";
 Object bean = new Object();
 JsonParser parser = mapper.getFactory().createParser(json);
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
 Object result = handler._deserialize(parser, null, 0, "StringType");
 assertEquals("StringType", result);
 }

 /**
 * Test method for {@link ExternalTypeHandler#_deserializeAndSet(JsonParser, DeserializationContext, Object, int, String)} method.
 *
 * @throws IOException
 */
 @Test
 public void test_deserializeAndSet() throws IOException {
 String json = "[\"StringType\" : \"John\"]";
 Object bean = new Object();
 JsonParser parser = mapper.getFactory().createParser(json);
 ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
 handler._deserializeAndSet(parser, null, bean, 0, "StringType");
 assertEquals("John", bean.getClass().getField("value").get(bean));
 }

 /**
 * Test method for {@link ExternalTypeHandler#Builder} class.
 *
 * @throws IOException
 */
 @Test
 public void testBuilder() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
 SettableBeanProperty prop1 = new SettableBeanProperty(MyClass.class, "value");
 TypeDeserializer typeDeserializer1 = new StringDeserializer();
 SettableBeanProperty prop2 = new SettableBeanProperty(MyClass.class, "type");
 TypeDeserializer typeDeserializer2 = new SimpleTypeDeserializer(String.class);
 builder.addExternal(prop1, typeDeserializer1);
 builder.addExternal(prop2, typeDeserializer2);
 ExternalTypeHandler handler = builder.build();
 }

}