package com.fasterxml.jackson.databind.module.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
 DeserializationConfig config = new DeserializationConfig();
 config.setTypeResolver(resolver);

 JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<List<Integer>>() {}.getType());
 JavaType resolvedType = config.getTypeFactory().findTypeByName("com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver$LinkedList", javaType.getRawClass());

 assertEquals("LinkedList", resolvedType.getRawClass().getName());
 }

 @Test
 public void testFindTypeMappingWithAbstractType() {
 DeserializationConfig config = new DeserializationConfig();
 config.setTypeResolver(resolver);

 JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class);
 JavaType resolvedType = config.getTypeFactory().findTypeByName("com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver$LinkedList", javaType.getRawClass());

 assertNull("Should not be able to resolve concrete List implementation for abstract List type", resolvedType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithSameType() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(ArrayList.class, ArrayList.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithIncorrectSubType() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(ArrayList.class, HashMap.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithNonAbstractSuperType() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Integer.class, LinkedList.class);
 }
}