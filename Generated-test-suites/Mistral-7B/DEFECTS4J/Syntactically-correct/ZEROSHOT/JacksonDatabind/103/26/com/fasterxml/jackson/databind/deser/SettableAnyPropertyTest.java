package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

 private ObjectMapper mapper;
 private SettableAnyProperty settableAnyProperty;

 @Before
 public void setUp() throws Exception {
 mapper = new ObjectMapper();
 AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
 BeanProperty property = Mockito.mock(BeanProperty.class);
 JavaType type = Map.class.getType();
 KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
 JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
 TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);
 settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
 }

 @Test
 public void testProperty() {
 BeanProperty actual = settableAnyProperty.getProperty();
 assertEquals(property, actual);
 }

 @Test
 public void testHasValueDeserializer() {
 assertFalse(settableAnyProperty.hasValueDeserializer());
 }

 @Test
 public void testType() {
 JavaType actual = settableAnyProperty.getType();
 assertEquals(type, actual);
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 String propName = "testProp";
 Object key = "testKey";
 Object value = "testValue";
 JsonParser parser = mapper.getFactory().createParser("{\"k\":\"t\",\"v\":\"tv\"}");
 Object pojo = new Object();
 settableAnyProperty.deserializeAndSet(parser, Mockito.mock(DeserializationContext.class), pojo, propName);
 assertEquals(value, mapper.readValue(parser, Map.class).get(key));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWhenPropNameIsNull() throws IOException {
 Object pojo = new Object();
 settableAnyProperty.set(pojo, null, "testValue");
 fail("Expected UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWhenValueIsNull() throws IOException {
 Object pojo = new Object();
 settableAnyProperty.set(pojo, "testProp", null);
 fail("Expected UnsupportedOperationException");
 }

 @Test
 public void testSerialize() throws IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
 Object pojo = new HashMap<>();
 pojo.put("testKey", "testValue");
 String json = writer.writeValueAsString(pojo);
 JsonParser parser = objectMapper.getFactory().createParser(json);
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(Mockito.mock(BeanProperty.class),
 Mockito.mock(AnnotatedMember.class), Map.class.getType(), null, null, null);
 SettableAnyProperty actualSettableAnyProperty = settableAnyProperty.deserializeAndSet(parser,
 Mockito.mock(DeserializationContext.class), pojo, "testKey");
 assertEquals(settableAnyProperty, actualSettableAnyProperty);
 }

 @Test
 public void testDeserialize() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("{\"k\":\"t\"}");
 Object value = settableAnyProperty.deserialize(parser, Mockito.mock(DeserializationContext.class));
 assertNull(value);
 }

 @Test
 public void testSet() throws IOException {
 Object pojo = new Object();
 settableAnyProperty.set(pojo, "testProp", "testValue");
 assertEquals("testValue", mapper.readValue(mapper.writeValueAsString(pojo), String.class));
 }
}