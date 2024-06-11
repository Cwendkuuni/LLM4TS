package com.fasterxml.jackson.databind.module.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
 ObjectMapper mapper = new ObjectMapper(config);

 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<List<Integer>>(){}.getType());
 JavaType mappedType = resolver.findTypeMapping(config, javaType);
 assertEquals(LinkedList.class, mappedType.getRawClass());
 }

 @Test
 public void testFindTypeMapping_fail() {
 DeserializationConfig config = new DeserializationConfig();
 config.setTypeResolver(resolver);
 ObjectMapper mapper = new ObjectMapper(config);

 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<String>(){}.getType());
 JavaType mappedType = resolver.findTypeMapping(config, javaType);
 assertNull(mappedType);
 }

 @Test
 public void testResolveAbstractType() {
 DeserializationConfig config = new DeserializationConfig();
 config.setTypeResolver(resolver);
 ObjectMapper mapper = new ObjectMapper(config);

 JavaType javaType = mapper.getTypeFactory().constructType(Collection.class);
 JavaType mappedType = resolver.resolveAbstractType(config, javaType);
 assertNull(mappedType);
 }
}