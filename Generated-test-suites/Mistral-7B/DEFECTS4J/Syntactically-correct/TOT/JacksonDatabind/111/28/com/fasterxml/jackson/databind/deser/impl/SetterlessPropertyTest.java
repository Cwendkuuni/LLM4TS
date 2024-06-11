package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

public class SetterlessPropertyTest {

 private ObjectMapper mapper;
 private SetterlessProperty property;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

 Class<?> targetClass = MyTargetClass.class;
 try {
 property = mapper.findProperty(targetClass.getDeclaredField("myListField"), MyValueDeserializer.class);
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 } catch (IllegalAccessException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testNormalDeserialization() throws JsonProcessingException, JsonMappingException {
 ObjectNode input = mapper.createObjectNode();
 input.put("myListField", "[1, 2, 3]");
 Object target = mapper.readValue(input.toString(), targetClass);

 assertNotNull(target);
 assertEquals("[1, 2, 3]", target.toString());
 assertNotNull(property.getBean());
 assertNotNull(property.getValue());
 assertEquals("[1, 2, 3]", property.getValue().toString());
 }

 @Test
 public void testNullDeserialization() throws JsonProcessingException, JsonMappingException {
 ObjectNode input = mapper.createObjectNode();
 input.put("myListField", null);
 Object target = mapper.readValue(input.toString(), targetClass);

 assertNotNull(target);
 assertNull(property.getValue());
 }

 @Test
 public void testEmptyDeserialization() throws JsonProcessingException, JsonMappingException {
 ObjectNode input = mapper.createObjectNode();
 Object target = mapper.readValue(input.toString(), targetClass);

 assertNotNull(target);
 assertNotNull(property.getValue());
 assertEquals("[]", property.getValue().toString());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetterMethodCall() throws JsonProcessingException, JsonMappingException {
 Object target = mapper.readValue(mapper.createObjectNode().toString(), targetClass);
 property.set(target, new Object());
 }

 @Test
 public void testDeserializationWithEmptyObject() throws JsonProcessingException, JsonMappingException {
 Object target = mapper.readValue(mapper.createObjectNode().toString(), targetClass);

 assertNotNull(target);
 assertNotNull(property.getValue());
 assertEquals("[]", property.getValue().toString());
 }

 @Test
 public void testDeserializationWithInvalidJson() throws JsonProcessingException, JsonMappingException {
 ObjectNode input = mapper.createObjectNode();
 input.put("myListField", "invalidJson");
 Object target = mapper.readValue(input.toString(), targetClass);

 assertNotNull(target);
 assertNotNull(property.getValue());
 assertEquals("[]", property.getValue().toString());
 }
}

class MyTargetClass {
 List<Integer> myListField;
}

class MyValueDeserializer extends StdDeserializer<List<Integer>> {
 protected MyValueDeserializer() {
 this(null);
 }

 protected MyValueDeserializer(Class<List<Integer>> vc) {
 super(vc);
 }

 @Override
 public List<Integer> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return ctxt.readValueAs(ArrayList.class, p);
 }
}