package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ValueInstantiator;

public class AtomicReferenceDeserializerTest {

 private ObjectMapper objectMapper;
 private TypeDeserializer typeDeserializer;
 private ValueInstantiator valueInstantiator;
 private JsonDeserializer<Object> jsonDeserializer;
 private AtomicReferenceDeserializer atomicReferenceDeserializer;

 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 typeDeserializer = mock(TypeDeserializer.class);
 valueInstantiator = mock(ValueInstantiator.class);
 jsonDeserializer = mock(JsonDeserializer.class);
 atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new JavaType(AtomicReference.class, objectMapper.getTypeFactory()), valueInstantiator, typeDeserializer, jsonDeserializer);
 }

 @Test
 public void testGetNullValue() throws JsonMappingException, JsonProcessingException {
 AtomicReference<Object> result = atomicReferenceDeserializer.getNullValue(mock(DeserializationContext.class));
 assertNull(result);
 }

 @Test
 public void testGetEmptyValue() {
 AtomicReference<Object> result = atomicReferenceDeserializer.getEmptyValue(mock(DeserializationContext.class));
 assertSame(new AtomicReference<Object>(), result);
 }

 @Test
 public void testReferenceValue() throws JsonMappingException, JsonProcessingException {
 Object contents = "Test Value";
 AtomicReference<Object> atomicReference = new AtomicReference<Object>(contents);
 AtomicReference<Object> result = atomicReferenceDeserializer.referenceValue(contents);
 assertSame(atomicReference, result);
 }

 @Test
 public void testGetReferenced() throws JsonMappingException, JsonProcessingException {
 AtomicReference<Object> atomicReference = new AtomicReference<Object>("Test Value");
 when(jsonDeserializer.deserialize(mock(DeserializationContext.class), mock(JsonParser.class))).thenReturn("Test Value");
 AtomicReference<Object> result = atomicReferenceDeserializer.getReferenced(atomicReference);
 assertEquals("Test Value", result.get());
 }

 @Test
 public void testUpdateReference() throws JsonMappingException, JsonProcessingException, InterruptedException {
 AtomicReference<Object> atomicReference = new AtomicReference<Object>("Test Value");
 Object newValue = "New Test Value";
 when(jsonDeserializer.deserialize(mock(DeserializationContext.class), mock(JsonParser.class))).thenReturn(newValue);
 AtomicReference<Object> result = atomicReferenceDeserializer.updateReference(atomicReference, newValue);
 assertEquals(newValue, result.get());
 }

 @Test
 public void testSupportsUpdate() throws JsonMappingException {
 assertTrue(atomicReferenceDeserializer.supportsUpdate(mock(DeserializationConfig.class)));
 }
}