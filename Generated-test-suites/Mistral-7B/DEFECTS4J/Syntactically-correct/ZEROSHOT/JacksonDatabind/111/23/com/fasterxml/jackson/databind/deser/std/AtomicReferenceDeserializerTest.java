package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyValueInstantiator;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class AtomicReferenceDeserializerTest {

 private AtomicReferenceDeserializer serializer;
 private DeserializationContext context;
 private JsonDeserializer<Object> valueDeserializer;
 private TypeDeserializer typeDeserializer;
 private EmptyValueInstantiator emptyValueInstantiator;
 private JavaType fullType;

 @Before
 public void setup() {
 valueDeserializer = mock(JsonDeserializer.class);
 typeDeserializer = mock(TypeDeserializer.class);
 emptyValueInstantiator = mock(EmptyValueInstantiator.class);
 context = mock(DeserializationContext.class);
 fullType = TypeFactory.defaultInstance().constructType(AtomicReference.class, Object.class);
 serializer = new AtomicReferenceDeserializer(fullType, null, typeDeserializer, valueDeserializer);
 }

 @Test
 public void testGetNullValue() throws Exception {
 assertEquals(new AtomicReference<Object>(), serializer.getNullValue(context));
 }

 @Test
 public void testGetEmptyValue() {
 assertEquals(new AtomicReference<Object>(), serializer.getEmptyValue(context));
 }

 @Test
 public void testReferenceValue() {
 AtomicReference<Object> reference = new AtomicReference<Object>();
 Object contents = new Object();
 AtomicReference<Object> result = serializer.referenceValue(contents);
 assertEquals(contents, result.get());
 }

 @Test
 public void testGetReferenced() throws Exception {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 assertEquals(reference.get(), serializer.getReferenced(reference));
 }

 @Test
 public void testUpdateReference() throws Exception {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object newValue = new Object();
 AtomicReference<Object> result = serializer.updateReference(reference, newValue);
 assertEquals(newValue, result.get());
 }

 @Test
 public void testSupportsUpdate() throws Exception {
 assertTrue(serializer.supportsUpdate(context.getDefaultConfig()));
 }
}