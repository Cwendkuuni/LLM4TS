package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

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
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class ObjectIdReferencePropertyTest {

private ObjectIdInfo objectIdInfo;
private SettableBeanProperty forward;
private ObjectIdReferenceProperty objectIdReferenceProperty;
private JsonDeserializer<?> jsonDeserializer;
private NullValueProvider nullValueProvider;

@Before
public void setup() {
objectIdInfo = mock(ObjectIdInfo.class);
forward = mock(SettableBeanProperty.class);
jsonDeserializer = mock(JsonDeserializer.class);
nullValueProvider = mock(NullValueProvider.class);
objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
}

@Test
public void testConstructorWithForwardAndObjectIdInfo() {
ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
assertNotNull(objectIdReferenceProperty);
}

@Test
public void testConstructorWithSameProperties() {
ObjectIdReferenceProperty src = objectIdReferenceProperty;
ObjectIdReferenceProperty objectIdReferenceProperty2 = new ObjectIdReferenceProperty(src, jsonDeserializer, nullValueProvider);
assertNotNull(objectIdReferenceProperty2);
assertSame(objectIdReferenceProperty, objectIdReferenceProperty2);
}

@Test
public void testConstructorWithNewName() {
PropertyName newName = PropertyName.construct("newName");
ObjectIdReferenceProperty objectIdReferenceProperty2 = new ObjectIdReferenceProperty(objectIdReferenceProperty, newName);
assertNotNull(objectIdReferenceProperty2);
assertNotSame(objectIdReferenceProperty, objectIdReferenceProperty2);
}

@Test
public void testGetAnnotation() throws Exception {
when(forward.getAnnotation(mock(Annotation.class))).thenReturn(mock(Annotation.class));
assertEquals(mock(Annotation.class), objectIdReferenceProperty.getAnnotation(mock(Annotation.class)));
}

@Test
public void testGetMember() throws Exception {
assertSame(forward.getMember(), objectIdReferenceProperty.getMember());
}

@Test
public void testGetCreatorIndex() throws Exception {
assertEquals(forward.getCreatorIndex(), objectIdReferenceProperty.getCreatorIndex());
}

@Test
public void testFixAccess() throws Exception {
objectIdReferenceProperty.fixAccess(Mockito.mock(DeserializationConfig.class));
verify(forward).fixAccess(Mockito.eq(Mockito.mock(DeserializationConfig.class)));
}

@Test
public void testDeserializeAndSet() throws Exception {
ObjectNode node = Mockito.mock(ObjectNode.class);
when(forward.deserializer().deserialize(eq(node), eq(Mockito.mock(DeserializationContext.class)), anyObject())).thenReturn(mock(Object.class));
objectIdReferenceProperty.deserializeAndSet(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), Mockito.mock(Object.class));
verify(forward).deserializer().deserialize(eq(node), eq(Mockito.mock(DeserializationContext.class)), anyObject());
}

@Test
public void testDeserializeSetAndReturn() throws Exception {
ObjectNode node = Mockito.mock(ObjectNode.class);
when(forward.deserializer().deserialize(eq(node), eq(Mockito.mock(DeserializationContext.class)), anyObject())).thenReturn(mock(Object.class));
ArgumentCaptor<Object> instanceCaptor = ArgumentCaptor.forClass(Object.class);
objectIdReferenceProperty.deserializeSetAndReturn(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), instanceCaptor.capture());
assertEquals(mock(Object.class), instanceCaptor.getValue());
verify(forward).deserializer().deserialize(eq(node), eq(Mockito.mock(DeserializationContext.class)), anyObject());
}

@Test
public void testSet() throws IOException {
Object value = new Object();
objectIdReferenceProperty.set(Mockito.mock(Object.class), value);
verify(forward).set(eq(Mockito.mock(Object.class)), value);
}

@Test
public void testSetAndReturn() throws IOException {
Object value = new Object();
Object expectedValue = new Object();
when(forward.setAndReturn(Mockito.mock(Object.class), value)).thenReturn(expectedValue);
assertEquals(expectedValue, objectIdReferenceProperty.setAndReturn(Mockito.mock(Object.class), value));
}

@Test(expected = UnresolvedForwardReference.class)
public void testDeserializeSetAndReturnWithUnresolvedForwardReference() throws IOException {
UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);
when(forward.deserializer().deserialize(eq(Mockito.mock(JsonParser.class)), eq(Mockito.mock(DeserializationContext.class)), anyObject())).thenThrow(reference);
objectIdReferenceProperty.deserializeSetAndReturn(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), Mockito.mock(Object.class));
verify(forward).deserializer().deserialize(eq(Mockito.mock(JsonParser.class)), eq(Mockito.mock(DeserializationContext.class)), anyObject());
}

@Test
public void testHandleResolvedForwardReference() throws IOException {
Object id = new Object();
Object value = new Object();
when(forward.getMember().getAnnotation(ObjectIdInfo.class)).thenReturn(objectIdInfo);
when(objectIdInfo.getIdProperty()).thenReturn(objectIdReferenceProperty);
ObjectIdReferenceProperty.PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, reference, Object.class, Mockito.mock(Object.class));
objectIdReferenceProperty.handleResolvedForwardReference(id, value);
verify(propertyReferring).handleResolvedForwardReference(eq(id), eq(value));
}
}