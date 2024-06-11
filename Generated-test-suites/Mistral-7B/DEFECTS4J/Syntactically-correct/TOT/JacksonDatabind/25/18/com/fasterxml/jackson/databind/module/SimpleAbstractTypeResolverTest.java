package com.fasterxml.jackson.databind.module.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for SimpleAbstractTypeResolver.
 */
public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new SimpleModule().setAbstractTypeResolver(resolver));
    }

    @Test
    public void testFindTypeMapping() {
        JavaType listType = objectMapper.getTypeFactory().constructType(new TypeReference<List<Integer>>(){}.getType());
        JavaType linkedListType = objectMapper.getTypeFactory().constructType(LinkedList.class);

        JavaType resolvedType = resolver.findTypeMapping(new DeserializationConfig(), listType);
        assertThat(resolvedType).isNotNull();
        assertThat(resolvedType.getRawClass()).isEqualTo(LinkedList.class);

        resolvedType = resolver.findTypeMapping(new DeserializationConfig(), linkedListType);
        assertThat(resolvedType).isNull();
    }

    @Test
    public void testResolveAbstractType() {
        JavaType collectionType = objectMapper.getTypeFactory().constructType(new TypeReference<Collection<String>>(){}.getType());

        JavaType resolvedType = resolver.resolveAbstractType(new DeserializationConfig(), collectionType);
        assertThat(resolvedType).isNull();
    }

    @Test
    public void testAddMappingsForInvalidTypes() {
        try {
            resolver.addMapping(String.class, Object.class);
            // This should not reach here
            fail("Exception expected for invalid type mappings.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            resolver.addMapping(Object.class, String.class);
            // This should not reach here
            fail("Exception expected for invalid type mappings.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAddMappingForSelfType() {
        try {
            resolver.addMapping(ArrayList.class, ArrayList.class);
            // This should not reach here
            fail("Exception expected for self-type mappings.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAddMappingForInvalidSubType() {
        try {
            resolver.addMapping(ArrayList.class, Collection.class);
            // This should not reach here
            fail("Exception expected for invalid subtype mappings.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}