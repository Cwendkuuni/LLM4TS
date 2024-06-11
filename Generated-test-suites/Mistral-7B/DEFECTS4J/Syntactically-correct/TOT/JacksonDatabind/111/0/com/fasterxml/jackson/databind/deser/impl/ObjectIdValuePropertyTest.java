package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;
 private JsonDeserializer<String> stringDeserializer;
 private JsonParser testParser;
 private DeserializationContext testContext;
 private ObjectNode testObjectNode;

 @Before
 public void setup() throws Exception {
 objectIdReader = new DefaultObjectIdReader(ObjectIdType.STRING, "id");
 stringDeserializer = new StringDeserializer();
 testParser = new TestJsonParser();
 testContext = new TestDeserializationContext();
 testObjectNode = new ObjectMapper().createObjectNode();
 }

 @Test
 public void testNullValue() throws IOException {
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new StandardPropertyMetadata());
 Object result = objectIdValueProperty.deserializeAndSet(testParser, testContext, null);
 assertNull(result);
 }

 @Test
 public void testEmptyStringValue() throws IOException {
 testObjectNode.put("id", "");
 Object result = objectIdValueProperty.deserializeAndSet(testParser, testContext, testObjectNode);
 assertNull(result);
 }

 @Test
 public void testValidValue() throws IOException {
 testObjectNode.put("id", "123");
 Object id = objectIdReader.readerForId("123");
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new StandardPropertyMetadata());
 Object result = objectIdValueProperty.deserializeAndSet(testParser, testContext, testObjectNode);
 assertEquals(id, result);
 }

 @Test(expected = IOException.class)
 public void testInvalidValue() throws IOException {
 testObjectNode.put("id", "invalidValue");
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new StandardPropertyMetadata());
 Object result = objectIdValueProperty.deserializeAndSet(testParser, testContext, testObjectNode);
 fail("Expected IOException but got " + result);
 }

 @Test
 public void testValueDeserializer() throws IOException {
 ObjectIdReader objectIdReaderWithDeserializer = new DefaultObjectIdReader(ObjectIdType.STRING, "id") {
 @Override
 public JsonDeserializer<Object> getIdDeserializer() {
 return stringDeserializer;
 }
 };
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReaderWithDeserializer, new StandardPropertyMetadata());
 Object result = objectIdValueProperty.deserializeAndSet(testParser, testContext, testObjectNode);
 assertEquals("value", result);
 }

 @Test
 public void testNullProvider() throws IOException {
 ObjectIdReader objectIdReaderWithNullProvider = new DefaultObjectIdReader(ObjectIdType.STRING, "id") {
 @Override
 public NullValueProvider getNullValueProvider() {
 return new NullValueProviderWithNull(true);
 }
 };
 ObjectIdValueProperty objectIdValuePropertyWithNullProvider = new ObjectIdValueProperty(
 objectIdReaderWithNullProvider, new StandardPropertyMetadata());
 Object result = objectIdValuePropertyWithNullProvider.deserializeAndSet(testParser, testContext, testObjectNode);
 assertNull(result);
 }

 @Test
 public void testName() throws IOException {
 ObjectIdReader objectIdReaderWithName = new DefaultObjectIdReader("newName", ObjectIdType.STRING, "id") {
 };
 ObjectIdValueProperty objectIdValuePropertyWithName = new ObjectIdValueProperty(objectIdReaderWithName, new StandardPropertyMetadata());
 Object result = objectIdValuePropertyWithName.deserializeAndSet(testParser, testContext, testObjectNode);
 assertTrue(testObjectNode.path("newName").isTextual());
 assertEquals("id", testObjectNode.get("id").asText());
 }

 @Test
 public void testWithValueDeserializer() throws IOException {
 ObjectIdValueProperty originalObjectIdValueProperty = objectIdValueProperty;
 JsonDeserializer<Integer> integerDeserializer = new StandardDeserializer<>(Integer.class);
 objectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, integerDeserializer, null);Object result = objectIdValueProperty.deserializeAndSet(testParser, testContext, testObjectNode);
 fail("Expected ObjectIdValueProperty but got " + objectIdValueProperty.getClass().getName());
 assertEquals(0, result.hashCode());
 }

 @Test
 public void testWithNullProvider() throws IOException {
 ObjectIdValueProperty originalObjectIdValueProperty = objectIdValueProperty;
 NullValueProvider nullValueProvider = new NullValueProviderWithNull(true);
 objectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, null, nullValueProvider);
 assertSame(nullValueProvider, objectIdValueProperty.getNullProvider());
 }

 @Test
 public void testWithName() throws IOException {
 ObjectIdReader objectIdReaderWithName = new DefaultObjectIdReader("newName", ObjectIdType.STRING, "id") {};
 ObjectIdValueProperty objectIdValuePropertyWithName = new ObjectIdValueProperty(objectIdReaderWithName, new StandardPropertyMetadata());
 Object result = objectIdValuePropertyWithName.setAndReturn(null, "value");
 ObjectIdValueProperty expectedObjectIdValueProperty = new ObjectIdValueProperty(objectIdReaderWithName, new StandardPropertyMetadata()) {
 @Override
 public PropertyName getName() {
 return new PropertyName("newName");
 }
 };
 assertEquals(expectedObjectIdValueProperty, result);
 }
}