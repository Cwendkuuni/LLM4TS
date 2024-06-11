package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.NullValueProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdReferencePropertyTest {

 private ObjectMapper objectMapper;
 private ObjectIdInfo objectIdInfo;
 private PropertyName propertyName;
 private SettableBeanProperty forward;
 private ObjectIdReferenceProperty objectIdReferenceProperty;
 private JsonDeserializer<Object> jsonDeserializer;
 private NullValueProvider nullValueProvider;
 private DeserializationConfig config;
 private BeanDescription beanDescription;

 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 objectIdInfo = mock(ObjectIdInfo.class);
 propertyName = mock(PropertyName.class);
 forward = mock(SettableBeanProperty.class);
 jsonDeserializer = mock(JsonDeserializer.class);
 nullValueProvider = mock(NullValueProvider.class);
 config = mock(DeserializationConfig.class);
 beanDescription = mock(BeanDescription.class);
 when(objectMapper.findBeanDescription(ClassUtil.getRawClass(Object.class))).thenReturn(beanDescription);
 }

 @Test
 public void testConstructorWithForwardAndObjectIdInfo() throws Exception {
 objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
 assertSame(forward, objectIdReferenceProperty._forward);
 assertSame(objectIdInfo, objectIdReferenceProperty._objectIdInfo);
 }

 @Test
 public void testConstructorWithSrcAndDeser() throws Exception {
 objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, jsonDeserializer, nullValueProvider);
 assertSame(forward, objectIdReferenceProperty._forward);
 assertSame(jsonDeserializer, objectIdReferenceProperty._valueDeserializer);
 assertSame(nullValueProvider, objectIdReferenceProperty._nullProvider);
 }

 @Test
 public void testConstructorWithSrcAndNewName() throws Exception {
 objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, new PropertyName("test"));
 assertSame(forward, objectIdReferenceProperty);
 assertSame(new PropertyName("test"), objectIdReferenceProperty.getName());
 }

 @Test
 public void testWithName() throws Exception {
 PropertyName newName = new PropertyName("test");
 ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withName(newName);
 assertSame(objectIdReferenceProperty, newObjectIdReferenceProperty);
 assertSame(newPropertyName, newObjectIdReferenceProperty.getName());
 }

 @Test
 public void testWithValueDeserializer() throws Exception {
 JsonDeserializer<Object> newJsonDeserializer = mock(JsonDeserializer.class);
 ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withValueDeserializer(newJsonDeserializer);
 assertSame(objectIdReferenceProperty, newObjectIdReferenceProperty);
 assertSame(newJsonDeserializer, newObjectIdReferenceProperty._valueDeserializer);
 }

 @Test
 public void testWithNullProvider() throws Exception {
 NullValueProvider newNullValueProvider = mock(NullValueProvider.class);
 ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withNullProvider(newNullValueProvider);
 assertSame(objectIdReferenceProperty, newObjectIdReferenceProperty);
 assertSame(newNullValueProvider, newObjectIdReferenceProperty._nullProvider);
 }

 @Test
 public void testFixAccess() throws Exception {
 objectIdReferenceProperty.fixAccess(config);
 assertSame(forward, objectIdReferenceProperty._forward);
 }

 @Test
 public void testGetAnnotation() throws Exception {
 Annotation annotation = mock(Annotation.class);
 when(forward.getAnnotation(Annotation.class)).thenReturn(annotation);
 assertSame(annotation, objectIdReferenceProperty.getAnnotation(Annotation.class));
 }

 @Test
 public void testGetMember() throws Exception {
 AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
 when(forward.getMember()).thenReturn(annotatedMember);
 assertSame(annotatedMember, objectIdReferenceProperty.getMember());
 }

 @Test
 public void testGetCreatorIndex() throws Exception {
 int creatorIndex = 0;
 when(forward.getCreatorIndex()).thenReturn(creatorIndex);
 assertSame(creatorIndex, objectIdReferenceProperty.getCreatorIndex());
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 Object instance = new Object();
 Object value = new Object();
 when(jsonDeserializer.deserialize(mock(JsonParser.class), config, instance)).thenReturn(value);
 objectIdReferenceProperty.deserializeAndSet(mock(JsonParser.class), config, instance);
 assertSame(value, objectIdReferenceProperty.getMember().get(instance));
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 Object instance = new Object();
 Object value = new Object();
 when(jsonDeserializer.deserialize(mock(JsonParser.class), config, instance)).thenReturn(value);
 when(forward.setAndReturn(instance, value)).thenReturn(value);
 Object result = objectIdReferenceProperty.deserializeSetAndReturn(mock(JsonParser.class), config, instance);
 assertSame(value, result);
 }

 @Test
 public void testSet() throws Exception {
 Object instance = new Object();
 Object value = new Object();
 objectIdReferenceProperty.set(instance, value);
 assertSame(value, objectIdReferenceProperty.getMember().get(instance));
 }

 @Test
 public void testSetAndReturn() throws Exception {
 Object instance = new Object();
 Object value = new Object();
 Object result = objectIdReferenceProperty.setAndReturn(instance, value);
 assertSame(value, result);
 }

 @Test
 public void testPropertyReferring() throws Exception {
 UnresolvedForwardReference ref = mock(UnresolvedForwardReference.class);
 Class<?> type = Object.class;
 Object ob = new Object();
 PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, ref, type, ob);
 assertSame(objectIdReferenceProperty, propertyReferring._parent);
 assertSame(ob, propertyReferring._pojo);
 }

 @Test
 public void testDeserializeAndSetWithUnresolvedForwardReference() throws Exception {
 Object instance = new Object();
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, jsonDeserializer, nullValueProvider);
 when(jsonDeserializer.deserialize(mock(JsonParser.class), config, instance)).thenThrow(new UnresolvedForwardReference());
 Object result = objectIdReferenceProperty.deserializeAndSet(mock(JsonParser.class), config, instance);
 assertNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeAndSetWithError() throws Exception {
 Object instance = new Object();
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, jsonDeserializer, nullValueProvider);
 when(jsonDeserializer.deserialize(mock(JsonParser.class), config, instance)).thenThrow(new IOException("Test error"));
 objectIdReferenceProperty.deserializeAndSet(mock(JsonParser.class), config, instance);
 }

 @Test
 public void testHandleResolvedForwardReference() throws Exception {
 Object id = new Object();
 Object value = new Object();
 ObjectIdReferenceProperty.PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, mock(UnresolvedForwardReference.class), ClassUtil.getRawClass(Object.class), new Object());
 propertyReferring.handleResolvedForwardReference(id, value);
 assertSame(value, objectIdReferenceProperty.getMember().get(new Object()));
 }
}