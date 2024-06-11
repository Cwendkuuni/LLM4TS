package com.fasterxml.jackson.databind.module.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

/**
 * Tests for {@link SimpleAbstractTypeResolver} class.
 */
public class SimpleAbstractTypeResolverTest {

    @Test
    public void testSimpleAbstractTypeResolver() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Test adding mappings
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        JavaType collectionType = new TypeReference<List<Integer>>() {}.getType();
        JavaType resolvedType = resolver.findTypeMapping(new DeserializationConfig(), collectionType);
        assertEquals(LinkedList.class, resolvedType.getRawClass());

        // Test with empty mapping
        SimpleAbstractTypeResolver emptyResolver = new SimpleAbstractTypeResolver();
        JavaType emptyType = new TypeReference<Object>() {}.getType();
        JavaType emptyResolvedType = emptyResolver.findTypeMapping(new DeserializationConfig(), emptyType);
        assertNull(emptyResolvedType);

        // Test with invalid mapping
        SimpleAbstractTypeResolver invalidResolver = new SimpleAbstractTypeResolver();
        JavaType invalidType = new TypeReference<String>() {}.getType();
        try {
            invalidResolver.addMapping(String.class, Integer.class);
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test with circular mapping
        SimpleAbstractTypeResolver circularResolver = new SimpleAbstractTypeResolver();
        JavaType circularSuperType = Collection.class;
        JavaType circularSubType = LinkedList.class;
        circularResolver.addMapping(circularSuperType, circularSubType);
        circularResolver.addMapping(circularSubType, circularSuperType);
        JavaType circularType = new TypeReference<List<Integer>>() {}.getType();
        try {
            circularResolver.findTypeMapping(new DeserializationConfig(), circularType);
            fail("Should not reach here");
        } catch (IllegalStateException e) {
            // expected
        }
    }
}