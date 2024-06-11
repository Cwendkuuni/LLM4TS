package com.example.junittest;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;

public class AtomicReferenceDeserializerTest {

 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() throws JsonProcessingException {
 mapper = new ObjectMapper();
 }

 @Test
 public void testDeserializeNullAtomicReference() throws JsonProcessingException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 AtomicReference<Object> result = deserializer.deserialize(mapper.getFactory().getNullValue(), new DeserializationContext(mapper, new StandardDeserializationContext()));
 assertEquals(null, result);
 }

 @Test
 public void testDeserializeEmptyAtomicReference() throws JsonProcessingException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 ObjectNode jsonNode = mapper.createObjectNode();
 jsonNode.put("empty", new Object());
 AtomicReference<Object> result = deserializer.deserialize(jsonNode, new DeserializationContext(mapper, new StandardDeserializationContext()));
 assertNull(result);
 }

 @Test
 public void testDeserializeAtomicReferenceWithContent() throws JsonProcessingException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 ObjectNode jsonNode = mapper.createObjectNode();
 jsonNode.put("value", new Object());
 AtomicReference<Object> result = deserializer.deserialize(jsonNode, new DeserializationContext(mapper, new StandardDeserializationContext()));
 assertEquals(new Object(), result.get());
 }

 @Test(expected = MismatchedNodeTypeException.class)
 public void testDeserializeInvalidType() throws JsonProcessingException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 ObjectNode jsonNode = mapper.createObjectNode();
 jsonNode.put("value", mapper.valueToTree(42));
 deserializer.deserialize(jsonNode, new DeserializationContext(mapper, new StandardDeserializationContext()));
 }

 @Test
 public void testUpdateAtomicReference() throws JsonProcessingException {
 AtomicReference<Object> atomicReference = new AtomicReference<Object>();
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 Object newValue = new Object();
 AtomicReference<Object> result = deserializer.updateReference(atomicReference, newValue);
 assertEquals(newValue, result.get());
 }
}