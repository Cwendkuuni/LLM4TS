package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotate.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

public class ObjectIdReferencePropertyTest {

 private ObjectIdReferenceProperty objectIdReferenceProperty;
 private ObjectIdReferenceProperty objectIdReferenceProperty2;
 private SettableBeanProperty settableBeanProperty;
 private SettableBeanProperty settableBeanProperty2;
 private ObjectIdInfo objectIdInfo;
 private JsonDeserializer<?> jsonDeserializer;
 private JsonDeserializer<?> jsonDeserializer2;
 private Object obj;
 private Object obj2;
 private JsonParser jsonParser;
 private DeserializationContext deserializationContext;
 private SimpleBeanPropertyFilter simpleBeanPropertyFilter;

 @Before
 public void setup() {
 objectIdInfo = Mockito.mock(ObjectIdInfo.class);
 jsonDeserializer = Mockito.mock(JsonDeserializer.class);
 jsonDeserializer2 = Mockito.mock(JsonDeserializer.class);
 settableBeanProperty = Mockito.mock(SettableBeanProperty.class);
 settableBeanProperty2 = Mockito.mock(SettableBeanProperty.class);
 obj = Mockito.mock(Object.class);
 obj2 = Mockito.mock(Object.class);
 jsonParser = Mockito.mock(JsonParser.class);
 deserializationContext = Mockito.mock(DeserializationContext.class);
 simpleBeanPropertyFilter = Mockito.mock(SimpleBeanPropertyFilter.class);

 objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanProperty, objectIdInfo);
 objectIdReferenceProperty2 = new ObjectIdReferenceProperty(objectIdReferenceProperty, jsonDeserializer, null);
 }

 @Test
 public void testConstructorWithSettableBeanPropertyAndObjectIdInfo() {
 // Test constructor with SettableBeanProperty and ObjectIdInfo
 assertEquals(settableBeanProperty, objectIdReferenceProperty._forward);
 assertNull(objectIdReferenceProperty._valueDeserializer);
 assertNull(objectIdReferenceProperty._nullProvider);
 assertNotNull(objectIdReferenceProperty._objectIdInfo);
 }

 @Test
 public void testConstructorWithObjectIdReferencePropertyAndJsonDeserializerAndNullValueProvider() {
 // Test constructor with ObjectIdReferenceProperty, JsonDeserializer, and NullValueProvider
 assertEquals(objectIdReferenceProperty, objectIdReferenceProperty2._forward);
 assertNotNull(objectIdReferenceProperty2._valueDeserializer);
 assertNull(objectIdReferenceProperty2._nullProvider);
 }

 @Test
 public void testConstructorWithObjectIdReferencePropertyAndNewName() {
 // Test constructor with ObjectIdReferenceProperty and new name
 assertEquals(objectIdReferenceProperty, objectIdReferenceProperty2._forward);
 assertNotNull(objectIdReferenceProperty2._valueDeserializer);
 assertNull(objectIdReferenceProperty2._nullProvider);
 assertNotNull(objectIdReferenceProperty2.getName());
 }

 @Test
 public void testGetAnnotation() {
 // Test getAnnotation
 Annotation annotation = Mockito.mock(Annotation.class);
 Mockito.when(settableBeanProperty.getAnnotation(JsonProperty.class)).thenReturn(annotation);
 Mockito.when(objectIdReferenceProperty.getAnnotation(JsonProperty.class)).thenReturn(annotation);
 assertEquals(annotation, objectIdReferenceProperty.getAnnotation(JsonProperty.class));
 }

 @Test
 public void testGetMember() {
 // Test getMember
 AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
 Mockito.when(settableBeanProperty.getMember()).thenReturn(annotatedMember);
 Mockito.when(objectIdReferenceProperty.getMember()).thenReturn(annotatedMember);
 assertEquals(annotatedMember, objectIdReferenceProperty.getMember());
 }

 @Test
 public void testGetCreatorIndex() {
 // Test getCreatorIndex
 int creatorIndex = 0;
 Mockito.when(settableBeanProperty.getCreatorIndex()).thenReturn(creatorIndex);
 Mockito.when(objectIdReferenceProperty.getCreatorIndex()).thenReturn(creatorIndex);
 assertEquals(creatorIndex, objectIdReferenceProperty.getCreatorIndex());
 }

 @Test
 public void testDeserializeSetAndReturnNull() throws IOException {
 // Test deserializeSetAndReturn null
 Mockito.when(jsonParser.nextValue()).thenReturn(null);
 Mockito.when(jsonDeserializer.deserialize(jsonParser, deserializationContext, obj)).thenReturn(null);
 Mockito.when(objectIdReferenceProperty.deserializeSetAndReturn(jsonParser, deserializationContext, obj)).thenReturn(null);
 assertNull(objectIdReferenceProperty.deserializeSetAndReturn(jsonParser, deserializationContext, obj));
 }

 @Test
 public void testDeserializeSetAndReturnObject() throws IOException {
 // Test deserializeSetAndReturn Object
 Mockito.when(jsonParser.nextValue()).thenReturn(ObjectNode.parse("{\"id\":1,\"name\":\"John\"}"));
 Mockito.when(jsonDeserializer.deserialize(jsonParser, deserializationContext, obj)).thenReturn(new Object());
 Mockito.when(objectIdReferenceProperty.deserializeSetAndReturn(jsonParser, deserializationContext, obj)).thenReturn(new Object());
 Object result = objectIdReferenceProperty.deserializeSetAndReturn(jsonParser, deserializationContext, obj);
 assertEquals(new Object(), result);
 }

 @Test(expected = UnresolvedForwardReference.class)
 public void testDeserializeSetAndReturnUnresolvedForwardReference() throws IOException {
 // Test deserializeSetAndReturn UnresolvedForwardReference
 Mockito.when(jsonParser.nextValue()).thenReturn(ObjectNode.parse("{\"@ref\":\"ref1\"}"));
 Mockito.when(jsonDeserializer.deserialize(jsonParser, deserializationContext, obj)).thenReturn(new Object());
 Mockito.when(objectIdReferenceProperty.deserializeSetAndReturn(jsonParser, deserializationContext, obj)).thenThrow(new UnresolvedForwardReference("ref1"));
 }

 @Test
 public void testSet() throws IOException {
 // Test set
 Mockito.when(obj2.getClass()).thenReturn(String.class);
 Mockito.when(settableBeanProperty.getName()).thenReturn(new PropertyName("name"));
 Mockito.when(jsonDeserializer.deserialize(jsonParser, deserializationContext, String.class)).thenReturn("value");
 Mockito.when(objectIdReferenceProperty.deserialize(jsonParser, deserializationContext, String.class)).thenReturn("value");
 Mockito.when(objectIdReferenceProperty.getName()).thenReturn(new PropertyName("name"));
 Mockito.when(objectIdReferenceProperty.getMember()).thenReturn(Mockito.mock(AnnotatedMember.class));
 Mockito.when(objectIdReferenceProperty.fixAccess(deserializationContext)).thenCallRealMethod();
 Mockito.when(simpleBeanPropertyFilter.filterProperty(Mockito.any(), Mockito.eq(SimpleBeanPropertyFilter.FilterFlag.NON_PRIVATE), Mockito.eq(false))).thenReturn(settableBeanProperty);
 Mockito.when(objectIdReferenceProperty.getMember().getAnnotation(JsonProperty.class)).thenReturn(Mockito.mock(JsonProperty.class));

 objectIdReferenceProperty.deserializeAndSet(jsonParser, deserializationContext, obj);
 assertEquals("value", obj.getClass().getMethod("setName").invoke(obj, "value"));
 }

 @Test
 public void testFixAccess() {
 // Test fixAccess
 SettableBeanProperty settableBeanProperty3 = Mockito.mock(SettableBeanProperty.class);
 Mockito.when(objectIdReferenceProperty._forward).thenReturn(settableBeanProperty3);
 objectIdReferenceProperty.fixAccess(deserializationContext);
 assertNotNull(settableBeanProperty3.getMember());
 }
}