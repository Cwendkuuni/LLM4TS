package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ObjectIdReferencePropertyTest {

 private ObjectIdInfo objectIdInfo;
 private ObjectIdReferenceProperty objectIdReferenceProperty;
 private SettableBeanProperty settableBeanProperty;
 private JsonDeserializer<?> jsonDeserializer;
 private JsonParser jsonParser;
 private DeserializationContext deserializationContext;
 private Object instance;
 private NullValueProvider nullValueProvider;

 @Test
 public void testObjectIdReferencePropertyConstructor() {
 objectIdInfo = Mockito.mock(ObjectIdInfo.class);
 settableBeanProperty = Mockito.mock(SettableBeanProperty.class);
 jsonDeserializer = Mockito.mock(JsonDeserializer.class);
 jsonParser = Mockito.mock(JsonParser.class);
 deserializationContext = Mockito.mock(DeserializationContext.class);
 instance = Mockito.mock(Object.class);
 nullValueProvider = Mockito.mock(NullValueProvider.class);

 objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanProperty, objectIdInfo);

 assertEquals(settableBeanProperty, objectIdReferenceProperty.getBackRef());
 assertEquals(objectIdInfo, objectIdReferenceProperty.getObjectIdInfo());
 }

 @Test
 public void testObjectIdReferencePropertyConstructorWithJsonDeserializer() {
 objectIdInfo = Mockito.mock(ObjectIdInfo.class);
 settableBeanProperty = Mockito.mock(SettableBeanProperty.class);
 jsonDeserializer = Mockito.mock(JsonDeserializer.class);
 jsonParser = Mockito.mock(JsonParser.class);
 deserializationContext = Mockito.mock(DeserializationContext.class);
 instance = Mockito.mock(Object.class);
 nullValueProvider = Mockito.mock(NullValueProvider.class);

 objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanProperty, jsonDeserializer, nullValueProvider);

 assertEquals(settableBeanProperty, objectIdReferenceProperty.getBackRef());
 assertEquals(jsonDeserializer, objectIdReferenceProperty.getValueDeserializer());
 assertEquals(nullValueProvider, objectIdReferenceProperty.getNullProvider());
 }

 @Test
 public void testObjectIdReferencePropertyConstructorWithPropertyName() {
 objectIdInfo = Mockito.mock(ObjectIdInfo.class);
 settableBeanProperty = Mockito.mock(SettableBeanProperty.class);
 jsonDeserializer = Mockito.mock(JsonDeserializer.class);
 jsonParser = Mockito.mock(JsonParser.class);
 deserializationContext = Mockito.mock(DeserializationContext.class);
 instance = Mockito.mock(Object.class);
 nullValueProvider = Mockito.mock(NullValueProvider.class);

 PropertyName propertyName = Mockito.mock(PropertyName.class);

 objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanProperty, propertyName);

 assertEquals(settableBeanProperty, objectIdReferenceProperty.getBackRef());
 assertEquals(propertyName, objectIdReferenceProperty.getName());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 objectIdInfo = Mockito.mock(ObjectIdInfo.class);
 settableBeanProperty = Mockito.mock(SettableBeanProperty.class);
 jsonDeserializer = Mockito.mock(JsonDeserializer.class);
 jsonParser = Mockito.mock(JsonParser.class);
 deserializationContext = Mockito.mock(DeserializationContext.class);
 instance = Mockito.mock(Object.class);
 nullValueProvider = Mockito.mock(NullValueProvider.class);
 Object expectedValue = Mockito.mock(Object.class);

 when(jsonParser.getValueAsToken()).thenReturn(Mockito.mock(JsonToken.class));
 when(jsonParser.nextValue()).thenReturn(expectedValue);
 when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(expectedValue);
 when(settableBeanProperty.setAndReturn(instance, expectedValue)).thenReturn(expectedValue);

 objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanProperty, jsonDeserializer, nullValueProvider);

 objectIdReferenceProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

 verify(instance).set(eq(expectedValue));
 }

 @Test
 public void testDeserializeSetAndReturnNull() throws IOException {
 objectIdInfo = Mockito.mock(ObjectIdInfo.class);
 settableBeanProperty = Mockito.mock(SettableBeanProperty.class);
 jsonDeserializer = Mockito.mock(JsonDeserializer.class);
 jsonParser = Mockito.mock(JsonParser.class);
 deserializationContext = Mockito.mock(DeserializationContext.class);
 instance = Mockito.mock(Object.class);
 nullValueProvider = Mockito.mock(NullValueProvider.class);

 when(jsonParser.getValueAsToken()).thenReturn(Mockito.mock(JsonToken.class));
 when(jsonParser.nextValue()).thenReturn(null);
 when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(null);
 when(settableBeanProperty.setAndReturn(instance, null)).thenReturn(null);

 objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanProperty, jsonDeserializer, nullValueProvider);

 assertNull(objectIdReferenceProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance));
 }

 @Test
 public void testDeserializeSetAndReturnUnresolvedForwardReference() throws IOException {
 objectIdInfo = Mockito.mock(ObjectIdInfo.class);
 settableBeanProperty = Mockito.mock(SettableBeanProperty.class);
 jsonDeserializer = Mockito.mock(JsonDeserializer.class);
 jsonParser = Mockito.mock(JsonParser.class);
 deserializationContext = Mockito.mock(DeserializationContext.class);
 instance = Mockito.mock(Object.class);
 nullValueProvider = Mockito.mock(NullValueProvider.class);
 ObjectIdReferenceProperty parent = Mockito.mock(ObjectIdReferenceProperty.class);
 AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
 JsonDeserializer<?> jsonDeserializerParent = Mockito.mock(JsonDeserializer.class);
 Object id = new Object();
 UnresolvedForwardReference reference = Mockito.mock(UnresolvedForwardReference.class);
 Object pojo = Mockito.mock(Object.class);
 Class<?> rawClass = Mockito.mock(Class.class);

 when(jsonParser.getValueAsToken()).thenReturn(Mockito.mock(JsonToken.class));
 when(jsonParser.nextValue()).thenReturn(id);
 when(jsonDeserializer.getObjectIdReader()).thenReturn(null);
 when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(pojo);
 when(settableBeanProperty.setAndReturn(instance, pojo)).thenReturn(pojo);
 when(objectIdInfo.hasIdAnnotation()).thenReturn(false);
 when(objectIdInfo.getObjectIdReader()).thenReturn(null);
 when(jsonDeserializer.getValueDeserializer()).thenReturn(jsonDeserializerParent);
 when(jsonDeserializerParent.getObjectIdReader()).thenReturn(Mockito.mock(ObjectIdReader.class));
 when(jsonDeserializerParent.getObjectIdInfo()).thenReturn(objectIdInfo);
 when(parent.getBackRef()).thenReturn(settableBeanProperty);
 when(parent.getObjectIdInfo()).thenReturn(objectIdInfo);
 when(parent.deserialize(jsonParser, deserializationContext)).thenReturn(id);
 when(parent.getMember()).thenReturn(annotatedMember);
 when(parent.getCreatorIndex()).thenReturn(0);

 objectIdReferenceProperty = new ObjectIdReferenceProperty(parent, jsonDeserializer, nullValueProvider);

 assertNotNull(objectIdReferenceProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance));

 verify(jsonDeserializerParent, times(1)).getObjectIdReader();
 verify(objectIdInfo, times(1)).getObjectIdReader();
 verify(pojo, times(1)).set(eq(instance));
 verify(parent, times(1)).handleResolvedForwardReference(eq(id), eq(pojo));
 }

 @Test
 public void testFixAccess() throws IOException {
 objectIdInfo = Mockito.mock(ObjectIdInfo.class);
 settableBeanProperty = Mockito.mock(SettableBeanProperty.class);
 jsonDeserializer = Mockito.mock(JsonDeserializer.class);
 jsonParser = Mockito.mock(JsonParser.class);
 deserializationContext = Mockito.mock(DeserializationContext.class);
 instance = Mockito.mock(Object.class);
 nullValueProvider = Mockito.mock(NullValueProvider.class);

 objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanProperty, jsonDeserializer, nullValueProvider);
 objectIdReferenceProperty.fixAccess(deserializationContext);

 verify(settableBeanProperty).fixAccess(deserializationContext);
 }

 @Test
 public void testGetAnnotation() throws IOException {
 objectIdInfo = Mockito.mock(ObjectIdInfo.class);
 settableBeanProperty = Mockito.mock(SettableBeanProperty.class);
 jsonDeserializer = Mockito.mock(JsonDeserializer.class);
 jsonParser = Mockito.mock(JsonParser.class);
 deserializationContext = Mockito.mock(DeserializationContext.class);
 instance = Mockito.mock(Object.class);
 nullValueProvider = Mockito.mock(NullValueProvider.class);

 Annotation annotation = Mockito.mock(Annotation.class);

 when(jsonParser.getValueAsToken()).thenReturn(Mockito.mock(JsonToken.class));
 when(jsonParser.nextValue()).thenReturn(annotation);
 when(objectIdInfo.getAnnotation(eq(Annotation.class))).thenReturn(annotation);

 objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanProperty, jsonDeserializer, nullValueProvider);

 assertEquals(annotation, objectIdReferenceProperty.getAnnotation(Annotation.class));
 }

 @Test
 public void testGetMember() throws IOException {
 objectIdInfo = Mockito.mock(ObjectIdInfo.class);
 settableBeanProperty = Mockito.mock(SettableBeanProperty.class);
 jsonDeserializer = Mockito.mock(JsonDeserializer.class);
 jsonParser = Mockito.mock(JsonParser.class);
 deserializationContext = Mockito.mock(DeserializationContext.class);
 instance = Mockito.mock(Object.class);
 nullValueProvider = Mockito.mock(NullValueProvider.class);

 when(jsonParser.getValueAsToken()).thenReturn(Mockito.mock(JsonToken.class));
 when(jsonParser.nextValue()).thenReturn(null);
 when(objectIdInfo.getMember()).thenReturn(Mockito.mock(AnnotatedMember.class));

 objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanProperty, jsonDeserializer, nullValueProvider);

 assertEquals(Mockito.mock(AnnotatedMember.class), objectIdReferenceProperty.getMember());
 }

 @Test
 public void testGetCreatorIndex() throws IOException {
 objectIdInfo = Mockito.mock(ObjectIdInfo.class);
 settableBeanProperty = Mockito.mock(SettableBeanProperty.class);
 jsonDeserializer = Mockito.mock(JsonDeserializer.class);
 jsonParser = Mockito.mock(JsonParser.class);
 deserializationContext = Mockito.mock(DeserializationContext.class);
 instance = Mockito.mock(Object.class);
 nullValueProvider = Mockito.mock(NullValueProvider.class);

 when(objectIdInfo.getCreatorIndex()).thenReturn(1);

 objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanProperty, jsonDeserializer, nullValueProvider);

 assertEquals(1, objectIdReferenceProperty.getCreatorIndex());
 }

 @Test
 public void testGetType() throws IOException {
 objectIdInfo = Mockito.mock(ObjectIdInfo.class);
 settableBeanProperty = Mockito.mock(SettableBeanProperty.class);
 jsonDeserializer = Mockito.mock(JsonDeserializer.class);
 jsonParser = Mockito.mock(JsonParser.class);
 deserializationContext = Mockito.mock(DeserializationContext.class);
 instance = Mockito.mock(Object.class);
 nullValueProvider = Mockito.mock(NullValueProvider.class);

 when(objectIdInfo.getType()).thenReturn(ObjectNode.class);

 objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanProperty, jsonDeserializer, nullValueProvider);

 assertEquals(ObjectNode.class, objectIdReferenceProperty.getType());
 }

 @Test
 public void testSet() throws IOException {
 objectIdInfo = Mockito.mock(ObjectIdInfo.class);
 settableBeanProperty = Mockito.mock(SettableBeanProperty.class);
 jsonDeserializer = Mockito.mock(JsonDeserializer.class);
 jsonParser = Mockito.mock(JsonParser.class);
 deserializationContext = Mockito.mock(DeserializationContext.class);
 instance = Mockito.mock(Object.class);
 nullValueProvider = Mockito.mock(NullValueProvider.class);
 Object value = Mockito.mock(Object.class);

 objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanProperty, jsonDeserializer, nullValueProvider);

 objectIdReferenceProperty.set(instance, value);

 verify(settableBeanProperty).set(eq(instance), eq(value));
 }

 @Test
 public void testSetAndReturn() throws IOException {
 objectIdInfo = Mockito.mock(ObjectIdInfo.class);
 settableBeanProperty = Mockito.mock(SettableBeanProperty.class);
 jsonDeserializer = Mockito.mock(JsonDeserializer.class);
 jsonParser = Mockito.mock(JsonParser.class);
 deserializationContext = Mockito.mock(DeserializationContext.class);
 instance = Mockito.mock(Object.class);
 nullValueProvider = Mockito.mock(NullValueProvider.class);
 Object value = Mockito.mock(Object.class);

 when(jsonParser.getValueAsToken()).thenReturn(Mockito.mock(JsonToken.class));
 when(jsonParser.nextValue()).thenReturn(value);
 when(settableBeanProperty.setAndReturn(instance, value)).thenReturn(value);

 objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanProperty, jsonDeserializer, nullValueProvider);

 assertEquals(value, objectIdReferenceProperty.setAndReturn(instance, value));
 }
}