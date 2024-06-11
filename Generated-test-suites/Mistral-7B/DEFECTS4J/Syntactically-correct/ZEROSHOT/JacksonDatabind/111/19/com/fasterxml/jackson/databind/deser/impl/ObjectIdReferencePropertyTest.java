package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;

public class ObjectIdReferencePropertyTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testObjectIdReferencePropertyCreation() {
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
 assertNull(property._forward);
 assertNull(property._objectIdInfo);
 }

 @Test
 public void testObjectIdReferencePropertyCreationWithForwardProperty() {
 SettableBeanProperty forward = mock(SettableBeanProperty.class);
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, null);
 assertSame(forward, property._forward);
 assertNull(property._objectIdInfo);
 }

 @Test
 public void testObjectIdReferencePropertyCreationWithObjectIdInfo() {
 ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
 SettableBeanProperty forward = mock(SettableBeanProperty.class);
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
 assertSame(forward, property._forward);
 assertSame(objectIdInfo, property._objectIdInfo);
 }

 @Test
 public void testObjectIdReferencePropertyCreationWithSameProperties() {
 ObjectIdReferenceProperty src = mock(ObjectIdReferenceProperty.class);
 JsonDeserializer<?> deser = mock(JsonDeserializer.class);
 nullValueProvider = mock(NullValueProvider.class);
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, deser, nullValueProvider);
 assertSame(src, property);
 }

 @Test
 public void testObjectIdReferencePropertyCreationWithNewName() {
 ObjectIdReferenceProperty src = mock(ObjectIdReferenceProperty.class);
 PropertyName newName = mock(PropertyName.class);
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, newName);
 assertSame(src, property);
 assertSame(newName, property.getName());
 }

 @Test
 public void testGetMember() throws Exception {
 ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
 SettableBeanProperty forward = mock(SettableBeanProperty.class);
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
 AnnotatedMember member = property.getMember();
 assertSame(forward.getMember(), member);
 }

 @Test
 public void testGetName() throws Exception {
 ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
 SettableBeanProperty forward = mock(SettableBeanProperty.class);
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
 PropertyName name = property.getName();
 assertNotNull(name);
 assertEquals("", name.getName());
 }

 @Test
 public void testGetValueDeserializer() throws Exception {
 ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
 SettableBeanProperty forward = mock(SettableBeanProperty.class);
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
 JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
 JsonDeserializer<?> returnedDeserializer = property.getValueDeserializer();
 assertSame(deserializer, returnedDeserializer);
 }

 @Test
 public void testGetNullProvider() throws Exception {
 ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
 SettableBeanProperty forward = mock(SettableBeanProperty.class);
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
 NullValueProvider nullValueProvider = mock(NullValueProvider.class);
 NullValueProvider returnedNullProvider = property.getNullProvider();
 assertSame(nullValueProvider, returnedNullProvider);
 }

 @Test
 public void testFixAccess() throws Exception {
 ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
 SettableBeanProperty forward = mock(SettableBeanProperty.class);
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
 DeserializationConfig config = mock(DeserializationConfig.class);
 property.fixAccess(config);
 verify(forward).fixAccess(config);
 }

 @Test
 public void testGetAnnotation() throws Exception {
 ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
 SettableBeanProperty forward = mock(SettableBeanProperty.class);
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
 Class<? extends Annotation> annotationClass = mock(Class.class);
 Object annotation = property.getAnnotation(annotationClass);
 assertNull(annotation);
 }

 @Test
 public void testHandleResolvedForwardReference() throws Exception {
 ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
 SettableBeanProperty forward = mock(SettableBeanProperty.class);
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
 Object id = "id";
 Object value = new Object();
 property.handleResolvedForwardReference(id, value);
 verify(forward).set(eq(property._pojo), value);
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
 SettableBeanProperty forward = mock(SettableBeanProperty.class);
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
 Object pojo = new Object();
 JsonParser parser = mock(JsonParser.class);
 ObjectNode node = mapper.createObjectNode();
 Object id = "id";
 node.put("id", id);
 Object deserializedValue = new Object();
 when(parser.readValueAs(ObjectNode.class)).thenReturn(node);
 when(forward.deserializeAndSet(parser, null, pojo)).thenReturn(deserializedValue);
 property.deserializeAndSet(parser, null, pojo);
 verify(forward).deserializeAndSet(parser, null, pojo);
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
 SettableBeanProperty forward = mock(SettableBeanProperty.class);
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
 Object pojo = new Object();
 Object deserializedValue = new Object();
 property.deserializeSetAndReturn(mock(JsonParser.class), null, pojo);
 assertEquals(deserializedValue, property.get(_pojo));
 }

 @Test
 public void testSet() throws Exception {
 ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
 SettableBeanProperty forward = mock(SettableBeanProperty.class);
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
 Object pojo = new Object();
 Object value = new Object();
 property.set(pojo, value);
 verify(forward).set(pojo, value);
 }

 @Test
 public void testSetAndReturn() throws Exception {
 ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
 SettableBeanProperty forward = mock(SettableBeanProperty.class);
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
 Object pojo = new Object();
 Object value = new Object();
 Object returnedValue = property.setAndReturn(pojo, value);
 assertEquals(value, returnedValue);
 }

 @Test
 public void testCreatePropertyReferring() {
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
 UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);
 Class<?> type = Object.class;
 Object pojo = new Object();
 PropertyReferring referencing = property.createPropertyReferring(reference, type, pojo);
 assertSame(property, referencing._parent);
 assertSame(reference, referencing._ref);
 assertSame(type, referencing._type);
 assertSame(pojo, referencing._pojo);
 }
}