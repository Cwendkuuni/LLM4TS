package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;

public class StringCollectionDeserializerTest {

 private ObjectMapper mapper;
 private JsonFactory jsonFactory;
 private StringCollectionDeserializer deserializer;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 jsonFactory = mapper.getFactory();
 deserializer = new StringCollectionDeserializer(mapper.getTypeFactory().constructCollectionType(Collection.class, String.class), null, null, null, null, null);
 }

 @Test
 public void testDeserializeEmptyArray() throws IOException {
 Collection<String> result = deserializer.deserialize(jsonFactory.createParser("[]"), new TestContext());
 assertEquals(Collections.emptyList(), result);
 }

 @Test
 public void testDeserializeSingleElementArray() throws IOException {
 Collection<String> result = deserializer.deserialize(jsonFactory.createParser("[\"\"]"), new TestContext());
 assertEquals(Collections.singletonList(""), result);
 }

 @Test
 public void testDeserializeArrayWithElements() throws IOException {
 Collection<String> result = deserializer.deserialize(jsonFactory.createParser("[\":one\",\"two\",\"three\"]"), new TestContext());
 assertEquals(Arrays.asList("", "one", "two", "three"), result);
 }

 @Test
 public void testDeserializeNullValue() throws IOException {
 Collection<String> result = deserializer.deserialize(jsonFactory.createParser("null"), new TestContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeEmptyString() throws IOException {
 Collection<String> result = deserializer.deserialize(jsonFactory.createParser("[\"\"]"), new TestContext());
 assertEquals(Collections.singletonList(""), result);
 }

 @Test
 public void testDeserializeEmptyStringWithOtherValues() throws IOException {
 Collection<String> result = deserializer.deserialize(jsonFactory.createParser("[\"\",\"one\",\"two\",\"three\"]"), new TestContext());
 assertEquals(Arrays.asList("", "", "one", "two", "three"), result);
 }

 @Test
 public void testDeserializeNullDeserializer() throws IOException {
 JsonDeserializer<String> nullValueDeserializer = null;
 StringCollectionDeserializer deserializerWithNullDeserializer = new StringCollectionDeserializer(mapper.getTypeFactory().constructCollectionType(Collection.class, String.class), null, null, null, nullDeserializer, null);
 assertThrows(NullPointerException.class, () -> deserializerWithNullDeserializer.deserialize(jsonFactory.createParser("[\":one\",\"two\",\"three\"]"), new TestContext()));
 }

 @Test
 public void testDeserializeWithValueDeserializer() throws IOException {
 JsonDeserializer<String> stringDeserializer = new StringDeserializer();
 StringCollectionDeserializer deserializerWithValueDeserializer = new StringCollectionDeserializer(mapper.getTypeFactory().constructCollectionType(Collection.class, String.class), null, null, null, stringDeserializer, null);
 Collection<String> result = deserializerWithValueDeserializer.deserialize(jsonFactory.createParser("[\":one\",\"two\",\"three\"]"), new TestContext());
 assertEquals(Arrays.asList("one", "two", "three"), result);
 }

 @Test
 public void testDeserializeWithValueDeserializerNullValue() throws IOException {
 JsonDeserializer<String> stringDeserializer = new StringDeserializer();
 StringCollectionDeserializer deserializerWithValueDeserializer = new StringCollectionDeserializer(mapper.getTypeFactory().constructCollectionType(Collection.class, String.class), null, null, null, stringDeserializer, null);
 Collection<String> result = deserializerWithValueDeserializer.deserialize(jsonFactory.createParser("null"), new TestContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeWithNullValueProvider() throws IOException {
 StringCollectionDeserializer deserializerWithNullValueProvider = new StringCollectionDeserializer(mapper.getTypeFactory().constructCollectionType(Collection.class, String.class), null, null, null, null, null, new NullValueProvider() {
 @Override
 public Object getNullValue(DeserializationContext ctxt) {
 return "nullValue";
 }
 });
 Collection<String> result = deserializerWithNullValueProvider.deserialize(jsonFactory.createParser("null"), new TestContext());
 assertEquals(Collections.singletonList("nullValue"), result);
 }

 @Test
 public void testDeserializeWithCustomDeserializer() throws IOException {
 StringCollectionDeserializer deserializerWithCustomDeserializer = new StringCollectionDeserializer(mapper.getTypeFactory().constructCollectionType(Collection.class, String.class), null, null, new CustomDeserializer(), null, null);
 Collection<String> result = deserializerWithCustomDeserializer.deserialize(jsonFactory.createParser("[\":one\",\"two\",\"three\"]"), new TestContext());
 assertEquals(Arrays.asList("CUSTOM:one", "CUSTOM:two", "CUSTOM:three"), result);
 }

 @Test
 public void testDeserializeWithDelegateDeserializer() throws IOException {
 JsonDeserializer<Object> delegateDeserializer = new BaseDeserializer();
 StringCollectionDeserializer deserializerWithDelegateDeserializer = new StringCollectionDeserializer(mapper.getTypeFactory().constructCollectionType(Collection.class, String.class), null, delegateDeserializer, null, null, null);
 Collection<String> result = deserializerWithDelegateDeserializer.deserialize(jsonFactory.createParser("[\":one\",\"two\",\"three\"]"), new TestContext());
 assertEquals(Arrays.asList("one", "two", "three"), result);
 }

 @Test
 public void testDeserializeWithWrongType() throws IOException {
 StringCollectionDeserializer deserializer = new StringCollectionDeserializer(mapper.getTypeFactory().constructCollectionType(Collection.class, Integer.class), null, null, null, null, null);
 assertThrows(JsonMappingException.class, () -> deserializer.deserialize(jsonFactory.createParser("[\":one\",\"two\",\"three\"]"), new TestContext()));
 }

 @Test
 public void testDeserializeWithUnwrappedSingleValue() throws IOException {
 StringCollectionDeserializer deserializerWithUnwrappedSingleValue = new StringCollectionDeserializer(mapper.getTypeFactory().constructCollectionType(Collection.class, String.class), null, null, null, null, Boolean.TRUE);
 Collection<String> result = deserializerWithUnwrappedSingleValue.deserialize(jsonFactory.createParser("[\":one\"]"), new TestContext());
 assertEquals(Collections.singletonList("one"), result);
 }

 @Test
 public void testDeserializeWithUnwrappedSingleValueAndOtherValues() throws IOException {
 StringCollectionDeserializer deserializerWithUnwrappedSingleValue = new StringCollectionDeserializer(mapper.getTypeFactory().constructCollectionType(Collection.class, String.class), null, null, null, null, Boolean.TRUE);
 Collection<String> result = deserializerWithUnwrappedSingleValue.deserialize(jsonFactory.createParser("[\":one\",\"two\",\"three\"]"), new TestContext());
 assertEquals(Arrays.asList("one", "two", "three"), result);
 }

 private static class TestContext extends DeserializationContext {
 @Override
 public ObjectReader getContextualValueFactory() {
 return mapper.getFactory();
 }
 }

 private static class StringDeserializer extends StdDeserializer<String> {
 @Override
 public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return p.getValueAsString();
 }
 }

 private static class CustomDeserializer extends JsonDeserializer<String> {
 @Override
 public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return "CUSTOM:" + p.getValueAsString();
 }
 }

 private static class BaseDeserializer extends JsonDeserializer<Object> {
 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return p.getValueAsString();
 }
 }

}