package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
 private SettableBeanProperty settableBeanProperty;
 private ObjectIdReferenceProperty objectIdReferenceProperty;
 private JsonDeserializer<?> jsonDeserializer;
 private Object pojo;
 private Object referencedObject;
 private JsonParser mockJsonParser;
 private DeserializationContext mockContext;
 private ObjectNode mockObjectNode;

 @Before
 public void setup() throws Exception {
 objectIdInfo = Mockito.mock(ObjectIdInfo.class);
 settableBeanProperty = Mockito.mock(SettableBeanProperty.class);
 jsonDeserializer = Mockito.mock(JsonDeserializer.class);
 pojo = new Object();
 referencedObject = new Object();
 mockJsonParser = Mockito.mock(JsonParser.class);
 mockContext = Mockito.mock(DeserializationContext.class);
 mockObjectNode = Mockito.mock(ObjectNode.class);
 }

 @Test
 public void testObjectIdReferencePropertyConstruction() {
 objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanProperty, objectIdInfo);
 assertNotNull(objectIdReferenceProperty);
 assertEquals(settableBeanProperty, objectIdReferenceProperty._forward);
 assertEquals(objectIdInfo, objectIdReferenceProperty._objectIdInfo);
 }

 @Test
 public void testObjectIdReferencePropertyConstructionWithJsonDeserializer() {
 objectIdReferenceProperty = new ObjectIdReferenceProperty(objectIdReferenceProperty, jsonDeserializer, null);
 assertNotNull(objectIdReferenceProperty);
 assertEquals(objectIdReferenceProperty, objectIdReferenceProperty._forward);
 assertEquals(jsonDeserializer, objectIdReferenceProperty._valueDeserializer);
 }

 @Test
 public void testObjectIdReferencePropertyConstructionWithPropertyName() {
 PropertyName newName = Mockito.mock(PropertyName.class);
 objectIdReferenceProperty = new ObjectIdReferenceProperty(objectIdReferenceProperty, newName);
 assertNotNull(objectIdReferenceProperty);
 assertEquals(objectIdReferenceProperty, objectIdReferenceProperty._forward);
 assertEquals(newName, objectIdReferenceProperty._name);
 }

 @Test
 public void testGetAnnotation() throws Exception {
 Annotation annotation = Mockito.mock(Annotation.class);
 Mockito.when(objectIdInfo.getIdAnnotation()).thenReturn(annotation);
 assertEquals(annotation, objectIdReferenceProperty.getAnnotation(annotation.getClass()));
 }

 @Test
 public void testGetMember() throws Exception {
 AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
 Mockito.when(settableBeanProperty.getMember()).thenReturn(annotatedMember);
 assertEquals(annotatedMember, objectIdReferenceProperty.getMember());
 }

 @Test
 public void testGetCreatorIndex() throws Exception {
 int creatorIndex = 0;
 Mockito.when(settableBeanProperty.getCreatorIndex()).thenReturn(creatorIndex);
 assertEquals(creatorIndex, objectIdReferenceProperty.getCreatorIndex());
 }

 @Test
 public void testFixAccess() throws Exception {
 objectIdReferenceProperty.fixAccess(mockContext);
 Mockito.verify(settableBeanProperty).fixAccess(mockContext);
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 Object id = new Object();
 Object value = new Object();
 UnresolvedForwardReference reference = Mockito.mock(UnresolvedForwardReference.class);
 Mockito.when(mockJsonParser.getValueAsToken()).thenReturn(Mockito.mock(JsonToken.class));
 Mockito.when(mockContext.handleUnresolvedForwardReference(Mockito.any(UnresolvedForwardReference.class), Mockito.any()))
 .thenReturn(value);
 Mockito.when(mockContext.findValueDeserializer(settableBeanProperty.getMember().getAnnotation(JsonIdentityInfo.class)))
 .thenReturn(jsonDeserializer);
 Mockito.when(jsonDeserializer.deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class))).thenReturn(id);
 Mockito.when(mockObjectNode.get("$ref")).thenReturn(mockObjectNode);
 Mockito.when(mockParser.getCodec()).thenReturn(Mockito.mock(ObjectMapper.class));
 ArgumentCaptor<Object> instanceCaptor = ArgumentCaptor.forClass(Object.class);
 ArgumentCaptor<Object> idCaptor = ArgumentCaptor.forClass(Object.class);
 ArgumentCaptor<Object> valueCaptor = ArgumentCaptor.forClass(Object.class);
 ArgumentCaptor<Object> instanceCaptor2 = ArgumentCaptor.forClass(Object.class);

 objectIdReferenceProperty.deserializeAndSet(mockJsonParser, mockContext, pojo);
 assertEquals(id, value);
 assertEquals(pojo, instanceCaptor.getValue());
 assertEquals(id, instanceCaptor2.getValue());
 Mockito.verify(mockContext).handleUnresolvedForwardReference(reference, objectIdReferenceProperty);
 Mockito.verify(mockContext).findValueDeserializer(settableBeanProperty.getMember().getAnnotation(JsonIdentityInfo.class));
 Mockito.verify(jsonDeserializer).deserialize(mockJsonParser, mockContext);
 Mockito.verify(objectIdReferenceProperty).set(instanceCaptor2.getValue(), id);
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 Object id = new Object();
 Object value = new Object();
 UnresolvedForwardReference reference = Mockito.mock(UnresolvedForwardReference.class);
 Mockito.when(mockJsonParser.getValueAsToken()).thenReturn(Mockito.mock(JsonToken.class));
 Mockito.when(mockContext.handleUnresolvedForwardReference(Mockito.any(UnresolvedForwardReference.class), Mockito.any()))
 .thenReturn(value);
 Mockito.when(mockContext.findValueDeserializer(settableBeanProperty.getMember().getAnnotation(JsonIdentityInfo.class)))
 .thenReturn(jsonDeserializer);
 Mockito.when(jsonDeserializer.deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class))).thenReturn(id);
 Mockito.when(mockContext.findNullValueProvider()).thenReturn(null);
 Mockito.when(mockObjectNode.get("$ref")).thenReturn(mockObjectNode);
 Mockito.when(mockParser.getCodec()).thenReturn(Mockito.mock(ObjectMapper.class));
 ArgumentCaptor<Object> instanceCaptor = ArgumentCaptor.forClass(Object.class);
 ArgumentCaptor<Object> idCaptor = ArgumentCaptor.forClass(Object.class);
 ArgumentCaptor<Object> valueCaptor = ArgumentCaptor.forClass(Object.class);

 Object result = objectIdReferenceProperty.deserializeSetAndReturn(mockJsonParser, mockContext, pojo);
 assertEquals(id, result);
 assertEquals(pojo, instanceCaptor.getValue());
 Mockito.verify(mockContext).handleUnresolvedForwardReference(reference, objectIdReferenceProperty);
 Mockito.verify(mockContext).findValueDeserializer(settableBeanProperty.getMember().getAnnotation(JsonIdentityInfo.class));
 Mockito.verify(jsonDeserializer).deserialize(mockJsonParser, mockContext);
 Mockito.verify(objectIdReferenceProperty).set(instanceCaptor.getValue(), id);
 }

 @Test
 public void testSet() throws Exception {
 Object value = new Object();
 objectIdReferenceProperty.set(pojo, value);
 Mockito.verify(settableBeanProperty).set(pojo, value);
 }

 @Test
 public void testSetAndReturn() throws Exception {
 Object value = new Object();
 Object result = objectIdReferenceProperty.setAndReturn(pojo, value);
 assertEquals(value, result);
 Mockito.verify(settableBeanProperty).setAndReturn(pojo, value);
 }

 @Test
 public void testPropertyReferring() throws Exception {
 UnresolvedForwardReference reference = Mockito.mock(UnresolvedForwardReference.class);
 PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, reference, Object.class, pojo);
 assertEquals(reference, propertyReferring.getReferencedObject());
 }

 @Test
 public void testPropertyReferringHandleResolvedForwardReference() throws Exception {
 Object value = new Object();
 UnresolvedForwardReference reference = Mockito.mock(UnresolvedForwardReference.class);
 PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, reference, Object.class, pojo);
 Mockito.when(reference.getId()).thenReturn(1L);
 propertyReferring.handleResolvedForwardReference(1L, value);
 Mockito.verify(objectIdReferenceProperty).set(pojo, value);
 }
}