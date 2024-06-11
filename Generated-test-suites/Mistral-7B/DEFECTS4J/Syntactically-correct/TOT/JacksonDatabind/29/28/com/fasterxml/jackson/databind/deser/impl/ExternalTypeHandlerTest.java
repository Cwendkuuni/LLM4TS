package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private ObjectMapper mapper;

 @Before
 public void setup() throws JsonProcessingException {
 mapper = new ObjectMapper();
 ObjectNode node = mapper.createObjectNode();
 node.put("name", "John Doe");
 node.put("age", 30);
 node.put("typeId", "com.example.PersonType");
 handler = new ExternalTypeHandler(new ExtTypedProperty[] {
 new ExtTypedProperty(new SimpleBeanProperty("name", String.class), new StdScalarTypeDeserializer(String.class)),
 new ExtTypedProperty(new SimpleBeanProperty("age", Integer.class), new StdScalarTypeDeserializer(Integer.class))
 }, new HashMap<>(), new String[] {"com.example.PersonType"}, new TokenBuffer[2]);
 }

 @Test
 public void testHandleTypePropertyValue() throws JsonProcessingException {
 JsonParser jp = mapper.getFactory().createParser(node.toString());
 handler.handleTypePropertyValue(jp, new TestContext(), "typeId", null);
 assertEquals("com.example.PersonType", handler._typeIds[0]);
 }

 @Test
 public void testHandleTypePropertyValueWithNullBean() throws JsonProcessingException {
 JsonParser jp = mapper.getFactory().createParser(node.toString());
 handler.handleTypePropertyValue(jp, new TestContext(), "typeId", null);
 assertNull(handler._tokens[0]);
 }

 @Test
 public void testHandlePropertyValue() throws JsonProcessingException {
 JsonParser jp = mapper.getFactory().createParser(node.toString());
 handler.handlePropertyValue(jp, new TestContext(), "name", null);
 assertEquals("John Doe", handler._properties[0].getProperty().get(handler.start().getBean()));
 }

 @Test
 public void testHandlePropertyValueWithNullBeanAndTypeIdSet() throws JsonProcessingException {
 JsonParser jp = mapper.getFactory().createParser(node.toString());
 handler.handlePropertyValue(jp, new TestContext(), "name", null);
 assertEquals("John Doe", handler._properties[0].getProperty().get(handler.start().getBean()));
 handler.handleTypePropertyValue(jp, new TestContext(), "typeId", handler.start().getBean());
 assertEquals("com.example.PersonType", handler._typeIds[0]);
 }

 @Test
 public void testHandlePropertyValueWithNullBeanAndNoTypeIdSet() throws JsonProcessingException {
 JsonParser jp = mapper.getFactory().createParser(node.toString());
 handler.handlePropertyValue(jp, new TestContext(), "name", null);
 assertEquals("John Doe", handler._properties[0].getProperty().get(handler.start().getBean()));
 }

 @Test
 public void testCompleteWithNullBean() throws JsonProcessingException {
 JsonParser jp = mapper.getFactory().createParser(node.toString());
 handler.handlePropertyValue(jp, new TestContext(), "name", null);
 handler.handlePropertyValue(jp, new TestContext(), "age", null);
 Object result = handler.complete(jp, new TestContext(), null);
 assertNull(result);
 }

 @Test
 public void testCompleteWithBean() throws JsonProcessingException {
 JsonParser jp = mapper.getFactory().createParser(node.toString());
 handler.handlePropertyValue(jp, new TestContext(), "name", new Object());
 handler.handlePropertyValue(jp, new TestContext(), "age", new Object());
 Object result = handler.complete(jp, new TestContext(), new Object());
 assertEquals(30, result.getClass().getMethod("getAge").invoke(result));
 assertEquals("John Doe", result.getClass().getMethod("getName").invoke(result));
 }

 @Test
 public void testCompleteWithPropertyBasedCreator() throws JsonProcessingException {
 PropertyBasedCreator creator = new TestPropertyBasedCreator();
 JsonParser jp = mapper.getFactory().createParser(node.toString());
 handler.handlePropertyValue(jp, new TestContext(), "name", null);
 handler.handlePropertyValue(jp, new TestContext(), "age", null);
 Object result = handler.complete(jp, new TestContext(), new PropertyValueBuffer(), creator);
 assertEquals(30, result.getClass().getMethod("getAge").invoke(result));
 assertEquals("John Doe", result.getClass().getMethod("getName").invoke(result));
 }

 @Test
 public void testCompleteWithMissingProperty() throws JsonProcessingException {
 JsonParser jp = mapper.getFactory().createParser(node.toString());
 handler.handlePropertyValue(jp, new TestContext(), "name", new Object());
 handler.handlePropertyValue(jp, new TestContext(), "age", new Object());
 handler.handleTypePropertyValue(jp, new TestContext(), "typeId", handler.start().getBean());
 handler.handlePropertyValue(jp, new TestContext(), "unknown", null);
 try {
 Object result = handler.complete(jp, new TestContext(), null);
 fail();
 } catch (MappingException e) {
 assertEquals("Missing property 'unknown' for external type id 'com.example.PersonType'", e.getMessage());
 }
 }

 @Test
 public void testCompleteWithMissingTypeId() throws JsonProcessingException {
 JsonParser jp = mapper.getFactory().createParser(node.toString());
 handler.handlePropertyValue(jp, new TestContext(), "name", null);
 handler.handlePropertyValue(jp, new TestContext(), "name", new Object());
 handler.handlePropertyValue(jp, new TestContext(), "age", null);
 try {
 Object result = handler.complete(jp, new TestContext(), null);
 fail();
 } catch (MappingException e) {
 assertEquals("Missing external type id property 'typeId'", e.getMessage());
 }
 }

 @Test
 public void testCompleteWithNullTypeId() throws JsonProcessingException {
 JsonParser jp = mapper.getFactory().createParser(node.toString());
 handler.handlePropertyValue(jp, new TestContext(), "name", null);
 handler.handlePropertyValue(jp, new TestContext(), "name", new Object());
 handler.handlePropertyValue(jp, new TestContext(), "age", null);
 handler.handleTypePropertyValue(jp, new TestContext(), null, handler.start().getBean());
 try {
 Object result = handler.complete(jp, new TestContext(), null);
 fail();
 } catch (MappingException e) {
 assertEquals("Missing external type id property 'typeId'", e.getMessage());
 }
 }

 @Test
 public void testCompleteWithNestedObjects() throws JsonProcessingException {
 ObjectNode node2 = mapper.createObjectNode();
 node2.put("name", "Jane Doe");
 node2.put("age", 25);
 node2.put("typeId", "com.example.AnotherType");
 JsonParser jp = mapper.getFactory().createParser(node.toString() + ",\n" + node2.toString());
 handler.handlePropertyValue(jp, new TestContext(), "name", null);
 handler.handlePropertyValue(jp, new TestContext(), "age", null);
 handler.handlePropertyValue(jp, new TestContext(), "typeId", handler.start().getBean());
 handler.handlePropertyValue(jp, new TestContext(), "person", null);
 handler.handlePropertyValue(jp, new TestContext(), "person.name", null);
 handler.handlePropertyValue(jp, new TestContext(), "person.age", null);
 handler.handlePropertyValue(jp, new TestContext(), "person.typeId", handler.start().getBean());
 Object result = handler.complete(jp, new TestContext(), null);
 assertEquals(25, result.getClass().getMethod("getAge").invoke(result.getClass().getMethod("getPerson").invoke(result)));
 assertEquals("Jane Doe", result.getClass().getMethod("getPerson").invoke(result).getClass().getMethod("getName").invoke(result.getClass().getMethod("getPerson").invoke(result)));
 assertEquals("com.example.AnotherType", result.getClass().getMethod("getTypeId").invoke(result));
 }

 @Test
 public void testCompleteWithNestedObjectsAndCreator() throws JsonProcessingException {
 PropertyBasedCreator creator = new TestPropertyBasedCreator();
 ObjectNode node2 = mapper.createObjectNode();
 node2.put("name", "Jane Doe");
 node2.put("age", 25);
 node2.put("typeId", "com.example.AnotherType");
 JsonParser jp = mapper.getFactory().createParser(node.toString() + ",\n" + node2.toString());
 handler.handlePropertyValue(jp, new TestContext(), "name", null);
 handler.handlePropertyValue(jp, new TestContext(), "age", null);
 handler.handlePropertyValue(jp, new TestContext(), "typeId", handler.start().getBean());
 handler.handlePropertyValue(jp, new TestContext(), "person", null);
 handler.handlePropertyValue(jp, new TestContext(), "person.name", null);
 handler.handlePropertyValue(jp, new TestContext(), "person.age", null);
 handler.handlePropertyValue(jp, new TestContext(), "person.typeId", handler.start().getBean());
 Object result = handler.complete(jp, new TestContext(), new PropertyValueBuffer(), creator);
 assertEquals(25, result.getClass().getMethod("getAge").invoke(result.getClass().getMethod("getPerson").invoke(result)));
 assertEquals("Jane Doe", result.getClass().getMethod("getPerson").invoke(result).getClass().getMethod("getName").invoke(result.getClass().getMethod("getPerson").invoke(result)));
 assertEquals("com.example.AnotherType", result.getClass().getMethod("getTypeId").invoke(result));
 }
}

class TestContext extends DeserializationContext {
 @Override
 public ObjectMapper _rootContext() {
 return null;
 }

 @Override
 public Object findValueInRoot(String name) {
 return null;
 }

 @Override
 public Object findCreator(Class<?> valueType) {
 return null;
 }

 @Override
 public void handleBindingException(BindingException ex) {
 fail();
 }
}

class TestPropertyBasedCreator implements PropertyBasedCreator {
 @Override
 public Object createUsingDefaults() {
 return new Object();
 }

 @Override
 public Object createFromProperties(PropertyValueBuffer buffer) {
 return buffer.readValueAs(Object.class);
 }

 @Override
 public PropertyDescriptor findCreatorProperty(String name) {
 return null;
 }
}