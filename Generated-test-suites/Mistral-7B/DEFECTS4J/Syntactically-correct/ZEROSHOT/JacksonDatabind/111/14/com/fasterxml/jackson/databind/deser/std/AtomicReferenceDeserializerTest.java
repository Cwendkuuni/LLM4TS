package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AtomicReferenceDeserializerTest {

 private AtomicReferenceDeserializer deserializer;
 private ObjectMapper objectMapper;
 private DeserializationContext context;
 private ValueInstantiator valueInstantiator;
 private TypeDeserializer typeDeserializer;
 private TypeSerializer typeSerializer;

 @Before
 public void setup() {
 valueInstantiator = mock(ValueInstantiator.class);
 typeDeserializer = mock(TypeDeserializer.class);
 typeSerializer = mock(TypeSerializer.class);
 context = mock(DeserializationContext.class);
 objectMapper = new ObjectMapper();
 deserializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>() {}, valueInstantiator, typeDeserializer, null);
 }

 @Test
 public void testGetNullValue() throws JsonMappingException, IOException {
 AtomicReference<Object> result = deserializer.getNullValue(context);
 assertNull(result.get());
 }

 @Test
 public void testGetEmptyValue() {
 AtomicReference<Object> result = deserializer.getEmptyValue(context);
 assertEquals(new AtomicReference<Object>(), result);
 }

 @Test
 public void testReferenceValue() throws IOException {
 Object contents = new Object();
 AtomicReference<Object> result = deserializer.referenceValue(contents);
 assertEquals(contents, result.get());
 }

 @Test
 public void testGetReferenced() throws IOException {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object result = deserializer.getReferenced(reference);
 assertEquals(reference.get(), result);
 }

 @Test
 public void testUpdateReference() throws IOException {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object newContents = new Object();
 AtomicReference<Object> updatedReference = deserializer.updateReference(reference, newContents);
 assertEquals(newContents, updatedReference.get());
 }

 @Test
 public void testWithResolved() throws IOException {
 TypeDeserializer typeDeser = new EmptyTypeDeserializer();
 AtomicReferenceDeserializer deserializer1 = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>() {}, valueInstantiator, typeDeseralizer, null);
 AtomicReferenceDeserializer deserializer2 = deserializer.withResolved(null, null);
 assertEquals(deserializer1, deserializer2);
 }

 @Test
 public void testSupportsUpdate() throws IOException {
 assert(deserializer.supportsUpdate(context.getConfig()));
 }

 @Test
 public void testDeserialize() throws IOException {
 ObjectNode node = objectMapper.createObjectNode();
 node.put("value", "test");
 JsonNode jsonNode = objectMapper.readTree(node.toString());
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 when(typeDeserializer.deserialize(context, jsonNode, null, null)).thenReturn("testValue");
 when(valueInstantiator.createValueInstance(context, JsonType.ValueType.STRING)).thenReturn(new String("testValue"));
 when(typeSerializer.deserialize(context, jsonNode, null)).thenReturn(new TypeReference<String>() {}.getValueClass());
 AtomicReference<Object> expected = new AtomicReference<Object>("testValue");
 Object result = deserializer.deserialize(context, jsonNode, typeDeserializer, typeSerializer);
 assertEquals(expected, result);
 }
}