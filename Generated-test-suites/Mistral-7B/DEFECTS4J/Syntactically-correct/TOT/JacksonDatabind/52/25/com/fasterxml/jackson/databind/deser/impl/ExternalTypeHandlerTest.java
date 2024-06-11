package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private ObjectMapper mapper;

 @Before
 public void setUp() throws Exception {
 ObjectNode node = mapper.createObjectNode();
 node.put("name1", "value1");
 node.put("typeId1", "type1");
 node.put("name2", "value2");
 node.put("typeId2", "type2");

 mapper = new ObjectMapper();
 handler = mapper.getDeserializationConfig().withoutTypeIdResolver()
 .withoutAnnotationIntrospector()
 .withoutSerializationFeature(SerializationFeature.FAIL_ON_EMPTY_BEANS)
 .withoutDefaultTyping()
 .withRootName("root")
 .readValue(mapper.writeValueAsString(node), ExternalTypeHandler.class);
 }

 @Test
 public void testHandleTypePropertyValue() throws Exception {
 JsonParser p = mapper.getFactory().createParser(mapper.writeValueAsString("{\"name1\":\"value1\",\"typeId1\":\"type1\"}"));
 Object bean = new Object();
 handler.handleTypePropertyValue(p, null, "name1", bean);
 assertEquals("type1", handler._typeIds[0]);
 }

 @Test
 public void testHandlePropertyValue_noTypeId() throws Exception {
 JsonParser p = mapper.getFactory().createParser(mapper.writeValueAsString("{\"name1\":\"value1\",\"name2\":\"value2\"}"));
 Object bean = new Object();
 handler.handlePropertyValue(p, null, "name1", bean);
 assertNull(handler._typeIds[0]);
 }

 @Test
 public void testHandlePropertyValue_noBean() throws Exception {
 JsonParser p = mapper.getFactory().createParser(mapper.writeValueAsString("{\"name1\":\"value1\",\"name2\":\"value2\"}"));
 handler.handlePropertyValue(p, null, "name1", null);
 assertNull(handler._typeIds[0]);
 }

 @Test
 public void testHandlePropertyValue_noIndex() throws Exception {
 JsonParser p = mapper.getFactory().createParser(mapper.writeValueAsString("{\"name1\":\"value1\",\"name2\":\"value2\",\"typeId1\":\"type1\"}"));
 try {
 handler.handlePropertyValue(p, null, "name3", new Object());
 fail();
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testComplete() throws Exception {
 Object root = handler.start();
 handler.complete(mapper.getFactory().createParser(mapper.writeValueAsString("{}}")), null, root);
 assertEquals(2, handler._properties.length);
 assertEquals("type1", handler._typeIds[0]);
 assertEquals("type2", handler._typeIds[1]);
 }

 @Test(expected = JsonProcessingException.class)
 public void testComplete_missingTypeId() throws Exception {
 Object root = handler.start();
 handler.complete(mapper.getFactory().createParser(mapper.writeValueAsString("{name1:value1,name2:value2}")), null, root);
 fail("Expected a JsonProcessingException");
 }

 @Test
 public void testComplete_missingProperty() throws Exception {
 Object root = handler.start();
 handler.complete(mapper.getFactory().createParser(mapper.writeValueAsString("{typeId1:type1}")), null, root);
 fail("Expected a JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testComplete_wrongTypeId() throws Exception {
 Object root = handler.start();
 handler.complete(mapper.getFactory().createParser(mapper.writeValueAsString("{name1:value1,typeId3:type1}")), null, root);
 fail("Expected a JsonMappingException");
 }

 @Test
 public void testComplete_nullTypeId() throws Exception {
 Object root = handler.start();
 handler.complete(mapper.getFactory().createParser(mapper.writeValueAsString("{name1:value1}")), null, root);
 assertNull(handler._typeIds[0]);
 }

 @Test
 public void testComplete_noTypeDeserializer() throws Exception {
 ExtTypedProperty property = new ExtTypedProperty(new SimpleBeanProperty("name", String.class), null);
 ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
 builder.addExternal(property, null);
 ExternalTypeHandler handler = builder.build();
 handler.start();
 try {
 handler.complete(mapper.getFactory().createParser(mapper.writeValueAsString("{typeId:type1}")), null, new Object());
 fail();
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testComplete_customTypeDeserializer() throws Exception {
 Class<? extends Object> clazz = MyCustomClass.class;
 TypeDeserializer deserializer = mapper.getTypeFactory().constructValueTypeDeserializer(clazz, null);
 ExtTypedProperty property = new ExtTypedProperty(new SimpleBeanProperty("name", String.class), deserializer);
 ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
 builder.addExternal(property, deserializer);
 ExternalTypeHandler handler = builder.build();
 handler.start();
 handler.complete(mapper.getFactory().createParser(mapper.writeValueAsString("{typeId:type1}")), null, new Object());
 assertEquals(clazz, handler._properties[0].getTypeDeserializer().getType());
 }

 @Test(expected = JsonMappingException.class)
 public void testComplete_missingTypeProperty() throws Exception {
 Class<? extends Object> clazz = MyCustomClass.class;
 TypeDeserializer deserializer = mapper.getTypeFactory().constructValueTypeDeserializer(clazz, null);
 ExtTypedProperty property = new ExtTypedProperty(new SimpleBeanProperty("name", String.class), deserializer);
 property.setTypeProperty(new SimpleBeanProperty("type", String.class));
 ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
 builder.addExternal(property, deserializer);
 ExternalTypeHandler handler = builder.build();
 handler.start();
 handler.complete(mapper.getFactory().createParser(mapper.writeValueAsString("{typeId:type1}")), null, new Object());
 fail("Expected a JsonMappingException");
 }

 @Test
 public void testComplete_customTypeProperty() throws Exception {
 Class<? extends Object> clazz = MyCustomClass.class;
 Class<String> stringType = String.class;
 TypeDeserializer deserializer = mapper.getTypeFactory().constructValueTypeDeserializer(clazz, null);
 ExtTypedProperty property = new ExtTypedProperty(new SimpleBeanProperty("name", String.class), deserializer);
 property.setTypeProperty(new SimpleBeanProperty("type", stringType));
 ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
 builder.addExternal(property, deserializer);
 ExternalTypeHandler handler = builder.build();
 handler.start();
 handler.complete(mapper.getFactory().createParser(mapper.writeValueAsString("{typeId:type1, type:StringType}")), null, new Object());
 assertEquals("StringType", handler._typeIds[0]);
 }

 @Test
 public void testComplete_customTypeDeserializerAndProperty() throws Exception {
 Class<? extends Object> clazz = MyCustomClass.class;
 Class<String> stringType = String.class;
 TypeDeserializer deserializer = mapper.getTypeFactory().constructValueTypeDeserializer(clazz, null);
 ExtTypedProperty property = new ExtTypedProperty(new SimpleBeanProperty("name", String.class), deserializer);
 property.setTypeDeserializer(mapper.getTypeFactory().constructValueTypeDeserializer(stringType, null));
 property.setTypeProperty(new SimpleBeanProperty("type", stringType));
 ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
 builder.addExternal(property, deserializer);
 ExternalTypeHandler handler = builder.build();
 handler.start();
 handler.complete(mapper.getFactory().createParser(mapper.writeValueAsString("{typeId:type1, type:StringType}")), null, new Object());
 assertEquals(stringType, handler._properties[0].getTypeDeserializer().getType());
 }

 @Test
 public void testComplete_multipleTypeIds() throws Exception {
 Class<? extends Object> clazz1 = MyCustomClass1.class;
 Class<? extends Object> clazz2 = MyCustomClass2.class;
 TypeDeserializer deserializer1 = mapper.getTypeFactory().constructValueTypeDeserializer(clazz1, null);
 TypeDeserializer deserializer2 = mapper.getTypeFactory().constructValueTypeDeserializer(clazz2, null);
 ExtTypedProperty property1 = new ExtTypedProperty(new SimpleBeanProperty("name1", String.class), deserializer1);
 property1.setTypeProperty(new SimpleBeanProperty("typeId1", String.class));
 ExtTypedProperty property2 = new ExtTypedProperty(new SimpleBeanProperty("name2", String.class), deserializer2);
 property2.setTypeProperty(new SimpleBeanProperty("typeId2", String.class));
 ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
 builder.addExternal(property1, deserializer1);
 builder.addExternal(property2, deserializer2);
 ExternalTypeHandler handler = builder.build();
 handler.start();
 handler.complete(mapper.getFactory().createParser(mapper.writeValueAsString("{" +
 "\"name1\":\"value1\",\"typeId1\":\"type1\"" +
 ",\"name2\":\"value2\",\"typeId2\":\"type2\"" +
 "}")), null, new Object());
 assertEquals(clazz1, handler._properties[0].getTypeDeserializer().getType());
 assertEquals(clazz2, handler._properties[1].getTypeDeserializer().getType());
 }

 @Test
 public void testComplete_nullTypeIds() throws Exception {
 Class<? extends Object> clazz = MyCustomClass.class;
 TypeDeserializer deserializer = mapper.getTypeFactory().constructValueTypeDeserializer(clazz, null);
 ExtTypedProperty property = new ExtTypedProperty(new SimpleBeanProperty("name", String.class), deserializer);
 property.setTypeProperty(new SimpleBeanProperty("type", String.class));
 ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
 builder.addExternal(property, deserializer);
 ExternalTypeHandler handler = builder.build();
 handler.start();
 handler.complete(mapper.getFactory().createParser(mapper.writeValueAsString("{typeId:null, type:StringType}")), null, new Object());
 assertNull(handler._typeIds[0]);
 }

 @Test
 public void testComplete_nullTypeIdAndTypeProperty() throws Exception {
 Class<? extends Object> clazz = MyCustomClass.class;
 TypeDeserializer deserializer = mapper.getTypeFactory().constructValueTypeDeserializer(clazz, null);
 ExtTypedProperty property = new ExtTypedProperty(new SimpleBeanProperty("name", String.class), deserializer);
 property.setTypeProperty(new SimpleBeanProperty("type", String.class));
 ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
 builder.addExternal(property, deserializer);
 ExternalTypeHandler handler = builder.build();
 handler.start();
 handler.complete(mapper.getFactory().createParser(mapper.writeValueAsString("{typeId:null, type:null}")), null, new Object());
 assertNull(handler._typeIds[0]);
 }

 @Test
 public void testComplete_noTypeIdAndTypeProperty() throws Exception {
 Class<? extends Object> clazz = MyCustomClass.class;
 TypeDeserializer deserializer = mapper.getTypeFactory().constructValueTypeDeserializer(clazz, null);
 ExtTypedProperty property = new ExtTypedProperty(new SimpleBeanProperty("name", String.class), deserializer);
 ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
 builder.addExternal(property, deserializer);
 ExternalTypeHandler handler = builder.build();
 handler.start();
 try {
 handler.complete(mapper.getFactory().createParser(mapper.writeValueAsString("{name:value}")), null, new Object());
 fail();
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testComplete_noTypeIdAndTypeProperty_nullTypeDeserializer() throws Exception {
 Class<? extends Object> clazz = MyCustomClass.class;
 ExtTypedProperty property = new ExtTypedProperty(new SimpleBeanProperty("name", String.class), null);
 property.setTypeProperty(new SimpleBeanProperty("type", String.class));
 ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
 builder.addExternal(property, null);
 ExternalTypeHandler handler = builder.build();
 handler.start();
 try {
 handler.complete(mapper.getFactory().createParser(mapper.writeValueAsString("{name:value}")), null, new Object());
 fail();
 } catch (JsonMappingException e) {
 // expected
 }
 }
}