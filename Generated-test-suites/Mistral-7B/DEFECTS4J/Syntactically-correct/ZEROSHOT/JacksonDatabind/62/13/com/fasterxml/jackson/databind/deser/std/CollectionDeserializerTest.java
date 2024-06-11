package com.example.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.jsontype.*;

@org.junit.runner.RunWith(org.junit.platform.runner.JUnitPlatform.class)
public class CollectionDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @org.junit.BeforeClass
 static void setup() throws Exception {
 mapper.registerModule(new SimpleModule()
 .addDeserializer(String[].class, new StringArrayDeserializer()));
 }

 @org.junit.Test
 public void testEmptyCollection() throws Exception {
 JsonParser p = mapper.getFactory().createParser("[]");
 CollectionDeserializer deserializer = new CollectionDeserializer(
 Map.class, null, null, null);
 List<Object> result = deserializer.deserialize(p, new TestContext(), null);
 assertTrue(result.isEmpty());
 }

 @org.junit.Test
 public void testCollectionWithSingleElement() throws Exception {
 JsonParser p = mapper.getFactory().createParser("[1]");
 CollectionDeserializer deserializer = new CollectionDeserializer(
 Collection.class, new SimpleIntDeserializer(), null, null);
 List<Object> result = deserializer.deserialize(p, new TestContext(), null);
 assertEquals(1, result.size());
 assertEquals(1, result.get(0));
 }

 @org.junit.Test
 public void testCollectionWithMultipleElements() throws Exception {
 JsonParser p = mapper.getFactory().createParser("[-1, 0, 2.5, 3, 4.5, 5.0]");
 CollectionDeserializer deserializer = new CollectionDeserializer(
 Collection.class, new SimpleIntDeserializer(), null, null);
 List<Object> result = deserializer.deserialize(p, new TestContext(), null);
 assertEquals(6, result.size());
 assertEquals(-1, result.get(0));
 assertEquals(0, result.get(1));
 assertEquals(2.5, result.get(2));
 assertEquals(3, result.get(3));
 assertEquals(4.5, result.get(4));
 assertEquals(5.0, result.get(5));
 }

 @org.junit.Test
 public void testCollectionWithNullElement() throws Exception {
 JsonParser p = mapper.getFactory().createParser("[null]");
 CollectionDeserializer deserializer = new CollectionDeserializer(
 Collection.class, new SimpleNullDeserializer(), null, null);
 List<Object> result = deserializer.deserialize(p, new TestContext(), null);
 assertEquals(1, result.size());
 assertNull(result.get(0));
 }

 @org.junit.Test
 public void testCollectionWithTypeInformation() throws Exception {
 JsonParser p = mapper.getFactory().createParser("[1, 2.5, false, null]");
 CollectionDeserializer deserializer = new CollectionDeserializer(
 List.class, new SimpleIntDeserializer(), new SimpleDoubleDeserializer(), null);
 TypeDeserializer typeDeserializer = new SimpleTypeDeserializer(String.class);
 List<Object> result = deserializer.deserialize(p, new TestContext(), typeDeserializer);
 assertEquals(4, result.size());
 assertTrue(result.get(0) instanceof Integer);
 assertEquals(1, result.get(0));
 assertTrue(result.get(1) instanceof Double);
 assertEquals(2.5, result.get(1));
 assertTrue(result.get(2) instanceof Boolean);
 assertEquals(false, result.get(2));
 assertNull(result.get(3));
 }

 @org.junit.Test
 public void testCollectionWithDelegateDeserializer() throws Exception {
 JsonParser p = mapper.getFactory().createParser("[\"apple\", \"banana\"]");
 CollectionDeserializer deserializer = new CollectionDeserializer(
 List.class, null, null, new StringDeserializer());
 List<Object> result = deserializer.deserialize(p, new TestContext(), null);
 assertEquals(2, result.size());
 assertTrue(result.get(0) instanceof String);
 assertEquals("apple", result.get(0));
 assertTrue(result.get(1) instanceof String);
 assertEquals("banana", result.get(1));
 }

 @org.junit.Test
 public void testCollectionWithCustomIdReader() throws Exception {
 JsonParser p = mapper.getFactory().createParser("[-1, 0, 2.5, 3, 4.5, 5.0]");
 CollectionDeserializer deserializer = new CollectionDeserializer(
 List.class, new SimpleIntDeserializer(), null, new TestIdReader());
 List<Object> result = deserializer.deserialize(p, new TestContext(), null);
 assertEquals(6, result.size());
 assertEquals(-1, result.get(0));
 assertEquals(0, result.get(1));
 assertEquals(2.5, result.get(2));
 assertEquals(3, result.get(3));
 assertEquals(4.5, result.get(4));
 assertEquals(5.0, result.get(5));
 }

 @org.junit.Test
 public void testCollectionWithEmptyString() throws Exception {
 JsonParser p = mapper.getFactory().createParser("[]");
 CollectionDeserializer deserializer = new CollectionDeserializer(
 List.class, null, null, null);
 List<Object> result = deserializer.deserialize(p, new TestContext(), null);
 assertTrue(result.isEmpty());
 }

 @JsonFormat(features = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
 static class TestContext extends DeserializationContext {
 }

 static class SimpleIntDeserializer extends StdDeserializer<Integer> {
 @Override
 public Integer deserialize(JsonParser p, DeserializationContext ctxt)
 throws IOException {
 return p.getValueAsInt();
 }
 }

 static class SimpleDoubleDeserializer extends StdDeserializer<Double> {
 @Override
 public Double deserialize(JsonParser p, DeserializationContext ctxt)
 throws IOException {
 return p.getValueAsDouble();
 }
 }

 static class SimpleNullDeserializer extends StdDeserializer<Object> {
 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt)
 throws IOException {
 return null;
 }
 }

 static class SimpleTypeDeserializer extends StdTypeDeserializer<String> {
 @Override
 public String deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt)
 throws IOException {
 return p.getValueAsString();
 }
 }

 static class StringArrayDeserializer extends StdDeserializer<String[]> {
 @Override
 public String[] deserialize(JsonParser p, DeserializationContext ctxt)
 throws IOException {
 JsonToken t = p.nextToken();
 if (t == JsonToken.START_ARRAY) {
 List<String> result = new ArrayList<String>();
 while (!p.isExpectedEndOfValue()) {
 result.add(p.getValueAsString());
 p.nextToken();
 }
 p.nextToken(JsonToken.END_ARRAY);
 return result.toArray(new String[result.size()]);
 }
 throw new IOException("Expected START_ARRAY");
 }
 }

 static class StringDeserializer extends StdDeserializer<String> {
 @Override
 public String deserialize(JsonParser p, DeserializationContext ctxt)
 throws IOException {
 return p.getValueAsString();
 }
 }

 static class TestIdReader extends StdScalarValueIdReader<Object> {
 @Override
 public Object idFromValue(Object value) {
 return value;
 }
 }
}