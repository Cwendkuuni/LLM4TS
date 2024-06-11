package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.MockValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class AtomicReferenceDeserializerTest {

 private ObjectMapper objectMapper;
 private TypeDeserializer typeDeserializer;
 private ValueInstantiator valueInstantiator;
 private AtomicReferenceDeserializer deserializer;

 @Before
 public void setup() {
 objectMapper = JsonMapperFactory.builder()
 .findValueInstantiator(Object.class).withValueInstance(new Object())
 .build();
 typeDeserializer = objectMapper.getTypeFactory().constructType(new TypeReference<TypeDeserializer>() {});
 valueInstantiator = new MockValueInstantiator();
 deserializer = new AtomicReferenceDeserializer(typeDeserializer.findRawType(AtomicReference.class), valueInstantiator, null, null);
 }

 @Test
 public void testNullValue() throws JsonMappingException, IOException {
 AtomicReference<Object> nullValue = deserializer.getNullValue(null);
 assertNull(nullValue.get());
 }

 @Test
 public void testEmptyValue() throws JsonMappingException, IOException {
 AtomicReference<Object> emptyValue = deserializer.getEmptyValue(null);
 assertNull(emptyValue.get());
 }

 @Test
 public void testReferenceValue() throws JsonMappingException, IOException {
 Object referenceValue = new Object();
 AtomicReference<Object> atomicReference = deserializer.referenceValue(referenceValue);
 assertEquals(referenceValue, atomicReference.get());
 }

 @Test
 public void testGetReferenced() throws JsonMappingException, IOException {
 AtomicReference<Object> atomicReference = new AtomicReference<Object>(new Object());
 Object referencedObject = deserializer.getReferenced(atomicReference);
 assertEquals(atomicReference.get(), referencedObject);
 }

 @Test
 public void testUpdateReference() throws JsonMappingException, IOException {
 AtomicReference<Object> atomicReference = new AtomicReference<Object>(new Object());
 Object newValue = new Object();
 AtomicReference<Object> updatedAtomicReference = deserializer.updateReference(atomicReference, newValue);
 assertEquals(newValue, atomicReference.get());
 assertEquals(newValue, updatedAtomicReference.get());
 }

 @Test
 public void testSupportsUpdate() throws JsonMappingException, IOException {
 assertTrue(deserializer.supportsUpdate(null));
 }

 @Test
 public void testDeserialize() throws JsonMappingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerValueDeserializer(AtomicReference.class, deserializer);
 mapper.registerSerializer(AtomicReference.class, new ToStringSerializer(AtomicReference.class));
 String json = "{\"class\":\"com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializerTest$TestObject\",\"value\":123}";
 TestObject testObject = mapper.readValue(json, TestObject.class);
 AtomicReference<Object> atomicReference = testObject.getAtomicReference();
 Object referencedValue = deserializer.getReferenced(atomicReference);
 assertEquals(123, referencedValue);
 }

 @Test
 public void testDeserializeWithNullValue() throws JsonMappingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerValueDeserializer(AtomicReference.class, deserializer);
 mapper.registerSerializer(AtomicReference.class, new ToStringSerializer(AtomicReference.class));
 String json = "{\"class\":\"com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializerTest$TestObject\",\"value\":null}";
 TestObject testObject = mapper.readValue(json, TestObject.class);
 AtomicReference<Object> atomicReference = testObject.getAtomicReference();
 assertNull(deserializer.getReferenced(atomicReference));
 }

 @Test
 public void testDeserializeWithEmptyValue() throws JsonMappingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerValueDeserializer(AtomicReference.class, deserializer);
 mapper.registerSerializer(AtomicReference.class, new ToStringSerializer(AtomicReference.class));
 String json = "{\"class\":\"com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializerTest$TestObject\",\"value\":{}}";
 TestObject testObject = mapper.readValue(json, TestObject.class);
 AtomicReference<Object> atomicReference = testObject.getAtomicReference();
 assertNull(deserializer.getReferenced(atomicReference));
 }

 static class TestObject {
 @JsonProperty("value")
 private AtomicReference<Object> atomicReference;

 public TestObject() {
 }

 @JsonCreator
 public TestObject(@JsonProperty("atomicReference") AtomicReference<Object> atomicReference) {
 this.atomicReference = atomicReference;
 }

 public AtomicReference<Object> getAtomicReference() {
 return atomicReference;
 }
 }
}