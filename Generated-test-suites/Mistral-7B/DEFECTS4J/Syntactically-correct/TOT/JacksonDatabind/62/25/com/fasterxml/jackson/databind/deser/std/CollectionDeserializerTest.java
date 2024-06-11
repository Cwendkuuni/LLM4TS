package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.io.IOException;
import java.util.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CollectionDeserializerTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testEmptyArray() throws IOException {
 CollectionDeserializer deserializer = new CollectionDeserializer(
 mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
 null, null, null, null, null);
 JsonParser parser = mapper.getFactory().createParser("[]");
 Collection<String> result = deserializer.deserialize(parser, null);
 Assert.assertEquals(0, result.size());
 }

 @Test
 public void testSingleValue() throws IOException {
 CollectionDeserializer deserializer = new CollectionDeserializer(
 mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
 new StringDeserializer(), null, null, null, null);
 JsonParser parser = mapper.getFactory().createParser("['foo']");
 Collection<String> result = deserializer.deserialize(parser, null);
 Assert.assertEquals(1, result.size());
 Assert.assertEquals("foo", result.get(0));
 }

 @Test
 public void testValueAndEmptyArray() throws IOException {
 CollectionDeserializer deserializer = new CollectionDeserializer(
 mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
 new StringDeserializer(), null, null, null, null);
 JsonParser parser = mapper.getFactory().createParser("{'foo': 1, []}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Collection<String> result = deserializer.createContextual(ctxt, ctxt.findProperty("foo"));
 Assert.assertEquals(1, result.size());
 Assert.assertEquals("1", result.get(0));
 }

 @Test
 public void testValueTypeDeserializer() throws IOException {
 CollectionDeserializer deserializer = new CollectionDeserializer(
 mapper.getTypeFactory().constructCollectionType(ArrayList.class, Number.class),
 new StringDeserializer(), new NumberDeserializer(), null, null, null);
 JsonParser parser = mapper.getFactory().createParser("['1', '2.5', 3.0]");
 Collection<Number> result = deserializer.deserialize(parser, null);
 Assert.assertEquals(3, result.size());
 Assert.assertEquals(1.0, result.get(0).doubleValue());
 Assert.assertEquals(2.5, result.get(1).doubleValue());
 Assert.assertEquals(3.0, result.get(2).doubleValue());
 }

 @Test
 public void testValueInstantiator() throws IOException {
 CollectionDeserializer deserializer = new CollectionDeserializer(
 mapper.getTypeFactory().constructCollectionType(ArrayList.class, String[].class),
 null, null, new ValueInstantiatorImpl(), null, null);
 JsonParser parser = mapper.getFactory().createParser("['foo', 'bar']");
 Collection<String[]> result = deserializer.deserialize(parser, null);
 Assert.assertEquals(2, result.size());
 String[] first = result.get(0);
 Assert.assertEquals(2, first.length);
 Assert.assertEquals("foo", first[0]);
 Assert.assertEquals("bar", first[1]);
 }

 @Test
 public void testDelegateDeserializer() throws IOException {
 String[] initialValues = {"foo", "bar"};
 JsonDeserializer<String[]> delegateDeserializer = new StringArrayDeserializer();
 CollectionDeserializer deserializer = new CollectionDeserializer(
 mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
 null, null, null, delegateDeserializer, null);
 JsonParser parser = mapper.getFactory().createParser(mapper.writeValueAsString(initialValues));
 Collection<String> result = deserializer.deserialize(parser, null);
 Assert.assertEquals(2, result.size());
 Assert.assertEquals("foo", result.get(0));
 Assert.assertEquals("bar", result.get(1));
 }

 @Test
 public void testPolymorphicDeserialization() throws IOException {
 CollectionDeserializer deserializer = new CollectionDeserializer(
 mapper.getTypeFactory().constructCollectionType(ArrayList.class, Number.class),
 null, new NumberDeserializer(), null, null, null);
 JsonParser parser = mapper.getFactory().createParser("['1', 1.5, 'three']");
 Collection<Number> result = deserializer.deserialize(parser, null);
 Assert.assertEquals(3, result.size());
 Assert.assertEquals(1.0, result.get(0).doubleValue());
 Assert.assertEquals(1.5, result.get(1).doubleValue());
 Assert.assertEquals(3.0, result.get(2).doubleValue());
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingValueDeserializer() throws IOException {
 CollectionDeserializer deserializer = new CollectionDeserializer(
 mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
 null, null, null, null, null);
 JsonParser parser = mapper.getFactory().createParser("[]");
 deserializer.deserialize(parser, null);
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingTypeDeserializer() throws IOException {
 CollectionDeserializer deserializer = new CollectionDeserializer(
 mapper.getTypeFactory().constructCollectionType(ArrayList.class, Number.class),
 null, null, null, null, null);
 JsonParser parser = mapper.getFactory().createParser("['1', '2.5', 3.0]");
 deserializer.deserializeWithType(parser, null, null);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidJson() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("{}");
 CollectionDeserializer deserializer = new CollectionDeserializer(
 mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
 null, null, null, null, null);
 deserializer.deserialize(parser, null);
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidJson2() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("['foo', 'bar',]");
 CollectionDeserializer deserializer = new CollectionDeserializer(
 mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
 null, null, null, null, null);
 deserializer.deserialize(parser, null);
 }

 @Test
 public void testCustomDeserializer() throws IOException {
 CollectionDeserializer deserializer = new CollectionDeserializer(
 mapper.getTypeFactory().constructCollectionType(ArrayList.class, MyClass.class),
 new MyClassDeserializer(), null, null, null, null);
 JsonParser parser = mapper.getFactory().createParser("['1', '2']");
 Collection<MyClass> result = deserializer.deserialize(parser, null);
 Assert.assertEquals(2, result.size());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testContextualDeserializer() throws IOException {
 CollectionDeserializer deserializer = mapper.getFactory()
 .constructValueDeserializer(Collection.class, new TypeReference<Collection<Integer>>() {});
 JsonParser parser = mapper.getFactory().createParser("[1, 2, 3]");
 Collection<Integer> result = deserializer.deserialize(parser, null);
 Assert.assertEquals(3, result.size());
 Assert.assertEquals(1, result.get(0).intValue());
 Assert.assertEquals(2, result.get(1).intValue());
 Assert.assertEquals(3, result.get(2).intValue());
 }

}

class StringDeserializer extends JsonDeserializer<String> {
 @Override
 public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return p.getValueAsString();
 }

 @Override
 public TypeDeserializer getContentTypeDeserializer(DeserializationContext ctxt) throws JsonMappingException {
 return null;
 }
}

class NumberDeserializer extends JsonDeserializer<Number> {
 @Override
 public Number deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 String str = p.getValueAsString();
 if (str.startsWith("'") && str.endsWith("'")) {
 return new BigDecimal(str.substring(1, str.length() - 1));
 }
 return NumberUtils.parseNumber(str, NumberFormat.getNumberInstance());
 }

 @Override
 public TypeDeserializer getContentTypeDeserializer(DeserializationContext ctxt) throws JsonMappingException {
 return new NumberDeserializer();
 }
}

class StringArrayDeserializer extends JsonDeserializer<String[]> {
 @Override
 public String[] deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 List<String> lst = new ArrayList<>();
 JsonParser currentParser = p;
 while (!currentParser.isClosed()) {
 String str = currentParser.nextTextValue();
 if (str != null) {
 lst.add(str);
 }
 if (currentParser.hasToken(JsonToken.FIELD_NAME)) {
 currentParser.nextToken();
 }
 }
 return lst.toArray(new String[lst.size()]);
 }

 @Override
 public TypeDeserializer getContentTypeDeserializer(DeserializationContext ctxt) throws JsonMappingException {
 return new TypeDeserializer() {
 @Override
 public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt)
 throws JsonProcessingException {
 return new Object[] { p.nextTextValue() };
 }
 };
 }
}

class ValueInstantiatorImpl implements ValueInstantiator {
 @Override
 public Object createUsingDefault(DeserializationContext ctxt) {
 return new ArrayList<>();
 }

 @Override
 public Object createFromString(DeserializationContext ctxt, String value)
 throws IOException {
 String[] values = value.split(",");
 return new ArrayList<>(Arrays.asList(values));
 }
}