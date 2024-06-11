package com.fasterxml.jackson.databind.module.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;

 @Before
 public void setup() {
 resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, LinkedList.class);
 }

 @Test
 public void testSimpleMapping() {
 DeserializationConfig config = new DeserializationConfig();
 config.setAnnotationIntrospector(new SimpleModule().getDefaultIntrospector());
 ObjectMapper mapper = new ObjectMapper(config);
 mapper.setTypeResolver(resolver);

 JavaType javaType = new ClassType(ArrayList.class);
 JavaType expectedType = new ClassType(LinkedList.class);
 JavaType actualType = mapper.getTypeFactory().constructType(javaType);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testMappingWithGenericType() {
 DeserializationConfig config = new DeserializationConfig();
 config.setAnnotationIntrospector(new SimpleModule().getDefaultIntrospector());
 ObjectMapper mapper = new ObjectMapper(config);
 mapper.setTypeResolver(resolver);

 JavaType javaType = new ClassType(ArrayList.class);
 JavaType expectedType = new ClassType(LinkedHashSet.class);
 JavaType actualType = mapper.getTypeFactory().constructType(javaType);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testMappingWithNestedType() {
 DeserializationConfig config = new DeserializationConfig();
 config.setAnnotationIntrospector(new SimpleModule().getDefaultIntrospector());
 ObjectMapper mapper = new ObjectMapper(config);
 mapper.setTypeResolver(resolver);

 JavaType javaType = new ClassType(Map.class);
 JavaType keyType = new ClassType(String.class);
 JavaType valueType = new ClassType(List.class);
 JavaType expectedType = new ClassType(HashMap.class);
 JavaType actualType = mapper.getTypeFactory().constructType(javaType, keyType, valueType);
 assertEquals(expectedType, actualType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIncorrectMapping() {
 DeserializationConfig config = new DeserializationConfig();
 config.setAnnotationIntrospector(new SimpleModule().getDefaultIntrospector());
 ObjectMapper mapper = new ObjectMapper(config);
 mapper.setTypeResolver(resolver);

 JavaType javaType = new ClassType(List.class);
 JavaType expectedType = new ClassType(ArrayList.class); // Incorrect subtype
 JavaType actualType = mapper.getTypeFactory().constructType(javaType);
 fail("Expected IllegalArgumentException, but got: " + actualType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMappedTypeNotAbstract() {
 DeserializationConfig config = new DeserializationConfig();
 config.setAnnotationIntrospector(new SimpleModule().getDefaultIntrospector());
 ObjectMapper mapper = new ObjectMapper(config);
 mapper.setTypeResolver(resolver);

 JavaType javaType = new ClassType(LinkedList.class);
 // LinkedList is not abstract, so this should fail
 JavaType actualType = mapper.getTypeFactory().constructType(javaType);
 fail("Expected IllegalArgumentException, but got: " + actualType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCircularMapping() {
 DeserializationConfig config = new DeserializationConfig();
 config.setAnnotationIntrospector(new SimpleModule().getDefaultIntrospector());
 ObjectMapper mapper = new ObjectMapper(config);
 mapper.setTypeResolver(resolver);

 SimpleAbstractTypeResolver resolver2 = new SimpleAbstractTypeResolver();
 resolver2.addMapping(LinkedList.class, ArrayList.class);
 resolver2.addMapping(ArrayList.class, LinkedList.class);
 mapper.setTypeResolver(resolver2);

 JavaType javaType = new ClassType(LinkedList.class);
 JavaType actualType = mapper.getTypeFactory().constructType(javaType);
 fail("Expected IllegalArgumentException, but got: " + actualType);
 }

 @Test
 public void testResolveAbstractType() {
 DeserializationConfig config = new DeserializationConfig();
 config.setAnnotationIntrospector(new SimpleModule().getDefaultIntrospector());
 ObjectMapper mapper = new ObjectMapper(config);
 mapper.setTypeResolver(resolver);

 JavaType javaType = new ClassType(ArrayList.class); // AbstractList
 JavaType expectedType = null; // Resolver returns null for abstract types
 JavaType actualType = mapper.getTypeResolver().resolveAbstractType(config, javaType);
 assertEquals(expectedType, actualType);
 }

}