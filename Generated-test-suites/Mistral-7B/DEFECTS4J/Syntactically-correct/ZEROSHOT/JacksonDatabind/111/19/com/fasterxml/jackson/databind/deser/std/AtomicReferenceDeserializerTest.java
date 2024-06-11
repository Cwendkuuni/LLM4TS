package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyValueProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer.StdSerializerProvider;

public class AtomicReferenceDeserializerTest {

 private static final ObjectMapper mapper = JsonMapperFactory.getMapper();

 @Test
 public void testAtomicReferenceDeserializer() throws JsonMappingException, IOException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new JavaType(AtomicReference.class), null, null, null);
 DeserializationContext context = mapper.getDeserializationContext();

 // Test constructor
 AtomicReference<Object> expected = new AtomicReference<Object>("expected");
 AtomicReference<Object> actual = deserializer.referenceValue(expected);
 assertEquals(expected, actual);

 // Test getNullValue
 AtomicReference<Object> nullValue = deserializer.getNullValue(context);
 assertNull(nullValue.get());

 // Test getEmptyValue
 AtomicReference<Object> emptyValue = deserializer.getEmptyValue(context);
 assertNull(emptyValue.get());

 // Test getReferenced
 AtomicReference<Object> reference = new AtomicReference<Object>("reference");
 Object referenced = deserializer.getReferenced(reference);
 assertEquals(reference.get(), referenced);

 // Test updateReference
 AtomicReference<Object> updatedReference = new AtomicReference<Object>("updated");
 deserializer.updateReference(reference, updatedReference);
 assertEquals(updatedReference.get(), reference.get());

 // Test supportsUpdate
 assertTrue(deserializer.supportsUpdate(context.getConfig()));
 }

 @Test
 public void testAtomicReferenceDeserializerWithTypeDeserializerAndValueDeserializer()
 throws JsonMappingException, IOException {
 TypeDeserializer typeDeserializer = new StdTypeDeserializer();
 JsonDeserializer<Object> valueDeserializer = new SimpleObjectDeserializer();
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new JavaType(AtomicReference.class), null, typeDeserializer, valueDeserializer);
 DeserializationContext context = mapper.getDeserializationContext();
 context.setTypeDeserializer(typeDeserializer);
 context.findValueDeserializer(Object.class, emptyValueProviderInstance);

 // Test constructor
 AtomicReference<Object> expected = new AtomicReference<Object>("expected");
 AtomicReference<Object> actual = deserializer.referenceValue(expected);
 assertEquals(expected, actual);

 // Test getNullValue
 AtomicReference<Object> nullValue = deserializer.getNullValue(context);
 assertNull(nullValue.get());

 // Test getEmptyValue
 AtomicReference<Object> emptyValue = deserializer.getEmptyValue(context);
 assertNull(emptyValue.get());

 // Test getReferenced
 AtomicReference<Object> reference = new AtomicReference<Object>("reference");
 Object referenced = deserializer.getReferenced(reference);
 assertEquals(reference.get(), referenced);

 // Test updateReference
 AtomicReference<Object> updatedReference = new AtomicReference<Object>("updated");
 deserializer.updateReference(reference, updatedReference);
 assertEquals(updatedReference.get(), reference.get());

 // Test supportsUpdate
 assertTrue(deserializer.supportsUpdate(context.getConfig()));
 }

 @Test
 public void testAtomicReferenceDeserializerWithNullValueDeserializer()
 throws JsonMappingException, IOException {
 JsonDeserializer<Object> nullValueDeserializer = new StringDeserializer();
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new JavaType(AtomicReference.class), null, null, nullValueDeserializer);
 DeserializationContext context = mapper.getDeserializationContext();
 context.setNullValueDeserializer(nullValueDeserializer);

 // Test constructor
 AtomicReference<Object> expected = new AtomicReference<Object>("expected");
 AtomicReference<Object> actual = deserializer.referenceValue(expected);
 assertEquals(expected, actual);

 // Test getNullValue
 AtomicReference<Object> nullValue = deserializer.getNullValue(context);
 assertEquals("null", nullValue.get());

 // Test getEmptyValue
 AtomicReference<Object> emptyValue = deserializer.getEmptyValue(context);
 assertNull(emptyValue.get());

 // Test getReferenced
 AtomicReference<Object> reference = new AtomicReference<Object>("reference");
 Object referenced = deserializer.getReferenced(reference);
 assertEquals(reference.get(), referenced);

 // Test updateReference
 AtomicReference<Object> updatedReference = new AtomicReference<Object>("updated");
 deserializer.updateReference(reference, updatedReference);
 assertEquals(updatedReference.get(), reference.get());

 // Test supportsUpdate
 assertTrue(deserializer.supportsUpdate(context.getConfig()));
 }

 private static final EmptyValueProvider emptyValueProviderInstance = new EmptyValueProvider() {
 @Override public Object createValueInstance(DeserializationConfig config, BeanDescription beanDesc)
 throws JsonMappingException, IOException {
 return new AtomicReference<Object>();
 }

 @Override public Object getEmptyValue(DeserializationContext ctxt, TypeDescriptor typeDesc)
 throws JsonProcessingException {
 return ctxt.getInstanceProvider().getEmptyValueProvider().getEmptyValue(ctxt, typeDesc);
 }
 };
}