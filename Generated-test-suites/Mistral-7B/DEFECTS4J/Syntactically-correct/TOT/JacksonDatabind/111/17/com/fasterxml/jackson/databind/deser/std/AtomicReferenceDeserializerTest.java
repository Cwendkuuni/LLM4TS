package com.example;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import org.jmock.lib.legacy.ClassImposterizer;
import org.jmock.lib.legacy.MockObjectTestCase;
import org.jmock.lib.legacy.TestValues;
import org.jmock.lib.legacy.Threading;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.None;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class AtomicReferenceDeserializerTest extends MockObjectTestCase {

 private AtomicReferenceDeserializer deserializer;
 private DeserializationContext ctxt;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 ctxt = mock(DeserializationContext.class);
 deserializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>(){},
 mock(ValueInstantiator.class),
 mock(TypeDeserializer.class),
 null);
 }

 @Test
 public void testGetNullValue() throws JsonMappingException {
 AtomicReference<Object> result = deserializer.getNullValue(ctxt);
 assertNull(result.get());
 }

 @Test
 public void testGetEmptyValue() {
 AtomicReference<Object> result = deserializer.getEmptyValue(ctxt);
 assertNull(result.get());
 }

 @Test
 public void testReferenceValue() throws JsonMappingException {
 Object contents = new Object();
 AtomicReference<Object> result = deserializer.referenceValue(contents);
 assertEquals(contents, result.get());
 }

 @Test
 public void testGetReferenced() throws JsonMappingException {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object referenced = deserializer.getReferenced(reference);
 assertEquals(referenced, reference.get());
 }

 @Test
 public void testUpdateReference() throws JsonMappingException {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object newValue = new Object();
 AtomicReference<Object> updatedReference = deserializer.updateReference(reference, newValue);
 assertEquals(newValue, updatedReference.get());
 }

 @Test
 public void testSupportsUpdate() {
 assertTrue(deserializer.supportsUpdate(null));
 }

 @Test(expected = IllegalStateException.class)
 public void testDeserializeInvalidJson() throws JsonMappingException {
 String json = "{\"notAnAtomicReferenceDeserializer\": \"invalid_json\"}";
 TypeDeserializer deser = deserializer; // assign the wrong deserializer type
 Object readValue = mapper.readValue(json, new TypeReference<Object>() {});
 }

 @Test
 public void testDeserializeWithValueDeserializer() throws JsonMappingException {
 // Prepare mock ValueDeserializer
 JsonDeserializer<?> valueDeser = mock(JsonDeserializer.class);
 TypeDeserializer typeDeser = new None(); // no type deserializer needed
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>(){},
 valueDeser, typeDeser, null);

 String json = "{\"_class\": \"com.example.MyClass\", \"value\": 42}";
 Object readValue = mapper.readValue(json, new TypeReference<Object>() {});
 AtomicReference<Object> result = (AtomicReference<Object>) readValue;
 assertEquals(42, result.get());
 }

 @Test
 public void testDeserializeWithNullValue() throws JsonMappingException {
 String json = "{\"_class\": \"com.example.AtomicReferenceDeserializer$AtomicReferenceDeserializer\", \"value\": null}";
 Object readValue = mapper.readValue(json, new TypeReference<Object>() {});
 AtomicReference<Object> result = (AtomicReference<Object>) readValue;
 assertNull(result.get());
 }

 @Test
 public void testDeserializeWithEmptyValue() throws JsonMappingException {
 String json = "{\"_class\": \"com.example.AtomicReferenceDeserializer$AtomicReferenceDeserializer\", \"value\": {}}";
 Object readValue = mapper.readValue(json, new TypeReference<Object>() {});
 AtomicReference<Object> result = (AtomicReference<Object>) readValue;
 assertNull(result.get());
 }

 @Test
 public void testDeserializeWithValue() throws JsonMappingException {
 // Prepare mock ValueDeserializer
 JsonDeserializer<?> valueDeser = mock(JsonDeserializer.class);
 TypeDeserializer typeDeser = new None(); // no type deserializer needed
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>(){},
 valueDeser, typeDeser, null);

 String json = "{\"_class\": \"com.example.AtomicReferenceDeserializer$AtomicReferenceDeserializer\", \"value\": { \"_class\": \"com.example.MyClass\", \"id\": 1, \"name\": \"Test\" } }";
 ObjectMapper mapper = new ObjectMapper();
 ObjectMapper mapperWithoutAtomic = new ObjectMapper(); // Test without AtomicReferenceDeserializer
 ObjectMapper mapperWithAtomic = new ObjectMapper(); // Test with AtomicReferenceDeserializer
 Object readValueWithAtomic = mapperWithAtomic.readValue(json, new TypeReference<Object>() {});
 Object readValueWithoutAtomic = mapperWithoutAtomic.readValue(json, new TypeReference<Object>() {});
 AtomicReference<Object> resultWithAtomic = (AtomicReference<Object>) readValueWithAtomic;
 Object resultWithoutAtomic = readValueWithoutAtomic; // Assume serialization is successful

 assertEquals(resultWithAtomic.get(), resultWithoutAtomic);
 }

 @Test
 public void testDeserializeWithEmptyJson() throws JsonMappingException {
 String json = "{\"_class\": \"com.example.AtomicReferenceDeserializer$AtomicReferenceDeserializer\"}";
 ObjectMapper mapper = new ObjectMapper();
 Object readValue = mapper.readValue(json, new TypeReference<Object>() {});
 AtomicReference<Object> result = (AtomicReference<Object>) readValue;
 assertNull(result.get());
 }
}