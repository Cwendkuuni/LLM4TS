package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.SettableAnyProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

 private SettableAnyProperty _property;
 private Object _pojo;

 @Before
 public void setUp() throws Exception {
 _pojo = new Object() {};
 ObjectIdReader idReader = new SimpleObjectIdReader();
 TypeDeserializer typeDeser = new StdTypeDeserializer();
 JsonDeserializer<Object> valueDeser = new FromStringDeserializer(String.class);
 _property = new SettableAnyProperty(
 new BeanProperty(new AnnotatedClass(Object.class), new AnnotatedField(new AnnotatedMethod(Object.class, "set", int.class), null), null, null),
 new AnnotatedField(_pojo.getClass().getDeclaredField("anyProperty"), null),
 JsonTypeFactory.getDefaultInstance().constructType(Object.class),
 idReader, valueDeser, typeDeser);
 }

 @Test
 public void testSettableAnyPropertyWithStringValue() throws Exception {
 String key = "name";
 String value = "John Doe";
 _property.deserializeAndSet(new JsonParser(new StringReader("{\"name\":\"John Doe\"}")), new DeserializationContext(), _pojo, key);
 assertEquals("John Doe", _pojo.getClass().getDeclaredField("anyProperty").get(_pojo));
 }

 @Test
 public void testSettableAnyPropertyWithNumberValue() throws Exception {
 String key = "age";
 int value = 30;
 _property.deserializeAndSet(new JsonParser(new StringReader("{\"age\":30}")), new DeserializationContext(), _pojo, key);
 assertEquals(30, _pojo.getClass().getDeclaredField("anyProperty").get(_pojo));
 }

 @Test
 public void testSettableAnyPropertyWithNullValue() throws Exception {
 String key = "city";
 _property.deserializeAndSet(new JsonParser(new StringReader("{\"city\":null}")), new DeserializationContext(), _pojo, key);
 assertNull(_pojo.getClass().getDeclaredField("anyProperty").get(_pojo));
 }

 @Test(expected = JsonMappingException.class)
 public void testSettableAnyPropertyWithInvalidKeyType() throws Exception {
 String key = new Integer(1);
 _property.deserializeAndSet(new JsonParser(new StringReader("{\"key\":1,\"value\":\"test\"}")), new DeserializationContext(), _pojo, key.toString());
 fail("Should have thrown JsonMappingException with invalid key type");
 }

 @Test(expected = JsonMappingException.class)
 public void testSettableAnyPropertyWithMissingValueDeserializer() throws Exception {
 _property = new SettableAnyProperty(
 new BeanProperty(new AnnotatedClass(Object.class), new AnnotatedField(new AnnotatedMethod(Object.class, "set", int.class), null), null, null),
 new AnnotatedField(_pojo.getClass().getDeclaredField("anyProperty"), null),
 JsonTypeFactory.getDefaultInstance().constructType(Object.class),
 null, null, null);
 _property.deserializeAndSet(new JsonParser(new StringReader("{\"key\":\"test\",\"value\":1}")), new DeserializationContext(), _pojo, "key");
 fail("Should have thrown JsonMappingException with missing value deserializer");
 }

 @Test(expected = JsonMappingException.class)
 public void testSettableAnyPropertyWithMissingKeyDeserializer() throws Exception {
 _property = new SettableAnyProperty(
 new BeanProperty(new AnnotatedClass(Object.class), new AnnotatedField(new AnnotatedMethod(Object.class, "set", int.class), null), null, null,
 new FromStringDeserializer(String.class), null),
 new AnnotatedField(_pojo.getClass().getDeclaredField("anyProperty"), null),
 JsonTypeFactory.getDefaultInstance().constructType(Object.class),
 null, null, null);
 _property.deserializeAndSet(new JsonParser(new StringReader("{\"value\":1}")), new DeserializationContext(), _pojo, "key");
 fail("Should have thrown JsonMappingException with missing key deserializer");
 }

 @Test(expected = JsonMappingException.class)
 public void testSettableAnyPropertyWithEmptyJson() throws Exception {
 _property.deserializeAndSet(new JsonParser(new StringReader("")), new DeserializationContext(), _pojo, "key");
 fail("Should have thrown JsonMappingException with empty Json");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettableAnyPropertyWithInvalidType() throws Exception {
 _property = new SettableAnyProperty(
 new BeanProperty(new AnnotatedClass(Object.class), new AnnotatedField(new AnnotatedMethod(Object.class, "set", int.class), null), null, null),
 new AnnotatedField(_pojo.getClass().getDeclaredField("anyProperty"), null),
 JsonTypeFactory.getDefaultInstance().constructType(float.class),
 null, null, null);
 _property.deserializeAndSet(new JsonParser(new StringReader("{\"key\":\"test\",\"value\":1}")), new DeserializationContext(), _pojo, "key");
 fail("Should have thrown IllegalArgumentException with invalid type");
 }
}