package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ObjectIdValuePropertyTest {

 private static ObjectMapper mapper;
 private static final ObjectIdReader objectIdReader = new StandardObjectIdReader();

 static {
 try {
 mapper = new ObjectMapper();
 mapper.registerModule(new SimpleModule().addDeserializer(ObjectIdValueProperty.class, new ObjectIdValuePropertyDeserializer()));
 mapper.setAnnotationIntrospector(new AnnotationIntrospector());
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @org.junit.Test
 public void testObjectIdValuePropertyCreation() throws IOException {
 PropertyName propertyName = new PropertyName("id");
 BeanPropertyDefinition idPropertyDefinition = new BeanPropertyDefinition("id", Object.class, null);
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new ObjectIdValueProperty.Builder()
 .name(propertyName)
 .idType(Object.class)
 .propertyMetadata(new StandardAnnotationIntrospector().findPropertyMetadata(BeanPropertyDefinition.class, idPropertyDefinition, null))
 .deserializer(new StdSerializer<Object>(Object.class) {})
 .build());

 assertEquals("id", property.getName());
 assertEquals(Object.class, property.getType());
 assertNotNull(property.getMetadata());
 assertSame(objectIdReader, property._objectIdReader);
 }

 @org.junit.Test
 public void testDeserializationHappyPath() throws IOException {
 Object idValue = new Integer(123);
 String json = mapper.writeValueAsString(new ObjectIdValueProperty.Builder()
 .name(new PropertyName("id"))
 .idType(Integer.class)
 .propertyMetadata(new StandardAnnotationIntrospector().findPropertyMetadata(BeanPropertyDefinition.class, null, null))
 .deserializer(new StdSerializer<Object>(Integer.class) {})
 .build());

 JsonParser parser = mapper.getFactory().createParser(json);
 Object idInstance = new Object();
 Object id = property.deserializeAndSet(parser, mapper.getDeserializationContext(), idInstance);
 assertEquals(idValue, id);
 // Verify that the object id was set correctly
 Object idProperty = mapper.getIntrospector().findPropertyValue(idInstance, "id");
 assertEquals(idValue, idProperty);
 }

 @org.junit.Test
 public void testDeserializationNullId() throws IOException {
 String json = mapper.writeValueAsString(new ObjectIdValueProperty.Builder()
 .name(new PropertyName("id"))
 .idType(Object.class)
 .propertyMetadata(new StandardAnnotationIntrospector().findPropertyMetadata(BeanPropertyDefinition.class, null, null))
 .deserializer(new StdSerializer<Object>(Object.class) {})
 .build());

 JsonParser parser = mapper.getFactory().createParser(json);
 Object idInstance = new Object();
 Object id = property.deserializeAndSet(parser, mapper.getDeserializationContext(), idInstance);
 assertNull(id);
 // Verify that the object id was not set
 Object idProperty = mapper.getIntrospector().findPropertyValue(idInstance, "id");
 assertNull(idProperty);
 }

 @org.junit.Test
 public void testDeserializationInvalidIdType() throws IOException {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new ObjectIdValueProperty.Builder()
 .name(new PropertyName("id"))
 .idType(String.class)
 .propertyMetadata(new StandardAnnotationIntrospector().findPropertyMetadata(BeanPropertyDefinition.class, null, null))
 .deserializer(new StdSerializer<Object>(Object.class) {})
 .build());

 String json = mapper.writeValueAsString(new String("123"));
 JsonParser parser = mapper.getFactory().createParser(json);
 Object idInstance = new Object();
 try {
 property.deserializeAndSet(parser, mapper.getDeserializationContext(), idInstance);
 fail("Should have thrown an exception");
 } catch (JsonMappingException e) {
 assertEquals("Not a valid Object Id: [123]", e.getMessage());
 }
 // Verify that the object id was not set
 Object idProperty = mapper.getIntrospector().findPropertyValue(idInstance, "id");
 assertNull(idProperty);
 }

 @org.junit.Test
 public void testSetValue() throws IOException {
 Object idInstance = new Object();
 Object idValue = new Integer(123);
 property.setAndReturn(idInstance, idValue);
 assertEquals(idValue, mapper.getIntrospector().findPropertyValue(idInstance, "id"));
 }

 @org.junit.Test
 public void testSetNullValue() throws IOException {
 Object idInstance = new Object();
 property.setAndReturn(idInstance, null);
 assertNull(mapper.getIntrospector().findPropertyValue(idInstance, "id"));
 }
}

class ObjectIdValuePropertyDeserializer extends StdDeserializer<ObjectIdValueProperty> {

 public ObjectIdValuePropertyDeserializer() {
 this(null);
 }

 public ObjectIdValuePropertyDeserializer(Class<?> vc) {
 super(vc);
 }

 @Override
 public ObjectIdValueProperty deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new ObjectIdValueProperty.Builder()
 .name(jp.getName())
 .idType(ctxt.getTypeFactory().constructType(jp.nextValue().getValueAsString()))
 .propertyMetadata(new StandardAnnotationIntrospector().findPropertyMetadata(BeanPropertyDefinition.class, null, null))
 .deserializer(this)
 .build());
 return property;
 }
}