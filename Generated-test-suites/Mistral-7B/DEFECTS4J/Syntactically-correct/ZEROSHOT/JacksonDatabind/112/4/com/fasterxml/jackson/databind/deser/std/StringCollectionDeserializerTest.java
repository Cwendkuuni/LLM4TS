package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;

public class StringCollectionDeserializerTest {

 private ObjectMapper mapper;
 private DeserializationContext context;
 private JsonParser parser;
 private StringCollectionDeserializer deserializer;
 private ValueInstantiator valueInstantiator;
 private JsonDeserializer<String> valueDeserializer;
 private NullValueProvider nullValueProvider;
 private TypeDeserializer typeDeserializer;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 context = mock(DeserializationContext.class);
 parser = mock(JsonParser.class);
 valueInstantiator = mock(ValueInstantiator.class);
 valueDeserializer = mock(JsonDeserializer.class);
 nullValueProvider = mock(NullValueProvider.class);
 typeDeserializer = mock(TypeDeserializer.class);
 deserializer = new StringCollectionDeserializer(new TypeReference<Collection<String>>() {}, valueDeserializer, valueInstantiator);
 }

 @Test
 public void testEmptyCollectionDeserialization() throws IOException, JsonMappingException {
 when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);
 when(valueDeserializer.deserialize(parser, context)).thenReturn(null);
 when(valueInstantiator.createUsingDefault(context)).thenReturn(Collections.emptyList());

 List<String> result = deserializer.deserialize(parser, context);
 assertEquals(Collections.emptyList(), result);
 }

 @Test
 public void testSingleValueDeserialization() throws IOException, JsonMappingException {
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(parser.nextTextValue()).thenReturn("Test String");
 when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);
 when(valueDeserializer.deserialize(parser, context)).thenReturn("Test String");
 when(valueInstantiator.createUsingDefault(context)).thenReturn(new ArrayList<>());

 List<String> result = deserializer.deserialize(parser, context);
 assertEquals(Arrays.asList("Test String"), result);
 }

 @Test
 public void testNullValueDeserialization() throws IOException, JsonMappingException {
 when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);

 List<String> result = deserializer.deserialize(parser, context);
 assertNull(result);
 }

 @Test
 public void testMultipleValuesDeserialization() throws IOException, JsonMappingException {
 when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(parser.nextTextValue()).thenReturn("First Value");
 when(parser.nextTextValue()).thenReturn("Second Value");
 when(parser.nextTextValue()).thenReturn(null);
 when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);
 when(valueDeserializer.deserialize(parser, context)).thenAnswer(i -> i.getValue(0));
 when(valueInstantiator.createUsingDefault(context)).thenReturn(new ArrayList<>());

 List<String> result = deserializer.deserialize(parser, context);
 assertEquals(Arrays.asList("First Value", "Second Value"), result);
 }

 @Test
 public void testDeserializeWithType() throws IOException, JsonMappingException {
 when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(typeDeserializer.deserializeTypedFromArray(parser, context)).thenReturn(new ArrayList<>());

 Collection<String> result = deserializer.deserializeWithType(parser, context, typeDeserializer);
 assertEquals(new ArrayList<>(), result);
 }
}