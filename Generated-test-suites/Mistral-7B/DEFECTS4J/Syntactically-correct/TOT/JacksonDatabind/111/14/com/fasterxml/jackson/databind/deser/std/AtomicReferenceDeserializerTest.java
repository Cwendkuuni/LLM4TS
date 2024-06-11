package com.example.junittests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class AtomicReferenceDeserializerTest {

 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testGetNullValue() throws JsonProcessingException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 TypeFactory.defaultInstance.constructType(AtomicReference.class, Object.class),
 null, null, null);
 AtomicReference<Object> result = deserializer.getNullValue(null);
 assertEquals(null, result);
 }

 @Test
 public void testGetEmptyValue() {
 AtomicReference<Object> emptyReference = new AtomicReference<Object>();
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 TypeFactory.defaultInstance.constructType(AtomicReference.class, Object.class),
 null, null, null);
 Object result = deserializer.getEmptyValue(null);
 assertEquals(emptyReference, result);
 }

 @Test
 public void testReferenceValue() throws JsonProcessingException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 TypeFactory.defaultInstance.constructType(AtomicReference.class, Object.class),
 null, null, null);
 Object initialValue = new Object();
 AtomicReference<Object> expectedResult = new AtomicReference<Object>(initialValue);
 String jsonInput = mapper.writeValueAsString(expectedResult);
 AtomicReference<Object> result = mapper.readValue(jsonInput, deserializer.getClass());
 assertEquals(expectedResult, result);
 }

 @Test
 public void testGetReferenced() throws JsonProcessingException {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 TypeFactory.defaultInstance.constructType(AtomicReference.class, Object.class),
 null, null, null);
 Object result = deserializer.getReferenced(reference);
 assertEquals(reference.get(), result);
 }

 @Test
 public void testUpdateReference() throws IOException, JsonProcessingException {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object newValue = new Object();
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 TypeFactory.defaultInstance.constructType(AtomicReference.class, Object.class),
 null, null, null);
 deserializer.updateReference(reference, newValue);
 assertEquals(newValue, reference.get());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSupportsUpdateWithUnsupportedConfig() {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 TypeFactory.defaultInstance.constructType(AtomicReference.class, Object.class),
 null, null, null);
 DeserializationConfig config = new DeserializationConfig();
 Boolean result = deserializer.supportsUpdate(config);
 fail("Expected UnsupportedOperationException but got: " + result);
 }

 @Test
 public void testSupportsUpdateWithSupportedConfig() {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 TypeFactory.defaultInstance.constructType(AtomicReference.class, Object.class),
 null, null, null);
 DeserializationConfig config = new DeserializationConfig();
 config.setSerializationInclusion(JsonInclude.Value.ALL);
 Boolean result = deserializer.supportsUpdate(config);
 assertTrue(result.booleanValue());
 }
}