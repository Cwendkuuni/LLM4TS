package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SettableAnyPropertyTest {

 private ObjectMapper mapper;
 private SettableAnyProperty property;

 @BeforeAll
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 property = mapper.findInjectableValue("com.fasterxml.jackson.databind.deser.SettableAnyProperty",
 MapperFeature.AUTO_DETECT_TYPES);
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 ObjectNode input = mapper.createObjectNode();
 input.put("name", "John Doe");
 ObjectNode instance = mapper.createObjectNode();

 property.deserializeAndSet(mapper.parserForTesting(), mapper.deserializationContext(), instance, "age");
 property.deserializeAndSet(mapper.parserForTesting(), mapper.deserializationContext(), instance, "address");

 assertEquals(2, instance.size());
 assertEquals("John Doe", instance.get("name").asText());
 assertEquals(42, instance.get("age").asInt());
 ObjectNode address = mapper.readerForMap().readValue(mapper.readTree("{\"street\": \"Main St\", \"city\": \"Anytown\"}"), ObjectNode.class);
 property.deserializeAndSet(mapper.parserForTesting(), mapper.deserializationContext(), instance, "address");
 assertEquals(3, instance.size());
 assertEquals(address, instance.get("address"));
 }

 @Test
 public void testDeserializeNullValue() throws Exception {
 ObjectNode input = mapper.createObjectNode();
 ObjectNode instance = mapper.createObjectNode();
 property.deserializeAndSet(mapper.parserForTesting(), mapper.deserializationContext(), instance, "nullValue");
 assertEquals(0, instance.size());
 }

 @Test
 public void testDeserializeType() throws Exception {
 ObjectNode input = mapper.createObjectNode();
 input.put("value", 42);
 JavaType javaType = mapper.getTypeFactory().constructType(int[].class);
 property = mapper.findInjectableValue("com.fasterxml.jackson.databind.deser.SettableAnyProperty",
 MapperFeature.AUTO_DETECT_TYPES)
 .withValueDeserializer(mapper.getTypeDeserializerInstance(JsonDeserializer.None.class));
 ObjectNode instance = mapper.createObjectNode();
 property.deserializeAndSet(mapper.parserForTesting(), mapper.deserializationContext(), instance, "list");
 assertEquals(1, instance.size());
 assertArrayEquals(new int[]{42}, instance.get("list").asText().split(",")
 .mapToInt(i -> Integer.parseInt(i)).toArray());
 }

 @Test
 public void testKeyDeserializer() throws Exception {
 ObjectNode input = mapper.createObjectNode();
 input.put("name", "John Doe");
 input.put("age", 42);
 ObjectNode instance = mapper.createObjectNode();
 KeyDeserializer keyDeserializer = mapper.getTypeDeserializerInstance(NameKeyDeserializer.class);
 property = mapper.findInjectableValue("com.fasterxml.jackson.databind.deser.SettableAnyProperty",
 MapperFeature.AUTO_DETECT_TYPES)
 .withKeyDeserializer(keyDeserializer);
 property.deserializeAndSet(mapper.parserForTesting(), mapper.deserializationContext(), instance, "name");
 property.deserializeAndSet(mapper.parserForTesting(), mapper.deserializationContext(), instance, "age");
 assertEquals(2, instance.size());
 assertEquals("John Doe", instance.get("name").asText());
 assertEquals(42, instance.get("age").asInt());
 }

 @Test
 public void testUnresolvedForwardReference() throws Exception {
 ObjectNode input = mapper.createObjectNode();
 input.put("name", "John Doe");
 ObjectNode instance = mapper.createObjectNode();
 property = mapper.findInjectableValue("com.fasterxml.jackson.databind.deser.SettableAnyProperty",
 MapperFeature.AUTO_DETECT_TYPES);
 property.deserializeAndSet(mapper.parserForTesting(), mapper.deserializationContext(), instance, "age");
 UnresolvedForwardReference reference = new UnresolvedForwardReference();
 assertEquals(1, instance.size());
 assertThrows(JsonMappingException.class, () -> property.deserializeAndSet(mapper.parserForTesting(), mapper.deserializationContext(), instance, "age"));
 }

 @AfterAll
 public void teardown() throws Exception {
 mapper.close();
 }
}

class NameKeyDeserializer extends KeyDeserializer {
 @Override
 public Object deserializeKey(String key, DeserializationContext ctxt)
 throws IOException {
 return new String(key.getBytes("UTF-8"));
 }
}