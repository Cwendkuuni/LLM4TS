package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.JsonTestUtils;

public class SettableAnyPropertyTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testSettableAnyProperty() throws IOException {
 Object testInstance = new TestClass();

 // Test setting a field
 SettableAnyProperty property1 = new SettableAnyProperty(
 AnnotationIntrospector.findProperty("_testField", TestClass.class),
 AnnotationIntrospector.findMember("_testSetter", TestClass.class),
 MAPPER.getTypeFactory().constructType(TestClass.class),
 null,
 null,
 null);
 property1.deserializeAndSet(createParser("{\"_testField\": 1}"), MAPPER.getDeserializationConfig(), testInstance, "_testField");
 assertEquals(1, testInstance._testField);

 // Test setting a method
 SettableAnyProperty property2 = new SettableAnyProperty(
 AnnotationIntrospector.findProperty("_testMethod", TestClass.class),
 AnnotationIntrospector.findMember("_testSetter", TestClass.class),
 MAPPER.getTypeFactory().constructType(TestClass.class),
 null,
 null,
 null);
 property2.deserializeAndSet(createParser("{\"_testMethod\": 1}"), MAPPER.getDeserializationConfig(), testInstance, "_testMethod");
 assertEquals(1, testInstance._testMethod);

 // Test null value
 SettableAnyProperty property3 = new SettableAnyProperty(
 AnnotationIntrospector.findProperty("_testNullField", TestClass.class),
 AnnotationIntrospector.findMember("_testSetter", TestClass.class),
 MAPPER.getTypeFactory().constructType(TestClass.class),
 null,
 null,
 null);
 property3.deserializeAndSet(createParser("{}"), MAPPER.getDeserializationConfig(), testInstance, "_testNullField");
 assertNull(testInstance._testNullField);

 // Test setting a property with value deserializer and type deserializer
 SettableAnyProperty property4 = new SettableAnyProperty(
 AnnotationIntrospector.findProperty("_testMapProperty", TestClass.class),
 AnnotationIntrospector.findMember("_testSetter", TestClass.class),
 MAPPER.getTypeFactory().constructType(TestClass.class),
 null,
 MAPPER.constructValueDeserializer(HashMap.class),
 null);
 property4.deserializeAndSet(createParser("{\"_testMapProperty\": [1, 2]}"), MAPPER.getDeserializationConfig(), testInstance, "_testMapProperty");
 Map<Object, Object> map = (Map<Object, Object>) testInstance._testMapProperty;
 assertEquals(2, map.size());
 assertEquals(1, map.get(0));
 assertEquals(2, map.get(1));

 // Test setting a property with key deserializer
 SettableAnyProperty property5 = new SettableAnyProperty(
 AnnotationIntrospector.findProperty("_testMapPropertyWithKeyDeserializer", TestClass.class),
 AnnotationIntrospector.findMember("_testSetter", TestClass.class),
 MAPPER.getTypeFactory().constructType(TestClass.class),
 MAPPER.constructKeyDeserializer(String.class),
 null,
 null);
 property5.deserializeAndSet(createParser("{\"_testMapPropertyWithKeyDeserializer\": [{\"key\": \"testKey1\", \"value\": 1}, {\"key\": \"testKey2\", \"value\": 2}]}"), MAPPER.getDeserializationConfig(), testInstance, "_testMapPropertyWithKeyDeserializer");
 Map<String, Object> map2 = (Map<String, Object>) testInstance._testMapPropertyWithKeyDeserializer;
 assertEquals(2, map2.size());
 assertEquals(1, map2.get("testKey1"));
 assertEquals(2, map2.get("testKey2"));
 }

 private JsonParser createParser(String content) throws IOException {
 JsonFactory factory = MAPPER.getFactory();
 return factory.createParser(JsonTestUtils.createTestInputSource(content));
 }

 private static class TestClass {
 int _testField;
 int _testMethod;
 int _testNullField;
 Map<Object, Object> _testMapProperty;
 Map<String, Object> _testMapPropertyWithKeyDeserializer;

 public void _testSetter(Object key, Object value) {
 if (key == "_testField") {
 _testField = (int) value;
 } else if (key == "_testMethod") {
 _testMethod = (int) value;
 }
 }
 }
}