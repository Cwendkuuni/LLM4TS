package com.fasterxml.jackson.databind.module.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

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
 ObjectMapper mapper = new ObjectMapper();
 mapper.setTypeResolver(resolver);

 // Test with Collection
 JavaType type1 = mapper.getTypeFactory().constructType(ArrayList.class);
 JavaType resolvedType1 = resolver.findTypeMapping(mapper.getConfig(), type1);
 assertEquals(LinkedList.class, resolvedType1.getRawClass());

 // Test with List
 JavaType type2 = mapper.getTypeFactory().constructType(List.class);
 JavaType resolvedType2 = resolver.findTypeMapping(mapper.getConfig(), type2);
 assertEquals(LinkedList.class, resolvedType2.getRawClass());

 // Test with invalid types
 JavaType type3 = mapper.getTypeFactory().constructType(String.class);
 assertNull(resolver.findTypeMapping(mapper.getConfig(), type3));

 JavaType type4 = mapper.getTypeFactory().constructType(SimpleAbstractTypeResolver.class);
 assertNull(resolver.findTypeMapping(mapper.getConfig(), type4));
 }

 @Test(expected = JsonMappingException.class)
 public void testFindTypeMappingWithInvalidMappings() {
 ObjectMapper mapper = new ObjectMapper();
 mapper.setTypeResolver(resolver);

 SimpleAbstractTypeResolver wrongResolver = new SimpleAbstractTypeResolver();
 wrongResolver.addMapping(List.class, Integer.class);
 JavaType type = mapper.getTypeFactory().constructType(List.class);
 wrongResolver.findTypeMapping(mapper.getConfig(), type);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithInvalidArguments() {
 SimpleAbstractTypeResolver wrongResolver = new SimpleAbstractTypeResolver();
 wrongResolver.addMapping(LinkedList.class, Collection.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithSelfReference() {
 SimpleAbstractTypeResolver wrongResolver = new SimpleAbstractTypeResolver();
 wrongResolver.addMapping(List.class, List.class);
 }
}