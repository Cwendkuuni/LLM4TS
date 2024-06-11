package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ExternalTypeHandlerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();
 private ExternalTypeHandler handler;

 @Before
 public void setUp() throws Exception {
 ObjectNode node = MAPPER.createObjectNode();
 node.put("name", "John Doe");
 node.put("typeId", "com.test.Person");
 ExtTypedProperty[] properties = new ExtTypedProperty[] {
 new ExtTypedProperty(SettableBeanProperty.stdProperty("age"), new SimpleTypeDeserializer(Integer.class)),
 new ExtTypedProperty(SettableBeanProperty.stdProperty("address"), new ObjectIdReader(Person.class))
 };
 handler = new ExternalTypeHandler(properties, new HashMap<>(), null, null);
 }

 @Test
 public void testHandleTypePropertyValue() throws Exception {
 JsonParser p = MAPPER.getFactory().createParser(node.toString());
 DeserializationContext ctxt = MAPPER.getDeserializationContext();
 Object bean = new Object();
 handler.handleTypePropertyValue(p, ctxt, "typeId", bean);
 assertEquals("com.test.Person", handler._typeIds[0]);
 }

 @Test
 public void testHandlePropertyValue() throws Exception {
 JsonParser p = MAPPER.getFactory().createParser(node.toString());
 DeserializationContext ctxt = MAPPER.getDeserializationContext();
 Object bean = new Object();
 handler.handlePropertyValue(p, ctxt, "name", bean);
 assertEquals("John Doe", handler._properties[0].getProperty().get(bean));
 handler.handlePropertyValue(p, ctxt, "typeId", bean);
 assertEquals("com.test.Person", handler._properties[1].getProperty().get(bean));
 }

 @Test(expected = IOException.class)
 public void testMissingTypePropertyValue() throws Exception {
 JsonParser p = MAPPER.getFactory().createParser(node.toString());
 DeserializationContext ctxt = MAPPER.getDeserializationContext();
 Object bean = new Object();
 handler.handlePropertyValue(p, ctxt, "unknown", bean);
 }

 @Test
 public void testMissingPropertyValue() throws Exception {
 JsonParser p = MAPPER.getFactory().createParser(node.toString());
 DeserializationContext ctxt = MAPPER.getDeserializationContext();
 Object bean = new Object();
 handler.handlePropertyValue(p, ctxt, "typeId", bean);
 handler.handlePropertyValue(p, ctxt, "age", bean);
 assertNull(handler._properties[1].getProperty().get(bean));
 }

 @Test(expected = com.fasterxml.jackson.databind.JsonMappingException.class)
 public void testMissingExternalTypeIdProperty() throws Exception {
 JsonParser p = MAPPER.getFactory().createParser(node.toString());
 DeserializationContext ctxt = MAPPER.getDeserializationContext();
 Object bean = new Object();
 handler.handlePropertyValue(p, ctxt, "name", bean);
 handler.handlePropertyValue(p, ctxt, "age", bean);
 handler.complete(p, ctxt, bean);
 }

 @Test
 public void testComplete() throws Exception {
 JsonParser p = MAPPER.getFactory().createParser(node.toString());
 DeserializationContext ctxt = MAPPER.getDeserializationContext();
 Object bean = new Person();
 handler.handlePropertyValue(p, ctxt, "name", bean);
 handler.handlePropertyValue(p, ctxt, "typeId", bean);
 handler.handlePropertyValue(p, ctxt, "age", bean);
 handler.complete(p, ctxt, bean);
 assertNotNull(bean.getClass().getName().equals("com.test.Person"));
 assertEquals(30, handler.getProperty("age").get(bean));
 }

 @Test
 public void testCompleteWithNullValue() throws Exception {
 JsonParser p = MAPPER.getFactory().createParser(node.toString());
 DeserializationContext ctxt = MAPPER.getDeserializationContext();
 Object bean = new Person();
 handler.handlePropertyValue(p, ctxt, "name", bean);
 handler.handlePropertyValue(p, ctxt, "typeId", bean);
 handler.handlePropertyValue(p, ctxt, "age", bean);
 handler.handlePropertyValue(p, ctxt, "address", bean);
 handler.handlePropertyValue(p, ctxt, "address.street", bean);
 handler.handlePropertyValue(p, ctxt, "address.city", bean);
 handler.handlePropertyValue(p, ctxt, "address.zipCode", bean);
 handler.handlePropertyValue(p, ctxt, "address.zipCode", bean); // Null value
 handler.complete(p, ctxt, bean);
 assertNotNull(bean.getClass().getName().equals("com.test.Person"));
 assertNull(handler.getProperty("address.zipCode").get(bean));
 }

 @Test
 public void testCompleteWithMissingCreatorProperty() throws Exception {
 JsonParser p = MAPPER.getFactory().createParser(node.toString());
 DeserializationContext ctxt = MAPPER.getDeserializationContext();
 PropertyValueBuffer buffer = new PropertyValueBuffer();
 PersonCreator creator = new PersonCreator();
 handler.handlePropertyValue(p, ctxt, "name", buffer);
 handler.handlePropertyValue(p, ctxt, "typeId", buffer);
 handler.handlePropertyValue(p, ctxt, "age", buffer);
 handler.handlePropertyValue(p, ctxt, "address.street", buffer);
 handler.handlePropertyValue(p, ctxt, "address.city", buffer);
 handler.handlePropertyValue(p, ctxt, "address.zipCode", buffer);
 handler.complete(p, ctxt, buffer, creator);
 Person person = creator.build(ctxt, buffer);
 assertNotNull(person.getClass().getName().equals("com.test.Person"));
 assertEquals(30, person.getAge());
 assertEquals("Main St", person.getAddress().getStreet());
 assertNull(person.getAddress().getCity());
 assertNull(person.getAddress().getZipCode());
 }
}

class Person {
 private int age;
 private Address address;

 public int getAge() { return age; }
 public void setAge(int age) { this.age = age; }
 public Address getAddress() { return address; }
 public void setAddress(Address address) { this.address = address; }

 static class Address {
 private String street;
 private String city;
 private String zipCode;

 public String getStreet() { return street; }
 public void setStreet(String street) { this.street = street; }
 public String getCity() { return city; }
 public void setCity(String city) { this.city = city; }
 public String getZipCode() { return zipCode; }
 public void setZipCode(String zipCode) { this.zipCode = zipCode; }
 }
}

class PersonCreator implements PropertyBasedCreator<Person> {
 @Override
 public Person createInstance(DeserializationContext ctxt, PropertyValueBuffer valueProps)
 throws IOException {
 Person p = new Person();
 p.setAge(valueProps.nextValue().asInt());
 p.setAddress(valueProps.nextValue(Address.class));
 return p;
 }

 @Override
 public Map<String, String> getParameterNames() {
 Map<String, String> map = new HashMap<>();
 map.put("age", "age");
 map.put("address", "address");
 return map;
 }
}