package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.base.PropertyName;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.ClassKey;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Test;

public class ObjectIdValuePropertyTest {

 private static final ObjectIdReader objectIdReader = new ObjectIdReader() {
  @Override
  public PropertyName propertyName() {
   return new PropertyName("objectId");
  }

  @Override
  public Class<?> idType() {
   return Long.class;
  }

  @Override
  public JsonDeserializer<?> getIdDeserializer() {
   return new None();
  }

  @Override
  public ObjectIdGenerator idGenerator() {
   // NOP
   return null;
  }

  @Override
  public ObjectIdResolver idResolver() {
   // NOP
   return null;
  }

  @Override
  public SettableBeanProperty idProperty() {
   return null;
  }
 };

 @Test
 public void testObjectIdValuePropertyConstruction() throws IOException {
  ObjectMapper mapper = new ObjectMapper();

  ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
  assertEquals("objectId", property.getName());
  assertEquals(Long.class, property.getType());
  assertNull(property.getAnnotations());
  assertNull(property.getMember());
  assertNull(property.getBeanPropertyDefinition());
  assertNull(property.getValueDeserializer());
  assertNull(property._objectIdReader);

  ObjectIdValueProperty copy = new ObjectIdValueProperty(property, new None<>());
  assertEquals(property, copy);
 }

 @Test
 public void testObjectIdValuePropertySetAndGet() throws IOException {
  ObjectMapper mapper = new ObjectMapper();
  Object id = 1L;

  ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
  assertEquals(null, property.get(null));

  Object testInstance = mapper.readValue("[1]", Object.class);
  property.setAndReturn(testInstance, id);
  assertEquals(id, property.get(testInstance));
 }

 @Test
 public void testObjectIdValuePropertyDeserialize() throws IOException {
  ObjectMapper mapper = new ObjectMapper();

  ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

  AnnotatedClass ac = mapper.getSerializationConfig().getIntrospector().findClassAnnotations(Object.class, null);
  AnnotatedMember member = ac.findMember("objectId");
  BeanPropertyDefinition bpd = AnnotationIntrospector.findPropertyDefinition(member, mapper.getSerializationConfig());
  property.setValueDeserializer(bpd.getNullable(JsonDeserializer.class));

  Object testInstance = mapper.readValue("[1, null]", Object[].class)[0];
  Object id = 1L;

  property.deserializeAndSet(mapper.getFactory().createParser("1"), mapper.getDeserializationContext(), testInstance);
  assertEquals(id, property.get(testInstance));
 }

 @Test
 public void testObjectIdValuePropertyDeserializeSetAndReturn() throws IOException {
  ObjectMapper mapper = new ObjectMapper();

  ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

  AnnotatedClass ac = mapper.getSerializationConfig().getIntrospector().findClassAnnotations(Object.class, null);
  AnnotatedMember member = ac.findMember("objectId");
  BeanPropertyDefinition bpd = AnnotationIntrospector.findPropertyDefinition(member, mapper.getSerializationConfig());
  property.setValueDeserializer(bpd.getNullable(JsonDeserializer.class));

  Object testInstance = mapper.readValue("[1, null]", Object[].class)[0];
  Object id = 1L;

  Object deserializedId = property.deserializeSetAndReturn(mapper.getFactory().createParser("1"), mapper.getDeserializationContext(), testInstance);
  assertEquals(id, deserializedId);
 }
}