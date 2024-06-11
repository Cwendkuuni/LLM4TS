package com.example.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.test.BaseTest;

@JacksonStdImpl
public class CollectionDeserializerTest extends BaseTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testCollectionDeserializer() throws Exception {
 CollectionDeserializer deserializer = new CollectionDeserializer(
 Map.class,
 new ObjectMapper().findValueDeserializer(Object.class, null),
 null,
 null,
 null,
 null);

 // Test empty collection
 String jsonEmpty = "[]";
 Collection<Object> result = mapper.readValue(jsonEmpty, deserializer.getType());
 assertEquals(Collections.emptyList(), result);

 // Test empty collection with null elements
 String jsonEmptyWithNull = "[null]";
 result = mapper.readValue(jsonEmptyWithNull, deserializer.getType());
 assertEquals(Arrays.asList(new Object[] { null }), result);

 // Test collection with single element
 String jsonSingle = "[1]";
 result = mapper.readValue(jsonSingle, deserializer.getType());
 assertEquals(Collections.singletonList(1), result);

 // Test collection with multiple elements
 String jsonMultiple = "[1, 2, 3]";
 result = mapper.readValue(jsonMultiple, deserializer.getType());
 assertEquals(Arrays.asList(1, 2, 3), result);

 // Test collection with polymorphic elements
 String jsonPolymorphic = "[1, {\"number\": 2, \"type\": \"NUMBER\"}, 3]";
 TypeDeserializer valueTypeDeserializer = new StandardSerializer<Object>(Object.class) {
 @Override
 public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
 throws IOException {
 Object obj = super.deserializeWithType(p, ctxt, null);
 if (obj instanceof Number) {
 return new NumberDeserializer().deserialize(p, ctxt, JsonType.constructUnsafe(Number.class));
 } else {
 return obj;
 }
 }
 };
 CollectionDeserializer deserializerPolymorphic = new CollectionDeserializer(
 Map.class,
 null,
 valueTypeDeserializer,
 null,
 null,
 null);
 result = mapper.readValue(jsonPolymorphic, deserializerPolymorphic.getType());
 assertEquals(Arrays.asList(1, new HashMap<String, Object>() {{
 put("number", 2);
 put("type", "NUMBER");
 }}, 3), result);
 }

 @Test
 public void testCollectionDeserializer_withDelegateDeserializer() throws Exception {
 Class<List<String>> listType = List.class.getType();
 typeOf(CollectionDeserializer.class).getConstructor(Map.class,
 ObjectMapper.class, String[].class, ValueInstantiator.class,
 JsonDeserializer.class, Boolean.TYPE)
 .newInstance(Map.class, mapper, new String[0].getClass(), new ValueInstantiator() {
 @Override
 public Object createUsingDefault(DeserializationContext ctxt)
 throws IOException, JsonProcessingException {
 return new ArrayList<String>();
 }
 }, new StringDeserializer(), null);

 String jsonDelegate = "[\"element1\", \"element2\"]";
 Object result = mapper.readValue(jsonDelegate, listType);
 assertEquals(Arrays.asList("element1", "element2"), result);
 }

 @Test
 public void testCollectionDeserializer_withValueDeserializerAndTypeDeserializer() throws Exception {
 JsonDeserializer<Integer> integerDeserializer = new StandardDeserializer<Integer>(Integer.class);
 TypeDeserializer integerTypeDeserializer = new StandardSerializers.IntegerArrayTypeDeserializer();
 CollectionDeserializer deserializerInt = new CollectionDeserializer(
 List.class,
 integerDeserializer,
 integerTypeDeserializer,
 null,
 null,
 null);

 String jsonInt = "[1, 2, 3]";
 List<Integer> result = mapper.readValue(jsonInt, deserializerInt.getType());
 assertEquals(Arrays.asList(1, 2, 3), result);
 }

 @Test
 public void testCollectionDeserializer_withValueDeserializer() throws Exception {
 JsonDeserializer<Integer> integerDeserializer = new StandardDeserializer<Integer>(Integer.class);
 CollectionDeserializer deserializerInt = new CollectionDeserializer(
 List.class,
 integerDeserializer,
 null,
 null,
 null,
 null);

 String jsonInt = "[1, 2, 3]";
 List<Integer> result = mapper.readValue(jsonInt, deserializerInt.getType());
 assertEquals(Arrays.asList(1, 2, 3), result);
 }

 @Test
 public void testCollectionDeserializer_withValueInstantiator() throws Exception {
 ValueInstantiator valueInstantiator = new StandardDeserializationProbe()
 .findValueInstantiator(Map.class, null);
 CollectionDeserializer deserializer = new CollectionDeserializer(
 List.class,
 null,
 null,
 valueInstantiator,
 null,
 null);

 String jsonValueInstantiator = "[\"value1\", \"value2\"]";
 Object result = mapper.readValue(jsonValueInstantiator, deserializer.getType());
 assertEquals(Arrays.asList("value1", "value2"), result);
 }
}