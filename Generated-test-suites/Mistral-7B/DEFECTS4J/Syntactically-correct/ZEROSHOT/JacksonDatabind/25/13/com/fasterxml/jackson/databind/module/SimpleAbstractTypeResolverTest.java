package com.fasterxml.jackson.databind.module.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ClassKey;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;

 @Before
 public void setUp() {
 resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, LinkedList.class);
 }

 @Test
 public void testAddMapping() {
 Class<?> superType = Collection.class;
 Class<? extends Collection> subType = LinkedList.class;
 resolver.addMapping(superType, subType);
 assertEquals(subType, resolver._mappings.get(new ClassKey(superType)));

 superType = List.class;
 subType = LinkedList.class;
 resolver.addMapping(superType, subType);
 assertEquals(subType, resolver._mappings.get(new ClassKey(superType)));
 }

 @Test
 public void testFindTypeMapping() {
 DeserializationConfig config = new DeserializationConfig();
 config.addHandler(new SimpleModule().addResolver(resolver));
 ObjectMapper mapper = new ObjectMapper(config);

 JavaType listType = mapper.getTypeFactory().constructType(List.class);
 JavaType expected = mapper.getTypeFactory().constructType(LinkedList.class);
 JavaType actual = resolver.findTypeMapping(config, listType);
 assertEquals(expected, actual);

 assertNull(resolver.findTypeMapping(config, String.class));
 }

 @Test
 public void testResolveAbstractType() {
 DeserializationConfig config = new DeserializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);
 JavaType javaType = mapper.getTypeFactory().constructType(List.class);
 assertNull(resolver.resolveAbstractType(config, javaType));
 }
}