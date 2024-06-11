package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

 private ObjectMapper mapper;
 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty property;

 @Before
 public void setup() throws Exception {
 ObjectNode config = mapper.createObjectNode();
 config.put("MapperJars", "test-jackson-databind.jar");
 mapper = new ObjectMapper();
 mapper.registerModule(new AnnotationIntrospector());
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
 mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
 mapper.registerDeserializer(JsonNode.class, new JsonNodeDeserializer());
 objectIdReader = new ObjectIdReader("idProperty", JsonDeserializers.stringDeserializer(), null);
 property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 JsonNode node = mapper.readTree("{\"id\":1}");
 Object id = node.path("id").asInt();
 Object instance = new Object();
 property.deserializeAndSet(mapper.parserForObject(node), mapper.getContext(mapper.getFactory()), instance);
 assertEquals(id, property.get(instance));
 }

 @Test
 public void testDeserializeAndSetNull() throws IOException {
 JsonNode node = mapper.readTree("{}");
 Object instance = new Object();
 property.deserializeAndSet(mapper.parserForObject(node), mapper.getContext(mapper.getFactory()), instance);
 assertNull(property.get(instance));
 }

 @Test
 public void testDeserializeAndSetInvalidType() throws IOException {
 JsonNode node = mapper.readTree("{\"id\":\"invalid\"}");
 Object instance = new Object();
 try {
 property.deserializeAndSet(mapper.parserForObject(node), mapper.getContext(mapper.getFactory()), instance);
 fail();
 } catch (Exception e) {
 assertTrue(e instanceof JsonMappingException);
 }
 }

 @Test
 public void testSet() throws IOException {
 Object id = 1;
 Object instance = new Object();
 property.set(instance, id);
 assertEquals(id, property.get(instance));
 }

 @Test
 public void testSetNull() throws IOException {
 Object instance = new Object();
 property.set(instance, null);
 assertNull(property.get(instance));
 }

 @Test
 public void testSetInvalidType() throws IOException {
 Object instance = new Object();
 try {
 property.set(instance, new Object());
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 @JsonDeserialize(using = JsonDeserializers.StringDeserializer.class)
 public static class TestObject {
 private int id;

 public int getId() {
 return id;
 }
 }

 @Test
 public void testDeserializeAndSetIdProperty() throws IOException {
 JsonNode node = mapper.readTree("{\"id\":1}");
 TestObject instance = mapper.readValue(node.traverse(), TestObject.class);
 assertEquals(1, instance.getId());
 }
}