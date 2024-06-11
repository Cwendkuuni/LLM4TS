package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.FieldProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FieldPropertyTest {

 private ObjectNode node;
 private Object target;
 private FieldProperty property;

 @Before
 public void setup() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 node = mapper.createObjectNode();
 target = new MyClass();
 property = new FieldProperty(
 new BeanPropertyDefinition(null, MyClass.class, "fieldName", MyType.class),
 ObjectTypeFactory.defaultInstance, null, null, new AnnotatedField(
 ClassUtil.makeAccessible(MyClass.class, "fieldName"), MyClass.class, "fieldName"));
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object value = "someValue";
 node.put("fieldName", value);
 property.deserializeAndSet(node, new DeserializationContext(null, null, null), target);
 assertEquals(value, target.getClass().getField("fieldName").get(target));
 }

 @Test
 public void testDeserializeAndSetNull() throws IOException {
 node.put("fieldName", null);
 property.deserializeAndSet(node, new DeserializationContext(null, null, null), target);
 assertNull(target.getClass().getField("fieldName").get(target));
 }

 @Test
 public void testDeserializeAndSetWithTypeDeserializer() throws IOException {
 TypeDeserializer typeDeserializer = new CustomTypeDeserializer();
 property = new FieldProperty(
 new BeanPropertyDefinition(null, MyClass.class, "fieldName", MyType.class),
 ObjectTypeFactory.defaultInstance, typeDeserializer, null, new AnnotatedField(
 ClassUtil.makeAccessible(MyClass.class, "fieldName"), MyClass.class, "fieldName"));
 node.put("fieldName", "someStringValue");
 property.deserializeAndSet(node, new DeserializationContext(null, null, null), target);
 MyType expectedValue = new MyType();
 assertEquals(expectedValue, target.getClass().getField("fieldName").get(target));
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeAndSetWithInvalidValue() throws IOException {
 node.put("fieldName", new Object());
 property.deserializeAndSet(node, new DeserializationContext(null, null, null), target);
 fail("Should have thrown JsonProcessingException");
 }

 @Test
 public void testSet() throws IOException {
 target.getClass().getField("fieldName").set(target, new Object());
 property.set(node, target);
 assertSame(target, node.get("fieldName"));
 }

 @Test
 public void testSetNull() throws IOException {
 target.getClass().getField("fieldName").set(target, null);
 property.set(node, target);
 assertNull(node.get("fieldName"));
 }

 @Test
 public void testSetWithTypeDeserializer() throws IOException {
 MyType value = new MyType();
 target.getClass().getField("fieldName").set(target, value);
 property.set(node, target);
 ObjectNode expectedNode = node.deepClone();
 expectedNode.put("fieldName", ObjectMapper.writeValueAsString(value));
 assertJsonEqualsIgnoreQuotes(expectedNode.toString(), node.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testSetWithInvalidValue() throws IOException {
 node.put("fieldName", new Object());
 property.set(node, target);
 fail("Should have thrown JsonProcessingException");
 }

 private static void assertJsonEqualsIgnoreQuotes(String json1, String json2) {
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode node1 = mapper.readTree(json1);
 ObjectNode node2 = mapper.readTree(json2);
 assertEquals(node1.deepClone(), node2.deepClone());
 }

 static class MyClass {
 private static final long serialVersionUID = 1L;
 private transient Field fieldName;

 public static transient Field getFieldName() {
 return FieldUtil.makeAccessible(MyClass.class, "fieldName");
 }
 }

 static class MyType {

 }

 static class CustomTypeDeserializer extends StdTypeDeserializer {
 @Override
 public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
 throws IOException {
 return new MyType();
 }
}
}