package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class FieldPropertyTest {

 private static class TestBean {
 @JsonProperty("name")
 private String name;

 @JsonProperty("age")
 private int age;
 }

 private ObjectMapper objectMapper;
 private Object testInstance;
 private FieldProperty fieldProperty;

 @Before
 public void setup() throws InstantiationException, IllegalAccessException {
 objectMapper = new ObjectMapper();
 TestBean testBean = new TestBean();
 testInstance = testBean;
 Field nameField = TestBean.class.getDeclaredField("name");
 nameField.setAccessible(true);
 fieldProperty = new FieldProperty(
 ObjectPropertyDescriptor.createPropertyDescriptor("name", String.class, null),
 null, null, null, new AnnotatedField(new AnnotatedMember(nameField), null));
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 String json = "{\"name\":\"John\",\"age\":42}";
 JsonParser jsonParser = objectMapper.getFactory().createParser(json);
 JsonToken currentToken = jsonParser.nextToken();
 fieldProperty.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), testInstance);
 assertEquals("John", testInstance.getClass().getDeclaredField("name").get(testInstance));
 assertEquals(42, testInstance.getClass().getDeclaredField("age").get(testInstance));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 String json = "{\"name\":\"John\",\"age\":42}";
 JsonParser jsonParser = objectMapper.getFactory().createParser(json);
 Object result = fieldProperty.deserializeSetAndReturn(jsonParser, objectMapper.getDeserializationContext(), testInstance);
 assertEquals(testInstance, result);
 assertEquals("John", testInstance.getClass().getDeclaredField("name").get(testInstance));
 assertEquals(42, testInstance.getClass().getDeclaredField("age").get(testInstance));
 }

 @Test
 public void testSet() throws IOException {
 String name = "Jane";
 testInstance.getClass().getDeclaredField("name").set(testInstance, name);
 fieldProperty.set(testInstance, "Jane");
 assertEquals("Jane", testInstance.getClass().getDeclaredField("name").get(testInstance));
 }

 @Test
 public void testSetAndReturn() throws IOException {
 String name = "Jane";
 Object result = fieldProperty.setAndReturn(testInstance, "Jane");
 assertEquals(testInstance, result);
 assertEquals("Jane", testInstance.getClass().getDeclaredField("name").get(testInstance));
 }

 @Test
 public void testWithName() {
 SettableBeanProperty result = fieldProperty.withName(new PropertyName("lastName"));
 assertEquals("name", result.getName());
 assertEquals("FieldProperty", result.getClass().getSimpleName());
 }

 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer<String> stringDeserializer = new StringDeserializer();
 SettableBeanProperty result = fieldProperty.withValueDeserializer(stringDeserializer);
 assertEquals(stringDeserializer, result.getValueDeserializer());
 }

 @Test
 public void testWithNullProvider() {
 NullValueProvider nullValueProvider = new StandardNullValueProvider();
 SettableBeanProperty result = fieldProperty.withNullProvider(nullValueProvider);
 assertEquals(nullValueProvider, result.getNullProvider());
 }

 @Test
 public void testFixAccess() throws IllegalAccessException {
 Field privateField = TestBean.class.getDeclaredField("name");
 privateField.setAccessible(true);
 privateField.set(testInstance, "Test");
 fieldProperty.fixAccess(objectMapper.getDeserializationConfig());
 privateField.setAccessible(false);
 String name = (String) fieldProperty.get(testInstance);
 assertEquals("Test", name);
 }

 @Test
 public void testGetAnnotation() {
 JsonProperty jsonProperty = new JsonProperty();
 AnnotatedField annotatedField = new AnnotatedField(new AnnotatedMember(new AnnotatedElement("name", String.class)), jsonProperty);
 SettableBeanProperty result = new FieldProperty(
 ObjectPropertyDescriptor.createPropertyDescriptor("name", String.class, null),
 null, null, null, annotatedField);
 @JsonProperty("lastName")
 String lastName = new String("LastName");
 result.getClass().getDeclaredField("_annotated").set(result, annotatedField);
 Annotation annotation = result.getAnnotation(JsonProperty.class);
 assertEquals(lastName, annotation);
 }

 @Test
 public void testGetMember() {
 AnnotatedMember result = fieldProperty.getMember();
 assertEquals(fieldProperty._annotated, result);
 }
}

class StringDeserializer extends StdSerializer<String> {
 protected StringDeserializer() { this(null); }
 StringDeserializer(Class<String> vc) { super(vc); }
 @Override
 public String deserialize(JsonParser p, DeserializationContext ctxt)
 throws IOException {
 return "StringDeserialized";
 }
}