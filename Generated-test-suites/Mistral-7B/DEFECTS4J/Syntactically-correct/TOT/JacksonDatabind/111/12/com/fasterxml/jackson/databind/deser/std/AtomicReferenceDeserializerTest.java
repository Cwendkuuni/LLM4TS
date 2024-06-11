package com.example.junit4;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer;
import com.fasterxml.jackson.databind.deser.std.ReferenceTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AtomicReferenceDeserializerTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testEmptyAtomicReferenceDeserialization() throws JsonProcessingException, IOException {
 AtomicReference<Object> atomicReference = new AtomicReference<>();
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new SimpleType(AtomicReference.class), null, null, null);
 AtomicReference<Object> deserializedObject = deserializer.deserialize(null, null, null, null, null, null);
 assertEquals(atomicReference, deserializedObject);
 }

 @Test
 public void testNullAtomicReferenceDeserialization() throws JsonProcessingException, IOException {
 AtomicReference<Object> atomicReference = new AtomicReference<>();
 ObjectNode node = objectMapper.createObjectNode();
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new SimpleType(AtomicReference.class), null, null, null);
 AtomicReference<Object> deserializedObject = deserializer.deserialize(node, null, null, null, null, null);
 assertNull(deserializedObject);
 }

 @Test
 public void testValueAtomicReferenceDeserialization() throws JsonProcessingException, IOException {
 AtomicReference<Object> atomicReference = new AtomicReference<>(new Object());
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new SimpleType(AtomicReference.class), null, null, null);
 ObjectNode node = objectMapper.createObjectNode();
 node.put("value", new ObjectMapper().valueToTree(new Object()));
 AtomicReference<Object> deserializedObject = deserializer.deserialize(node, null, null, null, null, null);
 assertEquals(atomicReference.get(), deserializedObject.get());
 }

 @Test
 public void testUpdateAtomicReferenceDeserialization() throws JsonProcessingException, IOException {
 AtomicReference<Object> atomicReference = new AtomicReference<>(new Object());
 AtomicReference<Object> updatedAtomicReference = new AtomicReference<>(new Object());
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new SimpleType(AtomicReference.class), null, null, null);
 ObjectNode node = objectMapper.createObjectNode();
 node.put("value", new ObjectMapper().valueToTree(updatedAtomicReference.get()));
 AtomicReference<Object> deserializedObject = deserializer.deserialize(node, null, null, null, null, null);
 atomicReference.set(updatedAtomicReference.get());
 assertEquals(atomicReference, deserializedObject);
 }

 @Test
 public void testThrowsExceptionWithInvalidType() throws JsonProcessingException, IOException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new SimpleType(new TypeReference<List<String>>(){}.getType()), null, null, null);
 ObjectNode node = objectMapper.createObjectNode();
 try {
 deserializer.deserialize(node, null, null, null, null, null);
 fail();
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testThrowsExceptionWithNullObjectDeserializer() throws JsonProcessingException, IOException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new SimpleType(AtomicReference.class), null, null, null);
 ObjectNode node = objectMapper.createObjectNode();
 try {
 deserializer.deserialize(node, null, null, null, null, new JsonNullDeserializer());
 fail();
 } catch (JsonMappingException e) {
 // Expected
 }
}

 @Test
 public void testThrowsExceptionWithUnknownDeserializer() throws JsonProcessingException, IOException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new SimpleType(AtomicReference.class), null, null, null);
 ObjectNode node = objectMapper.createObjectNode();
 try {
 deserializer.deserialize(node, null, null, null, null, new StdDeserializer.Base());
 fail();
 } catch (JsonMappingException e) {
 // Expected
 }
}
}