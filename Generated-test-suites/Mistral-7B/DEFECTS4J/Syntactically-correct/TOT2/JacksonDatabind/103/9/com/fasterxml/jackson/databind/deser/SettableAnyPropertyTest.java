package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SettableAnyPropertyTest {

 private SettableAnyProperty _anyProperty;
 private Object _pojo;

 @Before
 public void setup() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 _pojo = new Object();
 _anyProperty = mapper.findValueHandler(Object.class).getSettableAnyProperty(_pojo.getClass(), null, null,
 null, null, null);
 }

 @Test
 public void testDeserializeAndSet() throws JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{\"key\":\"value\"}");
 _anyProperty.deserializeAndSet(p, mapper.getDeserializationContext(), _pojo, "key");
 assertEquals("value", _pojo.getClass().getDeclaredField("map").get(_pojo));
 }

 @Test
 public void testDeserializeAndSetWithTypeHandler() throws JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{\"key\":{\"name\":\"John\",\"age\":30}}");
 _anyProperty = _anyProperty.withValueDeserializer(mapper.readValue(Class.forName("com.fasterxml.jackson.databind.ser.TestNameSerializer"), mapper));
 _anyProperty.deserializeAndSet(p, mapper.getDeserializationContext(), _pojo, "key");
 assertEquals("John", _pojo.getClass().getDeclaredField("name").get(_pojo));
 assertEquals(30, _pojo.getClass().getDeclaredField("age").get(_pojo));
 }

 @Test
 public void testDeserializeWithNullValue() throws JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{\"key\":null}");
 _anyProperty.deserializeAndSet(p, mapper.getDeserializationContext(), _pojo, "key");
 assertNotNull(_pojo.getClass().getDeclaredField("map").get(_pojo));
 assertEquals(0, _pojo.getClass().getDeclaredField("map").get(_pojo).size());
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithInvalidJson() throws JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{\"key\":invalidJson}");
 _anyProperty.deserializeAndSet(p, mapper.getDeserializationContext(), _pojo, "key");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeserializeWithInvalidType() throws JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{\"key\":123}");
 _anyProperty.deserializeAndSet(p, mapper.getDeserializationContext(), _pojo, "key");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithUnresolvedForwardReference() throws JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{\"key\":{\"$ref\":\"#/properties/anotherKey\"}}");
 _anyProperty.deserializeAndSet(p, mapper.getDeserializationContext(), _pojo, "key");
 }

 @Test
 public void testSerialization() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectWriter writer = mapper.writer();
 assertEquals("{\"key\":\"value\"}", writer.writeValueAsString(_pojo));
 }

 @Test
 public void testSerializationWithTypeHandler() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerSerializer(TestNameSerializer.class, new TestNameSerializer());
 ObjectWriter writer = mapper.writer();
 assertEquals("{\"key\":{\"name\":\"John\",\"age\":30}}", writer.writeValueAsString(_pojo));
 }
}