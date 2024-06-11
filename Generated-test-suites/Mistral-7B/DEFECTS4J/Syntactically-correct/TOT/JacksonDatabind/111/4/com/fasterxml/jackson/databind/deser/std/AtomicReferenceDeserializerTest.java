package com.example.jackson.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testDeserializeNull() throws IOException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 TypeFactory.defaultInstance.constructType(AtomicReference.class, Object.class),
 null,
 null,
 null);
 AtomicReference<Object> result = deserializer.deserialize(null, new DeserializationContext(null), null);
 assertEquals(null, result);
 }

 @Test
 public void testDeserializeEmptyObject() throws IOException {
 AtomicReference<Object> emptyAtomicReference = new AtomicReference<>();
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 TypeFactory.defaultInstance.constructType(AtomicReference.class, Object.class),
 null,
 null,
 null);
 String json = "{}";
 AtomicReference<Object> result = deserializer.deserialize(objectMapper.readTree(json), new DeserializationContext(null), null);
 assertEquals(emptyAtomicReference, result);
 }

 @Test
 public void testDeserializeValue() throws IOException {
 AtomicReference<Object> expected = new AtomicReference<>(new Integer(42));
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 TypeFactory.defaultInstance.constructType(AtomicReference.class, Object.class),
 null,
 null,
 null);
 String json = "{\"value\":42}";
 AtomicReference<Object> result = deserializer.deserialize(objectMapper.readTree(json), new DeserializationContext(null), null);
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeTypeDeserializer() throws IOException {
 TypeDeserializer typeDeserializer = new StdTypeDeserializer();
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 TypeFactory.defaultInstance.constructType(AtomicReference.class, String.class),
 null,
 typeDeserializer,
 null);
 String json = "{\"type\":\"java.lang.String\",\"value\":\"Hello World\"}";
 AtomicReference<String> result = (AtomicReference<String>) deserializer.deserialize(objectMapper.readTree(json), new DeserializationContext(null), null);
 assertEquals("Hello World", result.get());
 }

 @Test
 public void testDeserializeValueDeserializer() throws IOException, JsonMappingException {
 JsonDeserializer<String> valueDeserializer = new StringDeserializer();
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 TypeFactory.defaultInstance.constructType(AtomicReference.class, String.class),
 null,
 null,
 valueDeserializer);
 String json = "{\"value\":\"Hello World\"}";
 AtomicReference<String> result = deserializer.deserialize(objectMapper.readTree(json), new DeserializationContext(null), null);
 assertEquals("Hello World", result.get());
 }

 @Test
 public void testUpdateReference() throws IOException {
 AtomicReference<Object> atomicReference = new AtomicReference<>(new Integer(42));
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 TypeFactory.defaultInstance.constructType(AtomicReference.class, Object.class),
 null,
 null,
 null);
 AtomicReference<Object> updatedAtomicReference = deserializer.updateReference(atomicReference, new String("Updated Value"));
 assertEquals("Updated Value", updatedAtomicReference.get());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUpdateReferenceUnsupported() throws IOException {
 AtomicReference<Object> atomicReference = new AtomicReference<>(new Integer(42));
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 TypeFactory.defaultInstance.constructType(AtomicReference.class, Object.class),
 null,
 null,
 null);
 deserializer.updateReference(atomicReference, new Integer(43));
 }

 @Test
 public void testGetNullValue() {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 TypeFactory.defaultInstance.constructType(AtomicReference.class, Object.class),
 null,
 null,
 null);
 AtomicReference<Object> result = deserializer.getNullValue(new DeserializationContext(null));
 assertNull(result);
 }

 @Test
 public void testGetEmptyValue() {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 TypeFactory.defaultInstance.constructType(AtomicReference.class, Object.class),
 null,
 null,
 null);
 AtomicReference<Object> result = deserializer.getEmptyValue(new DeserializationContext(null));
 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testReferenceValue() throws IOException {
 Object contents = new Integer(42);
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 TypeFactory.defaultInstance.constructType(AtomicReference.class, Object.class),
 null,
 null,
 null);
 AtomicReference<Object> result = deserializer.referenceValue(contents);
 assertEquals(contents, result.get());
 }

 @Test
 public void testGetReferenced() {
 AtomicReference<Object> atomicReference = new AtomicReference<>(new Integer(42));
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 TypeFactory.defaultInstance.constructType(AtomicReference.class, Object.class),
 null,
 null,
 null);
 Object result = deserializer.getReferenced(atomicReference);
 assertEquals(atomicReference.get(), result);
 }
}