package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNodeShape;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;

 @Before
 public void setup() throws IOException {
 ObjectIdGenerator idGenerator = new ObjectIdGenerator();
 ObjectIdResolver idResolver = new ObjectIdResolver();
 objectIdReader = new ObjectIdReader("id", Object.class, null, IdType.INT, idGenerator, idResolver);
 BeanDeserializerBuilder builder = new BeanDeserializerBuilder(Object.class, new ObjectMapper());
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 objectIdValueProperty = (ObjectIdValueProperty) builder.addProperty("idProperty", new JsonIdentityProperty(), property).build();
 }

 @Test
 public void testGetAnnotation() {
 Annotation annotation = objectIdValueProperty.getAnnotation(JsonIdentityInfo.class);
 assertNull(annotation);
 }

 @Test
 public void testGetMember() {
 AnnotatedMember annotatedMember = objectIdValueProperty.getMember();
 assertNull(annotatedMember);
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
 node.put("id", 1L);
 JsonParser jsonParser = ObjectMapper.getDefaultInstance().getFactory().createParser(node);

 Object instance = new Object();
 objectIdValueProperty.deserializeAndSet(jsonParser, new DeserializationContext(), instance);
 assertEquals(1L, objectIdValueProperty.getMember().get(instance).longValue());
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
 node.put("id", 1L);
 Object id = objectIdValueProperty.deserializeSetAndReturn(ObjectMapper.getDefaultInstance().getFactory().createParser(node), new DeserializationContext(), new Object());
 assertEquals(1L, id.longValue());
 }

 @Test
 public void testSet() throws IOException {
 Object instance = new Object();
 objectIdValueProperty.set(instance, 1L);
 assertEquals(1L, objectIdValueProperty.getMember().get(instance).longValue());
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object instance = new Object();
 Object returnValue = objectIdValueProperty.setAndReturn(instance, 1L);
 assertEquals(1L, objectIdValueProperty.getMember().get(instance).longValue());
 assertEquals(instance, returnValue);
 }

 @Test
 public void testWithName() {
 ObjectIdValueProperty newProperty = objectIdValueProperty.withName(PropertyName.construct("newName"));
 assertEquals("newName", newProperty.getName().getName());
 }

 @Test
 public void testWithValueDeserializer() throws IOException {
 JsonDeserializer<Object> deserializer = new StandardDeserializer<>();
 ObjectIdValueProperty newProperty = objectIdValueProperty.withValueDeserializer(deserializer);
 assertEquals(deserializer, newProperty.getValueDeserializer());
 }

 @Test
 public void testSerialization() throws IOException {
 ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerModule(new SimpleModule().addSerializer(Object.class, new ToStringSerializer()));
 Object instance = new Object();
 mapper.setValue(instance, "id", 1L);
 ObjectNode result = mapper.writeValueAsObject(instance);
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 JsonSerializer<Object> serializer = property.getSerializer();
 JsonNode expected = node.getObject("idProperty");
 expected.put("value", 1L);
 expected.setShape(JsonNodeShape.OBJECT);
 JsonNode actual = serializer.serializeValues(mapper.valueToTree(instance));
 assertEquals(expected, actual);
 }

}

class ObjectIdGenerator implements ObjectIdGenerator {

 @Override
 public ObjectId generateId(SerializerProvider serializers) {
 return 1L;
 }

}

class ObjectIdResolver implements ObjectIdResolver {

 @Override
 public ObjectId idFromString(String id, DeserializationContext ctxt) {
 return new ObjectIdImpl(id.getBytes());
 }

}

class ObjectIdImpl implements ReadableObjectId {

 private final byte[] id;

 ObjectIdImpl(byte[] id) {
 this.id = id;
 }

 @Override
 public void bindItem(Object bean) {
 // Empty implementation
 }

 @Override
 public ObjectId idWithPrefix(String prefix) {
 return new ObjectIdImpl(prefix.getBytes() + id);
 }

 @Override
 public void bindValue(ObjectId id) {
 // Empty implementation
 }

 @Override
 public void bindValue(String id) {
 // Empty implementation
 }

 @Override
 public byte[] idBytes() {
 return id;
 }

}

class JsonIdentityProperty {

 @JsonIdentityInfo(generator = ObjectIdGenerators.IntArrayGenerator.class, property = "id")
 @JsonIdentityReference(alwaysAsId = true)
 public static class JsonIdentityPropertyHolder {
 @JsonIgnore
 private int id;

 public int getId() {
 return id;
 }

 public void setId(int id) {
 this.id = id;
 }
 }

}