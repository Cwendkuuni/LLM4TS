package com.example.json; // replace with your test package name

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JsonValueSerializerTest {

 @Test
 public void testJsonValueSerializerSimple() throws JsonMappingException, IOException {
 Class<MyClass> clazz = MyClass.class;
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 MyClass obj = new MyClass();
 obj.setValue(42);
 String json = mapper.writeValueAsString(obj);
 ObjectNode node = mapper.readValue(json, ObjectNode.class);
 assertEquals(42, node.get("value").asInt());
 }

 @Test
 public void testJsonValueSerializerWithExplicitValueSerializer() throws JsonMappingException, IOException {
 Class<MyClass> clazz = MyClass.class;
 Class<StringSerializer> stringSerializerClass = StringSerializer.class;
 AnnotatedMethod accessorMethod = MyClass.class.getMethod("getValue");
 JsonSerializer<Object> valueSerializer = new StringSerializer();
 JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(accessorMethod, valueSerializer);
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 MyClass obj = new MyClass();
 obj.setValue("hello");
 String json = mapper.writeValueAsString(obj, jsonValueSerializer);
 ObjectNode node = mapper.readValue(json, ObjectNode.class);
 assertEquals("hello", node.get("value").asText());
 }

 @Test
 public void testJsonValueSerializerWithContextualValueSerializer() throws JsonMappingException, IOException {
 Class<MyClass> clazz = MyClass.class;
 Class<StringSerializer> stringSerializerClass = StringSerializer.class;
 AnnotatedMethod accessorMethod = MyClass.class.getMethod("getValue");
 JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(accessorMethod, null, null, false);
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 MyClass obj = new MyClass();
 obj.setValue("hello");
 JsonSerializer<Object> valueSerializer = mapper.findValueSerializer(String.class, null);
 JsonValueSerializer contextualJsonValueSerializer = jsonValueSerializer.withResolved(null, valueSerializer, false);
 String json = mapper.writeValueAsString(obj, contextualJsonValueSerializer);
 ObjectNode node = mapper.readValue(json, ObjectNode.class);
 assertEquals("hello", node.get("value").asText());
 }

 @Test
 public void testJsonValueSerializerWithForceTypeInformation() throws JsonMappingException, IOException {
 Class<MyClass> clazz = MyClass.class;
 Class<StringSerializer> stringSerializerClass = StringSerializer.class;
 AnnotatedMethod accessorMethod = MyClass.class.getMethod("getValue");
 JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(accessorMethod, null, null, true);
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 MyClass obj = new MyClass();
 obj.setValue("hello");
 String json = mapper.writeValueAsString(obj, jsonValueSerializer);
 ObjectNode node = mapper.readValue(json, ObjectNode.class);
 Object actual = mapper.readValue(node.toString(), TypeFactory.defaultInstance().constructType(String.class));
 assertEquals("hello", actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testJsonValueSerializerWithNullValue() throws JsonMappingException, IOException {
 Class<MyClass> clazz = MyClass.class;
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 MyClass obj = new MyClass();
 ObjectNode node = mapper.createObjectNode();
 mapper.writerWithDefaultPrettyPrinter().writeValue(node, obj);
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
 assertEquals("", json);
 }

 @Test
 public void testJsonValueSerializerWithException() throws JsonMappingException, IOException {
 Class<MyClass> clazz = MyClass.class;
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 MyClass obj = new MyClass();
 try {
 obj.setValue(new RuntimeException("boom"));
 } catch (Exception e) {
 // suppress
 }
 String json = mapper.writeValueAsString(obj);
 ObjectNode node = mapper.readValue(json, ObjectNode.class);
 assertNotNull(node.get("value"));
 assertTrue(node.get("value").isNull());
 }

}

class MyClass {
 @JsonValue
 public Object getValue() {
 return value;
 }

 public void setValue(Object value) {
 this.value = value;
 }

 private Object value;
}

class StringSerializer extends StdSerializer<String> {
 @Override
 public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
 gen.writeString(value);
 }
}