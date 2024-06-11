package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ObjectIdValuePropertyTest {

 private static final ObjectIdReader objectIdReader = new ObjectIdReader() {{
 setIdType(String.class);
 setGenerator(new StdSerializer<Object>(Object.class) {
 @Override public void serialize(Object id, JsonGenerator jg, SerializerProvider sp) throws IOException {
 jg.writeString(id.toString());
 }
 });
 setResolver(new ObjectIdResolver());
 }};

 @SuppressWarnings("unchecked")
 @Test
 public void testObjectIdValuePropertyCreation() throws IOException {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 assertEquals("objectId", property.getName());
 assertNull(property.getValueDeserializer());
 assertNull(property.getMember());
 assertNull(property.getAnnotation(ObjectId.class));
 }

 @Test
 public void testObjectIdValuePropertyWithValueDeserializer() throws IOException {
 ObjectIdReader objectIdReader = new ObjectIdReader() {{
 setIdType(UUID.class);
 setValueDeserializer(new FromStringDeserializer(UUID.class));
 }};
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 assertEquals(UUID.class, property.getIdType());
 assertNotNull(property.getValueDeserializer());
 }

 @Test
 public void testObjectIdValuePropertyWithName() throws IOException {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 property = property.withName(new PropertyName("newName"));
 assertEquals("newName", property.getName().getShortName());
 }

 @Test
 public void testObjectIdValuePropertyWithValueDeserializerAndName() throws IOException {
 ObjectIdReader objectIdReader = new ObjectIdReader() {{
 setIdType(UUID.class);
 setValueDeserializer(new FromStringDeserializer(UUID.class));
 }};
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 property = property.withName(new PropertyName("newName"));
 property = property.withValueDeserializer(new FromObjectDeserializer(Integer.class));
 assertEquals(Integer.class, property.getValueDeserializer().getClass());
 assertEquals("newName", property.getName().getShortName());
 }

 @Test
 public void testObjectIdValuePropertyDeserializeAndSet() throws IOException {
 ObjectNode node = new ObjectMapper().createObjectNode();
 node.put("objectId", "existingId");
 JsonParser jp = new ObjectMapper().getFactory().createParser(node.toString());
 Object id = objectIdReader.getValueDeserializer().deserialize(jp, null);
 Object instance = new Object();
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 property.deserializeAndSet(jp, null, instance);
 assertEquals("existingId", id);
 }

 @Test
 public void testObjectIdValuePropertyDeserializeSetAndReturn() throws IOException {
 ObjectNode node = new ObjectMapper().createObjectNode();
 node.put("objectId", "existingId");
 JsonParser jp = new ObjectMapper().getFactory().createParser(node.toString());
 Object instance = new Object();
 Object id = objectIdReader.getValueDeserializer().deserialize(jp, null);
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 Object result = property.deserializeSetAndReturn(jp, null, instance);
 assertEquals(instance, result);
 }

 @Test
 public void testObjectIdValuePropertySetAndReturn() throws IOException {
 Object instance = new Object();
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 Object id = UUID.randomUUID();
 property.setAndReturn(instance, id);
 assertNotNull(instance.getClass().getDeclaredField("objectId"));
 Field objectIdField = instance.getClass().getDeclaredField("objectId");
 objectIdField.setAccessible(true);
 assertEquals(id, objectIdField.get(instance));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testObjectIdValuePropertySet() throws IOException {
 Object instance = new Object();
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 property.set(instance, "newId");
 fail("Should not call set() on ObjectIdProperty that has no SettableBeanProperty");
 }
}