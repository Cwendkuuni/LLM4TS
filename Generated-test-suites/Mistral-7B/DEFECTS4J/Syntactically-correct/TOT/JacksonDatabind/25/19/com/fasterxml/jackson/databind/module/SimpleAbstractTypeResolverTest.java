package com.fasterxml.jackson.databind.module.test;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.type.ClassKey;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;

 @Before
 public void setup() {
 resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, LinkedList.class);
 }

 @Test
 public void testFindTypeMapping() {
 DeseralizationConfig config = new DeserializationConfig();
 JavaType javaType = JavaType.construct(Collection.class, String.class);
 JavaType expectedType = JavaType.construct(LinkedList.class, String.class);
 JavaType actualType = resolver.findTypeMapping(config, javaType);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testFindTypeMappingWithAbstractClass() {
 DeseralizationConfig config = new DeserializationConfig();
 JavaType javaType = JavaType.construct(AbstractList.class);
 JavaType expectedType = JavaType.construct(LinkedList.class);
 JavaType actualType = resolver.findTypeMapping(config, javaType);
 assertEquals(expectedType, actualType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindTypeMappingWithInvalidMapping() {
 DeseralizationConfig config = new DeserializationConfig();
 JavaType javaType = JavaType.construct(Collection.class, String.class);
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(List.class, Collection.class); // this is invalid
 JavaType actualType = resolver.findTypeMapping(config, javaType);
 }

 @Test
 public void testResolveAbstractType() {
 DeseralizationConfig config = new DeserializationConfig();
 JavaType javaType = JavaType.construct(AbstractList.class);
 JavaType expectedType = null;
 JavaType actualType = resolver.resolveAbstractType(config, javaType);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testResolutionWithNestedMappings() {
 DeseralizationConfig config = new DeserializationConfig();
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(LinkedList.class, ArrayList.class);
 JavaType javaType = JavaType.construct(Collection.class, Number[].class);
 JavaType expectedType = JavaType.construct(ArrayList.class, Number[].class);
 JavaType actualType = resolver.findTypeMapping(config, javaType);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testResolutionWithComplexTypes() {
 DeseralizationConfig config = new DeserializationConfig();
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Map.class, HashMap.class);
 resolver.addMapping(HashMap.class, TreeMap.class);
 JavaType javaType = JavaType.construct(Map.class, Integer.class, String.class);
 JavaType expectedType = JavaType.construct(TreeMap.class, Integer.class, String.class);
 JavaType actualType = resolver.findTypeMapping(config, javaType);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testResolutionWithMultipleMappings() {
 DeseralizationConfig config = new DeserializationConfig();
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, ArrayList.class);
 JavaType javaType = JavaType.construct(List.class, String.class);
 JavaType expectedType = JavaType.construct(ArrayList.class, String.class);
 JavaType actualType = resolver.findTypeMapping(config, javaType);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testResolutionWithEmptyMapping() {
 DeseralizationConfig config = new DeserializationConfig();
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 JavaType javaType = JavaType.construct(Collection.class, String.class);
 JavaType actualType = resolver.findTypeMapping(config, javaType);
 assertEquals(null, actualType);
 }
}