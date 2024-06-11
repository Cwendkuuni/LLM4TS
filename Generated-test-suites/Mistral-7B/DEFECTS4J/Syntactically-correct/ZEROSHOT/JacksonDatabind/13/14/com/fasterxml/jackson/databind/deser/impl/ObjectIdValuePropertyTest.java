package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.StandardSerializer;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

public class ObjectIdValuePropertyTest {

 private ObjectMapper objectMapper;
 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;

 @Before
 public void setup() throws JsonProcessingException {
 objectMapper = new ObjectMapper();
 ObjectIdGenerator idGenerator = ObjectIdGenerators.IntGenerator.instance;
 objectIdReader = new ObjectIdReader(new PropertyName("id"), idGenerator);
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STANDARD_ITEM);
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object id = new Integer(1);
 JsonParser jp = objectMapper.getFactory().createParser("{ \"id\": " + id + " }");
 Object instance = new Object();
 objectIdValueProperty.deserializeAndSet(jp, objectMapper.getDeserializationContext(), instance);
 assertNotNull(objectIdValueProperty.getBean());
 assertEquals(id, objectIdValueProperty.getBean());
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object id = new Integer(1);
 JsonParser jp = objectMapper.getFactory().createParser("{ \"id\": " + id + " }");
 Object instance = new Object();
 Object returnValue = objectIdValueProperty.deserializeSetAndReturn(jp, objectMapper.getDeserializationContext(), instance);
 assertNotNull(objectIdValueProperty.getBean());
 assertEquals(id, objectIdValueProperty.getBean());
 assertEquals(instance, returnValue);
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object id = new Integer(1);
 Object instance = new Object();
 objectIdValueProperty.setAndReturn(instance, id);
 assertNotNull(objectIdValueProperty.getBean());
 assertEquals(id, objectIdValueProperty.getBean());
 }

 @Test
 public void testAnnotations() {
 Annotation annotation = objectIdValueProperty.getAnnotation(ObjectIdGenerator.class);
 assertNull(annotation);
 }

 @Test
 public void testGetMember() {
 AnnotatedMember annotatedMember = objectIdValueProperty.getMember();
 assertNull(annotatedMember);
 }

 @Test
 public void testSerialize() throws JsonProcessingException {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STANDARD_ITEM);
 Object id = new Integer(1);
 property.setAndReturn(new Object(), id);
 SimpleBeanPropertyFilter filter = new SimpleBeanPropertyFilter("*", null, false);
 JsonSerializer<ObjectIdValueProperty> serializer = new StandardSerializer<>(ObjectIdValueProperty.class);
 JsonGenerator jg = objectMapper.getFactory().createGenerator();
 jg.setSerializerFilter(filter);
 jg.writeStartObject();
 property.serialize(jg, objectMapper.getSerializationConfig(), null);
 jg.writeEndObject();
 String json = jg.getOutputContent();
 JsonParser jp = objectMapper.getFactory().createParser(json);
 Object result = objectMapper.readValue(jp, Object.class);
 assertEquals(id, result);
 }
}