package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

 private ObjectIdValueProperty objectIdValueProperty;
 private ObjectIdReader objectIdReader;
 private JsonDeserializer<String> stringDeserializer;
 private JsonFactory jsonFactory;
 private JsonParser jsonParser;

 @Before
 public void setUp() throws Exception {
 objectIdReader = new ObjectIdReader("idProperty") {
 @Override
 public Class<?> getIdType() {
 return String.class;
 }

 @Override
 public JsonDeserializer<?> getIdDeserializer() {
 return stringDeserializer;
 }

 @Override
 public BeanDescription getBeanDescription() {
 return null;
 }

 @Override
 public JsonDeserializer<?> getIdPropertyValueDeserializer() {
 return null;
 }

 @Override
 public PropertyName idPropertyName() {
 return PropertyName.construct("idProperty");
 }
 };

 stringDeserializer = new StandardDeserializer<String>(String.class);
 jsonFactory = new JsonFactory();
 jsonParser = jsonFactory.createParser("[\"123\"]");
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 }

 @Test
 public void testDeserializeAndSetNull() throws IOException {
 Object target = new Object();
 objectIdValueProperty.deserializeAndSet(jsonParser, null, target);
 assertNull(objectIdValueProperty.get(target));
 }

 @Test
 public void testDeserializeAndSetValidId() throws IOException {
 Object target = new Object();
 String id = "456";
 jsonParser.nextValue(); // consume [
 jsonParser.nextValue(); // consume "123"
 jsonParser.nextValue(); // consume ]
 jsonParser.nextValue(); // consume "456"
 objectIdValueProperty.deserializeAndSet(jsonParser, null, target);
 assertEquals("456", objectIdValueProperty.get(target));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetNull() throws IOException {
 Object target = new Object();
 objectIdValueProperty.set(target, null);
 fail("Should not be able to set null value");
 }

 @Test
 public void testSetValidId() throws IOException {
 Object target = new Object();
 String id = "789";
 objectIdValueProperty.set(target, id);
 assertEquals("789", objectIdValueProperty.get(target));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithoutIdProperty() throws IOException {
 Object target = new Object();
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
 objectIdValueProperty.set(target, "101");
 fail("Should not be able to set value on ObjectIdValueProperty without idProperty");
 }

 @Test
 public void testDeserializeAndSetInvalidJson() throws IOException {
 Object target = new Object();
 jsonParser.nextValue(); // consume [
 jsonParser.nextValue(); // consume "123"
 jsonParser.nextToken(); // consume INVALID_TOKEN
 objectIdValueProperty.deserializeAndSet(jsonParser, null, target);
 assertNull(objectIdValueProperty.get(target));
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeAndSetInvalidIdDeserializer() throws IOException {
 Object target = new Object();
 jsonParser.nextValue(); // consume [
 jsonParser.nextValue(); // consume "123"
 jsonParser.nextValue(); // consume "invalid"
 objectIdValueProperty.deserializeAndSet(jsonParser, null, target);
 fail("Deserializer should fail on invalid id");
 }

 @Test
 public void testGetAnnotation() {
 Annotation annotation = objectIdValueProperty.getAnnotation(JsonProperty.class);
 assertNull(annotation);
 }

 @Test
 public void testGetMember() {
 AnnotatedMember member = objectIdValueProperty.getMember();
 assertNull(member);
 }
}