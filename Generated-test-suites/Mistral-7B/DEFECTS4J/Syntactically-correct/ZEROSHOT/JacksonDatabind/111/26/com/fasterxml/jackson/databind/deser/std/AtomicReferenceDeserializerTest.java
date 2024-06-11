package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.MockValueInstantiator;
import com.fasterxml.jackson.databind.deser.MockTypeDeserializer;
import com.fasterxml.jackson.databind.deser.MockValueDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AtomicReferenceDeserializerTest {

 private ObjectMapper mapper;
 private AtomicReferenceDeserializer deserializer;
 private JavaType type;
 private TypeDeserializer typeDeserializer;
 private ValueInstantiator valueInstantiator;
 private ValueDeserializer valueDeserializer;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 type = mapper.getTypeFactory().constructType(new TypeReference<AtomicReference<String>>() {});
 deserializer = new AtomicReferenceDeserializer(type, new MockValueInstantiator(), typeDeserializer, valueDeserializer);
 typeDeserializer = new MockTypeDeserializer();
 valueInstantiator = new MockValueInstantiator();
 valueDeserializer = new MockValueDeserializer();
 }

 @Test
 public void testGetNullValue() throws JsonMappingException, IOException {
 AtomicReference<String> nullValue = deserializer.getNullValue(mapper.getDeserializationContext());
 assertNull(nullValue.get());
 }

 @Test
 public void testGetEmptyValue() {
 AtomicReference<String> emptyValue = deserializer.getEmptyValue(mapper.getDeserializationContext());
 assertNotNull(emptyValue);
 assertNull(emptyValue.get());
 }

 @Test
 public void testReferenceValue() throws JsonMappingException, IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("value", "Hello");
 AtomicReference<String> reference = mapper.readValue(node, AtomicReference.class);
 Object contents = mapper.readTree(node).get("value").asText();
 AtomicReference<String> newReference = deserializer.referenceValue(contents);
 assertEquals(reference, newReference);
 assertEquals(contents, newReference.get());
 }

 @Test
 public void testGetReferenced() throws JsonMappingException, IOException {
 AtomicReference<String> reference = new AtomicReference<String>("Hello");
 Object value = deserializer.getReferenced(reference);
 assertEquals(reference.get(), value);
 }

 @Test
 public void testUpdateReference() throws JsonMappingException, IOException {
 AtomicReference<String> reference = new AtomicReference<String>("Hello");
 String newValue = "World";
 AtomicReference<String> updatedReference = deserializer.updateReference(reference, newValue);
 assertEquals(updatedReference, reference);
 assertEquals(newValue, updatedReference.get());
 }

 @Test
 public void testSupportsUpdate() throws JsonMappingException, IOException {
 assertTrue(deserializer.supportsUpdate(mapper.getDeserializationConfig()));
 }

 @Test
 public void testWithResolved() {
 AtomicReferenceDeserializer newDeserializer = deserializer.withResolved(typeDeserializer, valueDeserializer);
 assertEquals(deserializer, newDeserializer);
 }
}