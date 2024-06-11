package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class ObjectIdReferencePropertyTest {

 private ObjectIdInfo objectIdInfo;
 private SettableBeanProperty forward;
 private ObjectIdReferenceProperty property;
 private JsonDeserializer<?> deserializer;
 private NullValueProvider nullProvider;
 private Object pojo;

 @Before
 public void setUp() {
 objectIdInfo = mock(ObjectIdInfo.class);
 forward = mock(SettableBeanProperty.class);
 deserializer = mock(JsonDeserializer.class);
 nullProvider = mock(NullValueProvider.class);
 pojo = new Object();
 property = new ObjectIdReferenceProperty(forward, objectIdInfo);
 }

 @Test
 public void testWithName() {
 PropertyName newName = PropertyName.construct("newName");
 ObjectIdReferenceProperty newProperty = property.withName(newName);
 assertEquals(newName, newProperty.getName());
 }

 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer<?> newDeserializer = mock(JsonDeserializer.class);
 ObjectIdReferenceProperty newProperty = property.withValueDeserializer(newDeserializer);
 assertEquals(newDeserializer, newProperty.getValueDeserializer());
 }

 @Test
 public void testWithNullProvider() {
 NullValueProvider newNullProvider = mock(NullValueProvider.class);
 ObjectIdReferenceProperty newProperty = property.withNullProvider(newNullProvider);
 assertEquals(newNullProvider, newProperty.getNullProvider());
 }

 @Test
 public void testFixAccess() {
 DeserializationConfig config = mock(DeserializationConfig.class);
 property.fixAccess(config);
 verify(forward).fixAccess(config);
 }

 @Test
 public void testGetAnnotation() {
 Annotation annotation = mock(Annotation.class);
 when(forward.getAnnotation(Mockito.any(Class.class))).thenReturn(annotation);
 assertEquals(annotation, property.getAnnotation(Annotation.class));
 }

 @Test
 public void testGetMember() {
 AnnotatedMember member = mock(AnnotatedMember.class);
 when(forward.getMember()).thenReturn(member);
 assertEquals(member, property.getMember());
 }

 @Test
 public void testGetCreatorIndex() {
 int creatorIndex = 0;
 when(forward.getCreatorIndex()).thenReturn(creatorIndex);
 assertEquals(creatorIndex, property.getCreatorIndex());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 ObjectNode node = objectMapper().createObjectNode();
 Object value = new Object();
 ObjectIdReferenceProperty newProperty = new ObjectIdReferenceProperty(property, deserializer, nullProvider);
 newProperty.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), pojo);
 assertEquals(value, pojo);
 }

 @Test
 public void testDeserializeSetAndReturnNull() throws IOException {
 ObjectNode node = objectMapper().createObjectNode();
 UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);
 when(deserializer.getObjectIdReader()).thenReturn(new ObjectIdReader() {
 @Override
 public ObjectIdReader.IdType getIdType() {
 return ObjectIdReader.IdType.OBJECT_ID;
 }

 @Override
 public ObjectId createObjectId(JsonParser p, DeserializationContext ctxt) throws IOException {
 return reference;
 }
 });
 property.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), pojo);
 assertNull(pojo);
 verify(reference).getRoid().appendReferring(any(PropertyReferring.class));
 }

 @Test
 public void testDeserializeSetAndReturnValue() throws IOException {
 ObjectNode node = objectMapper().createObjectNode();
 Object value = new Object();
 property.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), pojo);
 assertEquals(value, pojo);
 }

 @Test(expected = IOException.class)
 public void testUnresolvedForwardReference() throws IOException {
 ObjectNode node = objectMapper().createObjectNode();
 UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);
 when(deserializer.getObjectIdReader()).thenReturn(null);
 property.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), pojo);
 fail("Expected IOException but got no exception");
 }

 @Test
 public void testSet() throws IOException {
 Object value = new Object();
 property.set(pojo, value);
 assertEquals(value, pojo);
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object value = new Object();
 Object newValue = new Object();
 when(property.setAndReturn(any(Object.class), any(Object.class))).thenReturn(newValue);
 assertEquals(newValue, property.setAndReturn(pojo, value));
 }
}