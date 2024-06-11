package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class InnerClassPropertyTest {

 private ObjectMapper objectMapper;
 private JsonFactory jsonFactory;
 private InnerClassProperty property;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 jsonFactory = objectMapper.getFactory();
 try {
 property = new InnerClassProperty(null, new ClassName("com.test.InnerClass") {}.getDeclaredConstructor());
 } catch (NoSuchMethodException e) {
 throw new RuntimeException("Couldn't find constructor for inner class", e);
 }
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object root = new Object();
 JsonParser jp = jsonFactory.createParser("{\"innerClass\": null}");
 property.deserializeAndSet(jp, new TestContext(), root);
 assertNull(property.get(root));

 jp = jsonFactory.createParser("{\"innerClass\": {} }");
 property.deserializeAndSet(jp, new TestContext(), root);
 assertInstanceOf(InnerClass.class, property.get(root));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object root = new Object();
 Object innerClass = new Object();
 Object returnedInnerClass = property.deserializeSetAndReturn(jsonFactory.createParser("{\"innerClass\": {} }"), new TestContext(), root);
 assertEquals(innerClass, returnedInnerClass);
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object root = new Object();
 Object innerClass = new Object();
 Object returnedInnerClass = property.setAndReturn(root, innerClass);
 assertEquals(innerClass, returnedInnerClass);
 }

 @Test
 public void testValueDeserializer() throws IOException {
 Object root = new Object();
 JsonDeserializer<InnerClass> deserializer = new JsonDeserializer<InnerClass>() {
 @Override
 public InnerClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return new InnerClass();
 }
 };
 property = new InnerClassProperty(null, deserializer);
 JsonParser jp = jsonFactory.createParser("{\"innerClass\": null}");
 property.deserializeAndSet(jp, new TestContext(), root);
 assertNull(property.get(root));

 jp = jsonFactory.createParser("{\"innerClass\": {}} ");
 property.deserializeAndSet(jp, new TestContext(), root);
 assertInstanceOf(InnerClass.class, property.get(root));
 }

 @Test
 public void testValueDeserializerWithType() throws IOException {
 Object root = new Object();
 JsonDeserializer<InnerClass> deserializer = new JsonDeserializer<InnerClass>() {
 @Override
 public InnerClass deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer vd) throws IOException {
 return new InnerClass();
 }
 };
 property = new InnerClassProperty(null, deserializer);
 JsonParser jp = jsonFactory.createParser("{\"innerClass\": null}");
 property.deserializeAndSet(jp, new TestContext(), root);
 assertNull(property.get(root));

 jp = jsonFactory.createParser("{\"innerClass\": {}} ");
 property.deserializeAndSet(jp, new TestContext(), root);
 assertInstanceOf(InnerClass.class, property.get(root));
 }

 @Test
 public void testValueTypeDeserializer() throws IOException {
 Object root = new Object();
 JsonDeserializer<InnerClass> deserializer = new JsonDeserializer<InnerClass>() {
 @Override
 public InnerClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return new InnerClass();
 }
 };
 property = new InnerClassProperty(null, deserializer, InnerClass.class);
 JsonParser jp = jsonFactory.createParser("{\"innerClass\": null}");
 property.deserializeAndSet(jp, new TestContext(), root);
 assertNull(property.get(root));

 jp = jsonFactory.createParser("{\"innerClass\": {}} ");
 property.deserializeAndSet(jp, new TestContext(), root);
 assertInstanceOf(InnerClass.class, property.get(root));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingConstructor() throws IOException {
 property = new InnerClassProperty(null, new ClassName("com.test.MissingConstructorInnerClass") {}.getDeclaredConstructor());
 Object root = new Object();
 JsonParser jp = jsonFactory.createParser("{\"innerClass\": null}");
 property.deserializeAndSet(jp, new TestContext(), root);
 fail("Expected IllegalArgumentException");
 }
}

class TestContext extends DeserializationContext {
 @Override public Object instantiateValue(Class<?> valueType, AnnotatedMember member) throws IOException { return null; }
 @Override public Object findValueDeserializer(Class<?> valueType, JsonDeserializer.None none) { return null; }
}

class ClassName {
 private Class<?> clazz;

 public ClassName(Class<?> clazz) { this.clazz = clazz; }

 public Class<?> getDeclaredConstructor() throws NoSuchMethodException {
 return clazz.getDeclaredConstructor();
 }
}

class InnerClass {
}