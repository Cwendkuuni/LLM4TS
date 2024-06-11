package com.example.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyContextTypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StringCollectionDeserializerTest {

 private StringCollectionDeserializer stringCollectionDeserializer;
 private ObjectMapper objectMapper;
 private JsonDeserializer<String> stringDeserializer;
 private ValueInstantiator valueInstantiator;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 stringDeserializer = mock(JsonDeserializer.class);
 valueInstantiator = mock(ValueInstantiator.class);
 stringCollectionDeserializer = new StringCollectionDeserializer(
 new SimpleType(List.class.getName(), String.class),
 stringDeserializer, valueInstantiator);
 }

 @Test
 public void testEmptyCollectionDeserialization() throws IOException {
 Collection<String> result = stringCollectionDeserializer.deserialize(objectMapper.getFactory().getJsonParser(new StringReader("[]")), new DeserializationContext(objectMapper), null);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSingleElementCollectionDeserialization() throws IOException {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("value", "test");
 Collection<String> result = stringCollectionDeserializer.deserialize(objectMapper.getFactory().getJsonParser(jsonNode), new DeserializationContext(objectMapper), null);
 assertEquals(1, result.size());
 assertEquals("test", result.iterator().next());
 }

 @Test
 public void testMultipleElementsCollectionDeserialization() throws IOException {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("values", Arrays.asList("test1", "test2"));
 Collection<String> result = stringCollectionDeserializer.deserialize(objectMapper.getFactory().getJsonParser(jsonNode), new DeserializationContext(objectMapper), null);
 assertEquals(2, result.size());
 assertTrue(result.containsAll(Arrays.asList("test1", "test2")));
 }

 @Test
 public void testDeserializationWithCustomDeserializer() throws IOException {
 when(stringDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("customDeserializedString");
 Collection<String> result = stringCollectionDeserializer.deserialize(objectMapper.getFactory().getJsonParser(new StringReader("[\"customDeserializedString\"]")), new DeserializationContext(objectMapper), null);
 assertEquals(1, result.size());
 assertEquals("customDeserializedString", result.iterator().next());
 }

 @Test
 public void testDeserializationWithValueInstantiator() throws IOException {
 when(valueInstantiator.createWithValue(any(DeserializationContext.class), any(Class.class), anyString(), any(JsonParser.class), any(Object.class))).thenReturn(new ArrayList<>());
 Collection<String> result = stringCollectionDeserializer.deserialize(objectMapper.getFactory().getJsonParser(new StringReader("[\"test1\", \"test2\"]")), new DeserializationContext(objectMapper), null);
 assertEquals(2, result.size());
 assertTrue(result.containsAll(Arrays.asList("test1", "test2")));
 }

 @Test
 public void testDeserializationWithTypeDeserializer() throws IOException {
 TypeDeserializer emptyContextTypeDeserializer = new EmptyContextTypeDeserializer();
 Collection<String> result = stringCollectionDeserializer.deserializeWithType(objectMapper.getFactory().getJsonParser(new StringReader("[\"test1\", \"test2\"]")), new DeserializationContext(objectMapper), emptyContextTypeDeserializer);
 assertTrue(result instanceof List);
 assertEquals(2, ((List) result).size());
 assertTrue(result.containsAll(Arrays.asList("test1", "test2")));
 }

 @Test(expected = IOException.class)
 public void testDeserializationWithInvalidJson() throws IOException {
 stringCollectionDeserializer.deserialize(objectMapper.getFactory().getJsonParser(new StringReader("{")), new DeserializationContext(objectMapper), null);
 }
}