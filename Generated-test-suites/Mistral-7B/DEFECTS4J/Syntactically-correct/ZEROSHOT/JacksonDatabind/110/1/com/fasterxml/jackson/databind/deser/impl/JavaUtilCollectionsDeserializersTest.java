package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

 private static JavaType getTestType(int type, Class<?> rawType) {
 return TypeFactory.defaultInstance().constructType(new TypeReference<Collection<?>>(){}.getType(), rawType);
 }

 @Test
 public void testFindForCollection_SingletonSet() throws JsonMappingException {
 DeserializationContext ctxt = new DeserializationContext(new AnnotationIntrospector(), new StandardSerializerProvider(), null);
 JavaType type = getTestType(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, Set.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
 assertNotNull(deserializer);
 StdDeserializer stdDeserializer = (StdDeserializer) deserializer;
 assertEquals(Set.class, stdDeserializer.getContentClass());
 }

 @Test
 public void testFindForCollection_SingletonList() throws JsonMappingException {
 DeserializationContext ctxt = new DeserializationContext(new AnnotationIntrospector(), new StandardSerializerProvider(), null);
 JavaType type = getTestType(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, List.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
 assertNotNull(deserializer);
 StdDeserializer stdDeserializer = (StdDeserializer) deserializer;
 assertEquals(List.class, stdDeserializer.getContentClass());
 }

 @Test
 public void testFindForCollection_SingletonMap() throws JsonMappingException {
 DeserializationContext ctxt = new DeserializationContext(new AnnotationIntrospector(), new StandardSerializerProvider(), null);
 JavaType type = getTestType(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, Map.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
 assertNotNull(deserializer);
 StdDeserializer stdDeserializer = (StdDeserializer) deserializer;
 assertEquals(Map.class, stdDeserializer.getContentClass());
 }

 @Test
 public void testFindForCollection_UnmodifiableSet() throws JsonMappingException {
 DeserializationContext ctxt = new DeserializationContext(new AnnotationIntrospector(), new StandardSerializerProvider(), null);
 JavaType type = getTestType(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, Set.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
 assertNotNull(deserializer);
 StdDeserializer stdDeserializer = (StdDeserializer) deserializer;
 assertEquals(Set.class, stdDeserializer.getContentClass());
 }

 @Test
 public void testFindForCollection_UnmodifiableList() throws JsonMappingException {
 DeserializationContext ctxt = new DeserializationContext(new AnnotationIntrospector(), new StandardSerializerProvider(), null);
 JavaType type = getTestType(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, List.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
 assertNotNull(deserializer);
 StdDeserializer stdDeserializer = (StdDeserializer) deserializer;
 assertEquals(List.class, stdDeserializer.getContentClass());
 }

 @Test
 public void testFindForCollection_UnmodifiableMap() throws JsonMappingException {
 DeserializationContext ctxt = new DeserializationContext(new AnnotationIntrospector(), new StandardSerializerProvider(), null);
 JavaType type = getTestType(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, Map.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
 assertNotNull(deserializer);
 StdDeserializer stdDeserializer = (StdDeserializer) deserializer;
 assertEquals(Map.class, stdDeserializer.getContentClass());
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForCollection_InvalidType() throws JsonMappingException {
 DeserializationContext ctxt = new DeserializationContext(new AnnotationIntrospector(), new StandardSerializerProvider(), null);
 JavaType type = getTestType(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, Boolean[].class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
 fail("Expected JsonMappingException");
 }
}