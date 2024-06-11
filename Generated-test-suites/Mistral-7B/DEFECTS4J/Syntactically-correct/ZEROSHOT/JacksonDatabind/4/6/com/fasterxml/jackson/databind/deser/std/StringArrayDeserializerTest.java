package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.StdDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import com.fasterxml.jackson.test.BaseMapTest;

@JacksonStdImpl
public class StringArrayDeserializerTest extends BaseMapTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();
 private static final JsonFactory JSON_FACTORY = MAPPER.getFactory();

 private ObjectBuffer objectBuffer;
 private DeserializationContext context;
 private JsonParser jsonParser;
 private StringArrayDeserializer deserializer;
 private JsonDeserializer<String> elementDeserializer;

 @Before
 public void setUp() throws Exception {
 objectBuffer = new ObjectBuffer(16);
 context = MAPPER.getDeserializationContext().overrideActiveContext(new TestContext());
 jsonParser = new JsonParser(new CharArrayBuilder(128), context);
 deserializer = StringArrayDeserializer.instance;
 elementDeserializer = mock(JsonDeserializer.class);
 }

 @Test
 public void testDeserialize_EmptyArray() throws Exception {
 String[] result = deserializer.deserialize(jsonParser, context);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserialize_EmptyString() throws Exception {
 jsonParser.nextValue("[]");
 String[] result = deserializer.deserialize(jsonParser, context);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserialize_SingleValue() throws Exception {
 jsonParser.nextValue("abc");
 String[] result = deserializer.deserialize(jsonParser, context);
 assertArrayEquals(new String[]{"abc"}, result);
 }

 @Test
 public void testDeserialize_SingleNullValue() throws Exception {
 jsonParser.nextValue("null");
 String[] result = deserializer.deserialize(jsonParser, context);
 assertArrayEquals(new String[]{}, result);
 }

 @Test
 public void testDeserialize_ArrayWithCustomElementDeserializer() throws Exception {
 when(elementDeserializer.deserialize(jsonParser, context)).thenReturn("customValue");
 jsonParser.nextToken();
 jsonParser.nextValue("xyz");
 deserializer = new StringArrayDeserializer(elementDeserializer);
 String[] result = deserializer.deserialize(jsonParser, context);
 assertArrayEquals(new String[]{"customValue"}, result);
 }

 @Test
 public void testDeserialize_ArrayWithNullElements() throws Exception {
 String[] values = {"foo", null, "bar"};
 jsonParser.startArray();
 for (String value : values) {
 if (value != null) {
 jsonParser.value(value);
 } else {
 jsonParser.valueNull();
 }
 }
 jsonParser.endArray();
 String[] result = deserializer.deserialize(jsonParser, context);
 assertArrayEquals(values, result);
 }

 @Test
 public void testDeserialize_ArrayWithEmptyString() throws Exception {
 String[] values = {"foo", "", "bar"};
 jsonParser.startArray();
 for (String value : values) {
 if (value != null) {
 jsonParser.value(value);
 } else {
 jsonParser.valueNull();
 }
 }
 jsonParser.endArray();
 String[] result = deserializer.deserialize(jsonParser, context);
 assertArrayEquals(values, result);
 }

 @Test
 public void testDeserialize_ArrayWithCustomStartArrayDeserializer() throws IOException {
 ObjectBuffer buffer = context.leaseObjectBuffer();
 Object[] chunk = buffer.resetAndStart();
 JsonDeserializer<String> startArrayDeserializer = mock(JsonDeserializer.class);
 when(startArrayDeserializer.deserialize(jsonParser, context)).thenReturn(chunk[0]);
deserializer = new StringArrayDeserializer(startArrayDeserializer);
 String[] result = deserializer.deserialize(jsonParser, context);
 assertArrayEquals(new String[]{String.valueOf(chunk[0])}, result);
 }

 @Test(expected = IOException.class)
 public void testDeserialize_InvalidJson() throws Exception {
 jsonParser.nextValue("{foo: bar}");
 deserializer.deserialize(jsonParser, context);
 }

 @Test
 public void testDeserializeWithType_EmptyArray() throws Exception {
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 String[] result = (String[]) deserializer.deserializeWithType(jsonParser, context, typeDeserializer);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeWithType_ArrayWithNullElements() throws Exception {
 String[] values = {"foo", null, "bar"};
 Type type = collectTypes(values);
 String[] result = (String[]) deserializer.deserializeWithType(jsonParser, context, typeDeserializer);
 assertArrayEquals(values, result);
 }

 @Test
 public void testDeserializeWithType_ArrayWithCustomElementDeserializer() throws Exception {
 when(elementDeserializer.deserialize(jsonParser, context)).thenReturn("customValue");
 Type type = constructType(String[].class, String.class);
 String[] result = (String[]) deserializer.deserializeWithType(jsonParser, context, typeDeserializer);
 assertArrayEquals(new String[]{"customValue"}, result);
 }

 @Test
 public void testCreateContextual() throws Exception {
 JsonDeserializer<String> deseralizerWithElementDeserializer = mock(JsonDeserializer.class);
 when(deseralizerWithElementDeserializer.isDefaultDeserializer()).thenReturn(false);
 StringArrayDeserializer expected = new StringArrayDeserializer(deseralizerWithElementDeserializer);
 JsonDeserializer<?> actual = deserializer.createContextual(context, null);
 assertEquals(expected, actual);
 }

 @Test
 public void testCreateContextual_NullElementDeserializer() throws Exception {
 JsonDeserializer<?> actual = deserializer.createContextual(context, null);
 assertNull(actual);
 }

 private static Type constructType(Class<?> componentType, Class<?>... typeArgs) {
 return MAPPER.constructType(componentType, typeArgs);
 }

 private static Type collectTypes(Object[] values) throws JsonProcessingException {
 return MAPPER.getTypeFactory().constructArrayType(String.class, values);
 }
}