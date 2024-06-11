package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

import org.junit.Test;

public class ObjectIdReferencePropertyTest {

 private static final ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
 private static final SettableBeanProperty forward = mock(SettableBeanProperty.class);
 private static final JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
 private static final NullValueProvider nullValueProvider = mock(NullValueProvider.class);
 private static final ObjectIdReferenceProperty objectIdReferenceProperty;

 static {
 ObjectIdReferenceProperty obj = new ObjectIdReferenceProperty(forward, objectIdInfo);
 objectIdReferenceProperty = new ObjectIdReferenceProperty(obj, valueDeserializer, nullValueProvider);
 }

 @Test
 public void testWithName() {
 PropertyName newName = mock(PropertyName.class);
 ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withName(newName);
 assertSame(objectIdReferenceProperty, newObjectIdReferenceProperty.getForward());
 assertSame(newName, newObjectIdReferenceProperty.getName());
 }

 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer<?> deser = mock(JsonDeserializer.class);
 ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withValueDeserializer(deser);
 assertSame(objectIdReferenceProperty, newObjectIdReferenceProperty.getForward());
 assertSame(deser, newObjectIdReferenceProperty.getValueDeserializer());
 }

 @Test
 public void testWithNullProvider() {
 NullValueProvider nva = mock(NullValueProvider.class);
 ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withNullProvider(nva);
 assertSame(objectIdReferenceProperty, newObjectIdReferenceProperty.getForward());
 assertSame(nva, newObjectIdReferenceProperty.getNullProvider());
 }

 @Test
 public void testFixAccess() throws IOException {
 DeserializationConfig config = mock(DeserializationConfig.class);
 objectIdReferenceProperty.fixAccess(config);
 verify(forward).fixAccess(config);
 }

 @Test
 public void testGetAnnotation() {
 Class<Annotation> annotationClass = mock(Annotation.class);
 assertNull(objectIdReferenceProperty.getAnnotation(annotationClass));
 assertNull(forward.getAnnotation(annotationClass));
 }

 @Test
 public void testGetMember() {
 AnnotatedMember member = mock(AnnotatedMember.class);
 assertSame(member, objectIdReferenceProperty.getMember());
 assertSame(member, forward.getMember());
 }

 @Test
 public void testGetCreatorIndex() {
 assertSame(0, objectIdReferenceProperty.getCreatorIndex());
 assertSame(0, forward.getCreatorIndex());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 JsonParser p = mock(JsonParser.class);
 DeserializationContext ctxt = mock(DeserializationContext.class);
 Object instance = mock(Object.class);
 when(valueDeserializer.deserialize(p, ctxt)).thenReturn("value");
 objectIdReferenceProperty.deserializeAndSet(p, ctxt, instance);
 verify(forward).set(instance, "value");
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 JsonParser p = mock(JsonParser.class);
 DeserializationContext ctxt = mock(DeserializationContext.class);
 Object instance = mock(Object.class);
 Object returnValue = mock(Object.class);
 when(valueDeserializer.deserialize(p, ctxt)).thenReturn(returnValue);
 when(forward.setAndReturn(instance, returnValue)).thenReturn("returnValue");
 Object result = objectIdReferenceProperty.deserializeSetAndReturn(p, ctxt, instance);
 assertSame("returnValue", result);
 }

 @Test
 public void testSet() throws IOException {
 Object instance = mock(Object.class);
 Object value = mock(Object.class);
 objectIdReferenceProperty.set(instance, value);
 verify(forward).set(instance, value);
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object instance = mock(Object.class);
 Object value = mock(Object.class);
 Object result = mock(Object.class);
 when(forward.setAndReturn(instance, value)).thenReturn(result);
 Object returnValue = objectIdReferenceProperty.setAndReturn(instance, value);
 assertSame(result, returnValue);
 }

 @Test
 public void testCreatePropertyReferring() {
 UnresolvedForwardReference ref = mock(UnresolvedForwardReference.class);
 Class<?> type = mock(Class.class);
 Object ob = mock(Object.class);
 ObjectIdReferencePropertyPropertyReferring = new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, ref, type, ob);
 assertSame(ref, ObjectIdReferencePropertyPropertyReferring.getReferring());
 assertSame(type, ObjectIdReferencePropertyPropertyReferring.getType());
 assertSame(ob, ObjectIdReferencePropertyPropertyReferring.getPojo());
 }
}