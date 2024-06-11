package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;
 private JsonDeserializer<Object> valueDeserializer;
 private JsonParser jsonParser;
 private DeserializationContext deserializationContext;
 private Object instance;

 @Before
 public void setup() {
 objectIdReader = mock(ObjectIdReader.class);
 valueDeserializer = mock(JsonDeserializer.class);
 jsonParser = mock(JsonParser.class);
 deserializationContext = mock(DeserializationContext.class);
 instance = new Object();

 when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
 when(objectIdReader.getIdType()).thenReturn(ObjectIdType.STRING_ID);
 when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
 when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
 when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 }

 @Test
 public void testGetAnnotation() {
 assertNull(objectIdValueProperty.getAnnotation(ObjectId.class));
 }

 @Test
 public void testGetMember() {
 assertNull(objectIdValueProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 String id = "1";
 when(valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(id);

 objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

 verify(objectIdReader.idProperty).bindItem(instance);
 verify(objectIdReader.idProperty).setAndReturn(instance, id);
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 String id = "1";
 when(valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(id);

 Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

 assertEquals(instance, result);
 }

 @Test
 public void testSet() throws IOException {
 String id = "1";
 objectIdValueProperty.set(instance, id);

 SettableBeanProperty idProp = objectIdReader.idProperty;
 verify(idProp).setAndReturn(instance, id);
 }

 @Test
 public void testSetAndReturn() throws IOException {
 String id = "1";
 Object result = objectIdValueProperty.setAndReturn(instance, id);

 assertEquals(instance, result);
 }
}