package com.fasterxml.jackson.databind.module.test;

import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link SimpleAbstractTypeResolver}.
 */
public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        resolver = new SimpleAbstractTypeResolver();
        // Let's test if adding mappings works correctly
        resolver.addMapping(ArrayList.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        objectMapper = new ObjectMapper();
        objectMapper.setTypeResolver(resolver);
    }

    /**
     * Test for {@link SimpleAbstractTypeResolver#addMapping(Class, Class)}.
     */
    @Test
    public void testAddMapping() {
        // Given
        Class<?> collectionType = ArrayList.class;
        Class<? extends Collection<?>> subType = LinkedList.class;

        // When
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Then
        // Nothing should be added to the map initially
        Map<ClassKey, Class<?>> mappings = resolver._mappings;
        assertEquals(0, mappings.size());

        // Add a mapping
        SimpleAbstractTypeResolver resolverWithMapping = resolver.addMapping(collectionType, subType);

        // Verify that the mapping was added
        assertEquals(1, resolverWithMapping._mappings.size());
        ClassKey key = new ClassKey(collectionType);
        assertEquals(subType, resolverWithMapping._mappings.get(key));

        // Verify that the mapping is still present after creating a new instance
        SimpleAbstractTypeResolver newInstance = new SimpleAbstractTypeResolver();
        newInstance.addMapping(collectionType, subType);
        assertEquals(1, newInstance._mappings.size());
        assertEquals(subType, newInstance._mappings.get(key));
    }

    /**
     * Test for {@link SimpleAbstractTypeResolver#findTypeMapping(DeserializationConfig, JavaType)}.
     */
    @Test
    public void testFindTypeMapping() {
        // Given
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class);

        // When
        JavaType mappedType = resolver.findTypeMapping(new DeserializationConfig(), javaType);

        // Then
        // No mapping should be found initially
        assertEquals(null, mappedType);

        // Add a mapping
        resolver.addMapping(ArrayList.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        // Verify that the mapping is found
        mappedType = resolver.findTypeMapping(new DeserializationConfig(), javaType);
        assertEquals(LinkedList.class, mappedType.getRawClass());

        // Verify that no mapping is found for a non-abstract type
        JavaType nonAbstractType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        mappedType = resolver.findTypeMapping(new DeserializationConfig(), nonAbstractType);
        assertEquals(null, mappedType);
    }

    /**
     * Test for {@link SimpleAbstractTypeResolver#resolveAbstractType(DeserializationConfig, JavaType)}.
     */
    @Test
    public void testResolveAbstractType() {
        // Given
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class);

        // When
        JavaType resolvedType = resolver.resolveAbstractType(new DeserializationConfig(), javaType);

        // Then
        // Nothing should be resolved initially
        assertEquals(null, resolvedType);

        // Add a mapping
        resolver.addMapping(ArrayList.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        // Verify that nothing is resolved for a non-abstract type
        JavaType nonAbstractType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        resolvedType = resolver.resolveAbstractType(new DeserializationConfig(), nonAbstractType);
        assertEquals(null, resolvedType);

        // Verify that an abstract type is resolved correctly
        JavaType abstractType = TypeFactory.defaultInstance().constructType(List.class);
        resolvedType = resolver.resolveAbstractType(new DeserializationConfig(), abstractType);
        assertEquals(null, resolvedType);

        // Verify that a mapped abstract type is resolved correctly
        JavaType mappedAbstractType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        resolver.addMapping(ArrayList.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        resolvedType = resolver.resolveAbstractType(new DeserializationConfig(), mappedAbstractType);
        assertEquals(LinkedList.class, resolvedType);
    }
}