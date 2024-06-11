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

public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;
    private DeserializationConfig config;

    @Before
    public void setup() {
        resolver = new SimpleAbstractTypeResolver();
        config = new DeserializationConfig();
    }

    @Test
    public void testAddMapping() {
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        JavaType type = new TypeReference<List<String>>() {}.getType();
        JavaType resolvedType = resolver.findTypeMapping(config, type);
        assertEquals(LinkedList.class, resolvedType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingInvalidArguments() {
        resolver.addMapping(String.class, Integer.class);
    }

    @Test
    public void testFindTypeMappingWithNoMapping() {
        JavaType type = new TypeReference<Map<String, Integer>>() {}.getType();
        JavaType resolvedType = resolver.findTypeMapping(config, type);
        assertEquals(null, resolvedType);
    }

    @Test
    public void testFindTypeMappingWithMultipleMappings() {
        resolver.addMapping(Collection.class, ArrayList.class);
        resolver.addMapping(Collection.class, LinkedList.class);

        JavaType type = new TypeReference<List<String>>() {}.getType();
        JavaType resolvedType = resolver.findTypeMapping(config, type);
        assertEquals(LinkedList.class, resolvedType.getRawClass());

        type = new TypeReference<List<Integer>>() {}.getType();
        resolvedType = resolver.findTypeMapping(config, type);
        assertEquals(ArrayList.class, resolvedType.getRawClass());
    }

    @Test
    public void testResolveAbstractType() {
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        JavaType type = new TypeReference<List<String>>() {}.getType();
        JavaType resolvedType = resolver.resolveAbstractType(config, type);
        assertEquals(null, resolvedType);
    }
}