package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for StringArrayDeserializer.
 */
public class StringArrayDeserializerTest {

 @Test
 public void testDeserializeStandardArray() throws IOException {
 String json = "[\"Alice\", \"Bob\", \"Charlie\"]";
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 String[] result = mapper.readValue(json, deserializer.getType());
 assertArrayEquals(new String[]{"Alice", "Bob", "Charlie"}, result);
 }

 @Test
 public void testDeserializeCustomArrayElementDeserializer() throws IOException {
 JsonDeserializer<String> customDeserializer = new CustomStringDeserializer();
 StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
 ObjectMapper mapper = new ObjectMapper();
 String json = "[null, \"Alice\", \"Bob\", \"Charlie\"]";
 String[] result = mapper.readValue(json, deserializer.getType());
 assertEquals(new String[]{"null", "Alice", "Bob", "Charlie"}, result);
 }

 @Test
 public void testDeserializeNullArray() throws IOException {
 String json = "[null]";
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 String[] result = mapper.readValue(json, deserializer.getType());
 assertArrayEquals(new String[]{}, result);
 }

 @Test
 public void testDeserializeEmptyArray() throws IOException {
 String json = "[]";
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 String[] result = mapper.readValue(json, deserializer.getType());
 assertArrayEquals(new String[]{}, result);
 }

 @Test
 public void testDeserializeEmptyStringAsNull() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.disable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
 String json = "[\"\"]";
 String[] result = mapper.readValue(json, StringArrayDeserializer.class);
 assertArrayEquals(new String[]{null}, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeInvalidJson() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 String json = "[1, 2, 3]";
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 mapper.readValue(json, deserializer.getType());
 fail("Expected IOException but no exception was thrown.");
 }

 @Test
 public void testDeserializeContextual() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 String json = "[null, \"Alice\", \"Bob\", \"Charlie\"]";
 String[] result = mapper.readValue(json, ContextualDeserializer.class);
 assertEquals(new String[]{"null", "Alice", "Bob", "Charlie"}, result);
 }

 /**
 * Custom deserializer for strings.
 */
 @JsonDeserialize(using = CustomStringDeserializer.class)
 static class CustomStringDeserializer extends StdDeserializer<String> {
 @Override
 public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 String value = jp.getText();
 if (value.equals("null")) {
 return null;
 }
 return value.toUpperCase();
 }
 }

}