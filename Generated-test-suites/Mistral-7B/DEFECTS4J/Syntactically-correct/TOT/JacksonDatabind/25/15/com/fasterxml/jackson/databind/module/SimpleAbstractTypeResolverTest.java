package com.fasterxml.jackson.databind.module.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for SimpleAbstractTypeResolver
 */
public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;

 @Before
 public void setup() {
 resolver = new SimpleAbstractTypeResolver();
 // Add mappings for Collection and List to LinkedList
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, LinkedList.class);
 }

 /**
 * Test for findTypeMapping method with Collection as type
 */
 @Test
 public void testFindTypeMappingCollection() throws Exception {
 DeserializationConfig config = new DeserializationConfig();
 config.addHandler(new SimpleAbstractTypeResolver(resolver));
 ObjectMapper mapper = new ObjectMapper(config);
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<List<String>>() {});
 JavaType mappedType = resolver.findTypeMapping(config, javaType);
 assertNotNull(mappedType);
 assertEquals(LinkedList.class, mappedType.getRawClass());
 }

 /**
 * Test for findTypeMapping method with abstract List as type
 */
 @Test
 public void testFindTypeMappingAbstractList() throws Exception {
 DeserializationConfig config = new DeserializationConfig();
 config.addHandler(new SimpleAbstractTypeResolver(resolver));
 Class<?> abstractListClass = AbstractList.class;
 JavaType javaType = mapper.getTypeFactory().constructType(abstractListClass.getGenericSuperclass());
 JavaType mappedType = resolver.findTypeMapping(config, javaType);
 assertNotNull(mappedType);
 assertEquals(LinkedList.class, mappedType.getRawClass());
 }

 /**
 * Test for findTypeMapping method with non-abstract class as type
 */
 @Test(expected = IllegalArgumentException.class)
 public void testFindTypeMappingNonAbstractType() throws Exception {
 DeserializationConfig config = new DeserializationConfig();
 config.addHandler(new SimpleAbstractTypeResolver(resolver));
 JavaType javaType = mapper.getTypeFactory().constructType(ArrayList.class);
 resolver.findTypeMapping(config, javaType);
 // The test should fail with IllegalArgumentException
 }

 /**
 * Test for findTypeMapping method with null type
 */
 @Test(expected = NullPointerException.class)
 public void testFindTypeMappingNullType() throws Exception {
 DeserializationConfig config = new DeserializationConfig();
 config.addHandler(new SimpleAbstractTypeResolver(resolver));
 JavaType javaType = null;
 resolver.findTypeMapping(config, javaType);
 // The test should fail with NullPointerException
 }

 /**
 * Test for resolveAbstractType method
 */
 @Test
 public void testResolveAbstractType() throws Exception {
 DeserializationConfig config = new DeserializationConfig();
 config.addHandler(new SimpleAbstractTypeResolver(resolver));
 JavaType javaType = mapper.getTypeFactory().constructType(List.class);
 JavaType resolvedType = resolver.resolveAbstractType(config, javaType);
 assertNull(resolvedType);
 }
}