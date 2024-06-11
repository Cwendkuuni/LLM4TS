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

public class ObjectIdReferencePropertyTest {

 private ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
 private SettableBeanProperty forward = Mockito.mock(SettableBeanProperty.class);
 private JsonDeserializer<?> valueDeserializer = Mockito.mock(JsonDeserializer.class);
 private NullValueProvider nullProvider = Mockito.mock(NullValueProvider.class);
 private DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
 private ObjectIdReferenceProperty objectIdReferenceProperty;

 @Test
 public void testObjectIdReferencePropertyCreation() throws IOException {
 ObjectIdReferenceProperty newObjectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
 assertEquals("Forward property not set correctly", forward, newObjectIdReferenceProperty._forward);
 assertEquals("ObjectIdInfo not set correctly", objectIdInfo, newObjectIdReferenceProperty._objectIdInfo);
 }

 @Test
 public void testObjectIdReferencePropertyCreationWithSameProperties() {
 ObjectIdReferenceProperty newObjectIdReferenceProperty1 = new ObjectIdReferenceProperty(forward, objectIdInfo);
 ObjectIdReferenceProperty newObjectIdReferenceProperty2 = new ObjectIdReferenceProperty(newObjectIdReferenceProperty1, valueDeserializer, nullProvider);
 assertEquals("Forward property not set correctly in second object", forward, newObjectIdReferenceProperty2._forward);
 assertEquals("ObjectIdInfo not set correctly in second object", objectIdInfo, newObjectIdReferenceProperty2._objectIdInfo);
 }

 @Test
 public void testObjectIdReferencePropertyCreationWithNewName() {
 PropertyName newName = Mockito.mock(PropertyName.class);
 ObjectIdReferenceProperty newObjectIdReferenceProperty = new ObjectIdReferenceProperty(forward, newName);
 assertEquals("Forward property not set correctly", forward, newObjectIdReferenceProperty._forward);
 assertEquals("Name not set correctly", newName, newObjectIdReferenceProperty.getName());
 }

 @Test
 public void testWithName() {
 ObjectIdReferenceProperty newObjectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
 PropertyName newName = Mockito.mock(PropertyName.class);
 ObjectIdReferenceProperty newObjectIdReferencePropertyWithName = newObjectIdReferenceProperty.withName(newName);
 assertEquals("Forward property not set correctly", forward, newObjectIdReferencePropertyWithName._forward);
 assertEquals("Name not set correctly", newName, newObjectIdReferencePropertyWithName.getName());
 }

 @Test
 public void testWithValueDeserializer() {
 ObjectIdReferenceProperty newObjectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
 JsonDeserializer<?> deserializer = Mockito.mock(JsonDeserializer.class);
 ObjectIdReferenceProperty newObjectIdReferencePropertyWithDeserializer = newObjectIdReferenceProperty.withValueDeserializer(deserializer);
 assertEquals("Forward property not set correctly", forward, newObjectIdReferencePropertyWithDeserializer._forward);
 assertEquals("Deserializer not set correctly", deserializer, newObjectIdReferencePropertyWithDeserializer._valueDeserializer);
 }

 @Test
 public void testWithNullProvider() {
 ObjectIdReferenceProperty newObjectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
 NullValueProvider nullValueProvider = Mockito.mock(NullValueProvider.class);
 ObjectIdReferenceProperty newObjectIdReferencePropertyWithNullProvider = newObjectIdReferenceProperty.withNullProvider(nullValueProvider);
 assertEquals("Forward property not set correctly", forward, newObjectIdReferencePropertyWithNullProvider._forward);
 assertEquals("NullValueProvider not set correctly", nullValueProvider, newObjectIdReferencePropertyWithNullProvider._nullProvider);
 }

 @Test
 public void testFixAccess() throws IOException {
 ObjectIdReferenceProperty newObjectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
 newObjectIdReferenceProperty.fixAccess(config);
 Mockito.verify(forward).fixAccess(config);
 }

 @Test
 public void testGetAnnotation() throws IOException {
 Annotation annotation = Mockito.mock(Annotation.class);
 Mockito.when(forward.getAnnotation(Mockito.eq(annotation.getClass()))).thenReturn(annotation);
 ObjectIdReferenceProperty newObjectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
 assertEquals("Annotation not returned correctly", annotation, newObjectIdReferenceProperty.getAnnotation(annotation.getClass()));
 }

 @Test
 public void testGetMember() throws IOException {
 AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
 Mockito.when(forward.getMember()).thenReturn(annotatedMember);
 ObjectIdReferenceProperty newObjectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
 assertEquals("Member not returned correctly", annotatedMember, newObjectIdReferenceProperty.getMember());
 }

 @Test
 public void testGetCreatorIndex() throws IOException {
 int creatorIndex = 5;
 Mockito.when(forward.getCreatorIndex()).thenReturn(creatorIndex);
 ObjectIdReferenceProperty newObjectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
 assertEquals("Creator index not returned correctly", creatorIndex, newObjectIdReferenceProperty.getCreatorIndex());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object instance = Mockito.mock(Object.class);
 JsonParser jsonParser = Mockito.mock(JsonParser.class);
 Object value = Mockito.mock(Object.class);
 ObjectIdReferenceProperty newObjectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
 newObjectIdReferenceProperty.deserializeAndSet(jsonParser, config, instance);
 Mockito.verify(forward).set(instance, value);
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object instance = Mockito.mock(Object.class);
 Object value = Mockito.mock(Object.class);
 ObjectIdReferenceProperty newObjectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
 Mockito.when(forward.setAndReturn(instance, value)).thenReturn(value);
 Object result = newObjectIdReferenceProperty.deserializeSetAndReturn(jsonParser, config, instance);
 assertEquals("Result not returned correctly", value, result);
 }

 @Test
 public void testHandleResolvedForwardReference() throws IOException {
 UnresolvedForwardReference unresolvedForwardReference = Mockito.mock(UnresolvedForwardReference.class);
 Object id = "id";
 Object value = "value";
 ObjectIdReferenceProperty newObjectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
 Mockito.when(newObjectIdReferenceProperty._objectIdInfo.getIdValueClass()).thenReturn(Object.class);
 PropertyReferring propertyReferring = Mockito.mock(PropertyReferring.class);
 Mockito.when(newObjectIdReferenceProperty._parent).thenReturn(newObjectIdReferenceProperty);
 Mockito.when(newObjectIdReferenceProperty._forward.set(instance, value)).thenReturn(value);
 Mockito.when(newObjectIdReferenceProperty._forward.setAndReturn(instance, value)).thenReturn(value);
 Mockito.when(newObjectIdReferenceProperty._forward.getMember()).thenReturn(annotatedMember);
 Mockito.when(newObjectIdReferenceProperty._valueDeserializer.getObjectIdReader()).thenReturn(null);
 newObjectIdReferenceProperty.handleResolvedForwardReference(id, value);
 Mockito.verify(propertyReferring).handleResolvedForwardReference(id, value);
 }
}