package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer.Std;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.databind.util.ObjectIdGenerator.AlwaysIdGenerator;
import com.fasterxml.jackson.databind.util.ObjectIdResolver.DefaultObjectIdResolver;

public class ObjectIdValuePropertyTest {

 private ObjectMapper objectMapper;
 private ObjectIdValueProperty property;
 private JsonParser parser;

 @Before
 public void setup() {
 ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
 when(objectIdReader.propertyName).thenReturn(new PropertyName("objectId"));
 when(objectIdReader.getIdType()).thenReturn(String.class);
 when(objectIdReader.generator).thenReturn(new AlwaysIdGenerator());
 when(objectIdReader.resolver).thenReturn(new DefaultObjectIdResolver());
 when(objectIdReader.idProperty).thenReturn(new Std(String.class, null, null, null, null, null, null, new ToStringSerializer(String.class), NameTransformer.IDENTITY));

 property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
 objectMapper = new ObjectMapper();
 parser = mock(JsonParser.class);
 }

 @Test
 public void testGetAnnotation() throws Exception {
 assertNull(property.getAnnotation(ObjectIdGenerator.class));
 }

 @Test
 public void testGetMember() throws Exception {
 assertNull(property.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 Object id = "123";
 when(parser.getValueAsString()).thenReturn(id);
 property.deserializeAndSet(parser, objectMapper.getDeserializationContext(), null);

 Object instance = new Object();
 Object expectedId = "123";
 assertSame(expectedId, objectMapper.findValueToDeserialize(parser, Object.class));

 // verify that id is set on the instance
 ObjectIdValueProperty idProperty = (ObjectIdValueProperty) property;
 SettableBeanProperty idProp = idProperty.getIdProperty();
 assertSame(expectedId, idProp.get(instance));
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 Object id = "123";
 when(parser.getValueAsString()).thenReturn(id);
 Object instance = new Object();
 Object result = property.deserializeSetAndReturn(parser, objectMapper.getDeserializationContext(), instance);

 assertSame(instance, result);
 }

 @Test
 public void testSet() throws Exception {
 Object id = "123";
 Object instance = new Object();
 property.set(instance, id);

 // verify that id is set on the instance
 ObjectIdValueProperty idProperty = (ObjectIdValueProperty) property;
 SettableBeanProperty idProp = idProperty.getIdProperty();
 assertSame("123", idProp.get(instance));
 }

 @Test
 public void testSetAndReturn() throws Exception {
 Object id = "123";
 Object instance = new Object();
 Object result = property.setAndReturn(instance, id);

 assertSame(instance, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnsupportedOperationException() throws Exception {
 property.set(null, null);
 }
}