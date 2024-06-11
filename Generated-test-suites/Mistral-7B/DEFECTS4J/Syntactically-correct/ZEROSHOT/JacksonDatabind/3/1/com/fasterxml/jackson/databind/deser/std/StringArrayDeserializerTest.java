package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

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

/**
 * Test file for StringArrayDeserializer class.
 */
@JacksonStdImpl
public class StringArrayDeserializerTest {

 private ObjectBuffer objectBuffer;
 private DeserializationContext deserializationContext;
 private JsonFactory jsonFactory;
 private JsonParser jsonParser;
 private StringArrayDeserializer stringArrayDeserializer;
 private JsonDeserializer<String> stringDeserializer;

 @Before
 public void setup() throws Exception {
 jsonFactory = new ObjectMapper().getFactory();
 deserializationContext = jsonFactory.createParser("{}").getCodec().treeAsTokens(jsonFactory.createParser("{}")).nextValue().context();
 stringArrayDeserializer = StringArrayDeserializer.instance;
 stringDeserializer = new StdDeserializer<String>(String.class);
 }

 @Test
 public void testDeserializeNull() throws IOException {
 String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
 assertNull(result);
 }

 @Test
 public void testDeserializeEmptyArray() throws IOException {
 JsonParser jsonParser = jsonFactory.createParser("[{}]");
 String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeOneElementArray() throws IOException {
 JsonParser jsonParser = jsonFactory.createParser("['abc']");
 String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
 assertEquals("abc", result[0]);
 }

 @Test
 public void testDeserializeMultipleElementsArray() throws IOException {
 JsonParser jsonParser = jsonFactory.createParser("[\"abc\",\"def\",null]");
 ObjectBuffer buffer = deserializationContext.leaseObjectBuffer();
 String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
 assertArrayEquals(new String[] {"abc", "def", null}, result);
 deserializationContext.returnObjectBuffer(buffer);
 }

 @Test
 public void testDeserializeCustomElementDeserializer() throws IOException {
 jsonParser = jsonFactory.createParser("[\"abc\",\"def\"]");
 stringDeserializer = new StdDeserializer<String>(String.class) {
 @Override
 public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 return jp.getText() + "_custom";
 }
 };
 StringArrayDeserializer customStringArrayDeserializer = new StringArrayDeserializer(stringDeserializer);
 String[] result = customStringArrayDeserializer.deserialize(jsonParser, deserializationContext);
 assertArrayEquals(new String[] {"abc_custom", "def_custom"}, result);
 }

 @Test
 public void testDeserializeWithTypeDeserializer() throws IOException {
 TypeDeserializer typeDeserializer = new StdTypeDeserializer() {
 @Override
 public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt, TypeDeserializer vd) throws IOException {
 return new Object[] { "abc", "def", null };
 }
 };
 String[] result = stringArrayDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
 assertArrayEquals(new String[] {"abc", "def", null}, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeNotStartArrayToken() throws IOException {
 jsonParser = jsonFactory.createParser("{\"value\":\"abc\"}");
 stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
 fail("Expected IOException to be thrown");
 }

 @Test(expected = IOException.class)
 public void testDeserializeNonStringElement() throws IOException {
 jsonParser = jsonFactory.createParser("[1,2,3]");
 stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
 fail("Expected IOException to be thrown");
 }

 @Test(expected = IOException.class)
 public void testDeserializeNullValueDeserializer() throws IOException {
 jsonParser = jsonFactory.createParser("[null]");
 stringArrayDeserializer = new StringArrayDeserializer(null);
 stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
 fail("Expected IOException to be thrown");
 }
}