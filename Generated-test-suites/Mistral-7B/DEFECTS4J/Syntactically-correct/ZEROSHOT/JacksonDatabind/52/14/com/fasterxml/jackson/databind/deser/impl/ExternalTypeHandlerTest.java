package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 handler = new ExternalTypeHandler.Builder()
 .addExternal(new TestBean.NameProperty("name"), new NameTypeDeserializer())
 .addExternal(new TestBean.AgeProperty("age"), new IntegerDeserializer())
 .build();
 }

 @After
 public void teardown() throws IOException {
 mapper.close();
 }

 @Test
 public void testHandleTypePropertyValue() throws IOException {
 JsonParser p = mapper.getFactory().createParser("{\"name\":\"John\",\"type\":\"Person\"}");
 TestBean bean = mapper.readValue(p, TestBean.class);
 handler.start();
 assertTrue(handler.handleTypePropertyValue(p, mapper.getDeserializationContext(), "type", bean));
 assertEquals("Person", bean.getType());
 }

 @Test
 public void testHandlePropertyValue() throws IOException {
 JsonParser p = mapper.getFactory().createParser("{\"name\":\"John\",\"age\":42}");
 TestBean bean = mapper.readValue(p, TestBean.class);
 handler.start();
 assertTrue(handler.handlePropertyValue(p, mapper.getDeserializationContext(), "name", bean));
 assertEquals("John", bean.getName());
 assertTrue(handler.handlePropertyValue(p, mapper.getDeserializationContext(), "age", bean));
 assertEquals(42, bean.getAge());
 }

 @Test
 public void testComplete() throws IOException {
 JsonParser p = mapper.getFactory().createParser("{\"name\":\"John\",\"type\":\"Person\",\"age\":42}");
 TestBean bean = mapper.readValue(p, TestBean.class);
 handler.start();
 handler.complete(p, mapper.getDeserializationContext(), bean);
 assertEquals("John", bean.getName());
 assertEquals(42, bean.getAge());
 assertEquals("Person", bean.getType());
 }

 @Test(expected = IOException.class)
 public void testMissingTypeIdProperty() throws IOException {
 JsonParser p = mapper.getFactory().createParser("{\"name\":\"John\",\"age\":42}");
 TestBean bean = mapper.readValue(p, TestBean.class);
 handler.start();
 handler.complete(p, mapper.getDeserializationContext(), bean);
 fail("Expected IOException to be thrown");
 }

 @Test(expected = IOException.class)
 public void testMissingPropertyForTypeId() throws IOException {
 TestBean bean = mapper.readValue(mapper.getFactory().createParser("{\"type\":\"Person\"}"), TestBean.class);
 handler.start();
 handler.complete(mapper.getFactory().createParser("{\"type\":\"Person\"}"), mapper.getDeserializationContext(), bean);
 fail("Expected IOException to be thrown");
 }

 @Test
 public void testCompleteWithCreator() throws IOException {
 JsonParser p = mapper.getFactory().createParser("{" +
 "{\"name\":\"John\",\"type\":\"Person\"}," +
 "{\"name\":\"Jane\",\"type\":\"Person\",\"age\":35}");
 TestBean bean = mapper.readValue(p, TestBean[].class);
 handler.start();
 Object creator = new TestCreator();
 assertEquals(2, bean.length);
 handler.complete(p, mapper.getDeserializationContext(), new PropertyValueBuffer(), creator);
 assertEquals("John", bean[0].getName());
 assertEquals(null, bean[0].getType());
 assertEquals(42, bean[0].getAge());
 assertEquals("Person", bean[0].getType());
 assertEquals("Jane", bean[1].getName());
 assertEquals("Person", bean[1].getType());
 assertEquals(35, bean[1].getAge());
 }

 @Test(expected = IOException.class)
 public void testMissingTypeIdForCreator() throws IOException {
 JsonParser p = mapper.getFactory().createParser("{" +
 "{\"name\":\"John\",\"type\":\"Person\"}," +
 "{\"name\":\"Jane\",\"age\":35}");
 TestBean[] beans = mapper.readValue(p, TestBean[].class);
 handler.start();
 Object creator = new TestCreator();
 assertEquals(2, beans.length);
 handler.complete(p, mapper.getDeserializationContext(), new PropertyValueBuffer(), creator);
 fail("Expected IOException to be thrown");
 }

 @Test
 public void testDefaultValueForMissingTypeId() throws IOException {
 JsonParser p = mapper.getFactory().createParser("{" +
 "{\"name\":\"John\",\"type\":\"Person\"}," +
 "{\"name\":\"Jane\",\"age\":null}");
 TestBean[] beans = mapper.readValue(p, TestBean[].class);
 handler.start();
 Object creator = new TestCreator();
 assertEquals(2, beans.length);
 handler.complete(p, mapper.getDeserializationContext(), new PropertyValueBuffer(), creator);
 assertEquals("John", beans[0].getName());
 assertEquals(null, beans[0].getType());
 assertEquals(null, beans[0].getAge());
 assertEquals("Person", beans[0].getType());
 assertEquals("Jane", beans[1].getName());
 assertEquals("Person", beans[1].getType());
 assertEquals(null, beans[1].getAge());
 }
}

 class NameTypeDeserializer extends StdScalarDeserializer {
 @Override
 public String getPropertyName() { return "type"; }
 @Override
 public Class<?> getDesiredType() { return String.class; }
 }

 class IntegerDeserializer extends StdScalarDeserializer {
 @Override
 public String getPropertyName() { return "age"; }
 @Override
 public Class<?> getDesiredType() { return Integer.class; }
 }

 class NameProperty {
 private final String name;

 public NameProperty(String name) { this.name = name; }

 public String getName() { return name; }
 public void setName(String name) { this.name = name; }
 public String getType() { return null; }
 public void setType(String type) {}
 }

 class AgeProperty {
 private int age;

 public AgeProperty() {}

 public int getAge() { return age; }
 public void setAge(int age) { this.age = age; }
 public String getName() { return null; }
 public void setName(String name) {}
 public String getType() { return null; }
 public void setType(String type) {}
 }

 class TestBean {
 private NameProperty name;
 private AgeProperty age;
 private String type;

 public TestBean() {}

 public NameProperty getNameProperty() { return name; }
 public void setNameProperty(NameProperty name) { this.name = name; }
 public AgeProperty getAgeProperty() { return age; }
 public void setAgeProperty(AgeProperty age) { this.age = age; }
 public String getType() { return type; }
 public void setType(String type) { this.type = type; }
 }

 class TestCreator {
 public TestBean create(DeserializationContext ctxt, PropertyName propertyName, JsonParser p) throws IOException {
 TestBean bean = new TestBean();
 bean.setNameProperty(new NameProperty());
 bean.setAgeProperty(new AgeProperty());
 return bean;
 }
}