package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

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

public class ObjectIdReferencePropertyTest {

 private ObjectIdReferenceProperty property;
 private ObjectIdInfo objectIdInfo;
 private SettableBeanProperty forward;
 private JsonDeserializer<Object> deserializer;
 private NullValueProvider nullProvider;
 private Object id;

 @Before
 public void setup() throws Exception {
 objectIdInfo = new ObjectIdInfo(JsonType.constructFromInt(1), "name");
 forward = new SettableBeanProperty(null, null, null);
 property = new ObjectIdReferenceProperty(forward, objectIdInfo);
 deserializer = new JsonDeserializer<Object>() {};
 nullProvider = new NullValueProviderDefaultImpl();
 }

 @Test
 public void testConstructorWithForwardAndObjectIdInfo() {
 assertNotNull(property);
 assertEquals(forward, property._forward);
 assertEquals(objectIdInfo, property._objectIdInfo);
 }

 @Test
 public void testConstructorWithSrcDeserializerAndNullValueProvider() {
 SettableBeanProperty src = new SettableBeanProperty(null, null, null);
 ObjectIdReferenceProperty newProperty = new ObjectIdReferenceProperty(src, deserializer, nullProvider);
 assertEquals(src, newProperty);
 assertEquals(deserializer, newProperty._valueDeserializer);
 assertEquals(nullProvider, newProperty._nullProvider);
 }

 @Test
 public void testConstructorWithSrcAndNewName() {
 PropertyName newName = PropertyName.construct("newName");
 ObjectIdReferenceProperty newProperty = new ObjectIdReferenceProperty(property, newName);
 assertEquals(property, newProperty);
 assertEquals(newName, newProperty.getName());
 }

 @Test
 public void testGetAnnotation() throws Exception {
 Annotation annotation = new Annotation() {};
 AnnotatedMember member = new AnnotatedMember() {
 @Override
 public Annotation getAnnotation(Class<?> type) {
 return annotation;
 }
 };
 assertEquals(annotation, property.getAnnotation(Annotation.class));
 assertEquals(member, property.getMember());
 }

 @Test
 public void testGetCreatorIndex() {
 assertEquals(-1, property.getCreatorIndex());
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
 node.put("name", "value");
 JsonParser parser = ObjectMapper.getDefaultInstance().treeAsTokens(node);
 Object instance = new Object();
 property.deserializeAndSet(parser, null, instance);
 assertEquals("value", property.get(instance));
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
 node.put("name", "value");
 JsonParser parser = ObjectMapper.getDefaultInstance().treeAsTokens(node);
 Object instance = new Object();
 Object result = property.deserializeSetAndReturn(parser, null, instance);
 assertNull(result);
 assertEquals("value", property.get(instance));
 }

 @Test
 public void testSet() throws Exception {
 Object instance = new Object();
 property.set(instance, "value");
 assertEquals("value", property.get(instance));
 }

 @Test
 public void testSetAndReturn() throws Exception {
 Object instance = new Object();
 Object result = property.setAndReturn(instance, "value");
 assertEquals("value", property.get(instance));
 assertNull(result);
 }

 @Test
 public void testDeserializeWithUnresolvedForwardReference() throws Exception {
 UnresolvedForwardReference reference = new UnresolvedForwardReference();
 property.deserializeSetAndReturn(new JsonParser() {}, null, null);
 assertEquals(reference, property._objectIdInfo.getReferencedBy().get(0));
 }

 @Test
 public void testResolveForwardReference() throws Exception {
 UnresolvedForwardReference reference = new UnresolvedForwardReference();
 Object idRef = new Object();
 Object value = new Object();
 property.deserializeSetAndReturn(new JsonParser() {}, null, idRef);
 property.set(idRef, value);
 property.handleResolvedForwardReference(id, idRef);
 assertEquals(value, property.get(new Object()));
 }
}