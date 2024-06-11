package com.fasterxml.jackson.databind.module.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleAbstractTypeResolverTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static SimpleAbstractTypeResolver createResolver() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, ArrayList.class);
        resolver.addMapping(Map.class, HashMap.class);
        return resolver;
    }

    @Test
    public void testFindTypeMapping() throws Exception {
        SimpleAbstractTypeResolver resolver = createResolver();
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = TypeFactory.defaultInstance().constructType(new TypeReference<List<Map<String, String>>>(){}.getType());
        JavaType result = resolver.findTypeMapping(config, type);
        Assert.assertNotNull(result);
        Assert.assertEquals(new TypeReference<List<Map<String, String>>>() {}.getType(), result.getType());
    }

    @Test
    public void testFindTypeMappingWhenNoMapping() throws Exception {
        SimpleAbstractTypeResolver resolver = createResolver();
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType result = resolver.findTypeMapping(config, type);
        Assert.assertNull(result);
    }

    @Test
    public void testFindTypeMappingWithCustomConfig() throws Exception {
        SimpleAbstractTypeResolver resolver = createResolver();
        DeserializationConfig config = new DeserializationConfig();
        config.addHandler(new SimpleAbstractTypeResolver());
        JavaType type = TypeFactory.defaultInstance().constructType(new TypeReference<List<Map<String, String>>>(){}.getType());
        JavaType result = resolver.findTypeMapping(config, type);
        Assert.assertNull(result);
    }

    @Test
    public void testResolveAbstractType() throws Exception {
        SimpleAbstractTypeResolver resolver = createResolver();
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = TypeFactory.defaultInstance().constructType(List.class);
        JavaType result = resolver.resolveAbstractType(config, type);
        Assert.assertNull(result);
    }

    @Test
    public void testResolveAbstractTypeWhenMappingExists() throws Exception {
        SimpleAbstractTypeResolver resolver = createResolver();
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = TypeFactory.defaultInstance().constructType(List.class);
        JavaType result = resolver.resolveAbstractType(config, type);
        Assert.assertEquals(TypeFactory.defaultInstance().constructType(ArrayList.class), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithInvalidParameters() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(String.class, Integer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithMismatchedTypes() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithSelfAssignment() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, List.class);
    }
}