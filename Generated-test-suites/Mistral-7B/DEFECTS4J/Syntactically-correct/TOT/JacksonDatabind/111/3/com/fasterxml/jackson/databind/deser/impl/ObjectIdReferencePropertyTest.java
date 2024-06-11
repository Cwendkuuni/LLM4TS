package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.NullValueProvider;
import com.fasterxml.jackson.databind.ObjectIdInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ObjectIdReferencePropertyTest {

 private ObjectIdReferenceProperty property;
 private ObjectMapper objectMapper;
 private ObjectIdInfo objectIdInfo;
 private JsonDeserializer<String> jsonDeserializer;
 private SettableBeanProperty forward;
 private AnnotatedMember annotatedMember;
 private DeserializationConfig config;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 objectMapper = mock(ObjectMapper.class);
 objectIdInfo = mock(ObjectIdInfo.class);
 jsonDeserializer = mock(JsonDeserializer.class);
 forward = mock(SettableBeanProperty.class);
 annotatedMember = mock(AnnotatedMember.class);
 config = mock(DeserializationConfig.class);
 property = new ObjectIdReferenceProperty(forward, objectIdInfo);
 }

 @Test
 public void testPropertyWithName() {
 PropertyName newName = mock(PropertyName.class);
 ObjectIdReferenceProperty newProperty = property.withName(newName);
 assertEquals(property, newProperty.getBackReference());
 assertEquals(newName, newProperty.getName());
 }

 @Test
 public void testPropertyWithValueDeserializer() throws JsonProcessingException {
 ObjectReader reader = mock(ObjectReader.class);
 JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
 nullValueProvider = mock(NullValueProvider.class);
 ObjectIdReferenceProperty newProperty = property.withValueDeserializer(deserializer);
 assertEquals(property, newProperty.getBackReference());
 assertEquals(deserializer, newProperty.getValueDeserializer());
 }

 @Test
 public void testPropertyWithNullProvider() throws JsonProcessingException {
 NullValueProvider nva = mock(NullValueProvider.class);
 ObjectIdReferenceProperty newProperty = property.withNullProvider(nva);
 assertEquals(property, newProperty.getBackReference());
 assertEquals(nva, newProperty.getNullProvider());
 }

 @Test
 public void testFixAccess() {
 property.fixAccess(config);
 // No verification needed as the method does not make any external calls
 }

 @Test
 public void testGetAnnotation() {
 Class<JsonDeserialize> jsonDeserializeAnnotationClass = JsonDeserialize.class;
 JsonDeserialize jsonDeserializeAnnotation = mock(JsonDeserialize.class);
 when(forward.getAnnotation(jsonDeserializeAnnotationClass)).thenReturn(jsonDeserializeAnnotation);
 assertEquals(jsonDeserializeAnnotation, property.getAnnotation(jsonDeserializeAnnotationClass));
 }

 @Test
 public void testGetMember() {
 assertEquals(forward.getMember(), property.getMember());
 }

 @Test
 public void testGetCreatorIndex() {
 assertEquals(forward.getCreatorIndex(), property.getCreatorIndex());
 }

 @Test
 public void testDeserializeAndSet() throws JsonProcessingException {
 String inputJson = "{\"id\": \"ref-1\"}";
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn(inputJson);
 Object instance = new Object();
 when(objectMapper.readValue(parser, Object.class)).thenReturn(inputJson);
 property.deserializeAndSet(parser, config, instance);
 assertEquals("ref-1", property.getValue(instance));
 }

 @Test
 public void testDeserializeSetAndReturn() throws JsonProcessingException {
 String inputJson = "{\"id\": \"ref-1\"}";
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn(inputJson);
 Object instance = new Object();
 when(objectMapper.readValue(parser, Object.class)).thenReturn(inputJson);
 assertEquals(instance, property.deserializeSetAndReturn(parser, config, instance));
 assertEquals("ref-1", property.getValue(instance));
 }

 @Test
 public void testSet() throws JsonProcessingException {
 String value = "ref-1";
 Object instance = new Object();
 property.set(instance, value);
 assertEquals(value, property.getValue(instance));
 }

 @Test
 public void testSetAndReturn() throws JsonProcessingException {
 String value = "ref-1";
 Object instance = new Object();
 assertEquals(instance, property.setAndReturn(instance, value));
 assertEquals(value, property.getValue(instance));
 }

 @Test
 public void testHandleResolvedForwardReference() {
 Object id = new Object();
 Object value = new Object();
 PropertyReferring referencing = new PropertyReferencing(property, mock(UnresolvedForwardReference.class), Object.class, new Object());
 referencing.handleResolvedForwardReference(id, value);
 assertEquals(value, property.getValue(new Object()));
 }

 @Test
 public void testDeserializeWithIdentityInfo() throws JsonProcessingException {
 ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
 JsonDeserializer<String> jsonDeserializer = mock(JsonDeserializer.class);
 when(jsonDeserializer.getObjectIdReader()).thenReturn(objectIdInfo);
 ObjectIdReferenceProperty propertyWithIdentityInfo = new ObjectIdReferenceProperty(property, jsonDeserializer, nullValueProvider);
 ObjectParser parser = mock(ObjectParser.class);
 when(parser.readValueAsId(JsonNode.class)).thenReturn(objectIdInfo);
 Object instance = new Object();
 when(objectMapper.treeAsTokens(parser.readValueAs(JsonNode.class))).thenReturn(mock(JsonParser.class));
 String id = "ref-1";
 when(parser.getValueAsString()).thenReturn(id);
 propertyWithIdentityInfo.deserializeAndSet(parser, config, instance);
 assertEquals(id, propertyWithIdentityInfo.getValue(instance));
 }

 @Test
 public void testDeserializeWithNullValue() throws JsonProcessingException {
 ObjectIdReferenceProperty propertyWithNullValue = new ObjectIdReferenceProperty(property, jsonDeserializer, nullValueProvider);
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn(null);
 Object instance = new Object();
 propertyWithNullValue.deserializeAndSet(parser, config, instance);
 assertNull(propertyWithNullValue.getValue(instance));
 }
}