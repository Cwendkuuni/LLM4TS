package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StringCollectionDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @BeforeAll
 static void setup() {
 MAPPER.registerModule(new SimpleModule()
 .addDeserializer(Collection.class, new StringCollectionDeserializer(
 MapTypeFactory.constructCollectionType(List.class, String.class), null, null))
 );
 }

 @Test
 void testEmptyCollection() throws IOException {
 JsonNode node = MAPPER.readTree("[]");
 Collection<String> result = MAPPER.readValue(node, Collection.class);
 assertEquals(Collections.emptyList(), result);
 }

 @Test
 void testEmptyCollectionWithValueDeserializer() throws IOException {
 JsonNode node = MAPPER.readTree("[]");
 JsonDeserializer<String> valueDeserializer = new StringDeserializer();
 StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
 MapTypeFactory.constructCollectionType(List.class, String.class), null, null, null, null, null,
 null, null, valueDeserializer);
 Collection<String> result = MAPPER.readerForUpdating(new ArrayList<>()).readValue(node);
 assertEquals(Collections.emptyList(), result);
 }

 @Test
 void testSingleValueCollection() throws IOException {
 JsonNode node = MAPPER.readTree("[\"" + "one" + "\"]");
 Collection<String> result = MAPPER.readValue(node, Collection.class);
 assertEquals(Collections.singletonList("one"), result);
 }

 @Test
 void testSingleValueCollectionWithValueDeserializer() throws IOException {
 JsonNode node = MAPPER.readTree("[\"" + "one" + "\"]");
 JsonDeserializer<String> valueDeserializer = new StringDeserializer();
 StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
 MapTypeFactory.constructCollectionType(List.class, String.class), null, null, null, null, null,
 null, null, valueDeserializer);
 Collection<String> result = MAPPER.readerForUpdating(new ArrayList<>()).readValue(node);
 assertEquals(Collections.singletonList("one"), result);
 }

 @Test
 void testNullValueCollection() throws IOException {
 JsonNode node = MAPPER.readTree("null");
 Collection<String> result = MAPPER.readValue(node, Collection.class);
 assertNull(result);
 }

 @Test
 void testNullValueCollectionWithValueDeserializer() throws IOException {
 JsonNode node = MAPPER.readTree("null");
 JsonDeserializer<String> valueDeserializer = new StringDeserializer();
 StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
 MapTypeFactory.constructCollectionType(List.class, String.class), null, null, null, null, null,
 null, null, valueDeserializer);
 Collection<String> result = MAPPER.readerForUpdating(new ArrayList<>()).readValue(node);
 assertNull(result);
 }

 @Test
 void testStringCollectionWithEmptyString() throws IOException {
 JsonNode node = MAPPER.readTree("[\"\", \"one\", \"two\"]");
 Collection<String> result = MAPPER.readValue(node, Collection.class);
 assertEquals(Arrays.asList("", "one", "two"), result);
 }

 @Test
 void testStringCollectionWithEmptyStringAndValueDeserializer() throws IOException {
 JsonNode node = MAPPER.readTree("[\"\", \"one\", \"two\"]");
 JsonDeserializer<String> valueDeserializer = new StringDeserializer();
 StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
 MapTypeFactory.constructCollectionType(List.class, String.class), null, null, null, null, null,
 null, null, valueDeserializer);
 Collection<String> result = MAPPER.readerForUpdating(new ArrayList<>()).readValue(node);
 assertEquals(Arrays.asList("", "one", "two"), result);
 }

 @Test
 void testStringCollectionWithValueDeserializer() throws IOException {
 JsonNode node = MAPPER.readTree("[\"one\", \"two\", \"three\"]");
 JsonDeserializer<String> valueDeserializer = new StringDeserializer();
 StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
 MapTypeFactory.constructCollectionType(List.class, String.class), null, null, null, null, null,
 null, null, valueDeserializer);
 Collection<String> result = MAPPER.readerForUpdating(new ArrayList<>()).readValue(node);
 assertEquals(Arrays.asList("one", "two", "three"), result);
 }

 @Test
 void testStringCollectionWithValueDeserializerAndNullValue() throws IOException {
 JsonNode node = MAPPER.readTree("[\"one\", null, \"three\"]");
 JsonDeserializer<String> valueDeserializer = new StringDeserializer();
 StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
 MapTypeFactory.constructCollectionType(List.class, String.class), null, null, null, null, null,
 null, null, valueDeserializer);
 Collection<String> result = MAPPER.readerForUpdating(new ArrayList<>()).readValue(node);
 assertEquals(Arrays.asList("one", null, "three"), result);
 }

 @Test
 void testStringCollectionWithValueDeserializerAndTypeDeserializer() throws IOException {
 JsonNode node = MAPPER.readTree("[\"one\", \"two\", \"three\"]");
 TypeDeserializer typeDeserializer = new SimpleTypeDeserializer();
 JsonDeserializer<String> valueDeserializer = new StringDeserializer();
 StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
 MapTypeFactory.constructCollectionType(List.class, String.class), null, null, null, null, null,
 typeDeserializer, null, valueDeserializer);
 Collection<String> result = MAPPER.readerForUpdating(new ArrayList<>()).readValue(node);
 assertEquals(Arrays.asList("one", "two", "three"), result);
 }

 @Test
 void testStringCollectionWithValueDeserializerAndContextualDeserializer() throws IOException {
 JsonNode node = MAPPER.readTree("[\"one\", \"two\", \"three\"]");
 final class StringUpperContextualDeserializer extends StdDeserializer<String> {
 @Override
 public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 String value = jp.getValueAsString();
 return value.toUpperCase();
 }
 }
 JsonDeserializer<String> valueDeserializer = new StringUpperContextualDeserializer();
 StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
 MapTypeFactory.constructCollectionType(List.class, String.class), null, null, null, null, null,
 null, new StringUpperContextualDeserializer(), valueDeserializer);
 Collection<String> result = MAPPER.readerForUpdating(new ArrayList<>()).readValue(node);
 assertEquals(Arrays.asList("ONE", "TWO", "THREE"), result);
 }

 @Test
 void testStringCollectionWithValueDeserializerAndCustomDeserializer() throws IOException {
 JsonNode node = MAPPER.readTree("[\"one\", \"two\", \"three\"]");
 final class StringToIntegerDeserializer extends StdDeserializer<String> {
 @Override
 public Integer deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 Integer value = Integer.parseInt(jp.getValueAsString());
 return value;
 }
 }
 JsonDeserializer<String> valueDeserializer = new StringToIntegerDeserializer();
 StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
 MapTypeFactory.constructCollectionType(List.class, Integer.class), null, null, null, null, null,
 null, null, valueDeserializer);
 Collection<Integer> result = MAPPER.readerForUpdating(new ArrayList<>()).readValue(node);
 assertEquals(Arrays.asList(1, 2, 3), result);
 }

 @Test
 void testStringCollectionWithValueDeserializerAndValueInstantiator() throws IOException {
 JsonNode node = MAPPER.readTree("[\"one\", \"two\", \"three\"]");
 final class MyClass {
 private String value;
 public MyClass(String value) {
 this.value = value;
 }
 }
 ValueInstantiator valueInstantiator = new ValueInstantiator() {
 @Override
 public Object createValueInstance(DeserializationContext ctxt) throws IOException {
 return new MyClass("default");
 }
 };
 JsonDeserializer<String> valueDeserializer = new StringDeserializer();
 StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
 MapTypeFactory.constructCollectionType(List.class, MyClass.class), valueInstantiator, null, null, null, null,
 null, null, valueDeserializer);
 Collection<MyClass> result = MAPPER.readerForUpdating(new ArrayList<>()).readValue(node);
 assertEquals(Arrays.asList(new MyClass("one"), new MyClass("two"), new MyClass("three")), result);
 }
}