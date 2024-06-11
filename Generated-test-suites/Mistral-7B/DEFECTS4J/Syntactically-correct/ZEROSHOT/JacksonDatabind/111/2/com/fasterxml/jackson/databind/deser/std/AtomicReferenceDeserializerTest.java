package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class AtomicReferenceDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testGetNullValue() throws JsonMappingException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>() {}, null, null, null);
 DeserializationContext context = mock(DeserializationContext.class);
 AtomicReference<Object> result = deserializer.getNullValue(context);
 assertNull(result);
 }

 @Test
 public void testGetEmptyValue() {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>() {}, null, null, null);
 AtomicReference<Object> result = deserializer.getEmptyValue(null);
 assertNotNull(result);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testReferenceValue() throws JsonMappingException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>() {}, null, null, null);
 Object value = new Object();
 AtomicReference<Object> result = deserializer.referenceValue(value);
 assertEquals(value, result.get());
 }

 @Test
 public void testGetReferenced() throws JsonMappingException {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>() {}, null, null, null);
 Object result = deserializer.getReferenced(reference);
 assertEquals(reference.get(), result);
 }

 @Test
 public void testUpdateReference() throws JsonMappingException {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>() {}, null, null, null);
 Object newValue = new Object();
 AtomicReference<Object> updatedReference = deserializer.updateReference(reference, newValue);
 assertEquals(newValue, updatedReference.get());
 assertEquals(reference, updatedReference);
 }

 @Test
 public void testSupportsUpdate() throws JsonMappingException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>() {}, null, null, null);
 assertTrue(deserializer.supportsUpdate(null));
 }
}