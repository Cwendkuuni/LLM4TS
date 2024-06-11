package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {

 private ObjectMapper objectMapper;
 private DeserializationContext context;
 private JsonParser jsonParser;
 private ObjectBuffer objectBuffer;
 private StringArrayDeserializer stringArrayDeserializer;
 private JsonDeserializer<String> stringDeserializer;

 @Before
 public void setup() throws IOException {
 objectMapper = new ObjectMapper();
 context = objectMapper.getDeserializationContext();
 jsonParser = context.getBaseParser();
 objectBuffer = context.leaseObjectBuffer();
 stringDeserializer = mock(JsonDeserializer.class);
 stringArrayDeserializer = new StringArrayDeserializer();
 }

 @Test
 public void testDeserializeNullElementDeserializer() throws IOException {
 String[] result = stringArrayDeserializer.deserialize(jsonParser, context);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeEmptyArray() throws IOException {
 jsonParser.nextToken(); // consume START_ARRAY
 String[] result = stringArrayDeserializer.deserialize(jsonParser, context);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeOneElement() throws IOException {
 when(stringDeserializer.deserialize(jsonParser, context)).thenReturn("test");
 jsonParser.nextToken(); // consume START_ARRAY
 jsonParser.nextToken(); // consume VALUE_STRING
 jsonParser.nextToken(); // consume END_ARRAY
 String[] result = stringArrayDeserializer.deserialize(jsonParser, context);
 assertArrayEquals(new String[]{"test"}, result);
 }

 @Test
 public void testDeserializeCustomElementDeserializer() throws IOException {
 when(stringDeserializer.deserialize(jsonParser, context)).thenReturn("test");
 stringArrayDeserializer = new StringArrayDeserializer(stringDeserializer);
 jsonParser.nextToken(); // consume START_ARRAY
 jsonParser.nextToken(); // consume VALUE_STRING
 jsonParser.nextToken(); // consume END_ARRAY
 String[] result = stringArrayDeserializer.deserialize(jsonParser, context);
 assertArrayEquals(new String[]{"test"}, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJson() throws IOException {
 jsonParser.nextToken(); // consume INT
 String[] result = stringArrayDeserializer.deserialize(jsonParser, context);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeNullValue() throws IOException {
 jsonParser.nextToken(); // consume START_ARRAY
 jsonParser.nextToken(); // consume VALUE_NULL
 String[] result = stringArrayDeserializer.deserialize(jsonParser, context);
 }

 @Test
 public void testDeserializeMultipleElements() throws IOException {
 when(stringDeserializer.deserialize(jsonParser, context)).thenReturn("test1");
 when(stringDeserializer.deserialize(jsonParser, context)).thenReturn("test2");
 jsonParser.nextToken(); // consume START_ARRAY
 jsonParser.nextToken(); // consume VALUE_STRING
 String value1 = jsonParser.getText();
 jsonParser.nextToken(); // consume VALUE_STRING
 String value2 = jsonParser.getText();
 jsonParser.nextToken(); // consume END_ARRAY
 String[] result = stringArrayDeserializer.deserialize(jsonParser, context);
 assertArrayEquals(new String[]{"test1", "test2"}, result);
 }

 @Test
 public void testDeserializeWithType() throws IOException {
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 when(typeDeserializer.deserializeTypedFromArray(jsonParser, context)).thenReturn(new Object[]{"test"});
 String[] result = stringArrayDeserializer.deserializeWithType(jsonParser, context, typeDeserializer);
 assertArrayEquals(new String[]{"test"}, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmptyStringAsNull() throws IOException {
 jsonParser.nextToken(); // consume START_ARRAY
 jsonParser.nextToken(); // consume VALUE_STRING
 jsonParser.nextText("");
 jsonParser.nextToken(); // consume END_ARRAY
 String[] result = stringArrayDeserializer.deserialize(jsonParser, context);
 assertArrayEquals(new String[0], result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmptyStringAsNullFeatureDisabled() throws IOException {
 context.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, false);
 jsonParser.nextToken(); // consume START_ARRAY
 jsonParser.nextToken(); // consume VALUE_STRING
 jsonParser.nextText("");
 jsonParser.nextToken(); // consume END_ARRAY
 String[] result = stringArrayDeserializer.deserialize(jsonParser, context);
 assertArrayEquals(new String[0], result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeNullValueAsNullFeatureDisabled() throws IOException {
 context.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, false);
 jsonParser.nextToken(); // consume START_ARRAY
 jsonParser.nextToken(); // consume VALUE_NULL
 jsonParser.nextToken(); // consume END_ARRAY
 String[] result = stringArrayDeserializer.deserialize(jsonParser, context);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeNullValueAsNullFeatureEnabled() throws IOException {
 context.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
 jsonParser.nextToken(); // consume START_ARRAY
 jsonParser.nextToken(); // consume VALUE_NULL
 String[] result = stringArrayDeserializer.deserialize(jsonParser, context);
 assertNull(result);
 }

 @Test
 public void testCreateContextual() throws IOException, JsonMappingException {
 StringArrayDeserializer deserializer = mock(StringArrayDeserializer.class);
 when(deserializer.isDefaultDeserializer(stringDeserializer)).thenReturn(false);
 JsonDeserializer<String> contextualDeserializer = stringArrayDeserializer.createContextual(context, null);
 assertEquals(deserializer, contextualDeserializer);
 }

 @Test
 public void testCreateContextualWithDefaultDeserializer() throws IOException, JsonMappingException {
 StringArrayDeserializer deserializer = mock(StringArrayDeserializer.class);
 when(deserializer.isDefaultDeserializer(stringDeserializer)).thenReturn(true);
 JsonDeserializer<String> contextualDeserializer = stringArrayDeserializer.createContextual(context, null);
 assertNull(contextualDeserializer);
 }

 @Test
 public void testCreateContextualWithCustomDeserializer() throws IOException, JsonMappingException {
 StringArrayDeserializer deserializer = mock(StringArrayDeserializer.class);
 when(deserializer.isDefaultDeserializer(stringDeserializer)).thenReturn(false);
 when(deserializer.createContextual(context, null)).thenReturn(stringDeserializer);
 JsonDeserializer<String> contextualDeserializer = stringArrayDeserializer.createContextual(context, null);
 assertEquals(stringDeserializer, contextualDeserializer);
 }

 @Test
 public void testCreateContextualWithTypeDeserializer() throws IOException, JsonMappingException {
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 when(typeDeserializer.deserializeTypedFromArray(jsonParser, context)).thenReturn(new Object[]{"test"});
 JsonDeserializer<String> contextualDeserializer = stringArrayDeserializer.createContextual(context, null);
 assertEquals(contextualDeserializer, stringDeserializer);
 }
}