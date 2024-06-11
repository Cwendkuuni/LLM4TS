package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.InnerClassProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class InnerClassPropertyTest {

 private ObjectNode testNode;
 private Object testInstance;
 private JsonDeserializer<Object> testDeserializer;
 private SettableBeanProperty<Object, Object> testProperty;
 private InnerClassProperty<Object, Object> testInnerProperty;

 @Before
 public void setup() throws Exception {
 Class<?> testClass = TestInnerClass.class;
 ObjectMapper mapper = new ObjectMapper();
 testNode = mapper.createObjectNode();
 testInstance = mapper.readValue(mapper.writeValueAsString(new TestObject()), testClass);
 testDeserializer = new StdSerializer<Object>(TestInnerClass.class) {
 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return ctxt.readValueForType(p, TestInnerClass.class);
 }
 };
 BeanPropertyDefinition pdef = mapper.findPropertyDefinition(testClass, "inner");
 testProperty = new SettableBeanProperty<>(pdef, null);
 testInnerProperty = new InnerClassProperty<>(testProperty, TestInnerClass.Inner.class.getDeclaredConstructor());
 }

 @Test
 public void testDefaultConstructor() throws Exception {
 Object value = testInnerProperty.get(_creator.newInstance(testInstance));
 assertNotNull(value);
 }

 @Test
 public void testSetAndGet() throws Exception {
 TestInnerClass.Inner expectedValue = new TestInnerClass.Inner("test");
 testInnerProperty.set(testInstance, expectedValue);
 TestInnerClass.Inner actualValue = (TestInnerClass.Inner) testInnerProperty.get(testInstance);
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testDeserialize() throws Exception {
 String json = "{\"inner\":{\"value\":\"test\"}}";
 testNode.putPOJO("inner", new TestObject());
 testNode.set("inner.value", "test");
 Object parsedInstance = mapper.readTree(json).traverse().deepGetValue(testClass);
 testProperty.set(parsedInstance, null);
 JsonParser jp = mapper.getFactory().createParser(json);
 testInnerProperty.deserializeAndSet(jp, new TestDeserializationContext(), parsedInstance);
 TestInnerClass.Inner actualValue = (TestInnerClass.Inner) testInnerProperty.get(parsedInstance);
 assertEquals("test", actualValue.getValue());
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 Object parsedInstance = mapper.readTree(testNode.toString()).traverse().deepGetValue(testClass);
 TestInnerClass.Inner actualValue = (TestInnerClass.Inner) testInnerProperty.deserializeSetAndReturn(mapper.getFactory().createParser(testNode.toString()), new TestDeserializationContext(), parsedInstance);
 assertEquals("test", actualValue.getValue());
 }

 @Test
 public void testSetAndReturn() throws Exception {
 TestInnerClass.Inner expectedValue = new TestInnerClass.Inner("test2");
 TestInnerClass.Inner actualValue = (TestInnerClass.Inner) testInnerProperty.setAndReturn(testInstance, expectedValue);
 assertEquals(expectedValue, actualValue);
 }
}

class TestObject {
 private TestInnerClass.Inner inner;
}

class TestInnerClass {
 static class Inner {
 private String value;

 Inner(String value) {
 this.value = value;
 }

 String getValue() {
 return value;
 }
 }

 private Inner inner;

 public Inner getInner() {
 return inner;
 }

 public void setInner(Inner inner) {
 this.inner = inner;
 }
}

class TestDeserializationContext extends DeserializationContext {
 @Override
 public JsonDeserializer<?> findValueDeserializer(BeanProperty property, JsonDeserializer<?> deserializer) {
 return testDeserializer;
 }
}