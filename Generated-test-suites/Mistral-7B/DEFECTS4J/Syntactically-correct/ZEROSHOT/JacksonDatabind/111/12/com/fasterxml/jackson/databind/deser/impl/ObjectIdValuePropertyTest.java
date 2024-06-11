package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;
 private JsonDeserializer<Object> valueDeserializer;
 private Object instance;

 @Before
 public void setUp() throws Exception {
 ObjectIdReader mockObjectIdReader = new ObjectIdReader() {
 @Override
 public PropertyMetadata metadata() {
 return new PropertyMetadata();
 }

 @Override
 public String idType() {
 return "String";
 }

 @Override
 public String propertyName() {
 return "objectId";
 }

 @Override
 public JsonDeserializer<Object> idDeserializer() {
 return valueDeserializer;
 }

 @Override
 public ObjectIdGenerator idGenerator() {
 return null;
 }

 @Override
 public ObjectIdResolver idResolver() {
 return null;
 }

 @Override
 public SettableBeanProperty idProperty() {
 return null;
 }
 };
 objectIdReader = mockObjectIdReader;
 valueDeserializer = new StdScalarDeserializer(String.class);
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.setAnnotationIntrospector(new AnnotationIntrospector());
 instance = new Object();
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 }

 @Test
 public void testInstance() throws Exception {
 assertEquals("com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty",
 objectIdValueProperty.getClass().getName());
 }

 @Test
 public void testConstructorWithObjectIdReader() throws Exception {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 assertEquals(objectIdReader.propertyName(), objectIdValueProperty.getName());
 assertEquals("String", objectIdValueProperty.getIdType());
 assertNull(objectIdValueProperty.getValueDeserializer());
 assertNull(objectIdValueProperty.getNullProvider());
 assertNull(objectIdValueProperty.getIdProperty());
 }

 @Test
 public void testConstructorWithObjectIdReaderDeserializer() throws Exception {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, valueDeserializer, null);
 assertEquals(objectIdReader.propertyName(), objectIdValueProperty.getName());
 assertEquals("String", objectIdValueProperty.getIdType());
 assertEquals(valueDeserializer, objectIdValueProperty.getValueDeserializer());
 assertNull(objectIdValueProperty.getNullProvider());
 assertNull(objectIdValueProperty.getIdProperty());
 }

 @Test
 public void testConstructorWithObjectIdReaderName() throws Exception {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyName("newName"));
 assertEquals("newName", objectIdValueProperty.getName().asString());
 assertEquals("String", objectIdValueProperty.getIdType());
 assertEquals(valueDeserializer, objectIdValueProperty.getValueDeserializer());
 assertNull(objectIdValueProperty.getNullProvider());
 assertNull(objectIdValueProperty.getIdProperty());
 }

 @Test
 public void testGetAnnotation() throws Exception {
 assertNull(objectIdValueProperty.getAnnotation(JsonProperty.class));
 }

 @Test
 public void testGetMember() throws Exception {
 assertNull(objectIdValueProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("objectId", "123");
 JsonParser jsonParser = objectMapper.treeToValues(jsonNode, instance.getClass());
 objectIdValueProperty.deserializeAndSet(jsonParser, new TestContext(), instance);
 assertEquals("123", objectIdValueProperty.getValue());
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("objectId", "123");
 JsonParser jsonParser = objectMapper.treeToValues(jsonNode, instance.getClass());
 Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, new TestContext(), instance);
 assertEquals("123", result);
 assertEquals("123", objectIdValueProperty.getValue());
 }

 @Test
 public void testSet() throws Exception {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("objectId", "123");
 objectIdValueProperty.set(instance, jsonNode);
 assertEquals("123", objectIdValueProperty.getValue());
 }

 @Test
 public void testSetAndReturn() throws Exception {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("objectId", "123");
 Object result = objectIdValueProperty.setAndReturn(instance, jsonNode);
 assertEquals("123", result);
 assertEquals("123", objectIdValueProperty.getValue());
 }

 @Test (expected = UnsupportedOperationException.class)
 public void testSetWithoutIdProperty() throws Exception {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 objectIdValueProperty.set(instance, "123");
 fail("Should throw UnsupportedOperationException");
 }
}