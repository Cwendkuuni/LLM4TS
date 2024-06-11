package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.StandardDeserializationContext;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SetterlessPropertyTest {

 private ObjectNode input;
 private Object testInstance;
 private SettableBeanProperty property;

 @Before
 public void setup() throws InstantiationException, IllegalAccessException, NoSuchMethodException {
 testInstance = new TestClass();
 property = new SetterlessProperty(new BeanPropertyDefinition(null, TestClass.class, "list"),
 JsonTypeFactory.instance.constructCollectionType(List.class, String.class),
 null, null, new AnnotatedMethod(TestClass.class.getMethod("getList")));
 }

 @Test
 public void testEmptyListDeserialization() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode json = mapper.createObjectNode();
 json.put("list", JsonNull.getInstance());
 ObjectNode result = mapper.readTree(json);
 property.deserializeAndSet(mapper.findValueTree(result), new StandardDeserializationContext(), testInstance);
 assertEquals(Collections.emptyList(), testInstance.getClass().getMethod("getList").invoke(testInstance));
 }

 @Test
 public void testListDeserialization() throws JsonProcessingException {
 input = mapper.createObjectNode();
 input.put("list", mapper.valueToTree(Arrays.asList("one", "two", "three")));
 ObjectNode result = mapper.readTree(input);
 property.deserializeAndSet(mapper.findValueTree(result), new StandardDeserializationContext(), testInstance);
 assertEquals(Arrays.asList("one", "two", "three"), testInstance.getClass().getMethod("getList").invoke(testInstance));
 }

 @Test(expected = JsonProcessingException.class)
 public void testNullDeserialization() throws JsonProcessingException {
 input = mapper.createObjectNode();
 input.put("list", mapper.valueToTree(null));
 ObjectNode result = mapper.readTree(input);
 property.deserializeAndSet(mapper.findValueTree(result), new StandardDeserializationContext(), testInstance);
 fail("Should have thrown JsonProcessingException");
 }

 @Test(expected = IllegalAccessException.class)
 public void testIllegalAccess() throws JsonProcessingException {
 input = mapper.createObjectNode();
 input.put("list", mapper.valueToTree(Arrays.asList("one", "two", "three")));
 ObjectNode result = mapper.readTree(input);
 Object list = property.deserializeSetAndReturn(mapper.findValueTree(result), new StandardDeserializationContext(), testInstance);
 Object newList = Arrays.asList("one", "two", "four");
 property.set(testInstance, newList);
 assertEquals(newList, testInstance.getClass().getMethod("getList").invoke(testInstance));
 try {
 property.deserializeAndSet(mapper.findValueTree(result), new StandardDeserializationContext(), testInstance);
 } catch (IllegalAccessException e) {
 // expected
 }
 }

 @Test
 public void testFixAccess() throws JsonProcessingException {
 property.fixAccess(new StandardDeserializationContext());
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode json = mapper.createObjectNode();
 json.put("list", mapper.valueToTree(Arrays.asList("one", "two", "three")));
 ObjectNode result = mapper.readTree(json);
 property.deserializeAndSet(mapper.findValueTree(result), new StandardDeserializationContext(), testInstance);
 assertEquals(Arrays.asList("one", "two", "three"), testInstance.getClass().getMethod("getList").invoke(testInstance));
 }

 @Test
 public void testWithName() {
 property = property.withName(new PropertyName("newName"));
 assertEquals("newName", property.getName().asString());
 }

 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer<String> stringDeserializer = new StringDeserializer();
 property = property.withValueDeserializer(stringDeserializer);
 assertEquals(stringDeserializer, property.getValueDeserializer());
 }

 @Test
 public void testWithNullProvider() {
 NullValueProvider nva = new StandardNullValueProvider();
 property = property.withNullProvider(nva);
 assertEquals(nva, property.getNullProvider());
 }
}

class TestClass {
 private List<String> list;

 public List<String> getList() {
 return list;
 }
}