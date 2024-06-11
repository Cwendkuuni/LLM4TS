package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;

public class ObjectIdReferencePropertyTest {

 private ObjectIdReferenceProperty createProperty(ObjectIdInfo objectIdInfo) {
 return new ObjectIdReferenceProperty(null, objectIdInfo);
 }

 @Test
 public void testConstructorWithObjectIdInfo() {
 ObjectIdReferenceProperty property = createProperty(new ObjectIdInfo(int.class, "id"));
 assertNotNull(property);
 assertNull(property._forward);
 assertSame(ObjectIdInfo.class, property._objectIdInfo.getClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullObjectIdInfo() {
 createProperty(null);
 }

 @Test
 public void testCopyConstructor() {
 ObjectIdReferenceProperty src = createProperty(new ObjectIdInfo(int.class, "id"));
 ObjectIdReferenceProperty copy = new ObjectIdReferenceProperty(src, null, null);
 assertSame(src, copy._forward);
 assertSame(src._objectIdInfo, copy._objectIdInfo);
 }

 @Test
 public void testCopyConstructorWithName() {
 ObjectIdReferenceProperty src = createProperty(new ObjectIdInfo(int.class, "id"));
 PropertyName newName = PropertyName.construct("newName");
 ObjectIdReferenceProperty copy = new ObjectIdReferenceProperty(src, newName);
 assertSame(src, copy._forward);
 assertSame(src._objectIdInfo, copy._objectIdInfo);
 assertSame(newName, copy._name);
 }

 @Test
 public void testCopyConstructorWithValueDeserializer() {
 JsonDeserializer<Integer> deserializer = new StandardDeserializer<Integer>(Integer.class);
 ObjectIdReferenceProperty src = createProperty(new ObjectIdInfo(Integer.class, "id"));
 ObjectIdReferenceProperty copy = new ObjectIdReferenceProperty(src, deserializer, null);
 assertSame(src, copy._forward);
 assertSame(src._objectIdInfo, copy._objectIdInfo);
 assertSame(deserializer, copy._valueDeserializer);
 }

 @Test
 public void testCopyConstructorWithNullValueProvider() {
 ObjectIdReferenceProperty src = createProperty(new ObjectIdInfo(Integer.class, "id"));
 NullValueProvider nva = new StandardSerializableNullValueProvider();
 ObjectIdReferenceProperty copy = new ObjectIdReferenceProperty(src, null, nva);
 assertSame(src, copy._forward);
 assertSame(src._objectIdInfo, copy._objectIdInfo);
 assertSame(nva, copy._nullProvider);
 }

 @Test
 public void testGetObjectIdInfo() {
 ObjectIdReferenceProperty property = createProperty(new ObjectIdInfo(int.class, "id"));
 assertSame(property._objectIdInfo, property.getObjectIdInfo());
 }

 @Test
 public void testFixAccess() throws Exception {
 ObjectIdReferenceProperty property = createProperty(new ObjectIdInfo(int.class, "id"));
 Object testInstance = new Object();
 property.fixAccess(new DeserializationConfig());
 property.deserializeAndSet(mockParser(), mockContext(), testInstance);
 assertNotNull(testInstance.getClass().getDeclaredField("id"));
 assertTrue(testInstance.getClass().getDeclaredField("id").isAccessible());
 }

 @Test
 public void testGetAnnotation() throws Exception {
 ObjectIdReferenceProperty property = createProperty(new ObjectIdInfo(int.class, "id"));
 Annotation annotation = new JacksonAnnotation();
 property.setAnnotation(annotation.getClass(), annotation);
 assertSame(annotation, property.getAnnotation(annotation.getClass()));
 }

 @Test
 public void testGetMember() throws Exception {
 ObjectIdReferenceProperty property = createProperty(new ObjectIdInfo(int.class, "id"));
 AnnotatedMember member = mock(AnnotatedMember.class);
 when(member.getName()).thenReturn("id");
 when(member.getAnnotated()).thenReturn(new Object());
 property.setMember(member);
 assertSame(member, property.getMember());
 }

 @Test
 public void testGetCreatorIndex() throws Exception {
 ObjectIdReferenceProperty property = createProperty(new ObjectIdInfo(int.class, "id"));
 when(property.getMember().getIndexIn()).thenReturn(1);
 int index = property.getCreatorIndex();
 assertEquals(1, index);
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 ObjectIdReferenceProperty property = createProperty(new ObjectIdInfo(Integer.class, "id"));
 Object testInstance = new Object();
 JsonParser mockParser = mockParser();
 DeserializationContext mockContext = mockContext();
 property.deserializeAndSet(mockParser, mockContext, testInstance);
 String idValue = "123";
 ObjectNode node = new ObjectMapper().readValue(idValue, new TypeReference<ObjectNode>() {});
 when(mockParser.getValueAsTree()).thenReturn(node);
 assertEquals("123", testInstance.getId());
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 ObjectIdReferenceProperty property = createProperty(new ObjectIdInfo(Integer.class, "id"));
 Object testInstance = new Object();
 JsonParser mockParser = mockParser();
 DeserializationContext mockContext = mockContext();
 Object result = property.deserializeSetAndReturn(mockParser, mockContext, testInstance);
 String idValue = "123";
 ObjectNode node = new ObjectMapper().readValue(idValue, new TypeReference<ObjectNode>() {});
 when(mockParser.getValueAsTree()).thenReturn(node);
 assertNull(result);
 assertEquals("123", testInstance.getId());
 }

 @Test
 public void testSet() throws Exception {
 ObjectIdReferenceProperty property = createProperty(new ObjectIdInfo(Integer.class, "id"));
 Object testInstance = new Object();
 int idValue = 123;
 property.set(testInstance, idValue);
 assertEquals(idValue, testInstance.getId());
 }

 @Test
 public void testSetAndReturn() throws Exception {
 ObjectIdReferenceProperty property = createProperty(new ObjectIdInfo(Integer.class, "id"));
 Object testInstance = new Object();
 int idValue = 123;
 Object result = property.setAndReturn(testInstance, idValue);
 assertEquals(idValue, testInstance.getId());
 assertNull(result);
 }

 @Test
 public void testCreatePropertyWithForwardProperty() throws Exception {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(Integer.class, "id");
 SettableBeanProperty forwardProperty = mock(SettableBeanProperty.class);
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forwardProperty, objectIdInfo);
 assertSame(forwardProperty, property._forward);
 assertSame(objectIdInfo, property._objectIdInfo);
 }

 @Test
 public void testCreatePropertyWithUnresolvedForwardReference() throws Exception {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(Integer.class, "id");
 UnresolvedForwardReference forwardReference = mock(UnresolvedForwardReference.class);
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, objectIdInfo, forwardReference);
 assertNull(property._forward);
 assertSame(objectIdInfo, property._objectIdInfo);
 assertSame(forwardReference, property._unresolvedForwardReference);
 }

}